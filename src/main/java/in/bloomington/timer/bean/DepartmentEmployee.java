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
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DepartmentEmployee{

		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		static final long serialVersionUID = 500L;		
		static Logger logger = LogManager.getLogger(DepartmentEmployee.class);
    private String id="", employee_id="",
				department_id="",
				// secondary dept for department directors
				department2_id="", 
				effective_date="",
				expire_date="",				
				new_department_id="",
				change_date="";// change department
		boolean active = true;
		Department department = null, department2=null;
		Employee employee = null;
		public DepartmentEmployee(){
    }
		
    public DepartmentEmployee(String val){
				setId(val);
    }
		// for new record
    public DepartmentEmployee(String val,
															String val2,
															String val3){
				setEmployee_id(val);
				setDepartment_id(val2);
				setEffective_date(val3);
		}
    public DepartmentEmployee(String val,
															String val2,
															String val3,
															String val4,
															String val5,
															String val6
															){
				setVals(val, val2, val3, val4, val5, val6);
    }
    void setVals(String val,
								 String val2,
								 String val3,
								 String val4,
								 String val5,
								 String val6
								){
				setId(val);
				setEmployee_id(val2);
				setDepartment_id(val3);
				setDepartment2_id(val4);
				setEffective_date(val5);
				setExpire_date(val6);
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
		public String getDepartment2_id(){
				if(department2_id.isEmpty())
						return "-1";
				return department2_id;
    }		
		public String getNew_department_id(){
				return new_department_id;
    }		
		public String getEffective_date(){
				if(id.isEmpty())
						return CommonInc.default_effective_date;
				return effective_date;
    }
    public String getExpire_date(){
				return expire_date;
    }
		public boolean hasExpireDate(){
				return !expire_date.isEmpty();
		}
    public String getChange_date(){
				return change_date;
    }
		public boolean hasSecondaryDept(){
				return !department2_id.isEmpty();
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
    public void setDepartment2_id (String val){
				if(val != null && !val.equals("-1"))
						department2_id = val;
    }		
    public void setNew_department_id (String val){
				if(val != null && !val.equals("-1"))
						new_department_id = val;
    }
    public void setEffective_date (String val){
				if(val != null && !val.equals("-1"))
						effective_date = val;
    }		
    public void setChange_date (String val){
				if(val != null)
						change_date = val;
    }		
    public void setExpire_date(String val){
				if(val != null && !val.equals("-1"))
						expire_date = val;
    }
		public Department getDepartment(){
				if(department == null && !department_id.isEmpty()){
						Department dd = new Department(department_id);
						String back = dd.doSelect();
						if(back.isEmpty()){
								department = dd;
						}

				}
				return department;
		}
		public Department getDepartment2(){
				if(department2 == null && !department2_id.isEmpty()){
						Department dd = new Department(department2_id);
						String back = dd.doSelect();
						if(back.isEmpty()){
								department2 = dd;
						}

				}
				return department2;
		}		
		public Employee getEmployee(){
				if(employee == null && !employee_id.isEmpty()){
						Employee dd = new Employee(employee_id);
						String back = dd.doSelect();
						if(back.isEmpty()){
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
				if(!id.isEmpty()){
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
				String qq = "select id,employee_id,department_id,department2_id,"+
						" date_format(effective_date,'%m/%d/%Y'),"+
						" date_format(expire_date,'%m/%d/%Y') "+
						" from department_employees where ";
				if(!id.isEmpty()){
						qq += " id = ? ";
				}
				else if(!employee_id.isEmpty()){
						qq += " employee_id = ? ";
				}
				else if(!department_id.isEmpty()){
						qq += " (department_id = ? or department_id2=?)";
				}
				else{
						msg = " no id set ";
						return msg;
				}
				logger.debug(qq);
				con = UnoConnect.getConnection();
				if(con == null){
						return "No db connection";
				}
				try{
						pstmt = con.prepareStatement(qq);
						if(!id.isEmpty()){
								pstmt.setString(1, id);
						}
						else if(!employee_id.isEmpty()){
								pstmt.setString(1, employee_id);
						}
						else{
								pstmt.setString(1, department_id);
								pstmt.setString(2, department_id);										
						}
						rs = pstmt.executeQuery();
						//
						if(rs.next()){
								setVals(rs.getString(1),
												rs.getString(2),
												rs.getString(3),
												rs.getString(4),
												rs.getString(5),
												rs.getString(6));
						}
						else{
								msg = "Department Employee not found";
						}
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(pstmt, rs);
						UnoConnect.databaseDisconnect(con);
				}
				return msg;
		}
		public String doSave(){
				//
				Connection con = null;
				PreparedStatement pstmt = null, pstmt2=null, pstmt3=null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = " select id from department_employees where employee_id = ? and department_id=? and expire_date is null ";
				String qq2 = " insert into department_employees values(0,?,?,?,?,?) ";
				if(employee_id.isEmpty()){
						msg = " employee id not set ";
						return msg;
				}
				if(department_id.isEmpty()){
						msg = " department not set ";
						return msg;
				}				
				logger.debug(qq);
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "No DB connections ";
						return msg;
				}
				try{
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, employee_id);
						pstmt.setString(2, department_id);
						rs = pstmt.executeQuery();
						if(rs.next()){
								id = rs.getString(1);
						}
						if(id.isEmpty()){
								qq = qq2;
								pstmt2 = con.prepareStatement(qq2);
								pstmt2.setString(1, employee_id);
								pstmt2.setString(2, department_id);
								if(department2_id.isEmpty()){
										pstmt2.setNull(3, Types.INTEGER);
								}
								else{
										pstmt2.setString(3, department2_id);
								}
								if(effective_date.isEmpty())
										effective_date = Helper.getToday();
								java.util.Date date_tmp = df.parse(effective_date);
								pstmt2.setDate(4, new java.sql.Date(date_tmp.getTime()));
								if(expire_date.isEmpty())
										pstmt2.setNull(5, Types.DATE);
								else{
										date_tmp = df.parse(expire_date);
										pstmt2.setDate(5, new java.sql.Date(date_tmp.getTime()));
								}
								pstmt2.executeUpdate();
								qq = "select LAST_INSERT_ID()";
								pstmt3 = con.prepareStatement(qq);
								rs = pstmt3.executeQuery();
								if(rs.next()){
										id = rs.getString(1);
								}								
						}

				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(rs, pstmt, pstmt2, pstmt3);
						UnoConnect.databaseDisconnect(con);
				}
				return msg;
		}
		public String doUpdate(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				if(employee_id.isEmpty()){
						return " employee id not set ";
				}
				if(department_id.isEmpty()){
						return " department id not set ";
				}
				String qq = "update department_employees set employee_id=?,department_id=?,department2_id=?,effective_date=?,expire_date=? where id=? ";
				logger.debug(qq);
				try{
						con = UnoConnect.getConnection();
						if(con != null){
								pstmt = con.prepareStatement(qq);
								pstmt.setString(1, employee_id);
								pstmt.setString(2, department_id);
								if(department2_id.isEmpty()){
										pstmt.setNull(3, Types.INTEGER);
								}
								else{
										pstmt.setString(3, department2_id);
								}								
								java.util.Date date_tmp = df.parse(effective_date);
								pstmt.setDate(4, new java.sql.Date(date_tmp.getTime()));
								if(expire_date.isEmpty())
										pstmt.setNull(5, Types.DATE);
								else{
										date_tmp = df.parse(expire_date);
										pstmt.setDate(5, new java.sql.Date(date_tmp.getTime()));
								}
								pstmt.setString(6, id);
								pstmt.executeUpdate();
						}
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(pstmt, rs);
						UnoConnect.databaseDisconnect(con);
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
				if(id.isEmpty()){
						return " id not set ";
				}
				if(new_department_id.isEmpty()){
						return " new department id not set ";
				}
				String start_date = effective_date; 
				String old_expire_date = Helper.getDateFrom(start_date,-1);
				String qq = "update department_employees set expire_date=? where id=? ";
				logger.debug(qq);
				con = UnoConnect.getConnection();				
				try{
						if(con != null){
								pstmt = con.prepareStatement(qq);
								java.util.Date date_tmp = df.parse(old_expire_date);
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
						Helper.databaseDisconnect(pstmt, rs);
						UnoConnect.databaseDisconnect(con);
				}
				if(msg.isEmpty()){
						// effective_date = start_date;
						department_id = new_department_id;
						id="";
						msg = doSave();
				}
				return msg;
		}				

}
