package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.*;
import java.sql.*;
import java.text.*;
import javax.naming.*;
import javax.naming.directory.*;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//
public class PayPeriod implements Serializable{

    static final long serialVersionUID = 2900L;
    static Logger logger = LogManager.getLogger(PayPeriod.class);
    String id="", start_date="", end_date="", date="";
    int startYear =0,startMonth=0,startDay=0;
    int endYear =0,endMonth=0,endDay=0, days=14;
    int start_date_int = 0, end_date_int = 0;
    // these dates are needed for string comparison
    // to be in yyyy-mm-dd format 
    String startDateYmd="", endDateYmd=""; 
    public static final String[] allMonths =
    {"January","February","March","April","May","June",
     "July","August","September","October","November","December"};
    public static final String[] allMonthsShort =
    {"Jan","Feb","Mar","Apr","May","Jun",
     "Jul","Aug","Sep","Oct","Nov","Dec"};		
    static SimpleDateFormat dateFormat = Helper.dateFormat;
    boolean inAltPayPeriodSet = false;
    public PayPeriod(){
    }
    public PayPeriod(String val){
	setId(val);
    }		
    public PayPeriod(int day, int month, int year){
	setDate(day, month, year);
    }
    public PayPeriod(String val, String val2, String val3){
	setId(val);
	setStartDate(val2);
	setEndDate(val3);
    }
    public PayPeriod(String val, String val2, String val3,
		     int val4, int val5, int val6,
		     int val7, int val8, int val9, int val10,
		     String val11, String val12){
	setVals(val, val2, val3, val4, val5, val6, val7, val8, val9,
		val10, val11, val12);
    }
    void setVals(String val, String val2, String val3,
		 int val4, int val5, int val6,
		 int val7, int val8, int val9,
		 int val10,
		 String val11, String val12){
	setId(val);
	setStartDate(val2);
	setEndDate(val3);
	setStartYear(val4);
	setStartMonth(val5);
	setStartDay(val6);
	setEndYear(val7);
	setEndMonth(val8);
	setEndDay(val9);
	setDays(val10);
	setStartDateYmd(val11);
	setEndDateYmd(val12);
	setIntDates();
    }
    //
    // getters
    //
    public String getDate(){
	return date;
    }
    public String getId(){
	return id;
    }
    public String getStart_date(){
	return start_date;
    }
    public String getEnd_date(){
	return end_date;
    }
    public String getStartDate(){
	return start_date;
    }
    public String getEndDate(){
	return end_date;
    }		
    public String getStartDateYmd(){
	return startDateYmd;
    }
    public String getEndDateYmd(){
	return endDateYmd;
    }		
    public int[] getStartDateInt(){
	int[] ret = {startMonth,startDay,startYear};
	return ret;
    }
    public String getStartDateText(){
	return allMonths[startMonth-1]+" "+startDay+" "+startYear;
    }
    public String getEndDateText(){
	return allMonths[endMonth-1]+" "+endDay+" "+endYear;
    }	
    public int[] getEndDateInt(){
	int[] ret = {endMonth,endDay,endYear};
	return ret;
    }
    public int getDays(){
	return days;
    }
    //
    // setters
    //
    public void setId (String val){
	if(val != null)
	    id = val;
    }		
    public void setDate (String val){
	if(val != null)
	    date = val;
    }
    public void setDate (int day, int month, int year){
	date = month+"/"+day+"/"+year;
    }
    public void setStartDate (String val){
	if(val != null)
	    start_date = val;
    }
    public void setEndDate (String val){
	if(val != null)
	    end_date = val;
    }		
    public void setStartDateYmd (String val){
	if(val != null)
	    startDateYmd = val;
    }
    public void setEndDateYmd (String val){
	if(val != null)
	    endDateYmd = val;
    }		
    public void setStartYear (int val){
	startYear = val;
    }
    public void setStartMonth(int val){
	startMonth = val;
    }
    public void setStartDay(int val){
	startDay = val;
    }
    public void setEndYear (int val){
	endYear = val;
    }
    public void setEndMonth(int val){
	endMonth = val;
    }
    public void setEndDay(int val){
	endDay = val;
    }
    public void setDays(int val){
	if(val > 0)
	    days = val;
    }		
    public int getStartYear(){
	return startYear;
    }
    public String toString(){
	return getDateRange();
    }
    public String getStartMonthName(){
	return allMonths[startMonth];
    }
    public String getMonthNames(){
	if(startMonth == endMonth){
	    return allMonths[startMonth-1];
	}
	return allMonths[startMonth-1]+"/"+allMonths[endMonth-1];
    }
    public void setInAltPayPeriodSet(){
	inAltPayPeriodSet = true;
    }
    //
    // Note: to test end of year timewarp, data input must be done first
    //       
    // if we have two different years, means we are
    // at the end of the year pay period, this is handled
    // by special case
    //
    public boolean hasTwoDifferentYears(){
	return startYear < endYear;
	// return true; // test for new year
    }
    //
 // Note: to test end of year timewarp, data input must be done first    
    //
    // this is needed in December (31 days) only
    // to find the split day for the end of the year
    // pay period
    public int getDaysToYearEnd(){
	// return 31 - startDay + 1;
	return 9;
    }
    //
     // Note: to test end of year timewarp, data input must be done first
    /*
     * for the end of the year, the pay period is divided
     * into two ranges from (start_date, to 12/31/first year)
     * and (01/01/second year to end_date)
     */
    public String getFirstPayEndDate(){
	String ret = "";
	if(hasTwoDifferentYears()){
	    // ret = "11/19/2024"; // test
	    ret = "12/31/"+startYear;
	}
	else{
	    ret = end_date;
	}
	return ret;
    }
		
