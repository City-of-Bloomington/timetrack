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
// import javax.servlet.ServletContext;
// import org.apache.struts2.util.ServletContextAware;  
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TermNotificationJob implements Job{

    static final long serialVersionUID = 55L;		
    static Logger logger = LogManager.getLogger(TermNotificationJob.class);
    public TermNotificationJob(){

    }
    public void execute(JobExecutionContext context)
        throws JobExecutionException {
				
	try{
	    /**
	    JobDataMap dataMap = context.getJobDetail().getJobDataMap();
	    if(dataMap != null){
		String val = dataMap.getString("mail_host");
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

    }
    public void doDestroy() {

    }	    
    public void doWork(){
	HandleTermNotification handle = new
	    HandleTermNotification();
	String msg = handle.process();
	if(!msg.isEmpty())
	    logger.error(msg);
    }

}






















































