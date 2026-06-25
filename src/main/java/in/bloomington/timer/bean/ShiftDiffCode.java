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

public class ShiftDiffCode{

    String id="", name = "", nw_name="";
    static Logger logger = LogManager.getLogger(ShiftDiffCode.class);

    public ShiftDiffCode(){

    }
    public ShiftDiffCode(
		   String val,
		   String val2,
		   String val3
		   ){
	setId(val);
	setName(val2);
	setNwName(val3);
    }
	
    public String getId(){
	return id;
    }
    public String getName(){
	return name;
    }

    public String getNwName(){
	return nw_name;
    }	
    //
    // setters
    //
    public void setId (String val){
	if(val != null)		
	    id = val;
    }
    public void setName(String val){
	if(val != null){
	   name = val;
	}
    }	
    public void setNwName(String val){
	if(val != null)
	    nw_name = val;
    }
    public  String doSave(){
	//
	Connection con = null;
	PreparedStatement pstmt = null, pstmt2 = null;
	ResultSet rs = null;
		
	String qq = "insert into "+ 
	    " shift_diff_codes values(0,?,?) ";
	String back = "";
	if(name.isEmpty() || nw_name.isEmpty()){
	    back = " shift code or corresponding code not set ";
	    return back;
	}
	con = UnoConnect.getConnection();				
	if(con == null){
	    back = "Could not connect to DB ";
	    return back;
	}
	try{
	    logger.debug(qq);
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, name);
	    pstmt.setString(2, nw_name);			
	    pstmt.executeUpdate();
	    Helper.databaseDisconnect(pstmt, rs);
	    //
	    qq = "select LAST_INSERT_ID() ";
	    logger.debug(qq);
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
	return back;
    }			
    public String doUpdate(){
	//
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
		
	String qq = 
	    " update shift_diff_codes "+
	    " set name=?, nw_name=? "+			
	    " where id = ? ";
	String back = "";
	if(id.isEmpty() || name.isEmpty() || nw_name.isEmpty()){
	    back = " shift codes not set properly ";
	    return back;
	}
	con = UnoConnect.getConnection();	
	if(con == null){
	    back = "Could not connect to DB ";
	    return back;
	}
	try{
	    logger.debug(qq);
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, name);
	    pstmt.setString(2, nw_name);			
	    pstmt.setString(3, id);
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
    public String doDelete(){
	//
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
		
	String qq = 
	    " delete from shift_diff_codes "+
	    " where id = ? ";
	String back = "";
	if(id.isEmpty()){
	    back = " holiday id not set ";
	    return back;
	}
	logger.debug(qq);
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
		
	String qq = "select name,nw_name from "+
	    " shift_diff_codes where id = ? ";
	String back = "";
	logger.debug(qq);
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
		String str = rs.getString(1);
		String str2 = rs.getString(2);
		setName(str);
		setNwName(str2);
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
CREATE TABLE shift_diff_codes (
  id int unsigned NOT NULL AUTO_INCREMENT,
  name varchar(16) not null,
  nw_name varchar(24) not null,
  PRIMARY KEY (id)
) ENGINE=InnoDB;

insert into shift_diff_codes
values(0,'ASP','ASP OT'),
       (0,'ASCEDC','ASCEDC OT'),
       (0,'ASREC','ASREC OT'),
       (0,'NSP','NSP/HI OT'),
       (0,'ASP-SPO','ASP-SPO');
*/
