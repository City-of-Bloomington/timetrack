package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.Arrays;
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

    boolean debug = false, hasTmwrpRuns = false;
    static Logger logger = LogManager.getLogger(MultiJobDoc.class);
    static final long serialVersionUID = 2400L;
    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
    DecimalFormat dfn = new DecimalFormat("##0.00");
    private String employee_id="", pay_period_id="";
    private String job_id="";
    double week1Total = 0, week2Total = 0, week1_flsa=0, week2_flsa=0;
    double week1AmountTotal=0,week2AmountTotal=0;		
    PayPeriod payPeriod = null;
    Employee employee = null;
    JobTask job = null; // we need one of the jobs to get more info
    Document document = null; // we need one to get the accruals 
    // Employee initiater = null;
    Workflow lastWorkflow = null;
    Map<JobType, Map<Integer, Double>> daily = null;				
    List<TimeBlock> timeBlocks = null;
    List<String> warnings = new ArrayList<>();
    List<JobTask> jobs = null;
    List<Document> documents = null;

    SalaryGroup salaryGroup = null;
    Map<String, List<String>> allAccruals = new TreeMap<>();
    Map<Integer, Double> earnedAccrualTotals = null;		
    Map<Integer, Double> hourCodeTotals = null;
    Map<String, Double> hourCodeWeek1 = null;
    Map<String, Double> hourCodeWeek2 = null;
    //
    Map<Integer, Double> amountCodeTotals = null; 
    Map<String, Double> amountCodeWeek1 = null;
    Map<String, Double> amountCodeWeek2 = null;
		
    Map<Integer, Double> usedAccrualTotals = null;
    Map<Integer, List<TimeBlock>> dailyBlocks = null;
    List<EmployeeAccrual> employeeAccruals = null;
    List<TimeNote> timeNotes = null;
    List<TimeIssue> timeIssues = null;
    List<Employee> nextActioners = null;
    Map<Integer, Map<Integer, Double>> usedWeeklyAccruals = null;		
    Map<String, AccrualWarning> warningMap = new TreeMap<>();
    HolidayList holidays = null;
    public MultiJobDoc(String val,
		       String val2
		       ){
	setEmployee_id(val);				
	setPay_period_id(val2);
    }
    public MultiJobDoc(){
				
    }
    public String findDocuments(){
	String back = "";
	if(documents == null &&
	   !employee_id.isEmpty() && !pay_period_id.isEmpty()){
	    DocumentList dl = new DocumentList(employee_id);
	    dl.setPay_period_id(pay_period_id);
	    back = dl.find();
	    if(back.isEmpty()){
		List<Document> ones = dl.getDocuments();
		if(ones != null && ones.size() > 0){
		    documents = ones;
		    for(Document one:documents){
			one.prepareDaily();
			if(one.hasTmwrpRun()){
			    hasTmwrpRuns = true;
			}
		    }
		    // we need one for accruals
		    document = documents.get(0); // any one will do
		}
		prepareDaily(true);								
	    }
	}
	return back;
    }
    public boolean hasDocuments(){
	findDocuments();
	return documents != null && documents.size() > 0;
    }
    public boolean hasTmwrpRuns(){
	return hasTmwrpRuns;
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
	if(!employee_id.isEmpty()){
	    try{
		seed += Integer.parseInt(employee_id)*31;
								
	    }catch(Exception ex){
	    }
	}
	if(!pay_period_id.isEmpty()){
	    try{
		seed += Integer.parseInt(pay_period_id)*37;
								
	    }catch(Exception ex){
	    }
	}				
	return seed;
    }
    public PayPeriod getPayPeriod(){
	if(!pay_period_id.isEmpty() && payPeriod == null){
	    PayPeriod one = new PayPeriod(pay_period_id);
	    String back = one.doSelect();
	    if(back.isEmpty()){
		payPeriod = one;
	    }
	}
	return payPeriod;
    }				
    public Employee getEmployee(){
	if(!employee_id.isEmpty() && employee == null){
	    Employee one = new Employee(employee_id);
	    String back = one.doSelect();
	    if(back.isEmpty()){
		employee = one;
	    }
	}
	return employee;
    }

    public List<JobTask> getJobs(){
	if(jobs == null){
	    if(!employee_id.isEmpty() && !pay_period_id.isEmpty()){
		JobTaskList jl = new JobTaskList(employee_id, pay_period_id);
		jl.setActiveOnly();
		String back = jl.find();
		if(back.isEmpty()){
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
	}
	return jobs;
    }
    public boolean hasMultipleJobs(){
	getJobs();
	return jobs != null && jobs.size() > 0;
    }
    public List<Document> getDocuments(){
	return documents;
    }
    //
    // ToDo change to accrual instead of hourCode
    // or discard all the way
    private void fillWarningMap(){
	AccrualWarningList tl = new AccrualWarningList();
	String back = tl.find();
	if(back.isEmpty()){
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

    public Map<JobType, Map<Integer, String>> getDaily(){
	Set<JobType> set = daily.keySet();
	Map<JobType, Map<Integer, String>> mapd = new TreeMap<>();
	for(JobType str:set){
	    Map<Integer, String> map2 = new TreeMap<>();
						
	    Map<Integer, Double> map = daily.get(str);
	    for(int j=0;j<16;j++){ // 8 total week1, 15 total week2
		double val = map.get(j);
		map2.put(j, dfn.format(val));
	    }
	    mapd.put(str, map2);
	}
	return mapd;
    }				

    public String getWeek1_flsa(){
	return ""+dfn.format(week1_flsa);
    }
    public String getWeek2_flsa(){
	return ""+dfn.format(week2_flsa);
    }						
    public boolean hasDaily(){
	return daily != null && daily.size() > 0;
    }
    public void prepareDaily(boolean includeEmptyBlocks){
	if(daily == null){
	    if(documents == null)
		findDocuments();
	    if(documents != null && documents.size() > 0){
		for(Document doc:documents){
		    Map<JobType, Map<Integer, Double>> mp = doc.getDailyDbl();
		    if(mp != null){
			if(daily == null){
			    daily = mp;
			}
			else{
			    // normally one key (job name)
			    Set<JobType> keySet = mp.keySet();
			    for(JobType key:keySet){
				Map<Integer, Double> map = mp.get(key);
				daily.put(key, map);
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
			    for(int i=0;i<14;i++){
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
		    // not needed
		    if(doc.hasHourCodeTotals()){
			Map<Integer, Double> hrct = doc.getHourCodeTotals();
			if(hrct != null && hrct.size() > 0){
			    if(hourCodeTotals == null){
				hourCodeTotals = hrct;
			    }
			    else{
				Set<Integer> all = new HashSet<>();
				Set<Integer> set = hourCodeTotals.keySet();
				Set<Integer> set2 = hrct.keySet();
				if(set != null && set.size() > 0){
				    for(int i:set){
					all.add(i);
				    }
				}
				if(set2 != null && set2.size() > 0){
				    for(int i:set2){
					all.add(i);
				    }
				}																
				for(int i:all){
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
		    }
		    // hourCodeWeek1
		    if(true){
			Map<String, Double> hrw1 = doc.getHourCodeWeek1Dbl();
			if(hrw1 != null && hrw1.size() > 0){
			    if(hourCodeWeek1 == null){
				hourCodeWeek1 = hrw1;
			    }
			    else{
				Set<String> all = new HashSet<>();
				Set<String> set = hourCodeWeek1.keySet();
				Set<String> set2 = hrw1.keySet();
				if(set != null && set.size() > 0){
				    for(String key:set){
					all.add(key);
				    }
				}														
				if(set2 != null && set2.size() > 0){
				    for(String key:set2){
					all.add(key);
				    }
				}
				for(String key:all){
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
			if(hrw2 != null && hrw2.size() > 0){
			    if(hourCodeWeek2 == null){
				hourCodeWeek2 = hrw2;
			    }
			    else{
				Set<String> all = new HashSet<>();
				Set<String> set = hourCodeWeek2.keySet();
				Set<String> set2 = hrw2.keySet();
				if(set != null && set.size() > 0){
				    for(String key:set){
					all.add(key);
				    }
				}														
				if(set2 != null && set2.size() > 0){
				    for(String key:set2){
					all.add(key);
				    }
				}
																
				for(String key:all){
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
		    }
		    // amountCodeWeek1
		    if(true){
			Map<String, Double> hrw1 = doc.getAmountCodeWeek1Dbl();
			if(hrw1 != null && hrw1.size() > 0){
			    if(amountCodeWeek1 == null){
				amountCodeWeek1 = hrw1;
			    }
			    else{
				Set<String> all = new HashSet<>();
				Set<String> set = amountCodeWeek1.keySet();
				Set<String> set2 = hrw1.keySet();
				if(set != null && set.size() > 0){
				    for(String key:set){
					all.add(key);
				    }
				}														
				if(set2 != null && set2.size() > 0){
				    for(String key:set2){
					all.add(key);
				    }
				}
				for(String key:all){
				    double dd = 0;
				    if(amountCodeWeek1.containsKey(key)){
					dd = amountCodeWeek1.get(key);
				    }
				    if(hrw1.containsKey(key)){
					dd += hrw1.get(key);
				    }
				    amountCodeWeek1.put(key, dd);
				}
			    }
			}
			Map<String, Double> hrw2 = doc.getAmountCodeWeek2Dbl();
			if(hrw1 != null && hrw2.size() > 0){
			    if(amountCodeWeek2 == null){
				amountCodeWeek2 = hrw2;
			    }
			    else{
				Set<String> all = new HashSet<>();
				Set<String> set = amountCodeWeek2.keySet();
				Set<String> set2 = hrw2.keySet();
				if(set != null && set.size() > 0){
				    for(String key:set){
					all.add(key);
				    }
				}														
				if(set2 != null && set2.size() > 0){
				    for(String key:set2){
					all.add(key);
				    }
				}
																
				for(String key:all){
				    double dd = 0;
				    if(amountCodeWeek2.containsKey(key)){
					dd = amountCodeWeek2.get(key);
				    }
				    if(hrw2.containsKey(key)){
					dd += hrw2.get(key);
				    }
				    amountCodeWeek2.put(key, dd);
				}
			    }												
			}
		    }
		    //
		    Map<Integer, Map<Integer, Double>> usedWkAccruals = doc.getUsedWeeklyAccruals();
		    if(usedWkAccruals != null && !usedWkAccruals.isEmpty()){
			if(usedWeeklyAccruals == null){
			    usedWeeklyAccruals = usedWkAccruals;
			}
			else{
			    for(int week_id=1;week_id <3; week_id++){
				if(usedWkAccruals.containsKey(week_id)){
				    Map<Integer, Double> map = usedWkAccruals.get(week_id);
				    Set<Integer> set = map.keySet();
				    Set<Integer> all = new HashSet<>();
				    if(!set.isEmpty()){
					all.addAll(set);
				    }
				    if(usedWeeklyAccruals.containsKey(week_id)){
					Map<Integer, Double> map2 = usedWeeklyAccruals.get(week_id);
					set = map2.keySet();																				
					if(set.isEmpty()){
					    all.addAll(set);
					}
					for(Integer hr_id:all){
					    double hrs = 0;
					    if(map.containsKey(hr_id)){
						hrs = map.get(hr_id);
					    }
					    if(map2.containsKey(hr_id)){
						hrs += map2.get(hr_id);
					    }
					    map2.put(hr_id, hrs);
					}
				    }
				    else{
					usedWeeklyAccruals.put(week_id, map);
				    }
				}
			    }
			}
		    }
		    week1Total += doc.getWeek1TotalDbl();
		    week2Total += doc.getWeek2TotalDbl();
		    week1AmountTotal += doc.getWeek1AmountTotalDbl();
		    week2AmountTotal += doc.getWeek2AmountTotalDbl();
		    week1_flsa += doc.getWeek1_flsaDbl();
		    week2_flsa += doc.getWeek2_flsaDbl();
		    List<TimeBlock> tbs = doc.getTimeBlocks();
		    if(tbs == null || tbs.size() == 0) continue;
		    if(timeBlocks == null){
			timeBlocks = tbs;
		    }
		    else {
			for(TimeBlock tb:tbs){
			    timeBlocks.add(tb);
			}
		    }
		} // end for
	    } // end if
	    // one of the document can provide all these
	    if(document != null){
		employeeAccruals = document.getEmpAccruals();
		usedAccrualTotals = document.getUsedAccrualTotals();
		earnedAccrualTotals = document.getEarnedAccrualTotals();
	    }
	    checkForWarnings();
	}
    }
		
    public List<TimeBlock> getTimeBlocks(){
	return timeBlocks;
    }
    public String getWeek1Total(){
	return ""+dfn.format(week1Total);
    }
    public String getWeek2Total(){
	return ""+dfn.format(week2Total);
    }
    public String getWeek1AmountTotal(){
	return ""+dfn.format(week1AmountTotal);
    }
    public String getWeek2AmountTotal(){
	return ""+dfn.format(week2AmountTotal);
    }		
    public String getPayPeriodTotal(){
	double ret = 0;
	ret = week1Total+week2Total;
	return ""+dfn.format(ret);
    }
    public String getPayPeriodAmount(){
	double ret = 0;
	ret = week1AmountTotal+week2AmountTotal;
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
			if(!ret.isEmpty()) ret += ", ";
			ret += str+": "+dfn.format(one.getHours());
		    }
		}
	    }
	}
	return ret;
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
    public boolean hasAmountCodeWeek1(){
	return amountCodeWeek1 != null && amountCodeWeek1.size() > 0;
    }
    public boolean hasAmountCodeWeek2(){
	return amountCodeWeek2 != null && amountCodeWeek2.size() > 0;
    }				
    public Map<Integer, List<TimeBlock>> getDailyBlocks(){
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
    public Map<String, String> getAmountCodeWeek1(){
	Map<String, String> map2 = new TreeMap<>();
	if(hasAmountCodeWeek1()){
	    Set<String> keys = amountCodeWeek1.keySet();
	    for(String key:keys){
		double val = amountCodeWeek1.get(key);
		map2.put(key, dfn.format(val));
	    }
	}
	return map2;
    }
    public Map<String, String> getAmountCodeWeek2(){
	Map<String, String> map2 = new TreeMap<>();
	if(hasAmountCodeWeek2()){
	    Set<String> keys = amountCodeWeek2.keySet();
	    for(String key:keys){
		double val = amountCodeWeek2.get(key);
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
		if(accrual_id != null && !accrual_id.isEmpty()){
		    double hrs_total = one.getHours();
		    list.add(""+dfn.format(hrs_total));
		    try{
			int cd_id = Integer.parseInt(accrual_id);
			if(earnedAccrualTotals != null &&
			   earnedAccrualTotals.containsKey(cd_id)){
			    double hrs_earned = earnedAccrualTotals.get(cd_id);
			    list.add(""+dfn.format(hrs_earned));
			    if(hrs_earned > 0){
				hrs_total += hrs_earned;
				one.setHours(hrs_total);
			    }
			}
			else{
			    list.add("0.00"); // nothing earned
			}
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
	if(!pay_period_id.isEmpty()){
	    hl.setPay_period_id(pay_period_id);
	}
	String back = hl.find();
	if(back.isEmpty()){
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

    private void checkForWarnings(){
	checkForWarningsAfter();
	checkForWarningsBefore();
	checkWarningForCommute();
    }
    private void checkWarningForCommute(){
	if(dailyBlocks != null){
	    // all reg codes
	    // 18,19,20,21,22,23,93, 111,117,151,36,37,38,39,40,41
	    Set<String> comSet = new HashSet<>(Arrays.asList("157","158")); // regular codes
	    Set<String> regSet = new HashSet<>(Arrays.asList("1","18","19","20","21","22","23","36","37","38","39","40","41","93","111","117","151"));

	    Set<Integer> orderIdSet = dailyBlocks.keySet();
	    if(orderIdSet != null){
		for(Integer order_id:orderIdSet){
		    boolean hasRegular = false;
		    boolean hasCommute = false;
		    String day = "";
		    List<TimeBlock> blocks = dailyBlocks.get(order_id);
		    for(TimeBlock block:blocks){
			day = block.getDate();
			String code_id = block.getHour_code_id();
			if(!code_id.isEmpty()){
			    if(regSet.contains(code_id)){
				hasRegular = true;
			    }
			    else if(comSet.contains(code_id)){
				hasCommute = true;
			    }
			}
		    }
		    if(hasCommute && !hasRegular){
			String str = " The Sustainable Commute Benefit can only be claimed on days you work regular hours.  Please add a Regular Hours block or remove the Sustainable Commute block on "+day;
		    warnings.add(str);		    
		    }
		}
	    }
	}
    }    
    // after submission
    private void checkForWarningsAfter(){
	if(job == null){
	    getJobs();
	}
	if(week1Total > 0){
	    checkWeekWarnings(hourCodeWeek1, week1Total);
	    checkForExcessUse(week1Total, 1);						
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
	    checkForExcessUse(week2Total, 2);						
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
				    if(back.isEmpty()){
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
				    if(back.isEmpty()){
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
		if(str != null && !str.isEmpty()){
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
		System.err.println(" d_dif "+d_dif);
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
