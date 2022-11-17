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

public class HandleNwAccrual{

    boolean debug = false;
    static final long serialVersionUID = 53L;
    static Logger logger = LogManager.getLogger(HandleNwAccrual.class);
    static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    static DecimalFormat df = new DecimalFormat("#0.00");
    String date="", write_date="", // date of last pay period
	dept_ref_id=""; // dept referance in NW app, one or more values
    Hashtable<String, String> empHash = null;
    Hashtable<String, Double[]> empHashArr = null;
    //
    // accrual values from New World (Carry Over)
    //
    public HandleNwAccrual(){
    }
    public HandleNwAccrual(String val,
			   String val2,
			   String val3){
	setDept_ref_id(val);
	setDate(val2);
	setWriteDate(val3);
    }
    public HandleNwAccrual(boolean deb,
			   String val,
			   PayPeriod val2){
	debug = deb;
	setDept_ref_id(val);
	if(val2 != null){
	    setWriteDate(val2.getEnd_date()); // last day of pay period
	}
	/*
	  if(date.isEmpty() && !write_date.isEmpty()){
	  date = Helper.getDateAfter(end_date, 5);
	  }
	*/
    }	
    //
    // setters
    //
    public void setDept_ref_id(String val){
	if(val != null){		
	    dept_ref_id = val;
	}
    }
    public void setDate(String val){
	if(val != null){		
	    date = val;
	}
    }
    public void setWriteDate(String val){
	if(val != null){		
	    write_date = val;
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
		empHashArr = new Hashtable<>();
		for(Employee one:emps){
		    // System.err.println(" emp "+one);
		    empHash.put(one.getEmployee_number(), one.getId());
		    Double[] hrsArr = {0.,0.,0.,0.,0.,0.,0.,0.}; 
		    empHashArr.put(one.getEmployee_number(), hrsArr);
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
	double ptoa=0, cua=0, holya=0, scka=0, lba=0, vla=0, klv=0, pfla=0;

	/*
	 * 6 sick, 8 pto, 9 holiday comp, 15 comp
	 * 18 leave (police), 7 vacation (fire), 16 (kelly)
	 * 21 Paid family medical leave
	 */
	//
	//
	String qq = "{call HR.HRReport_AccrualBalance(?,?,?,null,?,null,1,1,null,0)} ";
	logger.debug(qq);
	if(dept_ref_id.isEmpty() || date.isEmpty()){
	    msg = "Dept or date not set ";
	    return msg;
	}
	msg = prepareEmployee();
	if(!msg.isEmpty() || empHash == null){
	    msg += " could not find related employees ";
	    return msg;
	}
	try{
	    SingleConnect sConnect = SingleConnect.getInstance();
	    con = SingleConnect.getNwConnection();
	    if(con == null){
		msg = " Could not connect to DB ";
		System.err.println(msg);
		logger.error(msg);
		return msg;
	    }
	    ps = con.prepareCall(qq);
	    ps.setString(1, dept_ref_id);   // "16,17, 24" for two hr depts
	    ps.setString(2, "6,8,9,15,18,21"); // sick, pto, holiday, comp, police leave, 21=paid family medical leave
	    ps.setInt(3,258);
	    ps.setString(4, date); // accruals upto end of last payperiod
	    rs = ps.executeQuery();
	    while(rs.next()){
		double arr[] = {0,0,0,0,0, 0,0,0};
		String str  = rs.getString(1); // employeeId
		String str2 = rs.getString(2); // employee num
		String str3 = rs.getString(3);  // full name
		str ="";
		scka = rs.getDouble(18);  // sick       id=2
		ptoa = rs.getDouble(19); // pto accrual id=1
		holya = rs.getDouble(20); // holiday earned id=4
		cua = rs.getDouble(21); // cte comp time earned id=3
		lba = rs.getDouble(22); // police leave
		pfla = rs.getDouble(23); // paid family medical leave
		arr[0] = ptoa;
		arr[1] = scka;
		arr[2] = cua;
		arr[3] = holya;
		arr[4] = lba; // police leave
		// arr[5] = vla; // fire
		// arr[6] = klv; // Kelly leave, fire
		arr[7] = pfla;// family medical leave
		if(empHash.containsKey(str2)){
		    str = empHash.get(str2);  // emp id
		    System.err.println("handleNwAccrual: id,emp_num,accruals "+ str+" "+str2+" "+str3+" "+ptoa+" "+scka+" "+holya+" "+cua+" "+lba+" "+pfla);
		    // sick bank
		    // EmployeeAccrual empa = new EmployeeAccrual(""+2,str,scka,end_date);
		    EmployeeAccrual empa = new EmployeeAccrual(str, write_date);
		    msg = empa.doSaveBatch(arr);
		    //
		}
		else{
		    System.err.println(" emp num not found "+str2);
		}
	    }
	}
	catch (Exception ex) {
	    logger.error(ex);
	    msg += ex;
	}
	finally{
	    Helper.databaseDisconnect(ps, rs);
	}
	return msg;
    }
    /**
     * because of MS sql problem not being able to get all the
     * accruals in one call, we split the sql into two groups
     * to get all the list 
     */
    public String processFire(){

	Connection con = null;
	CallableStatement ps = null;
	ResultSet rs = null;
	String msg="";
	double ptoa=0, cua=0, holya=0, scka=0, lba=0, vla=0, klv=0, pfla=0;

	/*
	 * 6 sick, 8 pto, 9 holiday comp, 15 comp
	 * 18 leave (police), 7 vacation (fire), 16 (kelly)
	 * 21 Paid family medical leave
	 */
	//
	//
	String qq = "{call HR.HRReport_AccrualBalance(?,?,?,null,?,null,1,1,null,0)} ";
	logger.debug(qq);
	if(dept_ref_id.isEmpty() || date.isEmpty()){
	    msg = "Dept or date not set ";
	    return msg;
	}
	msg = prepareEmployee();
	if(!msg.isEmpty() || empHash == null){
	    msg += " could not find related employees ";
	    return msg;
	}
	try{
	    SingleConnect sConnect = SingleConnect.getInstance();
	    con = SingleConnect.getNwConnection();
	    if(con == null){
		msg = " Could not connect to DB ";
		System.err.println(msg);
		logger.error(msg);
		return msg;
	    }
	    ps = con.prepareCall(qq);
	    ps.setString(1, dept_ref_id);   // "16,17, 24" for two hr depts
	    ps.setString(2, "6,8,9,15,21"); // sick, pto, holiday, comp, leave police, vacation,Kelly leave (fire), 21=paid family medical leave
	    
	    // ps.setString(2, "21,18,7,16"); // sick, pto, holiday, comp, leave, vacation,Kelly leave (fire), 21=paid family medical leave	    
	    ps.setInt(3,258);
	    ps.setString(4, date); // accruals upto end of last payperiod
	    rs = ps.executeQuery();
	    while(rs.next()){
		double arr[] = {0,0,0,0,0, 0,0,0};
		String str  = rs.getString(1); // employeeId
		String str2 = rs.getString(2); // employee num
		String str3 = rs.getString(3);  // full name
		str ="";
		scka = rs.getDouble(18);  // sick       id=2
		ptoa = rs.getDouble(19); // pto accrual id=1
		holya = rs.getDouble(20); // holiday earned id=4
		cua = rs.getDouble(21); // cte comp time earned id=3
		pfla = rs.getDouble(22); // paid family medical leave
		if(empHash.containsKey(str2)){
		    String emp_id = empHash.get(str2);  // emp id
		    Double[] hrsArr = empHashArr.get(str2);
		    hrsArr[0] = ptoa;
		    hrsArr[1] = scka;
		    hrsArr[2] = cua;
		    hrsArr[3] = holya;
		    hrsArr[7] = pfla;
		    // empHashArr.put(str2, hrsArr);
		    // System.err.println("handleNwAccrual: id,emp_num,accruals "+ emp_id+" "+str2+" "+str3+" "+ptoa+" "+scka+" "+holya+" "+cua+" "+lba+" "+vla+" "+klv+" "+pfla);
		}
		else{
		    System.err.println(" emp num not found "+str2);
		}
	    }
	    ps.setString(1, dept_ref_id);   // 
	    ps.setString(2, "7,16"); // vacation,Kelly leave (fire)
	    ps.setInt(3,258);
	    ps.setString(4, date); // accruals upto end of last payperiod
	    rs = ps.executeQuery();
	    while(rs.next()){
		double arr[] = {0,0,0,0,0, 0,0,0};
		String str  = rs.getString(1); // employeeId
		String str2 = rs.getString(2); // employee num
		String str3 = rs.getString(3);  // full name
		str ="";
		vla = rs.getDouble(18);
		klv = rs.getDouble(19);
		// arr[4] = lba; // police leave
		// arr[5] = vla; // fire
		// arr[6] = klv; // Kelly leave, fire
		if(empHash.containsKey(str2)){
		    String emp_id = empHash.get(str2);  // emp id
		    Double[] hrsArr = empHashArr.get(str2);
		    ptoa = hrsArr[0];
		    scka = hrsArr[1];
		    cua = hrsArr[2];
		    holya = hrsArr[3];
		    // 4 leave police skipped
		    pfla = hrsArr[7];
		    hrsArr[5] = vla;
		    hrsArr[6] = klv;
		    arr[0] = hrsArr[0];
		    arr[1] = hrsArr[1];
		    arr[2] = hrsArr[2];
		    arr[3] = hrsArr[3];
		    arr[4] = hrsArr[4];
		    arr[5] = hrsArr[5];
		    arr[6] = hrsArr[6];
		    arr[7] = hrsArr[7];			
		    System.err.println("handleNwAccrual: id,emp_num,accruals "+ emp_id+" "+str2+" "+str3+" "+ptoa+" "+scka+" "+holya+" "+cua+" "+lba+" "+vla+" "+klv+" "+pfla);

		    EmployeeAccrual empa = new EmployeeAccrual(emp_id, write_date);
		    msg = empa.doSaveBatch(arr);
		    if(!msg.isEmpty()){
			System.err.println(" error "+msg);
		    }
		    //
		}
		else{
		    System.err.println(" emp num not found "+str2);
		}
	    }	    
	}
	catch (Exception ex) {
	    logger.error(ex);
	    msg += ex;
	}
	finally{
	    Helper.databaseDisconnect(ps, rs);
	}
	return msg;
    }
    /**
       //
       // for testing only
       //
    public String processSpecial(String empNum){
		
	Connection con = null;
	PreparedStatement pstmt = null;
	CallableStatement ps = null;
	ResultSet rs = null;
	String msg="";
	double ptoa=0, cua=0, holya=0, scka=0, lba=0,vla=0;


	String qq = "{call HR.HRReport_AccrualBalance(?,?,?,null,?,null,1,1,null,0)} ";
	logger.debug(qq);
	if(dept_ref_id.isEmpty() || date.isEmpty()){
	    msg = "Dept or date not set ";
	    return msg;
	}
	try{
	    SingleConnect sConnect = SingleConnect.getInstance();
	    con = SingleConnect.getNwConnection();
	    if(con == null){
		msg = " Could not connect to DB ";
		System.err.println(msg);
		logger.error(msg);
		return msg;
	    }
	    ps = con.prepareCall(qq);
	    ps.setString(1, dept_ref_id);   // "16,17, 24" for two hr depts
	    ps.setString(2, "6,8,9,15,18,7"); // sick, pto, holiday, comp, leave , vacation
	    ps.setInt(3,258);
	    ps.setString(4, date); // accruals upto end of last payperiod
	    rs = ps.executeQuery();
	    while(rs.next()){
		String str  = rs.getString(1); // employeeId
		String str2 = rs.getString(2); // employee num
		// if(!str2.equals(empNum)) continue;
		String str3 = rs.getString(3);  // full name
		str ="";
		scka = rs.getDouble(18);  // sick       id=2
		ptoa = rs.getDouble(19); // pto accrual id=1
		holya = rs.getDouble(20); // holiday earned id=4
		cua = rs.getDouble(21); // cte comp time earned id=3
		lba = rs.getDouble(22);
		vla = rs.getDouble(23);
		System.err.println("emp accrual "+ empNum+" "+str2+" "+str3+" "+ptoa+" "+scka+" "+holya+" "+cua+" "+lba+" "+vla);
	    }
	}
	catch (Exception ex) {
	    logger.error(ex);
	    msg += ex;
	}
	finally{
	    Helper.databaseDisconnect(ps, rs);
	}
	return msg;
    }		
*/
}
