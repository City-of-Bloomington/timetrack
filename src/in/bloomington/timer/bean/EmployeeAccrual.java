package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.Serializable;
import java.util.Set;
import java.util.HashSet;
import java.util.Hashtable;
import java.sql.*;
import java.text.SimpleDateFormat;
import org.apache.log4j.Logger;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;

public class EmployeeAccrual extends CommonInc{
		
		static final long serialVersionUID = 100L;
		static Logger logger = Logger.getLogger(EmployeeAccrual.class);
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		String id="", accrual_id="",employee_id="", date="",
				related_hour_code_id="";
		double hours = 0.0;
		Accrual accrual = null;
		HourCode hourCode = null;
		Employee employee = null;
    public EmployeeAccrual(){

    }
    public EmployeeAccrual(String val){
				setId(val);
    }
    public EmployeeAccrual(String val,
													 String val2,
													 String val3,
													 String val4,
													 double val5,
													 String val6,
													 String val7,
													 int val8,
													 String val9){
				setVals(val, val2, val3, val4, val5, val6, val7, val8, val9);
    }
    void setVals(String val,
								 String val2,
								 String val3,
								 String val4,
								 double val5,
								 String val6,
								 String val7,
								 int val8,
								 String val9
								 ){
				setId(val);
				setAccrual_id(val2);
				setRelated_hour_code_id(val3);
				setEmployee_id(val4);
				setHours(val5);
				setDate(val6);
				if(val7 != null){
						accrual = new Accrual(accrual_id, val7, val8);
				}
				if(val9 != null){
						hourCode = new HourCode(related_hour_code_id, val9);
				}
    }		
		public String getId(){
				return id;
    }
		public String getRelated_hour_code_id(){
				return related_hour_code_id;
    }
		public String getAccrual_id(){
				return accrual_id;
    }
		public String getEmployee_id(){
				return employee_id;
    }
		
		public double getHours(){
				return hours;
    }
		public String getDate(){
				return date;
    }
    public void setId (String val){
				if(val != null)
						id = val;
    }
		public HourCode getHourCode(){
				return hourCode;
		}
    public void setRelated_hour_code_id(String val){
				if(val != null)
						related_hour_code_id = val;
    }		
    public void setAccrual_id (String val){
				if(val != null)
						accrual_id = val;
    }
    public void setEmployee_id (String val){
				if(val != null)
						employee_id = val;
    }
		public void setHours(double val){
				hours = val;
    }
    public void setDate(String val){
				if(val != null)
						date = val;
    }
		public String toString(){
				String ret = getEmployee_id()+" "+getAccrual_id()+" "+getRelated_hour_code_id()+" "+getHours();
				return ret;
		}
		public Accrual getAccrual(){
				if(accrual == null && !accrual_id.equals("")){
						Accrual one = new Accrual(accrual_id);
						String back = one.doSelect();
						if(back.equals("")){
								accrual = one;
						}
				}
				return accrual;
		}
		public Employee getEmployee(){
				if(employee == null && !employee_id.equals("")){
						employee = new Employee(employee_id);
						String back = employee.doSelect();
				}
				return employee;
						
		}		
		public String doSelect(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "select a.id,a.accrual_id,ec.id,a.employee_id,a.hours,date_format(a.date,'%m/%d/%Y'),t.name,t.pref_max_level,ec.name from employee_accruals a join accruals t on t.id=a.accrual_id join hour_codes ec on ec.accrual_id=a.accrual_id where a.id=?";
				if(id.equals("")){
						msg = "accrual id is not set";
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
								rs = pstmt.executeQuery();
								//
								if(rs.next()){
										setVals(rs.getString(1),
														rs.getString(2),
														rs.getString(3),
														rs.getString(4),
														rs.getDouble(5),
														rs.getString(6),
														rs.getString(7),
														rs.getInt(8),
														rs.getString(9));
								}
								else{
										msg = "Accrual not found";
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
				String qq = "insert into employee_accruals values(0,?,?,?,?) ";
				if(employee_id.equals("")){
						msg = " employee id not set ";
						return msg;
				}
				if(accrual_id.equals("")){
						msg = " accrual type not set ";
						return msg;
				}
				logger.debug(qq);
				try{
						con = Helper.getConnection();
						if(con != null){
								pstmt = con.prepareStatement(qq);
								pstmt.setString(1, accrual_id);
								pstmt.setString(2, employee_id);
								pstmt.setDouble(3, hours);
								if(date.equals("")){
										date = Helper.getToday();
								}
								java.util.Date date_tmp = df.parse(date);
								pstmt.setDate(4, new java.sql.Date(date_tmp.getTime()));								
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
				doSelect();
				return msg;
		}
		/**
		 * we do not change employee
		 */
		public String doUpdate(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "update employee_accruals set accrual_id=?,hours=?,date=? where id=?";
				if(accrual_id.equals("")){
						msg = " accrual type not set ";
						return msg;
				}
				logger.debug(qq);
				try{
						con = Helper.getConnection();
						if(con != null){
								pstmt = con.prepareStatement(qq);
								pstmt.setString(1, accrual_id);
								pstmt.setDouble(2, hours);
								java.util.Date date_tmp = df.parse(date);
								pstmt.setDate(3, new java.sql.Date(date_tmp.getTime()));
								pstmt.setString(4, id);
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
				doSelect();
				return msg;
		}		
		
}
