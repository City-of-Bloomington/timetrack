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

public class AltPayPeriodGroup{

    String id="", group_id="", group_name="", dept_name="";
    boolean debug = false;
    static Logger logger = LogManager.getLogger(AltPayPeriodGroup.class);
    Group group = null;
   
    public AltPayPeriodGroup(){
    }
    public AltPayPeriodGroup(
		   String val,
		   String val2
		   ){
	setId(val);
	setGroup_id(val2);
    }
    public AltPayPeriodGroup(
			     String val,
			     String val2,
			     String val3,
			     String val4
			     ){
	setId(val);
	setGroup_id(val2);
	setGroupName(val3);
	setDeptName(val4);
    }	
    public String getId(){
	return id;
    }
    public String getGroup_id(){
	return group_id;
    }
    public String getGroupName(){
	return group_name;
    }
    public String getDeptName(){
	return dept_name;
    }
    
    public Group getGroup(){
	if(group == null && !group_id.isEmpty()){
	    Group one = new Group(group_id);
	    String back = one.doSelect();
	    if(back.isEmpty())
		group = one;
	}
	return group;
    }	
    //
    // setters
    //
    public void setId (String val){
	if(val != null)		
	    id = val;
    }
    public void setGroup_id (String val){
	if(val != null){
	    group_id = val;
	}
    }
    public void setGroupName (String val){
	if(val != null){
	    group_name = val;
	}
    }
    public void setDeptName (String val){
	if(val != null){
	    dept_name = val;
	}
    }    
    
    public  String doSave(){
	//
	Connection con = null;
	PreparedStatement pstmt = null, pstmt2 = null;
	ResultSet rs = null;
		
	String qq = "insert into "+ 
	    " alt_pay_period_groups values(0,?) ";
	String back = "";
	if(group_id.isEmpty()){
	    back = " group_id not set ";
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
	    pstmt.executeUpdate();
	    //
	    qq = "select LAST_INSERT_ID() ";
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt2 = con.prepareStatement(qq);			
	    rs = pstmt2.executeQuery();
	    if(rs.next()){
		id = rs.getString(1);
	    }				
	    
	}
	catch(Exception ex){
	    back += ex;
	    logger.error(ex+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(rs, pstmt, pstmt2);
	    UnoConnect.databaseDisconnect(con);
	}
	if(back.isEmpty()){
	    doSelect();
	}
	return back;
    }			
    public String doDelete(){
	//
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
		
	String qq = 
	    " delete from alt_pay_period_groups "+
	    " where id = ? ";
	String back = "";
	if(id.isEmpty()){
	    back = " id not set ";
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
		
	String qq = "select a.id,a.group_id,g.name,d.name from "+
	    " alt_pay_period_groups a left join groups g on g.id=a.group_id "+
	    " left join departments d on d.id=g.department_id where a.id = ? ";
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
		setGroupName(rs.getString(3));
		setDeptName(rs.getString(4));
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
CREATE TABLE alt_pay_period_groups (
  id int unsigned NOT NULL AUTO_INCREMENT,
  group_id int unsigned not null unique,
  PRIMARY KEY (id),
  foreign key(group_id) references groups(id)
) ENGINE=InnoDB;


 */
