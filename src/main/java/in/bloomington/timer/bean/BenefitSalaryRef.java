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

public class BenefitSalaryRef{

    static final long serialVersionUID = 3700L;	
    static Logger logger = LogManager.getLogger(BenefitSalaryRef.class);
    String id="", benefit_name="", salary_group_id="";
    //
    public BenefitSalaryRef(){
	super();
    }
    public BenefitSalaryRef(String val){
	//
	setId(val);
    }		
    public BenefitSalaryRef(String val, String val2, String val3){
	//
	// initialize
	//
	setId(val);
	setBenefitName(val2);
	setSalaryGroup_id(val3);
    }
    public boolean equals(Object obj){
	if(obj instanceof BenefitSalaryRef){
	    BenefitSalaryRef one =(BenefitSalaryRef)obj;
	    return id.equals(one.getId());
	}
	return false;				
    }
    public int hashCode(){
	int seed = 29;
	if(!id.equals("")){
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
    public String getBenefitName(){
	return benefit_name;
    }
    public String getSalaryGroup_id(){
	return salary_group_id;
    }

    //
    // setters
    //
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setBenefitName(String val){
	if(val != null)
	    benefit_name = val.trim();
    }
    public void setSalaryGroup_id(String val){
	if(val != null)
	    salary_group_id = val;
    }		
    public String toString(){
	return benefit_name+": "+salary_group_id;
    }
    //
    public String doSelect(){
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "select id,benefit_name, salary_group_id "+
	    "from benefit_salary_ref where id=?";
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
		setBenefitName(rs.getString(2));
		setSalaryGroup_id(rs.getString(3));
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
	String qq = " insert into benefit_salary_ref values(0,?,?)";
	if(benefit_name.equals("")){
	    msg = "benefit name is required";
	    return msg;
	}
	try{
	    con = UnoConnect.getConnection();
	    if(con == null){
		msg = "Could not connect to DB ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, benefit_name);
	    pstmt.setString(2, salary_group_id);
	    pstmt.executeUpdate();
	    Helper.databaseDisconnect(pstmt, rs);
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
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = " update benefit_salary_ref set benefit_name=?, salary_group_id=? where id=?";
	if(benefit_name.equals("")){
	    msg = "benefit name is required";
	    return msg;
	}
	try{
	    con = UnoConnect.getConnection();
	    if(con == null){
		msg = "Could not connect to DB ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, benefit_name);
	    pstmt.setString(2, salary_group_id);
	    pstmt.setString(3, id);
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

}
