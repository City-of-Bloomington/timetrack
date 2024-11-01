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
    List<Group> groups = null;
    List<GroupManager> managers = null;
    String groupsTitle = "Manage Group(s)";
    String group_id="", pay_period_id="", leave_id="",
	department_id=""; 
    String selected_group_id = "";
    String[] statusVals = {"Approved","Denied"};
    PayPeriod payPeriod = null;
    Group group = null;
    Department department = null;
    List<LeaveRequest> leaves = null;
    LeaveRequest leave = null;
    LeaveReview review = null;
    Employee reviewer = null;
    List<LeaveReview> reviews = null;
    String display_ids = ""; // next to show
    String displayed_ids = ""; // previous showing
    int leaves_total_number = 0;
    public String execute(){
	String ret = SUCCESS;
	String back = doPrepare("leaveReview.action");
	if(action.equals("Submit")){
	    getUser();
	    System.err.println(" user "+user);
	    review.setReviewed_by(user.getId());
	    if(activeMail){ 
		// if(true){
		review.setActiveMail();
		review.setMail_host(mail_host);
		review.setUser(user); // current manager
	    }
	    back = review.doSave();
	    if(!back.isEmpty()){
		addError(back);
		displayed_ids = "";
	    }
	    else{
		addMessage("Saved Successfully");
	    }	    
	}
	else if(!leave_id.isEmpty()){
	    getReview();
	    review.setLeave_id(leave_id);
	}
	else{
	    findLeaveRequests();
	    if(leaves == null || leaves.size() == 0){
		addMessage("There is no leave requests"); 
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
	getUser();
	GroupManagerList gml = new GroupManagerList(user.getId());
	getPay_period_id();
	gml.setPay_period_id(pay_period_id);
	gml.setApproversOnly();
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
	return managers;
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
    // diplayed from current showing to
    // ignore in next showing
    public void setDisplayed_ids(String val){
	if(val != null)
	    displayed_ids = val;
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
	// to do
	getGroups();
	if(hasGroups()){
	    if(groups != null && groups.size() > 0){
		// we need only one
		Group one = groups.get(0);
		department_id = one.getDepartment_id();
	    }
	}
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
	if(!displayed_ids.isEmpty())
	    rrl.setIdsToIgnore(displayed_ids);
	// rrl.setMaxLimit(3); // review 3 at a time
	String back = rrl.find();
	if(back.isEmpty()){
	    List<LeaveRequest> ones = rrl.getRequests();
	    if(ones != null){
		leaves_total_number = ones.size();
		if(leaves_total_number <=5){
		    leaves = ones;
		}
		else{
		    leaves = new ArrayList<>();
		    for(int jj=0;jj<5;jj++){
			leaves.add(ones.get(jj));
		    }
		}
	    }
	}
    }
    public String getDisplay_ids(){
	if(leaves == null){
	    findLeaveRequests();
	}
	if(leaves != null){
	    for(LeaveRequest one:leaves){
		if(!display_ids.isEmpty()) display_ids +=",";
		display_ids += one.getId();
	    }
	}
	return display_ids;
    }
    public List<LeaveReview> getReviews(){
	return reviews;
    }
    public boolean hasReviews(){
	if(reviews == null)
	    findReviews();
	return reviews != null && reviews.size() > 0;
	
    }
    void findReviews(){
	LeaveReviewList lrl = new LeaveReviewList();
	lrl.setReviewed_by(user.getId());
	String back = lrl.find();
	if(back.isEmpty()){
	    List<LeaveReview> ones = lrl.getReviews();
	    if(ones != null && ones.size() > 0){
		reviews = ones;
	    }
	}
    }

}





































