package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EmpTerminate{

    static final long serialVersionUID = 3700L;	
    static Logger logger = LogManager.getLogger(EmpTerminate.class);
    static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");    
    String id="", employee_id="", full_name="", job_id="", job_ids = "",
	jobTitles="";
    String job_grade="", job_step="", supervisor_id="", supervisor_phone="";
    String employment_type="", // full, part time, temp, union
	date_of_hire="", last_pay_period_date="",
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
    String comments="",submitted_by_id ="", submitted_date="";

    List<DepartmentEmployee> departmentEmployees = null;
    List<GroupManager> groupManagers = null;
    List<JobTask> jobs = null;
    List<Document> documents = null;
    JobTask job = null;
    Group group = null;
    Department department = null;
    Employee emp = null, supervisor = null, submitted_by=null;
    String[] forward_emails_arr = null;
    String[] drive_to_shared_email_arr = null;
    //
    public EmpTerminate(){
	
    }
    public EmpTerminate(String val){
	//
	setId(val);
    }
    void
	setVals(
		String str, String str2, String str3,
		String str4, String str5, String str6,
		
		String str7, String str8, String str9,
		String str10, String str11, String str12,
		
		String str13, String str14, String str15,
		String str16, String str17, String str18,
		
		String str19, String str20, String str21,
		String str22, String str23, String str24,
		
		String str25, String str26, String str27,
		String str28, String str29, String str30,
		
		String str31, String str32, String str33,
		int str34, double str35, double str36,

		double str37, double str38, String str39,
		String str40, String str41){
	setId(str);
	setEmployee_id(str2);
	setFull_name(str3);
	setJob_ids(str4);
	setJob_grade(str5);

	setJob_step(str6);
	setSupervisor_id(str7);
	setSupervisor_phone(str8);
	setEmployment_type(str9);
	setDate_of_hire(str10);

	setLast_pay_period_date(str11);
	setDepartment_id(str12);
	setEmp_address(str13);
	setEmp_city(str14);
	setEmp_state(str15);
	
	setEmp_zip(str16);
	setEmp_phone(str17);
	setEmp_alt_phone(str18);
	setLast_day_of_work(str19);
	setDate_of_birth(str20);

	setPersonal_email(str21);
	setEmail(str22);
	setEmail_account_action(str23);
	setForward_emails(str24);
	setForward_days_cnt(str25);

	setDrive_action(str26);
	setDrive_to_person_email(str27);
	setDrive_to_shared_emails(str28);
	setCalendar_action(str29);
	setCalendar_to_email(str30);

	setZoom_action(str31);
	setZoom_to_email(str32);
	setBadge_returned(str33);
	setHours_per_week(str34);
	setPay_period_worked_hrs(str35);

	setComp_time(str36);
	setVac_time(str37);
	setPto(str38);
	setComments(str39);
	setSubmitted_by_id(str40);
	setSubmitted_date(str41);
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
    public String getFull_name(){
	return full_name;
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
	return last_pay_period_date;
    }
    public String getLast_day_of_work(){
	return last_day_of_work;
    }
    public String getDepartment_id(){
	return department_id;
    }
    public Department getDepartment(){
	if(department == null && !department_id.isEmpty()){
	    department = new Department(department_id);
	    department.doSelect();
	}
	return department;
    }
    public Group getGroup(){
	if(group == null){
	    getJobs();
	    if(job != null){
		group = job.getGroup();
	    }
	}
	return group;
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
    void setDriveToSharedArr(){
	if(!drive_to_shared_emails.isEmpty()){
	    if(drive_to_shared_emails.indexOf(",") > -1){
		try{
		    drive_to_shared_email_arr = drive_to_shared_emails.split(",");
		}catch(Exception ex){}
	    }
	    else {
		drive_to_shared_email_arr = new String[1];
		drive_to_shared_email_arr[0] = drive_to_shared_emails;
	    }
	}
    }
    public String getDrive_to_shared_email(){
	setDriveToSharedArr();
	if(drive_to_shared_email_arr != null && drive_to_shared_email_arr.length > 0)
	    return drive_to_shared_email_arr[0];
	return null;
    }
    public String getDrive_to_shared_email2(){
	setDriveToSharedArr();
	if(drive_to_shared_email_arr != null && drive_to_shared_email_arr.length > 1)
	    return drive_to_shared_email_arr[1];
	return null;
    }
    public String getDrive_to_shared_email3(){
	setDriveToSharedArr();
	if(drive_to_shared_email_arr != null && drive_to_shared_email_arr.length > 2)
	    return drive_to_shared_email_arr[2];
	return null;
    }
    public String getDrive_to_shared_email4(){
	setDriveToSharedArr();
	if(drive_to_shared_email_arr != null && drive_to_shared_email_arr.length > 3)
	    return drive_to_shared_email_arr[3];
	return null;
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
	return comments;
    }
    public String getSubmitted_by_id(){
	return submitted_by_id;
    }
    public String submitted_date(){
	return submitted_date;
    }
    public Employee getSubmitted_by(){
	if(submitted_by == null && !submitted_by_id.isEmpty()){
	    Employee one = new Employee(submitted_by_id);
	    String back = one.doSelect();
	    if(back.isEmpty()){
		submitted_by = one;
	    }
	}
	return submitted_by;
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
		employee_id = job.getEmployee_id();
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
				jobTitles += one.getName();
				employee_id = one.getEmployee_id();
				jobs.add(one);
				if(job == null){
				    job = one;
				}
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
    public void setFull_name(String val){
	if(val != null)
	    full_name = val;
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
    public void setSupervisor(Employee val){
	if(val != null){
	    supervisor =val;
	    supervisor_id = val.getId();
	}
    }

    public void setSubmitted_by(Employee val){
	if(val != null)
	    submitted_by =val;
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
	    last_pay_period_date=val;
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
    void findForwardEmailArr(){
	if(forward_emails_arr == null && !forward_emails.isEmpty()){
	    if(forward_emails.indexOf(",") > 0){
		try{
		    forward_emails_arr = forward_emails.split(",");
		}catch(Exception ex){}
	    }
	    else{
		forward_emails_arr = new String[1];
		forward_emails_arr[0] = forward_emails;
	    }
	}
    }
    public void setForward_emails(String val){
	if(val != null)
	    forward_emails=val;
    }
    public void setForward_email(String val){
	if(val != null){
	    if(!forward_emails.isEmpty())
		forward_emails +=",";
	    forward_emails += val;
	}
    }
    public void setForward_email2(String val){
	if(val != null){
	    if(!forward_emails.isEmpty())
		forward_emails +=",";
	    forward_emails += val;
	}
    }
    public void setForward_email3(String val){
	if(val != null){
	    if(!forward_emails.isEmpty())
		forward_emails +=",";
	    forward_emails += val;
	}
    }
    public void setForward_email4(String val){
	if(val != null){
	    if(!forward_emails.isEmpty())
		forward_emails +=",";
	    forward_emails += val;
	}
    }
    
    public String getForward_email(){
	findForwardEmailArr();
	if(forward_emails_arr != null)
	    return forward_emails_arr[0];
	return null;
    }
    public String getForward_email2(){
	findForwardEmailArr();
	if(forward_emails_arr != null && forward_emails_arr.length > 1)
	    return forward_emails_arr[1];
	return null;
    }
    public String getForward_email3(){
	findForwardEmailArr();
	if(forward_emails_arr != null && forward_emails_arr.length > 2)
	    return forward_emails_arr[2];
	return null;
    }
    public String getForward_email4(){
	findForwardEmailArr();
	if(forward_emails_arr != null && forward_emails_arr.length > 3)
	    return forward_emails_arr[3];
	return null;
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
	if(val != null){
	    drive_to_person_email = val;
	}
    }
    public void setDrive_to_shared_emails(String val){
	if(val != null){
	    if(!drive_to_shared_emails.isEmpty())
		drive_to_shared_emails +=",";
	    drive_to_shared_emails += val;
	}
    }
    public void setDrive_to_shared_email(String val){
	setDrive_to_shared_emails(val);
    }
    public void setDrive_to_shared_email2(String val){
	setDrive_to_shared_emails(val);
    }
    public void setDrive_to_shared_email3(String val){
	setDrive_to_shared_emails(val);
    }
    public void setDrive_to_shared_email4(String val){
	setDrive_to_shared_emails(val);
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
	    comments = val;
    }
    public void setSubmitted_by_id(String val){
	if(val != null)
	    submitted_by_id = val;
    }
    public void setSubmitted_date(String val){
	if(val != null)
	    submitted_date = val;
    }
    public String populateOneJob(){
	String back = "";
	if(job == null && !job_id.isEmpty()){
	    JobTask one  = new JobTask(job_id);
	    back = one.doSelect();
	    if(back.isEmpty()){
		job = one;
	    }
	}
	if(job != null && back.isEmpty()){
	    jobTitles = job.getPosition().getName();
	    employment_type = job.getSalaryGroup().getName();
	    group = job.getGroup();
	    date_of_hire = job.getEffective_date();
	    department_id = group.getDepartment_id();
	    department = group.getDepartment();
	    emp = job.getEmployee();
	    full_name = emp.getFull_name();
	    email = emp.getEmail();
	    hours_per_week = job.getWeekly_regular_hours();
	}
	return back;
    }
    public String populateMulitJobs(){
	String back = "";
	getJobs();
	if(jobs != null){
	    job = jobs.get(0);
	    job_id = job.getId();
	    back = populateOneJob();
	    if(back.isEmpty()){
		for(int i=1;i<jobs.size();i++){
		    if(!jobTitles.isEmpty()) jobTitles +=", ";
		    jobTitles += jobs.get(i).getPosition().getName();
		}
	    }
	}
	return back;
    }
    public String doTerminate(){
	String back="";
	if(last_pay_period_date.isEmpty()){
	    back = " last pay period date not set ";
	    return back;
	}
	getJobs();
	if(jobs != null && jobs.size() > 0){
	    for(JobTask one:jobs){
		if(job == null)
		    job = one; // we need one for next action
		one.setExpire_date(last_pay_period_date);
		back += one.doTerminate();
	    }
	    if(jobs.size() == 1){
		emp = job.getEmployee();
		if(emp != null && emp.isGroupManager()){
		    List<GroupManager> ones = emp.getGroupManagers();
		    for(GroupManager one:groupManagers){
			one.setExpire_date(last_pay_period_date);
			back += one.doUpdate();
		    }
		}
		if(emp.hasDepartments()){
		    /**
		       // we are keeping the dept info for now since
		       // some employees come back such as seasonal
		    List<DepartmentEmployee> ones =
			emp.getDepartmentEmployees();
		    for(DepartmentEmployee one:ones){
			one.setExpire_date(last_pay_priod_date);
			back += one.doUpdate();
		    }
		    */
		}
		if(hasDocuments()){
		    CleanUp cleanUp = new CleanUp();
		    cleanUp.setDocuments(documents);
		    String msg = cleanUp.doClean();
		    if(msg.isEmpty()){
			back += " clean up problem "+msg;
		    }
		}
	    }
	}
	return back;
    }
    void findDocuments(){
	//
	String pay_period_id = "";
	if(!last_pay_period_date.isEmpty()){
	    PayPeriod pp = new PayPeriod();
	    if(pp.findByEndDate(last_pay_period_date)){
		pay_period_id = pp.getId();
	    }
	    else{
		System.err.println(" could not find pay_period_id for "+last_pay_period_date);
	    }
	}
	if(documents == null
	   && jobs != null
	   && !pay_period_id.isEmpty()){
	    for(JobTask one:jobs){
		DocumentList dl = new DocumentList();
		dl.setPay_period_id(pay_period_id);
		dl.setJob_id(one.getId());
		String back = dl.findForCleanUp();
		if(back.isEmpty()){
		    List<Document> ones = dl.getDocuments();
		    if(ones != null && ones.size() > 0){
			if(documents == null)
			    documents = ones;
			else{
			    for(Document doc:ones){
				documents.add(doc);
			    }
			}
		    }
		}
	    }
	}
    }
    boolean hasDocuments(){
	findDocuments();
	return documents != null && documents.size() > 0;
    }    
    public String doSave(){
	
	String back="";
	Connection con = null;
	PreparedStatement pstmt = null, pstmt2=null;
	ResultSet rs = null;	
	String qq = "insert into emp_terminations values(0,?,?,?,?, ?,?,?,?,?,"+
	    "?,?,?,?,?, ?,?,?,?,?,"+
	    "?,?,?,?,?, ?,?,?,?,?,"+
	    "?,?,?,?,?, ?,?,?,?,?,"+
	    "?)";
	con = UnoConnect.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    return back;
	}
	if(job_ids.isEmpty()){
	    back = "job(s) not set ";
	    return back;
	}
	if(supervisor_id.isEmpty()){
	    back = "supervisor not set ";
	    return back;
	}
	if(last_pay_period_date.isEmpty()){
	    back = "last_pay_period_date not set";
	    return back;
	}
	logger.debug(qq);				
	try{
	    pstmt = con.prepareStatement(qq);
	    back = setParams(pstmt);
	    if(back.isEmpty()){
		pstmt.executeUpdate();
		qq = "select LAST_INSERT_ID()";
		pstmt2 = con.prepareStatement(qq);
		rs = pstmt2.executeQuery();
		if(rs.next()){
		    id = rs.getString(1);
		}
	    }
	}
	catch(Exception ex){
	    back += ex+":"+qq;
	    logger.error(back);
	}
	finally{
	    Helper.databaseDisconnect(rs, pstmt, pstmt2);
	    UnoConnect.databaseDisconnect(con);
	}	
	return back;
    }
    String setParams(PreparedStatement pstmt){
	String back = "";
	try{
	    pstmt.setString(1, employee_id);
	    pstmt.setString(2, full_name);
	    pstmt.setString(3, job_ids);
	    if(job_grade.isEmpty())
		pstmt.setNull(4, Types.VARCHAR);
	    else
		pstmt.setString(4, job_grade);
	    if(job_step.isEmpty())
		pstmt.setNull(5, Types.VARCHAR);
	    else	    
		pstmt.setString(5, job_step);
	    pstmt.setString(6, supervisor_id);
	    if(supervisor_phone.isEmpty())
		pstmt.setNull(7, Types.VARCHAR);
	    else
		pstmt.setString(7, supervisor_phone);
	    pstmt.setString(8, employment_type);
	    if(date_of_hire.isEmpty())
		pstmt.setNull(9, Types.DATE);
	    else{
		pstmt.setDate(9, new java.sql.Date(dateFormat.parse(date_of_hire).getTime()));	
	    }
	    pstmt.setDate(10, new java.sql.Date(dateFormat.parse(last_pay_period_date).getTime()));		    
	    if(department_id.isEmpty())
		pstmt.setNull(11, Types.VARCHAR);
	    else	    
		pstmt.setString(11, department_id);
	    if(emp_address.isEmpty())
		pstmt.setNull(12, Types.VARCHAR);
	    else	    	    
		pstmt.setString(12, emp_address);
	    if(emp_city.isEmpty())
		pstmt.setNull(13, Types.VARCHAR);
	    else	    
		pstmt.setString(13, emp_city);
	    if(emp_state.isEmpty())
		pstmt.setNull(14, Types.VARCHAR);
	    else
		pstmt.setString(14, emp_state);
	    if(emp_zip.isEmpty())
		pstmt.setNull(15, Types.VARCHAR);
	    else	    
		pstmt.setString(15, emp_zip);
	    if(emp_phone.isEmpty())
		pstmt.setNull(16, Types.VARCHAR);
	    else
		pstmt.setString(16, emp_phone);
	    if(emp_alt_phone.isEmpty())
		pstmt.setNull(17, Types.VARCHAR);
	    else
		pstmt.setString(17, emp_alt_phone);
	    if(last_day_of_work.isEmpty())
		pstmt.setNull(18, Types.DATE);
	    else {
		pstmt.setDate(18, new java.sql.Date(dateFormat.parse(last_day_of_work).getTime()));		
	    }
	    if(date_of_birth.isEmpty())
		pstmt.setNull(19, Types.DATE);
	    else{
		pstmt.setDate(19, new java.sql.Date(dateFormat.parse(date_of_birth).getTime()));
	    }
	    if(personal_email.isEmpty())
		pstmt.setNull(20, Types.VARCHAR);
	    else	    
		pstmt.setString(20, personal_email);
	    if(email.isEmpty())
		pstmt.setNull(21, Types.VARCHAR);
	    else
		pstmt.setString(21, email);
	    if(email_account_action.isEmpty())
		pstmt.setNull(22, Types.VARCHAR);
	    else
		pstmt.setString(22, email_account_action);
	    if(forward_emails.isEmpty())
		pstmt.setNull(23, Types.VARCHAR);
	    else
		pstmt.setString(23, forward_emails);
	    if(forward_days_cnt.isEmpty())
		pstmt.setNull(24, Types.INTEGER);
	    else
		pstmt.setString(24, forward_days_cnt);
	    if(drive_action.isEmpty())
		pstmt.setNull(25, Types.VARCHAR);
	    else	    
		pstmt.setString(25, drive_action);
	    if(drive_to_person_email.isEmpty())
		pstmt.setNull(26, Types.VARCHAR);
	    else
		pstmt.setString(26, drive_to_person_email);
	    if(drive_to_shared_emails.isEmpty())
		pstmt.setNull(27, Types.VARCHAR);
	    else	    
		pstmt.setString(27, drive_to_shared_emails);
	    if(calendar_action.isEmpty())
		pstmt.setNull(28, Types.VARCHAR);
	    else	    
		pstmt.setString(28, calendar_action);
	    if(calendar_to_email.isEmpty())
		pstmt.setNull(29, Types.VARCHAR);
	    else
		pstmt.setString(29, calendar_to_email);
	    if(zoom_action.isEmpty())
		pstmt.setNull(30, Types.VARCHAR);
	    else
		pstmt.setString(30, zoom_action);
	    if(zoom_to_email.isEmpty())
		pstmt.setNull(31, Types.VARCHAR);
	    else	    
		pstmt.setString(31, zoom_to_email);
	    if(badge_returned.isEmpty())
		pstmt.setNull(32, Types.VARCHAR);
	    else
		pstmt.setString(32, badge_returned);
	    pstmt.setInt(33, hours_per_week);
	    pstmt.setDouble(34, pay_period_worked_hrs);
	    pstmt.setDouble(35, comp_time);
	    
	    pstmt.setDouble(36, vac_time);
	    pstmt.setDouble(37, pto);
	    if(comments.isEmpty())
		pstmt.setNull(38, Types.VARCHAR);
	    else	    
		pstmt.setString(38, comments);
	    if(submitted_by_id.isEmpty())
		pstmt.setNull(39, Types.VARCHAR);
	    else
		pstmt.setString(39, submitted_by_id);
	    if(submitted_date.isEmpty())
		submitted_date = Helper.getToday();
	    pstmt.setDate(40, new java.sql.Date(dateFormat.parse(submitted_date).getTime()));
	}catch(Exception ex){
	    back += ex;
	}
	return back;
    }
    public String doUpdate(){
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "update emp_terminations set "+
	    "employee_id=?,full_name=?,job_ids=?,job_grade=?,"+
	    "job_step=?, supervisor_id=?, supervisor_phone=?, employment_type=?,"+
	    "date_of_hire=?,"+ // date
	    "last_pay_period_date=?,"+ // date
	    
	    "department_id=?,emp_address=?,emp_city=?,emp_state=?,"+
	    "emp_zip=?,emp_phone=?,emp_alt_phone=?,"+
	    "last_day_of_work=?,"+ // date
	    "date_of_birth=?,"+// date
	    
	    "personal_email=?,email=?,email_account_action=?,forward_emails=?,forward_days_cnt=?,"+
	    "drive_action=?,drive_to_person_email=?,drive_to_shared_emails=?,calendar_action=?,calendar_to_email=?,"+

	    "zoom_action=?,zoom_to_email=?,badge_returned=?,hours_per_week=?,pay_period_worked_hrs=?,"+
	    "comp_time=?,vac_time=?,pto=?,comments=?,submitted_by_id=?,"+
	    "submitted_date=? "+ // date
	    " where id = ? "; 
	String back = "";
	logger.debug(qq);
	con = UnoConnect.getConnection();				
	if(con == null){
	    back = "Could not connect to DB ";
	    return back;
	}
	try{
	    pstmt = con.prepareStatement(qq);
	    back = setParams(pstmt);
	    if(back.isEmpty()){
		pstmt.setString(41, id);
		pstmt.executeUpdate();
	    }
	}
	catch(Exception ex){
	    back += ex;
	    logger.error(ex+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(pstmt, rs);
	    UnoConnect.databaseDisconnect(con);
	}
	return back;
    }	    

    public String doSelect(){
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "select  "+
	    "id,employee_id,full_name,job_ids,job_grade,"+
	    "job_step, supervisor_id, supervisor_phone, employment_type,"+
	    "date_format(date_of_hire,'%m/%d/%Y'),"+ // date
	    
	    "date_format(last_pay_period_date,'%m/%d/%Y'),"+ // date
	    "department_id,emp_address,emp_city,emp_state,"+
	    
	    "emp_zip,emp_phone,emp_alt_phone,"+
	    "date_format(last_day_of_work,'%m/%d/%Y'),"+ // date
	    "date_format(date_of_birth,'%m/%d/%Y'),"+ // date
	    
	    "personal_email,email,email_account_action,forward_emails,forward_days_cnt,"+
	    "drive_action,drive_to_person_email,drive_to_shared_emails,calendar_action,calendar_to_email,"+

	    "zoom_action,zoom_to_email,badge_returned,hours_per_week,pay_period_worked_hrs,"+
	    "comp_time,vac_time,pto,comments,submitted_by_id,"+
	    "date_format(submitted_date,'%m/%d/%Y') "+ // date
	    " from emp_terminations where id =? "; 
	String back = "";
	logger.debug(qq);
	con = UnoConnect.getConnection();				
	if(con == null){
	    back = "Could not connect to DB ";
	    return back;
	}
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,id);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		setVals(rs.getString(1),
			rs.getString(2),
			rs.getString(3),
			rs.getString(4),
			rs.getString(5),
			
			rs.getString(6),
			rs.getString(7),
			rs.getString(8),
			rs.getString(9),
			rs.getString(10),
			rs.getString(11),
			rs.getString(12),
			rs.getString(13),
			rs.getString(14),
			rs.getString(15),
			
			rs.getString(16),
			rs.getString(17),
			rs.getString(18),
			rs.getString(19),
			rs.getString(20),
			
			rs.getString(21),
			rs.getString(22),
			rs.getString(23),
			rs.getString(24),
			rs.getString(25),
			
			rs.getString(26),
			rs.getString(27),
			rs.getString(28),
			rs.getString(29),
			rs.getString(30),
			
			rs.getString(31),
			rs.getString(32),
			rs.getString(33),
			rs.getInt(34),
			rs.getDouble(35),
			
			rs.getDouble(36),
			rs.getDouble(37),
			rs.getDouble(38),
			rs.getString(39),
			rs.getString(40),
			rs.getString(41));
	    }
	}
	catch(Exception ex){
	    back += ex;
	    logger.error(ex+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(pstmt, rs);
	    UnoConnect.databaseDisconnect(con);
	}
	return back;
    }
    /**
    create table emp_terminations (
    id int unsigned not null auto_increment,
    employee_id int unsigned,
    full_name varchar(80),
    job_ids varchar(80),
    job_grade varchar(32),
    
    job_step varchar(32),
    supervisor_id int unsigned,
    supervisor_phone varchar(32),
    employment_type varchar(32),
    date_of_hire date, 
    
    last_pay_period_date date,
    department_id int unsigned,
    emp_address varchar(80),
    emp_city varchar(80),
    emp_state varchar(80),
    
    emp_zip varchar(16),
    emp_phone varchar(24),
    emp_alt_phone varchar(20),
    last_day_of_work date,
    date_of_birth date,
    
    personal_email varchar(80),
    email varchar(80),
    email_account_action varchar(32),
    forward_emails varchar(160),
    forward_days_cnt int,
    
    drive_action varchar(32),
    drive_to_person_email varchar(80),
    drive_to_shared_emails varchar(160),
    calendar_action varchar(32),
    calendar_to_email varchar(80),
    
    zoom_action varchar(32),
    zoom_to_email varchar(80),
    badge_returned varchar(32),
    hours_per_week int,
    pay_period_worked_hrs double,
    
    comp_time double,
    vac_time double,
    pto  double,
    comments text,
    submitted_by_id int unsigned,
    
    submitted_date date,
    primary key(id),
    foreign key(employee_id) references employees(id),
    foreign key(supervisor_id) references employees(id),    
    foreign key(submitted_by_id) references employees(id)
    )engine=InnoDB;
    
     */

}
