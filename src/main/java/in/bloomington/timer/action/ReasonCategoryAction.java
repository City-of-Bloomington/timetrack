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

public class ReasonCategoryAction extends TopAction{

    static final long serialVersionUID = 750L;	
    static Logger logger = LogManager.getLogger(ReasonCategoryAction.class);
    //
    ReasonCategory reasonCategory = null;
    List<ReasonCategory> categories = null;
    String categoriesTitle = "Reason Categories";
    public String execute(){
	String ret = SUCCESS;
	String back = canProceed("reasonCategory.action");
	if(!back.isEmpty()){
	    return back;
	}
	if(action.equals("Save")){
	    back = reasonCategory.doSave();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		addMessage("Added Successfully");
	    }
	}				
	else if(action.startsWith("Save")){
	    back = reasonCategory.doUpdate();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		addMessage("Saved Successfully");
	    }
	}
	else{		
	    getReasonCategory();
	    if(!id.isEmpty()){
		back = reasonCategory.doSelect();
		if(!back.isEmpty()){
		    addError(back);
		}
	    }
	}
	return ret;
    }
    public ReasonCategory getReasonCategory(){
	if(reasonCategory == null){
	    reasonCategory = new ReasonCategory(id);
	}
	return reasonCategory;
						
    }
    public void setReasonCategory(ReasonCategory val){
	if(val != null){
	    reasonCategory = val;
	}
    }
    public String getCategoriessTitle(){
	return categoriesTitle;
    }

    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
	    action = val;
    }
    public boolean hasCategories(){
	getCategories();
	return categories != null;
    }
    public List<ReasonCategory> getCategories(){
	if(categories == null){
	    ReasonCategoryList tl = new ReasonCategoryList();
	    String back = tl.find();
	    if(back.isEmpty()){
		List<ReasonCategory> ones = tl.getReasonCategories();
		if(ones != null && ones.size() > 0){
		    categories = ones;
		}
	    }
	}
	return categories;
    }
		

}





































