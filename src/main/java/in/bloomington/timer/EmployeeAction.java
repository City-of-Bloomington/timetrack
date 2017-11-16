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
import org.apache.log4j.Logger;
import in.bloomington.timer.list.*;
import in.bloomington.timer.bean.*;

public class EmployeeAction extends TopAction{

		static final long serialVersionUID = 1150L;	
		static Logger logger = Logger.getLogger(EmployeeAction.class);
		//
		Employee employee = null;
		DepartmentEmployee departmentEmployee = null; // for new employee
		GroupEmployee groupEmployee = null; // for new employee
		List<Employee> employees = null;
		String employeesTitle = "Current Employees";
		List<Type> departments = null;
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
						back = employee.doSave();
						if(!back.equals("")){
								addActionError(back);
						}
						else{
								id = employee.getId();
								departmentEmployee.setEmployee_id(id);
								back = departmentEmployee.doSave();
								groupEmployee.setEmployee_id(id);
								groupEmployee.setEffective_date(departmentEmployee.getEffective_date());
								back += groupEmployee.doSave();
								if(!back.equals("")){
										addActionError(back);
								}
								else{
										addActionMessage("Saved Successfully");
								}
						}
				}				
				else if(action.startsWith("Save")){
						back = employee.doUpdate();
						if(!back.equals("")){
								addActionError(back);
						}
						else{
								addActionMessage("Saved Successfully");
						}
				}
				else{		
						getEmployee();
						if(!id.equals("")){
								back = employee.doSelect();
								if(!back.equals("")){
										addActionError(back);
								}								
						}
						getDepartmentEmployee();
				}
				return ret;
		}
		public Employee getEmployee(){
				if(employee == null){
						employee = new Employee();
						employee.setId(id);
				}
				return employee;
						
		}
		public DepartmentEmployee getDepartmentEmployee(){
				if(departmentEmployee == null){
						departmentEmployee = new DepartmentEmployee();
				}
				return departmentEmployee;
						
		}
		public GroupEmployee getGroupEmployee(){
				if(groupEmployee == null){
						groupEmployee = new GroupEmployee();
				}
				return groupEmployee;
						
		}		
		public void setEmployee(Employee val){
				if(val != null){
						employee = val;
				}
		}
		public void setDepartmentEmployee(DepartmentEmployee val){
				if(val != null){
						departmentEmployee = val;
				}
		}
		public void setGroupEmployee(GroupEmployee val){
				if(val != null){
						groupEmployee = val;
				}
		}		
		public String getEmployeesTitle(){
				return employeesTitle;
		}

		public void setAction2(String val){
				if(val != null && !val.equals(""))		
						action = val;
		}
		public List<Employee> getEmployees(){
				if(employees == null){
						EmployeeList tl = new EmployeeList();
						tl.setActiveOnly();
						String back = tl.find();
						if(back.equals("")){
								List<Employee> ones = tl.getEmployees();
								if(ones != null && ones.size() > 0){
										employees = ones;
								}
						}
				}
				return employees;
		}
		public List<Type> getDepartments(){
				TypeList tl = new TypeList("departments");
				tl.setActiveOnly();
				String back = tl.find();
				if(back.equals("")){
						List<Type> ones = tl.getTypes();
						if(ones != null && ones.size() > 0){
								departments = ones;
						}
				}
				return departments;
		}
}




































