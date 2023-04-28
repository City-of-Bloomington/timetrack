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

public class DepartmentEmployeeAction extends TopAction{

    static final long serialVersionUID = 2100L;	
    static Logger logger = LogManager.getLogger(DepartmentEmployeeAction.class);
    //
    String department_id="", emp_id="";
    Employee employee = null;
    List<Type> departments = null;
    DepartmentEmployee departmentEmployee = null;
    List<Employee> otherEmployees = null;
    List<PayPeriod> payPeriods = null;		
    String departmentEmployeesTitle = "Employees in this department";
    public String execute(){
	String ret = SUCCESS;
	String back = canProceed("departmentEmployee.action");
	if(!back.isEmpty()){
	    return back;
	}
	if(action.equals("Save")){
	    back = departmentEmployee.doSave();
	    if(!back.isEmpty()){
		addActionError(back);
		addError(back);
	    }
	    else{
		addMessage("Saved Successfully");
	    }
	}				
	else if(action.startsWith("Save")){ 
	    back = departmentEmployee.doUpdate();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		addMessage("Saved Successfully");								
	    }
	}
	else{		
	    getDepartmentEmployee();
	    if(!id.isEmpty()){
		back = departmentEmployee.doSelect();
		if(!back.isEmpty()){
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
	    departmentEmployee.setEmployee_id(emp_id);
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
	if(val != null && !val.isEmpty())		
	    action = val;
    }
    public void setDepartment_id(String val){
	if(val != null && !val.equals("-1"))		
	    department_id = val;
    }
    public void setEmp_id(String val){
	if(val != null && !val.equals("-1"))		
	    emp_id = val;
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
}





































