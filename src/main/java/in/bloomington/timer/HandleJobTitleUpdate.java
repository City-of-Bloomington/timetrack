package in.bloomington.timer;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.*;
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
		String date="";
		// Parks
		String department_id="5", dept_ref = "39"; // department reference to New World app
		// employee_number, jobs list
		Hashtable<String, Set<String>> empJobs = null;
		// NW employee jobs
		Hashtable<String, Set<String>> empNwJobs = null;
		Hashtable<String, Set<String>> empJobNotInTT = null;
		Hashtable<String, Set<String>> empJobNotInNW = null;		
				
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
		/*
		public Hashtable<String, <Set<String>> getEmployeeJobsNotInNW(){
				return empJobNotInNW;
		}
		*/
		/**
		 * jobs not in New World but not in TimeTrack
		 */
		/*
		public Hashtable<String, <Set<String>> getEmployeeJobsNotInTT(){
				return empJobNotInTT;
		}
		*/
		//
   public String process(){
			 findEmployeeJobs();
			 findNWJobs();
			 String msg = "", status="Success", errors="";
			 System.err.println(" TimeTrack Jobs");
			 // System.err.println(empJobs);
			 empJobNotInNW = new Hashtable<>();
			 empJobNotInTT = new Hashtable<>();
			 Set<String> set = empJobs.keySet();
			 int jj=1, jj2=1;
			 for(String str:set){
					 Set<String> sst = empJobs.get(str);
					 if(empNwJobs.containsKey(str)){
							 Set<String> sst2 = empNwJobs.get(str);
							 for(String str2:sst){
									 if(!sst2.contains(str2)){
											 if(empJobNotInNW.containsKey(str)){
													 Set<String> tempSet = empJobNotInNW.get(str);
													 tempSet.add(str2);
											 }
											 else{
													 Set<String> tempSet = new HashSet<>();
													 tempSet.add(str2);
													 empJobNotInNW.put(str, tempSet);
											 }
									 }
							 }
							 for(String str2:sst2){
									 if(!sst.contains(str2)){
											 if(empJobNotInTT.containsKey(str)){
													 Set<String> tempSet = empJobNotInTT.get(str);
													 tempSet.add(str2);
											 }
											 else{
													 Set<String> tempSet = new HashSet<>();
													 tempSet.add(str2);
													 empJobNotInTT.put(str, tempSet);
											 }

									 }
							 }
					 }
					 else{
							 jj2++;
					 }
			 }
			 System.err.println(" Jobs in NW but not in TT ");			 
			 if(!empJobNotInTT.isEmpty()){
					 Set<String> sst = empJobNotInTT.keySet();
					 jj=1;
					 for(String str:sst){
							 Set<String> set2 = empJobNotInTT.get(str);
							 System.err.println(jj+" "+str+" "+set2);
							 jj++;
					 }
			 }
			 System.err.println(" Jobs in TT but not in NW ");			 
			 if(!empJobNotInNW.isEmpty()){
					 Set<String> sst = empJobNotInNW.keySet();
					 jj=1;
					 for(String str:sst){
							 Set<String> set2 = empJobNotInNW.get(str);
							 System.err.println(jj+" "+str+" "+set2);
							 jj++;
					 }
			 }			 
			 return msg;
		}
		String findEmployeeJobs(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg = "";
				String qq = "select e.employee_number,"+
						" p.name "+
						" from jobs j, positions p,employees e ";
				String qw = " where p.id=j.position_id and j.employee_id=e.id ";
				qw += " and j.expire_date is null ";
				if(!department_id.equals("")){
						qq += ", department_employees d ";
						qw += " and d.employee_id=e.id and d.department_id=? ";
						qw += " and d.expire_date is null ";
				}
				qq += qw;
				qq += " order by e.employee_number";
				logger.debug(qq);
				con = UnoConnect.getConnection();
				if(con == null){
						msg = " Could not connect to DB ";
						logger.error(msg);
						return msg;
				}
				try{
						pstmt = con.prepareStatement(qq);
						int jj=1;				
						if(!department_id.equals("")){
								pstmt.setString(jj++, department_id);
						}
						rs = pstmt.executeQuery();
						empJobs = new Hashtable<>();
						while(rs.next()){
								String str = rs.getString(1); //  employee_number
								String str2 = rs.getString(2); // job title
								if(str2.indexOf("-") > -1){
										str2=str2.replace('-',' ');
								}
								if(str != null && !str.equals("")){
										if(empJobs.containsKey(str)){
												Set<String> jobSet = empJobs.get(str);
												jobSet.add(str2);
										}
										else{
												Set<String> jobSet = new HashSet<>();
												jobSet.add(str2);
												empJobs.put(str, jobSet);
										}
								}
						}
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(pstmt, rs);
						UnoConnect.databaseDisconnect(con);
				}
				return msg;						
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
}
