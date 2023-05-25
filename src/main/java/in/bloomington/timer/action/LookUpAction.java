package in.bloomington.timer.action;
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

public class LookUpAction extends TopAction{

    static final long serialVersionUID = 1150L;	
    static Logger logger = LogManager.getLogger(LookUpAction.class);
    //
    String lookUpTitle = "Lookup Employee Times";
    String pay_period_id="", 
	document_id=""; //for one only
    String type=""; // needed for js
    String other_employee_id = "", other_employee_name="";
    PayPeriod currentPayPeriod=null,
	nextPayPeriod=null, payPeriod = null;
    List<Document> documents = null;
    List<PayPeriod> payPeriods = null;
    // boolean notSubmitAndApproveFlag = true;
    String[] document_ids = null;
    public String execute(){
	String ret = SUCCESS;
	String back = canProceed("lookUp.action");
	resetEmployee();
	if(!action.isEmpty()){
	    if(!hasDocuments()){
		addMessage("No records found");
	    }
	}
	return ret;
    }
    public void setOther_employee_id(String val){
	if(val != null && !val.isEmpty())		
	    other_employee_id = val;
    }
    public void setEmployee_name(String val){
	other_employee_name = val;
    }
    public String getOther_employee_id(){
	return other_employee_id;
    }
    public String getOther_employee_name(){
	return other_employee_name;
    }
		
    void findDocuments(){
	//
	if(documents == null &&
	   !other_employee_id.isEmpty()
	   && !pay_period_id.isEmpty()){
	    DocumentList dl = new DocumentList();
	    dl.setEmployee_id(other_employee_id);
	    dl.setPay_period_id(pay_period_id);
	    String back = dl.find();
	    if(back.isEmpty()){
		List<Document> ones = dl.getDocuments();
		if(ones != null && ones.size() > 0){
		    documents = ones;
		}
	    }
	}
    }
    public boolean hasDocuments(){
	findDocuments();
	return documents != null && documents.size() > 0 ;
    }
    public List<Document> getDocuments(){
	findDocuments();
	return documents;
    }		
    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
	    action = val;
    }
    public void setType(String val){ // needed for js but not used here
	if(val != null && !val.isEmpty())		
	    type = val; // all employees
    }		
    public void setPay_period_id(String val){
	if(val != null && !val.equals("-1"))		
	    pay_period_id = val;
    }
    public String getPay_period_id(){
	if(pay_period_id.isEmpty()){
	    getPayPeriods();
	}
	return pay_period_id;
    }

    public List<PayPeriod> getPayPeriods(){
	if(payPeriods == null){
	    PayPeriodList tl = new PayPeriodList();
	    tl.setTwoPeriodsAheadOnly();
	    String back = tl.find();
	    if(back.isEmpty()){
		List<PayPeriod> ones = tl.getPeriods();
		if(ones != null && ones.size() > 0){
		    payPeriods = ones;
		    payPeriod = payPeriods.get(0);
		    // pay_period_id = payPeriod.getId();										
		}
	    }
	}
	return payPeriods;
    }
    public PayPeriod getPayPeriod(){
	if(!pay_period_id.isEmpty()){
	    PayPeriod one = new PayPeriod(pay_period_id);
	    String back = one.doSelect();
	    if(back.isEmpty()){
		payPeriod = one;
	    }
	}
	else{
	    getPayPeriods();
	}
	return payPeriod;

    }

}





































