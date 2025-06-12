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


public class PayrollProcessAction extends TopAction{

    static final long serialVersionUID = 2260L;	
    static Logger logger = LogManager.getLogger(PayrollProcessAction.class);
    //
    List<Group> groups = null;
    List<GroupManager> managers = null;
    String groupsTitle = "Manage Group(s)";
    String pay_period_id="", group_id="";
    String department_id=""; // needed for timewarp link
    String workflow_id = "", document_id=""; 
    PayPeriod currentPayPeriod=null, previousPayPeriod=null,
	nextPayPeriod = null, payPeriod = null;
    String selected_group_id = "";
    Group group = null;
    List<Document> documents = null;
    List<Document> notSubmittedDocs = null;		
    List<PayPeriod> payPeriods = null;
    List<Employee> nonDocEmps = null;
    List<Employee> notApprovedEmps = null;
    List<Employee> noDocNorSubmitEmps = null;
    List<Employee> notProcessedEmps = null;
    List<Document> notApprovedDocs = null;    
    boolean notSubmitAndApproveFlag = true;
    Map<String, List<Document>> notApprovedMap = null;
    String[] document_ids = null;

    public String execute(){
	String ret = SUCCESS;
	String back = doPrepare("payrollProcess.action");
	resetEmployee();
	if(action.startsWith("PayrollOne")){
	    if(document_id != null && user != null){
		TimeAction one =
		    new TimeAction(CommonInc.default_payroll_process_workflow_id,
				   document_id,
				   user.getId());
		back = one.doSave();
		if(!back.isEmpty()){
		    if(!back.isEmpty()){
			addError(back);
		    }
		}
		if(!hasErrors()){
		    addMessage("Payroll Process Approved Successfully");
		}
	    }
	}
	else if(action.startsWith("Payroll")){
	    if(document_ids != null){
		for(String doc_id:document_ids){
		    TimeAction one =
			new TimeAction(CommonInc.default_payroll_process_workflow_id,
				       doc_id,
				       user.getId());
		    back = one.doSave();
		    if(!back.isEmpty()){
			if(!back.isEmpty()){
			    addError(back);
			}
		    }
		}
		if(!hasErrors()){
		    addMessage("Payroll Process Approved Successfully");
		}
	    }
	}
	selected_group_id = obtainGroupIdFromSession();				
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
	selected_group_id = group_id;
	addGroupIdToSession(selected_group_id);
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
	if(!selected_group_id.isEmpty())
	    group_id = selected_group_id;
	if(group_id.isEmpty()){
	    getGroup();
	}
	return group_id;
    }
    public boolean hasMoreThanOneGroup(){
	return isGroupManager() && groups != null && groups.size() > 1;
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
	    else if(groups.size() > 0){
		group = groups.get(0);
		group_id = group.getId();
	    }
	}
	return group;
    }		
    public boolean isGroupManager(){
	getManagers();
	return managers != null && managers.size() > 0;
    }
    public List<GroupManager> getManagers(){
		
	GroupManagerList gml = new GroupManagerList(user.getId());
	getPay_period_id();
	gml.setPay_period_id(pay_period_id);
	gml.setProcessorsOnly();
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
    public GroupManager findGroupApprover(String gid){
	GroupManager gmanager = null;
	GroupManagerList gml = new GroupManagerList();
	getPay_period_id();
	gml.setPay_period_id(pay_period_id);
	gml.setGroup_id(gid);
	gml.setApproversOnly();
	String back = gml.find();
	if(back.isEmpty()){
	    List<GroupManager> ones = gml.getManagers();
	    if(ones != null && ones.size() > 0){
		gmanager = ones.get(0); // first
	    }
	}
	return gmanager;
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
		    }
		}
	    }
	}
	return currentPayPeriod;
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
		getPay_period_id();
	    }
	}
	return payPeriod;
    }				
    public boolean hasGroups(){
	return isGroupManager() && groups != null && groups.size() > 0;
    }
    public List<Group> getGroups(){
	return groups;
    }
    public String getPay_period_id(){
	if(pay_period_id.isEmpty()){
	    PayPeriodList ppl = new PayPeriodList();
	    ppl.setApproveSuitable();
	    String back = ppl.find();
	    if(back.isEmpty()){
		List<PayPeriod> ones = ppl.getPeriods();
		if(ones != null && ones.size() > 0){
		    payPeriod = ones.get(0);
		    pay_period_id = payPeriod.getId();
		}
	    }						
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
    public PayPeriod getPreviousPayPeriod(){
	//
	if(previousPayPeriod == null){
	    if(pay_period_id.isEmpty())
		getPay_period_id();						
	    PayPeriodList ppl = new PayPeriodList();
	    ppl.setPreviousTo(pay_period_id); 
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
    public boolean hasNotApprovedEmps(){
	findNotSubmittedAndNotApprovedEmps();				
	return notApprovedEmps != null && notApprovedEmps.size() > 0;
    }
    public boolean hasNotProcessedEmps(){
	findNotSubmittedAndNotApprovedEmps();				
	return notProcessedEmps != null && notProcessedEmps.size() > 0;
    }		
    public boolean needAction(){
	return hasNotApprovedMap() || hasNotSubmittedDocs() || hasNonDocEmps();
    }
    public List<Employee> getNotApprovedEmps(){
	return notApprovedEmps;
    }
    void findNotSubmittedAndNotApprovedEmps(){
	if(notApprovedMap != null) return;
	getNonDocEmps();
	if(hasDocuments()){
	    for(Document one:documents){
		if(one.canBeApproved()){
		    if(notApprovedEmps == null)
			notApprovedEmps = new ArrayList<>();
		    if(notApprovedDocs == null){
			notApprovedDocs = new ArrayList<>();
		    }
		    if(!notApprovedEmps.contains(one.getEmployee()))
			notApprovedEmps.add(one.getEmployee());
		    if(!notApprovedDocs.contains(one))
			notApprovedDocs.add(one);
		}
		else if(one.canBeProcessed()){
		    if(notProcessedEmps == null)
			notProcessedEmps = new ArrayList<>();
		    if(!notProcessedEmps.contains(one.getEmployee()))
			notProcessedEmps.add(one.getEmployee());
		}
		else if(one.isApproved() ||
			one.isSubmitted() ||
			one.isProcessed()){
		    continue;
		}
		else{
		    Employee emp = one.getEmployee();
		    if(nonDocEmps != null && nonDocEmps.contains(emp)){
			continue;
		    }
		    else{
			if(notSubmittedDocs == null)
			    notSubmittedDocs = new ArrayList<>();
			if(!notSubmittedDocs.contains(one))
			    notSubmittedDocs.add(one);
		    }
		}
	    }
	}
    }    

  public boolean hasNotApprovedMap(){
	findNotSubmittedAndNotApprovedEmps();
	if(notApprovedDocs != null && notApprovedDocs.size() > 0){
	    if(notApprovedMap == null){
		notApprovedMap = new HashMap<>();
	    }
	    for(Document one:notApprovedDocs){
		GroupManager gmanager = findGroupApprover(one.getGroup().getId());
		if(gmanager != null){
		    Employee ee = gmanager.getEmployee();
		    String full_name = ee.getFull_name();
		    if(!notApprovedMap.containsKey(full_name)){
			List<Document> docls = new ArrayList<>();
			docls.add(one);
			notApprovedMap.put(full_name,docls);
		    }
		    else{
			List<Document> docLs = notApprovedMap.get(full_name);
			if(!docLs.contains(one)){
			    docLs.add(one);
			    notApprovedMap.put(full_name,docLs);
			}
		    }
		}
	    }
	}
	return notApprovedDocs != null && notApprovedDocs.size() > 0;
    }    
    public boolean hasNoDocNorSubmitEmps(){
	return hasNotSubmittedDocs() || hasNonDocEmps();
    }
    public List<Employee> getNoDocNorSubmitEmps(){
	return noDocNorSubmitEmps;
    }
    public boolean hasNotSubmittedDocs(){
	findNotSubmittedAndNotApprovedEmps();
	return notSubmittedDocs != null && notSubmittedDocs.size() > 0;
    }
    public List<Document> getNotSubmittedDocs(){
	return notSubmittedDocs;
    }				
    public void setCheck_all(boolean val){
	// will do nothing
    }
    public Map<String, List<Document>> getNotApproved(){
	return notApprovedMap;
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
		// we need only one
		Group one = groups.get(0);
		department_id = one.getDepartment_id();
	    }
	}
    }
		
}





































