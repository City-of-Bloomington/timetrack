package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.sql.*;
import javax.sql.*;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * this class is needed for employees who have multiple jobs
 *
 */
public class MultiJobDoc{

		boolean debug = false;
		static Logger logger = LogManager.getLogger(MultiJobDoc.class);
		static final long serialVersionUID = 2400L;
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		DecimalFormat dfn = new DecimalFormat("##0.00");
    private String employee_id="", pay_period_id="";
		private String job_id="";
		double week1Total = 0, week2Total = 0, week1_flsa=0, week2_flsa=0;
		PayPeriod payPeriod = null;
		Employee employee = null;
		JobTask job = null; // we need one of the jobs to get more info
		Document document = null; // we need one to get the accruals 
		// Employee initiater = null;
		Workflow lastWorkflow = null;
		Map<String, Map<Integer, String>> daily = null;				
		List<TimeBlock> timeBlocks = null;
		List<String> warnings = new ArrayList<>();
		List<JobTask> jobs = null;
		List<Document> documents = null;

		SalaryGroup salaryGroup = null;
		Map<String, List<String>> allAccruals = new TreeMap<>();
		Map<Integer, Double> hourCodeTotals = null;
		Map<Integer, Double> usedAccrualTotals = null;
		Map<String, Double> hourCodeWeek1 = null;
		Map<String, Double> hourCodeWeek2 = null;
		Map<Integer, List<TimeBlock>> dailyBlocks = null;
		List<EmployeeAccrual> employeeAccruals = null;
		List<TimeNote> timeNotes = null;
		List<TimeIssue> timeIssues = null;
		List<Employee> nextActioners = null;
		Map<String, AccrualWarning> warningMap = new TreeMap<>();
		HolidayList holidays = null;
    public MultiJobDoc(String val,
											 String val2
											 ){
				setEmployee_id(val);				
				setPay_period_id(val2);
				findDocuments();
				// fillWarningMap();
    }
    public MultiJobDoc(){
    }
		void findDocuments(){
				if(documents == null &&
					 !(employee_id.equals("") || pay_period_id.equals(""))){
						DocumentList dl = new DocumentList(employee_id);
						dl.setPay_period_id(pay_period_id);
						String back = dl.find();
						if(back.equals("")){
								List<Document> ones = dl.getDocuments();
								if(ones != null && ones.size() > 0){
										documents = ones;
										for(Document one:documents){
												one.prepareDaily();
										}
										document = ones.get(0); // any one will do
								}
						}
				}
		}
		public boolean hasDocuments(){
				findDocuments();
				return documents != null && documents.size() > 0;
		}
				
    //
    // getters
    //
    public String getEmployee_id(){
				return employee_id;
    }
    public String getPay_period_id(){
				return pay_period_id;
    }
    //
    // setters
    //
    public void setPay_period_id (String val){
				if(val != null && !val.equals("-1"))
						pay_period_id = val;
    }
    public void setEmployee_id(String val){
				if(val != null && !val.equals("-1"))						
						employee_id = val;
    }
		public String toString(){
				return employee_id+" "+pay_period_id;
		}

		public boolean equals(Object o) {
				if (o instanceof MultiJobDoc) {
						MultiJobDoc c = (MultiJobDoc) o;
						return (this.employee_id.equals(c.getEmployee_id()))
								&& this.pay_period_id.equals(c.getPay_period_id());
				}
				return false;				
		}
		public int hashCode(){
				int seed = 23;
				if(!employee_id.equals("")){
						try{
								seed += Integer.parseInt(employee_id)*31;
								
						}catch(Exception ex){
						}
				}
				if(!pay_period_id.equals("")){
						try{
								seed += Integer.parseInt(pay_period_id)*37;
								
						}catch(Exception ex){
						}
				}				
				return seed;
		}
		public PayPeriod getPayPeriod(){
				if(!pay_period_id.equals("") && payPeriod == null){
						PayPeriod one = new PayPeriod(pay_period_id);
						String back = one.doSelect();
						if(back.equals("")){
								payPeriod = one;
						}
				}
				return payPeriod;
		}				
		public Employee getEmployee(){
				if(!employee_id.equals("") && employee == null){
						Employee one = new Employee(employee_id);
						String back = one.doSelect();
						if(back.equals("")){
								employee = one;
						}
				}
				return employee;
		}

