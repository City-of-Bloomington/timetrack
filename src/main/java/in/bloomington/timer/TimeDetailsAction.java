package in.bloomington.timer;

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
import in.bloomington.timer.util.Helper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TimeDetailsAction extends TopAction{

		static final long serialVersionUID = 4300L;	
		static Logger logger = LogManager.getLogger(TimeDetailsAction.class);
		DecimalFormat df = new DecimalFormat("###.00");
		//
		PayPeriod payPeriod = null, currentPayPeriod=null,
				previousPayPeriod=null, nextPayPeriod=null;
		String timeBlocksTitle = "Time Details";
		String pay_period_id = "";
		String document_id = "", job_id="";
		Document document = null;
		MultiJobDoc mjdoc = null;
		String date = "", source="";
		JobTask job = null;
		List<JobTask> jobs = null;
		List<Type> jobTypes = null;
		List<PayPeriod> payPeriods = null;
		public String execute(){
				String ret = SUCCESS;
				String back = doPrepare("timeDetails.action");
				getDocument_id();
				if(hasNoJob()){
						addError("No job found for employee ");
						addActionError("No job found for employee ");						
				}
				if(action.equals("View")){
						ret = "view";
				}
				if(showAllJobs()){
						ret = "view";
				}
				return ret;
		}
		public String getTimeBlocksTitle(){
				return timeBlocksTitle;
		}
		public void setAction2(String val){
				if(val != null && !val.equals(""))		
						action = val;
		}
		public void setSource(String val){
				if(val != null && !val.equals(""))		
						source = val;
		}
		//
		// This is the first thing that will be called on timedetails page		
		// check if we have document_id, if not we assume
		// it is a new pay period and we will create one
		//
		public String getDocument_id(){
				//
				if(document_id.equals("")){
						DocumentList dl = new DocumentList();
						if(employee_id.equals("")){
								getEmployee_id();
						}
						dl.setEmployee_id(employee_id);
						if(pay_period_id.equals("")){
								getPayPeriod();
						}
						dl.setPay_period_id(pay_period_id);
						if(job_id.equals("")){
								getJob();
						}
						if(!job_id.equals("all")){
								dl.setJob_id(job_id);
						}
						String back = dl.find();
						if(back.equals("")){
								List<Document> ones = dl.getDocuments();
								if(ones != null && ones.size() > 0){
										document = ones.get(0);
										document_id = document.getId();
								}
						}
				}
				// 
				// if we could not find, then we create a new one
				//
				if(document_id.equals("")){
						if(employee_id.equals("")){
								getEmployee_id();
								if(employee_id.equals("")){
										addError("Employee not set ");								
								}
						}
						if(pay_period_id.equals("")){
								getPayPeriod();
								if(pay_period_id.equals("")){
										addError("Pay period not set ");								
								}
						}
						if(job_id.equals("")){
								getJob();
								if(job_id.equals("")){
										addError("Job not set ");								
								}														
						}
						if(!job_id.equals("all")){
								Document one = new Document(null, employee_id, pay_period_id, job_id, null, user.getId());
								String back = one.doSave();
								if(back.equals("")){
										document_id = one.getId();
										document = one;
								}
						}
				}
				return document_id;
		}

		
		public void setDocument_id(String val){
				if(val != null && !val.equals(""))		
						document_id = val;
		}
		public void setPay_period_id(String val){
				if(val != null && !val.equals(""))		
						pay_period_id = val;
		}
		public void setJob_id(String val){
				if(val != null && !val.equals("-1"))		
						job_id = val;
		}		
		public void setDate(String val){
				if(val != null && !val.equals(""))		
						date = val;
		}
		public String getPay_period_id(){
				if(pay_period_id.equals("") && !document_id.equals("")){
						getDocument();
						if(document != null)
								pay_period_id = document.getPay_period_id();
				}
				return pay_period_id;
		}
		public String getSource(){
				return source;
		}
		public boolean hasMjdoc(){
				getMjdoc();
				return mjdoc != null;
		}
		public MultiJobDoc getMjdoc(){
				if(mjdoc == null){
						getEmployee();
						if(employee_id.equals("")){
								addError(" employee id not set ");
								return null;
						}
						getPayPeriod();
						if(pay_period_id.equals("")){
								addError(" pay period not set ");
								return null;
						}
						MultiJobDoc one = new MultiJobDoc(employee_id, pay_period_id);
						String back = one.findDocuments();
						if(back.equals("")){
								mjdoc = one;
						}
						else{
								System.err.println(" error "+back);
								addError(back);
						}
				}
				return mjdoc;
		}		
		public Document getDocument(){
				if(document == null && !document_id.equals("")){
						Document one = new Document(document_id);
						String back = one.doSelect();
						if(back.equals("")){
								document = one;
								job_id = document.getJob_id();
						}
				}
				return document;
		}
		public List<PayPeriod> getPayPeriods(){
				if(payPeriods == null){
						getPayPeriod(); // so that we can initialize the list
						PayPeriodList tl = new PayPeriodList();
						tl.setTwoPeriodsAheadOnly();
						tl.setEmployee_id(employee_id);
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
		public PayPeriod getPayPeriod(){
				//
				// if pay period is not set, we look for current one
				//
				if(payPeriod == null){
						if(pay_period_id.equals("")){
								if(document_id.equals("")){
										PayPeriodList ppl = new PayPeriodList();
										ppl.currentOnly();
										String back = ppl.find();
										if(back.equals("")){
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
								if(back.equals("")){
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
		public PayPeriod getPreviousPayPeriod(){
				//
				if(previousPayPeriod == null){
						if(pay_period_id.equals(""))
								getPayPeriod();
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
								getPayPeriod();
						PayPeriodList ppl = new PayPeriodList();
						ppl.setNextTo(pay_period_id); // relative to this currently used 
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
		public boolean isCurrentPayPeriod(){
				getCurrentPayPeriod();
				getPay_period_id();
				return pay_period_id.equals(currentPayPeriod.getId());
		}
		public JobTask getJob(){
				if(job_id.equals("") && job == null){
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
						if(job != null && !job_id.equals("all")){
								job_id = job.getId();
						}
				}
				else if(!job_id.equals("") && !job_id.equals("all") && job == null){
						JobTask one = new JobTask(job_id);
						String back = one.doSelect();
						if(back.equals("")){
								job = one;
						}
				}
				return job;
		}
		public boolean showAllJobs(){
				return hasMultipleJobs() && job_id.equals("all");
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
						getPayPeriod();
						if(payPeriod != null){
								jl.setPay_period_id(payPeriod.getId());
						}
						String back = jl.find();
						if(back.equals("")){
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
		public boolean hasNoJob(){
				getJobs();
				return jobs == null || jobs.size() == 0;
		}
		public boolean hasMultipleJobs(){
				getJobs();
				return jobs != null && jobs.size() > 1;
		}
		public boolean hasJobTypes(){
				getJobTypes();
				return jobTypes != null && jobTypes.size() > 0;
		}
		public List<Type> getJobTypes(){
				getJobs();
				if(jobs != null && jobs.size() > 1){
						if(jobTypes == null){
								jobTypes = new ArrayList<>();
								Type tp = new Type("all","All");
								jobTypes.add(tp);					
								for(JobTask one:jobs){
										tp = new Type(one.getId(), one.getName());
										jobTypes.add(tp);
								}
						}
				}
				return jobTypes;
		}
				
}





































