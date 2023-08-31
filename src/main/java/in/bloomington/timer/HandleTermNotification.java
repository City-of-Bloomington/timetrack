package in.bloomington.timer;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.*;
import java.sql.*;
import java.text.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;
import in.bloomington.timer.list.*;
import javax.servlet.ServletContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HandleTermNotification{

    boolean debug = false, activeMail = false;
    static final long serialVersionUID = 53L;
    static String mail_host = "";
    static String url = "";
    static Logger logger = LogManager.getLogger(HandleTermNotification.class);
    String date="";
    List<EmpTerminate> terms = null;    
    List<Employee> emps = null;
 		
    public HandleTermNotification(){
	setActiveMailFlag();
    }
    void setActiveMailFlag(){
	ServletContext ctx = SingleContextHolder.getContext();
	if(ctx != null){
	    System.err.println(" ctx is Ok");				
	    String val = ctx.getInitParameter("activeMail");
	    if(val != null && val.equals("true")){
		activeMail = true;
	    }
	    val = ctx.getInitParameter("mail_host");
	    if(val != null && !val.isEmpty()){
		mail_host = val;
	    }
	    val = ctx.getInitParameter("url");
	    if(val != null && !val.isEmpty()){
		url = val;
	    }	    
	    System.err.println(" activeMail "+activeMail);
	    System.err.println(" mail_host "+mail_host);	    
	}
	else{
	    System.err.println(" ctx is null, could not retreive activeMail flag ");
	}
    }
    public void setActiveMail(){
	activeMail = true; // overide for testing
    }
    public void setMail_host(String val){
	if(val != null){		
	    mail_host = val;
	}
    }
    //
    // setters
    //
    public void setDate(String val){
	if(val != null){		
	    date = val;
	}
    }
    public void setUrl(String val){
	if(val != null){		
	    url = val;
	}
    }    
    //
    String findNonNotifiedTerms(){
	EmpTerminateList etl = new EmpTerminateList();
	etl.setNotificationReminder();
	String back = etl.find();
	if(back.isEmpty()){
	    List<EmpTerminate> ones = etl.getTerms();
	    if(ones != null && ones.size() > 0){
		terms = ones;
	    }
	}
	return back;
    }
    public String process(){
	String msg = "";
	if(terms == null){
	    msg = findNonNotifiedTerms();
	    if(!msg.isEmpty()){
		return msg;
	    }						
	}
	if(terms == null || terms.size() < 1){
	    msg = "No terminations to process";
	    System.err.println(" Notification: no terminations found ");
	    return msg;
	}
	else{
	    System.err.println(" Found "+terms.size());
	}
	if(activeMail){
	    String success_list="";
	    String failure_list="";
	    String failure_error="";
	    for(EmpTerminate one:terms){
		Employee submitter = one.getSubmitted_by();
		Employee emp = one.getEmployee();
		if(submitter != null && emp != null){
		    String to_str = submitter.getFull_name()+"<"+submitter.getEmail()+">";
		    String msg_txt = composeText(one, emp);
		    msg = doSend(to_str, msg_txt);
		    if(msg.isEmpty()){
			if(!success_list.isEmpty()) success_list +=", "; 
			success_list += to_str;
		    }
		    else{
			if(!failure_list.isEmpty()){
			    failure_list +=", ";
			    failure_error += ", ";
			}
			failure_list += to_str;
			if(failure_error.indexOf(msg) == -1){
			    if(!failure_error.isEmpty()){
				failure_error += ", ";
			    }										    failure_error += msg;
			}
		    }
		    if(!success_list.isEmpty()){
			NotificationLog nlog = new NotificationLog(success_list, msg_txt, "Success",null);
			nlog.doSave();										
		    }
		    if(!failure_list.isEmpty()){
			NotificationLog nlog = new NotificationLog(failure_list, msg_txt, "Failure", failure_error);
			nlog.doSave();		
		    }						
		}
	    }
	}
	else{
	    System.err.println(" active mail is turned off, no email sent");
	}
	return msg;
    }
    String composeText(EmpTerminate term, Employee emp){
	String body_text ="This is an automated message from the TimeTrack timekeeping system.\n\n"+
	    "On"+term.getSubmitted_date()+" you submitted an employee termination request for .\n\n"+
	    emp.getFull_name()+":\n\n"+
	    "for the job(s): "+term.getJobTitles()+"\n\n"+
	    "To send the termination notifications for other departments and complete the termination proces  go to <a href=\""+url+"\">TimeTack </a>\n"+ 
	    "After you login \n\n"+
	    "'My Groups' tab, you will see the list of your previous terminations that lack notification\n\n."+
	    "If you have any questions or support needs, please contact the ITS Helpdesk at (812) 349-3454 or helpdesk@bloomington.in.gov for assistance.\n\n"+
	    "Usage guidelines are available here:\n"+
	    "https://docs.google.com/document/d/1krOtCGtJ_SaPCOBF0cv5bMu-q6Buv1KI68PjS0TrZl0/edit#heading=h.ye6yj01u0is4\n\n"+
	    "Thank you\n"+
	    "\n\n";
	return body_text;
    }
		
    String doSend(String to_str, String msg_txt){
	String msg = "";
	Properties props = new Properties();
	props.put("mail.smtp.host", mail_host);
				
	Session session = Session.getDefaultInstance(props, null);
	try{
	    Message message = new MimeMessage(session);					    message.setSubject("Employee Termination Notification Reminder");
	    message.setText(msg_txt);
	    message.setFrom(new InternetAddress(CommonInc.fromEmailStr));
	    InternetAddress[] addrArray = InternetAddress.parse(to_str);
	    message.setRecipients(Message.RecipientType.TO, addrArray);
	    // message.setRecipients(Message.RecipientType.BCC, addrArray);
	    Transport.send(message);
	}
	catch (MessagingException mex){
	    //
	    // Failure
	    msg += mex;
	    //
	    logger.error(mex);
	    Exception ex = mex;
	    do {
		if (ex instanceof SendFailedException) {
		    SendFailedException sfex = (SendFailedException)ex;
		    javax.mail.Address [] invalid = sfex.getInvalidAddresses();
		    if (invalid != null) {
			logger.error("    ** Invalid Addresses");
			if (invalid != null) {
			    for (int i = 0; i < invalid.length; i++) 
				logger.error("         " + invalid[i]);
			}
		    }
		    javax.mail.Address [] validUnsent = sfex.getValidUnsentAddresses();
		    if (validUnsent != null) {
			logger.error("    ** ValidUnsent Addresses");
			if (validUnsent != null) {
			    for (int i = 0; i < validUnsent.length; i++) 
				logger.error("         "+validUnsent[i]);
			}
		    }
		    javax.mail.Address [] validSent = sfex.getValidSentAddresses();
		    if (validSent != null) {
			logger.error("    ** ValidSent Addresses");
			if (validSent != null) {
			    for (int i = 0; i < validSent.length; i++) 
				logger.error("         "+validSent[i]);
			}
		    }
		}
		if (ex instanceof MessagingException)
		    ex = ((MessagingException)ex).getNextException();
		else { // any other exception
		    logger.error(ex);
		    ex = null;
		}
	    } while (ex != null);
	} catch (Exception ex){
	    logger.error(ex);
	}						
	return msg;
    }

}
