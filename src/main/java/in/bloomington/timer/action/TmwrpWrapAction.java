package in.bloomington.timer.action;

/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.*;
import java.io.*;
import java.text.*;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;  
import in.bloomington.timer.bean.*;
import in.bloomington.timer.list.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.timewarp.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class TmwrpWrapAction extends TopAction{

    static final long serialVersionUID = 4320L;	
    static Logger logger = LogManager.getLogger(TmwrpWrapAction.class);
    DecimalFormat df = new DecimalFormat("###.00");
    //
    Department department = null;
    List<PayPeriod> payPeriods = null;		
    String timeBlocksTitle = "TimeWarp";
    String pay_period_id = "", source="";
    String department_id = "", group_id="";
    String type=""; // for custom
    String outputType="html";
    boolean isHand = false, csvOutput = false, isUtil = false;
    PayPeriod payPeriod = null, currentPayPeriod=null;
    List<Group> groups = null;
    Hashtable<String, Profile> profMap = null;
    List<Department> departments = null;
    List<Employee> employees = null, noDataEmployees = null;

    List<String> allCsvLines = null;
    List<Employee> empsWithNoEmpNum = null;
    // Set<String> employeeSet = null;
    Map<Employee, List<TmwrpRun>> employeeRuns = new TreeMap<>();
    public String execute(){
	String ret = SUCCESS;
	String back = doPrepare("tmwrpWrap.action");
	if(!action.isEmpty() && !department_id.isEmpty()){
	    if(department == null)
		getDepartment();
	    if(department.isUtilities()) 
		isUtil = true;
	    else if(department.isHand())
		isHand = true;
	    /**
	       back = prepareEmployeeSet();
	       if(!back.isEmpty()){
	       addError(back);
	       }
	    */
	    getEmployees();
	    back = doProcess();
	    if(!csvOutput){
		if(employeeRuns == null || employeeRuns.size() == 0){
		    back = "No records found";
		    addMessage(back);
		    if(type.equals("single")){ // one department
			ret = "single";
		    }
		    return ret;
		}
	    }
	    else{
		prepareCsvs();
		ret = "csv";
	    }
	}
	else{
	    if(type.equals("single")){ // one department
		ret = "single";
	    }
	}
	return ret;
    }
    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
	    action = val;
    }
    public void setSource(String val){
	if(val != null && !val.isEmpty())		
	    source = val;
    }		
    public void setType(String val){
	if(val != null && !val.isEmpty())		
	    type = val;
    }		
    public void setDepartment_id(String val){
	if(val != null && !val.equals("-1"))		
	    department_id = val;
    }
    public String getDepartment_id(){
	if(department_id.isEmpty()){
	    if(user != null ){
		if(user.isAdmin()){
		    if(department_id.isEmpty())
			return "-1";
		}
		else{
		    department = user.getDepartment();
		    if(department != null){
			department_id = department.getId();
		    }
		}
	    }
	}				
	return department_id;
    }
    public void setPay_period_id(String val){
	if(val != null && !val.isEmpty())		
	    pay_period_id = val;
    }
    public void setIsHand(boolean val){
	if(val)
	    isHand = val;
    }
    public void setOutputType(String val){
	if(val != null){
	    outputType = val;
	    if(val.equals("csv"))
		csvOutput = true;
	    else
		csvOutput = false;
	}
    }
    public void setCsvOutuput(boolean val){
	if(val)
	    csvOutput = val;
    }
    public boolean getIsHand(){
	return isHand;
    }
    public String getOutputType(){
	return outputType;
    }
    public String getSource(){
	return source;
    }		
    public void setGroup_id(String val){
	if(val != null && !val.equals("-1") && !val.equals("all"))
	    group_id = val;
    }
    public String getGroup_id(){
	if(group_id.isEmpty())
	    return "-1";
	return group_id;
    }
    public String getPay_period_id(){
	if(pay_period_id.isEmpty()){
	    getPayPeriod();
	}
	return pay_period_id;
    }
    public String doProcess(){
	String back = "", msg="";
	if(payPeriod == null){
	    getPayPeriod();
	}
	if(payPeriod == null){
	    back = " could not determine pay period";
	    return back;
	}
	/**
	   if(employeeSet == null){
	   prepareEmployeeSet();
	   }
	*/
	if(employees == null){
	    // isHand is set here
	    back = findEmployees();
	    if(!back.isEmpty()){
		return back;
	    }
	}
	if(employees == null){
	    back = " Employees list not found ";
	    return back;
	}
	for(Employee emp:employees){
	    // String emp_num = emp.getEmployee_number();
	    emp.setPay_period_id(pay_period_id);
	    System.err.println(" emp "+emp.getFull_name());
	    // we need to include department
	    TmwrpRunList trl = new TmwrpRunList(emp.getId(),
						pay_period_id);
	    if(!department_id.isEmpty()){
		trl.setDepartment_id(department.getId());
	    }
	    back = trl.find();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		//
		// some employees may have multiple jobs
		//
		List<TmwrpRun> ones = trl.getTmwrpRuns();
		if(ones != null && ones.size() > 0){
		    employeeRuns.put(emp, ones);
		}
		else{
		    if(noDataEmployees == null)
			noDataEmployees = new ArrayList<>();
		    noDataEmployees.add(emp);
		}
	    }
	}
	return back;
    }
    public boolean hasEmployeeRuns(){
	return employeeRuns != null && !employeeRuns.isEmpty();
    }
    public Map<Employee, List<TmwrpRun>> getEmployeeRuns(){
	return employeeRuns;
    }
    public List<PayPeriod> getPayPeriods(){
	if(payPeriods == null){
	    getPayPeriod(); // so that we can initialize the list
	    PayPeriodList tl = new PayPeriodList();
	    // tl.avoidFuturePeriods();
	    tl.setTwoPeriodsAheadOnly();
	    String back = tl.find();
	    if(back.isEmpty()){
		List<PayPeriod> ones = tl.getPeriods();
		if(ones != null && ones.size() > 0){
		    payPeriods = ones;
		}
	    }
	}
	return payPeriods;
    }
    public List<Department> getDepartments(){
	if(departments == null){
	    DepartmentList tl = new DepartmentList();
	    tl.setActiveOnly();
	    String back = tl.find();
	    if(back.isEmpty()){
		List<Department> ones = tl.getDepartments();
		if(ones != null && ones.size() > 0){
		    departments = ones;
		}
	    }
	}
	return departments;
    }
    public Department getDepartment(){
	if(department == null && !department_id.isEmpty()){
	    Department one = new Department(department_id);
	    String back = one.doSelect();
	    if(back.isEmpty()){
		department = one;
	    }
	}
	return department;
    }
    public List<Employee> getEmpsWithNoEmpNum(){
	if(empsWithNoEmpNum == null){
	    getDepartment();
	    if(department != null){
		EmployeeList el = new EmployeeList();
		el.setDepartment_id(department.getId());
		el.setPay_period_id(pay_period_id);
		el.setHasNoEmployeeNumber();
		el.setActiveOnly();
		if(!group_id.isEmpty()){
		    el.setGroup_id(group_id);
		}
		String back = el.find();
		if(back.isEmpty()){
		    List<Employee> ones = el.getEmployees();
		    if(ones != null && ones.size() > 0){
			empsWithNoEmpNum = ones;
		    }
		}
	    }
	}
	return empsWithNoEmpNum;
    }
    public List<Employee> getEmployees(){
	if(employees == null){
	    findEmployees();
	}
	return employees;
    }
    public String findEmployees(){
	String back = "";
	if(employees == null){
	    getDepartment();
	    if(department != null){
		isHand = department.getName().equals("HAND");
		EmployeeList el = new EmployeeList();
		el.setDepartment_id(department.getId());
		el.setPay_period_id(pay_period_id);
		el.setHasEmployeeNumber();
		el.setActiveOnly();
		if(!group_id.isEmpty()){
		    el.setGroup_id(group_id);
		}
		back = el.find();
		if(back.isEmpty()){
		    List<Employee> ones = el.getEmployees();
		    if(ones != null && ones.size() > 0){
			employees = ones; // all
		    }
		}
	    }
	}
	return back;
    }
    public boolean hasNoDataEmployees(){
	return noDataEmployees != null;
    }
    public List<Employee> getNoDataEmployees(){
	return noDataEmployees;
    }
    public List<Group> getGroups(){
	findGroups();
	return groups;
    }
    public boolean hasGroups(){
	findGroups();
	return groups != null && groups.size() > 0;
    }
    void findGroups(){
	if(groups == null && !department_id.isEmpty()){
	    GroupList gl = new GroupList();
	    gl.setDepartment_id(department_id);
	    gl.setPay_period_id(pay_period_id);
	    String back = gl.find();
	    if(back.isEmpty()){
		List<Group> ones = gl.getGroups();
		if(ones != null && ones.size() > 0)
		    groups = ones;
	    }
	}
    }
    public PayPeriod getPayPeriod(){
	//
	// if pay period is not set, we look for previous one
	//
	if(payPeriod == null){
	    if(pay_period_id.isEmpty()){
		PayPeriodList ppl = new PayPeriodList();
		ppl.setApproveSuitable();
		String back = ppl.find();
		if(back.isEmpty()){
		    List<PayPeriod> ones = ppl.getPeriods();
		    if(ones != null && ones.size() > 0){
			payPeriod = ones.get(0);
			pay_period_id = payPeriod.getId();
		    }
		}
	    }
	    else{
		PayPeriod one = new PayPeriod(pay_period_id);
		String back = one.doSelect();
		if(back.isEmpty()){
		    payPeriod = one;
		}
	    }
	}
	return payPeriod;
    }
    public PayPeriod getCurrentPayPeriod(){
	//
	if(currentPayPeriod == null){
	    PayPeriodList ppl = new PayPeriodList();
	    ppl.currentOnly();
	    String back = ppl.find();
	    if(back.isEmpty()){
		List<PayPeriod> ones = ppl.getPeriods();
		if(ones != null && ones.size() > 0){
		    currentPayPeriod = ones.get(0);
		    if(pay_period_id.isEmpty()){
			pay_period_id = currentPayPeriod.getId();
			payPeriod = currentPayPeriod;
		    }
		}
	    }
	}
	return currentPayPeriod;
    }
    public String getCsvFileName(){
				
	String dt = payPeriod.getEnd_date();
	dt = Helper.getDateAfter(dt, 3); // 3 days after the last pay period
	dt = Helper.getYymmddDate(dt);
	String dept_name = department.getName();
	if(dept_name != null){
	    // get rid of spaces
	    dept_name = dept_name.replaceAll("\\s","_");
	}
	String ret = "timesheet_"+dt+"_"+dept_name+".csv";
	return ret;
    }
    public List<String> getCsvLines(){
	return allCsvLines; 
    }
    public boolean hasEmpsWithNoEmpNum(){
	getEmpsWithNoEmpNum();
	return empsWithNoEmpNum != null && empsWithNoEmpNum.size() > 0;
    }
    /**
     * prepare the list of csv lines to add to csv file
     */
    void prepareCsvs(){
	if(payPeriod.hasTwoDifferentYears()){
	    prepareEndYearCsv();
	}
	else{
	    prepareCsv();
	}
    }
    //
    // works for all
    // 
    void prepareCsv(){
	allCsvLines = new ArrayList<>();
	String line =",,,,", line2 =",,,,,,";
	String utilChar = "";
	if(isUtil){
	    utilChar ="u"; // append to all earn codes for Utility depart
	}
	if(employeeRuns != null && employeeRuns.size() > 0){
	    Set<Employee> empSet = employeeRuns.keySet();
	    for(Employee emp:empSet){
		List<TmwrpRun> runs = employeeRuns.get(emp);
		emp.setPay_period_id(pay_period_id);
		// boolean multipleJobs = emp.hasMultipleJobs();
		for(TmwrpRun run:runs){
		    JobTask job = run.getDocument().getJob();
		    SalaryGroup sgrp = job.getSalaryGroup();
		    //}
		    if(run.hasCsvHourRows()){
			Map<CodeRef, Double> map = run.getCsvHourRows();
			Set<CodeRef> refSet = map.keySet();
			for(CodeRef refKey:refSet){
			    double dd = map.get(refKey);
			    String csvLine = emp.getEmployee_number()+","+df.format(dd)+","+utilChar+refKey.getNw_code()+","+payPeriod.getEnd_date()+",";
			    csvLine += line;
			    if(isHand){
				csvLine += refKey.getGl_value()+",";
			    }
			    else{
				csvLine +=",";
			    }
			    csvLine += line2;
			    if(sgrp != null && sgrp.isTemporary()){
				csvLine += job.getName();
			    }
			    allCsvLines.add(csvLine);														
			}
		    }
		    if(run.hasCsvAmountRows()){
			Map<CodeRef, Double> map = run.getCsvAmountRows();
			Set<CodeRef> refSet = map.keySet();
			for(CodeRef refKey:refSet){
			    double dd = map.get(refKey);
			    String csvLine = emp.getEmployee_number()+",0,"+utilChar+refKey.getNw_code()+","+payPeriod.getEnd_date()+",";
			    csvLine += df.format(dd)+",";
			    csvLine += line; // gl_value not needed here
			    csvLine += line2;
			    if(sgrp != null && sgrp.isTemporary()){
				csvLine += job.getName();
			    }														
			    allCsvLines.add(csvLine);														
			}
		    }										
		}
	    }
	}
    }
    /**
     * End of the year is special case where there will be two
     * pay periods one start before end of the year and ends
     * at the end of the year and the second start on the
     * new year start date and ends at the end of pay period
     */
    void prepareEndYearCsv(){
	allCsvLines = new ArrayList<>();
	String line =",,,,", line2 =",,,,,,";
	String utilChar = "";
	if(isUtil){
	    utilChar ="u"; // append to all earn codes for Utility depart
	}
	if(employeeRuns != null && employeeRuns.size() > 0){
	    Set<Employee> empSet = employeeRuns.keySet();
	    for(Employee emp:empSet){
		List<TmwrpRun> runs = employeeRuns.get(emp);
		emp.setPay_period_id(pay_period_id);
		// boolean multipleJobs = emp.hasMultipleJobs();
		for(TmwrpRun run:runs){
		    JobTask job = run.getDocument().getJob();
		    SalaryGroup sgrp = job.getSalaryGroup();
		    if(run.hasCycle1HourRows()){
			// first pay period ends at 12/31/yyyy
			Map<CodeRef, Double> map = run.getCycle1HourRows();
			Set<CodeRef> refSet = map.keySet();
			for(CodeRef refKey:refSet){
			    double dd = map.get(refKey);
			    String csvLine = emp.getEmployee_number()+","+df.format(dd)+","+utilChar+refKey.getNw_code()+","+payPeriod.getFirstPayEndDate()+",";
			    csvLine += line;
			    if(isHand){
				csvLine += refKey.getGl_value()+",";
			    }
			    else{
				csvLine +=",";
			    }
			    csvLine += line2;
			    if(sgrp.isTemporary()){
				csvLine += job.getName();
			    }
			    allCsvLines.add(csvLine);														
			}
		    }
		    if(run.hasCycle1AmountRows()){
			Map<CodeRef, Double> map = run.getCycle1AmountRows();
			Set<CodeRef> refSet = map.keySet();
			for(CodeRef refKey:refSet){
			    double dd = map.get(refKey);
			    String csvLine = emp.getEmployee_number()+",0,"+utilChar+refKey.getNw_code()+","+payPeriod.getFirstPayEndDate()+",";
			    csvLine += df.format(dd)+",";
			    csvLine += line; // gl_value not needed here
			    csvLine += line2;
			    if(sgrp != null && sgrp.isTemporary()){
				csvLine += job.getName();
			    }														
			    allCsvLines.add(csvLine);														
			}
		    }
		    if(run.hasCycle2HourRows()){
			Map<CodeRef, Double> map = run.getCycle2HourRows();
			Set<CodeRef> refSet = map.keySet();
			for(CodeRef refKey:refSet){
			    double dd = map.get(refKey);
			    String csvLine = emp.getEmployee_number()+","+df.format(dd)+","+utilChar+refKey.getNw_code()+","+payPeriod.getEnd_date()+",";
			    csvLine += line;
			    if(isHand){
				csvLine += refKey.getGl_value()+",";
			    }
			    else{
				csvLine +=",";
			    }
			    csvLine += line2;
			    if(sgrp != null && sgrp.isTemporary()){
				csvLine += job.getName();
			    }
			    allCsvLines.add(csvLine);														
			}
		    }
		    if(run.hasCycle2AmountRows()){
			Map<CodeRef, Double> map = run.getCycle2AmountRows();
			Set<CodeRef> refSet = map.keySet();
			for(CodeRef refKey:refSet){
			    double dd = map.get(refKey);
			    String csvLine = emp.getEmployee_number()+",0,"+utilChar+refKey.getNw_code()+","+payPeriod.getEnd_date()+",";
			    csvLine += df.format(dd)+",";
			    csvLine += line; // gl_value not needed here
			    csvLine += line2;
			    if(sgrp != null && sgrp.isTemporary()){
				csvLine += job.getName();
			    }														
			    allCsvLines.add(csvLine);														
			}
		    }
		}
	    }
	}
    }
				
}





































