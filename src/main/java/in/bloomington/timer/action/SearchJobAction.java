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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SearchJobAction extends TopAction{

    static final long serialVersionUID = 1800L;	
    static Logger logger = LogManager.getLogger(SearchJobAction.class);
    //
    List<JobTask> jobs = null;
    JobTaskList joblst = null;
    String jobTasksTitle = "Current jobs";
    List<Type> salaryGroups = null;
    List<Type> positions = null;
    List<Employee> employees = null;
    List<Type> departments = null;
    public String execute(){
	String ret = SUCCESS;
	String back = canProceed("searchJob.action");
	if(!back.isEmpty()){
	    return back;
	}
	if(!action.isEmpty()){
	    back = joblst.find();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		List<JobTask> ones = joblst.getJobs();
		if(ones != null){
		    if(ones.size() > 1){
			jobs = ones;
			addMessage("Found "+jobs.size()+" Jobs");
		    }
		    else if(ones.size() == 1){
			String job_id = ones.get(0).getId();
			try{
			    HttpServletResponse res = ServletActionContext.getResponse();
			    String str = "jobTask.action?id="+job_id;
			    res.sendRedirect(str);
			    return super.execute();
			}catch(Exception ex){
			    System.err.println(ex);
			}	
		    }
		}
		else{
		    addActionMessage("No match found");
		    addMessage("No match found");
		}
	    }
	}
	getJoblst();
	return ret;
    }
    public JobTaskList getJoblst(){ 
	if(joblst == null){
	    joblst = new JobTaskList();
	}		
	return joblst;
    }

    public void setJoblst(JobTaskList val){
	if(val != null){
	    joblst = val;
	}
    }

    public String getJobTasksTitle(){
	return jobTasksTitle;
    }
    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
	    action = val;
    }
    // todo
    public List<JobTask> getJobs(){
	return jobs;
    }

    public boolean hasJobs(){
	return jobs != null && jobs.size() > 0;
    }
    public List<Type> getSalaryGroups(){
	TypeList tl = new TypeList("salary_groups");
	String back = tl.find();
	if(back.isEmpty()){
	    List<Type> ones = tl.getTypes();
	    if(ones != null && ones.size() > 0){
		salaryGroups = ones;
	    }
	}
	return salaryGroups;
    }
    public List<Type> getPositions(){
	TypeList tl = new TypeList("positions");
	String back = tl.find();
	if(back.isEmpty()){
	    List<Type> ones = tl.getTypes();
	    if(ones != null && ones.size() > 0){
		positions = ones;
	    }
	}
	return positions;
    }
    public List<Employee> getEmployees(){
	EmployeeList tl = new EmployeeList();
	tl.setActiveOnly();
	String back = tl.find();
	if(back.isEmpty()){
	    List<Employee> ones = tl.getEmployees();
	    if(ones != null && ones.size() > 0){
		employees = ones;
	    }
	}
	return employees;
    }
    public List<Type> getDepartments(){
	if(departments == null){
	    TypeList tl = new TypeList();
	    tl.setTable_name("departments");
	    tl.setActiveOnly();
	    String back = tl.find();
	    if(back.isEmpty()){
		List<Type> ones = tl.getTypes();
		if(ones != null && ones.size() > 0){
		    departments = ones;
		}
	    }
	}
	return departments;
    }		
		
}





































