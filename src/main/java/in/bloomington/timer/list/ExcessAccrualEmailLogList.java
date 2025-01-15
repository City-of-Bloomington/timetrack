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
import in.bloomington.timer.bean.ExcessAccrualEmailLog;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExcessAccrualEmailLogList extends CommonInc{

    static Logger logger = LogManager.getLogger(ExcessAccrualEmailLog.class);
    final static long serialVersionUID = 302L;
    static SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
    String date_from="", date_to="", date_at="", limit="50";
    boolean debug = false;
    List<ExcessAccrualEmailLog> logs = null;
    public ExcessAccrualEmailLogList(){
    }		
    public ExcessAccrualEmailLogList(String val, String val2){
	setDate_from(val);
	setDate_to(val2);
    }
    public void setDate_from(String val){
	if(val != null)
	    date_from = val;
    }
    public void setDate_to(String val){
	if(val != null)
	    date_to = val;
    }
    public List<ExcessAccrualEmailLog> getLogs(){
	return logs;
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
	String qw = "";
	String qq = " select id,date_format(sent_date,'%m/%d/%Y'),email_from,email_to,email_cc,email_subject,email_msg,error_msg from excess_accrual_email_logs ";
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
	    }										
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		ExcessAccrualEmailLog one =
		    new ExcessAccrualEmailLog(
				 rs.getString(1),
				 rs.getString(2),
				 rs.getString(3),
				 rs.getString(4),
				 rs.getString(5),
				 rs.getString(6),
				 rs.getString(7),
				 rs.getString(8));
		if(logs == null)
		    logs = new ArrayList<>();
		logs.add(one);
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























































