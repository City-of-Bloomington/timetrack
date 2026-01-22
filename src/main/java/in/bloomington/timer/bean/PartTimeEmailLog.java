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
import java.time.format.DateTimeFormatter;
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
    String id="", sent_time = "", warn_id="",
	employee_id="", supervisor_id="", 
	email_to="",
	cc="", // supervisor
	subject="",
	text_message="",
	send_errors="";
    String employee_name = "", supervisor_name="", job_title="";
    // Integer warn_type = 1; // 1:Wednesday warning, 2:week total warning
    // Integer part_week_num = 1; // 1, 2
    // Integer week_of_year = null;
    Employee employee = null, supervisor = null;
    PartTimeWarn warn = null;
    public PartTimeEmailLog(){
    }
    public PartTimeEmailLog(String val){
	setId(val);
	doSelect();
    }
    public PartTimeEmailLog(
			    String val,
			    String val2,
			    String val3,
			    String val4,
			    String val5,
			    
			    String val6,
			    String val7,
			    String val8,
			    String val9, 
			    String val10,

			    String val11,
			    String val12,
			    Integer val13,
			    Integer val14,
			    Integer val15,
			    Double val16,
			    Integer val17,
			    
			    String val18, // additional
			    String val19,
			    String val20
			    ){
	setVals(val, val2, val3, val4, val5, 
		val6, val7, val8, val9, val10,
		val11, val12, val13, val14, val15, 
		val16, val17, val18, val19, val20);
    }	
    // for new record
    public PartTimeEmailLog(
			    String val,
			    String val2,
			    String val3,
			    String val4,
			    String val5,
			    String val6,
			    String val7
		    ){
	setWarn_id(val);
	setEmployee_id(val2);
	setSupervisor_id(val3);
	setEmailTo(val4);
	setCc(val5);
	setSubject(val6);
	setTextMessage(val7);
	warn = new PartTimeWarn(val);
	String back = warn.doSelect();
	if(!back.isEmpty()){
	    logger.error(back);
	}
    }
    private void setVals(
			    String val,
			    String val2,
			    String val3,
			    String val4,
			    String val5,
			    
			    String val6,
			    String val7,
			    String val8,
			    String val9, 
			    String val10,

			    String val11,
			    String val12,
			    Integer val13,
			    Integer val14,
			    Integer val15,
			    Double val16,
			    Integer val17,
			    
			    String val18, // additional
			    String val19,
			    String val20
			    ){
	setId(val);
	setWarn_id(val2);
	setSentTime(val3);
	setEmployee_id(val4);
	setSupervisor_id(val5);
	setEmailTo(val6);
	setCc(val7);
	setSubject(val8);
	setTextMessage(val9);
	setSendErrors(val10);
	setEmployeeName(val18);
	setSupervisorName(val19);
	setJobTitle(val20);
	warn =
	    new PartTimeWarn(val11,val12,val13,val14,val15,val16,val17);
    }	    
    public String getId(){
	return id;
    }
    public String getWarn_id(){
	return warn_id;
    }        
    /**
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
    */
    public String getSentTime(){
	return sent_time;
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
    public 	void setWarn_id(String val){
	if(val != null)
	    warn_id = val;
    }    
    /**
    public void setWeekNum(Integer val){
	if(val != null)
	    week_num = val;
    }
    public void setWeekOfYear(Integer val){
	if(val != null)
	    week_of_year = val;
    }    
    public void setWarnType(Integer val){
	if(val != null)
	    warn_type = val;
    }
    public 	void setJob_id(String val){
	if(val != null)
	    job_id = val;
    }        
    */
    public 	void setEmployee_id(String val){
	if(val != null)
	    employee_id = val;
    }
    public 	void setSupervisor_id(String val){
	if(val != null)
	    supervisor_id = val;
    }
    

    public 	void setSentTime(String val){
	if(val != null)
	    sent_time = val;
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
    public PartTimeWarn getWarn(){
	return warn;
	    
    }
    // not needed here
    private int findCurrentWeekOfYear(){
	// 
	// current week number in this year
	//
	LocalDate date = LocalDate.now();
	// Use WeekFields.ISO for the ISO-8601 standard (Monday as first day)
        WeekFields weekFields = WeekFields.ISO;
	// WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int localeWeekNumber = date.get(weekFields.weekOfWeekBasedYear());
        System.out.println("Locale Week Number: " + localeWeekNumber);
	return localeWeekNumber;
	/**
	   for specific date
	   LocalDate specificDate = LocalDate.of(2025, 1, 1);
	   // Get the ISO week number
	   int weekNumber = specificDate.get(WeekFields.ISO.weekOfWeekBasedYear());
	   // M month
	   // d day
	   // uuuu year
	   DateTimeFormatter f = DateTimeFormatter.ofPattern( "M/d/uuuu" , Locale.US ) ;
	   LocalDate ld = LocalDate.parse( "1/23/2017" , f ) 


	 */
	
    }
		
    public String doSave(){
		
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null, pstmt2 = null, pstmt3=null;
	ResultSet rs = null;
	int cnt = 0;
	// first we check if we already warned the employee
	//
	String qq = " select count(*) from part_time_email_logs where warn_id=? ";
	String qq2 = " insert into part_time_email_logs values(0,?,?,?, now(),"+
	    "?,?,?,?,?) ";
	if(warn_id.isEmpty() || email_to.isEmpty()){
	    back = "Part time warning or employee email not set ";
	    return back;
	}
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
	    pstmt.setString(1, warn_id);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		cnt = rs.getInt(1);
	    }
	    if(cnt == 0){
		qq = qq2;
		pstmt2 = con.prepareStatement(qq);
		pstmt2.setString(1, warn_id);
		pstmt2.setString(2, employee_id);
		pstmt2.setString(3, supervisor_id);
		if(email_to.isEmpty())
		    pstmt2.setNull(4, Types.VARCHAR);								
		else
		    pstmt2.setString(4, email_to);
		if(cc.isEmpty())
		    pstmt2.setNull(5, Types.VARCHAR);								
		else
		    pstmt2.setString(5, cc);
		if(subject.isEmpty())
		    pstmt2.setNull(6, Types.VARCHAR);								
		else
		    pstmt2.setString(6, subject);
		if(text_message.isEmpty())
		    pstmt2.setNull(7, Types.VARCHAR);								
		else
		    pstmt2.setString(7, text_message);
		if(send_errors.isEmpty())
		    pstmt2.setNull(8, Types.VARCHAR);								
		else
		    pstmt2.setString(8, send_errors);
		pstmt2.executeUpdate();
		qq = "select LAST_INSERT_ID()";
		pstmt3 = con.prepareStatement(qq);
		rs = pstmt3.executeQuery();
		if(rs.next()){
		    id = rs.getString(1);
		}
	    }
	}catch(Exception ex){
	    back += ex+":"+qq;
	    logger.error(back);
	}
	finally{
	    Helper.databaseDisconnect(rs, pstmt, pstmt2, pstmt3);
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
	qq = " select l.id,"+
	    "l.warn_id,"+	    
	    "l.employee_id,"+
	    "l.supervisor_id,"+
	    "date_format(l.sent_time,'%m/%d/%Y %H:%i'),"+
	    
	    "l.email_to,"+
	    "l.supervisor_cc,"+
	    "l.email_subject,"+
	    "l.text_message,"+
	    "l.send_error,"+
	    
	    "w.id,"+
	    "w.job_id,"+
	    "w.pay_week_num,"+
	    "w.warn_type,"+
	    "w.week_of_year,"+
	    "w.week_total,"+
	    "w.critical_value,"+
	    
	    "concat_ws(' ',e.first_name,e.last_name) employee_name,"+
	    "concat_ws(' ',e2.first_name,e2.last_name) supervisor_name, "+
	    "p.name job_title "+
	    "from part_time_email_logs l "+
	    "join part_time_warns w on w.id=l.warn_id "+
	    "join employees e on l.employee_id = e.id "+
	    "join employees e2 on l.supervisor_id=e2.id "+
	    "left join jobs j on j.id=w.job_id "+
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
		setVals(
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
			rs.getInt(13),
			rs.getInt(14),
			rs.getInt(15),
			rs.getDouble(16),
			rs.getInt(17),
			rs.getString(18),
			rs.getString(19),
			rs.getString(20));
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
    warn_id int unsigned,
    employee_id int unsigned,
    supervisor_id int unsigned,
    sent_time date,
    email_to varchar(80),
    supervisor_cc varchar(80),
    email_subject varchar(80),
    text_message varchar(514),
    send_error varchar(514),
    primary key(id),
    foreign key(employee_id) references employees(id),
    foreign key(supervisor_id) references employees(id),
    foreign key(warn_id) references part_time_warns(id)    
    )engine=InnoDB;

 select l.id,
	    l.warn_id,	    
	    l.employee_id,
	    l.supervisor_id,
	    date_format(l.sent_time,'%m/%d/%Y %H:%i'),
	    l.email_to,
	    l.supervisor_cc,
	    l.email_subject,
	    l.text_message,
	    l.send_error,
	    w.id,
	    w.job_id,
	    w.pay_week_num,
	    w.warn_type,
	    w.week_of_year,
	    w.week_total,
	    w.critical_value,
	    concat_ws(' ',e.first_name,e.last_name) employee_name,
	    concat_ws(' ',e2.first_name,e2.last_name) supervisor_name, 
	    p.name job_title 
	    from part_time_email_logs l 
	    join part_time_warns w on w.id=l.warn_id 
	    join employees e on l.employee_id = e.id 
	    join employees e2 on l.supervisor_id=e2.id 
	    left join jobs j on j.id=w.job_id 
	    left join positions p on p.id=j.position_id
	    where l.id=1;
	    
     */
	
}























































