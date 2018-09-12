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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;

public class TimeBlockList{

		boolean debug = false;
		static final long serialVersionUID = 4200L;
		static Logger logger = LogManager.getLogger(TimeBlockList.class);
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");		
		String employee_id = "", pay_period_id="", job_id="";
		String date_from="", date_to="", document_id="", department_id="";
		String date = "";// specific day
		String code = "", code2 = ""; // needed for HAND and planning
		boolean active_only = false, for_today = false, dailyOnly=false,
				clockInOnly = false, hasClockInAndOut = false;
		double total_hours = 0.0, week1_flsa=0.0, week2_flsa=0.0;
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
		Map<Integer, Double> usedAccrualTotals = new TreeMap<>();
		HolidayList holidays = null;
		
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
    public void setDepartment_id(String val){
				if(val != null)
						department_id = val;
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
    public void setCode(String val){
				if(val != null)
						code = val;
    }
    public void setCode2(String val){
				if(val != null)
						code2 = val;
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
		public Map<Integer, Double> getUsedAccrualTotals(){
				return usedAccrualTotals;
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
		public double getWeek1_flsa(){
				return week1_flsa;
		}
		public double getWeek2_flsa(){
				return week2_flsa;
		}
		public boolean isHoliday(String date){
				if(holidays != null){
						return holidays.isHoliday(date);
				}
				return false;
		}
		public String getHolidayName(String date){
				if(holidays != null){
						return holidays.getHolidayName(date);
				}
				return "";
		}
    //
    // getters
    //
		public String find(){

				prepareBlocks();
				prepareHolidays();
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
						
						"t.clock_in,"+
						"t.clock_out,"+
						"t.inactive,"+
						" datediff(t.date,p.start_date), "+ // order id start at 0
						" c.name,"+
						
						" c.description,"+
						" cf.nw_code "+
						
						" from time_blocks t "+
						" join time_documents d on d.id=t.document_id "+
						" join pay_periods p on p.id=d.pay_period_id "+
						" join hour_codes c on t.hour_code_id=c.id "+
						" left join code_cross_ref cf on c.id=cf.code_id ";
				String qw = "";
				if(!department_id.equals("")){
						qq += ", department_employees de ";
						if(!qw.equals("")) qw += " and ";								
						qw += "  de.employee_id=d.employee_id and de.department_id=? ";
				}
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
				if(!code.equals("") && !code2.equals("")){
						if(!qw.equals("")) qw += " and ";
						qw += "(c.name like ? or c.name like ?)";
				}
				else if(!code.equals("")){
						if(!qw.equals("")) qw += " and ";
						qw += "c.name like ? ";
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
						if(!department_id.equals("")){
								pstmt.setString(jj++, department_id);
						}										
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
						if(!code.equals("") && !code2.equals("")){
								pstmt.setString(jj++, code);
								pstmt.setString(jj++, code2);								
						}
						else if(!code.equals("")){
								pstmt.setString(jj++, code);
						}						
						rs = pstmt.executeQuery();
						while(rs.next()){
								double hrs = rs.getDouble(10);
								int order_id = rs.getInt(14); // 15
								int hr_code_id = rs.getInt(4);
								String hr_code = rs.getString(15); //16
								String hr_code_desc = rs.getString(16); // 17
								String date = rs.getString(5);
								boolean isHoliday = isHoliday(date);
								String holidayName = "";
								if(isHoliday){
										holidayName = getHolidayName(date);
								}
								if(hr_code_desc == null) hr_code_desc = "";
								if(hr_code != null){
										if(hr_code.indexOf("ONCALL") > -1){ // oncall35 id=17
												hrs = 1.0;
										}
										else if(hr_code.indexOf("CO") > -1){ // Call Out id=16
												if(hrs < 3.) hrs = 3;
										}
								}								
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
																			isHoliday,
																			holidayName,
																			rs.getString(13) != null,
																			rs.getInt(14),
																			hr_code,
																			rs.getString(16),
																			rs.getString(17)
																			);
										timeBlocks.add(one);
										addToBlocks(order_id, one);										
								}
								if(hr_code.toLowerCase().indexOf("reg") > -1){
										if(order_id < 7)
												week1_flsa += hrs;
										else
												week2_flsa += hrs;
								}
								// modified hr_code
								hr_code += ": "+hr_code_desc;
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
		/**
			 select t.hour_code_id, sum(t.hours)                                             from time_blocks t,time_documents d                                             where t.document_id=d.id and t.inactive is null and                             t.date >= '2018-08-27' and d.employee_id=10                                      and t.date <= (select end_date from pay_periods p,time_documents d              where p.id=d.pay_period_id and d.id=310) and t.hour_code_id in                   (select id from hour_codes where accrual_id is not null)                        group by t.hour_code_id;
 
		 */
		public String findUsedAccruals(){

				prepareBlocks();
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", last_date="", // last accrual date
						emp_id = "", end_date=""; // payPeriod end_date
				//
				// we assume all employee accruals are updated the day before
				// any payperiod therefore we are concerned by the current
				// pay period end date
				//
				// we find the last accrual carry over date and employee id
				// given document id and the end date of this pay period
				//
				String qq = " ";
				qq = " select a.date,a.employee_id,p.end_date from employee_accruals a,                pay_periods p, time_documents d                                                 where d.employee_id=a.employee_id                                               and p.id = d.pay_period_id and a.date < p.start_date                            and d.id = ? order by a.date desc limit 1 ";
				//
				// find total accrual hours used  (PTO and other related
				// hour codes) since last accrual carry over date
				//
				String qq2 = " select c.accrual_id, sum(t.hours)                                     from time_blocks t,time_documents d,hour_codes c                                 where t.document_id=d.id and t.inactive is null                                 and c.id=t.hour_code_id and c.inactive is null                                  and c.accrual_id is not null                                                    and t.date >= ? and d.employee_id=?                                             and t.date <= ? group by c.accrual_id";
				
				/**
					 //
					 // this addition will cause to add all the accrual used since
					 // the last update, this could mean two pay period hours
					 //
					 (select p.end_date from pay_periods p,                                          time_documents d2 where p.id=d2.pay_period_id and d2.id=d.id)                   and t.hour_code_id in (select id from hour_codes where                          accrual_id is not null) group by c.accrual_id ";
       */				
				con = Helper.getConnection();
				if(con == null){
						msg = " Could not connect to DB ";
						logger.error(msg);
						return msg;
				}
				logger.debug(qq);
				try{
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, document_id);
						rs = pstmt.executeQuery();
						if(rs.next()){
								last_date = rs.getString(1); // accrual date
								emp_id = rs.getString(2);
								end_date = rs.getString(3); // end of pay period
						}
						qq = qq2;
						logger.debug(qq);
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, last_date);
						pstmt.setString(2, emp_id);
						pstmt.setString(3, end_date);						
						rs = pstmt.executeQuery();
						while(rs.next()){
								int code_id = rs.getInt(1); // accrual_id now
								double hrs = rs.getDouble(2);
								usedAccrualTotals.put(code_id, hrs);
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
		void prepareBlocks(){
				for(int i=0;i<14;i++){
						daily.put(i,0.);
				}
		}
		void prepareHolidays(){
				HolidayList hl = new HolidayList(debug);
				if(!pay_period_id.equals("")){
						hl.setPay_period_id(pay_period_id);
				}
				else{
						if(!date_from.equals(""))
								hl.setDate_from(date_from);
						if(!date_to.equals(""))
								hl.setDate_to(date_to);
				}
				String back = hl.find();
				if(back.equals("")){
						holidays = hl;
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
