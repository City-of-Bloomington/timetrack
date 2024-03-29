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
import in.bloomington.timer.bean.*;
import in.bloomington.timer.list.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.timewarp.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class TmwrpInitiateAction extends TopAction{

    static final long serialVersionUID = 4320L;	
    static Logger logger = LogManager.getLogger(TmwrpInitiateAction.class);
    //
    List<PayPeriod> payPeriods = null;
    TmwrpInitiate tmwrp = null;
    String tmwrpWrapsTitle = "Timewarp Initiate";
    String pay_period_id = "", source="", employee_name="",
	new_employee_id="", department_id="", group_id="",
	type="";
    PayPeriod payPeriod = null, currentPayPeriod=null;
    List<Type> departments = null;
    List<String> emps = null;
    public String execute(){
	String ret = SUCCESS;
	String back = doPrepare("tmwrpWrapInitiate.action");
	if(!back.isEmpty()){
	    return back;
	}
	if(!action.isEmpty()){
	    if(!pay_period_id.isEmpty()){
		getTmwrp();
		tmwrp.setPay_period_id(pay_period_id);
		if(!group_id.isEmpty()){
		    tmwrp.setGroup_id(group_id);		
		    back = tmwrp.doProcessGroup();
		    if(!back.isEmpty()){
			addMessage(back);
		    }
		    else{
			addMessage("Updated successfully");
		    }
		    emps = tmwrp.getEmps();
		}
		else if(!department_id.isEmpty()){
		    tmwrp.setDepartment_id(department_id);		
		    back = tmwrp.doProcessDept();
		    if(!back.isEmpty()){
			addMessage(back);
		    }
		    else{
			addMessage("Updated successfully");
		    }
		    emps = tmwrp.getEmps();
		}								
		else if(!new_employee_id.isEmpty()){
		    tmwrp.setEmployee_id(new_employee_id);
		    back = tmwrp.doProcessOne();
		    if(!back.isEmpty()){
			addMessage(back);
		    }
		    else{
			addMessage("Updated successfully");
		    }										
		}
		else { // all
		    back = tmwrp.doProcess();
		    if(!back.isEmpty()){
			addMessage(back);
		    }
		    else{
			addMessage("Updated successfully");
		    }
		    emps = tmwrp.getEmps();
		}
								
	    }
	    else{
		addMessage("Need to select a pay period ");
	    }
	}
	return ret;
    }
    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
	    action = val;
    }
    public void setSource(String val){
	if(val != null && !val.isEmpty())		
	    source = val;
    }		
    public void setPay_period_id(String val){
	if(val != null && !val.isEmpty())		
	    pay_period_id = val;
    }
    public void setGroup_id(String val){
	if(val != null && !val.equals("-1") && !val.isEmpty())		
	    group_id = val;
    }		
    public void setNew_employee_id(String val){
	if(val != null && !val.isEmpty())		
	    new_employee_id = val;
    }
    // 
    public void setDepartment_id(String val){
	if(val != null && !val.equals("-1")){		
	    department_id = val;
	}
    }
    public void setDept_id(String val){
	if(val != null && !val.equals("-1"))		
	    department_id = val;
    }
    public String getDepartment_id(){
	return department_id;
    }
    public String getEmployee_name(){
	return employee_name;
    }
    public String getNew_employee_id(){
	return new_employee_id;
    }				
    public void setEmployee_name(String val){
	if(val != null && !val.isEmpty())		
	    employee_name = val;
    }		
    public String getSource(){
	return source;
    }		
    public String getPay_period_id(){
	if(pay_period_id.isEmpty()){
	    getPayPeriod();
	}
	return pay_period_id;
    }
    public TmwrpInitiate getTmwrp(){
	if(tmwrp == null){
	    tmwrp = new TmwrpInitiate();
	}
	return tmwrp;
    }
    public void setTmwrp(TmwrpInitiate val){
	if(val != null)
	    tmwrp = val;
    }
    public void setType(String val){
	// needed for js
    }
		
    public List<PayPeriod> getPayPeriods(){
	if(payPeriods == null){
	    getPayPeriod(); // so that we can initialize the list
	    PayPeriodList tl = new PayPeriodList();
	    tl.avoidFuturePeriods();
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

    public PayPeriod getPayPeriod(){
	//
	// if not set we look for current one
	//
	if(payPeriod == null){
	    if(pay_period_id.isEmpty()){
		getCurrentPayPeriod();
	    }
	    else{
		PayPeriod one = new PayPeriod(pay_period_id);
		String back = one.doSelect();
		if(back.isEmpty()){
		    payPeriod = one;
		}
	    }
	}
	return payPeriod;
    }
    public PayPeriod getCurrentPayPeriod(){
	//
	if(currentPayPeriod == null){
	    PayPeriodList ppl = new PayPeriodList();
	    ppl.currentOnly();
	    String back = ppl.find();
	    if(back.isEmpty()){
		List<PayPeriod> ones = ppl.getPeriods();
		if(ones != null && ones.size() > 0){
		    currentPayPeriod = ones.get(0);
		    if(pay_period_id.isEmpty()){
			pay_period_id = currentPayPeriod.getId();
			payPeriod = currentPayPeriod;
		    }
		}
	    }
	}
	return currentPayPeriod;
    }
    public boolean hasEmps(){
	return emps != null && emps.size() > 0;
    }
    /**
     * list of employees' full names who needed timewarp initiations;
     */
    public List<String> getEmps(){
	return emps;
    }
    public List<Type> getDepartments(){
	if(departments == null){
	    TypeList tl = new TypeList("departments");
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
    public boolean hasDepartments(){
	getDepartments();
	return departments != null && departments.size() > 0;
    }
				
}





































