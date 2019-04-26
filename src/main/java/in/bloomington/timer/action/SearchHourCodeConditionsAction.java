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

public class SearchHourCodeConditionsAction extends TopAction{

		static final long serialVersionUID = 1800L;	
		static Logger logger = LogManager.getLogger(SearchHourCodeConditionsAction.class);
		//
		String conditionsTitle = "Hour Code Restrictions";
		List<Type> departments = null;
		List<HourCodeCondition> conditions = null;
		List<SalaryGroup> salaryGroups = null;
		List<HourCode> hourCodes = null;
		String dept_id="", salary_group_id = "", hour_code_id="";
		public String execute(){
				String ret = SUCCESS;
				String back = doPrepare();
				if(!action.equals("")){
						HourCodeConditionList hcl = new HourCodeConditionList();
						hcl.setDepartment_id(dept_id);
						hcl.setSalary_group_id(salary_group_id);
						hcl.setHour_code_id(hour_code_id);
						back = hcl.find();
						if(!back.equals("")){
								addError(back);
						}
						else{
								List<HourCodeCondition> ones = hcl.getConditions();
								if(ones != null && ones.size() > 0){
										conditions = ones;
										addMessage("Found "+ones.size());
								}
								else{
										addMessage("No match found");
										conditionsTitle = "No match found";
								}
						}
				}
				return ret;
		}
		public String getConditionsTitle(){
				return conditionsTitle;
		}
		public void setAction2(String val){
				if(val != null && !val.equals(""))		
						action = val;
		}
		public void setDept_id(String val){
				if(val != null && !val.equals(""))		
						dept_id = val;
		}
		public String getDept_id(){
				if(dept_id.equals("")){
						return "-1";
				}
				return dept_id;
		}
		public void setHour_code_id(String val){
				if(val != null && !val.equals(""))		
						hour_code_id = val;
		}
		public String getHour_code_id(){
				if(hour_code_id.equals("")){
						return "-1";
				}
				return hour_code_id;
		}		
		public void setSalary_group_id(String val){
				if(val != null && !val.equals(""))		
						salary_group_id = val;
		}
		public String getSalary_group_id(){
				if(salary_group_id.equals("")){
						return "-1";
				}
				return salary_group_id;
		}		
		
		public boolean hasConditions(){
				return conditions != null && conditions.size() > 0;
		}

		public List<HourCodeCondition> getConditions(){
				return conditions;
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
		public List<SalaryGroup> getSalaryGroups(){
				if(salaryGroups == null){
						SalaryGroupList sgl = new SalaryGroupList();
						String back = sgl.find();
						if(back.equals("")){
								List<SalaryGroup> ones = sgl.getSalaryGroups();
								if(ones != null && ones.size() > 0){
										salaryGroups = ones;
								}
						}
				}
				return salaryGroups;

		}
		public List<HourCode> getHourCodes(){
				if(hourCodes == null){
						HourCodeList sgl = new HourCodeList();
						String back = sgl.find();
						if(back.equals("")){
								List<HourCode> ones = sgl.getHourCodes();
								if(ones != null && ones.size() > 0){
										hourCodes = ones;
								}
						}
				}
				return hourCodes;

		}		
		
}





































