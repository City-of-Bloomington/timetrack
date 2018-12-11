package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.sql.*;
import javax.naming.*;
import javax.naming.directory.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;

public class RollBack{

		static final long serialVersionUID = 3700L;	
		static Logger logger = LogManager.getLogger(RollBack.class);
    String id="", date_time="";
		String date = "";
		int days = 10; // big number
		boolean is_failure = false;
		String[] delqq = {
				"delete from group_employees where id > ?",
				"delete from department_employees where id > ?",
				"delete from group_managers where id > ?",
				"delete from jobs where id > ?",
				"delete from groups where id > ?",
				
				"delete from positions where id > ?",
				"delete from time_actions where document_id in (select id from time_documents where employee_id > ?)",
				"delete from time_documents where employee_id >?",
				"delete from employee_accruals where employee_id >?",
				"delete from employees where id > ?",
		};

			/*
			create table roll_backs(                                                        id int auto_increment primary key,                                              date_time datetime,                                                             group_employees int,                                                            department_employees int,                                                       group_managers int,                                                             jobs int ,                                                                      groups int,                                                                     positions int,                                                                  employees int,                                                                  is_success char(1)                                                              )engine=InnoDB;

			//
			// to start we added this to preserve the previous data
			// 
			insert into roll_backs values(0,now(),104,103,47,46,72,49,116,null);
			
		*/
		String[] table_names = {"group_employees",
														"department_employees",
														"group_managers",
														"jobs",
														"groups",

														"positions",
														"employees"};
		int[] int_vals ={0,0,0,0,0, 0,0};

		
		//
		public RollBack(){

		}
		public boolean isSuccess(){
				return !is_failure;
		}
		public boolean isFailure(){
				return is_failure;
		}
		public String getDate(){
				return date;
		}
		public boolean isCurrent(){
				return days == 0;
		}
    //
    // getters
    //
		public String doPrepare(){
				String back = "";
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String q = "select max(id) from ", qq="";
				String qq2 = "insert into roll_backs values(0,now(),?,?,?, ?,?,?,?,?)";
				con = UnoConnect.getConnection();
				date = Helper.getToday();
				days = 0;
				if(con == null){
						back = "Could not connect to DB";
						return back;
				}
				try{
						logger.debug(qq);
						int jj=0;
						for(String str:table_names){
								qq = q+str;
								pstmt = con.prepareStatement(qq);
								rs = pstmt.executeQuery();
								if(rs.next()){
										int_vals[jj] = rs.getInt(1);
										if(int_vals[jj] == 0) is_failure = true;
								}
								jj++;
						}
						qq = qq2;
						pstmt = con.prepareStatement(qq);
						for(int i=0;i<7;i++){
								pstmt.setInt(i+1, int_vals[i]);
						}
						if(is_failure){
								pstmt.setString(8,"y");
						}
						else{
								pstmt.setNull(8,Types.CHAR);
						}
						pstmt.executeUpdate();
				}
				catch(Exception ex){
						back += ex+":"+qq;
						logger.error(back);
				}
				finally{
						Helper.databaseDisconnect(pstmt, rs);			
				}
				return back;
		}
		public String doRollback(){
				
				String back = "";
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				boolean is_failure = false;
				String qq = "select * from roll_backs order by id desc limit 1";
				con = UnoConnect.getConnection();
				if(con == null){
						back = "Could not connect to DB";
						return back;
				}
				try{
						logger.debug(qq);
						pstmt = con.prepareStatement(qq);
						rs = pstmt.executeQuery();
						if(rs.next()){
								for(int jj=3;jj<11;jj++){
										if(jj == 10){
												String str = rs.getString(jj);
												if(str != null){
														is_failure = true;
														back = " max values problem ";
												}
										}
										else{
												int_vals[jj-3] = rs.getInt(jj);
										}
								}
						}
						for(int i=0;i<int_vals.length;i++){
								if(int_vals[i] == 0){
										is_failure = true;
										break;
								}
						}
						if(isSuccess()){
								int jj=0;
								System.err.println("Rollbacks");
								date = Helper.getToday();
								days = 0;
								for(int j2=0;j2<delqq.length;j2++){
										qq = delqq[j2];
										logger.debug(qq);
										System.err.print(qq+" ");
										jj=j2;
										if(j2 > 6){
												jj=6; // employees table
										}
										pstmt = con.prepareStatement(qq);
										
										System.err.println(int_vals[jj]);
										pstmt.setInt(1, int_vals[jj]);
										pstmt.executeUpdate();
								}
						}
				}
				catch(Exception ex){
						back += ex+":"+qq;
						logger.error(back);
				}
				finally{
						Helper.databaseDisconnect(pstmt, rs);			
				}
				return back;

		}
		public String findLastRollDate(){
				String back = "";
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;				
				String qq = "select date_format(date_time,'%m/%d/%Y'),datediff(now(),date_time) from roll_backs order by id desc limit 1";
				con = UnoConnect.getConnection();
				if(con == null){
						back = "Could not connect to DB";
						return back;
				}
				try{
						logger.debug(qq);
						pstmt = con.prepareStatement(qq);
						rs = pstmt.executeQuery();
						if(rs.next()){
								String str = rs.getString(1);
								if(str != null) date = str;
								days = rs.getInt(2); // days past
						}
				}
				catch(Exception ex){
						back += ex+":"+qq;
						logger.error(back);
				}
				finally{
						Helper.databaseDisconnect(pstmt, rs);			
				}
				return back;
		}
		
}
