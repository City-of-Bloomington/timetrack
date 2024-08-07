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

public class MpoReport{

    static Logger logger = LogManager.getLogger(MpoReport.class);
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");		
    static DecimalFormat df4 = new DecimalFormat("#0.0000");
    static DecimalFormat df = new DecimalFormat("#0.00");
    String date_from="", date_to="";
    int year = 0, quarter = 0;
    String start_date ="", end_date=""; // temp holders
    List<TimeBlock> timeBlocks = null;
    List<WarpEntry> entries = null;
    boolean debug = false, ignoreProfiles = false;
    String dept="", department_id="", type="html"; 
    String dept_ref_id="";
    String salary_group_id="";
    String code="";
    String code2="";
    Department department = null;
    Hashtable<String, Profile> profiles = null;
    List<BenefitGroup> benefitGroups = null;
    Map<String, List<WarpEntry>> mapEntries = null;
    Hashtable<String, Double> hoursSums = null;
    Hashtable<String, Double> amountsSums = null;
    List<WarpEntry> dailyEntries = null; 
    double totalHours =0, totalAmount=0;
    String errors = "";
    public MpoReport(){
				
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
    public List<TimeBlock> getTimeBlocks(){
	return timeBlocks;
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
    public String getType(){
	return type;
    }
    public String getSalary_group_id(){
	if(salary_group_id.isEmpty())
	    return "-1";
	return salary_group_id;
    }
    public void setSalary_group_id(String val){
	if(val != null && !val.equals("-1")){
	    salary_group_id = val;
	}
    }
    public String getDepartment_id(){
	if(department_id.isEmpty())
	    return "-1";
	return department_id;
    }
    public Department getDepartment(){
	if(department == null && !department_id.isEmpty()){
	    Department one = new Department(department_id);
	    String back = one.doSelect();
	    if(back.isEmpty()){
		department = one;
		dept_ref_id = one.getRef_id();
	    }
	}
	return department;
    }
    public boolean hasDepartment(){
	getDepartment();
	return department != null;
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
    public void setDepartment_id(String val){
	if(val != null && !val.equals("-1")){
	    department_id = val;
	}
    }    
    public void setType(String val){
	if(val != null){
	    type = val;
	}
    }
    public void setIgnoreProfiles(){
	ignoreProfiles = true; // we do not need profiles for FMLA report
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
    public List<BenefitGroup> getBenefitGroups(){
	if(benefitGroups == null){
	    BenefitGroupList tl = new BenefitGroupList();
	    String back = tl.find();
	    if(back.isEmpty()){
		List<BenefitGroup> ones = tl.getBenefitGroups();
		if(ones != null && ones.size() > 0){
		    benefitGroups = ones;
		}
	    }
	}
	return benefitGroups;
    }				
    String setProfiles(){
	String msg="";
	if(profiles == null){
	    getBenefitGroups();
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
    public String find(){
	//
	// We are looking for Reg earn codes and its derivatives for
	// planning department
	//
	String msg = errors;
	msg += setStartAndEndDates();
	if(!msg.isEmpty()){
	    return msg;
	}
	TimeBlockList tbl = new TimeBlockList();
	tbl.setDate_from(start_date);
	tbl.setDate_to(end_date);
	tbl.setCode(code);
	tbl.setCode2(code2);
	if(!department_id.isEmpty()){
	    tbl.setDepartment_id(department_id);
	}
	if(!salary_group_id.isEmpty()){
	    tbl.setSalary_group_id(salary_group_id);
	}
	msg = tbl.find();
	if(!msg.isEmpty()){
	    return msg;
	}				
	List<TimeBlock> ones = tbl.getTimeBlocks();
	if(ones != null && ones.size() > 0){
	    timeBlocks = ones;
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
	if(!ignoreProfiles){
	    msg = setProfiles();
	    if(!msg.isEmpty()){
		return msg;
	    }
	}
	//
	// using subquery
	//
	String qq = "select tt.name,tt.empnum,tt.date,tt.code,sum(hours) "+
	    "from (select "+
	    " concat_ws(' ',e.first_name,e.last_name) AS name,"+
	    " e.employee_number as empnum,"+
	    " t.date AS date,"+
	    " concat_ws(': ',c.name, c.description) AS code, "+
	    " t.hours AS hours "+
	    " from time_blocks t "+
	    " join hour_codes c on t.hour_code_id=c.id "+						
	    " join time_documents d on d.id=t.document_id "+
	    " join pay_periods p on p.id=d.pay_period_id "+
	    " join employees e on d.employee_id=e.id ";
	String qw ="where t.inactive is null and t.date >= ? and t.date <= ? ";
	if(!salary_group_id.isEmpty()){
	    qq += " join jobs j on d.job_id=j.id ";
	    qw += " and j.salary_group_id = ? ";
	}
	else{
	    if(!code2.isEmpty()){
		qw += " and (c.name like ? or c.name like ?) ";
	    }
	    else {
		qw += " and c.name like ? ";
	    }
	}
	if(!department_id.isEmpty()){
	    qq += " join department_employees de on de.employee_id=d.employee_id ";

	    qw += " and de.department_id = ? ";
	}
	qq += qw +") tt ";
	qq += " group by tt.name,tt.empnum,tt.code,tt.date ";
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
	    if(!salary_group_id.isEmpty()){
		pstmt.setString(jj++, salary_group_id);
	    }
	    else{
		pstmt.setString(jj++, code);
		if(!code2.isEmpty()){
		    pstmt.setString(jj++, code2);
		}
	    }
	    if(!department_id.isEmpty()){
		pstmt.setString(jj++, department_id);
	    }
	    rs = pstmt.executeQuery();
	    jj=0;
	    while(rs.next()){
		jj++;
		double hourly_rate = 0;
		if(!ignoreProfiles){
		    if(profiles != null){
			String str = rs.getString(2);// emp_num;
			if(profiles.containsKey(str)){
			    Profile pp = profiles.get(str);
			    hourly_rate = pp.getHourlyRate();
			}
		    }
		}
		if(dailyEntries == null)
		    dailyEntries = new ArrayList<>();
		WarpEntry one =
		    new WarpEntry(debug,
				  rs.getString(1), // name
				  rs.getString(2), // emp num
				  rs.getString(4), // code
				  rs.getString(3), // date
				  rs.getDouble(5), // hours
				  hourly_rate); // hourly rate
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
    public String findHoursByNameCode(){
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
	if(!ignoreProfiles){
	    msg = setProfiles();
	    if(!msg.isEmpty()){
		return msg;
	    }
	}
	//
	// using subquery
	//
	String qq = "select tt.name,tt.empnum,tt.code,sum(hours) "+
	    "from ( select "+
	    " concat_ws(' ',e.first_name,e.last_name) AS name,"+
	    " e.employee_number AS empnum,"+
	    " concat_ws(': ',c.name,c.description) AS code, "+
	    " t.hours AS hours "+
	    " from time_blocks t "+
	    " join hour_codes c on t.hour_code_id=c.id "+						
	    " join time_documents d on d.id=t.document_id "+
	    " join pay_periods p on p.id=d.pay_period_id "+
	    " join employees e on d.employee_id=e.id ";
	String qw = " where t.inactive is null and "+
	    " t.date >= ? and t.date <= ? ";
	if(!salary_group_id.isEmpty()){
	    qq += " join jobs j on d.job_id=j.id ";
	    qw += " and j.salary_group_id = ? ";
	}
	else{
	    if(!code2.isEmpty()){
		qw += " and (c.name like ? or c.name like ?) ";
	    }
	    else {
		qw += " and c.name like ? ";
	    }
	}
	if(!department_id.isEmpty()){
	    qq += " join department_employees de on de.employee_id=d.employee_id ";
						
	    qw += " and de.department_id = ? ";
	}
	qq += qw;
	qq += " ) tt ";
	qq += " group by tt.code,tt.name,tt.empnum ";
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
	    if(!salary_group_id.isEmpty()){
		pstmt.setString(jj++, salary_group_id);
	    }
	    else{
		pstmt.setString(jj++, code);
		if(!code2.isEmpty())
		    pstmt.setString(jj++, code2);
	    }
	    if(!department_id.isEmpty()){
		pstmt.setString(jj++, department_id);
	    }
	    rs = pstmt.executeQuery();
	    jj=0;
	    while(rs.next()){
		jj++;
		double hourly_rate = 0;
		if(!ignoreProfiles){
		    if(profiles != null){
			String str = rs.getString(2);// emp_num;
			if(profiles.containsKey(str)){
			    Profile pp = profiles.get(str);
			    hourly_rate = pp.getHourlyRate();
			}
		    }
		}
		WarpEntry one =
		    new WarpEntry(debug,
				  rs.getString(1), // name
				  rs.getString(2), // emp num
				  rs.getString(3), // code
				  rs.getDouble(4), // hours
				  hourly_rate); // hourly rate
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
/*
	HAND report all codes
	
				select tt.name,tt.empnum,tt.date,tt.code,sum(hours)                                  from (select                                                                    concat_ws(' ',e.first_name,e.last_name) AS name,                                e.employee_number as empnum,                                                    t.date AS date,                                                                 c.name AS code, 								                                                 t.hours AS hours                                                                from time_blocks t                                                              join hour_codes c on t.hour_code_id=c.id 						                           join time_documents d on d.id=t.document_id                                     join pay_periods p on p.id=d.pay_period_id                                      join department_employees de on de.employee_id=d.employee_id                    join employees e on d.employee_id=e.id                                          where t.inactive is null                                                        and de.department_id = 3                                                        and p.start_date >= str_to_date('01/01/2019','%m/%d/%Y')                        and p.end_date <= str_to_date('12/31/2019','%m/%d/%Y')                          ) tt                                                                           group by tt.code,tt.name,tt.empnum,tt.date                                      INTO OUTFILE '/var/lib/mysql-files/hand_report.csv'                             FIELDS TERMINATED BY ','                                                        ENCLOSED BY '"'                                                                 LINES TERMINATED BY '\n';
				
*/

