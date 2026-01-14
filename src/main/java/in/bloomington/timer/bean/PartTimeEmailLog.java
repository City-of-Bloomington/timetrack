package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */

import java.util.*;
import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.sql.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.naming.*;
import javax.sql.*;
import javax.naming.directory.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.Employee;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class PartTimeEmailLog{
    static Logger logger = LogManager.getLogger(PartTimeEmailLog.class);
    final static long serialVersionUID = 292L;
    boolean debug = false;		
    String id="", date_time = "", 
	employee_id="", supervisor_id="", job_id="",
	email_to="",
	cc="", // supervisor
	subject="",
	text_message="",
	send_errors="";
    String employee_name = "", supervisor_name="", job_title="";
    Integer warn_type = 1; // 1:Wednesday warning, 2:week total warning
    Integer week_num = 1; // 1, 2
    Employee employee = null, supervisor = null;
    public PartTimeEmailLog(){
    }
    public PartTimeEmailLog(String val){
	if(val != null)
	    id = val;
    }
    public PartTimeEmailLog(
			    String val,
			    String val2,
			    String val3,
			    String val4,
			    int val5,
			    
			    int val6,
			    String val7,
			    String val8,
			    String val9,
			    String val10,
			    
			    String val11,
			    String val12,
			    String val13,
			    String val14,
			    String val15
			    ){
	setId(val);
	setEmployee_id(val2);
	setSupervisor_id(val3);
	setJob_id(val4);
	setWeekNum(val5);
	setWarnType(val6);
	setEmailTo(val7);
	setCc(val8);
	setSubject(val9);
	setTextMessage(val10);
	setSendErrors(val11);
	setDateTime(val12);
	setEmployeeName(val13);
	setSupervisorName(val14);
	setJobTitle(val15);
    }	
    // for new record
    public PartTimeEmailLog(
			    String val,
			    String val2,
			    String val3,
			    int val4,
			    String val5,
			    String val6,
			    String val7,
			    String val8
		    ){
	setEmployee_id(val);
	setSupervisor_id(val2);
	setJob_id(val3);
	setWarnType(val4);
	setEmailTo(val5);
	setCc(val6);
	setSubject(val7);
	setTextMessage(val8);
				
    }	
    public String getId(){
	return id;
    }
    public String getJob_id(){
	return job_id;
    }    
    public String getEmployee_id(){
	return employee_id;
    }
    public String getSupervisor_id(){
	return supervisor_id;
    }    
    public Integer getWeekNum(){
	return week_num;
    }
    public Integer getWarnType(){
	return warn_type;
    }
    public String getWarnTypeText(){
	String ret = "Week Total Warning";
	if(warn_type == 1){
	    ret = "Wednesday Warning";
	}
	return ret;
    }
    public String getDateTime(){
	return date_time;
    }

    public String getEmailTo(){
	return email_to;
    }
    public String getCc(){
	return cc;
    }
    public String getReceipiants(){
	String ret = email_to;
	if(!cc.isEmpty()){
	    if(!ret.isEmpty()) ret += ", ";						
	    ret += cc;
	}
	return ret;
    }		
    public String getSubject(){
	return subject;
    }
    public String getTextMessage(){
	return text_message;
    }
    public String getSendErrors(){
	return send_errors;
    }
    public String getStatus(){
	return send_errors.isEmpty()? "Success":"Failure";
    }
    public boolean isFailure(){
	return !send_errors.isEmpty();
    }
    public boolean isSuccess(){
	return send_errors.isEmpty();
    }
    public String getEmployeeName(){
	return employee_name;
    }
    public String getSupervisorName(){
	return supervisor_name;
    }
    public String getJobTitle(){
	return job_title;
    }    
    //
    public 	void setId(String val){
	if(val != null)
	    id = val;
    }
    
    public void setWeekNum(Integer val){
	if(val != null)
	    week_num = val;
    }
    public void setWarnType(Integer val){
	if(val != null)
	    warn_type = val;
    }    
    public 	void setEmployee_id(String val){
	if(val != null)
	    employee_id = val;
    }
    public 	void setSupervisor_id(String val){
	if(val != null)
	    supervisor_id = val;
    }
    public 	void setJob_id(String val){
	if(val != null)
	    job_id = val;
    }    
    public 	void setDateTime(String val){
	if(val != null)
	    date_time = val;
    }

    public 	void setEmailTo(String val){
	if(val != null)
	    email_to = val;
    }
    public	void setCc(String val){
	if(val != null)
	    cc = val;
    }
    public void setSubject(String val){
	if(val != null)
	    subject = val;
    }
    public void setTextMessage(String val){
	if(val != null)
	    text_message = val;
    }
    public void setSendErrors(String val){
	if(val != null)
	    send_errors = val;
    }
    public void setEmployeeName(String val){
	if(val != null)
	    employee_name = val;
    }
    public void setSupervisorName(String val){
	if(val != null)
	    supervisor_name = val;
    }
    public void setJobTitle(String val){
	if(val != null)
	    job_title = val;
    }    
    private int findCurrentWeekNumber(){
	// 
	// current week number in this year
	//
	LocalDate date = LocalDate.now();
	WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int localeWeekNumber = date.get(weekFields.weekOfWeekBasedYear());
        System.out.println("Locale Week Number: " + localeWeekNumber);
	return localeWeekNumber;
    }
		
    public String doSave(){
		
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null, pstmt2 = null;
	ResultSet rs = null;
	int cnt = 0;
	// first we check if we already warned the employee
	//
	String qq = " select count(*) from part_time_email_logs where job_id=? "+
	    " and week_num = ? and warn_type = ? ";
	String qq2 = " insert into part_time_email_logs values(0,?,?,?,?,?, now(),"+
	    "?,?,?,?,?) ";
	if(job_id.isEmpty() && email_to.isEmpty()){
	    back = "Job or employee email not set ";
	    return back;
	}
	week_num = findCurrentWeekNumber();
	con = UnoConnect.getConnection();
	if(con == null){
	    back = "Could not connect to Database ";
	    return back;
	}
	try{
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,job_id);
	    pstmt.setInt(2, week_num);
	    pstmt.setInt(3, warn_type);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		cnt = rs.getInt(1);
	    }
	    if(cnt == 0){
		qq = qq2;
		pstmt2 = con.prepareStatement(qq);		
		pstmt2.setString(1, employee_id);
		pstmt2.setString(2, supervisor_id);
		pstmt2.setString(3, job_id);
		pstmt2.setInt(4, week_num);
		pstmt2.setInt(5, warn_type);
		if(email_to.isEmpty())
		    pstmt2.setNull(6, Types.VARCHAR);								
		else
		    pstmt2.setString(6, email_to);
		if(cc.isEmpty())
		    pstmt2.setNull(7, Types.VARCHAR);								
		else
		    pstmt2.setString(7, cc);
		if(subject.isEmpty())
		    pstmt2.setNull(8, Types.VARCHAR);								
		else
		    pstmt2.setString(8, subject);
		if(text_message.isEmpty())
		    pstmt2.setNull(9, Types.VARCHAR);								
		else
		    pstmt2.setString(9, text_message);
		if(send_errors.isEmpty())
		    pstmt2.setNull(10, Types.VARCHAR);								
		else
		    pstmt2.setString(10, send_errors);
		pstmt2.executeUpdate();
	    }
	}catch(Exception ex){
	    back += ex+":"+qq;
	    logger.error(back);
	}
	finally{
	    Helper.databaseDisconnect(rs, pstmt, pstmt2);
	    UnoConnect.databaseDisconnect(con);
	}
	return back;
		
    }
    public String doSelect(){
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "";
	qq = " select l.id,l.employee_id,l.supervisor_id,l.job_id,l.week_num,l.warn_type,date_format(l.sent_time,'%m/%d/%Y %H:%i'),l.email_to,l.supervisor_cc,l.email_subject,l.text_message,l.send_error,concat_ws(' ',e.first_name,e.last_name) employee_name, concat_ws(' ',e2.first_name,"+
	    "e2.last_name) supervisor_name, "+
	    "p.name job_title "+
	    "from part_time_email_logs l "+
	    "join employees e on l.employee_id = e.id "+
	    "join employees e2 on l.supervisor_id=e2.id "+
	    "left join jobs j on j.id=l.job_id "+
	    " left join positions p on p.id=j.position_id  "+
	    "where l.id=? ";
	if(debug)
	    logger.debug(qq);
	con = UnoConnect.getConnection();
	if(con == null){
	    back = "Could not connect to DB ";
	    return back;
	}				
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,id);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		setId(rs.getString(1));
		setEmployee_id(rs.getString(2));
		setSupervisor_id(rs.getString(3));
		setJob_id(rs.getString(4));
		setWeekNum(rs.getInt(5));
		setWarnType(rs.getInt(6));
		setDateTime(rs.getString(7));
		setEmailTo(rs.getString(8));					
		setCc(rs.getString(9));
		setSubject(rs.getString(10));
		setTextMessage(rs.getString(11));
		setSendErrors(rs.getString(12));
		setEmployeeName(rs.getString(13));
		setSupervisorName(rs.getString(14));
		setJobTitle(rs.getString(15));
	    }
	    else{
		back = "No match found";
	    }
	}catch(Exception ex){
	    back += ex+":"+qq;
	    logger.error(back);
	}
	finally{
	    Helper.databaseDisconnect(pstmt, rs);
	    UnoConnect.databaseDisconnect(con);
	}
	return back;
    }
    
    /**

    create table part_time_email_logs (
    id int unsigned not null auto_increment,
    employee_id int unsigned,
    supervisor_id int unsigned,
    job_id int unsigned,
    week_num int,
    warn_type int,
    sent_time date,
    email_to varchar(80),
    supervisor_cc varchar(80),
    email_subject varchar(80),
    text_message varchar(514),
    send_error varchar(514),
    primary key(id),
    foreign key(employee_id) references employees(id),
    foreign key(supervisor_id) references employees(id),
    foreign key(job_id) references jobs(id)    
    )engine=InnoDB;

	select l.id,l.employee_id,l.supervisor_id,l.job_id,l.week_num,l.warn_type,date_format(l.sent_time,'%m/%d/%Y %H:%i'),l.email_to,l.supervisor_cc,l.email_subject,l.text_message,l.send_error,concat_ws(' ',e.first_name,e.last_name) employee_name, concat_ws(' ',e2.first_name,
	    e2.last_name) supervisor_name, 
	    p.name job_title 
	    from part_time_email_logs l 
	    join employees e on l.employee_id = e.id 
	    join employees e2 on l.supervisor_id=e2.id 
	    left join jobs j on j.id=l.job_id 
	    left join positions p on p.id=j.position_id
	    where l.id=1;
	    
     */
	
}























































