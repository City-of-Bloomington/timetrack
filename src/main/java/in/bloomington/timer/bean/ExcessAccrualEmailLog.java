package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */

import java.util.*;
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


public class ExcessAccrualEmailLog{
    static Logger logger = LogManager.getLogger(ExcessAccrualEmailLog.class);
    final static long serialVersionUID = 292L;
    boolean debug = false;		
    String date_time = "", 
	email_from = "",
	id = "",
	email_to="",
	email_cc="",
	sent_date="",
	subject="",
	text_message="",
	sent_errors="";
    Employee user = null;
    public ExcessAccrualEmailLog(){

    }
    public ExcessAccrualEmailLog(
		    String val,
		    String val2,
		    String val3,
		    String val4,
		    String val5,
		    String val6
		    ){
	setEmailFrom(val);
	setEmailTo(val2);
	setEmailCc(val3);
	setSubject(val4);
	setTextMessage(val5);
	setSentErrors(val6);
    }	    
    public ExcessAccrualEmailLog(
		    String val,
		    String val2,
		    String val3,
		    String val4,
		    String val5,
		    String val6,
		    String val7,
		    String val8
		    ){
	setId(val);
	setDate(val2);	
	setEmailFrom(val3);
	setEmailTo(val4);
	setEmailCc(val5);
	setSubject(val6);
	setTextMessage(val7);
	setSentErrors(val8);
    }	

    public String getId(){
	return id;
    }
    public String getDate(){
	return sent_date;
    }

    public String getEmailTo(){
	return email_to;
    }
    public String getEmailCc(){
	return email_cc;
    }
    public String getEmailFrom(){
	return email_from;
    }
    public String getSubject(){
	return subject;
    }
    public String getTextMessage(){
	return text_message;
    }
    public String getSentErrors(){
	return sent_errors;
    }
    public String getStatus(){
	return sent_errors.isEmpty()? "Success":"Failure";
    }
    public boolean isFailure(){
	return !sent_errors.isEmpty();
    }
    public boolean isSuccess(){
	return sent_errors.isEmpty();
    }
    //
    public 	void setId(String val){
	if(val != null)
	    id = val;
    }
    public 	void setDate(String val){
	if(val != null)
	    sent_date = val;
    }

    public 	void setEmailTo(String val){
	if(val != null)
	    email_to = val;
    }
    public void setEmailFrom(String val){
	if(val != null)
	    email_from = val;
    }		
    public	void setEmailCc(String val){
	if(val != null)
	    email_cc = val;
    }
    public void setSubject(String val){
	if(val != null)
	    subject = val;
    }
    public void setTextMessage(String val){
	if(val != null)
	    text_message = val;
    }
    public void setSentErrors(String val){
	if(val != null)
	    sent_errors = val;
    }
		
    public String doSave(){
		
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = " insert into excess_accrual_email_logs values(0,now(),?,?, "+
	    "?,?,?,?)";
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
	    if(email_from.isEmpty())
		pstmt.setNull(1, Types.VARCHAR);								
	    else
		pstmt.setString(1, email_from);
	    if(email_to.isEmpty())
		pstmt.setNull(2, Types.VARCHAR);								
	    else
		pstmt.setString(2, email_to);						
	    if(email_cc.isEmpty())
		pstmt.setNull(3, Types.VARCHAR);								
	    else
		pstmt.setString(3, email_cc);
	    if(subject.isEmpty())
		pstmt.setNull(4, Types.VARCHAR);								
	    else
		pstmt.setString(4, subject);
	    if(text_message.isEmpty())
		pstmt.setNull(5, Types.VARCHAR);								
	    else
		pstmt.setString(5, text_message);
	    if(sent_errors.isEmpty())
		pstmt.setNull(6, Types.VARCHAR);								
	    else
		pstmt.setString(6, sent_errors);
	    pstmt.executeUpdate();
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
    public String doSelect(){
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "";
	qq = " select id,to_char(sent_date,'%m/%d/%Y'),email_from,email_to,email_cc,email_subject,email_msg,error_msg from excess_accrual_email_logs where id=? ";
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
		setDate(rs.getString(2));
		setEmailFrom(rs.getString(3));					
		setEmailTo(rs.getString(4));
		setEmailCc(rs.getString(5));										
		setSubject(rs.getString(6));
		setTextMessage(rs.getString(7));
		setSentErrors(rs.getString(8));
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
	
}
/**
create table excess_accrual_email_logs(
id int unsigned auto_increment primary key,
sent_date date,
email_from varchar(80),
email_to varchar(80),
email_cc varchar(160),
email_subject varchar(80),
email_msg varchar(1200),
error_msg varchar(1200)
)Engine=InnoDB;





 */






















































