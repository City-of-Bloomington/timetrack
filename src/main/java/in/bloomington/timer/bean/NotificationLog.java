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

public class NotificationLog{

		static final long serialVersionUID = 3700L;	
		static Logger logger = LogManager.getLogger(NotificationLog.class);
    String id="", receipiants="", message="",date="", status="", error_msg="";
		//
		public NotificationLog(String val){
				//
				setId(val);
    }
		// for saving
		public NotificationLog(String val, String val2,
													 String val3, String val4
													 ){
				setReceipiants(val);
				setMessage(val2);
				setStatus(val3);
				setError_msg(val4);
    }				
		public NotificationLog(String val, String val2,
													 String val3, String val4,
													 String val5, String val6													 
													 ){
				setId(val);
				setReceipiants(val2);
				setMessage(val3);
				setDate(val4);
				setStatus(val5);
				setError_msg(val6);
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
    public String getReceipiants(){
				return receipiants;
    }		
    public String getDateTime(){
				return date; // date time
    }
    public String getDate(){
				return date;
    }		
		public String getStatus(){
				return status;
    }
    public String getError_msg(){
				return error_msg;
    }		
    //
    // setters
    //
    public void setId(String val){
				if(val != null)
						id = val;
    }
    public void setReceipiants(String val){
				if(val != null)
						receipiants = val.trim();
    }
    public void setMessage(String val){
				if(val != null)
						message = val.trim();
    }
    public void setStatus(String val){
				if(val != null)
						status = val;
    }
    public void setDate(String val){
				if(val != null)
						date = val;
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
				String qq = "select id,receipants,message,date_format(date,'%m/%d/%Y %h:%i'),status,error_msg "+
						"from notification_logs where id=?";
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
								setReceipiants(rs.getString(2));
								setMessage(rs.getString(3));
								setDate(rs.getString(4));
								setStatus(rs.getString(5));
								setError_msg(rs.getString(6));
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
				String qq = " insert into notification_logs values(0,?,?,now(),?,?)";
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could not connect to DB ";
						return msg;
				}				
				try{
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, receipiants);
						pstmt.setString(2, message);
						pstmt.setString(3, status);
						if(error_msg.isEmpty())
								pstmt.setNull(4, Types.VARCHAR);
						else
								pstmt.setString(4, error_msg);
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
