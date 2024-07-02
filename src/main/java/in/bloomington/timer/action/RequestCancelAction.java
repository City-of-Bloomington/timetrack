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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RequestCancelAction extends TopAction{

    static final long serialVersionUID = 3800L;	
    static Logger logger = LogManager.getLogger(RequestCancelAction.class);
    //
    String document_id="";
    Document document = null;
    RequestCancel requst = null;
    Employee approver = null;
    Employee processor = null;
    String notification_status = "";
    public String execute(){
	doPrepare();
	String ret = SUCCESS;
	String back = "";
	if(action.equals("Submit")){
	    if(user == null)
		getUser();
	    getRequst();
	    requst.setRequestBy_id(user.getId());
	    if(requst.hasApprover()){
		approver = requst.getApprover();
	    }
	    if(requst.hasProcessor()){
		processor = requst.getProcessor();
	    }
	    back = notifyManagers();
	    if(back.isEmpty()){
		notification_status = "Success ";
		requst.setNotificationStatus("Success");
	    }
	    else{
		notification_status = "Failure ";
		requst.setNotificationStatus("Failure");
	    }
	    System.err.println(" saving ");
	    back = requst.doSave();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		if(notification_status.equals("Success")){
		    back = "Saved successfully, supervisor(s) informed";
		}
		addMessage(back);
		id = requst.getId();
	    }
	}				
	else{
	    if(user == null)
		getUser();
	    getRequst();
	    if(!document_id.isEmpty()){
		requst.setDocument_id(document_id);
		requst.setRequestBy_id(user.getId());
	    }
	}
	return ret;
    }
    public RequestCancel getRequst(){
	if(requst == null){
	    requst = new RequestCancel();
	}
	return requst;
    }
    public void setRequst(RequestCancel val){
	if(val != null){
	    requst = val;
	}
    }
    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
	    action = val;
    }
    public void setDocument_id(String val){
	if(val != null && !val.isEmpty())		
	    document_id = val;
    }    
    private String notifyManagers(){
	String back = "";
	if(approver != null || processor != null){
	    // if(activeMail){
	    if(true){
		String to = "", cc="", email_from="", subject="", message="";
		getUser();
		if(user != null){
		    if(!user.getEmail().isEmpty()){
			email_from = user.getEmail();
		    }
		    else{
			email_from = "no_reply@bloomington.in.gov";
		    }
		}
		if(approver != null){
		    to = approver.getFull_name()+"<"+approver.getEmail()+">";
		}
		if(processor != null && approver != null && !approver.equals(processor)){
		    if(to.isEmpty()){
			to = processor.getFull_name()+"<"+processor.getEmail()+">";		    }
		    else{
			cc = processor.getFull_name()+"<"+processor.getEmail()+">";
		    }
		}
		subject = "Approval cancel request from employee "+user;
		message = "The employee "+user.getFull_name()+" requested that your approval for his/her timesheet be cancelled so that an update can be performed regarding certain date(s) and times for the reasons given below;\n";

		message += requst.getRequestReason()+"\n";
		message += "Here is the related document "+
		    "<a href=\""+url+"timeDetails.action?document_id="+requst.getDocument_id()+"\">link</a> \n";		
		message += "If you cancel your approval that means you agree with the change. After the employee changes the related records you need to reapprove the timesheet again. \n"+
		    "If you do not cancel that means you do not agree and the time records will stay the same. You may contact your employee for further details. \n\n"+
		    "Thanks\n\nITS\n";
		System.err.println(message);
		MailHandle mail =
		    new MailHandle(mail_host,
				   to, 
				   email_from,
				   cc,
				   null, // bcc
				   subject,
				   message,
				   debug
				   );
		// back = mail.send();
	    }
	    else{
		back = "email activity flag is turned off, if you need to send email this flag need to be turned on in your configuration file";
		addError(back);
		System.err.println(" after inactive mail ");
	    }
	}
	return back;	
    }

}





































