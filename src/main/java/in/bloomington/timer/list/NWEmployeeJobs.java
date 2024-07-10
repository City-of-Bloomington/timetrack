package in.bloomington.timer.list;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */

import java.util.*;
import java.sql.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;

/**
 * find employee jobs in New World app
 * needed for employee job(s) termination
 */
   

public class NWEmployeeJobs{

    boolean debug = false;
    static final long serialVersionUID = 50L;
    static Logger logger = LogManager.getLogger(NWEmployeeJobs.class);
    EnvBean envBean = null;
    PayPeriod payPeriod = null;
    String end_date = null;
    String last_day_of_work = "";
    String date = "05/17/2024";
    String employee_number="2661", employee_id="1635",
	first_name="", last_name=""; //Jackson Eads
    List<JobTerminate> jobTerms = null;
    List<JobTerminate> matchedJobs = null;
    //
    List<JobTask> jobs = null; // jobs we want to terminate
    String[] job_ids = null;
    //
    // basic constructor
    //
    public NWEmployeeJobs(){

    }
    public NWEmployeeJobs(EnvBean bean,
			  String val,
			  String val2,
			  String val3,
			 List<JobTask> vals
			 ){
	setEnvBean(bean);
	setEmployee_id(val);
	setEmployeeNumber(val2);
	setLast_day_of_work(val3);
	setJobs(vals);
    }
    public NWEmployeeJobs(EnvBean bean,
			  String val,
			  String val2,
			  String val3,
			  String[] vals
			 ){
	setEnvBean(bean);
	setEmployee_id(val);
	setEmployeeNumber(val2);
	setLast_day_of_work(val3);
	setJob_ids(vals);
	//
	findSelectedJobs();
    }
    public void setJob_ids(String[] vals){
	if(vals != null)
	    job_ids = vals;
    }
    //
    // setters
    //
    public void setEnvBean(EnvBean val){
	if(val != null){
	    envBean = val;
	}
    }
    public void setEndDate(String val){
	if(val != null){
	    end_date = val;
	}
    }
    public void setLast_day_of_work(String val){
	if(val != null){
	    last_day_of_work = val;
	}
    }    
    public void setJobs(List<JobTask> vals){
	if(vals != null)
	    jobs = vals;
    }

    
    public void setDate(String val){
	if(val != null){
	    date = val;
	}
    }		
    public void setEmployee_id(String val){
	if(val != null){
	    employee_id = val;
	}
    }
    public void setEmployeeNumber(String val){
	if(val != null){
	    employee_number = val;
	}
    }
    public void setFirstName(String val){
	if(val != null){
	    first_name = val;
	}
    }
    public void setLastName(String val){
	if(val != null){
	    last_name = val;
	}
    }
    public List<JobTerminate> getJobTerms(){
	return jobTerms;
    }
    public boolean hasJobTerms(){
	return jobTerms != null && jobTerms.size() > 0;
    }    
    public boolean hasJobs(){
	return jobs != null && jobs.size() > 0;
    }
    private void findSelectedJobs(){
	if(jobs == null && job_ids != null){
	    for(String str:job_ids){
		JobTask one = new JobTask(str);
		String back = one.doSelect();
		if(jobs == null) jobs = new ArrayList<>();
		jobs.add(one);
	    }
	}
    }
    public boolean hasNwJob(){
       	return
	    jobs != null && jobs.size() > 0 &&
	    jobTerms != null && jobTerms.size() > 0;
    }
    public JobTerminate updateFoundJob(){
	JobTask jj = jobs.get(0);
	JobTerminate jt = jobTerms.get(0);
	jt.setJob_title(jj.getName().trim());
	jt.setJob_id(jj.getId());
	jt.setGroup_id(jj.getGroup_id());
	jt.setWeeklyHours(""+jj.getWeekly_regular_hours());
	jt.findSupervisor();
	jt.findSupervisorInfo(envBean);	    
	return jt;
    }
    public List<JobTerminate> findMatchingJobs(){
	if(jobs != null && jobs.size() > 0){
	    if(jobTerms == null){
		jobTerms = new ArrayList<>();
		for(JobTask jj:jobs){
		    JobTerminate one = new JobTerminate();
		    one.setJob_id(jj.getId());
		    String jttl = jj.getName().trim();
		    one.setJob_title(jttl);
		    one.setGroup_id(jj.getGroup_id());
		    one.setWeeklyHours(""+jj.getWeekly_regular_hours());
		    one.setStart_date(jj.getEffective_date());
		    one.setLast_day_of_work(last_day_of_work);
		    one.findSupervisorInfo(envBean);		
		    jobTerms.add(one);
		    if(matchedJobs == null)
			matchedJobs = new ArrayList<>();		
		    matchedJobs.add(one);
		}
	    }
	    else{
		for(JobTask jj:jobs){
		    boolean found = false;
		    String jttl = jj.getName().trim();
		    for(JobTerminate jt:jobTerms){
			String jttl2 = jt.getNwJobTitle().trim();
			if(jttl.equals(jttl2)){
			    System.err.println("match job "+jttl+":"+jttl2);
			    jt.setJob_id(jj.getId());
			    jt.setJob_title(jttl);
			    jt.setGroup_id(jj.getGroup_id());
			    jt.setWeeklyHours(""+jj.getWeekly_regular_hours());
			    jt.findSupervisorInfo(envBean);
			    found = true;
			    if(matchedJobs == null)
				matchedJobs = new ArrayList<>();
			    matchedJobs.add(jt);
			    break;
			}
		    }
		    if(!found){
			logger.error(" no job match found for job title "+jttl);
		    }
		}
	    }
	}
	else{
	    System.err.println("No jobs to match");
	    logger.error("No jobs to match");
	}
	return matchedJobs;
    }

