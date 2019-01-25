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
import in.bloomington.timer.util.*;
import in.bloomington.timer.timewarp.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReportParkAction extends TopAction{

		static final long serialVersionUID = 1800L;	
		static Logger logger = LogManager.getLogger(ReportParkAction.class);
		static final int startYear = 2017; // 
		//
		String outputType = "html";
		String employmentType = ""; // All, Full Time, Temp
		List<JobTask> jobs = null;
		String reportTitle = "Parks Current Employees and Jobs ";
		public String execute(){
				String ret = SUCCESS;
				String back = doPrepare();
				if(!action.equals("")){
						back = findJobs();						
						if(!back.equals("")){
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

		public String getReportTitle(){
				return reportTitle;
		}
		public void setAction2(String val){
				if(val != null && !val.equals(""))		
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
				String filename="parks_jobs_report_"+Helper.getToday().replace("/","_")+".csv";
				return filename;
		}
		public String getEmploymentType(){
				if(employmentType.equals(""))
						return "-1";
				return employmentType;
		}
		public void setEmploymentType(String val){
				if(val != null && !val.equals("-1")){
						employmentType = val; // salary_group_id = 3 for Temp
				}
		}		
		public String findJobs(){
				String back = "";
				JobTaskList jtl = new JobTaskList();
				jtl.setDepartment_id("5"); // parks department only
				jtl.setActiveOnly();
				jtl.setNotExpired();
				if(employmentType.startsWith("Temp")){
						jtl.setSalary_group_id("3");
				}
				else if(employmentType.startsWith("Non Temp")){
						jtl.setNonTemp();
				}
				back = jtl.find();
				if(back.equals("")){
						List<JobTask> ones = jtl.getJobs();
						if(ones != null && ones.size() > 0)
								jobs = ones;
				}
				return back;
		}
}





































