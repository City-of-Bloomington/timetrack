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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TimeDetailsAction extends TopAction{

		static final long serialVersionUID = 4300L;	
		static Logger logger = LogManager.getLogger(TimeDetailsAction.class);
		DecimalFormat df = new DecimalFormat("###.00");
		//
		PayPeriod payPeriod = null, currentPayPeriod=null;
		String timeBlocksTitle = "Time Details";
		String pay_period_id = "";
		String document_id = "";
		Document document = null;
		String date = "", source="";
		JobTask jobTask = null;
		List<PayPeriod> payPeriods = null;
		public String execute(){
				String ret = SUCCESS;
				String back = doPrepare("timeDetails.action");
				if(!back.equals("")){
						return "login";
				}
				getDocument_id();
				if(hasNoJob()){
						addActionError("No job found for employee ");						
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
		// check if we have document_id, if not we assume
		// it is a new pay period and we will create one
		//
		public String getDocument_id(){
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
						}
						if(pay_period_id.equals("")){
								getPayPeriod();
						}
						Document one = new Document(null, employee_id, pay_period_id, null, user.getId());
						String back = one.doSave();
						if(back.equals("")){
								document_id = one.getId();
								document = one;
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
		public void setDate(String val){
				if(val != null && !val.equals(""))		
						date = val;
		}
		public String getPay_period_id(){
				return pay_period_id;
		}
		public String getSource(){
				return source;
		}
		public Document getDocument(){
				if(document == null && !document_id.equals("")){
						Document one = new Document(document_id);
						String back = one.doSelect();
						if(back.equals("")){
								document = one;
						}
				}
				return document;
		}
		public List<PayPeriod> getPayPeriods(){
				if(payPeriods == null){
						getPayPeriod(); // so that we can initialize the list
						PayPeriodList tl = new PayPeriodList();
						tl.setTwoPeriodsAheadOnly();
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
		public boolean isCurrentPayPeriod(){
				getCurrentPayPeriod();
				getPay_period_id();
				return pay_period_id.equals(currentPayPeriod.getId());
		}
		public JobTask getJobTask(){
				if(jobTask == null){
						JobTaskList jl = new JobTaskList(getEmployee_id());
						if(payPeriod != null){
								jl.setPay_period_id(payPeriod.getId());
						}
						String back = jl.find();
						if(back.equals("")){
								List<JobTask> ones = jl.getJobTasks();
								if(ones != null && ones.size() > 0){
										jobTask = ones.get(0); // get one
								}
						}
				}
				return jobTask;
		}
		public boolean hasNoJob(){
				getJobTask();
				return jobTask == null;
		}

}





































