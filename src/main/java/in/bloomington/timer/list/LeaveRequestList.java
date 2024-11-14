package in.bloomington.timer.list;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.*;
import java.sql.*;
import java.io.*;
import java.text.SimpleDateFormat;
import javax.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;

public class LeaveRequestList{

    static Logger logger = LogManager.getLogger(LeaveRequestList.class);
    static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    static SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");    
    static final long serialVersionUID = 3800L;
    String job_id="", date_from="", date_to="", sortBy="t.id desc";
    String date_from_ff="", date_to_ff="";
    String pay_period_id = "", initiated_by="";
    String limit="";
    String group_id="", group_ids=""; // for reviewers
    String filter_emp_id="";
    String ids_to_ignore = ""; 
    boolean active_only = false;
    boolean is_reviewed = false, not_reviewed=false;
    boolean approved_only = false;

    List<LeaveRequest> requests = null;
		
    public LeaveRequestList(){
    }
    public List<LeaveRequest> getRequests(){
	return requests;
    }
		
    public void setJob_id(String val){
	if(val != null)
	    job_id = val;
    }
    public void setDate_from(String val){
	if(val != null)
	    date_from = val;
    }
    public void setDate_to(String val){
	if(val != null)
	    date_to = val;
    }
    public void setDate_from_ff(String val){
	if(val != null)
	    date_from_ff = val;
    }
    public void setDate_to_ff(String val){
	if(val != null)
	    date_to_ff = val;
    }    
    
