package in.bloomington.timer.list;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.*;
import java.sql.*;
import java.io.*;
import javax.sql.*;
import java.text.SimpleDateFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;

public class TermNotificationList{

    static Logger logger = LogManager.getLogger(TermNotificationList.class);
    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");	
    static final long serialVersionUID = 3800L;
    String date_from = "", date_to="", department_id="";
    boolean recent_only = false, set_limit=true;
    List<TermNotification> notifications = null;
	
    public TermNotificationList(){
    }
    public void setDate_from(String val){
	if(val != null)
	    date_from =  val;
    }
    public void setDate_to(String val){
	if(val != null)
	    date_to =  val;
    }
    public void setDepartment_id(String val){
	if(val != null)
	    department_id =  val;
    }    
    public String getDate_from(){
	return date_from ;
    }
    public String getDate_to(){
	return date_to ;
    }
    public String getDepartment_id(){
	return department_id ;
    }        
    public void setRecent_only(){
	recent_only = true;
    }
    
    public List<TermNotification> getNotifications(){
	return notifications;
    }
    void checkLimitFlag(){
	if(!date_from.isEmpty() || !date_to.isEmpty() || !department_id.isEmpty()){
	    set_limit = false;
	}
    }
    public String find(){
		
	String back = "";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	checkLimitFlag();
	Connection con = UnoConnect.getConnection();
	String qq = "select t.id,date_format(t.send_time,'%m/%d/%Y %H:%i'), "+
	    "t.sender_id, t.termination_id, t.recipient_emails, t.email_text, t.send_error from term_notifications t";
				
	if(con == null){
	    back = "Could not connect to DB";
	    return back;
	}
	String qw = "";
	try{
	    if(!date_from.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";
		qw += " send_date >= ? ";
	    }
	    if(!date_to.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";
		qw += " send_date <= ? ";
	    }
	    if(!department_id.isEmpty()){
		qq += " join emp_terminations et on et.id=t.termination_id ";
		if(!qw.isEmpty()) qw += " and ";
		qw += " et.department_id = ? ";		
	    }
	    if(!qw.isEmpty()){
		qq += " where "+qw;
	    }
	    qq += " order by id desc ";
	    if(set_limit)
		qq += " limit 10 ";
	    logger.debug(qq);
	    pstmt = con.prepareStatement(qq);
	    int jj=1;
	    if(!date_from.isEmpty()){
		java.util.Date date_tmp = df.parse(date_from);
		pstmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
	    }
	    if(!date_to.isEmpty()){
		java.util.Date date_tmp = df.parse(date_to);
		pstmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
	    }
	    if(!department_id.isEmpty()){
		pstmt.setString(jj++, department_id);
	    }
	    rs = pstmt.executeQuery();
	    if(notifications == null)
		notifications = new ArrayList<>();
	    while(rs.next()){
		TermNotification one =
		    new TermNotification(rs.getString(1),
					 rs.getString(2),
					 rs.getString(3),
					 rs.getString(4),
					 rs.getString(5),
					 rs.getString(6),
					 rs.getString(7)
					 );
		if(!notifications.contains(one))
		    notifications.add(one);
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






















































