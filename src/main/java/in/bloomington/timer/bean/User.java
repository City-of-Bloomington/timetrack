package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.Serializable;
import java.util.Set;
import java.util.List;
import java.util.HashSet;
import java.util.Hashtable;
import java.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;

public class User implements Serializable{

		static Logger logger = LogManager.getLogger(User.class);
		static final long serialVersionUID = 3900L;		
    private String id="", employee_id="",
				email="",
				username="", full_name="", role="", first_name="", last_name="",
				inactive="";
		Employee employee = null;
		String employee_number = "", id_code=""; // needed for employee data
		public User(){
    }
		
    public User(String val){
				setId(val);
    }
    public User(String val, String val2){
				setId(val);
				setUsername(val2);

    }
    public User(String val,
								String val2,
								String val3,
								String val4
								){
				setId(val);
				setUsername(val2);
				setFirst_name(val3);
				setLast_name(val4);
    }		
    public User(String val,
								String val2,
								String val3,
								String val4,
								String val5,
								boolean val6
								){
				setVals(val, val2, val3, val4, val5, val6);
    }
    void setVals(String val,
								String val2,
								String val3,
								String val4,
								String val5,
								boolean val6
								){
				setId(val);
				setUsername(val2);
				setFirst_name(val3);
				setLast_name(val4);
				setRole(val5);
				setInactive(val6);
    }
    //
    // getters
    //
    public String getUsername(){
				return username;
    }
		public String getId(){
				return id;
    }
    public String getFull_name(){
				if(full_name.equals("")){
						full_name = first_name;
						if(!full_name.equals("")) full_name += " ";
						full_name += last_name;
				}
				return full_name;
    }
		public String getFirst_name(){
				return first_name;
    }
		public String getLast_name(){
				return last_name;
    }
		public String getEmployee_number(){
				return employee_number;
    }
		public String getId_code(){
				return id_code;
    }		
    public String getRole(){
				return role;
    }
		public String getEmail(){
				String ret = "";
				if(email.equals("")){
						ret = username+CommonInc.emailStr;
				}
				else{
						ret = email;
				}
				return ret;
		}
		public boolean isInactive(){
				return !inactive.equals("");
		}
		public boolean isActive(){
				return inactive.equals("");
		}		
		public boolean hasRole(String val){
				return role.indexOf(val) > -1;
		}
		public boolean hasNoRole(){
				return role.equals("");
		}
		public boolean isUser(){
				return hasRole("User");
		}
		public boolean isAdmin(){
				return hasRole("Admin");
		}
		public boolean isSuper(){
				return hasRole("Super");
		}

    //
    // setters
    //
    public void setId (String val){
				if(val != null)
						id = val;
    }
    public void setUsername (String val){
				if(val != null)
						username = val;
    }

    public void setFirst_name (String val){
				if(val != null)
						first_name = val;
    }
    public void setLast_name (String val){
				if(val != null)
						last_name = val;
    }
    public void setEmail(String val){
				if(val != null)
						email = val;
    }		
    public void setRole (String val){
				if(val != null && !val.equals("-1"))
						role = val;
    }
    public void setInactive (boolean val){
				if(val)
						inactive = "y";
    }
    public void setEmployee_number(String val){
				if(val != null)
						employee_number = val;
    }
    public void setId_code(String val){
				if(val != null)
						id_code = val;
    }		
		public String toString(){
				return getFull_name();
		}
		public String getInfo(){
				String ret="";
				if(!id.equals("")){
						ret = "id = "+id+", ";
				}
				ret += "username = "+username;
				if(!first_name.equals("")){
						ret += ", first name = "+first_name;
				}
				if(!last_name.equals("")){
						ret += ", last name = "+last_name;
				}
				if(!employee_number.equals("")){
						ret += ", emp # = "+employee_number;
				}
				if(!id_code.equals("")){
						ret += ", id code = "+id_code;
				}
				return ret;
		}
		public boolean equals(Object o) {
				if (o instanceof User) {
						User c = (User) o;
						if ( this.id.equals(c.getId())) 
								return true;
				}
				return false;
		}
		public int hashCode(){
				int seed = 31;
				if(!id.equals("")){
						try{
								seed += Integer.parseInt(id)*47;
						}catch(Exception ex){
								// we ignore
						}
				}
				return seed;
		}

		public String getEmployee_id(){
				if(employee == null){
						Employee emp = new Employee(id);
						String back = emp.doSelect();
						if(back.equals("")){
								employee = emp;
						}
				}
				if(employee != null){
						employee_id = employee.getId();
				}
				return employee_id;
		}