    public String getDateRange(){
	return start_date+" - "+end_date;
    }
    // needed for comparison
    private void setIntDates(){
	start_date_int = startYear*10000+startMonth*100+startDay;
	end_date_int = endYear*10000+endMonth*100+endDay;
    }
    // date is in yyy-mm-dd format
    // any date to be in between the comparison
    // must be start_date <= date <= end_date
    public boolean isDateWithin(String date){
	if(date == null) return false;
	String date2 = date.trim();
	int date_int = 0;
	if(date2.indexOf("-") > -1){
	    date2 = date2.replace("-","");
	    try{
		date_int = Integer.parseInt(date2);
	    }
	    catch(Exception ex){
		System.err.println(ex);
	    }
	}
	return date_int >= start_date_int && date_int <= end_date_int;
    }
    // something like 08/02 - 08/09
    public String getWeek1DateRange(){
	String endWeek1 = Helper.getDateAfter(start_date, 6);
	endWeek1 = endWeek1.substring(0,endWeek1.lastIndexOf("/"));
	String startWeek1 = start_date.substring(0,start_date.lastIndexOf("/"));
	String ret = startWeek1+" - "+endWeek1;
	return ret;
    }
    public String getWeek2DateRange(){
	String startWeek2 = Helper.getDateAfter(start_date, 7);
	startWeek2 = startWeek2.substring(0, startWeek2.lastIndexOf("/"));
	String endWeek2  = end_date.substring(0,end_date.lastIndexOf("/"));
	String ret = startWeek2+" - "+endWeek2;
	return ret;				
    }
    // we compate start_date with today and find if is more than two weeks
    // in the future
    public boolean isTwoWeekOrMoreInFuture(){
	String today = Helper.getToday();
	int days = findDateDiffWithDate(today);
	return days >= 14;
    }
    public boolean isTodayFirstDayOfPayPeriod(){
	String today = Helper.getToday();
	return start_date.equals(today);
    }

