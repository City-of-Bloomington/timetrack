package in.bloomington.timer.list;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */

import java.util.*;
import java.sql.*;
import java.io.*;
import java.text.*;
import java.util.ArrayList;
import java.util.List;
import in.bloomington.timer.util.Helper;
import in.bloomington.timer.util.UnoConnect;
import in.bloomington.timer.bean.ShiftDiffCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ShiftDiffCodeList{

    String id = "", name="", nw_name="";
    static final long serialVersionUID = 54L;
    static Logger logger = LogManager.getLogger(ShiftDiffCodeList.class);
    List<ShiftDiffCode> shiftCodes = null;
    Map<String, String> shiftCodeMap = null;
    //
    // basic constructor
    public ShiftDiffCodeList(){
    }
    public ShiftDiffCodeList(String val){
	setId(val);
    }
    //
    public void setId(String val){
	if(val != null)
	    id = val;
    }
		
    public void setName(String val){
	if(val != null)
	    name = val;
    }
    public void setNwName(String val){
	if(val != null)
	    nw_name = val;
    }
    public List<ShiftDiffCode> getShiftCodes(){
	return shiftCodes;
    }
    public Map<String, String> getShiftCodeMap(){
	return shiftCodeMap;
    }
	
    public boolean hasShiftDiffCode(String var){
	if(shiftCodeMap != null){
	    return shiftCodeMap.containsKey(var);
	}
	return false;
    }
    public String getShiftCodeNw(String var){
	String str = "";
	if(shiftCodeMap != null){
	    if(shiftCodeMap.containsKey(var)){
		str = shiftCodeMap.get(var);
	    }
	}
	return str;
    }
    //
    // find all matching records
    // return "" or any exception thrown by DB
    //
    public String find(){
	//
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
		
	String qq = "select id,name,nw_name from shift_diff_codes ";
	String qw = "";
	if(!id.isEmpty()){
	    qw = " id = ? ";
	}
	if(!name.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw = " name = ? ";   
	}
	if(!nw_name.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw = " nw_name = ? ";   
	}	    	
	if(!qw.isEmpty()){
	    qq += " where "+qw;
	}
	qq += " order by name ";
	String back = "";
	con = UnoConnect.getConnection();				
	if(con == null){
	    back = "Could not connect to DB ";
	    return back;
	}
	try{
	    logger.debug(qq);
	    pstmt = con.prepareStatement(qq);
	    int jj = 1;
	    if(!id.isEmpty()){
		pstmt.setString(jj++,id);
	    }
	    if(!name.isEmpty()){
		pstmt.setString(jj++,name);
	    }
	    if(!nw_name.isEmpty()){
		pstmt.setString(jj++,nw_name);
	    }	    
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		if(shiftCodeMap == null)
		    shiftCodeMap = new HashMap<>();
		if(shiftCodes == null)
		   shiftCodes = new ArrayList<>();
		String str  = rs.getString(1);
		String str2 = rs.getString(2);
		String str3 = rs.getString(3);
		shiftCodeMap.put(str2, str3);
		ShiftDiffCode one = new ShiftDiffCode(str, str2, str3);
		shiftCodes.add(one);
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






















































