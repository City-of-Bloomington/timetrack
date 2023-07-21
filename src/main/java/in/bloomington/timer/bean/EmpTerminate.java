package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EmpTerminate{

    static final long serialVersionUID = 3700L;	
    static Logger logger = LogManager.getLogger(EmpTerminate.class);
    String id="", employee_id="", job_ids = "", job_id="", jobTitles="";
    String job_grade="", job_step="", supervisor_id="", supervisor_phone="";
    String employment_type="", // full, part time, temp, union
	date_of_hire="", last_pay_priod_date="",
	department_id=""; // department 
    String emp_address="", emp_city="", emp_state="",
	emp_zip="", emp_phone="", emp_alt_phone="",
	last_day_of_work="", date_of_birth="",
	personal_email="";
    //ITS
    String email="", email_account_action="",  // Active, Forward to
	forward_emails ="", // multiple
	forward_days_cnt="",
	drive_action="",//Archive, Transfer to Person, transfer to shared drive
	drive_to_person_email="",
	drive_to_shared_email="", // array
	drive_to_shared_emails=""; // multiple
    String calendar_action="", // Close, Transfer to
	calendar_to_email="",
	zoom_action="", // Close, Transfer to
	zoom_to_email="";
    String badge_returned=""; //NA, Yes, No
    int hours_per_week=0;    
    double pay_period_worked_hrs=0, comp_time=0, vac_time=0, pto=0;
    String commnents="",submited_by_id ="", submited_date="";

    List<DepartmentEmployee> departmentEmployees = null;
    List<GroupManager> groupManagers = null;
    List<JobTask> jobs = null;
    JobTask job = null;
    Employee emp = null, supervisor = null, submited_by=null;
    //
    public EmpTerminate(){
	
    }
    public EmpTerminate(String val){
	//
	setId(val);
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
	return id;
    }
    public String getEmployee_id(){
	return employee_id;
    }    
    public String getJob_id(){ // adding one job a time
	return job_id;
    }
    public String getJob_ids(){ 
	return job_ids;
    }
    public String getJob_grade(){ // adding one job a time
	return job_grade;
    }
    public String getJob_step(){ // adding one job a time
	return job_step;
    }
    public String getEmployment_type(){
	return employment_type;
    }
    public String getSupervisor_id(){
	return supervisor_id;
    }
    public String getSupervisor_phone(){
	return supervisor_phone;
    }
    public Employee getSupervisor(){
	if(supervisor == null && !supervisor_id.isEmpty()){
	    Employee one = new Employee(supervisor_id);
	    String back = one.doSelect();
	    if(back.isEmpty()){
		supervisor = one;
	    }
	}
	return supervisor;
    }
    
    public Employee getEmp(){
	if(emp == null){
	    if(employee_id.isEmpty() && !job_id.isEmpty()){
		JobTask job = new JobTask(job_id);
		String back = job.doSelect();
		employee_id = job.getEmployee_id();
	    }
	    if(!employee_id.isEmpty()){
		Employee one = new Employee(employee_id);
		String back = one.doSelect();
		if(back.isEmpty()){
		    emp = one;
		}
	    }	    
	}
	return emp;				
    }
    public String getDate_of_hire(){
	return date_of_hire;
    }
    public String getLast_pay_period_date(){
	return last_pay_priod_date;
    }
    public String getLast_day_of_work(){
	return last_day_of_work;
    }
    public String getDepartment_id(){
	return department_id;
    }
    public String getEmp_address(){
	return emp_address;
    }
    public String getEmp_city(){
	return emp_city;
    }
    public String getEmp_state(){
	return emp_state;
    }
    public String getEmp_zip(){
	return emp_zip;
    }
    public String getEmp_phone(){
	return emp_phone;
    }
    public String getEmp_alt_phone(){
	return emp_alt_phone;
    }
    public String getPersonal_email(){
	return personal_email;
    }
    public String getDate_of_birth(){
	return date_of_birth;
    }
    //
    // ITS
    public String getEmail(){
	return email;
    }
    public String getEmail_account_action(){
	return email_account_action;
    }
    public String getForward_emails(){
	return forward_emails;
    }
    
    public String getForward_days_cnt(){
	return forward_days_cnt;
    }
        
    public String getDrive_action(){
	return drive_action;
    }
    
    public String getDrive_to_person_email(){
	return drive_to_person_email;
    }
    public String getDrive_to_shared_email(){
	return drive_to_shared_email;
    }    
    
    public String getCalendar_action(){
	return calendar_action;
    }
    public String getCalendar_to_email(){
	return calendar_to_email;
    }
    public String getZoom_action(){
	return zoom_action;
    }
    public String getZoom_to_email(){
	return zoom_to_email;
    }    
    public String getBadge_returned(){
	return badge_returned;
    }
    public int getHours_per_week(){
	return hours_per_week;
    }
    public double getPay_period_worked_hrs(){
	return pay_period_worked_hrs;
    }
    public double getComp_time(){
	return comp_time;
    }
    public double getVac_time(){
	return vac_time;
    }
    public double getPto(){
	return pto;
    }
    public String getComments(){
	return commnents;
    }
    public String submited_by_id(){
	return submited_by_id;
    }
    public String submited_date(){
	return submited_date;
    }
    public Employee getSubmited_by(){
	if(submited_by == null && !submited_by_id.isEmpty()){
	    Employee one = new Employee(submited_by_id);
	    String back = one.doSelect();
	    if(back.isEmpty()){
		submited_by = one;
	    }
	}
	return submited_by;
    }
    //
    // setters
    //
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setJob_id(String val){
	if(val != null && !val.isEmpty()){
	    if(job_id.isEmpty()){
		job_id = val; // we need one to get group and dept info
	    }
	    if(!job_ids.isEmpty())job_ids += ","; // comma separated
	    job_ids += val;
	}
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
	    JobTask one = new JobTask(job_id);
	    String back = one.doSelect();
	    if(back.isEmpty()){
		job = one;
		if(jobTitles.isEmpty())
		    jobTitles = one.getName();
	    }
	}
	return job;
    }
    public List<JobTask> getJobs(){
	if(jobs == null){
	    if(job_ids.indexOf(",") > -1){
		try{
		    String[] id_arr = job_ids.split(",");
		    if(id_arr != null && id_arr.length > 1){
			for(String str:id_arr){
			    JobTask one = new JobTask(str);
			    String back = one.doSelect();
			    if(back.isEmpty()){
				if(jobs == null)
				    jobs = new ArrayList<>();
				if(!jobTitles.isEmpty()) jobTitles += ", ";
				jobTitles = one.getName();
				jobs.add(one);
			    }
			}
		    }
		}catch(Exception ex){
		    logger.error("Error jobs ids "+job_ids+" "+ex);
		}
	    }
	    else{
		jobs = new ArrayList<>();
		getJob();
		jobs.add(job);
	    }
	}
	return jobs;
    }
    public String getJobTitles(){
	if(jobTitles.isEmpty())
	    getJobs();
	return jobTitles;
    }
    public boolean hasJobs(){
	getJobs();
	return jobs != null && jobs.size() > 0;
    }
    //
    // setters
    //
    public void setEmployee_id(String val){
	if(val != null)
	    employee_id = val;
    }    
    public void setJob_ids(String val){
	if(val != null)
	    job_ids=val;
    }
    public void setJob_grade(String val){ // adding one job a time
	if(val != null)
	    job_grade=val;
    }
    public void setJob_step(String val){ // adding one job a time
	if(val != null)
	    job_step=val;
    }
    public void setEmployment_type(String val){
	if(val != null)
	    employment_type=val;
    }
    public void setSupervisor_id(String val){
	if(val != null)
	    supervisor_id=val;
    }
    public void setSupervisor_phone(String val){
	if(val != null)
	    supervisor_phone=val;
    }
    

    public void setDate_of_hire(String val){
	if(val != null)
	    date_of_hire=val;
    }
    public void setLast_pay_period_date(String val){
	if(val != null)
	    last_pay_priod_date=val;
    }
    public void setLast_day_of_work(String val){
	if(val != null)
	    last_day_of_work=val;
    }
    public void setDepartment_id(String val){
	if(val != null)
	    department_id=val;
    }
    public void setEmp_address(String val){
	if(val != null)
	    emp_address=val;
    }
    public void setEmp_city(String val){
	if(val != null)
	    emp_city=val;
    }
    public void setEmp_state(String val){
	if(val != null)
	    emp_state=val;
    }
    public void setEmp_zip(String val){
	if(val != null)
	    emp_zip=val;
    }
    public void setEmp_phone(String val){
	if(val != null)
	    emp_phone=val;
    }
    public void setEmp_alt_phone(String val){
	if(val != null)
	    emp_alt_phone=val;
    }
    public void setPersonal_email(String val){
	if(val != null)
	    personal_email=val;
    }
    public void setDate_of_birth(String val){
	if(val != null)
	    date_of_birth=val;
    }
    //
    // ITS
    public void setEmail(String val){
	if(val != null)
	    email=val;
    }
    public void setEmail_account_action(String val){
	if(val != null)
	    email_account_action=val;
    }
    public void setForward_emails(String val){
	if(val != null)
	    forward_emails=val;
    }
    public void setForward_email(String[] vals){
	if(vals != null){
	    for(String str:vals){
		if(!forward_emails.isEmpty())
		    forward_emails +=",";
		    forward_emails=str;
	    }
	}
    }
    public String[] getForward_email(){
	String[] str_arr = null;
	if(!forward_emails.isEmpty()){
	    if(forward_emails.indexOf(",") > -1){
		try{
		    str_arr = forward_emails.split(",");
		}catch(Exception ex){}
	    }
	    else{
		str_arr = new String[1];
		str_arr[0] = forward_emails;
	    }
	}
	return str_arr;
    }
    
    public void setForward_days_cnt(String val){
	if(val != null)
	    forward_days_cnt=val;
    }
        
    public void setDrive_action(String val){
	if(val != null)
	    drive_action=val;
    }
    public void setDrive_to_person_email(String val){
	if(val != null)
	    drive_to_person_email=val;
    }
    public void setDrive_to_shared_emails(String val){
	if(val != null)
	    drive_to_shared_emails=val;
    }    
    public void setCalendar_action(String val){
	if(val != null)
	    calendar_action=val;
    }
    public void setCalendar_to_email(String val){
	if(val != null)
	    calendar_to_email=val;
    }
    public void setZoom_action(String val){
	if(val != null)
	    zoom_action = val;
    }
    public void setZoom_to_email(String val){
	if(val != null)
	    zoom_to_email = val;
    }    
    public void setBadge_returned(String val){
	if(val != null)
	    badge_returned = val;
    }
    public void setHours_per_week(Integer val){
	if(val != null)
	    hours_per_week = val;
    }
    public void setPay_period_worked_hrs(Double val){
	if(val != null)
	    pay_period_worked_hrs = val;
    }
    public void setComp_time(Double val){
	if(val != null)
	    comp_time = val;
    }
    public void setVac_time(Double val){
	if(val != null)
	    vac_time = val;
    }
    public void setPto(Double val){
	if(val != null)
	    pto = val;
    }
    public void setComments(String val){
	if(val != null)
	    commnents = val;
    }
    public void submited_by_id(String val){
	if(val != null)
	    submited_by_id = val;
    }
    public void submited_date(String val){
	if(val != null)
	    submited_date = val;
    }

    //
    // when we want to terminate one job
    //
    /**
     // ToDo later
     public String terminateJob(){
     String back = "";
     JobTask job = new JobTask(job_id);
     job.setExpire_date(expire_date);
     back = job.doTerminate();
     back = job.doSelect();
     employee_id = job.getEmployee_id();
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
    */
}
