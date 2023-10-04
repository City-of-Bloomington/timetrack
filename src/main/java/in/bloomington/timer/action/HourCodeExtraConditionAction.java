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
import in.bloomington.timer.bean.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class HourCodeExtraConditionAction extends TopAction{

    static final long serialVersionUID = 850L;	
    static Logger logger = LogManager.getLogger(HourCodeExtraConditionAction.class);
    //
    HourCodeExtraCondition hourcodeCondition = null;
    List<HourCodeExtraCondition> conditions = null;
    String hourcodeConditionsTitle = "Earn Code Extra Restrictions";
    List<HourCode> hourcodes = null;
    List<SalaryGroup> salaryGroups = null;
    // associate types
    String[] types = new String[]{"Regular","Used","Earned","Overtime","Unpaid","Monetary","Call Out","Other"}; 
    public String execute(){
	String ret = SUCCESS;
	String back = canProceed("hourCodeExtraCondition.action");
	if(!back.isEmpty()){
	    return back;
	}
	if(action.equals("Save")){
	    back = hourcodeCondition.doSave();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		addMessage("Added Successfully");
		id = hourcodeCondition.getId();
	    }
	}				
	else if(action.startsWith("Save")){
	    back = hourcodeCondition.doUpdate();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		addMessage("Saved Successfully");
	    }
	}
	else{		
	    getHourcodeCondition();
	    if(!id.isEmpty()){
		back = hourcodeCondition.doSelect();
		if(!back.isEmpty()){
		    addError(back);
		}								
	    }
	}
	return ret;
    }
    public HourCodeExtraCondition getHourcodeCondition(){
	if(hourcodeCondition == null){
	    hourcodeCondition = new HourCodeExtraCondition(id);
	}
	return hourcodeCondition;
						
    }
    public void setHourcodeExtraCondition(HourCodeExtraCondition val){
	if(val != null){
	    hourcodeCondition = val;
	}
    }
    public String getHourcodeConditionsTitle(){
	return hourcodeConditionsTitle;
    }

    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
	    action = val;
    }
    public List<HourCodeExtraCondition> getConditions(){
	if(conditions == null){
	    HourCodeExtraConditionList tl = new HourCodeExtraConditionList();
	    String back = tl.find();
	    if(back.isEmpty()){
		List<HourCodeExtraCondition> ones = tl.getConditions();
		if(ones != null && ones.size() > 0){
		    conditions = ones;
		}
	    }
	}
	return conditions;
    }
    public boolean hasConditions(){
	getConditions();
	return conditions != null;
    }
    public List<HourCode> getHourcodes(){
	if(hourcodes == null){
	    HourCodeList tl = new HourCodeList();
	    String back = tl.find();
	    if(back.isEmpty()){
		List<HourCode> ones = tl.getHourCodes();
		if(ones != null && ones.size() > 0){
		    hourcodes = ones;
		}
	    }
	}
	return hourcodes;
    }		
    public List<SalaryGroup> getSalaryGroups(){
	if(salaryGroups == null){
	    SalaryGroupList tl = new SalaryGroupList();
	    String back = tl.find();
	    if(back.isEmpty()){
		List<SalaryGroup> ones = tl.getSalaryGroups();
		if(ones != null && ones.size() > 0){
		    salaryGroups = ones;
		}
	    }
	}
	return salaryGroups;
    }
    public String[] getTypes(){
	return types;
    }
}





































