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

public class WeekSplit{

		static Logger logger = LogManager.getLogger(WeekSplit.class);
		static final long serialVersionUID = 180L;				
		boolean debug = false;
		Profile profile = null;
		BenefitGroup bGroup = null;
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
		String excess_hours_calculation_method="";
		double daily_hrs = 8; // except Sanitaiton 10, delman 12
		double prof_hrs = 0, net_reg_hrs= 0;		
		boolean consolidated = false;
		Department department = null;
		ArrayList<Hashtable<String, Double>> dailyArr = null;

		// for HAND dept, multiple types of regular code are used
		// such as HOME_REG, HOUSE_REG, RENT_REG, etc
		Hashtable<String, Double> regHash = new Hashtable<>();		
		//
		// for non regular code we use this hash such as PTO, CU, FMLA
		//
		Hashtable<String, Double> hash = new Hashtable<>();
		//
		JobTask job = null;
		SalaryGroup salaryGroup = null;
		Group group = null;
		Shift shift = null;		

    public WeekSplit(boolean deb,
										 Profile val,
										 Department val2,
										 JobTask val3){
				debug = deb;
				setProfile(val);
				setDepartment(val2);
				setJob(val3);
    }
    public void setProfile(Profile val){
				if(val != null){
						profile = val;
						st_weekly_hrs = profile.getStWeeklyHrs();
						bGroup = profile.getBenefitGroup();
						dailyArr = new ArrayList<Hashtable<String, Double>>(7);
						for(int j=0;j<7;j++){
								Hashtable<String, Double> one = new Hashtable<String, Double>();
								dailyArr.add(one);
						}						
				}
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
						group  = job.getGroup();
						if(group != null){
								if(group.hasShift()){
										shift = group.getShift();
										double dd = shift.getDuration()/60.;
										if(dd > 0){
												daily_hrs = dd;
										}
								}
								excess_hours_calculation_method = group.getExcessHoursCalculationMethod();
						}
				}
		}
		double getRegularHours(){
				return regular_hrs;
		}
		//
		void add(TimeBlock te){
				//
				if(te != null){
						HourCode hrCode = te.getHourCode();
						String nw_code = te.getNw_code();
						double hours = te.getHours();
						if(salaryGroup != null && salaryGroup.isUnionned()){
								addToDaily(te);
						}
						else{
								if(hrCode.isRegular()){ // Reg or Temp
										String code = hrCode.getName();
										regular_hrs += hours;
										total_hrs += hours;																					
										// needed for HAND dept Only, we are using original code
										// not nw_code
										addToHash(regHash, code, hours);
								}
								else{
										if(hrCode.isUsed()){
												earn_time_used += hours;
												total_hrs += hours;
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
										else if(hrCode.isMonetary()){
												if(hours < 1.0)
														hours = 1.0;
												unpaid_hrs += hours;
										}
										else{ // other
												non_reg_hrs += hours;
												total_hrs += hours;
										}
										addToHash(hash, nw_code, hours);
								}
						}
				}
		}		
		//
		// for union or similar
		//
		void computeDailyUnionEarnedTime(){
				for(int jj=0;jj<7;jj++){
						double hours = 0, dif_hrs = 0;
						Hashtable<String, Double> daily = dailyArr.get(jj);
						Set<String> codes = daily.keySet();
						if(codes  != null && codes.size() > 0){
								for(String code:codes){
										double hrs = daily.get(code).doubleValue();
										if(code.equals("Reg") || code.indexOf("REG") > -1){
												hours += hrs;
										}
								}
								if(hours > 8.009){
										
										dif_hrs = hours - daily_hrs;
										if(dif_hrs > 0.009)
										if(jj == 6){
												earned_time20 += dif_hrs;												
										}
										else{
												earned_time15 += dif_hrs;
										}
								}
						}
				}
		}
		//
		// for union or similar 
		//
		void addToDaily(TimeBlock te){
				//
				int jj = te.getOrder_index() % 7;
				HourCode hrCode = te.getHourCode();
				String code = te.getNw_code();
				// String code = hrCode.getName(); // only here we need our hour_code
				// if(nw_code.equals("")) nw_code = code;
				double hours = te.getHours();
				double prev_hours = 0, dif_hrs = 0;
				Hashtable<String, Double> daily = dailyArr.get(jj);
				if(hrCode.isRegular()){
						regular_hrs += hours;
						total_hrs += hours;
						if(daily.containsKey(code)){
							 hours += daily.get(code).doubleValue();
						}
				}
				else{ // non regular such On Call, or CO Call Out
						//
						if(hrCode.isMonetary()){
								// hours do not count
								if(hours < 1.0) hours = 1.0;
								unpaid_hrs += hours;
								if(daily.containsKey(code)){
										hours +=  daily.get(code);
								}
						}						
						else if(hrCode.isCallOut()){ // call out (if < 3 ==> 3)
								non_reg_hrs += hours;// hours are taken care off in timeblock
								total_hrs += hours;																		
								if(daily.containsKey(code)){
										hours +=  daily.get(code);
								}
						}
						else if(hrCode.isUsed()){
								earn_time_used += hours;
								total_hrs += hours;								
								if(daily.containsKey(code)){
										hours +=  daily.get(code);
								}											
						}
						else if(hrCode.isEarned()){
								unpaid_hrs += hours;
								if(daily.containsKey(code)){
										hours +=  daily.get(code);
								}											
						}
						else if(hrCode.isOvertime()){
								unpaid_hrs += hours;
								if(daily.containsKey(code)){
										hours +=  daily.get(code);
								}											
						}
						else{ // any thing else such as holidays
								non_reg_hrs += hours;
								total_hrs += hours;									
								if(daily.containsKey(code)){
										hours +=  daily.get(code);
								}								
						}
				}
				daily.put(code, hours);
				dailyArr.set(jj, daily);
				//
		}		
		//
		public Hashtable<String, Double> getNonRegularHours(){
				return hash;
		}
		public Hashtable<String, Double> getRegularHash(){
				return regHash;
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
				if(bGroup != null){
						if(bGroup.isUnioned()){
								computeDailyUnionEarnedTime();
						}
				}
				consolidateDaily();
				findNetRegular();
		}

		public double getNetRegular(){
				return net_reg_hrs;
		}
		/**
		 * this is needed in PayPeriodTimes
		 */
		public void findNetRegular(){

				net_reg_hrs = 0;
				if(salaryGroup != null){
						if(salaryGroup.isTemporary()){
								if(regular_hrs > 40){
										net_reg_hrs = 40;
								}
								else{
										net_reg_hrs = regular_hrs;
								}
								return;
						}
						else if(salaryGroup.isUnionned()){
								net_reg_hrs = regular_hrs - earned_time;
								if(net_reg_hrs < 0.009){
										net_reg_hrs = 0;
								}
								return;
						}
						else if(salaryGroup.isFireSworn()){
								net_reg_hrs = regular_hrs - earned_time;
								if(net_reg_hrs < 0.009){
										net_reg_hrs = 0;
								}
								return;								
						}
						else if(salaryGroup.isFireSworn5x8()){
								net_reg_hrs = regular_hrs - earned_time;
								if(net_reg_hrs < 0.009){
										net_reg_hrs = 0;
								}
								return;								
						}						
				}
				else if(bGroup != null){
						if(bGroup.isTemporary()){
								if(regular_hrs > 40){
										net_reg_hrs = 40;
								}
								else{
										net_reg_hrs = regular_hrs;
								}
								return;
						}
						else if(bGroup.isUnioned()){
								net_reg_hrs = regular_hrs - earned_time;
								if(net_reg_hrs < 0.009){
										net_reg_hrs = 0;
								}
								return;
						}
				}				
				net_reg_hrs = regular_hrs - earned_time;
				if(net_reg_hrs > st_weekly_hrs){
						net_reg_hrs = st_weekly_hrs;
				}
				else if(net_reg_hrs < 0.009){
						net_reg_hrs = 0;
				}
				
		}
		/**
		 * we try to find earned time for union employee if they choose
		 * not to pick themselves, the default is earned time
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
								// any non regular hrs
								//
								if(key.indexOf("Reg") == -1 && key.indexOf("REG") == -1){
										addToHash(hash, key, hours);
								}
						}
				}
				if(excess_hours_calculation_method.equals("Donation")){
						return;
				}
				if(earned_time15 > 0.009){
						if(excess_hours_calculation_method.equals("Monetary")){
								String code = "OT1.5";
								addToHash(hash, code, earned_time15);
								earned_time += earned_time15;
						}
						else if(excess_hours_calculation_method.equals("Earn Time")){
								String code = "CE1.5";
								addToHash(hash, code, earned_time15);
								earned_time += earned_time15;
						}
				}
				if(earned_time20 > 0.009){
						if(excess_hours_calculation_method.equals("Monetary")){						
								String code = "OT2.0";
								addToHash(hash, code, earned_time15);
								earned_time += earned_time15;
						}
						else if(excess_hours_calculation_method.equals("Earn Time")){
								String code = "CE2.0"; 
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

}
