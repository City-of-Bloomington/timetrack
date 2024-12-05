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

public class LeaveReviewList{

    static Logger logger = LogManager.getLogger(LeaveReviewList.class);
    static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy"); 
    static final long serialVersionUID = 3800L;
    String leave_id="", date_from="", date_to="", sortBy="id desc";
    String date_from_ff="", date_to_ff="";
    String employee_id="";
    String reviewed_by="";
    List<LeaveReview> reviews = null;
		
    public LeaveReviewList(){
    }
    public List<LeaveReview> getReviews(){
	return reviews;
    }
		
    public void setLeave_id(String val){
	if(val != null)
	    leave_id = val;
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
    public void setReviewed_by(String val){
	if(val != null)
	    reviewed_by=val;
    }
    public void setEmployee_id(String val){
	if(val != null)
	    employee_id=val;
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
	String qq = "select r.id,r.leave_id,date_format(r.review_date,'%m/%d/%Y'),r.review_status,r.review_notes,r.reviewed_by "+
	    "from leave_reviews r join leave_requests l on l.id=r.leave_id "+
	    "join jobs j on j.id=l.job_id ";
	if(con == null){
	    back = "Could not connect to DB";
	    return back;
	}
	String qw = "";
	try{
	    if(!leave_id.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";
		qw += " leave_id = ? ";
	    }
	    if(!date_from.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";
		qw += " review_date >= ? ";		
	    }
	    if(!date_to.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";
		qw += " review_date <= ? ";
	    }
	    if(!reviewed_by.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";
		qw += " reviewed_by = ? ";
	    }
	    if(!date_from_ff.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";
		qw += " review_date >= ? ";		
	    }
	    if(!date_to_ff.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";
		qw += " review_date <= ? ";
	    }
	    if(!employee_id.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";
		qw += " j.employee_id = ? ";
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
	    if(!leave_id.isEmpty()){
		pstmt.setString(jj++,leave_id);
	    }
	    if(!date_from.isEmpty()){
		pstmt.setDate(jj++, new java.sql.Date(dateFormat.parse(date_from).getTime()));
	    }
	    if(!date_to.isEmpty()){
		pstmt.setDate(jj++, new java.sql.Date(dateFormat.parse(date_to).getTime()));
	    }
	    if(!reviewed_by.isEmpty()){
		pstmt.setString(jj++,reviewed_by);
	    }
	    if(!date_from_ff.isEmpty()){
		pstmt.setString(jj++, date_from_ff);
	    }
	    if(!date_to_ff.isEmpty()){
		pstmt.setString(jj++, date_to_ff);
	    }
	    if(!employee_id.isEmpty()){
		pstmt.setString(jj++,employee_id);
	    }	    
	    rs = pstmt.executeQuery();
	    if(reviews == null)
		reviews = new ArrayList<>();
	    while(rs.next()){
		LeaveReview one =
		    new LeaveReview(rs.getString(1),
				     rs.getString(2),
				     rs.getString(3),
				     rs.getString(4),
				     rs.getString(5),
				     rs.getString(6));
		if(!reviews.contains(one))
		    reviews.add(one);
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






















































