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

public class TimeActionAction extends TopAction{

    static final long serialVersionUID = 4350L;	
    static Logger logger = LogManager.getLogger(TimeActionAction.class);
    //
    TimeAction timeAction = null;
    String timeActionsTitle = "Time Actions";
    String document_id = "", workflow_id="";
    String source = ""; // where we are coming from
    //
    public String execute(){
				String ret = SUCCESS;
				String back = doPrepare("timeAction");
				if(action.startsWith("Submit")){
						getTimeAction();
						getUser();
						if(user != null){
								timeAction.setAction_by(user.getId());
								back = timeAction.doSave();
								if(!back.isEmpty()){
										addError(back);
								}
								else{ // normally we return to TimeDetails
										addMessage("Saved Successfully");
								}
						}
				}
				else if(action.startsWith("Cancel")){
						getTimeAction();
						getUser();
						if(user != null){
								timeAction.setCancelled_by(user.getId());
								back = timeAction.doCancel();
								if(!back.isEmpty()){
										addError(back);
								}
								else{ // normally we return to TimeDetails
										addMessage("Cancelled Successfully");
										ret = "timeDetails";
								}
						}
				}				
				else{		
						getTimeAction();
						if(!id.isEmpty()){
								back = timeAction.doSelect();
								if(!back.isEmpty()){
										addError(back);
								}
								else{
										document_id = timeAction.getDocument_id();
										workflow_id = timeAction.getWorkflow_id();
								}
						}
				}
				if(!source.isEmpty()){
						return source;
				}
				return ret;
    }
    public TimeAction getTimeAction(){ 
				if(timeAction == null){
						timeAction = new TimeAction();
						timeAction.setId(id);
						timeAction.setDocument_id(document_id);
						timeAction.setWorkflow_id(workflow_id);
				}
				return timeAction;
    }
    public void setTimeAction(TimeAction val){
				if(val != null){
						timeAction = val;
				}
    }
    public String getTimeActionsTitle(){
				return timeActionsTitle;
    }
    public void setAction2(String val){
				if(val != null && !val.isEmpty())		
						action = val;
    }
    public void setSource(String val){
				if(val != null && !val.isEmpty())		
						source = val;
    }		
    //
    // this is passed through the link
    public String getDocument_id(){
				if(document_id.isEmpty() && timeAction != null){
						document_id = timeAction.getDocument_id();
				}
				return document_id;
    }
    public void setDocument_id(String val){
				if(val != null && !val.isEmpty())		
						document_id = val;
    }
    public void setWorkflow_id(String val){
				if(val != null && !val.isEmpty())		
						workflow_id = val;
    }		
    public String getWorkflow_id(){
				return workflow_id;
    }
}





































