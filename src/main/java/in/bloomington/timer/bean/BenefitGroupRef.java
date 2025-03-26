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

public class BenefitGroupRef{

    static final long serialVersionUID = 3700L;	
    static Logger logger = LogManager.getLogger(BenefitGroupRef.class);
    String id="", benefit_name="", salary_group_id="", salary_group_name="";
    //
    public BenefitGroupRef(){
	super();
    }
    public BenefitGroupRef(String val){
	//
	setId(val);
    }		
    public BenefitGroupRef(String val, String val2, String val3){
	//
	// initialize
	//
	setId(val);
	setBenefitName(val2);
	setSalaryGroup_id(val3);
    }
    public BenefitGroupRef(String val, String val2, String val3, String val4){
	//
	// initialize
	//
	setId(val);
	setBenefitName(val2);
	setSalaryGroup_id(val3);
	setSalaryGroupName(val4);
    }    
    public boolean equals(Object obj){
	if(obj instanceof BenefitGroupRef){
	    BenefitGroupRef one =(BenefitGroupRef)obj;
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
    public String getName(){
	return benefit_name;
    }    
    public String getSalaryGroup_id(){
	return salary_group_id;
    }
    public String getSalaryGroupName(){
	if(salary_group_name.isEmpty() && !salary_group_id.isEmpty()){
	    findSalaryGroupName();
	}
	return salary_group_name;
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
    public void setName(String val){
	if(val != null)
	    benefit_name = val.trim();
    }    
    public void setSalaryGroup_id(String val){
	if(val != null)
	    salary_group_id = val;
    }
    public void setSalaryGroupName(String val){
	if(val != null)
	    salary_group_name = val;
    }    
    public String toString(){
	return benefit_name+": "+salary_group_id;
    }
    void findSalaryGroupName(){
	if(!salary_group_id.isEmpty()){
	    SalaryGroup sg = new SalaryGroup(salary_group_id);
	    String back = sg.doSelect();
	    if(back.isEmpty()){
		salary_group_name = sg.getName();
	    }
	}
    }
    //
    public String doSelect(){
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "select id,benefit_name, salary_group_id "+
	    "from benefit_group_refs where id=?";
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
	String qq = " insert into benefit_group_refs values(0,?,?)";
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
	String qq = " update benefit_group_refs set benefit_name=?, salary_group_id=? where id=?";
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
/**
rename table  benefit_salary_ref
to benefit_group_refs

alter table  benefit_salary_ref  rename to benefit_group_refs;
delete from benefit_group_refs;
//
// add the following to the table
//
insert into benefit_group_refs values
(1, 'FIRE SWORN - Firefighter Sworn : Fire Union Non-Exempt',9),
(2, 'FIRE SWORN - Firefighter Sworn : Regular Exempt', 9),
(3, 'FIRE SWORN 5X8 - Firefighter Sworn 5x8 : Fire Union Exempt',10),
(4, 'FIRE SWORN 5X8 - Firefighter Sworn 5x8 : Regular Exempt',10),
(5, 'POLICE SWORN  - Sworn Police Officers : Police Union Non-Exempt',6),
(6, 'POLICE SWORN  - Sworn Police Officers : Regular Non-Exempt',6),
(7, 'POLICE SWORN DET - Police Sworn Detective : Police Union Non-Exempt',7),
(8, 'POLICE SWORN MGT - Police Sworn Management : Police Union Exempt',8),
(9, 'CEDC4/2 - Central Dispatch 4 on 2 off : Regular Non-Exempt',2),
(10, 'CEDC 5/2 - Central Dispatch 5 on 2 off : Regular Non-Exempt',2),
(11, 'CEDC 5/2 - Central Dispatch 5 on 2 off : Regular Exempt',1),
(12, 'NON-U RPARTnx - Non-Union Regular PT Non-Exempt : Regular Non-Exempt',11),
(13, 'uNON-U RFTx - Utilities Non-Union Reg FT Ex : Regular Exempt',1),
(14, 'uNON-U RPARTnx - Utilities Non-Union Reg PT NonEx : Regular Non-Exempt',11),
(15, 'uNON-U RFTnx - Utilities Non-Union Reg FT NonEx : Regular Non-Exempt',2),
(16, 'TEMP W/BEN - Temporary Employee With Benefits : Regular Non-Exempt',12),
(17, 'TEMP  - Temporary Employees : Regular Non-Exempt', 3),
(18, 'uTEMP - Utilities Temporary Employee : Regular Non-Exempt',3),
(19, 'NON-U RFULLnx - Non-Union Regular FT Non-Exempt : Regular Non-Exempt',2),
(20, 'NON-U RFULLx - Non-Union Regular FT Exempt : Regular Exempt',1),
(21, 'AFSCME RFT - AFSCME-80 Hours : AFSCME Non-Exempt',4),
(22, 'uAFSCME RFT - Utilities AFSCME RFT 80 Hours : AFSCME Non-Exempt',4),
(23, 'COUNCIL MEM - Council Members : Regular Exempt',1),
(24, 'BOARD - Board pd members (USB, BPW, BPS) : Regular Exempt',1),
(25, 'ELECTED  - Elected Employees : Regular Exempt',1);




 */
