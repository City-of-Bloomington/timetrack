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
    // Map<Integer, Map<Integer, Double>> usedMap = null;
    static Hashtable<String, Double> primeFactors = new Hashtable<>();
    static Hashtable<String, String> hourCodes = new Hashtable<>();
    static Hashtable<Integer, Double> week1_used = new Hashtable<>();
    static Hashtable<Integer, Double> week2_used = new Hashtable<>();    
    // we need these two for old data only
    static Hashtable<Integer, Double> week1_reg = new Hashtable<>();
    static Hashtable<Integer, Double> week2_reg = new Hashtable<>();
    static Hashtable<Integer, Double> week1_non_reg = new Hashtable<>();
    static Hashtable<Integer, Double> week2_non_reg = new Hashtable<>();        
    static {
	primeFactors.put("cp_earn_10",0.5);
	primeFactors.put("cp_earn_15",0.5);
	primeFactors.put("cp_earn_20",0.5);
	primeFactors.put("ot_earn_10",0.5);
	primeFactors.put("ot_earn_15",0.5);
	primeFactors.put("ot_earn_20",0.5);
	// hour code id's
	hourCodes.put("cp_earn_10","71");// Comp time 1.0
	hourCodes.put("cp_earn_15","34");// Comp time 1.5
	hourCodes.put("cp_earn_20","45");// Comp time 2.0
	hourCodes.put("ot_earn_10","78");// Overtime 1.0
	hourCodes.put("ot_earn_15","43");// Overtime 1.5
	hourCodes.put("ot_earn_20","44");// Overtime 2.0
    }
    // earn codes to exlude from type = Other
    // H1.0 5, BMP 3, JD 13, MP = 62, MVH HP 132, OJI 72,USCC Holiday 182,  
    
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
    public String doSaveBolk(Double grs_reg_total,
			     Hashtable<String, Double> hash,
			     Double weeklyEarnTimeUsed,
			     Integer week_no){ 
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
	if(week_no == null){
	    msg = " week nubmer not set ";
	    return msg;
	}	
	if(hash == null || hash.isEmpty()){
	    return msg;
	}

	double reg_total = grs_reg_total;
	double earned_time_used = weeklyEarnTimeUsed;
	System.err.println(" week  "+week_no+" reg "+reg_total+ " used "+earned_time_used);
	if(earned_time_used > 0){
	    System.err.println("weekly earned time used reduced "+earned_time_used);
	    reg_total = reg_total - earned_time_used; 
	}
	System.err.println(" week  "+week_no+" reg "+reg_total);	
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
		double factor = 0.5; // for everybody
		String code_id = "";
		System.err.println(" key "+key+" "+dd);
		if(dd > 0){
		    if(hourCodes.containsKey(key)){
			code_id = hourCodes.get(key);
		    }
		    if(code_id.isEmpty()){
			msg =" Earn Code id not found" ;
			logger.error(msg);
			continue;
		    }		    
		    if(reg_total >= 40.){
			pstmt.setString(1, run_id);
			pstmt.setInt(2, week_no);
			pstmt.setString(3, code_id);
			pstmt.setDouble(4, dd); // hours
			pstmt.setDouble(5, factor); 
			pstmt.executeUpdate();
		    }
		    else { // < or = 
			if(reg_total+dd <= 40.){
			    reg_total += dd;
			}
			else{
			    dd = dd - (40 - reg_total);
			    reg_total = 40;
			    System.err.println(" dd "+dd);
			    if(dd > 0){
				pstmt.setString(1, run_id);
				pstmt.setInt(2, week_no);
				pstmt.setString(3, code_id);
				pstmt.setDouble(4, dd); // hours
				pstmt.setDouble(5, factor); 
				pstmt.executeUpdate();
			    }
			}
		    }
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
    public String findTotalReg(String start_date, String end_date){
	Connection con = null;
	PreparedStatement pstmt = null, pstmt2=null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = "select r.id,r.week1_grs_reg_hrs week1_reg, r.week2_grs_reg_hrs  "+
	    "from tmwrp_runs r "+	
	    "join time_documents d on d.id=r.document_id "+
	    "join pay_periods p on p.id = d.pay_period_id "+
	    "join jobs j on d.job_id=j.id and j.salary_group_id in (2,4) ";
	String qw = "";
	if(!start_date.isEmpty()){
	    qw = " p.start_date >= ? ";
	}
	if(!end_date.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " p.start_date <= ? ";
	}
	if(!qw.isEmpty())
	    qq = qq + " where "+qw;
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
	    while(rs.next()){
		int run_id = rs.getInt(1);
		double w1_reg = rs.getDouble(2);
		double w2_reg = rs.getDouble(3);
		if(w1_reg > 0)
		    week1_reg.put(run_id, w1_reg);
		if(w2_reg > 0)
		    week2_reg.put(run_id, w2_reg);
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
       all hour codes of type Other that are excluded from
       the se (3,5,13,62,72,109,132,162,164,182);
       select id,name,description from hour_codes c where c.type='Other' and c.id not in
        (3,5,13,62,72,109,132,162,164,182);
     */
    public String findTotalNonReg(String start_date, String end_date){
	Connection con = null;
	PreparedStatement pstmt = null, pstmt2=null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = "select r.id run_id,sum(b.hours) non_reg "+
	    "from tmwrp_runs r "+
	    "join time_documents d on d.id=r.document_id "+
	    "join pay_periods p on p.id = d.pay_period_id "+
	    "join jobs j on d.job_id=j.id and j.salary_group_id in (2,4) "+		    "join tmwrp_blocks b on b.run_id=r.id and b.term_type='Week 1' "+
	    "join hour_codes c on b.hour_code_id=c.id and c.type = 'Other' ";
	String qq2 = "select r.id run_id,sum(b.hours) non_reg "+
	    "from tmwrp_runs r "+
	    "join time_documents d on d.id=r.document_id "+
	    "join pay_periods p on p.id = d.pay_period_id "+
	    "join jobs j on d.job_id=j.id and j.salary_group_id in (2,4) "+		    "join tmwrp_blocks b on b.run_id=r.id and b.term_type='Week 2' "+	
	    "join hour_codes c on b.hour_code_id=c.id and c.type = 'Other' ";
	
	String qw = " where c.id not in (3,5,13,62,72,132,162,164,182) ";
	if(!start_date.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " p.start_date >= ? ";
	}
	if(!end_date.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " p.start_date <= ? ";
	}
	qq = qq + " "+qw;
	qq2 = qq2+" "+qw;
	qq += " group by run_id ";
	qq2 += " group by run_id ";
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
		pstmt.setDate(jj, new java.sql.Date(date_tmp.getTime()));
		pstmt2.setDate(jj, new java.sql.Date(date_tmp.getTime()));
		jj++;
	    }
	    if(!end_date.isEmpty()){
		java.util.Date date_tmp = df.parse(end_date);
		pstmt.setDate(jj, new java.sql.Date(date_tmp.getTime()));
		pstmt2.setDate(jj, new java.sql.Date(date_tmp.getTime()));
	    }	
	    rs = pstmt.executeQuery();	
	    while(rs.next()){
		double w1_non_reg = 0;
		int run_id = rs.getInt(1);
		if(rs.getString(2) != null){
		    w1_non_reg = rs.getDouble(2);
		    if(w1_non_reg > 0)
			week1_non_reg.put(run_id, w1_non_reg);
		}
	    }
	    rs = pstmt2.executeQuery();	
	    while(rs.next()){
		double w2_non_reg = 0;
		int run_id = rs.getInt(1);
		if(rs.getString(2) != null){
		    w2_non_reg = rs.getDouble(2);
		    if(w2_non_reg > 0)
			week2_non_reg.put(run_id, w2_non_reg);
		}	    
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
	// (34,43,44,45,46,50,71,78,79,112,113,114,115,127,128,170,172,173,177) "+
	String qq2 = "replace into tmwrp_primes values(?,?,?,?,?) ";
	String qq = "select b.run_id run_id,if(b.term_type = 'Week 1',1,2) week_no, ht.alt_hour_code_id hour_code,ht.prime_factor factor, sum(b.hours) hours  "+
	    "from tmwrp_blocks b "+
	    "join hour_codes c on c.id=b.hour_code_id "+
	    "join tmwrp_runs r on r.id=b.run_id "+
	    "join time_documents d on d.id=r.document_id "+
	    "join jobs j on d.job_id=j.id "+
	    "join pay_periods p on p.id = d.pay_period_id "+
	    "join hour_code_translates ht on ht.hour_code_id=b.hour_code_id "+
	    "where c.type in ('Earned','Overtime') "+
	    "and j.salary_group_id in (2,4) ";
	if(!start_date.isEmpty()){
	    qq += " and  p.start_date >= ? ";
	}
	if(!end_date.isEmpty()){
	    qq += " and  p.start_date <= ? ";
	}
	qq += " group by run_id,week_no,hour_code,factor ";
	msg = findTotalReg(start_date, end_date);
	msg = findTotalNonReg(start_date, end_date);
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
		double total_reg = 0; // reg and other codes
		int run_id = rs.getInt(1); 
		int week_no = rs.getInt(2);
		String code_id = rs.getString(3);
		double factor = rs.getDouble(4);
		double hours = rs.getDouble(5);
		// String emp_id = rs.getString(6);
		if(week_no == 1){
		    if(week1_reg.containsKey(run_id)){
			total_reg = week1_reg.get(run_id);
			/**
			if(run_id == 162909){
			    System.err.println("w1 total reg "+total_reg+" "+hours);
			}
			*/
		    }
		    if(week1_non_reg.containsKey(run_id)){
			total_reg += week1_non_reg.get(run_id);
			/**
			if(run_id == 162909){
			    System.err.println("w1 non-reg "+week1_non_reg.get(run_id));
			    System.err.println("w1 total reg "+total_reg+" "+hours);
			}
			*/
		    }
		    if(week1_used.containsKey(run_id)){
			/**
			if(run_id == 162909){
			    System.err.println("w1 used "+week1_used.get(run_id));
			    System.err.println("w1 total reg "+total_reg);
			    System.err.println(" hrs "+hours);

			}
			*/
			if(total_reg > week1_used.get(run_id)){
			    total_reg = total_reg - week1_used.get(run_id);
			}
			else{
			    total_reg = 0;
			}
			/**
			if(run_id == 162909){
			    System.err.println("w1 total reg "+total_reg);
			    System.err.println(" hrs "+hours);
			}
			*/
		    }
		}
		else {
		    if(week2_reg.containsKey(run_id)){
			total_reg = week2_reg.get(run_id);
			/**
			if(run_id == 162909){
			    System.err.println("w2 total reg "+total_reg);
			    System.err.println(" hrs "+hours);
			}
			*/
		    }
		    if(week2_non_reg.containsKey(run_id)){
			total_reg += week2_non_reg.get(run_id);
			/**
			if(run_id == 162909){
			    System.err.println("w2 non reg "+ week2_non_reg.get(run_id));
			    System.err.println("w2 total reg "+total_reg);
			    System.err.println(" hrs "+hours);
			}
			*/
		    }
		    if(week2_used.containsKey(run_id)){
			/**
			if(run_id == 162909){			
			    System.err.println("w2 used "+ week2_used.get(run_id));
			}
			*/
			if(total_reg > week2_used.get(run_id)){
			    total_reg = total_reg - week2_used.get(run_id);
			}
			else{
			    total_reg = 0;
			}
			/**
			if(run_id == 162909){
			    System.err.println("w2 total reg "+total_reg);
			    System.err.println(" hrs "+hours);
			}
			*/
		    }		    
		}
		/**
		if(run_id == 162909){
		    System.err.println("after 1,2 total reg "+total_reg);
		    System.err.println(" hrs "+hours);
		}
		*/
		if(total_reg < 40.){
		    /**
		    if(run_id == 162909){
			System.err.println("after < 40 total reg "+total_reg);
			System.err.println(" hrs "+hours);
		    }
		    */
		    if(total_reg + hours > 40){
			hours = total_reg + hours - 40;
			total_reg = 40.;
			if(week_no == 1){
			    week1_reg.put(run_id, total_reg);
			}
			else{
			    week2_reg.put(run_id, total_reg);
			}			
		    }
		    else { // <= 40 fill reg till 40
			total_reg = total_reg + hours;
			hours = 0;
			if(week_no == 1){
			    week1_reg.put(run_id, total_reg);
			}
			else{
			    week2_reg.put(run_id, total_reg);
			}
		    }
		    /**
		    if(run_id == 162909){
			System.err.println(" after change hrs "+hours);
		    }
		    */

		}
		else { // >= 40
		    // all hours are overtime
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
		if(week_no == 1){
		    week1_used.put(run_id, hrs);
		}
		else{
		    week2_used.put(run_id, hrs);
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


       
       insert into hour_code_translates select id,id,0.5 from hour_codes
       where type in ('Earned','Overtime');

       update hour_code_translates set alt_hour_code_id=78 where hour_code_id=112;
       update hour_code_translates set alt_hour_code_id=43 where hour_code_id=113;
       update hour_code_translates set alt_hour_code_id=71 where hour_code_id=114;
       update hour_code_translates set alt_hour_code_id=34 where hour_code_id=115;
       update hour_code_translates set alt_hour_code_id=43 where hour_code_id=127;
 
       update hour_code_translates set alt_hour_code_id=44 where hour_code_id=128;
      update hour_code_translates set alt_hour_code_id=118 where hour_code_id=118;       

      select c.name,t.hour_code_id,t.alt_hour_code_id,t.prime_factor from hour_code_translates t,hour_codes c where c.id=t.alt_hour_code_id;

      update hour_code_translates set prime_factor=0.50;

      
	// hour codes
	(34,43,44,45,46,50,71,78,79,112,113,114,115,127,128,170,172,173,177)
	// Earned
       select b.run_id run_id,if(b.term_type = 'Week 1',1,2) week_no, ht.alt_hour_code_id hour_code,ht.prime_factor factor, sum(b.hours)
       from tmwrp_blocks b
       join hour_codes c on b.hour_code_id=c.id
       join tmwrp_runs r on r.id=b.run_id
       join time_documents d on d.id=r.document_id
       join jobs j on d.job_id=j.id
       join pay_periods p on p.id = d.pay_period_id
       join hour_code_translates ht on ht.hour_code_id=b.hour_code_id
       where c.type in ('Earned','Overtime')
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
       
       
       	String qq =
	select r.id run_id,d.employee_id emp_id, d.pay_period_id pay_period_id, sum(b.hours) week1_non_reg,sum(b2.hours) week2_non_reg 
	    from tmwrp_runs r 
	    join time_documents d on d.id=r.document_id 
	    join pay_periods p on p.id = d.pay_period_id 
	    join jobs j on d.job_id=j.id and j.salary_group_id in (2,4) 		    left join tmwrp_blocks b on b.run_id=r.id and b.term_type='Week 1' 
	    left join tmwrp_blocks b2 on b2.run_id=r.id and b2.term_type='Week 2' 
	    left join hour_codes c on b.hour_code_id=c.id and c.type = 'Other' 
	    left join hour_codes c2 on b2.hour_code_id=c2.id and c2.type = 'Other'
	    where p.start_date > '2026-02-01' group by run_id,emp_id,pay_period_id

	    	select r.id,r.week1_grs_reg_hrs week1_reg, r.week1_grs_reg_hrs  
	    from tmwrp_runs r
	    join time_documents d on d.id=r.document_id 
	    join pay_periods p on p.id = d.pay_period_id 
	    join jobs j on d.job_id=j.id and j.salary_group_id in (2,4) 
	    where p.start_date > '2026-03-01';
	    
    */
}
