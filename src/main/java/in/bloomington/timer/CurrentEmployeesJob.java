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
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// not needed anymore

public class CurrentEmployeesJob implements Job{

    boolean debug = true;
		static final long serialVersionUID = 55L;		
		static Logger logger = LogManager.getLogger(CurrentEmployeesJob.class);
		EnvBean envBean = null;
		public CurrentEmployeesJob(){

		}
		public void execute(JobExecutionContext context)
        throws JobExecutionException {
				try{
						JobDataMap dataMap = context.getJobDetail().getJobDataMap();
						if(dataMap != null){
								EnvBean val = (EnvBean) dataMap.get("envBean");
								if(val != null){
										envBean = val;
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

		}
		public void doDestroy() {
		}	    
    public void doWork(){
				HandleCurrentEmployees handle = new HandleCurrentEmployees(envBean);
				String msg = handle.process();
				if(!msg.isEmpty())
						logger.error(msg);
		}
		
}






















































