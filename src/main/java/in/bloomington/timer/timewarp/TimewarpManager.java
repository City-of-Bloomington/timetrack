package in.bloomington.timer.timewarp;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.*;
import java.sql.*;
import java.text.*;
import javax.naming.*;
import javax.naming.directory.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TimewarpManager{

    boolean debug = false;
    static final long serialVersionUID = 53L;
    static Logger logger = LogManager.getLogger(TimewarpManager.class);
    static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    static DecimalFormat df = new DecimalFormat("#0.00");
    String document_id="", pay_period_id="", job_id="";
    // PROF HRS code id
    String prof_hrs_id = CommonInc.profHoursEarnCodeID;
    PayPeriod payPeriod = null;
    JobTask job = null;
    Group group = null; // needed for Fire - BC earn code
    Document document = null;
    HolidayList holys = null;
    SalaryGroup salaryGroup = null;
    boolean isHand = false;
    //
    public TimewarpManager(){
    }
    public TimewarpManager(String val){
	setDocument_id(val);
    }
    //
    // setters
    //
    public void setPay_period_id(String val){
	if(val != null){		
	    pay_period_id = val;
	}
    }
    public void setJob_id(String val){
	if(val != null){		
	    job_id = val;
	}
    }		
    public void setDocument_id(String val){
	if(val != null){		
	    document_id = val;
	}
    }
    //
    public String getPay_period_id(){
	return pay_period_id;
    }
    public String getJob_id(){
	return job_id;
    }		
    public String getDocument_id(){
	return document_id;
    }		
    public String doProcess(){
	String back = "";
	getDocument();
	// 
	getHolidayList();
	if(document != null){
	    TimewarpProcess process =
		new TimewarpProcess(document,
				    holys);
	    back = process.find();
	    if(back.isEmpty() || back.startsWith("No time")){
		back = processData(process);
	    }
	}
	else{
	    back = "No document found "; // should not happen
	}
	return back;
    }
    private String processData(TimewarpProcess process){
	String back = "";
	double week1_grs_reg_hrs = 0, week2_grs_reg_hrs=0,
	    week1_net_reg_hrs=0, week2_net_reg_hrs = 0,
	    cycle1_net_reg_hrs=0, cycle2_net_reg_hrs = 0;
	int cycle_order=1; // default one pay period only
	int splitDay = 14;
	boolean twoDifferentYears = false, weekOneHasSplit = false,
	    noWeekSplit = true;
	if(salaryGroup == null){
	    back = "No salary group found ";
	    return back;
	}
	getPayPeriod();
	if(payPeriod == null){
	    back = "Pay period not found ";
	    return back;
	}				
	String reg_code_id = salaryGroup.getDefault_regular_id();
	if(group != null && group.getName().equals("Fire - BC")){
	    reg_code_id = CommonInc.regEarnCodeFireBCGroupID;// REG FIRE BC
	}
	week1_grs_reg_hrs = process.getWeek1Regular();
	week2_grs_reg_hrs = process.getWeek2Regular();
	week1_net_reg_hrs = process.getWeek1NetRegular();
	week2_net_reg_hrs = process.getWeek2NetRegular();
	if(payPeriod.hasTwoDifferentYears()){
	    twoDifferentYears = true;
	    splitDay = payPeriod.getDaysToYearEnd();
	    // System.err.println(" splitDay "+splitDay);
	    if(splitDay == 0 || splitDay == 7){
		noWeekSplit = true;
	    }
	    else if(splitDay < 7){
		weekOneHasSplit = true;
		noWeekSplit = false;
	    }
	    else if(splitDay < 14){ // 8,9,10,11,12,13
		weekOneHasSplit = false;
		noWeekSplit = false;
	    }
	    //
	    if(splitDay < 14){						
		cycle1_net_reg_hrs = process.getNetRegularHoursForFirstPay();
		cycle2_net_reg_hrs = process.getNetRegularHoursForSecondPay();
	    }
	}

	TmwrpRun tmwrpRun = new TmwrpRun(document_id,
					 reg_code_id,
					 week1_grs_reg_hrs,
					 week2_grs_reg_hrs,
					 week1_net_reg_hrs,
					 week2_net_reg_hrs,
					 cycle1_net_reg_hrs,
					 cycle2_net_reg_hrs);
	back = tmwrpRun.doSaveOrUpdate();
	String run_id = tmwrpRun.getId();
	if(tmwrpRun.isOldRecord()){
	    back = tmwrpRun.doCleanUp();
	}
	// non regular hours
	TmwrpBlock block = new TmwrpBlock();
	block.setRun_id(run_id);
	if(back.isEmpty() && !run_id.isEmpty()){
	    Hashtable<String, Double> hash = new Hashtable<>();;
	    Hashtable<String, Double> hash2 = null;
	    
	    if(!twoDifferentYears){
		hash = process.getWeek1All();
		if(!hash.isEmpty()){
		    back += block.doSaveBolk(hash, "Week 1",cycle_order, "Hours");
		}
		hash = process.getWeek2All();						
		if(!hash.isEmpty()){
		    back += block.doSaveBolk(hash, "Week 2",cycle_order, "Hours");
		}
		hash = process.getWeek1MonetaryHash();
		if(!hash.isEmpty()){
		    back += block.doSaveBolk(hash, "Week 1",cycle_order, "Amount");
		}
		hash = process.getWeek2MonetaryHash();
		if(!hash.isEmpty()){
		    back += block.doSaveBolk(hash, "Week 2",cycle_order, "Amount");
		}							
	    }
	    else{ // two different years
		//end of the year blocks
		//
		// System.err.println(" no week split "+noWeekSplit);
		if(noWeekSplit){ // two weeks are completely separate
		    // in two different years
		    hash =  process.getWeek1All();
		    if(!hash.isEmpty()){										
			back += block.doSaveBolk(hash, "Week 1",1, "Hours");
		    }
		    hash = process.getWeek1MonetaryHash();
		    if(!hash.isEmpty()){
			back += block.doSaveBolk(hash, "Week 1",1, "Amount");
		    }
		    hash =  process.getWeek2All();
		    if(!hash.isEmpty()){										
			back += block.doSaveBolk(hash, "Week 2",2, "Hours");
		    }
		    hash = process.getWeek2MonetaryHash();
		    if(!hash.isEmpty()){											
			back += block.doSaveBolk(hash, "Week 2",2, "Amount");
		    }
		}
		else {
		    // System.err.println(" week one split "+weekOneHasSplit);
		    if(weekOneHasSplit){
			// first pay period
			// week 1 split 1 only
			hash  =  process.getWeekSplitNonRegularHours(1, 1);
			if(process.isHand){
			    // hand regular hash
			    hash2  = process.getWeekSplitRegularHours(1, 1);
			    if(!hash2.isEmpty()){
				mergeTwoHashes(hash2, hash);
			    }
			}
			if(!hash.isEmpty()){										
			    back += block.doSaveBolk(hash, "Week 1",1, "Hours");
			}
			// monetary
			hash  =  process.getWeekSplitMonetaryHash(1, 1);
			if(!hash.isEmpty()){										
			    back += block.doSaveBolk(hash, "Week 1",1, "Amount");
			}												
			//
			// second pay period
			//
			hash  = process.getWeekSplitNonRegularHours(1, 2);
			if(process.isHand){
			    // hand regular hash
			    hash2  = process.getWeekSplitRegularHours(1, 2);
			    if(!hash2.isEmpty()){										
				mergeTwoHashes(hash2, hash);
			    }
			}
			hash2 =  process.getWeek1().getEarnedHours();
			if(!hash2.isEmpty()){
			    mergeTwoHashes(hash2, hash);
			}
			if(!hash.isEmpty()){										
			    back += block.doSaveBolk(hash, "Week 1",2, "Hours");
			}
			//
			hash =  process.getWeek2All();
			if(!hash.isEmpty()){										
			    back += block.doSaveBolk(hash, "Week 2",2, "Hours");
			}
			//
			// monetary
			//
			hash  = process.getWeekSplitMonetaryHash(1, 2);
			if(!hash.isEmpty()){
			    back += block.doSaveBolk(hash, "Week 1",2, "Amount");
			}
			//
			hash =  process.getWeek2MonetaryHash();
			if(!hash.isEmpty()){										
			    back += block.doSaveBolk(hash, "Week 2",2, "Amount");
			}										
		    }
		    else{
			System.err.println(" week two split true ");
			// hours
			hash  =  process.getWeek1All();
			if(!hash.isEmpty()){										
			    back += block.doSaveBolk(hash, "Week 1",1, "Hours");
			}
			// week 2 split 1
			hash  =	process.getWeekSplitNonRegularHours(2, 1); 
			if(process.isHand){
			    hash2  = process.getWeekSplitRegularHours(2, 1);
			    if(!hash2.isEmpty()){										
				mergeTwoHashes(hash2, hash);
			    }
			}
			if(!hash.isEmpty()){
			    back += block.doSaveBolk(hash, "Week 2",1, "Hours");
			}
			//
			// monetary
			//
			hash  =  process.getWeek1MonetaryHash();
			if(!hash.isEmpty()){										
			    back += block.doSaveBolk(hash, "Week 1",1, "Amount");
			}
			// week 2 split 1
			hash  =	process.getWeekSplitMonetaryHash(2, 1); 
			if(!hash.isEmpty()){
			    back += block.doSaveBolk(hash, "Week 2",1, "Amount");
			}												
			//
			// second pay
			// hours
			hash = process.getWeek1().getEarnedHours();
			if(!hash.isEmpty()){
			    back += block.doSaveBolk(hash, "Week 1",2, "Hours");
			}
			//
			hash = process.getWeekSplitNonRegularHours(2, 2);
			hash2 =  process.getWeek2().getEarnedHours();
			if(!hash2.isEmpty()){
			    mergeTwoHashes(hash2, hash);
			}
			if(process.hasProfHours()){
			    double dd = process.getProfHours();
			    hash.put(prof_hrs_id, dd);
			}
			if(!hash.isEmpty()){
			    back += block.doSaveBolk(hash, "Week 2",2, "Hours");
			}
			// monetary
			hash = process.getWeekSplitMonetaryHash(2, 2);
			if(!hash.isEmpty()){
			    back += block.doSaveBolk(hash, "Week 2",2, "Amount");
			}
		    }
		}
	    }
	}
	return back;
    }
    public HolidayList getHolidayList(){
	if(holys == null){
	    getPayPeriod();
	    if(payPeriod != null){
		holys = new HolidayList(payPeriod.getStart_date(),
					payPeriod.getEnd_date());
		String back = holys.find();
	    }
	}
	return holys;
    }
    public PayPeriod getPayPeriod(){
	//
	if(payPeriod == null && !pay_period_id.isEmpty()){
	    PayPeriod one = new PayPeriod(pay_period_id);
	    String back = one.doSelect();
	    if(back.isEmpty()){
		payPeriod = one;
	    }
	}
	return payPeriod;
    }
    public Document getDocument(){
	//
	if(document == null && !document_id.isEmpty()){
	    Document one = new Document(document_id);
	    String back = one.doSelect();
	    if(back.isEmpty()){
		document = one;
		payPeriod = document.getPayPeriod();
		pay_period_id = document.getPay_period_id();
		job = document.getJob();
		group = job.getGroup();
		salaryGroup = job.getSalaryGroup();
	    }
	}
	return document;
    }				
    void mergeTwoHashes(Hashtable<String, Double> tFrom,
			Hashtable<String, Double> tTo){
	if(tFrom != null && !tFrom.isEmpty()){
	    Enumeration<String> keys = tFrom.keys();
	    while(keys.hasMoreElements()){
		String key = keys.nextElement();
		Double val = tFrom.get(key);
		if(tTo.containsKey(key)){
		    double val2 = tTo.get(key).doubleValue() + val.doubleValue();
		    tTo.put(key, val2);
		}
		else{
		    tTo.put(key, val);
		}
	    }
	}
    }
}
