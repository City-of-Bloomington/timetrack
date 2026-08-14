package in.bloomington.timer;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.*;
import java.sql.*;
import java.text.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HandleTempNotification{

    boolean debug = false;
    static final long serialVersionUID = 53L;
    static Logger logger = LogManager.getLogger(HandleTempNotification.class);
    static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    static DecimalFormat df = new DecimalFormat("#0.00");
    static int temp_max_days = 275; // 9 months
    static int seasonal_max_days = 183; // 6 months
    //
    boolean activeMail = false;
    String mail_host = "";
    String date = "";
    //
    // each supervisor with list of employees
    Hashtable<String, List<List<String>>> supervisorEmps = new Hashtable<>();
    //
    // accrual values from New World (Carry Over)
    //
    public HandleTempNotification(){
    }
    public HandleTempNotification(String val, boolean val2){
	setMailHost(val);
	setActiveMail(val2);
    }

    //
    // setters
    //
    public void setMailHost(String val){
	if(val != null){		
	    mail_host = val;
	}
    }
    public void setActiveMail(boolean val){
	activeMail = val;
    }
    public void setActiveMail(){ // for testing
	activeMail = true;
    }
    /**
	select distinct (j.id),
	concat_ws(':',e2.email,	
	concat_ws(' ',e2.first_name,e2.last_name)) as approver,
	concat_ws(' ',e.first_name,e.last_name) as full_name,
	    p.name as job_title, 
	    g.name as group_name,
	    DATEDIFF(now(), j.effective_date) as days_since_start 
	    from jobs j 
	    join salary_groups sg on sg.id=j.salary_group_id 
	    join positions p on j.position_id=p.id 
	     join employees e on j.employee_id=e.id 
	     join `groups` g on g.id=j.group_id 
	     join departments d on g.department_id=d.id
	     left join group_managers mg on mg.group_id=g.id
	     and mg.employee_id = (select gm.employee_id from group_managers gm join workflow_nodes wn on wn.id=gm.wf_node_id
	     join employees e3 on gm.employee_id=e3.id and e3.inactive is null 
	     where gm.group_id = g.id and gm.expire_date is null and gm.wf_node_id=3 and gm.inactive is null order by gm.primary_flag desc limit 1)
	     left join employees e2 on mg.employee_id=e2.id 
	     where j.salary_group_id = 3 and
	     j.inactive is null and 
	     j.expire_date is null and
	     DATEDIFF(now(), j.effective_date) > 275 and
	     e.inactive is null 

     */
    //
    public String process(){
	String back = "";
	if(activeMail){
	    back = findEmployees();
	    if(supervisorEmps.size() > 0){
		back = composeAndSend();
	    }
	}
	else{
	    back = "activeMail is "+activeMail;
	}
	return back;
	    
    }    
    private String composeAndSend(){
	String back = "";
	Set<String> supervisors = supervisorEmps.keySet();
	for(String one:supervisors){
	    List<List<String>> ll = supervisorEmps.get(one);
	    String email = "", name = ""; // supervisor
	    try{
		String[] strArr = one.split(":");
		if(strArr != null){
		    email = strArr[0];
		    name = strArr[1];
		}
	    }catch(Exception ex){
		System.err.println(ex);
	    }
	    if(!email.isEmpty()){
		String msg = composeText(name, ll);
		System.err.println(" super "+name+" "+email);
		System.err.println(" msg "+msg);
		back = doSend(email, msg);
	    }
	}
	return back;
    }
    String composeText(String supervisor, List<List<String>> emps){
	String body_text ="This is an automated message from the TimeTrack timekeeping system.\n\n"+
	    "The following employee(s) have exceeded maximum employment limit for the employment categories list below. \n\n"+
	    "Employee Name, Job Title, Group, Employment Type \n\n";
	for(List<String> ll:emps){
	    body_text += ll.get(1)+", "+ll.get(2)+", "+ll.get(3)+", "+ll.get(0)+"\n\n";
	}
	body_text += "\n Thank You \n\n";
	return body_text;
    }    
    private String findEmployees(){
		
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="";
	String qq = "select distinct (j.id),"+
	    "concat_ws(':',e2.email,concat_ws(' ',e2.first_name,e2.last_name)) as approver,"+
	    "concat_ws(' ',e.first_name,e.last_name) as full_name,"+	    
	    "p.name as job_title,"+
	    "g.name as group_name,"+
	    "DATEDIFF(now(), j.effective_date) as days_since_start "+
	    "from jobs j "+
	    "join salary_groups sg on sg.id=j.salary_group_id "+
	    "join positions p on j.position_id=p.id "+
	    "join employees e on j.employee_id=e.id "+
	    "join `groups` g on g.id=j.group_id "+
	    "join departments d on g.department_id=d.id "+
	    "left join group_managers mg on mg.group_id=g.id "+
	    "and mg.employee_id = (select gm.employee_id from group_managers gm join workflow_nodes wn on wn.id=gm.wf_node_id "+
	    "join employees e3 on gm.employee_id=e3.id and e3.inactive is null "+
	    "where gm.group_id = g.id and gm.expire_date is null and gm.wf_node_id=3 and gm.inactive is null order by gm.primary_flag desc limit 1) "+
	    "left join employees e2 on mg.employee_id=e2.id "+
	    "where j.salary_group_id = ? and "+ // temp 3, seasonal  13
	    "j.inactive is null and  "+
	    "j.expire_date is null and "+
	    "DATEDIFF(now(), j.effective_date) > ? and "+
	    "e.inactive is null"; 

	con = UnoConnect.getConnection();
	if(con == null){
	    msg = "Could not connect to DB";
	    return msg;
	}
	try{
	    logger.debug(qq);
	    pstmt = con.prepareStatement(qq);
	    pstmt.setInt(1,3); // salary group
	    pstmt.setInt(2, temp_max_days);
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		List<String> one = new ArrayList<>();
		one.add("Temp");
		String supervisor = rs.getString(2); // supervisor
		for(int j=3;j<6;j++){
		    one.add(rs.getString(j));
		}
		if(supervisorEmps.containsKey(supervisor)){
		    List<List<String>> emps = supervisorEmps.get(supervisor);
		    emps.add(one);
		}
		else{
		    List<List<String>> emps = new ArrayList<>();
		    emps.add(one);
		    supervisorEmps.put(supervisor,emps); 
		}
	    }
	    pstmt.setInt(1,13);
	    pstmt.setInt(2, seasonal_max_days);
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		List<String> one = new ArrayList<>();
		one.add("Seasonal");
		String supervisor = rs.getString(2); // supervisor
		for(int j=2;j<6;j++){
		    one.add(rs.getString(j));
		}
		if(supervisorEmps.containsKey(supervisor)){
		    List<List<String>> emps = supervisorEmps.get(supervisor);
		    emps.add(one);
		}
		else{
		    List<List<String>> emps = new ArrayList<>();
		    emps.add(one);
		    supervisorEmps.put(supervisor,emps); 
		}
	    }	    
	}
	catch (Exception ex) {
	    logger.error(ex+":"+qq);
	    msg += ex;
	}
	finally{
	    Helper.databaseDisconnect(pstmt, rs);
	    UnoConnect.databaseDisconnect(con);
	}
	return msg;
    }
    String doSend(String to_str, String msg_txt){
	String msg = "";
	Properties props = new Properties();
	props.put("mail.smtp.host", mail_host);
				
	Session session = Session.getDefaultInstance(props, null);
	try{
	    Message message = new MimeMessage(session);					    message.setSubject("Employment Limit Exceeded");
	    message.setText(msg_txt);
	    message.setFrom(new InternetAddress(CommonInc.fromEmailStr));
	    InternetAddress[] addrArray = InternetAddress.parse(to_str);
	    message.setRecipients(Message.RecipientType.TO, addrArray);
	    // message.setRecipients(Message.RecipientType.BCC, addrArray);
	    Transport.send(message);
	}
	catch (MessagingException mex){
	    //
	    // Failure
	    msg += mex;
	    //
	    logger.error(mex);
	    Exception ex = mex;
	    do {
		if (ex instanceof SendFailedException) {
		    SendFailedException sfex = (SendFailedException)ex;
		    javax.mail.Address [] invalid = sfex.getInvalidAddresses();
		    if (invalid != null) {
			logger.error("    ** Invalid Addresses");
			if (invalid != null) {
			    for (int i = 0; i < invalid.length; i++) 
				logger.error("         " + invalid[i]);
			}
		    }
		    javax.mail.Address [] validUnsent = sfex.getValidUnsentAddresses();
		    if (validUnsent != null) {
			logger.error("    ** ValidUnsent Addresses");
			if (validUnsent != null) {
			    for (int i = 0; i < validUnsent.length; i++) 
				logger.error("         "+validUnsent[i]);
			}
		    }
		    javax.mail.Address [] validSent = sfex.getValidSentAddresses();
		    if (validSent != null) {
			logger.error("    ** ValidSent Addresses");
			if (validSent != null) {
			    for (int i = 0; i < validSent.length; i++) 
				logger.error("         "+validSent[i]);
			}
		    }
		}
		if (ex instanceof MessagingException)
		    ex = ((MessagingException)ex).getNextException();
		else { // any other exception
		    logger.error(ex);
		    ex = null;
		}
	    } while (ex != null);
	} catch (Exception ex){
	    logger.error(ex);
	}						
	return msg;
    }
}