		public List<JobTask> getJobs(){
				if(!employee_id.equals("") && !pay_period_id.equals("")){
						JobTaskList jl = new JobTaskList(employee_id, pay_period_id);
						String back = jl.find();
						if(back.equals("")){
								List<JobTask> ones = jl.getJobs();
								if(ones != null && ones.size() > 0){
										jobs = ones;
										for(JobTask one:jobs){
												if(one.isPrimary()){
														job = one;
														break;
												}
										}
										if(job == null){ // if no primary set, any one will do
												job = jobs.get(0); 
										}
								}
						}
				}
				return jobs;
		}
		public boolean hasMultipleJobs(){
				getJobs();
				return jobs != null && jobs.size() > 0;
		}
/*
		private void fillWarningMap(){
				AccrualWarningList tl = new AccrualWarningList();
				String back = tl.find();
				if(back.equals("")){
						List<AccrualWarning> ones = tl.getAccrualWarnings();
						if(ones != null && ones.size() > 0){
								for(AccrualWarning one:ones){
										String str = one.getHourCode().getName();
										String str2 = one.getHourCode().getDescription();
										if(str == null) continue;
										if(str2 == null) str2 = "";
										str += ": "+str2;
										warningMap.put(str, one);
								}
						}
				}
		}
*/

		public String getWeek1_flsa(){
				return ""+dfn.format(week1_flsa);
		}
		public String getWeek2_flsa(){
				return ""+dfn.format(week2_flsa);
		}						
		public boolean hasDaily(){
				// prepareDaily();
				return daily != null && daily.size() > 0;
		}
		public void prepareDaily(boolean includeEmptyBlocks){
				if(daily == null){
						if(documents == null)
								findDocuments();
						if(documents != null && documents.size() > 0){
								for(Document doc:documents){
										doc.prepareDaily(true);
										// daily
										Map<String, Map<Integer, String>> ones = doc.getDaily();
										if(ones != null && ones.size() > 0){
												if(daily == null){
														daily = ones;
												}
												else{
														Set<String> keySet = ones.keySet();
														for(String key:keySet){												
																Map<Integer, String> map = ones.get(key);
																if(daily.containsKey(key)){
																		Map<Integer, String> one = daily.get(key);
																		for(int i=0;i<16;i++){
																				if(one.containsKey(i)){
																						String sdd = one.get(i);
																						double dd = 0;
																						if(sdd != null){
																								try{
																										dd = Double.parseDouble(sdd);
																								}catch(Exception ex){}
																						}
																						if(map.containsKey(i)){
																								sdd = map.get(i);
																								try{
																										dd += Double.parseDouble(sdd);
																								}catch(Exception ex){}				
																						}
																						map.put(i, dfn.format(dd));
																				}
																		}
																		daily.put(key, map);
																}
																else{
																		daily.put(key, map);
																}
														} // for key
												} // else
										}
										// time blocks
										Map<Integer, List<TimeBlock>> blocks = doc.getDailyBlocks();
										if(blocks != null){
												if(dailyBlocks == null){
														dailyBlocks = blocks;
												}
												else{
														for(int i=0;i<16;i++){
																List<TimeBlock> bls = null, bls2=null;
																if(dailyBlocks.containsKey(i)){
																		bls = dailyBlocks.get(i);
																}
																if(blocks.containsKey(i)){
																		bls2 = blocks.get(i);
																}
																if(bls != null && bls2 != null){
																		for(TimeBlock tb:bls2){
																				bls.add(tb);
																		}
																}
																else if(bls2 != null){
																		bls = bls2;
																}
																dailyBlocks.put(i, bls);
														}
												}
										}
										// hour code total
										Map<Integer, Double> hrct = doc.getHourCodeTotals();
										if(hrct != null && hrct.size() > 0){
												if(hourCodeTotals == null){
														hourCodeTotals = hrct;
												}
												else{
														Set<Integer> set = hourCodeTotals.keySet();
														Set<Integer> set2 = hrct.keySet();
														if(set2 != null && set2.size() > 0){
																for(int i:set2){
																		set.add(i);
																}
														}
														for(int i:set){
																double dd = 0;
																if(hourCodeTotals.containsKey(i)){
																		dd = hourCodeTotals.get(i);
																}
																if(hrct.containsKey(i)){
																		dd += hrct.get(i);
																}
																hourCodeTotals.put(i, dd);
														}
												}
										}
										// hourCodeWeek1
										Map<String, Double> hrw1 = doc.getHourCodeWeek1Dbl();
										if(hrw1 != null && hrw1.size() > 0){
												if(hourCodeWeek1 == null){
														hourCodeWeek1 = hrw1;
												}
												else{
														Set<String> set = hourCodeWeek1.keySet();
														Set<String> set2 = hrw1.keySet();
														if(set2 != null && set2.size() > 0){
																for(String key:set2){
																		set.add(key);
																}
														}
														for(String key:set){
																double dd = 0;
																if(hourCodeWeek1.containsKey(key)){
																		dd = hourCodeWeek1.get(key);
																}
																if(hrw1.containsKey(key)){
																		dd += hrw1.get(key);
																}
																hourCodeWeek1.put(key, dd);
														}
												}
										}
										Map<String, Double> hrw2 = doc.getHourCodeWeek2Dbl();
										if(hrw1 != null && hrw2.size() > 0){
												if(hourCodeWeek2 == null){
														hourCodeWeek2 = hrw2;
												}
												else{
														Set<String> set = hourCodeWeek2.keySet();
														Set<String> set2 = hrw2.keySet();
														if(set2 != null && set2.size() > 0){
																for(String key:set2){
																		set.add(key);
																}
														}
														for(String key:set){
																double dd = 0;
																if(hourCodeWeek2.containsKey(key)){
																		dd = hourCodeWeek2.get(key);
																}
																if(hrw2.containsKey(key)){
																		dd += hrw2.get(key);
																}
																hourCodeWeek2.put(key, dd);
														}
												}												
										}
										week1Total += doc.getWeek1TotalDbl();
										week2Total += doc.getWeek2TotalDbl();
										week1_flsa += doc.getWeek1_flsaDbl();
										week2_flsa += doc.getWeek2_flsaDbl();										
								} // end for
						} // end if
						if(includeEmptyBlocks){
								fillTwoWeekEmptyBlocks();
						}
						// getEmpAccruals();
				}
		}
		
