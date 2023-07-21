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

public class JobTerminateAction extends TopAction{

    static final long serialVersionUID = 1150L;	
    static Logger logger = LogManager.getLogger(JobTerminateAction.class);
    //
    String  pay_period_id="",
	job_id="", emp_id="",
	department_id="",
	expire_date="", source="";
    List<Document> documents = null;
    Employee emp = null;
    EmpTerm term = null;
    CleanUp cleanUp = null;
    JobTask job = null;
    List<PayPeriod> payPeriods = null;
		
    String terminateTitle = "Employee Job Termination";
    public String execute(){
	String ret = SUCCESS;
	String back = doPrepare("jobTerminate.action");
	if(!action.isEmpty()){ // normally 'Submit'
	    if(term == null){
		getTerm();
	    }
	    term.setJob_id(job_id);
	    term.setExpire_date(expire_date);
	    back = term.doTerminate();
	    if(!back.isEmpty()){
		back = "could not get employee info ";
		addError(back);
	    }
	    else{
		addMessage("Terminated successfully");
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
		else{
		    addMessage("No document found for cleanup");
		}
	    }
	}
	else {
	    getUser();
	    if(user != null && !(user.isAdmin() || user.isHrAdmin())){
		if(user.hasDepartment()){
		    department_id = user.getDepartment_id();
		}
	    }
	    getTerm();
	}
	return ret;
    }
    public EmpTerm getTerm(){
	if(term == null){
	    term = new EmpTerm();
	    if(!job_id.isEmpty()){
		term.setJob_id(job_id);
		term.setExpire_date(expire_date);
	    }
	}
	return term;
    }
    public void setTerm(EmpTerm val){
	if(val != null){
	    term = val;
	    term.setJob_id(job_id);
	    term.setExpire_date(expire_date);
	}
    }
    public void setCleanUp(CleanUp val){
	if(val != null)
	    cleanUp = val;
    }
    public CleanUp getCleanUp(){
	cleanUp = new CleanUp();
	if(hasDocuments()){
	    cleanUp.setDocuments(documents);
	}
	return cleanUp;
    }
    void findDocuments(){
	//
	if(!expire_date.isEmpty()){
	    PayPeriod pp = new PayPeriod();
	    if(pp.findByEndDate(expire_date)){
		pay_period_id = pp.getId();
	    }
	    else{
		System.err.println(" could not find pay_period_id for "+expire_date);
	    }
	}
	if(documents == null
	   && (!job_id.isEmpty())
	   && !pay_period_id.isEmpty()){
	    DocumentList dl = new DocumentList();
	    dl.setJob_id(job_id); // in case one job
	    dl.setPay_period_id(pay_period_id);
	    String back = dl.findForCleanUp();
	    if(back.isEmpty()){
		List<Document> ones = dl.getDocuments();
		if(ones != null && ones.size() > 0){
		    documents = ones;
		}
	    }
	}
    }
    boolean hasDocuments(){
	findDocuments();
	return documents != null && documents.size() > 0;
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
    public String getJob_id(){
	return job_id;
    }    
				
    public void setDepartment_id(String val){
	if(val != null && !val.isEmpty())		
	    department_id = val;
    }		
    public void setSource(String val){
	if(val != null && !val.isEmpty())		
	    source = val;
    }		
    public String getExpire_date(){
	return expire_date;
    }
    public void setExpire_date(String val){
	if(val != null && !val.equals("-1"))
	    expire_date = val;
    }
    public void setJob_id(String val){
	if(val != null && !val.isEmpty())
	    job_id = val;
    }
    public boolean hasEmpId(){
	if(emp_id.isEmpty() && !job_id.isEmpty()){
	    if(job == null){
		JobTask one = new JobTask(job_id);
		String back = one.doSelect();
		if(back.isEmpty()){
		    job = one;
		    emp_id = one.getEmployee_id();
		}
	    }
	}
	return !emp_id.isEmpty();
    }
    public String getEmp_id(){
	hasEmpId();
	return emp_id;
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





































