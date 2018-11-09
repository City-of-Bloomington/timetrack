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

public class HandleEmployeesUpdate{

		boolean debug = false;
		static final long serialVersionUID = 53L;
		static Logger logger = LogManager.getLogger(HandleEmployeesUpdate.class);
		EnvBean envBean = null;
		String date="";
		Hashtable<String, Employee> empTable = null, empUsrTable=null;
    public HandleEmployeesUpdate(EnvBean val){
				if(val != null)
						envBean = val;
    }
    //
    // setters
    //
    public void setDate(String val){
				if(val != null){		
						date = val;
				}
    }
		//
		// find all the employees who have initiated document time but
		// can not submit for approval
		//
   public String process(){
			 String msg = "", status="Success", errors="";
				List<Employee> dbEmps = null;
				List<Employee> ldapEmps = null;
				Hashtable<String, Employee> empTable = null;
				String inactiveSet = "", inactiveLog = "";
				EmployeeList ul = new EmployeeList();
				ul.setActiveOnly();
				ul.setExclude_name("Admin");// exlude admin emp
				String back = ul.find();
				if(back.equals("")){
						List<Employee> ones = ul.getEmployees();
						if(ones != null && ones.size() > 0){
								dbEmps = ones;
						}
				}
				else{
						msg = back;
						status = "Failure";
						errors = "Error get emps from DB "+msg;
				}
				if(msg.equals("")){
						EmpList el = new EmpList(envBean);
						back = el.find();
						if(back.equals("")){
								ldapEmps = el.getEmps();
								empTable = new Hashtable<>();
								empUsrTable = new Hashtable<>();
								for(Employee one:ldapEmps){
										// System.err.println(one.getInfo());
										empUsrTable.put(one.getUsername(), one);
										if(one.hasNoEmployeeNumber()) continue;
										empTable.put(one.getEmployee_number(), one);
								}
						}
						else{
								msg = back;
								status = "Failure";
								errors = "Error get emps from ldap "+msg;								
						}
				}
				if(msg.equals("")){
						/*						
						if(dbEmps != null){

								System.err.println(" DB emps ");
								for(Emp one:dbEmps){
										System.err.println(one.getInfo());
								}
						}
						if(ldapEmps != null){
								int jj=1;
								System.err.println(" Ldap emps ");
								for(Employee one:ldapEmps){
										System.err.println((jj++)+" "+one.getInfo());
								}
						}
						*/
						// changes
						for(Employee one:dbEmps){
								if(!empTable.containsKey(one.getEmployee_number()) &&
									 !empUsrTable.containsKey(one.getUsername())){
										if(!inactiveSet.equals("")){
												inactiveSet += ",";
												inactiveLog += ", ";
										}
										inactiveSet += one.getId();
										inactiveLog += one.getId()+" "+one.getFull_name();
								}
								if(empTable.containsKey(one.getEmployee_number())){
										Employee ldapEmp = empTable.get(one.getEmployee_number());
										if(!one.isSameEntity(ldapEmp)){
												msg += one.doUpdateFromLdap(ldapEmp);
										}
								}
						}
						
						System.err.println("the following emps will change to inactive "+inactiveLog);
						//
						// new employees
						// this handled by ImportEmployee code
						/*
						empTable = new Hashtable<>();
						for(Employee one:dbEmps){
								empTable.put(one.getUsername(), one);
						}
						int jj=1;
						for(Employee one:ldapEmps){
								if(one.getFirst_name().startsWith("*")) continue;
								if(one.hasNoEmployeeNumber()) continue;
								if(!empTable.containsKey(one.getUsername())){
										//
										// normally this should be a new employee
										//
										msg += one.doSave();
										System.err.println("new "+(jj++)+" "+one.getInfo());
								}
						}
						*/
						//
						System.err.println(" The following id list of inactive employees "+inactiveSet);
						if(!inactiveSet.equals("")){
								Employee emp = new Employee();
								msg = emp.updateInactiveStatus(inactiveSet);
								if(!msg.equals("")){
										status = "Failure";
										errors =" error updating emps status "+msg;
								}
						}
				}
				EmployeesLog ll = new EmployeesLog(inactiveLog, status, errors);
				msg = ll.doSave();
				return msg;
		}

}
