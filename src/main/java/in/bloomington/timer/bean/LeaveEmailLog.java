package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.sql.*;
import javax.naming.*;
import javax.naming.directory.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;

public class LeaveEmailLog{

    static final long serialVersionUID = 3700L;	
    static Logger logger = LogManager.getLogger(LeaveEmailLog.class);
    String id="", email_from="", email_to="", message="",date="",
	email_type = "Request", // Request, Review, Cancelled Request, default=Request
	error_msg="";
    //
    public LeaveEmailLog(String val){
	//
	setId(val);
    }
    // for new record
    public LeaveEmailLog(String val, String val2,
			 String val3, String val4,
			 String val5){
	setEmail_to(val);	
	setEmail_from(val2);
	setMessage(val3);
	setEmail_type(val4);
	setError_msg(val5);
    }
    // for saving
    public LeaveEmailLog(String val, String val2,
			 String val3, String val4,
			 String val5, String val6,
			 String val7
			   ){
	setId(val);
	setEmail_from(val2);
	setEmail_to(val3);
	setMessage(val4);
	setDate(val5);
	setEmail_type(val6);
	setError_msg(val7);
    }		
    //
    // getters
    //
    public String getId(){
	return id;
    }
    public String getMessage(){
	return message;
    }
    public String getEmail_to(){
	return email_to;
    }		
    public String getEmail_from(){
	return email_from; 
    }
    public String getDate(){
	return date;
    }		
    public String getEmail_type(){
	return email_type;
    }
    public String getError_msg(){
	return error_msg;
    }
    public String getStatus(){
	if(error_msg.isEmpty()){
	    return "Success";
	}
	return "Failed";
    }
	
    //
    // setters
    //
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setEmail_to(String val){
	if(val != null)
	    email_to = val.trim();
    }
    public void setEmail_from(String val){
	if(val != null)
	    email_from = val.trim();
    }    
    public void setMessage(String val){
	if(val != null)
	    message = val.trim();
    }
    public void setDate(String val){
	if(val != null)
	    date = val;
    }
    public void setEmail_type(String val){
	if(val != null)
	    email_type = val;
    }    
    public void setError_msg(String val){
	if(val != null)
	    error_msg = val;
    }		
    public String toString(){
	return message;
    }
    //
    public String doSelect(){
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "select id,email_to,email_from, email_msg,date_format(sent_date,'%m/%d/%Y'),emaiL_type,error_msg "+
	    "from leave_emaiL_logs where id=?";
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
		setEmail_to(rs.getString(2));
		setEmail_from(rs.getString(3));
		setMessage(rs.getString(4));
		setDate(rs.getString(5));
		setEmail_type(rs.getString(6));
		setError_msg(rs.getString(7));
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
	String qq = " insert into leave_email_logs values(0,?,?,?,now(),?,?)";
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = "Could not connect to DB ";
	    return msg;
	}				
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, email_to);
	    pstmt.setString(2, email_from);
	    pstmt.setString(3, message);
	    pstmt.setString(4, email_type);
	    if(error_msg.isEmpty())
		pstmt.setNull(5, Types.VARCHAR);
	    else
		pstmt.setString(5, error_msg);
	    pstmt.executeUpdate();
	    //
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

}
/**
create table leave_email_logs(
id int unsigned auto_increment primary key,
email_to varchar(80),
email_from varchar(80),
email_msg varchar(1200),
sent_date date not null,
email_type enum('Request','Review','Cancelled Request'),
error_msg varchar(1200)
)engine=InnoDB;

alter table leave_email_logs modify email_type enum('Request','Review','Cancelled Request');

*/
