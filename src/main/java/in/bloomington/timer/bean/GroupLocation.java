package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.sql.*;
import javax.naming.*;
import javax.naming.directory.*;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GroupLocation{

    static final long serialVersionUID = 3700L;	
    static Logger logger = LogManager.getLogger(GroupLocation.class);
    String id="", location_id="", group_id="";
    Group group = null;
    Location location = null;
    //
    public GroupLocation(){
    }
    public GroupLocation(String val){
	//
	setId(val);
    }		
    public GroupLocation(String val, String val2, String val3){
	//
	// initialize
	//
	setId(val);
	setGroup_id(val2);
	setLocation_id(val3);
    }
    public GroupLocation(String val,
			 String val2,
			 String val3,
			 String val4,
			 String val5){
	//
	// initialize
	//
	setId(val);
	setGroup_id(val2);
	setLocation_id(val3);
	location = new Location(val3, val4, val5);
    }
    public GroupLocation(String val,
			 String val2,
			 String val3,
												 
			 String val4,
			 String val5,
												 
			 String val6, // group
			 String val7,
			 String val8,
			 String val9,
			 boolean val10,
			 boolean val11,
			 boolean val12,
			 boolean val13
			 ){
	setVals(val, val2, val3,
		val4, val5,
		val6, val7, val8, val9, val10, val11, val12, val13);
    }
    private void setVals(
			 String val,
			 String val2,
			 String val3,
												 
			 String val4,
			 String val5,
												 
			 String val6, // group
			 String val7,
			 String val8,
			 String val9,
			 boolean val10,
			 boolean val11,
			 boolean val12,
			 boolean val13
			 ){
	setId(val);
	setGroup_id(val2);
	setLocation_id(val3);
	location = new Location(val3, val4, val5);
	group = new Group(val2, val6, val7, val8, val9, val10, val11, val12, val13);
    }		

    public int hashCode(){
	int seed = 29;
	if(!group_id.isEmpty()){
	    try{
		seed += Integer.parseInt(group_id);
		if(!location_id.isEmpty()){
		    seed += Integer.parseInt(location_id);
		}
	    }catch(Exception ex){
	    }
	}
	return seed;
    }
    //
    // getters
    //
    public String getId(){
	return id;
    }
    public String getLocation_id(){
	return location_id;
    }
    public String getGroup_id(){
	return group_id;
    }
    public String getDepartment_id(){
	return ""; // not needed
    }				
    //
    // setters
    //
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setGroup_id(String val){
	if(val != null && !val.equals("-1"))
	    group_id = val;
    }
    public void setDepartment_id(String val){
	// not needed 
    }		
    public void setLocation_id(String val){
	if(val != null && !val.equals("-1"))
	    location_id = val;
    }		
    public String toString(){
	return group_id+" "+location_id;
    }
    public Location getLocation(){
	if(location == null && !location_id.isEmpty()){
	    Location one = new Location(location_id);
	    String back = one.doSelect();
	    if(back.isEmpty()){
		location = one;
	    }
	}
	return location;
    }
    public Group getGroup(){
	if(group == null && !group_id.isEmpty()){
	    Group one = new Group(group_id);
	    String back = one.doSelect();
	    if(back.isEmpty()){
		group = one;
	    }
	}
	return group;
    }
    //
    public String doSelect(){
	String back = "";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "select t.id,t.group_id,t.location_id,"+
	    " l.ip_address,l.name,"+
	    " g.name,g.description,g.department_id,g.excess_hours_earn_type,g.allow_pending_accrual,"+
	    " g.clock_time_required,"+
	    " g.include_in_auto_batch,"+	    	    
	    " g.inactive "+
	    " from group_locations t "+
	    " left join locations l on l.id = t.location_id "+
	    " left join `groups` g on g.id = t.group_id "+
	    " where t.id=? ";
				
	Connection con = UnoConnect.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    return back;
	}
	try{
	    logger.debug(qq);
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,id);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		setVals(rs.getString(1),
			rs.getString(2),
			rs.getString(3),
												
			rs.getString(4), // location
			rs.getString(5),
												
			rs.getString(6), // group
			rs.getString(7),
			rs.getString(8),
			rs.getString(9),
			rs.getString(10) != null,
			rs.getString(11) != null,
			rs.getString(13) != null,
			rs.getString(14) != null);

	    }
	    else{
		back ="Record "+id+" Not found";
	    }
	}
	catch(Exception ex){
	    back += ex+":"+qq;
	    logger.error(back);
	}
	finally{
	    Helper.databaseDisconnect(pstmt, rs);
	    UnoConnect.databaseDisconnect(con);
	}
	return back;
    }
    public String doSave(){
	PreparedStatement pstmt = null, pstmt2=null;
	ResultSet rs = null;
	Connection con = null;
	String msg="", str="";
	String qq = " insert into group_locations values(0,?,?)";
	if(group_id.isEmpty()){
	    msg = "group id is required";
	    return msg;
	}
	if(location_id.isEmpty()){
	    msg = "location id is required";
	    return msg;
	}
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = "Could not connect to DB ";
	    return msg;
	}
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, group_id);
	    pstmt.setString(2, location_id);
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
    public String doUpdate(){

	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection con = null;
	String msg="", str="";
	String qq = " update group_locations set group_id=?,location_id=? where id=?";
	if(group_id.isEmpty()){
	    msg = "group id not set";
	    return msg;
	}
	if(location_id.isEmpty()){
	    msg = "location id not set";
	    return msg;
	}
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = "Could not connect to DB ";
	    return msg;
	}
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, group_id);
	    pstmt.setString(2, location_id);
	    pstmt.setString(3, id);
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
    public String doDelete(){
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection con = null;
	String msg="", str="";
	String qq = " delete from group_locations where id=?";
	if(id.isEmpty()){
	    msg = "id is required";
	    return msg;
	}
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = "Could not connect to DB ";
	    return msg;
	}
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, id);
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
