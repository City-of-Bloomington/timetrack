package in.bloomington.timer;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.*;
import java.util.Set;
import java.sql.*;
import java.text.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HandleJobTitleUpdate{

    boolean debug = false;
    static final long serialVersionUID = 53L;
    static Logger logger = LogManager.getLogger(HandleJobTitleUpdate.class);
    EnvBean envBean = null;
    String date="", pay_period_id="";
    PayPeriod payPeriod = null;
    // Parks
    String department_id="5", dept_ref = "39"; // department reference to New World app
    // employee_number, jobs list
    Hashtable<Employee, Set<JobTask>> empJobs = new Hashtable<>();
    // NW employee jobs
    Hashtable<String, Set<String>> empNwJobs = new Hashtable<>();
    Hashtable<String, Set<String>> empJobNotInTT = null;
    Hashtable<Employee, Set<JobTask>> empJobNotInNW = null;		
    Hashtable<Employee, Set<JobTask>> empNotInNW = null;
    Hashtable<Employee, Set<JobTask>> empJobCanDelete = null;
    Hashtable<Employee, Set<JobTask>> empJobNeedUpdate = null;
    //
    // added to fix park jobs
    Map<String, List<List<String>>> nwEmpJobs = new TreeMap<>();
    Hashtable<String, Hashtable<String, JobTask>> curEmpJobs = new Hashtable<>();    
    public HandleJobTitleUpdate(EnvBean val){
	if(val != null)
	    envBean = val;
    }
    public HandleJobTitleUpdate(){

    }		
    //
    // setters
    //
    public void setDate(String val){
	if(val != null){		
	    date = val;
	}
    }
    public void setDeptRef(String val){
	if(val != null){		
	    dept_ref = val;
	}
    }
    /**
     * jobs not in Timetrack but not in New World
     */

    public Hashtable<Employee, Set<JobTask>> getEmpJobCanDelete(){
	return empJobCanDelete;
    }
    public Hashtable<Employee, Set<JobTask>> getEmpJobNeedUpdate(){
	return empJobNeedUpdate;
    }
    public Hashtable<Employee, Set<JobTask>> getEmpNotInNW(){
	return empNotInNW;
    }		
    public boolean hasEmpJobCanDelete(){
	return empJobCanDelete != null && !empJobCanDelete.isEmpty();
    }
    public boolean hasEmpJobNeedUpdate(){
	return empJobCanDelete != null && !empJobNeedUpdate.isEmpty();
    }		
    public boolean hasEmployeeNotInNW(){
	return empNotInNW != null && !empNotInNW.isEmpty();
    }
    public String specialProcess(){
	String back = findNWJobs();
	back = findEmployeeJobForFix();
	doNextStep();
	return back;
    }
    public String process(){
	findEmployeeJobs();
	findNWJobs();
	String msg = "", status="Success", errors="";
	System.err.println(" TimeTrack Jobs");
	empJobNotInNW = new Hashtable<>();
	empJobNotInTT = new Hashtable<>();
	empJobCanDelete = new Hashtable<>();
	empJobNeedUpdate = new Hashtable<>();
	empNotInNW = new Hashtable<>();
	Set<Employee> set = empJobs.keySet();
	int jj=1;
	/*
	  Set<String> keySet = empNwJobs.keySet();
	  for(String str:keySet){
	  Set<String> sst2 = empNwJobs.get(str);
	  System.err.println(str+" "+sst2);
	  }
	*/
	for(Employee emp:set){
	    Set<JobTask> jset = empJobs.get(emp);
	    String emp_num = emp.getEmployee_number();
	    if(emp_num == null || emp_num.isEmpty()) continue;
	    if(empNwJobs.containsKey(emp_num)){
		Set<String> sst2 = empNwJobs.get(emp_num);
		for(JobTask job:jset){
		    String job_name = job.getName();
		    if(job_name.indexOf("-") > -1){
			job_name=job_name.replace('-',' ');
		    }									 
		    if(!sst2.contains(job_name)){
			if(empJobNotInNW.containsKey(emp)){
			    Set<JobTask> tempSet = empJobNotInNW.get(emp);
			    tempSet.add(job);
			}
			else{
			    Set<JobTask> tempSet = new HashSet<>();
			    tempSet.add(job);
			    empJobNotInNW.put(emp, tempSet);
			}
		    }
		}
	    }
	    else{
		empNotInNW.put(emp, jset);
	    }
	}
	System.err.println(" Jobs in TT but not in NW ");			 
	if(!empJobNotInNW.isEmpty()){
	    Set<Employee> empSet = empJobNotInNW.keySet();
	    jj=1;
	    for(Employee emp:empSet){
		Set<JobTask> set2 = empJobNotInNW.get(emp);
		Iterator<JobTask> itr = set2.iterator(); 
		while (itr.hasNext()){
		    JobTask job = itr.next();
		    if(!job.checkIfJobHasTimeBlccks()){
			jj++;
			if(empJobCanDelete.containsKey(emp)){
			    Set<JobTask> tempSet = empJobCanDelete.get(emp);
			    tempSet.add(job);
			}
			else{
			    Set<JobTask> tempSet = new HashSet<>();
			    tempSet.add(job);
			    empJobCanDelete.put(emp, tempSet);
			}
		    }
		    else{
			System.err.println(jj+" "+emp+" job "+job+" is used ");
			jj++;
			if(empJobNeedUpdate.containsKey(emp)){
			    Set<JobTask> tempSet = empJobNeedUpdate.get(emp);
			    tempSet.add(job);
			}
			else{
			    Set<JobTask> tempSet = new HashSet<>();
			    tempSet.add(job);
			    empJobNeedUpdate.put(emp, tempSet);
			}											 
		    }
		}
	    }
	}
	return msg;
    }
    // old setting
    public String findEmployeeJobs(){
	getPayPeriod();
	DepartmentEmployeeList dempl = new DepartmentEmployeeList();
	dempl.setDepartment_id("5"); // parks
	dempl.setNoExpireDate();
	dempl.setEmployeeActiveOnly();
	String back = dempl.find();
	if(back.isEmpty()){
	    List<DepartmentEmployee> ones = dempl.getDepartmentEmployees();
	    if(ones != null && ones.size() > 0){
		for(DepartmentEmployee demp:ones){
		    Employee emp = demp.getEmployee();
		    emp.setPay_period_id(pay_period_id);
		    List<JobTask> jobs = emp.getJobs();
		    if(jobs != null && jobs.size() > 0){
			Set<JobTask> jset = new HashSet<>();
			for(JobTask job:jobs){
			    jset.add(job);
			}
			empJobs.put(emp, jset);
		    }
		}
	    }
	}
	return back;
    }
    // needed method to fix jobs
    public String findEmployeeJobForFix(){
	getPayPeriod();
	DepartmentEmployeeList dempl = new DepartmentEmployeeList();
	dempl.setDepartment_id("5"); // parks
	dempl.setNoExpireDate();
	dempl.setEmployeeActiveOnly();
	String back = dempl.find();
	if(back.isEmpty()){
	    int jj=1;
	    List<DepartmentEmployee> ones = dempl.getDepartmentEmployees();
	    if(ones != null && ones.size() > 0){
		for(DepartmentEmployee demp:ones){
		    Employee emp = demp.getEmployee();
		    emp.setPay_period_id(pay_period_id);
		    boolean needIn = true;
		    List<JobTask> jobs = emp.getJobs();
		    if(jobs != null && jobs.size() > 0){
			for(JobTask job:jobs){
			    SalaryGroup sg = job.getSalaryGroup();
			    if(sg != null && !(sg.isTemporary() ||
				 sg.isPartTime() ||
				 sg.isSeasonal())){
				needIn = false;
			    }
			}
		    }
		    else{
			needIn = false;
		    }
		    if(needIn){
			String emp_num = emp.getEmployee_number();
			Hashtable<String, JobTask> oneJobs = null;
			if(curEmpJobs.containsKey(emp_num)){
			    oneJobs = curEmpJobs.get(emp_num);
			}
			else{
			    oneJobs = new Hashtable<>();
			}
			for(JobTask job:jobs){
			    String jobTitle = job.getName();
			    oneJobs.put(jobTitle, job);
			    System.err.println(jj+" "+jobTitle);
			    jj++;
			}
			curEmpJobs.put(emp_num, oneJobs);
		    }
		}
	    }
	}
	//System.err.println(" emp jobs "+curEmpJobs);
	return back;
    }    
    
    /**
       select           
       e.EmployeeNumber               as employeeNum,
       e.EmployeeName                 as name,
       e.LastName                     as lastname,
       e.FirstName                    as firstname,
       x.Title                        as xtitle,
       job.JobTitle                   as title,
       job.DepartmentId               as job_department_id,
       e.OrgStructureDescconcatenated as department,   
       e.DepartmentId                 as employee_department_id,
       e.xGroupCodeDesc               as benefitGroup,
       udf.ValString                  as username,
       job.CycleHours/2               as weeklyHours,
       e.*                                   
       from HR.vwEmployeeInformation     e                                   
       join HR.vwEmployeeJobWithPosition job on e.EmployeeId=job.EmployeeId
       and GETDATE() between job.EffectiveDate     and job.EffectiveEndDate
       and GETDATE() between job.PositionDetailESD and job.PositionDetailEED
       left join COB.jobTitleCrosswalk   x    on job.JobTitle=x.Code
       join HR.EmployeeName          n    on e.EmployeeId=n.EmployeeId
       and GETDATE() between   n.EffectiveDate     and   n.EffectiveEndDate
       left join dbo.UDFEntry    udf  on n.EmployeeNameId=udf.AttachedFKey and udf.UDFAttributeID=52 and udf.TableID=66                                   
       where e.vsEmploymentStatusId=258
       and e.DepartmentId = 39
       order by e.OrgStructureDescconcatenated, e.employeename;
       //
       // modified
       //
       select           
       e.EmployeeNumber               as employeeNum,
       e.EmployeeName                 as name,
       e.LastName                     as lastname,
       e.FirstName                    as firstname,
       x.Title                        as xtitle,
       job.JobTitle                   as title,
       e.xGroupCodeDesc               as benefitGroup,
       job.CycleHours/2               as weeklyHours,
       from HR.vwEmployeeInformation     e                                   
       join HR.vwEmployeeJobWithPosition job on e.EmployeeId=job.EmployeeId
       and GETDATE() between job.EffectiveDate     and job.EffectiveEndDate
       and GETDATE() between job.PositionDetailESD and job.PositionDetailEED
       left join COB.jobTitleCrosswalk   x    on job.JobTitle=x.Code
       join HR.EmployeeName  n  on e.EmployeeId=n.EmployeeId
       and GETDATE() between  n.EffectiveDate  and   n.EffectiveEndDate
       where e.vsEmploymentStatusId=258
       and e.DepartmentId = 39
       order by e.employeename, e.EmployeeName,job.JobTitle

    */
    public String findNWJobs(){
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="";
	PayPeriod pp = new PayPeriod();
	// using current date
	String qq = "select e.EmployeeNumber,                                                e.EmployeeName,                                                                 e.LastName,                                                                     e.FirstName,                                                                    x.Title,                                                                      job.JobTitle,                                                                     e.xGroupCodeDesc,                                                             job.CycleHours/2,                                                               job.RateAmount,                                                                 job.EffectiveDate                                                               from HR.vwEmployeeInformation     e                                             join HR.vwEmployeeJobWithPosition job on e.EmployeeId=job.EmployeeId            and GETDATE() between job.EffectiveDate     and job.EffectiveEndDate            and GETDATE() between job.PositionDetailESD and job.PositionDetailEED           left join COB.jobTitleCrosswalk   x    on job.JobTitle=x.Code                   join HR.EmployeeName  n  on e.EmployeeId=n.EmployeeId                           and GETDATE() between  n.EffectiveDate  and   n.EffectiveEndDate                where e.vsEmploymentStatusId=258 ";
	qq += " and e.departmentID in ("+dept_ref+") "; 				
	qq += "order by e.employeename, job.JobTitle ";
				
	con = SingleConnect.getNwConnection();
	if(con == null){
	    msg = " Could not connect to DB ";
	    logger.error(msg);
	    return msg;
	}
	try{
	    pstmt = con.prepareStatement(qq);
	    empNwJobs = new Hashtable<>();
	    rs = pstmt.executeQuery();
	    int jj=1;
	    while(rs.next()){
		String str = rs.getString(1); // emp number
		String str2 = rs.getString(6).trim(); //title
		String str3 = rs.getString(4)+" "+rs.getString(3); //name
		String str4 = rs.getString(7); // group
		String str5 = rs.getString(9); // rate
		String str6 = rs.getString(10); // date
		String grp="";
		if(str4.indexOf("Temporary") > -1) grp = "3";//"Temp";
		    else if(str4.indexOf("Seasonal") > -1) grp="13";//Seasonal"; 
		    else if(str4.indexOf("PT Non") > -1) grp="11";//"Part Time Non-Exempt"
		    else if(str4.indexOf("PT NX") > -1) grp="14";//Part Time"		
		else if(str4.indexOf("FT Exempt") > -1) grp="1";//Exempt
		else if(str4.indexOf("FT Non-Exempt") > -1) grp="2";//Non-Exempt
		else if(str4.indexOf("AFSCME") > -1) grp="4";//Union";
		else grp="Unknown "+str4;
		if(!(grp.equals("1") ||
		     grp.equals("2") ||
		     grp.equals("11") ||
		     grp.equals("4"))){
		    String dd2="", mm2="", dayBefore="";
		    String[] ppInfo = pp.findPayPeriodInfo(str6); 

		    System.err.println(str+" "+str3+" "+str2+" "+str6+" "+grp+" "+ppInfo[0]+" "+ppInfo[1]+" "+ppInfo[2]+" "+ppInfo[3]);
		    // jj++;		    
		    if(nwEmpJobs.containsKey(str)){
			List<List<String>> all = nwEmpJobs.get(str);
			List<String> njob = new ArrayList<>();
			njob.add(str2);
			njob.add(grp);
			njob.add(str6);
			njob.add(ppInfo[0]);
			njob.add(ppInfo[1]);
			njob.add(ppInfo[2]);
			njob.add(ppInfo[3]);
			all.add(njob);
			nwEmpJobs.put(str, all);

		    }
		    else{
			List<List<String>> all = new ArrayList<>();
			List<String> njob = new ArrayList<>();
			njob.add(str2);
			njob.add(grp);
			njob.add(str6);
			njob.add(ppInfo[0]);
			njob.add(ppInfo[1]);
			njob.add(ppInfo[2]);
			njob.add(ppInfo[3]);			
			all.add(njob);
			nwEmpJobs.put(str, all);			
		    }
		}
		    /**
		if(str2.indexOf("-") > -1){
		    str2 = str2.replace('-',' ');
		}
		    */
		    /**
		if(str != null){
		    if(empNwJobs.containsKey(str)){
			Set<String> set = empNwJobs.get(str);
			set.add(str2);
		    }
		    else{
			Set<String> set = new HashSet<>();
			set.add(str2);
			empNwJobs.put(str, set);
		    }
		}
		    */
		
	    }
		
	}
	catch(Exception ex){
	    msg += ex+":"+qq;
	    logger.error(msg);
	}
	finally{
	    Helper.databaseDisconnect(pstmt, rs);
	}
	return msg;
    }
    public void doNextStep(){
	if(nwEmpJobs != null && curEmpJobs != null){
	    Set<String> curKeys = curEmpJobs.keySet();
	    int jj=1;
	    for(String key:curKeys){ // emp_num
		System.err.println(" emp numbr "+key);
		if(jj > 15) break;
		if(nwEmpJobs.containsKey(key)){
		    List<List<String>> allJobs = nwEmpJobs.get(key);
		    Hashtable<String, JobTask> oneJobs = curEmpJobs.get(key);
		    for(List<String> jobl:allJobs){
			String jtitle = jobl.get(0);
			String sg_id = jobl.get(1);
			String nw_date = jobl.get(2);
			// for new job
			String p_id = jobl.get(3); // in pay period
			String p_start = jobl.get(4);
			// for old job termination
			String p2_id = jobl.get(5); // one before
			String p2_end = jobl.get(6); // one before end
			if(oneJobs != null && oneJobs.containsKey(jtitle)){
			    JobTask job = oneJobs.get(jtitle);
			    doFix(job, sg_id, nw_date, p_id, p_start, p2_id,p2_end);
			    jj++;
			}
		    }
		}

	    }

	}
	
    }
    // copy the old job to a new one
    // in time_documents starting from p_id replace the old job with the new job
    // expire the old job
    //    
    private String doFix(JobTask job,
			 String sg_id,
			 String nw_date,
			 String p_id,
			 String p_start,
			 String p2_id,
			 String p2_end){
	String back = "";
	String eff_date = job.getEffective_date();
	PayPeriod pp = new PayPeriod();
	System.err.println(eff_date+" "+p_start);
	long days = pp.findDateDiffWithDate(eff_date, p_start);
	//
	// if more than 20 days we do change otherwise we skip
	if(days > 20){
	    System.err.println(" days "+days);
	    //
	    // terminate the job
	    String old_job_id = job.getId();
	    job.setExpire_date(p2_end);
	    job.doUpdate();
	    // create a new job from at with the new date p_start
	    // and salary group sg_id
	    job.removeExpireDate();
	    job.setEffective_date(p_start);
	    job.setSalary_group_id(sg_id);
	    job.doSave();
	    String new_job_id = job.getId();
	    System.err.println(" old_job_id "+old_job_id+" "+new_job_id+" "+p2_id);
	    // get the new job id
	    // update time_documents wiht the new new_job_id replacing old_job id starting starting p_id2 and after
	    //
	    back = updateDocuments(old_job_id, new_job_id, p2_id);
	    if(!back.isEmpty()){
		System.err.println(back);
	    }
	}
	else{ // we may need to check salary group
	    if(!job.getSalary_group_id().equals(sg_id)){
		job.setSalary_group_id(sg_id);
		back = job.doUpdate();
	    }
	}
	return back;
    }
    private String updateDocuments(String old_job_id, String new_job_id, String pay_period_id){
	Connection con = null;
	PreparedStatement pstmt = null, pstmt2=null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = "update time_documents set job_id=? where job_id=? and "+
	    " pay_period_id >= ? ";
	logger.debug(qq);
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = "Could not connect to DB";
	    return msg;
	}							
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, new_job_id);
	    pstmt.setString(2, old_job_id);
	    pstmt.setString(3, pay_period_id);
	    pstmt.executeUpdate();
	}
	catch(Exception ex){
	    msg += " "+ex;
	    logger.error(msg+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(rs, pstmt, pstmt2);
	    UnoConnect.databaseDisconnect(con);
	}
	return msg;	    
    }
    public PayPeriod getPayPeriod(){
	//
	if(payPeriod == null){
	    if(!pay_period_id.isEmpty()){
		PayPeriod pp = new PayPeriod(pay_period_id);
		String back = pp.doSelect();
		if(back.isEmpty()){
		    payPeriod = pp;
		    pay_period_id = payPeriod.getId();
		}
	    }
	    else{
		PayPeriodList ppl = new PayPeriodList();
		ppl.currentOnly(); 
		String back = ppl.find();
		if(back.isEmpty()){
		    List<PayPeriod> ones = ppl.getPeriods();
		    if(ones != null && ones.size() > 0){
			payPeriod = ones.get(0);
			pay_period_id = payPeriod.getId();
		    }
		}
	    }
	}
	return payPeriod;
    }		
		
}
