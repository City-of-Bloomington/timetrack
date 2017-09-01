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
import org.apache.log4j.Logger;
import in.bloomington.timer.list.*;
import in.bloomington.timer.bean.*;

public class DepartmentEmployeeAction extends TopAction{

		static final long serialVersionUID = 2100L;	
		static Logger logger = Logger.getLogger(DepartmentEmployeeAction.class);
		//
		String department_id="", employee_id="";
		Employee employee = null;
		List<Type> departments = null;
		DepartmentEmployee departmentEmployee = null;
		List<Employee> otherEmployees = null;
		String departmentEmployeesTitle = "Employees in this department";
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
						back = departmentEmployee.doSave();
						if(!back.equals("")){
								addActionError(back);
						}
						else{
								addActionMessage("Saved Successfully");
						}
				}				
				else if(action.startsWith("Save")){ 
						back = departmentEmployee.doUpdate();
						if(!back.equals("")){
								addActionError(back);
						}
						else{
								addActionMessage("Saved Successfully");
						}
				}
				else{		
						getDepartmentEmployee();
						if(!id.equals("")){
								back = departmentEmployee.doSelect();
								if(!back.equals("")){
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





































