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

public class SearchDeptEmployeeAction extends TopAction{

		static final long serialVersionUID = 1800L;	
		static Logger logger = LogManager.getLogger(SearchDeptEmployeeAction.class);
		//
		DepartmentEmployeeList deptEmpLst = null;
		String deptEmployeesTitle = "Department Employees";
		List<DepartmentEmployee> departmentEmployees = null;
		List<Type> departments = null;
		String department_id = "";
		public String execute(){
				String ret = SUCCESS;
				String back = doPrepare();
				if(!action.isEmpty()){
						back = deptEmpLst.find();
						if(!back.isEmpty()){
								addError(back);
						}
						else{
								List<DepartmentEmployee> ones = deptEmpLst.getDepartmentEmployees();
								
								if(ones != null){
										if(ones.size() > 1){
												departmentEmployees = ones;
												addMessage("Found "+departmentEmployees.size()+" department employees");
										}
										else if(ones.size() == 1){
												String id = ones.get(0).getId();
												try{
														HttpServletResponse res = ServletActionContext.getResponse();
														String str = "departmentEmployee.action?id="+id;
														res.sendRedirect(str);
														return super.execute();
												}catch(Exception ex){
														System.err.println(ex);
														
												}	
										}
								}
								else{
										addMessage("No match found");
								}
						}
				}
				getDeptEmpLst();
				return ret;
		}
		public DepartmentEmployeeList getDeptEmpLst(){ 
				if(deptEmpLst == null){
						deptEmpLst = new DepartmentEmployeeList();
				}		
				return deptEmpLst;
		}

		public void setDepartmentEmployeeList(DepartmentEmployeeList val){
				if(val != null){
						deptEmpLst = val;
				}
		}
		// needed for auto  complete and limit the user
		// to his/her department
		
		public String getDepartment_id(){
				return department_id;
		}
		public void setDepartment_id(String val){
				if(val != null)
						department_id = val;
		}
		public String getDeptEmployeeTitle(){
				return deptEmployeesTitle;
		}
		public void setAction2(String val){
				if(val != null && !val.isEmpty())		
						action = val;
		}
		public boolean hasDepartmentEmployees(){
				return departmentEmployees != null && departmentEmployees.size() > 0;
		}

		public List<DepartmentEmployee> getDepartmentEmployees(){
				return departmentEmployees;
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
		
}





































