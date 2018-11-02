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
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JobTask implements Serializable{

		static Logger logger = LogManager.getLogger(JobTask.class);
		static final long serialVersionUID = 2400L;
    private String id="",
				employee_id="", group_id="",
				employee_number="", // needed for update from nw
				position_id="",
				salary_group_id="",
				inactive="",
				effective_date="", expire_date="", primary_flag="";
		String clock_time_required="";
    int weekly_regular_hours=40, comp_time_weekly_hours=40;
		double comp_time_factor=1.0, holiday_comp_factor=1.0;
		double hourly_rate=0;
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		SalaryGroup salaryGroup = null;
		Position position = null;
		Employee employee = null;
		Group group = null;
    public JobTask(String val,
									 String val2,
									 String val3,
									 String val4,
									 String val5,
									 
									 String val6,
									 String val7,
									 boolean val8,
									 int val9,
									 int val10,
									 double val11,
									 
									 double val12,
									 boolean val13,
									 double val14,
									 boolean val15,
									 
									 String val16,
									 String val17,
									 String val18,
									 boolean val19
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
								val16,
								val17,
								val18,
								val19);
		}
    public JobTask(String val,
									 String val2,
									 String val3,
									 String val4,
									 String val5,
									 
									 String val6,
									 String val7,
									 boolean val8,
									 int val9,
									 int val10,
									 double val11,
									 
									 double val12,
									 boolean val13,
									 double val14,
									 boolean val15,
									 
									 String val16,
									 String val17,
									 String val18,
									 boolean val19,
									 String val20
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
								val16,
								val17,
								val18,
								val19);
				
				setEmployee_number(val20);

		}
		private void setVals(String val,
												 String val2,
												 String val3,
												 String val4,
												 String val5,
												 String val6,
												 String val7,
												 boolean val8,
												 int val9,
												 int val10,
												 double val11,
												 double val12,
												 boolean val13,
												 double val14,
												 boolean val15,												 

												 String val16,
												 String val17,
												 String val18,
												 boolean val19
												 
												 ){
				setId(val);
				setPosition_id(val2);
				setSalary_group_id(val3);
				setEmployee_id(val4);
				setGroup_id(val5);
				setEffective_date(val6);
				setExpire_date(val7);
				setPrimary_flag(val8);
				setWeekly_regular_hours(val9);
				setComp_time_weekly_hours(val10);
				setComp_time_factor(val11);
				setHoliday_comp_factor(val12);
				setClock_time_required(val13);
				setHourlyRateDbl(val14);				
				setInactive(val15);
				if(!salary_group_id.equals("")){
						salaryGroup = new SalaryGroup(salary_group_id, val16,val17,val18, val19);
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
    public String getGroup_id(){
				return group_id;
    }		
    public String getEmployee_number(){
				return employee_number;
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
		public boolean isPrimary(){
				return getPrimary_flag();
    }
		public boolean hasNoGroup(){
				return group_id.equals("");
    }		
		public boolean getInactive(){
				return !inactive.equals("");
    }
		public boolean getClock_time_required(){
				return !clock_time_required.equals("");
		}
		public boolean isActive(){
				return inactive.equals("");
    }
		public boolean isPunchClockOnly(){
				return !clock_time_required.equals("");
		}
		public boolean isLeaveEligible(){
				getSalaryGroup();
				return salaryGroup != null && salaryGroup.isLeaveEligible();
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
				if(val != null && !val.equals("-1"))
						position_id = val;
    }
    public void setEmployee_id(String val){
				if(val != null && !val.equals("-1"))
						employee_id = val;
    }
    public void setGroup_id(String val){
				if(val != null && !val.equals("-1"))
						group_id = val;
    }		
    public void setEmployee_number(String val){
				if(val != null)
						employee_number = val;
    }		
    public void setName(String val){
				// needed for auto_complete
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
		public void setClock_time_required(boolean val){
				if(val)
						clock_time_required = "y";
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
		public void setHourlyRateDbl(double val){
				if(val > 0)
						hourly_rate = val;
		}
		public void setHourlyRate(String val){
				if(val  != null && !val.equals("")){
						try{
								hourly_rate = Double.parseDouble(val);
						}catch(Exception ex){}
				}
		}
		public String getName(){
				getPosition();
				if(position != null) {
						return position.getName();
				}
				return "";
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
		public Group getGroup(){
				if(!group_id.equals("") && group == null){
						Group one = new Group(group_id);
						String back = one.doSelect();
						if(back.equals("")){
								group = one;
						}
				}
				return group;
		}		
		public Position getPosition(){
				if(!position_id.equals("") && position == null){
						Position one = new Position(position_id);
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
		public void compareWith(double weekly_hrs,
														double hr_rate,
														double comp_after,
														double comp_factor,
														double holiday_factor,
														String job_name,
														BenefitGroup bGroup){
				//
				// salary groups id:1:exempt, 2:Non-Exempt, 3:Temp, 4:Union
				//
				boolean needUpdate = false;
				int new_weekly_regular_hours = (int) weekly_hrs;
				int new_comp_time_weekly_hours = (int) comp_after;
				String new_salary_group_id = "";
				if(bGroup.isExempt()){
						new_salary_group_id = "1";
				}
				else if(bGroup.isNonExempt()){
						new_salary_group_id = "2";
				}
				else if(bGroup.isTemporary()){
						new_salary_group_id = "3";
				}
				else if(bGroup.isUnioned()){
						new_salary_group_id = "4";
				}
				else if(bGroup.isPartTime()){
						new_salary_group_id = "5";
				}				
				if(!new_salary_group_id.equals("")){
						if(!salary_group_id.equals(new_salary_group_id)){
								salary_group_id = new_salary_group_id;
								needUpdate = true;
						}
				}
				if(weekly_regular_hours != new_weekly_regular_hours){
						weekly_regular_hours = new_weekly_regular_hours;
						needUpdate = true;
				}
				if(comp_time_weekly_hours != new_comp_time_weekly_hours){
						comp_time_weekly_hours = new_comp_time_weekly_hours;
						needUpdate = true;
				}
				if(comp_factor - comp_time_factor > 0.1 ||
					 comp_factor - comp_time_factor < -0.1
					 ){
						comp_time_factor = comp_factor;
						needUpdate = true;						
				}
				if(holiday_comp_factor - holiday_factor > 0.1 ||
					 holiday_comp_factor - holiday_factor < -0.1){
						holiday_comp_factor = holiday_factor;
						needUpdate = true;						
				}
				if(hr_rate - hourly_rate > 0.1 || hr_rate - hourly_rate < -0.1){
						hourly_rate = hr_rate;
						needUpdate = true;							
				}
				if(!job_name.equals("")){
						getPosition();
						boolean pos_update = false;
						if(position != null){
								String alias = position.getAlias();
								if(!job_name.equals(alias)){
										pos_update = true;
								}
						}
						else{
								pos_update = true;
						}
						if(pos_update){
								boolean found = false;
								needUpdate = true;
								PositionList pl = new PositionList();
								pl.setName(job_name);
								pl.setActiveOnly();
								pl.setExactMatch();
								String back = pl.find();
								if(back.equals("")){
										List<Position> ones = pl.getPositions();
										if(ones != null && ones.size() > 0){
												position = ones.get(0);
												position_id = position.getId();
												found = true;
										}
								}
								if(!found){
										Position pos = new Position(job_name, job_name, job_name);
										back = pos.doSave();
										if(back.equals("")){
												position = pos;												
												position_id = pos.getId();
										}
								}
						}
				}
				if(needUpdate){
						String back = doPartialUpdate();
						if(!back.equals(""))
								System.err.println(back);
				}
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
						"j.group_id,"+
						"date_format(j.effective_date,'%m/%d/%Y'),"+
						
						"date_format(j.expire_date,'%m/%d/%Y'),"+
						"j.primary_flag,"+
						"j.weekly_regular_hours,"+
						"j.comp_time_weekly_hours,"+
						"j.comp_time_factor,"+
						
						"j.holiday_comp_factor,"+
						"j.clock_time_required, "+
						"j.hourly_rate,"+
						"j.inactive, "+
						"g.name,g.description,g.default_regular_id,g.inactive "+
						" from jobs j "+
						" left join salary_groups g on g.id=j.salary_group_id "+
						" where j.id =? ";
				logger.debug(qq);
				con = Helper.getConnection();
				if(con == null){
						msg = "Could not connect to DB";
						return msg;
				}			
				try{
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
												rs.getString(7),
												rs.getString(8) != null,
												rs.getInt(9),
												rs.getInt(10),
												rs.getDouble(11),
												rs.getDouble(12),
												rs.getString(13) != null,
												rs.getDouble(14),														
												rs.getString(15) != null,
												
												rs.getString(16),
												rs.getString(17),
												rs.getString(18),
												rs.getString(19) != null
												);
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
				String qq = "insert into jobs values(0,?,?,?,?, ?,?,?,?,?, ?,?,?,?,null) ";
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
				if(group_id.equals("")){
						msg = " group not set ";
						return msg;
				}				
				logger.debug(qq);
				try{
						con = Helper.getConnection();
						if(con == null){
								msg = "Could not connect to DB";
								return msg;
						}			
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, position_id);
						pstmt.setString(2, salary_group_id);
						pstmt.setString(3, employee_id);
						pstmt.setString(4, group_id);						
						
						if(effective_date.equals(""))
								effective_date = Helper.getToday();
						java.util.Date date_tmp = df.parse(effective_date);
						pstmt.setDate(5, new java.sql.Date(date_tmp.getTime()));
						if(!expire_date.equals("")){
								date_tmp = df.parse(expire_date);								
								pstmt.setDate(6, new java.sql.Date(date_tmp.getTime()));
						}
						else
								pstmt.setNull(6, Types.DATE);	
						if(primary_flag.equals(""))
										pstmt.setNull(7, Types.CHAR);
						else
								pstmt.setString(7, "y");
						pstmt.setInt(8, weekly_regular_hours);
						pstmt.setInt(9, comp_time_weekly_hours);
						pstmt.setDouble(10, comp_time_factor);
						pstmt.setDouble(11, holiday_comp_factor);
						if(clock_time_required.equals(""))
								pstmt.setNull(12, Types.CHAR);
						else
								pstmt.setString(12, "y");
						pstmt.setDouble(13, hourly_rate);
						pstmt.executeUpdate();
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
						return " job id not set ";
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
				if(group_id.equals("")){
						msg = " group not set ";
						return msg;
				}				
				String qq = "update jobs set position_id=?,"+
						"salary_group_id=?,"+
						"employee_id=?,"+
						"group_id=?,"+
						"effective_date=?,"+
						
						"expire_date=?,"+
						"primary_flag=?,"+
						"weekly_regular_hours=?,"+
						"comp_time_weekly_hours=?,"+
						"comp_time_factor=?,"+
						
						"holiday_comp_factor=?,"+
						"clock_time_required=?,"+
						"hourly_rate=?, "+
						"inactive=? "+
						"where id=? ";
				logger.debug(qq);
				try{
						con = Helper.getConnection();
						if(con == null){
								msg = "Could not connect to DB";
								return msg;
						}						
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, position_id);
						pstmt.setString(2, salary_group_id);
						pstmt.setString(3, employee_id);
						pstmt.setString(4, group_id);
						if(effective_date.equals(""))
								effective_date = Helper.getToday();
						java.util.Date date_tmp = df.parse(effective_date);
						pstmt.setDate(5, new java.sql.Date(date_tmp.getTime()));
						if(!expire_date.equals("")){
								date_tmp = df.parse(expire_date);
								pstmt.setDate(6, new java.sql.Date(date_tmp.getTime()));
						}
						else
								pstmt.setNull(6, Types.DATE);										
						if(primary_flag.equals(""))
								pstmt.setNull(7, Types.CHAR);
						else
								pstmt.setString(7, "y");
						pstmt.setInt(8, weekly_regular_hours);
						pstmt.setInt(9, comp_time_weekly_hours);
						pstmt.setDouble(10, comp_time_factor);
						pstmt.setDouble(11,holiday_comp_factor);
						if(clock_time_required.equals(""))
								pstmt.setNull(12, Types.CHAR);
						else
								pstmt.setString(12, "y");								
						pstmt.setDouble(13, hourly_rate);
						if(inactive.equals(""))
								pstmt.setNull(14, Types.CHAR);
						else
								pstmt.setString(14, "y");
						pstmt.setString(15, id);
						pstmt.executeUpdate();
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
		// we update job based on info we get from NW (if any)
		//
		public String doPartialUpdate(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				if(id.equals("")){
						return " id not set ";
				}
				if(position_id.equals("")){
						msg = " position not set ";
						return msg;
				}
				if(salary_group_id.equals("")){
						msg = " salary group not set ";
						return msg;
				}								
				String qq = "update jobs set "+
						"position_id=?,"+
						"salary_group_id=?,"+
						"weekly_regular_hours=?,"+
						"comp_time_weekly_hours=?,"+
						"comp_time_factor=?,"+
						
						"holiday_comp_factor=?,"+
						"hourly_rate=? "+
						"where id=? ";
				logger.debug(qq);
				try{
						con = Helper.getConnection();
						if(con == null){
								msg = "Could not connect to DB";
								return msg;
						}
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, position_id);
						pstmt.setString(2, salary_group_id);
						pstmt.setInt(3, weekly_regular_hours);
						pstmt.setInt(4, comp_time_weekly_hours);
						pstmt.setDouble(5, comp_time_factor);
						pstmt.setDouble(6, holiday_comp_factor);
						pstmt.setDouble(7, hourly_rate);
						pstmt.setString(8, id);
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
