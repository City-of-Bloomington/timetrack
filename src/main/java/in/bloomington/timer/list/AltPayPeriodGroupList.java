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
import in.bloomington.timer.bean.AltPayPeriodGroup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AltPayPeriodGroupList{

    boolean debug;
    static final long serialVersionUID = 54L;
    static Logger logger = LogManager.getLogger(AltPayPeriodGroupList.class);
    static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    List<AltPayPeriodGroup> altGroups = null;
    //
    // this contains only dates
    Set<String> groupIdSet = null;
    //
    // basic constructor
    public AltPayPeriodGroupList(){

    }
    public List<AltPayPeriodGroup> getAltGroups(){
	return altGroups;
    }
    public Set<String> getGroupIdSet(){
	return groupIdSet;
    }
    // return "" or any exception thrown by DB
    //
    public String find(){
	//
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "select a.id,a.group_id,g.name,d.name from "+
	    " alt_pay_period_groups a left join groups g on g.id=a.group_id "+
	    " left join departments d on d.id=g.department_id ";		
	qq += " order by a.id ";
	String back = "";
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
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		String str  = rs.getString(1);
		String str2 = rs.getString(2);
		String str3 = rs.getString(3);
		String str4 = rs.getString(4);
		// allSet.add(str2);
		if(altGroups == null)
		    altGroups = new ArrayList<>();
		if(groupIdSet == null)
		    groupIdSet =  new HashSet<>();
		AltPayPeriodGroup one = new AltPayPeriodGroup(str, str2, str3, str4);
		if(!altGroups.contains(one))
		    altGroups.add(one);
		groupIdSet.add(str2);
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






















































