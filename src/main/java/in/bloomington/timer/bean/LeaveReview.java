package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.List;
import java.sql.*;
import java.text.SimpleDateFormat;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LeaveReview implements java.io.Serializable{

    static final long serialVersionUID = 3700L;
    static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd"); 
    static Logger logger = LogManager.getLogger(LeaveReview.class);
    String id="", status="", // Approved, Denied
	leave_id="", reviewed_by="", date="", notes="";
    LeaveRequest leave = null;
    Employee reviewer = null;
    //
    public LeaveReview(){

    }
    public LeaveReview(String val){
	//
	setId(val);
    }		
    public LeaveReview(String val, String val2, String val3, String val4, String val5, String val6){
	setId(val);
	setLeave_id(val2);	
	setDate(val3);
	setReviewStatus(val4);
	setNotes(val5);	
	setReviewed_by(val6);
    }		
    public boolean equals(Object obj){
	if(obj instanceof LeaveReview){
	    LeaveReview one =(LeaveReview)obj;
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
    // for yyyy-mm-dd format
    public String getDate(){
	return date;
    }
    public String getNotes(){
	return notes;
    }
    public String getReviewed_by(){
	return reviewed_by;
    }
    public String getLeave_id(){
	return leave_id;
    }
    public String getReviewStatus(){
	return status;
    }    
    //
    // setters
    //
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setDate(String val){
	if(val != null)
	    date=val;
    }
    public void setNotes(String val){
	if(val != null)
	    notes=val;
    }
    public void setReviewStatus(String val){
	if(val != null)
	    status=val;
    }    
    public void setReviewed_by(String val){
	if(val != null)
	    reviewed_by=val;
    }
    public void setLeave_id(String val){
	if(val != null)
	    leave_id=val;
    }
    public boolean hasNotes(){
	return !notes.isEmpty();
    }
    public String toString(){
	return id;
    }
    public LeaveRequest getLeave(){
	if(leave == null && !leave_id.isEmpty()){
	    LeaveRequest one = new LeaveRequest(leave_id);
	    String back = one.doSelect();
	    if(back.isEmpty()){
		leave = one;
	    }
	}
	return leave;
    }
    public Employee getReviewer(){
	if(reviewer == null && !reviewed_by.isEmpty()){
	    Employee one = new Employee(reviewed_by);
	    String back = one.doSelect();
	    if(back.isEmpty()){
		reviewer  = one;
	    }
	}
	return reviewer;
    }
    //
    public String doSelect(){
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "select id,leave_id,date_format(review_date,'%m/%d/%Y'),review_status,review_notes,reviewed_by "+
	    "from leave_reviews where id=?";
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
		setLeave_id(rs.getString(2));
		setDate(rs.getString(3));
		setReviewStatus(rs.getString(4));
		setNotes(rs.getString(5));
		setReviewed_by(rs.getString(6));
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
	String qq = " insert into leave_reviews values(0,?,now(),?,?, ?)";
	if(leave_id.isEmpty()){
	    msg = "Leave request is required";
	    return msg;
	}
	if(status.isEmpty()){
	    msg = "Review Status is required";
	    return msg;
	}
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = "Could not connect to DB ";
	    return msg;
	}
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, leave_id);
	    pstmt.setString(2, status);
	    if(notes.isEmpty()){
		pstmt.setNull(3, Types.VARCHAR);
	    }
	    else
		pstmt.setString(3, notes);
	    pstmt.setString(4, reviewed_by);	    
	    pstmt.executeUpdate();
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
	doSelect();
	return msg;
    }
    public String doUpdate(){
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", str="", qq2="";
	String qq = " update leave_reviews set review_status=?,review_notes=?,reviewed_by=? where id=?";
	if(id.isEmpty()){
	    msg = " id is required";
	    return msg;
	}
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = "Could not connect to DB ";
	    return msg;
	}
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, status);
	    if(notes.isEmpty()){
		pstmt.setNull(2, Types.VARCHAR);
	    }
	    else
		pstmt.setString(2, notes);
	    pstmt.setString(3, reviewed_by);
	    pstmt.setString(4, id);
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
	doSelect();
	return msg;
    }		

}
/*

create table leave_reviews(
id int unsigned auto_increment,
leave_id int unsigned not null,
review_date date,
review_status enum('Approved','Denied'),
review_notes varchar(1024),
reviewed_by int unsigned not null,
primary key(id),
foreign key(leave_id) references leave_requests(id),
foreign key(reviewed_by) references employees(id)
)engine=InnoDB;




 */
