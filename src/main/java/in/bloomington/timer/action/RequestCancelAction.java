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
    RequestCancel request = null;
    Employee approver = null;
    Employee processor = null;
    String notification_status = "";
    public String execute(){
	String ret = SUCCESS;
	String back = "";
	if(action.equals("Submit")){
	    if(user == null)
		getUser();
	    getRequest();
	    request.setRequestBy_id(user.getId());
	    if(request.hasApprover()){
		approver = request.getApprover();
	    }
	    if(request.hasProcessor()){
		processor = request.getProcessor();
	    }
	    back = notifyManagers();
	    if(back.isEmpty()){
		notification_status = "Success ";
		request.setNotificationStatus("Success");
	    }
	    else{
		notification_status = "Failure ";
		request.setNotificationStatus("Failure");
	    }
	    back = request.doSave();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		if(notification_status.equals("Success")){
		    back = "Saved Successfully and the supervisor(s) informed";
		}
		addMessage(back);
		id = request.getId();
	    }
	}				
	else{
	    if(user == null)
		getUser();
	    getRequest();
	    if(!document_id.isEmpty()){
		request.setDocument_id(document_id);
		request.setRequestBy_id(user.getId());
	    }
	}
	return ret;
    }
    public RequestCancel getRequest(){
	if(request == null){
	    request = new RequestCancel();
	}
	return request;
    }
    public void setRequest(RequestCancel val){
	if(val != null){
	    request = val;
	}
    }
    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
	    action = val;
    }
    private String notifyManagers(){
	String back = "";
	if(approver != null || processor != null){
	    if(activeMail){
		String to = "", cc="", email_from="", subject="", message="";
		getUser();
		if(user != null){
		    if(!user.getEmail().isEmpty()){
			email_from = user.getEmail();
		    }
		    else{
			email_from = "donotreply@bloomington.in.gov";
		    }
		}
		if(approver != null){
		    to = approver.getFull_name()+"<"+approver.getEmail()+">";
		}
		if(processor != null){
		    if(to.isEmpty()){
			to = processor.getFull_name()+"<"+processor.getEmail()+">";		    }
		    else{
			cc = processor.getFull_name()+"<"+processor.getEmail()+">";
		    }
		}
		subject = "Approval cancel request from employee "+user;
		message = "";
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
		back = mail.send();
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





































