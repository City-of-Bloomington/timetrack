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
import in.bloomington.timer.bean.*;
import in.bloomington.timer.list.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.timewarp.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class TmwrpInitiateAction extends TopAction{

    static final long serialVersionUID = 4320L;	
    static Logger logger = LogManager.getLogger(TmwrpInitiateAction.class);
    //
    List<PayPeriod> payPeriods = null;
		TmwrpInitiate tmwrp = null;
    String tmwrpWrapsTitle = "Timewarp Initiate";
    String pay_period_id = "", source="";
		PayPeriod payPeriod = null, currentPayPeriod=null;
		List<String> emps = null;
    public String execute(){
				String ret = SUCCESS;
				String back = doPrepare("tmwrpWrapInitiate.action");
				if(!action.equals("") && !pay_period_id.equals("")){
						getTmwrp();
						tmwrp.setPay_period_id(pay_period_id);
						back = tmwrp.doProcess();
						if(!back.equals("")){
								addMessage(back);
						}
						else{
								addMessage("Updated successfully");
						}
						emps = tmwrp.getEmps();
				}
				return ret;
    }
    public void setAction2(String val){
				if(val != null && !val.equals(""))		
						action = val;
    }
    public void setSource(String val){
				if(val != null && !val.equals(""))		
						source = val;
    }		
    public void setPay_period_id(String val){
				if(val != null && !val.equals(""))		
						pay_period_id = val;
    }
    public String getSource(){
				return source;
    }		
    public String getPay_period_id(){
				if(pay_period_id.equals("")){
						getPayPeriod();
				}
				return pay_period_id;
    }
		public TmwrpInitiate getTmwrp(){
				if(tmwrp == null){
						tmwrp = new TmwrpInitiate();
				}
				return tmwrp;
		}
		public void setTmwrp(TmwrpInitiate val){
				if(val != null)
						tmwrp = val;
		}
    public List<PayPeriod> getPayPeriods(){
				if(payPeriods == null){
						getPayPeriod(); // so that we can initialize the list
						PayPeriodList tl = new PayPeriodList();
						tl.avoidFuturePeriods();
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
				// if not set we look for current one
				//
				if(payPeriod == null){
						if(pay_period_id.equals("")){
								getCurrentPayPeriod();
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
		public boolean hasEmps(){
				return emps != null && emps.size() > 0;
		}
		/**
		 * list of employees' full names who needed timewarp initiations;
		 */
		public List<String> getEmps(){
				return emps;
		}
				
				
}





































