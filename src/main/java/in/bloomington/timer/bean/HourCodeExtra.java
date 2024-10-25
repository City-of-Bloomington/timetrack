package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.sql.*;
import javax.sql.*;
import java.text.SimpleDateFormat;
import in.bloomington.timer.list.*;
import in.bloomington.timer.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HourCodeExtra implements Serializable{

    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
    static Logger logger = LogManager.getLogger(HourCodeExtra.class);
    static final long serialVersionUID = 800L;
    String id="", inactive="";
    String default_value_fixed = "";
    //
    // hour code that need to be present so that the other codes
    // can be used
    //
    String hour_code_associate_type = "Regular";
    int times_per_day = 1;
    double max_total_per_year = 500.;
    String hour_code_id = "";
    String[] delete_code_id = null;
    List<String> hourCode_ids = null;
    List<HourCode> hourCodes = null;
    public HourCodeExtra(
			 String val,
			 Integer val2,
			 boolean val3,
			 Double val4,
			 String val5,
			 boolean val6
			 ){
	setId(val);
	setTimesPerDay(val2);	
	setDefaultValueFixed(val3);
	setMaxTotalPerYear(val4);
	setHourCodeAssociateType(val5);	
	setInactive(val6);
    }
    public HourCodeExtra(String val){
	setId(val);
    }
    public HourCodeExtra(){
    }		
    //
    // getters
    //
    public Integer getTimesPerDay(){
	return times_per_day;
    }		
    public boolean getDefaultValueFixed(){
	return !default_value_fixed.isEmpty();
    }
    public Double getMaxTotalPerYear(){
	return max_total_per_year;
    }
    public String getHourCodeAssociateType(){
	return hour_code_associate_type;
    }
    public boolean getInactive(){
	return !inactive.isEmpty();
    }
    public boolean isActive(){
	return inactive.isEmpty();
    }		
    public String getId(){
	return id;
    }
    public boolean isDefaultValueFixed(){
	return !default_value_fixed.isEmpty();
    }
    //
    // setters
    //
    public void setId (String val){
	if(val != null)
	    id = val;
    }
    public void setTimesPerDay(Integer val){
	if(val != null)
	   times_per_day = val;
    }
    
    public void setHourCodeAssociateType(String val){
	if(val != null)
	    hour_code_associate_type = val;
    }
    public void setDefaultValueFixed(boolean val){
	if(val)
	    default_value_fixed = "y";
	else
	    default_value_fixed = "";
    }
    public void setMaxTotalPerYear(Double val){
	if(val != null)
	    max_total_per_year = val;
    }
    public void setInactive(boolean val){
	if(val)
	    inactive = "y";
    }
    
    // add new hour code
    public void setHour_code_id (String val){
	if(val != null && !val.equals("-1"))
	    hour_code_id = val;
    }
    public String getHour_code_id(){
	if(hour_code_id.isEmpty())
	    return "-1";
	return hour_code_id;
    }
    public void setDelete_code_id (String[] vals){
	if(vals != null)
	    delete_code_id = vals;
    }
    public boolean containsHourCode_id(String val){
	if(val != null){
	    if(hourCode_ids != null){
		for(String str:hourCode_ids){
		    if(str.equals(val)) return true;
		}
	    }
	}
	return false;
    }
    public String toString(){
	return id;
    }
    public boolean equals(Object o) {
	if (o instanceof HourCodeExtra) {
	    HourCodeExtra c = (HourCodeExtra) o;
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
    public boolean hasHourCodes(){
	if(hourCode_ids.isEmpty()){
	    findHourCodes();
	}
	return !hourCode_ids.isEmpty();
    }
    public List<String> getHourCode_ids(){
	if(hourCode_ids == null)
	    findHourCodes();
	return hourCode_ids;
    }
    public List<HourCode> getHourCodes(){

	if(hourCode_ids != null){
	    if(hourCodes == null)
		hourCodes = new ArrayList<>();
	    for(String str:hourCode_ids){
		HourCode one = new HourCode(str);
		String back = one.doSelect();
		hourCodes.add(one);
	    }
	}
	return hourCodes;
    }
    void findHourCodes(){
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = "select hour_code_id  "+
	    " from hour_code_extra_related where hour_code_extra_id = ? ";
	logger.debug(qq);
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = "Could not connect to DB ";
	    logger.error(msg);
	}								
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, id);
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		if(hourCode_ids == null)
		    hourCode_ids = new ArrayList<>();
		str = rs.getString(1);
		if(str != null)
		    hourCode_ids.add(str);
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
    }
    public String doSelect(){
	//
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = "select id,times_per_day,default_value_fixed,max_total_per_year,hour_code_associate_type,inactive "+
	    " from hour_code_extras where id =? ";
	logger.debug(qq);
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = "Could not connect to DB ";
	    return msg;
	}								
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, id);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		setTimesPerDay(rs.getInt(2));
		setDefaultValueFixed(rs.getString(3) != null);
		setMaxTotalPerYear(rs.getDouble(4));
		setHourCodeAssociateType(rs.getString(5));
		setInactive(rs.getString(6) != null);
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
	findHourCodes();
	return msg;
    }
    public String addHourCode(){
	//
	Connection con = null;
	PreparedStatement pstmt = null, pstmt2=null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = "insert into hour_code_extra_related values(0,?,?) ";
	if(hour_code_id.isEmpty()){
	    msg = " need to pick an hour code ";
	    return msg;
	}
	if(id.isEmpty()){
	    msg = " id not set ";
	    return msg;
	}	
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = "Could not connect to DB ";
	    return msg;
	}							
	logger.debug(qq);
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, id);
	    pstmt.setString(2, hour_code_id);
	    pstmt.executeUpdate();
	    //
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
    public String deleteSelectedCodes(){
	//
	Connection con = null;
	PreparedStatement pstmt = null, pstmt2=null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = "delete from hour_code_extra_related where hour_code_extra_id=? and hour_code_id=? ";
	if(hour_code_id.isEmpty()){
	    msg = " need to pick an hour code ";
	    return msg;
	}
	if(id.isEmpty()){
	    msg = " id not set ";
	    return msg;
	}	
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = "Could not connect to DB ";
	    return msg;
	}							
	logger.debug(qq);
	try{
	    pstmt = con.prepareStatement(qq);
	    for(String st:delete_code_id){
		pstmt.setString(1, id);
		pstmt.setString(2, st);
		pstmt.executeUpdate();
	    }
	    //
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
    
    public String doSave(){
	//
	Connection con = null;
	PreparedStatement pstmt = null, pstmt2=null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = "insert into hour_code_extras values(0,?,?,?,?,null) ";
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = "Could not connect to DB ";
	    return msg;
	}							
	logger.debug(qq);
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setInt(1, times_per_day);
	    if(default_value_fixed.isEmpty())
		pstmt.setNull(2,Types.CHAR);
	    else
		pstmt.setString(2, "y");
	    pstmt.setDouble(3, max_total_per_year);
	    if(hour_code_associate_type.isEmpty())
		pstmt.setNull(4, Types.VARCHAR);
	    else
		pstmt.setString(4, hour_code_associate_type);
	    pstmt.executeUpdate();
	    //
	    qq = "select LAST_INSERT_ID()";
	    pstmt2 = con.prepareStatement(qq);
	    rs = pstmt2.executeQuery();
	    if(rs.next()){
		id = rs.getString(1);
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
    //
    // we can update expire date and inactive
    //
    public String doUpdate(){
	//
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", str="";
	if(id.isEmpty()){
	    return " id not set ";
	}
	String qq = "update hour_code_extras set "+
	    " times_per_day=?,default_value_fixed=?,max_total_per_year=?,"+
	    " hour_code_associate_type=?,"+
	    "inactive=? where id=? ";
				
	logger.debug(qq);
	con = UnoConnect.getConnection();				
	if(con == null){
	    msg = "Could not connect to DB ";
	    return msg;
	}			
	try{
	    pstmt = con.prepareStatement(qq);
	    int jj=1;
	    pstmt.setInt(jj++, times_per_day);
	    if(default_value_fixed.isEmpty())
		pstmt.setNull(jj++,Types.CHAR);
	    else
		pstmt.setString(jj++, default_value_fixed);
	    pstmt.setDouble(jj++, max_total_per_year);
	    if(hour_code_associate_type.isEmpty())
		pstmt.setNull(jj++, Types.VARCHAR);
	    else
		pstmt.setString(jj++, hour_code_associate_type);
	    if(inactive.isEmpty()){
		pstmt.setNull(jj++, Types.CHAR);
	    }
	    else{
		pstmt.setString(jj++,"y");
	    }
	    pstmt.setString(jj++, id);
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
	if(!hour_code_id.isEmpty()){
	   msg += addHourCode();
	}
	if(delete_code_id != null){
	   msg += deleteSelectedCodes();
	}
	msg += doSelect();
	return msg;
    }

}

/**
   //
   // this is added to handle COMMUTE earn code
   // used by regular employees once a day
   // hour_code_type_associate type 'Regular'
   //

  create table hour_code_extras (
  id int unsigned not null auto_increment,
  times_per_day tinyint default 1,
  default_value_fixed char(1) default 'y',
  max_total_per_year double default 500,
  hour_code_associate_type enum('Regular','Used','Earned','Overtime','Unpaid','Other','Call Out','Monetary') default 'Regular',
  inactive char(1),
  primary key(id)
  )engine=InnoDB;

  create table hour_code_extra_related (
  id int unsigned not null auto_increment,
  hour_code_extra_id int unsigned not null,  
  hour_code_id int unsigned NOT NULL,
  FOREIGN KEY (hour_code_id) REFERENCES hour_codes(id),
  FOREIGN KEY (hour_code_extra_id) REFERENCES hour_code_extras(id),    
  primary key(id)  
  )engine=InnoDB;    

  insert into hour_code_extras values(0,1,'y',500,'Regular',null);
  insert into hour_code_extra_related values(0,1,157);
  insert into hour_code_extra_related values(0,1,158);  
 */
