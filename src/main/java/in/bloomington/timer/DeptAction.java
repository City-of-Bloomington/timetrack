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
import in.bloomington.timer.list.*;
import in.bloomington.timer.bean.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DeptAction extends TopAction{

		static final long serialVersionUID = 3800L;	
		static Logger logger = LogManager.getLogger(DeptAction.class);
		//
		// default department
		String deptsTitle = "Departments";
		Department department = null;
		List<Department> departments = null;
		List<Group> groups = null;
		List<Employee> employees = null;
		public String execute(){
				String ret = SUCCESS;
				String back = doPrepare();
				if(action.equals("Save")){
						back = department.doSave();
						if(!back.equals("")){
								addActionError(back);
								addError(back);
						}
						else{
								addActionMessage("Saved Successfully");
								addMessage("Saved Successfully");
								id = department.getId();
						}
				}				
				else if(action.startsWith("Save")){
						back = department.doUpdate();
						if(!back.equals("")){
								addActionError(back);
								addError(back);
						}
						else{
								id = department.getId();
								addActionMessage("Saved Successfully");
								addMessage("Saved Successfully");
						}
				}
				else{		
						getDepartment();
						if(!id.equals("")){
								back = department.doSelect();
								if(!back.equals("")){
										addActionError(back);
										addError(back);
								}
						}
				}
				return ret;
		}
		public Department getDepartment(){
				if(department == null){
						department = new Department();
						department.setId(id);
				}
				return department;
						
		}
		public void setDepartment(Department val){
				if(val != null){
						department = val;
				}
		}
		public String getDeptsTitle(){
				
				return deptsTitle;
		}

		public void setAction2(String val){
				if(val != null && !val.equals(""))		
						action = val;
		}
		public List<Department> getDepartments(){
				if(departments == null){
						DepartmentList tl = new DepartmentList();
						tl.setActiveOnly();
						String back = tl.find();
						if(back.equals("")){
								List<Department> ones = tl.getDepartments();
								if(ones != null && ones.size() > 0){
										departments = ones;
								}
						}
				}
				return departments;
		}
		public List<Group> getGroups(){
				if(department != null && !department.getId().equals("")){
						GroupList gl = new GroupList();
						gl.setDepartment_id(department.getId());
						String back = gl.find();
						if(back.equals("")){
								List<Group> ones = gl.getGroups();
								if(ones != null && ones.size() > 0){
										groups = ones;
								}
						}
				}
				return groups;
		}
		public List<Employee> getEmployees(){
				if(employees == null && !id.equals("")){
						EmployeeList tl = new EmployeeList();
						tl.setDepartment_id(id);
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
		public boolean hasEmployees(){
				getEmployees();
				return employees != null && employees.size() > 0;
		}
}





































