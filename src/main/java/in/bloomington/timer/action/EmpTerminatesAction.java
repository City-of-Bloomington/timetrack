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

public class EmpTerminatesAction extends TopAction{

    static final long serialVersionUID = 1800L;	
    static Logger logger = LogManager.getLogger(EmpTerminatesAction.class);
    //
    String date_from = "", date_to="", department_id="", group_id="";
    String status="";
    EmpTerminateList termLst = null;
    String empTerminatesTitle = "Terminations ";
    List<EmpTerminate> terms = null;
    List<Department> departments = null;
    public String execute(){
	String ret = SUCCESS;
	String back = doPrepare();
	if(!action.isEmpty()){
	    termLst.setDepartment_id(department_id);
	    termLst.setGroup_id(group_id);
	    termLst.setDate_from(date_from);
	    termLst.setDate_to(date_to);
	    back = termLst.find();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		List<EmpTerminate> ones = termLst.getTerms();
		if(ones != null && ones.size() > 0){
		    terms = ones;
		    empTerminatesTitle = " Found "+terms.size()+" terminations";
		    addMessage("Found "+terms.size()+" terminations ");
		}
		else{
		    addMessage("No match found");
		    empTerminatesTitle = "No match found";
		}
	    }
	}
	else{
	    getTermLst();
	    back = termLst.find();
	}
	return ret;
    }
    public EmpTerminateList getTermLst(){ 
	if(termLst == null){
	    termLst = new EmpTerminateList();
	}		
	return termLst;
    }

    public void setTermLst(EmpTerminateList val){
	if(val != null){
	    termLst = val;
	}
    }

    public String getEmpTerminatesTitle(){
	return empTerminatesTitle;
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
    public void setGroup_id(String val){
	if(val != null && !val.isEmpty() && !val.equals("-1"))		
	   group_id = val;
    }
    public void setStatus(String val){ // sent, not-sent, all
	if(val != null && !val.isEmpty() && !val.equals("-1"))		
	   status = val;
    }

    public String getGroup_id(){
	return group_id;
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
    public String getStatus(){
	if(status.isEmpty()) return "-1";
	return status;
    }    
    public boolean hasTerms(){
	return terms != null && terms.size() > 0;
    }

    public List<EmpTerminate> getTerms(){
	return terms;
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





































