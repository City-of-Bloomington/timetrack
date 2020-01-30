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

public class SearchReasonConditionsAction extends TopAction{

		static final long serialVersionUID = 1L;	
		static Logger logger = LogManager.getLogger(SearchReasonConditionsAction.class);
		//
		String conditionsTitle = "Code Reason Restrictions";
		List<Type> departments = null;
		List<CodeReasonCondition> conditions = null;
		List<SalaryGroup> salaryGroups = null;
		List<HourCode> hourCodes = null;
		List<EarnCodeReason> reasons = null;		
		String dept_id="",
				salary_group_id = "",
				reason_id="",
				group_id="",
				hour_code_id="";
		public String execute(){
				String ret = SUCCESS;
				String back = doPrepare();
				if(!action.isEmpty()){
						CodeReasonConditionList crl = new CodeReasonConditionList();
						crl.setDepartment_id(dept_id);
						crl.setSalary_group_id(salary_group_id);
						crl.setHour_code_id(hour_code_id);
						crl.setReason_id(reason_id);
						crl.setGroup_id(group_id);
						back = crl.find();
						if(!back.isEmpty()){
								addError(back);
						}
						else{
								List<CodeReasonCondition> ones = crl.getConditions();
								if(ones != null && ones.size() > 0){
										conditions = ones;
										addMessage("Found "+ones.size()+" records");
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
				if(val != null && !val.isEmpty())		
						action = val;
		}
		public void setDept_id(String val){
				if(val != null && !val.equals("-1"))		
						dept_id = val;
		}
		public String getDept_id(){
				if(dept_id.isEmpty()){
						return "-1";
				}
				return dept_id;
		}
		public void setHour_code_id(String val){
				if(val != null && !val.equals("-1"))		
						hour_code_id = val;
		}
		public String getHour_code_id(){
				if(hour_code_id.isEmpty()){
						return "-1";
				}
				return hour_code_id;
		}		
		public void setSalary_group_id(String val){
				if(val != null && !val.equals("-1"))		
						salary_group_id = val;
		}
		public String getSalary_group_id(){
				if(salary_group_id.isEmpty()){
						return "-1";
				}
				return salary_group_id;
		}
		public void setGroup_id(String val){
				if(val != null && !val.equals("-1"))		
						group_id = val;
		}
		public String getGroup_id(){
				if(group_id.isEmpty()){
						return "-1";
				}
				return group_id;
		}
		public void setReason_id(String val){
				if(val != null && !val.equals("-1"))		
						reason_id = val;
		}
		public String getReason_id(){
				if(reason_id.isEmpty()){
						return "-1";
				}
				return reason_id;
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
		public List<SalaryGroup> getSalaryGroups(){
				if(salaryGroups == null){
						SalaryGroupList sgl = new SalaryGroupList();
						String back = sgl.find();
						if(back.isEmpty()){
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
						sgl.setEarnTypesOnly(); // only earned or overtime
						sgl.setRecord_method("Time");
						String back = sgl.find();
						if(back.isEmpty()){
								List<HourCode> ones = sgl.getHourCodes();
								if(ones != null && ones.size() > 0){
										hourCodes = ones;
								}
						}
				}
				return hourCodes;
		}
		public List<EarnCodeReason> getReasons(){
				if(reasons == null){
						EarnCodeReasonList tl = new EarnCodeReasonList();
						String back = tl.find();
						if(back.isEmpty()){
								List<EarnCodeReason> ones = tl.getReasons();
								if(ones != null && ones.size() > 0){
										reasons = ones;
								}
						}
				}
				return reasons;
		}		
		public List<CodeReasonCondition> getConditions(){
				return conditions;
		}		
		public boolean hasConditions(){
				return conditions != null && conditions.size() > 0;
		}
}





































