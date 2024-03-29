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
import in.bloomington.timer.timewarp.TimewarpManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TimeBlockAction extends TopAction{

    static final long serialVersionUID = 4300L;
    DecimalFormat dFormat = new DecimalFormat("###.00");
    static Logger logger = LogManager.getLogger(TimeBlockAction.class);
    //
    TimeBlock timeBlock = null;
    String timeBlocksTitle = "Time Block Entry";
    String document_id = "", group_id="", department_id="";
    String date = "";
    int order_index = 0;
    Employee employee = null;
    Document document = null;
    PayPeriod payPeriod = null;
    TimewarpManager timewarpManager = null;
    List<EmployeeAccrual> employeeAccruals = null;
    List<HourCode> hourCodes = null;
    List<EarnCodeReason> earnReasons = null;
    List<HourCode> monetaryHourCodes = null;
    // Department department = null;
    //
    public String execute(){
	String ret = SUCCESS;
	String back = doPrepare("timeBlock.action");
	if(!back.isEmpty()){
	    return back;
	}
	if(user == null){
	    back = "Session time out, please logout and login again";
	    System.err.println(" Time Action "+back);
	    addError(back);
	}
	if(action.equals("Save")){
	    if(timeBlock.hasErrors()){
		addError(timeBlock.getErrors());
		return "json";
	    }
	    if(timeBlock.areAllTimesSet()){
		timeBlock.setAction_by_id(user.getId());
		back = timeBlock.doSave();
		if(!back.isEmpty()){
		    addError(back);
		    return "json";
		}
		else{
		    timewarpManager.setDocument_id(timeBlock.getDocument_id());
		    back = timewarpManager.doProcess();
		    addMessage("Added Successfully");
		}
	    }
	    else{
		back = "All fields are required";
		addError(back);
		return "json";								
	    }
	}				
	else if(action.startsWith("Save")){
	    if(timeBlock.areAllTimesSet()){						
		timeBlock.setAction_by_id(user.getId());
		back = timeBlock.doUpdate();
		if(!back.isEmpty()){
		    addError(back);								
		    return "json";
		}
		else{
		    timewarpManager.setDocument_id(timeBlock.getDocument_id());
		    back = timewarpManager.doProcess();
		    addMessage("Updated Successfully");								
		}
	    }
	    else{
		back = "Not all required fields are set";
		addError(back);
		return "json";	
	    }
	}
	else if(action.equals("Delete")){
	    getTimeBlock();
	    back = timeBlock.doSelect();
	    timewarpManager.setDocument_id(timeBlock.getDocument_id());
	    //
	    // we need document_id so that when we delete the timeblock
	    // we stay on the same payperiod
	    //
	    document_id = timeBlock.getDocument_id();
	    timeBlock.setAction_by_id(user.getId());
	    back = timeBlock.doDelete();
	    if(!back.isEmpty()){
		addActionError(back);
		addError(back);
	    }
	    else{
		back = timewarpManager.doProcess();
		try{
		    HttpServletResponse res = ServletActionContext.getResponse();
		    String str = url+"timeDetails.action?document_id="+document_id;
		    res.sendRedirect(str);
		    return super.execute();
		}catch(Exception ex){
		    System.err.println(ex);
		}	
	    }
	}
	else{		
	    getTimeBlock();
	    if(!id.isEmpty()){
		back = timeBlock.doSelect();
		if(!back.isEmpty()){
		    addActionError(back);
		    addError(back);
		}
		document_id = timeBlock.getDocument_id();
	    }
	}
	return ret;
    }
    public TimeBlock getTimeBlock(){ 
	if(timeBlock == null){
	    timeBlock = new TimeBlock();
	    timewarpManager = new TimewarpManager();
	    if(!id.isEmpty())
		timeBlock.setId(id);
	    if(!document_id.isEmpty()){
		timeBlock.setDocument_id(document_id);
		timewarpManager.setDocument_id(document_id);
	    }
	    if(!date.isEmpty())
		timeBlock.setDate(date);
	    timeBlock.setOrder_index(order_index);
	}
	return timeBlock;
    }
    public void setTimeBlock(TimeBlock val){
	if(val != null){
	    timeBlock = val;
	}
    }

    public String getTimeBlocksTitle(){
	return timeBlocksTitle;
    }
    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
	    action = val;
    }
    public void setOrder_index(int val){
	if(val > 0)
	    order_index = val;
    }
    public int getOrder_index(){
	if(order_index == 0 && timeBlock != null){
	    order_index = timeBlock.getOrder_index();
	}
	return order_index;
    }
    public void setErrors(String val){
	// do nothing
    }
    //
    // this is passed through the link
    public String getDocument_id(){
	if(document_id.isEmpty() && timeBlock != null){
	    document_id = timeBlock.getDocument_id();
	}
	return document_id;
    }
    public Document getDocument(){
	if(document == null){
	    if(document_id.isEmpty()){
		getDocument_id();
	    }
	    if(!document_id.isEmpty()){
		Document one = new Document(document_id);
		String back = one.doSelect();
		if(back.isEmpty()){
		    document = one;
		    employee = document.getEmployee();
		    payPeriod = document.getPayPeriod();
		    /**
		       if(employee != null){
		       department = employee.getDepartment();
		       }
		    */
		    JobTask job = document.getJob();
		    if(job != null){
			Group group = job.getGroup();
			group_id = job.getGroup_id();
			if(group != null){
			    department_id = group.getDepartment_id();
			}
		    }
		}
	    }
	}
	return document;
    }
		
    public void setDocument_id(String val){
	if(val != null && !val.isEmpty())		
	    document_id = val;
    }
    public void setDate(String val){
	if(val != null && !val.isEmpty())		
	    date = val;
    }
    public List<HourCode> getHourCodes(){
	//
	// earn codes are part of finding document
	//
	getDocument();
	findHourCodes();
	return hourCodes;
    }
    void findHourCodes(){
	if(hourCodes == null){
	    getDocument();
	    if(document != null){
		HourCodeList ecl = new HourCodeList();
		String salary_group_id = document.getJob().getSalary_group_id();
		ecl.setSalary_group_id(salary_group_id);
		if(!department_id.isEmpty()){
		    ecl.setDepartment_id(department_id);
		}
		ecl.setGroup_id(group_id);
		ecl.setActiveOnly();
		String back = ecl.lookFor();
		if(back.isEmpty()){
		    List<HourCode> ones = ecl.getHourCodes();
		    if(ones != null && ones.size() > 0){
			hourCodes = ones;
		    }
		}
	    }
	}
    }
    //
    // needed for Police department only (for now)
    //
    void findEarnReason(){
	if(earnReasons == null){
	    getDocument();
	    if(document != null){
		// Police department_id=20
		// if(!department_id.isEmpty() && department_id.equals("20")){
		CodeReasonConditionList crcl = new CodeReasonConditionList();
		String salary_group_id = document.getJob().getSalary_group_id();
		crcl.setSalary_group_id(salary_group_id);
		if(!department_id.isEmpty()){
		    crcl.setDepartment_id(department_id);
		}
		crcl.setActiveOnly();
		String back = crcl.lookFor();
		if(back.isEmpty()){
		    List<EarnCodeReason> ones = crcl.getReasons();
		    if(ones != null && ones.size() > 0){
			earnReasons = ones;
		    }
		}
		System.err.println(" earn reasons "+earnReasons.size());
	    }
	}
    }
    public boolean hasEarnReason(){
	findEarnReason();
	return earnReasons != null && earnReasons.size() > 0;
    }
    public List<EarnCodeReason> getEarnReasons(){
	return earnReasons;
    }
    public boolean hasMonetaryHourCodes(){
	if(hasHourCodes()){
	    monetaryHourCodes = new ArrayList<>();
	    for(HourCode one:hourCodes){
		if(one.isMonetary()){
		    monetaryHourCodes.add(one);
		}
	    }
	}
	return monetaryHourCodes != null && monetaryHourCodes.size() > 0;
    }
    public List<HourCode> getMonetaryHourCodes(){
	return monetaryHourCodes;
    }
    public boolean hasHourCodes(){
	getHourCodes();
	return hourCodes != null;
    }
    // we know we have document_id, we can use to find
    // employee accruals (if any)
    public List<EmployeeAccrual> getEmpAccruals(){
	if(document == null){
	    getDocument();
	}
	if(document.hasEmpAccruals()){
	    employeeAccruals = document.getEmpAccruals();
	}
	return employeeAccruals;
    }
    public boolean hasEmpAccruals(){
	getEmpAccruals();
	boolean ret = employeeAccruals != null && employeeAccruals.size() > 0;
	return ret;
    }

}





































