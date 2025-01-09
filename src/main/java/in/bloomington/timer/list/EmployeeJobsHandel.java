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
 * needed for employee job update
 */
   

public class EmployeeJobsHandel{

    boolean debug = false;
    static final long serialVersionUID = 50L;
    static Logger logger = LogManager.getLogger(EmployeeJobsHandel.class);
    EnvBean envBean = null;
    PayPeriod payPeriod = null;
    String end_date = null;
    String last_day_of_work = "";
    String date = "";

    //
    List<String> benefitGroups = new ArrayList<>();
    HashMap<String, Integer> gradeCompTbl = null;
    HashMap<String, String> benSalTbl = null;
    HashMap<String, JobTask> jobTbl = null;
    HashMap<String, List<JobTask>> jobTblTemp = null;    
    String[] job_ids = null;
    //
    // basic constructor
    //
    public EmployeeJobsHandel(){

    }
    //
    // setters
    //
    public void setEnvBean(EnvBean val){
	if(val != null){
	    envBean = val;
	}
    }
    
    public void setDate(String val){
	if(val != null){
	    date = val;
	}
    }
    private String prepareTables(){
	String back = "";
	BenefitSalaryRefList bsrl = new BenefitSalaryRefList();
	back = bsrl.find();
	if(back.isEmpty()){
	    List<BenefitSalaryRef> refList = bsrl.getRefs();
	    if(refList != null && refList.size() > 0){
		benSalTbl = new HashMap<>();
		for(BenefitSalaryRef one:refList){
		    String str = one.getBenefitName();
		    String str2 = one.getSalaryGroup_id();
		    if(str2 == null || str2.isEmpty()){
			continue;
		    }
		    else{
			// System.err.println(" adding "+str+": "+str2);
			benSalTbl.put(str, str2);
		    }
		}
	    }
	}
	GradeCompHoursList gchl = new GradeCompHoursList();
	back += gchl.find();
	if(back.isEmpty()){
	    List<GradeCompHours> ones  = gchl.getGradeHours();
	    if(ones != null && ones.size() > 0){
		gradeCompTbl = new HashMap<>();
		for(GradeCompHours one:ones){
		    String str = one.getGrade();
		    Integer str2 = one.getHours();
		    if(str2 == null || str2 == 0){
			continue;
		    }
		    else{
			// System.err.println(" adding "+str+": "+str2);
			gradeCompTbl.put(str, str2);
		    }
		}
	    }
	}
	JobTaskList jtl = new JobTaskList();
	back = jtl.findForUpdate();
	if(back.isEmpty()){
	    int jj=1;
	    List<JobTask> ones = jtl.getJobs();
	    if(ones != null && ones.size() > 0){
		jobTbl = new HashMap<>();
		jobTblTemp = new HashMap<>();
		for(JobTask job:ones){
		    String str = job.getEmployee_number();
		    String salary_group_id = job.getSalary_group_id();
		    if(salary_group_id.equals("3") || //temp
		       salary_group_id.equals("12")){ // temp w/ben
			if(jobTblTemp.containsKey(str)){
			    List<JobTask> jobs = jobTblTemp.get(str);
			    jobs.add(job);
			    jobTblTemp.put(str, jobs);
			}
			else{
			    List<JobTask> jobs = new ArrayList<>();
			    jobs.add(job);
			    jobTblTemp.put(str, jobs);
			}
		    }
		    else{
			jobTbl.put(str, job);
		    }
		    // System.err.println(" adding "+jj+", "+str+": "+job);
		    jj++;
		}
	    }
	}
	return back;
    }
    /**


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
//

       
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
    public  String find(){
	String back = "";
	back = prepareTables();
	back = updateJobs();
	return back;
    }

    public  String updateJobs(){
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

	// Bloomington_EmployeeGradeStepRateTitle_template output
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
	if(date.isEmpty()){
	    date = Helper.getToday();
	}
	System.err.println("  date "+date);
	con = SingleConnect.getNwConnection();
	// null everyboday
	String qq =
	    "{call dbo.Bloomington_EmployeeGradeStepRateTitle_template(?,null)}";
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
	    cs.setString(1,date);	    
	    // cs.setString(2,employee_number);
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
	    int jj = 1, jj2=1;
	    while(rs.next()){
		String emp_num = rs.getString(2);
		String rate = "";
		String week_hrs = "20";
		String emp_name = rs.getString(3);
		String ben_group = rs.getString(5);
		String jobTitle = "";
		String jTitle = rs.getString(9); // position
		// System.err.println("NW job before "+jTitle);
		if(jTitle.indexOf(" - ") > 0){
		    jobTitle = jTitle.substring(jTitle.indexOf(" - ")+3).trim();
		}
		else{
		    jobTitle = jTitle.trim();
		}
		String effect_date = rs.getString(6);
		String flsa_code = rs.getString(12);;
		String grade = rs.getString(13);
		String nw_grade = grade;
		if(grade.indexOf("-") > 0){
		    grade = grade.substring(0, grade.indexOf("-"));
		    grade = grade.trim();
		}
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
		    // week_hrs = "40";
		}
		else{
		    if(hr_rate != null && hr_rate.lastIndexOf(".") > -1){
			rate = hr_rate.substring(0,hr_rate.lastIndexOf(".")+3);
		    }
		}
		/**
		if(grade != null && grade.indexOf("- City") > 0){
		    grade = grade.substring(0, grade.indexOf("- City"));
		}
		*/
		/*
		System.err.println("Emp "+emp_name);
		// System.err.println("Emp "+emp_num);		
		// System.err.println("NW job "+jobTitle);
		System.err.println("grade "+grade);
		System.err.println("b group "+ben_group);
		*/
		String empInfo = emp_name+", nw_grade:"+nw_grade+", grade:"+grade+", b_group:"+ben_group;		
		/**
		System.err.println("date "+effect_date);
		System.err.println("step code "+step_code);
		// System.err.println("Weekly hrs "+week_hrs);
		System.err.println(" rate "+rate);
		System.err.println(" position date "+p_date);
		*/
		String salary_group_id = null;
		Integer comp_hours = null;
		JobTask job = null;
		List<JobTask> jobs = null;
		boolean need_update = false;
		String changes = "";
		String change_title = "";
		if(benSalTbl.containsKey(ben_group)){
		    salary_group_id = benSalTbl.get(ben_group);
		}
		if(gradeCompTbl.containsKey(grade)){
		    comp_hours = gradeCompTbl.get(grade);
		}
		if(jobTbl.containsKey(emp_num)){
		    job = jobTbl.get(emp_num);
		}
		else if(jobTblTemp.containsKey(emp_num)){
		    jobs = jobTblTemp.get(emp_num);
		    for(JobTask one:jobs){
			if(!one.getSalary_group_id().equals(salary_group_id)){
			    one.setSalary_group_id(salary_group_id);
			    one.doUpdate();
			}			
		    }
		}
		if(job != null){
		    String job_sal_group_id = job.getSalary_group_id();
		    int job_com_hours = job.getComp_time_weekly_hours();
		    String job_title = job.getTitle().trim();
		    // System.err.println(" grade "+grade);
		    // System.err.println(" comp_hours "+comp_hours);
		    if(salary_group_id == null){
			continue;
		    }
		    if(salary_group_id.equals("3")){
			if(job_sal_group_id.equals("3")){
			    // both temp
			    continue;
			}
			else{ // anything else
			    job.setSalary_group_id(salary_group_id);
			    job.setWeekly_regular_hours(20);
			    job.setComp_time_factor(1.);
			    job.setHoliday_comp_factor(1.);
			    job.setAlt_position_name(jobTitle);
			    job.setEffective_date(p_date);
			    need_update = true;
			    changes += "salary_group_id: "+salary_group_id;
			}
		    }
		    else if(salary_group_id.equals("1")){
			if(!job_sal_group_id.equals(salary_group_id)){
			    job.setSalary_group_id(salary_group_id);
			    if(comp_hours != null){
				job.setComp_time_weekly_hours(comp_hours);
				job.setComp_time_factor(1.);
				job.setHoliday_comp_factor(1.);
				job.setEffective_date(p_date);
				need_update = true;
				changes += "salary_group_id: "+salary_group_id;
			    }
			}
			else {
			    if(comp_hours != null){
				//System.err.println(" comp_hours "+comp_hours);
				if(!comp_hours.equals(job_com_hours)){
				    job.setComp_time_weekly_hours(comp_hours);
				    job.setEffective_date(p_date);
				    need_update = true;
				    changes += "comp_week_hours: "+comp_hours;
				}
			    }
			}
			if(!job_title.equals(jobTitle)){
			    job.setAlt_position_name(jobTitle);
			    job.setEffective_date(p_date);
			    need_update = true;
			    change_title += "title: "+jobTitle;
			}
		    }
		    else if(salary_group_id.equals("2")){
			if(!job_sal_group_id.equals(salary_group_id)){
			    changes += "salary_group_id: "+salary_group_id;
			    job.setSalary_group_id(salary_group_id);
			    if(comp_hours != null){
				job.setWeekly_regular_hours(40);
				job.setComp_time_weekly_hours(comp_hours);
				job.setComp_time_factor(1.5);
				job.setHoliday_comp_factor(1.5);
				job.setEffective_date(p_date);
				need_update = true;
			    }
			}
			if(!job_title.equals(jobTitle)){
			    job.setAlt_position_name(jobTitle);
			    job.setEffective_date(p_date);
			    change_title += "title: "+jobTitle;
			    need_update = true;
			}			
		    }
		    else{ // union, police, fire etc
			// we check salary group and job title only
			//
			if(!job_sal_group_id.equals(salary_group_id)){
			    job.setSalary_group_id(salary_group_id);
			    job.setEffective_date(p_date);
			    changes += "salary_group_id: "+salary_group_id;
			    need_update = true;			    
			}
			if(!job_title.equals(jobTitle)){
			    job.setAlt_position_name(jobTitle);
			    job.setEffective_date(p_date);
			    change_title += "title: "+jobTitle;
			    need_update = true;
			}			
		    }
		    if(need_update){
			back += job.doUpdate();
			/**
			if(!change_title.isEmpty()){
			    System.err.println(empInfo);
			    System.err.println(" changes "+jj+": "+emp_num+", "+change_title);
			    jj++;
			}
			*/
			if(!changes.isEmpty()){
			    System.err.println(empInfo);
			    System.err.println(" changes "+jj2+": "+emp_num+", "+changes);
			    jj2++;
			}
			System.err.println(" ");			
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
    create table benefit_salary_ref(
    id int unsigned not null auto_increment,
    benefit_name varchar(80),
    salary_group_id int unsigned,
    primary key(id),
    foreign key(salary_group_id) references salary_groups(id)
    )engine=InnoDB;
insert into benefit_salary_ref values
(0,'uNON-U RFTx - Utilities Non-Union Reg FT Ex',1),
(0,'TEMP  - Temporary Employees',3),
(0,'uNON-U RFTnx - Utilities Non-Union Reg FT NonEx',2),
(0,'FIRE SWORN - Firefighter Sworn',9),
(0,'uAFSCME RFT - Utilities AFSCME RFT 80 Hours',4),
(0,'NON-U RFULLnx - Non-Union Regular FT Non-Exempt',2),
(0,'AFSCME RFT - AFSCME-80 Hours',4),
(0,'NON-U RFULLx - Non-Union Regular FT Exempt',1),
(0,'BOARD - Board pd members (USB, BPW, BPS)',null),
(0,'CEDC 5/2 - Central Dispatch 5 on 2 off',1),
(0,'POLICE SWORN  - Sworn Police Officers',6),
(0,'CEDC4/2 - Central Dispatch 4 on 2 off',2),
(0,'POLICE SWORN MGT - Police Sworn Management',8),
(0,'NON-U RPARTnx - Non-Union Regular PT Non-Exempt',11),
(0,'ELECTED  - Elected Employees',null),
(0,'uNON-U RPARTnx - Utilities Non-Union Reg PT NonEx',11),
(0,'POLICE SWORN DET - Police Sworn Detective',7),
(0,'uTEMP - Utilities Temporary Employee',3),
(0,'COUNCIL MEM - Council Members',null),
(0,'FIRE SWORN 5X8 - Firefighter Sworn 5x8',10),
(0,'TEMP W/BEN - Temporary Employee With Benefits',12)
    

create table grade_comp_hours(
    id int unsigned not null auto_increment,
    grade_name varchar(32),
    comp_week_hours int unsigned,
    primary key(id)
    )engine=InnoDB;
    
insert into grade_comp_hours values(0,'Grade 04',40),
(0,'Grade 05',40),
(0,'Grade 06',40),
(0,'Grade 07',45),
(0,'Grade 08',45),
(0,'Grade 09',45),
(0,'Grade 10',45),
(0,'Grade 11',45),
(0,'Grade 12',50),
(0,'Grade 13',50),
(0,'Grade 14',50)
    */
}






















































