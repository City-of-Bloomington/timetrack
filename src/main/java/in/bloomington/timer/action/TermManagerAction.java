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

public class TermManagerAction extends TopAction{

    static final long serialVersionUID = 3800L;	
    static Logger logger = LogManager.getLogger(TermManagerAction.class);
    //
    String termManagersTitle = "Termination Managers";
    List<Department> departments = null;
    TermManager manager = null;
    List<TermManager> managers = null;
		
    @Override
    public String execute(){
	String ret = SUCCESS;
	String back = canProceed("termManager.action");
	if(!back.isEmpty()){
	    return back;
	}
	if(action.equals("Save")){
	    back = manager.doSave();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		addMessage("Saved Successfully");
	    }
	}				
	else if(action.startsWith("Save")){
	    back = manager.doUpdate();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		addMessage("Updated Successfully");
	    }
	}
	else{		
	    getManager();
	    if(!id.isEmpty()){
		back = manager.doSelect();
		if(!back.isEmpty()){
		    addError(back);
		}
	    }
	}
	return ret;
    }
    public TermManager getManager(){
	if(manager == null){
	    manager = new TermManager();
	    manager.setId(id);
	}
	return manager;
						
    }
    public void setManager(TermManager val){
	if(val != null){
	    manager = val;
	}
    }
    public List<Department> getDepartments(){
	if(departments == null){
	    DepartmentList tl = new DepartmentList();
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
    public String getTermManagersTitle(){
	return termManagersTitle;
    }

    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
	    action = val;
    }
    public boolean hasManagers(){
	getManagers();
	return managers != null && managers.size() > 0;
    }
    public List<TermManager> getManagers(){
	if(managers == null){
	    TermManagerList tl = new TermManagerList();
	    // tl.setActiveOnly();
	    String back = tl.find();
	    if(back.isEmpty()){
		List<TermManager> ones = tl.getManagers();
		if(ones != null && ones.size() > 0){
		    managers = ones;
		}
	    }
	}
	return managers;
    }

}





































