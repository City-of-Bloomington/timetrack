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

public class Address{

    static final long serialVersionUID = 3700L;	
    static Logger logger = LogManager.getLogger(Address.class);
    String id="", employee_id="", line_1="", line_2="", city="", state="", zip="", inactive="";
    //
    public Address(){
	super();
    }
    public Address(String val){
	//
	setId(val);
    }		
    public Address(String val,
		   String val2,
		   String val3,
		   String val4,
		   String val5,
		   String val6,
		   String val7,
		   boolean val8){
	setId(val);
	setEmployee_id(val2);
	setLine_1(val3);
	setLine_2(val4);
	setCity(val5);
	setState(val6);
	setZip(val7);
	setInactive(val8);
    }		
    public boolean equals(Object obj){
	if(obj instanceof Address){
	    Address one =(Address)obj;
	    return employee_id.equals(one.getEmployee_id());
	}
	return false;				
    }
    public int hashCode(){
	int seed = 29;
	if(!id.equals("")){
	    try{
		seed += Integer.parseInt(employee_id);
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
    public String getEmployee_id(){
	return employee_id;
    }
    public String getLine_1(){
	return line_1;
    }
    public String getLine_2(){
	return line_2;
    }
    public String getLineAddress(){
	String ret = line_1;
	if(!line_2.isEmpty()){
	    ret += " "+line_2;
	}
	return ret;
    }
    public String getCity(){
	return city;
    }
    public String getState(){
	return state;
    }
    public String getZip(){
	return zip;
    }    
    public boolean getInactive(){
	return !inactive.equals("");
    }
    public boolean isInactive(){
	return !inactive.equals("");
    }
    public boolean isActive(){
	return inactive.equals("");
    }
    //
    // setters
    //
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setEmployee_id(String val){
	if(val != null)
	    employee_id = val;
    }
    public void setLine_1(String val){
	if(val != null)
	    line_1 = val.trim();
    }
    public void setLine_2(String val){
	if(val != null)
	    line_2 = val.trim();
    }
    public void setCity(String val){
	if(val != null)
	    city = val;
    }
    public void setState(String val){
	if(val != null)
	    state = val;
    }
    public void setZip(String val){
	if(val != null)
	    zip = val;
    }    
    public void setInactive(boolean val){
	if(val)
	    inactive = "y";
    }		
    public String toString(){
	String ret = line_1;
	if(!line_2.isEmpty())
	    ret += " "+line_2;
	if(!city.isEmpty()){
	    ret += " "+city;
	}
	if(!state.isEmpty()){
	    ret += ", "+state;
	}
	if(!zip.isEmpty()){
	    ret += " "+zip;
	}
	return ret;
    }
    //
    public String doSelect(){
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "select id,employee_id,line_1,line_2,city,state,zip,inactive "+
	    "from addresses where id=?";
	con = UnoConnect.getConnection();
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
		setEmployee_id(rs.getString(2));
		setLine_1(rs.getString(3));
		setLine_2(rs.getString(4));
		setCity(rs.getString(5));
		setState(rs.getString(6));
		setZip(rs.getString(7));
		setInactive(rs.getString(8) != null);
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
	Connection con = null;
	PreparedStatement pstmt = null, pstmt2=null;
	ResultSet rs = null;
	String msg="", str="";
	inactive=""; // default
	String qq = " insert into addresses values(0,?,?,?,?, ?,?,?)";
	if(employee_id.equals("")){
	    msg = "Employee id is required";
	    return msg;
	}
	try{
	    con = UnoConnect.getConnection();
	    if(con == null){
		msg = "Could not connect to DB ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    msg = setParams(pstmt);
	    if(msg.equals("")){
		pstmt.executeUpdate();
		Helper.databaseDisconnect(pstmt, rs);
		//
		qq = "select LAST_INSERT_ID()";
		pstmt2 = con.prepareStatement(qq);
		rs = pstmt2.executeQuery();
		if(rs.next()){
		    id = rs.getString(1);
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
    String setParams(PreparedStatement pstmt){
	String msg = "";
	int jj=1;
	try{
	    pstmt.setString(jj++, employee_id);
	    if(line_1.isEmpty()){
		pstmt.setNull(jj++, Types.VARCHAR);
	    }
	    else
		pstmt.setString(jj++, line_1);
	    if(line_2.isEmpty()){
		pstmt.setNull(jj++, Types.VARCHAR);
	    }
	    else
		pstmt.setString(jj++, line_2);
	    if(city.isEmpty()){
		pstmt.setNull(jj++, Types.VARCHAR);
	    }
	    else
		pstmt.setString(jj++, city);
	    if(state.isEmpty()){
		pstmt.setNull(jj++, Types.VARCHAR);
	    }
	    else
		pstmt.setString(jj++, state);
	    if(zip.isEmpty()){
		pstmt.setNull(jj++, Types.VARCHAR);
	    }
	    else
		pstmt.setString(jj++, zip);	    
	    if(inactive.isEmpty())
		pstmt.setNull(jj++, Types.CHAR);
	    else
		pstmt.setString(jj++, "y");						
	}
	catch(Exception ex){
	    msg += " "+ex;
	    logger.error(msg);
	}
	return msg;
    }
    public String doUpdate(){
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = " update addresses set employee_id=?, line_1=?,line_2=?,city=?,state=?,zip=?, inactive=? where id=?";
	if(employee_id.equals("")){
	    msg = "Employee id is required";
	    return msg;
	}
	try{
	    con = UnoConnect.getConnection();
	    if(con == null){
		msg = "Could not connect to DB ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    msg = setParams(pstmt);
	    pstmt.setString(8, id);
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
