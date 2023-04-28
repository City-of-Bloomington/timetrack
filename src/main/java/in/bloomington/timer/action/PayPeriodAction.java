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
import in.bloomington.timer.util.Helper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PayPeriodAction extends TopAction{

    static final long serialVersionUID = 3160L;	
    static Logger logger = LogManager.getLogger(PayPeriodAction.class);
    //
    String payPeriodsTitle = "All Pay Periods";
    String pay_period_id="";
    PayPeriod currentPayPeriod = null;
    List<PayPeriod> payPeriods = null;
    static List<Integer> years = null;
    int year = Helper.getCurrentYear();
		
    public String execute(){
	String ret = SUCCESS;
	String back = canProceed("payPeriod.action");
	if(!back.isEmpty()){
	    return back;
	}
	if(!action.isEmpty()){
	    getPayPeriods();
	}
	return ret;
    }

    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
	    action = val;
    }
    public void setYear(Integer val){
	if(val != null && (val != -1))		
	    year = val;
    }
    public Integer getYear(){
	return year;
    }
    public void setPay_period_id(String val){
	if(val != null && !val.equals("-1"))		
	    pay_period_id = val;
    }
    public String getPay_period_id(){
	if(pay_period_id.isEmpty()){
	    getCurrentPayPeriod();
	}
	return pay_period_id;
    }
    public PayPeriod getCurrentPayPeriod(){
	if(currentPayPeriod == null){
	    PayPeriodList ppl = new PayPeriodList();
	    ppl.currentOnly();
	    String back = ppl.find();
	    if(back.isEmpty()){
		List<PayPeriod> ones = ppl.getPeriods();
		if(ones != null && ones.size() > 0){
		    currentPayPeriod = ones.get(0);
		    pay_period_id = currentPayPeriod.getId();
		}
	    }						
	}
	return currentPayPeriod;
    }
    public List<PayPeriod> getPayPeriods(){
	if(payPeriods == null){
	    PayPeriodList tl = new PayPeriodList();
	    tl.setYear(""+year);
	    tl.setOrderBy(" id asc ");
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
    public boolean hasPayPeriods(){
	getPayPeriods();
	return payPeriods != null && payPeriods.size() > 0;
    }
    public List<Integer> getYears(){
	if(years == null){
	    PayPeriodList ppl = new PayPeriodList();
	    String back = ppl.findYearList();
	    if(back.isEmpty()){
		years = ppl.getYears();
	    }
	    else{
		addError(back);
	    }
	}
	return years;
    }


}





































