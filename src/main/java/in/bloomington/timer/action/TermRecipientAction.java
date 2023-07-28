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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TermRecipientAction extends TopAction{

    static final long serialVersionUID = 3800L;	
    static Logger logger = LogManager.getLogger(TermRecipientAction.class);
    //
    String recipientsTitle = "Current Termination Nofication Recipients";
    TermRecipient recipient = null;
    List<TermRecipient> recipients = null;

    public String execute(){
	String ret = SUCCESS;
	String back = canProceed("termRecipient.action");
	if(!back.isEmpty()){
	    return back;
	}
	if(action.equals("Save")){
	    back = recipient.doSave();
	    if(!back.isEmpty()){
		addError(back);
		addActionError(back);
	    }
	    else{
		addMessage("Saved Successfully");
	    }
	}				
	else if(action.startsWith("Save")){
	    back = recipient.doUpdate();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		addMessage("Saved Successfully");
	    }
	}
	else if(action.startsWith("Remove")){
	    if(recipient == null){
		getRecipient();
	    }
	    back = recipient.doDelete();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		addMessage("Deleted Successfully");
	    }
	}	
	else{		
	    getRecipient();
	    if(!id.isEmpty()){
		back = recipient.doSelect();
		if(!back.isEmpty()){
		    addError(back);
		}
	    }
	}
	return ret;
    }
    public TermRecipient getRecipient(){
	if(recipient == null){
	    recipient = new TermRecipient();
	    recipient.setId(id);
	}
	return recipient;
						
    }
    public void setRecipient(TermRecipient val){
	if(val != null){
	    recipient = val;
	}
    }
		
    public String getRecipientsTitle(){
				
	return recipientsTitle;
    }

    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
	    action = val;
    }
    public List<TermRecipient> getRecipients(){
	if(recipients == null){
	    TermRecipientList tl = new TermRecipientList();
	    String back = tl.find();
	    if(back.isEmpty()){
		List<TermRecipient> ones = tl.getRecipients();
		if(ones != null && ones.size() > 0){
		    recipients = ones;
		}
	    }
	}
	return recipients;
    }

}





































