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
import in.bloomington.timer.util.Helper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SwitchAction extends TopAction{

    static final long serialVersionUID = 1150L;	
    static Logger logger = LogManager.getLogger(SwitchAction.class);
    //
    String new_employee_id = "",
	document_id="",working_date="",
	source="",type="",
	department_id="";
    String employeesTitle = "Current Employees";
    public String execute(){
	String ret = SUCCESS;
	String back = doPrepare("switch.action");
	if(!action.isEmpty()){ // normally 'Change'
	    if(!new_employee_id.isEmpty()){
		setEmployee_id(new_employee_id);
		getEmployee();
	    }
	    try{
		HttpServletResponse res = ServletActionContext.getResponse();
		// we do not need the url here
		String str = "timeDetails.action?";
		boolean found = false;
		if(!document_id.isEmpty()){
		    str += "document_id="+document_id;
		    found = true;
		}
		else{
		    if(working_date.isEmpty()){
			working_date = Helper.getToday();
		    }
		    document_id = findDocument(working_date, new_employee_id);
		    if(!document_id.isEmpty()){
			str += "document_id="+document_id;
			found = true;
		    }
		}
		if(!source.isEmpty()){
		    str += "&source="+source;
		}
		if(found){
		    res.sendRedirect(str);
		}
		else{
		    back = "could not get employee info ";
		    addError(back);
		}
	    }catch(Exception ex){
		System.err.println(ex);
	    }
	}
	else {
	    if(user != null && !(isAdmin() || user.isITSAdmin() || user.isHrAdmin())){
		String val = user.getDepartment_id();
		    if(val != null)
			department_id = val;
	    }
	}
	return ret;
    }
    private String findDocument(String wdate, String emp_id){
	String doc_id = "";
	DocumentList dl = new DocumentList();
	dl.setDate(wdate);
	dl.setEmployee_id(emp_id);
	String back = dl.find();
	if(back.isEmpty()){
	    List<Document> ones = dl.getDocuments();
	    if(ones != null && ones.size() > 0){
		doc_id = ones.get(0).getId();
	    }
	}
	return doc_id;
    }
    public String getEmployeesTitle(){
	return employeesTitle;
    }
    public String getDepartment_id(){
	return department_id;
    }
    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
	    action = val;
    }
    public String getWorkingDate(){
	return working_date;
    }
    public void setWorkingDate(String val){
	if(val != null)
	   working_date = val;
    }    
    
    public void setType(String val){
	// needed for js
    }		
    public void setNew_employee_id(String val){
	if(val != null && !val.isEmpty())		
	    new_employee_id = val;
    }
		
    public void setEmployee_name(String val){
	// for auto complete
    }
    public String getEmployee_name(){
	return "";
    }    
    public void setDocument_id(String val){
	if(val != null && !val.isEmpty())		
	    document_id = val;
    }
    public void setDepartment_id(String val){
	if(val != null && !val.isEmpty())		
	    department_id = val;
    }		
    public void setSource(String val){
	if(val != null && !val.isEmpty())		
	    source = val;
    }		


}





































