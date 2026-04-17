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
import in.bloomington.timer.util.*;
import in.bloomington.timer.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EmpPayRateScheduleAction extends TopAction{

    static final long serialVersionUID = 3850L;	
    static Logger logger = LogManager.getLogger(EmpPayRateScheduleAction.class);
    //
    List<Department> depts = null;
    String rateSchedulesTitle = "Employee Pay Rate Schedules";
    QuartzMisc quartzMisc = null;
    EmpPayRateScheduler schedular = null;
    List<PayPeriod> payPeriods = null;
    String week_no = "1", //1,2
	pay_period_id="", dept_ref_id="";
    public String execute(){
	String ret = SUCCESS;
	String back = canProceed("Schedule.action");
	if(!back.isEmpty()){
	    return back;
	}
	prepareSchedular();				
	if(action.equals("Schedule")){
	    try{
		back = schedular.run();
		if(!back.isEmpty()){
		    addError(back);
		}
		else{
		    addMessage("Scheduled Successfully");
		}
	    }catch(Exception ex){
		addError(""+ex);
	    }
	}
	else if(action.startsWith("Import")){ // import now given the date
	    if(pay_period_id.isEmpty() || week_no.isEmpty()){
		addError("Pay period not or week number not set");
	    }
	    else{
		HandleEmpPayRate handle = new HandleEmpPayRate(dept_ref_id, pay_period_id, week_no);
		back = handle.process();
		if(!back.isEmpty()){
		    addError(back);
		}
		else{
		    addMessage("Imported Successfully");
		}
	    }
	}
	return ret;
    }
    private void prepareSchedular(){
	String date = "04/05/2026"; // Sunday 
	schedular = new EmpPayRateScheduler(date);

    }
    private String doClean(){
	String msg = "";
	/**
	if(quartzMisc != null){
	    msg = quartzMisc.doClean();
	}
	*/
	return msg;
    }
    public String getRateSchedulesTitle(){
				
	return rateSchedulesTitle;
    }


    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
	    action = val;
    }
    public void setPayPeriod_id(String val){
	if(val != null && !val.isEmpty())		
	    pay_period_id = val;
    }
    public void setWeekNo(String val){
	if(val != null && !val.equals("-1"))		
	    week_no = val;
    }		
    public void setDept_ref_id(String val){
	if(val != null && !val.equals("-1"))		
	    dept_ref_id = val;
    }		
    // read only 
    public String getWeekNo(){
	return week_no;
    }
    public String getPayPeriod_id(){
	return pay_period_id;
    }
    public String getDept_ref_id(){
	return dept_ref_id;
    }	    
    public List<Department> getDepts(){
	if(depts == null){
	    DepartmentList dl = new DepartmentList();
	    dl.setActiveOnly();
	    dl.hasRefIds();
	    String msg = dl.find();
	    if(!msg.isEmpty()){
		logger.error(msg);
	    }
	    else{
		List<Department> ones = dl.getDepartments();
		if(ones != null && ones.size() > 0){
		    depts = ones;
		}
	    }
	}
	return depts;
    }
    public boolean hasDepts(){
	getDepts();
	return depts != null && depts.size() > 0;
    }
    public List<PayPeriod> getPayPeriods(){
	if(payPeriods == null){
	    PayPeriodList tl = new PayPeriodList();
	    tl.setOnePeriodAheadOnly();
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
}





































