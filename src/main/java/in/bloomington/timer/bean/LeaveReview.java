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
    boolean activeMail = false;
    String mail_host = "";
    Employee user = null;
    //
    // the form will have 3 leave request max
    //
    String notes_1="",notes_2="",notes_3="";
    String rev_status_1="",rev_status_2="",rev_status_3="";
    String leave_id_1="",leave_id_2="",leave_id_3="";
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
    public void setLeave_id_1(String val){
	if(val != null)
	    leave_id_1=val;
    }
    public void setLeave_id_2(String val){
	if(val != null)
	    leave_id_2=val;
    }
    public void setLeave_id_3(String val){
	if(val != null)
	    leave_id_3=val;
    }    
    public void setRev_status_1(String val){
	if(val != null)
	    rev_status_1=val;
    }
    public void setRev_status_2(String val){
	if(val != null)
	    rev_status_2=val;
    }
    public void setRev_status_3(String val){
	if(val != null)
	    rev_status_3=val;
    }
    public void setNotes_1(String val){
	if(val != null)
	    notes_1=val;
    }
    public void setNotes_2(String val){
	if(val != null)
	    notes_2=val;
    }
    public void setNotes_3(String val){
	if(val != null)
	    notes_3=val;
    }
    public void setActiveMail(){
	activeMail = true;
    }
    public void setMail_host(String val){
	if(val != null)
	    mail_host = val;
    }
    public void setUser(Employee val){
	if(val != null)
	    user = val;
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
	String back = "";
	if(!leave_id_1.isEmpty() && !rev_status_1.isEmpty()){
	    String str = saveOne(leave_id_1, rev_status_1, notes_1);
	    if(!str.isEmpty()){
		back = "Leave request "+leave_id_1+" enncountered this problem "+str;
	    }
	    else if(activeMail){
		informEmployee(leave_id_1, rev_status_1, notes_1);
	    }
	}
	if(!leave_id_2.isEmpty() && !rev_status_2.isEmpty()){
	    String str = saveOne(leave_id_2, rev_status_2, notes_2);
	    if(!str.isEmpty()){
		if(!back.isEmpty()) back += ", ";
		back += "Leave request "+leave_id_2+" enncountered this problem "+str;
	    } else if(activeMail){
		informEmployee(leave_id_2, rev_status_2, notes_2);
	    }

	}
	if(!leave_id_3.isEmpty() && !rev_status_3.isEmpty()){
	    String str = saveOne(leave_id_3, rev_status_3, notes_3);
	    if(!str.isEmpty()){
		if(!back.isEmpty()) back += ", ";
		back += "Leave request "+leave_id_3+" enncountered this problem "+str;
	    }else if(activeMail){
		informEmployee(leave_id_3, rev_status_3, notes_3);
	    }
	}
	return back;
    }
    public String saveOne(String leave_one, String status_one, String rev_notes){	
	Connection con = null;
	PreparedStatement pstmt = null, pstmt2=null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = " insert into leave_reviews values(0,?,now(),?,?, ?)";
	if(leave_one.isEmpty()){
	    msg = "Leave request is required";
	    return msg;
	}
	if(status_one.isEmpty()){
	    msg = "Review Status is required";
	    return msg;
	}
	if(status_one.equals("Denied") && rev_notes.isEmpty()){
	    msg = "If leave is denied a reason must be provided";
	    return msg;
	}
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = "Could not connect to DB ";
	    return msg;
	}
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, leave_one);
	    pstmt.setString(2, status_one);
	    if(rev_notes.isEmpty()){
		pstmt.setNull(3, Types.VARCHAR);
	    }
	    else
		pstmt.setString(3, rev_notes);
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
    String informEmployee(String leave_one,
			  String rev_status_one,
			  String rev_notes_one){
	String back = "";
	String manager_email="";
	String emp_email="";
	String subject = "";
	String email_msg = "";
	String email_from = "";
	String email_to = "";
	Employee manager = null;
	LeaveRequest leave = new LeaveRequest(leave_one);
	back = leave.doSelect();
	if(!back.isEmpty()){
	    back += " could not get leave record "+back;
	    return back;
	}
	Employee emp = leave.getEmployee();
	subject = "Leave review for "+emp.getFull_name();
	if(emp != null){
	    email_to = emp.getEmail();
	}
	email_from = user.getEmail();
	email_msg = "Hi "+emp.getFull_name()+"\n\n";
	email_msg += "Your leave request for "+leave.getTotalHours()+" hrs of "+
	    "'"+leave.getEarnCodes()+"' for your "+leave.getJobTitle()+" position for the period "+leave.getDate_range()+" is "+rev_status_one+" ";
	if(rev_status_one.equals("Denied")){
	    email_msg += " for the following reason(s):\n "+rev_notes_one+".";
	}
	email_msg += "\n\n ";
	email_msg += "Leave Description: "+leave.getRequestDetails()+"\n\n";
	email_msg += "Thanks\n\n";
	email_msg += user.getFull_name();
	email_msg += "\n\n";
	//
	// for logs
	String email_txt = "Hi "+emp.getFull_name()+"\n";
	email_txt += "Your leave request for "+leave.getTotalHours()+" hrs of "+
	    leave.getEarnCodes()+" for your "+leave.getJobTitle()+" position for the period "+leave.getDate_range()+" is "+rev_status_one+" ";
	if(rev_status_one.equals("Denied")){
	    email_txt += " for the following reason(s): "+rev_notes_one+".\n";
	}
	email_txt += "Leave Description: "+leave.getRequestDetails();
	email_txt += "Thanks\n";
	email_txt += user.getFull_name();
	MailHandle mailer = new
	    MailHandle(mail_host,
		       email_to,
		       email_from,
		       email_from, // cc
		       null,											 
		       subject,
		       email_msg
		       );
	back += mailer.send();
	LeaveEmailLog lel = new LeaveEmailLog(
					      email_to,
					      email_from,
					      email_txt,
					      "Review",
					      back);
	back += lel.doSave();
	return back;
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
