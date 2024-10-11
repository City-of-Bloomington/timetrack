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
    String[] hr_code_prefered_order = {"4","6","2","8"};
    public String execute(){
	String ret = SUCCESS;
	String back = canProceed("leaveRequest.action");
	if(!back.isEmpty()){
	    return back;
	}
	if(action.equals("Save")){
	    leave.setInitiated_by(user.getId());
	    leave.setJob_id(job_id);
	    leave.setEarn_code_ids(earn_code_ids);
	    back = leave.doSave();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		if(activeMail){
		//if(true){
		    // now iform the supervisor		    
		    back = informManager();
		}
		addMessage("Saved Successfully");
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
    
    public List<LeaveRequest> getRequests(){
	if(requests == null){
	    LeaveRequestList tl = new LeaveRequestList();
	    //tl.setInitiated_by(user.getId());
	    tl.setJob_id(job_id);
	    tl.setActiveOnly();
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
    public boolean hasRequests(){
	getRequests();
	return requests != null && requests.size() > 0;
    }
    public List<HourCode> getHourCodes(){
	//
	// earn codes are part of finding document
	//
	findHourCodes();
	addAccrualsToHourCodes();
	reorderHourCodes();
	return hourCodes;
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
	}
	GroupManager groupManager = leave.getManager();
	if(groupManager != null){
	    Employee one = groupManager.getEmployee();
	    if(one != null)
		manager = one;
	    email_to = manager.getEmail();
	}

	/**
	email_msg = "<!DOCTYPE html>";
	email_msg += "<htmL lang=\"en\">";
	email_msg += "<head>";
	email_msg += "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>\n";
	email_msg += "<title>Leave Request</title>\n";
	email_msg += "</head>\n";
	email_msg += "<body>\n";
	*/
	email_msg = "Hi "+manager.getFull_name()+"\n\n";
	email_msg += "I am requesting "+leave.getTotalHours()+" hrs of '"+
	    leave.getEarnCodes()+"' leave from "+leave.getJobTitle()+" position for the period "+leave.getDate_range()+".\n\n"; 
	if(leave.hasNotes()){
	    email_msg += "Leave Description: "+leave.getRequestDetails()+
"\n\n";
	}
	email_msg += "Thanks\n\n";
	email_msg += emp.getFull_name();
	email_msg += "\n\n";
	// email_msg += "<br /><br />";
	// email_msg += "</body></html>";
	//
	// text for logs
	String email_txt ="Hi "+manager.getFull_name()+"\n";
	email_txt += "I am requesting "+leave.getTotalHours()+" hrs of "+
	    leave.getEarnCodes()+" leave from "+leave.getJobTitle()+" position for the period "+leave.getDate_range()+".\n"; 

	if(leave.hasNotes()){
	    email_txt += "<b>Leave Description:</b> "+leave.getRequestDetails()+"\n";
	}
	email_txt += "Thanks ";
	email_txt += emp.getFull_name();


	    
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
					      email_txt,
					      "Request",
					      back);
	back += lel.doSave();
	return back;
    }
}





































