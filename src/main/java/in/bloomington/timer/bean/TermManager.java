package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.sql.*;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;

public class TermManager implements java.io.Serializable{

    static final long serialVersionUID = 3750L;	
    static Logger logger = LogManager.getLogger(TermManager.class);
    String id="", employee_id="", department_id="", inactive="";
    String deptName="", empName="All";
    Employee employee = null;
    Department department = null;
    
    //
    public TermManager(){
	super();
    }
    public TermManager(String val){
	//
	setId(val);
    }		
    public TermManager(String val, String val2, String val3, boolean val4){
	// // for new record
	setId(val);
	setEmployee_id(val2);
	setDepartment_id(val3);
	setInactive(val4);
    }
    public TermManager(String val, String val2, String val3, boolean val4,
		       String val5, String val6){
	// // for new record
	setId(val);
	setEmployee_id(val2);
	setDepartment_id(val3);
	setInactive(val4);
	setDeptName(val5);
	setEmpName(val6);
    }		    
    public boolean equals(Object obj){
	if(obj instanceof TermManager){
	    TermManager one =(TermManager)obj;
	    return id.equals(one.getId());
	}
	return false;				
    }
    public int hashCode(){
	int seed = 17;
	if(!id.isEmpty()){
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
    public String getEmployee_id(){
	return employee_id;
    }
    public String getDepartment_id(){
	return department_id;
    }		
    public boolean getInactive(){
	return !inactive.isEmpty();
    }
    public boolean isInactive(){
	return !inactive.isEmpty();
    }
    public boolean isActive(){
	return inactive.isEmpty();
    }
    public String getDeptName(){
	if(deptName.isEmpty() || deptName.equals("All")){
	    getDepartment();
	    if(department != null){
		deptName = department.getName();
	    }
	}
	return deptName;
    }
    public String getEmpName(){
	if(empName.isEmpty()){
	    getEmployee();
	    if(employee != null){
		empName = employee.getFull_name();
	    }
	}
	return empName;
    }
    //
    // setters
    //
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setEmployee_id(String val){
	if(val != null)
	    employee_id = val;
    }
    public void setDepartment_id(String val){
	if(val != null)
	    department_id = val;
    }		
    public void setInactive(boolean val){
	if(val)
	    inactive = "y";
    }
    public void setDeptName(String val){
	if(val != null)
	    deptName = val;
    }
    public void setEmpName(String val){
	if(val != null)
	    empName = val;
    }    
    public Employee getEmployee(){
	if(employee == null && !employee_id.isEmpty()){
	    Employee one = new Employee(employee_id);
	    String back = one.doSelect();
	    if(back.isEmpty()){
		employee = one;
	    }
	}
	return employee;
    }
    public Department getDepartment(){
	if(department == null && !department_id.isEmpty()){
	    Department one = new Department(department_id);
	    String back = one.doSelect();
	    if(back.isEmpty()){
		department = one;
	    }
	}
	return department;
    }
    public String toString(){
	return id;
    }
    //
    public String doSelect(){
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "select t.id,t.employee_id,t.department_id,t.inactive, "+
	    "d.name as deptName,concat_ws(' ',e.first_name,e.last_name) as empName "+ 
	    "from term_managers t "+
	    "join employees e on e.id=t.employee_id "+	    
	    "left join department_employees de on de.department_id=t.department_id "+
	    "left join departments d on d.id = de.department_id "+	    
	    "where t.id=?";
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
		setEmployee_id(rs.getString(2));
		setDepartment_id(rs.getString(3));
		setInactive(rs.getString(4) != null);
		setDeptName(rs.getString(5));
		setEmpName(rs.getString(6));
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
	inactive=""; // default
	String qq = " insert into term_managers values(0,?,?,null)";
	String qq2 = "select LAST_INSERT_ID()";
	if(employee_id.isEmpty()){
	    msg = "Employee is required";
	    return msg;
	}
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = "Could not connect to DB ";
	    return msg;
	}
	try{
	    int cnt = 0;
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, employee_id);
	    if(department_id.isEmpty())
		pstmt.setNull(2, Types.VARCHAR);
	    else
		pstmt.setString(2, department_id);
	    pstmt.executeUpdate();
	    qq = qq2;
	    pstmt2 = con.prepareStatement(qq2);
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
    public String doUpdate(){
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = " update term_managers set employee_id=?, department_id=?, inactive=? where id=?";
	if(employee_id.isEmpty()){
	    msg = "Employee is required";
	    return msg;
	}
	if(id.isEmpty()){
	    msg = "Record id not set";
	    return msg;
	}	
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = "Could not connect to DB ";
	    return msg;
	}
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, employee_id);
	    if(department_id.isEmpty())
		pstmt.setNull(2, Types.VARCHAR);
	    else
		pstmt.setString(2, department_id);
	    if(inactive.isEmpty())
		pstmt.setNull(3, Types.CHAR);
	    else
		pstmt.setString(3, "y");
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
    create table term_managers (
    id int unsigned not null auto_increment,
    employee_id int unsigned not null,
    department_id int unsigned,
    inactive char(1),
    primary key(id),
    foreign key(employee_id) references employees(id),
    foreign key(department_id) references departments(id)
    )engine=InnoDB;
    */    
}
