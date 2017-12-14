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

public class EmployeesLogList{

		static Logger logger = LogManager.getLogger(EmployeesLogList.class);
		static final long serialVersionUID = 3800L;
		List<EmployeesLog> logs = null;
	
		public EmployeesLogList(){
		}
		public List<EmployeesLog> getLogs(){
				return logs;
		}

		public String find(){
		
				String back = "";
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				Connection con = Helper.getConnection();
				String qq = "select id,emps_id_set,date_format(date,'%m/%d/%Y %h:%i'),status,errors from employees_logs order by id desc limit 10 ";
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
								EmployeesLog one =
										new EmployeesLog(rs.getString(1),
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
						Helper.databaseDisconnect(con, pstmt, rs);
				}
				return back;
		}
}






















































