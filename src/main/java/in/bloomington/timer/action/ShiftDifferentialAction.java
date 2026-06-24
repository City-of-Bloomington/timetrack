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
    List<Department> depts = null;
    QuartzMisc quartzMisc = null;
    // Scheduler schedular = null;
    String dept_ref_id="36", effective_date="";
    public String execute(){
	String ret = SUCCESS;
	String back = canProceed("ShiftDifferntial.action");
	if(!back.isEmpty()){
	    return back;
	}
	
	// prepareSchedular();				
	if(action.equals("Schedule")){
	    /**
	    try{
		back = schedular.run();
		if(!back.isEmpty()){
		    addError(back);
		}
		else{
		    addMessage("Scheduled Successfully");
		}
	    }catch(Exception ex){
		addError(""+ex);
	    }
	    */
	}
	else if(action.startsWith("Submit")){ // import now given the date
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
    private String doClean(){
	String msg = "";
	/**
	if(quartzMisc != null){
	    msg = quartzMisc.doClean();
	}
	*/
	return msg;
    }

    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
	    action = val;
    }
    public void setEffectiveDatedate(String val){
	if(val != null && !val.isEmpty())		
	    effective_date = val;
    }
    public void setDept_ref_id(String val){
	if(val != null && !val.equals("-1"))		
	    dept_ref_id = val;
    }		
    // 
    public String getEffectiveDate(){
	if(effective_date.isEmpty()){
	    effective_date = Helper.getToday();
	}
	return effective_date;
    }
    public String getDept_ref_id(){
	if(dept_ref_id.isEmpty()){
	    return "-1";
	}
	return dept_ref_id;
    }	    
    public List<Department> getDepts(){
	if(depts == null){
	    DepartmentList dl = new DepartmentList();
	    dl.setActiveOnly();
	    dl.hasRefIds();
	    String msg = dl.find();
	    if(!msg.isEmpty()){
		logger.error(msg);
	    }
	    else{
		List<Department> ones = dl.getDepartments();
		if(ones != null && ones.size() > 0){
		    depts = ones;
		}
	    }
	}
	return depts;
    }
    public boolean hasDepts(){
	getDepts();
	return depts != null && depts.size() > 0;
    }

}





































