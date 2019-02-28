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
import in.bloomington.timer.report.TimesReport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReportTimesAction extends TopAction{

		static final long serialVersionUID = 1800L;	
		static Logger logger = LogManager.getLogger(ReportTimesAction.class);
		static final int startYear = 2017; // 
		//
		String outputType = "html";
		List<Department> departments = null;
		Department department = null;
		TimesReport report = null;
		List<Integer> years = null;
		String department_id = ""; // parks=5		
		String timesTitle = "Employee Pay Period Times ";
		Map<String, Set<String>> empJobs = null;
		Map<String, Set<String>> empCodes = null;
		Map<String, Map<String, Map<String, Map<String, String>>>> times = null;
		// Date-Range  Emp-Name     job        code        hours
		List<String> datesList = null;
		List<String[]> arrAll = null;		
		public String execute(){
				String ret = SUCCESS;
				String back = doPrepare();
				if(!action.equals("")){
						back = report.find();						
						if(!back.equals("")){
								addError(back);
						}
						else{
								outputType = report.getType();
								arrAll = report.getArrAll();
								if(arrAll == null && arrAll.size() == 0){
										addMessage("No records found");
								}
						}
						if(outputType.equals("csv")){
								return "csv";
						}
				}
				else{
						getReport();
				}
				return ret;
		}
		public TimesReport getReport(){ 
				if(report == null){
						report = new TimesReport();
						report.setDepartment_id(department_id);
				}		
				return report;
		}

		public void setReport(TimesReport val){
				if(val != null){
						report = val;
				}
		}		
		public void setOutputType(String val){
				if(val != null && !val.equals("-1")){
						outputType = val;
				}
		}
		public String getOutputType(){
				return outputType;
		}
		public List<Integer> getYears(){
				if(years == null){
						int currentYear = Helper.getCurrentYear();
						years = new ArrayList<>();
						for(int yy=currentYear;yy >= startYear;yy--){
								years.add(yy);
						}
				}
				return years;
		}
		public String getTimesTitle(){
				return timesTitle;
		}
		public void setAction2(String val){
				if(val != null && !val.equals(""))		
						action = val;
		}
		//
		// needed only for csv output
		public String getFileName(){
				getDepartment();
				String filename = "";
				if(department != null){
						filename = department.getName().replace(' ','_');
				}
				filename +="_times_"+Helper.getToday().replace(' ','_')+".csv";
				return filename;
		}

		public void setDepartment_id(String val){
				if(val != null && !val.equals("-1")){
						department_id = val;
				}
		}
		public boolean hasDepartment(){
				return !department_id.equals("");
		}
		public String getDepartment_id(){
				if(department_id.equals("")){
						return "-1";
				}
				return department_id;
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
		public List<Department> getDepartments(){
				if(departments == null){
						DepartmentList gsl = new DepartmentList();
						gsl.ignoreSpecialDepts();
						String back = gsl.find();
						if(back.equals("")){
								List<Department> ones = gsl.getDepartments();
								if(ones != null && ones.size() > 0){
										departments = ones;
								}
						}
				}
				return departments;
		}				
		public String getDateRange(){
				String str = report.getStart_date()+" - "+report.getEnd_date();
				return str;
		}
		public List<String[]> getArrAll(){
				return arrAll;
		}
		public boolean hasData(){
				return arrAll != null && !arrAll.isEmpty();
		}		
}





































