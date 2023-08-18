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

public class HandleCurrentEmployees{

    boolean debug = false;
    static final long serialVersionUID = 53L;
    static Logger logger = LogManager.getLogger(HandleCurrentEmployees.class);
    EnvBean envBean = null;
    String date="";
    Hashtable<String, Employee> empTable = null;
    public HandleCurrentEmployees(EnvBean val){
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
	List<Employee> dbUsers = null;
	List<Employee> ldapUsers = null;
	Hashtable<String, Employee> empTable = null;
	String inactiveSet = "", inactiveLog = "";
	EmployeeList ul = new EmployeeList();
	ul.setActiveOnly();
	ul.setExclude_name("Admin");// exlude admin user
	String back = ul.find();
	if(back.isEmpty()){
	    List<Employee> ones = ul.getEmployees();
	    if(ones != null && ones.size() > 0){
		dbUsers = ones;
	    }
	}
	else{
	    msg = back;
	    status = "Failure";
	    errors = "Error get emps from DB "+msg;
	}
	if(msg.isEmpty()){
	    EmpList el = new EmpList(envBean);
	    back = el.find();
	    if(back.isEmpty()){
		ldapUsers = el.getEmps();
		empTable = new Hashtable<>();
		for(Employee one:ldapUsers){
		    if(!one.getFirst_name().startsWith("*")){
			empTable.put(one.getUsername(), one);
		    }
		}
	    }
	    else{
		msg = back;
		status = "Failure";
		errors = "Error get emps from ldap "+msg;								
	    }
	}
	if(msg.isEmpty()){
	    if(dbUsers != null){
		/*
		  System.err.println(" DB users ");
		  for(User one:dbUsers){
		  System.err.println(one.getInfo());
		  }
		*/
	    }
	    /*
	      if(ldapUsers != null){
	      int jj=1;
	      System.err.println(" Ldap users ");
	      for(Employee one:ldapUsers){
	      System.err.println((jj++)+" "+one.getInfo());
	      }
	      }
	    */
	    for(Employee one:dbUsers){
		if(!empTable.containsKey(one.getUsername())){
		    if(!inactiveSet.isEmpty()){
			inactiveSet += ",";
			inactiveLog += ", ";
		    }
		    inactiveSet += one.getId();
		    inactiveLog += one.getId()+" "+one.getFull_name();
		}
	    }
	    System.err.println("the following emps will change to inactive "+inactiveLog);
	    /*
	      if(!inactiveSet.isEmpty()){
	      Employee user = new Employee();
	      msg = user.updateInactiveStatus(inactiveSet);
	      if(!msg.isEmpty()){
	      status = "Failure";
	      errors =" error updating emps status "+msg;
	      }
	      }
	    */
	}
	EmployeesLog ll = new EmployeesLog(inactiveLog, status, errors);
	msg = ll.doSave();
	return msg;
    }

}
