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

public class NWEmpJobsAction extends TopAction{

    static final long serialVersionUID = 3800L;	
    static Logger logger = LogManager.getLogger(NWEmpJobsAction.class);
    //
    String date = "04/18/2024";
    String emp_number="2661", emp_id="1635";
    List<JobTask> jobs = null;
    NWEmployeeJobs nwEmpJobs = null;
    String pay_period_id = "686";
    public String execute(){
	String ret = SUCCESS;
	
	String back = "";
	if(!action.isEmpty()){
	    getNwEmpJobs();
	    back = nwEmpJobs.find3();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		addMessage("Success");
	    }
	}				
	return ret;
    }
    private void getNwEmpJobs(){
	getJobs();
	if(jobs == null){
	    System.err.println(" no job found ");
	}
	else{
	    System.err.println(" found "+jobs.size());
	}
	nwEmpJobs = new NWEmployeeJobs(envBean, emp_id, emp_number, date, jobs);
       
    }
    public List<JobTask> getJobs(){
	if(!emp_id.isEmpty() && jobs == null){
	    JobTaskList jtl = new JobTaskList();
	    jtl.setEmployee_id(emp_id);
	    if(!pay_period_id.isEmpty()){
		jtl.setPay_period_id(pay_period_id);
	    }
	    jtl.setActiveOnly();
	    String back = jtl.find();
	    if(back.isEmpty()){
		List<JobTask> ones = jtl.getJobs();
		if(ones != null && ones.size() > 0){
		    jobs = ones;
		}
	    }
	}
	return jobs;
    }
    
}





































