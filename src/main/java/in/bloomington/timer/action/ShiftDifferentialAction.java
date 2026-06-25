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
import in.bloomington.timer.util.*;
import in.bloomington.timer.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ShiftDifferentialAction extends TopAction{

    static final long serialVersionUID = 3850L;	
    static Logger logger = LogManager.getLogger(ShiftDifferentialAction.class);
    //
    String shiftDifTitle = "Shift Differential";
    ShiftDiffCode code = null;
    List<ShiftDiffCode> codes = null;
    String dept_ref_id="36", effective_date="", id="";
    public String execute(){
	String ret = SUCCESS;
	String back = canProceed("ShiftDifferntial.action");
	if(!back.isEmpty()){
	    return back;
	}
	getCode();
	if(action.equals("Save")){
	    back = code.doSave();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		addMessage("Save Successfully");
	    }	    
	}
	else if(action.equals("Save")){ //update
	    back = code.doUpdate();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		id = code.getId();
		addMessage("Update Successfully");
	    }	    
	}
	else if(action.equals("Delete")){ //update
	    back = code.doDelete();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		id = "";
		addMessage("Delete Successfully");
	    }
	    
	}	
	else if(!id.isEmpty()){
	    back = code.doSelect();
	    if(!back.isEmpty()){
		addError(back);
	    }
	}
	// for testing only
	else if(action.startsWith("Test")){ // import now given the date
	    HandleShiftDifferential handle = new HandleShiftDifferential();
	    back = handle.process();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		addMessage("Ran Successfully");
	    }
	}
	return ret;
    }
    public void setId(String val){
	if(val != null)
	    id = val;
    }    
    public ShiftDiffCode getCode(){
	if(code == null)
	    code = new ShiftDiffCode();
	if(!id.isEmpty()){
	    code.setId(id);
	}
	return code;
    }
    public void setCode(ShiftDiffCode val){
	if(val != null)
	    code = val;
    }
    public boolean hasCodes(){
	if(codes == null)
	    findCodes();
	return codes != null && codes.size() > 0;
    }
    public List<ShiftDiffCode> getCodes(){
	return codes;
    }
    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
	    action = val;
    }
    public void setEffectiveDatedate(String val){
	if(val != null && !val.isEmpty())		
	    effective_date = val;
    }
    // 
    public String getEffectiveDate(){
	if(effective_date.isEmpty()){
	    effective_date = Helper.getToday();
	}
	return effective_date;
    }
    private void findCodes(){
	ShiftDiffCodeList sdcl = new ShiftDiffCodeList();
	String back = sdcl.find();
	if(back.isEmpty()){
	    codes = sdcl.getShiftCodes();
	}
    }
			     
 
}





































