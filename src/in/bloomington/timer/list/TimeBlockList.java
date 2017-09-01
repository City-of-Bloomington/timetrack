package in.bloomington.timer.list;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Hashtable;
import java.util.TreeMap;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.*;
import org.apache.log4j.Logger;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;

public class TimeBlockList{

		static final long serialVersionUID = 4200L;
		static Logger logger = Logger.getLogger(TimeBlockList.class);
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");		
		String employee_id = "", pay_period_id="", job_id="";
		String date_from="", date_to="", document_id="";
		String date = "";// specific day
		boolean active_only = false, for_today = false, dailyOnly=false,
				clockInOnly = false, hasClockInAndOut = false;
		double total_hours = 0.0;
		List<TimeBlock> timeBlocks = null;
		Hashtable<Integer, List<TimeBlock>> blocks = new Hashtable<>();
		//
		// for 14 days pay period
		//
		Map<Integer, List<TimeBlock>> dailyBlocks = new TreeMap<>();
		Map<Integer, Double> hourCodeTotals = new TreeMap<>();
		Map<Integer, Double> daily = new TreeMap<>();
		Map<String, Double> hourCodeWeek1 = new TreeMap<>();
		Map<String, Double> hourCodeWeek2 = new TreeMap<>();
    public TimeBlockList(){
    }
    public TimeBlockList(String val){
				setEmployee_id(val);
    }
    public void setDocument_id (String val){
				if(val != null)
						document_id = val;
    }		
    public void setEmployee_id (String val){
				if(val != null)
						employee_id = val;
    }
    public void setJob_id (String val){
				if(val != null)
						job_id = val;
    }		
    public void setPay_period_id (String val){
				if(val != null)
						pay_period_id = val;
    }
    public void setDate_from (String val){
				if(val != null)
						date_from = val;
    }
    public void setDate_to (String val){
				if(val != null)
						date_to = val;
    }
    public void setDate(String val){
				if(val != null)
						date = val;
    }		
		public String getEmployee_id(){
				return employee_id;
		}
		public String getPay_period_id(){
				return pay_period_id;
		}
		public String getDate_from(){
				return date_from;
		}
		public String getDate_to(){
				return date_to;
		}
		public String getDate(){
				return date;
		}		
		public void setActiveOnly(){
				active_only = true;
		}
		public void setForToday(){
				for_today = true;
		}
		public void setDailyOnly(){
				dailyOnly = true;
		}
		public void hasClockInOnly(){
				clockInOnly = true;
		}
		public void hasClockInAndOut(){
				hasClockInAndOut = true;
		}
		public List<TimeBlock> getTimeBlocks(){
				return timeBlocks;
		}
		public Map<Integer, List<TimeBlock>> getDailyBlocks(){
				return dailyBlocks;
		}
		public Map<Integer, Double> getHourCodeTotals(){
				return hourCodeTotals;
		}
		// total hour codes for week1
		public Map<String, Double> getHourCodeWeek1(){
				return hourCodeWeek1;
		}
		// total hour codes for week2
		public Map<String, Double> getHourCodeWeek2(){
				return hourCodeWeek2;
		}		
		public Map<Integer, Double> getDaily(){
				return daily;
		}
		public double getTotal_hours(){
				return total_hours;
		}
    //
    // getters
    //
		public String find(){

				parepareBlocks();
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "select t.id,"+
						" t.document_id,"+
						"t.job_id,"+
						"t.hour_code_id,"+
						"date_format(t.date,'%m/%d/%Y'),"+
						
						"t.begin_hour,"+
						"t.begin_minute,"+
						"t.end_hour,"+
						"t.end_minute,"+
						"t.hours,"+
						
						"t.ovt_pref,"+
						"t.clock_in,"+
						"t.clock_out,"+
						"t.inactive,"+
						" datediff(t.date,p.start_date), "+
						" c.name "+
						" from time_blocks t "+
						" join time_documents d on d.id=t.document_id "+
						" join pay_periods p on p.id=d.pay_period_id "+
						" join hour_codes c on t.hour_code_id=c.id ";
				String qw = "";
				if(!pay_period_id.equals("")){
						if(!qw.equals("")) qw += " and ";						
						qw += "d.pay_period_id=? ";
				}
				if(!document_id.equals("")){
						if(!qw.equals("")) qw += " and ";						
						qw += "t.document_id=? ";
				}				
				if(!employee_id.equals("")){
						if(!qw.equals("")) qw += " and ";
						qw += "d.employee_id=? ";
				}
				if(!job_id.equals("")){
						if(!qw.equals("")) qw += " and ";
						qw += "t.job_id=? ";
				}				
				if(!date_from.equals("")){
						if(!qw.equals("")) qw += " and ";
						qw += "t.date >= ? ";
				}
				if(!date_to.equals("")){
						if(!qw.equals("")) qw += " and ";
						qw += "t.date <= ? ";
				}
				if(!date.equals("")){
						if(!qw.equals("")) qw += " and ";
						qw += "t.date = ? ";
				}
				if(clockInOnly){
						if(!qw.equals("")) qw += " and ";
						qw += " t.clock_in is not null and t.clock_out is null ";
				}
				else if(hasClockInAndOut){
						if(!qw.equals("")) qw += " and ";
						qw += " t.clock_in is not null and t.clock_out is not null ";
				}
				if(active_only){
						if(!qw.equals("")) qw += " and ";
						qw += " t.inactive is null ";
				}
				if(!qw.equals("")){
						qq += " where "+qw;
				}
				qq += " order by t.date, t.begin_hour ";
				con = Helper.getConnection();
				if(con == null){
						msg = " Could not connect to DB ";
						logger.error(msg);
						return msg;
				}
				logger.debug(qq);
				try{
						pstmt = con.prepareStatement(qq);
						int jj=1;
						if(!pay_period_id.equals("")){
								pstmt.setString(jj++, pay_period_id);
						}
						if(!document_id.equals("")){
								pstmt.setString(jj++, document_id);
						}						
						if(!employee_id.equals("")){
								pstmt.setString(jj++, employee_id);
						}
						if(!job_id.equals("")){
								pstmt.setString(jj++, job_id);
						}						
						if(!date_from.equals("")){
								java.util.Date date_tmp = df.parse(date_from);
								pstmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
						}
						if(!date_to.equals("")){
								java.util.Date date_tmp = df.parse(date_to);
								pstmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
						}
						if(!date.equals("")){
								java.util.Date date_tmp = df.parse(date);
								pstmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
						}									
						rs = pstmt.executeQuery();
						while(rs.next()){
								double hrs = rs.getDouble(10);
								int order_id = rs.getInt(15);
								int hr_code_id = rs.getInt(4);
								String hr_code = rs.getString(16);
								if(!dailyOnly){
										if(timeBlocks == null)
												timeBlocks = new ArrayList<>();
										TimeBlock one =
												new TimeBlock(rs.getString(1),
																			rs.getString(2),
																			rs.getString(3),
																			rs.getString(4),
																			rs.getString(5),
																			rs.getInt(6),
																			rs.getInt(7),
																			rs.getInt(8),
																			rs.getInt(9),
																			hrs,
																			rs.getString(11),
																			rs.getString(12),
																			rs.getString(13),
																			rs.getString(14) != null,
																			rs.getInt(15),
																			hr_code
																			);
										timeBlocks.add(one);
										addToBlocks(order_id, one);										
								}
								addToHourCodeTotals(order_id, hr_code_id, hr_code, hrs);
								total_hours += hrs;
								addToDaily(order_id, hrs);
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
		void parepareBlocks(){
				for(int i=0;i<14;i++){
						daily.put(i,0.);
				}
		}
		void addToDaily(int order_id, double hrs){
				double total = 0.;
				if(daily.containsKey(order_id)){
						total = daily.get(order_id);
						total += hrs;
				}
				daily.put(order_id, total);
				
		}		
		void addToBlocks(int order_id, TimeBlock block){
				List<TimeBlock> list = dailyBlocks.get(order_id);
				if(list != null){
						list.add(block);
				}
				else{
						list = new ArrayList<TimeBlock>();
						list.add(block);
				}
				dailyBlocks.put(order_id, list);
		}
		//
		// aggregate hour code for each week and total for payperiod
		//
		void addToHourCodeTotals(int order_id,
														 int hr_code_id,
														 String hr_code,
														 double hrs){
				if(hourCodeTotals.containsKey(hr_code_id)){
						Double val = hourCodeTotals.get(hr_code_id);
						double val2 = val.doubleValue()+hrs;
						hourCodeTotals.put(hr_code_id, val2);
						if(order_id < 7){
								if(hourCodeWeek1.containsKey(hr_code)){
										val = hourCodeWeek1.get(hr_code);
										val2 = val.doubleValue()+hrs;										
										hourCodeWeek1.put(hr_code, val2);
								}
								else{
										hourCodeWeek1.put(hr_code, hrs);
								}
						}
						else{
								if(hourCodeWeek2.containsKey(hr_code)){
										val = hourCodeWeek2.get(hr_code);
										val2 = val.doubleValue()+hrs;										
										hourCodeWeek2.put(hr_code, val2);
								}
								else{
										hourCodeWeek2.put(hr_code, hrs);
								}
						}
				}
				else{
						hourCodeTotals.put(hr_code_id, hrs);
						if(order_id < 7){
								hourCodeWeek1.put(hr_code, hrs);
						}
						else{
								hourCodeWeek2.put(hr_code, hrs);
						}
				}
		}
		
}
