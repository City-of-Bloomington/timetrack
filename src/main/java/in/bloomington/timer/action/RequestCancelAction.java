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

public class RequestCancelAction extends TopAction{

    static final long serialVersionUID = 3800L;	
    static Logger logger = LogManager.getLogger(RequestCancelAction.class);
    //
    String document_id="";
    Document document = null;
    RequestCancel request = null;
    public String execute(){
	String ret = SUCCESS;
	String back = "";
	if(action.equals("Submit")){
	    back = request.doSave();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		addMessage("Saved Successfully");
		id = request.getId();
	    }
	}				
	else{
	    if(user == null)
		getUser();
	    getRequest();
	    if(!document_id.isEmpty()){
		request.setDocument_id(document_id);
		request.setRequestBy_id(user.getId());
	    }
	}
	return ret;
    }
    public RequestCancel getRequest(){
	if(request == null){
	    request = new RequestCancel();
	}
	return request;
    }
    public void setRequest(RequestCancel val){
	if(val != null){
	    request = val;
	}
    }
    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
	    action = val;
    }
    /**
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
    */
}





































