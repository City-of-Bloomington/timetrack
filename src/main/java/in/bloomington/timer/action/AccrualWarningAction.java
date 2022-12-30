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


public class AccrualWarningAction extends TopAction{

    static final long serialVersionUID = 3800L;	
    static Logger logger = LogManager.getLogger(AccrualWarningAction.class);
    //
    AccrualWarning accrualWarning = null;
    List<AccrualWarning> accrualWarnings = null;
    String accrualWarningsTitle = " Current warnings ";
    List<HourCode> hourCodes = null;
    List<Accrual> accruals = null;
    public String execute(){
	String ret = SUCCESS;
	String back = doPrepare();
	if(action.equals("Save")){
	    back = accrualWarning.doSave();
	    if(!back.isEmpty()){
		addActionError(back);
		addError(back);
	    }
	    else{
		addMessage("Saved Successfully");
		addActionMessage("Saved Successfully");
	    }
	}				
	else if(action.startsWith("Save")){
	    back = accrualWarning.doUpdate();
	    if(!back.isEmpty()){
		addError(back);
		addActionError(back);
	    }
	    else{
		addMessage("Saved Successfully");
		addActionMessage("Saved Successfully");
	    }
	}
	else{		
	    getAccrualWarning();
	    if(!id.isEmpty()){
		back = accrualWarning.doSelect();
		if(!back.isEmpty()){
		    addError(back);
		    addActionError(back);
		}
	    }
	}
	return ret;
    }
    public AccrualWarning getAccrualWarning(){
	if(accrualWarning == null){
	    accrualWarning = new AccrualWarning();
	    accrualWarning.setId(id);
	}
	return accrualWarning;
						
    }
    public void setAccrualWarning(AccrualWarning val){
	if(val != null){
	    accrualWarning = val;
	}
    }
    public boolean hasAccrualWarnings(){
	getAccrualWarnings();
	return accrualWarnings != null && accrualWarnings.size() > 0;
    }
		
    public String getAccrualWarningsTitle(){
	return accrualWarningsTitle;
    }

    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
	    action = val;
    }
    public List<AccrualWarning> getAccrualWarnings(){
	if(accrualWarnings == null){
	    AccrualWarningList tl = new AccrualWarningList();
	    String back = tl.find();
	    if(back.isEmpty()){
		List<AccrualWarning> ones = tl.getAccrualWarnings();
		if(ones != null && ones.size() > 0){
		    accrualWarnings = ones;
		}
	    }
	}
	return accrualWarnings;
    }
    public List<Accrual> getAccruals(){
	if(accruals == null){
	    AccrualList tl = new AccrualList();
	    String back = tl.find();
	    if(back.isEmpty()){
		List<Accrual> ones = tl.getAccruals();
		if(ones != null && ones.size() > 0){
		    accruals = ones;
		}
	    }
	}
	return accruals;
    }    
    public List<HourCode> getHourCodes(){
	//
	HourCodeList hcl = new HourCodeList();
	hcl.relatedToAccrualsOnly();
	String back = hcl.find();
	if(back.isEmpty()){
	    List<HourCode> ones = hcl.getHourCodes();
	    if(ones != null && ones.size() > 0){
		hourCodes = ones;
	    }
	}
	return hourCodes;
    }		

}





































