package in.bloomington.timer.list;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */

import java.util.*;
import java.sql.*;
import java.io.*;
import java.text.*;
import java.util.ArrayList;
import java.util.List;
import in.bloomington.timer.util.Helper;
import in.bloomington.timer.util.UnoConnect;
import in.bloomington.timer.bean.Holiday;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HolidayList{

    boolean debug;
    String year = "", date_from="", date_to="", pay_period_id="";
    static final long serialVersionUID = 54L;
    static Logger logger = LogManager.getLogger(HolidayList.class);
    static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    List<Holiday> holidays = null;
    //
    // this contains only dates
    Set<String> allSet = null;
    Map<String, String> holidayMap = null;
    boolean inAltPayPeriodSet = false;
    //
    // basic constructor
    public HolidayList(boolean deb){

	debug = deb;
	//
	// initialize
	//
    }
    public HolidayList(String val){
	//
	// initialize
	//
	setYear(val);
    }
    public HolidayList(String val, String val2){
	//
	// initialize
	//
	setDate_from(val);
	setDate_to(val2);		
    }	
    //
    public void setYear(String val){
	if(val != null)
	    year = val;
    }
		
    public void setDate_from(String val){
	if(val != null)
	    date_from = val;
    }
    public void setDate_to(String val){
	if(val != null)
	    date_to = val;
    }
    public void setPay_period_id(String val){
	if(val != null)
	    pay_period_id = val;
    }
    public void isInAltPayPeriodSet(){
	inAltPayPeriodSet = true;
    }
    public List<Holiday> getHolidays(){
	return holidays;
    }
    public boolean isHoliday(String date){
	if(holidayMap != null){
	    return holidayMap.containsKey(date);
	}
	return false;
    }
    public String getHolidayName(String date){
	String str = "";
	if(holidayMap != null){
	    if(holidayMap.containsKey(date)){
		str = holidayMap.get(date);
	    }
	}
	return str;
    }
    //
    // find all matching records
    // return "" or any exception thrown by DB
    //
    public String find(){
	//
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
		
	String qq = "select h.id,date_format(h.date,'%m/%d/%Y'),h.description from "+
	    " holidays h", qw = "";
	if(!year.isEmpty()){
	    qw += " year(h.date) = ? ";
	}
	else{
	    if(!pay_period_id.isEmpty()){
		if(inAltPayPeriodSet){
		    qq += ", pay_periods_alt p ";
		}
		else{
		    qq += ", pay_periods p ";
		}
		if(!qw.isEmpty()) qw += " and ";				
		qw += " p.id = ? and h.date >= p.start_date and h.date <= p.end_date ";
	    }
	    else {
		if(!date_from.isEmpty()){
		    qw += " h.date >= ? ";
		}
		if(!date_to.isEmpty()){
		    if(!qw.isEmpty()) qw += " and ";
		    qw += " h.date <= ? ";
		}
	    }
	}
	if(!qw.isEmpty()){
	    qq += " where "+qw;
	}
	qq += " order by date ";
	String back = "";
	con = UnoConnect.getConnection();				
	if(con == null){
	    back = "Could not connect to DB ";
	    return back;
	}
	try{
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);
	    int jj = 1;
	    if(!year.isEmpty()){
		pstmt.setString(jj,year);

	    }
	    else{
		if(!pay_period_id.isEmpty()){
		    pstmt.setString(jj,pay_period_id);
		}
		else{
		    if(!date_from.isEmpty()){
			pstmt.setDate(jj++, new java.sql.Date(dateFormat.parse(date_from).getTime()));						
		    }
		    if(!date_to.isEmpty()){
			pstmt.setDate(jj++, new java.sql.Date(dateFormat.parse(date_to).getTime()));							
		    }
		}
	    }
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		String str  = rs.getString(1);
		String str2 = rs.getString(2);
		String str3 = rs.getString(3);
		// allSet.add(str2);
		if(holidays == null)
		    holidays = new ArrayList<>();
		if(holidayMap == null)
		    holidayMap =  new Hashtable<>();
		holidayMap.put(str2, str3);
		Holiday one = new Holiday(debug, str, str2, str3);
		holidays.add(one);
	    }
	}
	catch(Exception ex){
	    back += ex;
	    logger.error(ex+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(pstmt, rs);
	    UnoConnect.databaseDisconnect(con);
	}
	return back;
    }
}






















































