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

public class LeaveReportAction extends TopAction{

    static final long serialVersionUID = 1150L;	
    static Logger logger = LogManager.getLogger(LeaveReportAction.class);
    //
    static final int max_requests = 100;
    List<Group> groups = null;
    String leavesTitle = "Leave History";
    String group_id="", pay_period_id="",
	dept_id=""; 
    // leave filter
    String emp_id="", date_from="", date_to="";
    //
    // String[] statusVals = {"Approved","Denied"};
    PayPeriod payPeriod = null, currentPayPeriod = null;
    Group group = null;
    Department department = null;
    List<LeaveRequest> leaves = null;
    LeaveRequest leave = null;
    List<Employee> employees = null;//for filter
    List<Department> departments = null;
    List<PayPeriod> payPeriods = null;    
    int leaves_total_number = 0;
    boolean allDepts = false;
    public String execute(){
	String ret = SUCCESS;
	getUser();
	resetEmployee();
	String back = doPrepare("leaveReport.action");
	getEmployee();
	if(employee.isAdmin()){
	    allDepts = true;
	}
	if(dept_id.isEmpty()){
	    dept_id = employee.getDepartment_id();	
	}
	if(!action.isEmpty()){
	    if(employee.isAdmin()){
		allDepts = true;
	    }
	    if(dept_id.isEmpty()){
		dept_id = employee.getDepartment_id();	
	    }   
	    findLeaveRequests();
	}
	else{
	    getCurrentPayPeriod();
	    findLeaveRequests();
	}
	if(leaves != null && leaves.size() > 0){
	    addMessage("Found "+leaves.size()+" approved leave requests");
	}
	else{
	    addMessage("No approved leave requests found");
	}
	return ret;
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
    public void setPay_period_id(String val){
	if(val != null && !val.equals("-1")){		
	    pay_period_id = val;
	}
    }
    public String getPay_period_id(){
	if(pay_period_id.isEmpty()){
	    return "-1";
	}
	return pay_period_id;
    }
    public void setDate_from(String val){
	if(val != null && !val.isEmpty()){		
	   date_from = val;
	}
    }
    public String getDate_from(){
	return date_from;
    }
    public String getDate_to(){
	return date_to;
    }    
    public void setDate_to(String val){
	if(val != null && !val.isEmpty()){		
	   date_to = val;
	}
    }        
    public String getGroup_id(){
	if(group_id.isEmpty()){
	    return "-1";
	}
	return group_id;
    }
    public Integer getLeavesTotalNumber(){
	return leaves_total_number;
	
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
    }
    public void setEmp_id(String val){
	if(val != null && !val.equals("-1")){
	    emp_id = val;
	}
    }
    public List<PayPeriod> getPayPeriods(){
	if(payPeriods == null){
	    PayPeriodList tl = new PayPeriodList();
	    tl.setTwoPeriodsAheadOnly();
	    String back = tl.find();
	    if(back.isEmpty()){
		List<PayPeriod> ones = tl.getPeriods();
		if(ones != null && ones.size() > 0){
		    payPeriods = ones;
		}
	    }
	}
	return payPeriods;
    }
    public PayPeriod getPayPeriod(){
	//
	if(payPeriod == null){
	    if(!pay_period_id.isEmpty()){
		PayPeriod one = new PayPeriod(pay_period_id);
		String back = one.doSelect();
		if(back.isEmpty())
		    payPeriod = one;
	    }
	    else {
		getCurrentPayPeriod();
	    }
	}
	return payPeriod;
    }		
    public PayPeriod getCurrentPayPeriod(){
	//
	if(currentPayPeriod == null){
	    PayPeriodList ppl = new PayPeriodList();
	    ppl.currentOnly();
	    String back = ppl.find();
	    if(back.isEmpty()){
		List<PayPeriod> ones = ppl.getPeriods();
		if(ones != null && ones.size() > 0){
		    currentPayPeriod = ones.get(0);
		    if(pay_period_id.isEmpty()){
			pay_period_id = currentPayPeriod.getId();
			payPeriod = currentPayPeriod;
		    }
		}
	    }
	}
	return currentPayPeriod;
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
    public boolean hasLeaves(){
	getLeaves();
	return leaves != null && leaves.size() > 0;
    }
    public List<LeaveRequest> getLeaves(){
	return leaves;
    }
    void findLeaveRequests(){
	if(group_id.isEmpty()){
	    getGroup();
	}
	LeaveRequestList rrl = new LeaveRequestList();
	rrl.setApprovedOnly();
	if(!dept_id.isEmpty()){
	    rrl.setDepartment_id(dept_id);
	}
	if(!date_from.isEmpty()){
	    rrl.setDate_from_ff(date_from);
	    pay_period_id = "";
	}
	if(!date_to.isEmpty()){
	    rrl.setDate_to_ff(date_to);
	    pay_period_id = "";	    
	}
	if(!emp_id.isEmpty()){
	    rrl.setFilter_emp_id(emp_id);
	}
	if(!group_id.isEmpty()){
	    rrl.setGroup_id(group_id);
	}
	if(!pay_period_id.isEmpty()){
	    rrl.setPay_period_id(pay_period_id);
	}
	// rrl.setLimit("100");
	String back = rrl.find();
	if(back.isEmpty()){
	    List<LeaveRequest> ones = rrl.getRequests();
	    if(ones != null){
		if(employees == null)
		    employees = new ArrayList<>();
		for(LeaveRequest one:ones){
		    Employee empp = one.getEmployee();
		    if(!employees.contains(empp)){
			employees.add(empp);
		    }
		}
		leaves_total_number = ones.size();
		// if(leaves_total_number <= max_requests){
		leaves = ones;
		/**
		}
		else{
		    leaves = new ArrayList<>();
		    for(int jj=0;jj<max_requests;jj++){
			leaves.add(ones.get(jj));
		    }
		*/
	    }
	}
    }

}





































