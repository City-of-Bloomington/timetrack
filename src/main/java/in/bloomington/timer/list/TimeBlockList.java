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
import java.util.Set;
import java.util.HashSet;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;
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
    DecimalFormat dfn = new DecimalFormat("##0.00");		
    String employee_id = "", pay_period_id="", job_id="";
    String date_from="", date_to="", document_id="", department_id="";
    String date = "";// specific day
    String code = "", code2 = ""; // needed for HAND and planning
    boolean active_only = false, for_today = false, dailyOnly=false,
				clockInOnly = false, hasClockInAndOut = false;
    double total_hours = 0.0, week1_flsa=0.0, week2_flsa=0.0;
    double week1Total = 0, week2Total = 0;
    double week1AmountTotal = 0, week2AmountTotal = 0;		
    String duration = "";
    List<TimeBlock> timeBlocks = null;
    Set<JobType> jobTypeSet = new HashSet<>();
    Hashtable<Integer, List<TimeBlock>> blocks = new Hashtable<>();
    //
    // for 14 days pay period
    //
    Map<Integer, List<TimeBlock>> dailyBlocks = new TreeMap<>();
    Map<Integer, Double> hourCodeTotals = new TreeMap<>();
    Map<String, Double> hourCodeWeek1 = new TreeMap<>();
    Map<String, Double> hourCodeWeek2 = new TreeMap<>();
		//
		Map<Integer, Double> amountCodeTotals = new TreeMap<>();
    Map<String, Double> amountCodeWeek1 = new TreeMap<>();
    Map<String, Double> amountCodeWeek2 = new TreeMap<>();
		
    Map<Integer, Double> usedAccrualTotals = new TreeMap<>();
    HolidayList holidays = null;
    // 		Map<String, Map<Integer, Double>> daily = new TreeMap<>();
    Map<JobType, Map<Integer, Double>> daily = new TreeMap<>();		
    // week 1,2 / hour_code_id /hours
    Map<Integer, Map<Integer, Double>> usedWeeklyAccruals = new TreeMap<>();
    Document document = null;
    List<String> jobNames = null;
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
    // hours duration for clock-in, clock-out 
    public void setDuration(String val){
				if(val != null)
						duration = val;
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
    public Map<Integer, Double> getAmountCodeTotals(){
				return amountCodeTotals;
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
    // total hour codes for week1
    public Map<String, Double> getAmountCodeWeek1(){
				return amountCodeWeek1;
    }
    // total hour codes for week2
    public Map<String, Double> getAmountCodeWeek2(){
				return amountCodeWeek2;
    }		
    public double getWeek1Total(){
				return week1Total;
    }
    public double getWeek2Total(){
				return week2Total;
    }
    public double getWeek1AmountTotal(){
				return week1AmountTotal;
    }
    public double getWeek2AmountTotal(){
				return week2AmountTotal;
    }		
		
    public Map<JobType, Map<Integer, Double>> getDailyDbl(){
				return daily;
    }
    public Map<JobType, Map<Integer, String>> getDaily(){
				Set<JobType> set = daily.keySet();
				Map<JobType, Map<Integer, String>> mapd = new TreeMap<>();
				for(JobType str:set){
						Map<Integer, String> map2 = new TreeMap<>();
						Map<Integer, Double> map = daily.get(str);
						for(int j=0;j<16;j++){ // 8 total week1, 15 total week2
								if(map.containsKey(j)){
										double val = map.get(j);
										map2.put(j, dfn.format(val));
								}
								else{
										map2.put(j, dfn.format("0"));
								}
						}
						mapd.put(str, map2);
				}
				return mapd;
    }		
    public Map<Integer, Map<Integer, Double>> getUsedWeeklyAccruals(){
				return usedWeeklyAccruals;
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
    public Document getDocument(){
				if(!document_id.equals("")){
						Document one = new Document(document_id);
						String back = one.doSelect();
						if(back.equals("")){
								document = one;
						}
				}
				return document;
    }
    // find employee jobs in this pay period
    //
    // normally one job only per document
    //
    private List<String> findJobNames(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="", str2="";
				List<String> jobNames = new ArrayList<>();
				String qq = "select "+
						" distinct ps.name,j.id "+ // job name (position)
						" from positions ps join jobs j on ps.id=j.position_id, "+
						" time_documents d,"+
						" pay_periods p ";
				String qw = "d.id=? and d.job_id=j.id and p.id=d.pay_period_id and ps.inactive is null and j.inactive is null and (j.expire_date is null or "+
						" j.expire_date <= p.end_date) and j.effective_date <= p.start_date ";
				qq = qq +" where "+qw;
				con = UnoConnect.getConnection();
				if(con == null){
						logger.error(msg);
						return null;
				}
				logger.debug(qq);
				try{
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, document_id);
						rs = pstmt.executeQuery();
						while(rs.next()){
								str = rs.getString(1);
								if(!jobNames.contains(str)){
										jobNames.add(str);
								}
								str2 = rs.getString(2);
								JobType one = new JobType(str2, str);
								jobTypeSet.add(one);
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
				return jobNames;
    }
    /**

// modified on 3/6 to include hour code components that
// will simplify building HourCode class since we are going
// to needed more than before

			 
       create or replace view time_blocks_view as                                            select t.id time_block_id,                                                     t.document_id document_id,                                                      t.hour_code_id hour_code_id,                                                    t.date,                                                                         t.begin_hour begin_hour,                                                        t.begin_minute begin_minute,                                                    t.end_hour end_hour,                                                            t.end_minute end_minute,                                                        t.hours hours,                                                                  t.amount amount,                                                                t.clock_in clock_in,                                                            t.clock_out clock_out,                                                          t.inactive inactive,                                                            datediff(t.date,p.start_date) order_id,                                         c.name code_name,                                                               c.description code_description,                                                 c.record_method record_method,                                                  c.accrual_id accrual_id,                                                        c.type code_type,                                                               c.default_monetary_amount,                                                      cf.nw_code nw_code_name,                                                        ps.name job_name,                                                               j.id job_id,                                                                    d.pay_period_id pay_period_id,                                                  d.employee_id employee_id                                                       from time_blocks t                                                              join time_documents d on d.id=t.document_id                                     join pay_periods p on p.id=d.pay_period_id                                      join jobs j on d.job_id=j.id                                                    join positions ps on j.position_id=ps.id                                        join hour_codes c on t.hour_code_id=c.id                                        left join code_cross_ref cf on c.id=cf.code_id ;			 

    */
    //
    /**
     * find using view (old)
     *
		      select time_block_id,
						"document_id,"+
						"hour_code_id,"+
						"date_formatted,"+
						"begin_hour,"+
						"begin_minute,"+
						"end_hour,"+
						"end_minute,"+
						"hours,"+
						"amount,"+
						"clock_in,"+
						"clock_out,"+
						"inactive,"+
						"order_id,"+
						"code_name,"+
						"code_description,"+
						"nw_code_name,"+
						"job_name,"+
						"accrual_id,"+
						"job_id "+
						"from time_blocks_view v ";
		 
		 
    */
    public String find(){

				prepareBlocks();
				prepareHolidays();
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "select "+
						"time_block_id,"+
						"document_id,"+
						"hour_code_id,"+
						"date_format(date,'%m/%d/%Y'),"+
						"begin_hour,"+
						
						"begin_minute,"+
						"end_hour,"+
						"end_minute,"+
						"hours,"+
						"amount,"+
						
						"clock_in,"+
						"clock_out,"+
						"inactive,"+
						"order_id,"+
						"code_name,"+
						
						"code_description,"+
						"record_method,"+
						"accrual_id,"+
						"code_type,"+
						"default_monetary_amount,"+
						
						"nw_code_name,"+
						"job_name,"+
						"job_id, "+
						"pay_period_id,"+
						"employee_id "+
						
						"from time_blocks_view v ";
				String qw = "";
				if(!department_id.equals("")){
						qq += ", department_employees de ";
						if(!qw.equals("")) qw += " and ";								
						qw += "  de.employee_id=v.employee_id and de.department_id=? ";
				}
				if(!pay_period_id.equals("")){
						if(!qw.equals("")) qw += " and ";						
						qw += "pay_period_id=? ";
				}
				if(!document_id.equals("")){
						if(!qw.equals("")) qw += " and ";						
						qw += "document_id=? ";
				}				
				if(!employee_id.equals("")){
						if(!qw.equals("")) qw += " and ";
						qw += "employee_id=? ";
				}
				if(!job_id.equals("")){
						if(!qw.equals("")) qw += " and ";
						qw += "job_id=? ";
				}				
				if(!date_from.equals("")){
						if(!qw.equals("")) qw += " and ";
						qw += "date >= ? ";
				}
				if(!date_to.equals("")){
						if(!qw.equals("")) qw += " and ";
						qw += "date <= ? ";
				}
				if(!date.equals("")){
						if(!qw.equals("")) qw += " and ";
						qw += "date = ? ";
				}
				if(!code.equals("") && !code2.equals("")){
						if(!qw.equals("")) qw += " and ";
						qw += "(code_name like ? or code_name like ?)";
				}
				else if(!code.equals("")){
						if(!qw.equals("")) qw += " and ";
						qw += "code_name like ? ";
				}
				if(clockInOnly){
						if(!qw.equals("")) qw += " and ";
						qw += " clock_in is not null and clock_out is null ";
				}
				else if(hasClockInAndOut){
						if(!qw.equals("")) qw += " and ";
						qw += " clock_in is not null and clock_out is not null ";
				}
				if(active_only){
						if(!qw.equals("")) qw += " and ";
						qw += " inactive is null ";
				}
				if(!qw.equals("")){
						qq += " where "+qw;
				}
				qq += " order by date, begin_hour ";
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
								double hrs = rs.getDouble(9);
								double amnt = rs.getDouble(10);
								int order_id = rs.getInt(14); 
								int code_id = rs.getInt(3);
								String code_name = rs.getString(15); 
								String code_desc = rs.getString(16);
								String record_method = rs.getString(17);
								String related_accrual_id = "";
								str = rs.getString(18);
								if(str != null && !str.equals("")){
										related_accrual_id = str;
								}
								String code_type = rs.getString(19);
								double default_amount = rs.getDouble(20);
								
								String job_name = rs.getString(22); // job name
								String job_id = rs.getString(23);

								String date = rs.getString(4); // formatted date
								boolean isHoliday = isHoliday(date);
								String holidayName = "";
								if(isHoliday){
										holidayName = getHolidayName(date);
								}
								HourCode hrCode = new HourCode(""+code_id,
																							 code_name,
																							 code_desc,
																							 record_method,
																							 related_accrual_id,
																							 code_type,
																							 default_amount);
								if(code_desc == null) code_desc = "";
								if(hrCode.isRecordMethodMonetary()){
										hrs = 0;
										if(amnt == 0.0){
												amnt = hrCode.getDefaultMonetaryAmount();
										}
								}
								else if(hrCode.isCallOut()){
										if(hrs < 3.) hrs = 3;
										amnt = 0;
								}
								if(!dailyOnly){
										if(timeBlocks == null)
												timeBlocks = new ArrayList<>();
										TimeBlock one =
												new TimeBlock(rs.getString(1),
																			rs.getString(2),
																			rs.getString(3), // code_id
																			rs.getString(4), // date
																			rs.getInt(5),  // b
																			
																			rs.getInt(6),
																			rs.getInt(7),
																			rs.getInt(8),
																			hrs,
																			amnt,
																			
																			rs.getString(11), // clock in
																			rs.getString(12), // clock out
																			isHoliday,
																			holidayName,
																			rs.getString(13) != null,
																			
																			rs.getInt(14), // order id
																			code_name,
																			code_desc, 
																			rs.getString(21),  // nw_code
																			job_name, 
																			
																			job_id 
																			);
										one.setHourCode(hrCode); //
										timeBlocks.add(one);
										addToBlocks(order_id, one);
										if(!related_accrual_id.equals("")){
												addToUsedAccruals(order_id, code_id, hrs);
										}
								}
								if(hrCode.isRegular()){
										if(order_id < 7)
												week1_flsa += hrs;
										else
												week2_flsa += hrs;
								}
								code_name += ": "+code_desc;
								JobType jtype = new JobType(job_id, job_name);
								if(hrCode.isRecordMethodMonetary()){
										addToAmountCodeTotals(order_id, code_id, code_name, amnt);
										if(order_id < 7){
												week1AmountTotal += amnt;
										}
										else{
												week2AmountTotal += amnt;
										}
								}
								else{
										addToHourCodeTotals(order_id, code_id, code_name, hrs);
										addToDaily(jtype, order_id, hrs, code_name);
										total_hours += hrs;										
								}
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
     * this method is needed for employee with multiple jobs and find out
     * if they have clocked-in already but no clocked-out
     *
     */
    public String findDocumentForClockInOnly(int time_hr, int time_min){

				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				double dd_time = time_hr+(time_min/60.);
				Double dd_time2 = dd_time+24;
				String qq = "select "+
						" t.document_id "+
						" from time_blocks t "+
						" join time_documents d on d.id=t.document_id "+
						" join pay_periods p on p.id=d.pay_period_id "+
						" where "+
						" t.clock_in is not null and t.clock_out is null "+
						" and t.inactive is null "+
						" and d.pay_period_id=? "+
						" and d.employee_id=? ";
				qq += " and ";						
				qq += " ((("+dd_time+" - (t.begin_hour+t.begin_minute/60.)) between 0 and ? and t.date=?) "+
						"  or "+
						" (("+dd_time2+" - (t.begin_hour+t.begin_minute/60.)) between 0 and ? and t.date=?)) ";
				con = UnoConnect.getConnection();
				if(con == null){
						msg = " Could not connect to DB ";
						logger.error(msg);
						return msg;
				}
				logger.debug(qq);
				try{
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, pay_period_id);
						pstmt.setString(2, employee_id);
						pstmt.setString(3, duration);
						String date = Helper.getToday();
						java.util.Date date_tmp = df.parse(date);
						pstmt.setDate(4, new java.sql.Date(date_tmp.getTime()));
						pstmt.setString(5, duration);						
						String date2 = Helper.getYesterday();
						date_tmp = df.parse(date2);
						pstmt.setDate(6, new java.sql.Date(date_tmp.getTime()));						
						rs = pstmt.executeQuery();
						if(rs.next()){
								document_id = rs.getString(1); 
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
     * find time blocks for clock-in only
     * in today or yesterday with time duration of 12 hrs
     *
     select t.id,t.document_id,t.hour_code_id,date_format(t.date,'%m/%d/%Y'),t.begin_hour,t.begin_minute,t.end_hour,t.end_minute,t.hours,t.clock_in,t.clock_out,((hour(current_time()) + minute(current_time())/60.) - (t.begin_hour+t.begin_minute/60.)) tdif  from time_blocks t,time_documents d,pay_periods p                        where t.document_id=d.id and d.pay_period_id=p.id and d.id=1719                  and p.id = 547                                                                  and t.date >= '2018-12-20' and t.date <= '2018-12-21'                           and ((((hour(current_time()) + minute(current_time())/60.) - (t.begin_hour+t.begin_minute/60.)) <= 12.0                                                         and ((hour(current_time()) + minute(current_time())/60.) - (t.begin_hour+t.begin_minute/60.)) > 0.) or                                                          (((hour(current_time()) + 12 + minute(current_time())/60.) - (t.begin_hour+t.begin_minute/60.)) <= 12.0                                                          and ((hour(current_time()) + 12+ minute(current_time())/60.) - (t.begin_hour+t.begin_minute/60.)) > 0.) or                                                     (((hour(current_time()) + 24 + minute(current_time())/60.) - (t.begin_hour+t.begin_minute/60.)) <= 12.0                                                           and ((hour(current_time()) + 24+ minute(current_time())/60.) - (t.begin_hour+t.begin_minute/60.)) > 0.))                                                       and t.clock_in is not null and t.clock_out is null                              and t.inactive is null 

     select t.id,t.document_id,t.hour_code_id,date_format(t.date,'%m/%d/%Y'),t.begin_hour,t.begin_minute,t.end_hour,t.end_minute,t.hours,t.clock_in,t.clock_out,((hour(current_time()) + minute(current_time())/60.) - (t.begin_hour+t.begin_minute/60.)) tdif,                                                                   ((hour(current_time()) + minute(current_time())/60.) - (t.begin_hour+t.begin_minute/60.)) tdif2,                                                                ((hour(current_time()) + 24 + minute(current_time())/60.) - (t.begin_hour+t.begin_minute/60.)) tdif4                                                               from time_blocks t,time_documents d,pay_periods p                               where t.document_id=d.id and d.pay_period_id=p.id and d.id=1719                  and p.id = 547                                                                  and t.date between '2018-12-20' and '2018-12-21'                            and (                                                                               (((hour(current_time()) + minute(current_time())/60.) - (t.begin_hour+t.begin_minute/60.)) between 0 and 13.0 and t.date = '2018-12-21') or                                                (((hour(current_time()) + 24 + minute(current_time())/60.) - (t.begin_hour+t.begin_minute/60.)) between 0 and 13.0 and t.date='2018-12-20'))                                           and t.clock_in is not null and t.clock_out is null                              and t.inactive is null 
								
		 
    */ 
    public String findTimeBlocksForClockIn(int time_hr, int time_min){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg = "";
				double dd_time = time_hr+time_min/60.;
				double dd_time2 = dd_time+24;
				String qq = "select t.id,"+
						" t.document_id,"+
						"t.hour_code_id,"+
						"date_format(t.date,'%m/%d/%Y'),"+
						"t.begin_hour,"+
						
						"t.begin_minute,"+
						"t.end_hour,"+
						"t.end_minute,"+
						"t.hours,"+
						"t.amount,"+
						
						"t.clock_in,"+
						"t.clock_out"+
							
						" from time_blocks t,time_documents d,pay_periods p ";
				String qw = " t.document_id=d.id and d.pay_period_id=p.id ";
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
						qw += "d.job_id=? ";
				}				
				if(!date_from.equals("")){
						if(!qw.equals("")) qw += " and ";
						qw += "t.date >= ? ";
				}
				if(!date_to.equals("")){
						if(!qw.equals("")) qw += " and ";
						qw += "t.date <= ? ";
				}
				if(clockInOnly){
						if(!qw.equals("")) qw += " and ";
						qw += " t.clock_in is not null and t.clock_out is null ";
				}
				if(!duration.equals("")){
						if(!qw.equals("")) qw += " and ";
						qw += " ((("+dd_time+" - (t.begin_hour+t.begin_minute/60.)) between 0 and ? and t.date=?) "+
						"  or "+
								" (("+dd_time2+" - (t.begin_hour+t.begin_minute/60.)) between 0 and ? and t.date=?)) ";
				}
				if(active_only){
						if(!qw.equals("")) qw += " and ";
						qw += " t.inactive is null ";
				}
				if(!qw.equals("")){
						qq += " where "+qw;
				}
				// qq += " order by t.date, t.begin_hour ";
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
						if(!duration.equals("")){
								pstmt.setString(jj++, duration);
								java.util.Date date_tmp = df.parse(date_to);
								pstmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
								pstmt.setString(jj++, duration);
								date_tmp = df.parse(date_from);
								pstmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));								
						}
						rs = pstmt.executeQuery();
						while(rs.next()){
								if(timeBlocks == null)
										timeBlocks = new ArrayList<>();
								TimeBlock one =
										new TimeBlock(rs.getString(1),
																	rs.getString(2),
																	rs.getString(3),
																	rs.getString(4),
																	rs.getInt(5),
																	
																	rs.getInt(6),
																	rs.getInt(7),
																	rs.getInt(8),
																	rs.getDouble(9),
																	rs.getDouble(10),
																	
																	rs.getString(11),
																	rs.getString(12)
																	);
								if(!timeBlocks.contains(one)){
										timeBlocks.add(one);
								}
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
    public String findUsedAccruals(){

				prepareBlocks();
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", last_date="", // last accrual date
						emp_id = "", end_date=""; // payPeriod end_date
				//
				//
				// we find the last accrual carry over date given employee id
				// and the end date of this pay period
				//
				// modified to handle multiple jobs
				//
				// find total accrual hours used  (PTO and other related
				// hour codes) since last accrual carry over date
				//
				String qq = " select c.accrual_id, sum(t.hours)                                     from time_blocks t,time_documents d,hour_codes c                                 where t.document_id=d.id and t.inactive is null                                 and c.id=t.hour_code_id and c.inactive is null                                  and c.accrual_id is not null                                                    and d.pay_period_id = ? and d.employee_id=?                                     group by c.accrual_id";
				//
				// we are looking for accrual used in this pap period only
				//
				con = UnoConnect.getConnection();
				if(con == null){
						msg = " Could not connect to DB ";
						logger.error(msg);
						return msg;
				}
				logger.debug(qq);
				try{
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, pay_period_id);
						pstmt.setString(2, employee_id);
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
						Helper.databaseDisconnect(pstmt, rs);
						UnoConnect.databaseDisconnect(con);
				}
				return msg;
    }		
    void prepareBlocks(){
				prepareDaily();
    }
    void prepareDaily(){
				if(jobNames == null){
						jobNames = findJobNames();
						if(jobTypeSet.size() > 0){
								for(JobType one:jobTypeSet){
										Map<Integer, Double> map = new TreeMap<>();
										for(int i=0;i<16;i++){
												map.put(i,0.);
										}
										daily.put(one, map);
								}
						}
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
    /**
     * hour codes ids that used accruals distributed
     * for week1 (index 1) or week2 (index 2) 
     */
    void addToUsedAccruals(int order_id, int code_id, double hrs){
				int week_id = 1;
				if(order_id > 6) week_id = 2; 
				if(usedWeeklyAccruals.containsKey(week_id)){
						Map<Integer, Double> map = usedWeeklyAccruals.get(week_id);
						if(map.containsKey(code_id)){
								double ttl_hrs = map.get(code_id);
								ttl_hrs += hrs;
								map.put(code_id, ttl_hrs);
						}
						else{
								map.put(code_id, hrs);
						}
				}
				else{
						Map<Integer, Double> map = new TreeMap<>();
						map.put(code_id, hrs);
						usedWeeklyAccruals.put(week_id, map);		
				}
    }
    //
    void addToDaily(JobType jtype,
										int order_id,
										double hrs,
										String hr_code){
				double total = 0.;
				try{
						if(daily.containsKey(jtype)){
								Map<Integer, Double> map = daily.get(jtype);
								//
								// leaving space for total at index 7
								//
								if(order_id > 6) order_id = order_id + 1;
								if(map != null && map.containsKey(order_id)){
										total = map.get(order_id);
										total += hrs;
										double week_total = 0;
										if(hr_code.indexOf("ONCALL") == -1){
												if(order_id < 7){
														if(map.containsKey(7)){
																week_total = map.get(7)+hrs; // total week1
														}
														else{
																week_total = hrs;
														}
														map.put(7, week_total);
														week1Total = week_total;
												}
												else{
														if(map.containsKey(15)){														
																week_total = map.get(15)+hrs; // total week2
														}
														else{
																week_total = hrs;
														}
														map.put(15, week_total);
														week2Total = week_total;
												}
										}
								}
								map.put(order_id, total);
								daily.put(jtype, map);
						}
						else{ // this is not needed
								Map<Integer, Double> map = new TreeMap<>();
								map.put(order_id, hrs);
								daily.put(jtype, map);
						}
				}catch(Exception ex){
						logger.error(ex);
				}
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
    void addToAmountCodeTotals(int order_id,
														 int hr_code_id,
														 String hr_code,
														 double amount){
				try{
						if(amountCodeTotals.containsKey(hr_code_id)){
								Double val = amountCodeTotals.get(hr_code_id);
								double val2 = val.doubleValue()+amount;
								amountCodeTotals.put(hr_code_id, val2);
								if(order_id < 7){
										if(amountCodeWeek1.containsKey(hr_code)){
												val = amountCodeWeek1.get(hr_code);
												val2 = val.doubleValue()+amount;
												amountCodeWeek1.put(hr_code, val2);
										}
										else{
												amountCodeWeek1.put(hr_code, amount);
										}
								}
								else{
										if(amountCodeWeek2.containsKey(hr_code)){
												val =amountCodeWeek2.get(hr_code);
												val2 = val.doubleValue()+amount;										
												amountCodeWeek2.put(hr_code, val2);
										}
										else{
												amountCodeWeek2.put(hr_code, amount);
										}
								}
						}
						else{
								amountCodeTotals.put(hr_code_id, amount);
								if(order_id < 7){
										amountCodeWeek1.put(hr_code, amount);
								}
								else{
										amountCodeWeek2.put(hr_code, amount);
								}
						}
				}catch(Exception ex){
						logger.error(ex);
				}
    }		
		/*

       create or replace view time_block_sum_view as


			 select concat_ws(' - ',date_format(p.start_date, '%m/%d'), date_format(p.end_date,'%m/%d')) date_range,                                                         concat_ws(', ',e.last_name,e.first_name) full_name,                             ps.name job_name,                                                               c.name hour_code,			                                                         sum(t.hours) hours                                                              from time_blocks t                                                              join hour_codes c on t.hour_code_id=c.id                                        join time_documents d on d.id=t.document_id                                     join pay_periods p on p.id=d.pay_period_id                                      join jobs j on d.job_id=j.id                                                    join positions ps on j.position_id=ps.id                                        join employees e on e.id=d.employee_id                                          join department_employees de on de.employee_id=e.id                             where                                                                           t.inactive is null                                                              and ((t.clock_in is not null and t.clock_out is not null) or (t.clock_in is null and t.clock_out is null))                                                       and de.department_id=1                                                          and p.start_date >= '2019-02-11'                                                and p.end_date <= '2019-02-25'                                                  group by date_range,full_name,job_name,hour_code                                order by date_range,full_name,job_name,hour_code



		 */
		
}
