package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.sql.*;
import java.util.Hashtable;
import java.util.Set;
import javax.naming.*;
import javax.naming.directory.*;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WeeklyEmployeeRate{

    static final long serialVersionUID = 3700L;	
    static Logger logger = LogManager.getLogger(WeeklyEmployeeRate.class);
    String id="", 
	pay_period_id="",
	week_no="", // 1, 2
	employee_id="";
    double pay_rate = 0;
    //
    public WeeklyEmployeeRate(){
	super();
    }
    public WeeklyEmployeeRate(String val){
	//
	setId(val);
    }
    public WeeklyEmployeeRate(String val, String val2){
	//
	setPayPeriod_id(val);
	setWeekNo(val2);
    }    
    public WeeklyEmployeeRate(String val, String val2, String val3, Double val4){
	// // new record

	setPayPeriod_id(val);
	setWeekNo(val2);	
	setEmployee_id(val3);
	setPayRate(val4);
    }    
    public WeeklyEmployeeRate(String val, String val2, String val3, String val4, Double val5){
	//
	// initialize
	//
	setId(val);
	setPayPeriod_id(val2);
	setWeekNo(val3);	
	setEmployee_id(val4);
	setPayRate(val5);
    }

    public boolean equals(Object obj){
	if(obj instanceof WeeklyEmployeeRate){
	    WeeklyEmployeeRate one =(WeeklyEmployeeRate)obj;
	    return id.equals(one.getId());
	}
	return false;				
    }
    public int hashCode(){
	int seed = 29;
	if(!id.equals("")){
	    try{
		seed += Integer.parseInt(id);
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
    public String getWeekNo(){
	return week_no;
    }
    public String getEmployee_id(){
	return employee_id;
    }
    public String getPayPeriod_id(){
	return pay_period_id;
    }
    public Double getPayRate(){
	return pay_rate;
    }
    //
    // setters
    //
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setWeekNo(String val){
	if(val != null)
	    week_no = val;
    }
    public void setEmployee_id(String val){
	if(val != null)
	    employee_id = val;
    }
    public void setPayPeriod_id(String val){
	if(val != null)
	   pay_period_id = val;
    }		
    public void setPayRate(Double val){
	if(val != null)
	   pay_rate = val;
    }	
    public String toString(){
	return employee_id+": "+pay_period_id+": "+week_no+" "+pay_rate;
    }
    //
    public String doSelect(){
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "select id,pay_period_id,week_no,employee_id,pay_rate "+
	    "from weekly_employee_rates where id=?";
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
		setPayPeriod_id(rs.getString(2));
		setWeekNo(rs.getString(3));
		setEmployee_id(rs.getString(4));
		setPayRate(rs.getDouble(5));
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
	String qq = " insert into weekly_employee_rates values(0,?,?,?,?)";
	if(employee_id.equals("")){
	    msg = "Employee id is required";
	    return msg;
	}
	if(pay_period_id.equals("")){
	    msg = "pay_period id is required";
	    return msg;
	}
	if(week_no.equals("")){
	    msg = "week number is required";
	    return msg;
	}
	if(pay_rate == 0){
	    msg = "pay rate is required";
	    return msg;
	}
	try{
	    con = UnoConnect.getConnection();
	    if(con == null){
		msg = "Could not connect to DB ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, pay_period_id);
	    pstmt.setString(2, week_no);
	    pstmt.setString(3, employee_id);
	    pstmt.setDouble(4, pay_rate);
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
    public String doSaveBatch(Hashtable<String, Double> empHash){
	Connection con = null;
	PreparedStatement pstmt = null, pstmt2=null, pstmt3=null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = " select id from weekly_employee_rates where pay_period_id=? and employee_id=? and week_no=? ";
	String qq2 = " update weekly_employee_rates set pay_rate = ? where id=? ";
	String qq3 = " insert into weekly_employee_rates values(0,?,?,?,?)";
	if(pay_period_id.equals("")){
	    msg = "pay_period id is required";
	    return msg;
	}
	if(week_no.equals("")){
	    msg = "week number is required";
	    return msg;
	}
	try{
	    con = UnoConnect.getConnection();
	    if(con == null){
		msg = "Could not connect to DB ";
		return msg;
	    }
	    pstmt  = con.prepareStatement(qq);
	    pstmt2 = con.prepareStatement(qq2);
	    pstmt3 = con.prepareStatement(qq3);
	    Set<String> keys = empHash.keySet();
	    for(String emp_id:keys){
		double pay_rate = empHash.get(emp_id);
		String rec_id="";
		pstmt.setString(1, pay_period_id);
		pstmt.setString(2, emp_id);		    
		pstmt.setString(3, week_no);
		rs = pstmt.executeQuery();
		if(rs.next()){
		    qq = qq2;
		    rec_id=rs.getString(1);
		    pstmt2.setDouble(1,pay_rate);
		    pstmt2.setString(2, rec_id);
		    pstmt2.executeUpdate();
		}
		else{
		    qq = qq3;
		    pstmt3.setString(1, pay_period_id);
		    pstmt3.setString(2, week_no);
		    pstmt3.setString(3, emp_id);
		    pstmt3.setDouble(4, pay_rate);
		    pstmt3.executeUpdate();
		}
	    }
	}
	catch(Exception ex){
	    msg += " "+ex;
	    logger.error(msg+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(rs, pstmt, pstmt2, pstmt3);
	    UnoConnect.databaseDisconnect(con);
	}
	return msg;
    }    

    public String doUpdate(){
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = " update weekly_employee_rates set pay_period_id=?, week_no=?,employee_id=?,pay_rate=? where id=?";
	if(employee_id.equals("")){
	    msg = "Employee id is required";
	    return msg;
	}
	if(pay_period_id.equals("")){
	    msg = "pay_period id is required";
	    return msg;
	}
	if(week_no.equals("")){
	    msg = "week number is required";
	    return msg;
	}
	try{
	    con = UnoConnect.getConnection();
	    if(con == null){
		msg = "Could not connect to DB ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, pay_period_id);
	    pstmt.setString(2, week_no);
	    pstmt.setString(3, employee_id);
	    pstmt.setDouble(4, pay_rate);
	    pstmt.setString(5, id);
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
       create table weekly_employee_rates(
       id int unsigned not null auto_increment,
       pay_period_id int unsigned,
       week_no int,
       employee_id int unsigned not null,
       pay_rate double (7,2),
       primary key(id),
       foreign key(employee_id) references employees(id),
       foreign key(pay_period_id) references pay_periods(id)
       )engine=InnoDB;


     */
}
