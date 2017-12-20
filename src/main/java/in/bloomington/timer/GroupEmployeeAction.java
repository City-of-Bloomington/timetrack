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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GroupEmployeeAction extends TopAction{

		static final long serialVersionUID = 2100L;	
		static Logger logger = LogManager.getLogger(GroupEmployeeAction.class);
		//
		String group_id="", employee_id="", department_id="";
		Employee employee = null;
		List<Group> groups = null;
		GroupEmployee groupEmployee = null;
		List<GroupEmployee> groupEmployees = null;
		String groupEmployeesTitle = "Group Members";
		public String execute(){
				String ret = SUCCESS;
				String back = doPrepare();
				if(!back.equals("")){
						try{
								HttpServletResponse res = ServletActionContext.getResponse();
								String str = url+"Login";
								res.sendRedirect(str);
								return super.execute();
						}catch(Exception ex){
								System.err.println(ex);
						}	
				}
				if(action.equals("Save")){
						back = groupEmployee.doSave();
						if(!back.equals("")){
								addActionError(back);
						}
						else{
								group_id = groupEmployee.getGroup_id();
								employee_id = groupEmployee.getEmployee_id();
								addActionMessage("Saved Successfully");
						}
				}				
				else if(action.startsWith("Save")){ 
						back = groupEmployee.doUpdate();
						if(!back.equals("")){
								addActionError(back);
						}
						else{
								group_id = groupEmployee.getGroup_id();
								employee_id = groupEmployee.getEmployee_id();
								addActionMessage("Saved Successfully");
						}
				}
				else if(action.startsWith("Change")){ 
						back = groupEmployee.doChange();
						if(!back.equals("")){
								addActionError(back);
						}
						else{
								addActionMessage("Changed Successfully");
						}
				}				
				else{		
						getGroupEmployee();
						if(!id.equals("")){
								back = groupEmployee.doSelect();
								if(!back.equals("")){
										addActionError(back);
								}
								employee_id = groupEmployee.getEmployee_id();
								group_id = groupEmployee.getGroup_id();
						}
				}
				return ret;
		}
		public GroupEmployee getGroupEmployee(){ 
				if(groupEmployee == null){
						groupEmployee = new GroupEmployee(id);
						groupEmployee.setEmployee_id(employee_id);
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
				if(department_id.equals("") && !employee_id.equals("")){
						getEmployee();
						if(employee != null){
								department_id = employee.getDepartment_id();
						}
				}
				return department_id;
		}
		public Employee getEmployee(){
				if(employee == null && !employee_id.equals("")){
						Employee one = new Employee(employee_id);
						String back = one.doSelect();
						if(back.equals("")){
								employee = one;
						}
				}
				return employee;
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
		public void setEmployee_id(String val){
				if(val != null && !val.equals("-1"))		
						employee_id = val;
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
						GroupList tl = new GroupList();
						if(!department_id.equals("")){
								tl.setDepartment_id(department_id);
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
		

}





































