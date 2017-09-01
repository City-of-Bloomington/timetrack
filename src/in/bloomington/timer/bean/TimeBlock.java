package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Hashtable;
import java.sql.*;
import javax.sql.*;
import java.text.SimpleDateFormat;
import org.apache.log4j.Logger;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;

public class TimeBlock extends Block{

		static Logger logger = Logger.getLogger(TimeBlock.class);
		static final long serialVersionUID = 4000L;		
		private String inactive=""; // for deleted stuff;
		String hour_code = ""; // for showing on jsp
		String action_by_id="", action_type=""; // for logs
		int order_index=0;
		// from the interface
		Map<String, String> accrualBalance = new Hashtable<>();
		// for clock_in
		public TimeBlock(
										 String val,
										 String val2,
										 String val3,
										 String val4,
										 String val5,
										 
										 int val6,
										 int val7,
										 int val8,
										 int val9,
										 double val10,
										 
										 String val11,
										 String val12,
										 String val13
							 ){
				super(val, val2, val3, val4, val5, val6, val7, val8, val9, val10,
							val11, val12, val13);
		}
    public TimeBlock(
							 String val,
							 String val2,
							 String val3,
							 String val4,
							 String val5,
							 int val6,
							 int val7,
							 int val8,
							 int val9,
							 double val10,
							 String val11,
							 String val12,
							 String val13,
							 boolean val14,
							 int val15,
							 String val16
							 ){
				super(val, val2, val3, val4, val5, val6, val7, val8, val9, val10,
							val11, val12, val13);
				setInactive(val14);
				setOrder_index(val15);
				setHour_code(val16);
		}
    public TimeBlock(String val){
				super(val);
    }
    public TimeBlock(){
    }		
    //
    // getters
    //
		public boolean getInactive(){
				return !inactive.equals("");
    }
		public boolean isActive(){
				return inactive.equals("");
    }
		public boolean hasData(){
				return !id.equals("");
		}
		public String getHour_code(){
				return hour_code;
		}
    //
    // setters
    //
		public void setInactive(boolean val){
				if(val)
						inactive = "y";
		}
		public void setAction_by_id(String val){
				if(val != null)
						action_by_id = val;
		}
		public void setHour_code(String val){
				if(val != null)
						hour_code = val;
		}		
		public void setOrder_index(int val){
				order_index = val;
		}
		public void setAction_type(String val){
				if(val != null)
						action_type = val;
		}		
		public void setAccrual_balance(String[] vals){
				if(vals != null){
						for(String str: vals){
								if(str.indexOf("_") > 0){
										String arr[] = str.split("_");
										accrualBalance.put(arr[0],arr[1]);
								}
						}
				}
		}
		// do display in calendar view
		public int getDayInt(){
				if(date.equals("")){
						return 0;
				}
				return Helper.getDayInt(date);
		}
		public void setTime_in(String val){
				if(val != null && !val.equals("")){
						if(val.indexOf(":") > -1){
								String dd[] = val.split(":");
								if(dd != null){
										try{
												begin_hour = Integer.parseInt(dd[0]);
												begin_minute = Integer.parseInt(dd[1]);
										}catch(Exception ex){
												System.err.println(ex);
										}
								}
						}
				}
		}
		public void setTime_out(String val){
				if(val != null && !val.equals("")){
						if(val.indexOf(":") > -1){
								String dd[] = val.split(":");
								if(dd != null){
										try{
												end_hour = Integer.parseInt(dd[0]);
												end_minute = Integer.parseInt(dd[1]);
										}catch(Exception ex){
												System.err.println(ex);
										}
								}
						}
				}
		}
		public String getTime_in(){
				String ret = "";
				if(begin_hour < 10){
						ret = "0";
				}
				ret += begin_hour+":";
				if(begin_minute < 10){
						ret += "0";
				}
				ret += begin_minute;
				return ret;
		}
		public String getTime_out(){
				String ret = "";
				if(end_hour < 10){
						ret = "0";
				}
				ret += end_hour+":";
				if(end_minute < 10){
						ret += "0";
				}
				ret += end_minute;
				return ret;
		}
		public String getTimeInfo(){
				
				String ret = getTime_in();
				if(isClockOut()){
						ret += " - "+getTime_out();
						ret += " ("+getHours()+" hrs)";
				}
				return ret;
		}
		
