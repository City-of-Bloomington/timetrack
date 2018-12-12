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
import in.bloomington.timer.bean.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class HourCodeConditionAction extends TopAction{

		static final long serialVersionUID = 850L;	
		static Logger logger = LogManager.getLogger(HourCodeConditionAction.class);
		//
		HourCodeCondition hourcodeCondition = null;
		List<HourCodeCondition> conditions = null;
		String hourcodeConditionsTitle = "Current hour code restrictions";
		List<HourCode> hourcodes = null;
		List<Type> departments = null;
		List<Type> salaryGroups = null;
		List<Group> groups = null;
		String department_id = "";
		public String execute(){
				String ret = SUCCESS;
				String back = doPrepare();
				if(action.equals("Save")){
						back = hourcodeCondition.doSave();
						if(!back.equals("")){
								addActionError(back);
								addError(back);
						}
						else{
								addMessage("Added Successfully");
								id = hourcodeCondition.getId();
						}
				}				
				else if(action.startsWith("Save")){
						back = hourcodeCondition.doUpdate();
						if(!back.equals("")){
								addError(back);
						}
						else{
								addMessage("Saved Successfully");
						}
				}
				else{		
						getHourcodeCondition();
						if(!id.equals("")){
								back = hourcodeCondition.doSelect();
								if(!back.equals("")){
										addError(back);
								}								
						}
				}
				return ret;
		}
		public HourCodeCondition getHourcodeCondition(){
				if(hourcodeCondition == null){
						hourcodeCondition = new HourCodeCondition(id);
				}
				return hourcodeCondition;
						
		}
		public void setHourcodeCondition(HourCodeCondition val){
				if(val != null){
						hourcodeCondition = val;
				}
		}
		public String getHourcodeConditionsTitle(){
				return hourcodeConditionsTitle;
		}

		public void setAction2(String val){
				if(val != null && !val.equals(""))		
						action = val;
		}
		public void setDepartment_id(String val){
				if(val != null && !val.equals("-1"))		
						department_id = val;
		}
		public String getDepartment_id(){
				if(department_id.equals("")){
						return "-1";
				}
				return department_id;
		}
		public List<HourCodeCondition> getConditions(){
				if(conditions == null){
						HourCodeConditionList tl = new HourCodeConditionList();
						String back = tl.find();
						if(back.equals("")){
								List<HourCodeCondition> ones = tl.getConditions();
								if(ones != null && ones.size() > 0){
										conditions = ones;
								}
						}
				}
				return conditions;
		}
		public boolean hasConditions(){
				getConditions();
				return conditions != null;
		}
		public List<HourCode> getHourcodes(){
				if(hourcodes == null){
						HourCodeList tl = new HourCodeList();
						String back = tl.find();
						if(back.equals("")){
								List<HourCode> ones = tl.getHourCodes();
								if(ones != null && ones.size() > 0){
										hourcodes = ones;
								}
						}
				}
				return hourcodes;
		}		
		public List<Type> getDepartments(){
				if(departments == null){
						TypeList tl = new TypeList("departments");
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
		public List<Type> getSalaryGroups(){
				if(salaryGroups == null){
						TypeList tl = new TypeList("salary_groups");
						String back = tl.find();
						if(back.equals("")){
								List<Type> ones = tl.getTypes();
								if(ones != null && ones.size() > 0){
										salaryGroups = ones;
								}
						}
				}
				return salaryGroups;
		}
		public List<Group> getGroups(){
				if(groups == null){
						GroupList tl = new GroupList();
						if(department_id.equals("")){
								getHourcodeCondition();
								department_id = hourcodeCondition.getDepartment_id();
						}
						if(!(department_id.equals("") || department_id.equals("-1"))){
								tl.setDepartment_id(department_id);
						}
						String back = tl.find();
						if(back.equals("")){
								List<Group> ones = tl.getGroups();
								if(ones != null && ones.size() > 0){
										groups = ones;
								}
						}
				}
				return groups;
		}				

}





































