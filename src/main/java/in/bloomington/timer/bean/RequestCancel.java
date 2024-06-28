package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.List;
import java.sql.*;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RequestCancel implements java.io.Serializable{

    static final long serialVersionUID = 3700L;	
    static Logger logger = LogManager.getLogger(RequestCancel.class);
    String id="", document_id="",
	request_date="",
	request_by_id="",
	request_reason="";
    Employee requestBy = null;
    //
    public RequestCancel(){

    }
    public RequestCancel(String val){
	//
	setId(val);
    }		
    public RequestCancel(String val, String val2, String val3, String val4, String val5){
	setId(val);
	setDocument_id(val2);
	setRequestReason(val3);
	setRequestDate(val4);
	setRequestBy_id(val5);
    }		
    public boolean equals(Object obj){
	if(obj instanceof
	   RequestCancel){
	    RequestCancel one =(RequestCancel)obj;
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
    //
    // getters
    //
    public String getId(){
	return id;
    }
    public String getRequestReason(){
	return request_reason;
    }
    public String getRequestDate(){
	return request_date;
    }		
    public String getRequestBy_id(){
	return request_by_id;
    }
    public String getDocument_id(){
	return document_id;
    }		
    //
    // setters
    //
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setRequestReason(String val){
	if(val != null)
	    request_reason = val.trim();
    }
    public void setRequestDate(String val){
	if(val != null)
	    request_date = val;
    }		
    public void setRequestBy_id(String val){
	if(val != null)
	    request_by_id = val;
    }		
    public void setDocument_id(String val){
	if(val != null)
	    document_id = val;
    }
    public Employee getRequestBy(){
	if(requestBy == null && !request_by_id.isEmpty()){
	    Employee one = new Employee(request_by_id);
	    String back = one.doSelect();
	    if(back.isEmpty()){
		requestBy = one;
	    }
	}
	return requestBy;
    }
    public String toString(){
	return id+" "+request_reason;
    }
    //
    public String doSelect(){
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "select id,document_id,request_reason,date_format(request_date,'%m/%d/%Y %h:%i'),request_by_id "+
	    "from cancel_requests where id=?";
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
		setDocument_id(rs.getString(2));		
		setRequestReason(rs.getString(3));
		setRequestDate(rs.getString(4));
		setRequestBy_id(rs.getString(5));
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
	String qq = " insert into cancel_requests values(0,?,?,now(),?)";
	if(request_reason.isEmpty()){
	    msg = "requst reason is required";
	    return msg;
	}
	if(document_id.isEmpty()){
	    msg = "time document is required";
	    return msg;
	}	
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = "Could not connect to DB ";
	    return msg;
	}
				
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, document_id);
	    pstmt.setString(2, request_reason);
	    pstmt.setString(3, request_by_id);
	    pstmt.executeUpdate();
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
    /**
    create table cancel_requests (
    id int unsigned not null auto_increment,
    document_id int unsigned,    
    request_reason varchar(1024),
    request_date datetime,
    request_by_id int unsigned,
    primary key(id),
    foreign key(document_id) references time_documents(id),        
    foreign key(request_by_id) references employees(id)
    )engine=InnoDB;

    */
}
