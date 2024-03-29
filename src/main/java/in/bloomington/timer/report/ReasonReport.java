package in.bloomington.timer.report;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.ArrayList;
import java.util.*;
import java.sql.*;
import java.text.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;
import in.bloomington.timer.list.*;
import in.bloomington.timer.timewarp.WarpEntry;

public class ReasonReport{

    boolean debug = false;
    static final long serialVersionUID = 3820L;
    static final int startYear = CommonInc.reportStartYear; 
    static Logger logger = LogManager.getLogger(ReasonReport.class);
    //
    final static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");		
    final static DecimalFormat df4 = new DecimalFormat("#0.0000");
    final static DecimalFormat df = new DecimalFormat("#0.00");
    String date_from="", date_to="";
    int year = 0, quarter = 0;
    String start_date ="", end_date=""; // temp holders
    List<WarpEntry> entries = null;

    String dept="", department_id="", dept_ref_id="36", outputType="html"; 
    String code="";
    String code2="";
    Hashtable<String, Profile> profiles = null;
    Map<String, List<WarpEntry>> mapEntries = null;
    Hashtable<String, Double> hoursSums = null;
    Hashtable<String, Double> amountsSums = null;
    List<WarpEntry> dailyEntries = null; 
    double totalHours =0, totalAmount=0;
    double hourly_rate = 35.;
    String errors = "";
    public ReasonReport(){
				
    }	
    public int getYear(){
	if(year == 0)
	    return -1;
	return year;
    }
    public int getQuarter(){
	if(quarter == 0)
	    return -1;
	return quarter;
    }
    public String getDate_from(){
	return date_from;
    }
    public String getDate_to(){
	return date_to;
    }
    public List<WarpEntry> getEntries(){
	return entries;
    }
    public List<WarpEntry> getDailyEntries(){
	return dailyEntries;
    }
    public String getStart_date(){
	return start_date;
    }
    public String getEnd_date(){
	return end_date;
    }
    public String getOutputType(){
	return outputType;
    }
    public Map<String, List<WarpEntry>> getMapEntries(){
	return mapEntries;
    }
    public Hashtable<String, String> getHoursSums(){
	if(hoursSums != null){
	    Hashtable<String, String> hash = new Hashtable<>();
	    Set<String> set =hoursSums.keySet();
	    for (String str:set){
		double val = hoursSums.get(str);
		hash.put(str, df.format(val));
	    }
	    return hash;
	}
	return null;
    }
    public Hashtable<String, String> getAmountsSums(){
	if(amountsSums != null){
	    Hashtable<String, String> hash = new Hashtable<>();
	    Set<String> set =amountsSums.keySet();
	    for (String str:set){
		double val = amountsSums.get(str);
		hash.put(str, df.format(val));
	    }
	    return hash;
	}				
	return null;
    }
    public String getTotalHours(){
	return df.format(totalHours);
    }
    public String gettotalAmount(){
	return df.format(totalAmount);
    }
    public boolean hasEntries(){
	return entries != null && entries.size() > 0;
    }
    public boolean hasDailyEntries(){
	return dailyEntries != null && dailyEntries.size() > 0;
    }
    public boolean hasAnyEntries(){
	return hasEntries() || hasDailyEntries();
    }
    //
    // setters
    //
    public void setYear (int val){
	if(val > 0)
	    year = val;
    }
    public void setQuarter (int val){
	if(val > 0)
	    quarter = val;
    }		
    public void setDate_from (String val){
	if(val != null){
	    date_from = val;
	}
    }
    public void setDate_to (String val){
	if(val != null){
	    date_to = val;
	}
    }
    public void setOutputType(String val){
	if(val != null){
	    outputType = val;
	}
    }
    public List<BenefitGroup> getBenefitGroups(){
	List<BenefitGroup> benefitGroups = null;
	BenefitGroupList tl = new BenefitGroupList();
	String back = tl.find();
	if(back.isEmpty()){
	    List<BenefitGroup> ones = tl.getBenefitGroups();
	    if(ones != null && ones.size() > 0){
		benefitGroups = ones;
	    }
	}
	return benefitGroups;
    }						
    String setProfiles(){
	String msg="";
	if(profiles == null){
	    List<BenefitGroup> benefitGroups = getBenefitGroups();
	    ProfileList pl = new ProfileList(end_date,
					     dept_ref_id,
					     benefitGroups);

	    msg = pl.find();
	    List<Profile> ones = pl.getProfiles();
	    if(msg.isEmpty() && ones.size() > 0){
		profiles = new Hashtable<String, Profile>(ones.size());
		for(Profile one:ones){
		    String str = one.getEmployee_number();
		    if(str != null && !str.isEmpty())
			profiles.put(str, one);
		}
	    }
	}
	return msg;
    }			 
    String setStartAndEndDates(){
	String msg = "";
	//
	// We use start_date and end_date so that we do not override date_from
	// and date_to if they are not set
	//
	start_date = date_from;
	end_date = date_to;
	if(year > 0 && quarter > 0){
	    start_date = CommonInc.quarter_starts[quarter]+year;
	    end_date = CommonInc.quarter_ends[quarter]+year;
	}
	start_date = start_date.trim();
	if(start_date.isEmpty()){
	    msg = "Year and quarter or start date not set ";
	    return msg;
	}
	if(end_date.isEmpty()){
	    end_date = Helper.getToday();
	}
	return msg;
    }
    /**
     *
     create table service_references(name varchar(20))Engine=InnoDB;
    */
			 
