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

public class Document{

    boolean debug = false;
    static Logger logger = LogManager.getLogger(Document.class);
    static final long serialVersionUID = 2400L;
    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
    DecimalFormat dfn = new DecimalFormat("##0.00");
    private String id="", employee_id="", pay_period_id="",
				initiated="",initiated_by="", selected_job_id="";
    private String job_id="";
    double week1Total = 0, week2Total = 0, week1_flsa=0, week2_flsa=0;
    PayPeriod payPeriod = null;
    Employee employee = null;
    Employee initiater = null;
    Workflow lastWorkflow = null;
    List<TimeAction> timeActions = null;
    //
    Map<JobType, Map<Integer, String>> daily = null;
    Map<JobType, Map<Integer, Double>> dailyDbl = null;		
    List<TimeBlock> timeBlocks = null;
    List<String> warnings = new ArrayList<>();
    JobTask job = null;
    List<JobTask> jobs = null;
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
    TimeAction lastTimeAcion = null;
    Map<String, AccrualWarning> warningMap = new TreeMap<>();
    // week 1,2 / hour_code_id /hours
    Map<Integer, Map<Integer, Double>> usedWeeklyAccruals = null;
    HolidayList holidays = null;
		boolean accrualAdjusted = false;
    public Document(String val,
										String val2,
										String val3,
										String val4,
										String val5,
										String val6
										){
				setId(val);
				setEmployee_id(val2);				
				setPay_period_id(val3);
				setJob_id(val4);
				setInitiated(val5);
				setInitiated_by(val6);
				fillWarningMap();

    }
    public Document(String val){
				setId(val);
				fillWarningMap();
    }
    public Document(){
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
    public String getJob_id(){
				return job_id;
    }		
    public String getInitiated(){
				return initiated;
    }
    public String getInitiated_by(){
				return initiated_by;
    }		
    public String getId(){
				return id;
    }
    //
    // setters
    //
    public void setId (String val){
				if(val != null)
						id = val;
    }
    public void setPay_period_id (String val){
				if(val != null && !val.equals("-1"))
						pay_period_id = val;
    }
    public void setJob_id (String val){
				if(val != null && !val.equals("-1"))
						job_id = val;
    }		
    public void setEmployee_id(String val){
				if(val != null && !val.equals("-1"))						
						employee_id = val;
    }
    public void setInitiated(String val){
				if(val != null)
						initiated = val;
    }
    public void setInitiated_by(String val){
				if(val != null)
						initiated_by = val;
    }
    public String toString(){
				return id;
    }

    public boolean equals(Object o) {
				if (o instanceof Document) {
						Document c = (Document) o;
						if ( this.id.equals(c.getId())) 
								return true;
				}
				return false;
    }
    public int hashCode(){
				int seed = 37;
				if(!id.equals("")){
						try{
								seed += Integer.parseInt(id)*31;
						}catch(Exception ex){
								// we ignore
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
    public Employee getInitiater(){
				if(!initiated_by.equals("") && initiater == null){
						Employee one = new Employee(initiated_by);
						String back = one.doSelect();
						if(back.equals("")){
								initiater = one;
						}
				}
				return initiater;
    }
    public List<TimeAction> getTimeActions(){
				if(timeActions == null && !id.equals("")){
						TimeActionList tal = new TimeActionList(id);
						tal.setSortby("a.id desc");
						String back = tal.find();
						if(back.equals("")){
								List<TimeAction> ones = tal.getTimeActions();
								if(ones != null && ones.size() > 0){
										timeActions = ones;
										lastTimeAcion = ones.get(0); // last is first since desc
								}
						}
				}
				return timeActions;
    }
    public TimeAction getLastTimeAction(){
				if(hasTimeBlocks() && lastTimeAcion == null){
						lastTimeAcion = timeActions.get(0);
				}
				return lastTimeAcion;
    }
    public boolean hasTimeActions(){
				getTimeActions();
				return timeActions != null && timeActions.size() > 0;
    }
    public boolean hasLastTimeAction(){
				getLastTimeAction();
				return lastTimeAcion != null;
    }
    public boolean hasLastWorkflow(){
				getLastWorkflow();
				return lastWorkflow != null;
    }
    public Workflow getLastWorkflow(){
				if(hasLastTimeAction()){
						lastWorkflow = lastTimeAcion.getWorkflow();
				}
				return lastWorkflow;
    }
    public boolean hasNextActioners(){
				if(hasLastWorkflow() && nextActioners == null){
						getJob();
						if(lastWorkflow.hasNextNode() && job != null){
								String wf_id = lastWorkflow.getNext_workflow_id();
								if(wf_id.equals("2")){ // submit for approval
										getEmployee();
										if(employee != null){
												if(nextActioners == null)
														nextActioners = new ArrayList<>();
												nextActioners.add(employee);
										}
								}
								else{
										GroupManagerList gml = new GroupManagerList();
										gml.setWorkflow_id(wf_id);
										gml.setGroup_id(job.getGroup_id());
										String back = gml.find();
										if(back.equals("")){
												List<GroupManager> ones = gml.getManagers();
												if(ones != null){
														for(GroupManager one:ones){
																Employee emp = one.getEmployee();
																if(emp != null){
																		if(nextActioners == null)
																				nextActioners = new ArrayList<>();
																		nextActioners.add(emp);
																}
														}
												}
										}
								}
						}
				}
				return nextActioners != null && nextActioners.size() > 0;
    }
    // this next actions managers list
    public List<Employee> getNextActioners(){
				return nextActioners;
    }
    public String getNextActionerNames(){
				String ret = "";
				if(hasNextActioners()){
						for(Employee one:nextActioners){
								if(!ret.equals("")) ret += ", ";
								ret += one.getFull_name();
						}
				}
				return ret;
    }
    public JobTask getJob(){
				if(!job_id.equals("") && job == null){
						JobTask one = new JobTask(job_id);
						String back = one.doSelect();
						if(back.equals("")){
								job = one;
						}
				}
				return job;
    }		
    private void fillWarningMap(){
				AccrualWarningList tl = new AccrualWarningList();
				String back = tl.find();
				if(back.equals("")){
						List<AccrualWarning> ones = tl.getAccrualWarnings();
						if(ones != null && ones.size() > 0){
								for(AccrualWarning one:ones){
										List<HourCode> codes = one.getHourCodes();
										if(codes != null && codes.size() > 0){
												for(HourCode cc:codes){		
														warningMap.put(cc.getCodeInfo(), one);
												}
										}
								}
						}
				}
    }		
    public boolean canBeApproved(){
				getLastTimeAction();
				if(lastTimeAcion != null){
						Workflow lastWorkFlow = lastTimeAcion.getWorkflow();
						return lastWorkFlow != null && lastWorkFlow.canApprove();
				}
				return false;
    }
    public boolean isApproved(){
				getTimeActions();
				if(timeActions != null){
						for(TimeAction one:timeActions){ 
								Workflow wf = one.getWorkflow();
								if(wf.isApproved()){
										return true;
								}
						}
				}
				return false;
    }
    public boolean isProcessed(){
				getTimeActions();
				if(timeActions != null){
						for(TimeAction one:timeActions){ 
								Workflow wf = one.getWorkflow();
								if(wf.isProcessed()){
										return true;
								}
						}
				}
				return false;
    }
    Map<Integer, Map<Integer, Double>> getUsedWeeklyAccruals(){
				return usedWeeklyAccruals;
    }		
    public boolean isSubmitted(){
				getTimeActions();
				if(timeActions != null){
						for(TimeAction one:timeActions){ 
								Workflow wf = one.getWorkflow();
								if(wf.isSubmitted()){
										return true;
								}
						}
				}
				return false;
    }
    public TimeAction getSubmitTimeAction(){
				getTimeActions();
				if(timeActions != null){
						for(TimeAction one:timeActions){ 
								Workflow wf = one.getWorkflow();
								if(wf.isSubmitted()){
										return one;
								}
						}
				}
				return null;
    }
    public boolean canBeProcessed(){
				return isApproved() && !isProcessed();
    }
    public String getWeek1_flsa(){
				return ""+dfn.format(week1_flsa);
    }
    public String getWeek2_flsa(){
				return ""+dfn.format(week2_flsa);
    }
    public double getWeek1_flsaDbl(){
				return week1_flsa;
    }
    public double getWeek2_flsaDbl(){
				return week2_flsa;
    }					
    public boolean hasDaily(){
				prepareDaily();
				return daily != null && daily.size() > 0;
    }
    public boolean isPunchClockOnly(){
				getJob();
				if(job != null){
						return job.isPunchClockOnly();
				}
				return false;
    }
		
    public void prepareDaily(boolean includeEmptyBlocks){
				if(daily == null && !id.equals("")){
						TimeBlockList tl = new TimeBlockList();
						tl.setDocument_id(id);
						tl.setActiveOnly();
						String back = tl.find();
						if(back.equals("")){
								Map<JobType, Map<Integer, String>> ones = tl.getDaily();
								if(ones != null && ones.size() > 0){
										daily = tl.getDaily();
										dailyDbl = tl.getDailyDbl();
										dailyBlocks = tl.getDailyBlocks();
										hourCodeTotals = tl.getHourCodeTotals();
										hourCodeWeek1 = tl.getHourCodeWeek1();
										hourCodeWeek2 = tl.getHourCodeWeek2();
										List<TimeBlock> ones2 = tl.getTimeBlocks();
										if(ones2 != null && ones2.size() > 0){
												timeBlocks = ones2;
										}
										usedWeeklyAccruals = tl.getUsedWeeklyAccruals();
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
		
    public Map<JobType, Map<Integer, Double>> getDailyDbl(){
				return dailyDbl;
    }		
    public Map<JobType, Map<Integer, String>> getDaily(){
				return daily;
    }		
		
    public List<TimeBlock> getTimeBlocks(){
				if(timeBlocks == null){
						prepareDaily();
				}
				if(timeBlocks == null){
						timeBlocks = new ArrayList<>();
						dailyBlocks = new TreeMap<>();
				}
				return timeBlocks;
    }
    public String getWeek1Total(){
				return ""+dfn.format(week1Total);
    }
    public String getWeek2Total(){
				return ""+dfn.format(week2Total);
    }
    public double getWeek1TotalDbl(){
				return week1Total;
    }
    public double getWeek2TotalDbl(){
				return week2Total;
    }		
    public String getPayPeriodTotal(){
				double ret = week1Total+week2Total;
				return ""+dfn.format(ret);
    }
    // we need at least one job to find some info
    // about the employee
    public JobTask getJobTask(){
				if(job == null){
						getJobs();
						if(jobs.size() == 1){
								job = jobs.get(0);
						}
						else if(!selected_job_id.equals("")){
								for(JobTask one:jobs){
										if(one.getId().equals(selected_job_id)){
												job = one;
												break;
										}
								}
						}
						else {
								for(JobTask one:jobs){
										if(one.isPrimary()){
												job = one;
												selected_job_id = one.getId();
												break;
										}
								}								
						}
				}
				return job;
    }
    public List<JobTask> getJobs(){
				if(jobs == null){
						JobTaskList jl = new JobTaskList(employee_id);
						jl.setPay_period_id(pay_period_id);
						String back = jl.find();
						if(back.equals("")){
								List<JobTask> ones = jl.getJobTasks();
								if(ones != null && ones.size() > 0){
										jobs = ones; // get one
								}
						}
				}
				return jobs;
    }		
    public boolean hasJob(){
				getJob();
				return job != null;
    }
    public SalaryGroup getSalaryGroup(){
				if(salaryGroup == null){
						getJob();
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

				if(employeeAccruals == null){
						EmployeeAccrualList al = new EmployeeAccrualList();						
						al.setDocument_id(id);
						String back = al.find();
						if(back.equals("")){
								List<EmployeeAccrual> ones = al.getEmployeeAccruals();
								if(ones != null && ones.size() > 0){
										employeeAccruals = ones;
								}
						}
						// now we adjust the totals see below
						findHourCodeTotals();
						findUsedAccruals();
						adjustAccruals();
						checkForWarnings();
				}
				return employeeAccruals;
    }
    private void checkForExcessUse(double weekTotal,
																	 int whichWeek){
				if(job != null){
						if(weekTotal > job.getWeekly_regular_hours()+0.001){
								double week_excess = weekTotal - job.getWeekly_regular_hours();
								double week_excess_adj = week_excess;
								if(usedWeeklyAccruals != null &&
									 !usedWeeklyAccruals.isEmpty()){
										if(usedWeeklyAccruals.containsKey(whichWeek)){
												Map<Integer, Double> map = usedWeeklyAccruals.get(whichWeek);
												Set<Integer> keys = map.keySet();
												if(!keys.isEmpty()){
														for(int key:keys){
																String str = "";
																double used = map.get(key);
																if(used <= week_excess_adj){
																		HourCode hrc = new HourCode(""+key);
																		String back = hrc.doSelect();
																		if(back.equals("")){
																				str = "Week "+whichWeek+" excess of ("+dfn.format(used)+" hrs) of ("+hrc.getName()+") used ";
																				if(!warnings.contains(str))
																						warnings.add(str);
																				str ="";
																				week_excess_adj -= used; // adjust for next key (if any)
																				continue;
																		}	
																}
																else if(week_excess_adj > 0.001
																				&& used > week_excess_adj+0.001){
																		// need is the amount we need to get
																		// to weekly regular total required
																		double need = used - week_excess_adj;
																		double real_need = 0;
																		HourCode hrc = new HourCode(""+key);
																		String back = hrc.doSelect();
																		double min_hrs = 0;
																		if(back.equals("")){
																				if(hrc.hasAccrualWarning()){
																						AccrualWarning acw = hrc.getAccrualWarning();
																						if(acw.require_min()){
																								min_hrs = acw.getMin_hrs();
																								if(need < min_hrs){
																										need = min_hrs;
																										if(used > need){
																												week_excess_adj = used-need;
																												str = "Week "+whichWeek+" excess of ("+dfn.format(week_excess_adj)+" hrs) of ("+hrc.getName()+") used";
																												if(!warnings.contains(str))
																														warnings.add(str);
																												return;
																										}
																								} // need > min_hrs
																								else { // need > min_hrs
																										need -= min_hrs;
																										real_need += min_hrs;
																								}
																						}
																						else{
																								str = "Week "+whichWeek+" excess of ("+dfn.format(week_excess_adj)+" hrs) of ("+hrc.getName()+") used";
																								if(!warnings.contains(str))
																										warnings.add(str);
																								return;
																						}
																						if(need > 0.001){
																								if(acw.require_step()){
																										int cnt = (int)(need / acw.getStep_hrs());
																										if(need % acw.getStep_hrs() > 0){
																												cnt += 1;
																										}
																										need = cnt*acw.getStep_hrs();
																										real_need += need;
																										week_excess = used-real_need;
																										if(week_excess > 0.001){
																												
																												str = "Week "+whichWeek+" excess of ("+dfn.format(week_excess)+" hrs) of ("+hrc.getName()+") used";
																												if(!warnings.contains(str))
																														warnings.add(str);
																												return;
																										}
																								}
																						}
																				}
																		}
																}
														}
												}
										}
								}
						}
				}				
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
				if(usedAccrualTotals == null){
						TimeBlockList tl = new TimeBlockList();
						tl.setActiveOnly();
						tl.setEmployee_id(employee_id);
						tl.setPay_period_id(pay_period_id);
						String back = tl.findUsedAccruals();
						if(back.equals("")){
								usedAccrualTotals = tl.getUsedAccrualTotals();
						}
						else{
								logger.error(back);
						}
				}
    }
    public Map<Integer, Double> getUsedAccrualTotals(){
				if(usedAccrualTotals == null)
						findUsedAccruals();
				return usedAccrualTotals;
    }
    public void findHourCodeTotals(){
				if(hourCodeTotals == null){
						TimeBlockList tl = new TimeBlockList();
						tl.setActiveOnly();
						tl.setDocument_id(id);
						String back = tl.find();
						if(back.equals("")){
								hourCodeTotals = tl.getHourCodeTotals();
						}
				}
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
						prepareDaily();
				}
				return dailyBlocks;
    }
    public Map<Integer, Double> getHourCodeTotals(){
				return hourCodeTotals;
    }
    public Map<String, Double> getHourCodeWeek1Dbl(){
				return hourCodeWeek1;
    }
    public Map<String, Double> getHourCodeWeek2Dbl(){
				return hourCodeWeek2;
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
				if(!accrualAdjusted){
						accrualAdjusted = true;
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
														if(isUnionned()){
																if(accrual.hasPref_max_leval()){
																		// union it is 40 instead of 10
																		if(hrs_total > accrual.getPref_max_level()*4){
																				String str = "Your "+accrual.getName()+": "+accrual.getDescription()+" balance is "+dfn.format(hrs_total)+" and currently exceeds the city target balance. Please use Comp Time Accrued instead of PTO or Sick time until this balance is reduced to no more than "+(accrual.getPref_max_level()*4)+" hours.";
																				if(!warnings.contains(str))
																						warnings.add(str);
																		}
																}
														}
														else{
																if(accrual.hasPref_max_leval()){
																		if(hrs_total > accrual.getPref_max_level()){
																				String str = "Your "+accrual.getName()+": "+accrual.getDescription()+" balance is "+dfn.format(hrs_total)+" and currently exceeds the city target balance. Please use Comp Time Accrued instead of PTO or Sick time until this balance is reduced to no more than "+accrual.getPref_max_level()+" hours.";
																				if(!warnings.contains(str))
																						warnings.add(str);
																		}
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
    }
    public void prepareHolidays(){
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
				prepareHolidays();
				if(payPeriod == null){
						getPayPeriod();
				}
				String date = payPeriod.getStart_date();
				if(dailyBlocks == null){
						dailyBlocks = new TreeMap<>();
				}
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
    }
    private void checkForWarnings(){
				checkForWarningsAfter();
				checkForWarningsBefore();
    }
    // after submission
    private void checkForWarningsAfter(){
				if(job == null){
						getJob();
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
						getJob();
				}
				if(week1Total > 0){
						checkWeekWarnings(hourCodeWeek1, week1Total);
						checkForExcessUse(week1Total, 1);
				}
				if(week2Total > 0){
						checkWeekWarnings(hourCodeWeek2, week2Total);
						checkForExcessUse(week2Total, 2);						
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
				getJob();
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
    public boolean hasTimeNotes(){
				getTimeNotes();
				return timeNotes != null && timeNotes.size() > 0;
    }
    public List<TimeNote> getTimeNotes(){
				if(timeNotes == null && !id.equals("")){
						TimeNoteList tnl = new TimeNoteList(id);
						String back = tnl.find();
						if(back.equals("")){
								List<TimeNote> ones = tnl.getTimeNotes();
								if(ones != null && ones.size() > 0){
										timeNotes = ones;
								}
						}
				}
				return timeNotes;
    }
    public boolean hasTimeIssues(){
				getTimeIssues();
				return timeIssues != null && timeIssues.size() > 0;
    }
    public List<TimeIssue> getTimeIssues(){
				if(timeIssues == null && !id.equals("")){
						TimeIssueList tnl = new TimeIssueList(id);
						tnl.setOpenOnly();
						String back = tnl.find();
						if(back.equals("")){
								List<TimeIssue> ones = tnl.getTimeIssues();
								if(ones != null && ones.size() > 0){
										timeIssues = ones;
								}
						}
				}
				return timeIssues;
    }		
    public String doSelect(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "select id,employee_id,pay_period_id,job_id,date_format(initiated,'%m/%d/%Y %H;%i'),initiated_by from time_documents where id =? ";
				logger.debug(qq);
				try{
						con = UnoConnect.getConnection();
						if(con != null){
								pstmt = con.prepareStatement(qq);
								pstmt.setString(1, id);
								rs = pstmt.executeQuery();
								if(rs.next()){
										setEmployee_id(rs.getString(2));
										setPay_period_id(rs.getString(3));
										setJob_id(rs.getString(4));
										setInitiated(rs.getString(5));
										setInitiated_by(rs.getString(6));
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
    // save only, no updates
    //
    public String doSave(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "insert into time_documents values(0,?,?,?,now(),?) ";
				if(employee_id.equals("")){
						msg = " employee ID not set ";
						return msg;
				}
				if(pay_period_id.equals("")){
						msg = " pay period not set ";
						return msg;
				}
				if(job_id.equals("")){
						msg = " job not set ";
						return msg;
				}				
				if(initiated_by.equals("")){
						msg = " initiater not set ";
						return msg;
				}				
				logger.debug(qq);
				con = UnoConnect.getConnection();				
				try{
						if(con != null){
								pstmt = con.prepareStatement(qq);
								pstmt.setString(1, employee_id);
								pstmt.setString(2, pay_period_id);
								pstmt.setString(3, job_id);
								pstmt.setString(4, initiated_by);
								pstmt.executeUpdate();
						}
						qq = "select LAST_INSERT_ID()";
						pstmt = con.prepareStatement(qq);
						rs = pstmt.executeQuery();
						if(rs.next()){
								id = rs.getString(1);
						}
						//
						// we look for first workflow 
						//
						Workflow wf = findFirstWorkFlow();
						if(wf != null){
								TimeAction ta = new TimeAction(wf.getId(), id, initiated_by);
								msg = ta.doSave();
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
    Workflow findFirstWorkFlow(){
				Workflow workflow = null;
				WorkflowList wfl = new WorkflowList();
				wfl.forFirstWorkflow();
				String back = wfl.find();
				if(back.equals("")){
						List<Workflow> ones = wfl.getWorkflows();
						if(ones != null && ones.size() == 1)
								workflow = ones.get(0);
				}
				return workflow;
    }
		
}
