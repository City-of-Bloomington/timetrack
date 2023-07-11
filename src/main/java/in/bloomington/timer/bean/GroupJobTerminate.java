package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.sql.*;
import java.util.List;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GroupJobTerminate{

    static final long serialVersionUID = 3700L;	
    static Logger logger = LogManager.getLogger(GroupJobTerminate.class);
    String group_id="", expire_date="";
    List<DepartmentEmployee> departmentEmployees = null;
    List<GroupManager> groupManagers = null;
    List<JobTask> jobs = null;
    Group group = null;
    //
    public GroupJobTerminate(){
	
    }
    public GroupJobTerminate(String val){
	//
	setGroup_id(val);
    }		
    public GroupJobTerminate(String val, String val2){
	//
	setGroup_id(val);
	setExpire_date(val2);
    }

    public boolean equals(Object obj){
	if(obj instanceof GroupJobTerminate){
	    GroupJobTerminate one =(GroupJobTerminate)obj;
	    return group_id.equals(one.getGroup_id());
	}
	return false;				
    }
    public int hashCode(){
	int seed = 29;
	if(!group_id.equals("")){
	    try{
		seed += Integer.parseInt(group_id);
	    }catch(Exception ex){
	    }
	}
	return seed;
    }
    //
    // getters
    //
    public String getGroup_id(){
	return group_id; 
    }
    public String getExpire_date(){
	return expire_date;
    }
    public Group getGroup(){
	if(group == null){
	    if(!group_id.isEmpty()){
	       Group gg  = new Group(group_id);
	       String back = gg.doSelect();
	       if(back.isEmpty())
		   group = gg;
	    }
	}
	return group;				
    }
    //
    // setters
    //
    public void setGroup_id(String val){
	if(val != null)
	    group_id = val;
    }
    public void setExpire_date(String val){
	if(val != null && !val.isEmpty())
	    expire_date = val;
    }
    public String toString(){
	return group_id;
    }
    public boolean hasDepartments(){
	return departmentEmployees != null && departmentEmployees.size() > 0;
    }
    public List<DepartmentEmployee> getDepartmentEmployees(){
	return departmentEmployees;
    }
    public boolean hasJobs(){
	return jobs != null && jobs.size() > 0;
    }
    public List<JobTask> getJobs(){
	return jobs;
    }
    //
    // when we want to terminate one job
    //
    public String doTerminate(){
	String back="";
	back = terminateAll();
	return back;
    }
    //
    public String terminateAll(){
	String back = "";
	getGroup();
	if(group == null){
	    back = "No group found ";
	    return back;
	}
	if(expire_date.isEmpty()){
	    back = "Expire date not set";
	    return back;
	}
	if(group.hasActiveJobs()){
	    jobs = group.getActiveJobs();
	    for(JobTask one:jobs){
		one.setExpire_date(expire_date);
		back += one.doUpdate();
	    }
	}
	return back;
    }
}