		/*
		public void prepareDaily(boolean includeEmptyBlocks){
				if(daily == null && !id.equals("")){
						TimeBlockList tl = new TimeBlockList();
						tl.setDocument_id(id);
						tl.setActiveOnly();
						String back = tl.find();
						if(back.equals("")){
								Map<String, Map<Integer, String>> ones = tl.getDaily();
								if(ones != null && ones.size() > 0){
										daily = ones;
										// }								
										// Map<Integer, Double> ones = tl.getDaily();
										// if(ones != null && ones.size() > 0){
										// daily = ones;
										dailyBlocks = tl.getDailyBlocks();
										hourCodeTotals = tl.getHourCodeTotals();
										hourCodeWeek1 = tl.getHourCodeWeek1();
										hourCodeWeek2 = tl.getHourCodeWeek2();
										List<TimeBlock> ones2 = tl.getTimeBlocks();
										if(ones2 != null && ones2.size() > 0){
												timeBlocks = ones2;
										}
										week1_flsa = tl.getWeek1_flsa();
										week2_flsa = tl.getWeek2_flsa();
										week1Total = tl.getWeek1Total();
										week2Total = tl.getWeek2Total();										
								}
						}
						if(includeEmptyBlocks){
								fillTwoWeekEmptyBlocks();
						}
						getEmpAccruals();
				}
		}
		public void prepareDaily(){
				//
				// include empty blocks as well
				//
				prepareDaily(true);
		}		
		*/

		public Map<String, Map<Integer, String>> getDaily(){
				return daily;
		}
		
