package in.bloomington.timer.list;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.TreeMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;
import java.util.Date;
import java.sql.*;
import org.javatuples.Triplet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;

public class TimeBlockList{

    boolean debug = false;
    static final long serialVersionUID = 4200L;
    static Logger logger = LogManager.getLogger(TimeBlockList.class);
    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
    DecimalFormat dfn = new DecimalFormat("##0.00");		
    String employee_id = "", pay_period_id="", job_id="";
    String date_from="", date_to="", document_id="", department_id="";
    String date = "";// specific day
    String code = "", code2 = ""; // needed for HAND and planning
    String accrual_as_of_date = "";
    boolean active_only = false, for_today = false, dailyOnly=false,
	clockInOnly = false, hasClockInAndOut = false, hasBlockNotes = false;
    double total_hours = 0.0, week1_flsa=0.0, week2_flsa=0.0;
    //
    //the following are needed for clocked-In search
    int clocked_in_hour = -1, clocked_in_minute=-1;
    //
    int total_minutes = 0;
    double week1Total = 0, week2Total = 0;
    double week1AmountTotal = 0, week2AmountTotal = 0;
    int week1MintTotal = 0, week2MintTotal = 0;
    String duration = "";
    List<TimeBlock> timeBlocks = null;
    List<TimeBlock> timeBlockWithNotes = null;
    Set<JobType> jobTypeSet = new HashSet<>();
    Hashtable<Integer, List<TimeBlock>> blocks = new Hashtable<>();
    //
    // for 14 days pay period
    //
    Map<Integer, List<TimeBlock>> dailyBlocks = new TreeMap<>();
    Map<Integer, Double> hourCodeTotals = new TreeMap<>();
    Map<String, Double> hourCodeWeek1 = new TreeMap<>();
    Map<String, Double> hourCodeWeek2 = new TreeMap<>();
    //
    Map<Integer, Double> amountCodeTotals = new TreeMap<>();
    Map<String, Double> amountCodeWeek1 = new TreeMap<>();
    Map<String, Double> amountCodeWeek2 = new TreeMap<>();

    Map<String, Double> reasonTotals = new TreeMap<>();
    Map<String, Double> reasonWeek1 = new TreeMap<>();
    Map<String, Double> reasonWeek2 = new TreeMap<>();		
    Map<Integer, Double> usedAccrualTotals = new TreeMap<>();
    Map<Integer, Double> earnedAccrualTotals = new TreeMap<>();		
    HolidayList holidays = null;
    //
    // week 1,2 / hour_code_id /hours
    Map<JobType, Map<Integer, Integer>> dailyInt = new TreeMap<>();
		
    Map<Integer, Map<Integer, Double>> usedWeeklyAccruals = new TreeMap<>();
    List<Triplet<String, String, String>> unscheduleds = null;
    Document document = null;
    List<String> jobNames = null;
    public TimeBlockList(){
    }
    public TimeBlockList(String val){
	setEmployee_id(val);
    }
    public void setDocument_id (String val){
	if(val != null)
	    document_id = val;
    }		
    public void setEmployee_id (String val){
	if(val != null)
	    employee_id = val;
    }
    public void setJob_id (String val){
	if(val != null)
	    job_id = val;
    }
    public void setDepartment_id(String val){
	if(val != null)
	    department_id = val;
    }		
    public void setPay_period_id (String val){
	if(val != null)
	    pay_period_id = val;
    }
    public void setDate_from (String val){
	if(val != null)
	    date_from = val;
    }
    public void setDate_to (String val){
	if(val != null)
	    date_to = val;
    }
    public void setDate(String val){
	if(val != null)
	    date = val;
    }
    public void setCode(String val){
	if(val != null)
	    code = val;
    }
    public void setCode2(String val){
	if(val != null)
	    code2 = val;
    }
    // hours duration for clock-in, clock-out 
    public void setDuration(String val){
	if(val != null)
	    duration = val;
    }		
    public String getEmployee_id(){
	return employee_id;
    }
    public String getPay_period_id(){
	return pay_period_id;
    }
    public String getDate_from(){
	return date_from;
    }
    public String getDate_to(){
	return date_to;
    }
    public String getDate(){
	return date;
    }		
    public void setActiveOnly(){
	active_only = true;
    }
    public void setForToday(){
	for_today = true;
    }
    public void setDailyOnly(){
	dailyOnly = true;
    }
    public void hasClockInOnly(){
	clockInOnly = true;
    }
    public void hasClockInAndOut(){
	hasClockInAndOut = true;
    }
    public List<TimeBlock> getTimeBlocks(){
	return timeBlocks;
    }
    public boolean hasTimeBlockWithNotes(){
	return timeBlockWithNotes != null && timeBlockWithNotes.size() > 0;
    }
    public List<TimeBlock> getTimeBlockWithNotes(){
	return timeBlockWithNotes;
    }
    public Map<Integer, List<TimeBlock>> getDailyBlocks(){
	return dailyBlocks;
    }
    public Map<Integer, Double> getHourCodeTotals(){
	return hourCodeTotals;
    }
    public Map<Integer, Double> getAmountCodeTotals(){
	return amountCodeTotals;
    }		
    public Map<Integer, Double> getUsedAccrualTotals(){
	return usedAccrualTotals;
    }
    public Map<Integer, Double> getEarnedAccrualTotals(){
	return earnedAccrualTotals;
    }
    // total hour codes for week1
    public Map<String, Double> getHourCodeWeek1(){
	return hourCodeWeek1;
    }
    // total hour codes for week2
    public Map<String, Double> getHourCodeWeek2(){
	return hourCodeWeek2;
    }
    // total hour codes for week1
    public Map<String, Double> getAmountCodeWeek1(){
	return amountCodeWeek1;
    }
    // total hour codes for week2
    public Map<String, Double> getAmountCodeWeek2(){
	return amountCodeWeek2;
    }
    public Map<String, Double> getReasonTotals(){
	return reasonTotals;
    }		
    public Map<String, Double> getReasonWeek1(){
	return reasonWeek1;
    }
    // total hour codes for week2
    public Map<String, Double> getReasonWeek2(){
	return reasonWeek2;
    }
    public boolean hasReasonTotals(){
	return reasonTotals != null && !reasonTotals.isEmpty();
    }

