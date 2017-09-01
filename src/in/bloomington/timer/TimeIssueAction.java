package in.bloomington.timer;
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
import org.apache.log4j.Logger;
import in.bloomington.timer.list.*;
import in.bloomington.timer.bean.*;

public class TimeIssueAction extends TopAction{

		static final long serialVersionUID = 3800L;	
		static Logger logger = Logger.getLogger(TimeIssueAction.class);
		//
		String document_id = "", time_block_id="", reported_by="";
		String timeIssuesTitle = "Time Issues";
		TimeIssue timeIssue = null;		
		List<TimeIssue> timeIssues = null;

		public String execute(){
				String ret = SUCCESS;
				String back = doPrepare();
				// for this login is not required
				if(action.equals("Save")){
						// timeIssue.setReported_by(user.getEmployee_id());
						back = timeIssue.doSave();
						if(!back.equals("")){
								addActionError(back);
						}
						else{
								addActionMessage("Saved Successfully");
						}
				}
				if(action.startsWith("Close")){
						timeIssue.setClosed_by(user.getEmployee_id());
						back = timeIssue.doClose();
						if(!back.equals("")){
								addActionError(back);
						}
						else{
								addActionMessage("Closed Successfully");
						}
				}				
				else{		
						getTimeIssue();
						if(!id.equals("")){
								back = timeIssue.doSelect();
								if(!back.equals("")){
										addActionError(back);
								}
						}
				}
				return ret;
		}
		public TimeIssue getTimeIssue(){
				if(timeIssue == null){
						timeIssue = new TimeIssue();
						timeIssue.setId(id);
						timeIssue.setTime_block_id(time_block_id);
						timeIssue.setReported_by(reported_by);
				}
				return timeIssue;
						
		}
		public void setTimeIssue(TimeIssue val){
				if(val != null){
						timeIssue = val;
				}
		}
		public String getTimeIssuesTitle(){
				return timeIssuesTitle;
		}

		public void setAction2(String val){
				if(val != null && !val.equals(""))		
						action = val;
		}
		public void setDocument_id(String val){
				if(val != null && !val.equals(""))		
						document_id = val;
		}
		public void setReported_by(String val){
				if(val != null && !val.equals(""))		
						reported_by = val;
		}		
		public void setTime_block_id(String val){
				if(val != null && !val.equals(""))		
						time_block_id = val;
		}		
		public List<TimeIssue> getTimeIssues(){
				if(timeIssues == null){
						TimeIssueList tl = new TimeIssueList();
						tl.setTime_block_id(time_block_id);
						tl.setDocument_id(document_id);
						String back = tl.find();
						if(back.equals("")){
								List<TimeIssue> ones = tl.getTimeIssues();
								if(ones != null && ones.size() > 0){
										timeIssues = ones;
								}
						}
				}
				return timeIssues;
		}

}





































