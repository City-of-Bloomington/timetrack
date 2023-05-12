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
    static final String[] roles =
    {"Employee",
     "Admin",
     "Edit",
     "EngineeringAdmin",
     "FireReport",	
     "FMLAReport",
     "HANDReport",
     "HrAdmin",	
     "MPOReport",
     "ITSAdmin",
     "ParkAdmin",
     "PoliceAdmin",	
     "PublicWorksAdmin",	
     "TargetEmployee",
     "SearchEmployee",
     "Timewarp"
    };
    String emp_id="";
    // dept_id needed for adding employee by non-admin users
    String dept_id="", effective_date=""; // from wizard
    boolean wizard = false;
    Employee emp = null;
    Department dept = null;
    DepartmentEmployee departmentEmployee = null; // for new employee
    // GroupEmployee groupEmployee = null; // for new employee
    List<Employee> employees = null;
    String employeesTitle = "Current Employees";
    List<Type> departments = null;
    List<PayPeriod> payPeriods = null;
    List<Group> groups = null;
    public String execute(){
	String ret = SUCCESS;
	String back = canProceed("employee.action");
	if(!back.isEmpty()){
	    return back;
	}
	if(action.equals("Save")){
	    back = emp.validate();
	    if(!back.isEmpty()){
		addError(back);
		return ret;
	    }
	    back = emp.doSave();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		emp_id = emp.getId();
	    }
	}				
	else if(action.startsWith("Save")){
	    back = emp.doUpdate();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		addMessage("Saved Successfully");
	    }
	}
	else if(action.startsWith("Edit")){
	    getEmp();
	    back = emp.doSelect();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    getDepartmentEmployee();
	}				
	else{		
	    getEmp();
	    if(!emp_id.isEmpty()){
		back = emp.doSelect();
		if(!back.isEmpty()){
		    addError(back);
		}
		else{
		    ret="view";
		}
	    }
	    else{
		if(canEdit()){
		    if(user.hasDepartment()){
			dept = user.getDepartment();
			dept_id= dept.getId();
		    }
		}
	    }
	    // not sure about this if still needed
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
    public Department getDept(){
	if(dept == null){
	    dept = new Department(dept_id);
	    if(!dept_id.isEmpty()){
		dept.doSelect();
	    }
	}
	return dept;
    }
    public DepartmentEmployee getDepartmentEmployee(){
	if(departmentEmployee == null){
	    departmentEmployee = new DepartmentEmployee();
	}
	return departmentEmployee;
						
    }
    public void setEmp(Employee val){
	if(val != null){
	    emp = val;
	}
    }
    public void setDept(Department val){
	if(val != null){
	    dept = val;
	}
    }
    public void setEmp_id(String val){
	if(val != null){
	    emp_id = val;
	}
    }
    public String getEmp_id(){
	if(emp_id.isEmpty() && emp != null){
	    emp_id = emp.getId();
	}
	return emp_id;
    }
    public void setDept_id(String val){
	if(val != null){
	    dept_id = val;
	}
    }
    public String getDept_id(){
	if(dept_id.isEmpty() && dept != null){
	    dept_id = dept.getId();
	}
	return dept_id;
    }
    public void setWizard(boolean val){
	if(val){
	    wizard = true;
	}
    }
    public boolean isWizard(){
	return wizard;
    }
    public void setDepartmentEmployee(DepartmentEmployee val){
	if(val != null){
	    departmentEmployee = val;
	}
    }
    public String getEmployeesTitle(){
	return employeesTitle;
    }
    public String getEffective_date(){
	return effective_date;
    }
    public void setEffective_date(String val){
	if(val != null)
	    effective_date = val;
    }		
    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
	    action = val;
    }
    public List<Employee> getEmployees(){
	if(emp == null){
	    EmployeeList tl = new EmployeeList();
	    tl.setActiveOnly();
	    String back = tl.find();
	    if(back.isEmpty()){
		List<Employee> ones = tl.getEmployees();
		if(ones != null && ones.size() > 0){
		    employees = ones;
		}
	    }
	}
	return employees;
    }
    public List<Type> getDepartments(){
	if(departments == null){
	    TypeList tl = new TypeList("departments");
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
    public List<Group> getGroups(){
	if(groups == null && !dept_id.isEmpty()){
	    GroupList gl = new GroupList();
	    if(emp.hasDepartments()){
		List<DepartmentEmployee> depEmps = emp.getDepartmentEmployees();
		if(depEmps != null && depEmps.size() > 0){
		    for(DepartmentEmployee one:depEmps){
			gl.setDepartment_id(one.getDepartment_id());
		    }
		}
		else{
		    gl.setDepartment_id(dept_id);
		}
	    }
	    else{
		gl.setDepartment_id(dept_id);
	    }
	    gl.setActiveOnly();
	    String back = gl.find();
	    if(back.isEmpty()){
		List<Group> ones = gl.getGroups();
		if(ones != null && ones.size() > 0){
		    groups = ones;
		}
	    }
	}
	return groups;
    }
    public boolean hasGroups(){
	getGroups();
	return groups != null && groups.size() > 0;
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
	    if(back.isEmpty()){
		List<PayPeriod> ones = tl.getPeriods();
		if(ones != null && ones.size() > 0){
		    payPeriods = ones;
		}
	    }
	}
	return payPeriods;
    }		
    public boolean canAssignRoles(){
	getUser();
	return user.isAdmin();
    }

}





































