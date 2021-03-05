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
import in.bloomington.timer.report.TimesReport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReportTimesAction extends TopAction{

		static final long serialVersionUID = 1800L;	
		static Logger logger = LogManager.getLogger(ReportTimesAction.class);
		static final int startYear = CommonInc.reportStartYear; 
		//
		String outputType = "html";
		List<Department> departments = null;
		Department department = null, dept=null;
		TimesReport report = null;
		List<Integer> years = null;
		String department_id = ""; // parks=5
		List<Group> groups = null;
		Group group = null;
		String dept_id="", group_id="", salaryGroup_id="", pay_period_id="";
		String date_from="", date_to="", quarter="",year="";

		List<SalaryGroup> salaryGroups = null;
		List<PayPeriod> payPeriods = null;
		
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
				if(!action.isEmpty()){
						getReport();
						report.setPay_period_id(pay_period_id);
						report.setDepartment_id(dept_id);
						report.setGroup_id(group_id);
						report.setSalary_group_id(salaryGroup_id);
						report.setQuarter(quarter);
						report.setYear(year);
						report.setDate_from(date_from);
						report.setDate_to(date_to);
						///
						back = report.find();						
						if(!back.isEmpty()){
								addError(back);
						}
						else{
								List<String[]> all = report.getArrAll();
								if(all == null || all.size() <= 1){
										addMessage("No records found");
								}
								else{
										arrAll = all;
								}
						}
						if(outputType.equals("csv")){
								return "csv";
						}
				}
				else{
						getReport();
				}
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

		public String getDate_from(){
				return date_from;
    }
		public String getDate_to(){
				return date_to;
    }
		public String getOutputType(){
				return outputType;
		}
		public String getYear(){
				if(year.isEmpty())
						return "-1";
				return year;
		}
		public String getQuarter(){
				if(quarter.isEmpty())
						return "-1";
				return quarter;
		}
		public void setYear(String val){
				if(val != null && !val.equals("-1")){
						year = val;
				}
		}
		public void setQuarter(String val){
				if(val != null && !val.equals("-1")){
						quarter = val;
				}
		}		
		public void setDate_from(String val){
				if(val != null && !val.isEmpty()){
						date_from = val;
				}
		}
		public void setDate_to(String val){
				if(val != null && !val.isEmpty()){
						date_to = val;
				}
		}
		public void setOutputType(String val){
				if(val != null && !val.isEmpty()){
						outputType = val;
				}
		}
		public List<PayPeriod> getPayPeriods(){
				if(payPeriods == null){
						PayPeriodList tl = new PayPeriodList();
						tl.avoidFuturePeriods();
						tl.setLimit("10");
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
		public List<Integer> getYears(){
				if(years == null){
						int currentYear = Helper.getCurrentYear();
						years = new ArrayList<>();
						for(int yy=currentYear;yy >= startYear;yy--){// startYear=2018
								years.add(yy);
						}
				}
				return years;
		}
		public String getTimesTitle(){
				return timesTitle;
		}
		public void setAction2(String val){
				if(val != null && !val.isEmpty())		
						action = val;
		}
		
		//
		// needed only for csv output
		public String getFileName(){
				getDept();
				String filename = "";
				if(group != null){
						filename = group.getName().replace(' ','_');
				}
				else if(dept != null){
						filename = dept.getName().replace(' ','_');
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
				return !department_id.isEmpty();
		}
		public String getDepartment_id(){
				if(department_id.isEmpty()){
						return "-1";
				}
				return department_id;
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
		public void setDept_id(String val){
				if(val != null && !val.equals("-1")){
						dept_id = val;
				}
		}
		public void setPay_period_id(String val){
				if(val != null && !val.equals("-1")){
						pay_period_id = val;
				}
		}
		public String getPay_period_id(){
				if(pay_period_id.isEmpty()){
						return "-1";
				}
				return pay_period_id;
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
		public boolean hasDept(){
				return !dept_id.isEmpty();
		}		
		public String getDept_id(){
				if(dept_id.isEmpty()){
						return "-1";
				}
				return dept_id;
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





































