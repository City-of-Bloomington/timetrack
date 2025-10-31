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

public class AltPayPeriodGroupAction extends TopAction{

    static final long serialVersionUID = 1800L;	
    static Logger logger = LogManager.getLogger(AltPayPeriodGroupAction.class);
    //
    AltPayPeriodGroup altGroup = null;
    List<AltPayPeriodGroup> altGroups = null;
    List<Group> groups = null;
    String groupsTitle = "Alt Pay Period Groups";
    List<Type> departments = null;
    String department_id="";
    public String execute(){
	String ret = SUCCESS;
	String back = canProceed("group.action");
	if(!back.isEmpty()){
	    return back;
	}
	if(action.equals("Save")){
	    back = altGroup.doSave();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		id = altGroup.getId();
		addMessage("Added Successfully");
	    }
	}				
	else if(action.equals("Delete")){
	    getAltGroup();
	    back = altGroup.doDelete();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		id = "";
		altGroup = null;
		addMessage("Deleted Successfully");
	    }
	}				
	else{
	    if(!id.isEmpty()){
		getAltGroup();
		altGroup.doSelect();
	    }
	}
	findAltGroups();
	return ret;
    }
    public AltPayPeriodGroup getAltGroup(){ 
	if(altGroup == null){
	    altGroup = new AltPayPeriodGroup();
	    if(!id.isEmpty())
		altGroup.setId(id);
	}		
	return altGroup;
    }

    public void setAltGroup(AltPayPeriodGroup val){
	if(val != null){
	    altGroup = val;
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
    public List<AltPayPeriodGroup> getAltGroups(){
	return altGroups;
    }
    private String findAltGroups(){
	String back = "";
	if(altGroups == null){
	    AltPayPeriodGroupList apgl = new AltPayPeriodGroupList();
	    back = apgl.find();
	    if(back.isEmpty()){
		List<AltPayPeriodGroup> ones = apgl.getAltGroups();
		if(ones != null && ones.size() > 0){
		    altGroups = ones;
		}
	    }
	}
	return back;
    }    
    public boolean hasAltGroups(){
	if(altGroups == null)
	    findAltGroups();
	return altGroups != null && altGroups.size() > 0;
    }

}





