		public List<TimeBlock> getTimeBlocks(){
				/*
				if(timeBlocks == null){
						prepareDaily();
				}
				if(timeBlocks == null){
						timeBlocks = new ArrayList<>();
						dailyBlocks = new TreeMap<>();
				}
				*/
				return timeBlocks;
		}
		public String getWeek1Total(){
				return ""+dfn.format(week1Total);
		}
		public String getWeek2Total(){
				return ""+dfn.format(week2Total);
		}
		public String getPayPeriodTotal(){
				double ret = week1Total+week2Total;
				return ""+dfn.format(ret);
		}
		public SalaryGroup getSalaryGroup(){
				if(salaryGroup == null){
						getJobs();
						if(job != null){
								salaryGroup = job.getSalaryGroup();
						}
				}
				return salaryGroup;
		}
		public boolean isExempt(){
				if(salaryGroup == null)
						getSalaryGroup();
				if(salaryGroup != null){
						return salaryGroup.isExempt();
				}
				return false;
		}
		public boolean isTemporary(){
				if(salaryGroup == null)
						getSalaryGroup();
				if(salaryGroup != null){
						return salaryGroup.isTemporary();
				}
				return false;
		}
		public boolean isPartTime(){
				if(salaryGroup == null)
						getSalaryGroup();
				if(salaryGroup != null){
						return salaryGroup.isPartTime();
				}
				return false;
		}		
		public boolean isUnionned(){
				if(salaryGroup == null)
						getSalaryGroup();
				if(salaryGroup != null){
						return salaryGroup.isUnionned();
				}
				return false;
		}		
		public List<EmployeeAccrual> getEmpAccruals(){
				// get it from one of the doucments
				if(document != null){
						employeeAccruals = document.getEmpAccruals();
				}
				return employeeAccruals;
		}
		// short description of employee accrual balance as of now
		public String getEmployeeAccrualsShort(){
				String ret = "";
				if(hasAllAccruals()){
						for(EmployeeAccrual one:employeeAccruals){
								if(one.getHours() > 0){
										String str = one.getAccrual().getName();
										if(ret.indexOf(str) == -1){
												if(!ret.equals("")) ret += ", ";
												ret += str+": "+dfn.format(one.getHours());
										}
								}
						}
				}
				return ret;
		}
		public void findUsedAccruals(){
				/*
				if(usedAccrualTotals == null){
						TimeBlockList tl = new TimeBlockList();
						tl.setActiveOnly();
						tl.setDocument_id(id);
						String back = tl.findUsedAccruals();
						if(back.equals("")){
								usedAccrualTotals = tl.getUsedAccrualTotals();
						}
						else{
								logger.error(back);
						}
				}
				*/
		}
		public void findHourCodeTotals(){
				/*
				if(hourCodeTotals == null){
						TimeBlockList tl = new TimeBlockList();
						tl.setActiveOnly();
						tl.setDocument_id(id);
						String back = tl.find();
						if(back.equals("")){
								hourCodeTotals = tl.getHourCodeTotals();
						}
				}
				*/
		}
		public boolean hasDailyBlocks(){
				return dailyBlocks != null;
		}
		public boolean hasTimeBlocks(){
				return timeBlocks != null;
		}
		public boolean hasHourCodeTotals(){
				return hourCodeTotals != null;
		}
		public boolean hasAllAccruals(){
				if(allAccruals.size() == 0){
						getEmpAccruals();
				}
				return allAccruals != null && allAccruals.size() > 0;
		}
		public boolean hasHourCodeWeek1(){
				return hourCodeWeek1 != null && hourCodeWeek1.size() > 0;
		}
		public boolean hasHourCodeWeek2(){
				return hourCodeWeek2 != null && hourCodeWeek2.size() > 0;
		}
		public Map<Integer, List<TimeBlock>> getDailyBlocks(){
				if(dailyBlocks == null){
						// prepareDaily();
				}
				return dailyBlocks;
		}
		public Map<Integer, Double> getHourCodeTotals(){
				return hourCodeTotals;
		}
		// change double to string for formating purpose
		public Map<String, String> getHourCodeWeek1(){
				Map<String, String> map2 = new TreeMap<>();
				if(hasHourCodeWeek1()){
						Set<String> keys = hourCodeWeek1.keySet();
						for(String key:keys){
								double val = hourCodeWeek1.get(key);
								map2.put(key, dfn.format(val));
						}
				}
				return map2;
		}
		// change double to string for formating purpose
		public Map<String, String> getHourCodeWeek2(){
				Map<String, String> map2 = new TreeMap<>();
				if(hasHourCodeWeek2()){
						Set<String> keys = hourCodeWeek2.keySet();
						for(String key:keys){
								double val = hourCodeWeek2.get(key);
								map2.put(key, dfn.format(val));
						}
				}
				return map2;
		}
		public Map<String, List<String>> getAllAccruals(){
				return allAccruals;
		}
		
