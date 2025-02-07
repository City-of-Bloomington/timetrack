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

public class LeaveRequest implements java.io.Serializable{

    static final long serialVersionUID = 3700L;
    static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd"); 
    static Logger logger = LogManager.getLogger(LeaveRequest.class);
    String id="", start_date="", end_date="", group_id="",
	job_id="", initiated_by="", request_date="", request_details="";
    String start_date_ff="", end_date_ff="";
    float total_hours = 0.0f;
    String cancel_reason = "";
    String[] earn_code_ids = {""}; // PTO
    JobTask job = null;
    Group group = null;
    Document document = null;
    Employee employee = null, reviewer=null;
    PayPeriod payPeriod = null;
    String pay_period_id = ""; //current only
    GroupManager manager = null;
    List<LeaveLog> leaveLogs = null;
    //
    // review variables
    String status="", reviewed_by="", review_notes="", review_id="", review_date="";
    //
    //
    public LeaveRequest(){

    }
    public LeaveRequest(String val){
	//
	setId(val);
    }		
    public LeaveRequest(String val, String val2, String val3, String val4, String[] val5, Float val6, String val7, String val8, String val9, String val10, String val11, String val12, String val13, String val14, String val15, String val16, String val17){
	setId(val);
	setJob_id(val2);	
	setStartDate(val3);
	setLastDate(val4);
	setEarn_code_ids(val5);
	setTotalHours(val6);	
	setRequestDetails(val7);
	setInitiated_by(val8);
	setRequestDate(val9);
	setStartDateFF(val10);
	setEndDateFF(val11);
	setReviewStatus(val12);
	setReviewed_by(val13);
	setReviewNotes(val14);
	setReview_id(val15);
	setReviewDate(val16);
	setGroup_id(val17);
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
    // for yyyy-mm-dd format
    public String getStartDate(){
	return start_date;
    }
    public String getLastDate(){
	return end_date;
    }
    // for mm/dd/format
    public String getStartDateFF(){
	return start_date_ff;
    }
    public String getLastDateFF(){
	return end_date_ff;
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
    public String getGroup_id(){
	return group_id;
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
    public String getReviewStatus(){
	return status;
    }
    public String getReviewNotes(){
	return review_notes;
    }
    public String getReview_id(){
	return review_id;
    }
    public String getReviewDate(){
	return review_date;
    }    
    //
    // setters
    //
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setStartDate(String val){
	if(val != null)
	    start_date=val;
    }
    public void setLastDate(String val){
	if(val != null)
	    end_date=val;
    }
    public void setStartDateFF(String val){
	if(val != null)
	    start_date_ff=val;
    }
    public void setEndDateFF(String val){
	if(val != null)
	    end_date_ff=val;
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
    public void setGroup_id(String val){
	if(val != null)
	    group_id=val;
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
    public void setReview_id(String val){
	if(val != null)
	    review_id = val;
    }
    public void setReviewDate(String val){
	if(val != null)
	    review_date = val;
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
    public void setCancelReason(String val){
	if(val != null)
	    cancel_reason = val;
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
	    if(!group_id.isEmpty()){
		Group one = new Group(group_id);
		String back = one.doSelect();
		if(back.isEmpty()){
		    group = one;
		}
	    }
	    else{
		getJob();
		if(job != null){
		    group = job.getGroup();
		    group_id = job.getGroup_id();
		}
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
    void findCurrentPayPeriod(){
	if(pay_period_id.isEmpty()){
	    PayPeriodList ppl = new PayPeriodList();
	    ppl.currentOnly();
	    String back = ppl.find();
	    if(back.isEmpty()){
		List<PayPeriod> ones = ppl.getPeriods();
		if(ones != null && ones.size() > 0){
		    payPeriod = ones.get(0);
		    pay_period_id = payPeriod.getId();
		}
	    }
	}
    }
    public Document getDocument(){
	if(pay_period_id.isEmpty()){
	    findCurrentPayPeriod();
	}
	if(document == null && !job_id.isEmpty()){
	    DocumentList dl = new DocumentList();
	    dl.setJob_id(job_id);
	    dl.setPay_period_id(pay_period_id);
	    String back = dl.find();
	    if(back.isEmpty()){
		List<Document> ones = dl.getDocuments();
		if(ones != null && ones.size() > 0){
		    document = ones.get(0);
		}
	    }
	}
	return document;
    }
    public String getDate_range(){
	String ret = "";
	if(start_date_ff != null && !start_date_ff.isEmpty()){	
	    if(start_date_ff.equals(end_date_ff)){
		    ret = start_date_ff;
	    }
	    else{
		ret = start_date_ff+" - "+end_date_ff;
	    }
	}
	return ret;
    }
    public String getManagerName(){
	String full_name = "";
	findGroupManager();
	if(manager != null){
	    Employee emp = manager.getEmployee();
	    full_name = emp.getFull_name();
	}
	return full_name;
    }
    // if approved or not reviewed yet
    public boolean canBeCancelled(){
	if(isNotReviewed() || isApproved()){
	    if(payPeriod == null)
		findCurrentPayPeriod();
	    return (start_date.compareTo(payPeriod.getStartDateYmd()) >= 0);
	}
	return false;
    }
    // Not reviewed yet
    public boolean canBeEdited(){
	if(isNotReviewed()){
	    if(payPeriod == null)
		findCurrentPayPeriod();
	    return (start_date.compareTo(payPeriod.getStartDateYmd()) >= 0);
	}
	return false;
    }
    public List<LeaveLog> getLeaveLogs(){
	if(!id.isEmpty() && leaveLogs == null){
	    findLeaveLogs();
	}
	return leaveLogs;
    }
    void findLeaveLogs(){
	if(!id.isEmpty()){
	    LeaveLogList lll = new LeaveLogList(id);
	    String back = lll.find();
	    if(back.isEmpty()){
		List<LeaveLog> logs = lll.getLogs();
		if(logs != null && logs.size() > 0){
		    leaveLogs = logs;
		}
	    }
	}
    }
    public boolean hasLeaveLogs(){
	getLeaveLogs();
	return leaveLogs != null && leaveLogs.size() > 0;
    }
    // for notification
    public GroupManager getManager(){
	findGroupManager();
	return manager;
    }    
    void findGroupManager(){
	if(manager == null){
	    GroupManagerList gml = new GroupManagerList();
	    if(group_id.isEmpty()){
		getGroup();
	    }
	    gml.setGroup_id(group_id);
	    gml.setLeaveReviewOnly();
	    gml.setActiveOnly();
	    gml.setNotExpired();
	    String back = gml.find();
	    if(back.isEmpty()){
		List<GroupManager> managers = gml.getManagers();
		if(managers != null && managers.size() > 0){
		manager = managers.get(0);
		}
	    }
	}
    }
    //
    public String doSelect(){
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "select t.id,t.job_id,t.start_date,t.end_date,t.hour_code_ids,t.total_hours,t.request_details,t.initiated_by,date_format(t.request_date,'%m/%d/%Y'), date_format(t.start_date,'%m/%d/%Y'), date_format(t.end_date,'%m/%d/%Y'),r.review_status,r.reviewed_by,r.review_notes,r.id,r.review_date,j.group_id "+
	    "from leave_requests t "+
	    "join jobs j on j.id=t.job_id "+
	    " left join leave_reviews r on r.leave_id=t.id "+
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
		String earn_codes = rs.getString(5);
		String[] arr = {""};
		if(earn_codes != null){
		    if(earn_codes.indexOf(",") > 0){
			try{
			    arr = earn_codes.split(",");
			}catch(Exception ex){
			    System.err.println(ex);
			}		    }
		    else{
			arr[0] = earn_codes;
		    }
		}
		setJob_id(rs.getString(2));
		setStartDate(rs.getString(3));
		setLastDate(rs.getString(4));
		setEarn_code_ids(arr);
		setTotalHours(rs.getFloat(6));
		setRequestDetails(rs.getString(7));
		setInitiated_by(rs.getString(8));
		setRequestDate(rs.getString(9));
		setStartDateFF(rs.getString(10));
		setEndDateFF(rs.getString(11));
		if(rs.getString(12) == null){
		    setReviewStatus("Pending");
		}
		else{
		    setReviewStatus(rs.getString(12));
		}
		setReviewed_by(rs.getString(13));
		setReviewNotes(rs.getString(14));
		setReview_id(rs.getString(15));
		setReviewDate(rs.getString(16));
		setGroup_id(rs.getString(17));
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
	String qq = " insert into leave_requests values(0,?,?,?,?, ?,?,?,now())";
	if(start_date.isEmpty()){
	    msg = "start date is required";
	    return msg;
	}
	if(end_date.isEmpty()){
	    msg = "end date is required";
	    return msg;
	}
	if(start_date.compareTo(end_date) > 0){
	    msg ="Start date must be less or equal End Date";
	    return msg;
	}
	if(payPeriod == null)
	    findCurrentPayPeriod();
	if(payPeriod != null){
	    if(start_date.compareTo(payPeriod.getStartDateYmd()) < 0){
		msg = "Start date can not be before the current pay period";
		return msg;
	    }
	}
	// we can use start_date > end_date 
	if(start_date.compareTo(end_date) > 0){
	    msg = "Start date can not be more than end date "; 
	}
	String today = Helper.getToday();
	
	if(earn_code_ids == null){
	    msg = "Hour Code is required";
	    return msg;
	}
	if(total_hours <= 0){
	    msg = "Total hours must be set ";
	    return msg;
	}
	if(job_id.isEmpty()){
	    msg = "Job is required";
	    return msg;
	}	
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
	    pstmt.setString(1, job_id);
	    pstmt.setString(2, start_date);
	    pstmt.setString(3, end_date);	    
	    pstmt.setString(4, earn_codes_str);
	    pstmt.setFloat(5, total_hours);
	    if(request_details.isEmpty()){
		pstmt.setNull(6, Types.VARCHAR);
	    }
	    else
		pstmt.setString(6, request_details);
	    pstmt.setString(7, initiated_by);	    
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
	addLeaveLog();
	return msg;
    }
    public String doUpdate(){
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", qq2="";
	String qq = " update leave_requests set job_id=?, start_date=?,end_date=?,hour_code_ids=?,total_hours=?,request_details=?,initiated_by=? where id=?";
	if(id.isEmpty()){
	    msg = "job id is required";
	    return msg;
	}
	if(start_date.isEmpty()){
	    msg = "start date is required";
	    return msg;
	}
	if(end_date.isEmpty()){
	    msg = "end date is required";
	    return msg;
	}
	if(earn_code_ids == null){
	    msg = "Hour Code is required";
	    return msg;
	}
	if(total_hours <= 0){
	    msg = "Total hours must be set ";
	    return msg;
	}
	if(job_id.isEmpty()){
	    msg = "Job is required";
	    return msg;
	}
	String earn_codes_str = "";
	for(String str:earn_code_ids){
	    if(!earn_codes_str.isEmpty()) earn_codes_str +=",";
	    earn_codes_str += str;
	}
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = "Could not connect to DB ";
	    return msg;
	}
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, job_id);
	    pstmt.setString(2, start_date);
	    pstmt.setString(3, end_date);	   	    
	    pstmt.setString(4, earn_codes_str);
	    pstmt.setFloat(5, total_hours);
	    if(request_details.isEmpty()){
		pstmt.setNull(6, Types.VARCHAR);
	    }
	    else
		pstmt.setString(6, request_details);
	    pstmt.setString(7, initiated_by);
	    pstmt.setString(8, id);
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
	addLeaveLog("Edited");
	return msg;
    }
    public String addLeaveLog(){
	return addLeaveLog("");
    }
    public String addLeaveLog(String newStatus){
	LeaveLog log = new LeaveLog();
	log.setLeave_id(id);
	log.setJob_id(job_id);	
	log.setStartDate(start_date);
	log.setLastDate(end_date);
	log.setEarn_code_ids(earn_code_ids);
	log.setTotalHours(total_hours);	
	log.setRequestDetails(request_details);
	log.setInitiated_by(initiated_by);
	log.setRequestDate(request_date);
	log.setReview_id(review_id);
	log.setReviewDate(review_date);
	if(newStatus.isEmpty()){
	    log.setReviewStatus(status);
	    log.setReviewed_by(reviewed_by);	    
	}
	else{
	    log.setReviewStatus(newStatus);
	    getEmployee();
	    log.setReviewed_by(employee.getId());
	}
	log.setReviewNotes(review_notes);	
	String msg = log.doSave();
	return msg;
    }
}

/*
create table leave_requests(
id int unsigned auto_increment,
job_id int unsigned not null,
start_date date not null,
end_date date not null,
hour_code_ids varchar(54),
total_hours decimal(5,2),
request_details varchar(1024),
initiated_by int unsigned not null,
request_date date,
primary key(id),
foreign key(job_id) references jobs(id),
foreign key(initiated_by) references employees(id)
)engine=InnoDB;






 */
