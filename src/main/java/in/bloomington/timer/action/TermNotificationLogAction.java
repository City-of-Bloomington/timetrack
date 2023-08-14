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

public class TermNotificationLogAction extends TopAction{

    static final long serialVersionUID = 1800L;	
    static Logger logger = LogManager.getLogger(TermNotificationLogAction.class);
    //
    String date_from = "", date_to="", department_id="";
    TermNotificationList termNoteLst = null;
    String termNotificationsTitle = "Termination Notification Logs";
    List<TermNotification> notifications = null;
    List<Department> departments = null;
    public String execute(){
	String ret = SUCCESS;
	String back = doPrepare();
	getTermNoteLst();	
	if(!action.isEmpty()){
	    termNoteLst.setDepartment_id(department_id);
	    termNoteLst.setDate_from(date_from);
	    termNoteLst.setDate_to(date_to);
	    termNoteLst.setNoLimit();
	}
	back = termNoteLst.find();
	if(!back.isEmpty()){
	    addError(back);
	}
	else{
	    List<TermNotification> ones = termNoteLst.getNotifications();
	    if(ones != null && ones.size() > 0){
		notifications = ones;
		termNotificationsTitle = " Found "+notifications.size()+" groups";
		addMessage("Found "+notifications.size()+" notifications ");
	    }
	    else{
		addMessage("No match found");
		termNotificationsTitle = "No match found";
	    }
	}
	return ret;
    }
    public TermNotificationList getTermNoteLst(){ 
	if(termNoteLst == null){
	    termNoteLst = new TermNotificationList();
	}		
	return termNoteLst;
    }

    public void setTermNoteLst(TermNotificationList val){
	if(val != null){
	    termNoteLst = val;
	}
    }

    public String getTermNotificationsTitle(){
	return termNotificationsTitle;
    }
    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
	    action = val;
    }
    public void setDate_from(String val){
	if(val != null && !val.isEmpty())		
	   date_from = val;
    }
    public void setDate_to(String val){
	if(val != null && !val.isEmpty())		
	   date_to = val;
    }
    public void setDepartment_id(String val){
	if(val != null && !val.isEmpty() && !val.equals("-1"))		
	   department_id = val;
    }
    
    public String getDate_from(){
	return date_from;
    }
    public String getDate_to(){
	return date_to;
    }
    public String getDepartment_id(){
	if(department_id.isEmpty()) return "-1";
	return department_id;
    }    
    public boolean hasNotifications(){
	return notifications != null && notifications.size() > 0;
    }

    public List<TermNotification> getNotifications(){
	return notifications;
    }
    public List<Department> getDepartments(){
	if(departments == null){
	    DepartmentList tl = new DepartmentList();
	    tl.setActiveOnly();
	    String back = tl.find();
	    if(back.isEmpty()){
		List<Department> ones = tl.getDepartments();
		if(ones != null && ones.size() > 0){
		    departments = ones;
		}
	    }
	}
	return departments;
    }		
		
}





