		public void adjustAccruals(){
				if(employeeAccruals != null && usedAccrualTotals != null){
						for(EmployeeAccrual one: employeeAccruals){
								String accrual_id = one.getAccrual_id();
								Accrual accrual = one.getAccrual();
								List<String> list = new ArrayList<>();
								String accName = accrual.getName();
								String accDesc = accrual.getDescription();
								if(accDesc == null) accDesc="";
								String codeInfo = accName+": "+accDesc;
								//
								// String related_id = one.getRelated_hour_code_id();
								if(accrual_id != null && !accrual_id.equals("")){
										double hrs_total = one.getHours();
										list.add(""+dfn.format(hrs_total));
										try{
												int cd_id = Integer.parseInt(accrual_id);
												if(usedAccrualTotals.containsKey(cd_id)){
														double hrs_used = usedAccrualTotals.get(cd_id);
														list.add(""+dfn.format(hrs_used));
														if(hrs_total > hrs_used){
																hrs_total -= hrs_used;
														}
														else{
																hrs_total = 0.0;
														}
														one.setHours(hrs_total);
												}
												else{
														list.add("0.00"); // nothing used
												}
												list.add(""+dfn.format(hrs_total)); // adjusted
												if(accrual.hasPref_max_leval()){
														if(hrs_total > accrual.getPref_max_level()){
																String str = "Your "+accrual.getName()+": "+accrual.getDescription()+" balance is "+dfn.format(hrs_total)+" and currently exceeds the city target balance. Please use Comp Time Accrued instead of PTO until this balance is reduced to no more than "+accrual.getPref_max_level()+" hours.";
																if(!warnings.contains(str))
																		warnings.add(str);
														}
												}
												allAccruals.put(codeInfo, list);
										}catch(Exception ex){
												logger.error(ex);
										}
								}
						}
				}
		}
		void prepareHolidays(){
				HolidayList hl = new HolidayList(debug);
				if(!pay_period_id.equals("")){
						hl.setPay_period_id(pay_period_id);
				}
				String back = hl.find();
				if(back.equals("")){
						holidays = hl;
				}
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
		// since not everyday we have timeblock, we need to fill
		// the empty ones, needed for display and related links
		void fillTwoWeekEmptyBlocks(){
				/*
				prepareHolidays();
				if(payPeriod == null){
						getPayPeriod();
				}				
				String date = payPeriod.getStart_date();
				for(int j=13;j >=0;j--){
						if(!dailyBlocks.containsKey(j)){
								TimeBlock one =
										new TimeBlock();
								//
								// we need to set what we need in timeblock
								// document_id, date
								// probably document_id directly from action
								// and we do not need to save it in block
								one.setDocument_id(id);
								// we need to set the day date
								//
								one.setOrder_index(j);								
								String dt = Helper.getDateAfter(date, j);
								one.setDate(dt);
								if(holidays.isHoliday(dt)){
										one.setIsHoliday(true);
										one.setHolidayName(holidays.getHolidayName(dt));
								}
								// we use start date from payperiod and we add j days to it
								//
								List<TimeBlock> lone = new ArrayList<>();
								lone.add(one);
								//
								dailyBlocks.put(j, lone);
						}
				}
				*/
		}
		private void checkForWarnings(){
				checkForWarningsAfter();
				checkForWarningsBefore();
		}
		// after submission
		private void checkForWarningsAfter(){
				if(job == null){
						getJobs();
				}
				if(week1Total > 0){
						checkWeekWarnings(hourCodeWeek1, week1Total);
				}
				if(job != null){
						if(week1Total < job.getWeekly_regular_hours()){
								String str = "Week 1 total hours are less than "+job.getWeekly_regular_hours()+" hrs";
								if(!warnings.contains(str))
										warnings.add(str);
						}
				}
				if(week2Total > 0){
						checkWeekWarnings(hourCodeWeek2, week2Total);
				}
				if(job != null){				
						if(week2Total < job.getWeekly_regular_hours()){
								String str = "Week 2 total hours are less than "+job.getWeekly_regular_hours()+" hrs";
								if(!warnings.contains(str))
										warnings.add(str);
						}
				}
				checkForUnauthorizedHoliday();
		}
		/**
		 * check if the employee is eligible for holiday
		 * if the day before holiday or day after is authorized unpaid leave
		 * the employee is not eligible
		 */
		private void checkForUnauthorizedHoliday(){
				int day = -1, next_day = -1;
				String day_hr_code="", next_hr_code = "";
				if(timeBlocks != null){
						for(TimeBlock one:timeBlocks){
								String str = one.getHour_code();
								int order_index = one.getOrder_index();
								if(str != null && !str.equals("")){
										if(str.startsWith("H1.0") || str.startsWith("UA")){
												if(day < 0){
														day = order_index;
														day_hr_code = str;
												}
												else{
														// make sure they are next to each other
														if(order_index == day + 1){
																next_day = order_index;
																next_hr_code = str;
																// 
																// if both are holidays or both are UA, then we
																// check with next
																if(day_hr_code.equals(next_hr_code)){
																		// skip
																		day = order_index;
																		day_hr_code = str;
																		next_day = -1;
																		next_hr_code = "";
																}
																else{
																		warnings.add(" Possible Uneligible for Holiday day used adjacent to Unpaid Authorized leave ");
																		break;
																}
														}
														else {
																day = order_index;
																day_hr_code = str;
																next_day = -1;
																next_hr_code = "";
														}
												}
										}
								}
						}
				}
		}
		// before submission
		private void checkForWarningsBefore(){
				if(job == null){
						getJobs();
				}
				if(week1Total > 0){
						checkWeekWarnings(hourCodeWeek1, week1Total);
				}
				if(week2Total > 0){
						checkWeekWarnings(hourCodeWeek2, week2Total);
				}
		}		
		/**
		 * in this function we check the weekly hour code entry times
		 * to make sure they comply with the rules set in 'Accrual warnings'
		 * db rules
		 * 1-if certain hour code has a min such as PTO can not be less than 1
		 * 2-if hour code hours used should be in certain increments
		 *   such 0.25 (quarter hour increments)
		 * 3-Check if excess hours were used, such as using 7 hours PTO when it is
		 *   only needed 6
		 */
		private void checkWeekWarnings(Map<String, Double> hourCodeWeek,
																	 double weekTotal
																	 ){
				getJobs();
				for(String key:warningMap.keySet()){
						if(hourCodeWeek.containsKey(key)){
								AccrualWarning acc_warn = warningMap.get(key);
								double dbl_used = hourCodeWeek.get(key);
								if(acc_warn.require_min()){
										if(dbl_used < acc_warn.getMin_hrs()){
												warnings.add(acc_warn.getMin_warning_text());
										}
								}
								if(acc_warn.require_step()){
										if(dbl_used % acc_warn.getStep_hrs() > 0){
												String str = acc_warn.getStep_warning_text()+" ("+dfn.format(dbl_used)+" hrs)";
												if(!warnings.contains(str))
														warnings.add(str);
										}
								}
								double d_dif = weekTotal - dbl_used; // 45.27 - 8.27
								double d_need = 0;
								if(job != null){
										if(d_dif < job.getWeekly_regular_hours())
												d_need = job.getWeekly_regular_hours() - d_dif;   // 40 - 37 = 3
								}
								else{
										
										if(d_dif < 40)
												d_need = 40. - d_dif;   // 40 - 37 = 3
								}
								if(d_need > 0){
										if(acc_warn.require_step()){
												if(d_need % acc_warn.getStep_hrs() > 0.01){
														// 
														// adjust the need accroding to step
														// such as 6.1 ==> 6.25
														d_need = d_need + acc_warn.getStep_hrs() - d_need % acc_warn.getStep_hrs();
												}
												// if need is less than min (normally 1 hr)
												// than we changed to min such as 0.50, 0.75
												if(d_need < acc_warn.getMin_hrs()){
														d_need = acc_warn.getMin_hrs();
												}
										}
								}
								double dd = dbl_used - d_need; // 8.3 - 6.25
								if(dd > 0.01){
										String str = acc_warn.getExcess_warning_text()+" ("+dfn.format(dd)+" hrs)";
										if(!warnings.contains(str))
												warnings.add(str);
								}
						}
				}
		}
		public boolean hasWarnings(){
				return warnings.size() > 0;
		}
		public List<String> getWarnings(){
				return warnings;
		}

		
}
