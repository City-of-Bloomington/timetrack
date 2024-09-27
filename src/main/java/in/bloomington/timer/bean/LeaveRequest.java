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
    String id="", start_date="", end_date="",
	job_id="", initiated_by="", request_date="", request_details="";
    String start_date_ff="", end_date_ff="";
    float total_hours = 24.0f;
    String earn_code_id="2"; // PTO
    JobTask job = null;
    Group group = null;
    Document document = null;
    Employee employee = null;
    PayPeriod payPeriod = null;
    String pay_period_id = ""; //current only
    GroupManager manager = null;
    //
    public LeaveRequest(){

    }
    public LeaveRequest(String val){
	//
	setId(val);
    }		
    public LeaveRequest(String val, String val2, String val3, String val4, String val5, Float val6, String val7, String val8, String val9, String val10, String val11){
	setId(val);
	setJob_id(val2);	
	setStartDate(val3);
	setLastDate(val4);
	setEarn_code_id(val5);
	setTotalHours(val6);	
	setRequestDetails(val7);
	setInitiated_by(val8);
	setRequestDate(val9);
	setStartDateFF(val10);
	setEndDateFF(val11);		
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
    public String getEarn_code_id(){
	return earn_code_id;
    }    
    public float getTotalHours(){
	return total_hours;
    }
    public String getRequestDate(){
	return request_date;
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
    public void setEarn_code_id(String val){
	if(val != null)
	    earn_code_id = val;
    }
    public void setTotalHours(Float val){
	if(val != null)
	    total_hours=val;
    }
    public void setRequestDate(String val){
	if(val != null)
	    request_date=val;
    }
    public boolean hasNotes(){
	return !request_details.isEmpty();
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
    public String getEarnCode(){
	String ret = "";
	if(!earn_code_id.isEmpty()){
	    HourCode one = new HourCode(earn_code_id);
	    String back = one.doSelect();
	    if(back.isEmpty()){
		ret = one.getName();
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
	return start_date_ff+" - "+end_date_ff;
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
    // for notification
    public GroupManager getManager(){
	findGroupManager();
	return manager;
    }    
    void findGroupManager(){
	if(manager == null){
	    GroupManagerList gml = new GroupManagerList();
	    getGroup();
	    gml.setGroup_id(group.getId());
	    gml.setApproversOnly();
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
	String qq = "select id,job_id,date_format(start_date,'%Y-%m-%d'),date_format(end_date,'%Y-%m-%d'),hour_code_id,total_hours,request_details,initiated_by,date_format(request_date,'%m/%d/%Y'), date_format(start_date,'%m/%d/%Y'), date_format(end_date,'%m/%d/%Y') "+
	    "from leave_requests where id=?";
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
		setJob_id(rs.getString(2));
		setStartDate(rs.getString(3));
		setLastDate(rs.getString(4));
		setEarn_code_id(rs.getString(5));
		setTotalHours(rs.getFloat(6));
		setRequestDetails(rs.getString(7));
		setInitiated_by(rs.getString(8));
		setRequestDate(rs.getString(9));
		setStartDateFF(rs.getString(10));
		setEndDateFF(rs.getString(11));		
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
	String qq = " insert into leave_requests values(0,?,?,?,?, ?,?,?,now())";
	if(start_date.isEmpty()){
	    msg = "start date is required";
	    return msg;
	}
	if(end_date.isEmpty()){
	    msg = "end date is required";
	    return msg;
	}
	if(earn_code_id.isEmpty()){
	    msg = "Hour Code is required";
	    return msg;
	}
	if(total_hours <= 16){
	    msg = "Total hours must be more than 16 ";
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
				
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, job_id);
	    pstmt.setDate(2, new java.sql.Date(dateFormat.parse(start_date).getTime()));
	    pstmt.setDate(3, new java.sql.Date(dateFormat.parse(end_date).getTime()));
	    pstmt.setString(4, earn_code_id);
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
	return msg;
    }
    public String doUpdate(){
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", str="", qq2="";
	String qq = " update leave_requests set job_id=?, start_date=?,end_date=?,hour_code_id=?,total_hours=?,request_details=?,initiated_by=? where id=?";
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
	if(earn_code_id.isEmpty()){
	    msg = "Hour Code is required";
	    return msg;
	}
	if(total_hours <= 16){
	    msg = "Total hours must be more than 16 ";
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
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, job_id);
	    pstmt.setDate(2, new java.sql.Date(dateFormat.parse(start_date).getTime()));
	    pstmt.setDate(3, new java.sql.Date(dateFormat.parse(end_date).getTime()));
	    pstmt.setString(4, earn_code_id);
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
	return msg;
    }		

}
/*
create table leave_requests(
id int unsigned auto_increment,
job_id int unsigned not null,
start_date date not null,
end_date date not null,
hour_code_id int unsigned,
total_hours decimal(5,2),
request_details varchar(1024),
initiated_by int unsigned not null,
request_date date,
primary key(id),
foreign key(job_id) references jobs(id),
foreign key(initiated_by) references employees(id)
)engine=InnoDB;






 */
