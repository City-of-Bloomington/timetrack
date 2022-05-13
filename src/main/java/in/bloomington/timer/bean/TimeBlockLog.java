package in.bloomington.timer.bean;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;
import in.bloomington.timer.*;


public class TimeBlockLog extends Block{

		static Logger logger = LogManager.getLogger(TimeBlockLog.class);
		static final long serialVersionUID = 4100L;		
		String time_block_id="", action_type="", action_by_id="", action_time="";;
		String location_id="";
		Location location = null;
		Employee action_by = null;
    public TimeBlockLog(
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

							 int val11,
							 double val12,
							 String val13,
							 String val14,
							 
							 String val15,
							 String val16,
							 
							 String val17,
							 String val18,
							 String val19
							 ){
				super(val, val2, val3,
							val4,
							val5, val6, val7, val8, val9, val10, val11, val12, val13, val14);
				setTime_block_id(val15);
				setAction_type(val16);
				setAction_by_id(val17);
				setAction_time(val18);
				setLocation_id(val19);
		}
    public TimeBlockLog(String val){
				super(val);
    }
    public TimeBlockLog(){
    }		
    //
    // getters
    //
		public String getTime_block_id(){
				return time_block_id;
    }
		public String getAction_type(){
				return action_type;
    }
		public String getAction_by_id(){
				return action_by_id;
    }		
		public String getAction_time(){
				return action_time;
    }
		public boolean isClockInOut(){
				return isClockIn() && isClockOut();
		}
		public boolean isClockInOnly(){
				return isClockIn() && !isClockOut();
		}		
		public boolean showBeginTime(){
				getHourCode();
				return !(action_type.equals("Delete") ||
								 hourCode.isRecordMethodMonetary() ||
								 hourCode.isRecordMethodHours());
		}
		public boolean showEndTime(){
				getHourCode();
				return !(action_type.equals("Delete") ||
								 hourCode.isRecordMethodMonetary() ||
								 hourCode.isRecordMethodHours() || isClockInOnly())
						|| isClockInOut();
		}		
    //
    // setters
    //
		public void setTime_block_id(String val){
				if(val != null)
						time_block_id = val;
		}
		public void setAction_type(String val){
				if(val != null)
						action_type = val;
		}
		public void setAction_by_id(String val){
				if(val != null)
						action_by_id = val;
		}
		public void setLocation_id(String val){
				if(val != null)
						location_id = val;
		}		
		public void setAction_time(String val){
				if(val != null)
						action_time = val;
		}
		public boolean hasLocation(){
				return !location_id.isEmpty();
		}
		public Employee getAction_by(){
				if(action_by == null && !action_by_id.isEmpty()){
						Employee one = new Employee(action_by_id);
						String back = one.doSelect();
						if(back.isEmpty()){
								action_by = one;
						}
				}
				return action_by;
		}
		public Location getLocation(){
				if(location == null && !location_id.isEmpty()){
						Location one = new Location(location_id);
						String back = one.doSelect();
						if(back.isEmpty()){
								location = one;
						}
				}
				return location;
		}
		@Override
		public boolean equals(Object o) {
				if (o instanceof TimeBlockLog) {
						TimeBlockLog c = (TimeBlockLog) o;
						if ( this.id.equals(c.getId())) 
								return true;
				}
				return false;
		}
		public String doSelect(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "select id,document_id,hour_code_id,earn_code_reason_id,date_format(date,'%m/%d/%Y'),begin_hour,begin_minute,end_hour,end_minute,hours,minutes,amount,clock_in,clock_out,time_block_id,action_type,action_by_id,date_format(action_time,'%m/%d/%y %H:%i'),location_id from time_block_logs where id =? ";
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
												rs.getInt(6),
												rs.getInt(7),
												rs.getInt(8),
												rs.getInt(9),
												rs.getDouble(10),
												rs.getInt(11),
												rs.getDouble(12),
												rs.getString(13),
												rs.getString(14));
								setTime_block_id(rs.getString(15));
								setAction_type(rs.getString(16));
								setAction_by_id(rs.getString(17));
								setAction_time(rs.getString(18));
								setLocation_id(rs.getString(19));
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
		/*
		 * we save only no updates are allowed
		 */ 
		public String doSave(){
				//
				Connection con = null;
				PreparedStatement pstmt = null, pstmt2=null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "insert into time_block_logs values(0,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,now(),?) ";
				if(document_id.isEmpty()){
						msg = " document not set ";
						return msg;
				}
				if(hour_code_id.isEmpty()){
						msg = " hour code not set ";
						return msg;
				}				
				logger.debug(qq);
				con = Helper.getConnection();
				if(con == null){
						msg = "Could do not get connection to DB";
						return msg;
				}							
				try{
						pstmt = con.prepareStatement(qq);
						int jj=1;
						pstmt.setString(jj++, document_id);
						pstmt.setString(jj++, hour_code_id);
						if(earn_code_reason_id.isEmpty())
								pstmt.setNull(jj++, Types.INTEGER);
						else
								pstmt.setString(jj++, earn_code_reason_id);						
						java.util.Date date_tmp = df.parse(date);
						pstmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
						pstmt.setInt(jj++, begin_hour);
						
						pstmt.setInt(jj++, begin_minute);
						pstmt.setInt(jj++, end_hour);
						pstmt.setInt(jj++, end_minute);
						pstmt.setDouble(jj++, hours);
						pstmt.setInt(jj++, minutes);
						
						pstmt.setDouble(jj++, amount);						
						if(clock_in.isEmpty())
								pstmt.setNull(jj++, Types.CHAR);
						else
								pstmt.setString(jj++, "y");
						if(clock_out.isEmpty())
								pstmt.setNull(jj++, Types.CHAR);
						else
								pstmt.setString(jj++, "y");								
						pstmt.setString(jj++, time_block_id);
						pstmt.setString(jj++, action_type);
						
						pstmt.setString(jj++, action_by_id);
						if(location_id.isEmpty())
								pstmt.setNull(jj++, Types.VARCHAR);
						else
								pstmt.setString(jj++, location_id);						
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
						// UnoConnect.databaseDisconnect(con);
						Helper.databaseDisconnect(con);						
				}
				return msg;
		}

}
