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
		PayPeriod payPeriod = null;
		JobTask job = null;
		Group group = null; // needed for Fire BC earn code
		Document document = null;
		HolidayList holys = null;
		SalaryGroup salaryGroup = null;
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
				getHolidayList();
				if(document != null){
						TimewarpProcess process =
								new TimewarpProcess(document,
																		 holys);
					  back = process.find();
						if(back.equals("")){
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
						week1_net_reg_hrs=0, week2_net_reg_hrs = 0;
				if(salaryGroup == null){
						back = "No salary group found ";
						return back;
				}
				String reg_code_id = salaryGroup.getDefault_regular_id();
				if(group != null && group.getName().equals("Fire BC")){
						reg_code_id = "111"; // REG FIRE BC
				}
				week1_grs_reg_hrs = process.getWeek1Regular();
				week2_grs_reg_hrs = process.getWeek2Regular();
				week1_net_reg_hrs = process.getWeek1NetRegular();
				week2_net_reg_hrs = process.getWeek2NetRegular();
				TmwrpRun tmwrpRun = new TmwrpRun(document_id,
																				 reg_code_id,
																				 week1_net_reg_hrs,
																				 week2_grs_reg_hrs,
																				 week1_net_reg_hrs,
																				 week2_net_reg_hrs);
				back = tmwrpRun.doSaveOrUpdate();
				String run_id = tmwrpRun.getId();
				if(tmwrpRun.isOldRecord()){
						back = tmwrpRun.doCleanUp();
				}
				// non regular hours
				TmwrpBlock block = new TmwrpBlock();
				block.setRun_id(run_id);
				if(back.equals("") && !run_id.equals("")){
						Hashtable<String, Double> hash = process.getWeek1All();						
						if(!hash.isEmpty()){
								back += block.doSaveBolk(hash, "Week 1", "Hours");
						}
						hash = process.getWeek2All();						
						if(!hash.isEmpty()){
								back += block.doSaveBolk(hash, "Week 2", "Hours");
						}
						hash = process.getWeek1MonetaryHash();
						if(!hash.isEmpty()){
								back += block.doSaveBolk(hash, "Week 1", "Amount");
						}
						hash = process.getWeek2MonetaryHash();
						if(!hash.isEmpty()){
								back += block.doSaveBolk(hash, "Week 2", "Amount");
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
				if(payPeriod == null && !pay_period_id.equals("")){
						PayPeriod one = new PayPeriod(pay_period_id);
						String back = one.doSelect();
						if(back.equals("")){
								payPeriod = one;
						}
				}
				return payPeriod;
    }
    public Document getDocument(){
				//
				if(document == null && !document_id.equals("")){
						Document one = new Document(document_id);
						String back = one.doSelect();
						if(back.equals("")){
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

}
