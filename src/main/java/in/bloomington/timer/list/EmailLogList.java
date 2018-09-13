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
import in.bloomington.timer.util.CommonInc;
import in.bloomington.timer.bean.EmailLog;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EmailLogList extends CommonInc{

		static Logger logger = LogManager.getLogger(EmailLog.class);
		final static long serialVersionUID = 302L;
		static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		String date_from="", date_to="", date_at="";
		boolean debug = false;
		List<EmailLog> emailLogs = null;
		public EmailLogList(boolean val){
				debug = val;
		}
		public EmailLogList(boolean val, String val2, String val3){
				debug = val;
				setDate_from(val2);
				setDate_to(val3);
		}
		public void setDate_from(String val){
				if(val != null)
						date_from = val;
		}
		public void setDate_to(String val){
				if(val != null)
						date_to = val;
		}
		public List<EmailLog> getEmailLogs(){
				return emailLogs;
		}
		public String find(){
				String back = "";
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				// year worth of data
				String qq = " select id,user_id,email_from,email_to,cc,bcc,subject,text_message,send_errors,date_format(date_time,'%m/%d/%y %H:%i'),type from email_logs order by id desc limit 10 ";
				if(debug)
						logger.debug(qq);
				try{
						con = Helper.getConnection();
						if(con == null){
								back = "Could not connect to DB ";
								return back;
						}
						pstmt = con.prepareStatement(qq);				
						rs = pstmt.executeQuery();
						while(rs.next()){
								EmailLog one =
										new EmailLog(debug,
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
																 rs.getString(11));
								if(emailLogs == null)
										emailLogs = new ArrayList<>();
								emailLogs.add(one);
						}
				}catch(Exception ex){
						logger.error(ex);
				}
				finally{
						Helper.databaseDisconnect(con, pstmt, rs);									
				}
				return back;				
		}
		
}























































