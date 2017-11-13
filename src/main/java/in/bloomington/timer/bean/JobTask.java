package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.Serializable;
import java.util.List;
import java.sql.*;
import javax.sql.*;
import java.text.SimpleDateFormat;
import org.apache.log4j.Logger;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;

public class JobTask implements Serializable{

		static Logger logger = Logger.getLogger(JobTask.class);
		static final long serialVersionUID = 2400L;		
    private String id="", employee_id="", position_id="",
				salary_group_id="",
				inactive="",
				effective_date="", expire_date="", primary_flag="";
    int weekly_regular_hours=40, comp_time_weekly_hours=40;
		double comp_time_factor=1.0, holiday_comp_factor=1.0;
		
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		SalaryGroup salaryGroup = null;
		Type position = null;
		Employee employee = null;
    public JobTask(String val,
									 String val2,
									 String val3,
									 String val4,
									 String val5,
									 String val6,
									 boolean val7,
									 int val8,
									 int val9,
									 double val10,
									 double val11,
									 boolean val12,

									 String val13,
									 String val14,
									 String val15,
									 boolean val16
							 ){
				setVals(val,
								val2,
								val3,
								val4,
								val5,
								val6,
								val7,
								val8,
								val9,
								val10,
								val11,
								val12,
								val13,
								val14,
								val15,
								val16);
		}
		private void setVals(String val,
												 String val2,
												 String val3,
												 String val4,
												 String val5,
												 String val6,
												 boolean val7,
												 int val8,
												 int val9,
												 double val10,
												 double val11,
												 boolean val12,

												 String val13,
												 String val14,
												 String val15,
												 boolean val16
												 ){
				setId(val);
				setPosition_id(val2);
				setSalary_group_id(val3);
				setEmployee_id(val4);
				setEffective_date(val5);
				setExpire_date(val6);
				setPrimary_flag(val7);
				setWeekly_regular_hours(val8);
				setComp_time_weekly_hours(val9);
				setComp_time_factor(val10);
				setHoliday_comp_factor(val11);
				setInactive(val12);
				if(!salary_group_id.equals("")){
						salaryGroup = new SalaryGroup(salary_group_id, val13,val14,val15, val16);
				}
    }
    public JobTask(String val){
				setId(val);
    }
    public JobTask(){
    }		
    //
    // getters
    //
    public String getPosition_id(){
				return position_id;
    }
    public String getEmployee_id(){
				return employee_id;
    }
    public String getSalary_group_id(){
				return salary_group_id;
    }
    public String getEffective_date(){
				if(id.equals(""))
						return CommonInc.default_effective_date;
				return effective_date;
    }
    public String getExpire_date(){
				return expire_date;
    }		
		public boolean getPrimary_flag(){
				if(id.equals(""))
						return true; // default
				return !primary_flag.equals("");
    }		
		public boolean getInactive(){
				return !inactive.equals("");
    }
		public boolean isActive(){
				return inactive.equals("");
    }
    public int getWeekly_regular_hours(){
				return weekly_regular_hours;
    }
    public int getComp_time_weekly_hours(){
				return comp_time_weekly_hours;
    }
		public double getComp_time_factor(){
				return comp_time_factor;
		}
		public double getHoliday_comp_factor(){
				return holiday_comp_factor;
		}
		public String getId(){
				return id;
    }
		
