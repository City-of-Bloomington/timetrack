package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.sql.*;
import java.util.List;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EmpTerminate{

    static final long serialVersionUID = 3700L;	
    static Logger logger = LogManager.getLogger(EmpTerminate.class);
    String id="", expire_date="", job_id="";
    List<DepartmentEmployee> departmentEmployees = null;
    List<GroupManager> groupManagers = null;
    List<JobTask> jobs = null;
    JobTask job = null;
    Employee emp = null;
    //
    public EmpTerminate(){
	
    }
    public EmpTerminate(String val){
	//
	setId(val);
    }		
    public EmpTerminate(String val, String val2){
	//
	setId(val);
	setExpire_date(val2);
    }

    public boolean equals(Object obj){
	if(obj instanceof EmpTerminate){
	    EmpTerminate one =(EmpTerminate)obj;
	    return id.equals(one.getId());
	}
	return false;				
    }
    public int hashCode(){
	int seed = 29;
	if(!id.equals("")){
	    try{
		seed += Integer.parseInt(id);
	    }catch(Exception ex){
	    }
	}
	return seed;
    }
    //
    // getters
    //
    public String getId(){
	return id; // emp_id
    }
    public String getExpire_date(){
	return expire_date;
    }
    public String getJob_id(){ // we are terminating one job only
	return job_id;
    }    
    public Employee getEmp(){
	if(emp == null){
	    if(id.isEmpty() && !job_id.isEmpty()){
		JobTask job = new JobTask(job_id);
		String back = job.doSelect();
		id = job.getEmployee_id();
	    }
	    if(!id.isEmpty()){
		Employee one = new Employee(id);
		String back = one.doSelect();
		if(back.isEmpty()){
		    emp = one;
		}
	    }	    
	}
	return emp;				
    }
    //
    // setters
    //
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setJob_id(String val){
	if(val != null && !val.isEmpty())
	    job_id = val;
    }    
    public void setExpire_date(String val){
	if(val != null && !val.isEmpty())
	    expire_date = val;
    }
    public String toString(){
	return id;
    }
    public boolean hasDepartments(){
	return departmentEmployees != null && departmentEmployees.size() > 0;
    }
    public List<DepartmentEmployee> getDepartmentEmployees(){
	return departmentEmployees;
    }
    public boolean isGroupManager(){
	return groupManagers != null && groupManagers.size() > 0;
    }
    public List<GroupManager> getGroupManagers(){
	return groupManagers;
    }
    public boolean hasJob(){
	return !job_id.isEmpty();
    }
    //
    // when dealing with one job termination
    //
    public JobTask getJob(){
	if(!job_id.isEmpty() && job == null){
	    JobTask jone = new JobTask(job_id);
	    String back = jone.doSelect();
	    if(back.isEmpty()){
		job = jone;
	    }
	}
	return job;
    }
    public boolean hasJobs(){
	return jobs != null && jobs.size() > 0;
    }
    public List<JobTask> getJobs(){
	return jobs;
    }
    //
    // when we want to terminate one job
    //
    public String terminateJob(){
	String back = "";
	JobTask job = new JobTask(job_id);
	job.setExpire_date(expire_date);
	back = job.doTerminate();
	back = job.doSelect();
	id = job.getEmployee_id();
	return back;
	    
    }
    public String doTerminate(){
	String back="";
	if(!job_id.isEmpty()){
	    back = terminateJob();
	}
	else{
	    back = terminateAll();
	}
	return back;
    }
    //
    public String terminateAll(){
	String back = "";
	getEmp();
	if(emp == null){
	    back = "No employee found ";
	    return back;
	}
	if(expire_date.isEmpty()){
	    back = "Expire date not set";
	    return back;
	}
	if(emp.hasDepartments()){
	    departmentEmployees = emp.getDepartmentEmployees();
	}
	if(emp.isGroupManager()){
	    groupManagers = emp.getGroupManagers();
	    for(GroupManager one:groupManagers){
		one.setExpire_date(expire_date);
		back += one.doUpdate();
	    }
	}
	if(emp.hasJobs()){
	    jobs = emp.getJobs();
	    for(JobTask one:jobs){
		one.setExpire_date(expire_date);
		back += one.doUpdate();
	    }
	}
	return back;
    }
}
