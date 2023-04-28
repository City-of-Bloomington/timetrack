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

public class CodeReasonScheduleAction extends TopAction{

    static final long serialVersionUID = 3850L;	
    static Logger logger = LogManager.getLogger(CodeReasonScheduleAction.class);
    //
    List<Department> depts = null;
    String codeReasonSchedulerTitle = "Code Reason Scheduler";
    QuartzMisc quartzMisc = null;
    CodeReasonScheduler schedular = null;
    String date = "", write_date="", prev_date="", next_date="", dept_ref_id="";
    public String execute(){
	String ret = SUCCESS;
	String back = canProceed("codeReasonSchedule.action");
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
		    addMessage("Scheduled Successfully");
		}
	    }catch(Exception ex){
		addError(""+ex);
	    }
	}
	else if(action.startsWith("Run")){ // import now given the date
	    CodeReasonJob job = new CodeReasonJob();
	    job.setOutputLocation(xls_output_location);
	    job.doWork();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		addMessage("Ran Successfully");
	    }
	}
	return ret;
    }
    private void prepareSchedular(){
	String msg = "";
	String date = Helper.getToday();
	if(!date.isEmpty()){
	    schedular = new CodeReasonScheduler(date, xls_output_location);
	}
	quartzMisc = new QuartzMisc("code_reason"); // type
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
    public String getCodeReasonSchedularTitle(){
				
	return codeReasonSchedulerTitle;
    }


    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
	    action = val;
    }
    public void setDate(String val){
	if(val != null && !val.isEmpty())		
	    date = val;
    }
    // read only 
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





































