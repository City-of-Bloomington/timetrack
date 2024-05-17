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
    String last_pay_period_date = "", last_day_of_work = "";
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
    List<JobTerminate> jobTerms = null;
    boolean hasMoreThanOneJob = false;
    boolean hasTempJobs = false;
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
		    if(!term.isStarted()){
			System.err.println(" ret view ");
			ret = "view";
		    }
		    else{
			back += term.findEmployeeAddress();
			if(!back.isEmpty()){
			    addError(back);
			}
			if(hasJobWithBenefits()){
			    back += term.findDocumentForInfo();
			    if(!back.isEmpty()){
				addError(back);
			    }
			    term.setAccrualValues();
			}
		    }
		}
	    }
	    else{
		if(!job_id.isEmpty()){
		    hasTempJobs = !hasJobWithBenefits();
		}
		if(hasTempJobs){
		    ret = "select_jobs";
		}
		else { // one job
		    ret = "set_expire_date";
		}
	    }
	}
	else if(action.startsWith("Next")){ 
	    //
	    if(selected_job_ids != null &&
	       !last_day_of_work.isEmpty() &&
	       !last_pay_period_date.isEmpty()){
		getTerm();
		if(emp_id.isEmpty()){
		    getJob(); // to get emp_id
		}
		term.setEmployee_id(emp_id);
		term.setJob_ids(selected_job_ids);
		// term.findAllJobs();
		// term.findSupervisor();
		// term.findSupervisorPhone(envBean);
		term.setSubmitted_by(user);
		term.setSubmitted_date(Helper.getToday());
		term.setLast_pay_period_date(last_pay_period_date);
		back = term.doSave();
		if(!back.isEmpty()){
		    addError(back);
		}
		id = term.getId();
		back += findMatchingJobsInNW();
		if(back.isEmpty() && jobTerms.size() > 0){
		    term.setJobTerms(jobTerms);
		}
		back = term.populateOneJob(); 
		if(!back.isEmpty()){
		    addError(back);
		}
		back += term.findEmployeeAddress();
		if(!back.isEmpty()){
		    addError(back);
		}
		if(hasJobWithBenefits()){
		    back += term.findDocumentForInfo();
		    if(!back.isEmpty()){
			addError(back);
		    }
		    term.setAccrualValues();
		}
	    }
	    else{
		if(last_pay_period_date.isEmpty())
		    addError("last pay period date not set");
		if(last_day_of_work.isEmpty())
		    addError("Last day of work not set");
		if(selected_job_ids == null)
		    addError("No job selected ");
		ret = "select_jobs";
		findAllJobs();
		return ret;
	    }
	}	    
	else if(action.equals("Submit")){ 
	    if(term != null){
		back = term.findJobTerms();
		back += term.doTerminate(); 
		if(!back.isEmpty()){
		    addError(back);
		}
		term.setSubmitted_by(user);
		term.setProcess_status("Ready");
		back += term.doUpdate();
		if(!back.isEmpty()){
		    addError(back);
		}
		else {
		    addMessage("Terminated Successfully");
		}
	    }
	}
	else if(action.startsWith("Send")){
	    getTerm();
	    if(term != null){
		back = term.doSelect();
		// back = term.doTerminate();
		TermNotification tn = new TermNotification();
		tn.setTerm(term);
		tn.setTermination_id(term.getId());
		tn.setSender(user);
		back = tn.doSend(mail_host);
		if(!back.isEmpty()){
		    addError(back);
		}
		else {
		    back = term.changeInformFlagAndCompletedStatus();
		    if(!back.isEmpty()){
			addError(back);
		    }
		    else
			addMessage("Send Successfully");
		    ret = "view";
		}
	    }
	}	
	return ret;
    }

    String findMatchingJobsInNW(){
	String back = "";
	getEmp();
	if(emp != null){
	    NWEmployeeJobs nej = new NWEmployeeJobs(envBean,
						    emp.getId(),
						    emp.getEmployee_number(),
						    last_day_of_work,
						    selected_job_ids);

	    back = nej.find();
	    if(hasJobWithBenefits()){
		if(nej.hasNwJob()){
		    JobTerminate jj = nej.updateFoundJob();
		    jj.setBadge_code(emp.getId_code());
		    back += jj.doSave();
		    if(!back.isEmpty()){
			System.err.println(" after jobterm save "+back);
		    }
		    jobTerms = new ArrayList<>();
		    jobTerms.add(jj);
		}
		else{
		    back = "No job term found ";
		    System.err.println(" no match found ");
		}
	    }
	    else{
		List<JobTerminate> ones = nej.findMatchingJobs();
		if(ones != null){
		    jobTerms = ones;
		    for(JobTerminate jj:jobTerms){
			jj.setTerminate_id(id);
			// jj.findSupervisor();
			// jj.findSupervisorInfo(envBean);
			jj.setBadge_code(emp.getId_code());
			back += jj.doSave();
		    }
		    if(!back.isEmpty()){
			System.err.println(" after jobterm save "+back);
		    }
		}
		else{
		    back = "No job term found ";
		    System.err.println(" no match found ");
		}
	    }
	}
	return back;
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
	findAllJobs();
	return jobs != null && jobs.size() == 1;
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
    String findAllJobs(){
	String back = "";
	if(emp_id.isEmpty() && !job_id.isEmpty()){
	    JobTask job = new JobTask(job_id);
	    back = job.doSelect();
	    if(back.isEmpty()){
		emp_id = job.getEmployee_id();
	    }
	}
	if(!emp_id.isEmpty()){
	    JobTaskList jl = new JobTaskList();
	    jl.setEmployee_id(emp_id);
	    jl.setNotExpired();
	    back = jl.find();
	    if(back.isEmpty()){
		List<JobTask> ones = jl.getJobs();
		if(ones != null){
		    if(ones.size() > 0){
			jobs = ones;
			job = ones.get(0);
		    }
		}
	    }
	}
	return back;
    }    
    public boolean hasJobs(){
	if(jobs == null)
	    findAllJobs();
	return jobs != null && jobs.size() > 0;
    }
    public boolean hasMultiJobs(){
	if(jobs == null)
	    findAllJobs();
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
	else if(!job_id.isEmpty()){
	    JobTask one = new JobTask(job_id);
	    String back = one.doSelect();
	    if(back.isEmpty()){
		job = one;
		emp_id = job.getEmployee_id();
	    }
	}
	else if(selected_job_ids != null){
	    job_id = selected_job_ids[0];
	    JobTask one = new JobTask(job_id);
	    String back = one.doSelect();
	    if(back.isEmpty()){
		job = one;
		emp_id = job.getEmployee_id();
	    }	    
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
    public Employee getEmp(){
	if(emp == null){
	    if(emp_id.isEmpty()){
		getJob();
		if(job != null){
		    emp = job.getEmployee();
		}
	    }
	    else if(!emp_id.isEmpty()){
		Employee one = new Employee(emp_id);
		String back = one.doSelect();
		if(back.isEmpty()){
		    emp = one;
		}
	    }
	}
	return emp;
    }
    public void setEmp_id(String val){
	if(val != null && !val.isEmpty())		
	    emp_id = val;
    }
    public String getEmp_id(){
	return emp_id;
    }
    public void setLast_pay_period_date(String val){
	if(val != null && !val.isEmpty() && !val.equals("-1"))		
	    last_pay_period_date = val;
    }
    public void setLast_day_of_work(String val){
	if(val != null && !val.isEmpty())		
	    last_day_of_work = val;
    }    
    public void setSource(String val){
	if(val != null && !val.isEmpty())		
	    source = val;
    }
    public String getLast_day_of_work(){
	return last_day_of_work;
    }
    public String getLast_pay_period_date(){
	return last_pay_period_date;
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





































