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

public class PrimeReport{

    static Logger logger = LogManager.getLogger(PrimeReport.class);
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
    double totalHours = 0, totalAmount=0;
    List<List<String>> allEntries = null;
    String errors = "";
    public PrimeReport(){
				
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
    /**
    public List<WarpEntry> getDailyEntries(){
	return dailyEntries;
    }
    */
    public String getStart_date(){
	return start_date;
    }
    public String getEnd_date(){
	return end_date;
    }
    public String getType(){
	return type;
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

    public List<List<String>> getAllEntries(){
	return allEntries;
    }
    public String getTotalHours(){
	return df.format(totalHours);
    }
    public String gettotalAmount(){
	return df.format(totalAmount);
    }
    public boolean hasEntries(){
	return allEntries != null && allEntries.size() > 0;
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

    public String find(){
	String msg = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	List<String> headers = new ArrayList<>();
	headers.add("Employee Number");
	headers.add("Employee");
	headers.add("Earn Code");
	headers.add("Hours");
	headers.add("factor");
	headers.add("week #");
	headers.add("date ragne");
	headers.add("Pay Rate");
	headers.add("Total Pay");
	headers.add("Rate Date");
	//
	// We are looking for Reg earn codes and its derivatives for
	// planning department
	//
	msg = setStartAndEndDates();
	if(!msg.isEmpty()){
	    return msg;
	}
	String qq = "select e.employee_number,concat_ws(' ',e.first_name,e.last_name) AS name, "+
	    "c.name,t.hours,t.prime_factor,t.week_no, "+
	    "if(week_no = 1,concat_ws('-',date_format(p.start_date,'%m/%d/%Y'),date_format(date_add(p.start_date,INTERVAL 6 DAY),'%m/%d/%Y')),concat_ws('-',date_format(date_add(p.start_date,INTERVAL 7 DAY),'%m/%d/%Y'),date_format(p.end_date,'%m/%d/%Y'))) date_range,w.pay_rate,w.pay_rate*t.hours*t.prime_factor as total_prime,"+
	    "date_format(w.rate_date,'%m/%d/%Y') "+
	    "from tmwrp_primes t "+ 
	    "join tmwrp_runs r on r.id=t.run_id "+ 
	    "join time_documents d on d.id=r.document_id "+
	    "join pay_periods p on p.id=d.pay_period_id "+
	    "join employees e on e.id=d.employee_id "+
	    "join hour_codes c on c.id=t.hour_code_id "+
	    "join employee_pay_rates w on w.employee_id=e.id ";
	String qw = "where w.rate_date = (select max(w2.rate_date) from employee_pay_rates w2 where "+
	    "w2.rate_date < if(t.week_no = 1,date_add(p.start_date, INTERVAL 6 DAY),p.end_date) and w2.employee_id=e.id)";
	if(!start_date.isEmpty()){
	    qw += " and p.start_date >= ? ";
	}
	if(!end_date.isEmpty()){
	    qw += " and p.start_date <= ? ";
	}
	con = Helper.getConnection();
	if(con == null){
	    msg = " Could not connect to DB ";
	    logger.error(msg);
	    return msg;
	}
	qq = qq+qw;
	logger.debug(qq);
	try{
	    pstmt = con.prepareStatement(qq);
	    int j=1;
	    if(!start_date.isEmpty()){
		java.util.Date date_tmp = dateFormat.parse(start_date);
		pstmt.setDate(j++, new java.sql.Date(date_tmp.getTime()));
	    }
	    if(!end_date.isEmpty()){
		java.util.Date date_tmp = dateFormat.parse(end_date);
		pstmt.setDate(j++, new java.sql.Date(date_tmp.getTime()));
	    }
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		if(allEntries == null){
		    allEntries = new ArrayList<>();
		    allEntries.add(headers);
		}
		List<String> ll = new ArrayList<>();
		for(int jj=1;jj<11;jj++){
		    String str = rs.getString(jj);
		    ll.add(str);
		}
		allEntries.add(ll);
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
		
}
/*

// start here				
				
select e.id, e.employee_number,concat_ws(' ',e.first_name,e.last_name) AS name,
t.hour_code_id,c.name,t.hours,t.prime_factor,t.week_no,
concat_ws('-',date_format(p.start_date,'%m/%d/%y'),date_format(p.end_date,'%m/%d/%y')) date_range,w.pay_rate,w.pay_rate*t.hours*t.prime_factor as total_prime,
w.rate_date,
if(t.week_no = 1, date_add(p.start_date, INTERVAL 6 DAY), p.end_date) AS week_end_date
  from tmwrp_primes t
join tmwrp_runs r on r.id=t.run_id
join time_documents d on d.id=r.document_id
join pay_periods p on p.id=d.pay_period_id
join employees e on e.id=d.employee_id
join hour_codes c on c.id=t.hour_code_id
join employee_pay_rates w on w.employee_id=e.id
where w.rate_date = (select max(w2.rate_date) from employee_pay_rates w2 where
w2.rate_date < if(t.week_no = 1,date_add(p.start_date, INTERVAL 6 DAY),p.end_date) and w2.employee_id=e.id)


date_add(date, INTERVAL 6 DAY)


	select e.employee_number,concat_ws(' ',e.first_name,e.last_name) AS name, 
 c.name,t.hours,t.prime_factor,t.week_no,
	    if(week_no = 1,concat_ws('-',date_format(p.start_date,'%m/%d/%Y'),date_format(date_add(p.start_date,INTERVAL 6 DAY),'%m/%d/%Y')),concat_ws('-',date_format(date_add(p.start_date,INTERVAL 7 DAY),'%m/%d/%Y'),date_format(p.end_date,'%m/%d/%Y'))) date_range,w.pay_rate,w.pay_rate*t.hours*t.prime_factor as total_prime,
	    date_format(w.rate_date,'%m/%d/%Y'),
	    date_format(if(t.week_no = 1, date_add(p.start_date, INTERVAL 6 DAY), p.end_date),'%m/%d/%Y') AS week_end_date 
	    from tmwrp_primes t 
	    join tmwrp_runs r on r.id=t.run_id 
	    join time_documents d on d.id=r.document_id 
	    join pay_periods p on p.id=d.pay_period_id 
	    join employees e on e.id=d.employee_id 
	    join hour_codes c on c.id=t.hour_code_id 
	    join employee_pay_rates w on w.employee_id=e.id 
	    where w.rate_date = (select max(w2.rate_date) from employee_pay_rates w2 where 
	    w2.rate_date < if(t.week_no = 1,date_add(p.start_date, INTERVAL 6 DAY),p.end_date) and w2.employee_id=e.id);



				
*/

