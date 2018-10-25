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
import in.bloomington.timer.list.*;
import in.bloomington.timer.bean.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class JobTaskAction extends TopAction{

		static final long serialVersionUID = 1800L;	
		static Logger logger = LogManager.getLogger(JobTaskAction.class);
		//
		String add_employee_id = "";
		JobTask jobTask = null;
		List<JobTask> jobTasks = null;
		String jobTasksTitle = "Current jobs";
		List<Type> salaryGroups = null;
		List<Position> positions = null;
		List<Employee> employees = null;
		List<Group> groups = null;
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
				clearAll();
				if(action.equals("Save")){
						back = jobTask.doSave();
						if(!back.equals("")){
								addActionError(back);
								addError(back);
						}
						else{
								addMessage("Added Successfully");
								addActionMessage("Added Successfully");
						}
				}				
				else if(action.startsWith("Save")){
						back = jobTask.doUpdate();
						if(!back.equals("")){
								addActionError(back);
								addError(back);
						}
						else{
								addMessage("Saved Successfully");
								addActionMessage("Saved Successfully");
						}
				}
				else{		
						getJobTask();
						if(!id.equals("")){
								back = jobTask.doSelect();
								if(!back.equals("")){
										addError(back);
										addActionError(back);
								}								
						}
				}
				return ret;
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
		// todo
		public List<JobTask> getJobTasks(){
				JobTaskList tl = new JobTaskList();
				// tl.setActiveOnly();
				String back = tl.find();
				List<JobTask> ones = tl.getJobTasks();
				if(ones != null && ones.size() > 0){
						jobTasks = ones;
				}
				return jobTasks;
		}
		public boolean hasJobTasks(){
				getJobTasks();
				return jobTasks != null && jobTasks.size() > 0;
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
		
}





































