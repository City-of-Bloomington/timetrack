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

public class ReviewAction extends TopAction{

    static final long serialVersionUID = 1150L;	
    static Logger logger = LogManager.getLogger(ReviewAction.class);
    //
    List<Group> groups = null;
    List<GroupManager> managers = null;
    String groupsTitle = "Manage Group(s)";
    String pay_period_id="", group_id="",
	department_id="", // needed to forward to timewarp
	document_id=""; //for one only
		
    String workflow_id = "";
    Group group = null;
    PayPeriod currentPayPeriod=null, previousPayPeriod=null,
	nextPayPeriod=null, payPeriod = null;
    List<Document> documents = null;
    List<PayPeriod> payPeriods = null;
    List<Employee> nonDocEmps = null;
    List<Employee> notSubmittedEmps = null;
    List<Employee> notApprovedEmps = null;
    List<Employee> noDocNorSubmitEmps = null;		
    boolean notSubmitAndApproveFlag = true;		
    String[] document_ids = null;

    public String execute(){
	String ret = SUCCESS;
	String back = doPrepare();
	return ret;
    }

    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
	    action = val;
    }
    public void setPay_period_id(String val){
	if(val != null && !val.equals("-1"))		
	    pay_period_id = val;
    }
    public void setGroup_id(String val){
	if(val != null && !val.isEmpty())		
	    group_id = val;
    }
    public void setDocument_ids(String[] vals){
	if(vals != null)		
	    document_ids = vals;
    }
    public void setDocument_id(String val){
	if(val != null)		
	    document_id = val;
    }				
    public String getGroup_id(){
	if(group_id.isEmpty()){
	    getGroup();
	}
	return group_id;
    }
    public boolean isGroupManager(){
	getManagers();
	return managers != null && managers.size() > 0;
    }
    public List<GroupManager> getManagers(){
	if(employee_id.isEmpty()){
	    getEmployee_id();
	}
	GroupManagerList gml = new GroupManagerList(employee_id);
	getPay_period_id();
	gml.setPay_period_id(pay_period_id);
	gml.setReviewersOnly();
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
    public PayPeriod getPreviousPayPeriod(){
	//
	if(previousPayPeriod == null){
	    if(pay_period_id.isEmpty())
		getPay_period_id();
	    PayPeriodList ppl = new PayPeriodList();
	    ppl.setPreviousTo(pay_period_id); // relative to currently used
	    String back = ppl.find();
	    if(back.isEmpty()){
		List<PayPeriod> ones = ppl.getPeriods();
		if(ones != null && ones.size() > 0){
		    previousPayPeriod = ones.get(0);
		}
	    }
	}
	return previousPayPeriod;
    }
    public PayPeriod getNextPayPeriod(){
	//
	if(nextPayPeriod == null){
	    if(pay_period_id.isEmpty())
		getPay_period_id();						
	    PayPeriodList ppl = new PayPeriodList();
	    ppl.setNextTo(pay_period_id); 						
	    String back = ppl.find();
	    if(back.isEmpty()){
		List<PayPeriod> ones = ppl.getPeriods();
		if(ones != null && ones.size() > 0){
		    nextPayPeriod = ones.get(0);
		}
	    }
	}
	return nextPayPeriod;
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
    public String getPay_period_id(){
	if(pay_period_id.isEmpty()){
	    getCurrentPayPeriod();
	}
	return pay_period_id;
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
		
    public boolean hasDocuments(){
	getDocuments();
	return documents != null && documents.size() > 0;
    }
    public List<Document> getDocuments(){
	if(documents == null){
	    if(hasGroups()){
		if(pay_period_id.isEmpty()){
		    getPay_period_id(); // current
		}
		DocumentList dl = new DocumentList();
		dl.setPay_period_id(pay_period_id);
		if(!group_id.isEmpty() && !group_id.equals("all")){
		    dl.setGroup_id(group_id);
		}
		else if(groups != null && groups.size() > 0){
		    for(Group one:groups){
			dl.setGroup_id(one.getId());										
		    }
		}
		String back = dl.find();
		if(back.isEmpty()){
		    List<Document> ones = dl.getDocuments();
		    if(ones != null && ones.size() > 0){
			documents = ones;
		    }
		}
	    }
	}
	return documents;
    }
    public List<Employee> getNonDocEmps(){
	if(nonDocEmps == null){
	    EmployeeList empl = new EmployeeList();
	    if(!group_id.isEmpty() && !group_id.equals("all")){
		empl.setGroup_id(group_id);
	    }
	    else if(groups != null && groups.size() > 0){
		for(Group one:groups){
		    empl.setGroup_id(one.getId());										
		}
	    }
	    if(pay_period_id.isEmpty()){
		getPay_period_id(); // current
	    }
	    empl.setNoDocumentForPayPeriodId(pay_period_id);
	    empl.setActiveOnly();
	    String back = empl.find();
	    if(back.isEmpty()){
		List<Employee> ones = empl.getEmployees();
		if(ones != null && ones.size() > 0){
		    nonDocEmps = ones;
		    if(noDocNorSubmitEmps == null){
			noDocNorSubmitEmps = ones;
		    }
		    else{
			for(Employee one:ones){
			    noDocNorSubmitEmps.add(one);
			}
		    }
		}
	    }
	}
	return nonDocEmps;
    }
    public boolean hasNonDocEmps(){
	getNonDocEmps();
	return nonDocEmps != null && nonDocEmps.size() > 0; 
    }
    public boolean hasNotSubmittedEmps(){
	findNotSubmittedAndNotApprovedEmps();
	return notSubmittedEmps != null && notSubmittedEmps.size() > 0;

    }
    public boolean hasNoDocNorSubmitEmps(){
	return hasNotSubmittedEmps() || hasNonDocEmps();
    }
    public List<Employee> getNoDocNorSubmitEmps(){
	return noDocNorSubmitEmps;
    }
    public boolean hasNotApprovedEmps(){
	findNotSubmittedAndNotApprovedEmps();				
	return notApprovedEmps != null && notApprovedEmps.size() > 0;
    }
    public List<Employee> getNotSubmittedEmps(){
	return notSubmittedEmps;
    }
    public List<Employee> getNotApprovedEmps(){
	return notApprovedEmps;
    }
    void findNotSubmittedAndNotApprovedEmps(){
	if(notSubmitAndApproveFlag){
	    notSubmitAndApproveFlag = false; // to turn off
	    getNonDocEmps();
	    if(hasDocuments()){
		for(Document one:documents){
		    if(one.canBeApproved()){
			if(notApprovedEmps == null)
			    notApprovedEmps = new ArrayList<>();
			notApprovedEmps.add(one.getEmployee());
		    }
		    else if(one.isApproved() || one.isProcessed()){
			continue;
		    }
		    else{
			Employee emp = one.getEmployee();
			if(nonDocEmps != null && nonDocEmps.contains(emp)){
			    continue;
			}
			else{
			    if(notSubmittedEmps == null)
				notSubmittedEmps = new ArrayList<>();
			    notSubmittedEmps.add(emp);
			    if(noDocNorSubmitEmps == null)
				noDocNorSubmitEmps = new ArrayList<>();
			    noDocNorSubmitEmps.add(emp);
			}
		    }
		}
	    }
	}
    }
    public String getDepartment_id(){
	if(department_id.isEmpty()){
	    findDepartment();
	}
	return department_id;
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
}





































