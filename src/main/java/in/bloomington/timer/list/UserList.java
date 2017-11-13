package in.bloomington.timer.list;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.*;
import java.sql.*;
import java.io.*;
import javax.naming.*;
import javax.naming.directory.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;

public class UserList extends CommonInc{

		static final long serialVersionUID = 3400L;
		static Logger logger = Logger.getLogger(UserList.class);
    String id = "", username="", last_name="", full_name="";

		boolean active_only = false;
		List<User> users = null;
    //
    // basic constructor
    public UserList(){

    }
    public UserList(String val){

				username =  val;
    }
    //
    // setters
    //
		public void setUsername(String val){
				if(val != null)
						username = val;
		}
		public void setLast_name(String val){
				if(val != null)
						last_name = val;
		}
		public void setActiveOnly(){
				active_only = true;
		}
		public List<User> getUsers(){
				return users;
		}
		public  String find(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				
				String qq = "select u.id,u.username,u.first_name,u.last_name,u.role,u.inactive "+
						" from users u ";
				String qw = "";
				if(!username.equals("")){
						if(!qw.equals("")) qw += " and ";
						qw += " u.username like ? ";
				}
				if(!last_name.equals("")){ // for auto_complete service
						if(!qw.equals("")) qw += " and ";
						qw += " u.last_name like ? ";
				}
				if(active_only){
						if(!qw.equals("")) qw += " and ";
						qw += " u.inactive is null ";
				}
				if(!qw.equals(""))
						qq += " where "+qw;
				qq += " order by u.last_name,u.first_name ";
				String back = "";
				try{
						logger.debug(qq);
						con = Helper.getConnection();
						if(con == null){
								back = "Could not connect to DB ";
								return back;
						}
						pstmt = con.prepareStatement(qq);
						int jj = 1;
						if(!username.equals("")){
								pstmt.setString(jj++,username);
						}
						if(!last_name.equals("")){ // for auto_complete 
								pstmt.setString(jj++,last_name+"%");
						}						
						rs = pstmt.executeQuery();
						while(rs.next()){
								if(users == null)
										users = new ArrayList<>();
								User user = new User(rs.getString(1),
																		 rs.getString(2),
																		 rs.getString(3),
																		 rs.getString(4),
																		 rs.getString(5),
																		 rs.getString(6) != null);
								if(!users.contains(user))
										users.add(user);
						}
				}
				catch(Exception ex){
						back += ex;
						logger.error(ex+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(con, pstmt, rs);
				}
				return back;
    }
}






















































