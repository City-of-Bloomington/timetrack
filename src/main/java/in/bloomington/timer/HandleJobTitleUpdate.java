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
					 if(emp_num == null || emp_num.equals("")) continue;
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
		String findEmployeeJobs(){
				getPayPeriod();
				DepartmentEmployeeList dempl = new DepartmentEmployeeList();
				dempl.setDepartment_id("5"); // parks
				dempl.setNoExpireDate();
				dempl.setEmployeeActiveOnly();
				String back = dempl.find();
				if(back.equals("")){
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
				// using current date
				String qq = "select e.EmployeeNumber,                                                        e.EmployeeName,                                                                 e.LastName,                                                                     e.FirstName,                                                                    x.Title,                                                                      job.JobTitle,                                                                     e.xGroupCodeDesc,                                                             job.CycleHours/2                             from HR.vwEmployeeInformation     e                                             join HR.vwEmployeeJobWithPosition job on e.EmployeeId=job.EmployeeId            and GETDATE() between job.EffectiveDate     and job.EffectiveEndDate            and GETDATE() between job.PositionDetailESD and job.PositionDetailEED           left join COB.jobTitleCrosswalk   x    on job.JobTitle=x.Code                   join HR.EmployeeName  n  on e.EmployeeId=n.EmployeeId                           and GETDATE() between  n.EffectiveDate  and   n.EffectiveEndDate                where e.vsEmploymentStatusId=258 ";
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
						while(rs.next()){
								String str = rs.getString(1); // emp number
								String str2 = rs.getString(6).trim(); //title
								if(str2.indexOf("-") > -1){
										str2 = str2.replace('-',' ');
								}
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
    public PayPeriod getPayPeriod(){
				//
				if(payPeriod == null){
						if(!pay_period_id.equals("")){
								PayPeriod pp = new PayPeriod(pay_period_id);
								String back = pp.doSelect();
								if(back.equals("")){
										payPeriod = pp;
										pay_period_id = payPeriod.getId();
								}
						}
						else{
								PayPeriodList ppl = new PayPeriodList();
								ppl.currentOnly(); 
								String back = ppl.find();
								if(back.equals("")){
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
