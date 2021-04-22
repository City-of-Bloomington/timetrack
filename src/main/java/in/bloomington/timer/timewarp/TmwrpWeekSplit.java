package in.bloomington.timer.timewarp;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */

import java.util.Hashtable;
import java.util.*;
import java.sql.*;
import java.text.*;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TmwrpWeekSplit{

		static Logger logger = LogManager.getLogger(TmwrpWeekSplit.class);
		static final long serialVersionUID = 180L;				
		boolean debug = false;
		// Profile profile = null;
		// BenefitGroup bGroup = null;
		double total_hrs = 0, regular_hrs = 0,
				non_reg_hrs = 0, earn_time_used = 0,
				earned_time = 0,
				earned_time15 = 0f, earned_time20 =0, // for union
				over_time15 = 0, over_time20 = 0,
				unpaid_hrs = 0,
				over_time25 = 0;
    double st_weekly_hrs = 40,
				comp_weekly_hrs = 0,
				comp_factor = 1,
				holiday_factor = 1;
		String excess_hours_earn_type="";
		double daily_hrs = 8; // except Sanitaiton 10, delman 12
		double prof_hrs = 0, net_reg_hrs= 0;
		int regular_mints = 0, total_mints=0;
		boolean consolidated = false;
		Department department = null;
		// reg
		ArrayList<Hashtable<String, Double>> regDailyArr = null;
		// non-reg
		ArrayList<Hashtable<String, Double>> dailyArr = null;
		// for HAND dept, multiple types of regular code are used
		// such as HOME_REG, HOUSE_REG, RENT_REG, etc
		Hashtable<String, Double> regHash = new Hashtable<>();		
		//
		// for non regular code we use this hash such as PTO, CU, FMLA
		//
		Hashtable<String, Double> hash = new Hashtable<>();
		Hashtable<String, Double> monetaryHash = new Hashtable<>();
		//
		JobTask job = null;
		SalaryGroup salaryGroup = null;
		Group group = null;
		Shift shift = null;		

    public TmwrpWeekSplit(boolean deb,
										 Department val2,
										 JobTask val3){
				debug = deb;
				setDepartment(val2);
				setJob(val3);
    }
		void setDepartment(Department val){
				if(val != null){
						department = val;
						if(department.isSanitation()){
								daily_hrs = 10;
						}
				}
		}
		void setJob(JobTask val){
				if(val != null){
						job = val;
						salaryGroup = job.getSalaryGroup();
						if(salaryGroup != null){
								if(salaryGroup.isFireSworn()){
										st_weekly_hrs = 48;
										comp_weekly_hrs = 53;
								}
								else{
										st_weekly_hrs = job.getWeekly_regular_hours();
										comp_weekly_hrs = job.getComp_time_weekly_hours();
								}
								comp_factor = job.getComp_time_factor();
								holiday_factor = job.getHoliday_comp_factor();
								
						}
						regDailyArr = new ArrayList<Hashtable<String, Double>>(7);
						dailyArr = new ArrayList<Hashtable<String, Double>>(7);						
						for(int j=0;j<7;j++){
								Hashtable<String, Double> one = new Hashtable<String, Double>();
								regDailyArr.add(one);
								Hashtable<String, Double> two = new Hashtable<String, Double>();
								dailyArr.add(two);								
						}									
						group  = job.getGroup();
						if(group != null){
								if(group.hasShift()){
										shift = group.getShift();
										double dd = shift.getDuration()/60.;
										if(dd > 0){
												daily_hrs = dd;
										}
								}
								excess_hours_earn_type = group.getExcessHoursEarnType();
						}
				}
		}
		double getRegularHours(){
				return regular_hrs;
		}
		int getRegularMinutes(){
				return regular_mints;
		}
		void showInfo(){
				System.err.println(" non regular hash "+hash);
				System.err.println(" regular hash "+regHash);				
				System.err.println(" nonetary hash "+monetaryHash);
				System.err.println(" reg hrs "+regular_mints/60.);
				System.err.println(" daily "+dailyArr);				
		}
		//
		void add(TimeBlock te){
				//
				if(te != null){
						HourCode hrCode = te.getHourCode();
						String code_id = hrCode.getId();
						double hours = te.getHours();
						int mints = te.getMinutes();
						if(hrCode.isMonetary()){
								double amount = te.getAmount();
								addToMonetary(code_id, amount);
								return;
						}
						if(salaryGroup != null && salaryGroup.isUnionned()){
								if(hrCode.isRegular())
										addToRegDaily(te);
								else
										addToDaily(te);
						}
						else{
								if(hrCode.isRegular()){ // Reg or Temp
										regular_mints += mints;
										total_mints += mints;
										// needed for HAND dept Only, we are using original code
										// not nw_code
										addToHash(regHash, code_id, hours);
								}
								else{
										if(hrCode.isUsed()){
												earn_time_used += hours;
										}
										else if(hrCode.isCallOut()){
												non_reg_hrs += hours;
										}										
										else if(hrCode.isUnpaid()){
												unpaid_hrs += hours;
										}
										else if(hrCode.isEarned()){										
												unpaid_hrs += hours;
										}
										else if(hrCode.isOvertime()){												
												unpaid_hrs += hours;
										}
										else{ // other
												non_reg_hrs += hours;
												total_mints += mints;
										}
										addToHash(hash, code_id, hours);
								}
						}
				}
		}		
		//
		// for union or similar, regular only
		//
		void computeDailyUnionEarnedTime(){
				for(int jj=0;jj<7;jj++){
						double hours = 0, dif_hrs = 0;
						Hashtable<String, Double> daily = regDailyArr.get(jj);
						Set<String> codes = daily.keySet();
						if(codes  != null && codes.size() > 0){
								for(String code:codes){
										double hrs = daily.get(code).doubleValue();
										hours += hrs;
								}
								if(hours > 8.01){
										dif_hrs = hours - daily_hrs;
										if(dif_hrs > CommonInc.critical_small){
												if(jj == 6){ // Sunday
														earned_time20 += dif_hrs;												
												}
												else{
														earned_time15 += dif_hrs;
												}
										}
								}
						}
				}
		}
		//
		// for union or similar non-regular codes
		//
		void addToDaily(TimeBlock te){
				//
				int jj = te.getOrder_index() % 7;
				HourCode hrCode = te.getHourCode();
				String code_id = hrCode.getId();
				double hours = te.getHours();
				int mints = te.getMinutes();
				double prev_hours = 0, dif_hrs = 0;
				Hashtable<String, Double> daily = dailyArr.get(jj);
				//
				if(hrCode.isCallOut()){ // call out (if < 3 ==> 3)
						non_reg_hrs += hours;// hours are taken care off in timeblock
						if(daily.containsKey(code_id)){
								hours +=  daily.get(code_id);
						}
				}
				else if(hrCode.isUsed()){
						earn_time_used += hours;
						if(daily.containsKey(code_id)){
								hours +=  daily.get(code_id);
						}											
				}
				else if(hrCode.isEarned()){
						unpaid_hrs += hours;
						if(daily.containsKey(code_id)){
								hours +=  daily.get(code_id);
						}											
				}
				else if(hrCode.isUnpaid()){
						unpaid_hrs += hours;
						if(daily.containsKey(code_id)){
								hours +=  daily.get(code_id);
						}											
				}				
				else if(hrCode.isOvertime()){
						unpaid_hrs += hours;
						if(daily.containsKey(code_id)){
								hours +=  daily.get(code_id);
						}											
				}
				else{ // any thing else such as holidays
						non_reg_hrs += hours;
						total_mints += mints;
						if(daily.containsKey(code_id)){
								hours +=  daily.get(code_id);
						}								
				}
				daily.put(code_id, hours);
				dailyArr.set(jj, daily);
		}
		//
		// for union or similar
		// reg code only,
		//
		void addToRegDaily(TimeBlock te){
				//
				int jj = te.getOrder_index() % 7;
				HourCode hrCode = te.getHourCode();
				String code_id = hrCode.getId();
				double hours = te.getHours();
				int mints = te.getMinutes();
				double prev_hours = 0, dif_hrs = 0;
				Hashtable<String, Double> daily = regDailyArr.get(jj);
				if(hrCode.isRegular()){
						regular_mints += mints;
						total_mints += mints;
						if(daily.containsKey(code_id)){
							 hours += daily.get(code_id).doubleValue();
						}
				}
				daily.put(code_id, hours);
				regDailyArr.set(jj, daily);
		}				
		//
		public Hashtable<String, Double> getNonRegularHours(){
				return hash;
		}
		public Hashtable<String, Double> getRegularHash(){
				return regHash;
		}
		public Hashtable<String, Double> getMonetaryHash(){
				return monetaryHash;
		}
		public boolean hasMonetary(){
				return monetaryHash != null && !monetaryHash.isEmpty();
		}
		
		//
		// we need this function to adjust regular time used (in HAND)
		// based on difference between reg hrs and net reg hrs
		//
		public void adjustRegHashBy(double ratio){
				Hashtable<String, Double> tmpHash = new Hashtable<String, Double>();
				Enumeration<String> keys = regHash.keys();
				while(keys.hasMoreElements()){
						String key = keys.nextElement();
						double hours = regHash.get(key);
						double hours2 = hours * ratio;
						tmpHash.put(key, hours2);
				}				
				regHash = tmpHash;
		}
		//
		public void doCalculations(){
				if(salaryGroup != null){
						if(salaryGroup.isUnionned()){				
								consolidateDaily();
						}
				}
				findNetRegular();
		}

		public double getNetRegular(){
				return net_reg_hrs;
		}
		/**
		 * this is needed in PayPeriodProcess
		 */
		public void findNetRegular(){

				net_reg_hrs = 0;
				regular_hrs = regular_mints/60.;
				if(salaryGroup != null){
						if(salaryGroup.isTemporary()){
								if(regular_hrs > CommonInc.cityStandardWeeklyHrs){
										net_reg_hrs = CommonInc.cityStandardWeeklyHrs; // 40
								}
								else{
										net_reg_hrs = regular_hrs;
								}
								return;
						}
						else if(salaryGroup.isUnionned()){
								net_reg_hrs = regular_hrs - earned_time;
								if(net_reg_hrs < CommonInc.critical_small){
										net_reg_hrs = 0;
								}
								return;
						}
						else if(salaryGroup.isFireSworn()){
								net_reg_hrs = regular_hrs - earned_time;
								if(net_reg_hrs < CommonInc.critical_small){
										net_reg_hrs = 0;
								}
								return;								
						}
						else if(salaryGroup.isFireSworn5x8()){
								net_reg_hrs = regular_hrs - earned_time;
								if(net_reg_hrs < CommonInc.critical_small){
										net_reg_hrs = 0;
								}
								return;								
						}						
				}
				net_reg_hrs = regular_hrs - earned_time;
				if(net_reg_hrs > Math.max(st_weekly_hrs, CommonInc.cityStandardWeeklyHrs)){
						net_reg_hrs = Math.max(st_weekly_hrs, CommonInc.cityStandardWeeklyHrs);
				}
				else if(net_reg_hrs < CommonInc.critical_small){
						net_reg_hrs = 0;
				}
		}
		/**
		 * we try to find earned time for union employee if they choose
		 * not to pick themselves,
		 */
		void consolidateDaily(){
				
				if(consolidated ||
					 (salaryGroup !=null && !salaryGroup.isUnionned())) return;
				// else if (bGroup != null && !bGroup.isUnioned()) return;
				//
				// we need to add earn_time hours if any for union people
				// who did not choose to select overtime or earned time
				//
				for(int j=0;j<7;j++){
						Hashtable<String, Double> daily = dailyArr.get(j);
						Enumeration<String> keys = daily.keys();
						while(keys.hasMoreElements()){
								String key = keys.nextElement();
								double hours = daily.get(key);
								//
								// non regular
								addToHash(hash, key, hours);
						}
				}
				
				if(excess_hours_earn_type.equals("Donation")){
						return;
				}
				//
				computeDailyUnionEarnedTime();
				//
				if(earned_time15 > CommonInc.critical_small){
						if(excess_hours_earn_type.equals("Monetary")){
								String code_id = CommonInc.overTime15EarnCodeID; // OT1.5
								addToHash(hash, code_id, earned_time15);
								earned_time += earned_time15;
						}
						else if(excess_hours_earn_type.equals("Earn Time")){
								String code_id = CommonInc.compTime15EarnCodeID; // CE1.5
								addToHash(hash, code_id, earned_time15);
								earned_time += earned_time15;
						}
				}
				if(earned_time20 > CommonInc.critical_small){
						if(excess_hours_earn_type.equals("Monetary")){						
								String code = CommonInc.overTime20EarnCodeID; // OT2.0
								addToHash(hash, code, earned_time20);
								earned_time += earned_time15;
						}
						else if(excess_hours_earn_type.equals("Earn Time")){
								String code = CommonInc.compTime20EarnCodeID; // CE2.0; 
								addToHash(hash, code, earned_time20);
								earned_time += earned_time20;
						}
				}				
				consolidated = true;
		}

		public void addToHash(Hashtable<String, Double> tHash, String code, double val){
				if(tHash.containsKey(code)){
						double hours = tHash.get(code).doubleValue();
						hours += val;
						tHash.put(code, hours);
				}
				else{
						tHash.put(code, val);
				}
		}
		public void addToMonetary(String code, double val){
				if(val > 0.0){
						if(monetaryHash.containsKey(code)){
								double amount = monetaryHash.get(code).doubleValue();
								amount += val;
								monetaryHash.put(code, amount);
						}
						else{
								monetaryHash.put(code, val);
						}
				}
		}
		/**
		 * regular hours when added to non regular we get 40
		 */
		public double getEarnedTime(){
				return earned_time;
		}
		public double getUnpaidHrs(){
				return unpaid_hrs;
		}		
		public double getEarnedTimeUsed(){
				return earn_time_used;
		}
		public double getNonRegularHrs(){
				return non_reg_hrs;
		}
		public double getTotalHours(){
				return total_hrs;
		}
		public int getTotalMinutes(){
				return total_mints;
		}
		

}
