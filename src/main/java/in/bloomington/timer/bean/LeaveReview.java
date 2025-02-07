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
    String id="", status="", // Approved, Denied, Cancelled
	leave_id="", reviewed_by="", date="", notes="", cancel_reason="";
    boolean activeMail = false;
    String mail_host = "";
    Employee user = null;
    //
    // the form will have 3 leave request max
    //
    String notes_1="",notes_2="",notes_3="", notes_4="", notes_5="";
    String notes_6="",notes_7="",notes_8="", notes_9="", notes_10="";    
    String rev_status_1="",rev_status_2="",rev_status_3="", rev_status_4="",
	rev_status_5="";
    String rev_status_6="",rev_status_7="",rev_status_8="", rev_status_9="",
	rev_status_10="";    
    String leave_id_1="",leave_id_2="",leave_id_3="", leave_id_4="",
	leave_id_5="";
    String leave_id_6="",leave_id_7="",leave_id_8="", leave_id_9="",
	leave_id_10="";    
    LeaveRequest leave = null
	;
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
    public void setCancelReason(String val){
	if(val != null)
	    cancel_reason=val;
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
    public void setLeave(LeaveRequest val){
	if(val != null)
	    leave=val;
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
    public void setLeave_id_4(String val){
	if(val != null)
	    leave_id_4=val;
    }
    public void setLeave_id_5(String val){
	if(val != null)
	    leave_id_5=val;
    }
    public void setLeave_id_6(String val){
	if(val != null)
	    leave_id_6=val;
    }
    public void setLeave_id_7(String val){
	if(val != null)
	    leave_id_7=val;
    }
    public void setLeave_id_8(String val){
	if(val != null)
	    leave_id_8=val;
    }
    public void setLeave_id_9(String val){
	if(val != null)
	    leave_id_9=val;
    }
    public void setLeave_id_10(String val){
	if(val != null)
	    leave_id_10=val;
    }    
    public void setRev_status_1(String val){
	if(val != null && !val.equals("-1"))
	    rev_status_1=val;
    }
    public void setRev_status_2(String val){
	if(val != null && !val.equals("-1"))
	    rev_status_2=val;
    }
    public void setRev_status_3(String val){
	if(val != null && !val.equals("-1"))
	    rev_status_3=val;
    }
    public void setRev_status_4(String val){
	if(val != null && !val.equals("-1"))
	    rev_status_4=val;
    }
    public void setRev_status_5(String val){
	if(val != null && !val.equals("-1"))
	    rev_status_5=val;
    }
    public void setRev_status_6(String val){
	if(val != null && !val.equals("-1"))
	    rev_status_6=val;
    }
    public void setRev_status_7(String val){
	if(val != null && !val.equals("-1"))
	    rev_status_7=val;
    }
    public void setRev_status_8(String val){
	if(val != null && !val.equals("-1"))
	    rev_status_8=val;
    }
    public void setRev_status_9(String val){
	if(val != null && !val.equals("-1"))
	    rev_status_9=val;
    }
    public void setRev_status_10(String val){
	if(val != null && !val.equals("-1"))
	    rev_status_10=val;
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
    public void setNotes_4(String val){
	if(val != null)
	    notes_4=val;
    }
    public void setNotes_5(String val){
	if(val != null)
	    notes_5=val;
    }
    public void setNotes_6(String val){
	if(val != null)
	    notes_6=val;
    }
    public void setNotes_7(String val){
	if(val != null)
	    notes_7=val;
    }
    public void setNotes_8(String val){
	if(val != null)
	    notes_8=val;
    }
    public void setNotes_9(String val){
	if(val != null)
	    notes_9=val;
    }
    public void setNotes_10(String val){
	if(val != null)
	    notes_10=val;
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
	if(!leave_id_4.isEmpty() && !rev_status_4.isEmpty()){
	    String str = saveOne(leave_id_4, rev_status_4, notes_4);
	    if(!str.isEmpty()){
		if(!back.isEmpty()) back += ", ";
		back += "Leave request "+leave_id_4+" enncountered this problem "+str;
	    }else if(activeMail){
		informEmployee(leave_id_4, rev_status_4, notes_4);
	    }
	}
	if(!leave_id_5.isEmpty() && !rev_status_5.isEmpty()){
	    String str = saveOne(leave_id_5, rev_status_5, notes_5);
	    if(!str.isEmpty()){
		if(!back.isEmpty()) back += ", ";
		back += "Leave request "+leave_id_5+" enncountered this problem "+str;
	    }else if(activeMail){
		informEmployee(leave_id_5, rev_status_5, notes_5);
	    }
	}
	if(!leave_id_6.isEmpty() && !rev_status_6.isEmpty()){
	    String str = saveOne(leave_id_6, rev_status_6, notes_6);
	    if(!str.isEmpty()){
		if(!back.isEmpty()) back += ", ";
		back += "Leave request "+leave_id_6+" enncountered this problem "+str;
	    }else if(activeMail){
		informEmployee(leave_id_6, rev_status_6, notes_6);
	    }
	}
	if(!leave_id_7.isEmpty() && !rev_status_7.isEmpty()){
	    String str = saveOne(leave_id_7, rev_status_7, notes_7);
	    if(!str.isEmpty()){
		if(!back.isEmpty()) back += ", ";
		back += "Leave request "+leave_id_7+" enncountered this problem "+str;
	    }else if(activeMail){
		informEmployee(leave_id_7, rev_status_7, notes_7);
	    }
	}
	if(!leave_id_8.isEmpty() && !rev_status_8.isEmpty()){
	    String str = saveOne(leave_id_8, rev_status_8, notes_8);
	    if(!str.isEmpty()){
		if(!back.isEmpty()) back += ", ";
		back += "Leave request "+leave_id_8+" enncountered this problem "+str;
	    }else if(activeMail){
		informEmployee(leave_id_8, rev_status_8, notes_8);
	    }
	}
	if(!leave_id_9.isEmpty() && !rev_status_9.isEmpty()){
	    String str = saveOne(leave_id_9, rev_status_9, notes_9);
	    if(!str.isEmpty()){
		if(!back.isEmpty()) back += ", ";
		back += "Leave request "+leave_id_9+" enncountered this problem "+str;
	    }else if(activeMail){
		informEmployee(leave_id_9, rev_status_9, notes_9);
	    }
	}
	if(!leave_id_10.isEmpty() && !rev_status_10.isEmpty()){
	    String str = saveOne(leave_id_10, rev_status_10, notes_10);
	    if(!str.isEmpty()){
		if(!back.isEmpty()) back += ", ";
		back += "Leave request "+leave_id_10+" enncountered this problem "+str;
	    }else if(activeMail){
		informEmployee(leave_id_10, rev_status_10, notes_10);
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
	LeaveRequest leave = new LeaveRequest(leave_one);
	msg = leave.doSelect();
	msg = leave.addLeaveLog();
	return msg;
    }
    public String doCancel(){
	Connection con = null;
	PreparedStatement pstmt = null, pstmt2=null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = " select id from leave_reviews where leave_id=?";
	if(leave_id.isEmpty()){
	    msg = "Leave request id not set ";
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
	    rs = pstmt.executeQuery();
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
	if(id.isEmpty()){
	    msg = performCancel();
	}
	else if(msg.isEmpty()){
	    status = "Cancelled";
	    notes = "Request is cancelled by the employee for the following reason: "+cancel_reason;
	    msg = doUpdate();
	}
	return msg;
    }
    public String performCancel(){	
	Connection con = null;
	PreparedStatement pstmt = null, pstmt2=null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = " insert into leave_reviews values(0,?,now(),?,?, ?)";
	if(leave_id.isEmpty()){
	    msg = "Leave request is required";
	    return msg;
	}
	status = "Cancelled";
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = "Could not connect to DB ";
	    return msg;
	}
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, leave_id);
	    pstmt.setString(2, status);
	    cancel_reason = "Request cancelled by the employee for the following reason: "+cancel_reason;
	    pstmt.setString(3, cancel_reason);
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
	String qq = " update leave_reviews set review_status=?,review_notes=?,reviewed_by=?,review_date=CURDATE() where id=?";
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
	String email_cc = "";
	String email_to = "";
	String group_id = "";
	Employee manager = null;
	List<LeaveReceiver> receivers = null;
	LeaveRequest leave = new LeaveRequest(leave_one);
	back = leave.doSelect();
	if(!back.isEmpty()){
	    back += " could not get leave record "+back;
	    return back;
	}
	if(rev_status_one.equals("Approved")){
	    group_id = leave.getGroup_id();
	    if(!group_id.isEmpty()){
		LeaveReceiverList lrr = new LeaveReceiverList();
		lrr.setGroup_id(group_id);
		back = lrr.find();
		if(back.isEmpty()){
		    List<LeaveReceiver> ones = lrr.getReceivers();
		    if(ones != null && ones.size() > 0){
			receivers = ones;
		    }
		}
	    }
	    else{
		logger.error("Group id not set ");
	    }
	}
	Employee emp = leave.getEmployee();
	subject = "[Time Track] Leave request for "+emp.getFull_name();
	if(emp != null){
	    email_to = emp.getEmail();
	}
	email_from = user.getEmail();
	email_cc = email_from; // user to himself
	email_msg += "Your leave request for "+leave.getTotalHours()+" hours of '"+leave.getEarnCodes()+"' for your "+leave.getJobTitle()+" position ";
	if(leave.isSameDayLeave()){
	    email_msg += " on "+leave.getDate_range();
	}
	else{
	    email_msg += " between "+leave.getDate_range();
	}
	email_msg += " is "+rev_status_one+"\n\n";
	email_msg += "Leave Description: "+leave.getRequestDetails()+"\n\n";
	if(!rev_notes_one.isEmpty()){
	    email_msg += "Review notes: "+rev_notes_one+"\n\n";
	    
	}	    
	if(rev_status_one.equals("Approved")){
	    email_msg += "Please update your availability in your personal calendar, department time off calendar, and any out of office notifications.\n\n";
	}	
	//
	MailHandle mailer = new
	    MailHandle(mail_host,
		       email_to,
		       email_from,
		       email_cc, // cc
		       null,
		       subject,
		       email_msg
		       );
	back += mailer.send();
	LeaveEmailLog lel = new LeaveEmailLog(
					      email_to,
					      email_from,
					      email_msg,
					      "Review",
					      back);
	back += lel.doSave();
	if(receivers != null){
	    email_to = "";
	    email_cc = "";
	    email_msg = "";
	    for(LeaveReceiver one: receivers){
		if(email_to.isEmpty()){
		    email_to = one.getEmail();
		}
		else if(email_cc.isEmpty()){
		    email_cc = one.getEmail();
		}
		else{
		    email_cc +=","+one.getEmail();
		    
		}
	    }
	    email_msg = "Leave for  "+emp.getFull_name();
	    if(leave.isSameDayLeave()){
		email_msg += " on "+leave.getDate_range();
		email_msg +=" has been approved.\n\n";		    
	    }
	    else{
		
		email_msg += " has been approved for "+leave.getDate_range()+
		    ". Leave begins "+leave.getStartDateFF()+" and the last day on leave is "+leave.getLastDateFF()+"\n\n.";
	    }

	    ///
	    mailer = new
		MailHandle(mail_host,
			   email_to,
			   email_from,
			   email_cc, // cc
			   null,
			   subject,
			   email_msg
			   );
	    back += mailer.send();
	    lel = new LeaveEmailLog(
				    email_to,
				    email_from,
				    email_msg,
				    "Receive",
				    back);
	    
	}
	
	return back;
    }    
}
/*

create table leave_reviews(
id int unsigned auto_increment,
leave_id int unsigned not null,
review_date date,
review_status enum('Approved','Denied','Cancelled'),
review_notes varchar(1024),
reviewed_by int unsigned not null,
primary key(id),
foreign key(leave_id) references leave_requests(id),
foreign key(reviewed_by) references employees(id)
)engine=InnoDB;

alter table leave_reviews modify review_status enum('Approved','Denied','Cancelled');
;;
;;
create table leave_logs(
id int unsigned auto_increment,
leave_id int unsigned not null,
job_id int unsigned not null,
start_date date not null,
end_date date not null,
hour_code_ids varchar(54),
total_hours decimal(5,2),
request_details varchar(1024),
initiated_by int unsigned not null,
request_date date,
review_id int unsigned not null,
review_date date,
review_status enum('Approved','Denied','Cancelled'),
review_notes varchar(1024),
reviewed_by int unsigned not null,
primary key(id),
foreign key(leave_id) references leave_requests(id),
foreign key(review_id) references leave_reviews(id),
foreign key(initiated_by) references employees(id),
foreign key(reviewed_by) references employees(id)
)engine=InnoDB;


 */
