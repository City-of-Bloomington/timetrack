package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.Serializable;
import java.util.List;
import java.sql.*;
import java.text.SimpleDateFormat;
import org.apache.log4j.Logger;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;

public class DepartmentEmployee{

		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		static final long serialVersionUID = 500L;		
		static Logger logger = Logger.getLogger(DepartmentEmployee.class);
    private String id="", employee_id="", department_id="",
				effective_date="",
				expire_date="",				
				new_department_id="", change_date="";// change department
		boolean active = true;
		Type department = null;
		Employee employee = null;
		public DepartmentEmployee(){
    }
		
    public DepartmentEmployee(String val){
				setId(val);
    }
    public DepartmentEmployee(String val,
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
				setEmployee_id(val2);
				setDepartment_id(val3);
				setEffective_date(val4);
				setExpire_date(val5);
				setActive(val6);
    }
    //
    // getters
    //
    public String getEmployee_id(){
				return employee_id;
    }
		public String getId(){
				return id;
    }
		public String getDepartment_id(){
				return department_id;
    }
		public String getNew_department_id(){
				return new_department_id;
    }		
		public String getEffective_date(){
				if(id.equals(""))
						return CommonInc.default_effective_date;
				return effective_date;
    }
    public String getExpire_date(){
				return expire_date;
    }
    public String getChange_date(){
				return change_date;
    }
		public boolean isActive(){
				return active;
		}
    //
    // setters
    //
    public void setId (String val){
				if(val != null)
						id = val;
    }
    public void setEmployee_id (String val){
				if(val != null)
						employee_id = val;
    }

    public void setDepartment_id (String val){
				if(val != null && !val.equals("-1"))
						department_id = val;
    }
    public void setNew_department_id (String val){
				if(val != null && !val.equals("-1"))
						new_department_id = val;
    }		
    public void setEffective_date (String val){
				if(val != null)
						effective_date = val;
    }		
    public void setChange_date (String val){
				if(val != null)
						change_date = val;
    }		
    public void setExpire_date(String val){
				if(val != null)
						expire_date = val;
    }
		public void setActive(boolean val){
				active = val;
		}
		public Type getDepartment(){
				if(department == null && !department_id.equals("")){
						Type dd = new Type(department_id);
						dd.setTable_name("departments");
						String back = dd.doSelect();
						if(back.equals("")){
								department = dd;
						}

				}
				return department;
		}
		public Employee getEmployee(){
				if(employee == null && !employee_id.equals("")){
						Employee dd = new Employee(employee_id);
						String back = dd.doSelect();
						if(back.equals("")){
								employee = dd;
						}
						else{
								System.err.println(" back "+back);
						}
				}
				return employee;				

		}
		public String toString(){
				return id;
		}
		public boolean equals(Object o) {
				if (o instanceof DepartmentEmployee) {
						DepartmentEmployee c = (DepartmentEmployee) o;
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
		public String doSelect(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "select id,employee_id,department_id,"+
						" date_format(effective_date,'%m/%d/%Y'),"+
						" date_format(expire_date,'%m/%d/%Y'), "+
						" expire_date < now() "+
						" from department_employees where ";
				if(!id.equals("")){
						qq += " id = ? ";
				}
				else if(!employee_id.equals("")){
						qq += " employee_id = ? ";
				}
				else if(!department_id.equals("")){
						qq += " department_id = ? ";
				}
				else{
						msg = " no id set ";
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
								else if(!employee_id.equals("")){
										pstmt.setString(1, employee_id);
								}
								else{
										pstmt.setString(1, department_id);
								}
								rs = pstmt.executeQuery();
								//
								if(rs.next()){
										setVals(rs.getString(1),
														rs.getString(2),
														rs.getString(3),
														rs.getString(4),
														rs.getString(5),
														rs.getString(6) == null);
								}
								else{
										msg = "Department Employee not found";
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
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "insert into department_employees values(0,?,?,?,?) ";
				if(employee_id.equals("")){
						msg = " employee id not set ";
						return msg;
				}
				if(department_id.equals("")){
						msg = " department not set ";
						return msg;
				}				
				logger.debug(qq);
				try{
						con = Helper.getConnection();
						if(con != null){
								pstmt = con.prepareStatement(qq);
								pstmt.setString(1, employee_id);
								pstmt.setString(2, department_id);
								if(effective_date.equals(""))
										effective_date = Helper.getToday();
								java.util.Date date_tmp = df.parse(effective_date);
								pstmt.setDate(3, new java.sql.Date(date_tmp.getTime()));
								if(expire_date.equals(""))
										pstmt.setNull(4, Types.DATE);
								else{
										date_tmp = df.parse(expire_date);
										pstmt.setDate(4, new java.sql.Date(date_tmp.getTime()));
								}
								pstmt.executeUpdate();
						}
						qq = "select LAST_INSERT_ID()";
						pstmt = con.prepareStatement(qq);
						rs = pstmt.executeQuery();
						if(rs.next()){
								id = rs.getString(1);
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
		public String doUpdate(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				if(employee_id.equals("")){
						return " employee id not set ";
				}
				if(department_id.equals("")){
						return " department id not set ";
				}				
				String qq = "update department_employees set employee_id=?,department_id=?,effective_date=?,expire_date=? where id=? ";
				logger.debug(qq);
				try{
						con = Helper.getConnection();
						if(con != null){
								pstmt = con.prepareStatement(qq);
								pstmt.setString(1, employee_id);
								pstmt.setString(2, department_id);								
								java.util.Date date_tmp = df.parse(effective_date);
								pstmt.setDate(3, new java.sql.Date(date_tmp.getTime()));
								if(expire_date.equals(""))
										pstmt.setNull(4, Types.DATE);
								else{
										date_tmp = df.parse(expire_date);
										pstmt.setDate(4, new java.sql.Date(date_tmp.getTime()));
								}
								pstmt.setString(5, id);
								pstmt.executeUpdate();
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
		/**
		 * change department
		 * the old depatment assignment will set to expire on the new change date
		 */
		public String doChange(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				if(id.equals("")){
						return " id not set ";
				}
				if(new_department_id.equals("")){
						return " new department id not set ";
				}				
				String qq = "update department_employees set expire_date=? where id=? ";
				
				logger.debug(qq);
				try{
						con = Helper.getConnection();
						if(con != null){
								pstmt = con.prepareStatement(qq);
								java.util.Date date_tmp = df.parse(change_date);
								pstmt.setDate(1, new java.sql.Date(date_tmp.getTime()));
								pstmt.setString(2, id);
								pstmt.executeUpdate();
						}
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(con, pstmt, rs);
				}
				if(msg.equals("")){
						effective_date = change_date;
						department_id = new_department_id;
						id="";
						msg = doSave();
				}
				return msg;
		}				

}
