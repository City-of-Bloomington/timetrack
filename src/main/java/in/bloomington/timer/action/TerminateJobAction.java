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

public class TerminateJobAction extends TopAction{

    static final long serialVersionUID = 1150L;	
    static Logger logger = LogManager.getLogger(TerminateJobAction.class);
    //
    String emp_id = "", pay_period_id="",
	full_name="",
	department_id="",
	expire_date="", source="";
    String last_pay_priod_date = "";
    List<Document> documents = null;
    Employee emp = null;
    EmpTerminate term = null;
    String job_id = "", group_id="";
    String[] selected_job_ids = null;
    CleanUp cleanUp = null;
    JobTask job = null;
    Group group = null;
    List<JobTask> jobs = null;
    List<PayPeriod> payPeriods = null;
    boolean hasMoreThanOneJob = false;
    String terminateTitle = "Employee Termination Wizard";
    public String execute(){
	String ret = SUCCESS;
	String back = doPrepare("terminate.action");
	if(action.isEmpty()){ // normally 'Submit'
	    if(!id.isEmpty()){
		term = new EmpTerminate(id);
		back = term.doSelect();
		if(!back.isEmpty()){
		    addError(back);
		}
		else{
		    ret = "view";
		}
	    }
	    else{
		if(!job_id.isEmpty()){
		    hasMoreThanOneJob = haveMultipleJobsInTheSameGroup();
		}
		if(hasMoreThanOneJob){
		    ret = "select_jobs";
		}
		else { // one job
		    ret = "set_expire_date";
		}
	    }
	}
	else if(action.equals("Edit")){
	    term = new EmpTerminate(id);
	    back = term.doSelect();
	    if(!back.isEmpty()){
		addError(back);
	    }
	}
	else if(action.equals("Next")){
	    if(hasJobWithBenefits() || hasOneJobOnly()){
		if(last_pay_priod_date.isEmpty()){
		    addMessage("Last pay period date is required");
		    return "set_expire_date";
		}
		else{
		    getTerm();
		    term.setJob_id(job_id);
		    term.setSupervisor(user);
		    term.setSubmitted_by(user);
		    term.setSubmitted_date(Helper.getToday());
		    term.setLast_pay_period_date(last_pay_priod_date);
		    back = term.populateOneJob();
		    if(!back.isEmpty()){
			addError(back);
		    }
		    back = term.findDocumentForInfo();
		    if(!back.isEmpty()){
			addError(back);
		    }
		    term.setAccrualValues();
		}
	    }
	    else {
		if(selected_job_ids  != null && !last_pay_priod_date.isEmpty()){
		    System.err.println(" "+selected_job_ids.length);
		    // added selected jobs to term class
		    getTerm();
		    for(String str:selected_job_ids){
			term.setJob_id(str);
		    }
		    term.setSupervisor(user);
		    term.setSubmitted_by(user);
		    term.setSubmitted_date(Helper.getToday());
		    term.setLast_pay_period_date(last_pay_priod_date);
		    back = term.populateMulitJobs();
		    if(!back.isEmpty()){
			addError(back);
		    }		
		}
		else{
		    addError("No job selected Or last pay period date not set");
		    haveMultipleJobsInTheSameGroup();
		    ret = "select_jobs";
		}
	    }
	}
	else if(action.equals("Submit")){
	    if(term != null){
		term.setSubmitted_by(user);
		back = term.doSave();
		if(back.isEmpty()){
		    back = term.doTerminate();
		}
		if(!back.isEmpty()){
		    addError(back);
		}
		else {
		    addMessage("Successfully terminated");
		}
	    }
	}
	else if(action.startsWith("Save")){ //update
	    if(term != null){
		term.setSubmitted_by(user);
		back = term.doUpdate();
		if(!back.isEmpty()){
		    addError(back);
		}
		else {
		    addMessage("Successfully updated");
		}
	    }
	}
	else if(action.startsWith("Send")){
	    getTerm();
	    if(term != null){
		back = term.doSelect();
		TermNotification tn = new TermNotification();
		tn.setTerm(term);
		tn.setSender(user);
		back = tn.doSend(mail_host);
		if(!back.isEmpty()){
		    addError(back);
		}
		else {
		    back = term.changeRecipientInformFlag();
		    if(!back.isEmpty()){
			addError(back);
		    }
		    else
			addMessage("Successfully Sent");
		}
	    }
	}	
	return ret;
    }
    public EmpTerminate getTerm(){
	if(term == null){
	    term = new EmpTerminate();
	    if(!id.isEmpty()){
		term.setId(id);
	    }
	}
	return term;
    }
    public void setTerm(EmpTerminate val){
	if(val != null){
	    term = val;
	}
    }
    boolean hasOneJobOnly(){
	return !haveMultipleJobsInTheSameGroup();
    }
    boolean haveMultipleJobsInTheSameGroup(){
	if(!job_id.isEmpty()){
	    JobTask job = new JobTask(job_id);
	    String back = job.doSelect();
	    if(back.isEmpty()){
		String group_id = job.getGroup_id();
		emp_id = job.getEmployee_id();
		JobTaskList jl = new JobTaskList();
		jl.setGroup_id(group_id);
		jl.setEmployee_id(emp_id);
		jl.setNotExpired();
		back = jl.find();
		if(back.isEmpty()){
		    List<JobTask> ones = jl.getJobs();
		    if(ones != null){
			if(ones.size() > 1){
			    jobs = ones;
			    return true;
			}
			else if(ones.size() == 1){
			    job = ones.get(0);
			}
		    }
		}
	    }
	}
	return false;
    }
    public boolean hasMultiJobs(){
	return jobs != null && jobs.size() > 1;
    }
    public List<JobTask> getJobs(){
	return jobs;
    }
    public JobTask getJob(){
	if(job == null && jobs != null){
	    job = jobs.get(0); // we need one
	    group = job.getGroup();
	}
	return job;
    }
    public boolean hasJobWithBenefits(){
	getJob();
	if(job != null){
	   SalaryGroup sg = job.getSalaryGroup();
	   if(sg != null){
	       if(sg.isTemporary()) return false;
	       else return true;
	   }
	}
	return false;
    }
    public boolean hasGroupInfo(){
	getGroup();
	return group != null;
    }
    public Group getGroup(){
	if(group == null){
	    getJob();
	}
	return group;
    }
    public void setSelected_job_id(String[] vals){
	if(vals != null){
	    selected_job_ids = vals;
	}
    }
    public String getTermiateTitle(){
	return terminateTitle;
    }
    public String getDepartment_id(){
	return department_id;
    }
    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
	    action = val;
    }

    public void setJob_id(String val){
	if(val != null && !val.isEmpty())		
	    job_id = val;
    }
    public void setGroup_id(String val){
	if(val != null && !val.isEmpty())		
	    group_id = val;
    }
    public String getJob_id(){
	return job_id;
    }
    public String getGroup_id(){
	return group_id;
    }

    public void setEmp_id(String val){
	if(val != null && !val.isEmpty())		
	    emp_id = val;
    }
    public void setLast_pay_period_date(String val){
	if(val != null && !val.isEmpty() && !val.equals("-1"))		
	    last_pay_priod_date = val;
    }
    public void setSource(String val){
	if(val != null && !val.isEmpty())		
	    source = val;
    }		
    public List<PayPeriod> getPayPeriods(){
	if(payPeriods == null){
	    PayPeriodList tl = new PayPeriodList();
	    tl.setAheadPeriods(4);
	    tl.setLimit("10");
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





































