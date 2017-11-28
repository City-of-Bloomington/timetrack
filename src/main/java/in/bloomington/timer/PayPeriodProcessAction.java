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


public class PayPeriodProcessAction extends TopAction{

		static final long serialVersionUID = 4320L;	
		static Logger logger = LogManager.getLogger(PayPeriodProcessAction.class);
		DecimalFormat df = new DecimalFormat("###.00");
		//
		PayPeriod payPeriod = null, currentPayPeriod=null;
		Department department = null;
		List<PayPeriod> payPeriods = null;		
		String timeBlocksTitle = "Time Details";
		String pay_period_id = "";
		String department_id = "";
		String source="", outputType="html";
		boolean isHand = false, csvOutput = false;
		Hashtable<String, Profile> profMap = null;
		List<Department> departments = null;
		List<Profile> profiles = null;
		List<BenefitGroup> benefitGroups = null;
		List<Employee> employees = null, noDataEmployees;
		List<PayPeriodProcess> processes = null;
		HolidayList holys = null;
		public String execute(){
				String ret = SUCCESS;
				String back = doPrepare();
				if(!back.equals("")){
						try{
								HttpServletResponse res = ServletActionContext.getResponse();
								String str = url+"Login";
								res.sendRedirect(str);
								return super.execute();
						}catch(Exception ex){
								System.err.println(ex);
						}	
				}
				if(!action.equals("")){
						back = doProcess();
						if(!back.equals("")){
								addActionError(back);
						}
						else{
								if(csvOutput){
										if(payPeriod.hasTwoDifferentYears()){
												if(isHand){ // ToDo
														ret = "handEndYearCsv";
												}
												else{
														ret = "endYearCsv";
												}
 										}
										else{
												if(isHand){
														ret = "handCsv";
												}
												else{
														ret = "csv";
												}
										}
								}
						}
				}
				return ret;
		}
		public String getTimeBlocksTitle(){
				return timeBlocksTitle;
		}
		public void setAction2(String val){
				if(val != null && !val.equals(""))		
						action = val;
		}
		public void setDepartment_id(String val){
				if(val != null && !val.equals("-1"))		
						department_id = val;
		}
		public String getDepartment_id(){
				if(department_id.equals(""))
						return "-1";
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
		public void setSource(String val){
				if(val != null && !val.equals(""))		
						source = val;
		}
		public String getPay_period_id(){
				return pay_period_id;
		}
		public String getSource(){
				return source;
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
				if(employees == null){
						// isHand is set here
						getEmployees();
				}
				if(employees == null){
						back = " Employees list not found ";
						return back;
				}
				if(benefitGroups == null){
						getBenefitGroups();
				}
				if(benefitGroups == null){
						back = " benefit groups data not found ";
						return back;
				}
				if(profiles == null){
						getProfiles();
				}
				if(profiles == null){
						back = " could not find employees profiles ";
						return back;
				}
				if(holys == null){
						getHolidayList();
				}
				for(Employee emp:employees){
						String emp_num = emp.getEmployee_number();
						Profile profile = null;
						if(!emp_num.equals("") && profMap.containsKey(emp_num)){
								profile = profMap.get(emp_num);
						}
						System.err.println(" emp "+emp.getUser());						
						System.err.println(" profile "+profile);
						PayPeriodProcess one =
								new PayPeriodProcess(emp,
																		 profile,
																		 payPeriod,
																		 holys,
																		 isHand, // HAND flag
																		 csvOutput);			
						msg = one.find();
						if(!msg.equals("")){
								if(msg.startsWith("No time")){
										if(noDataEmployees == null){
												noDataEmployees = new ArrayList<>();
										}
										noDataEmployees.add(emp);
								}
								else{
										if(!back.equals("")) back +=", ";
										back += msg;
								}
						}
						else{
								if(processes == null){
										processes = new ArrayList<>();
								}
								processes.add(one);
						}
				}
				return back;
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
		public List<BenefitGroup> getBenefitGroups(){
				if(benefitGroups == null){
						BenefitGroupList tl = new BenefitGroupList();
						String back = tl.find();
						if(back.equals("")){
								List<BenefitGroup> ones = tl.getBenefitGroups();
								if(ones != null && ones.size() > 0){
										benefitGroups = ones;
								}
						}
				}
				return benefitGroups;
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
		public List<Profile> getProfiles(){
				getDepartment();
				getPayPeriod();
				getBenefitGroups();
				if(profiles == null && department != null && payPeriod != null){
						ProfileList pfl = new ProfileList(payPeriod.getEnd_date(),
																					department.getRef_id(),
																					benefitGroups);
						String back = pfl.find();
						if(back.equals("")){
								List<Profile> ones = pfl.getProfiles();
								if(ones != null && ones.size() > 0){
										profiles = ones;
								}
						}
						if(profiles != null){
								profMap = new Hashtable<String, Profile>();
								for(Profile one:profiles){
										profMap.put(one.getEmployee_number(), one);
								}
						}
				}
				return profiles;
		}
		public List<Employee> getEmployees(){
				if(employees == null){
						getDepartment();
						if(department != null){
								isHand = department.getName().equals("HAND");
								EmployeeList el = new EmployeeList();
								el.setDepartment_id(department.getId());
								el.setHasEmployeeNumber();
								String back = el.find();
								if(back.equals("")){
										List<Employee> ones = el.getEmployees();
										if(ones != null && ones.size() > 0){
												employees = ones;
										}
								}
						}
				}
				return employees;
		}
		public boolean hasNoDataEmployees(){
				return noDataEmployees != null;
		}
		public List<Employee> getNoDataEmployees(){
				return noDataEmployees;
		}
		public PayPeriod getPayPeriod(){
				//
				// if pay period is not set, we look for current one
				//
				if(payPeriod == null){
						if(pay_period_id.equals("")){
								PayPeriodList ppl = new PayPeriodList();
								ppl.currentOnly();
								String back = ppl.find();
								if(back.equals("")){
										List<PayPeriod> ones = ppl.getPeriods();
										if(ones != null && ones.size() > 0){
												payPeriod = ones.get(0);
												currentPayPeriod = payPeriod;
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
		public HolidayList getHolidayList(){
				if(holys == null){
						getPayPeriod();
						if(payPeriod != null){
								holys = new HolidayList(payPeriod.getStart_date(),
																				payPeriod.getEnd_date());
								String back = holys.find();
						}
				}
				return holys;
		}
		public List<PayPeriodProcess> getProcesses(){
				return processes;
		}
		public boolean hasProcesses(){
				return processes != null && processes.size() > 0;
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

}





