    public void setPay_period_id(String val){
	if(val != null)
	    pay_period_id = val;
    }
    public void setInitiated_by(String val){
	if(val != null)
	    initiated_by=val;
    }
    public void setGroup_id(String val){
	if(val != null)
	    group_id=val;
    }
    public void setGroup_ids(String val){ // comma separated
	if(val != null)
	    group_ids=val;
    }
    public void setMaxLimit(int val){
	if(val > 0){
	    limit = ""+val;
	}
    }
    public void setIdsToIgnore(String val){ // comma separated
	if(val != null)
	    ids_to_ignore=val;
    }
    public void setFilter_emp_id(String val){
	if(val != null)
	    filter_emp_id = val;
    }
    public void setActiveOnly(){
	active_only = true;
    }
    public void setIsReviewed(){
	is_reviewed = true;
    }
    public void setNotReviewed(){
	not_reviewed = true;
    }    
    public void setApprovedOnly(){
	approved_only = true;
    }
    public void setSortBy(String val){
	if(val != null)
	    sortBy = val;
    }
    public void setDecided(){
	is_reviewed = true;
    }
    public String find(){
		
	String back = "";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection con = UnoConnect.getConnection();
	String qq = "select t.id,t.job_id,t.start_date,t.end_date,t.hour_code_ids,t.total_hours,t.request_details,t.initiated_by,date_format(t.request_date,'%m/%d/%Y'), "+
	    "date_format(t.start_date,'%m/%d/%Y'),date_format(t.end_date,'%m/%d/%Y'),r.review_status,r.reviewed_by,r.review_notes "+
	    "from leave_requests t "+
	    "join jobs j on j.id=t.job_id "+
	    "left join leave_reviews r on r.leave_id=t.id ";
	if(con == null){
	    back = "Could not connect to DB";
	    return back;
	}
	String qw = "";
	try{
	    if(!job_id.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";
		qw += " t.job_id = ? ";
	    }
	    if(!pay_period_id.isEmpty()){
		qq += ", pay_periods p ";
		if(!qw.isEmpty()) qw += " and ";
		qw += " p.start_date <= t.start_date ";
		if(approved_only){
		    if(!qw.isEmpty()) qw += " and ";
		    qw += " p.end_date >= t.start_date ";
		}
		qw += " and p.id = ? ";		
	    }
	    if(!date_from.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";
		qw += " (t.start_date >= ? and t.end_date >= ?)";		
	    }
	    if(!date_to.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";
		qw += " (t.start_date <= ? and t.end_date <= ?)";		
	    }
	    if(!date_from_ff.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";
		qw += " (t.start_date >= ? and t.end_date >= ?)";		
	    }
	    if(!date_to_ff.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";
		qw += " (t.start_date <= ? and t.end_date <= ?)";		
	    }	    
	    if(!initiated_by.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";
		qw += " t.initiated_by = ? ";
	    }
	    if(!filter_emp_id.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";
		qw += " j.employee_id = ? ";
	    }	    
	    if(!group_ids.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";
		qw += " j.group_id in ("+group_ids+") ";
	    }
	    else if(!group_id.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";
		qw += " j.group_id = ? ";
	    }
	    if(is_reviewed){
		if(!qw.isEmpty()) qw += " and ";
		qw += " t.id in (select leave_id from leave_reviews)";
	    }
	    else if(not_reviewed){
		if(!qw.isEmpty()) qw += " and ";
		qw += " t.id not in (select leave_id from leave_reviews)";
	    }
	    if(!ids_to_ignore.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";
		qw += " t.id not in ("+ids_to_ignore+")";
	    }
	    if(approved_only){
		if(!qw.isEmpty()) qw += " and ";
		qw += " r.review_status='Approved' ";
		
	    }
	    if(!qw.isEmpty()){
		qq += " where "+qw;
	    }
	    if(!sortBy.isEmpty()){
		qq += " order by "+sortBy;
	    }
	    if(!limit.isEmpty()){
		qq += " limit "+limit;
	    }
	    logger.debug(qq);
	    pstmt = con.prepareStatement(qq);
	    int jj=1;
	    if(!job_id.isEmpty()){
		pstmt.setString(jj++,job_id);
	    }
	    if(!pay_period_id.isEmpty()){
		pstmt.setString(jj++,pay_period_id);
	    }
	    if(!date_from.isEmpty()){
		pstmt.setDate(jj++, new java.sql.Date(dateFormat.parse(date_from).getTime()));
		pstmt.setDate(jj++, new java.sql.Date(dateFormat.parse(date_from).getTime()));		
	    }
	    if(!date_to.isEmpty()){
		pstmt.setDate(jj++, new java.sql.Date(dateFormat.parse(date_to).getTime()));
		pstmt.setDate(jj++, new java.sql.Date(dateFormat.parse(date_to).getTime()));		
	    }
	    if(!date_from_ff.isEmpty()){
		pstmt.setDate(jj++, new java.sql.Date(df2.parse(date_from_ff).getTime()));
		pstmt.setDate(jj++, new java.sql.Date(df2.parse(date_from_ff).getTime()));	
	    }
	    if(!date_to_ff.isEmpty()){
		pstmt.setDate(jj++, new java.sql.Date(df2.parse(date_to_ff).getTime()));
		pstmt.setDate(jj++, new java.sql.Date(df2.parse(date_to_ff).getTime()));	
	    }	    
	    if(!initiated_by.isEmpty()){
		pstmt.setString(jj++,initiated_by);
	    }
	    if(!filter_emp_id.isEmpty()){
		pstmt.setString(jj++, filter_emp_id);
	    }	    
	    if(!group_id.isEmpty()){
		pstmt.setString(jj++, group_id);
	    }
	    rs = pstmt.executeQuery();
	    if(requests == null)
		requests = new ArrayList<>();
	    while(rs.next()){
		String[] arr = {""};
		String hr_codes = rs.getString(5);
		if(hr_codes != null){
		    if(hr_codes.indexOf(",") > 0){
			try{
			    arr = hr_codes.split(",");
			}catch(Exception ex){
			    System.err.println(ex);
			}
		    }
		    else{
			arr[0] = hr_codes;
		    }
		}
		String statusStr = rs.getString(12);
		if(statusStr == null)
		    statusStr = "Pending";
		LeaveRequest one =
		    new LeaveRequest(rs.getString(1),
				     rs.getString(2),
				     rs.getString(3),
				     rs.getString(4),
				     arr,
				     rs.getFloat(6),
				     rs.getString(7),
				     rs.getString(8),
				     rs.getString(9),
				     rs.getString(10),
				     rs.getString(11),
				     statusStr,
				     rs.getString(13),
				     rs.getString(14)
				     );
		if(!requests.contains(one))
		    requests.add(one);
	    }
	}
	catch(Exception ex){
	    back += ex+" : "+qq;
	    logger.error(back);
	}
	finally{
	    Helper.databaseDisconnect(pstmt, rs);
	    UnoConnect.databaseDisconnect(con);
	}
	return back;
    }

		
}






















































