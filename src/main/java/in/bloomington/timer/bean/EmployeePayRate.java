package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.sql.*;
import java.util.Hashtable;
import java.util.Set;
import java.text.SimpleDateFormat;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EmployeePayRate{

    static final long serialVersionUID = 3700L;
    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");    
    static Logger logger = LogManager.getLogger(EmployeePayRate.class);
    String id="", 
	employee_id="";
    String rate_date = null;
    double pay_rate = 0;
    //
    public EmployeePayRate(){
	super();
    }
    public EmployeePayRate(String val){
	//
	setRateDate(val);
    }
    public EmployeePayRate(String val, String val2, Double val3){
	// new record

	setRateDate(val);	
	setEmployee_id(val2);
	setPayRate(val3);
    }    
    public EmployeePayRate(String val, String val2, String val3, Double val4){
	setId(val);
	setRateDate(val2);
	setEmployee_id(val3);
	setPayRate(val4);
    }

    public boolean equals(Object obj){
	if(obj instanceof EmployeePayRate){
	    EmployeePayRate one =(EmployeePayRate)obj;
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
    public String getRateDate(){
	return rate_date;
    }
    public String getEmployee_id(){
	return employee_id;
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
    public void setRateDate(String val){
	if(val != null)
	    rate_date = val;
    }
    public void setEmployee_id(String val){
	if(val != null)
	    employee_id = val;
    }
    public void setPayRate(Double val){
	if(val != null)
	   pay_rate = val;
    }	
    public String toString(){
	return employee_id+": "+rate_date+" "+pay_rate;
    }
    //
    public String doSelect(){
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "select id,date_format(rate_date,'%m/%d/%Y'),employee_id,pay_rate "+
	    "from employee_pay_rates where id=?";
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
		setRateDate(rs.getString(2));
		setEmployee_id(rs.getString(3));
		setPayRate(rs.getDouble(4));
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
	String qq = " insert into employee_pay_rates values(0,?,?,?)";
	if(employee_id.equals("")){
	    msg = "Employee id is required";
	    return msg;
	}
	if(rate_date.equals("")){
	    msg = "rate date is required";
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
	    java.util.Date date_tmp = df.parse(rate_date);
	    pstmt.setDate(1, new java.sql.Date(date_tmp.getTime()));
	    pstmt.setString(2, employee_id);
	    pstmt.setDouble(3, pay_rate);
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
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="";
	int this_year = Helper.getCurrentYear();
	String qq = " insert into employee_pay_rates values(0,?,?,?)";
	
	if(rate_date.equals("")){
	    msg = "date is required";
	    return msg;
	}
	try{
	    java.util.Date date_tmp = df.parse(rate_date);		    
	    con = UnoConnect.getConnection();
	    if(con == null){
		msg = "Could not connect to DB ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    Set<String> keys = empHash.keySet();
	    for(String emp_id:keys){
		pstmt.setDate(1, new java.sql.Date(date_tmp.getTime()));
		pstmt.setString(2, emp_id);
		pstmt.setDouble(3, empHash.get(emp_id));
		pstmt.executeUpdate();
	    }
	}
	catch(Exception ex){
	    msg += " "+ex;
	    logger.error(msg+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(rs, pstmt);
	    UnoConnect.databaseDisconnect(con);
	}
	return msg;
    }    
    public String doUpdate(){
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = " update employee_pay_rates set rate_date=?, employee_id=?,pay_rate=? where id=?";
	if(employee_id.equals("")){
	    msg = "Employee id is required";
	    return msg;
	}
	if(rate_date.equals("")){
	    msg = "rate_date is required";
	    return msg;
	}
	if(pay_rate == 0){
	    msg = "pay_rate is required";
	    return msg;
	}
	try{
	    con = UnoConnect.getConnection();
	    if(con == null){
		msg = "Could not connect to DB ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    java.util.Date date_tmp = df.parse(rate_date);
	    pstmt.setDate(1, new java.sql.Date(date_tmp.getTime()));
	    pstmt.setString(2, employee_id);
	    pstmt.setDouble(3, pay_rate);
	    pstmt.setString(4, id);
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

       create table employee_pay_rates(
       id int unsigned not null auto_increment,
       rate_date date,
       employee_id int unsigned not null,
       pay_rate double (7,2),
       primary key(id),
       foreign key(employee_id) references employees(id),
       index(rate_date)
       )engine=InnoDB;
     */
}
