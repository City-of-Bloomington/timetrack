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

public class EmpWizardAction extends TopAction{

    static final long serialVersionUID = 1800L;	
    static Logger logger = LogManager.getLogger(EmpWizardAction.class);
    //
    EmployeeList emplst = null;
    String employeesTitle = "Current Employees";
    List<Employee> employees = null;
    List<Type> departments = null;
    List<PayPeriod> payPeriods = null;
    String department_id = "", effective_date="";
    public String execute(){
	String ret = SUCCESS;
	String back = canProceed("empWizard.action");
	if(!back.isEmpty()){
	    return back;
	}
	if(!action.isEmpty() && effective_date.isEmpty()){
	    addError("Start date is required ");
	    action = "";
	}
	if(!action.isEmpty()){
	    back = emplst.find();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		List<Employee> ones = emplst.getEmployees();
		String str = "employee.action?wizard=true";								
		if(ones != null && ones.size() == 1){
		    String emp_id = ones.get(0).getId();
		    str += "&emp_id="+emp_id;
		}
		if(!effective_date.isEmpty()){
		    str += "&effective_date="+effective_date;
		}
		try{
		    HttpServletResponse res = ServletActionContext.getResponse();
		    res.sendRedirect(str);
		    return super.execute();
		}catch(Exception ex){
		    System.err.println(ex);
										
		}											
	    }
	}
	else{
	    getUser();
	    if(user != null && !user.isAdmin()){
		String val = user.getDepartment_id();
		if(val != null)
		    department_id = val;
	    }
	}
	getEmplst();
	return ret;
    }
    public EmployeeList getEmplst(){ 
	if(emplst == null){
	    emplst = new EmployeeList();
	}		
	return emplst;
    }
    public List<PayPeriod> getPayPeriods(){
	if(payPeriods == null){
	    PayPeriodList tl = new PayPeriodList();
	    tl.setTwoPeriodsAheadOnly();
	    tl.setLimit("4");
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
    public void setEmplst(EmployeeList val){
	if(val != null){
	    emplst = val;
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
    public String getEffective_date(){
	return effective_date;
    }
    public void setEffective_date(String val){
	if(val != null && !val.equals("-1"))
	    effective_date = val;
    }		
    public String getEmployeeTitle(){
	return employeesTitle;
    }
    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
	    action = val;
    }
    public boolean hasEmployees(){
	return employees != null && employees.size() > 0;
    }

    public List<Employee> getEmployees(){
	return employees;
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





































