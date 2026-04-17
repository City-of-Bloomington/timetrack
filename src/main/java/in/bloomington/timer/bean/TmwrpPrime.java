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
    static final long serialVersionUID = 1500L;
    static Hashtable<String, Double> primeFactors = new Hashtable<>();
    static {
	primeFactors.put("cp_earn_10",0.5);
	primeFactors.put("cp_earn_15",0.33);
	primeFactors.put("cp_earn_20",0.25);
	primeFactors.put("ot_earn_10",0.5);
	primeFactors.put("ot_earn_15",0.33);
	primeFactors.put("ot_earn_20",0.25);	
    }
    
    String run_id="",
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
	setPrimeCode(val3);
	setHours(val4);
	setPrimeFactor(val5);
    }		
		
    //
    // getters
    //
    public String getRun_id(){
	return run_id;
    }		
    public String getPrimeCode(){
	return prime_code;
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
	    if ( this.run_id.equals(c.getRun_id()) && this.week_no.equals(c.getWeekNo()) && this.prime_code.equals(c.getPrimeCode()))
		return true;
	}
	return false;
    }
    public int hashCode(){
	int seed = 37;
	try{
	    seed += run_id.hashCode()+week_no.hashCode()+prime_code.hashCode();
	}catch(Exception ex){
	    // we ignore
	}
	return seed;
    }
    public String toString(){
	return run_id+" "+week_no+" "+prime_code;
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
	if(primeFactors.containsKey(prime_code)){
	    factor = primeFactors.get(prime_code);
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
	    pstmt.setString(3, prime_code);
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
		if(dd > 0){
		    if(primeFactors.containsKey(key)){
			factor = primeFactors.get(key);
		    }
		    pstmt.setString(1, run_id);
		    pstmt.setString(2, week_no);
		    pstmt.setString(3, key);
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
       prime_code varchar(20) not null,
       hours double(5,2),
       prime_factor double(5,2),
       foreign key(run_id) references tmwrp_runs(id),
       unique(run_id,week_no,prime_code)
       )engine=InnoDB;    
    */
}
