package in.bloomington.timer;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 */

import java.sql.*;
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
import in.bloomington.timer.bean.Node;
import in.bloomington.timer.bean.JobTask;
import in.bloomington.timer.bean.Employee;
import in.bloomington.timer.bean.Department;
import in.bloomington.timer.bean.SalaryGroup;
import in.bloomington.timer.bean.GroupManager;
import in.bloomington.timer.list.TypeList;
import in.bloomington.timer.list.GroupList;
import in.bloomington.timer.list.NodeList;
import in.bloomington.timer.list.DepartmentList;
import in.bloomington.timer.list.SalaryGroupList;

import org.apache.log4j.Logger;

public class EmployeesImport{

		boolean debug = true;
		static Logger logger = Logger.getLogger(EmployeesImport.class);
		static final long serialVersionUID = 220L;			
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Map<String, String> emps = new HashMap<>(); // username, id
		Map<String, String> depts = new HashMap<>(); // name, id
		Map<String, String> groups = new HashMap<>(); // name, id
		Map<String, String> positions = new HashMap<>(); // name, id
		Map<String, String> salaryGrps = new HashMap<>(); // salary groups
		Map<String, String> roles = new HashMap<>(); // name, id
		String errors = "";
		EmployeesImport(){

		}
		public String doImport(String fileName){
				//
				// clean up
				/**
					delete from group_employees where id > 104;
					delete from department_employees where id > 103;
					delete from group_managers where id > 47;
					delete from jobs where id > 46;
					delete from employees where id > 116;
					delete from groups where id > 72;
					delete from positions where id > 49;
					
				 */
				
				prepareMaps();
				String qq = "insert into employees "+
						" (id,"+
						" username,"+
						" first_name,"+
						" last_name,"+
						" employee_number,"+
						
						" email,"+
						" role) "+
						" values(0,?,?,?,?, ?,?,'Employee') ";
				String qq2 = "insert into departments (id,name,description) values(0,?,?)";
				String qq3 = "insert into groups (id, name, description, department_id) values(0,?,?,?)";
				String qq4 = "insert into department_employees (id,employee_id,department_id,effective_date) values(0,?,?,'2017-07-01')";
				String qq5 = "insert into group_employees (id,group_id,employee_id,effective_date) values(0,?,?,'2017-07-01')";
				String qq6 = "insert into group_managers (id,group_id,employee_id,wf_node_id,start_date) values(0,?,?,?,?)"; 				
				// if we do not have it
				String qq7 = "insert into positions (id,name) values(0,?) ";
				String qq8 = "insert into jobs (id,position_id,salary_group_id,employee_id,effective_date,primary_flag,weekly_regular_hours,comp_time_weekly_hours,comp_time_factor,holiday_comp_factor) values(0,?,?,?,'2017-07-01', 'y',?,?,?,?)";

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
				Reader in = null;
				int jj=1; 
				try{
						in = new FileReader(fileName);
						//
						// if you want to define the header 
						// Iterable<CSVRecord> records = CSVFormat.RFC4180.withHeader("ID", "Last Name", "First Name").parse(in);
						//
						// header auto detection
						// Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
						Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(in);
						//
						String str="", str2="", str3="";						
						for (CSVRecord record : records) {
								str = record.get(0);
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
														str = dept.doSave();
														if(str.equals("")){
																depts.put(dept.getName(), dept.getId());
														}
														else{
																errors += str;
														}
												}
										}
								}
								else if(str != null && str.trim().equals("Group")){
										str = record.get(1); // group name
										str2 = record.get(2); // dept name
										if(str != null && !str.equals("") &&
											 str2 != null && !str2.equals("")){
												if(depts.containsKey(str2)){
														String dept_id = depts.get(str2);
														Group gg = new Group();
														gg.setName(str);
														gg.setDepartment_id(dept_id);
														str = gg.doSave();
														if(str.equals("")){
																groups.put(gg.getName(), gg.getId());
														}
														else{
																errors += str;
														}
												}
												else{
														errors += " department "+str+" not found ";
												}
										}
								}
								else if(str != null &&
												str.equals("Employee")){
										String position_id="", dept_id="",
												salary_grp_id="",
												emp_group_id="";
										int weekly_regular_hours=40,
												comp_time_weekly_hours=40;
										double comp_time_factor = 1.0,
												holiday_comp_factor=1.0;
										str = record.get(1);
										if(str != null && !str.equals("")){
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
												}
												str = record.get(4); // dept
												if(str != null && !str.equals("")){
														if(depts.containsKey(str)){
																dept_id = depts.get(str);
																emp.setDepartment_id(dept_id);
														}
														else {
																errors += " Employee department not set properly";
														}
												}
												str = record.get(5); // title
												if(str != null && !str.equals("")){
														if(str.indexOf("/") > 0){
																str = str.replaceAll("/","_");
														}
														if(positions.containsKey(str)){
																position_id = positions.get(str);
														}
														else{
																Type position = new Type();
																position.setName(str);
																position.setTable_name("positions");
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
												str = record.get(6);
												if(str != null && !str.equals("")){
														if(salaryGrps.containsKey(str)){
																salary_grp_id = salaryGrps.get(str);
																if(str.startsWith("Exempt")){
																		weekly_regular_hours = 40;
																		comp_time_weekly_hours = 45;
																		comp_time_factor=1.0;
																		holiday_comp_factor=1.0;
																}
																else if(str.startsWith("Non")){ // non
																		weekly_regular_hours = 40;
																		comp_time_weekly_hours = 40;
																		comp_time_factor=1.5;
																		holiday_comp_factor=1.5;
																}
																else if(str.startsWith("Temp")){
																		weekly_regular_hours = 20;
																		comp_time_weekly_hours = 0;
																		comp_time_factor=0;
																		holiday_comp_factor=0;
																}
																else if(str.startsWith("Union")){ // non
																		weekly_regular_hours = 40;
																		comp_time_weekly_hours = 40;
																		comp_time_factor=1.5;
																		holiday_comp_factor=2.0;
																}																
														}
														else{
																errors += " salary group: "+str+" does not exist";
														}
												}
												str = record.get(7);
												if(str != null && !str.equals("")){
														if(groups.containsKey(str)){
																emp_group_id = groups.get(str);
																emp.setGroup_id(emp_group_id);
														}
														else{
																errors += " employee group name not set properly: "+str;
														}
												}
												if(errors.equals("")){
														str = emp.doSave();
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
										str2 = record.get(2); // role
										str3 = record.get(3); // username
										String group_id="", role_id="", emp_id="";
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
												if(roles.containsKey(str2)){
														role_id = roles.get(str2);
												}
												else{
														errors +=" Error group manager role not set properly "+str2;
												}
												if(emps.containsKey(str3)){
														emp_id = emps.get(str3);
												}
												else{
														errors +=" Error group manager username not set properly "+str3;
												}
												if(errors.equals("")){
														GroupManager gm = new GroupManager();
														gm.setGroup_id(group_id);
														gm.setEmployee_id(emp_id);
														gm.setWf_node_id(role_id);
														gm.setStart_date("07/01/2017");
														str = gm.doSave();
														if(!str.equals("")) errors += str;
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
		void prepareMaps(){
				groups.put("Directors","7"); // we already have this for directors
				DepartmentList dl = new DepartmentList();
				String back = dl.find();
				if(back.equals("")){
						List<Department> dds = dl.getDepartments();
						for(Department one:dds){
								depts.put(one.getName(), one.getId());
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
				NodeList nl = new NodeList();
				back = nl.find();
				if(back.equals("")){
						List<Node> dds = nl.getNodes();
						for(Node one:dds){
								roles.put(one.getName(), one.getId());
						}
				}				
		}
		

}
