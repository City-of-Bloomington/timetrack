package in.bloomington.timer.action;
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

public class EmployeeAction extends TopAction{

		static final long serialVersionUID = 1150L;	
		static Logger logger = LogManager.getLogger(EmployeeAction.class);
		//
		static final String[] roles = {"Employee","Timewarp","FMLAReport",
																	 "MPOReport","TargetEmployee","ParkReport",
																	 "FireReport","HrAdmin", "ITSAdmin",
																	 "PoliceAdmin","PublicWorksAdmin",
																	 "Admin"};
		String emp_id="";
		Employee emp = null;
		DepartmentEmployee departmentEmployee = null; // for new employee
		GroupEmployee groupEmployee = null; // for new employee
		List<Employee> employees = null;
		String employeesTitle = "Current Employees";
		List<Type> departments = null;
		List<PayPeriod> payPeriods = null;
		public String execute(){
				String ret = SUCCESS;
				String back = doPrepare();
				if(action.equals("Save")){
						back = emp.validate();
						if(!back.equals("")){
								addError(back);
								return ret;
						}
						back = emp.doSave();
						if(!back.equals("")){
								addError(back);
						}
						else{
								emp_id = emp.getId();
						}
				}				
				else if(action.startsWith("Save")){
						back = emp.doUpdate();
						if(!back.equals("")){
								addError(back);
						}
						else{
								addMessage("Saved Successfully");
						}
				}
				else if(action.startsWith("Edit")){
						getEmp();
						back = emp.doSelect();
						if(!back.equals("")){
								addError(back);
						}
						getDepartmentEmployee();
				}				
				else{		
						getEmp();
						if(!emp_id.equals("")){
								back = emp.doSelect();
								if(!back.equals("")){
										addError(back);
								}
								else{
										ret="view";
								}
						}
						getDepartmentEmployee();
				}
				return ret;
		}
		public Employee getEmp(){
				if(emp == null){
						emp = new Employee();
						emp.setId(emp_id);
				}
				return emp;
						
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
		public void setEmp(Employee val){
				if(val != null){
						emp = val;
				}
		}
		public void setEmp_id(String val){
				if(val != null){
						emp_id = val;
				}
		}
		public String getEmp_id(){
				if(emp_id.equals("") && emp != null){
						emp_id = emp.getId();
				}
				return emp_id;
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
				if(emp == null){
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
		public String[] getRoles(){
				return roles;
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





































