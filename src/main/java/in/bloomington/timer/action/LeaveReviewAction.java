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

public class LeaveReviewAction extends TopAction{

    static final long serialVersionUID = 1150L;	
    static Logger logger = LogManager.getLogger(LeaveReviewAction.class);
    //
    static final int max_requests = 10;
    List<Group> groups = null;
    List<GroupManager> managers = null;
    String groupsTitle = "Manage Group(s)";
    String group_id="", pay_period_id="", leave_id="",
	department_id=""; 
    String selected_group_id = "";
    // leave filter
    String filter_emp_id="", date_from="", date_to="";
    // review filter
    String rev_filter_emp_id="", rev_date_from="", rev_date_to="";    
    String[] statusVals = {"Approved","Denied"};
    PayPeriod payPeriod = null;
    Group group = null;
    Department department = null;
    List<LeaveRequest> leaves = null;
    LeaveRequest leave = null;
    LeaveReview review = null;
    Employee reviewer = null;
    List<LeaveReview> reviews = null;
    List<Employee> employees = null;//for filter
    List<Employee> rev_employees = null;//for history filter
    int leaves_total_number = 0;
    public String execute(){
	String ret = SUCCESS;
	doPrepare();
	if(!has_session){
	    ret = LOGIN;
	    return ret;
	}
	getUser();
	resetEmployee();
	if(employee == null || !employee.canLeaveReview()){
	    try{
		String str = "timeDetails.action";
		HttpServletResponse res = ServletActionContext.getResponse();
		res.sendRedirect(str);
		return super.execute();
	    }catch(Exception ex){
		System.err.println(ex);
	    }	
	}	
	String back = doPrepare("leaveReview.action");
	if(action.equals("Submit")){
	    review.setReviewed_by(user.getId());
	    if(activeMail){ 
		review.setActiveMail();
		review.setMail_host(mail_host);
		review.setUser(user); // current manager
	    }
	    back = review.doSave();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		addMessage("Saved Successfully");
	    }	    
	}
	else if(action.startsWith("Refresh")){
	    findLeaveRequests();
	    if(leaves == null || leaves.size() == 0){
		addMessage("There are no leave requests"); 
	    }
	}
	else if(!leave_id.isEmpty()){
	    getReview();
	    review.setLeave_id(leave_id);
	}
	else{
	    findLeaveRequests();
	    if(leaves == null || leaves.size() == 0){
		addMessage("There are no leave requests"); 
	    }
	}
	return ret;
    }
    public LeaveRequest getLeave(){
	if(!leave_id.isEmpty()){
	    LeaveRequest one = new LeaveRequest(leave_id);
	    String back = one.doSelect();
	    if(back.isEmpty()){
		leave = one;
	    }
	}
	return leave;
    }
    public Employee getReviewer(){
	if(reviewer == null){
	    getUser();
	    reviewer = user;
	}
	return reviewer;
    }
    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
	    action = val;
    }
    public void setGroup_id(String val){
	if(val != null && !val.isEmpty()){		
	    group_id = val;
	    selected_group_id = group_id;
	}
    }
    public void setLeave_id(String val){
	if(val != null && !val.isEmpty()){		
	   leave_id = val;
	}
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
    public void setRev_date_to(String val){
	if(val != null && !val.isEmpty()){		
	   rev_date_to = val;
	}
    }
    public void setRev_date_from(String val){
	if(val != null && !val.isEmpty()){		
	   rev_date_from = val;
	}
    }
    public String getRev_date_from(){
	return rev_date_from;
    }
    public String getRev_date_to(){
	return rev_date_to;
    }    
    public void setDate_to(String val){
	if(val != null && !val.isEmpty()){		
	   date_to = val;
	}
    }        
    public String getLeave_id(){
	return leave_id = "";
    }
    public LeaveReview getReview(){
	if(review == null){
	    review = new LeaveReview();
	    if(!leave_id.isEmpty())
		review.setLeave_id(leave_id);
	    if(!id.isEmpty()){
		review.setId(id);
	    }
	}
	return review;
    }
    
    public void setReview(LeaveReview val){
	if(val != null)
	    review = val;
    }
    public String getGroup_id(){
	if(!selected_group_id.isEmpty())
	    group_id = selected_group_id;
	if(group_id.isEmpty()){
	    getGroup();
	}
	return group_id;
    }
    public String[] getStatusVals(){
	return statusVals;
    }
    public boolean isGroupManager(){
	getManagers();
	return managers != null && managers.size() > 0;
    }
    public Integer getLeavesTotalNumber(){
	if(leaves_total_number == 0)
	    findLeaveRequests();
	return leaves_total_number;
	
    }
    public List<GroupManager> getManagers(){
	if(managers == null)
	    findManagers();
	return managers;
    }
	
    private void findManagers(){
	if(managers == null){
	    getUser();
	    if(user != null){
		GroupManagerList gml = new GroupManagerList(user.getId());
		getPay_period_id();
		gml.setPay_period_id(pay_period_id);
		// gml.setApproversOnly();
		gml.setLeaveReviewOnly();
		String back = gml.find();
		if(back.isEmpty()){
		    List<GroupManager> ones = gml.getManagers();
		    if(ones != null && ones.size() > 0){
			managers = ones;
			for(GroupManager one:managers){
			    Group one2 = one.getGroup();
			    if(one2 != null){
				if(groups == null)
				    groups = new ArrayList<>();
				if(!groups.contains(one2))
				    groups.add(one2);
			    }
			}
		    }
		}
	    }
	}
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
		findPayPeriod();
	    }
	}
	return payPeriod;
    }
    public String getPay_period_id(){
	if(pay_period_id.isEmpty() && payPeriod == null){
	    getPayPeriod();
	}
	pay_period_id = payPeriod.getId();
	return pay_period_id;
    }
    void findPayPeriod(){
	//
	if(payPeriod == null){
	    PayPeriodList ppl = new PayPeriodList();
	    ppl.currentOnly();
	    String back = ppl.find();
	    if(back.isEmpty()){
		List<PayPeriod> ones = ppl.getPeriods();
		if(ones != null && ones.size() > 0){
		    payPeriod = ones.get(0);
		    if(pay_period_id.isEmpty()){
			pay_period_id = payPeriod.getId();
		    }
		}
	    }
	}
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
	    else if(groups.size() > 0){ // one or more
		group = groups.get(0); // only one group is shown
		group_id = group.getId();
	    }
	}
	return group;
    }
    public boolean hasMoreThanOneGroup(){
	return isGroupManager() && groups != null && groups.size() > 1;
    }
    public boolean hasGroups(){
	return isGroupManager() && groups != null && groups.size() > 0;
				
    }
    public List<Group> getGroups(){
	return groups;
    }
    public void setCheck_all(boolean val){
	// will do nothing
    }
    public String getDepartment_id(){
	if(department_id.isEmpty()){
	    findDepartment();
	}
	return department_id;
    }
    public void setDepartment_id(String val){
	if(val != null && !val.equals("-1")){
	    department_id = val;
	}
    }
    public void setFilter_emp_id(String val){
	if(val != null && !val.equals("-1")){
	    filter_emp_id = val;
	}
    }
    public void setRev_filter_emp_id(String val){
	if(val != null && !val.equals("-1")){
	    rev_filter_emp_id = val;
	}
    }    
    public String getRev_filter_emp_id(){
	if(rev_filter_emp_id.isEmpty())
	    return "-1";
	return rev_filter_emp_id;
    }
    
	
    public Department getDepartment(){
	if(department == null){
	    if(department_id.isEmpty()){
		findDepartment();
	    }
	    if(!department_id.isEmpty()){
		Department dd = new Department(department_id);
		String back = dd.doSelect();
		if(back.isEmpty()){
		    department = dd;
		}
	    }
	}
	return department;
    }
    void findDepartment(){
	getGroups();
	if(hasGroups()){
	    if(groups != null && groups.size() > 0){
		Group one = groups.get(0);
		department_id = one.getDepartment_id();
	    }
	}
    }
    public boolean hasEmployees(){
	getEmployees();
	return employees != null && employees.size() > 0;
    }
    public List<Employee> getEmployees(){
	if(employees == null)
	    findLeaveRequests();
	if(employees != null && employees.size() > 1){
	    Collections.sort(employees);
	}
	return employees;
	
    }
    public boolean hasLeaveRequest(){
	return !leave_id.isEmpty();
    }
    public boolean hasLeaves(){
	getLeaves();
	return leaves != null && leaves.size() > 0;
    }
    public List<LeaveRequest> getLeaves(){
	if(leaves == null){
	    findLeaveRequests();
	}
	return leaves;
    }
    void findLeaveRequests(){
	if(group_id.isEmpty()){
	    getGroup();
	}
	LeaveRequestList rrl = new LeaveRequestList();
	rrl.setNotReviewed();
	if(pay_period_id.isEmpty()){
	    getPayPeriod();
	}
	if(!pay_period_id.isEmpty())
	    rrl.setPay_period_id(pay_period_id);
	if(!date_from.isEmpty()){
	    rrl.setDate_from_ff(date_from);
	}
	if(!date_to.isEmpty()){
	    rrl.setDate_to_ff(date_to);
	}
	if(!filter_emp_id.isEmpty()){
	    rrl.setFilter_emp_id(filter_emp_id);
	}
	if(hasMoreThanOneGroup()){
	    String group_ids = "";
	    for(Group gg:groups){
		if(!group_ids.isEmpty()) group_ids +=",";
		group_ids +=gg.getId();
	    }
	    rrl.setGroup_ids(group_ids);
	}
	else if(!group_id.isEmpty()){
	    rrl.setGroup_id(group_id);
	}
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
		if(leaves_total_number <= max_requests){
		    leaves = ones;
		}
		else{
		    leaves = new ArrayList<>();
		    for(int jj=0;jj<max_requests;jj++){
			leaves.add(ones.get(jj));
		    }
		}
	    }
	}
    }
    
    public List<LeaveReview> getReviews(){
	return reviews;
    }
    public boolean hasReviews(){
	if(reviews == null)
	    findReviews();
	return reviews != null && reviews.size() > 0;
	
    }
    //
    // we need a filter if we have more than 3 reviews
    //
    public boolean isRevFilterNeeded(){
	if(hasReviews()){
	    return reviews != null && reviews.size() > 3;
	}
	return false;
    }
    public boolean hasRevEmployees(){
	return rev_employees != null && rev_employees.size() > 0;
    }
    public List<Employee> getRev_employees(){
	return rev_employees;
    }
    void findReviews(){
	LeaveReviewList lrl = new LeaveReviewList();
	lrl.setReviewed_by(user.getId());
	if(!rev_date_from.isEmpty()){
	    lrl.setDate_from_ff(rev_date_from);
	}
	if(!rev_date_to.isEmpty()){
	    lrl.setDate_to_ff(rev_date_to);
	}
	if(!rev_filter_emp_id.isEmpty()){
	    lrl.setEmployee_id(rev_filter_emp_id);
	}	
	String back = lrl.find();
	if(back.isEmpty()){
	    List<LeaveReview> ones = lrl.getReviews();
	    if(ones != null && ones.size() > 0){
		reviews = ones;
	    }
	    if(reviews != null){
		for(LeaveReview one:reviews){
		    Employee emp = one.getLeave().getEmployee();
		    if(emp != null){
			if(rev_employees == null)
			    rev_employees = new ArrayList<>();
			if(!rev_employees.contains(emp))
			    rev_employees.add(emp);
		    }
		}
		
	    }
	}
    }

}





































