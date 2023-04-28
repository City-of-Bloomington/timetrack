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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DataEntryAction extends TopAction{

    static final long serialVersionUID = 1150L;	
    static Logger logger = LogManager.getLogger(DataEntryAction.class);
    //
    List<Group> groups = null;
    List<GroupManager> managers = null;
    String groupsTitle = "Manage Group(s)";
    String pay_period_id="", group_id="";
    String workflow_id = "";
    PayPeriod currentPayPeriod=null, previousPayPeriod=null,
	nextPayPeriod=null, payPeriod = null;		
    List<Document> documents = null;
    List<PayPeriod> payPeriods = null;
    List<Employee> nonDocEmps = null;
    List<String> pages = null;
    int page_size = 20;
    int page_number = 1;
    int total_records = 0;
    Group group = null;
    String[] document_ids = null;
    public String execute(){
	String ret = SUCCESS;
	String back = doPrepare();
	resetEmployee();
	if(!back.isEmpty()){
	    addError(back);
	}
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
	gml.setTimeMaintainerOnly();
	if(!group_id.isEmpty() && !group_id.equals("all")){
	    gml.setGroup_id(group_id);
	}
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
    public boolean hasGroups(){
	if(groups != null){
	    if(group_id.isEmpty()){
		group = groups.get(0);
		group_id = group.getId();
	    }
	}
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
    public boolean hasMoreThanOneGroup(){
	return isGroupManager() && groups != null && groups.size() > 1;
    }		
    public String getPay_period_id(){
	if(pay_period_id.isEmpty()){
	    PayPeriodList ppl = new PayPeriodList();
	    ppl.currentOnly();
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
    public PayPeriod getPayPeriod(){
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
	    //dl.setPageSize(page_size);
	    //dl.setPageNumber(page_number);
	    String back = dl.find();
	    if(back.isEmpty()){
		List<Document> ones = dl.getDocuments();
		if(ones != null && ones.size() > 0){
		    documents = ones;
		}
		total_records = dl.getTotalRecords();
	    }
	}
	return documents;
    }
    public List<Employee> getNonDocEmps(){
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
	    }
	}
	return nonDocEmps;
    }
    public boolean hasNonDocEmps(){
	getNonDocEmps();
	return nonDocEmps != null && nonDocEmps.size() > 0; 
    }
    public void setPageNumber(Integer val){
	if(val != null)
	    page_number = val;
    }
    public void setPageSize(Integer val){
	if(val != null)
	    page_size = val;
    }
    public boolean hasPages(){
	getPages();
	return pages != null && pages.size() > 0;
    }
    public List<String> getPages(){
	if(pages == null){
	    int page_count = 0;
	    if(total_records > page_size){
		page_count = total_records / page_size;
		if(total_records % page_size > 0) page_count++;
	    }
	    // if one page, we do not bother
	    if(page_count > 1){
		pages = new ArrayList<>();
		for(int i=1;i<=page_count;i++){
		    pages.add(""+i);
		}
	    }
	}
	return pages;
    }
}





































