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
		Employee action_by = null;
    public TimeBlockLog(
							 String val,
							 String val2,
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
							 String val14,
							 String val15,
							 String val16
							 ){
				super(val, val2, val4, val5, val6, val7, val8, val9, val10, val11, val12);
				setTime_block_id(val13);
				setAction_type(val14);
				setAction_by_id(val15);
				setAction_time(val16);
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
				return isClockIn() || isClockOut();
		}
		public boolean showBeginTime(){
				getHourCode();
				return !(action_type.equals("Delete") || hourCode.isRecordMethodHours());
		}
		public boolean showEndTime(){
				getHourCode();				
				return !(action_type.equals("Delete") || hourCode.isRecordMethodHours() || isClockIn());
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
		public void setAction_time(String val){
				if(val != null)
						action_time = val;
		}		
		public Employee getAction_by(){
				if(action_by == null && !action_by_id.equals("")){
						Employee one = new Employee(action_by_id);
						String back = one.doSelect();
						if(back.equals("")){
								action_by = one;
						}
				}
				return action_by;
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
				String qq = "select id,document_id,hour_code_id,date_format(date,'%m/%d/%Y'),begin_hour,begin_minute,end_hour,end_minute,hours,clock_in,clock_out,time_block_id,action_type,action_by_id,date_format(action_time,'%m/%d/%y %H:%i') from time_block_logs where id =? ";
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
												rs.getInt(5),
												rs.getInt(6),
												rs.getInt(7),
														rs.getInt(8),
												rs.getDouble(9),
												rs.getString(10),
												rs.getString(11));
								setTime_block_id(rs.getString(12));
								setAction_type(rs.getString(13));
								setAction_by_id(rs.getString(14));
								setAction_time(rs.getString(15));
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
		/*
		 * we save only no updates are allowed
		 */ 
		public String doSave(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "insert into time_block_logs values(0,?,?,?, ?,?,?,?,? ,?,?,?,?,?, now()) ";
				if(document_id.equals("")){
						msg = " document not set ";
						return msg;
				}
				if(hour_code_id.equals("")){
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
						pstmt.setString(1, document_id);
						pstmt.setString(2, hour_code_id);
						java.util.Date date_tmp = df.parse(date);
						pstmt.setDate(3, new java.sql.Date(date_tmp.getTime()));
						pstmt.setInt(4, begin_hour);
						pstmt.setInt(5, begin_minute);
						pstmt.setInt(6, end_hour);
						pstmt.setInt(7, end_minute);
						pstmt.setDouble(8, hours);
						if(clock_in.equals(""))
								pstmt.setNull(9, Types.CHAR);
						else
								pstmt.setString(9, "y");
						if(clock_out.equals(""))
								pstmt.setNull(10, Types.CHAR);
						else
								pstmt.setString(10, "y");								
						pstmt.setString(11, time_block_id);
						pstmt.setString(12, action_type);
						pstmt.setString(13, action_by_id);
						pstmt.executeUpdate();
						Helper.databaseDisconnect(pstmt, rs);
						//
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
						Helper.databaseDisconnect(pstmt, rs);
				}
				return msg;
		}

}
