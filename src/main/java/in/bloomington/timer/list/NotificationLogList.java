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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;

public class NotificationLogList{

    static Logger logger = LogManager.getLogger(NotificationLogList.class);
    static final long serialVersionUID = 3800L;
    List<NotificationLog> logs = null;
	
    public NotificationLogList(){
    }
    public List<NotificationLog> getLogs(){
	return logs;
    }

    public String find(){
		
	String back = "";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection con = UnoConnect.getConnection();
	String qq = "select id,receipiants,message,date_format(date,'%m/%d/%y %H:%i'),status,error_msg from notification_logs order by id desc limit 5 ";
	if(con == null){
	    back = "Could not connect to DB";
	    return back;
	}
	try{
	    logger.debug(qq);
	    pstmt = con.prepareStatement(qq);
	    rs = pstmt.executeQuery();
	    if(logs == null)
		logs = new ArrayList<NotificationLog>();
	    while(rs.next()){
		NotificationLog one =
		    new NotificationLog(rs.getString(1),
					rs.getString(2),
					rs.getString(3),
					rs.getString(4),
					rs.getString(5),
					rs.getString(6));
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






















































