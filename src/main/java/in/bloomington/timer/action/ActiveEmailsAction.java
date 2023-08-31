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

public class ActiveEmailsAction extends TopAction{

    static final long serialVersionUID = 1810L;	
    static Logger logger = LogManager.getLogger(ActiveEmailsAction.class);
    List<Employee> employees = null;
    //
    // employees who are active and used timetrack since last two weeks
    //
    String activeEmailsTitle = "Current TimeTrack Active Employees Emails";
    List<Type> departments = null;
    String department_id = "";
    public String execute(){
	String ret = SUCCESS;
	String back = canProceed("activeEmails.action");
	if(!back.isEmpty()){
	    return back;
	}
	if(!action.isEmpty()){
	    back = findEmployees();
	    if(!back.isEmpty()){
		addError(back);
	    }
	}				
	return ret;
    }
    private String findEmployees(){
	EmployeeList empl = new EmployeeList();
	empl.setActiveOnly();
	empl.setUsedTimeTrack();
	if(!department_id.isEmpty()){
	    empl.setDepartment_id(department_id);
	}
	String back = empl.find();
	if(back.isEmpty()){
	    List<Employee> ones = empl.getEmployees();
	    if(ones != null && ones.size() > 0)
		employees = ones;
	}
	return back;
    }

    public String getActiveEmailsTitle(){
	return activeEmailsTitle;
    }
    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
	    action = val;
    }
    public void setDepartment_id(String val){
	if(val != null && !val.equals("-1"))		
	    department_id = val;
    }
    public String getDepartment_id(){
	if(department_id.isEmpty())
	    return "-1"; // for all
	return department_id;
    }
    public boolean hasEmployees(){
	return employees != null;
    }
    public List<Employee> getEmployees(){
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
}





































