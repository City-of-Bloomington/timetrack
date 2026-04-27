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
    //
    String dept_ref_id=""; // dept referance in NW app, one or more values
    String rate_date = "";
    Hashtable<String, String> empHash = null;
    Hashtable<String, Double> empOldRates = null;        
    Hashtable<String, Double> empNewRates = null;
    //
    // accrual values from New World (Carry Over)
    //
    public HandleEmpPayRate(){
    }
    // new
    public HandleEmpPayRate(String val){
	setRateDate(val);
    }
    public HandleEmpPayRate(String val,
			   String val2){
	setDept_ref_id(val);
	setRateDate(val2);
    }    
    //
    // setters
    //
    public void setDept_ref_id(String val){
	if(val != null){		
	    dept_ref_id = val;
	}
    }
    public void setRateDate(String val){
	if(val != null){		
	    rate_date = val;
	}
    }
        
    private String prepareEmployee(){
	String msg = "";
	EmployeeList empl = new EmployeeList();
	if(!dept_ref_id.isEmpty()){
	    empl.setDept_ref_id(dept_ref_id);
	}
	empl.setHasEmployeeNumber();
	msg = empl.find();
	if(msg.isEmpty()){
	    List<Employee> emps = empl.getEmployees();
	    if(emps != null && emps.size() > 0){
		empHash = new Hashtable<>();
		empNewRates = new Hashtable<>();		
		for(Employee one:emps){
		    // System.err.println(" emp "+one.getId()+","+one.getEmployee_number());
		    empHash.put(one.getEmployee_number(), one.getId());
		}
	    }
	}
	EmpPayRateList epl = new EmpPayRateList();
	msg = epl.findLatest();
	if(msg.isEmpty()){
	    empOldRates = epl.getRateHash();
	}
	else{
	    logger.error(msg);
	    System.err.println(msg);
	}
	return msg;
    }
    //
    public String process(){
		
	Connection con = null;
	PreparedStatement pstmt = null;
	CallableStatement ps = null;
	ResultSet rs = null;
	String msg="", date="";
	double rate = 0;
	//
	//
	String qq = "{CALL HR.HRReport_EmployeePayRateReport(null,'0',null,?,null,'3,1,2',2,0,1,0,0,3,0)}";
	if(dept_ref_id.isEmpty()){
	    qq = "{CALL HR.HRReport_EmployeePayRateReport(null,'0',null,null,null,'3,1,2',2,0,1,0,0,3,0)}";
	}
	// if rate_date is given
	if(!rate_date.isEmpty()){
	    date = Helper.getYymmddDate2(rate_date);
	    System.err.println(" date "+date);
	    qq = "{CALL HR.HRReport_EmployeePayRateReport('"+date+"','0',null,?,null,'3,1,2',2,0,1,0,0,3,0)}";
	    if(dept_ref_id.isEmpty()){
	    qq = "{CALL HR.HRReport_EmployeePayRateReport('"+date+"','0',null,null,null,'3,1,2',2,0,1,0,0,3,0)}";
	    }
	}
	msg = prepareEmployee();
	if(!msg.isEmpty()){
	    return msg;
	}
	logger.debug(qq);
	/**
	if(dept_ref_id.isEmpty()){
	    msg = "Dept or date not set ";
	    return msg;
	}
	*/
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
	    if(!dept_ref_id.isEmpty()){
		ps.setString(1, dept_ref_id);
	    }
	    rs = ps.executeQuery();
	    while(rs.next()){
		String str = rs.getString(5); // 5 employee number
		String str2 = rs.getString(6); // 6 name
		double str3 = rs.getDouble(13);// 9 current rate
		// String str4 = rs.getString(20); // current annula
		if(empHash != null && empHash.containsKey(str)){
		    double old_rate = 0;
		    String emp_id = empHash.get(str);
		    if(empOldRates != null && empOldRates.containsKey(emp_id)){
		       old_rate = empOldRates.get(emp_id);
		    }
		    if(str3 != old_rate){
			empNewRates.put(emp_id, str3);
		    }
		}
	    }
	    EmployeePayRate empPayRate = new EmployeePayRate(rate_date);
	    msg = empPayRate.doSaveBatch(empNewRates);
	    //
	}
	catch (Exception ex) {
	    logger.error(ex+":"+qq);
	    msg += ex;
	}
	finally{
	    Helper.databaseDisconnect(ps, rs);
	    // SingleConnect.disconnect();
	}
	return msg;
    }
    public String initialStartProcess(){
	String curDate = Helper.getToday();	
	String star_year = "01/01/2026";
	String init_date = "01/04/2026";
	curDate = Helper.getYymmddDate2(curDate);
	String msg = prepareEmployee();
	// run this first
	// msg = initailStart(start_year);
	// run this next
	// msg = initailStart(init_date);
	String next_date = "04/19/2026";//init_date;
	String date_ff = Helper.getYymmddDate2(next_date);
	int jj = 1;
	while(date_ff.compareTo(curDate) < 0){ // 4/26
	    next_date = Helper.getDateFrom(next_date, 7);
	    date_ff = Helper.getYymmddDate2(next_date);
	    System.err.println(" date "+date_ff);
	    msg = initailStart(next_date, date_ff);
	    if(!msg.isEmpty()){
		System.err.println(" Error "+msg);
	    }
	    msg = prepareEmployee();
	    jj++;
	    if(jj > 3) break;
	}
	return msg;
    }
    String initailStart(String init_date, String date_ff){
		
	Connection con = null;
	PreparedStatement pstmt = null;
	CallableStatement ps = null;
	ResultSet rs = null;
	String msg="", date="";
	double rate = 0;
	//
	// all dept
	
	String qq = "";
	date = date_ff;
	if(!date.isEmpty()){
	    // date = Helper.getYymmddDate2(init_date);
	    // System.err.println(" date "+date);
	    qq = "{CALL HR.HRReport_EmployeePayRateReport('"+date+"','0',null,null,null,'3,1,2',2,0,1,0,0,3,0)}";
	}
	logger.debug(qq);
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
	    if(!dept_ref_id.isEmpty()){
		ps.setString(1, dept_ref_id);
	    }
	    rs = ps.executeQuery();
	    while(rs.next()){
		String str = rs.getString(5); // 5 employee number
		String str2 = rs.getString(6); // 6 name
		double str3 = rs.getDouble(13);// 9 current rate
		// String str4 = rs.getString(20); // current annula
		if(empHash != null && empHash.containsKey(str)){
		    double old_rate = 0;
		    String emp_id = empHash.get(str);
		    if(empOldRates != null && empOldRates.containsKey(emp_id)){
		       old_rate = empOldRates.get(emp_id);
		    }
		    if(str3 != old_rate){
			empNewRates.put(emp_id, str3);
		    }
		}
	    }
	    EmployeePayRate empPayRate = new EmployeePayRate(init_date);
	    msg = empPayRate.doSaveBatch(empNewRates);
	    //
	}
	catch (Exception ex) {
	    logger.error(ex+":"+qq);
	    msg += ex;
	}
	finally{
	    Helper.databaseDisconnect(ps, rs);
	    // SingleConnect.disconnect();
	}
	return msg;
    }    

    

    /**
       finding the latest date and related dates in a table
       


/////////////////////
// this worked

SELECT t1.*
FROM employee_pay_rates t1
INNER JOIN (
    SELECT employee_id, MAX(rate_date) as max_date
    FROM employee_pay_rates
    GROUP BY employee_id
) t2 ON t1.employee_id = t2.employee_id 
     AND t1.rate_date = t2.max_date;



     */
}
