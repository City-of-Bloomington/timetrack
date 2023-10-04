package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.Serializable;
import java.util.List;
import java.sql.*;
import javax.sql.*;
import java.text.SimpleDateFormat;
import in.bloomington.timer.list.*;
import in.bloomington.timer.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HourCodeExtraCondition implements Serializable{

    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
    static Logger logger = LogManager.getLogger(HourCodeExtraCondition.class);
    static final long serialVersionUID = 800L;
    String id="", hour_code_id="", inactive="";
    String default_value_fixed = ""; 
    String hour_code_associate_type = "Regular";
    int times_per_day = 1;
    double max_total_per_year = 500.;
    HourCode hourCode = null;
    
    public HourCodeExtraCondition(
			     String val,
			     String val2,
			     Integer val3,
			     boolean val4,
			     Double val5,
			     String val6,
			     boolean val7
			     ){
	setId(val);
	setHourCode_id(val2);
	setTimesPerDay(val3);	
	setDefaultValueFixed(val4);
	setMaxTotalPerYear(val5);
	setHourCodeAssociateType(val6);	
	setInactive(val7);
    }
    public HourCodeExtraCondition(String val){
	setId(val);
    }
    public HourCodeExtraCondition(){
    }		
    //
    // getters
    //
    public String getHourCode_id(){
	return hour_code_id;
    }
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
    public void setHourCode_id (String val){
	if(val != null && !val.equals("-1"))
	    hour_code_id = val;
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
    public String toString(){
	return id;
    }
    public boolean equals(Object o) {
	if (o instanceof HourCodeExtraCondition) {
	    HourCodeExtraCondition c = (HourCodeExtraCondition) o;
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
    public HourCode getHourCode(){
	if(!hour_code_id.isEmpty() && hourCode == null){
	    HourCode one = new HourCode(hour_code_id);
	    String back = one.doSelect();
	    if(back.isEmpty()){
		hourCode = one;
	    }
	}
	return hourCode;
    }
    public String doSelect(){
	//
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = "select id,hour_code_id,times_per_day,default_value_fixed,max_total_per_year,hour_code_associate_type,inactive "+
	    " from hour_code_extra_conditions where id =? ";
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
		setHourCode_id(rs.getString(2));
		setTimesPerDay(rs.getInt(3));
		setDefaultValueFixed(rs.getString(4) != null);
		setMaxTotalPerYear(rs.getDouble(5));
		setHourCodeAssociateType(rs.getString(6));
		setInactive(rs.getString(7) != null);
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
	PreparedStatement pstmt = null, pstmt2=null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = "insert into hour_code_extra_conditions values(0,?,?,?,?,?,null) ";
	if(hour_code_id.isEmpty()){
	    msg = " need to pick an hour code ";
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
	    pstmt.setString(1, hour_code_id);
	    pstmt.setInt(2, times_per_day);
	    if(default_value_fixed.isEmpty())
		pstmt.setNull(3,Types.CHAR);
	    else
		pstmt.setString(3, "y");
	    pstmt.setDouble(4, max_total_per_year);
	    if(hour_code_associate_type.isEmpty())
		pstmt.setNull(5, Types.VARCHAR);
	    else
		pstmt.setString(5, hour_code_associate_type);
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
	if(hour_code_id.isEmpty()){
	    return " hour code is required";
	}
	String qq = "update hour_code_extra_conditions set hour_code_id=?,"+
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
	    pstmt.setString(jj++, hour_code_id);
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
	if(msg.isEmpty()){
	    doSelect();
	}

	return msg;
    }

}

/**
   //
   // this is added to handle COMMUTE earn code
   // used by regular employees once a day
   // hour_code_type_associate type 'Regular'
   //
  create table hour_code_extra_conditions (
  id int unsigned not null auto_increment,
  hour_code_id int(10) unsigned NOT NULL,
  times_per_day tinyint default 1,
  default_value_fixed char(1) default 'y',
  max_total_per_year double default 500,
  hour_code_associate_type enum('Regular','Used','Earned','Overtime','Unpaid','Other','Call Out','Monetary') default 'Regular',
  inactive char(1),
  primary key(id),
  FOREIGN KEY (hour_code_id) REFERENCES hour_codes (id)
  )engine=InnoDB;
  
 */
