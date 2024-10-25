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


public class HourCodeExtraAction extends TopAction{

    static final long serialVersionUID = 850L;	
    static Logger logger = LogManager.getLogger(HourCodeExtraAction.class);
    //
    HourCodeExtra extra = null;
    List<HourCodeExtra> extras = null;
    String extrasTitle = "Earn Code additional Conditions ";
    List<HourCode> hourcodes = null;
    List<SalaryGroup> salaryGroups = null;
    // associate types
    String[] types = new String[]{"Regular","Used","Earned","Overtime","Unpaid","Monetary","Call Out","Other"}; 
    public String execute(){
	String ret = SUCCESS;
	String back = canProceed("hourCodeExtra.action");
	if(!back.isEmpty()){
	    return back;
	}
	if(action.equals("Save")){
	    back = extra.doSave();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		addMessage("Added Successfully");
		id = extra.getId();
	    }
	}				
	else if(action.startsWith("Save")){
	    back = extra.doUpdate();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		addMessage("Saved Successfully");
	    }
	}
	else if(action.startsWith("Add")){
	    back = extra.addHourCode();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		addMessage("Added Successfully");
	    }
	}	
	else{		
	    getExtra();
	    if(!id.isEmpty()){
		back = extra.doSelect();
		if(!back.isEmpty()){
		    addError(back);
		}								
	    }
	}
	return ret;
    }
    public HourCodeExtra getExtra(){
	if(extra == null){
	    extra = new HourCodeExtra(id);
	}
	return extra;
						
    }
    public void setExtra(HourCodeExtra val){
	if(val != null){
	    extra = val;
	}
    }
    public String getExtrasTitle(){
	return extrasTitle;
    }

    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
	    action = val;
    }
    public List<HourCodeExtra> getExtras(){
	if(extras == null){
	    HourCodeExtraList tl = new HourCodeExtraList();
	    String back = tl.find();
	    if(back.isEmpty()){
		List<HourCodeExtra> ones = tl.getExtras();
		if(ones != null && ones.size() > 0){
		    extras = ones;
		}
	    }
	}
	return extras;
    }
    public boolean hasExtras(){
	getExtras();
	return extras != null;
    }
    public List<HourCode> getHourCodes(){
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





































