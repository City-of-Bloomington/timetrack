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

public class EmployeesUpdateJob implements Job{

    boolean debug = true;
		boolean run_employee_update = true;
		static final long serialVersionUID = 55L;		
		static Logger logger = LogManager.getLogger(EmployeesUpdateJob.class);
		EnvBean envBean = null;
		public EmployeesUpdateJob(){

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
				// since we can get envBean from context 
				// HandleEmployeesUpdate handle = new HandleEmployeesUpdate(envBean);
				HandleEmployeesUpdate handle = new HandleEmployeesUpdate();				
				String msg = handle.process();
				if(!msg.equals(""))
						logger.error(msg);
		}
		
}






















