		public Employee getEmployee(){
				if(employee_id.equals("")){
						getEmployee_id(); // will do both
				}
				return employee;
		}
		//
		public boolean hasEmployee(){
				getEmployee();
				return employee != null;
		}
		public String validate(){
				String msg = "";
				if(username.equals("")){
						msg = "username";
				}
				if(first_name.equals("")){
						if(!msg.equals("")) msg += ", ";
						msg += "first name";
				}
				if(last_name.equals("")){
						if(!msg.equals("")) msg += ", ";
						msg += "last name";
				}
				if(!msg.equals("")){
						msg += " required but not set";
				}
				return msg;
		}
		public String doSelect(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "select id,username,first_name,last_name,role,inactive from users where ";
				if(!id.equals("")){
						qq += " id = ? ";
				}
				else if(!username.equals("")){
						qq += " username like ? ";
				}
				else{
						msg = "User info can not be found as no user id is set";
						return msg;
				}
				logger.debug(qq);
				try{
						con = Helper.getConnection();
						if(con != null){
								pstmt = con.prepareStatement(qq);
								if(!id.equals("")){
										pstmt.setString(1, id);
								}
								else if(!username.equals("")){
										pstmt.setString(1, username);
								}
								rs = pstmt.executeQuery();
								//
								if(rs.next()){
										setVals(rs.getString(1),
														rs.getString(2),
														rs.getString(3),
														rs.getString(4),
														rs.getString(5),
														rs.getString(6) != null);
								}
								else{
										msg = "User not found";
								}
						}
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(con, pstmt, rs);
				}
				return msg;
		}
		public String doSave(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;		
				String msg="", str="";
				inactive=""; // default
				String qq = " insert into users values(0,?,?,?,? ,?)";
				if(username.equals("")){
						msg = "username is required";
						return msg;
				}
				if(last_name.equals("")){
						msg = "last name is required";
						return msg;
				}				
				try{
						con = Helper.getConnection();
						if(con == null){
								msg = "Could not connect to DB ";
								return msg;
						}
						pstmt = con.prepareStatement(qq);
						msg = setParams(pstmt);
						if(msg.equals("")){
								pstmt.executeUpdate();
								qq = "select LAST_INSERT_ID()";
								pstmt = con.prepareStatement(qq);
								rs = pstmt.executeQuery();
								if(rs.next()){
										id = rs.getString(1);
								}
						}
						if(!employee_number.equals("")){
								Employee emp = new Employee();
								emp.setId_code(id_code);
								emp.setEmployee_number(employee_number);
								msg = emp.doSave();
								if(msg.equals(""))
										employee = emp;
						}
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(con, pstmt, rs);
				}
				return msg;
		}
		String setParams(PreparedStatement pstmt){
				String msg = "";
				int jj=1;
				try{
						pstmt.setString(jj++, username);
						if(first_name.equals(""))
								pstmt.setNull(jj++, Types.VARCHAR);
						else
								pstmt.setString(jj++, first_name);
						pstmt.setString(jj++, last_name);
						if(role.equals(""))
								pstmt.setNull(jj++, Types.INTEGER);
						else
								pstmt.setString(jj++, role);
						if(inactive.equals(""))
								pstmt.setNull(jj++, Types.CHAR);
						else
								pstmt.setString(jj++, "y");						
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg);
				}
				return msg;
		}
		public String doUpdate(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = " update users set username=?,first_name=?,last_name=?,role=?, inactive=? where id=?";
				if(username.equals("")){
						msg = "username is required";
						return msg;
				}
				if(last_name.equals("")){
						msg = "last name is required";
						return msg;
				}
				if(id.equals("")){
						msg = "user id not set ";
						return msg;
				}				
				try{
						con = Helper.getConnection();
						if(con == null){
								msg = "Could not connect to DB ";
								return msg;
						}
						pstmt = con.prepareStatement(qq);
						msg = setParams(pstmt);
						pstmt.setString(6, id);
						pstmt.executeUpdate();
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(con, pstmt, rs);
				}
				return msg;
		}
		/**
		 * change the iactive status of the following users to inactive
		 * this function is used by CurrentEmployeesHandler
		 */
		public String updateInactiveStatus(String idSet){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = " update users set inactive='y' where id in ("+idSet+")";
				if(idSet == null || idSet.equals("")){
						return msg;
				}
				try{
						con = Helper.getConnection();
						if(con == null){
								msg = "Could not connect to DB ";
								return msg;
						}
						pstmt = con.prepareStatement(qq);
						pstmt.executeUpdate();
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(con, pstmt, rs);
				}
				return msg;
		}				
}