    public int findDateDiffWithDate(String date){
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", str="";
	int days = 0;
	String qq = "select  "+
	    "datediff(p.start_date,?) ";
	if(inAltPayPeriodSet){
	    qq += "from pay_periods_alt p where id=?";
	}
	else{
	    qq += "from pay_periods p where id=?";
	}
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = " could not connect to Database ";
	    logger.error(msg);
	    return days;
	}
	logger.debug(qq);
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setDate(1, new java.sql.Date(dateFormat.parse(date).getTime()));
	    pstmt.setString(2, id);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		days = rs.getInt(1);
	    }
	    else{
		msg = "No pay period found";
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
	return days;
    }
    public String find(){
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = "select p.id,"+
	    "date_format(p.start_date,'%m/%d/%Y'), "+
	    "date_format(p.end_date,'%m/%d/%Y'), "+
	    "year(p.start_date),month(p.start_date),day(p.start_date),"+
	    "year(p.end_date),month(p.end_date),day(p.end_date), "+
	    "datediff(p.end_date,p.start_date) ";
	if(inAltPayPeriodSet){
	    qq += "from pay_periods_alt p where ";

	}
	else{
	    qq += "from pay_periods p where ";
	}
	if(!id.isEmpty()){
	    return doSelect();
	}
	qq += " ? between p.start_date and p.end_date ";
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = " could not connect to Database ";
	    logger.error(msg);
	    return msg;
	}
	logger.debug(qq);
	try{
	    pstmt = con.prepareStatement(qq);
	    if(!id.isEmpty()){
		pstmt.setString(1, id);
	    }
	    else{
		pstmt.setDate(1, new java.sql.Date(dateFormat.parse(date).getTime()));
	    }
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		setId(rs.getString(1));
		setStartDate(rs.getString(2));
		setEndDate(rs.getString(3));
		setStartYear(rs.getInt(4));
		setStartMonth(rs.getInt(5));
		setStartDay(rs.getInt(6));
		setEndYear(rs.getInt(7));
		setEndMonth(rs.getInt(8));
		setEndDay(rs.getInt(9));
		setDays(rs.getInt(10));
		setIntDates();
	    }
	    else{
		msg = "No pay period found";
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
    public boolean findByEndDate(String date){
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", str="";
	boolean pass = true;
	String qq = "select p.id,"+
	    "date_format(p.start_date,'%m/%d/%Y'), "+
	    "date_format(p.end_date,'%m/%d/%Y'), "+
	    "year(p.start_date),month(p.start_date),day(p.start_date),"+
	    "year(p.end_date),month(p.end_date),day(p.end_date), "+
	    "datediff(p.end_date,p.start_date) ";
	if(inAltPayPeriodSet){
	  qq += "from pay_periods_alt p ";
	}
	else{
	  qq += "from pay_periods p ";
	}
	qq += "where p.end_date = ? ";
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = " could not connect to Database ";
	    logger.error(msg);
	    return false;
	}
	logger.debug(qq);
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setDate(1, new java.sql.Date(dateFormat.parse(date).getTime()));
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		setId(rs.getString(1));
		setStartDate(rs.getString(2));
		setEndDate(rs.getString(3));
		setStartYear(rs.getInt(4));
		setStartMonth(rs.getInt(5));
		setStartDay(rs.getInt(6));
		setEndYear(rs.getInt(7));
		setEndMonth(rs.getInt(8));
		setEndDay(rs.getInt(9));
		setDays(rs.getInt(10));
		setIntDates();
	    }
	    else{
		pass = false;
		logger.error("No pay period found");
	    }
	}
	catch(Exception ex){
	    msg += " "+ex;
	    logger.error(msg+":"+qq);
	    pass = false;
	}
	finally{
	    Helper.databaseDisconnect(pstmt, rs);
	    UnoConnect.databaseDisconnect(con);
	}
	return pass;
    }		
    public String doSelect(){
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = "select p.id,"+
	    "date_format(p.start_date,'%m/%d/%Y'), "+
	    "date_format(p.end_date,'%m/%d/%Y'), "+
	    "year(p.start_date),month(p.start_date),day(p.start_date),"+
	    "year(p.end_date),month(p.end_date),day(p.end_date), "+
	    "p.start_date,p.end_date ";
	if(inAltPayPeriodSet){
	    qq +=" from pay_periods_alt p ";
	}
	else{
	    qq +=" from pay_periods p ";
	}
	qq += " where id=?";
	if(id.isEmpty()){
	    msg = " id not set ";
	    logger.error(msg);
	    return msg;
	}					
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = " could not connect to Database ";
	    logger.error(msg);
	    return msg;
	}
	logger.debug(qq);
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, id);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		setId(rs.getString(1));
		setStartDate(rs.getString(2));
		setEndDate(rs.getString(3));
		setStartYear(rs.getInt(4));
		setStartMonth(rs.getInt(5));
		setStartDay(rs.getInt(6));
		setEndYear(rs.getInt(7));
		setEndMonth(rs.getInt(8));
		setEndDay(rs.getInt(9));
		setStartDateYmd(rs.getString(10));
		setEndDateYmd(rs.getString(11));
		setIntDates();
	    }
	    else{
		msg = "No pay period found";
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
       create table pay_periods_alt(
       id int(10) unsigned NOT NULL AUTO_INCREMENT,
      start_date date DEFAULT NULL,
      end_date date DEFAULT NULL,
      PRIMARY KEY (id)
         ) ENGINE=InnoDB  ;
	 
insert into pay_periods_alt select id,start_date,end_date from pay_periods;
insert into pay_periods_alt select id,date_sub(start_date,INTERVAL 1 DAY),date_sub(end_date,INTERVAL 1 DAY) from pay_periods;
;;
;; transition date on test server 05/25/2025
;;
update pay_periods_alt set end_date=date_sub(end_date,INTERVAL 1 DAY)
where start_date >= '2025-05-25';
update pay_periods_alt set start_date=date_sub(start_date,INTERVAL 1 DAY)
where start_date >= '2025-05-25';



     */
}
