package in.bloomington.timer;

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
				String back = doPrepare();
				if(!back.equals("")){
						try{
								HttpServletResponse res = ServletActionContext.getResponse();
								String str = url+"Login";
								res.sendRedirect(str);
								return super.execute();
						}catch(Exception ex){
								System.err.println(ex);
						}	
				}
				if(action.startsWith("Submit")){
						getTimeAction();
						timeAction.setAction_by(user.getId());
						back = timeAction.doSave();
						if(!back.equals("")){
								addActionError(back);
						}
						else{ // normally we return to TimeDetails
								addActionMessage("Submitted Successfully");
								/*
								if(source.equals("timeDetails")){
										String str = url+"timeDetails.action?document_id="+document_id;
										try{
												HttpServletResponse res = ServletActionContext.getResponse();
												res.sendRedirect(str);
												return super.execute();
										}catch(Exception ex){
												System.err.println(ex);
										}											
								}
								*/
						}
				}				
				else{		
						getTimeAction();
						if(!id.equals("")){
								back = timeAction.doSelect();
								if(!back.equals("")){
										addActionError(back);
								}
								else{
										document_id = timeAction.getDocument_id();
										workflow_id = timeAction.getWorkflow_id();
								}
						}
				}
				if(!source.equals("")){
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
				if(val != null && !val.equals(""))		
						action = val;
		}
		public void setSource(String val){
				if(val != null && !val.equals(""))		
						source = val;
		}		
		//
		// this is passed through the link
		public String getDocument_id(){
				if(document_id.equals("") && timeAction != null){
						document_id = timeAction.getDocument_id();
				}
				return document_id;
		}
		public void setDocument_id(String val){
				if(val != null && !val.equals(""))		
						document_id = val;
		}
		public void setWorkflow_id(String val){
				if(val != null && !val.equals(""))		
						workflow_id = val;
		}		
		public String getWorkflow_id(){
				return workflow_id;
		}
}





