		public boolean isHourType(){
				getHourCode();
				if(hourCode != null){
						String str = hourCode.getRecord_method();
						return str.equals("Hours");
				}
				return false;// default is Time
		}
		@Override
		public boolean equals(Object o) {
				if (o instanceof TimeBlock) {
						TimeBlock c = (TimeBlock) o;
						if ( this.id.equals(c.getId())) 
								return true;
				}
				return false;
		}
		public String checkWithAvailableBalance(){
				String msg = "";
				if(accrualBalance != null){
						if(accrualBalance.containsKey(hour_code_id)){
								String val = accrualBalance.get(hour_code_id);
								try{
										double aval = Double.parseDouble(val);
										if(hours > aval){
												msg = "Entered value "+hours+" greater than available balance "+aval;
										}
								}catch(Exception ex){}
						}
				}
				return msg;
		}
		public boolean checkIfEndTimeChanged(){
				return (end_hour + end_minute) > 0;
		}
		/**
		 * check if the data entry conflext with existing data on the same
		 * day for Time entry, we do not check for hours entry,
		 * we ignore inactive records
		 select t.id from time_blocks t where t.document_id=2 and t.date = '2017-08-03' and ((13.0 > t.begin_hour+t.begin_minute/60. and 13.0 < t.end_hour+t.end_minute/60.) or (16.0 > t.begin_hour+t.begin_minute/60. and 16.0 < t.end_hour+t.end_minute/60.)) and t.inactive is null;
		 
		 */
		public String checkForConflicts(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg = "";
				if(isHourType()){
						return checkWithAvailableBalance();
				}
				double timeIn = begin_hour+begin_minute/60.;
				double timeOut = end_hour+end_minute/60.;				
				String qq = " select count(*) from time_blocks t "+
						" where t.document_id=? and t.date = ? and t.inactive is null ";
				
				if((clock_in.equals("") && clock_out.equals(""))
					 || (!clock_in.equals("") && !clock_out.equals(""))){				
						qq +=" and ((? > t.begin_hour+t.begin_minute/60. "+ // start in between
								" and ? < t.end_hour+t.end_minute/60.) or "+
						" (? > t.begin_hour+t.begin_minute/60. "+ // end in between
						" and ? < t.end_hour+t.end_minute/60.)) ";
						if(timeOut < timeIn){
								msg = "Time IN is greater than time OUT";
								return msg;
						}						
				}
				else if(!clock_in.equals("")){				
						qq +=" and (? > t.begin_hour+t.begin_minute/60. "+ // start in between
								" and ? < t.end_hour+t.end_minute/60.) ";
				}
				// for updates we would have an id to exclude
				if(!id.equals("")){
						qq += " and t.id <> ? ";
				}
				logger.debug(qq);
				con = Helper.getConnection();				
				if(con == null){
						msg = " Could not connect to DB ";
						return msg;
				}
				try{
						int jj=1;
						pstmt = con.prepareStatement(qq);
						pstmt.setString(jj++, document_id);
						if(date.equals(""))
								date = Helper.getToday();
						java.util.Date date_tmp = df.parse(date);
						pstmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
						pstmt.setDouble(jj++, timeIn);
						pstmt.setDouble(jj++, timeIn);
						if((clock_in.equals("") && clock_out.equals(""))
							 || (!clock_in.equals("") && !clock_out.equals(""))){								
								pstmt.setDouble(jj++, timeOut);
								pstmt.setDouble(jj++, timeOut);
						}
						if(!id.equals("")){
								pstmt.setString(jj++, id);
						}
						rs = pstmt.executeQuery();
						if(rs.next()){
								int cnt = rs.getInt(1);
								if(cnt > 0){
										msg = "Data entry conflict";
								}
						}
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(con, pstmt, rs);
				}						
				return msg;	
		}
		public String doSelect(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "select t.id,t.document_id,t.job_id,t.hour_code_id,date_format(t.date,'%m/%d/%Y'),t.begin_hour,t.begin_minute,t.end_hour,t.end_minute,t.hours,t.ovt_pref,t.clock_in,t.clock_out,t.inactive, datediff(t.date,p.start_date),c.name "+
						" from time_blocks t "+
						" join time_documents d on d.id=t.document_id "+
						" join pay_periods p on p.id=d.pay_period_id "+
						" join hour_codes c on t.hour_code_id=c.id "+
						" where t.id=? ";
				logger.debug(qq);
				try{
						con = Helper.getConnection();
						if(con != null){
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
														rs.getInt(6),
														rs.getInt(7),
														rs.getInt(8),
														rs.getInt(9),
														rs.getDouble(10),
														rs.getString(11),
														rs.getString(12),
														rs.getString(13));
										
										setInactive(rs.getString(14) != null);
										setOrder_index(rs.getInt(15));
										setHour_code(rs.getString(16));
								}
						}
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(con, pstmt, rs);
				}
				return msg;
		}
		//
		// calculate hours for time entry type codes
		// such Reg, Temp, etc
		String prepareTimes(){

				String msg = "";
				if(isHourType()){
						// to put these in the end when ordered by begin_hour, begin_minute
						// this for PTO, Holiday, etc
						if(begin_hour == 0 && begin_minute == 0){
								begin_hour = 23;
								begin_minute = 59;
								end_hour = 23;
								end_minute = 59;
						}
				}
				else {
						hours = (end_hour+end_minute/60.) - (begin_hour+begin_minute/60.);
						if(hours < 0){
								hours = 0.0;
								msg = "Time in is greater than time out";
						}
				}
				return msg;
		}
		public String doSave(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				if(action_type.equals("")) action_type="Add";
				String qq = "insert into time_blocks values(0,?,?,?,?, ?,?,?,?,? ,?,?,?,null) ";
				if((clock_in.equals("") && clock_out.equals(""))
					 || (!clock_in.equals("") && !clock_out.equals(""))){
						msg = prepareTimes();
				}
				if(!msg.equals("")){
						return msg;
				}				
				if(document_id.equals("")){
						msg = " document not set ";
						return msg;
				}
				if(job_id.equals("")){
						msg = " job not set ";
						return msg;
				}
				if(hour_code_id.equals("")){
						msg = " hour code not set ";
						return msg;
				}
				msg = checkForConflicts();
				if(!msg.equals("")){
						return msg;
				}
				logger.debug(qq);
				try{
						con = Helper.getConnection();
						if(con != null){
								pstmt = con.prepareStatement(qq);
								pstmt.setString(1, document_id);
								pstmt.setString(2, job_id);
								pstmt.setString(3, hour_code_id);
								if(date.equals(""))
										date = Helper.getToday();
								java.util.Date date_tmp = df.parse(date);
								pstmt.setDate(4, new java.sql.Date(date_tmp.getTime()));
								pstmt.setInt(5, begin_hour);
								pstmt.setInt(6, begin_minute);
								pstmt.setInt(7, end_hour);
								pstmt.setInt(8, end_minute);
								pstmt.setDouble(9, hours);
								if(ovt_pref.equals(""))
										pstmt.setNull(10,Types.INTEGER);
								else
										pstmt.setString(10, ovt_pref);
								if(clock_in.equals(""))
										pstmt.setNull(11,Types.CHAR);
								else
										pstmt.setString(11, "y");
								if(clock_out.equals(""))
										pstmt.setNull(12,Types.CHAR);
								else
										pstmt.setString(12, "y");								
								pstmt.executeUpdate();
						}
						qq = "select LAST_INSERT_ID()";
						pstmt = con.prepareStatement(qq);
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
						Helper.databaseDisconnect(con, pstmt, rs);
				}
				if(msg.equals("")){
						TimeBlockLog tbl = new TimeBlockLog(null, document_id,
																								job_id, hour_code_id,
																								date,
																								begin_hour, begin_minute,
																								end_hour, end_minute,
																								hours, ovt_pref,
																								clock_in,clock_out,
																								id,
																								action_type,
																								action_by_id,
																								null);
						msg = tbl.doSave();
																								
				}
				// add log here
				return msg;
		}
		//
		// we can update expire date and inactive
		//
		public String doUpdate(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				
				if(isClockIn() && !isClockOut()){
						if(checkIfEndTimeChanged()){ /// for admins who
								setClock_out("y");
								setAction_type("ClockOut");
						}
				}
				if(action_type.equals("")) action_type="Update";
				if((clock_in.equals("") && clock_out.equals(""))
					 || (!clock_in.equals("") && !clock_out.equals(""))){				
						msg = prepareTimes();
				}
				if(!msg.equals("")){
						return msg;
				}
				if(id.equals("")){
						return " id not set ";
				}
				if(job_id.equals("")){
						msg = " job not set ";
						return msg;
				}
				if(hour_code_id.equals("")){
						msg = " hour code not set ";
						return msg;
				}
				msg = checkForConflicts();
				if(!msg.equals("")){
						return msg;
				}
				String qq = "update time_blocks set job_id=?,hour_code_id=?,begin_hour=?,begin_minute=?,end_hour=?,end_minute=?,hours=?,ovt_pref=?,clock_in=?,clock_out=? where id=? ";
				logger.debug(qq);
				try{
						con = Helper.getConnection();
						if(con != null){
								pstmt = con.prepareStatement(qq);
								pstmt.setString(1, job_id);
								pstmt.setString(2, hour_code_id);
								pstmt.setInt(3, begin_hour);
								pstmt.setInt(4, begin_minute);
								pstmt.setInt(5, end_hour);
								pstmt.setInt(6, end_minute);
								pstmt.setDouble(7, hours);
								if(ovt_pref.equals(""))
										pstmt.setNull(8,Types.INTEGER);
								else
										pstmt.setString(8, ovt_pref);
								if(clock_in.equals(""))
										pstmt.setNull(9,Types.CHAR);
								else
										pstmt.setString(9, "y");
								if(clock_out.equals(""))
										pstmt.setNull(10,Types.CHAR);
								else
										pstmt.setString(10, "y");	
								//
								// we do not change inactive here
								// doDelete will take care of it
								pstmt.setString(11, id);
								pstmt.executeUpdate();
						}
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(con, pstmt, rs);
				}
				if(msg.equals("")){
						TimeBlockLog tbl = new TimeBlockLog(null, document_id,
																								job_id, hour_code_id,
																								date,
																								begin_hour, begin_minute,
																								end_hour, end_minute,
																								hours, ovt_pref,
																								clock_in,clock_out,
																								id, action_type,
																								action_by_id,null);
						msg = tbl.doSave();
						msg = doSelect();
				}				
				// add log
				return msg;
		}
		/*
		 * we do not delete record but we turn it inactive
		 */
		public String doDelete(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				action_type = "Delete";
				String qq = "update time_blocks set inactive='y' where id=? ";
				logger.debug(qq);
				try{
						con = Helper.getConnection();
						if(con != null){
								pstmt = con.prepareStatement(qq);
								pstmt.setString(1, id);
								pstmt.executeUpdate();
						}
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(con, pstmt, rs);
				}
				msg = doSelect();
				if(msg.equals("")){
						TimeBlockLog tbl = new TimeBlockLog(null, document_id,
																								job_id, hour_code_id,
																								date,
																								begin_hour, begin_minute,
																								end_hour, end_minute,
																								hours, ovt_pref,
																								clock_in,clock_out,
																								id, action_type,
																								action_by_id,null);
						msg = tbl.doSave();
																								
				}
				// add log here
				return msg;
		}		

}
