package in.bloomington.timer.list;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.*;
import java.sql.*;
import java.io.*;
import javax.sql.*;
import java.text.SimpleDateFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;

public class EmpTerminateList{

    static Logger logger = LogManager.getLogger(EmpTerminateList.class);
    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");	
    static final long serialVersionUID = 3800L;
    String date_from = "", date_to="", department_id="", group_id="";
    String submitted_by_id = "";
	
    String status = ""; // all, sent, not-sent
    boolean recent_only = false, set_limit=true;
    boolean notification_reminder = false;
    List<EmpTerminate> terms = null;
	
    public EmpTerminateList(){
    }
    public void setDate_from(String val){
	if(val != null)
	    date_from =  val;
    }
    public void setDate_to(String val){
	if(val != null)
	    date_to =  val;
    }
    public void setDepartment_id(String val){
	if(val != null)
	    department_id =  val;
    }
    public void setGroup_id(String val){
	if(val != null)
	    group_id =  val;
    }    
    public String getDate_from(){
	return date_from ;
    }
    public String getDate_to(){
	return date_to ;
    }
    public String getDepartment_id(){
	return department_id ;
    }
    public String getGroup_id(){
	return group_id ;
    }    
    public void setRecent_only(){
	recent_only = true;
    }
    public void setStatus(String val){
	if(val != null)
	    status = val;
    }
    public List<EmpTerminate> getTerms(){
	return terms;
    }
    public void setNotificationReminder(){
	notification_reminder = true;
    }
    public void setSubmitted_by_id(String val){
	if(val != null)
	    submitted_by_id = val;
    }
    void checkLimitFlag(){
	if(!date_from.isEmpty() || !date_to.isEmpty() || !department_id.isEmpty()){
	    set_limit = false;
	}
    }
    public String find(){
		
	String back = "";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	checkLimitFlag();
	Connection con = UnoConnect.getConnection();
	String qq = "select  "+
	    "id,"+
	    "employee_id,"+
	    "full_name,"+
	    "employment_type,"+
	    "date_format(last_pay_period_date,'%m/%d/%Y'),"+ // date
	    
	    "emp_address,"+
	    "emp_city,"+
	    "emp_state,"+
	    "emp_zip,"+
	    "emp_phone,"+
	    
	    "emp_alt_phone,"+
	    "date_format(date_of_birth,'%m/%d/%Y'),"+ // date	    
	    "personal_email,"+
	    "email,"+
	    "email_account_action,"+
	    
	    "forward_emails,"+
	    "forward_days_cnt,"+
	    "drive_action,"+
	    "drive_to_person_email,"+
	    "drive_to_shared_emails,"+
	    
	    "calendar_action,"+
	    "calendar_to_email,"+
	    "zoom_action,"+
	    "zoom_to_email,"+
	    "comp_time,"+
	    
	    "vac_time,"+
	    "pto,"+
	    "remarks,"+
	    "submitted_by_id,"+
	    "date_format(submitted_date,'%m/%d/%Y'), "+ // date

	    "process_status, "+	    
	    "recipients_informed "+

	    " from emp_terminations "; 				
	if(con == null){
	    back = "Could not connect to DB";
	    return back;
	}
	String qw = "";
	try{
	    if(!date_from.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";
		qw += " last_pay_period_date >= ? ";
	    }
	    if(!date_to.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";
		qw += " last_pay_period_date <= ? ";
	    }

	    if(!submitted_by_id.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";
		qw += " submitted_by_id = ? ";		
	    }	    
	    if(notification_reminder){
		if(!qw.isEmpty()) qw += " and ";
		qw += " recipients_informed is null ";		
	    }
	    else if(!status.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";		
		if(status.equals("sent")){
		    qw += " recipients_informed is not null ";
		}
		else{
		    qw += " recipients_informed is null ";
		}
	    }
	    if(!qw.isEmpty()){
		qq += " where "+qw;
	    }
	    qq += " order by id desc ";
	    if(set_limit)
		qq += " limit 10 ";
	    logger.debug(qq);
	    pstmt = con.prepareStatement(qq);
	    int jj=1;
	    if(!date_from.isEmpty()){
		java.util.Date date_tmp = df.parse(date_from);
		pstmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
	    }
	    if(!date_to.isEmpty()){
		java.util.Date date_tmp = df.parse(date_to);
		pstmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
	    }
	    rs = pstmt.executeQuery();
	    if(terms == null)
		terms = new ArrayList<>();
	    while(rs.next()){
		EmpTerminate one =
		    new EmpTerminate(
				     rs.getString(1),
				     rs.getString(2),
				     rs.getString(3),
				     rs.getString(4),
				     rs.getString(5),
				     
				     rs.getString(6),
				     rs.getString(7),
				     rs.getString(8),
				     rs.getString(9),
				     rs.getString(10),
				     
				     rs.getString(11),
				     rs.getString(12),
				     rs.getString(13),
				     rs.getString(14),
				     rs.getString(15),
				     
				     rs.getString(16),
				     rs.getString(17),
				     rs.getString(18),
				     rs.getString(19),
				     rs.getString(20),
				     
				     rs.getString(21),
				     rs.getString(22),
				     rs.getString(23),
				     rs.getString(24),
				     rs.getDouble(25),
				     
				     rs.getDouble(26),
				     rs.getDouble(27),
				     rs.getString(28),
				     rs.getString(29),
				     rs.getString(30),
				     
				     rs.getString(32),	     
				     rs.getString(31) != null);

		if(!terms.contains(one))
		    terms.add(one);
	    }
	}
	catch(Exception ex){
	    back += ex+" : "+qq;
	    logger.error(back);
	}
	finally{
	    Helper.databaseDisconnect(pstmt, rs);
	    UnoConnect.databaseDisconnect(con);
	}
	return back;
    }
}






















































