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
import org.apache.log4j.Logger;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;
import in.bloomington.timer.*;


public class TimeBlockLog extends Block{

		static Logger logger = Logger.getLogger(TimeBlockLog.class);
		static final long serialVersionUID = 4100L;		
		String time_block_id="", action_type="", action_by_id="", action_time="";;
		User action_by = null;
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
							 String val11,
							 
							 String val12,
							 String val13,
							 String val14,
							 String val15,
							 String val16,
							 String val17
							 ){
				super(val, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13);
				setTime_block_id(val14);
				setAction_type(val15);
				setAction_by_id(val16);
				setAction_time(val17);
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
		public User getAction_by(){
				if(action_by == null && !action_by_id.equals("")){
						User one = new User(action_by_id);
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
				String qq = "select id,document_id,job_id,hour_code_id,date_format(date,'%m/%d/%Y'),begin_hour,begin_minute,end_hour,end_minute,hours,ovt_pref,clock_in,clock_out,time_block_id,action_type,action_by_id,date_format(action_time,'%m/%d/%Y %H:%i') from block_time_logs where id =? ";
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
														rs.getInt(0),
														rs.getDouble(10),
														rs.getString(11),
														rs.getString(12),
														rs.getString(13));
										setTime_block_id(rs.getString(14));
										setAction_type(rs.getString(15));
										setAction_by_id(rs.getString(16));
										setAction_time(rs.getString(17));
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
		/*
		 * we save only no updates are allowed
		 */ 
		public String doSave(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "insert into time_block_logs values(0,?,?,?,?, ?,?,?,?,? ,?,?,?,?,?,?, now()) ";
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
				logger.debug(qq);
				try{
						con = Helper.getConnection();
						if(con != null){
								pstmt = con.prepareStatement(qq);
								pstmt.setString(1, document_id);
								pstmt.setString(2, job_id);
								pstmt.setString(3, hour_code_id);
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
								pstmt.setString(13, time_block_id);
								pstmt.setString(14, action_type);
								pstmt.setString(15, action_by_id);
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
				return msg;
		}

}
