package in.bloomington.timer.leave;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.Serializable;
import java.util.List;
import java.sql.*;
import javax.sql.*;
import java.text.SimpleDateFormat;
import in.bloomington.timer.bean.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LeaveBlockLog{

		static Logger logger = LogManager.getLogger(LeaveBlockLog.class);
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");		
		static final long serialVersionUID = 250L;		
    String id="", block_id="",
				document_id="", 
				hour_code_id="",
				request_date="", request_approval="", action_status="",
				action_by="", action_date="", change_type="", change_time="",
				change_by="";
		//
		int order_index = 0;
		String hour_code_name = "", hour_code_desc="", job_name="";
		String date = ""; // the user pick date, needed for PTO, Holiday etc
		String errors = "";
		double hours = 0.0;
		HourCode hourCode = null;
		JobTask jobTask = null;
		LeaveDocument document = null;
		//
    public LeaveBlockLog(
								 String val,
								 String val2,
								 String val3,
								 String val4,
								 String val5,
								 
								 double val6,
								 String val7,								 
								 boolean val8,
								 String val9,
								 String val10,
								 
								 String val11,
								 String val12,
								 String val13,
								 String val14
							 ){
				setVals(val, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14);
		}		
		void setVals(
								 String val,
								 String val2,
								 String val3,
								 String val4,
								 String val5,
								 
								 double val6,
								 String val7,								 
								 boolean val8,
								 String val9,
								 String val10,
								 
								 String val11,
								 String val12,
								 String val13,
								 String val14
							 ){								
				setId(val);
				setDocument_id(val2);
				setBlock_id(val3);
				setHour_code_id(val4);
				setDate(val5);
				setHours(""+val6);
				setRequestDate(val7);
				setRequestApproval(val8);
				setActionStatus(val9);
				setActionBy(val10);
				setActionDate(val11);
				setChangeType(val12);
				setChangeTime(val13);
				setChangeBy(val14);
    }
    public LeaveBlockLog(String val){
				setId(val);
    }
    public LeaveBlockLog(){
    }		
    //
    // getters
    //
		public String getId(){
				return id;
    }
		public String getBlock_id(){
				return block_id;
    }		
    public String getDocument_id(){
				return document_id;
    }
    public String getHour_code_id(){
				return hour_code_id;
    }
		public String getId_compound(){
				getHourCode();
				return hourCode.getId_compound();
		}
    public String getDate(){
				return date;
    }
    public String getRequestDate(){
				return request_date;
    }
    public String getActionStatus(){
				return action_status;
    }
    public String getActionBy(){
				return action_by;
    }
		public String getActionDate(){
				return action_date;
    }
    public double getHours(){
				return hours;
    }
    public String getHoursStr(){
				if(hours == 0.0){
						return "";
				}
				return ""+hours;
    }
    public String getChangeBy(){
				return change_by;
    }
    public String getChangeType(){
				return change_type;
    }
    public String getChangeTime(){
				return change_time;
    }		
		public String getHourCodeName(){
				return hour_code_name;
    }
    //
    // setters
    //
    public void setId (String val){
				if(val != null)
						id = val;
    }
    public void setDocument_id (String val){
				if(val != null && !val.equals("-1"))
						document_id = val;
    }
		//
		// we may get something like 1_Hours
		//
    public void setHour_code_id(String val){
				if(val != null && !val.equals("-1")){
						if(val.indexOf("_") > -1){
								hour_code_id = val.substring(0,val.indexOf("_"));
						}
						else{
								hour_code_id = val;
						}
				}
    }
		public void setBlock_id(String val){
				if(val != null)
						block_id = val;
    }
		public void setChangeType(String val){
				if(val != null)
					 change_type = val;
    }
		public void setChangeBy(String val){
				if(val != null)
					 change_by = val;
    }
		public void setChangeTime(String val){
				if(val != null)
					 change_time = val;
    }		
		//
		// set by user when click on calendar block
		// needed for hour code that uses hours instead of
		// begin time and end time
		//
    public void setDate(String val){ 
				if(val != null)
						date = val;
    }
    public void setRequestDate(String val){ 
				if(val != null)
						request_date = val;
    }
    public void setRequestApproval(boolean val){ 
				if(val)
						request_approval = "y";
    }		
    public void setActionStatus(String val){ 
				if(val != null)
						action_status = val;
    }
    public void setActionBy(String val){ 
				if(val != null)
						action_by = val;
    }
    public void setActionDate(String val){ 
				if(val != null)
						action_date = val;
    }
    public void setHours(String val){
				if(val != null && !val.equals("")){
						try{
								hours = Double.parseDouble(val);
						}catch(Exception ex){

						}
				}
    }
		public String toString(){
				return id;
		}
		public boolean equals(Object o) {
				if (o instanceof LeaveBlockLog) {
						LeaveBlockLog c = (LeaveBlockLog) o;
						if ( this.id.equals(c.getId())) 
								return true;
				}
				return false;
		}
		public int hashCode(){
				int seed = 37;
				if(!id.equals("")){
						try{
								seed += Integer.parseInt(id)*31;
						}catch(Exception ex){
								// we ignore
						}
				}
				return seed;
		}
		public HourCode getHourCode(){
				if(hourCode == null && !hour_code_id.equals("")){
						HourCode one = new HourCode(hour_code_id);
						String back = one.doSelect();
						if(back.equals("")){
								hourCode = one;
						}
				}
				else{
						hourCode = new HourCode();
				}
				return hourCode;
		}
		public LeaveDocument getDocument(){
				if(!document_id.equals("") && document == null){
						LeaveDocument one = new LeaveDocument(document_id);
						String back = one.doSelect();
						if(back.equals("")){
							 document = one;
						}
				}
				return document;
		}
		public String doSave(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				if(!errors.equals("")){
						return errors;
				}
				String qq = "insert into leave_block_logs values(0,?,?,?,?, "+
						"?,?,?,?,?, "+
						"?,?,now(),?) ";
				String qq2 = "select LAST_INSERT_ID()";
				if(document_id.equals("")){
						msg = " document not set ";
						return msg;
				}
				if(hour_code_id.equals("")){
						msg = " hour code not set ";
						return msg;
				}
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could do not get connection to DB";
						return msg;
				}
				logger.debug(qq);
				if(date.equals(""))
						date = Helper.getToday();
				try{
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, document_id);
						pstmt.setString(2, block_id);
						pstmt.setString(3, hour_code_id);
						java.util.Date date_tmp = df.parse(date);
						pstmt.setDate(4, new java.sql.Date(date_tmp.getTime()));
						pstmt.setDouble(5, hours);
						date_tmp = df.parse(request_date);
						pstmt.setDate(6, new java.sql.Date(date_tmp.getTime()));						
						if(!request_approval.equals(""))
								pstmt.setString(7, "y");
						else
								pstmt.setNull(7, Types.CHAR);
						if(!action_status.equals(""))
								pstmt.setString(8, action_status);
						else
								pstmt.setNull(8, Types.VARCHAR);
						if(!action_by.equals(""))
								pstmt.setString(9, action_by);
						else
								pstmt.setNull(9, Types.INTEGER);
						if(!action_date.equals("")){
								date_tmp = df.parse(action_date);
								pstmt.setDate(10, new java.sql.Date(date_tmp.getTime()));
						}
						else{
								pstmt.setNull(10, Types.DATE);
						}
						pstmt.setString(11, change_type);
						pstmt.setString(12, change_by);						
						pstmt.executeUpdate();
						Helper.databaseDisconnect(pstmt, rs);
						//						
						pstmt = con.prepareStatement(qq2);
						rs = pstmt.executeQuery();
						if(rs.next()){
								id = rs.getString(1);
						}
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
		//
		// when manager approve or deny
		//
		public String doSelect(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "select t.id,"+
						" t.document_id,"+
						" t.block_id,"+
						" t.hour_code_id,"+
						" date_format(t.date,'%m/%d/%Y'),"+
						
						" t.hours,"+
						" date_format(t.request_date,'%m/%d/%Y'),"+
						" t.request_approval,"+
						" t.action_status,"+
						" t.action_by,"+
						
						" date_format(t.action_date,'%m/%d/%Y'),"+
						" t.change_type,"+
						" date_format(t.change_time,'%m/%d/%Y '),"+						
						" t.change_by "+						
						" from leave_block_logs t "+
						" where t.id=? ";
				logger.debug(qq);
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could do not get connection to DB";
						return msg;
				}
				try{
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, id);
						rs = pstmt.executeQuery();
						if(rs.next()){
								setVals(
												rs.getString(1),
												rs.getString(2),
												rs.getString(3),
												rs.getString(4),
												rs.getString(5),
												
												rs.getDouble(6),
												rs.getString(7),														
												rs.getString(8) != null,
												rs.getString(9),
												rs.getString(10),
												
												rs.getString(11),
												rs.getString(12),
												rs.getString(13),														
												rs.getString(14)														
												);
						}
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

		
}
