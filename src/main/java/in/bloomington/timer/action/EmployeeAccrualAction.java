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

public class EmployeeAccrualAction extends TopAction{

    static final long serialVersionUID = 1150L;	
    static Logger logger = LogManager.getLogger(EmployeeAccrualAction.class);
    //
    EmployeeAccrual empAccrual = null;
    List<EmployeeAccrual> empAccruals = null;
    List<Employee> employees = null;
    List<Type> accruals = null;
    List<HourCode> hourCodes = null;
    String employeeAccrualsTitle = "Latest Employee Accruals";
    String employee_selected_id="";
    public String execute(){
	String ret = SUCCESS;
	String back = canProceed("employeeAccrual.action");
	if(!back.isEmpty()){
	    return back;
	}
	if(action.equals("Save")){
	    back = empAccrual.doSave();
	    if(!back.isEmpty()){
		addActionError(back);
		addError(back);
	    }
	    else{
		addMessage("Saved Successfully");
	    }
	}				
	else if(action.startsWith("Save")){
	    back = empAccrual.doUpdate();
	    if(!back.isEmpty()){
		addActionError(back);
		addError(back);
	    }
	    else{
		addMessage("Saved Successfully");
	    }
	}
	else if(action.equals("Next")){
	    getEmpAccrual();
	    empAccrual.setEmployee_id(employee_selected_id);
	}
	else if(!id.isEmpty()){
	    getEmpAccrual();
	    back = empAccrual.doSelect();
	    if(!back.isEmpty()){
		addActionError(back);
	    }
	    else{
		employee_selected_id = empAccrual.getEmployee_id();
	    }
	}
	else if(!employee_selected_id.isEmpty()){
	    getEmpAccrual();
	    empAccrual.setEmployee_id(employee_selected_id);
	}
	else {
	    ret ="select"; // pick an employee first
	}
	return ret;
    }
    public EmployeeAccrual getEmpAccrual(){
	if(empAccrual == null){
	    empAccrual = new EmployeeAccrual();
	    empAccrual.setId(id);
	}
	return empAccrual;
						
    }
    public void setEmpAccrual(EmployeeAccrual val){
	if(val != null){
	    empAccrual = val;
	}
    }
    public String getEmployeeAccrualsTitle(){
	return employeeAccrualsTitle;
    }

    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
	    action = val;
    }
    public void setEmployee_selected_id(String val){
	if(val != null && !val.equals("-1"))		
	    employee_selected_id = val;
    }
    public String getEmployee_selected_id(){
	if(employee_selected_id.isEmpty()){
	    return "-1"; // for list
	}
	return employee_selected_id;
    }
    public List<Type> getAccruals(){
	TypeList tl = new TypeList("accruals");
	tl.setActiveOnly();
	String back = tl.find();
	if(back.isEmpty()){
	    List<Type> ones = tl.getTypes();
	    if(ones != null && ones.size() > 0){
		accruals = ones;
	    }
	}
	return accruals;
    }
    public List<HourCode> getHourCodes(){
	//
	if(hourCodes == null){
	    HourCodeList ecl = new HourCodeList();
	    ecl.setActiveOnly();
	    ecl.relatedToAccrualsOnly();
	    String back = ecl.find();
	    if(back.isEmpty()){
		List<HourCode> ones = ecl.getHourCodes();
		if(ones != null && ones.size() > 0){
		    hourCodes = ones;
		}
	    }
	}
	return hourCodes;
    }
    public List<EmployeeAccrual> getEmpAccruals(){
	//
	if(empAccruals == null){
	    EmployeeAccrualList eal = new EmployeeAccrualList();
	    eal.setEmployee_id(employee_selected_id);
	    eal.setMostCurrent();
	    String back = eal.find();
	    if(back.isEmpty()){
		List<EmployeeAccrual> ones = eal.getEmployeeAccruals();
		if(ones != null && ones.size() > 0){
		    empAccruals = ones;
		}
	    }
	}
	return empAccruals;
    }
    public boolean hasAccruals(){
	getAccruals();
	return accruals != null && accruals.size() > 0;
    }
		
    public boolean hasHourCodes(){
	getHourCodes();
	return hourCodes != null && hourCodes.size() > 0;

    }
    public boolean hasEmpAccruals(){
	getEmpAccruals();
	return empAccruals != null && empAccruals.size() > 0;
    }
    public boolean isEmployeeSelected(){
	return !employee_selected_id.isEmpty();
    }
		
}





