    public double getWeek1Total(){
	return week1MintTotal/60.;
    }
    public double getWeek2Total(){
	return week2MintTotal/60.;
    }		
    public double getWeek1AmountTotal(){
	return week1AmountTotal;
    }
    public double getWeek2AmountTotal(){
	return week2AmountTotal;
    }		
    public List<Triplet<String, String, String>> getUnscheduleds(){
	return unscheduleds;
    }
    public void setHasBlockNotes(){
	hasBlockNotes = true;
    }
    public boolean hasUnscheduleds(String emp_id){
	findUnscheduleds(emp_id);
	return unscheduleds != null && unscheduleds.size() > 0;
    }
    //
    // using total minutes instead of hours
    // then convert to hours
    public Map<JobType, Map<Integer, String>> getDaily(){
	Set<JobType> set = dailyInt.keySet();
	Map<JobType, Map<Integer, String>> mapd = new TreeMap<>();
	for(JobType str:set){
	    Map<Integer, String> map2 = new TreeMap<>();
	    Map<Integer, Integer> map = dailyInt.get(str);
	    for(int j=0;j<16;j++){ // 8 total week1, 15 total week2
		if(map.containsKey(j)){
		    int val = map.get(j);
		    map2.put(j, dfn.format(val/60.));
		}
		else{
		    map2.put(j, "0.0");
		}
	    }
	    mapd.put(str, map2);
	}
	return mapd;
    }		
		
    public Map<JobType, Map<Integer, Integer>> getDailyInt(){
	return dailyInt;
    }		
    public Map<Integer, Map<Integer, Double>> getUsedWeeklyAccruals(){
	return usedWeeklyAccruals;
    }

    public double getTotal_hours(){
	return getWeek1Total()+getWeek2Total();
    }		
    public double getWeek1_flsa(){
	return week1_flsa;
    }
    public double getWeek2_flsa(){
	return week2_flsa;
    }
    public boolean isHoliday(String date){
	if(holidays != null){
	    return holidays.isHoliday(date);
	}
	return false;
    }
    public String getHolidayName(String date){
	if(holidays != null){
	    return holidays.getHolidayName(date);
	}
	return "";
    }
    public String getAccrualAsOfDate(){
	return accrual_as_of_date;
    }
				
