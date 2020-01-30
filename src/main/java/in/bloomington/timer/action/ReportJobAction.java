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
import in.bloomington.timer.util.*;
import in.bloomington.timer.timewarp.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReportJobAction extends TopAction{

		static final long serialVersionUID = 1800L;	
		static Logger logger = LogManager.getLogger(ReportJobAction.class);
		static final int startYear = 2017; // 
		//
		String outputType = "html";
		String employmentType = ""; // All, Full Time, Temp
		List<JobTask> jobs = null;
		List<Department> departments = null;
		Department department = null;
		String department_id = ""; // parks=5		
		String jobsTitle = "Current Employee Jobs ";
		public String execute(){
				String ret = SUCCESS;
				String back = doPrepare();
				if(!action.isEmpty()){
						back = findJobs();						
						if(!back.isEmpty()){
								addError(back);
						}
						else{
								if(jobs != null && jobs.size() > 0){
										addMessage("Found "+jobs.size()+" jobs");
								}										
						}
						if(outputType.equals("csv")){
								return "csv";
						}						
				}
				return ret;
		}
		public void setOutputType(String val){
				if(val != null && !val.equals("-1")){
						outputType = val;
				}
		}
		public String getOutputType(){
				return outputType;
		}

		public String getJobsTitle(){
				return jobsTitle;
		}
		public void setAction2(String val){
				if(val != null && !val.isEmpty())		
						action = val;
		}
		// 
		public List<JobTask> getJobs(){
				return jobs;
		}
		public boolean hasJobs(){
				return jobs != null && jobs.size() > 0;
		}		
		// needed only for csv output
		public String getFileName(){
				getDepartment();
				String filename = "";
				if(department != null){
						filename = department.getName().replace(' ','_');
				}
				filename +="_jobs_"+Helper.getToday().replace(' ','_')+".csv";
				return filename;
		}
		public String getEmploymentType(){
				if(employmentType.isEmpty())
						return "-1";
				return employmentType;
		}
		public void setEmploymentType(String val){
				if(val != null && !val.equals("-1")){
						employmentType = val; // salary_group_id = 3 for Temp
				}
		}
		public void setDepartment_id(String val){
				if(val != null && !val.equals("-1")){
						department_id = val;
				}
		}
		public boolean hasDepartment(){
				return !department_id.isEmpty();
		}
		public String getDepartment_id(){
				if(department_id.isEmpty()){
						return "-1";
				}
				return department_id;
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
		public List<Department> getDepartments(){
				if(departments == null){
						DepartmentList gsl = new DepartmentList();
						gsl.ignoreSpecialDepts();
						String back = gsl.find();
						if(back.isEmpty()){
								List<Department> ones = gsl.getDepartments();
								if(ones != null && ones.size() > 0){
										departments = ones;
								}
						}
				}
				return departments;
		}				
		public String findJobs(){
				String back = "";
				if(department_id.isEmpty()){
						back ="You need to pick a department";
						return back;
				}
				JobTaskList jtl = new JobTaskList();
				jtl.setDepartment_id(department_id); // parks department only
				jtl.setActiveOnly();
				jtl.setNotExpired();
				jtl.setOrderByEmployee();
				if(employmentType.startsWith("Temp")){
						jtl.setSalary_group_id("3");
						jobsTitle = jobsTitle+" (Temp)";
						
				}
				else if(employmentType.startsWith("Non Temp")){
						jtl.setNonTemp();
						jobsTitle = jobsTitle+" (Full Time)";						
				}
				back = jtl.find();
				if(back.isEmpty()){
						List<JobTask> ones = jtl.getJobs();
						if(ones != null && ones.size() > 0)
								jobs = ones;
				}
				return back;
		}
}





































