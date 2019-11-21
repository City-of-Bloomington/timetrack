package in.bloomington.timer.util;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.*;
import java.sql.*;
import javax.sql.*;
import java.text.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class QuartzMisc{

		boolean debug = false;
		static Logger logger = LogManager.getLogger(QuartzMisc.class);
		String type = "accrual"; // accrual, notification, profile
		String prevScheduleDate = "", nextScheduleDate = "";
    public QuartzMisc(boolean deb){
				debug = deb;
    }
    public QuartzMisc(String val){
				if(val != null && !val.equals(""))
						type = val;
    }		
		public String getPrevScheduleDate(){
				return prevScheduleDate;
		}
		public String getNextScheduleDate(){
				return nextScheduleDate;
		}
		/**
		 * extract the previous date and next date for the scheduler that ran and
		 * will be run by the quartz scheduling framework
		 */
		public String findScheduledDates(){
		
				String msg = "";
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String qq = " select from_unixtime(prev_fire_time/1000), from_unixtime(next_fire_time/1000) from qrtz_triggers where job_group like '"+type+"'";
				try{
						con = UnoConnect.getConnection();
						if(con == null){
								msg = "Could not connect to DB";
								System.err.println(" could not connect to DB");
								return msg;
						}
						if(debug){
								System.err.println(qq);
								logger.debug(qq);
						}
						pstmt = con.prepareStatement(qq);
						rs = pstmt.executeQuery();
						if(rs.next()){
								String str = rs.getString(1);
								if(str != null){
										if(str.length() > 16){
												prevScheduleDate = str.substring(0, 16);
										}
										else{
												prevScheduleDate = str;
										}
								}
								str = rs.getString(2);
								if(str != null){
										if(str.length() > 16){										
												nextScheduleDate = str.substring(0, 16);
										}
										else{
												nextScheduleDate = str;
										}
								}
						}
				}catch(Exception ex){
						msg += ex;
						System.err.println(ex);
				}
				finally{
						Helper.databaseDisconnect(pstmt, rs);
						UnoConnect.databaseDisconnect(con);
				}
				return msg;
			
		}
		/**
		 * delete the old record if(any) so that new ones can be added
		 * useful for reruns 
		 */
		public String doClean(){
		
				String msg = "";
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				//
				// we need to delete the old records from the database
				// otherwise we get exception and avoid duplication in scheduling
				//
				String where_trig = " where trigger_group like '"+type+"'";
				String where_job = " where job_group like '"+type+"'";
				String[] qq = {"delete from qrtz_cron_triggers "+where_trig,
											 "delete from qrtz_simple_triggers "+where_trig,
											 "delete from qrtz_fired_triggers "+where_trig,
											 "delete from qrtz_triggers "+where_trig,
											 "delete from qrtz_job_details "+where_job};
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could not connect to DB";
						System.err.println(" could not connect to DB");
						return msg;
				}
				try{
						for(String str:qq){
								if(debug){
										System.err.println(str);
										logger.debug(str);
								}
								pstmt = con.prepareStatement(str);
								pstmt.executeUpdate();
								Helper.databaseDisconnect(pstmt, rs);
						}
				}catch(Exception ex){
						msg += ex;
				}
				finally{
						Helper.databaseDisconnect(pstmt, rs);
						UnoConnect.databaseDisconnect(con);
				}
				return msg;
		}

	
}
