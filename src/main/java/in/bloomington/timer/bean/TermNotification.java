package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.sql.*;
import java.util.*;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TermNotification{

    static final long serialVersionUID = 3700L;	
    static Logger logger = LogManager.getLogger(TermNotification.class);
    String id="";
    String sender_id = "", termination_id = "", send_time="",
	recipient_emails = "", email_text="", send_error="";
    Employee sender = null;
    Employee employee = null;
    Document valid_document = null;
    Map<String, List<String>> accruals = null;
    EmpTerminate term = null;
    public TermNotification(){

    }
    public TermNotification(String val){
	//
	setId(val);
    }		
    public TermNotification(String val,
			    String val2,
			    String val3,
			    String val4,
			    String val5,
			    String val6,
			    String val7){
	//
	// initialize
	//
	setId(val);
	setSendTime(val2);
	setSender_id(val3);
	setTermination_id(val4);
	setRecipientEmails(val5);
	setEmailText(val6);
	setSendError(val7);	
    }    
    public boolean equals(Object obj){
	if(obj instanceof TermNotification){
	    TermNotification one =(TermNotification)obj;
	    return id.equals(one.getId());
	}
	return false;				
    }
    public int hashCode(){
	int seed = 29;
	if(!id.equals("")){
	    try{
		seed += Integer.parseInt(id);
	    }catch(Exception ex){
	    }
	}
	return seed;
    }    
    public String getId(){
	return id;
    }
    public String getSendTime(){
	return send_time;
    }
    public String getSender_id(){
	return sender_id;
    }
    public String getTermination_id(){
	return termination_id;
    }    
    public String getEmailText(){
	return email_text;
    }
    public String getRecipientEmails(){
	return recipient_emails;
    }
    public String getSendError(){
	return send_error;
    }
    public EmpTerminate getTerm(){
	if(term == null && !termination_id.isEmpty()){
	    EmpTerminate one = new EmpTerminate(termination_id);
	    String back = one.doSelect();
	    if(back.isEmpty()){
		term = one;
	    }
	}
	return term;
    }
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setSender_id(String val){
	if(val != null)
	    sender_id = val;
    }
    public void setSendTime(String val){
	if(val != null)
	    send_time = val;
    }    
    public void setTermination_id(String val){
	if(val != null)
	    termination_id = val;
    }    
    public void setRecipientEmails(String val){
	if(val != null)
	    recipient_emails = val;
    }
    public void setEmailText(String val){
	if(val != null)
	    email_text = val;
    }
    public void setSendError(String val){
	if(val != null)
	    send_error = val;
    }
    String findRecipients(){
	String back = "";
	TermRecipientList trl = new TermRecipientList();
	back = trl.find();
	if(back.isEmpty()){
	    List<TermRecipient> ones = trl.getRecipients();
	    if(ones != null && ones.size() > 0){
		for(TermRecipient one:ones){
		    if(!recipient_emails.isEmpty())
			recipient_emails +=",";
		    recipient_emails += one.getEmail();
		}
	    }
	}
	return back;
    }

    String composeEmailText(){
	String back = "", text="";
	if(term == null){
	    getTerm();
	}
	if(term != null){
	    employee = term.getEmployee();
	    
	    text = "Employee Job(s) Termination \n\n";
	    text += "Employment Type: "+term.getEmployment_type()+"\n";
	    String emp_type = term.getEmployment_type().toLowerCase();
	    if(!emp_type.equals("temp") && emp_type.indexOf("part") > -1){
		text += "Regular Full-Time: Yes";
	    }
	    if(emp_type.indexOf("part") > -1){
		text += "Part-Time Hours Per week: "+term.getHours_per_week();
	    }
	    text += "Employee: "+term.getFull_name()+"\n";
	    if(employee != null){
		text += "New World Employee #: "+employee.getEmployee_number()+"\n";
		text += "Bagde ID: "+employee.getId_code()+"\n";
	    }
	    if(term.hasDateOfBirth()){
		text += "Date of Birth: "+term.getDate_of_birth();
	    }
	    if(term.hasDateOfHire()){
		text += "Date of Hire: "+term.getDate_of_hire();
	    }
	    text += "Last Day of Work: "+term.getLast_day_of_work();
	    text += "Last Payroll Period: "+term.getLast_pay_period_date();
	    text += "Department "+term.getDepartment()+"\n";
	    text += "Terminated job title(s): "+term.getJobTitles()+"\n";
	    if(term.hasJobGrade()){
		text += "Job Grade: "+term.getJob_grade()+"\n";
	    }
	    if(term.hasJobStep()){
		text += "Job Step: "+term.getJob_step()+"\n";
	    }
	    text += "Supervisor: "+term.getSupervisor()+"\n";
	    text += "Supervisor Phone Number: "+term.getSupervisor_phone()+"\n";
	    if(term.hasEmpAddress()){
		text += "Address: "+term.getEmp_address()+"\n";
		text += "City, State Zipcode: "+term.getEmpCityStateZip()+"\n";
	    }
	    if(term.hasPhones()){
		text += "Phones(s): "+term.getPhones()+"\n";
	    }
	    if(term.hasPersonalEmail()){
		text += "Personal Email: "+term.getPersonal_email();
	    }
	    if(term.hasEmail()){
		text += "Email Address: "+term.getEmail();
		text += "Email Account Action: "+term.getEmail_account_action();
		if(term.getEmail_account_action().indexOf("Foward") > -1){
		    text += "Forward Email to: "+term.getForward_emails();
		    text += "Forward for # Days: "+term.getForward_days_cnt();
		}
	    }
	    String dr_act = term.getDrive_action().toLowerCase();
	    if(dr_act.indexOf("Person") > -1){
		text += "Google/H Drive Address(es): "+term.getDrive_to_person_email()+"\n";
	    }
	    else if(dr_act.indexOf("shared") > -1){
		text += "Google/H Drive Address: "+term.getDrive_to_shared_emails()+"\n";
	    }
	    text += "Google Calendar Action: "+term.getCalendar_action()+"\n ";
	    String cal_act = term.getCalendar_action().toLowerCase();
	    if(cal_act.indexOf("transfer") > -1){
		text += "Calendar Email Address: "+term.getCalendar_to_email()+"\n";
	    }
	    text += "Zoom Account Action: "+term.getZoom_action()+"\n";
	    String zom_act = term.getZoom_action().toLowerCase();
	    if(zom_act.indexOf("transfer") > -1){
		text += "Zoom Account Email Address: "+term.getZoom_to_email()+"\n";
	    }
	    text += "Badge Return Status: "+term.getBadge_returned();
	    text += "Number of Hours Worked in the Current Pay Period: "+term.getPay_period_worked_hrs()+"\n";
	    if(term.getVac_time() > 0){
		text += "Vacation Time: "+term.getVac_time()+"\n";
	    }
	    if(term.getComp_time() > 0){
		text += "Comp Time: "+term.getComp_time()+"\n";
	    }
	    if(term.getPto() > 0){
		text += "PTO: "+term.getPto();
	    }
	    if(!term.getComments().isEmpty()){
		text += "Comments: "+term.getComments()+"\n\n";
	    }
	    text += "Thank You \n";
	    text += "Submitted by "+term.getSubmitted_by()+"\n\n";
	    email_text = text;
	}
	return back;
    }
    public String doSelect(){
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "select id,date_format(send_time,'%m/%d/%Y %H:%i'), "+
	    "sender_id, termination_id, recipient_emails, email_text, send_error from term_notifications where id=?";
	con = UnoConnect.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    return back;
	}
	try{
	    logger.debug(qq);
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,id);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		setSendTime(rs.getString(2));
		setSender_id(rs.getString(3));
		setTermination_id(rs.getString(4));
		setRecipientEmails(rs.getString(5));
		setEmailText(rs.getString(6));
		setSendError(rs.getString(7));
	    }
	    else{
		back ="Record "+id+" Not found";
	    }
	}
	catch(Exception ex){
	    back += ex+":"+qq;
	    logger.error(back);
	}
	finally{
	    Helper.databaseDisconnect(pstmt, rs);
	    UnoConnect.databaseDisconnect(con);
	}
	return back;
    }
    public String doSave(){
	Connection con = null;
	PreparedStatement pstmt = null, pstmt2=null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = " insert into term_notification values(0,now(),?,?,?,?,?)";
	try{
	    con = UnoConnect.getConnection();
	    if(con == null){
		msg = "Could not connect to DB ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    int jj=1;
	    pstmt.setString(jj++, sender_id);
	    pstmt.setString(jj++, termination_id);
	    pstmt.setString(jj++, recipient_emails);
	    pstmt.setString(jj++, email_text);
	    if(send_error.isEmpty()){
		pstmt.setNull(jj++, Types.VARCHAR);
	    }
	    else
		pstmt.setString(jj++, send_error);
	    pstmt.executeUpdate();
	    qq = "select LAST_INSERT_ID()";
	    pstmt2 = con.prepareStatement(qq);
	    rs = pstmt2.executeQuery();
	    if(rs.next()){
		id = rs.getString(1);
	    }
	}
	catch(Exception ex){
	    msg += " "+ex;
	    logger.error(msg+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(rs, pstmt, pstmt2);
	    UnoConnect.databaseDisconnect(con);						
	}
	return msg;
    }

    public String doDelete(){
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = " delete from term_notifications where id=?";
	try{
	    con = UnoConnect.getConnection();
	    if(con == null){
		msg = "Could not connect to DB ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, id);
	    pstmt.executeUpdate();
	}
	catch(Exception ex){
	    msg += " "+ex;
	    logger.error(msg+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(pstmt, rs);
	    UnoConnect.databaseDisconnect(con);
	}
	return msg;
    }
	
    /**
       // individuals who will receive termination notifications
       //
    create table term_notifications (
    id int unsigned not null auto_increment,
    send_time datetime,
    sender_id int unsigned,
    termination_id int unsigned,
    recipient_emails varchar(256),
    email_text text,
    send_error text,
    primary key(id),
    foreign key(termination_id) references emp_terminations(id),
    foreign key(sender_id) references employees(id)
    )engine=InnoDB;

    //
    // copy of old term email
    //
no_reply@bloomington.in.gov
	
Mon, Jul 31, 4:22 PM (17 hours ago)
	
to employee_termination
Regular Full-Time: Yes
Part-Time Hours Per Week:
Employee: Jayme Washel
Date of Birth: 12/20/73
Date of Hire: 01/29/01
Last Day of Work: 07/30/23
Last Payroll Period: 08/06/23
Department:: Fire
Job Title: Deputy Chief
Job Grade:
Job Step:
Rate of Pay: $88,586 / $42.59
Supervisor Name: Jason Moore
Supervisor Phone Number: 812-332-9763
Address: 4442 S Carberry Ct
City: Bloomington
State: IN
Zip Code: 47401
Phone Number: 812-325-5328
Alt. Phone Number:
Personal Email: truckie5175@gmail.com
Email Address: washelja@bloomington.in.gov
Email Account Action: Forward To
Forward Email Address1: litwinm@bloomington.in.gov
Forward Email Address2:
Forward Email Address3:
Forward Email Address4:
Forward for # Days: 120
Google Drive/H Drive Action: Archive
Google Drive Transfer To:
Google/H Drive Address1:
Google/H Drive Address2:
Google/H Drive Address3:
Google/H Drive Address4:
Google Calendar Action: Close
Calendar Email Address:
Zoom Account Action: Close
Zoom Account Email Address:
Badge Return Status: YesBadgeReturned
Number of Hours Worked in the Current Pay Period: 40
Vacation Time: 123.26
Comp Time: 5.61
PTO: 9.10
Remarks: Last day of employment was July 30th, 2023.

Jayme worked 40 hrs--used 9.38 vac during that week (30.62 hrs worked plus the 9.38 vac used)

Which leaves him with:
123.26 hrs of VAC
5.61 hrs of COMP
9.10 hrs of HOLIDAY COMP

Thank you!
Submitted By: Edie Henderson    

    
    */
}
