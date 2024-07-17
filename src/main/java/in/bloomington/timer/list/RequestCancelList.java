package in.bloomington.timer.list;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.ArrayList;
import java.util.List;
import java.text.*;
import java.util.Date;
import java.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;

public class RequestCancelList{

    static final long serialVersionUID = 1600L;
    static Logger logger = LogManager.getLogger(RequestCancelList.class);
    String document_id="";
    boolean active_only = false, inactive_only=false;
    List<RequestCancel> requests = null;
    public RequestCancelList(){
    }
    public RequestCancelList(String val){
	setDocument_id(val);
    }
    public void setDocument_id(String val){
	if(val != null)
	    document_id = val;
    }		
    public void setActive_status(String val){
	if(val != null && !val.equals("-1")){
	    if(val.equals("Active"))
		active_only = true;
	    else if(val.equals("Inactive"))
		inactive_only = true;
	}
    }		
    public void setActiveOnly(){
	active_only = true;
    }

    public String getDocument_id(){
	return document_id;
    }		
    public List<RequestCancel> getRequests(){
	return requests;
    }
    public String getActive_status(){
	if(active_only)
	    return "Active";
	else if(inactive_only)
	    return "Inactive";
	return "-1";
    }
    //
    // getters
    //
    public String find(){
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = "select id,document_id,request_reason,date_format(request_date,'%m/%d/%Y %h:%i'),request_by_id,approver_id,processor_id,notification_status "+
	    "from cancel_requests " ;	
	String qw = "";
	if(!document_id.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";						
	    qw += "document_id = ? ";
	}				
	if(!qw.isEmpty()){
	    qq += " where "+qw;
	}
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = " Could not connect to DB ";
	    logger.error(msg);
	    return msg;
	}
	logger.debug(qq);
	try{
	    pstmt = con.prepareStatement(qq);
	    int jj=1;
	    if(!document_id.isEmpty()){
		pstmt.setString(jj++, document_id);
	    }						
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		if(requests == null)
		   requests = new ArrayList<>();
		RequestCancel one = new RequestCancel(
				      rs.getString(1),
				      rs.getString(2),
				      rs.getString(3),
				      rs.getString(4),
				      rs.getString(5),
				      rs.getString(6),
				      rs.getString(7),
				      rs.getString(8));
		requests.add(one);
	    }
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
