package in.bloomington.timer;

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
		Set<String> employeeSet = null;
		List<TmwrpRun> allRuns = null;
		Map<Employee, List<TmwrpRun>> employeeRuns = new TreeMap<>();
    public String execute(){
				String ret = SUCCESS;
				String back = "";
				if(!action.equals("") && !department_id.equals("")){
						if(department == null)
								getDepartment();
						// utilities department
						if(department_id.equals("36")) 
								isUtil = true;
						else if(department_id.equals("3"))
								isHand = true;
						back = prepareEmployeeSet();
						if(!back.equals("")){
								addError(back);
						}
						getEmployees();
						back = doProcess();
						if(allRuns == null || allRuns.size() == 0){
								back = "No records found";
								addMessage(back);
								if(type.equals("single")){ // one department
										ret = "single";
								}
								return ret;
						}
						if(true){
								if(csvOutput){
										prepareCsvs();
										if(payPeriod.hasTwoDifferentYears()){
												if(isHand){ 
														ret = "handEndYearCsv";
												}
												else{ // end year 
														ret = "csv"; 
												}
										}
										else{
												ret = "csv";
										}
								}
								if(type.equals("single")){ // one department
										ret = "single";
								}
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
				if(val != null && !val.equals(""))		
						action = val;
    }
    public void setSource(String val){
				if(val != null && !val.equals(""))		
						source = val;
    }		
    public void setType(String val){
				if(val != null && !val.equals(""))		
						type = val;
    }		
    public void setDepartment_id(String val){
				if(val != null && !val.equals("-1"))		
						department_id = val;
    }
    public String getDepartment_id(){
				if(department_id.equals("")){
						if(user != null ){
								if(user.isAdmin()){
										if(department_id.equals(""))
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
				if(val != null && !val.equals(""))		
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
				if(group_id.equals(""))
						return "-1";
				return group_id;
		}
    public String getPay_period_id(){
				if(pay_period_id.equals("")){
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
				if(employeeSet == null){
						prepareEmployeeSet();
				}
				if(employees == null){
						// isHand is set here
						back = findEmployees();
						if(!back.equals("")){
								return back;
						}
				}
				if(employees == null){
						back = " Employees list not found ";
						return back;
				}
				for(Employee emp:employees){
						String emp_num = emp.getEmployee_number();
						emp.setPay_period_id(pay_period_id);
						System.err.println(" emp "+emp.getFull_name());
						TmwrpRunList trl = new TmwrpRunList(emp.getId(),
																								pay_period_id);
						back = trl.find();
						if(!back.equals("")){
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
						tl.avoidFuturePeriods();
						String back = tl.find();
						if(back.equals("")){
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
						if(back.equals("")){
								List<Department> ones = tl.getDepartments();
								if(ones != null && ones.size() > 0){
										departments = ones;
								}
						}
				}
				return departments;
    }
    public Department getDepartment(){
				if(department == null && !department_id.equals("")){
						Department one = new Department(department_id);
						String back = one.doSelect();
						if(back.equals("")){
								department = one;
						}
				}
				return department;
    }
    public String prepareEmployeeSet(){
				getDepartment();
				getPayPeriod();
				String back = "";
				if(employeeSet == null && department != null && payPeriod != null){
						ProfileList pfl = new ProfileList(payPeriod.getEnd_date(),
																							department.getRef_id());
						back = pfl.findEmployeeSet();
						if(back.equals("")){
								Set<String> set = pfl.getEmployeeSet();
								if(set != null && set.size() > 0){
										employeeSet = set;
								}
						}
				}
				return back;
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
								if(!group_id.equals("")){
										el.setGroup_id(group_id);
								}
								String back = el.find();
								if(back.equals("")){
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
								if(!group_id.equals("")){
										el.setGroup_id(group_id);
								}
								back = el.find();
								if(back.equals("")){
										List<Employee> ones = el.getEmployees();
										if(ones != null && ones.size() > 0){
												employees = new ArrayList<>();
												if(employeeSet == null || employeeSet.isEmpty()){
														back = " Employee numbers not set ";
														return back;
												}
												for(Employee one:ones){
														String empNum = one.getEmployee_number();
														if(employeeSet.contains(empNum)){
																employees.add(one);
														}
												}
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
				if(groups == null && !department_id.equals("")){
						GroupList gl = new GroupList();
						gl.setDepartment_id(department_id);
						gl.setPay_period_id(pay_period_id);
						String back = gl.find();
						if(back.equals("")){
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
						if(pay_period_id.equals("")){
								PayPeriodList ppl = new PayPeriodList();
								ppl.setApproveSuitable();
								String back = ppl.find();
								if(back.equals("")){
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
								if(back.equals("")){
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
						if(back.equals("")){
								List<PayPeriod> ones = ppl.getPeriods();
								if(ones != null && ones.size() > 0){
										currentPayPeriod = ones.get(0);
										if(pay_period_id.equals("")){
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
						if(isHand){
								prepareHandEndYearCsv();
						}
						else{
								prepareEndYearCsv();
						}
				}
				else{
						if(isHand){
								prepareHandCsv();
						}
						else{
								prepareCsv();
						}
				}
    }
    //
    // almost all use this csv
    // 
    void prepareCsv(){
				allCsvLines = new ArrayList<>();
				String line  = ",,,,,,,,,,,";
				String line2 = ",,,,,,,,,,"; // monetary
				/**
				for(PayPeriodProcess process:processes){
						String csvLine = process.getEmployee().getEmployee_number()+",";
						csvLine += process.get2WeekNetRegular()+",";
						csvLine += process.getRegCode()+",";
						csvLine += payPeriod.getEnd_date()+",";
						csvLine += line;						
						if(process.hasMultipleJobs()){
								csvLine += process.getJob_name();
						}
						allCsvLines.add(csvLine);
						// 
						Hashtable<String, Double> nreg = process.getAll();
						if(!nreg.isEmpty()){
								Set<String> keySet = nreg.keySet();
								for(String key:keySet){
										double dd = nreg.get(key);
										csvLine = process.getEmployee().getEmployee_number()+",";
										csvLine += dd+","+key+",";
										csvLine += payPeriod.getEnd_date()+",";
										csvLine += line;										
										if(process.hasMultipleJobs()){
												csvLine += process.getJob_name();
										}
										allCsvLines.add(csvLine);	
								}
						}
						nreg = process.getAllMonetary();
						if(!nreg.isEmpty()){
								Set<String> keySet = nreg.keySet();
								for(String key:keySet){
										double dd = nreg.get(key);
										csvLine = process.getEmployee().getEmployee_number()+",";
										csvLine += "0,"+key+",";
										csvLine += payPeriod.getEnd_date()+",";
										csvLine += dd+",";
										csvLine += line2;								
										if(process.hasMultipleJobs()){
												csvLine += process.getJob_name();
										}
										allCsvLines.add(csvLine);	
								}
						}				
				}
				*/
		}
    void prepareEndYearCsv(){
				allCsvLines = new ArrayList<>();
				String line  = ",,,,,,,,,,,";
				String line2 = ",,,,,,,,,,";
				/**
				for(PayPeriodProcess process:processes){
						if(process.getNetRegularHoursForFirstPay() > 0){
								String csvLine = process.getEmployee().getEmployee_number()+",";
								csvLine += process.getNetRegularHoursForFirstPay()+",";
								csvLine += process.getRegCode()+",";
								csvLine += payPeriod.getFirstPayEndDate()+",";
								csvLine += line;								
								if(process.hasMultipleJobs()){
										csvLine += process.getJob_name();
								}
								allCsvLines.add(csvLine);								
						}
						if(process.getNetRegularHoursForSecondPay() > 0){
								String csvLine = process.getEmployee().getEmployee_number()+",";
								csvLine += process.getNetRegularHoursForSecondPay()+",";
								csvLine += process.getRegCode()+",";
								csvLine += payPeriod.getEnd_date()+",";
								csvLine += line;								
								if(process.hasMultipleJobs()){
										csvLine += process.getJob_name();
								}
								allCsvLines.add(csvLine);								
						}						
						if(process.hasNonRegularFirstPay()){
								Hashtable<String, Double> nreg = process.getNonRegularFirstPay();
								if(!nreg.isEmpty()){
										Set<String> keySet = nreg.keySet();
										for(String key:keySet){
												double dd = nreg.get(key);
												String csvLine = process.getEmployee().getEmployee_number()+",";
												csvLine += dd+","+key+",";
												csvLine += payPeriod.getFirstPayEndDate()+",";
												csvLine += line;												
												if(process.hasMultipleJobs()){
														csvLine += process.getJob_name();
												}
												allCsvLines.add(csvLine);	
										}
								}
						}
						if(process.hasNonRegularSecondPay()){
								Hashtable<String, Double> nreg = process.getNonRegularSecondPay();
								if(!nreg.isEmpty()){
										Set<String> keySet = nreg.keySet();
										for(String key:keySet){
												double dd = nreg.get(key);
												String csvLine = process.getEmployee().getEmployee_number()+",";
												csvLine += dd+","+key+",";
												csvLine += payPeriod.getEnd_date()+",";
												csvLine += line;												
												if(process.hasMultipleJobs()){
														csvLine += process.getJob_name();
												}
												allCsvLines.add(csvLine);	
										}
								}
						}
						if(process.hasMonetaryFirstPay()){
								Hashtable<String, Double> nreg = process.getMonetaryFirstPay();
								if(!nreg.isEmpty()){
										Set<String> keySet = nreg.keySet();
										for(String key:keySet){
												double dd = nreg.get(key);
												String csvLine = process.getEmployee().getEmployee_number()+",";
												csvLine += "0,"+key+",";
												csvLine += payPeriod.getFirstPayEndDate()+",";
												csvLine += dd+",";
												csvLine += line2;												
												if(process.hasMultipleJobs()){
														csvLine += process.getJob_name();
												}
												allCsvLines.add(csvLine);	
										}
								}
						}
						if(process.hasMonetarySecondPay()){						
								Hashtable<String, Double> nreg = process.getMonetarySecondPay();
								if(!nreg.isEmpty()){
										Set<String> keySet = nreg.keySet();
										for(String key:keySet){
												double dd = nreg.get(key);
												String csvLine = process.getEmployee().getEmployee_number()+",";
												csvLine += "0,"+key+",";
												csvLine += payPeriod.getFirstPayEndDate()+",";
												csvLine += dd+",";
												csvLine += line2;												
												if(process.hasMultipleJobs()){
														csvLine += process.getJob_name();
												}
												allCsvLines.add(csvLine);	
										}
								}								
						}
				}
				*/
    }
    // 
    void prepareHandCsv(){
				allCsvLines = new ArrayList<>();
				String line =",,,,,", line2 =",,,,,,";
				/**
				if(processes != null && processes.size() > 0){
						for(PayPeriodProcess process:processes){
								Hashtable<CodeRef, String> hash = process.getTwoWeekHandHash();
								if(hash != null && !hash.isEmpty()){
										Set<CodeRef> keySet = hash.keySet();
										for(CodeRef key:keySet){
												String dd = hash.get(key);
												String csvLine = process.getEmployee().getEmployee_number()+",";
												csvLine += dd+","+key.getNw_code()+",";
												csvLine += payPeriod.getEnd_date()+",";
												csvLine += line;
												csvLine += key.getGl_value()+",";
												csvLine += line2;
												allCsvLines.add(csvLine);
										}
								}
								Hashtable<String, Double> hash2	= process.getAllMonetary();
								if(hash2 != null && !hash2.isEmpty()){
										Set<String> keySet = hash2.keySet();
										for(String key:keySet){
												double dd = hash2.get(key);
												String csvLine = process.getEmployee().getEmployee_number()+",";
												csvLine += "0,"+key+",";
												csvLine += payPeriod.getEnd_date()+",";
												csvLine += dd+",";
												csvLine += line;
												csvLine += line2;
												allCsvLines.add(csvLine);
										}
								}								
						}
				}
				*/
		}
		// ToDo
		void prepareHandEndYearCsv(){
				
		}
				
}





































