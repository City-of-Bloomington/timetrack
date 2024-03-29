package in.bloomington.timer.action;

/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.*;
import java.io.*;
import java.text.*;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;  
import in.bloomington.timer.list.*;
import in.bloomington.timer.bean.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ShiftAction extends TopAction{

    static final long serialVersionUID = 1800L;	
    static Logger logger = LogManager.getLogger(ShiftAction.class);
    //
    Shift shift = null;
    List<Shift> shifts = null;
    List<GroupShift> groupShifts = null;
    String shiftsTitle = "Current shifts";
    public String execute(){
	String ret = SUCCESS;
	String back = canProceed("shift.action");
	if(!back.isEmpty()){
	    return back;
	}
	if(action.equals("Save")){
	    back = shift.doSave();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		addMessage("Added Successfully");
	    }
	}				
	else if(action.startsWith("Save")){
	    back = shift.doUpdate();
	    if(!back.isEmpty()){
		addActionError(back);
		addError(back);								
	    }
	    else{
		addMessage("Added Successfully");
	    }
	}
	else{		
	    getShift();
	    if(!id.isEmpty()){
		back = shift.doSelect();
		if(!back.isEmpty()){
		    addError(back);
		}								
	    }
	}
	return ret;
    }
    public Shift getShift(){ 
	if(shift == null){
	    shift = new Shift();
	    shift.setId(id);
	}		
	return shift;
    }

    public void setShift(Shift val){
	if(val != null){
	    shift = val;
	}
    }

    public String getShiftsTitle(){
	return shiftsTitle;
    }
    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
	    action = val;
    }
    public List<Shift> getShifts(){
	if(shifts == null){
	    ShiftList tl = new ShiftList();
	    tl.setActiveOnly();
	    String back = tl.find();
	    if(back.isEmpty()){
		List<Shift> ones = tl.getShifts();
		if(ones != null && ones.size() > 0){
		    shifts = ones;
		}
	    }
	}
	return shifts;
    }
    //
    public List<GroupShift> getGroupShifts(){
	if(groupShifts == null){
	    getShift();
	    if(!shift.getId().isEmpty()){
		GroupShiftList gsl = new GroupShiftList();
		gsl.setShift_id(shift.getId());
		String back = gsl.find();
		if(back.isEmpty()){
		    List<GroupShift> ones = gsl.getGroupShifts();
		    if(ones != null && ones.size() > 0){
			groupShifts = ones;
		    }
		}
	    }
	}
	return groupShifts;
    }
    public boolean hasGroupShifts(){
	getGroupShifts();
	return groupShifts != null && groupShifts.size() > 0;
    }

		
}





































