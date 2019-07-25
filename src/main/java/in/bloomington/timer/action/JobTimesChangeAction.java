package in.bloomington.timer.action;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JobTimesChangeAction extends TopAction{

		static final long serialVersionUID = 2100L;	
		static Logger logger = LogManager.getLogger(JobTimesChangeAction.class);
		String related_employee_id="";
		String pay_period_id = "", to_job_id="", from_job_id="";
		PayPeriod currentPayPeriod=null, previousPayPeriod=null,
				nextPayPeriod=null, payPeriod = null;		
		Employee relatedEmployee = null;
		JobTimesChange jobTime = null;
		List<JobTask> jobs = null;
		List<PayPeriod> payPeriods = null;
		public String execute(){
				String ret = SUCCESS;
				String back = doPrepare();
				getJobTime();
				if(action.startsWith("Change")){
						jobTime.setEmployee_id(related_employee_id);
						jobTime.setPay_period_id(pay_period_id);
						jobTime.setTo_job_id(to_job_id);
						jobTime.setFrom_job_id(from_job_id);
						back = jobTime.doChange();
						if(!back.equals("")){
								addError(back);
						}
						else{
								addMessage("Changed Successfully");
						}
				}
				return ret;
		}
	 public List<JobTask> getJobs(){
			 if(pay_period_id.equals("")){
					 getPay_period_id();
			 }
			 if(jobs == null &&
					!pay_period_id.equals("") &&
					!related_employee_id.equals("")){
					 JobTaskList jtl = new JobTaskList();
					 jtl.setEmployee_id(related_employee_id);
					 jtl.setPay_period_id(pay_period_id);
					 jtl.setActiveOnly();
					 jtl.setOrderById();
					 String back = jtl.find();
					 if(back.equals("")){ 
							 List<JobTask> ones = jtl.getJobs();
							 if(ones != null && ones.size() > 0){
									 jobs = ones;
							 }
					 }
			 }
			 return jobs;
		}
		public void setJobTime(JobTimesChange val){
				if(val != null)		
						jobTime = val;
		}
		public void getJobTime(){
				if(jobTime == null){		
						jobTime = new JobTimesChange();
						jobTime.setEmployee_id(related_employee_id);
				}
		}		
		public void setAction2(String val){
				if(val != null && !val.equals(""))		
						action = val;
		}
		public void setRelated_employee_id(String val){
				if(val != null && !val.equals("-1"))		
						related_employee_id = val;
		}
		public String getRelated_employee_id(){
				return related_employee_id ;
		}		
		public void setPay_period_id(String val){
				if(val != null && !val.equals("-1"))		
						pay_period_id = val;
		}
		public String getPay_period_id(){
				if(pay_period_id.equals("")){
						getCurrentPayPeriod();
				}
				return pay_period_id;
		}
		public void setTo_job_id(String val){
				if(val != null && !val.equals("-1"))		
						to_job_id = val;
		}
		public String getTo_job_id(){
				return "-1";
		}
		public void setFrom_job_id(String val){
				if(val != null && !val.equals("-1"))		
						from_job_id = val;
		}
		public String getFrom_job_id(){
				return "-1";
		}
		public Employee getRelatedEmployee(){
				if(relatedEmployee == null){
						if(!related_employee_id.equals("")){
								Employee emp = new Employee(related_employee_id);
								String back = emp.doSelect();
								if(emp != null){
										relatedEmployee = emp;
								}
						}
				}
				return relatedEmployee;
		}
		public PayPeriod getCurrentPayPeriod(){
				//
				if(currentPayPeriod == null){
						PayPeriodList ppl = new PayPeriodList();
						ppl.currentOnly();
						String back = ppl.find();
						if(back.equals("")){
								List<PayPeriod> ones = ppl.getPeriods();
								if(ones != null && ones.size() > 0){
										currentPayPeriod = ones.get(0);
										if(pay_period_id.equals("")){
												pay_period_id = currentPayPeriod.getId();
												payPeriod = currentPayPeriod;
										}
								}
						}
				}
				return currentPayPeriod;
		}		
		public List<PayPeriod> getPayPeriods(){
				if(payPeriods == null){
						PayPeriodList tl = new PayPeriodList();
						tl.avoidFuturePeriods();
						tl.setLimit("3");
						String back = tl.find();
						if(back.equals("")){
								List<PayPeriod> ones = tl.getPeriods();
								if(ones != null && ones.size() > 0){
										payPeriods = ones;
								}
						}
				}
				return payPeriods;
		}
		public PayPeriod getPreviousPayPeriod(){
				//
				if(previousPayPeriod == null){
						if(pay_period_id.equals(""))
								getPay_period_id();
						PayPeriodList ppl = new PayPeriodList();
						ppl.setPreviousTo(pay_period_id); // relative to currently used
						String back = ppl.find();
						if(back.equals("")){
								List<PayPeriod> ones = ppl.getPeriods();
								if(ones != null && ones.size() > 0){
										previousPayPeriod = ones.get(0);
								}
						}
				}
				return previousPayPeriod;
		}
		public PayPeriod getNextPayPeriod(){
				//
				if(nextPayPeriod == null){
						if(pay_period_id.equals(""))
								getPay_period_id();						
						PayPeriodList ppl = new PayPeriodList();
						ppl.setNextTo(pay_period_id); 						
						String back = ppl.find();
						if(back.equals("")){
								List<PayPeriod> ones = ppl.getPeriods();
								if(ones != null && ones.size() > 0){
										nextPayPeriod = ones.get(0);
								}
						}
				}
				return nextPayPeriod;
		}
		public boolean hasJobs(){
				if(jobs == null){
						getJobs();
				}
				return jobs != null && jobs.size() > 1;
		}
		public boolean hasPayPeriods(){
				if(payPeriods == null){
						getPayPeriods();
				}
				return payPeriods != null && payPeriods.size() > 0;
		}

}





































