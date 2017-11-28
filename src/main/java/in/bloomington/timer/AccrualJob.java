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

public class AccrualJob implements Job{

    boolean debug = true;
		static final long serialVersionUID = 55L;		
		static Logger logger = LogManager.getLogger(AccrualJob.class);
		static List<Department> depts = null;
		// static Hashtable<String, BenefitGroup> benGroups = null;
		public AccrualJob(){

		}
		public void execute(JobExecutionContext context)
        throws JobExecutionException {
				try{
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
				DepartmentList dl = new DepartmentList();
				dl.setActiveOnly();
				dl.hasRefIds();
				String msg = dl.find();
				if(!msg.equals("")){
						logger.error(msg);
				}
				else{
						List<Department> ones = dl.getDepartments();
						if(ones != null && ones.size() > 0){
								depts = ones;
						}
				}
		}
		public void doDestroy() {

		}	    

    public void doWork(){
    
				String msg = "";
				String date = "";
				PayPeriodList ppl = new PayPeriodList();
				ppl.setLastPayPeriod();
				msg = ppl.find();
				if(!msg.equals("")){
						logger.error(msg);
						return;
				}
				else{
						List<PayPeriod> ones = ppl.getPeriods();
						if(ones != null && ones.size() > 0){
								PayPeriod period = ones.get(0);// last pay period
								String end_date = period.getEnd_date();
								date = Helper.getDateAfter(end_date, 5); // 5 days after 
						}
				}
				if(!date.equals("") && depts != null){
						for(Department dept:depts){
								HandleNwAccrual handle = new HandleNwAccrual(dept.getRef_id(), date);
								msg = handle.process();
						}
				}
		}

}






















































