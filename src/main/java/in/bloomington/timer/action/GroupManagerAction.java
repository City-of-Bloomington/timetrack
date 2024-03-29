package in.bloomington.timer.action;

/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.*;
import java.io.*;
import java.text.*;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;  
import in.bloomington.timer.list.*;
import in.bloomington.timer.bean.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GroupManagerAction extends TopAction{

    static final long serialVersionUID = 1800L;	
    static Logger logger = LogManager.getLogger(GroupManagerAction.class);
    //
    List<Node> nodes = null;
    String group_id="";
    String department_id="";
    List<Employee> employees = null;
    List<GroupManager> groupManagers = null;
    List<PayPeriod> payPeriods = null;
    Group group = null;
    GroupManager groupManager = null;
    String groupManagersTitle = "Managers of this group";

    public String execute(){
	String ret = SUCCESS;
	String back = canProceed("groupManager.action");
	if(!back.isEmpty()){
	    return back;
	}
	if(action.equals("Save")){
	    back = groupManager.doSave();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		addMessage("Saved Successfully");			
	    }
	}				
	else if(action.startsWith("Save")){
	    back = groupManager.doUpdate();
	    if(!back.isEmpty()){
		addActionError(back);
		addError(back);
	    }
	    else{
		addMessage("Saved Successfully");			
	    }
	}
	else if(action.startsWith("Delete")){
	    if(!id.isEmpty()){
		getGroupManager();								
		back = groupManager.doSelect();
		if(back.isEmpty()){
		    group_id = groupManager.getGroup_id();
		    back = groupManager.doDelete();
		    if(!back.isEmpty()){
			addActionError(back);
			addError(back);
		    }
		    else{
			try{
			    HttpServletResponse res = ServletActionContext.getResponse();
			    String str = "group.action?id="+group_id;
			    res.sendRedirect(str);
			    return super.execute();
			}catch(Exception ex){
			    System.err.println(ex);
			}												
		    }
		}
	    }
	}				
	else{		
	    getGroupManager();
	    if(!id.isEmpty()){
		back = groupManager.doSelect();
		if(!back.isEmpty()){
		    addActionError(back);
		    addError(back);
		}
	    }
	    else{
		ret=INPUT;
	    }
	}
	return ret;
    }
    public GroupManager getGroupManager(){ 
	if(groupManager == null){
	    groupManager = new GroupManager(id);
	    if(!group_id.isEmpty())
		groupManager.setGroup_id(group_id);
	}		
	return groupManager;
    }

    public void setGroupManager(GroupManager val){
	if(val != null){
	    groupManager = val;
	}
    }

    public String getGroupManagersTitle(){
	return groupManagersTitle;
    }
    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
	    action = val;
    }

    public void setGroup_id(String val){
	if(val != null && !val.isEmpty() && !val.equals("-1"))		
	    group_id = val;
    }
    public String getGroup_id(){
	if(group_id.isEmpty()){
	    if(group != null){
		group_id = group.getId();
	    }
	    else if(groupManager != null){
		group_id = groupManager.getGroup_id();
	    }
	}
	return group_id;
    }
    public Group getGroup(){
	getGroup_id();
	if(!group_id.isEmpty()){
	    Group one = new Group(group_id);
	    String back = one.doSelect();
	    if(back.isEmpty()){
		group = one;
		department_id = group.getDepartment_id();
	    }
	}
	return group;
    }
    public List<Node> getNodes(){
	if(nodes == null){
	    NodeList nl = new NodeList();
	    nl.setManagers_only();
	    String back = nl.find();
	    if(back.isEmpty()){
		List<Node> ones = nl.getNodes();
		if(ones != null)
		    nodes = ones;
	    }
	}
	return nodes;
    }
    public boolean hasNodes(){
	getNodes();
	return nodes != null;
    }		
    public boolean hasEmployees(){
	getEmployees();
	return employees != null;
    }
    public List<Employee> getEmployees(){
	//
	// given a group we will find employees in the related department
	//
	if(employees == null){
	    if(department_id.isEmpty()){
		getGroup();
	    }
	    EmployeeList empl = new EmployeeList();
	    if(!department_id.isEmpty()){
		empl.setDepartment_id(department_id);
	    }
	    empl.includeAllDirectors();
	    empl.setActiveOnly();
	    empl.setNotTerminated();
	    //
	    // managers can not manage the group they are in
	    //
	    empl.setExclude_group_id(group_id);
	    String back = empl.find();
	    if(back.isEmpty()){
		List<Employee> ones = empl.getEmployees();
		if(ones != null && ones.size() > 0){
		    employees = ones;
		}
	    }
	}
	return employees;
    }
    public boolean hasGroupManagers(){
	getGroupManagers();
	return groupManagers != null;
    }
    public List<GroupManager> getGroupManagers(){
	//
	getGroup_id();
	GroupManagerList gml = new GroupManagerList();
	gml.setGroup_id(group_id);
	String back = gml.find();
	if(back.isEmpty()){
	    List<GroupManager> ones = gml.getManagers();
	    if(ones != null && ones.size() > 0){
		groupManagers = ones;
	    }
	}
	return groupManagers;
    }
    public List<PayPeriod> getPayPeriods(){
	if(payPeriods == null){
	    PayPeriodList tl = new PayPeriodList();
	    tl.setOnePeriodAheadOnly();
	    tl.setLimit("5");
	    String back = tl.find();
	    if(back.isEmpty()){
		List<PayPeriod> ones = tl.getPeriods();
		Collections.reverse(ones);
		if(ones != null && ones.size() > 0){
		    payPeriods = ones;
		}
	    }
	}
	return payPeriods;
    }				
}





































