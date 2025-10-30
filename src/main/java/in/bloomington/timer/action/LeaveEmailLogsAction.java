package in.bloomington.timer.action;
/**
 * @copyright Copyright (C) 2014-2019 City of Bloomington, Indiana. All rights reserved.
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

public class LeaveEmailLogsAction extends TopAction{

    static final long serialVersionUID = 3800L;	
    static Logger logger = LogManager.getLogger(LeaveEmailLogsAction.class);
    //
    String logsTitle = "Most recent leave email logs";
    List<LeaveEmailLog> logs = null;
    LeaveEmailLogList lel = new LeaveEmailLogList();
    public String execute(){
	String ret = SUCCESS;
	String back = canProceed("leaveEmailLogs.action");
	if(!back.isEmpty()){
	    return back;
	}
	if(!action.isEmpty()){
	    lel.setNoLimit();
	    back = lel.find();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		List<LeaveEmailLog> ones = lel.getLogs();
		if(ones != null && ones.size() > 0){
		    logs = ones;
		    logsTitle = " Found "+logs.size()+" emails";
		    addMessage("Found "+logs.size()+" emails");
		}
		else{
		    addMessage("No match found");
		    logsTitle = "No match found";
		}
	    }
	}
	else{

	}
	getLel();
	return ret;
    }
    public LeaveEmailLogList getLel(){
	return lel;
    }
    public String getLogsTitle(){
	return logsTitle;
    }
    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
	    action =val;
    }
    public boolean hasLogs(){
	if(action.isEmpty())
	    getLogs();
	return logs != null && logs.size() > 0;
    }
    public List<LeaveEmailLog> getLogs(){
	if(action.isEmpty()){
	    LeaveEmailLogList tl = new LeaveEmailLogList();
	    String back = tl.find();
	    if(back.isEmpty()){
		List<LeaveEmailLog> ones = tl.getLogs();
		if(ones != null && ones.size() > 0){
		    logs = ones;
		}
	    }
	}
	return logs;
    }

}





































