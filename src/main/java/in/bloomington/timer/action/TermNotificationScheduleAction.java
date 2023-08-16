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

public class TermNotificationScheduleAction extends TopAction{

    static final long serialVersionUID = 3850L;	
    static Logger logger =
	LogManager.getLogger(TermNotificationScheduleAction.class);
    //
    String termNotificationTitle = "Termination Notification Schedules";
    QuartzMisc quartzMisc = null;
    TermNotificationScheduler schedular = null;
    String prev_date="", next_date="", date="";
    public String execute(){
	String ret = SUCCESS;
	String back = canProceed("termNotificationScheduler.action");
	if(!back.isEmpty()){
	    return back;
	}
	prepareSchedular();				
	if(action.equals("Schedule")){
	    back = doClean();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    try{
		back = schedular.run();
		if(!back.isEmpty()){
		    addError(back);
		}
		else{
		    if(quartzMisc != null){
			prev_date = quartzMisc.getPrevScheduleDate();
			if(prev_date.startsWith("1969")) // 0 cuases 1969 schedule date
			    prev_date = "No Previous date found";
			next_date = quartzMisc.getNextScheduleDate();
		    }
		    addMessage("Scheduled Successfully");										
		}
	    }catch(Exception ex){
		addActionError(""+ex);
	    }
	}
	else if(action.startsWith("Submit")){
	    HandleTermNotification handle = new HandleTermNotification();
	    handle.setActiveMail();
	    back = handle.process();
	    if(!back.isEmpty()){
		addActionError(back);
		addError(back);
	    }
	    else{
		addMessage("Submitted Successfully");								
	    }
	}
	return ret;
    }
    private void prepareSchedular(){
	String msg = "";
	if(date.isEmpty()){
	    date = Helper.getToday();
	}
	if(!date.isEmpty()){
	    schedular = new TermNotificationScheduler(date);
	}
	quartzMisc = new QuartzMisc("notification");
	msg = quartzMisc.findScheduledDates();
	if(msg.isEmpty()){
	    prev_date = quartzMisc.getPrevScheduleDate();
	    if(prev_date.startsWith("1969")) // 0 cuases 1969 schedule date
		prev_date = "No Previous date found";
	    next_date = quartzMisc.getNextScheduleDate();
	}				
    }
    private String doClean(){
	String msg = "";
	if(quartzMisc != null){
	    msg = quartzMisc.doClean();
	}
	return msg;
    }
    public String getTermNotificationTitle(){
				
	return termNotificationTitle;
    }

    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
	    action = val;
    }
    public void setDate(String val){
	if(val != null && !val.isEmpty())		
	    date = val;
    }
    public String getDate(){
	return date;
    }
    public String getPrev_date(){
	return prev_date;
    }
    public String getNext_date(){
	return next_date;
    }
    public boolean hasPrevDates(){
	return !prev_date.isEmpty();
    }

}





































