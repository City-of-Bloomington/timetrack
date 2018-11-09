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

import org.apache.log4j.Logger;

public class EmployeesImport{

		boolean debug = true;
		static Logger logger = Logger.getLogger(EmployeesImport.class);
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
		EmployeesImport(){

		}
		EmployeesImport(EnvBean val){
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
										if(str != null && !str.equals("")){
												if(!depts.containsKey(str)){
														Department dept = new Department();
														dept.setName(str);
														str = record.get(2);
														dept.setLdap_name(str);
														str = record.get(3);
														dept.setRef_id(str);
														if(!dept_refs.equals("")){
																dept_refs +=",";
														}
														dept_refs += str;
														str = dept.doSave();
														if(str.equals("")){
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
														if(str == null || str.equals(""))
																str = record.get(3);
														if(!dept_refs.equals("")){
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
										if(str != null && !str.equals("") &&
											 str2 != null && !str2.equals("")){
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
																		if(str.equals("")){
																				ones.add(gg);
																				deptGroups.put(str2, ones);
																				groups.put(gg.getName(), gg.getId());
																		}
																}
																else{
																		errors += str;
																}
														} // no groups in this department
														else{
																List<Group> ones = new ArrayList<>();
																Group gg = new Group();
																gg.setName(str);
																gg.setDescription(str);
																gg.setDepartment_id(dept_id);
																str = gg.doSave();
																if(str.equals("")){
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
										str = record.get(1);
										if(str != null && !str.equals("")){

												if(profMap.containsKey(str)){
														profile = profMap.get(str);
														bgroup = profile.getBenefitGroup();
												}
												Employee emp = new Employee();
												emp.setRole("Employee");
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
												if(str != null && !str.equals("")){
														emp.setUsername(str);
														back = emp.doSelect();
														if(!back.equals("")){ // not exist
																EmpList empl = new EmpList(bean, str, true);
																back = empl.simpleFind();
																if(!back.equals("")){
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
												if(str != null && !str.equals("")){
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
												if(job_name.equals("")){
														errors += " job title not found ";
												}
												if(job_name != null && !job_name.equals("")){
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
																if(str.equals("")){
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
												if(str != null && !str.equals("")){
														if(!dept_name.equals("") && deptGroups.containsKey(dept_name)){
																List<Group> grps = deptGroups.get(dept_name);
																for(Group one:grps){
																		if(one.getName().equals(str)){
																				emp_group_id = one.getId();
																				break;
																		}
																}
														}
														if(!emp_group_id.equals("")){
																emp.setGroup_id(emp_group_id);
														}
														else{
																errors += " employee group name not set properly: "+str;
														}
												}
												if(errors.equals("")){
														if(emp_exist){
																str = emp.doUpdateDeptGroupInfo();
														}
														else{
																str = emp.doSave();
														}
														if(str.equals("")){
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
												str = job.doSave();
												if(!str.equals("")){
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
										if(str.equals("") || str2.equals("") || str3.equals("")){
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
																if(back.equals("")){
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
																if(back.equals("")){
																		payroll_id = empp.getId();
																		payroll_ids.add(payroll_id);
																		emps.put(stt2, empp.getId());
																}														
																errors +=" Error group payroll username not set properly "+str3;
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
																if(back.equals("")){
																		reviewer_id = empp.getId();
																		reviewer_ids.add(reviewer_id);
																		emps.put(stt4, empp.getId());
																}
																else{
																		errors +=" Error group reviewer username not set properly "+str4;
																}
														}														
												}
												if(errors.equals("")){
														GroupManager gm = null;
														
														for(String ap_id:approver_ids){
																gm = new GroupManager();
																gm.setGroup_id(group_id);
																gm.setEmployee_id(ap_id);
																gm.setWf_node_id(approve_role_id);
																gm.setStart_date("07/01/2017");
																str = gm.doSave();
																if(!str.equals("")) errors += str;
														}
														for(String rol_id:payroll_ids){
																gm = new GroupManager();
																gm.setGroup_id(group_id);
																gm.setEmployee_id(rol_id);
																gm.setWf_node_id(payroll_role_id);
																gm.setStart_date("07/01/2017");
																str = gm.doSave();
																if(!str.equals("")) errors += str;
														}
														if(reviewer_ids != null && reviewer_ids.size() > 0){
																for(String rev_id:reviewer_ids){
																		gm = new GroupManager();
																		gm.setGroup_id(group_id);
																		gm.setEmployee_id(rev_id);
																		gm.setWf_node_id(review_role_id);
																		gm.setStart_date("07/01/2017");
																		str = gm.doSave();
																		if(!str.equals("")) errors += str;
																}
														}
												}
										}
								}
								if(!errors.equals("")){
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
		public List<BenefitGroup> getBenefitGroups(){
				if(benefitGroups == null){
						BenefitGroupList tl = new BenefitGroupList();
						String back = tl.find();
						if(back.equals("")){
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
				if(!dept_refs.equals("") && profMap.isEmpty()){
						getBenefitGroups();
						List<Profile> profiles = null;
						ProfileList pfl =
								new ProfileList(Helper.getToday(),
																dept_refs,
																benefitGroups);
						String back = pfl.find();
						if(back.equals("")){
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
				if(back.equals("")){
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
				if(back.equals("")){
						List<Type> dds = pl.getTypes();
						for(Type one:dds){
								positions.put(one.getName(), one.getId());
						}
				}
				SalaryGroupList sgl = new SalaryGroupList();
				back = sgl.find();
				if(back.equals("")){
						List<SalaryGroup> dds = sgl.getSalaryGroups();
						for(SalaryGroup one:dds){
								salaryGrps.put(one.getName(), one.getId());
						}
				}
		}

}
