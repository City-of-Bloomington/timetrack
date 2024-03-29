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
import in.bloomington.timer.util.CommonInc;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GroupAction extends TopAction{

    static final long serialVersionUID = 1800L;	
    static Logger logger = LogManager.getLogger(GroupAction.class);
    //
    Group group = null;
    List<Group> groups = null;
    List<GroupManager> groupManagers;
    List<HourCode> hourCodes = null;
    String groupsTitle = "Current groups";
    List<Type> departments = null;
    // for excess hour calculation types
    // either CE1.0, OT1.0 or (Donation) ignore
    // CommonInc.excess_hours_earn_types;
    String department_id="";
    public String execute(){
	String ret = SUCCESS;
	String back = canProceed("group.action");
	if(!back.isEmpty()){
	    return back;
	}
	if(action.equals("Save")){
	    back = group.doSave();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		id = group.getId();
		addMessage("Added Successfully");
	    }
	}				
	else if(action.startsWith("Save")){
	    back = group.doUpdate();
	    if(!back.isEmpty()){
		addError(back);								
	    }
	    else{
		id = group.getId();
		addMessage("Updated Successfully");
	    }
	}
	else if(action.startsWith("Edit")){
	    getGroup();
	    if(!id.isEmpty()){
		back = group.doSelect();
		if(!back.isEmpty()){
		    addError(back);								
		}
	    }
	}				
	else{		
	    getGroup();
	    if(!id.isEmpty()){
		back = group.doSelect();
		if(!back.isEmpty()){
		    addError(back);
		}
		ret = "view";
	    }
	}
	return ret;
    }
    public Group getGroup(){ 
	if(group == null){
	    group = new Group();
	    group.setId(id);
	    group.setDepartment_id(department_id);
	}		
	return group;
    }

    public void setGroup(Group val){
	if(val != null){
	    group = val;
	}
    }

    public String getGroupsTitle(){
	return groupsTitle;
    }
    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
	    action = val;
    }
    public void setDepartment_id(String val){
	if(val != null && !val.isEmpty())		
	    department_id = val;
    }		
    public List<Group> getGroups(){
	if(groups == null){
	    GroupList tl = new GroupList();
	    tl.setActiveOnly();
	    if(!department_id.isEmpty()){
		tl.setDepartment_id(department_id);
	    }
	    String back = tl.find();
	    if(back.isEmpty()){
		List<Group> ones = tl.getGroups();
		if(ones != null && ones.size() > 0){
		    groups = ones;
		}
	    }
	}
	return groups;
    }
    public List<Type> getDepartments(){
	TypeList tl = new TypeList("departments");
	String back = tl.find();
	if(back.isEmpty()){
	    List<Type> ones = tl.getTypes();
	    if(ones != null && ones.size() > 0){
		departments = ones;
	    }
	}
	return departments;
    }
    public boolean hasGroupManagers(){
	getGroupManagers();
	return groupManagers != null;
    }
    public List<GroupManager> getGroupManagers(){
	//
	if(groupManagers == null){
	    GroupManagerList gml = new GroupManagerList();
	    gml.setGroup_id(id);
	    String back = gml.find();
	    if(back.isEmpty()){
		List<GroupManager> ones = gml.getManagers();
		if(ones != null && ones.size() > 0){
		    groupManagers = ones;
		}
	    }
	}
	return groupManagers;
    }
    public List<HourCode> getHourCodes(){
	if(hourCodes == null){
	    HourCodeList gml = new HourCodeList();
	    gml.setEarnTypesOnly();
	    gml.setCurrentOnly();
	    String back = gml.find();
	    if(back.isEmpty()){
		List<HourCode> ones = gml.getHourCodes();
		if(ones != null && ones.size() > 0){
		    hourCodes = ones;
		}
	    }
	}
	return hourCodes;				
    }
    //
    public String[] getExcessHoursEarnTypes(){
	return CommonInc.excess_hours_earn_types;
    }
}





































