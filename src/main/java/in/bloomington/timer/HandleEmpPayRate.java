package in.bloomington.timer;
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
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HandleEmpPayRate{

    boolean debug = false;
    static final long serialVersionUID = 53L;
    static Logger logger = LogManager.getLogger(HandleEmpPayRate.class);
    static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    static DecimalFormat df = new DecimalFormat("#0.00");
    String week_no="", pay_period_id="", // date of last pay period
	dept_ref_id=""; // dept referance in NW app, one or more values
    Hashtable<String, String> empHash = null;
    Hashtable<String, Double> empHash2 = null;
    //
    // accrual values from New World (Carry Over)
    //
    public HandleEmpPayRate(){
    }
    public HandleEmpPayRate(String val,
			   String val2,
			   String val3){
	setDept_ref_id(val);
	setPayPeriod_id(val2);
	setWeekNo(val3);
    }
    //
    // setters
    //
    public void setDept_ref_id(String val){
	if(val != null){		
	    dept_ref_id = val;
	}
    }
    public void setPayPeriod_id(String val){
	if(val != null){		
	    pay_period_id = val;
	}
    }
    public void setWeekNo(String val){
	if(val != null){		
	    week_no = val;
	}
    }		
    private String prepareEmployee(){
	String msg = "";
	EmployeeList empl = new EmployeeList();
	empl.setDept_ref_id(dept_ref_id);
	empl.setHasEmployeeNumber();
	msg = empl.find();
	if(msg.isEmpty()){
	    List<Employee> emps = empl.getEmployees();
	    if(emps != null && emps.size() > 0){
		empHash = new Hashtable<>();
		empHash2 = new Hashtable<>();		
		for(Employee one:emps){
		    // System.err.println(" emp "+one.getId()+","+one.getEmployee_number());
		    empHash.put(one.getEmployee_number(), one.getId());
		}
	    }
	}
	return msg;
    }
    //
    public String process(){
		
	Connection con = null;
	PreparedStatement pstmt = null;
	CallableStatement ps = null;
	ResultSet rs = null;
	String msg="";
	double rate = 0;
	//
	//
	String qq = "{CALL HR.HRReport_EmployeePayRateReport(null,'0',null,?,null,'3,1,2',2,0,1,0,0,3,0)}";
	msg = prepareEmployee();
	if(!msg.isEmpty()){
	    return msg;
	}
	logger.debug(qq);
	if(dept_ref_id.isEmpty()){
	    msg = "Dept or date not set ";
	    return msg;
	}

	if(!msg.isEmpty() || empHash == null){
	    msg += " could not find related employees ";
	    return msg;
	}
	try{
	    con = SingleConnect.getNwConnection();
	    if(con == null){
		msg = " Could not connect to DB ";
		System.err.println(msg);
		logger.error(msg);
		return msg;
	    }
	    
	    ps = con.prepareCall(qq);
	    ps.setString(1, dept_ref_id);   // "16,17, 24" for two hr depts
	    rs = ps.executeQuery();
	    while(rs.next()){
		String str = rs.getString(5); // 5 employee number
		String str2 = rs.getString(6); // 6 name
		Double str3 = rs.getDouble(13);// 9 current rate
		String str4 = rs.getString(20); // current annula
		if(empHash.containsKey(str)){
		    String emp_id = empHash.get(str);
		    empHash2.put(emp_id, str3);
		}
	    }
	    WeeklyEmployeeRate weeklyEmpRate = new WeeklyEmployeeRate(pay_period_id, week_no);
	    msg = weeklyEmpRate.doSaveBatch(empHash2);
	    //
	}
	catch (Exception ex) {
	    logger.error(ex+":"+qq);
	    msg += ex;
	}
	finally{
	    Helper.databaseDisconnect(ps, rs);
	    SingleConnect.disconnect();
	}
	return msg;
    }

}
