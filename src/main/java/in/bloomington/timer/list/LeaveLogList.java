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

public class LeaveLogList{

    static Logger logger = LogManager.getLogger(LeaveRequestList.class);
    static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    static SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");    
    static final long serialVersionUID = 3800L;
    String leave_id="", date_from="", date_to="", sortBy="t.id desc";
    String date_from_ff="", date_to_ff="";
    String pay_period_id = "", initiated_by="";
    String limit="";
    String group_id="", group_ids=""; // for reviewers
    String filter_emp_id="";
    List<LeaveLog> logs = null;
		
    public LeaveLogList(){
	
    }
    public LeaveLogList(String val){
	setLeave_id(val);
    }    
    public List<LeaveLog> getLogs(){
	return logs;
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
    
    public void setPay_period_id(String val){
	if(val != null)
	    pay_period_id = val;
    }
    public void setMaxLimit(int val){
	if(val > 0){
	    limit = ""+val;
	}
    }
    public void setFilter_emp_id(String val){
	if(val != null)
	    filter_emp_id = val;
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
	    "from leave_logs t ";
	con = UnoConnect.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    return back;
	}
	String qw = "";
	try{
	    if(!leave_id.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";
		qw += " t.leave_id = ? ";
	    }
	    if(!pay_period_id.isEmpty()){
		qq += ", pay_periods p ";
		if(!qw.isEmpty()) qw += " and ";
		qw += " (p.start_date <= t.start_date or t.end_date >= p.start_date)";
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
	    /**
	    if(!filter_emp_id.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";
		qw += " j.employee_id = ? ";
	    }
	    */
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
	    if(!leave_id.isEmpty()){
		pstmt.setString(jj++,leave_id);
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
	    if(!filter_emp_id.isEmpty()){
		// pstmt.setString(jj++, filter_emp_id);
	    }	    
	    rs = pstmt.executeQuery();
	    if(logs == null)
		logs = new ArrayList<>();
	    while(rs.next()){
		String[] arr = {""};
		String hr_codes = rs.getString(6);
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
		LeaveLog one = new LeaveLog(rs.getString(1),
					    rs.getString(2),
					    rs.getString(3),		
					    rs.getString(4),
					    rs.getString(5),
					    arr,
					    rs.getFloat(7),
					    rs.getString(8),
					    rs.getString(9),
					    rs.getString(10),
					    rs.getString(11),
					    rs.getString(12),		
					    rs.getString(13),
					    rs.getString(14),
					    rs.getString(15));
		logs.add(one);
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






















































