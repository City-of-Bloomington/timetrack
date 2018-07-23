package in.bloomington.timer;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.*;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;  
import in.bloomington.timer.list.*;
import in.bloomington.timer.bean.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ImportEmployeeAction extends TopAction{

		static final long serialVersionUID = 1150L;	
		static Logger logger = LogManager.getLogger(ImportEmployeeAction.class);
		//
		List<Employee> employees = null;
		String department_id = "", dept_name="", group_name="", effective_date="";
		String importEmployeesTitle = "Most recent imports";
		Department department = null;
		List<Department> departments = null;
		List<Group> groups = null;
		public String execute(){
				String ret = SUCCESS;
				String back = doPrepare();
				if(!back.equals("")){
						try{
								HttpServletResponse res = ServletActionContext.getResponse();
								String str = url+"Login";
								res.sendRedirect(str);
								return super.execute();
						}catch(Exception ex){
								System.err.println(ex);
						}	
				}
				clearAll();
				if(action.equals("Import")){
						if(department_id.equals("") && dept_name.equals("")){
								back = "Department is required";
						}
						else{
								getDepartment();								
								EmpList el = new EmpList(envBean);
								if(department != null){
										el.setDept_name(department.getLdap_name());
								}
								else
										el.setDept_name(dept_name);
								if(!group_name.equals("")){
										el.setGroup_name(group_name);
								}
								back = el.find();
								if(back.equals("")){
										List<Employee> ones = el.getEmps();
										if(ones != null && ones.size() > 0){
												employees = ones;
										}
								}
								if(employees != null){
										for(Employee one:employees){
												/*
												String str = one.doSave();
												if(!str.equals("")){
														System.err.println(" error "+str);
														back += str;
												}
												*/
										}
										if(back.equals("")){
												addActionMessage("Imported successfully");
												addMessage("Imported successfully");
										}
								}
						}
						if(!back.equals("")){
								addError(back);
								addActionError(back);
								return ret;
						}
				}				
				return ret;
		}

		public String getImportEmployeesTitle(){
				return importEmployeesTitle;
		}

		public void setAction2(String val){
				if(val != null && !val.equals(""))		
						action = val;
		}
		public void setDepartment_id(String val){
				if(val != null && !val.equals("-1"))		
						department_id = val;
		}
		public void setGroup_name(String val){
				if(val != null && !val.equals("-1"))		
						group_name = val;
		}
		public void setDept_name(String val){
				if(val != null && !val.equals(""))		
						dept_name = val;
		}
		public Department getDepartment(){
				if(department == null && !department_id.equals("")){
						Department dp = new Department(department_id);
						String back = dp.doSelect();
						if(back.equals("")){
								department = dp;
						}
				}
				return department;
		}
		public String getDepartment_id(){
				return department_id;
		}
		public String getGroup_name(){
				return group_name;
		}
		public void setEffective_date(String val){
				if(val != null && !val.equals(""))		
						effective_date = val;
		}
		public List<Group> getGroups(){
				if(!department_id.equals("")){
						GroupList gl = new GroupList();
						gl.setActiveOnly();
						gl.setDepartment_id(department_id);
						String back = gl.find();
						if(back.equals("")){
								groups = gl.getGroups();
						}
				}
				return groups;
		}
		public boolean hasGroups(){
				getGroups();
				return groups != null && groups.size() > 0;
		}
		public List<Department> getDepartments(){
				if(departments == null){
						DepartmentList tl = new DepartmentList();
						tl.setActiveOnly();
						String back = tl.find();
						if(back.equals("")){
								List<Department> ones = tl.getDepartments();
								if(ones != null && ones.size() > 0){
								departments = ones;
								}
						}
				}
				return departments;
		}
}





