    //
    // setters
    //
    public void setId (String val){
				if(val != null)
						id = val;
    }
    public void setPosition_id (String val){
				if(val != null)
						position_id = val;
    }
    public void setEmployee_id(String val){
				if(val != null)
						employee_id = val;
    }
    public void setSalary_group_id(String val){
				if(val != null)
						salary_group_id = val;
    }
    public void setEffective_date(String val){
				if(val != null)
						effective_date = val;
    }
    public void setExprire_date(String val){
				if(val != null)
						expire_date = val;
    }		
    public void setExpire_date(String val){
				if(val != null)
						expire_date = val;
    }		
		public void setInactive(boolean val){
				if(val)
						inactive = "y";
		}
		public void setPrimary_flag(boolean val){
				if(val)
						primary_flag = "y";
		}
    public void setWeekly_regular_hours (int val){
				if(val > 0)
						weekly_regular_hours = val;
    }
		public void setComp_time_weekly_hours(int val){
				if(val > 0)
						comp_time_weekly_hours = val;
		}
		public void setComp_time_factor(double val){
				if(val > 0)
						comp_time_factor = val;
		}
		public void setHoliday_comp_factor(double val){
				if(val > 0)
						holiday_comp_factor = val;
		}
		public String toString(){
				return id;
		}
		public boolean equals(Object o) {
				if (o instanceof JobTask) {
						JobTask c = (JobTask) o;
						if ( this.id.equals(c.getId())) 
								return true;
				}
				return false;
		}
		public int hashCode(){
				int seed = 37;
				if(!id.equals("")){
						try{
								seed += Integer.parseInt(id)*31;
						}catch(Exception ex){
								// we ignore
						}
				}
				return seed;
		}
		public SalaryGroup getSalaryGroup(){
				if(!salary_group_id.equals("") && salaryGroup == null){
						SalaryGroup one = new SalaryGroup(salary_group_id);
						String back = one.doSelect();
						if(back.equals("")){
								salaryGroup = one;
						}
				}
				return salaryGroup;
		}
		public Type getPosition(){
				if(!position_id.equals("") && position == null){
						Type one = new Type(position_id);
						one.setTable_name("positions");
						String back = one.doSelect();
						if(back.equals("")){
								position = one;
						}
				}
				return position;
		}		
		public Employee getEmployee(){
				if(!employee_id.equals("") && employee == null){
						Employee one = new Employee(employee_id);
						String back = one.doSelect();
						if(back.equals("")){
								employee = one;
						}
				}
				return employee;
		}		
		public String doSelect(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "select j.id,"+
						"j.position_id,"+
						"j.salary_group_id,"+
						"j.employee_id,"+
						"date_format(j.effective_date,'%m/%d/%Y'),"+
						
						"date_format(j.expire_date,'%m/%d/%Y'),"+
						"j.primary_flag,"+
						"j.weekly_regular_hours,"+
						"j.comp_time_weekly_hours,"+
						"j.comp_time_factor,"+
						
						"j.holiday_comp_factor,"+
						"j.inactive, "+
						"g.name,g.description,g.default_regular_id,g.inactive "+
						" from jobs j "+
						" left join salary_groups g on g.id=j.salary_group_id "+
						" where j.id =? ";
				logger.debug(qq);
				try{
						con = Helper.getConnection();
						if(con != null){
								pstmt = con.prepareStatement(qq);
								pstmt.setString(1, id);
								rs = pstmt.executeQuery();
								if(rs.next()){
										setVals(rs.getString(1),
														rs.getString(2),
														rs.getString(3),
														rs.getString(4),
														rs.getString(5),
														rs.getString(6),
														rs.getString(7) != null,
														rs.getInt(8),
														rs.getInt(9),
														rs.getDouble(10),
														rs.getDouble(11),
														rs.getString(12) != null,
														
														rs.getString(13),
														rs.getString(14),
														rs.getString(15),
														rs.getString(16) != null
														);
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
				String qq = "insert into jobs values(0,?,?,?,?, ?,?,?,?,?, ?,null) ";
				if(employee_id.equals("")){
						msg = " employee_id not set ";
						return msg;
				}
				if(position_id.equals("")){
						msg = " position not set ";
						return msg;
				}
				if(salary_group_id.equals("")){
						msg = " salary group not set ";
						return msg;
				}				
				logger.debug(qq);
				try{
						con = Helper.getConnection();
						if(con != null){
								pstmt = con.prepareStatement(qq);
								pstmt.setString(1, position_id);
								pstmt.setString(2, salary_group_id);
								pstmt.setString(3, employee_id);
								
								if(effective_date.equals(""))
										effective_date = Helper.getToday();
								java.util.Date date_tmp = df.parse(effective_date);
								pstmt.setDate(4, new java.sql.Date(date_tmp.getTime()));
								if(!expire_date.equals("")){
										date_tmp = df.parse(expire_date);								
										pstmt.setDate(5, new java.sql.Date(date_tmp.getTime()));
								}
								else
										pstmt.setNull(5, Types.DATE);	
								if(primary_flag.equals(""))
										pstmt.setNull(6, Types.CHAR);
								else
										pstmt.setString(6, "y");
								pstmt.setInt(7, weekly_regular_hours);
								pstmt.setInt(8, comp_time_weekly_hours);
								pstmt.setDouble(9, comp_time_factor);
								pstmt.setDouble(10,holiday_comp_factor);
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
		//
		// we can update expire date and inactive
		//
		public String doUpdate(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				if(id.equals("")){
						return " id not set ";
				}
				if(employee_id.equals("")){
						msg = " employee_id not set ";
						return msg;
				}
				if(position_id.equals("")){
						msg = " position not set ";
						return msg;
				}
				if(salary_group_id.equals("")){
						msg = " salary group not set ";
						return msg;
				}								
				String qq = "update jobs set position_id=?,"+
						"salary_group_id=?,"+
						"employee_id=?,"+
						"effective_date=?,"+
						"expire_date=?,"+
						"primary_flag=?,"+
						"weekly_regular_hours=?,"+
						"comp_time_weekly_hours=?,"+
						"comp_time_factor=?,"+
						"holiday_comp_factor=?,"+
						"inactive=? where id=? ";
				logger.debug(qq);
				try{
						con = Helper.getConnection();
						if(con != null){
								pstmt = con.prepareStatement(qq);
								pstmt.setString(1, position_id);
								pstmt.setString(2, salary_group_id);
								pstmt.setString(3, employee_id);
								if(effective_date.equals(""))
										effective_date = Helper.getToday();
								java.util.Date date_tmp = df.parse(effective_date);
								pstmt.setDate(4, new java.sql.Date(date_tmp.getTime()));
								if(!expire_date.equals("")){
										date_tmp = df.parse(expire_date);
										pstmt.setDate(5, new java.sql.Date(date_tmp.getTime()));
								}
								else
										pstmt.setNull(5, Types.DATE);										
								if(primary_flag.equals(""))
										pstmt.setNull(6, Types.CHAR);
								else
										pstmt.setString(6, "y");
								pstmt.setInt(7, weekly_regular_hours);
								pstmt.setInt(8, comp_time_weekly_hours);
								pstmt.setDouble(9, comp_time_factor);
								pstmt.setDouble(10,holiday_comp_factor);
								if(inactive.equals(""))
										pstmt.setNull(11, Types.CHAR);
								else
										pstmt.setString(11, "y");
								pstmt.setString(12, id);
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
						doSelect();
				}

				return msg;
		}
		public String doDelete(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "delete jobs where id=? ";
				logger.debug(qq);
				try{
						con = Helper.getConnection();
						if(con != null){
								pstmt = con.prepareStatement(qq);
								pstmt.setString(1, id);
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

}
