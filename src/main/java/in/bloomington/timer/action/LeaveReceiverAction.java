package in.bloomington.timer.action;
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
import java.util.Collections;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;  
import in.bloomington.timer.list.*;
import in.bloomington.timer.bean.*;
import in.bloomington.timer.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LeaveReceiverAction extends TopAction{

    static final long serialVersionUID = 1150L;	
    static Logger logger = LogManager.getLogger(LeaveReceiverAction.class);
    //
    List<Group> groups = null;
    String leavesTitle = "Approved Leave Email Receivers";
    String group_id="", emp_id="", id="",
	dept_id=""; 

    //
    // String[] statusVals = {"Approved","Denied"};
    Group group = null;
    Department department = null;
    List<LeaveReceiver> receivers = null;
    LeaveReceiver receiver = null;
    List<Employee> employees = null;//for filter
    List<Department> departments = null;
    boolean allDepts = false;
    public String execute(){
	String ret = SUCCESS;
	getUser();
	resetEmployee();
	String back = doPrepare("leaveReceivers.action");
	getEmployee();
	if(employee.isAdmin()){
	    allDepts = true;
	}
	if(!action.isEmpty()){
	    getReceiver();
	    if(action.equals("Next")){
		if(!dept_id.isEmpty()){
		    findGroups();
		    back = findEmployees();
		}
		else{
		    back = "You need to pick a Department";
		}
	    }	    
	    else if(action.equals("Add")){
		back = receiver.doSave();
		if(back.isEmpty()){
		    addMessage("Added Successfully");
		    id = receiver.getId();
		}
		back = findEmployees();			
	    }
	    else if(action.startsWith("Save")){
		back = receiver.doUpdate();
		if(back.isEmpty()){
		    addMessage("Updated Successfully");
		}
		back = findEmployees();		
	    }
	    else if(action.equals("Delete")){
		back = receiver.doDelete();
		if(back.isEmpty()){
		    addMessage("Deleted Successfully");
		    id="";
		}
	    }
	}
	else if(!id.isEmpty()){
	    getReceiver();
	    back = receiver.doSelect();
	    dept_id = receiver.getDepartment_id();
	    group_id = receiver.getGroup_id();
	    emp_id = receiver.getEmployee_id();
	    findGroups();
	    findEmployees();
	}
	if(!back.isEmpty()){
	    addError(back);
	}
	if(dept_id.isEmpty()){
	    if(employee.isAdmin()){
		allDepts = true;
	    }
	    else{
		dept_id = employee.getDepartment_id();
	    }
	}
	findReceivers();
	return ret;
    }
    void findDepartment(){
	getEmployee();
	dept_id = employee.getDepartment_id();
    }
    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
	    action = val;
    }
    public void setGroup_id(String val){
	if(val != null && !val.equals("-1")){		
	    group_id = val;
	}
    }
    public String getGroup_id(){
	if(group_id.isEmpty()){
	    return "-1";
	}
	return group_id;
    }
    public void setId(String val){
	if(val != null && !val.equals("-1")){		
	    id = val;
	}
    }
    public String getId(){
	return id;
    }    
    public List<Department> getDepartments(){
	if(departments == null){
	    DepartmentList tl = new DepartmentList();
	    tl.ignoreSpecialDepts();
	    String back = tl.find();
	    if(back.isEmpty()){
		List<Department> ones = tl.getDepartments();
		if(ones != null && ones.size() > 0){
		    departments = ones;
		}
	    }
	}
	return departments;
    }    
    public LeaveReceiver getReceiver(){
	if(receiver == null)
	    receiver = new LeaveReceiver();
	if(!id.isEmpty()){
	    receiver.setId(id);
	}
	if(!group_id.isEmpty()){
	    receiver.setGroup_id(group_id);
	}
	if(!emp_id.isEmpty()){
	    receiver.setEmployee_id(emp_id);
	}
	return receiver;
    }
    public Group getGroup(){
	getGroups();
	if(hasGroups()){
	    if(group == null && !group_id.isEmpty()){
		if(!group_id.equals("all")){
		    Group one = new Group(group_id);
		    String back = one.doSelect();
		    if(back.isEmpty()){
			group = one;
		    }
		}
	    }
	}
	return group;
    }
    public boolean hasGroups(){
	getGroups();
	boolean has_groups = groups != null && groups.size() > 0;
	return has_groups;
				
    }
    public boolean hasDepartment(){
	return !dept_id.isEmpty();
    }
    public List<Group> getGroups(){
	if(groups == null){
	    findGroups();
	}
	return groups;
    }
    void findGroups(){
	if(!dept_id.isEmpty()){
	    GroupList gl = new GroupList();
	    gl.setDepartment_id(dept_id);
	    String back = gl.find();
	    if(back.isEmpty()){
		List<Group> ones = gl.getGroups();
		if(ones != null && ones.size() > 0){
		    groups = ones;
		}
	    }
	}
    }
    public String getDept_id(){
	if(dept_id.isEmpty()){
	    return "-1";
	}
	return dept_id;
    }
    public boolean showAllDepts(){
	if(allDepts){
	    getDepartments();
	}
	return allDepts && departments != null && departments.size() > 0;
    }
    public void setDept_id(String val){
	if(val != null && !val.equals("-1")){
	    dept_id = val;
	}
	else{
	    dept_id = "";
	}
    }
    public void setEmp_id(String val){
	if(val != null && !val.equals("-1")){
	    emp_id = val;
	}
    }
    public Department getDepartment(){
	if(department == null){
	    if(!dept_id.isEmpty()){
		Department dd = new Department(dept_id);
		String back = dd.doSelect();
		if(back.isEmpty()){
		    department = dd;
		}
	    }
	}
	return department;
    }
    public boolean hasDepts(){
	if(allDepts){
	    getDepartments();
	}
	return departments.size() > 0;
    }
    public String getEmp_id(){
	if(emp_id.isEmpty()){
	    return "-1";
	}
	return emp_id;
	     
    }
    String findEmployees(){
	String back = "";
	if(employees == null){
	    if(dept_id.isEmpty()){
		findDepartment();
	    }
	    EmployeeList el = new EmployeeList();
	    el.setActiveOnly();
	    el.setNotTerminated();
	    el.setUsedTimeTrack();
	    el.setDepartment_id(dept_id);
	    el.setHasEmail(); // full time equiv
	    back = el.find();
	    if(back.isEmpty()){
		List<Employee> ones = el.getEmployees();
		if(ones != null && ones.size() > 0){
		    employees = ones;
		}
	    }
	}
	return back;
	    
    }
    public boolean hasReceivers(){
	return receivers != null && receivers.size() > 0;
	
    }
    public List<LeaveReceiver> getReceivers(){
	return receivers;
    }
    String findReceivers(){
	String back = "";
	if(receivers == null){
	    LeaveReceiverList el = new LeaveReceiverList();
	    back = el.find();
	    if(back.isEmpty()){
		List<LeaveReceiver> ones = el.getReceivers();
		if(ones != null && ones.size() > 0){
		    receivers = ones;
		}
	    }
	}
	return back;
	    
    }    
    public boolean hasEmployees(){
	getEmployees();
	return employees != null && employees.size() > 0;
    }
    public List<Employee> getEmployees(){
	if(employees != null && employees.size() > 1){
	    Collections.sort(employees);
	}
	return employees;
	
    }

}





































