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
		// String employmentType = ""; // All, Full Time, Temp
		String department_id = ""; // for restriction purpose
		List<JobTask> jobs = null;
		List<Department> departments = null;
		List<Group> groups = null;
		Department dept = null;
		Group group = null;
		String dept_id="", group_id="", salaryGroup_id=""; 
		String jobsTitle = "Current Employee Jobs ";
		List<SalaryGroup> salaryGroups = null;
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
				// for non-admin users limited roles
				getUser();
				if(user != null && !user.isAdmin()){
						if(user.hasDepartment()){
								dept = user.getDepartment();
								dept_id = dept.getId();
								department_id = dept_id;
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
				String filename = "";
				getGroup();
				if(group == null){
						getDept();
				}
				if(group != null){
						filename += group.getName().replace(' ','_');
				}				
				else if(dept != null){
						filename += dept.getName().replace(' ','_');
				}
				filename +="_jobs_"+Helper.getToday().replace(' ','_')+".csv";
				return filename;
		}
		/**
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
		*/
		public void setDept_id(String val){
				if(val != null && !val.equals("-1")){
						dept_id = val;
				}
		}
		public void setDepartment_id(String val){
				if(val != null && !val.equals("-1")){
						department_id = val;
				}
		}		
		public void setGroup_id(String val){
				if(val != null && !val.equals("-1")){
						group_id = val;
				}
		}
		public void setSalaryGroup_id(String val){
				if(val != null && !val.equals("-1")){
						salaryGroup_id = val;
				}
		}		
		public boolean hasDepartment(){
				return !department_id.isEmpty();
		}
		public boolean hasDept(){
				return !dept_id.isEmpty();
		}		
		public String getDept_id(){
				if(dept_id.isEmpty()){
						return "-1";
				}
				return dept_id;
		}
		public String getDepartment_id(){
				if(department_id.isEmpty()){
						return "-1";
				}
				return department_id;
		}		
		public String getGroup_id(){
				if(group_id.isEmpty()){
						return "-1";
				}
				return group_id;
		}
		public boolean hasGroup(){
				return !group_id.isEmpty();
		}
		public String getSalaryGroup_id(){
				if(salaryGroup_id.isEmpty()){
						return "-1";
				}
				return salaryGroup_id;
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
		public Department getDept(){
				if(dept == null && !dept_id.isEmpty()){
						dept = new Department(dept_id);
						dept.doSelect();
				}
				return dept;
		}
		public Group getGroup(){
				if(group == null && !group_id.isEmpty()){
						group = new Group(dept_id);
						group.doSelect();
				}
				return group;
		}		
		public String findJobs(){
				String back = "";
				if(dept_id.isEmpty()){
						back ="You need to pick a department";
						return back;
				}
				JobTaskList jtl = new JobTaskList();
				if(group_id.isEmpty()){
						jtl.setDepartment_id(dept_id);
				}
				else{
						jtl.setGroup_id(group_id);
				}
				jtl.setActiveOnly();
				jtl.setNotExpired();
				jtl.setOrderByEmployee();
				if(!salaryGroup_id.isEmpty()){
						jtl.setSalary_group_id(salaryGroup_id);
				}
				back = jtl.find();
				if(back.isEmpty()){
						List<JobTask> ones = jtl.getJobs();
						if(ones != null && ones.size() > 0)
								jobs = ones;
				}
				return back;
		}
		public List<SalaryGroup> getSalaryGroups(){
				//
				SalaryGroupList gml = new SalaryGroupList();
				String back = gml.find();
				if(back.isEmpty()){
						List<SalaryGroup> ones = gml.getSalaryGroups();
						if(ones != null && ones.size() > 0){
								salaryGroups = ones;
						}
				}
				return salaryGroups;
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
		
}





































