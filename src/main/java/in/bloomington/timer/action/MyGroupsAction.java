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

public class MyGroupsAction extends TopAction{

    static final long serialVersionUID = 1150L;	
    static Logger logger = LogManager.getLogger(MyGroupsAction.class);
    //

    String groupsTitle = "Manage Group(s)";
    String pay_period_id="", group_id="";
    String selected_group_id = "", department_id="";
    
    PayPeriod currentPayPeriod=null, previousPayPeriod=null,
	nextPayPeriod=null, payPeriod = null;
    List<Group> groups = null;    
    Group group = null;
    Department department = null;
    List<PayPeriod> payPeriods = null;
    String employee_ids = ""; 
    public String execute(){
	String ret = SUCCESS;
	String back = doPrepare("mygroups.action");
	resetEmployee();
	selected_group_id = obtainGroupIdFromSession();
	return ret;
    }

    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
	    action = val;
    }

    public void setGroup_id(String val){
	if(val != null && !val.isEmpty() && !val.equals("all")){		
	    group_id = val;
	    selected_group_id = group_id;
	    addGroupIdToSession(selected_group_id);
	}
    }
    public void setGroup(Group val){
	if(val != null){		
	    group = val;
	    if(!group_id.isEmpty())
		group.doSelect();
	}
    }    
    public String getGroup_id(){
	if(!selected_group_id.isEmpty())
	    group_id = selected_group_id;
	if(group_id.isEmpty()){
	    getGroup();
	    group_id = group.getId();
	}
	return group_id;
    }
    public boolean isGroupManager(){
	findManagedGroups();
	return groups != null && groups.size() > 0;
    }
    void findManagedGroups(){
	if(groups != null ) return;
	GroupManagerList gml = new GroupManagerList(user.getId());
	getPayPeriod();
	gml.setPay_period_id(pay_period_id);
	gml.setEmployee_id(user.getId());
	gml.setApproversOnly();
	String back = gml.find();
	if(back.isEmpty()){
	    List<GroupManager> ones = gml.getManagers();
	    if(ones != null && ones.size() > 0){
		for(GroupManager one:ones){
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
    public PayPeriod getPayPeriod(){
	//
	if(payPeriod == null){
	    if(!pay_period_id.isEmpty()){
		PayPeriod one = new PayPeriod(pay_period_id);
		String back = one.doSelect();
		if(back.isEmpty()){
		    payPeriod = one;
		    pay_period_id = payPeriod.getId();
		}
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
    public Group getGroup(){
	getGroups();
	if(group == null && !group_id.isEmpty()){
	    Group one = new Group(group_id);
	    String back = one.doSelect();
	    if(back.isEmpty()){
		group = one;
	    }
	}
	else if(group != null && !group_id.isEmpty()){
	    if(!group.getId().equals(group_id)){
		group.setId(group_id);
		group.doSelect();
	    }
	}
	else if(groups.size() > 0){ // one or more
	    group = groups.get(0); // only one group is shown
	    group_id = group.getId();
	}
	return group;
    }
    public boolean hasMoreThanOneGroup(){
	isGroupManager();
	return groups != null && groups.size() > 0;
    }
    public boolean hasGroups(){
	boolean ret = isGroupManager();
	return ret;
				
    }
    public List<Group> getGroups(){
	return groups;
    }




}





































