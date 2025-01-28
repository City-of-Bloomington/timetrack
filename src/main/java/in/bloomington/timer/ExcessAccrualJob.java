package in.bloomington.timer;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */

import java.util.*;
import java.sql.*;
import java.io.*;
import java.text.*;
import javax.sql.*;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobDataMap;
import javax.servlet.ServletContext;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExcessAccrualJob implements Job{

    boolean debug = true;
    static final long serialVersionUID = 55L;		
    static Logger logger = LogManager.getLogger(ExcessAccrualJob.class);
    String accrual_id="3";
    String threshold_value = "40";
    String hr_email = "hrmail@bloomington.in.gov";
    String mail_host = "";
    boolean active_mail = false;
    public ExcessAccrualJob(){

	
    }
    public void execute(JobExecutionContext context)
        throws JobExecutionException {
	try{
	    /**
	    JobDataMap dataMap = context.getJobDetail().getJobDataMap();
	    if(dataMap != null){
		String val = (String) dataMap.get("active_mail");
		if(val != null && val.equals("true")){
		    active_mail = true;
		}
		val = (String) dataMap.get("mail_host");
		if(val != null){
		    mail_host = val;
		}		
	    }
	    */
	    doInit();
	    doWork();
	    doDestroy();
	}
	catch(Exception ex){
	    logger.error(ex);
	    System.err.println(ex);
	}
    }
    public void doInit(){
	ServletContext ctx = SingleContextHolder.getContext();
	if(ctx != null){
	    System.err.println(" ctx is Ok");				
	    String val = ctx.getInitParameter("activeMail");
	    if(val != null && val.equals("true")){
		active_mail = true;
	    }
	    val = ctx.getInitParameter("mail_host");
	    if(val != null && !val.isEmpty()){
		mail_host = val;
	    }	    
	    System.err.println(" Active mail? "+active_mail);										
	}
	else{
	    System.err.println(" ctx is null, could not retreive activeMail flag ");
	}
	
    }
    public void doDestroy() {

    }	    

    public void doWork(){
	List<ExcessAccrual> excesses = getExcesses();
	if(excesses != null && excesses.size() > 0){
	    for(ExcessAccrual one:excesses){
		Employee supervisor = one.getSupervisor();
		System.err.println(" Excess Accrual "+one.getEmployeeName()+" "+one.getAccrualValue());
		if(active_mail){
		    String back = composeAndEmail(one, supervisor);
		    if(!back.isEmpty()){
			logger.error(back);
		    }
		}
	    }
	}	
    }
    public List<ExcessAccrual> getExcesses(){
	List<ExcessAccrual> excesses = null;
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
	return excesses;
    }

    String composeAndEmail(ExcessAccrual excess, Employee supervisor){
	String back = "";
	    
	if(excess == null){
	    back = "No excess accrual record";
	    return back;
	}
	String email_from = "no_reply@bloomington.in.gov";
	String email_to = excess.getEmployee_email();
	String email_cc = supervisor.getEmail()+","+hr_email;
	String subject = "[Time Track] Excess Comp Time Accrual for "+excess.getEmployeeName();
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
	back = mailer.send();
	String sent_error = null;
	if(!back.isEmpty()){
	    sent_error = back;
	}
	ExcessAccrualEmailLog elog = new ExcessAccrualEmailLog(email_from,email_to,email_cc,subject, email_msg, sent_error);
	back += elog.doSave();
	return back;
    }    

}






















































