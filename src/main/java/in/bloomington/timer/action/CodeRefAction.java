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

public class CodeRefAction extends TopAction{

    static final long serialVersionUID = 3800L;	
    static Logger logger = LogManager.getLogger(CodeRefAction.class);
    //
    String codeRefsTitle = "Current Code Refs";
    CodeRef codeRef = null;
    List<CodeRef> codeRefs = null;
    List<HourCode> hourCodes = null;
    public String execute(){
	String ret = SUCCESS;
	
	String back = canProceed("codeRef.action");
	if(!back.isEmpty()){
	    return back;
	}
	if(action.equals("Save")){
	    back = codeRef.doSave();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		addMessage("Saved Successfully");
	    }
	}				
	else if(action.startsWith("Save")){
	    back = codeRef.doUpdate();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		addMessage("Saved Successfully");
	    }
	}
	else if(action.startsWith("Delete")){
	    getCodeRef();
	    back = codeRef.doDelete();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		addMessage("Deleted Successfully");
		id="";
		codeRef = new CodeRef();
	    }
	}				
	else{		
	    getCodeRef();
	    if(!id.isEmpty()){
		back = codeRef.doSelect();
		if(!back.isEmpty()){
		    addError(back);
		}
	    }
	}
	return ret;
    }
    public CodeRef getCodeRef(){
	if(codeRef == null){
	    codeRef = new CodeRef();
	    codeRef.setId(id);
	}
	return codeRef;
						
    }
    public void setCodeRef(CodeRef val){
	if(val != null){
	    codeRef = val;
	}
    }
		
    public String getCodeRefsTitle(){
				
	return codeRefsTitle;
    }


    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
	    action = val;
    }
    public List<CodeRef> getCodeRefs(){
	if(codeRefs == null){
	    CodeRefList tl = new CodeRefList();
	    String back = tl.find();
	    if(back.isEmpty()){
		List<CodeRef> ones = tl.getCodeRefs();
		if(ones != null && ones.size() > 0){
		    codeRefs = ones;
		}
	    }
	}
	return codeRefs;
    }
    public List<HourCode> getHourCodes(){
	if(hourCodes == null){
	    HourCodeList tl = new HourCodeList();
	    String back = tl.find();
	    if(back.isEmpty()){
		List<HourCode> ones = tl.getHourCodes();
		if(ones != null && ones.size() > 0){
		    hourCodes = ones;
		}
	    }
	}
	return hourCodes;

    }

}





































