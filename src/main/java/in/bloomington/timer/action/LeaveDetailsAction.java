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
import in.bloomington.timer.action.TopAction;
import in.bloomington.timer.list.*;
import in.bloomington.timer.bean.*;
import in.bloomington.timer.leave.*;
import in.bloomington.timer.util.Helper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LeaveDetailsAction extends TopAction{

		static final long serialVersionUID = 4300L;	
		static Logger logger = LogManager.getLogger(LeaveDetailsAction.class);
		DecimalFormat df = new DecimalFormat("###.00");
		//
		PayPeriod payPeriod = null, currentPayPeriod=null,
				previousPayPeriod=null, nextPayPeriod=null;
		String leaveBlocksTitle = "Leave Details";
		String pay_period_id = "";
		String document_id = "", job_id="";
		LeaveDocument document = null;
		String date = "", source="";
		JobTask job = null;
		List<JobTask> jobs = null;
		List<PayPeriod> payPeriods = null;
		public String execute(){
				String ret = SUCCESS;
				String back = doPrepare("leaveDetails.action");
				if(hasNoJob()){
						addError("No job found for employee ");
						addActionError("No job found for employee ");						
				}
				if(action.equals("View")){
						ret = "view";
				}
				return ret;
		}
		public String getLeaveBlocksTitle(){
				return leaveBlocksTitle;
		}
		public void setAction2(String val){
				if(val != null && !val.isEmpty())		
						action = val;
		}
		public void setSource(String val){
				if(val != null && !val.isEmpty())		
						source = val;
		}
		//
		// This is the first thing that will be called on timedetails page		
		// check if we have document_id, if not we assume
		// it is a new pay period and we will create one
		//
		public String getDocument_id(){
				//
				if(document_id.isEmpty()){
						LeaveDocumentList dl = new LeaveDocumentList();
						if(employee_id.isEmpty()){
								getEmployee_id();
						}
						dl.setEmployee_id(employee_id);
						if(pay_period_id.isEmpty()){
								getPayPeriod();
						}
						dl.setPay_period_id(pay_period_id);
						if(job_id.isEmpty()){
								getJob();
						}
						dl.setJob_id(job_id);						
						String back = dl.find();
						if(back.isEmpty()){
								List<LeaveDocument> ones = dl.getDocuments();
								if(ones != null && ones.size() > 0){
										document = ones.get(0);
										document_id = document.getId();
								}
						}
				}
				return document_id;
		}
		public LeaveDocument getDocument(){
				if(document == null && !document_id.isEmpty()){
						LeaveDocument one = new LeaveDocument(document_id);
						String back = one.doSelect();
						if(back.isEmpty()){
								document = one;
						}
				}
				return document;
		}
		public void setDocument_id(String val){
				if(val != null && !val.isEmpty())		
						document_id = val;
		}
		public void setPay_period_id(String val){
				if(val != null && !val.isEmpty())		
						pay_period_id = val;
		}
		public void setJob_id(String val){
				if(val != null && !val.equals("-1"))		
						job_id = val;
		}		
		public void setDate(String val){
				if(val != null && !val.isEmpty())		
						date = val;
		}
		public String getPay_period_id(){
				if(pay_period_id.isEmpty() && !document_id.isEmpty()){
						getDocument();
						if(document != null)
								pay_period_id = document.getPay_period_id();
				}
				return pay_period_id;
		}
		public String getSource(){
				return source;
		}
		public List<PayPeriod> getPayPeriods(){
				if(payPeriods == null){
						getPayPeriod(); // so that we can initialize the list
						PayPeriodList tl = new PayPeriodList();
						tl.setTwoPeriodsAheadOnly();
						tl.setEmployee_id(employee_id);
						String back = tl.find();
						if(back.isEmpty()){
								List<PayPeriod> ones = tl.getPeriods();
								if(ones != null && ones.size() > 0){
										payPeriods = ones;
								}
						}
				}
				return payPeriods;
		}		
		public PayPeriod getPayPeriod(){
				//
				// if pay period is not set, we look for current one
				//
				if(payPeriod == null){
						if(pay_period_id.isEmpty()){
								if(document_id.isEmpty()){
										PayPeriodList ppl = new PayPeriodList();
										ppl.currentOnly();
										String back = ppl.find();
										if(back.isEmpty()){
												List<PayPeriod> ones = ppl.getPeriods();
												if(ones != null && ones.size() > 0){
														payPeriod = ones.get(0);
														pay_period_id = payPeriod.getId();
												}
										}
								}
								else{
										getDocument();
										if(document != null){
												payPeriod = document.getPayPeriod();
												pay_period_id = document.getPay_period_id();
										}
								}
						}
						else{
								PayPeriod one = new PayPeriod(pay_period_id);
								String back = one.doSelect();
								if(back.isEmpty()){
										payPeriod = one;
								}
						}
				}
				return payPeriod;
		}
		public PayPeriod getCurrentPayPeriod(){
				//
				if(currentPayPeriod == null){
						PayPeriodList ppl = new PayPeriodList();
						ppl.currentOnly();
						String back = ppl.find();
						if(back.isEmpty()){
								List<PayPeriod> ones = ppl.getPeriods();
								if(ones != null && ones.size() > 0){
										currentPayPeriod = ones.get(0);
										if(pay_period_id.isEmpty()){
												pay_period_id = currentPayPeriod.getId();
												payPeriod = currentPayPeriod;
										}
								}
						}
				}
				return currentPayPeriod;
		}
		public PayPeriod getPreviousPayPeriod(){
				//
				if(previousPayPeriod == null){
						if(pay_period_id.isEmpty())
								getPayPeriod();
						PayPeriodList ppl = new PayPeriodList();
						ppl.setPreviousTo(pay_period_id); // relative to currently used
						String back = ppl.find();
						if(back.isEmpty()){
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
						if(pay_period_id.isEmpty())
								getPayPeriod();
						PayPeriodList ppl = new PayPeriodList();
						ppl.setNextTo(pay_period_id); // relative to this currently used 
						String back = ppl.find();
						if(back.isEmpty()){
								List<PayPeriod> ones = ppl.getPeriods();
								if(ones != null && ones.size() > 0){
										nextPayPeriod = ones.get(0);
								}
						}
				}
				return nextPayPeriod;
		}				
		public boolean isCurrentPayPeriod(){
				getCurrentPayPeriod();
				getPay_period_id();
				return pay_period_id.equals(currentPayPeriod.getId());
		}
		public JobTask getJob(){
				if(job_id.isEmpty() && job == null){
						getJobs();
						if(jobs.size() > 1){
								for(JobTask one:jobs){
										if(one.isPrimary()){
												job = one;
												break;
										}
								}
								//
								// if no job is marked as primary,
								// then we pick the first
								if(job == null){
										job = jobs.get(0);
								}
						}
						else if(jobs.size() == 1){
								job = jobs.get(0);
						}
						if(job != null){
								job_id = job.getId();
						}
				}
				else if(!job_id.isEmpty() && job == null){
						JobTask one = new JobTask(job_id);
						String back = one.doSelect();
						if(back.isEmpty()){
								job = one;
						}
				}
				return job;
		}
		public boolean hasJob(){
				getJob();
				return job != null;
		}
		public String getJob_id(){
				return job_id;
		}
		public List<JobTask> getJobs(){
				if(jobs == null){
						JobTaskList jl = new JobTaskList(getEmployee_id());
						if(payPeriod != null){
								jl.setPay_period_id(payPeriod.getId());
						}
						String back = jl.find();
						if(back.isEmpty()){
								List<JobTask> ones = jl.getJobTasks();
								if(ones != null && ones.size() > 0){
										jobs = ones;
										if(jobs.size() == 1){
												job = jobs.get(0);
										}
								}
						}
				}
				return jobs;
		}
		public boolean hasLeaves(){
				getJob();
				if(job != null && job.isLeaveEligible()){
						findDoumentForPlannedLeaves();
				}
				return document != null;
		}
		public boolean canHaveLeave(){
				getJob();
				return job != null && job.isLeaveEligible();
		}		
		//
		// leaves can be plan at least two weeks before
		// given a pay period
		//
		public boolean canPlanLeaveThisPayPeriod(){
				if(canHaveLeave()){
						getPayPeriod(); // based on selected pay_period_id
						return payPeriod != null && payPeriod.isTwoWeekOrMoreInFuture();
				}
				return false;
		}
		//
		// if the employee is allowed to plan leave for this pay priod
		// then we can create a new document (if not alreay exist)
		public void prepareDocument(){
				if(canPlanLeaveThisPayPeriod()){
						findDoumentForPlannedLeaves();
						if(document == null){
								getJob();
								LeaveDocument one = new LeaveDocument();
								one.setEmployee_id(employee_id);
								one.setPay_period_id(pay_period_id);
								one.setJob_id(job.getId());
								one.setInitiated_by(user.getId());
								String back = one.doSave();
								if(back.isEmpty()){
										document = one;
								}
								else{
										addError(back);
								}
						}
				}
		}
		
		public boolean hasNoJob(){
				getJobs();
				return jobs == null || jobs.size() == 0;
		}
		public boolean hasMultipleJobs(){
				getJobs();
				return jobs != null && jobs.size() > 1;
		}
		void findDoumentForPlannedLeaves(){
				LeaveDocumentList ldl = new LeaveDocumentList();
				getEmployee();
				getPayPeriod();
				if(pay_period_id.isEmpty() || employee_id.isEmpty()){
						addError("no pay period or employee set ");
						return;
				}
				ldl.setEmployee_id(employee_id);
				ldl.setPay_period_id(pay_period_id);
				String back = ldl.find();
				if(back.isEmpty()){
						List<LeaveDocument> ones = ldl.getDocuments();
						if(ones != null){
								document = ones.get(0);
						}
				}
				else{
						addError(back);
				}
		}
				
}





