    public Document getDocument(){
	if(!document_id.isEmpty()){
	    Document one = new Document(document_id);
	    String back = one.doSelect();
	    if(back.isEmpty()){
		document = one;
	    }
	}
	return document;
    }
    public int getClockedInHour(){
	return clocked_in_hour;
    }
    public int getClockedInMinute(){
	return clocked_in_minute;
    }				
    // find employee jobs in this pay period
    //
    // normally one job only per document
    //
    private List<String> findJobNames(){
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", str="", str2="";
	List<String> jobNames = new ArrayList<>();
	String qq = "select "+
	    " distinct ps.name,j.id "+ // job name (position)
	    " from positions ps join jobs j on ps.id=j.position_id, "+
	    " time_documents d,"+
	    " pay_periods p ";
	String qw = "d.id=? and d.job_id=j.id ";
	qq = qq +" where "+qw;
	con = UnoConnect.getConnection();
	if(con == null){
	    logger.error(msg);
	    return null;
	}
	logger.debug(qq);
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, document_id);
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		str = rs.getString(1);
		if(!jobNames.contains(str)){
		    jobNames.add(str);
		}
		str2 = rs.getString(2);
		JobType one = new JobType(str2, str);
		jobTypeSet.add(one);
	    }
	}
	catch(Exception ex){
	    msg += " "+ex;
	    logger.error(msg+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(pstmt, rs);
	    UnoConnect.databaseDisconnect(con);
	}								
	return jobNames;
    }
    /**

     // modified on 3/6 to include hour code components that
     // will simplify building HourCode class since we are going
     // to needed more than before


     // modified for earn_code_reason_id
     create or replace view time_blocks_view as                                            select t.id time_block_id,                                                     t.document_id document_id,                                                      t.hour_code_id hour_code_id,                                                    t.earn_code_reason_id earn_code_reason_id,                                      t.date,                                                                         t.begin_hour begin_hour,                                                        t.begin_minute begin_minute,                                                    t.end_hour end_hour,                                                            t.end_minute end_minute,                                                        t.hours hours,                                                                  t.minutes,                                                                      t.amount amount,                                                                t.clock_in clock_in,                                                            t.clock_out clock_out,                                                          t.notes notes,                                                                  t.inactive inactive,                                                            datediff(t.date,p.start_date) order_id,                                         c.name code_name,                                                               c.description code_description,                                                 c.record_method record_method,                                                  c.accrual_id accrual_id,                                                        c.type code_type,                                                               c.default_monetary_amount,                                                      c.earn_factor earn_factor,                                                      c.holiday_related holiday_related,                                              cf.nw_code nw_code_name,                                                        ps.name job_name,                                                               j.id job_id,                                                                    d.pay_period_id pay_period_id,                                                  d.employee_id employee_id,                                                      r.description reason                                                            from time_blocks t                                                              join time_documents d on d.id=t.document_id                                     join pay_periods p on p.id=d.pay_period_id                                      join jobs j on d.job_id=j.id                                                    join positions ps on j.position_id=ps.id                                        join hour_codes c on t.hour_code_id=c.id                                        left join code_cross_ref cf on c.id=cf.code_id 			          left join earn_code_reasons r on r.id=t.earn_code_reason_id 
			 
    */
    //
    /**
     // 
		 
		 
     */
    public String find(){

	prepareBlocks();
	prepareHolidays();
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = "select "+
	    "v.time_block_id,"+
	    "v.document_id,"+
	    "v.hour_code_id,"+
	    "v.earn_code_reason_id,"+
	    "date_format(v.date,'%m/%d/%Y'),"+ // 5
						
	    "v.begin_hour,"+
	    "v.begin_minute,"+
	    "v.end_hour,"+
	    "v.end_minute,"+
	    "v.hours,"+  //10

	    "v.minutes,"+
	    "v.amount,"+
	    "v.clock_in,"+
	    "v.clock_out,"+
	    "v.notes,"+
	    
	    "v.inactive,"+
	    "v.order_id,"+
	    "v.code_name,"+
	    "v.code_description,"+
	    "v.record_method,"+ //20
	    
	    "v.accrual_id,"+
	    "v.code_type,"+
	    "v.default_monetary_amount,"+
	    "v.earn_factor,"+
	    "v.holiday_related,"+ //25
	    
	    "v.nw_code_name,"+
	    "v.job_name,"+
	    "v.job_id, "+
	    "v.pay_period_id,"+
	    "v.employee_id, "+ //30
	    
	    "v.reason "+ //31
	    "from time_blocks_view v ";
	String qw = "";

	if(!document_id.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";						
	    qw += "v.document_id=? ";
	}
	else{
	    if(!department_id.isEmpty()){
		qq += " join jobs j on j.id=v.job_id ";
		qq += " join `groups` g on g.id=j.group_id ";
		if(!qw.isEmpty()) qw += " and ";								
		qw += "  g.department_id=? ";
	    }
	    if(!pay_period_id.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";						
		qw += "v.pay_period_id=? ";
	    }
	    if(!employee_id.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";
		qw += "v.employee_id=? ";
	    }
	    if(!job_id.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";
		qw += "v.job_id=? ";
	    }
	}
	if(!date_from.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += "v.date >= ? ";
	}
	if(!date_to.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += "v.date <= ? ";
	}
	if(!date.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += "v.date = ? ";
	}
	if(!code.isEmpty() && !code2.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += "(v.code_name like ? or v.code_name like ?)";
	}
	else if(!code.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += "v.code_name like ? ";
	}
	if(clockInOnly){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " v.clock_in is not null and v.clock_out is null ";
	}
	else if(hasClockInAndOut){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " v.clock_in is not null and v.clock_out is not null ";
	}
	if(hasBlockNotes){
	    if(!qw.isEmpty()) qw += " and ";	    
	    qw += " v.notes is not null";
	}
	if(active_only){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " v.inactive is null ";
	}
	if(!qw.isEmpty()){
	    qq += " where "+qw;
	}
	qq += " order by v.date, v.begin_hour ";
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = " Could not connect to DB ";
	    logger.error(msg);
	    return msg;
	}
	logger.debug(qq);
	try{
	    pstmt = con.prepareStatement(qq);
	    int jj=1;
	    if(!document_id.isEmpty()){
		pstmt.setString(jj++, document_id);
	    }
	    else{
		if(!department_id.isEmpty()){
		    pstmt.setString(jj++, department_id);
		}										
		if(!pay_period_id.isEmpty()){
		    pstmt.setString(jj++, pay_period_id);
		}
		if(!employee_id.isEmpty()){
		    pstmt.setString(jj++, employee_id);
		}
		if(!job_id.isEmpty()){
		    pstmt.setString(jj++, job_id);
		}
	    }
	    if(!date_from.isEmpty()){
		java.util.Date date_tmp = df.parse(date_from);
		pstmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
	    }
	    if(!date_to.isEmpty()){
		java.util.Date date_tmp = df.parse(date_to);
		pstmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
	    }
	    if(!date.isEmpty()){
		java.util.Date date_tmp = df.parse(date);
		pstmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
	    }
	    if(!code.isEmpty() && !code2.isEmpty()){
		pstmt.setString(jj++, code);
		pstmt.setString(jj++, code2);								
	    }
	    else if(!code.isEmpty()){
		pstmt.setString(jj++, code);
	    }						
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		int code_id = rs.getInt(3);
		String date = rs.getString(5); 								
		double hrs = rs.getDouble(10);
		int mints = rs.getInt(11);
		double amnt = rs.getDouble(12);
		int order_id = rs.getInt(17); 

		String code_name = rs.getString(18); 
		String code_desc = rs.getString(19);
		String record_method = rs.getString(20);
		String related_accrual_id = "";
		str = rs.getString(21);
		if(str != null && !str.isEmpty()){
		    related_accrual_id = str;
		}
		String reason = "";
		str = rs.getString(31);
		if(str != null)
		    reason = str;
		String code_type = rs.getString(22);
		double default_amount = rs.getDouble(23);
		double earn_factor = rs.getDouble(24);
		String holiday_related = rs.getString(25);
		String job_name = rs.getString(27); // job name
		String job_id = rs.getString(28);
		//
		boolean isHoliday = isHoliday(date);
		String holidayName = "";
		if(isHoliday){
		    holidayName = getHolidayName(date);
		}
		HourCode hrCode = new HourCode(""+code_id,
					       code_name,
					       code_desc,
					       record_method,
					       related_accrual_id,
					       code_type,
					       default_amount,
					       earn_factor,
					       holiday_related != null);
		if(code_desc == null) code_desc = "";
		if(hrCode.isRecordMethodMonetary()){
		    hrs = 0;
		    if(amnt == 0.0){
			amnt = hrCode.getDefaultMonetaryAmount();
		    }
		}
		else if(hrCode.isCallOut()){
		    if(hrs < 3.) hrs = 3;
		    amnt = 0;
		}
		if(!dailyOnly){
		    if(timeBlocks == null)
			timeBlocks = new ArrayList<>();
		    TimeBlock one =
			new TimeBlock(rs.getString(1),
				      rs.getString(2),
				      rs.getString(3), // code_id
				      rs.getString(4), // earn_code_reason
				      rs.getString(5), // date
																			
				      rs.getInt(6),  // b
				      rs.getInt(7),
				      rs.getInt(8),
				      rs.getInt(9),
				      hrs,           // 10
																			
				      mints,
				      amnt,
				      rs.getString(13), // clock in
				      rs.getString(14), // clock out
				      isHoliday,  //15
																			
				      holidayName,
				      rs.getString(15),  // notes
				      rs.getString(16) != null,

				      rs.getInt(17), // order id
				      code_name, // 20
				      
				      code_desc,  // 
				      rs.getString(26),  // nw_code
				      job_name, 
				      job_id   //24
				      );
		    one.setHourCode(hrCode); 
		    timeBlocks.add(one);
		    if(one.hasBlockNotes()){
			if(timeBlockWithNotes == null) timeBlockWithNotes = new ArrayList<>();
			timeBlockWithNotes.add(one);
		    }
		    addToBlocks(order_id, one);
		    if(!related_accrual_id.isEmpty() &&
		       code_type.equals("Used")){
			addToUsedAccruals(order_id, code_id, hrs);
		    }
		}
		if(hrCode.isRegular()){
		    if(order_id < 7)
			week1_flsa += hrs;
		    else
			week2_flsa += hrs;
		}
		if(!reason.isEmpty()){
		    reason = code_name+" - "+reason;
		    addToReasons(order_id, reason, hrs);
		}
		code_name += ": "+code_desc;
		JobType jtype = new JobType(job_id, job_name);
		if(hrCode.isRecordMethodMonetary()){
		    addToAmountCodeTotals(order_id, code_id, code_name, amnt);
		    if(order_id < 7){
			week1AmountTotal += amnt;
		    }
		    else{
			week2AmountTotal += amnt;
		    }
		}
		else{
		    addToHourCodeTotals(order_id, code_id, code_name, hrs);
		    addToDaily(jtype, order_id, mints, code_name);
		    total_hours += hrs;
		    total_minutes += mints;
		}
	    }
	}
	catch(Exception ex){
	    msg += " "+ex;
	    logger.error(msg+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(pstmt, rs);
	    UnoConnect.databaseDisconnect(con);
	}
	return msg;
    }
    /**
     * list of earn code - earn_reason, hours
     */
    void addToReasons(int order_id, String reason, double hrs){
	if(reasonTotals.containsKey(reason)){
	    Double val = reasonTotals.get(reason);
	    double val2 = val.doubleValue()+hrs;
	    reasonTotals.put(reason, val2);
	    if(order_id < 7){
		if(reasonWeek1.containsKey(reason)){
		    val = reasonWeek1.get(reason);
		    val2 = val.doubleValue()+hrs;										
		    reasonWeek1.put(reason, val2);
		}
		else{
		    reasonWeek1.put(reason, hrs);
		}
	    }
	    else{
		if(reasonWeek2.containsKey(reason)){
		    val = reasonWeek2.get(reason);
		    val2 = val.doubleValue()+hrs;										
		    reasonWeek2.put(reason, val2);
		}
		else{
		    reasonWeek2.put(reason, hrs);
		}
	    }
	}
	else{
	    reasonTotals.put(reason, hrs);
	    if(order_id < 7){
		reasonWeek1.put(reason, hrs);
	    }
	    else{
		reasonWeek2.put(reason, hrs);
	    }
	}
    }
    /**
     * this method is needed for employee with multiple jobs and find out
     * if they have clocked-in already but no clocked-out
     *
     */
    public String findDocumentForClockInOnly(int time_hr, int time_min){

	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", str="";
	double dd_time = time_hr+(time_min/60.);
	Double dd_time2 = dd_time+24;
	String qq = "select "+
	    " t.document_id, "+
	    " t.begin_hour,t.begin_minute "+
	    " from time_blocks t "+
	    " join time_documents d on d.id=t.document_id "+
	    " join pay_periods p on p.id=d.pay_period_id "+
	    " where "+
	    " t.clock_in is not null and t.clock_out is null "+
	    " and t.inactive is null "+
	    " and d.pay_period_id=? "+
	    " and d.employee_id=? ";
	qq += " and ";						
	qq += " ((("+dd_time+" - (t.begin_hour+t.begin_minute/60.)) between 0 and ? and t.date=?) "+
	    "  or "+
	    " (("+dd_time2+" - (t.begin_hour+t.begin_minute/60.)) between 0 and ? and t.date=?)) ";
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = " Could not connect to DB ";
	    logger.error(msg);
	    return msg;
	}
	logger.debug(qq);
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, pay_period_id);
	    pstmt.setString(2, employee_id);
	    pstmt.setString(3, duration);
	    String date = Helper.getToday();
	    java.util.Date date_tmp = df.parse(date);
	    pstmt.setDate(4, new java.sql.Date(date_tmp.getTime()));
	    pstmt.setString(5, duration);						
	    String date2 = Helper.getYesterday();
	    date_tmp = df.parse(date2);
	    pstmt.setDate(6, new java.sql.Date(date_tmp.getTime()));						
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		document_id = rs.getString(1);
		clocked_in_hour = rs.getInt(2);
		clocked_in_minute = rs.getInt(3);
	    }
	}
	catch(Exception ex){
	    msg += " "+ex;
	    logger.error(msg+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(pstmt, rs);
	    UnoConnect.databaseDisconnect(con);
	}
	return msg;
    }
    /**
     * find time blocks for clock-in only
     * in today or yesterday with time duration of 12 hrs
     *
     select t.id,t.document_id,t.hour_code_id,date_format(t.date,'%m/%d/%Y'),t.begin_hour,t.begin_minute,t.end_hour,t.end_minute,t.hours,t.clock_in,t.clock_out,((hour(current_time()) + minute(current_time())/60.) - (t.begin_hour+t.begin_minute/60.)) tdif  from time_blocks t,time_documents d,pay_periods p                        where t.document_id=d.id and d.pay_period_id=p.id and d.id=1719                  and p.id = 547                                                                  and t.date >= '2018-12-20' and t.date <= '2018-12-21'                           and ((((hour(current_time()) + minute(current_time())/60.) - (t.begin_hour+t.begin_minute/60.)) <= 12.0                                                         and ((hour(current_time()) + minute(current_time())/60.) - (t.begin_hour+t.begin_minute/60.)) > 0.) or                                                          (((hour(current_time()) + 12 + minute(current_time())/60.) - (t.begin_hour+t.begin_minute/60.)) <= 12.0                                                          and ((hour(current_time()) + 12+ minute(current_time())/60.) - (t.begin_hour+t.begin_minute/60.)) > 0.) or                                                     (((hour(current_time()) + 24 + minute(current_time())/60.) - (t.begin_hour+t.begin_minute/60.)) <= 12.0                                                           and ((hour(current_time()) + 24+ minute(current_time())/60.) - (t.begin_hour+t.begin_minute/60.)) > 0.))                                                       and t.clock_in is not null and t.clock_out is null                              and t.inactive is null 

     select t.id,t.document_id,t.hour_code_id,date_format(t.date,'%m/%d/%Y'),t.begin_hour,t.begin_minute,t.end_hour,t.end_minute,t.hours,t.clock_in,t.clock_out,((hour(current_time()) + minute(current_time())/60.) - (t.begin_hour+t.begin_minute/60.)) tdif,                                                                   ((hour(current_time()) + minute(current_time())/60.) - (t.begin_hour+t.begin_minute/60.)) tdif2,                                                                ((hour(current_time()) + 24 + minute(current_time())/60.) - (t.begin_hour+t.begin_minute/60.)) tdif4                                                               from time_blocks t,time_documents d,pay_periods p                               where t.document_id=d.id and d.pay_period_id=p.id and d.id=1719                  and p.id = 547                                                                  and t.date between '2018-12-20' and '2018-12-21'                            and (                                                                               (((hour(current_time()) + minute(current_time())/60.) - (t.begin_hour+t.begin_minute/60.)) between 0 and 13.0 and t.date = '2018-12-21') or                                                (((hour(current_time()) + 24 + minute(current_time())/60.) - (t.begin_hour+t.begin_minute/60.)) between 0 and 13.0 and t.date='2018-12-20'))                                           and t.clock_in is not null and t.clock_out is null                              and t.inactive is null 
								
		 
    */ 
    public String findTimeBlocksForClockIn(int time_hr, int time_min){
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg = "";
	double dd_time = time_hr+time_min/60.;
	double dd_time2 = dd_time+24;
	String qq = "select t.id,"+
	    " t.document_id,"+
	    "t.hour_code_id,"+
	    "t.earn_code_reason_id,"+
	    "date_format(t.date,'%m/%d/%Y'),"+
						
	    "t.begin_hour,"+
	    "t.begin_minute,"+
	    "t.end_hour,"+
	    "t.end_minute,"+
	    "t.hours,"+

	    "t.minutes,"+
	    "t.amount,"+
	    "t.clock_in,"+
	    "t.clock_out,"+
	    "t.notes"+
	    " from time_blocks t,time_documents d,pay_periods p ";
	String qw = " t.document_id=d.id and d.pay_period_id=p.id ";
	if(!pay_period_id.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";						
	    qw += "d.pay_period_id=? ";
	}
	if(!document_id.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";						
	    qw += "t.document_id=? ";
	}				
	if(!employee_id.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += "d.employee_id=? ";
	}
	if(!job_id.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += "d.job_id=? ";
	}				
	if(!date_from.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += "t.date >= ? ";
	}
	if(!date_to.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += "t.date <= ? ";
	}
	if(clockInOnly){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " t.clock_in is not null and t.clock_out is null ";
	}
	if(!duration.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " ((("+dd_time+" - (t.begin_hour+t.begin_minute/60.)) between 0 and ? and t.date=?) "+
		"  or "+
		" (("+dd_time2+" - (t.begin_hour+t.begin_minute/60.)) between 0 and ? and t.date=?)) ";
	}
	if(active_only){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " t.inactive is null ";
	}
	if(!qw.isEmpty()){
	    qq += " where "+qw;
	}
	// qq += " order by t.date, t.begin_hour ";
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = " Could not connect to DB ";
	    logger.error(msg);
	    return msg;
	}
	logger.debug(qq);
	try{
	    pstmt = con.prepareStatement(qq);
	    int jj=1;
	    if(!pay_period_id.isEmpty()){
		pstmt.setString(jj++, pay_period_id);
	    }
	    if(!document_id.isEmpty()){
		pstmt.setString(jj++, document_id);
	    }						
	    if(!employee_id.isEmpty()){
		pstmt.setString(jj++, employee_id);
	    }
	    if(!job_id.isEmpty()){
		pstmt.setString(jj++, job_id);
	    }						
	    if(!date_from.isEmpty()){
		java.util.Date date_tmp = df.parse(date_from);
		pstmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
	    }
	    if(!date_to.isEmpty()){
		java.util.Date date_tmp = df.parse(date_to);
		pstmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
	    }
	    if(!duration.isEmpty()){
		pstmt.setString(jj++, duration);
		java.util.Date date_tmp = df.parse(date_to);
		pstmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
		pstmt.setString(jj++, duration);
		date_tmp = df.parse(date_from);
		pstmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
	    }
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		if(timeBlocks == null)
		    timeBlocks = new ArrayList<>();
		TimeBlock one =
		    new TimeBlock(rs.getString(1),
				  rs.getString(2),
				  rs.getString(3),
				  rs.getString(4),
				  rs.getString(5),
																	
				  rs.getInt(6),
				  rs.getInt(7),
				  rs.getInt(8),
				  rs.getInt(9),
				  rs.getDouble(10),

				  rs.getInt(11),
				  rs.getDouble(12),
				  rs.getString(13),
				  rs.getString(14),
				  rs.getString(15)
				  );
		if(!timeBlocks.contains(one)){
		    timeBlocks.add(one);
		}
	    }
	}
	catch(Exception ex){
	    msg += " "+ex;
	    logger.error(msg+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(pstmt, rs);
	    UnoConnect.databaseDisconnect(con);
	}
	return msg;
				
    }
    //
    /*
     * find used accruals since last carry over balance
     * we find the related start pay period and requested pay period
     * and then find the used in between, sometimes the the two pay
     * periods are the same if the carry over was performed recently
     * and the pay period is the current
     */
    public String findUsedAccruals(){

	prepareBlocks();
	Connection con = null;
	PreparedStatement pstmt = null, pstmt2=null;
	ResultSet rs = null;
	String msg="", emp_id = ""; 
	String prev_pay_period="";
	//
	// find the previous pay period where the employee accruals were added
	// before that (if any) when added are set to last day of
	// the previous pay period
	//
	String qq = "select p2.id,date_format(p2.start_date,'%m/%d') from employee_accruals a, pay_periods p,pay_periods p2 where a.date >= p.start_date and a.date <= p.end_date and (p.id+1)=p2.id and p2.id <= ? and a.employee_id = ? order by p.id desc limit 1 ";
	//
	// we find the last accrual carry over date given employee id
	// and the end date of this pay period
	//
	// modified to handle multiple jobs
	//
	// find total accrual hours used  (PTO and other related
	// hour codes) since last accrual carry over date till the
	// end of the related given pay period
	//
	String qq2 = " select c.accrual_id, sum(t.hours)                                     from time_blocks t,time_documents d,hour_codes c,pay_periods p                  where t.document_id=d.id and t.inactive is null                                 and c.id=t.hour_code_id and c.inactive is null                                  and c.accrual_id is not null and c.type='Used'                                  and d.pay_period_id = p.id and d.employee_id=?                                  and p.id >= ? and p.id <=? group by c.accrual_id";
	//
	// we are looking for accrual used in this pap period only
	//
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = " Could not connect to DB ";
	    logger.error(msg);
	    return msg;
	}
	logger.debug(qq);
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, pay_period_id);										
	    pstmt.setString(2, employee_id);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		prev_pay_period = rs.getString(1);
		accrual_as_of_date = rs.getString(2);
	    }
	    qq = qq2;
	    pstmt2 = con.prepareStatement(qq2);
	    pstmt2.setString(1, employee_id);
	    pstmt2.setString(2, prev_pay_period);
	    pstmt2.setString(3, pay_period_id);						
	    rs = pstmt2.executeQuery();
	    while(rs.next()){
		int code_id = rs.getInt(1); // accrual_id now
		double hrs = rs.getDouble(2);
		usedAccrualTotals.put(code_id, hrs);
	    }
	}
	catch(Exception ex){
	    msg += " "+ex;
	    logger.error(msg+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(rs, pstmt, pstmt2);
	    UnoConnect.databaseDisconnect(con);
	}
	return msg;
    }
		
    /**
     * this function is intended to find the dates, earn code and hours
     * that the employee used within one year from now. This is needed
     * since some employees have limited number of times unscheduled times
     * can be used. This will help the managers to decide if they approve
     * or not, currently the two earn codes are the unscheduled PTOUN and SBUUN 
     */
    /*
      select date_format(t.date,'%m/%d/%Y') date, c.name code, sum(t.hours)           from time_blocks t,time_documents d,hour_codes c                                where t.document_id=d.id and t.inactive is null                                 and c.id=t.hour_code_id and c.inactive is null                                  and (c.name like 'PTO' or c.name like 'SBU')                                    and d.employee_id=1  and  t.date > str_to_date('07/01/2018','%m/%d/%Y')         and t.date <= curdate()                                                         group by date, code

    */
    public String findUnscheduleds(String doc_id){
	Connection con = null;
	PreparedStatement pstmt = null, pstmt2=null;
	ResultSet rs = null;
	Date end_date=null, prev_date = null;
	Calendar cal = Calendar.getInstance();
	String msg = "", emp_id="";
	String qq = "select d.employee_id,p.end_date from time_documents d, pay_periods p where d.pay_period_id=p.id and d.id=? ";
	String qq2 = "select date_format(t.date,'%m/%d/%Y') date, c.name code, sum(t.hours)                                                                               from time_blocks t,time_documents d,hour_codes c                                where t.document_id=d.id and t.inactive is null                                 and c.id=t.hour_code_id and c.inactive is null                                  and (c.name like 'PTOUN' or c.name like 'SBUUN')                                and d.employee_id=?  and  t.date >= ?  and t.date <= ?                          group by date, code ";
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = " Could not connect to DB ";
	    logger.error(msg);
	    return msg;
	}
	logger.debug(qq);
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, doc_id);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		emp_id = rs.getString(1);
		end_date = rs.getDate(2);
	    }
	    cal.setTime(end_date);
	    cal.add(Calendar.YEAR, -1);
	    prev_date = cal.getTime();
	    pstmt2 = con.prepareStatement(qq2);
	    pstmt2.setString(1, emp_id);
	    pstmt2.setDate(2, new java.sql.Date(prev_date.getTime()));
	    pstmt2.setDate(3, new java.sql.Date(end_date.getTime()));						
	    rs = pstmt2.executeQuery();
	    while(rs.next()){
		String str = rs.getString(1);
		String str2 = rs.getString(2);
		String str3 = rs.getString(3);
		if(unscheduleds == null){
		    unscheduleds = new ArrayList<Triplet<String, String, String>>();
		}
		Triplet<String, String, String> trp = Triplet.with(str, str2, str3);
		unscheduleds.add(trp);
	    }
	}
	catch(Exception ex){
	    msg += " "+ex;
	    logger.error(msg+":"+qq);
								
	}
	finally{
	    Helper.databaseDisconnect(rs, pstmt, pstmt2);
	    UnoConnect.databaseDisconnect(con);
	}
	return msg;
    }
    /**
     * find pending accruals that were added since the last
     * accrual carry over date 
     */
    public String findEarnedAccruals(){

	Connection con = null;
	PreparedStatement pstmt = null, pstmt2=null;
	ResultSet rs = null;
	String msg="", 
	    emp_id = ""; // payPeriod end_date
	String prev_pay_period="";
	//
	// find the previous pay period where the employee accruals were added
	// before that (if any) when added are set to last day of
	// the pay period
	//
	String qq = "select p2.id from employee_accruals a, pay_periods p,pay_periods p2 where a.date >= p.start_date and a.date <= p.end_date and (p.id+1)=p2.id and p2.id <= ? and a.employee_id = ? order by p.id desc limit 1 ";
	//
	// modified to handle multiple jobs
	//
	// find total accrual hours earned  (PTO and other related
	// hour codes) since last accrual carry over date
	//
	// we are using tmwrp_blocks instead of time_blocks
	// becuase it may include earn accruals from daily or weekly
	// overall totals
	//
	String qq2 = "select c.accrual_id, c.earn_factor, sum(b.hours)                   from tmwrp_blocks b,tmwrp_runs t, time_documents d,hour_codes c,                 pay_periods p                                                                   where t.document_id=d.id and b.run_id=t.id                                      and c.id=b.hour_code_id and c.inactive is null                                  and c.accrual_id is not null and c.earn_factor > 0                              and c.type='Earned' and d.pay_period_id = p.id                                  and d.employee_id=? and p.id >= ? and p.id <=?                                  group by c.accrual_id,c.earn_factor ";				
	//
	// we are looking for accrual used in this pap period only
	//
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = " Could not connect to DB ";
	    logger.error(msg);
	    return msg;
	}
	logger.debug(qq);
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, pay_period_id);										
	    pstmt.setString(2, employee_id);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		prev_pay_period = rs.getString(1);
	    }
	    qq = qq2;
	    pstmt2 = con.prepareStatement(qq2);
	    pstmt2.setString(1, employee_id);
	    pstmt2.setString(2, prev_pay_period);
	    pstmt2.setString(3, pay_period_id);
	    rs = pstmt2.executeQuery();
	    while(rs.next()){
		int code_id = rs.getInt(1); // accrual_id now
		double factor = rs.getDouble(2);
		double hrs = rs.getDouble(3);
		addToEarnedAccruals(code_id, factor, hrs);
	    }
	}
	catch(Exception ex){
	    msg += " "+ex;
	    logger.error(msg+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(rs, pstmt, pstmt2);
	    UnoConnect.databaseDisconnect(con);
	}
	return msg;
    }
    void addToEarnedAccruals(int code_id, double factor, double hrs){
	if(hrs > 0 && factor > 0){
	    if(earnedAccrualTotals.containsKey(code_id)){
		double ttl_hrs = earnedAccrualTotals.get(code_id);
		ttl_hrs += hrs*factor;
		earnedAccrualTotals.put(code_id, ttl_hrs);
	    }
	    else{
		earnedAccrualTotals.put(code_id, hrs*factor);
	    }
	}
    }
    void prepareBlocks(){
	prepareDaily();
    }
    void prepareDaily(){
	if(jobNames == null){
	    jobNames = findJobNames();
	    if(jobTypeSet.size() > 0){
		for(JobType one:jobTypeSet){
		    Map<Integer, Integer> map = new TreeMap<>();
		    for(int i=0;i<16;i++){
			map.put(i,0);
		    }
		    dailyInt.put(one, map);
		}
	    }
	}
    }
    void prepareHolidays(){
	HolidayList hl = new HolidayList(debug);
	if(!pay_period_id.isEmpty()){
	    hl.setPay_period_id(pay_period_id);
	}
	else{
	    if(!date_from.isEmpty())
		hl.setDate_from(date_from);
	    if(!date_to.isEmpty())
		hl.setDate_to(date_to);
	}
	String back = hl.find();
	if(back.isEmpty()){
	    holidays = hl;
	}
    }
    /**
     * hour codes ids that used accruals distributed
     * for week1 (index 1) or week2 (index 2) 
     */
    void addToUsedAccruals(int order_id, int code_id, double hrs){
	int week_id = 1;
	if(order_id > 6) week_id = 2; 
	if(usedWeeklyAccruals.containsKey(week_id)){
	    Map<Integer, Double> map = usedWeeklyAccruals.get(week_id);
	    if(map.containsKey(code_id)){
		double ttl_hrs = map.get(code_id);
		ttl_hrs += hrs;
		map.put(code_id, ttl_hrs);
	    }
	    else{
		map.put(code_id, hrs);
	    }
	}
	else{
	    Map<Integer, Double> map = new TreeMap<>();
	    map.put(code_id, hrs);
	    usedWeeklyAccruals.put(week_id, map);		
	}
    }
    void addToDaily(JobType jtype,
		    int order_id,
		    int mints,
		    String hr_code){
	int total = 0;
	try{
	    if(dailyInt.containsKey(jtype)){
		Map<Integer, Integer> map = dailyInt.get(jtype);
		//
		// leaving space for total at index 7
		//
		if(order_id > 6) order_id = order_id + 1;
		if(map != null && map.containsKey(order_id)){
		    total = map.get(order_id);
		    total += mints;
		    int week_total = 0;
		    if(order_id < 7){
			if(map.containsKey(7)){
			    week_total = map.get(7)+mints; // total week1
			}
			else{
			    week_total = mints;
			}
			map.put(7, week_total);
			week1MintTotal = week_total;
		    }
		    else{
			if(map.containsKey(15)){														
			    week_total = map.get(15)+mints; // total week2
			}
			else{
			    week_total = mints;
			}
			map.put(15, week_total);
			week2MintTotal = week_total;
		    }
		}
		map.put(order_id, total);
		dailyInt.put(jtype, map);
	    }
	}catch(Exception ex){
	    logger.error(ex);
	}
    }						
    void addToBlocks(int order_id, TimeBlock block){
	List<TimeBlock> list = dailyBlocks.get(order_id);
	if(list != null){
	    list.add(block);
	}
	else{
	    list = new ArrayList<TimeBlock>();
	    list.add(block);
	}
	dailyBlocks.put(order_id, list);
    }
    //
    // aggregate hour code for each week and total for payperiod
    //
    void addToHourCodeTotals(int order_id,
			     int hr_code_id,
			     String hr_code,
			     double hrs){
	if(hourCodeTotals.containsKey(hr_code_id)){
	    Double val = hourCodeTotals.get(hr_code_id);
	    double val2 = val.doubleValue()+hrs;
	    hourCodeTotals.put(hr_code_id, val2);
	    if(order_id < 7){
		if(hourCodeWeek1.containsKey(hr_code)){
		    val = hourCodeWeek1.get(hr_code);
		    val2 = val.doubleValue()+hrs;										
		    hourCodeWeek1.put(hr_code, val2);
		}
		else{
		    hourCodeWeek1.put(hr_code, hrs);
		}
	    }
	    else{
		if(hourCodeWeek2.containsKey(hr_code)){
		    val = hourCodeWeek2.get(hr_code);
		    val2 = val.doubleValue()+hrs;										
		    hourCodeWeek2.put(hr_code, val2);
		}
		else{
		    hourCodeWeek2.put(hr_code, hrs);
		}
	    }
	}
	else{
	    hourCodeTotals.put(hr_code_id, hrs);
	    if(order_id < 7){
		hourCodeWeek1.put(hr_code, hrs);
	    }
	    else{
		hourCodeWeek2.put(hr_code, hrs);
	    }
	}
    }
    void addToAmountCodeTotals(int order_id,
			       int hr_code_id,
			       String hr_code,
			       double amount){
	try{
	    if(amountCodeTotals.containsKey(hr_code_id)){
		Double val = amountCodeTotals.get(hr_code_id);
		double val2 = val.doubleValue()+amount;
		amountCodeTotals.put(hr_code_id, val2);
		if(order_id < 7){
		    if(amountCodeWeek1.containsKey(hr_code)){
			val = amountCodeWeek1.get(hr_code);
			val2 = val.doubleValue()+amount;
			amountCodeWeek1.put(hr_code, val2);
		    }
		    else{
			amountCodeWeek1.put(hr_code, amount);
		    }
		}
		else{
		    if(amountCodeWeek2.containsKey(hr_code)){
			val =amountCodeWeek2.get(hr_code);
			val2 = val.doubleValue()+amount;										
			amountCodeWeek2.put(hr_code, val2);
		    }
		    else{
			amountCodeWeek2.put(hr_code, amount);
		    }
		}
	    }
	    else{
		amountCodeTotals.put(hr_code_id, amount);
		if(order_id < 7){
		    amountCodeWeek1.put(hr_code, amount);
		}
		else{
		    amountCodeWeek2.put(hr_code, amount);
		}
	    }
	}catch(Exception ex){
	    logger.error(ex);
	}
    }

    /*
     *
     // look for records for employees who worked on holidays
     //
		 
     select e.id,concat_ws(' ',e.first_name,e.last_name), t.hours,t.date             from time_blocks t,time_documents d, employees e,tmwrp_runs r,jobs j            where d.id=t.document_id  and d.employee_id=e.id and r.document_id=d.id         and d.job_id=j.id                                                               and t.inactive is null and t.hour_code_id=1                                     and (r.week1_grs_reg_hrs > 32 or r.week2_grs_reg_hrs >32)                       and t.date in ('2019-11-05','2019-11-11')                                       and j.salary_group_id in (1,2,5,11,12)                                          order by e.first_name,e.last_name,t.date
		
    */
			
		
}
