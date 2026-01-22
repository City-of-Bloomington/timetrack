package in.bloomington.timer.list;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */

import java.util.*;
import java.sql.*;
import java.io.*;
import java.text.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.sql.*;
import in.bloomington.timer.util.Helper;
import in.bloomington.timer.util.UnoConnect;
import in.bloomington.timer.util.CommonInc;
import in.bloomington.timer.bean.PartTimeEmailLog;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PartTimeEmailLogList extends CommonInc{

    static Logger logger = LogManager.getLogger(PartTimeEmailLogList.class);
    final static long serialVersionUID = 302L;
    static SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
    String date_from="", date_to="", date_at="", limit="50";
    String employee_id = "", job_id="";
    Integer week_num = null, warn_type=null;
    boolean debug = false;
    List<PartTimeEmailLog> emailLogs = null;
    public PartTimeEmailLogList(){
    }		
    public PartTimeEmailLogList(String val, String val2){
	setDateFrom(val);
	setDateTo(val2);
    }
    public void setDateFrom(String val){
	if(val != null)
	    date_from = val;
    }
    public void setDateTo(String val){
	if(val != null)
	    date_to = val;
    }
    public void setEmployee_id(String val){
	if(val != null)
	    employee_id = val;
    }
    public void setJob_id(String val){
	if(val != null)
	    job_id = val;
    }    
    public void setWeekNum(Integer val){
	if(val != null)
	    week_num = val;
    }
    public void setWarnType(Integer val){
	if(val != null)
	    warn_type = val;
    }        
    public List<PartTimeEmailLog> getEmailLogs(){
	return emailLogs;
    }
    public void setPageSize(String val){
	if(val != null)
	    limit = val;
    }
    public String getPageSize(){
	return limit;
    }
    public String find(){
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qw = "", qq="";
	qq = " select l.id,"+
	    "l.warn_id,"+	    
	    "l.employee_id,"+
	    "l.supervisor_id,"+
	    "date_format(l.sent_time,'%m/%d/%Y %H:%i'),"+
	    
	    "l.email_to,"+
	    "l.supervisor_cc,"+
	    "l.email_subject,"+
	    "l.text_message,"+
	    "l.send_error,"+
	    
	    "w.id,"+
	    "w.job_id,"+
	    "w.pay_week_num,"+
	    "w.warn_type,"+
	    "w.week_of_year,"+
	    "w.week_total,"+
	    "w.critical_value,"+
	    
	    "concat_ws(' ',e.first_name,e.last_name) employee_name,"+
	    "concat_ws(' ',e2.first_name,e2.last_name) supervisor_name, "+
	    "p.name job_title "+
	    "from part_time_email_logs l "+
	    "join part_time_warns w on w.id=l.warn_id "+
	    "join employees e on l.employee_id = e.id "+
	    "join employees e2 on l.supervisor_id=e2.id "+
	    "left join jobs j on j.id=w.job_id "+
	    "left join positions p on p.id=j.position_id  ";
	if(!date_from.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " l.date_time >= ? ";
	}
	if(!date_to.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " l.date_time <= ? ";
	}
	if(!employee_id.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " l.employee_id = ? ";
	}
	if(!job_id.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " w.job_id = ? ";
	}	
	if(week_num != null){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " w.week_num = ? ";
	}
	if(warn_type != null){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " w.warn_type = ? ";
	}	
	if(!qw.isEmpty()){
	    qq += " where "+qw;
	}
	qq += " order by l.id desc ";
	if(limit.isEmpty()){
	    qq += " limit "+limit;
	}
	if(debug)
	    logger.debug(qq);
	con = UnoConnect.getConnection();
	if(con == null){
	    back = "Could not connect to DB ";
	    return back;
	}
	try{
	    int jj=1;
	    pstmt = con.prepareStatement(qq);
	    if(!date_from.isEmpty()){
		java.util.Date date_tmp = df.parse(date_from);
		pstmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
	    }
	    if(!date_to.isEmpty()){
		java.util.Date date_tmp = df.parse(date_to);
		pstmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
	    }										    if(!employee_id.isEmpty()){
		pstmt.setString(jj++, employee_id);
	    }
	    if(!job_id.isEmpty()){
		pstmt.setString(jj++, job_id);
	    }	    
	    if(week_num != null){
		pstmt.setInt(jj++, week_num);
	    }
	    if(warn_type != null){
		pstmt.setInt(jj++, warn_type);
	    }	    
	    
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		PartTimeEmailLog one =
		    new PartTimeEmailLog(
				 rs.getString(1),
				 rs.getString(2),
				 rs.getString(3),
				 rs.getString(4),
				 rs.getString(5),
				 rs.getString(6),
				 rs.getString(7),
				 rs.getString(8),
				 rs.getString(9),
				 rs.getString(10),
				 rs.getString(11),
				 rs.getString(12),
				 rs.getInt(13),
				 rs.getInt(14),
				 rs.getInt(15),
				 rs.getDouble(16),
				 rs.getInt(17),
				 rs.getString(18),
				 rs.getString(19),
				 rs.getString(20) 
				 
					 );
		if(emailLogs == null)
		    emailLogs = new ArrayList<>();
		emailLogs.add(one);
	    }
	}catch(Exception ex){
	    logger.error(ex);
	}
	finally{
	    Helper.databaseDisconnect(pstmt, rs);
	    UnoConnect.databaseDisconnect(con);
	}
	return back;				
    }
		
}























































