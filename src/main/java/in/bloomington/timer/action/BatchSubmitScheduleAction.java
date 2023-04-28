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

public class BatchSubmitScheduleAction extends TopAction{

    static final long serialVersionUID = 3850L;	
    static Logger logger =
	LogManager.getLogger(BatchSubmitScheduleAction.class);
    //
    List<PayPeriod> periods = null;
    String batchSubmitSchedulesTitle = "Submint Batch Schedules";
    QuartzMisc quartzMisc = null;
    BatchSubmitScheduler schedular = null;
    List<BatchLog> logs = null;
    String prev_date="", next_date="", pay_period_id="", date="";
    public String execute(){
	String ret = SUCCESS;
	String back = canProceed("batchSubmit.action");
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
	    if(pay_period_id.isEmpty()){
		addError("Pay period not selected");
	    }
	    else{
		HandleBatchSubmit handle = new HandleBatchSubmit(pay_period_id);
		handle.setRun_by(user.getId());
		back = handle.process();
		if(!back.isEmpty()){
		    addActionError(back);
		}
		else{
		    addMessage("Batch Submitted Successfully");
		}
	    }
	}
	return ret;
    }
    private void prepareSchedular(){
	String msg = "";
	PayPeriodList pl = new PayPeriodList();
	if(date.isEmpty()){
	    pl.setLastPayPeriod();
	    msg = pl.find();
	    if(msg.isEmpty()){
		List<PayPeriod> ones = pl.getPeriods();
		if(ones != null && ones.size() > 0){
		    PayPeriod one = ones.get(0);
		    String end_date = one.getEnd_date();
		    date = Helper.getDateAfter(end_date, 1);
		}
	    }
	}
	if(!date.isEmpty()){
	    schedular = new BatchSubmitScheduler(date);
	}
	quartzMisc = new QuartzMisc("batch");
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
    public String getBatchSubmitSchedularsTitle(){
				
	return batchSubmitSchedulesTitle;
    }

    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
	    action = val;
    }
    public void setDate(String val){
	if(val != null && !val.isEmpty())		
	    date = val;
    }
    public void setPay_period_id(String val){
	if(val != null && !val.equals("-1"))		
	    pay_period_id = val;
    }
    public String getPay_period_id(){
	if(pay_period_id.isEmpty())
	    return "-1";
	return pay_period_id;
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
    public List<PayPeriod> getPeriods(){
	if(periods == null){
	    PayPeriodList dl = new PayPeriodList();
	    dl.avoidFuturePeriods();
	    dl.setLimit("5");
	    String msg = dl.find();
	    if(!msg.isEmpty()){
		addError(msg);
		logger.error(msg);
	    }
	    else{
		List<PayPeriod> ones = dl.getPeriods();
		if(ones != null && ones.size() > 0){
		    periods = ones;
		}
	    }
	}
	return periods;
    }
    public boolean hasPeriods(){
	getPeriods();
	return periods != null && periods.size() > 0;
    }		
    public boolean hasLogs(){
	getBatch_logs();
	return logs != null && logs.size() > 0;
    }
    public List<BatchLog> getBatch_logs(){
	if(logs == null){
	    BatchLogList bll = new BatchLogList();
	    String back = bll.find();
	    if(back.isEmpty()){
		List<BatchLog> ones = bll.getLogs();
		if(ones != null && ones.size() > 0){
		    logs = ones;
		}
	    }
	}
	return logs;
    }
}





































