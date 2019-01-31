package in.bloomington.timer;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.*;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;  
import in.bloomington.timer.list.*;
import in.bloomington.timer.bean.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JobTitlesAction extends TopAction{

		static final long serialVersionUID = 3800L;	
		static Logger logger = LogManager.getLogger(JobTitlesAction.class);
		//
		String jobsTitle = "New World Job Titles";
		Employee emp = null;
		String employee_number = "";
		List<String> jobTitles = null;

		public String execute(){
				String ret = SUCCESS;
				String back = doPrepare();
				if(!action.equals("")){
						HandleJobTitleUpdate hjtl = new HandleJobTitleUpdate();
						back = hjtl.process();
						/*
						getEmp();
						List<String> ones = emp.findJobTitlesFromNW();
						if(ones != null && ones.size() > 0){
								jobTitles = ones;
						}
						*/
				}				
				return ret;
		}
		public Employee getEmp(){
				if(emp == null){
						emp = new Employee();
						emp.setEmployee_number(employee_number);
				}
				return emp;
						
		}
		public void setEmp(Employee val){
				if(val != null){
						emp = val;
				}
		}
		public String getJobsTitle(){
				return jobsTitle;
		}
		public void setEmployeeNumber(String val){
				if(val != null && !val.equals(""))		
						employee_number = val;
		}
		public String getEmployeeNumber(){
				return employee_number;
		}
		public void setAction2(String val){
				if(val != null && !val.equals(""))		
						action = val;
		}
		public List<String> getJobTitles(){
				return jobTitles;
		}
		public boolean hasJobTitles(){
				return jobTitles != null && jobTitles.size() > 0;
		}

}





































