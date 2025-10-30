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

public class LeaveEmailLogList{

    static Logger logger = LogManager.getLogger(LeaveEmailLogList.class);
    static final long serialVersionUID = 3800L;
    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");	
    String email_from="", email_to="", date_from = "", date_to="", limit="100";
    List<LeaveEmailLog> logs = null;
    public LeaveEmailLogList(){
    }
    public void setEmail_to(String val){
	if(val != null)
	    email_to = val;
    }
    public void setEmail_from(String val){
	if(val != null)
	    email_from = val;
    }
    public void setDate_from(String val){
	if(val != null)
	    date_from = val;
    }
    public void setDate_to(String val){
	if(val != null)
	    date_to = val;
    }
    public String getEmail_to(){
	return email_to;
    }
    public String getEmail_from(){
	return email_from;
    }
    public String getDate_from(){
	return date_from ;
    }
    public String getDate_to(){
	return date_to;
    }    
    public void setNoLimit(){
	limit = "";
    }
    public List<LeaveEmailLog> getLogs(){
	return logs;
    }

    public String find(){
		
	String back = "";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection con = UnoConnect.getConnection();
	String qq = "select id,email_to,email_from, email_msg,date_format(sent_date,'%m/%d/%Y'),emaiL_type,error_msg "+
	    "from leave_emaiL_logs ";
	String qw = "";
	if(con == null){
	    back = "Could not connect to DB";
	    return back;
	}
	try{
	    if(!email_to.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";
		qw += " email_to like ? ";
	    }
	    if(!email_from.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";
		qw += " email_from like ? ";
	    }
	    if(!date_from.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";
		qw += " sent_date >= ? ";
	    }
	    if(!date_to.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";
		qw += " sent_date <= ? ";
	    }	    
	    if(!qw.isEmpty()){
		qq += " where "+qw;
	    }
	    qq += " order by id desc ";
	    if(!limit.isEmpty()){
		qq += " limit "+limit;
	    }
	    logger.debug(qq);
	    pstmt = con.prepareStatement(qq);
	    int jj=1;
	    if(!email_to.isEmpty()){
		pstmt.setString(jj++, email_to+"%");
	    }
	    if(!email_from.isEmpty()){
		pstmt.setString(jj++, email_from+"%");
	    }
	    if(!date_from.isEmpty()){
		java.util.Date date_tmp = df.parse(date_from);
		pstmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
	    }
	    if(!date_to.isEmpty()){
		java.util.Date date_tmp = df.parse(date_to);
		pstmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));		    }	    	    
	    rs = pstmt.executeQuery();
	    if(logs == null)
		logs = new ArrayList<>();
	    while(rs.next()){
		LeaveEmailLog one =
		    new LeaveEmailLog(rs.getString(1),
				      rs.getString(2),
				      rs.getString(3),
				      rs.getString(4),
				      rs.getString(5),
				      rs.getString(6),
				      rs.getString(7));
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






















































