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
		    ret = "view";
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
	else if(action.equals("Edit")){
	    term = new EmpTerminate(id);
	    back = term.doSelect();
	    if(!back.isEmpty()){
		addError(back);
	    }
	}
	/**
	else if(action.equals("Next")){ 
	    
	    if(hasJobWithBenefits()){
		if(last_pay_period_date.isEmpty()){
		    addMessage("Last pay period date is required");
		    return "set_expire_date";
		}
		else{
		    getTerm();
		    term.setEmployee_id(emp_id);
		    //term.setJob_id(job_id);
		    // term.findSupervisor();
		    //term.findSupervisorPhone(envBean);
		    term.setEmployee_id(emp_id);
		    term.setJob_ids(selected_job_ids);
		    term.setSubmitted_by(user);
		    term.setSubmitted_date(Helper.getToday());
		    term.setLast_pay_period_date(last_pay_period_date);
		    back = findMatchingJobsInNW();
		    if(back.isEmpty()){
			term.setJobTerms(jobTerms);
		    }

		    back = term.populateOneJob();
		    if(!back.isEmpty()){
			addError(back);
		    }

		    back = term.findEmployeeAddress();
		    if(!back.isEmpty()){
			addError(back);
		    }
		    back = term.findDocumentForInfo();
		    if(!back.isEmpty()){
			addError(back);
		    }
		    term.setAccrualValues();
		    term.doSave();// process_status = "Started"
		}
	    }
	    else {
		// termination of one or more jobs but not final
		if((!job_id.isEmpty() || selected_job_ids!= null) &&
		   !last_pay_period_date.isEmpty()){
		    back = expireSelectedJobs(selected_job_ids);
		    if(hasDocuments()){
			getCleanUp();
			back = cleanUp.doClean();
			if(back.isEmpty()){
			    addMessage("Cleanup Success");
			}
			else{
			    addError(back);
			}
		    }
		    if(back.isEmpty())
			addMessage("Termination Success");
		    ret = "select_jobs";		    
		}
		else{
		    addError("No job selected Or last pay period date not set");
		    ret = "select_jobs";
		    return ret;
		}
	    }
	}
	*/
	else if(action.startsWith("Next")){ 
	    //
	    if((!job_id.isEmpty() || selected_job_ids!= null) &&
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
		/**
		term.setProcess_status("Ready");
		back = term.doUpdate();
		if(!back.isEmpty()){
		    addError(back);
		}
		*/
	    }
	    else{
		addError("last pay period date not set");
		ret = "select_jobs";
		return ret;
	    }
	}	    
	else if(action.equals("Submit")){ 
	    if(term != null){
		back = term.expireSelectedJobs();
		if(!back.isEmpty()){
		    addError(back);
		}		
		back += term.doTerminate(); // include clean
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
		    addMessage("Saved Successfully");
		}
	    }
	}
	/**
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
	*/
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
	    System.err.println(" NW "+back);
	    List<JobTerminate> ones = nej.findMatchingJobs();
	    if(ones != null){
		jobTerms = ones;
		for(JobTerminate jj:jobTerms){
		    jj.setTerminate_id(id);
		    jj.findSupervisor();
		    jj.findSupervisorInfo(envBean);
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
	if(!job_id.isEmpty()){
	    JobTask job = new JobTask(job_id);
	    back = job.doSelect();
	    if(back.isEmpty()){
		emp_id = job.getEmployee_id();
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
    /**
    String expireSelectedJobs(String[] jjs){
	String back = "";
	for(String j:jjs){
	    JobTask one = new JobTask(j);
	    back = one.doSelect();
	    one.setExpire_date(last_pay_period_date);
	    back = one.doUpdate();
	}
	return back;
    }
    */
    /**
    public CleanUp getCleanUp(){
	cleanUp = new CleanUp();
	if(hasDocuments()){
	    cleanUp.setDocuments(documents);
	}
	return cleanUp;
    }
    void findDocuments(){
	//
	findPayPeriodId();
	if(documents == null
	   && selected_job_ids != null
	   && !pay_period_id.isEmpty()){
	    
	    for(String jj_id:selected_job_ids){
		DocumentList dl = new DocumentList();
		dl.setJob_id(jj_id); // in case one job
		dl.setPay_period_id(pay_period_id);
		String back = dl.findForCleanUp();
		if(back.isEmpty()){
		    List<Document> ones = dl.getDocuments();
		    if(ones != null && ones.size() > 0){
			if(documents == null)
			    documents = ones;
			else{
			    documents.addAll(ones);
			}
		    }
		}
	    }
	}
    }
    String findPayPeriodId(){
	String back="";
	if(pay_period_id.isEmpty()){
	    PayPeriod pp = new PayPeriod();	
	    if(pp.findByEndDate(last_pay_period_date)){
		pay_period_id = pp.getId();
	    }
	    else{
		back = " could not find pay_period_id for "+expire_date;
	    }
	}
	return back;
    }
    // when terminating all jobs for an employee
    boolean hasDocuments(){
	findDocuments();
	return documents != null && documents.size() > 0;
    }
    */
}





































