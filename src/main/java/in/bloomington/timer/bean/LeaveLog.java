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

public class LeaveLog implements java.io.Serializable{

    static final long serialVersionUID = 3700L;
    static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
    static SimpleDateFormat df2 = new SimpleDateFormat("MM/dd/yyyy");
    static Logger logger = LogManager.getLogger(LeaveLog.class);
    String id="", leave_id="", start_date="", end_date="",
	job_id="", initiated_by="", request_date="", request_details="";
    // String start_date_ff="", end_date_ff="";
    String review_id="", status="", // Approved, Denied, Cancelled
	reviewed_by="", review_date="", review_notes="", cancel_reason="";
    
    float total_hours = 0.0f;
    String[] earn_code_ids = {""}; // PTO
    JobTask job = null;
    Group group = null;
    Document document = null;
    Employee employee = null, reviewer=null;
    PayPeriod payPeriod = null;
    String pay_period_id = ""; //current only
    GroupManager manager = null;
    //
    // review variables
    //
    //
    public LeaveLog(){

    }
    public LeaveLog(String val){
	//
	setId(val);
    }		
    public LeaveLog(String val, String val2, String val3, String val4, String val5, String[] val6, Float val7, String val8, String val9, String val10, String val11, String val12, String val13, String val14,
			String val15){
	setId(val);
	setLeave_id(val2);
	setJob_id(val3);	
	setStartDate(val4);
	setLastDate(val5);
	setEarn_code_ids(val6);
	setTotalHours(val7);	
	setRequestDetails(val8);
	setInitiated_by(val9);
	setRequestDate(val10);
	setReview_id(val11);
	setReviewDate(val12);
	setReviewStatus(val13);
	setReviewNotes(val14);	
	setReviewed_by(val15);

    }		
    public boolean equals(Object obj){
	if(obj instanceof LeaveRequest){
	    LeaveRequest one =(LeaveRequest)obj;
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
    public String getLeave_id(){
	return leave_id;
    }
    public String getReview_id(){
	return review_id;
    }    
    // for yyyy-mm-dd format
    public String getStartDate(){
	return start_date;
    }
    public String getLastDate(){
	return end_date;
    }
    public String getRequestDetails(){
	return request_details;
    }
    public String getInitiated_by(){
	return initiated_by;
    }
    public String getJob_id(){
	return job_id;
    }
    public String[] getEarn_code_ids(){
	return earn_code_ids;
    }    
    public float getTotalHours(){
	return total_hours;
    }
    public String getRequestDate(){
	return request_date;
    }
    public String getReviewDate(){
	return review_date;
    }    
    public String getReviewStatus(){
	return status;
    }
    public String getReviewNotes(){
	return review_notes;
    }
    //
    // setters
    //
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setLeave_id(String val){
	if(val != null)
	    leave_id = val;
    }
    public void setReview_id(String val){
	if(val != null)
	    review_id = val;
    }    
    public void setStartDate(String val){
	if(val != null)
	    start_date=val;
    }
    public void setLastDate(String val){
	if(val != null)
	    end_date=val;
    }
    public void setRequestDetails(String val){
	if(val != null)
	    request_details=val;
    }
    public void setInitiated_by(String val){
	if(val != null)
	    initiated_by=val;
    }
    public void setJob_id(String val){
	if(val != null)
	    job_id=val;
    }
    public void setEarn_code_ids(String[] vals){
	if(vals != null)
	    earn_code_ids = vals;
    }
    public void setTotalHours(Float val){
	if(val != null)
	    total_hours=val;
    }
    public void setRequestDate(String val){
	if(val != null)
	    request_date=val;
    }
    public void setReviewDate(String val){
	if(val != null)
	    review_date=val;
    }    
    public void setReviewStatus(String val){
	if(val != null)
	    status=val;
    }
    public void setReviewed_by(String val){
	if(val != null)
	    reviewed_by=val;
    }
    public void setReviewNotes(String val){
	if(val != null)
	    review_notes=val;
    }    
    public boolean hasNotes(){
	return !request_details.isEmpty();
    }
    public boolean hasReviewer(){
	return !reviewed_by.isEmpty();
    }
    public boolean hasReviewNotes(){
	return !review_notes.isEmpty();
    }
    public boolean isSameDayLeave(){
	return start_date.equals(end_date);
    }
    public boolean isApproved(){
	return status.equals("Approved");
    }
    public boolean isDenied(){
	return status.equals("Denied");
    }    
    public boolean isCancelled(){
	return status.equals("Cancelled");
    }
    public boolean isNotReviewed(){
	return status.isEmpty() || status.equals("Pending");
    }
    public String toString(){
	return id;
    }

    public JobTask getJob(){
	if(job == null && !job_id.isEmpty()){
	    JobTask one = new JobTask(job_id);
	    String back = one.doSelect();
	    if(back.isEmpty()){
		job = one;
	    }
	}
	return job;
    }
    public Group getGroup(){
	if(group == null){
	    getJob();
	    if(job != null){
		group = job.getGroup();
	    }
	}
	return group;
    }

    public String getEarnCodes(){
	String ret = "";
	if(earn_code_ids != null){
	    for(String str:earn_code_ids){
		HourCode one = new HourCode(str);
		String back = one.doSelect();
		if(back.isEmpty()){
		    if(!ret.isEmpty()) ret += ", ";
		    ret += one.getCodeInfo();
		}
	    }
	}
	return ret;
    }

    public String getJobTitle(){
	getJob();
	if(job != null){
	    return job.getTitle();
	}
	return "";
    }
    public Employee getEmployee(){
	if(employee == null){
	    getJob();
	    if(job != null)
		employee = job.getEmployee();
	}
	return employee;
    }
    public Employee getReviewer(){
	if(reviewer == null){
	    if(!reviewed_by.isEmpty()){
		Employee one = new Employee(reviewed_by);
		String back = one.doSelect();
		if(back.isEmpty()){
		    reviewer = one;
		}
	    }
	}
	return reviewer;
    }    
    public String getDate_range(){
	String ret = "";
	if(start_date != null && !start_date.isEmpty()){	
	    if(start_date.equals(end_date)){
		    ret = start_date;
	    }
	    else{
		ret = start_date+" - "+end_date;
	    }
	}
	return ret;
    }
    //
    public String doSelect(){
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "select t.id,"+
	    "t.leave_id,"+
	    "t.job_id,"+
	    "date_format(t.start_date,'%m/%d/%Y'), "+
	    "date_format(t.end_date,'%m/%d/%Y'),"+
	    "t.hour_code_ids,"+
	    "t.total_hours,"+
	    "t.request_details,"+
	    "t.initiated_by,"+
	    "date_format(t.request_date,'%m/%d/%Y'),"+ //10
	    "t.review_id,"+
	    "date_format(t.review_date,'%m/%d/%Y'),"+
	    "t.review_status,"+
	    "t.review_notes, "+
	    "t.reviewed_by "+
	    "from leave_logs t "+
	    "where t.id=?";
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
		String earn_codes = rs.getString(6);
		String[] arr = {""};
		if(earn_codes != null){
		    if(earn_codes.indexOf(",") > 0){
			try{
			    arr = earn_codes.split(",");
			}catch(Exception ex){
			    System.err.println(ex);
			}
		    }
		    else{
			arr[0] = earn_codes;
		    }
		}
		setLeave_id(rs.getString(2));
		setJob_id(rs.getString(3));		
		setStartDate(rs.getString(4));
		setLastDate(rs.getString(5));
		setEarn_code_ids(arr);
		setTotalHours(rs.getFloat(7));
		setRequestDetails(rs.getString(8));
		setInitiated_by(rs.getString(9));
		setRequestDate(rs.getString(10));
		setReview_id(rs.getString(11));
		setReviewDate(rs.getString(12));		
		setReviewStatus(rs.getString(13));
		setReviewed_by(rs.getString(14));
		setReviewNotes(rs.getString(15));
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
	String msg="";
	String qq = " insert into leave_logs values(0,?,?,?,?, ?,?,?,?,?,"+
	    "?,?,?,?,?)";
	con = UnoConnect.getConnection();	    
	if(con == null){
	    msg = "Could not connect to DB ";
	    return msg;
	}
	String earn_codes_str = "";
	for(String str:earn_code_ids){
	    if(!earn_codes_str.isEmpty()) earn_codes_str +=",";
	    earn_codes_str += str;
	}
	if(earn_codes_str.trim().isEmpty()){
	    msg = "Hour Codes are required";
	    return msg;
	}
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, leave_id);	    
	    pstmt.setString(2, job_id);
	    pstmt.setString(3, start_date);
	    pstmt.setString(4, end_date);	    
	    pstmt.setString(5, earn_codes_str);
	    pstmt.setFloat(6, total_hours);
	    if(request_details.isEmpty()){
		pstmt.setNull(7, Types.VARCHAR);
	    }
	    else
		pstmt.setString(7, request_details);
	    pstmt.setString(8, initiated_by);
	    pstmt.setDate(9, new java.sql.Date(df2.parse(request_date).getTime()));	    
	    if(review_id.isEmpty()){
		pstmt.setNull(10, Types.INTEGER);
		pstmt.setNull(11, Types.VARCHAR);
		pstmt.setString(12, "Pending");
		pstmt.setNull(13, Types.VARCHAR);
		pstmt.setNull(14, Types.VARCHAR);
	    }
	    else{
		pstmt.setString(10, review_id);	    
		pstmt.setString(11, review_date);
		pstmt.setString(12, status);
		pstmt.setString(13, review_notes);
		pstmt.setString(14, reviewed_by);
		
	    }
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

}

/*
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
review_id int unsigned,
review_date date,
review_status enum('Approved','Denied','Cancelled'),
review_notes varchar(1024),
reviewed_by int unsigned,
primary key(id),
foreign key(leave_id) references leave_requests(id),
foreign key(review_id) references leave_reviews(id),
foreign key(initiated_by) references employees(id),
foreign key(reviewed_by) references employees(id)
)engine=InnoDB;

 */
