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
	approver_id="",
	processor_id="",
	request_reason="",
	notification_status = "";
    Employee requestBy = null;
    Employee approver = null;
    Employee processor = null;
    Document document = null;
    List<TimeAction> actions = null;
    //
    public RequestCancel(){

    }
    public RequestCancel(String val){
	//
	setId(val);
    }		
    public RequestCancel(String val, String val2, String val3, String val4, String val5, String val6, String val7, String val8){
	setId(val);
	setDocument_id(val2);
	setRequestReason(val3);
	setRequestDate(val4);
	setRequestBy_id(val5);
	setApprover_id(val6);
	setProcessor_id(val7);
	setNotificationStatus(val8);
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
    public String getApprover_id(){
	return approver_id;
    }
    public String getProcessor_id(){
	return processor_id;
    }
    public String getNotificationStatus(){
	return notification_status;
    }
    //
    // setters
    //
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setRequestReason(String val){
	if(val != null && !val.trim().isEmpty())
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
    public void setApprover_id(String val){
	if(val != null)
	    approver_id = val;
    }
    public void setProcessor_id(String val){
	if(val != null)
	    processor_id = val;
    }    
    public void setNotificationStatus(String val){
	if(val != null)
	    notification_status = val;
    }
    public Document getDocument(){
	if(document == null && !document_id.isEmpty()){
	    Document one = new Document(document_id);
	    String back = one.doSelect();
	    if(back.isEmpty()){
		document = one;
	    }
	}
	return document;
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
    private String findActioners(){
	String back = "";
	if(!document_id.isEmpty()){
	    TimeActionList tal = new TimeActionList();
	    tal.setDocument_id(document_id);
	    tal.setApproversAndProcessorsOnly();
	    tal.setActiveOnly();
	    back = tal.find();
	    if(back.isEmpty()){
		List<TimeAction> ones = tal.getTimeActions();
		if(ones != null && ones.size() > 0){
		    actions = ones;
		}
	    }
	    if(actions != null){
		for(TimeAction action:actions){
		    if(action.isApproveAction()){
			approver = action.getActioner();
			approver_id = approver.getId();
		    }
		    else if(action.isProcessAction()){
			processor = action.getActioner();
			processor_id  = processor.getId();
		    }
		}
	    }
	}
	return back;
    }
    public boolean hasApprover(){
	if(id.isEmpty() && approver_id.isEmpty()){
	    findActioners();
	}
	return !approver_id.isEmpty();
    }
    public boolean hasProcessor(){
	if(hasApprover()){
	    return !processor_id.isEmpty();
	}
	return false;
    }
    public Employee getApprover(){
	if(approver == null && !approver_id.isEmpty()){
	    Employee one = new Employee(approver_id);
	    if(one.doSelect().isEmpty()){
		approver = one;
	    }
	}
	return approver;
    }
    
    public Employee getProcessor(){
	if(processor == null && !processor_id.isEmpty()){
	    Employee one = new Employee(processor_id);
	    if(one.doSelect().isEmpty()){
		processor = one;
	    }
	}
	return processor;
    }
    //
    public String doSelect(){
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "select id,document_id,request_reason,date_format(request_date,'%m/%d/%Y %h:%i'),request_by_id,approver_id,processor_id,notification_status "+
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
		setApprover_id(rs.getString(6));
		setProcessor_id(rs.getString(7));
		setNotificationStatus(rs.getString(8));
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
	String qq = " insert into cancel_requests values(0,?,?,now(),?,?,?,?)";
	findActioners();
	if(request_reason.isEmpty()){
	    msg = "request reason is required";
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
	    if(approver_id.isEmpty()){
		pstmt.setNull(4, Types.INTEGER);
	    }
	    else{
		pstmt.setString(4, approver_id);
	    }
	    if(processor_id.isEmpty()){
		pstmt.setNull(5, Types.INTEGER);
	    }
	    else{
		pstmt.setString(5, processor_id);
	    }
	    pstmt.setString(6, notification_status);
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
    approver_id int unsigned,
    processor_id int unsigned,
    notification_status enum('Success','Failure'),
    primary key(id),
    foreign key(document_id) references time_documents(id),        
    foreign key(request_by_id) references employees(id),
    foreign key(approver_id) references employees(id),
    foreign key(processor_id) references employees(id)
    )engine=InnoDB;

    
    */
}
