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

public class TimeBlockLogAction extends TopAction{

    static final long serialVersionUID = 4300L;	
    static Logger logger = LogManager.getLogger(TimeBlockLogAction.class);
    DecimalFormat df = new DecimalFormat("###.00");
    //
    PayPeriod payPeriod = null, currentPayPeriod=null,
	previousPayPeriod=null, nextPayPeriod=null;
    String timeBlockLogsTitle = "Time Block Logs";
    String pay_period_id = "", other_employee_id = "",
	employee_name="";
    String document_id = "";
    Document document = null;
    String date = "", source="";
    JobTask jobTask = null;
    List<PayPeriod> payPeriods = null;
    List<TimeBlockLog> timeBlockLogs = null;
		
    public String execute(){
	String ret = SUCCESS;
	String back = doPrepare("timeBlockLog.action");
	if(action.isEmpty()){
	    if(document_id.isEmpty()){
		return "search";
	    }
	}
	if(!other_employee_id.isEmpty()){
	    getDocument_id();
	}
	return ret;
    }
    public String getTimeBlockLogsTitle(){
	return timeBlockLogsTitle;
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
	if(document_id.isEmpty() && !other_employee_id.isEmpty()){
	    DocumentList dl = new DocumentList();
	    dl.setEmployee_id(other_employee_id);
	    if(pay_period_id.isEmpty()){
		getPayPeriod();
	    }
	    dl.setPay_period_id(pay_period_id);
	    String back = dl.find();
	    if(back.isEmpty()){
		List<Document> ones = dl.getDocuments();
		if(ones != null && ones.size() > 0){
		    document = ones.get(0);
		    document_id = document.getId();
		}
	    }
	}
	return document_id;
    }
    public void setDocument_id(String val){
	if(val != null && !val.isEmpty())		
	    document_id = val;
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
    public String getOther_employee_id(){
	return other_employee_id;
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
    public Document getDocument(){
	if(document == null && !document_id.isEmpty()){
	    Document one = new Document(document_id);
	    String back = one.doSelect();
	    if(back.isEmpty()){
		document = one;
	    }
	}
	return document;
    }
    public List<PayPeriod> getPayPeriods(){
	if(payPeriods == null){
	    getPayPeriod(); // so that we can initialize the list
	    PayPeriodList tl = new PayPeriodList();
	    tl.avoidFuturePeriods();
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
    public List<TimeBlockLog> getTimeBlockLogs(){
	if(timeBlockLogs == null && !document_id.isEmpty()){
	    TimeBlockLogList tbll = new TimeBlockLogList();
	    tbll.setDocument_id(document_id);
	    String back = tbll.find();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		List<TimeBlockLog> ones = tbll.getTimeBlockLogs();
		if(ones != null && ones.size() > 0){
		    timeBlockLogs = ones;
		}
	    }
	}
	return timeBlockLogs;
    }
    public boolean hasTimeBlockLogs(){
	getTimeBlockLogs();
	return timeBlockLogs != null && timeBlockLogs.size() > 0;
    }
}





































