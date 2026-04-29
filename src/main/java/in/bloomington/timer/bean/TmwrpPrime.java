package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.Hashtable;
import java.util.TreeMap;
import java.util.Map;
import java.sql.*;
import javax.sql.*;
import java.text.SimpleDateFormat;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TmwrpPrime{

    static Logger logger = LogManager.getLogger(TmwrpPrime.class);
    static SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
    static final long serialVersionUID = 1500L;
    Map<Integer, Map<Integer, Double>> usedMap = null;
    static Hashtable<String, Double> primeFactors = new Hashtable<>();
    static Hashtable<String, String> hourCodes = new Hashtable<>();    
    static {
	primeFactors.put("cp_earn_10",0.5);
	primeFactors.put("cp_earn_15",0.33);
	primeFactors.put("cp_earn_20",0.25);
	primeFactors.put("ot_earn_10",0.5);
	primeFactors.put("ot_earn_15",0.33);
	primeFactors.put("ot_earn_20",0.25);
	// hour code id's
	hourCodes.put("cp_earn_10","71");// Comp time 1.0
	hourCodes.put("cp_earn_15","34");// Comp time 1.5
	hourCodes.put("cp_earn_20","45");// Comp time 2.0
	hourCodes.put("ot_earn_10","78");// Overtime 1.0
	hourCodes.put("ot_earn_15","43");// Overtime 1.5
	hourCodes.put("ot_earn_20","44");// Overtime 2.0
    }
    
    String run_id="", hour_code_id="",
	week_no="", // 1, 2	
	prime_code="";

    double hours=0, prime_factor=0; 
    //

		
    public TmwrpPrime(){
    }
    public TmwrpPrime(String val){
	setRun_id(val);
    }    
    // for new record
    public TmwrpPrime(
		      String val,
		      String val2,
		      String val3,
		      Double val4
		      ){
	setRun_id(val);
	setWeekNo(val2);
	setPrimeCode(val3);
	setHours(val4);
    }	    
    public TmwrpPrime(
		      String val,
		      String val2,
		      String val3,
		      Double val4,
		      Double val5
		      ){
	setRun_id(val);
	setWeekNo(val2);
	setHourCode_id(val3);
	setHours(val4);
	setPrimeFactor(val5);
    }		
		
    //
    // getters
    //
    public String getRun_id(){
	return run_id;
    }		
    public String getHourCode_id(){
	return hour_code_id;
    }
    public double getHours(){
	return hours;
    }
    public String getWeekNo(){
	return week_no;
    }
    public double getPrimeFactor(){
	return prime_factor;
    }
    //
    // setters
    //
		
    public void setRun_id(String val){
	if(val != null)
	    run_id = val;
    }
    public void setWeekNo(String val){
	if(val != null)
	    week_no = val;
    }				
    public void setHourCode_id(String val){
	if(val != null){
	    hour_code_id = val;
	}
    }		
    public void setPrimeCode(String val){
	if(val != null){
	    prime_code = val;
	}
    }
    public void setHours(Double val){
	if(val != null)
	    hours = val;
    }
    public void setPrimeFactor(Double val){
	if(val != null)
	    prime_factor = val;
    }


    public boolean equals(Object o) {
	if (o instanceof TmwrpPrime) {
	    TmwrpPrime c = (TmwrpPrime) o;
	    if ( this.run_id.equals(c.getRun_id()) && this.week_no.equals(c.getWeekNo()) && this.hour_code_id.equals(c.getHourCode_id()))
		return true;
	}
	return false;
    }
    public int hashCode(){
	int seed = 37;
	try{
	    seed += run_id.hashCode()+week_no.hashCode()+hour_code_id.hashCode();
	}catch(Exception ex){
	    // we ignore
	}
	return seed;
    }
    public String toString(){
	return run_id+" "+week_no+" "+hour_code_id;
    }
		
    // ToDo start here
		
    public String doSave(){
	//
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = "insert into tmwrp_prime values(?,?,?,?,?) ";
	if(run_id.isEmpty()){
	    msg = " timewarp run not set ";
	    return msg;
	}
	if(week_no.isEmpty()){
	    msg = " week number not set ";
	    return msg;
	}
	if(prime_code.isEmpty()){
	    msg = " prime_code not set ";
	    return msg;
	}
	double factor = 0;
	String code_id = "";
	if(primeFactors.containsKey(prime_code)){
	    factor = primeFactors.get(prime_code);
	}
	if(hourCodes.containsKey(prime_code)){
	    code_id = hourCodes.get(prime_code);
	}
	if(factor == 0 || code_id.isEmpty()){
	    msg ="factor "+factor+" Earn Code id "+code_id;
	    logger.error(msg);
	    return msg;
	}
	logger.debug(qq);
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = "Could not connect to DB ";
	    return msg;
	}				
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, run_id);						
	    pstmt.setString(2, week_no);
	    pstmt.setString(3, code_id);
	    pstmt.setDouble(4, hours);
	    pstmt.setDouble(5, factor);
	    pstmt.executeUpdate();
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
    /// need revist TODO
    public String doSaveBolk(Hashtable<String, Double> hash,
			     Double weeklyEarnTimeUsed,
			     String week_no){ 
	//
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = "replace into tmwrp_primes values(?,?,?,?,?) ";
	if(run_id.isEmpty()){
	    msg = " timewarp run id not set ";
	    return msg;
	}
	if(week_no.isEmpty()){
	    msg = " week nubmer not set ";
	    return msg;
	}	
	if(hash == null || hash.isEmpty()){
	    return msg;
	}
	double earned_time_used = weeklyEarnTimeUsed;
	if(earned_time_used > 0){
	    System.err.println("weekly earned time used deduced "+earned_time_used); 
	}
	logger.debug(qq);
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = "Could not connect to DB ";
	    return msg;
	}
	Set<String> keys = hash.keySet();
	try{
	    pstmt = con.prepareStatement(qq);
	    for(String key:keys){
		double dd = hash.get(key);
		double factor = 0;
		String code_id = "";
		if(dd > 0){
		    if(earned_time_used > 0){
			if(dd <= earned_time_used){
			    earned_time_used = earned_time_used - dd;
			    dd = 0;
			    continue;
			}
			else if(dd > earned_time_used){
			    dd = dd - earned_time_used;
			    earned_time_used = 0;
			}
		    }
		    if(primeFactors.containsKey(key)){
			factor = primeFactors.get(key);
		    }
		    if(hourCodes.containsKey(key)){
			code_id = hourCodes.get(key);
		    }
		    if(factor == 0 || code_id.isEmpty()){
			msg ="factor "+factor+" Earn Code id "+code_id;
			logger.error(msg);
			continue;
		    }
		    pstmt.setString(1, run_id);
		    pstmt.setString(2, week_no);
		    pstmt.setString(3, code_id);
		    pstmt.setDouble(4, dd); // hours
		    pstmt.setDouble(5, factor); // multiplier
		    pstmt.executeUpdate();
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
    
    // needed when new changes were added for overtime prime
    // to include data from previous periods before this feature was
    // added
    public String fillPreviousPeriods(String start_date, String end_date){
	Connection con = null;
	PreparedStatement pstmt = null, pstmt2=null;
	ResultSet rs = null;
	String msg="", str="";
	// find all the earned hours
	// for Non-exempt and union employees
	//
	String qq2 = "replace into tmwrp_primes values(?,?,?,?,?) ";
	String qq = "select b.run_id run_id,if(b.term_type = 'Week 1',1,2) week_no, ht.alt_hour_code_id hour_code,ht.prime_factor factor, sum(b.hours) hours "+
	    "from tmwrp_blocks b "+
	    "join tmwrp_runs r on r.id=b.run_id "+
	    "join time_documents d on d.id=r.document_id "+
	    "join jobs j on d.job_id=j.id "+
	    "join pay_periods p on p.id = d.pay_period_id "+
	    "join hour_code_translates ht on ht.hour_code_id=b.hour_code_id "+
	    "where b.hour_code_id in (34,43,44,45,46,50,71,78,79,112,113,114,115,127,128,172,173) "+
	    "and j.salary_group_id in (2,4) ";
	if(!start_date.isEmpty()){
	    qq += " and  p.start_date >= ? ";
	}
	if(!end_date.isEmpty()){
	    qq += " and  p.start_date <= ? ";
	}
	qq += " group by run_id,week_no,hour_code,factor ";
	msg = findUsedCodes(start_date, end_date);
	if(!msg.isEmpty()){
	    return msg;
	}
	logger.debug(qq);
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = "Could not connect to DB ";
	    return msg;
	}							
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt2 = con.prepareStatement(qq2);
	    int jj=1;
	    if(!start_date.isEmpty()){
		java.util.Date date_tmp = df.parse(start_date);
		pstmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
	    }
	    if(!end_date.isEmpty()){
		java.util.Date date_tmp = df.parse(end_date);
		pstmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
	    }	
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		int run_id = rs.getInt(1); 
		int week_no = rs.getInt(2);
		String code_id = rs.getString(3);
		double factor = rs.getDouble(4);
		double hours = rs.getDouble(5);
		if(usedMap.containsKey(run_id)){
		    Map<Integer,Double> map = usedMap.get(run_id);
		    if(map.containsKey(week_no)){
			double dd = map.get(week_no);
			if(hours > dd){
			    hours = hours - dd;
			    dd = 0;
			    map.remove(week_no);
			    // if there is no other week
			    // we will remove all together
			    if(map.isEmpty()){ //
				usedMap.remove(run_id);
			    }
			}
			else if(hours < dd){
			    dd = dd - hours;
			}
			map.put(week_no, dd);
		    }
		}
		if(hours > 0){
		    // save 
		    pstmt2.setInt(1,run_id);
		    pstmt2.setInt(2,week_no);
		    pstmt2.setString(3,code_id);
		    pstmt2.setDouble(4,hours);
		    pstmt2.setDouble(5,factor);
		    pstmt2.executeUpdate();
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
    // needed to fill the previous pay periods
    // before the new feature is added
    public String findUsedCodes(String start_date, String end_date){
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", str="";
	
	
	String qq = "select b.run_id run_id,if(b.term_type = 'Week 1',1,2) week_no, sum(b.hours) "+
	    "from tmwrp_blocks b "+
	    "join tmwrp_runs r on r.id=b.run_id "+
	    "join hour_codes c on c.id=b.hour_code_id "+
	    "join time_documents d on d.id=r.document_id "+
	    "join jobs j on d.job_id=j.id "+
	    "join pay_periods p on p.id = d.pay_period_id "+
	    "where c.type='Used' "+
	    "and j.salary_group_id in (2,4) ";
	if(!start_date.isEmpty()){
	    qq += " and  p.start_date >= ? ";
	}
	if(!end_date.isEmpty()){
	    qq += " and  p.start_date <= ? ";
	}
	qq += " group by run_id,week_no ";
	//
	usedMap = new TreeMap<>();
	logger.debug(qq);
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = "Could not connect to DB ";
	    return msg;
	}							
	try{
	    pstmt = con.prepareStatement(qq);
	    int jj=1;
	    if(!start_date.isEmpty()){
		java.util.Date date_tmp = df.parse(start_date);
		pstmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
	    }
	    if(!end_date.isEmpty()){
		java.util.Date date_tmp = df.parse(end_date);
		pstmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
	    }	
	    rs = pstmt.executeQuery();
	    int prev_run = 0, prev_week = 0;
	    while(rs.next()){
		int run_id = rs.getInt(1);
		int week_no = rs.getInt(2);
		double hrs = rs.getDouble(3);
		if(usedMap.containsKey(run_id)){
		    Map<Integer, Double> map = usedMap.get(run_id);
		    map.put(week_no, hrs);
		}
		else{
		    Map<Integer, Double> map = new TreeMap<>();
		    map.put(week_no, hrs);
		    usedMap.put(run_id, map);
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
    // for two weeks 
    //
    public String doDelete(){
	//
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = "delete from tmwrp_primes where run_id=? ";
	logger.debug(qq);
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = "Could not connect to DB ";
	    return msg;
	}							
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, run_id);						
	    pstmt.executeUpdate();
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
       create table tmwrp_primes(
       run_id int unsigned not null,
       week_no int unsigned not null,
       hour_code_id int unsigned not null,
       hours double(5,2),
       prime_factor double(5,2),
       foreign key(run_id) references tmwrp_runs(id),
       foreign key(hour_code_id) references hour_codes(id),
       unique(run_id,week_no,hour_code_id)
       )engine=InnoDB;

       create table hour_code_translates(
       hour_code_id int unsigned not null,
       alt_hour_code_id int unsigned not null,
       prime_factor double(5,2),
       foreign key(hour_code_id) references hour_codes(id),
       foreign key(alt_hour_code_id) references hour_codes(id)
       )engine=InnoDB;

       (34,34,'0.33'), // CE1.5
       (43,43,0.33), //  OT1.5
       (44,44,0.25), // OT2.0
       (45,45,0.25),  // CE2.0
       (46,46,0.25), // HCE2.0
       (50,50,0.50), // HCE1.0
       (71,71,0.50), // CE1.0
       (78,78,0.50), // OT1.0
       (79,79,0.33), // HCE1.5
       
       insert into hour_code_translates values
       (34,34,'0.33'), 
       (43,43,0.33), 
       (44,44,0.25), 
       (45,45,0.25), 
       (46,46,0.25), 
       (50,50,0.50), 
       (71,71,0.50), 
       (78,78,0.50), 
       (79,79,0.33), 
       (112,78,0.50),
       (113,43,0.33),
       (114,71,0.50),
       (115,34,0.33),
       (127,43,0.33),
       (128,44,0.25),
       (172,43,0.33),
       (173,44,0.25)

	ct10Set.add("114");
	ct15Set.add("115");
	ot15Set.add("127");	
	ot20Set.add("128");
	ot15Set.add("172");
	ot20Set.add("173");	

	// Earned
       select b.run_id run_id,if(b.term_type = 'Week 1',1,2) week_no, ht.alt_hour_code_id hour_code,ht.prime_factor factor, sum(b.hours)
       from tmwrp_blocks b
       join tmwrp_runs r on r.id=b.run_id
       join time_documents d on d.id=r.document_id
       join jobs j on d.job_id=j.id
       join pay_periods p on p.id = d.pay_period_id
       join hour_code_translates ht on ht.hour_code_id=b.hour_code_id
       where b.hour_code_id in (34,43,44,45,46,50,71,78,79,112,113,114,115,127,128,172,173)
       and j.salary_group_id in (2,4)
       and p.start_date > '2026-03-01'
       group by run_id,week_no,hour_code,factor;
       
       // used
       // b.hour_code_id in (2,4,6,8)
       //
       select b.run_id run_id,if(b.term_type = 'Week 1',1,2) week_no, sum(b.hours)
       from tmwrp_blocks b
       join tmwrp_runs r on r.id=b.run_id
       join hour_codes c on c.id=b.hour_code_id
       join time_documents d on d.id=r.document_id
       join jobs j on d.job_id=j.id
       join pay_periods p on p.id = d.pay_period_id
       where c.type='Used'
       and j.salary_group_id in (2,4)
       and p.start_date > '2026-03-01'       
       group by run_id,week_no;
       
       
       
       
    */
}
