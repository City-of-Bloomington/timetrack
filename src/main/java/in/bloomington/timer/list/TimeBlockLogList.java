package in.bloomington.timer.list;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.ArrayList;
import java.util.List;
import java.text.*;
import java.util.Date;
import java.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;

public class TimeBlockLogList{

    static final long serialVersionUID = 3700L;
    static Logger logger = LogManager.getLogger(TimeBlockLogList.class);
    String document_id = "", id="", sortBy=" id desc ";
    List<TimeBlockLog> timeBlockLogs = null;
    public TimeBlockLogList(){
    }
    public TimeBlockLogList(String val){
				setDocument_id(val);
    }
    public void setId (String val){
				if(val != null)
						id = val;
    }		
    public void setDocument_id (String val){
				if(val != null)
						document_id = val;
    }

    public void setSortby(String val){
				if(val != null)
						sortBy = val;
    }
    public List<TimeBlockLog> getTimeBlockLogs(){
				return timeBlockLogs;
    }
    public String doIt(){
				return "";

    }

    //
    // getters
    //
    public String find(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", qw="";
				String qq = "select id,document_id,hour_code_id,earn_code_reason_id,date_format(date,'%m/%d/%Y'),begin_hour,begin_minute,end_hour,end_minute,hours,minutes,amount,clock_in,clock_out,time_block_id,action_type,action_by_id,date_format(action_time,'%m/%d/%y %H:%i') from time_block_logs ";

				if(!document_id.equals("")){
						if(!qw.equals("")) qw += " and ";
						qw += " document_id = ? ";
				}
				if(!qw.equals("")){
						qq += " where "+qw;
				}
				if(!sortBy.equals("")){
						qq += " order by "+sortBy;
				}
				con = UnoConnect.getConnection();
				if(con == null){
						msg = " Could not connect to DB ";
						logger.error(msg);
						return msg;
				}
				logger.debug(qq);
				try{
						pstmt = con.prepareStatement(qq);
						int jj=1;
						if(!document_id.equals("")){
								pstmt.setString(1, document_id);
						}
						rs = pstmt.executeQuery();
						while(rs.next()){
								if(timeBlockLogs == null)
										timeBlockLogs = new ArrayList<>();
								TimeBlockLog one =
										new TimeBlockLog(rs.getString(1),
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
																		 rs.getString(14),
																		 rs.getString(15),
																		 
																		 rs.getString(16),
																		 rs.getString(17),
																		 rs.getString(18));
								timeBlockLogs.add(one);
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

		/**
				select id,document_id,hour_code_id,earn_code_reason_id,date_format(date,'%m/%d/%Y'),begin_hour,begin_minute,end_hour,end_minute,hours,minutes,amount,clock_in,clock_out,time_block_id,action_type,action_by_id,date_format(action_time,'%m/%d/%y %H:%i') from time_block_logs order by id desc                                  into outfile '/var/lib/mysql-files/timetrack_logs.csv'                          fields terminated by ','                                                        enclosed by '"'                                                                 lines terminated by '\n';

				select dayofweek(action_time) day, hour(action_time) hour,count(*) cnt from time_block_logs where action_time > str_to_date('01/01/2019','%m/%d/%Y') group by day, hour                                                                       	into outfile '/var/lib/mysql-files/timetrack_logs.csv'                          fields terminated by ','                                                        lines terminated by '\n';				
				//
				// starting pay period 12/31/2018 so we added one day
				//
				select (dayofyear(action_time)+1)%14+1 day, hour(action_time) hour,count(*) cnt from time_block_logs where action_time >= str_to_date('01/01/2019','%m/%d/%Y') group by day, hour                
				

		 */
}
