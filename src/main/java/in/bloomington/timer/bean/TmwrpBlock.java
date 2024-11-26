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

public class TmwrpBlock{

    static Logger logger = LogManager.getLogger(TmwrpBlock.class);
    static final long serialVersionUID = 1500L;
    static SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");		
    String id="", run_id="", hour_code_id="",
	term_type=""; // Week 1, Week 2, Cycle
    int start_id = 1, cycle_order=1, minutes=0;
    double hours=0, amount=0; 
    //
    HourCode hourCode = null;
		
    public TmwrpBlock(){
    }		
    public TmwrpBlock(String val){
	setId(val);
    }
    // for new record - no amount
    public TmwrpBlock(int val,
		      String val2,
		      String val3,
		      String val4,
		      Integer val5,
		      Double val6
		      ){
	setId(val);
	setRun_id(val2);
	setHour_code_id(val3);
	setTermType(val4);
	setCycleOrder(val5);
	setHours(val6);
    }		
    // for new record with amount
    public TmwrpBlock(int val,
		      String val2,
		      String val3,
		      String val4,
		      Integer val5,
		      Double val6,
		      Double val7
		      ){
	setId(val);
	setRun_id(val2);
	setHour_code_id(val3);				
	setTermType(val4);
	setCycleOrder(val5);
	setHours(val6);
	setAmount(val7);
    }
		
    public TmwrpBlock(String val,
		      String val2,
		      String val3,
		      String val4,
		      Integer val5,
		      Double val6,
		      Double val7
		      ){
	setId(val);
	setRun_id(val2);
	setHour_code_id(val3);				
	setTermType(val4);
	setCycleOrder(val5);
	setHours(val6);
	setAmount(val7);
    }
    // needed for the list
    public TmwrpBlock(String val,
		      String val2,
		      String val3,
		      String val4,
		      Integer val5,
		      Double val6,
		      Double val7,
											
		      String val9, // hourCode
		      String val10,
		      String val11,
		      String val12,
		      String val13,
		      boolean val14,
		      String val15,
		      Double val16,
		      Double val17,
		      boolean val18,
		      boolean val19,
										
		      String val20, // nw_code
		      String val21){
	setId(val);
	setRun_id(val2);
	setHour_code_id(val3);				
	setTermType(val4);				
	setCycleOrder(val5);
	setHours(val6);
	setAmount(val7);
	hourCode = new HourCode(val9,
				val10,
				val11,
				val12,
				val13,
				val14,
				val15,
				val16,
				val17,
				val18,
				val19, // nw_code
				val20,
				val21);
    }
		
    //
    // getters
    //
    public String getId(){
	return id;
    }
    public String getRun_id(){
	return run_id;
    }		
    public String getHour_code_id(){
	return hour_code_id;
    }

    public double getHours(){
	return hours;
    }
    public Integer getMinutes(){
	return minutes;
    }
    public double getAmount(){
	return amount;
    }		
    public String getTermType(){
	return term_type;
    }
    public int getCycleOrder(){
	return cycle_order;
    }
    //
    // setters
    //
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setId(int val){
	if(val >  0)
	    id = ""+val;
    }		
		
    public void setRun_id(String val){
	if(val != null)
	    run_id = val;
    }
    public void setTermType(String val){
	if(val != null)
	    term_type = val;
    }				
		
    public void setHour_code_id(String val){
	if(val != null){
	    hour_code_id = val;
	}
    }
    public void setCycleOrder(Integer val){
	if(val != null)
	    cycle_order = val;
    }				
    public void setHours(Double val){
	if(val != null)
	    hours = val;
    }
    public void setAmount(Double val){
	if(val != null)
	    amount = val;
    }
    public void setMinutes(Integer val){
	if(val != null)
	    minutes = val;
    }		

    public boolean equals(Object o) {
	if (o instanceof TmwrpBlock) {
	    TmwrpBlock c = (TmwrpBlock) o;
	    if ( this.id.equals(c.getId())) 
		return true;
	}
	return false;
    }
    public int hashCode(){
	int seed = 37;
	if(!id.isEmpty()){
	    try{
		seed += Integer.parseInt(id)*31;
	    }catch(Exception ex){
		// we ignore
	    }
	}
	return seed;
    }
    public String toString(){
	return id;
    }
		
    public HourCode getHourCode(){
	if(hourCode == null && !hour_code_id.isEmpty()){
	    HourCode one = new HourCode(hour_code_id);
	    String back = one.doSelect();
	    if(back.isEmpty()){
		hourCode = one;
	    }
	}
	return hourCode;
    }
    // ToDo start here
		
    public String doSelect(){
	//
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = "select "+
	    "g.hour_code_id,g.term_type,g.cycle_order,g.hours,g.amount "+
	    " from tmwrp_blocks g where g.id =? and run_id=? ";
	logger.debug(qq);
	con = UnoConnect.getConnection();				
	try{
	    if(con != null){
		pstmt = con.prepareStatement(qq);
		pstmt.setString(1, id);
		pstmt.setString(2, run_id);								
		rs = pstmt.executeQuery();
		if(rs.next()){
		    setHour_code_id(rs.getString(1));										
		    setTermType(rs.getString(2));
		    setCycleOrder(rs.getInt(3));
		    setHours(rs.getDouble(4));
		    setAmount(rs.getDouble(5));
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

    public String doSave(){
	//
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = "insert into tmwrp_blocks values(?,?,?,?,?, ?,?) ";
	if(run_id.isEmpty()){
	    msg = " timewarp run not set ";
	    return msg;
	}
	if(id.isEmpty()){
	    msg = " timewarp id not set ";
	    return msg;
	}
								
	if(hour_code_id.isEmpty()){
	    msg = " hour code id not set ";
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
	    pstmt.setString(1, id);
	    pstmt.setString(2, run_id);						
	    pstmt.setString(3, hour_code_id);
	    pstmt.setString(4, term_type);
	    pstmt.setInt(5, cycle_order);
	    pstmt.setDouble(6, hours);
	    pstmt.setDouble(7, amount);
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
			     String term_type,
			     Integer cycle_order,
			     String code_type){ // Hours/Amount
	//
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = "replace into tmwrp_blocks values(?,?,?,?,?, ?,0) ";
	if(code_type.equals("Amount")){
	    qq = "replace into tmwrp_blocks values(?,?,?,?,?, 0,?)";
	}
	if(run_id.isEmpty()){
	    msg = " timewarp run not set ";
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
		pstmt.setInt(1, start_id++);
		pstmt.setString(2, run_id);						
		pstmt.setString(3, key);
		pstmt.setString(4, term_type);
		pstmt.setInt(5, cycle_order);
		pstmt.setDouble(6, dd); // amout or hours
		pstmt.executeUpdate();
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
    // most likely it is not needed
    //
    public String doDelete(){
	//
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = "delete from tmwrp_blocks where id=? and run_id=? ";
	logger.debug(qq);
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = "Could not connect to DB ";
	    return msg;
	}							
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, id);
	    pstmt.setString(2, run_id);						
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

}
