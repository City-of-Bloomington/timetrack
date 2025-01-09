package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.sql.*;
import javax.naming.*;
import javax.naming.directory.*;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GroupLeave{

    static final long serialVersionUID = 3700L;	
    static Logger logger = LogManager.getLogger(GroupLeave.class);
    String id="", salary_group_id="", group_id="", inactive="";
    String department_id = "";
    Group group = null;
    SalaryGroup salaryGroup = null;
    //
    public GroupLeave(){
    }
    public GroupLeave(String val){
	//
	setId(val);
    }		
    public GroupLeave(String val, String val2, String val3, boolean val4){
	//
	// initialize
	//
	setVals(val, val2, val3, val4);
    }
    private void setVals( String val, String val2, String val3, boolean val4){
	setId(val);
	setGroup_id(val2);
	setSalary_group_id(val3);
	setInactive(val4);

    }
    public int hashCode(){
	int seed = 29;
	if(!group_id.isEmpty()){
	    try{
		seed += Integer.parseInt(group_id);
		if(!salary_group_id.isEmpty()){
		    seed += Integer.parseInt(salary_group_id);
		}
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
    public String getSalary_group_id(){
	return salary_group_id;
    }
    public String getGroup_id(){
	if(group_id.isEmpty()){
	    return "-1";
	}
	return group_id;
    }
    public String getDepartment_id(){
	if(department_id.isEmpty() && !group_id.isEmpty()){
	    getGroup();
	    if(group != null)
		department_id = group.getDepartment_id();
	}
	return department_id; 
    }
    public boolean getInactive(){
	return !inactive.isEmpty();
    }
    //
    // setters
    //
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setGroup_id(String val){
	if(val != null && !val.equals("-1"))
	    group_id = val;
    }
    public void setSalary_group_id(String val){
	if(val != null && !val.equals("-1"))
	    salary_group_id = val;
    }
    public void setInactive(boolean val){
	if(val)
	    inactive = "y";
    }    
    public String toString(){
	return group_id+" "+salary_group_id;
    }
    public SalaryGroup getSalaryGroup(){
	if(salaryGroup == null && !salary_group_id.isEmpty()){
	    SalaryGroup one = new SalaryGroup(salary_group_id);
	    String back = one.doSelect();
	    if(back.isEmpty()){
		salaryGroup = one;
	    }
	}
	return salaryGroup;
    }
    public Group getGroup(){
	if(group == null && !group_id.isEmpty()){
	    Group one = new Group(group_id);
	    String back = one.doSelect();
	    if(back.isEmpty()){
		group = one;
	    }
	}
	return group;
    }
    public boolean isLeaveEligible(String g_id, String sg_id){
	String back = "";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "select count(*) "+
	    " from group_leaves t "+
	    " where t.group_id=? and t.salary_group_id=? and t.inactive is null";
				
	Connection con = UnoConnect.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    logger.error(back);
	    return false;
	}
	int cnt = 0;
	try{
	    logger.debug(qq);
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,g_id);
	    pstmt.setString(2,sg_id);	    
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		cnt = rs.getInt(1);
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
	return cnt > 0;
    }
    //
    public String doSelect(){
	String back = "";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "select t.id,t.group_id,t.salary_group_id,t.inactive "+
	    " from group_leaves t "+
	    " where t.id=? ";
				
	Connection con = UnoConnect.getConnection();
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
		setVals(rs.getString(1),
			rs.getString(2),
			rs.getString(3),
			rs.getString(4) != null);
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
	PreparedStatement pstmt = null, pstmt2=null;
	ResultSet rs = null;
	Connection con = null;
	String msg="", str="";
	String qq = " insert into group_leaves values(0,?,?,null)";
	if(group_id.isEmpty()){
	    msg = "group id is required";
	    return msg;
	}
	if(salary_group_id.isEmpty()){
	    msg = "salary group id is required";
	    return msg;
	}
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = "Could not connect to DB ";
	    return msg;
	}
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, group_id);
	    pstmt.setString(2, salary_group_id);
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
    public String doUpdate(){

	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection con = null;
	String msg="", str="";
	String qq = " update group_leaves set group_id=?,salary_group_id=?,inactive=? where id=?";
	if(group_id.isEmpty()){
	    msg = "group id not set";
	    return msg;
	}
	if(salary_group_id.isEmpty()){
	    msg = "salary group id not set";
	    return msg;
	}
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = "Could not connect to DB ";
	    return msg;
	}
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, group_id);
	    pstmt.setString(2, salary_group_id);
	    if(inactive.isEmpty()){
		pstmt.setNull(3, Types.CHAR);
	    }
	    else{
		pstmt.setString(3, "y");
	    }
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
    public String doDelete(){
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection con = null;
	String msg="", str="";
	String qq = " delete from group_leaves where id=?";
	if(id.isEmpty()){
	    msg = "id is required";
	    return msg;
	}
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = "Could not connect to DB ";
	    return msg;
	}
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, id);
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
       
CREATE TABLE group_leaves (
  id int unsigned NOT NULL AUTO_INCREMENT,
  group_id int unsigned not null,
  salary_group_id int unsigned not null,
  inactive char(1) DEFAULT NULL,
  PRIMARY KEY (id),
  foreign key(group_id) references groups(id),
  foreign key(salary_group_id) references salary_groups(id) 
) ENGINE=InnoDB;

    */
}
