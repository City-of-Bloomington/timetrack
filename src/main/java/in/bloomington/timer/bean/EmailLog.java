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


public class EmailLog{
		static Logger logger = LogManager.getLogger(EmailLog.class);
		final static long serialVersionUID = 292L;
		boolean debug = false;		
		String date_time = "", type="", // Approvers, Processors
				user_id="",
				email_from = "",
				id = "",
				email_to="",
				cc="",
				bcc="",
				subject="",
				text_message="",
				send_errors="";
		Employee user = null;
		public EmailLog(boolean val){
				debug = val;
		}
		public EmailLog(boolean val, String val2){
				debug = val;
				if(val2 != null)
						id = val2;
		}
		public EmailLog(boolean deb,
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
										String val11
										){
				debug = deb;
				setId(val);
				setUser_id(val2);
				setEmailFrom(val3);
				setEmailTo(val4);
				setCc(val5);
				setBcc(val6);
				setSubject(val7);
				setTextMessage(val8);
				setSendErrors(val9);
				setDateTime(val10);
				setType(val11);
		}	
		
		public EmailLog(boolean deb,
										String val,
										String val2,
										String val3,
										String val4,
										String val5,
										String val6,
										String val7,
										String val8,
										String val9
										){
				debug = deb;
				setUser_id(val);
				setEmailFrom(val2);
				setEmailTo(val3);
				setCc(val4);
				setBcc(val5);
				setSubject(val6);
				setTextMessage(val7);
				setSendErrors(val8);
				setType(val9);
				
		}	
		public String getId(){
				return id;
		}
		public String getUser_id(){
				return user_id;
		}
		public String getType(){
				return type;
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
		public	String getBcc(){
				return bcc;
		}
		public String getReceipiants(){
				String ret = email_to;
				if(!cc.equals("")){
						if(!ret.equals("")) ret += ", ";						
						ret += cc;
				}
				if(!bcc.equals("")){
						if(!ret.equals("")) ret += ", ";
						if(bcc.indexOf(",") > -1){
								ret += bcc.replace(",",", ");
						}
						else{
								ret += bcc;
						}
				}
				return ret;
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
		public String getSendErrors(){
				return send_errors;
		}
		public String getStatus(){
			  return send_errors.equals("")? "Success":"Failure";
		}
		public boolean isFailure(){
				return !send_errors.equals("");
		}
		public boolean isSuccess(){
				return send_errors.equals("");
		}
		public Employee getRunBy(){
				if(user == null && !user_id.equals("")){
						Employee one = new Employee(user_id);
						String back = one.doSelect();
						if(back.equals("")){
								user = one;
						}
				}
				return user;
		}
		//
		public 	void setId(String val){
				if(val != null)
				id = val;
		}
		public 	void setUser_id(String val){
				if(val != null)
				user_id = val;
		}		
		public 	void setDateTime(String val){
				if(val != null)
						date_time = val;
		}

		public 	void setType(String val){
				if(val != null)
						type = val;
		}
		public 	void setEmailTo(String val){
				if(val != null)
						email_to = val;
		}
		public void setEmailFrom(String val){
				if(val != null)
						email_from = val;
		}		
		public	void setCc(String val){
				if(val != null)
						cc = val;
		}
		public void setBcc(String val){
				if(val != null)
						bcc = val;
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
		
		public String doSave(){
		
				String back = "";
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				con = UnoConnect.getConnection();
				if(con == null){
						back = "Could not connect to Database ";
						return back;
				}
				String qq = " insert into email_logs values(0,?,now(),?,?, "+
								"?,?,?,?,?,"+
								"?)";
				
				try{
						if(debug){
								logger.debug(qq);
						}
						pstmt = con.prepareStatement(qq);			
						pstmt.setString(1, user_id);
						if(email_from.equals(""))
								pstmt.setNull(2, Types.VARCHAR);								
						else
								pstmt.setString(2, email_from);
						if(email_to.equals(""))
								pstmt.setNull(3, Types.VARCHAR);								
						else
								pstmt.setString(3, email_to);						
						if(cc.equals(""))
								pstmt.setNull(4, Types.VARCHAR);								
						else
								pstmt.setString(4, cc);
						if(bcc.equals(""))
								pstmt.setNull(5, Types.VARCHAR);								
						else
								pstmt.setString(5, bcc);
						if(subject.equals(""))
								pstmt.setNull(6, Types.VARCHAR);								
						else
								pstmt.setString(6, subject);
						if(text_message.equals(""))
								pstmt.setNull(7, Types.VARCHAR);								
						else
								pstmt.setString(7, text_message);
						if(send_errors.equals(""))
								pstmt.setNull(8, Types.VARCHAR);								
						else
								pstmt.setString(8, send_errors);
						if(type.equals(""))
								pstmt.setNull(9, Types.VARCHAR);								
						else
								pstmt.setString(9, type);						
						pstmt.executeUpdate();
				}catch(Exception ex){
						back += ex+":"+qq;
						logger.error(back);
				}
				finally{
						Helper.databaseDisconnect(pstmt, rs);	
				}
				return back;
		
		}
		public String doSelect(){
				String back = "";
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String qq = "";
				qq = " select id,user_id,to_char(date_time,'%m/%d/%Y %H:%i'),email_from,email_to,cc,bcc,subject,text_message,send_errors,type from email_logs where id=? ";
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
								setUser_id(rs.getString(2));										
								setDateTime(rs.getString(3));
								setEmailFrom(rs.getString(4));					
								setEmailTo(rs.getString(5));
								setCc(rs.getString(6));										
								setBcc(rs.getString(7));
								setSubject(rs.getString(8));
								setTextMessage(rs.getString(9));
								setSendErrors(rs.getString(10));
								setType(rs.getString(11));
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
				}
				return back;
		}
	
}























































