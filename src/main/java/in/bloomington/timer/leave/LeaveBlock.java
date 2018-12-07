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

public class LeaveBlock{

		static Logger logger = LogManager.getLogger(LeaveBlock.class);
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");		
		static final long serialVersionUID = 250L;		
    String id="",
				document_id="",
				hour_code_id="",
				request_date="", request_approval="", action_status="",
				action_by="", action_date="", inactive="",
				holidayName="";
		//
		int order_index = 0;
		String hour_code_name = "", hour_code_desc="", job_name="";
		String date = ""; // the user pick date, needed for PTO, Holiday etc
		String errors = "";
		double hours = 0.0;
		HourCode hourCode = null;
		JobTask jobTask = null;
		LeaveDocument document = null;
		boolean hourCodeSet = false, isHoliday=false;
		String change_by =""; // employee_id who perform the request or update
		//
    public LeaveBlock(
								 String val,
								 String val2,
								 String val3,
								 String val4,
								 double val5,
								 
								 String val6,
								 boolean val7,
								 String val8,
								 String val9,
								 String val10,
								 
								 boolean val11,
								 int val12,
								 String val13,
								 String val14,
								 String val15
							 ){
				setVals(val, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11);
				setOrderIndex(val12);
				setHourCodeName(val13);
				setHourCodeDesc(val14);
				setJobName(val14);
		}		
    public LeaveBlock(
								 String val,
								 String val2,
								 String val3,
								 String val4,
								 double val5,
								 String val6,
								 boolean val7,
								 String val8,
								 String val9,
								 String val10,
								 boolean val11
							 ){
				setVals(val, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11);
		}
		void setVals(
								 String val,
								 String val2,
								 String val3,
								 String val4,
								 double val5,
								 String val6,
								 boolean val7,
								 String val8,
								 String val9,
								 String val10,
								 boolean val11
							 ){								
				setId(val);
				setDocument_id(val2);
				setHour_code_id(val3);
				setDate(val4);
				setHours(""+val5);
				setRequestDate(val6);
				setRequestApproval(val7);
				setActionStatus(val8);
				setActionBy(val9);
				setActionDate(val10);
				setInactive(val11);
    }
    public LeaveBlock(String val){
				setId(val);
    }
    public LeaveBlock(){
    }		
    //
    // getters
    //
		public String getId(){
				return id;
    }
    public String getDocument_id(){
				return document_id;
    }
    public String getHour_code_id(){
				return hour_code_id;
    }
		// hourCode 
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
		public boolean getInactive(){
				return !inactive.equals("");
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
		public String getHourCodeName(){
				return hour_code_name;
    }
		public String getHourCodeDesc(){
				return hour_code_desc;
    }
    public int getOrderIndex(){
				return order_index;
    }		
		public String getHolidayName(){
				return holidayName;
		}
		public String getJobName(){
				return job_name;
		}		
		public boolean isHoliday(){
				return isHoliday;
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
		public void setHolidayName(String val){
				if(val != null)
						holidayName = val;
    }
		public void setIsHoliday(boolean val){
				if(val)
						isHoliday = val;
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
    public void setInactive(boolean val){ 
				if(val)
						inactive = "y";
    }				
    public void setHours(String val){
				if(val != null && !val.equals("")){
						try{
								hours = Double.parseDouble(val);
						}catch(Exception ex){

						}
				}
    }
		public void setOrderIndex(int val){ 
				order_index = val;
    }
    public void setHourCodeName(String val){ 
				if(val != null)
						hour_code_name = val;
    }
    public void setHourCodeDesc(String val){ 
				if(val != null)
						hour_code_desc = val;
    }		
    public void setJobName(String val){ 
				if(val != null)
						job_name = val;
    }
		public void setChange_by(String val){ 
				if(val != null)
						change_by = val;
    }
		public String toString(){
				return id;
		}
		public boolean equals(Object o) {
				if (o instanceof LeaveBlock) {
						LeaveBlock c = (LeaveBlock) o;
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
				if(!hourCodeSet){
						if(!hour_code_id.equals("")){
								HourCode one = new HourCode(hour_code_id);
								String back = one.doSelect();
								if(back.equals("")){
										hourCode = one;
										hourCodeSet = true;
								}
						}
						else{
								hourCode = new HourCode();
						}
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
				String change_type="Add";
				if(!errors.equals("")){
						return errors;
				}
				String qq = "insert into leave_blocks values(0,?,?,?,now(), ?,?,null,null,null,null) ";
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
						pstmt.setString(2, hour_code_id);
						java.util.Date date_tmp = df.parse(date);
						pstmt.setDate(3, new java.sql.Date(date_tmp.getTime()));
						pstmt.setDouble(4, hours);
						// request_date today
						if(!request_approval.equals(""))
								pstmt.setString(5, "y");
						else
								pstmt.setNull(5, Types.CHAR);
						pstmt.executeUpdate();
						Helper.databaseDisconnect(pstmt, rs);
						//
						pstmt = con.prepareStatement(qq2);
						rs = pstmt.executeQuery();
						if(rs.next()){
								id = rs.getString(1);
						}
						if(change_type.equals("")) change_type="Add";
						request_date = Helper.getToday();

							// need change
						LeaveBlockLog tbl = new LeaveBlockLog(null,
																									document_id,
																									id,
																									hour_code_id,
																									date,
																									
																									hours,
																									request_date,
																									!request_approval.equals(""),
																									null, // action_status
																									null,// action_by
																									
																									null, // action_date
																									change_type,
																									null,
																									change_by);
						msg += tbl.doSave();
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(pstmt, rs);
				}
				return msg;
		}
		//
		// the user may change, hour_code or hours
		//
		public String doPartialUpdate(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String change_type="";
				String qq = "update leave_blocks set hour_code_id=?,hours=?,request_approval=?,inactive=? where id=? ";
				if(id.equals("")){
						msg = " record id not set ";
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
						pstmt.setString(1, hour_code_id);
						pstmt.setDouble(2, hours);
						if(!request_approval.equals(""))
								pstmt.setString(3, "y");
						else
								pstmt.setNull(3, Types.CHAR);
						if(!inactive.equals(""))
								pstmt.setString(4, "y");
						else
								pstmt.setNull(4, Types.CHAR);
						pstmt.setString(5, id);
						pstmt.executeUpdate();
						doSelect();
						if(inactive.equals("")){
								change_type="Update";
						}
						else{
								change_type="Delete";
						}
						LeaveBlockLog tbl = new LeaveBlockLog(null,
																									document_id,
																									id,
																									hour_code_id,
																									date,
																									
																									hours,
																									request_date,
																									!request_approval.equals(""),
																									null, // action_status
																									null,// action_by
																									
																									null, // action_date
																									change_type,
																									null,
																									change_by);
						msg += tbl.doSave();
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(pstmt, rs);
				}
				return msg;						
		}
		//
		// when managers approve or deny
		//
		public String doActionChange(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String change_type="Update";
				String qq = "update leave_blocks set action_status=?,action_by=?,action_date=now() where id=? ";
				if(id.equals("")){
						msg = " record id not set ";
						return msg;
				}
				if(action_status.equals("") || action_by.equals("")){
						msg = "action status or action user not set";
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
						pstmt.setString(1, action_status);
						pstmt.setString(2, action_by);
						pstmt.setString(3, id);
						pstmt.executeUpdate();
						doSelect();
						LeaveBlockLog tbl = new LeaveBlockLog(null,
																									document_id,
																									id,
																									hour_code_id,
																									date,
																									
																									hours,
																									request_date,
																									!request_approval.equals(""),
																									action_status,
																									action_by,
																									
																									action_date,
																									change_type,
																									null,
																									action_by);		
						msg += tbl.doSave();
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(pstmt, rs);
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
						" t.hour_code_id,"+
						" date_format(t.date,'%m/%d/%Y'),"+
						" t.hours,"+
						
						" date_format(t.request_date,'%m/%d/%Y'),"+
						" t.request_approval,"+
						" t.action_status,"+
						" t.action_by,"+
						" date_format(t.action_date,'%m/%d/%Y'),"+
						
						" t.inactive "+
						" from leave_blocks t "+
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
												rs.getDouble(5),
												rs.getString(6),
												rs.getString(7) != null,
												rs.getString(8),
												rs.getString(9),
												rs.getString(10),														
												rs.getString(11) != null
												);
						}
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(pstmt, rs);
				}
				return msg;
		}

		
}
