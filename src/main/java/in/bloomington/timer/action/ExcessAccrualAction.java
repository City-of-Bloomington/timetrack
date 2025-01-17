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
import in.bloomington.timer.ExcessAccrualScheduler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ExcessAccrualAction extends TopAction{

    static final long serialVersionUID = 3800L;	
    static Logger logger = LogManager.getLogger(ExcessAccrualAction.class);
    //
    QuartzMisc quartzMisc = null;
    List<ExcessAccrual> excesses = null;
    List<Accrual> accruals = null;
    List<ExcessAccrualEmailLog> logs = null;
    ExcessAccrualScheduler schedular = null;    
    String excessAccrualsTitle = " Excess Accruals Warning ";
    String hr_email = "hrmail@bloomington.in.gov";
    String accrual_id = "3"; // comp time accrual
    String threshold_value = "40";
    String date = "";
    public String execute(){
	String ret = SUCCESS;
	String back = canProceed("excess_accrual.action");
	if(!back.isEmpty()){
	    return back;
	}
	if(action.startsWith("Run")){
	    getExcesses();
	    // send the email	    
	    // if(activeMail){
		back = sendEmails();
		if(!back.isEmpty()){
		    addError(back);
		}
		// }	    
	}
	else if(action.equals("Schedule")){
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
	else{		
	    getExcesses();
	}
	return ret;
    }
    public boolean hasExcesses(){
	getExcesses();
	return excesses != null && excesses.size() > 0;
    }
		
    public String getExcessAccrualsTitle(){
	return excessAccrualsTitle;
    }
    public String getDate(){
	if(date.isEmpty()){
	    date = Helper.getToday();
	}
	return date;
    }
	
    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
	    action = val;
    }
    public void setDate(String val){
	if(val != null && !val.isEmpty())		
	    date = val;
    }    
    public void setAccrual_id(String val){
	if(val != null && !val.isEmpty())		
	    accrual_id = val;
    }
    public void setTresholdValue(String val){
	if(val != null && !val.isEmpty())		
	    threshold_value = val;
    }    
    public List<ExcessAccrual> getExcesses(){
	if(excesses == null){
	    ExcessAccrualList tl = new ExcessAccrualList();
	    tl.setAccrual_id(accrual_id);
	    tl.setThresholdValue(threshold_value);
	    String back = tl.find();
	    if(back.isEmpty()){
		List<ExcessAccrual> ones = tl.getExcesses();
		if(ones != null && ones.size() > 0){
		    excesses = ones;
		}
	    }
	}
	return excesses;
    }
    private String doClean(){
	String msg = "";
	if(quartzMisc != null){
	    msg = quartzMisc.doClean("excess_accrual"); // job group
	}
	return msg;
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
    public boolean hasLogs(){
	getLogs();
	return logs != null && logs.size() > 0;
    }
    public List<ExcessAccrualEmailLog> getLogs(){
	if(logs == null){
	    ExcessAccrualEmailLogList el = new ExcessAccrualEmailLogList();
	    String back = el.find();
	    if(back.isEmpty()){
		List<ExcessAccrualEmailLog> ones = el.getLogs();
		if(ones != null && ones.size() > 0){
		    logs = ones;
		}
	    }
	    else{
		addError(back);
	    }
	}
	return logs;
    }
    String sendEmails(){
	String back = "";
	if(excesses != null && excesses.size() > 0){
	    for(ExcessAccrual one:excesses){
		Employee supervisor = one.getSupervisor();
		System.err.println(one.getEmployeeName()+" "+one.getAccrualValue()+" "+supervisor);
		back += composeAndEmail(one, supervisor);
		break; //only one
	    }
	}
	return back;
    }
    String composeAndEmail(ExcessAccrual excess, Employee supervisor){
	String back = "";
	System.err.println(" emp email "+excess.getEmployee_email());
	System.err.println(" emp email "+supervisor.getEmail());
	if(excess == null){
	    back = "No excess accrual record";
	    return back;
	}
	String email_from = "no_reply@bloomington.in.gov";
	String email_to = "sibow@bloomington.in.gov";// excess.getEmployee_email();
	String email_cc = null;//supervisor.getEmail()+","+hr_email;
	String subject = "[Time Track] Excess Accrual hours for "+excess.getEmployeeName();
	String email_msg = "Your "+excess.getAccrualValue()+" hours of accrued comp time exceeds "+excess.getTresholdValue()+". It is recommend that you comp time when you take time off instead the other Leave to reduce your accrued comp time balance.";	
	
	
	MailHandle mailer = new
	    MailHandle(mail_host,
		       email_to,
		       email_from,
		       email_cc, // cc
		       null,
		       subject,
		       email_msg
		       );
	back += mailer.send();
	String sent_error = null;
	if(!back.isEmpty()){
	    sent_error = back;
	}
	ExcessAccrualEmailLog elog = new ExcessAccrualEmailLog(email_from,email_to,email_cc,subject, email_msg, sent_error);
	back += elog.doSave();
	return back;
    }    


}





































