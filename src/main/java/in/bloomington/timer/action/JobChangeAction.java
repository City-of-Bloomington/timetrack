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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JobChangeAction extends TopAction{

		static final long serialVersionUID = 2100L;	
		static Logger logger = LogManager.getLogger(JobChangeAction.class);
		String department_id="";
		String related_employee_id="";
		Employee relatedEmployee = null;
		List<Type> departments = null;
		JobTask jobTask = null;
		List<JobTask> jobs = null;
		List<PayPeriod> payPeriods = null;
		List<Type> salaryGroups = null;
		List<Position> positions = null;
		public String execute(){
				String ret = SUCCESS;
				String back = doPrepare();
				if(action.startsWith("Change")){ 
						back = jobTask.doChange();
						if(!back.equals("")){
								addError(back);
						}
						else{
								addMessage("changed Successfully");
								related_employee_id = jobTask.getEmployee_id();
								getJobs();
								return "pickJob";
						}
				}
				else{
						if(!id.equals("")){
								getJobTask();
								back = jobTask.doSelect();
								if(!back.equals("")){
										addError(back);
								}
						}
						else{
								getJobs();
								// one job only
								if(jobs != null){
										if(jobs.size() == 1){
												jobTask = jobs.get(0);
										}
										else if(jobs.size() > 1){
												return "pickJob";
										}
								}
								if(jobTask == null){
										getJobTask();
								}
						}
				}
				return ret;
		}
	 public List<JobTask> getJobs(){
			 if(jobs == null && !related_employee_id.equals("")){
					 JobTaskList jtl = new JobTaskList();
					 jtl.setEmployee_id(related_employee_id);
					 jtl.setCurrentOnly();
					 jtl.setActiveOnly();
					 String back = jtl.find();
					 if(back.equals("")){
							 List<JobTask> ones = jtl.getJobs();
							 if(ones != null && ones.size() > 0){
									 jobs = ones;
							 }
					 }
			 }
			 return jobs;
		}
		public JobTask getJobTask(){ 
				if(jobTask == null){
						jobTask = new JobTask(id);
				}		
				return jobTask;
		}

		public void setJobTask(JobTask val){
				if(val != null){
						jobTask = val;
				}
		}

		public void setAction2(String val){
				if(val != null && !val.equals(""))		
						action = val;
		}
		public void setRelated_employee_id(String val){
				if(val != null && !val.equals("-1"))		
						related_employee_id = val;
		}		
		public void setDepartment_id(String val){
				if(val != null && !val.equals("-1"))		
						department_id = val;
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
}





































