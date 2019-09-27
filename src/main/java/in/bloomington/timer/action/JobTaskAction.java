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
import in.bloomington.timer.list.*;
import in.bloomington.timer.bean.*;
import in.bloomington.timer.HandleProfile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class JobTaskAction extends TopAction{

		static final long serialVersionUID = 1800L;	
		static Logger logger = LogManager.getLogger(JobTaskAction.class);
		//
		String add_employee_id = "", employee_number="";
		String dept_id = "";
		Employee emp = null;
		Department dept = null;
		JobTask jobTask = null, oldJob=null;
		String jobTasksTitle = "Current jobs";
		List<PayPeriod> payPeriods = null;
		List<Type> salaryGroups = null;
		List<Position> positions = null;
		List<Employee> employees = null;
		List<Group> groups = null;
		List<Type> departments = null;
		public String execute(){
				String ret = SUCCESS;
				String back = doPrepare();
				if(action.equals("Save")){
						back = jobTask.doSaveAll();
						if(!back.equals("")){
								addError(back);
								prepareData();
						}
						else{
								ret="edit";
								addMessage("Added Successfully");
						}
				}				
				else if(action.startsWith("Save")){
						back = jobTask.doUpdate();
						if(!back.equals("")){
								addError(back);
						}
						else{
								ret="edit";
								addMessage("Saved Successfully");
						}
				}
				else if(action.startsWith("Find")){
						back = fillJobInfo();
						if(!back.equals("")){
								addError(back);
						}
						prepareData();
				}
				else{		
						getJobTask();
						if(!id.equals("")){
								back = jobTask.doSelect();
								if(!back.equals("")){
										addError(back);
								}
								ret ="edit";
						}
						else{
								prepareData();
								if(oldJob != null){
										jobTask.setSalary_group_id(oldJob.getSalary_group_id());
										jobTask.setEffective_date(oldJob.getEffective_date());
										jobTask.setWeekly_regular_hours(oldJob.getWeekly_regular_hours());
										jobTask.setComp_time_weekly_hours(oldJob.getComp_time_weekly_hours());
										jobTask.setComp_time_factor(oldJob.getComp_time_factor());
										jobTask.setHoliday_comp_factor(oldJob.getHoliday_comp_factor());
										jobTask.setClock_time_required(oldJob.getClock_time_required());
								}
						}
				}
				return ret;
		}
		void prepareData(){
				getEmp(); // employee other than user
				if(emp != null && emp.hasDepartment()){
						dept = emp.getDepartment();
						dept_id = dept.getId();
						if(emp.hasJobs()){
								List<JobTask> jobs = emp.getJobs();
								oldJob = jobs.get(0); // to get some info from
						}
				}
				else{
						getUser();
						if(user != null && !user.isAdmin()){
								dept = user.getDepartment();
								if(dept != null){
										dept_id = dept.getId();
								}
								getGroups();
						}
				}						
		}
		public JobTask getJobTask(){ 
				if(jobTask == null){
						jobTask = new JobTask();
						jobTask.setId(id);
				}
				if(!add_employee_id.equals("")){
						jobTask.setEmployee_id(add_employee_id);
				}
				return jobTask;
		}
	 
		public void setJobTask(JobTask val){
				if(val != null){
						jobTask = val;
				}
		}
		public Employee getEmp(){
				if(!add_employee_id.equals("")){
						Employee one = new Employee(add_employee_id);
						String back = one.doSelect();
						if(back.equals("")){
								emp = one;
						}
				}
				return emp;
		}
		public String getJobTasksTitle(){
				return jobTasksTitle;
		}
		public void setAction2(String val){
				if(val != null && !val.equals(""))		
						action = val;
		}
		public void setAdd_employee_id(String val){
				if(val != null && !val.equals(""))		
						add_employee_id = val;
		}
		public void setEmployee_number(String val){
				if(val != null && !val.equals(""))		
						employee_number = val;
		}
		public String getEmployee_number(){
				return employee_number;
		}
		public boolean hasEmployeeNumber(){
				return !employee_number.equals("");
		}

		public List<Type> getSalaryGroups(){
				TypeList tl = new TypeList("salary_groups");
				String back = tl.find();
				if(back.equals("")){
						List<Type> ones = tl.getTypes();
						if(ones != null && ones.size() > 0){
								salaryGroups = ones;
						}
				}
				return salaryGroups;
		}
		public List<Position> getPositions(){
				PositionList tl = new PositionList();
				String back = tl.find();
				if(back.equals("")){
						List<Position> ones = tl.getPositions();
						if(ones != null && ones.size() > 0){
								positions = ones;
						}
				}
				return positions;
		}
		public List<Employee> getEmployees(){
				EmployeeList tl = new EmployeeList();
				tl.setActiveOnly();
				String back = tl.find();
				if(back.equals("")){
						List<Employee> ones = tl.getEmployees();
						if(ones != null && ones.size() > 0){
								employees = ones;
						}
				}
				return employees;
		}		
		public List<Type> getDepartments(){
				if(departments == null){
						TypeList tl = new TypeList();
						tl.setTable_name("departments");
						tl.setActiveOnly();
						String back = tl.find();
						if(back.equals("")){
								List<Type> ones = tl.getTypes();
								if(ones != null && ones.size() > 0){
										departments = ones;
								}
						}
				}
				return departments;
		}
		public List<PayPeriod> getPayPeriods(){
				if(payPeriods == null){
						PayPeriodList tl = new PayPeriodList();
						tl.setTwoPeriodsAheadOnly();
						tl.setLimit("5");
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
		public List<Group> getGroups(){
				if(groups == null && !dept_id.equals("")){
						GroupList gl = new GroupList();
						gl.setDepartment_id(dept_id);
						gl.setActiveOnly();
						String back = gl.find();
						if(back.equals("")){
								List<Group> ones = gl.getGroups();
								if(ones != null && ones.size() > 0){
										groups = ones;
								}
						}
				}
				return groups;
		}
		public boolean hasGroups(){
				getGroups();
				return groups != null && groups.size() > 0;
		}
		public boolean isDeptSpecified(){
				return !dept_id.equals("");
		}
		public String getDept_id(){
				return dept_id;
		}
		public String fillJobInfo(){
				String msg = "";
				if(employee_number.equals("")){
						msg = "Employee Number not set ";
						return msg;
				}
				Profile pp = null;
				HandleProfile hp = new HandleProfile();
				msg = hp.processOne(employee_number);
				if(msg.equals("")){
						if(hp.hasProfiles()){
								pp = hp.getOneProfile();
								getJobTask();
								jobTask.setWeekly_regular_hours(pp.getStWeeklyHrs());
								jobTask.setComp_time_weekly_hours(pp.getCompTimeAfter());
								jobTask.setComp_time_factor(pp.getCompTimeMultiple());
								jobTask.setHoliday_comp_factor(pp.getHolidayTimeMultiple());
								jobTask.setJobTitle(pp.getJobTitle());
								jobTask.setSalary_group_name(pp.getSalary_group_name());
								if(pp.getSalary_group_name().equals("Temp")){
										jobTask.setClock_time_required(true);
								}
						}
						else{
								addMessage("Not found or not active in NW");
						}
				}
				return msg;
		}

		
}





































