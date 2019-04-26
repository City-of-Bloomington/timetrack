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

public class GroupEmployeeAction extends TopAction{

		static final long serialVersionUID = 2100L;	
		static Logger logger = LogManager.getLogger(GroupEmployeeAction.class);
		//
		String group_id="", emp_id="", department_id="",
				department2_id="";
		Employee emp = null;
		List<Group> groups = null;
		List<PayPeriod> payPeriods = null;
		GroupEmployee groupEmployee = null;
		List<GroupEmployee> groupEmployees = null;
		String groupEmployeesTitle = "Group Members";
		public String execute(){
				String ret = SUCCESS;
				String back = doPrepare();
				if(action.equals("Save")){
						back = groupEmployee.doSave();
						if(!back.equals("")){
								addError(back);
						}
						else{
								group_id = groupEmployee.getGroup_id();
								emp_id = groupEmployee.getEmployee_id();
								addMessage("Saved Successfully");								
						}
				}				
				else if(action.startsWith("Save")){ 
						back = groupEmployee.doUpdate();
						if(!back.equals("")){
								addError(back);
						}
						else{
								group_id = groupEmployee.getGroup_id();
								emp_id = groupEmployee.getEmployee_id();
								addMessage("Saved Successfully");			
						}
				}
				else if(action.startsWith("Change")){ 
						back = groupEmployee.doChange();
						if(!back.equals("")){
								addError(back);
						}
						else{
								addMessage("Changed Successfully");			
						}
				}				
				else{		
						getGroupEmployee();
						if(!id.equals("")){
								back = groupEmployee.doSelect();
								if(!back.equals("")){
										addError(back);
								}
								emp_id = groupEmployee.getEmployee_id();
								group_id = groupEmployee.getGroup_id();
						}
				}
				return ret;
		}
		public GroupEmployee getGroupEmployee(){ 
				if(groupEmployee == null){
						groupEmployee = new GroupEmployee(id);
						groupEmployee.setEmployee_id(emp_id);
						groupEmployee.setGroup_id(group_id);
				}		
				return groupEmployee;
		}

		public void setGroupEmployee(GroupEmployee val){
				if(val != null){
						groupEmployee = val;
				}
		}
		public String getDepartment_id(){
				if(department_id.equals("") && !emp_id.equals("")){
						getEmp();
						if(emp != null){
								department_id = emp.getDepartment_id();
								if(emp.hasTwoDepartments()){
										department2_id = emp.getDepartment2_id(); 
								}
						}
				}
				return department_id;
		}
		public Employee getEmp(){
				if(emp == null && !emp_id.equals("")){
						Employee ee = new Employee(emp_id);
						String back = ee.doSelect();
						if(back.equals("")){
								emp = ee;
						}
				}
				return emp;
		}
		public String getGroupEmployeesTitle(){
				return groupEmployeesTitle;
		}

		public void setAction2(String val){
				if(val != null && !val.equals(""))		
						action = val;
		}
		public void setGroup_id(String val){
				if(val != null && !val.equals("-1"))		
						group_id = val;
		}
		public void setEmp_id(String val){
				if(val != null && !val.equals("-1"))		
						emp_id = val;
		}
		public void setDepartment_id(String val){
				if(val != null && !val.equals("-1"))		
						department_id = val;
		}					
		public List<Group> getGroups(){
				if(groups == null){
						// we are interested in the group of the employee department
						// we need this list just in case we need to change
						// employee group
						if(department_id.equals("")){
								getDepartment_id();
						}
						getEmp();
						if(employee.hasTwoDepartments()){
								department2_id = employee.getDepartment2_id();
						}
						GroupList tl = new GroupList();
						if(!department_id.equals("")){
								tl.setDepartment_id(department_id);
								if(!department2_id.equals("")){
										tl.setDepartment_id(department2_id);
								}
						}
						tl.setActiveOnly();
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
		public boolean hasGroupEmployees(){
				getGroupEmployees();
				return groupEmployees != null;
		}
		public List<GroupEmployee> getGroupEmployees(){
				if(groupEmployees == null){
						GroupEmployeeList gel = new GroupEmployeeList();
						if(group_id.equals("")){
								getGroupEmployee();
								group_id = groupEmployee.getGroup_id();
						}
						gel.setGroup_id(group_id);
						String back = gel.find();
						if(back.equals("")){
								List<GroupEmployee> ones = gel.getGroupEmployees();
								if(ones != null && ones.size() > 0){
										groupEmployees = ones;
								}
						}
				}
				return groupEmployees;
		}
		public List<PayPeriod> getPayPeriods(){
				if(payPeriods == null){
						PayPeriodList tl = new PayPeriodList();
						tl.setTwoPeriodsAheadOnly();
						tl.setLimit("5");
						String back = tl.find();
						if(back.equals("")){
								List<PayPeriod> ones = tl.getPeriods();
								if(ones != null && ones.size() > 0){
										payPeriods = ones;
								}
						}
				}
				return payPeriods;
		}		

}





































