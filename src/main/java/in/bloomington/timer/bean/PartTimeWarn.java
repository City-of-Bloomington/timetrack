package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */

import java.util.*;
import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.time.temporal.WeekFields;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.sql.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.naming.*;
import javax.sql.*;
import javax.naming.directory.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.Employee;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class PartTimeWarn{
    static Logger logger = LogManager.getLogger(PartTimeWarn.class);
    final static long serialVersionUID = 292L;
    boolean debug = false;		
    String id="",
	job_id="";
    Integer warn_type = 1; // 1:week total warning, 2:Wednesday
    Integer pay_week_num = 1; // 1, 2
    Integer week_of_year = null;
    Double week_total = null;
    Integer critical_value = null; // week total or Wednesday critic value 
    boolean need_new_email = false;
    public PartTimeWarn(){
    }
    public PartTimeWarn(String val){
	if(val != null)
	    id = val;
    }
    public PartTimeWarn(
			String val,
			String val2,
			Integer val3,
			Integer val4,
			Integer val5,
			
			Double val6,
			Integer val7){
	setId(val);
	setJob_id(val2);
	setPayWeekNum(val3);
	setWarnType(val4);
	setWeekOfYear(val5);
	setWeekTotal(val6);
	setCriticalValue(val7);
    }	
    // for new record
    public PartTimeWarn(
			String val,
			Integer val2,
			Integer val3,
			Double val4,
			Integer val5
		    ){
	setJob_id(val);
	setPayWeekNum(val2);
	setWarnType(val3);
	setWeekTotal(val4);
	setCriticalValue(val5);
    }	
    public String getId(){
	return id;
    }
    public String getJob_id(){
	return job_id;
    }    
    public Integer getPayWeekNum(){
	return pay_week_num;
    }
    public Integer getWarnType(){
	return warn_type;
    }
    public Integer getWeekOfYear(){
	return week_of_year;
    }
    public Double getWeekTotal(){
	return week_total;
    }
    public Integer getCriticalValue(){
	return critical_value;
    }    
    //
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    
    public void setPayWeekNum(Integer val){
	if(val != null)
	    pay_week_num = val;
    }
    public void setWarnType(Integer val){
	if(val != null)
	    warn_type = val;
    }
    public void setWeekOfYear(Integer val){
	if(val != null)
	    week_of_year = val;	
    }    
    public void setJob_id(String val){
	if(val != null)
	    job_id = val;
    }    
    public void setWeekTotal(Double val){
	if(val != null)
	    week_total = val;	
    }
    public void setCriticalValue(Integer val){
	if(val != null)
	    critical_value = val;	
    }
    public boolean needNewEmail(){
	return need_new_email;
    }
    public String getWarnTypeText(){
	String ret = "Week Total Warning";
	if(warn_type == 2){
	    ret = "Wednesday Warning";
	}
	return ret;
    }
    private int findCurrentWeekOfYear(){
	// 
	// current week number in this year
	//
	LocalDate date = LocalDate.now();
	// Use WeekFields.ISO for the ISO-8601 standard (Monday as first day)
        WeekFields weekFields = WeekFields.ISO;
	// WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int localeWeekNumber = date.get(weekFields.weekOfWeekBasedYear());
	return localeWeekNumber;
	/**
	   for specific date
	   LocalDate specificDate = LocalDate.of(2025, 1, 1);
	   // Get the ISO week number
	   int weekNumber = specificDate.get(WeekFields.ISO.weekOfWeekBasedYear());
	   // M month
	   // d day
	   // uuuu year
	   DateTimeFormatter f = DateTimeFormatter.ofPattern( "M/d/uuuu" , Locale.US ) ;
	   LocalDate ld = LocalDate.parse( "1/23/2017" , f ) 


	 */
	
    }
		
    public String doSave(){
		
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null, pstmt2 = null, pstmt3=null;
	ResultSet rs = null;
	int cnt = 0;
	// first we check if we already warned the employee
	//
	String qq = " select pw.id,pl.id from part_time_warns pw "+
	    " left join part_time_email_logs pl on pw.id=pl.warn_id "+
	    " where pw.job_id=? "+
	    " and pw.pay_week_num = ? and pw.warn_type = ? and "+
	    " pw.week_of_year=? ";
	String qq2 = " insert into part_time_warns values(0,?,?,?,?,?,?) ";
	if(job_id.isEmpty()){
	    back = "Job not set ";
	    return back;
	}
	week_of_year = findCurrentWeekOfYear();
	con = UnoConnect.getConnection();
	if(con == null){
	    back = "Could not connect to Database ";
	    return back;
	}
	try{
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,job_id);
	    pstmt.setInt(2, pay_week_num);
	    pstmt.setInt(3, warn_type);
	    pstmt.setInt(4, week_of_year);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		id = rs.getString(1);
		String str = rs.getString(2);
		if(str == null){
		    need_new_email = true;
		}
	    }
	    else{
		qq = qq2;
		pstmt2 = con.prepareStatement(qq);		
		pstmt2.setString(1, job_id);
		pstmt2.setInt(2, pay_week_num);
		pstmt2.setInt(3, warn_type);
		pstmt2.setInt(4, week_of_year);
		pstmt2.setDouble(5, week_total);
		pstmt2.setInt(6, critical_value);
		pstmt2.executeUpdate();
		qq = "select LAST_INSERT_ID()";
		pstmt3 = con.prepareStatement(qq);
		rs = pstmt3.executeQuery();
		if(rs.next()){
		    id = rs.getString(1);
		}
		need_new_email = true;
	    }
	}catch(Exception ex){
	    back += ex+":"+qq;
	    logger.error(back);
	}
	finally{
	    Helper.databaseDisconnect(rs, pstmt, pstmt2, pstmt3);
	    UnoConnect.databaseDisconnect(con);
	}
	return back;
		
    }
    public String doSelect(){
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = " select id,job_id,pay_week_num,warn_type,week_of_year,week_total, critical_value from part_time_warns where id=? ";
	if(debug)
	    logger.debug(qq);
	con = UnoConnect.getConnection();
	if(con == null){
	    back = "Could not connect to DB ";
	    return back;
	}				
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,id);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		setId(rs.getString(1));
		setJob_id(rs.getString(2));
		setPayWeekNum(rs.getInt(3));
		setWarnType(rs.getInt(4));
		setWeekOfYear(rs.getInt(5));
		setWeekTotal(rs.getDouble(6));					
		setCriticalValue(rs.getInt(7));
	    }
	    else{
		back = "No match found";
	    }
	}catch(Exception ex){
	    back += ex+":"+qq;
	    logger.error(back);
	}
	finally{
	    Helper.databaseDisconnect(pstmt, rs);
	    UnoConnect.databaseDisconnect(con);
	}
	return back;
    }
    
    /**

    create table part_time_warns (
    id int unsigned not null auto_increment,
    job_id int unsigned,
    pay_week_num int,
    warn_type int,
    week_of_year int,
    week_total decimal(6,2),
    critical_value int,
    primary key(id),
    foreign key(job_id) references jobs(id)    
    )engine=InnoDB;

	    
     */
	
}























