    /**
       table p
       1 EmployeeJobID
2 EffectiveDate
3 EffectiveEndDate
4 EmployeeID
5 GradeId
6 GradeType
7 GradeStepId
8 CycleHours
9 DailyHours
10 DepartmentId
11 RateAmount
12 PositionId
13 JobId
14 JobTitle
15 PositionDetailESD
16 PositionDetailEED
17 IsPrimaryJob
18 JobEventReasonId
19 PositionNumber
//
// table ej
1 EmployeeJobId
2 EmployeeId
3 EmploymentUniqueId
4 EffectiveDate
5 EffectiveEndDate
6 Comments
7 JobEventReasonId
8 IsPrimaryJob
9 PositionId
10 PositionEntryDate
11 Title
12 DepartmentId
13 DepartmentEntryDate
14 BenefitGroupId
15 vsBenefitExceptionCodeId
16 AccrualPlanHeaderId
17 vsAccrualExceptionCodeId
18 LongevityPlanHeaderId
19 vsLongevityExceptionCodeId
20 DailyHours
21 CycleHours
22 AnnualHours
23 FTE
24 WorkersCompHeaderId
25 vsEEOCCategoryId
26 vsEEOCFunctionId
27 WorkSiteId
28 PlanCalculationPercent
29 vsUnionCodeId
30 ChangedUserId
31 ChangedDate
32 vsPayGroup
33 GradeId
34 GradeStepId
35 SalaryHourHeaderId
36 SpecialAssignmentHeaderId
37 ProjectId
38 FLSAId
39 FLSARate
40 HolidayTimeFlag
41 HolidayHoursOverride
42 RateAmount
43 JobId
44 StandardWeeklyHours
45 vsAnnualSurveyFunction
46 AccrualProbationEndDate
47 vsEEO5Classification
48 ComputedJobTitle
//
// hr.Grade g table
1 GradeId
2 CompanyId
3 GradeCode
4 ChangedUserId
5 ChangedDate
6 GradeType
//
// HR.HRReport_EmployeePayRateReport
// output
//
       1 OrgStructureID
       2 DepartmentCode
       3 DepartmentDescription
       4 EmployeeID
       5 EmployeeNumber
       6 EmployeeName
       7 PrimaryFlag
       8 GradeType
       9 CurrentRate
       10 GradeCode
       11 StepCode
       12 GradeStepDesc
       13 AnnualSalary
       14 ProjectedRate
       15 ProjectedAnnualSalary
       16 LongevityHourly
       17 CertificationHourly
       18 SpecialAssignmentHourly
       19 BaseAnnual
       20 LongevityAnnual
       21 CertificationAnnual
       22 SpecialAssignmentAnnual
       23 AnnualHours
       24 NumberofPayment
       25 CycleHours
// example output
Emp Num, Name,Start Date,Hr Rate,Job Title,Grade, Step, Annual Pay, Pay Type

2661 Eads, Jackson L 2021-05-10 15.750000 FSC-ATT-GF, Class A - ATTENDANT/LABORER I, 1, 16380.00 Hourly
2661 Eads, Jackson L 2021-05-10 16.150000 FSC-SUP-GF, Parks Class D - Supervisor/Laborer II, 3, 16796.00 Hourly
2661 Eads, Jackson L 2021-05-10 15.950000 TLSP-LABII-GF, Parks Class D - Supervisor/Laborer II, 1, 16588.00 Hourly
2661 Eads, Jackson L 2021-05-10 15.950000 WIN-LABII-GF, Parks Class D - Supervisor/Laborer II, 1, 16588.00 Hourly
2807 Overtoom, Greg 2021-10-04 44.964192 null, Grade 10 - City Job Grade 10, null, 93525.52 Annual
2784 Wells, Cameron T 2021-08-23 32.780769 null, 602 - Officer 1st Class, 1, 68184.00 Annual
2188 Deck, Logan M 2019-04-22 23.020000 null, Fire Hourly - Fire Hourly, null, 67219.78 Hourly
2730 Lasher, Jason 2021-06-14 22.450000 null, 106 - AFSCME-106, 3, 46696.00 Hourly
    */
    public String find3(){
	return find();
    }
    public  String find(){
	//
	// 2661:Temp,  2807:Full, 2784:Police,3070:fire, 2730:Union
	// String[] emp_nums = {"2661","2807","2784","2188","2730"};
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", back="";
	CallableStatement cs = null;
	// using current date
	/*
        exec HR.HRReport_EmployeePayRateReport
	@EffectiveDate=NULL,
	@ProjectedIncrease=NULL,
	@EmployeeID=NULL,
	@strOrgStructureID=NULL,
	@strxGroupHeaderID=NULL,
	@strPayTypeID=NULL,
	@RoundDecimals=2,
	@ProposedRate=0,
	@IncludeLongevity=1,
	@IncludeSP=1,
	@IncludeCertification=1,
	@UserID=3,
	@PrimaryOnly=0;

	// second proceduce
1 EmployeeID
2 EmployeeNumber
3 EmployeeName
4 Department
5 BenefitGroup
6 HireDate
7 EffectiveDate
8 PrimaryJob
9 Position
10 Title
11 PayGroup
12 FlsaCode
13 GradeCode
14 StepCode
15 PayType Annual, Hourly
16 HourlyRate
17 PayPeriod
18 Annual
19 FTE
20 Project
21 PositionEntryDate

PayType Annual:exempt/non-exempt, Hourly:temp, Hourly:union, Annual:Police
	
	*/
	/**
        String qq = "exec HR.HRReport_EmployeePayRateReport "+
	    "@EffectiveDate=NULL,"+
	    "@ProjectedIncrease=NULL,"+
	    "@EmployeeID=NULL,"+
	    "@strOrgStructureID=NULL,"+
	    "@strxGroupHeaderID=NULL,"+
	    "@strPayTypeID=NULL,"+
	    "@RoundDecimals=2,"+
	    "@ProposedRate=0,"+
	    "@IncludeLongevity=1,"+
	    "@IncludeSP=1,"+
	    "@IncludeCertification=1,"+
	    "@UserID=3,"+
	    "@PrimaryOnly=0;";
	*/
	con = SingleConnect.getNwConnection();
	String qq =
	    "{call dbo.Bloomington_EmployeeGradeStepRateTitle_template(null,?)}";
	String qq2 =
	    "{CALL HR.HRReport_EmployeePayRateReport(null,null,?,null,null,null,2,0,1,1,1,3,0)}";
	if(con == null){
	    msg = " Could not connect to DB ";
	    logger.error(msg);
	    return msg;
	}
	try{
	    if(debug)
		logger.debug(qq);	    
	    cs = con.prepareCall(qq);
	    // for(String str:emp_nums){
	    
	    cs.setString(1,employee_number);
	    cs.executeQuery();
	    rs = cs.getResultSet();
	    /**
	       ResultSetMetaData rsmd = rs.getMetaData();
	       int columnCount = rsmd.getColumnCount();
	       for (int i = 1; i <= columnCount; i++ ) {
	       String name = rsmd.getColumnName(i);
	       System.err.println(i+" "+name);
	       }
	    */
	    boolean needStep = false;
	    while(rs.next()){
		/**
		String emp_id = rs.getString(1);
		String emp_num = rs.getString(2);
		String emp_name = rs.getString(3);
		String h_date = rs.getString(6);
		// String postion = rs.getString(9); // better than job title
		String p_rate = rs.getString(16);
		String j_title = rs.getString(9);
		String e_grade = rs.getString(13);
		String e_step = rs.getString(14); // null for fire
		String p_type = rs.getString(15);
		String p_annual = rs.getString(18);
		String p_date = rs.getString(21); // position date
		System.err.println(emp_num+" "+emp_name+" "+j_title+" "+h_date+" "+p_rate+", "+e_grade+", "+e_step+", "+p_annual+" "+p_type);
		*/
		String rate = "";
		String week_hrs = "20";
		String emp_name = rs.getString(3);		
		String jobTitle = rs.getString(9);
		String effect_date = rs.getString(6);
		String grade = rs.getString(13);
		String step_code = rs.getString(14);
		if(step_code == null)
		    needStep = true;
		// String week_hrs = rs.getString(4);
		String hr_rate = rs.getString(16);
		String annual_rate = rs.getString(18);
		String p_type = rs.getString(15);
		String p_date = rs.getString(21);
		if(p_type != null && p_type.equals("Annual")){
		    rate = annual_rate;
		    week_hrs = "40";
		}
		else{
		    if(hr_rate != null && hr_rate.lastIndexOf(".") > -1){
			rate = hr_rate.substring(0,hr_rate.lastIndexOf(".")+3);
		    }
		}
		if(grade != null && grade.indexOf("- City") > 0){
		    grade = grade.substring(0, grade.indexOf("- City"));
		}
		System.err.println("Emp "+emp_name);
		System.err.println("job "+jobTitle);
		System.err.println("date "+effect_date);
		System.err.println("grade "+grade);
		System.err.println("step code "+step_code);
		// System.err.println("Weekly hrs "+week_hrs);
		System.err.println(" rate "+rate);
		System.err.println(" position date "+p_date);
		if(jobTerms == null)
		    jobTerms = new ArrayList<>();
		JobTerminate jt = new JobTerminate(jobTitle,grade,step_code,rate,p_date, week_hrs);
		jt.setLast_day_of_work(last_day_of_work);
		jobTerms.add(jt);
	    }
	    if(needStep && jobTerms.size() == 1){
		logger.debug(qq2);
		System.err.println(qq2);
		cs = con.prepareCall(qq2);
		
		cs.setString(1,employee_number);
		cs.executeQuery();
		rs = cs.getResultSet();
		if(rs.next()){
		    String step_code = rs.getString(11);
		    System.err.println(" step code "+step_code);
		    if(step_code != null){
			jobTerms.get(0).setJob_step(step_code);
		    }
		}
	    }
	}
	catch(Exception ex){
	    back += ex;
	    logger.error(ex+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(cs, rs);
	    Helper.databaseDisconnect(pstmt, rs);
	}
	return back;
    }	
    /**
     * find jobs
     *
     */
    /**
1 JobTitle
2 EmployeeNumber
3 EmployeeJobID
4 EffectiveDate
5 EffectiveEndDate
6 EmployeeID
7 GradeId
8 GradeType
9 GradeStepId
10 CycleHours
11 DailyHours
12 DepartmentId
13 RateAmount
14 PositionId
15 JobId
16 JobTitle
17 PositionDetailESD
18 PositionDetailEED
19 IsPrimaryJob
20 JobEventReasonId
21 PositionNumber
    */       
    public  String findOld(){
	//
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", back="";
	//
	if(date == null || date.isEmpty()){
	    date = Helper.getToday();
	}
	String qq = "select p.JobTitle, g.GradeCode,e.EmployeeNumber, "+
	    " ej.StandardWeeklyHours, gs.StepCode, p.* "+
	    " from hr.vwEmployeeJobWithPosition p, "+
	    " hr.Grade g, "+
	    " hr.GradeStep gs, "+
	    " hr.employee e "+
	    " LEFT JOIN hr.EmployeeJob ej ON e.EmployeeId = ej.EmployeeId "+ 
	    " where ej.EmployeeId = p.EmployeeID "+
	    " and g.GradeId = ej.GradeId "+
	    " and gs.GradeStepId = p.GradeStepId "+
	    " and gs.GradeId = p.GradeId "+ // added 
	    " and ? between p.EffectiveDate and p.EffectiveEndDate "+
	    " and ? between p.PositionDetailESD and p.PositionDetailEED "+
	    " and ? between ej.EffectiveDate and ej.EffectiveEndDate ";
	if(!employee_number.isEmpty()){
	    qq += " and e.EmployeeNumber = ? ";
	}
	else{
	    qq += " and e.LastName like ? and e.FirstName like ? ";
	}

	con = SingleConnect.getNwConnection();
	if(con == null){
	    msg = " Could not connect to DB ";
	    logger.error(msg);
	    return msg;
	}
	try{
	    if(debug)
		logger.debug(qq);
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, date);
	    pstmt.setString(2, date);
	    pstmt.setString(3, date);	    
	    if(!employee_number.isEmpty()){
		pstmt.setString(4, employee_number);
	    }
	    else{
		pstmt.setString(4, last_name);
		pstmt.setString(5, first_name);								
	    }
	    rs = pstmt.executeQuery();
	    /**
	    ResultSetMetaData rsmd = rs.getMetaData();
	    int columnCount = rsmd.getColumnCount();							 
	    for (int i = 1; i <= columnCount; i++ ) {
		String name = rsmd.getColumnName(i);
		System.err.println(i+" "+name);
	    }
	    */
	    String prev_job_id="";
	    while(rs.next()){

		String jobTitle = rs.getString(1);
		String effect_date = rs.getString(20);
		String grade = rs.getString(2);
		String step_code = rs.getString(5);
		String week_hrs = rs.getString(4);
		String rate = rs.getString(16);
		String job_id = rs.getString(6);
		if(!job_id.equals(prev_job_id)){
		    prev_job_id = job_id;
		    if(effect_date != null){
			effect_date = effect_date.substring(0,10);
		    }
		    System.err.println("job "+job_id+" "+jobTitle);
		    System.err.println("date "+effect_date);
		    System.err.println("grade "+grade);
		    System.err.println("step code "+step_code);
		    System.err.println("Weekly hrs "+week_hrs);
		    System.err.println(" rate "+rate);
		    if(jobTerms == null)
			jobTerms = new ArrayList<>();
		    JobTerminate jt = new JobTerminate(jobTitle,grade,step_code,rate,effect_date, week_hrs);
		    jt.setLast_day_of_work(last_day_of_work);
		    jobTerms.add(jt);
		}
	    }
	}
	catch(Exception ex){
	    back += ex;
	    logger.error(ex+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(pstmt, rs);
	}

	return back;
    }
	
    /**
       exec HR.HRReport_EmployeePayRateReport			 
       1-@EffectiveDate='2021-12-09 00:00:00',
       2-@ProjectedIncrease=N'0',
       3-@EmployeeID=NULL,
       4-@strOrgStructureID=N'36', // dept_ref
       5-@strxGroupHeaderID=NULL,
       6-@strPayTypeID=N'3,1,2',
       7-@RoundDecimals=2,
       8-@ProposedRate=0,
       9-@IncludeLongevity=1,
       10-@IncludeSP=1,
       11-@IncludeCertification=1,
       12-@UserID=3

       // output
       1 OrgStructureID
       2 DepartmentCode
       3 DepartmentDescription
       4 EmployeeID
       5 EmployeeNumber
       6 EmployeeName
       7 PrimaryFlag
       8 GradeType
       9 CurrentRate
       10 GradeCode
       11 StepCode
       12 GradeStepDesc
       13 AnnualSalary
       14 ProjectedRate
       15 ProjectedAnnualSalary
       16 LongevityHourly
       17 CertificationHourly
       18 SpecialAssignmentHourly
       19 BaseAnnual
       20 LongevityAnnual
       21 CertificationAnnual
       22 SpecialAssignmentAnnual
       23 AnnualHours
       24 NumberofPayment
       25 CycleHours


        exec HR.HRReport_EmployeePayRateReport
	@EffectiveDate=NULL,
	@ProjectedIncrease=NULL,
	@EmployeeID=NULL,
	@strOrgStructureID=NULL,
	@strxGroupHeaderID=NULL,
	@strPayTypeID=NULL,
	@RoundDecimals=2,
	@ProposedRate=0,
	@IncludeLongevity=1,
	@IncludeSP=1,
	@IncludeCertification=1,
	@UserID=3,
	@PrimaryOnly=0;
	
	   AND (@EmployeeNumber is NULL OR (EMP.EmployeeNumber=@EmployeeNumber))


	
	exec dbo.Bloomington_EmployeeGradeStepRateTitle_template @AsOfDate=NULL,@EmployeeNumber=NULL;
       
    */

}






















