    public boolean checkRef(String ref){
	String msg = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	//
	String qq = "select count(*) from service_references where name=? ";
	int cnt = 0;
	logger.debug(qq);
	con = Helper.getConnection();
	if(con == null){
	    msg = " Could not connect to DB ";
	    logger.error(msg);
	}
	else{
	    try{
		pstmt = con.prepareStatement(qq);				
		pstmt.setString(1, ref);
		rs = pstmt.executeQuery();
		if(rs.next()){
		    cnt = rs.getInt(1);
		}
	    }catch(Exception ex){
		logger.error(ex);
	    }
	    finally{
		Helper.databaseDisconnect(pstmt, rs);
		UnoConnect.databaseDisconnect(con);
	    }				
	}
	System.err.println(" cnt "+cnt);
	return cnt > 0;
    }
    /**
     * include non reason codes as well
     *
     select tt.name,tt.empnum,tt.code,tt.reason,tt.date,sum(hours),sum(amount)          from (select distinct t.id,concat_ws(' ',e.first_name,e.last_name) AS name,     e.employee_number as empnum,date_format(t.date,'%m/%d/%Y') AS date,             c.name AS code,r.description AS reason,t.hours AS hours,t.amount AS amount      from time_blocks t                                                              join hour_codes c on c.id = t.hour_code_id                                      left join earn_code_reasons r on r.id=t.earn_code_reason_id                     join time_documents d on d.id=t.document_id                                     join jobs j on j.id = d.job_id                                                  join `groups` g on g.id = j.group_id                                              join employees e on e.id = j.employee_id                                       where t.inactive is null and (t.hours > 0 or t.amount > 0)                      and g.department_id = 20 and d.employee_id=e.id                                 and j.effective_date <= t.date and (j.expire_date is null or t.date <= j.expire_date)                                                                           and t.date >= '2021-08-12' and t.date <= '2021-08-15') tt                       group by tt.name,tt.empnum,tt.code,tt.reason,tt.date;

     // union
     //
     select concat_ws(' ',e.first_name,e.last_name) AS name,                         e.employee_number as empnum,                                                   date_format(r.run_time,'%m/%d/%Y') AS date,                                     c.name AS code,                                                                 null AS reason,                                                                 t.hours AS hours,t.amount AS amount                                             from tmwrp_blocks t                                                             join hour_codes c on c.id = t.hour_code_id                                      join tmwrp_runs r on t.run_id=r.id                                              join time_documents d on d.id=r.document_id                                     join jobs j on j.id = d.job_id                                                  join `groups` g on g.id = j.group_id                                              join employees e on e.id = j.employee_id                                        where t.hours > 0                                                               and d.employee_id=e.id                                                          and g.department_id = 20                                                        and j.effective_date <= r.run_time                                              and (j.expire_date is null or r.run_time <= j.expire_date)                      and c.id in (34,45,71,46,50,79,43,44,78,109)                                    and r.run_time >= '2021-01-01' and r.run_time <= '2021-02-31';

		 

		 
    */
    public String findHoursCodeDetails(){
	String msg = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	//
	msg = setStartAndEndDates();
	if(!msg.isEmpty()){
	    return msg;
	}
	setProfiles(); // to get the rates
	//
	// using subquery
	//
	//
	String qq = "select tt.name,tt.empnum,"+
	    " tt.code,tt.reason,tt.date,"+
	    " sum(tt.hours),sum(tt.amount) "+
	    " from ((select distinct t.id, "+
	    " concat_ws(' ',e.first_name,e.last_name) AS name,"+
	    " e.employee_number as empnum,"+
	    " date_format(t.date,'%Y-%m-%d') AS date,"+
	    " c.name AS code, "+
	    " r.description AS reason, "+
	    " t.hours AS hours,t.amount AS amount "+
	    " from time_blocks t "+
	    " join hour_codes c on c.id = t.hour_code_id "+
	    " left join earn_code_reasons r on r.id=t.earn_code_reason_id "+
	    " join time_documents d on d.id=t.document_id "+
	    " join jobs j on j.id = d.job_id "+
	    " join `groups` g on g.id = j.group_id "+
	    " join employees e on e.id = j.employee_id "+
	    " where t.inactive is null and (t.hours > 0 or t.amount > 0) "+
	    // " and d.employee_id=e.id  "+
            " and g.department_id = 20 "+ // Police
	    " and j.effective_date <= t.date and (j.expire_date is null or t.date <= j.expire_date) "+  
	    " and t.date >= ? and t.date <= ? )"+
	    //" group by concat_ws(' ',e.first_name,e.last_name),e.employee_number,c.name,r.description,date_format(t.date,'%Y-%m-%d')) "+
	    "union "+
	    "(select distinct t.id,concat_ws(' ',e.first_name,e.last_name) AS name,           e.employee_number as empnum,                                                   date_format(r.run_time,'%Y-%m-%d') AS date,                                     c.name AS code,                                                                 null AS reason,                                                                 t.hours AS hours,t.amount AS amount                                             from tmwrp_blocks t                                                             join hour_codes c on c.id = t.hour_code_id                                      join tmwrp_runs r on t.run_id=r.id                                              join time_documents d on d.id=r.document_id                                     join jobs j on j.id = d.job_id                                                  join `groups` g on g.id = j.group_id                                              join employees e on e.id = j.employee_id                                       where t.hours > 0                                                               and d.employee_id=e.id                                                          and g.department_id = 20                                                        and j.effective_date <= r.run_time                                              and (j.expire_date is null or r.run_time <= j.expire_date)                      and c.id in (34,43,44,45,46,50,71,78,79,109)                                    and r.run_time >= ? and r.run_time <= ?)";
	qq += " ) tt ";
	qq += " group by tt.name,tt.empnum,tt.code,tt.reason,tt.date ";				

				
	con = Helper.getConnection();
	if(con == null){
	    msg = " Could not connect to DB ";
	    logger.error(msg);
	    return msg;
	}
	logger.debug(qq);
	try{
	    pstmt = con.prepareStatement(qq);
	    int jj=1;
	    java.util.Date dt_start = dateFormat.parse(start_date);
	    java.util.Date dt_end = dateFormat.parse(end_date);						
	    pstmt.setDate(jj++, new java.sql.Date(dt_start.getTime()));
	    pstmt.setDate(jj++, new java.sql.Date(dt_end.getTime()));
	    pstmt.setDate(jj++, new java.sql.Date(dt_start.getTime()));
	    pstmt.setDate(jj++, new java.sql.Date(dt_end.getTime()));
	    rs = pstmt.executeQuery();
	    jj=0;
	    double hourly_rate = 0, old_hourly_rate=0;
	    String old_emp_num="";
	    while(rs.next()){
		jj++;
		if(dailyEntries == null)
		    dailyEntries = new ArrayList<>();
		String str = rs.getString(2); // emp_num
		
		if(str != null && !str.equals(old_emp_num)){
		    old_emp_num = str;
		    if(profiles != null && profiles.containsKey(str)){
			Profile pp = profiles.get(str);
			hourly_rate = pp.getHourlyRate();

		    }
		    else{
			hourly_rate = 0;
		    }
		    old_hourly_rate = hourly_rate;										
		}
		else{
		    hourly_rate = old_hourly_rate;
		}
		WarpEntry one =
		    new WarpEntry(debug,
				  rs.getString(1), // name
				  rs.getString(2), // emp num
				  rs.getString(3), // code
				  rs.getString(4), // reason
				  rs.getString(5), // date
				  rs.getDouble(6), // hours
				  hourly_rate,
				  rs.getDouble(7)); // amountPay
		if(!dailyEntries.contains(one))
		    dailyEntries.add(one);
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
    public String findHoursByDateAndCode(){
	String msg = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	//
	// We are looking for Reg earn codes and its derivatives for
	// planning department
	//
	msg = setStartAndEndDates();
	if(!msg.isEmpty()){
	    return msg;
	}				
	//
	// using subquery
	//
	String qq = "select tt.name,tt.empnum,"+
	    " tt.code,tt.reason,tt.date,"+
	    " sum(hours) "+
	    " from (select "+
	    " concat_ws(' ',e.first_name,e.last_name) AS name,"+
	    " e.employee_number as empnum,"+
	    " date_format(t.date,'%m/%d/%Y') AS date,"+
	    " c.name AS code, "+
	    " r.description AS reason, "+
	    " t.hours AS hours "+
	    " from time_blocks t "+
	    " join hour_codes c on t.hour_code_id=c.id "+						
	    " join time_documents d on d.id=t.document_id "+
	    " join pay_periods p on p.id=d.pay_period_id "+
	    " join department_employees de on de.employee_id=d.employee_id "+
	    " join employees e on d.employee_id=e.id "+
	    " join earn_code_reasons r on r.id=t.earn_code_reason_id "+
	    " where t.inactive is null "+
            " and t.earn_code_reason_id is not null "+
	    " and de.department_id = 20 "+ // police
	    " and t.date >= ? and t.date <= ? ";
	qq += " ) tt ";
	qq += " group by tt.name,tt.empnum,tt.code,tt.reason,tt.date ";
	con = Helper.getConnection();
	if(con == null){
	    msg = " Could not connect to DB ";
	    logger.error(msg);
	    return msg;
	}
	logger.debug(qq);
	try{
	    pstmt = con.prepareStatement(qq);
	    int jj=1;
	    pstmt = con.prepareStatement(qq);
	    java.util.Date date_tmp = dateFormat.parse(start_date);
	    pstmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
	    date_tmp = dateFormat.parse(end_date);
	    pstmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
	    rs = pstmt.executeQuery();
	    jj=0;
	    while(rs.next()){
		jj++;
		if(dailyEntries == null)
		    dailyEntries = new ArrayList<>();
		WarpEntry one =
		    new WarpEntry(debug,
				  rs.getString(1), // name
				  rs.getString(2), // emp num
				  // rs.getString(3)+" - "+rs.getString(4), // code
				  rs.getString(3),
				  rs.getString(4), // code
				  rs.getString(5), // date
				  rs.getDouble(6), // hours 
				  hourly_rate);
		dailyEntries.add(one);
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
    public String findHoursByNameAndCode(){
	String msg = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	//
	// We are looking for Reg earn codes and its derivatives for
	// planning department
	//
	msg = setStartAndEndDates();
	if(!msg.isEmpty()){
	    return msg;
	}
	//
	// using subquery
	//
	String qq = "select tt.name,tt.empnum,tt.code,tt.reason,"+
	    " sum(hours) "+
	    "from ( select "+
	    " concat_ws(' ',e.first_name,e.last_name) AS name,"+
	    " e.employee_number AS empnum,"+
	    " c.name AS code, "+
	    " r.description AS reason, "+
	    " t.hours AS hours "+
	    " from time_blocks t "+
	    " join hour_codes c on t.hour_code_id=c.id "+						
	    " join time_documents d on d.id=t.document_id "+
	    " join pay_periods p on p.id=d.pay_period_id "+
	    " join department_employees de on de.employee_id=d.employee_id "+
	    " join employees e on d.employee_id=e.id "+
	    " join earn_code_reasons r on r.id=t.earn_code_reason_id "+
	    " where t.inactive is null "+
	    " and t.earn_code_reason_id is not null "+
	    " and de.department_id = 20 "+
	    " and t.date >= ? and t.date <= ? ";
	qq += " ) tt ";
	qq += " group by tt.code,tt.reason,tt.name,tt.empnum ";
	con = Helper.getConnection();
	if(con == null){
	    msg = " Could not connect to DB ";
	    logger.error(msg);
	    return msg;
	}
	logger.debug(qq);
	try{
	    pstmt = con.prepareStatement(qq);
	    int jj=1;
	    entries = new ArrayList<>();
	    pstmt = con.prepareStatement(qq);
	    java.util.Date date_tmp = dateFormat.parse(start_date);
	    pstmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
	    date_tmp = dateFormat.parse(end_date);
	    pstmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
	    rs = pstmt.executeQuery();
	    jj=0;
	    while(rs.next()){
		jj++;
		WarpEntry one =
		    new WarpEntry(debug,
				  rs.getString(1), // name
				  rs.getString(2), // emp num
				  rs.getString(3)+" - "+rs.getString(4), // code
				  rs.getDouble(5), // hours
				  hourly_rate);
		addToHash(one);
		entries.add(one);
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
    void addToHash(WarpEntry val){
	if(val != null){
	    if(mapEntries == null){
		mapEntries = new TreeMap<>();
	    }
	    String code = val.getCode();
	    double hr = val.getHours();
	    double amount = val.getAmount();
	    totalHours += hr;
	    totalAmount += amount;
	    if(hr > 0){
		if(mapEntries.containsKey(code)){
		    List<WarpEntry> list = mapEntries.get(code);
		    list.add(val);
		}
		else{
		    List<WarpEntry> ll = new ArrayList<>();
		    ll.add(val);
		    mapEntries.put(code, ll);
		}
		if(hoursSums == null){
		    hoursSums = new Hashtable<>();
		}
		if(amountsSums == null){
		    amountsSums = new Hashtable<>();
		}
		if(hoursSums.containsKey(code)){
		    double hr2 = hoursSums.get(code);
		    hr += hr2;
		}
		hoursSums.put(code, hr);
		//
		if(amountsSums.containsKey(code)){
		    double amt2 = amountsSums.get(code);
		    amount += amt2;
		}
		amountsSums.put(code, amount);
	    }
	}
    }
		
}
