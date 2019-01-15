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
import javax.servlet.ServletContext;
import org.apache.struts2.util.ServletContextAware;  
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NotificationJob implements Job, ServletContextAware{

    boolean debug = true;
		static final long serialVersionUID = 55L;		
		static Logger logger = LogManager.getLogger(NotificationJob.class);
		boolean activeMail = false;
		String mail_host = "";
		PayPeriod lastPayPeriod = null;
    ServletContext ctx = null;		
		public NotificationJob(){

		}
    @Override  	
    public void setServletContext(ServletContext ctx) {  
        this.ctx = ctx;  
    }		
		public void execute(JobExecutionContext context)
        throws JobExecutionException {
				
				try{
						JobDataMap dataMap = context.getJobDetail().getJobDataMap();
						if(dataMap != null){
								String val = dataMap.getString("activeMail");
								/**
								if(val != null && val.equals("true")){
										activeMail = true;
								}
								*/
								val = dataMap.getString("mail_host");
								if(val != null){
										mail_host = val;
								}								
						}
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
				PayPeriodList ppl = new PayPeriodList();
				ppl.setLastPayPeriod();
				String msg = ppl.find();
				if(!msg.equals("")){
						logger.error(msg);
						return;
				}
				else{
						List<PayPeriod> ones = ppl.getPeriods();
						if(ones != null && ones.size() > 0){
								lastPayPeriod = ones.get(0);
						}
				}
				if(ctx != null){
						System.err.println(" Notification job using context ");
						String val = ctx.getInitParameter("activeMail");
						if(val != null && val.equals("true")){
								activeMail = true;
						}
				}
		}
		public void doDestroy() {
				lastPayPeriod = null;
				ctx = null;
		}	    
    public void doWork(){
				System.err.println("Notification Job active mail: "+activeMail);
				if(activeMail){
						if(lastPayPeriod != null){
								HandleNotification handle = new
										HandleNotification(lastPayPeriod,
																			 mail_host,
																			 activeMail);
								String msg = handle.process();
								if(!msg.equals(""))
										logger.error(msg);
						}
				}
		}

}






















































