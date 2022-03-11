package in.bloomington.timer.action;

/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.*;
import java.util.Arrays;
import java.util.stream.Collectors;
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
		String add_employee_id = "", employee_number="", first_name="", last_name="";
		String dept_id = "", effective_date="";
		boolean employee_found = false;
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
		List<String> jobTitles = null; // for new job from NW
		List<JobTask> jobs = null;
		List<Position> shortListPositions = null;
		List<Position> currentPositions = null;
		List<String> empDeptIds = null;
		public String execute(){
				String ret = SUCCESS;
				String back = doPrepare();
				if(action.equals("Save")){
						back = jobTask.doSaveAll();
						if(!back.isEmpty()){
								addError(back);
								prepareData();
						}
						else{// for adding another job
								effective_date = jobTask.getEffective_date();
								ret="view";
								id = jobTask.getId();
								addMessage("Added Successfully");
						}
				}				
				else if(action.startsWith("Save")){
						back = jobTask.doUpdate();
						if(!back.isEmpty()){
								addError(back);
						}
						else{
								id = jobTask.getId();
								ret="view";
								addMessage("Saved Successfully");
								prepareData();
						}
				}
				else if(action.startsWith("Edit")){
						getJobTask();
						back = jobTask.doSelect();
						if(!back.isEmpty()){
								addError(back);
						}
						else{
								id = jobTask.getId();
								ret="edit";
						}
				}				
				else if(action.startsWith("Find")){
						prepareData();
				}
				else{		
						getJobTask();
						if(!id.isEmpty()){
								back = jobTask.doSelect();
								if(!back.isEmpty()){
										addError(back);
								}
								prepareData();
								ret ="view";
						}
						else{
								prepareData();
								if(oldJob != null){
										jobTask.setSalary_group_id(oldJob.getSalary_group_id());
										if(!jobTask.hasEffective_date()){
												jobTask.setEffective_date(oldJob.getEffective_date());
										}
										jobTask.setWeekly_regular_hours(oldJob.getWeekly_regular_hours());
										jobTask.setComp_time_weekly_hours(oldJob.getComp_time_weekly_hours());
										jobTask.setComp_time_factor(oldJob.getComp_time_factor());
										jobTask.setHoliday_comp_factor(oldJob.getHoliday_comp_factor());
										jobTask.setClock_time_required(oldJob.getClock_time_required());
										if(effective_date.isEmpty()){
												effective_date = jobTask.getEffective_date();
										}
								}
						}
				}
				return ret;
		}
		void prepareData(){
				getEmp(); // employee other than user
				boolean includeFutureDate = id.isEmpty();
				if(emp != null && emp.hasDepartment(includeFutureDate)){
						dept = emp.getDepartment();
						dept_id = dept.getId();
						employee_number = emp.getEmployee_number();
						first_name = emp.getFirst_name();
						last_name = emp.getLast_name();
						if(effective_date.isEmpty() && emp.hasGroups()){ 
								GroupEmployee gemp = emp.getGroupEmployee();
								if(gemp != null){
										effective_date = gemp.getEffective_date();
								}
						}
						if(emp.hasAllJobs()){
								jobs = emp.getAllJobs();
								oldJob = jobs.get(0); // to get some info from
								currentPositions = new ArrayList<>();
								for(JobTask jj:jobs){
										Position pp = jj.getPosition();
										if(pp != null)
												currentPositions.add(pp);
								}
						}
						if(emp.hasMultipleDepts()){
								empDeptIds = emp.getAlEmpDeptIds();
						}
						if(id.isEmpty()){ 
								fillJobInfo();
						}
						getPositions();
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
				if(!add_employee_id.isEmpty()){
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
				if(!add_employee_id.isEmpty()){
						Employee one = new Employee(add_employee_id);
						String back = one.doSelect();
						if(back.isEmpty()){
								emp = one;
						}
				}
				return emp;
		}
		public String getJobTasksTitle(){
				return jobTasksTitle;
		}
		public void setAction2(String val){
				if(val != null && !val.isEmpty())		
						action = val;
		}
		public void setAdd_employee_id(String val){
				if(val != null && !val.isEmpty())		
						add_employee_id = val;
		}
		public void setEmployee_number(String val){
				if(val != null && !val.isEmpty())		
						employee_number = val;
		}
		// needed for new employee
		public void setEffective_date(String val){
				if(val != null && !val.isEmpty())		
						effective_date = val;
		}		
		public String getEmployee_number(){
				return employee_number;
		}
		public String getEffective_date(){
				return effective_date;
		}		
		public boolean hasEmployeeNumber(){
				return !employee_number.isEmpty();
		}
		public boolean isEmployeeFound(){
				return employee_found;
		}
		public boolean hasJobs(){
				return jobs != null && jobs.size() > 0;
		}
		public List<JobTask> getJobs(){
				return jobs;
		}
		public List<Type> getSalaryGroups(){
				TypeList tl = new TypeList("salary_groups");
				String back = tl.find();
				if(back.isEmpty()){
						List<Type> ones = tl.getTypes();
						if(ones != null && ones.size() > 0){
								salaryGroups = ones;
						}
				}
				return salaryGroups;
		}
		void findPositions(){
				PositionList tl = new PositionList();
				String back = tl.find();
				if(back.isEmpty()){
						List<Position> ones = tl.getPositions();
						if(ones != null && ones.size() > 0){
								positions = ones;
						}
				}
		}		
		public List<Position> getPositions(){
				if(positions == null){
						findPositions();
						findMissingPositions();
				}
				return positions;
		}
		public boolean hasShortListPositions(){
				return shortListPositions != null && shortListPositions.size() > 0;
		}
		// using streams
		public List<Position> getShortListPositions(){
				if(positions != null && jobTitles != null){
						List<Position> plist = positions.stream()
								.filter(pos -> jobTitles.stream()
												.anyMatch(jtitle ->
																	 jtitle.equals(pos.getName())))
								.collect(Collectors.toList());
						shortListPositions = plist;
						//
						// now let reduced it to shorter list
						// if the employee has some jobs
						//
						if(currentPositions != null &&
							 currentPositions.size() > 0
							 && shortListPositions != null &&
							 shortListPositions.size() > 0){
								List<Position> pplist = shortListPositions.stream()
										.filter(pp -> currentPositions.stream()
														.noneMatch(pos ->
																	 pos.equals(pp)))
										.collect(Collectors.toList());
								if(pplist != null && pplist.size()> 0){
										shortListPositions = pplist;
								}
								else{ // if all exusted
										shortListPositions = new ArrayList<>();
								}
						}
				}
				return shortListPositions;
		}
		void findMissingPositions(){
				if(positions != null && jobTitles != null){
						List<String> slist = jobTitles.stream()
								.filter(str -> positions.stream()
												.noneMatch(pos ->
																	pos.getName().equals(str)))
								.collect(Collectors.toList());
						if(slist != null && slist.size()> 0){
								Position pos = new Position();
								pos.doSaveBatch(slist);
								findPositions(); // repopulate
						}
						getShortListPositions();								
				}
		}
		public List<Type> getDepartments(){
				if(departments == null){
						TypeList tl = new TypeList();
						tl.setTable_name("departments");
						tl.setActiveOnly();
						String back = tl.find();
						if(back.isEmpty()){
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
						if(back.isEmpty()){
								List<PayPeriod> ones = tl.getPeriods();
								if(ones != null && ones.size() > 0){
										payPeriods = ones;
								}
						}
				}
				return payPeriods;
		}
		public List<Group> getGroups(){
				if(groups == null && !dept_id.isEmpty()){
						GroupList gl = new GroupList();
						gl.setDepartment_id(dept_id);
						gl.setActiveOnly();
						String back = gl.find();
						if(back.isEmpty()){
								List<Group> ones = gl.getGroups();
								if(ones != null && ones.size() > 0){
										groups = ones;
								}
						}
				}
				return groups;
		}
		public List<Group> getAllGroups(){
				if(groups == null && empDeptIds != null){
						GroupList gl = new GroupList();
						for(String idd:empDeptIds){
								gl.setDepartment_id(idd);
						}
						gl.setActiveOnly();
						String back = gl.find();
						if(back.isEmpty()){
								List<Group> ones = gl.getGroups();
								if(ones != null && ones.size() > 0){
										groups = ones;
								}
						}
				}
				else{
						groups = getGroups();
				}
				return groups;
		}		
		public boolean hasGroups(){
				// getGroups();
				getAllGroups();
				return groups != null && groups.size() > 0;
		}
		public boolean isDeptSpecified(){
				return !dept_id.isEmpty();
		}
		public String getDept_id(){
				return dept_id;
		}
		public String fillJobInfo(){
				String msg = "";
				boolean isTemp = false;
				Profile pp = null;
				boolean emp_num_needed = employee_number.isEmpty()? true:false;
				
				HandleProfile hp = new HandleProfile();
				msg = hp.processOne(effective_date,
														employee_number,
														first_name,
														last_name);
				if(msg.isEmpty()){
						if(hp.hasProfiles()){
								pp = hp.getOneProfile();
								employee_found = true;
								if(emp_num_needed){
										employee_number = pp.getEmployee_number();
								}
								getJobTask();
								jobTask.setWeekly_regular_hours(pp.getStWeeklyHrs());
								jobTask.setComp_time_weekly_hours(pp.getCompTimeAfter());
								jobTask.setComp_time_factor(pp.getCompTimeMultiple());
								jobTask.setHoliday_comp_factor(pp.getHolidayTimeMultiple());
								jobTask.setJobTitle(pp.getJobTitle());
								jobTask.setSalary_group_name(pp.getSalary_group_name());
								//
								if(pp.getSalary_group_name().equals("Temp")){
										jobTask.setClock_time_required(true);
										isTemp = true;
								}
								if(isTemp){
										msg = hp.processJobs(employee_number, effective_date);
										jobTitles = hp.getJobTitles();
								}
								if(emp_num_needed){
										if(emp != null){
												emp.setEmployee_number(employee_number);
												emp.doUpdate();
										}
								}
						}
						else{
								addMessage("Not found or not active in NW");
						}
				}
				return msg;
		}

		/**
			// parks
			select distinct(p.id), p.name name from positions p join jobs j on p.id=j.position_id join groups g on g.id = j.group_id where g.department_id=5 order by name;
		 */
		
}





































