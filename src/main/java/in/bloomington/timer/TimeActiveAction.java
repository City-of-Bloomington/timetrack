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
import in.bloomington.timer.util.*;
import in.bloomington.timer.report.*;
import in.bloomington.timer.timewarp.WarpEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TimeActiveAction extends TopAction{

		static final long serialVersionUID = 1800L;	
		static Logger logger = LogManager.getLogger(TimeActiveAction.class);
		//
		TimeActiveAudit audit = null;
		List<List<String>> entries = null;

		String start_date="", end_date="";
		public String execute(){
				String ret = SUCCESS;
				String back = doPrepare();
				if(!action.equals("")){
						back = audit.find();
						if(!back.equals("")){
								addError(back);
						}
						else{
								if(audit.hasEntries()){
										entries = audit.getEntries();
								}
								else{
										addMessage("No records found ");
								}
						}
				}
				else{
						getAudit();
				}
				return ret;
		}
		public void setAction2(String val){
				if(val != null && !val.equals(""))		
						action = val;
		}
		public TimeActiveAudit getAudit(){
				if(audit == null)
						audit = new TimeActiveAudit();
				return audit;
		}
		public void setAudit(TimeActiveAudit val){
				if(val != null)
						audit = val;
		}
		public boolean hasEntries(){
				return entries != null && entries.size() > 0;
		}
		public List<List<String>> getEntries(){
				return entries;
		}		
				
}





































