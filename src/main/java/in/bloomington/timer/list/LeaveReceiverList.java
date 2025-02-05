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
import in.bloomington.timer.bean.LeaveReceiver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LeaveReceiverList{

    boolean debug = false;
    String group_id="";
    static final long serialVersionUID = 54L;
    static Logger logger = LogManager.getLogger(LeaveReceiverList.class);
    static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    List<LeaveReceiver> receivers = null;
    //
    // basic constructor
    public LeaveReceiverList(){


    }
    public LeaveReceiverList(String val){
	//
	// initialize
	//
	setGroup_id(val);
    }
    //
    public void setGroup_id(String val){
	if(val != null)
	    group_id = val;
    }
		
    public List<LeaveReceiver> getReceivers(){
	return receivers;
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
	String qq = "select r.id,r.group_id,r.employee_id, "+
	    "g.name group_name,"+
	    "concat_ws(' ',e.first_name,e.last_name) employee_name, "+
	    "e.email employee_email,g.department_id dept_id "+
	    "from leave_receivers r "+
	    "join `groups` g on g.id=r.group_id "+
	    "join employees e on e.id = r.employee_id ";	
	String qw = "";
	if(!group_id.isEmpty()){
	    qw += " group_id = ? ";
	}
	if(!qw.isEmpty()){
	    qq += " where "+qw;
	}
	qq += " order by group_name,employee_name ";
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
	    int jj = 1;
	    if(!group_id.isEmpty()){
		pstmt.setString(jj,group_id);

	    }
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		LeaveReceiver one =
		    new LeaveReceiver(rs.getString(1),
				      rs.getString(2),
				      rs.getString(3),
				      rs.getString(4),
				      rs.getString(5),
				      rs.getString(6),
				      rs.getString(7));
		if(receivers == null)
		    receivers = new ArrayList<>();
		if(!receivers.contains(one))
		    receivers.add(one);
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






















































