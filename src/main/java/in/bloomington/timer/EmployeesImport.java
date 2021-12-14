package in.bloomington.timer;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 */

import java.sql.*;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.io.Reader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVFormat;
import java.text.SimpleDateFormat;
import in.bloomington.timer.bean.Type;
import in.bloomington.timer.bean.Group;
import in.bloomington.timer.bean.Position;
import in.bloomington.timer.bean.Node;
import in.bloomington.timer.bean.JobTask;
import in.bloomington.timer.bean.Employee;
import in.bloomington.timer.bean.Department;
import in.bloomington.timer.bean.SalaryGroup;
import in.bloomington.timer.bean.GroupManager;
import in.bloomington.timer.bean.EnvBean;
import in.bloomington.timer.bean.Profile;
import in.bloomington.timer.bean.BenefitGroup;
import in.bloomington.timer.list.TypeList;
import in.bloomington.timer.list.GroupList;
import in.bloomington.timer.list.NodeList;
import in.bloomington.timer.list.EmpList;
import in.bloomington.timer.list.BenefitGroupList;
import in.bloomington.timer.list.ProfileList;
import in.bloomington.timer.list.DepartmentList;
import in.bloomington.timer.list.SalaryGroupList;
import in.bloomington.timer.util.Helper;

//import org.apache.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EmployeesImport{

		boolean debug = true;
		static Logger logger = LogManager.getLogger(EmployeesImport.class);
		static final long serialVersionUID = 220L;
		static EnvBean bean = null;		
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Map<String, String> emps = new HashMap<>(); // username, id
		Map<String, Department> depts = new HashMap<>(); // name, id
		Map<String, String> groups = new HashMap<>(); // name, id
		Map<String, String> positions = new HashMap<>(); // name, id
		Map<String, String> salaryGrps = new HashMap<>(); // salary groups
		Map<String, List<Group>> deptGroups = new HashMap<>();
		List<BenefitGroup> benefitGroups = null;
		Map<String, Profile> profMap = new HashMap<>();
		String dept_refs = "";
		String errors = "";
		public EmployeesImport(){

		}
		public EmployeesImport(EnvBean val){
				if(val != null)
						bean = val;
		}		
		public String doImport(File myFile){
				//
				// clean up
				/**
					delete from group_employees where id > 104;delete from department_employees where id > 103;delete from group_managers where id > 47;delete from jobs where id > 46;delete from employees where id > 116;delete from groups where id > 72;delete from positions where id > 49;

					// on timetrack server
					delete from group_employees where id > 104;
					delete from department_employees where id > 103;
					delete from group_managers where id > 47;
					delete from jobs where id > 46;
					delete from groups where id > 72;
					delete from positions where id > 49;
					delete from employees where id > 116;
					delete from time_actions where document_id in (select id from time_documents where employee_id > 116);
					delete from time_documents where employee_id > 116;
					
				 */
				
				prepareMaps();
				// BufferedReader in = new BufferedReader(new FileReader(myFile));
				// String strLine = null;
				// while ((strLine = in.readLine()) != null){
				
				/*
					clean up for reimport
					delete from department_employees where id >
					delete from group_employees where id >
					delete from group_managers where id >
					delete from jobs where id >
					delete from employees where id >
					
					
				// this is the excel header
				// firs row
					 Deparment, name of dept
					 
				// second row
				   groups, group names,
					 
				// 3th row
				   Employee Number,
					 Employee Name, // last, firs
					 username,
					 Department,
					 Title,
					 Salary Group,
					 Employee Group

				*/
				String back = "";
				// Reader in = null;
				int jj=1; 
				try{
						// in = new FileReader(fileName);
						//
						// if you want to define the header 
						// Iterable<CSVRecord> records = CSVFormat.RFC4180.withHeader("ID", "Last Name", "First Name").parse(in);
						//
						// header auto detection
						// Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
						// File myFile = new File("/srv/data/timetrack/facilities.csv");
						FileReader fr = new FileReader(myFile);
						Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(fr);
						//
						String str="", str2="", str3="", str4="";						
						for (CSVRecord record : records) {
								boolean emp_exist = false;
								str = record.get(0);
								System.err.println(" record "+str);
								if(str != null && str.equals("Department")){
										str = record.get(1);
										if(str != null && !str.isEmpty()){
												if(!depts.containsKey(str)){
														Department dept = new Department();
														dept.setName(str);
														str = record.get(2);
														dept.setLdap_name(str);
														str = record.get(3);
														dept.setRef_id(str);
														if(!dept_refs.isEmpty()){
																dept_refs +=",";
														}
														dept_refs += str;
														str = dept.doSave();
														if(str.isEmpty()){
																depts.put(dept.getName(), dept);
														}
														else{
																errors += str;
														}
												}
												else{ //we already have the dept, we just need the
														// the dept ref to get the profiles
														Department dept = depts.get(str);
														str = dept.getRef_id();
														if(str == null || str.isEmpty())
																str = record.get(3);
														if(!dept_refs.isEmpty()){
																dept_refs +=",";
														}
														dept_refs += str;														
												}
										}
										System.err.println("dept refs "+dept_refs);
								}
								else if(str != null && str.trim().equals("Group")){
										// after departments is done
										prepareProfiles(); 
										str = record.get(1); // group name
										str2 = record.get(2); // dept name
										if(str != null && !str.isEmpty() &&
											 str2 != null && !str2.isEmpty()){
												if(depts.containsKey(str2)){
														String dept_id = "";
														Department dept = depts.get(str2);
														if(dept != null)
																dept_id = dept.getId();
														boolean groupFound = false;
														if(deptGroups.containsKey(str2)){
																List<Group> ones = deptGroups.get(str2);
																for(Group gg:ones){
																		if(gg.getName().equals(str)){
																				groupFound = true;
																				groups.put(gg.getName(), gg.getId());
																		}
																}
																if(!groupFound){
																		Group gg = new Group();
																		gg.setName(str);
																		gg.setDescription(str);
																		gg.setDepartment_id(dept_id);
																		str = gg.doSave();
																		if(str.isEmpty()){
																				ones.add(gg);
																				deptGroups.put(str2, ones);
																				groups.put(gg.getName(), gg.getId());
																		}
																}
																/** existing group
																else{
																		errors += str;
																}
																*/
														} // no groups in this department
														else{
																List<Group> ones = new ArrayList<>();
																Group gg = new Group();
																gg.setName(str);
																gg.setDescription(str);
																gg.setDepartment_id(dept_id);
																str = gg.doSave();
																if(str.isEmpty()){
																		ones.add(gg);
																		deptGroups.put(str2, ones);
																		groups.put(gg.getName(), gg.getId());
																}																
														}
												}
												else{
														errors += " department "+str+" not found ";
												}
										}
								}
								else if(str != null &&
												str.equals("Employee")){
										Profile profile = null;
										BenefitGroup bgroup = null;
										String position_id="", dept_id="",
												salary_grp_id="", dept_name="",
												emp_group_id="";
										int weekly_regular_hours=40,
												comp_time_weekly_hours=40;
										double comp_time_factor = 1.0,
												holiday_comp_factor=1.0;
										boolean clockIn = false;
										str = record.get(1);
										if(str != null && !str.isEmpty()){

												if(profMap.containsKey(str)){
														profile = profMap.get(str);
														bgroup = profile.getBenefitGroup();
												}
												Employee emp = new Employee();
												emp.setRolesText("Employee");
												emp.setEffective_date("07/01/2017");
												emp.setEmployee_number(str);
												str = record.get(2);
												if(str != null && str.indexOf(",") > 0){
														String[] strArr  = str.split(",");
														if(strArr != null && strArr.length == 2){
																emp.setLast_name(strArr[0].trim());
																emp.setFirst_name(strArr[1].trim());
														}
												}
												else{
														errors += " employee name not set properly "+str;
												}
												str = record.get(3); // username
												if(str != null && !str.isEmpty()){
														emp.setUsername(str);
														back = emp.doSelect();
														if(!back.isEmpty()){ // not exist
																EmpList empl = new EmpList(bean, str, true);
																back = empl.simpleFind();
																if(!back.isEmpty()){
																		errors += back;
																}
																else{
																		List<Employee> ldapEmps = empl.getEmps();
																		if(ldapEmps != null && ldapEmps.size() > 0){
																				Employee ldapEmp = ldapEmps.get(0);
																				emp.setId_code(ldapEmp.getId_code());
																				emp.setEmail(ldapEmp.getEmail());
																		}
																}
														}
														else{
																emp_exist = true;
														}
												}
												str = record.get(4); // dept
												if(str != null && !str.isEmpty()){
														dept_name = str;
														if(depts.containsKey(str)){
																Department dept = depts.get(str);
																if(dept != null)
																		dept_id = dept.getId();
																emp.setDepartment_id(dept_id);
														}
														else {
																errors += " Employee department not set properly";
														}
												}
												String job_name = "";
												if(profile != null){
														job_name = profile.getJob_name();
												}
												if(job_name.isEmpty()){
														errors += " job title not found ";
												}
												if(job_name != null && !job_name.isEmpty()){
														if(job_name.indexOf("/") > 0){
																job_name = job_name.replaceAll("/"," ");
														}
														if(positions.containsKey(job_name)){
																position_id = positions.get(job_name);
														}
														else{
																Position position = new Position(job_name,
																																 job_name,
																																 job_name);
																str = position.doSave();
																if(str.isEmpty()){
																		position_id = position.getId();
																		positions.put(position.getName(), position_id);
																}
																else{
																		errors += str;
																}
														}
												}
												if(profile != null && bgroup != null){
														System.err.println("using profile");
														str = bgroup.getSalary_group_name();
														System.err.println(" salary group "+str);
														if(salaryGrps.containsKey(str)){
																salary_grp_id = salaryGrps.get(str);
														}
														else {
																errors += " salary group name "+str+" not found ";
														}
														weekly_regular_hours = (int)profile.getStWeeklyHrs();
														comp_time_weekly_hours = (int)profile.getCompTimeAfter();
														comp_time_factor = profile.getCompTimeMultiple();
														holiday_comp_factor = profile.getHolidayTimeMultiple();
												}
												str = record.get(5); // group name
												if(str != null && !str.isEmpty()){
														if(!dept_name.isEmpty() && deptGroups.containsKey(dept_name)){
																List<Group> grps = deptGroups.get(dept_name);
																for(Group one:grps){
																		if(one.getName().equals(str)){
																				emp_group_id = one.getId();
																				break;
																		}
																}
														}
														if(!emp_group_id.isEmpty()){
																emp.setGroup_id(emp_group_id);
														}
														else{
																errors += " employee group name not set properly: "+str;
														}
												}
												try{ // in case we have this field
														str = record.get(6); // clockIn
														if(str != null && !str.isEmpty()){
																clockIn = true;
														}
												}catch(Exception ex){
														// we do not have, so we ignore
												}
												if(errors.isEmpty()){
														if(emp_exist){
																str = emp.doUpdateDeptGroupInfo();
														}
														else{
																str = emp.doSave();
														}
														if(str.isEmpty()){
																emps.put(emp.getUsername(), emp.getId());
														}
														else{
																errors += str;
														}
												}
												JobTask job = new JobTask();
												job.setPosition_id(position_id);
												job.setSalary_group_id(salary_grp_id);
												job.setEmployee_id(emp.getId());
												job.setEffective_date("07/01/2017");
												job.setGroup_id(emp_group_id);
												job.setPrimary_flag(true);
												job.setWeekly_regular_hours(weekly_regular_hours);
												job.setComp_time_weekly_hours(comp_time_weekly_hours);
												job.setComp_time_factor(comp_time_factor);
												job.setHoliday_comp_factor(holiday_comp_factor);
												if(clockIn){
														job.setClock_time_required(true);
												}
												str = job.doSave();
												if(!str.isEmpty()){
														errors += str;
												}
										}
								}
								else if(str != null &&
												str.equals("Manager")){
										str = record.get(1); // group name
										str2 = record.get(2); // approver username
										str3 = record.get(3); // payroll approver username
										str4 = record.get(4); // reviewers
										String approve_role_id="3", payroll_role_id="4";
										String review_role_id = "6";
										String group_id="", role_id="", emp_id="";
										String approver_id="", payroll_id="";
										String reviewer_id="";
										List<String> approver_ids = new ArrayList<>();
										List<String> payroll_ids = new ArrayList<>();
										List<String> reviewer_ids = new ArrayList<>();
										if(str.isEmpty() || str2.isEmpty() || str3.isEmpty()){
												errors += "Error setting group manager "+str;
										}
										else{
												if(groups.containsKey(str)){
														group_id = groups.get(str);
												}
												else{
														errors +=" Error manager group, group name does not exist "+str;
												}
												String[] str2Arr = new String[1];
												if(str2.indexOf(",") > 0){
														str2Arr = str2.split(",");
												}
												else{
														str2Arr[0] = str2;
												}
												for(String stt:str2Arr){
														System.err.println(stt);
														if(emps.containsKey(stt)){
																approver_id = emps.get(stt);
																approver_ids.add(approver_id);
														}
														else{
																Employee empp = new Employee();
																empp.setUsername(stt);
																back = empp.doSelect();
																if(back.isEmpty()){
																		approver_id = empp.getId();
																		approver_ids.add(approver_id);
																		emps.put(stt, empp.getId());
																}
																else{
																		errors +=" Error group approver not set properly "+str2;
																}
														}
												}
												String[] str3Arr = new String[1];
												if(str3.indexOf(",") > 0){
														str3Arr = str3.split(",");
												}
												else{
														str3Arr[0] = str3;
												}
												for(String stt2:str3Arr){
														if(emps.containsKey(stt2)){
																payroll_id = emps.get(stt2);
																payroll_ids.add(payroll_id);
														}
														else{
																Employee empp = new Employee();
																empp.setUsername(stt2);
																back = empp.doSelect();
																if(back.isEmpty()){
																		payroll_id = empp.getId();
																		payroll_ids.add(payroll_id);
																		emps.put(stt2, empp.getId());
																}
																else{
																		errors +=" Error group payroll username not set properly "+str3;
																}
														}
												}
												String[] str4Arr = new String[1];
												if(str4.indexOf(",") > 0){
														str4Arr = str4.split(",");
												}
												else{
														str4Arr[0] = str4;
												}
												for(String stt4:str4Arr){
														if(emps.containsKey(stt4)){
																reviewer_id = emps.get(stt4);
																reviewer_ids.add(reviewer_id);
														}
														else{
																Employee empp = new Employee();
																empp.setUsername(stt4);
																back = empp.doSelect();
																if(back.isEmpty()){
																		reviewer_id = empp.getId();
																		reviewer_ids.add(reviewer_id);
																		emps.put(stt4, empp.getId());
																}
																else{
																		errors +=" Error group reviewer username not set properly "+str4;
																}
														}														
												}
												if(errors.isEmpty()){
														GroupManager gm = null;
														
														for(String ap_id:approver_ids){
																gm = new GroupManager();
																gm.setGroup_id(group_id);
																gm.setEmployee_id(ap_id);
																gm.setWf_node_id(approve_role_id);
																gm.setStart_date("07/01/2017");
																str = gm.doSave();
																if(!str.isEmpty()) errors += str;
														}
														for(String rol_id:payroll_ids){
																gm = new GroupManager();
																gm.setGroup_id(group_id);
																gm.setEmployee_id(rol_id);
																gm.setWf_node_id(payroll_role_id);
																gm.setStart_date("07/01/2017");
																str = gm.doSave();
																if(!str.isEmpty()) errors += str;
														}
														if(reviewer_ids != null && reviewer_ids.size() > 0){
																for(String rev_id:reviewer_ids){
																		gm = new GroupManager();
																		gm.setGroup_id(group_id);
																		gm.setEmployee_id(rev_id);
																		gm.setWf_node_id(review_role_id);
																		gm.setStart_date("07/01/2017");
																		str = gm.doSave();
																		if(!str.isEmpty()) errors += str;
																}
														}
												}
										}
								}
								if(!errors.isEmpty()){
										System.err.println("Errors "+errors);
										break;
								}
						}
				}
				catch(Exception ex){
						System.err.println(ex);
						errors += ex;
				}
				return errors;
		}
		//
		// import for multiple jobs
		//
		public String doImportMultiJobs(File myFile){
				//
				prepareMaps();
				String back = "";
				int jj=1; 
				try{
						FileReader fr = new FileReader(myFile);
						Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(fr);
						//
						String str="", str2="", str3="", str4="";						
						for (CSVRecord record : records) {
								boolean emp_exist = false;
								str = record.get(0);
								System.err.println(" record "+str);
								if(str != null && str.equals("Department")){
										str = record.get(1);
										if(str != null && !str.isEmpty()){
												if(!depts.containsKey(str)){
														Department dept = new Department();
														dept.setName(str);
														str = record.get(2);
														dept.setLdap_name(str);
														str = record.get(3);
														dept.setRef_id(str);
														if(!dept_refs.isEmpty()){
																dept_refs +=",";
														}
														dept_refs += str;
														str = dept.doSave();
														if(str.isEmpty()){
																depts.put(dept.getName(), dept);
														}
														else{
																errors += str;
														}
												}
												else{ //we already have the dept, we just need the
														// the dept ref to get the profiles
														Department dept = depts.get(str);
														str = dept.getRef_id();
														if(str == null || str.isEmpty())
																str = record.get(3);
														if(!dept_refs.isEmpty()){
																dept_refs +=",";
														}
														dept_refs += str;														
												}
										}
										System.err.println("dept refs "+dept_refs);
								}
								else if(str != null && str.trim().equals("Group")){
										// after departments is done
										prepareProfiles(); 
										str = record.get(1); // group name
										str2 = record.get(2); // dept name
										if(str != null && !str.isEmpty() &&
											 str2 != null && !str2.isEmpty()){
												if(depts.containsKey(str2)){
														String dept_id = "";
														Department dept = depts.get(str2);
														if(dept != null)
																dept_id = dept.getId();
														boolean groupFound = false;
														if(deptGroups.containsKey(str2)){
																List<Group> ones = deptGroups.get(str2);
																for(Group gg:ones){
																		if(gg.getName().equals(str)){
																				groupFound = true;
																				groups.put(gg.getName(), gg.getId());
																		}
																}
																if(!groupFound){
																		Group gg = new Group();
																		gg.setName(str);
																		gg.setDescription(str);
																		gg.setDepartment_id(dept_id);
																		str = gg.doSave();
																		if(str.isEmpty()){
																				ones.add(gg);
																				deptGroups.put(str2, ones);
																				groups.put(gg.getName(), gg.getId());
																		}
																} // existing group we skip
														} // no groups in this department
														else{
																List<Group> ones = new ArrayList<>();
																Group gg = new Group();
																gg.setName(str);
																gg.setDescription(str);
																gg.setDepartment_id(dept_id);
																str = gg.doSave();
																if(str.isEmpty()){
																		ones.add(gg);
																		deptGroups.put(str2, ones);
																		groups.put(gg.getName(), gg.getId());
																}																
														}
												}
												else{
														errors += " department "+str+" not found ";
												}
										}
								}
								else if(str != null &&
												str.equals("Employee")){
										Profile profile = null;
										BenefitGroup bgroup = null;
										String position_id="", dept_id="",
												salary_grp_id="", dept_name="",
												emp_group_id="";
										int weekly_regular_hours=40,
												comp_time_weekly_hours=40;
										double comp_time_factor = 1.0,
												holiday_comp_factor=1.0;
										str = record.get(1); // employee number
										if(str != null && !str.isEmpty()){

												if(profMap.containsKey(str)){
														profile = profMap.get(str);
														bgroup = profile.getBenefitGroup();
												}
												Employee emp = new Employee();
												emp.setRolesText("Employee");
												emp.setEffective_date("07/01/2017");
												emp.setEmployee_number(str);
												str = record.get(2);
												if(str != null && str.indexOf(",") > 0){
														String[] strArr  = str.split(",");
														if(strArr != null && strArr.length == 2){
																emp.setLast_name(strArr[0].trim());
																emp.setFirst_name(strArr[1].trim());
														}
												}
												else{
														errors += " employee name not set properly "+str;
												}
												str = record.get(3); // username
												if(str != null && !str.isEmpty()){
														emp.setUsername(str);
														back = emp.doSelect();
														if(!back.isEmpty()){ // not exist
																EmpList empl = new EmpList(bean, str, true);
																back = empl.simpleFind();
																if(!back.isEmpty()){
																		errors += back;
																}
																else{
																		List<Employee> ldapEmps = empl.getEmps();
																		if(ldapEmps != null && ldapEmps.size() > 0){
																				Employee ldapEmp = ldapEmps.get(0);
																				emp.setId_code(ldapEmp.getId_code());
																				emp.setEmail(ldapEmp.getEmail());
																		}
																}
														}
														else{
																emp_exist = true;
														}
												}
												str = record.get(4); // dept
												if(str != null && !str.isEmpty()){
														dept_name = str;
														if(depts.containsKey(str)){
																Department dept = depts.get(str);
																if(dept != null)
																		dept_id = dept.getId();
																emp.setDepartment_id(dept_id);
														}
														else {
																errors += " Employee department not set properly";
														}
												}
												str = record.get(5); // groups name
												if(str != null && !str.isEmpty()){
														if(str.indexOf(",") == -1){
																if(!dept_name.isEmpty() && deptGroups.containsKey(dept_name)){
																		List<Group> grps = deptGroups.get(dept_name);
																		for(Group one:grps){
																				if(one.getName().equals(str)){
																						emp_group_id = one.getId();
																						break;
																				}
																		}
																}
																if(!emp_group_id.isEmpty()){
																		emp.setGroup_id(emp_group_id);
																}
																else{
																		errors += " employee group name not set properly: "+str;
																}
														}
														else{ // multiple groups
																String[] g_arr = null;
																try{
																		g_arr = str.split(",");
																}catch(Exception ex){
																		System.err.println(ex);
																}
																if(g_arr != null && g_arr.length > 0){
																		for(String gst:g_arr){
																				emp_group_id="";
																				if(!dept_name.isEmpty() && deptGroups.containsKey(dept_name)){
																						List<Group> grps = deptGroups.get(dept_name);
																						for(Group one:grps){
																								if(one.getName().equals(gst)){
																										emp_group_id = one.getId();
																										break;
																								}
																						}
																				}
																				if(!emp_group_id.isEmpty()){
																						emp.addGroup_id(emp_group_id);
																				}
																				else{
																						errors += " employee group name not set properly: "+str;
																				}
																		}
																}																
														}
												}
												if(errors.isEmpty()){
														if(emp_exist){
																str = emp.doUpdateDeptGroupInfo();
														}
														else{
																str = emp.doSave();
														}
														if(str.isEmpty()){
																emps.put(emp.getUsername(), emp.getId());
														}
														else{
																errors += str;
														}
												}
										}
								}
								else if(str != null &&
												str.equals("Manager")){
										str = record.get(1); // group name
										str2 = record.get(2); // approver username
										str3 = record.get(3); // payroll approver username
										str4 = record.get(4); // reviewers
										String approve_role_id="3", payroll_role_id="4";
										String review_role_id = "6";
										String group_id="", role_id="", emp_id="";
										String approver_id="", payroll_id="";
										String reviewer_id="";
										List<String> approver_ids = new ArrayList<>();
										List<String> payroll_ids = new ArrayList<>();
										List<String> reviewer_ids = new ArrayList<>();
										if(str.isEmpty() || str2.isEmpty() || str3.isEmpty()){
												errors += "Error setting group manager "+str;
										}
										else{
												if(groups.containsKey(str)){
														group_id = groups.get(str);
												}
												else{
														errors +=" Error manager group, group name does not exist "+str;
												}
												String[] str2Arr = new String[1];
												if(str2.indexOf(",") > 0){
														str2Arr = str2.split(",");
												}
												else{
														str2Arr[0] = str2;
												}
												for(String stt:str2Arr){
														if(emps.containsKey(stt)){
																approver_id = emps.get(stt);
																approver_ids.add(approver_id);
														}
														else{
																Employee empp = new Employee();
																empp.setUsername(stt);
																back = empp.doSelect();
																if(back.isEmpty()){
																		approver_id = empp.getId();
																		approver_ids.add(approver_id);
																		emps.put(stt, empp.getId());
																}
																else{
																		errors +=" Error group approver not set properly "+str2;
																}
														}
												}
												String[] str3Arr = new String[1];
												if(str3.indexOf(",") > 0){
														str3Arr = str3.split(",");
												}
												else{
														str3Arr[0] = str3;
												}
												for(String stt2:str3Arr){
														if(emps.containsKey(stt2)){
																payroll_id = emps.get(stt2);
																payroll_ids.add(payroll_id);
														}
														else{
																Employee empp = new Employee();
																empp.setUsername(stt2);
																back = empp.doSelect();
																if(back.isEmpty()){
																		payroll_id = empp.getId();
																		payroll_ids.add(payroll_id);
																		emps.put(stt2, empp.getId());
																}
																else{
																		errors +=" Error group payroll username not set properly "+stt2;
																}
														}
												}
												String[] str4Arr = new String[1];
												if(str4.indexOf(",") > 0){
														str4Arr = str4.split(",");
												}
												else{
														str4Arr[0] = str4;
												}
												for(String stt4:str4Arr){
														if(emps.containsKey(stt4)){
																reviewer_id = emps.get(stt4);
																reviewer_ids.add(reviewer_id);
														}
														else{
																Employee empp = new Employee();
																empp.setUsername(stt4);
																back = empp.doSelect();
																if(back.isEmpty()){
																		reviewer_id = empp.getId();
																		reviewer_ids.add(reviewer_id);
																		emps.put(stt4, empp.getId());
																}
																else{
																		errors +=" Error group reviewer username not set properly "+str4;
																}
														}														
												}
												if(errors.isEmpty()){
														GroupManager gm = null;
														
														for(String ap_id:approver_ids){
																gm = new GroupManager();
																gm.setGroup_id(group_id);
																gm.setEmployee_id(ap_id);
																gm.setWf_node_id(approve_role_id);
																gm.setStart_date("07/01/2017");
																str = gm.doSave();
																if(!str.isEmpty()) errors += str;
														}
														for(String rol_id:payroll_ids){
																gm = new GroupManager();
																gm.setGroup_id(group_id);
																gm.setEmployee_id(rol_id);
																gm.setWf_node_id(payroll_role_id);
																gm.setStart_date("07/01/2017");
																str = gm.doSave();
																if(!str.isEmpty()) errors += str;
														}
														if(reviewer_ids != null && reviewer_ids.size() > 0){
																for(String rev_id:reviewer_ids){
																		gm = new GroupManager();
																		gm.setGroup_id(group_id);
																		gm.setEmployee_id(rev_id);
																		gm.setWf_node_id(review_role_id);
																		gm.setStart_date("07/01/2017");
																		str = gm.doSave();
																		if(!str.isEmpty()) errors += str;
																}
														}
												}
										}
								}
								else if(str != null &&
												str.equals("Job")){
										str = record.get(1); // username to get id
										str2 = record.get(2); // group
										str3 = record.get(3); // job title
										str4 = record.get(4); // clock_in only
										boolean clockIn = false;
										Profile profile = null;
										BenefitGroup bgroup = null;
										Employee emp = null;
										String position_id="", dept_id="", dept_name="",
												salary_grp_id="", emp_id="", job_name="",
												emp_group_id="";
										int weekly_regular_hours=40,
												comp_time_weekly_hours=40;
										double comp_time_factor = 1.0,
												holiday_comp_factor=1.0;
										if(str3 != null)
												job_name = str3;
										if(str4 != null && !str4.isEmpty()){
												clockIn = true;
										}
										if(str != null && !str.isEmpty()){
												if(emps.containsKey(str)){
														emp_id = emps.get(str);
														Employee one = new Employee(emp_id);
														back = one.doSelect();
														if(back.isEmpty()){
																emp = one;
														}
												}
												else{
														errors +=" Employee username not found "+str;
												}
										}
										else{
												errors +=" Employee username not found "+str;
										}
										// employee number
										if(emp != null){
												String emp_num = emp.getEmployee_number();
												if(profMap.containsKey(emp_num)){
														profile = profMap.get(emp_num);
														bgroup = profile.getBenefitGroup();
												}
												else{
														errors +=" Employee number not found "+str2;
												}
										}
										else{
												errors +=" Employee not found ";
										}
										if(!errors.isEmpty()){
												System.err.println("Errors "+errors);
												break;
										}
										else{
												if(emp.hasDepartment()){
														Department dd = emp.getDepartment();
														if(dd != null){
																dept_id = dd.getId();
																dept_name = dd.getName();
														}
												}
												if(str2 != null && !str2.isEmpty()){
														if(!dept_name.isEmpty() && deptGroups.containsKey(dept_name)){
																List<Group> grps = deptGroups.get(dept_name);
																for(Group one:grps){
																		if(one.getName().equals(str2)){
																				emp_group_id = one.getId();
																				break;
																		}
																}
														}
														else{
																errors += " employee group name not set properly: "+str3;
														}
												}
												if(job_name != null && !job_name.isEmpty()){
														if(positions.containsKey(job_name)){
																position_id = positions.get(job_name);
														}
														else{
																Position position = new Position(job_name,
																																 job_name,
																																 job_name);
																str = position.doSave();
																if(str.isEmpty()){
																		position_id = position.getId();
																		positions.put(position.getName(), position_id);
																}
																else{
																		errors += str;
																}
														}
												}
												if(profile != null && bgroup != null){
														System.err.println("using profile");
														System.err.println(" prof "+profile);
														str = bgroup.getSalary_group_name();
														if(salaryGrps.containsKey(str)){
																salary_grp_id = salaryGrps.get(str);
														}
														else {
																errors += " salary group name "+str+" not found ";
														}
														weekly_regular_hours = (int)profile.getStWeeklyHrs();
														comp_time_weekly_hours = (int)profile.getCompTimeAfter();
														comp_time_factor = profile.getCompTimeMultiple();
														holiday_comp_factor = profile.getHolidayTimeMultiple();
												}
												if(!errors.isEmpty()){
														System.err.println("Errors "+errors);
														break;
												}												
												JobTask job = new JobTask();
												job.setPosition_id(position_id);
												job.setSalary_group_id(salary_grp_id);
												job.setEmployee_id(emp.getId());
												job.setEffective_date("07/01/2017");
												job.setGroup_id(emp_group_id);
												if(!emp.hasJobs()){ // first job will be primary
														job.setPrimary_flag(true);
												}
												if(clockIn)
														job.setClock_time_required(true);
												job.setWeekly_regular_hours(weekly_regular_hours);
												job.setComp_time_weekly_hours(comp_time_weekly_hours);
												job.setComp_time_factor(comp_time_factor);
												job.setHoliday_comp_factor(holiday_comp_factor);
												str = job.doSave();
												if(!str.isEmpty()){
														errors += str;
												}
												if(!errors.isEmpty()){
														System.err.println("Errors "+errors);
														break;
												}
										}
								}
								if(!errors.isEmpty()){
										System.err.println("Errors "+errors);
										break;
								}
						}
				}
				catch(Exception ex){
						System.err.println(ex);
						errors += ex;
				}
				return errors;
		}		
		//		
		public List<BenefitGroup> getBenefitGroups(){
				if(benefitGroups == null){
						BenefitGroupList tl = new BenefitGroupList();
						String back = tl.find();
						if(back.isEmpty()){
								List<BenefitGroup> ones = tl.getBenefitGroups();
								if(ones != null && ones.size() > 0){
										benefitGroups = ones;
								}
						}
				}
				return benefitGroups;
		}				
		void prepareProfiles(){
				//
				// knowing the dept_refs we can find list of employees profiles
				//
				if(!dept_refs.isEmpty() && profMap.isEmpty()){
						getBenefitGroups();
						List<Profile> profiles = null;
						ProfileList pfl =
								new ProfileList(Helper.getToday(),
																dept_refs,
																benefitGroups);
						String back = pfl.find();
						if(back.isEmpty()){
								List<Profile> ones = pfl.getProfiles();
								if(ones != null && ones.size() > 0){
										profiles = ones;
								}
						}
						if(profiles != null){
								for(Profile one:profiles){
										profMap.put(one.getEmployee_number(), one);
								}
						}
				}
		}
		
		void prepareMaps(){
				groups.put("Directors","7"); // we already have this for directors
				DepartmentList dl = new DepartmentList();
				String back = dl.find();
				if(back.isEmpty()){
						List<Department> dds = dl.getDepartments();
						for(Department one:dds){
								depts.put(one.getName(), one);
								if(one.hasGroups()){
										deptGroups.put(one.getName(), one.getGroups());
								}
						}
				}
				TypeList pl = new TypeList("positions");
				back = pl.find();
				if(back.isEmpty()){
						List<Type> dds = pl.getTypes();
						for(Type one:dds){
								positions.put(one.getName(), one.getId());
						}
				}
				SalaryGroupList sgl = new SalaryGroupList();
				back = sgl.find();
				if(back.isEmpty()){
						List<SalaryGroup> dds = sgl.getSalaryGroups();
						for(SalaryGroup one:dds){
								salaryGrps.put(one.getName(), one.getId());
						}
				}
		}

}
