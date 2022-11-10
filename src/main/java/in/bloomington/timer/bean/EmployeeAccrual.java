package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.Serializable;
import java.util.Set;
import java.util.HashSet;
import java.util.Hashtable;
import java.sql.*;
import java.text.SimpleDateFormat;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EmployeeAccrual extends CommonInc{
		
    static final long serialVersionUID = 100L;
    static Logger logger = LogManager.getLogger(EmployeeAccrual.class);
    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
    String id="", accrual_id="",employee_id="", date="",
	related_hour_code_id="";
    double hours = 0.0;
    Accrual accrual = null;
    HourCode hourCode = null;
    Employee employee = null;
    public EmployeeAccrual(){

    }
    public EmployeeAccrual(String val){
	setId(val);
    }
    // to save new record in batch
    public EmployeeAccrual(
			   String val, // emp_id
			   String val2){ // date for saving
	setEmployee_id(val);
	setDate(val2);
    }		
    public EmployeeAccrual(String val, // accrual_id
			   String val2, // emp_id
			   double val3, // hours
			   String val4){ // date for saving
	setAccrual_id(val);
	setEmployee_id(val2);
	setHours(val3);
	setDate(val4);
				
    }
    public EmployeeAccrual(EmployeeAccrual val){
	setId(val.getId());
	setAccrual_id(val.getAccrual_id());
	setEmployee_id(val.getEmployee_id());
	setHours(val.getHours());
	setDate(val.getDate());
	setRelated_hour_code_id(val.getRelated_hour_code_id());
    }		
    public EmployeeAccrual(String val,
			   String val2,
			   String val3,
			   String val4,
			   double val5,
			   String val6,
			   String val7,
			   String val8,
			   int val9,
			   String val10){
	setVals(val, val2, val3, val4, val5, val6, val7, val8, val9, val10);
    }
    void setVals(String val,
		 String val2,
		 String val3,
		 String val4,
		 double val5,
		 String val6,
		 String val7,
		 String val8,
		 int val9,
		 String val10
		 ){
	setId(val);
	setAccrual_id(val2);
	setRelated_hour_code_id(val3);
	setEmployee_id(val4);
	setHours(val5);
	setDate(val6);
	if(val7 != null){
	    accrual = new Accrual(accrual_id, val7, val8, val9);
	}
	if(val10 != null){
	    hourCode = new HourCode(related_hour_code_id, val10);
	}
    }		
    public String getId(){
	return id;
    }
    public String getRelated_hour_code_id(){
	return related_hour_code_id;
    }
    public String getAccrual_id(){
	return accrual_id;
    }
    public String getEmployee_id(){
	return employee_id;
    }
		
    public double getHours(){
	return hours;
    }
    public boolean hasValue(){
	return hours > 0;
    }
    public boolean isCompTimeRelated(){
	// we want to show this even if it has 0 value
	// needed for police when they earn comp time during the pay
	// period and they want to use it
	return accrual_id.equals("3");
	/**
	 // we may want to do the following to avoid accrual ID change
	 getAccrual();
	 return accraul != null && accrual.getName().equals("CUA");
	*/
    }
    public String getDate(){
	return date;
    }
    public void setId (String val){
	if(val != null)
	    id = val;
    }
    public HourCode getHourCode(){
	return hourCode;
    }
    public void setRelated_hour_code_id(String val){
	if(val != null)
	    related_hour_code_id = val;
    }		
    public void setAccrual_id (String val){
	if(val != null)
	    accrual_id = val;
    }
    public void setEmployee_id (String val){
	if(val != null)
	    employee_id = val;
    }
    public void setHours(double val){
	hours = val;
    }
    public void setDate(String val){
	if(val != null)
	    date = val;
    }
    public String toString(){
	String ret = getEmployee_id()+" "+getAccrual_id()+" "+getRelated_hour_code_id()+" "+getHours();
	return ret;
    }
    @Override
    public boolean equals(Object o) {
	if (o instanceof EmployeeAccrual) {
	    EmployeeAccrual c = (EmployeeAccrual) o;
	    if ( this.id.equals(c.getId()) &&
		 this.related_hour_code_id.equals(c.getRelated_hour_code_id()))
		return true;
	}
	return false;
    }
    @Override
    public int hashCode(){
	int seed = 31;
	if(!id.isEmpty()){
	    try{
		seed += Integer.parseInt(id)*43;
		if(!related_hour_code_id.isEmpty()){
		    seed += Integer.parseInt(related_hour_code_id)*31;
		}
	    }catch(Exception ex){
		// we ignore
	    }
	}
	return seed;
    }
		
    public Accrual getAccrual(){
	if(accrual == null && !accrual_id.isEmpty()){
	    Accrual one = new Accrual(accrual_id);
	    String back = one.doSelect();
	    if(back.isEmpty()){
		accrual = one;
	    }
	}
	return accrual;
    }
    public Employee getEmployee(){
	if(employee == null && !employee_id.isEmpty()){
	    employee = new Employee(employee_id);
	    String back = employee.doSelect();
	}
	return employee;
						
    }		
    public String doSelect(){
	//
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = "select a.id,a.accrual_id,ec.id,a.employee_id,a.hours,date_format(a.date,'%m/%d/%Y'),t.name,t.description,t.pref_max_level,ec.name from employee_accruals a join accruals t on t.id=a.accrual_id join hour_codes ec on ec.accrual_id=a.accrual_id where a.id=?";
	if(id.isEmpty()){
	    msg = "accrual id is not set";
	    return msg;
	}
	logger.debug(qq);
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = "Could not connect to DB ";
	    return msg;
	}				
	try{
	    pstmt = con.prepareStatement(qq);
	    if(!id.isEmpty()){
		pstmt.setString(1, id);
	    }
	    rs = pstmt.executeQuery();
	    //
	    if(rs.next()){
		setVals(rs.getString(1),
			rs.getString(2),
			rs.getString(3),
			rs.getString(4),
			rs.getDouble(5),
			rs.getString(6),
			rs.getString(7),
			rs.getString(8),
			rs.getInt(9),
			rs.getString(10));
	    }
	    else{
		msg = "Accrual not found";
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
    public String doSave(){
	String msg = doSaveOnly();
	msg += doSelect();
	return msg;
    }
    public String doSaveOnly(){
	//
	Connection con = null;
	PreparedStatement pstmt = null, pstmt2=null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = "insert into employee_accruals values(0,?,?,?,?) ";
	if(employee_id.isEmpty()){
	    msg = " employee id not set ";
	    return msg;
	}
	if(accrual_id.isEmpty()){
	    msg = " accrual type not set ";
	    return msg;
	}
	logger.debug(qq);
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = "Could not connect to DB ";
	    return msg;
	}							
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, accrual_id);
	    pstmt.setString(2, employee_id);
	    pstmt.setDouble(3, hours);
	    if(date.isEmpty()){
		date = Helper.getToday();
	    }
	    java.util.Date date_tmp = df.parse(date);
	    pstmt.setDate(4, new java.sql.Date(date_tmp.getTime()));								
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
    public String doSaveBatch(double arr[]){
	//
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = "insert into employee_accruals values(0,?,?,?,?) ";
	if(employee_id.isEmpty()){
	    msg = " employee id not set ";
	    return msg;
	}
	logger.debug(qq);
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = "Could not connect to DB ";
	    return msg;
	}							
	try{
	    if(date.isEmpty()){
		date = Helper.getToday();
	    }
	    java.util.Date date_tmp = df.parse(date);
	    System.err.println(" writing date "+date);
	    pstmt = con.prepareStatement(qq);
	    int jj=1;
	    for(double hrs:arr){
		if(hrs < 0) hrs = 0;
		pstmt.setInt(1, jj);
		pstmt.setString(2, employee_id);
		pstmt.setDouble(3, hrs);
		pstmt.setDate(4, new java.sql.Date(date_tmp.getTime()));
		pstmt.executeUpdate();								
		jj++;
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
     * we do not change employee
     */
    public String doUpdate(){
	//
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = "update employee_accruals set accrual_id=?,hours=?,date=? where id=?";
	if(accrual_id.isEmpty()){
	    msg = " accrual type not set ";
	    return msg;
	}
	logger.debug(qq);
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = "Could not connect to DB ";
	    return msg;
	}						
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, accrual_id);
	    pstmt.setDouble(2, hours);
	    java.util.Date date_tmp = df.parse(date);
	    pstmt.setDate(3, new java.sql.Date(date_tmp.getTime()));
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
	doSelect();
	return msg;
    }		
		
}
