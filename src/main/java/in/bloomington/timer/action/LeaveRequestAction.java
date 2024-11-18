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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LeaveRequestAction extends TopAction{

    static final long serialVersionUID = 3800L;	
    static Logger logger = LogManager.getLogger(LeaveRequestAction.class);
    //
    String pay_period_id = "", job_id = "";
    String leavesTitle = "Previous Leave Requests";
    LeaveRequest leave = null;
    List<LeaveRequest> requests = null;
    List<HourCode> hourCodes = null;
    Document document = null;
    List<EmployeeAccrual> empAccruals = null;
    JobTask job = null;
    String[] earn_code_ids = {""};
    String[] earn_code_ids_alt = {""};
    String[] hr_code_prefered_order = {"2","4","6","8"};
    String[] hr_code_exclude = {"3","73","120","147" };
    public String execute(){
	getUser();
	resetEmployee();
	String ret = SUCCESS;
	String back = doPrepare("leaveRequest.action");
	if(!back.isEmpty()){
	    return back;
	}
	if(action.startsWith("Submit")){
	    leave.setInitiated_by(user.getId());
	    leave.setJob_id(job_id);
	    leave.setEarn_code_ids(earn_code_ids);
	    back = leave.doSave();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		// if(true){
		if(activeMail){
		    back = informManager();
		    if(!back.isEmpty()){ // saved even if manger not informed
			addMessage(back);
		    }
		}
		// reset
		earn_code_ids = new String[1];
		earn_code_ids[0] = ""; 
		leave = new LeaveRequest();
		leave.setJob_id(job_id);
		leave.setInitiated_by(user.getId());
		addMessage("Submitted Successfully");
	    }
	}   // update is not really needed
	else if(action.startsWith("Save")){
	    leave.setEarn_code_ids(earn_code_ids);
	    leave.setInitiated_by(user.getId());
	    back = leave.doUpdate();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		addMessage("Updated Successfully");
		ret = "view";
	    }
	} // not needed as well
	else if(action.startsWith("Edit")){
	    if(!id.isEmpty()){
		getLeave();
		back = leave.doSelect();
		if(!back.isEmpty()){
		    addError(back);
		}
		job_id = leave.getJob_id();
		earn_code_ids = leave.getEarn_code_ids();
	    }
	}	
	else{		
	    getLeave();
	    if(!id.isEmpty()){
		back = leave.doSelect();
		if(!back.isEmpty()){
		    addError(back);
		}
		job_id = leave.getJob_id();
		earn_code_ids = leave.getEarn_code_ids();
		ret = "view";
	    }
	}
	return ret;
    }
    public LeaveRequest getLeave(){
	if(leave == null){
	    leave = new LeaveRequest();
	    leave.setId(id);
	    leave.setInitiated_by(user.getId());
	    leave.setJob_id(job_id);
	}
	return leave;
						
    }
    public void setLeave(LeaveRequest val){
	if(val != null){
	    leave = val;
	}
    }
		
    public String getLeavesTitle(){
				
	return leavesTitle;
    }
    public Document getDocument(){
	if(leave != null)
	    document = leave.getDocument();
	return document;
    }
    public boolean hasDocument(){
	getDocument();
	return document != null;
    }
    boolean hasAccruals(){
	if(empAccruals == null){
	    findEmployeeAccruals();
	}
	return empAccruals != null && empAccruals.size() > 0;
    }
    void findEmployeeAccruals(){
	if(hasDocument()){
	    getDocument();
	    if(document != null){
		if(document.hasEmpAccruals()){
		    List<EmployeeAccrual> ones = document.getEmpAccruals();
		    if(ones != null && ones.size() > 0){
			empAccruals = ones;
		    }
		}
	    }
	}
    }
    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
	    action = val;
    }
    public void setPay_period_id(String val){
	if(val != null && !val.isEmpty())		
	    pay_period_id = val;
    }
    public void setJob_id(String val){
	if(val != null && !val.isEmpty())		
	    job_id = val;
    }
    public void setEarn_code_ids(String[] val){
	if(val != null)		
	    earn_code_ids = val;
    }
    public String getJob_id(){
	return job_id;
    }
    public String[] getEarn_code_ids(){
	if(earn_code_ids == null){
	    earn_code_ids = leave.getEarn_code_ids();
	}
	return earn_code_ids;
    }
    void findCurrentPayPeriod(){
	//
	if(pay_period_id.isEmpty()){
	    PayPeriodList ppl = new PayPeriodList();
	    ppl.currentOnly();
	    String back = ppl.find();
	    if(back.isEmpty()){
		List<PayPeriod> ones = ppl.getPeriods();
		if(ones != null && ones.size() > 0){
		    pay_period_id = ones.get(0).getId();
		}
	    }
	}
    }    
    public List<LeaveRequest> getRequests(){
	if(requests == null){
	    findCurrentPayPeriod();
	    LeaveRequestList tl = new LeaveRequestList();
	    //tl.setInitiated_by(user.getId());
	    tl.setJob_id(job_id);
	    tl.setActiveOnly();
	    tl.setPay_period_id(pay_period_id);
	    // tl.setCurrentAndFuture();
	    //tl.setDecided();
	    String back = tl.find();
	    if(back.isEmpty()){
		List<LeaveRequest> ones = tl.getRequests();
		if(ones != null && ones.size() > 0){
		    requests = ones;
		}
	    }
	}
	return requests;
    }
    public boolean hasDecidedRequests(){
	getRequests();
	return requests != null && requests.size() > 0;
    }
    public List<HourCode> getHourCodes(){
	//
	// earn codes are part of finding document
	//
	findHourCodes();
	removeExcludedHourCodes();
	addAccrualsToHourCodes();
	reorderHourCodes();
	return hourCodes;
    }
    public String getHourCodesListSize(){
	String size="0";
	size = ""+getHourCodes().size();
	return size;
    }
	
    void removeExcludedHourCodes(){
	List<HourCode> ll = new ArrayList<>();
	if(hourCodes != null && hourCodes.size() > 0){
	    for(String str:hr_code_exclude){
		for(int jj=0;jj<hourCodes.size();jj++){
		    HourCode one = hourCodes.get(jj);
		    if(one.getId().equals(str)){
			hourCodes.remove(jj);
		    }
		}
	    }
	}
    }    
    void reorderHourCodes(){
	List<HourCode> ll = new ArrayList<>();
	if(hourCodes != null && hourCodes.size() > 0){
	    for(String str:hr_code_prefered_order){
		for(int jj=0;jj<hourCodes.size();jj++){
		    HourCode one = hourCodes.get(jj);
		    if(one.getId().equals(str)){
			ll.add(one);
			hourCodes.remove(jj);
		    }
		}
	    }
	    if(ll.size() > 0){
		ll.addAll(hourCodes);
		hourCodes = ll;
	    }
	}
    }
    void addAccrualsToHourCodes(){
	if(hasAccruals() && hourCodes != null){
	    for(EmployeeAccrual one:empAccruals){
		if(one.getHours() > 0){
		    for(HourCode code:hourCodes){
			if(code.getId().equals(one.getRelated_hour_code_id())){
			    code.setDescription(code.getDescription()+" ("+one.getHours()+")");
			}
		    }
		}
	    }
	}
    }
    public JobTask getJob(){
	if(!job_id.isEmpty()){
	    JobTask one = new JobTask(job_id);
	    String back = one.doSelect();
	    if(back.isEmpty()){
		job = one;
	    }
	}
	return job;
    }
    void findHourCodes(){
	if(hourCodes == null){
	    getJob();
	    if(job != null){
		HourCodeList ecl = new HourCodeList();
		String salary_group_id = job.getSalary_group_id();
		String group_id = job.getGroup_id();
		String department_id = job.getGroup().getDepartment_id();
		ecl.setSalary_group_id(salary_group_id);
		if(!department_id.isEmpty()){
		    ecl.setDepartment_id(department_id);
		}
		ecl.setGroup_id(group_id);
		ecl.setType("Used"); //
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
    String informManager(){
	String back = "";
	    
	if(leave == null){
	    back = "No leave request to process";
	    return back;
	}
	String manager_email="";
	String emp_email="";
	String subject = "";
	String email_msg = "";
	String email_from = "";
	String email_to = "";
	Employee manager = null;	
	Employee emp = leave.getEmployee();
	subject = "Leave request for "+emp.getFull_name();
	if(emp != null){
	    email_from = emp.getEmail();
	}		// we need only one

	GroupManager groupManager = leave.getManager();
	if(groupManager != null){
	    Employee one = groupManager.getEmployee();
	    if(one != null)
		manager = one;
	    email_to = manager.getEmail();
	}
	else{
	    back = "No group manager to inform ";
	    return back;
	}
	/**
	email_msg = manager.getFull_name()+", this is an automated notice sent by the Time Track system on behalf of "+emp.getFull_name()+", "+leave.getJobTitle()+".\n";
	*/
	if(leave.isSameDayLeave()){
	    email_msg += emp.getFull_name()+" requests "+leave.getTotalHours()+" hours of "+leave.getEarnCodes()+" leave from their "+leave.getJobTitle()+" position on "+leave.getStartDateFF()+".\n\n";
	}
	else{
	    email_msg += emp.getFull_name()+" requests "+leave.getTotalHours()+" hours of "+leave.getEarnCodes()+" leave from their "+leave.getJobTitle()+" position for "+leave.getDate_range()+".\n\n";
	    
	}
	if(leave.hasNotes()){
	    
	    email_msg += "Leave Description: "+leave.getRequestDetails()+
"\n\n";
	}
	else{
	    email_msg += "\n\n";
	}
	email_msg += " Go to Time Track Leave Review (https://bloomington.in.gov/timetrack/leave_review.action) to review this request.\n\n";
	
	MailHandle mailer = new
	    MailHandle(mail_host,
		       email_to,
		       email_from,
		       email_from, // cc
		       null,
		       subject,
		       email_msg
		       );
	back += mailer.send();
	LeaveEmailLog lel = new LeaveEmailLog(
					      email_to,
					      email_from,
					      email_msg,
					      "Request",
					      back);
	back += lel.doSave();
	return back;
    }
}





































