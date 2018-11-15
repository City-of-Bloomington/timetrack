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

public class DepartmentEmployeeChangeAction extends TopAction{

		static final long serialVersionUID = 2100L;	
		static Logger logger = LogManager.getLogger(DepartmentEmployeeChangeAction.class);
		String employee_id="", department_id="";
		Employee employee = null;
		List<Type> departments = null;
		DepartmentEmployee departmentEmployee = null;
		List<Employee> otherEmployees = null;
		String departmentEmployeesTitle = "Employees in this department";
		public String execute(){
				String ret = SUCCESS;
				String back = doPrepare();
				if(action.startsWith("Change")){ 
						back = departmentEmployee.doChange();
						if(!back.equals("")){
								addActionError(back);
								addError(back);
						}
						else{
								addMessage("changed Successfully");
								addActionMessage("Changed Successfully");
						}
				}
				else{		
						getDepartmentEmployee();
						if(!id.equals("")){
								back = departmentEmployee.doSelect();
								if(!back.equals("")){
										addError(back);
										addActionError(back);
								}								
						}
				}
				return ret;
		}
		public DepartmentEmployee getDepartmentEmployee(){ 
				if(departmentEmployee == null){
						departmentEmployee = new DepartmentEmployee(id);
						departmentEmployee.setEmployee_id(employee_id);
				}		
				return departmentEmployee;
		}

		public void setDepartmentEmployee(DepartmentEmployee val){
				if(val != null){
						departmentEmployee = val;
				}
		}

		public String getDepartmentEmployeesTitle(){
				return departmentEmployeesTitle;
		}
		public void setAction2(String val){
				if(val != null && !val.equals(""))		
						action = val;
		}
		public void setDepartment_id(String val){
				if(val != null && !val.equals("-1"))		
						department_id = val;
		}
		public void setEmployee_id(String val){
				if(val != null && !val.equals("-1"))		
						employee_id = val;
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

}





































