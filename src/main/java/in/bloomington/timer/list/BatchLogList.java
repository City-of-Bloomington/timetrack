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

public class BatchLogList{

		static Logger logger = LogManager.getLogger(BatchLogList.class);
		static final long serialVersionUID = 3800L;
		List<BatchLog> logs = null;
	
		public BatchLogList(){
		}
		public List<BatchLog> getLogs(){
				return logs;
		}

		public String find(){
		
				String back = "";
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				Connection con = UnoConnect.getConnection();
				String qq = "select id,names,run_by,date_format(date,'%m/%d/%Y %h:%i'),failure_reason from batch_logs order by id desc limit 5 ";
				if(con == null){
						back = "Could not connect to DB";
						return back;
				}
				try{
						logger.debug(qq);
						pstmt = con.prepareStatement(qq);
						rs = pstmt.executeQuery();
						if(logs == null)
								logs = new ArrayList<BatchLog>();
						while(rs.next()){
								BatchLog one =
										new BatchLog(rs.getString(1),
																 rs.getString(2),
																 rs.getString(3),
																 rs.getString(4),
																 rs.getString(5));
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






















































