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

public class EmpPayRateJob implements Job{

    boolean debug = true;
    static final long serialVersionUID = 55L;		
    static Logger logger = LogManager.getLogger(EmpPayRateJob.class);
    static List<Department> depts = null;
    public EmpPayRateJob(){

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
	if(!msg.isEmpty()){
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
	String week_no = "", pay_period_id="";
	PayPeriodList ppl = new PayPeriodList();
	ppl.setOnePeriodAheadOnly();
	msg = ppl.find();
	if(!msg.isEmpty()){
	    logger.error(msg);
	    return;
	}
	else{
	    String today = Helper.getToday();
	    List<PayPeriod> ones = ppl.getPeriods();
	    if(ones != null && ones.size() > 0){
		PayPeriod nextPayPeriod = ones.get(0);// next period
		PayPeriod currentPay = ones.get(1); // current
		week_no = currentPay.isDateInWeekOne(today)?"1":"2";
		if(week_no.equals("2")){
		    pay_period_id = nextPayPeriod.getId();
		    week_no = "1";
		}
		else{
		    pay_period_id = currentPay.getId();
		    week_no = "2";
		}
	    }
	}
	if(depts != null){
	    for(Department dept:depts){
		HandleEmpPayRate handle = new HandleEmpPayRate(dept.getRef_id(), pay_period_id, week_no);
		msg = handle.process();
	    }
	}
    }

}






















































