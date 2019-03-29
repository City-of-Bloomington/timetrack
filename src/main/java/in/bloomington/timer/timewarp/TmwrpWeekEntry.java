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
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TmwrpWeekEntry{

    boolean debug = false;
    static final long serialVersionUID = 160L;		
    static Logger logger = LogManager.getLogger(TmwrpWeekEntry.class);		
    // Profile profile = null;
    // BenefitGroup bGroup = null;
    static DecimalFormat ndf = new DecimalFormat("#0.00");		
    static SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
    static final double critical_small = 0.009;
		
    double total_hrs = 0, regular_hrs = 0,
				non_reg_hrs = 0, earn_time_used = 0,
				earned_time = 0, unpaid_hrs = 0,
				over_time15 = 0, over_time20=0, over_time25 = 0;
    double st_weekly_hrs = 40,
				daily_hrs = 8,
				comp_weekly_hrs = 0,
				comp_factor = 1,
				holiday_factor = 1;
    double prof_hrs = 0, excess_hrs = 0, net_reg_hrs= 0;
    boolean handSpecial = false; 
    List<HolidayWorkDay> holyWorkDays = null;
    Department department = null;
		JobTask job = null;
		Employee employee = null;
		SalaryGroup salaryGroup = null;
		Group group = null;
		Shift shift = null;
		//
		String excess_hours_calculation_method = "";
    //
    // these are used for the yearend pay period where we need
    // to split the hours of the week in two different years
    //
    TmwrpWeekSplit splitOne = null, splitTwo = null;
    int splitDay = 7; // no split
    //
    // for non regular earn code we use this hash such as PTO, CU, FMLA
    //
    Hashtable<String, Double> hash = new Hashtable<String, Double>();
    Hashtable<String, Double> earnedHash = new Hashtable<String, Double>();
    //
    // reg for hand Only 
    Hashtable<String, Double> regHash = new Hashtable<String, Double>();
		//
		//  monetary hash
		Hashtable<String, Double> monetaryHash = new Hashtable<String, Double>();		
    
    public TmwrpWeekEntry(boolean deb,
													Department val,
													JobTask val2){ 
				debug = deb;
				setDepartment(val);
				setJob(val2);
				splitOne = new TmwrpWeekSplit(debug,
																			val,
																			val2);
				splitTwo = new TmwrpWeekSplit(debug,
																			val,
																			val2);

    }
    public TmwrpWeekEntry(boolean deb,
										 Department val,
										 JobTask val2,										 
										 int val3 // split day
										 ){
				//
				this(deb, // val,
						 val, val2);
				//
				// if splitDay = 0 ignore, it just start of a new week
				// day 7 is day 0 in next week (7 - 7 = 0)
				//
				if(val3 > 0){  
						splitDay = val3;
				}
    }
    public void setHandSpecial(boolean val){
				handSpecial = val;
    }
    void setDepartment(Department val){
				if(val != null){
						department = val;
						if(department.isHand()){
								setHandSpecial(true);
						}
				}
    }
		void setJob(JobTask val){
				if(val != null){
						job = val;
						salaryGroup = job.getSalaryGroup();
						if(salaryGroup !=null){
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
    double getTotalHours(){
				return total_hrs;
    }
    double getRegularHours(){
				return regular_hrs;
    }
    double getEarnedTimeUsed(){
				return earn_time_used;
    }
    public void setHolidayWorkDay(HolidayWorkDay val){
				if(val != null){
						if(holyWorkDays == null){
								holyWorkDays = new ArrayList<HolidayWorkDay>();
						}
						holyWorkDays.add(val);
				}
    }
    //
    public void add(TimeBlock te){
				try{
						if(te != null){
								// daySeq 0,1,2,3,4,5,6
								if((te.getOrder_index()%7) < splitDay){
										splitOne.add(te);
								}
								else{
										splitTwo.add(te);
								}
						}
				}catch(Exception ex){
						logger.error(ex);
				}
    }
    //
    // adding non regular hours except earned, such as PTO, H1,
    //
    public void addToHash(Hashtable<String, Double> tHash, String code_id, double val){
				if(tHash.containsKey(code_id)){
						double hours = tHash.get(code_id).doubleValue();
						hours += val;
						tHash.put(code_id, hours);
				}
				else{
						tHash.put(code_id, val);
				}
    }
    //		
    // this is for holiday earned and other non CE codes
    //
    public void addToEarnedHash(TimeBlock te){
				addToHash(earnedHash, te.getHour_code_id(), te.getHours());
    }
    public void addToEarnedHash(String code_id, double hrs){
				if(code_id != null && hrs > 0)
						addToHash(earnedHash, code_id, hrs);
    }		
    //
    public Hashtable<String, Double> getNonRegularHours(){
				return hash;
    }
    
    public Hashtable<String, Double> getEarnedHours(){
				return earnedHash;
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
    public Hashtable<String, Double> getAll(){
				Hashtable<String, Double> all = new Hashtable<String, Double>();
				if(!hash.isEmpty())
						all.putAll(hash);
				if(!earnedHash.isEmpty())
						all.putAll(earnedHash);
				if(handSpecial){ 
						all.putAll(regHash);
				}
				return all;
    }
    public void doCalculations(){
	
				splitOne.doCalculations();
				splitTwo.doCalculations();
				total_hrs = splitOne.getTotalHours()+splitTwo.getTotalHours();
				regular_hrs = splitOne.getRegularHours()+splitTwo.getRegularHours();
				//
				// the following earned_time are the overtime for certain employees
				earned_time = splitOne.getEarnedTime()+splitTwo.getEarnedTime();
				earn_time_used = splitOne.getEarnedTimeUsed()+splitTwo.getEarnedTimeUsed();
				unpaid_hrs = splitOne.getUnpaidHrs()+splitTwo.getUnpaidHrs();
				//
				mergeMonetaryHashtablesFromSplits(); // monetary if any
				findHolidayEarned(); // if any				
				findExessHours();
				mergeHashtablesFromSplits();
				findProfHours();
				findNonRegularHrs();
				findNetRegular();
				//
				if(handSpecial){ // special case for HAND dept
						//
						// this difference is caused by comp time earned and prof hours
						// we normally subtract these from regular hours
						// in HAND case we have multiple Reg hours (HOME_REG, HOUSE_REG, )
						// so we need to subtract it evenly from the bunch used in
						// this week
						if(regular_hrs > 0.01 && net_reg_hrs + 0.01 < regular_hrs){
								double ratio = net_reg_hrs / regular_hrs; 
								splitOne.adjustRegHashBy(ratio);
								splitTwo.adjustRegHashBy(ratio);
						}
						mergeRegHashtablesFromSplits();
				}
    }
    //
    void mergeHashtablesFromSplits(){
				//
				// non regular hours
				Hashtable<String, Double> table = splitOne.getNonRegularHours();
				Hashtable<String, Double> table2 = splitTwo.getNonRegularHours();
				if(!table.isEmpty())
						mergeWithHash(table, hash);
				if(!table2.isEmpty())
						mergeWithHash(table2, hash);
    }
    // reg
    void mergeRegHashtablesFromSplits(){
				//
				Hashtable<String, Double> table = splitOne.getRegularHash();
				Hashtable<String, Double> table2 = splitTwo.getRegularHash();
				if(!table.isEmpty())				
						mergeWithHash(table, regHash);
				if(!table2.isEmpty())
						mergeWithHash(table2, regHash);
				// to all
				if(!hash.isEmpty())
						mergeWithHash(regHash, hash);				
    }
    // monetary
    void mergeMonetaryHashtablesFromSplits(){
				//
				Hashtable<String, Double> table = null;				
				if(splitOne.hasMonetary()){
						table = splitOne.getMonetaryHash();
						mergeWithHash(table, monetaryHash);
				}
				if(splitTwo.hasMonetary()){
						table = splitTwo.getMonetaryHash();
						mergeWithHash(table, monetaryHash);						
				}
    }		
    //
    void mergeWithHash(Hashtable<String, Double> tFrom,
											 Hashtable<String, Double> tTo){
				if(tFrom != null && tFrom.size() > 0){
						Enumeration<String> keys = tFrom.keys();
						while(keys.hasMoreElements()){
								String key = keys.nextElement();
								Double val = tFrom.get(key);
								addToHash(tTo, key, val);
						}
				}
    }
    //
    public double getProfHours(){
				return prof_hrs;
    }
    public double getExessHours(){
				return excess_hrs;
    }
    public void findProfHours(){
	
				prof_hrs = 0;
				if(salaryGroup != null){
						if(!salaryGroup.isExempt()){ // only exempt get prof hrs
								return;
						}
						// exempt only
						if(comp_weekly_hrs == 40){
								return;
						}
				}
				//
				// everybody else
				//
				prof_hrs = total_hrs - st_weekly_hrs - earned_time - excess_hrs;//- unpaid_hrs;
				if(prof_hrs < critical_small){
						prof_hrs = 0;
				}
    }
    boolean hasProfHours(){
				return prof_hrs > critical_small;
    }
    boolean hasSplitDay(){
				return splitDay < 7;
    }
    public double getNetRegular(){
				return net_reg_hrs;
    }
    /**
     * regular hours when added to non regular we get 40
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
								if(net_reg_hrs < critical_small){
										net_reg_hrs = 0;
								}
								return;
						}
				}
				net_reg_hrs = regular_hrs - earned_time - excess_hrs - prof_hrs;
				//
    }
    //
    public double getNonRegularHrs(){
	
				return non_reg_hrs;
    }
    public void findNonRegularHrs(){
	
				if(non_reg_hrs == 0){
						non_reg_hrs = splitOne.getNonRegularHrs() +
								splitTwo.getNonRegularHrs();
				}
    }
    /**
     * find excess hours to add CE or OT earn codes for employees
     * who work more than standard weekly hours, excluded unioned
     * employees as these are handled daily
     *
     */
    public void findExessHours(){
	
				excess_hrs = 0;
				double netHours = total_hrs - earned_time;
				//
				// for full time working less than 40 hrs
				//
				if(salaryGroup != null){
						if(salaryGroup.isTemporary()){
								excess_hrs = netHours < 40 ? 0: netHours - 40;
						}
						else if(salaryGroup.isUnionned()){ // union AFCSME employee
								// excess_hrs = 1f;
								excess_hrs = netHours - comp_weekly_hrs;
						}
						else if(salaryGroup.isFireSworn()){
								// fire excess is handled by NW
						}
						else if(salaryGroup.isFireSworn5x8()){
								// fire excess is handled by NW
								//
						}						
						else{
								if(st_weekly_hrs < 40 && netHours >= st_weekly_hrs){
										excess_hrs = netHours - st_weekly_hrs;
								}
								else if(netHours > comp_weekly_hrs){
										excess_hrs = netHours - comp_weekly_hrs;
								}
						}
				}				
    }
    public boolean hasExessHours(){
				return excess_hrs > critical_small;
    }
    public boolean hasInsufficientTotalHours(){
				boolean ret = false;
				
				if(salaryGroup != null && !salaryGroup.isTemporary()){
						if(total_hrs + 0.01 < st_weekly_hrs){
								ret = true;
						}
				}
				return ret;
    }
    /**
     * this method create the holiday earned hours
     * and add the time to earned_time (if any)
     */
    private void findHolidayEarned(){
				//
				if(holyWorkDays == null) return;
				//
				double holy_earn_hrs = 0;
				String code_id = "";
				double netHours = total_hrs - earned_time;
				double extra_hrs = 0;
				System.err.println(" net hours "+netHours);
				//
				// for full time working less than 40 hrs
				//
				if(salaryGroup != null){
						if(salaryGroup.isTemporary()){
								extra_hrs = netHours < 40 ? 0: netHours - 40;
						}
						else if(salaryGroup.isExcessCulculationDaily()){
								// union, police,
								// compute daily see below
						}
						else if(salaryGroup.isFireSworn()){
								// ignore
						}
						else if(salaryGroup.isFireSworn5x8()){
								// ignore
						}						
						else{
								if(netHours > st_weekly_hrs)
										extra_hrs = netHours - st_weekly_hrs;
						}
				}
				if(extra_hrs > critical_small){
						double holy_hours = 0;
						for(HolidayWorkDay one: holyWorkDays){
								holy_hours += one.getHours();
						}
						if(extra_hrs >= holy_hours){
								holy_earn_hrs = holy_hours;
						}
						else{
								holy_earn_hrs = extra_hrs;
						}
						String dstr = ndf.format(holy_earn_hrs);
						holy_earn_hrs = (double)(new Double(dstr));
						//
						if(holy_earn_hrs > critical_small){
								earned_time += holy_earn_hrs;
								//
								// create earn codes
								if(salaryGroup != null && salaryGroup.isUnionned()){
										code_id = CommonInc.holyCompTime20EarnCodeID; // "HCE2.0";
								}
								else{
										if(holiday_factor > 1.5){
												code_id = CommonInc.holyCompTime20EarnCodeID;// "HCE2.0";
										}
										else if(holiday_factor > 1.0){
												code_id = CommonInc.holyCompTime15EarnCodeID; // "HCE1.5";
										}
										else{
												code_id = CommonInc.holyCompTime10EarnCodeID; // "HCE1.0";
										}
								}
								addToEarnedHash(code_id, holy_earn_hrs);
						}
				}
    }
    /**
     * if the employee has excess hours, then depending on type of employement,
     * they may get CE, OT or PROF hours
     */
    public void createEarnRecord(){
				//
				String code_id = "";
				if(excess_hrs <= critical_small) return;
				if(salaryGroup != null){
						if(salaryGroup.isTemporary()){
								code_id = CommonInc.overTime15EarnCodeID; // "OT1.5";	// no CE1.5 for temp
								addToEarnedHash(code_id, excess_hrs);								
								return;
						}
						if(salaryGroup.isFireSworn()){ // NW takes care of this
								// code = "FIRE FLSA";	 pay period not weekly
								return;
						}						
						else if(!salaryGroup.isExcessCulculationWeekly()){
								// we are interested in weekly only for now
								// anything else we ignore here
								return;
						}
				}
				if(excess_hours_calculation_method.equals("Donation")){
						return;
				}
				//
				// this should work for full time or part time with benefit
				//
				code_id = CommonInc.compTime10EarnCodeID; // "CE1.0";
				//
				// we want to keep excess_hrs as we may need it
				// for other stuff
				//
				double excess_hrs2 = excess_hrs;
				if(st_weekly_hrs < 40){ // for those who have starndard hours < 40
						double dif = 40 - st_weekly_hrs;								
						if(excess_hrs2 > dif && dif > 0){
								addToEarnedHash(code_id, dif);
								excess_hrs2 -= dif;
						}
						else{
								addToEarnedHash(code_id, excess_hrs2);
								excess_hrs2 = 0;
						}
				}
				//
				// second level for those in if above 40
				// for hours after 40
				//
				if(excess_hrs2 > critical_small){ 
						if(excess_hours_calculation_method.equals("Monetary")){
								code_id =CommonInc.overTime10EarnCodeID ; // "OT1.0";
								if(comp_factor > 1.0){
										code_id = CommonInc.overTime15EarnCodeID; // "OT1.5";
								}
						}
						else{ // Earn time
								if(comp_factor > 1.0){
										code_id = CommonInc.compTime15EarnCodeID; // "CE1.5";
								}
						}
						String dstr = ndf.format(excess_hrs2);
						excess_hrs2 = (double) (new Double(dstr));
						addToEarnedHash(code_id, excess_hrs2);								
				}
    }
    public void createProfRecord(){
				//
				String code_id = CommonInc.profHoursEarnCodeID ;// "PROF HRS";	
				if(prof_hrs > critical_small){
						String dstr = ndf.format(prof_hrs);
						addToEarnedHash(code_id, new Double(dstr));
				}
    }	
}
