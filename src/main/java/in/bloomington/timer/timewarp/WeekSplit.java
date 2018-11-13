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
		double st_weekly_hrs = 0;
		double prof_hrs = 0, net_reg_hrs= 0;		
		boolean consolidated = false;
		ArrayList<Hashtable<String, Double>> dailyArr = null;

		// for HAND dept, multiple types of regular code are used
		// such as HOME_REG, HOUSE_REG, RENT_REG, etc
		Hashtable<String, Double> regHash = new Hashtable<String, Double>();		
		//
		// for non regular code we use this hash such as PTO, CU, FMLA
		//
		Hashtable<String, Double> hash = new Hashtable<String, Double>();
		//

    public WeekSplit(boolean deb, Profile val){
				debug = deb;
				setProfile(val);
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
		double getRegularHours(){
				return regular_hrs;
		}
		//
		void add(TimeBlock te){
				//
				if(te != null){
						String code = te.getHour_code();
						String nw_code = te.getNw_code();
						double hours = te.getHours();

						if(bGroup != null && bGroup.isUnioned()){
								addToDaily(te);
						}
						else{
								total_hrs += hours;											
								if(code.equals("Reg") ||
									 code.endsWith("REG") ||
									 code.startsWith("TEMP")){
										regular_hrs += hours;
										// needed for HAND dept Only, we are using original code
										// not nw_code
										addToHash(regHash, code, hours); 
								}
								else{ 
										if(te.getCode_desc().toLowerCase().indexOf("used") > -1){
												earn_time_used += hours;
										}
										else if(te.getCode_desc().toLowerCase().indexOf("unpaid") > -1){
												unpaid_hrs += hours;
										}
										else{
												non_reg_hrs += hours;
										}
										addToHash(hash, nw_code, hours);
								}
						}
				}
		}		
		//
		// for unioned emps only
		//
		void computeDailyUnionEarnedTime(){
				for(int jj=0;jj<7;jj++){
						double reg_hrs = 0, reg_2_hrs = 0, hours = 0, dif_hrs = 0;
						Hashtable<String, Double> daily = dailyArr.get(jj);
						Set<String> codes = daily.keySet();
						if(codes  != null && codes.size() > 0){
								for(String code:codes){
										double hrs = daily.get(code).doubleValue();
										if(code.equals("Reg")){
												reg_hrs += hrs;
												hours += hrs;
										}
								}
								if(hours > 8.009){
										dif_hrs = hours - 8;
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
		// for unioned only
		//
		void addToDaily(TimeEntry te){
				//
				int jj = te.getDaySeq() % 7;
				String code = te.getEarn_code(); // only here we
				String code_desc = te.getEarn_code_desc().toLowerCase();
				
				double hours = te.getHours();
				double prev_hours = 0, dif_hrs = 0;
				Hashtable<String, Double> daily = dailyArr.get(jj);
				if(code.indexOf("Reg") > -1 || code.indexOf("REG") > -1){
						regular_hrs += hours;
						if(daily.containsKey(code)){
								prev_hours = daily.get(code).doubleValue();
								hours += prev_hours;
						}
				}
				else{ // non regular such On Call, or CO Call Out
						//
						if(code.indexOf("ONCALL") > -1){ //oncall35: one time = $35 only
								hours = 1.0f;
								// non_reg_hrs += hours;
								if(daily.containsKey(code)){
										hours +=  daily.get(code);
								}
						}
						else if(code.indexOf("CO") > -1){ // call out (if < 3 ==> 3)
								if(hours  < 3.0f){
										hours = 3.0f;
								}
								non_reg_hrs += hours;
								if(daily.containsKey(code)){
										hours +=  daily.get(code);
								}								
						}
						else if(code.indexOf("COSH") > -1){ //call out holiday(if < 3 ==> 3)
								if(hours  < 3.0f){
										hours = 3.0f;
								}
								non_reg_hrs += hours;
								if(daily.containsKey(code)){
										hours +=  daily.get(code);
								}								
						}						
						else if(code_desc.indexOf("used") > -1){
								earn_time_used += hours;
								if(daily.containsKey(code)){
										hours +=  daily.get(code);
								}											
						}
						else{ // any thing else such as holidays
								non_reg_hrs += hours;								
								if(daily.containsKey(code)){
										hours +=  daily.get(code);
								}								
						}
				}
				total_hrs += hours;
				daily.put(code, hours);
				dailyArr.set(jj, daily);
				//
		}
		void addToDaily(TimeBlock te){
				//
				int jj = te.getOrder_index() % 7;
				String code = te.getHour_code(); // only here we need our hour_code
				String code2 = code.toLowerCase();
				String code_desc = te.getCode_desc().toLowerCase();
				double hours = te.getHours();
				double prev_hours = 0, dif_hrs = 0;
				Hashtable<String, Double> daily = dailyArr.get(jj);
				if(code2.indexOf("reg") > -1){
						regular_hrs += hours;
						if(daily.containsKey(code)){
								prev_hours = daily.get(code).doubleValue();
								hours += prev_hours;
						}
				}
				else{ // non regular such On Call, or CO Call Out
						//
						if(code.indexOf("ONCALL") > -1){
								// hours do not count
								// non_reg_hrs += hours;
								if(daily.containsKey(code)){
										hours +=  daily.get(code);
								}
						}						
						else if(code.indexOf("CO") > -1){ // call out (if < 3 ==> 3)
								non_reg_hrs += hours;// hours are taken care off in timeblock
								if(daily.containsKey(code)){
										hours +=  daily.get(code);
								}
						}
						else if(code_desc.indexOf("used") > -1){
								earn_time_used += hours;
								if(daily.containsKey(code)){
										hours +=  daily.get(code);
								}											
						}
						else{ // any thing else such as holidays
								non_reg_hrs += hours;								
								if(daily.containsKey(code)){
										hours +=  daily.get(code);
								}								
						}
				}
				total_hrs += hours;
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
				if(bGroup != null){
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
				
				if(consolidated || (bGroup != null && !bGroup.isUnioned())) return;
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
				if(earned_time15 > 0.009){
						String code = "CE1.5";
						addToHash(hash, code, earned_time15);
						earned_time += earned_time15;
				}
				if(earned_time20 > 0.009){
						String code = "CE2.0"; 
						addToHash(hash, code, earned_time20);
						earned_time += earned_time20;
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
