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
import in.bloomington.timer.bean.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class GroupLeaveAction extends TopAction{

    static final long serialVersionUID = 850L;	
    static Logger logger = LogManager.getLogger(GroupLeaveAction.class);
    //
    GroupLeave groupLeave = null;
    List<GroupLeave> groupLeaves = null;
    String groupLeavesTitle = "Current Group Leave Configuration";
    List<Type> departments = null;
    List<Type> salaryGroups = null;
    List<Group> groups = null;
    String department_id = "";
    String group_id = "";
    public String execute(){
	String ret = SUCCESS;
	String back = canProceed("groupLeave.action");
	if(!back.isEmpty()){
	    return back;
	}
	if(action.equals("Save")){
	    back = groupLeave.doSave();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		addMessage("Added Successfully");
		id = groupLeave.getId();
	    }
	}				
	else if(action.startsWith("Save")){
	    back = groupLeave.doUpdate();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		addMessage("Saved Successfully");
	    }
	}
	else if(action.startsWith("Delete")){
	    getGroupLeave();
	    back = groupLeave.doDelete();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		addMessage("Deleted Successfully");
	    }
	}	
	else{		
	    getGroupLeave();
	    if(!id.isEmpty()){
		back = groupLeave.doSelect();
		if(!back.isEmpty()){
		    addError(back);
		}								
	    }
	}
	return ret;
    }
    public GroupLeave getGroupLeave(){
	if(groupLeave == null){
	    groupLeave = new GroupLeave(id);
	}
	return groupLeave;
						
    }
    public void setGroupLeave(GroupLeave val){
	if(val != null){
	    groupLeave = val;
	}
    }
    public String getGroupLeavesTitle(){
	return groupLeavesTitle;
    }

    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
	    action = val;
    }
    public void setDepartment_id(String val){
	if(val != null && !val.equals("-1"))		
	    department_id = val;
    }
    public String getDepartment_id(){
	if(department_id.isEmpty()){
	    return "-1";
	}
	return department_id;
    }
    public List<GroupLeave> getGroupLeaves(){
	if(groupLeaves == null){
	    GroupLeaveList tl = new GroupLeaveList();
	    String back = tl.find();
	    if(back.isEmpty()){
		List<GroupLeave> ones = tl.getGroupLeaves();
		if(ones != null && ones.size() > 0){
		    groupLeaves = ones;
		}
	    }
	}
	return groupLeaves;
    }
    public boolean hasGroupLeaves(){
	getGroupLeaves();
	return groupLeaves != null;
    }
    public List<Type> getDepartments(){
	if(departments == null){
	    TypeList tl = new TypeList("departments");
	    String back = tl.find();
	    if(back.isEmpty()){
		List<Type> ones = tl.getTypes();
		if(ones != null && ones.size() > 0){
		    departments = ones;
		}
	    }
	}
	return departments;
    }
    public List<Type> getSalaryGroups(){
	if(salaryGroups == null){
	    TypeList tl = new TypeList("salary_groups");
	    String back = tl.find();
	    if(back.isEmpty()){
		List<Type> ones = tl.getTypes();
		if(ones != null && ones.size() > 0){
		    salaryGroups = ones;
		}
	    }
	}
	return salaryGroups;
    }
    public List<Group> getGroups(){
	if(groups == null){
	    GroupList tl = new GroupList();
	    if(department_id.isEmpty()){
		getGroupLeave();
		department_id = groupLeave.getDepartment_id();
	    }
	    if(!(department_id.isEmpty() || department_id.equals("-1"))){
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

}





































