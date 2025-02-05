/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
package in.bloomington.timer.bean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.*;
import java.sql.*;
import java.text.*;
import in.bloomington.timer.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LeaveReceiver{

    String id="", group_id = "", employee_id="", department_id="",groupName="",
	employeeName="", employeeEmail="";
    boolean debug = false;
    static Logger logger = LogManager.getLogger(LeaveReceiver.class);
    public LeaveReceiver(){
	
    }
    public LeaveReceiver(String val){
	setId(val);
    }    
    public LeaveReceiver(
			 String val,
			 String val2,
			 String val3,
			 String val4,
			 String val5,
			 String val6,
			 String val7
			 ){
	setId(val);
	setGroup_id(val2);
	setEmployee_id(val3);
	setGroupName(val4);
	setEmployeeName(val5);
	setEmployeeEmail(val6);
	setDepartment_id(val2);	
    }
	
    public String getId(){
	return id;
    }
    public String getGroup_id(){
	return group_id;
    }
    public String getGroupName(){
	return groupName;
    }
    public String getEmployee_id(){
	return employee_id;
    }
    public String getDepartment_id(){
	return department_id;
    }    
    public String getEmployeeName(){
	return employeeName;
    }
    public String getEmployeeEmail(){
	return employeeEmail;
    }
    public String getEmail(){
	return employeeEmail;
    }    
    //
    // setters
    //
    public void setId (String val){
	if(val != null)		
	    id = val;
    }
    public void setGroup_id(String val){
	if(val != null){
	    group_id = val;
	}
    }
    public void setEmployee_id(String val){
	if(val != null)
	   employee_id = val;
    }
    public void setDepartment_id(String val){
	if(val != null){
	    department_id = val;
	}
    }    
    public void setGroupName(String val){
	if(val != null){
	    groupName = val;
	}
    }
    public void setEmployeeName(String val){
	if(val != null){
	    employeeName = val;
	}
    }
    public void setEmployeeEmail(String val){
	if(val != null){
	    employeeEmail = val;
	}
    }
    public boolean equals(Object obj){
	if(obj instanceof LeaveReceiver){
	    LeaveReceiver one =(LeaveReceiver)obj;
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
    public  String doSave(){
	//
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
		
	String qq = "insert into "+ 
	    " leave_receivers values(0,?,?) ";
	String back = "";
	if(group_id.isEmpty() || employee_id.isEmpty()){
	    back = " group or employee not set ";
	    return back;
	}
	con = UnoConnect.getConnection();				
	if(con == null){
	    back = "Could not connect to DB ";
	    return back;
	}
	try{
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);
	    int jj = 1;
	    pstmt.setString(jj++,group_id);
	    pstmt.setString(jj++,employee_id);	    
	    pstmt.executeUpdate();
	    Helper.databaseDisconnect(pstmt, rs);
	    //
	    qq = "select LAST_INSERT_ID() ";
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);			
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		id = rs.getString(1);
	    }				
			
	}
	catch(Exception ex){
	    back += ex;
	    logger.error(ex+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(pstmt, rs);
	    UnoConnect.databaseDisconnect(con);
	}
	if(back.isEmpty()){
	    back = doSelect();
	}
	return back;
    }			
    public String doUpdate(){
	//
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
		
	String qq = 
	    " update leave_receivers "+
	    " set group_id=?, employee_id=? "+			
	    " where id = ? ";
	String back = "";
	if(group_id.isEmpty() || employee_id.isEmpty()){
	    back = " group or employee not set ";
	    return back;
	}
	con = UnoConnect.getConnection();	
	if(con == null){
	    back = "Could not connect to DB ";
	    return back;
	}
	try{
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);
	    int jj = 1;
	    pstmt.setString(jj++,group_id);
	    pstmt.setString(jj++,employee_id);	    
	    pstmt.setString(jj++,id);
	    pstmt.executeUpdate();

	}
	catch(Exception ex){
	    back += ex;
	    logger.error(ex+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(pstmt, rs);
	    UnoConnect.databaseDisconnect(con);
	}
	if(back.isEmpty()){
	    back = doSelect();
	}	
	return back;
    }
    public String doDelete(){
	//
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
		
	String qq = 
	    " delete from leave_receivers "+
	    " where id = ? ";
	String back = "";
	if(id.isEmpty()){
	    back = " holiday id not set ";
	    return back;
	}
	if(debug){
	    logger.debug(qq);
	}
	con = UnoConnect.getConnection();	
	if(con == null){
	    back = "Could not connect to DB ";
	    return back;
	}
	try{
	    pstmt = con.prepareStatement(qq);
	    int jj = 1;
	    pstmt.setString(jj++,id);
	    pstmt.executeUpdate();
	}
	catch(Exception ex){
	    back += ex;
	    logger.error(ex+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(pstmt, rs);
	    UnoConnect.databaseDisconnect(con);
	}
	return back;
    }			
    public String doSelect(){
	//
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
		
	String qq = "select r.id,r.group_id,r.employee_id, "+
	    "g.name group_name,"+
	    "concat_ws(' ',e.first_name,e.last_name) employee_name, "+
	    "e.email employee_email,g.department_id dept_id "+
	    "from leave_receivers r "+
	    "join `groups` g on g.id=r.group_id "+
	    "join employees e on e.id = r.employee_id "+
	    " where r.id = ? ";
	String back = "";
	if(debug){
	    logger.debug(qq);
	}
	con = UnoConnect.getConnection();				
	if(con == null){
	    back = "Could not connect to DB ";
	    return back;
	}
	try{
	    pstmt = con.prepareStatement(qq);
	    int jj = 1;
	    pstmt.setString(jj,id);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		setGroup_id(rs.getString(2));
		setEmployee_id(rs.getString(3));
		setGroupName(rs.getString(4));
		setEmployeeName(rs.getString(5));
		setEmployeeEmail(rs.getString(6));
		setDepartment_id(rs.getString(7));
	    }
	}
	catch(Exception ex){
	    back += ex;
	    logger.error(ex+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(pstmt, rs);
	    UnoConnect.databaseDisconnect(con);
	}
	return back;
    }	

}
/**
create table leave_receivers(
id int unsigned auto_increment,
group_id int unsigned not null,
employee_id int unsigned not null,
primary key(id),
foreign key(group_id) references `groups`(id),
foreign key(employee_id) references employees(id),
unique(group_id,employee_id)
)engine=InnoDB;

*/
