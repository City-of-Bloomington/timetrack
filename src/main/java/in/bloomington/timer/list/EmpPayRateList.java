package in.bloomington.timer.list;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Hashtable;
import java.text.*;
import java.util.Date;
import java.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;

public class EmpPayRateList{

    static final long serialVersionUID = 1600L;
    static Logger logger = LogManager.getLogger(EmpPayRateList.class);
    String date="";
    List<EmployeePayRate> empRates = null;
    Hashtable<String, Double> rateHash = null;      
    public EmpPayRateList(){
    }
    public EmpPayRateList(String val){
	setDate(val);
    }
    public void setDate(String val){
	if(val != null)
	    date = val;
    }
		
    public List<EmployeePayRate> getEmpRates(){
	return empRates;
    }
    public Hashtable getRateHash(){
	return rateHash;
    }
    //
    // find
    //
    public String find(){
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = "select p.id,date_format(p.rate_date,'%m/%d/%Y'),p.employee_id,p.pay_rate "+
	    "from employee_pay_rates p ";
	String qw = "";
	if(!qw.isEmpty()){
	    qq += " where "+qw;
	}
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = " Could not connect to DB ";
	    logger.error(msg);
	    return msg;
	}
	logger.debug(qq);
	System.err.println(qq);
	try{
	    pstmt = con.prepareStatement(qq);
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		if(empRates == null)
		    empRates = new ArrayList<>();
		EmployeePayRate one = new EmployeePayRate(
				      rs.getString(1),
				      rs.getString(2),
				      rs.getString(3),
				      rs.getDouble(4));
		empRates.add(one);
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
    public String findLatest(){
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", str="";
	int thisYear = Helper.getCurrentYear();
	String end_year_date = ""+thisYear+"-12-31";
	String qq = "select t1.id,date_format(t1.rate_date,'%m/%d/%Y'),t1.employee_id,t1.pay_rate "+
	    " FROM employee_pay_rates t1 "+
	    " INNER JOIN ( "+
	    " SELECT employee_id, MAX(rate_date) as max_date "+
	    " FROM employee_pay_rates where rate_date < '"+end_year_date+"'"+
	    " GROUP BY employee_id "+
	    " ) t2 ON t1.employee_id = t2.employee_id "+ 
	    " AND t1.rate_date = t2.max_date";	
	String qw = "";
	if(!qw.isEmpty()){
	    qq += " where "+qw;
	}
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = " Could not connect to DB ";
	    logger.error(msg);
	    return msg;
	}
	logger.debug(qq);
	try{
	    pstmt = con.prepareStatement(qq);
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		if(rateHash == null)
		    rateHash = new Hashtable<>();
		rateHash.put(rs.getString(3),
			     rs.getDouble(4));

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

}
