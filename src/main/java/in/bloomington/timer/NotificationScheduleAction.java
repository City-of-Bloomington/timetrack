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
import in.bloomington.timer.list.*;
import in.bloomington.timer.bean.*;
import in.bloomington.timer.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NotificationScheduleAction extends TopAction{

		static final long serialVersionUID = 3850L;	
		static Logger logger =
				LogManager.getLogger(NotificationScheduleAction.class);
		//
		// List<Department> depts = null;
		List<PayPeriod> periods = null;
		String notificationSchedulesTitle = "Notification Schedules";
		QuartzMisc quartzMisc = null;
		NotificationScheduler schedular = null;
		String prev_date="", next_date="", pay_period_id="", date="";
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
				clearAll();
				prepareSchedular();				
				if(action.equals("Schedule")){
						back = doClean();
						if(!back.equals("")){
								addActionError(back);
								addError(back);
						}
						try{
								back = schedular.run();
								if(!back.equals("")){
										addActionError(back);
										addError(back);
								}
								else{
										if(quartzMisc != null){
												prev_date = quartzMisc.getPrevScheduleDate();
												if(prev_date.startsWith("1969")) // 0 cuases 1969 schedule date
														prev_date = "No Previous date found";
												next_date = quartzMisc.getNextScheduleDate();
										}
										addActionMessage("Scheduled Successfully");
										addMessage("Scheduled Successfully");										
								}
						}catch(Exception ex){
								addActionError(""+ex);
								addError(""+ex);
						}
				}
				else if(action.startsWith("Notify")){ 
						if(pay_period_id.equals("")){
								addActionError("Pay period not selected");
								addError("Pay period not selected");
						}
						else{
								HandleNotification handle = new HandleNotification(pay_period_id, activeMail);
								back = handle.process();
								if(!back.equals("")){
										addActionError(back);
										addError(back);
								}
								else{
										addActionMessage("Notifications Processed Successfully");
										addMessage("Notifications Processed Successfully");
								}
						}
				}
				return ret;
		}
		private void prepareSchedular(){
				String msg = "";
				PayPeriodList pl = new PayPeriodList();
				if(date.equals("")){
						pl.setLastPayPeriod();
						msg = pl.find();
						if(msg.equals("")){
								List<PayPeriod> ones = pl.getPeriods();
								if(ones != null && ones.size() > 0){
										PayPeriod one = ones.get(0);
										String end_date = one.getEnd_date();
										date = Helper.getDateAfter(end_date, 1);
								}
						}
				}
				if(!date.equals("")){
						schedular = new NotificationScheduler(date, activeMail);
				}
				quartzMisc = new QuartzMisc("notification");
				msg = quartzMisc.findScheduledDates();
				if(msg.equals("")){
						prev_date = quartzMisc.getPrevScheduleDate();
						if(prev_date.startsWith("1969")) // 0 cuases 1969 schedule date
								prev_date = "No Previous date found";
						next_date = quartzMisc.getNextScheduleDate();
				}				
		}
		private String doClean(){
				String msg = "";
				if(quartzMisc != null){
						msg = quartzMisc.doClean();
				}
				return msg;
		}
		public String getNotificationSchedularsTitle(){
				
				return notificationSchedulesTitle;
		}

		public void setAction2(String val){
				if(val != null && !val.equals(""))		
						action = val;
		}
		public void setDate(String val){
				if(val != null && !val.equals(""))		
						date = val;
		}
		/*
		public void setDept_id(String val){
				if(val != null && !val.equals("-1"))		
						dept_id = val;
		}
		*/
		public void setPay_period_id(String val){
				if(val != null && !val.equals("-1"))		
						pay_period_id = val;
		}
		public String getPay_period_id(){
				if(pay_period_id.equals(""))
						return "-1";
				return pay_period_id;
		}
		// read only 
		public String getDate(){
				return date;
		}
		public String getPrev_date(){
				return prev_date;
		}
		public String getNext_date(){
				return next_date;
		}
		public boolean hasPrevDates(){
				return !prev_date.equals("");
		}
		/*
		public List<Department> getDepts(){
				if(depts == null){
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
				return depts;
		}
		public boolean hasDepts(){
				getDepts();
				return depts != null && depts.size() > 0;
		}
		*/
		public List<PayPeriod> getPeriods(){
				if(periods == null){
						PayPeriodList dl = new PayPeriodList();
						dl.avoidFuturePeriods();
						dl.setLimit("5");
						String msg = dl.find();
						if(!msg.equals("")){
								logger.error(msg);
						}
						else{
								List<PayPeriod> ones = dl.getPeriods();
								if(ones != null && ones.size() > 0){
										periods = ones;
								}
						}
				}
				return periods;
		}
		public boolean hasPeriods(){
				getPeriods();
				return periods != null && periods.size() > 0;
		}		
		
}





































