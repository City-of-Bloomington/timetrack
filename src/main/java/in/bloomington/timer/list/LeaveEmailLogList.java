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

public class LeaveEmailLogList{

    static Logger logger = LogManager.getLogger(LeaveEmailLogList.class);
    static final long serialVersionUID = 3800L;
    List<LeaveEmailLog> logs = null;
	
    public LeaveEmailLogList(){
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
	    "from leave_emaiL_logs order by id desc limit 20";
	if(con == null){
	    back = "Could not connect to DB";
	    return back;
	}
	try{
	    logger.debug(qq);
	    pstmt = con.prepareStatement(qq);
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






















































