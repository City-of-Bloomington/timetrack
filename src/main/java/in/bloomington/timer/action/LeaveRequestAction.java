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

public class LeaveRequestAction extends TopAction{

    static final long serialVersionUID = 3800L;	
    static Logger logger = LogManager.getLogger(LeaveRequestAction.class);
    //
    String pay_period_id = "", job_id = "";
    String leavesTitle = "Previous Leave Requests";
    LeaveRequest leave = null;
    List<LeaveRequest> requests = null;
    List<HourCode> hourCodes = null;
    JobTask job = null;
    String earn_code_id="";
    public String execute(){
	String ret = SUCCESS;
	String back = canProceed("leaveRequest.action");
	if(!back.isEmpty()){
	    return back;
	}
	if(action.equals("Save")){
	    leave.setInitiated_by(user.getId());
	    leave.setJob_id(job_id);
	    leave.setEarn_code_id(earn_code_id);
	    back = leave.doSave();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		addMessage("Saved Successfully");
	    }
	}				
	else if(action.startsWith("Save")){
	    leave.setEarn_code_id(earn_code_id);
	    leave.setInitiated_by(user.getId());
	    back = leave.doUpdate();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		addMessage("Updated Successfully");
		ret = "view";
	    }
	}
	else if(action.startsWith("Edit")){
	    if(!id.isEmpty()){
		getLeave();
		back = leave.doSelect();
		if(!back.isEmpty()){
		    addError(back);
		}
		job_id = leave.getJob_id();
		earn_code_id = leave.getEarn_code_id();
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
		earn_code_id = leave.getEarn_code_id();
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
    public void setEarn_code_id(String val){
	if(val != null && !val.isEmpty())		
	    earn_code_id = val;
    }    
    public String getJob_id(){
	return job_id;
    }
    public String getEarn_code_id(){
	if(earn_code_id.isEmpty()){
	    earn_code_id = leave.getEarn_code_id();
	}
	return earn_code_id;
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
	return hourCodes;
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
}





































