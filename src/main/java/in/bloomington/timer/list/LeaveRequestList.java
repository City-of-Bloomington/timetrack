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
    static final long serialVersionUID = 3800L;
    String job_id="", date_from="", date_to="", sortBy="t.id desc";
    String pay_period_id = "", initiated_by="";
    String group_id="", group_ids=""; // for reviewers
    boolean active_only = false;
    boolean not_reviewed = false;
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
    public void setActiveOnly(){
	active_only = true;
    }
    public void setNotReviewed(){
	not_reviewed = true;
    }
    public void setSortBy(String val){
	if(val != null)
	    sortBy = val;
    }
    public String find(){
		
	String back = "";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection con = UnoConnect.getConnection();
	String qq = "select t.id,t.job_id,t.start_date,t.end_date,t.hour_code_ids,t.total_hours,t.request_details,t.initiated_by,date_format(t.request_date,'%m/%d/%Y'), "+
	    "date_format(t.start_date,'%m/%d/%Y'),date_format(t.end_date,'%m/%d/%Y') "+
	    "from leave_requests t ";
	qq += " join jobs j on j.id=t.job_id ";
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
		qw += " (p.start_date >= t.start_date and p.start_date <= p.end_date) or (p.end_date >= t.start_date and p.end_date <= t.end_date) ";
		qw += " and p.id = ? ";
	    }
	    if(!date_from.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";
		qw += " (t.start_date >= ? and t.end_date <= ?)";		
	    }
	    if(!date_to.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";
		qw += " (t.start_date <= ? and t.end_date >= ?)";		
	    }
	    if(!initiated_by.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";
		qw += " t.initiated_by = ? ";
	    }
	    if(!group_ids.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";
		qw += " j.group_id in ("+group_ids+") ";
	    }
	    else if(!group_id.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";
		qw += " j.group_id = ? ";
	    }
	    if(not_reviewed){
		if(!qw.isEmpty()) qw += " and ";
		qw += " t.id not in (select leave_id from leave_reviews)";
	    }
	    if(!qw.isEmpty()){
		qq += " where "+qw;
	    }
	    if(!sortBy.isEmpty()){
		qq += " order by "+sortBy;
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
	    if(!initiated_by.isEmpty()){
		pstmt.setString(jj++,initiated_by);
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
				     rs.getString(11));
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






















































