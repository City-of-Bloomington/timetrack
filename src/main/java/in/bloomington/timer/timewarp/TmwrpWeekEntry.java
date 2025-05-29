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
    //     static final double critical_small = 0.01;
    String week_title = ""; 
    double total_hrs = 0, regular_hrs = 0,
	non_reg_hrs = 0, earn_time_used = 0,
	earned_time = 0, earned_time_daily=0,
	unpaid_hrs = 0,
	over_time15 = 0, over_time20=0, over_time25 = 0;
    double st_weekly_hrs = 40,
	daily_hrs = 8,
	comp_weekly_hrs = 0,
	comp_factor = 1,
	holiday_factor = 1;
    int total_mints=0, regular_mints = 0;
    double holy_earn_hrs = 0;

    double prof_hrs = 0, excess_hrs = 0, net_reg_hrs= 0;
    boolean handSpecial = false; 
    List<HolidayWorkDay> holyWorkDays = null;
    Department department = null;
    JobTask job = null;
    Employee employee = null;
    SalaryGroup salaryGroup = null;
    Group group = null;
    Shift shift = null;
    // irregular work days employee do not get overtime or comp time
    // on week where there is a holiday only
    boolean isIrregularWorkDayEmployee = false;
    boolean hasHolidays = false;		
    //
    String excess_hours_earn_type = "";
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
			  JobTask val2,
			  String val3){ 
	debug = deb;
	setDepartment(val);
	setJob(val2);
	setWeekTitle(val3);
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
			  int val3, // split day
			  String val4
			  ){
	//
	this(deb, val, val2, val4);
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
    public void setHasHolyDays(){
	hasHolidays = true;
    }
    public void setWeekTitle(String val){
	if(val != null)
	    week_title = val;
								
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
	    isIrregularWorkDayEmployee = job.isIrregularWorkDayEmployee();
	    salaryGroup = job.getSalaryGroup();
	    st_weekly_hrs = job.getWeekly_regular_hours();
	    comp_weekly_hrs = job.getComp_time_weekly_hours();
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
		Department dept = group.getDepartment();
		if(dept != null){
		    setDepartment(dept);
		}
		// comp time, overtime or donation
		excess_hours_earn_type = group.getExcessHoursEarnType();
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
		// System.err.println(" split day "+splitDay);
		// System.err.println(" index "+te.getOrder_index());
		if((te.getOrder_index()%7) < splitDay){
		    // System.err.println("one "+te.getHours());
		    splitOne.add(te);
		}
		else{
		    splitTwo.add(te);
		    // System.err.println("two "+te.getHours());
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
    void mergeTwoHashes(Hashtable<String, Double> tFrom,
			Hashtable<String, Double> tTo){
	if(tFrom != null && tFrom.size() > 0){
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
	if(!hash.isEmpty()){
	    all.putAll(hash);
	}
	if(!earnedHash.isEmpty()){
	    mergeTwoHashes(earnedHash, all);
	}
	if(handSpecial){
	    // already taken care of
	}
	return all;
    }
    public void doCalculations(){

	splitOne.doCalculations();
	splitTwo.doCalculations();
	if(hasSplitDay()){
	    /*
	      System.err.println(week_title+" split 1 ");
	      splitOne.showInfo();
	      System.err.println(week_title+" split 2 ");						
	      splitTwo.showInfo();
	    */
	    total_mints = splitOne.getTotalMinutes()+splitTwo.getTotalMinutes();
	    regular_mints = splitOne.getRegularMinutes()+splitTwo.getRegularMinutes();
	    //
	    regular_hrs = regular_mints/60.;
	    //
	    // the following earned_time are the overtime for certain employees
	    // from daily earns (union)
	    earned_time_daily = splitOne.getEarnedTime()+splitTwo.getEarnedTime();
	    earn_time_used = splitOne.getEarnedTimeUsed()+splitTwo.getEarnedTimeUsed();
	    unpaid_hrs = splitOne.getUnpaidHrs()+splitTwo.getUnpaidHrs();
	}
	else{
	    /*
	      System.err.println(week_title+" split 1 ");
	      splitOne.showInfo();
	    */
	    total_mints = splitOne.getTotalMinutes();
	    regular_mints = splitOne.getRegularMinutes();
	    //
	    regular_hrs = regular_mints/60.;
	    //
	    // the following earned_time are the overtime for certain employees
	    // from daily earns (union)
	    earned_time_daily = splitOne.getEarnedTime();
	    earn_time_used = splitOne.getEarnedTimeUsed();
	    unpaid_hrs = splitOne.getUnpaidHrs();						
	}
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
	    if(regular_hrs > CommonInc.critical_small && net_reg_hrs + CommonInc.critical_small < regular_hrs){
		double ratio = net_reg_hrs / regular_hrs; 
		splitOne.adjustRegHashBy(ratio);
		if(hasSplitDay()){								
		    splitTwo.adjustRegHashBy(ratio);
		}
	    }
	    mergeRegHashtablesFromSplits();
	}
    }
    //
    void mergeHashtablesFromSplits(){
	//
	// non regular hours
	Hashtable<String, Double> table = splitOne.getNonRegularHours();
	if(!table.isEmpty())
	    mergeWithHash(table, hash);

	if(hasSplitDay()){
	    Hashtable<String, Double> table2 = splitTwo.getNonRegularHours();
	    if(!table2.isEmpty())
		mergeWithHash(table2, hash);
	}
    }
    // reg
    void mergeRegHashtablesFromSplits(){
	//
	Hashtable<String, Double> table = splitOne.getRegularHash();
	if(!table.isEmpty())				
	    mergeWithHash(table, regHash);
	if(hasSplitDay()){
	    Hashtable<String, Double> table2 = splitTwo.getRegularHash();
	    if(!table2.isEmpty())
		mergeWithHash(table2, regHash);
	}
	// to all
	// if(!hash.isEmpty())
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
	if(hasSplitDay()){				
	    if(splitTwo.hasMonetary()){
		table = splitTwo.getMonetaryHash();
		mergeWithHash(table, monetaryHash);						
	    }
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
	    if(!salaryGroup.isExempt() &&
	       !salaryGroup.isFireSworn() && 
	       !salaryGroup.isFireSworn5x8()){ // prof hrs, exempt and firesworn
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
	prof_hrs = total_hrs - st_weekly_hrs - earned_time - holy_earn_hrs;
	if(prof_hrs < CommonInc.critical_small){
	    prof_hrs = 0;
	}
    }
    boolean hasProfHours(){
	return prof_hrs > CommonInc.critical_small;
    }
    boolean hasSplitDay(){
	return splitDay < 7 && splitDay > 0;
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
		if(regular_hrs > CommonInc.cityStandardWeeklyHrs){
		    net_reg_hrs = CommonInc.cityStandardWeeklyHrs;
		}
		else{
		    net_reg_hrs = regular_hrs;
		}
		return;
	    }
	    else if(salaryGroup.isUnionned()){
		net_reg_hrs = regular_hrs - earned_time - earned_time_daily;
		if(net_reg_hrs < CommonInc.critical_small){
		    net_reg_hrs = 0;
		}
		return;
	    }
	}
	net_reg_hrs = regular_hrs - earned_time - prof_hrs - holy_earn_hrs;
				
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
	total_hrs = total_mints/60.;
	double netHours = total_hrs - earned_time - holy_earn_hrs;
	//
	// for full time working less than 40 hrs
	//
	if(isIrregularWorkDayEmployee && hasHolidays){
	    //
	    // we use only regular hours to find if more than 40
	    //
	    excess_hrs = regular_hrs -
		comp_weekly_hrs -
		holy_earn_hrs -
		earned_time_daily;
	    if(excess_hrs > CommonInc.critical_small){
		earned_time = excess_hrs;
	    }
	    return;
	}
	else{
	    if(salaryGroup != null){
		if(salaryGroup.isTemporary()){
		    excess_hrs = netHours < 40 ? 0: netHours - 40;
		}
		else if(salaryGroup.isPoliceSworn()){
		    // fire excess is handled by NW
		    excess_hrs = 0;
		}						
		else{
		    //
		    if(netHours > comp_weekly_hrs){
			excess_hrs = netHours - comp_weekly_hrs;
		    }
		}
	    }
	}
	// we may have carry over from daily such as union
	if(excess_hrs >= earned_time_daily){
	    excess_hrs = excess_hrs - earned_time_daily;
	}
	else if(excess_hrs < earned_time_daily){
	    excess_hrs = 0;
	}
	if(excess_hrs > CommonInc.critical_small){
	    earned_time = excess_hrs;
	}
    }
    /**
     * if the employee has excess hours, then depending on type of employement,
     * they may get CE, OT or PROF hours
     */
    public void createEarnRecord(){
	//
	String code_id = "";
	if(excess_hrs <= CommonInc.critical_small) return;
	if(salaryGroup != null){
	    if(salaryGroup.isTemporary()){
		if(comp_factor > 1.0)
		    code_id = CommonInc.overTime15EarnCodeID; // "OT1.5";	// no CE1.5 for temp
		else
		    code_id = CommonInc.overTime10EarnCodeID; // "OT1.0";
		addToEarnedHash(code_id, excess_hrs);
		return;
	    }
	    if(salaryGroup.isExcessCulculationPayPeriod()){
		// we are interested in weekly only for now
		// anything else we ignore here
		return;
	    }
	}
	if(excess_hours_earn_type.equals("Donation")){
	    return;
	}
	//
	// this should work for full time or part time with benefit
	//
	code_id = CommonInc.compTime10EarnCodeID; // "CE1.0";
	//
	// we want to keep excess_hrs as we may need it
	// for other stuff, we are interested in weekly exess hours
	// even with daily union employee, they may work 8 daily hours
	// but they may total more than 40 if they work 48 hrs for example
	//
	// double excess_hrs2 = excess_hrs;
	double excess_hrs2 = earned_time;
	/**
	 * part time employee, they should get regular hours even when
	 * they work more than weekly hours that is less than 40
	 * commented out on 1/4/2021 according to HR 
	 *
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
	*/
	//
	// second level for those in if above 40
	// for hours after 40
	//
	if(excess_hrs2 > CommonInc.critical_small){ 
	    if(excess_hours_earn_type.equals("Monetary")){
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
    public boolean hasExessHours(){
	return excess_hrs > CommonInc.critical_small;
    }
    public boolean hasInsufficientTotalHours(){
	boolean ret = false;
				
	if(salaryGroup != null && !salaryGroup.isTemporary()){
	    if(total_hrs + CommonInc.critical_small < st_weekly_hrs){
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
	if(isIrregularWorkDayEmployee && hasHolidays){
	    return;
	}				
	String code_id = "";
	total_hrs = total_mints/60.;
	double netHours = total_hrs - earned_time;
	double extra_hrs = 0;
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
	    else{
		if(netHours > st_weekly_hrs)
		    extra_hrs = netHours - st_weekly_hrs;
	    }
	}
	if(extra_hrs > CommonInc.critical_small){
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
	    if(holy_earn_hrs > CommonInc.critical_small){
		// 
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

    public void createProfRecord(){
	//
	String code_id = CommonInc.profHoursEarnCodeID ;// "PROF HRS";	
	if(prof_hrs > CommonInc.critical_small){
	    String dstr = ndf.format(prof_hrs);
	    addToEarnedHash(code_id, new Double(dstr));
	}
    }	
}
