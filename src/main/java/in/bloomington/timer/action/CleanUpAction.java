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
import in.bloomington.timer.util.Helper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CleanUpAction extends TopAction{

		static final long serialVersionUID = 4300L;	
		static Logger logger = LogManager.getLogger(CleanUpAction.class);
		DecimalFormat df = new DecimalFormat("###.00");
		//
		PayPeriod payPeriod = null, currentPayPeriod=null,
				previousPayPeriod=null, nextPayPeriod=null;
		String timeBlockLogsTitle = "Time Document Cleanup";
		String pay_period_id = "", other_employee_id = "",
				employee_name="";
		String cleanUpTitle = "";
		List<Document> documents = null;
		String date = "", source="";
		CleanUp cleanUp = null;
		List<PayPeriod> payPeriods = null;
		
		public String execute(){
				String ret = SUCCESS;
				String back = doPrepare("cleanUpAction");
				if(!action.isEmpty()){
						if(hasDocuments()){
								getCleanUp();
								back = cleanUp.doClean();
								if(back.isEmpty()){
										addMessage("Cleanup Success");
								}
								else{
										addError(back);
								}
						}
						else{
								addMessage("No records found");
						}
				}
				return ret;
		}
		public String getCleanUpTitle(){
				return cleanUpTitle;
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
		void findDocuments(){
				//
				if(documents == null && !other_employee_id.isEmpty()
					 && !pay_period_id.isEmpty()){
						DocumentList dl = new DocumentList();
						dl.setEmployee_id(other_employee_id);
						if(pay_period_id.isEmpty()){
								getPayPeriod();
						}
						dl.setPay_period_id(pay_period_id);
						String back = dl.findForCleanUp();
						if(back.isEmpty()){
								List<Document> ones = dl.getDocuments();
								if(ones != null && ones.size() > 0){
										documents = ones;
								}
						}
				}
		}
		boolean hasDocuments(){
				findDocuments();
				return documents != null && documents.size() > 0;
		}
		public void setPay_period_id(String val){
				if(val != null && !val.isEmpty())		
						pay_period_id = val;
		}
		public void setOther_employee_id(String val){
				if(val != null && !val.isEmpty())		
						other_employee_id = val;
		}
		public void setEmployee_name(String val){
				// ignore
		}
		public void setCleanUp(CleanUp val){
				if(val != null)
						cleanUp = val;
		}
		public CleanUp getCleanUp(){
				cleanUp = new CleanUp();
				if(hasDocuments()){
						cleanUp.setDocuments(documents);
				}
				return cleanUp;
		}
		public String getOther_employee_id(){
				return other_employee_id;
		}
		public String getPay_period_id(){
				if(pay_period_id.isEmpty())
						getPayPeriod();
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
						tl.setLimit(""+4);
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
								PayPeriod one = new PayPeriod(pay_period_id);
								String back = one.doSelect();
								if(back.isEmpty()){
										payPeriod = one;
								}
						}
				}
				return payPeriod;
		}


}





































