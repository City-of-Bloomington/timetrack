package in.bloomington.timer.list;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Hashtable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;

public class JobTaskList{

    static final long serialVersionUID = 2500L;
    static Logger logger = LogManager.getLogger(JobTaskList.class);
    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
    List<JobTask> jobTasks = null;
    boolean active_only = false, current_only = false, inactive_only=false,
				clock_time_required = false, clock_time_not_required=false,
				not_expired = false, non_temp_emp = false;
		boolean avoid_recent_jobs = false; // jobs are recent if entered within 14 days
    String salary_group_id="", employee_id="", pay_period_id="";
    String id="", effective_date = "", which_date="j.effective_date",
				date_from="", date_to="", position_id="", employee_name="",
				department_id="", group_id="";
    String clock_status="";
    List<Group> groups = null;
    public JobTaskList(){
    }
    public JobTaskList(String val){
				setEmployee_id(val);
    }
    public JobTaskList(String val, String val2){
				setEmployee_id(val);
				setPay_period_id(val2);
    }		
    public List<JobTask> getJobTasks(){
				return jobTasks;
    }
    public List<JobTask> getJobs(){
				return jobTasks;
    }		
    public void setActiveOnly(){
				active_only = true;
    }
    public void setClock_time_required(){
				clock_time_required = true;
    }
    public void setId(String val){
				if(val != null && !val.equals("-1"))
						id = val;
    }
    public void setGroup_id(String val){
				if(val != null && !val.equals("-1"))
						group_id = val;
    }		
    public void setEmployee_id(String val){
				if(val != null && !val.equals("-1"))
						employee_id = val;
    }
    public void setEmployee_name(String val){
				// for auto_complete
    }
    public void setDepartment_id(String val){
				if(val != null && !val.equals("-1"))
						department_id = val;
    }
    public void setPosition_id(String val){
				if(val != null && !val.equals("-1"))
						position_id = val;
    }
		
    public void setWhich_date(String val){
				if(val != null)
						which_date = val;
    }
    public void setDate_from(String val){
				if(val != null)
						date_from = val;
    }
    public void setDate_to(String val){
				if(val != null)
						date_to = val;
    }		
    public void setPay_period_id(String val){
				if(val != null)
						pay_period_id = val;
    }		
    // normally this is pay period start date
    public void setEffective_date(String val){
				if(val != null){
						effective_date = val;
						active_only = true;
				}
    }		
    public void setSalary_group_id(String val){
				if(val != null && !val.equals("-1"))
						salary_group_id = val;
    }
		
    public void setActive_status(String val){
				if(val != null && !val.equals("-1")){
						if(val.equals("Active")) {
								active_only = true;
								setNotExpired();
						}
						else if(val.equals("Inactive"))
								inactive_only = true;
				}
    }		
    public void setClock_status(String val){
				if(val != null && !val.equals("-1")){
						if(val.equals("y")) 
								clock_time_required = true;
						else if(val.equals("n"))
								clock_time_not_required = true;
				}
    }		
    public void setCurrentOnly(){
				active_only = true;
    }
		public void setAvoidRecentJobs(){
				avoid_recent_jobs = true;
		}
    public String getId(){
				return id;
    }
    public String getSalary_group_id(){
				if(salary_group_id.equals(""))
						return "-1";
				return salary_group_id;
    }
    public String getGroup_id(){
				if(group_id.equals(""))
						return "-1";

				return group_id;
    }		
    public String getPosition_id(){
				if(position_id.equals(""))
						return "-1";
				return position_id;
    }
    public String getEmployee_id(){
				// for auto_complete
				return employee_id;
    }
    public String getDepartment_id(){
				if(department_id.equals(""))
						return "-1";				
				return department_id;
    }		
    public String getWhich_date(){
				return which_date;
    }
    public String getDate_from(){
				return date_from;
    }
    public String getDate_to(){
				return date_to;
    }
    public String getActive_status(){
				if(active_only)
						return "Active";
				else if(inactive_only)
						return "Inactive";
				return "-1";
    }
    public String getClock_status(){
				if(clock_time_required)
						return "y";
				else if(clock_time_not_required)
						return "n";
				return "-1";

    }
		public void setNotExpired(){
				not_expired = true;
		}
		public void setNonTemp(){
				non_temp_emp = true;
		}
    public List<Group> getGroups(){
				if(groups == null){
						if(!department_id.equals("")){
								GroupList gl = new GroupList();
								gl.setDepartment_id(department_id);
								gl.setActiveOnly();
								String back = gl.find();
								if(back.equals("")){
										List<Group> ones = gl.getGroups();
										if(ones != null && ones.size() > 0)
												groups = ones;
								}
						}
				}
				return groups;
    }
    public boolean hasGroups(){
				getGroups();
				return groups != null && groups.size() > 0;
    }
    //
    public String find(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", qw="";
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
						"j.clock_time_required,"+
						"j.hourly_rate,"+
						"date_format(j.added_date,'%m/%d/%Y'),"+
						
						"j.inactive,  "+
						
						"sg.name,sg.description,sg.default_regular_id,"+
						"sg.excess_culculation,sg.inactive, "+
						"p.name "+ // position name
						" from jobs j ";
				qq += " join salary_groups sg on sg.id=j.salary_group_id "+
						" join groups g on j.group_id=g.id "+
						" join positions p on j.position_id=p.id ";
				logger.debug(qq);
				con = UnoConnect.getConnection();
				if(con == null){
						msg = " Could not connect to DB ";
						logger.error(msg);
						return msg;
				}
				if(active_only){
						qw += " j.inactive is null ";
				}
				else if(inactive_only){
						qw += " j.inactive is not null ";
				}
				if(not_expired){
						if(!qw.equals("")) qw += " and ";
						qw += " j.expire_date is null "; 						
				}
				if(current_only){
						if(!qw.equals("")) qw += " and ";
						qw += " j.effective_date < now() and (j.expire_date > now() or j.exire_date is null) ";
				}
				if(avoid_recent_jobs){
						if(!qw.equals("")) qw += " and ";
						qw += " j.added_date < date_add(curdate(),INTERVAL 10 DAY) ";
				}				
				if(clock_time_required){
						if(!qw.equals("")) qw += " and ";
						qw += " j.clock_time_required is not null ";
				}
				else if(clock_time_not_required){
						if(!qw.equals("")) qw += " and ";
						qw += " j.clock_time_required is null ";
				}
				try{
						if(!salary_group_id.equals("")){
								if(!qw.equals("")) qw += " and ";
								qw += " j.salary_group_id = ? ";
						}
						else if(non_temp_emp){
								if(!qw.equals("")) qw += " and ";
								qw += " j.salary_group_id <> 3 "; // Temp salary is 3
						}
						if(!employee_id.equals("")){
								if(!qw.equals("")) qw += " and ";
								qw += " j.employee_id = ? ";
						}
						if(!group_id.equals("")){
								if(!qw.equals("")) qw += " and ";
								qw += " j.group_id = ? ";
						}						
						if(!effective_date.equals("")){
								if(!qw.equals("")) qw += " and ";
								qw += " j.effective_date <= ? and (j.expire_date > ? or j.expire_date is null)";
						}
						if(!pay_period_id.equals("")){
								qq += ", pay_periods pp ";
								if(!qw.equals("")) qw += " and ";
								qw += " j.effective_date <= pp.start_date and (j.expire_date >= pp.end_date or j.expire_date is null) and pp.id = ?";
						}
						if(!position_id.equals("")){
								if(!qw.equals("")) qw += " and ";
								qw += " j.position_id = ? ";
						}
						if(!department_id.equals("")){
								qq += " inner join department_employees de on de.employee_id=j.employee_id and g.department_id=de.department_id";
								if(!qw.equals("")) qw += " and ";
								qw += " de.department_id = ? ";
								if(current_only){
										qw += " and (de.expire_date >= now() or de.expire_date is null) ";
								}
						}
						if(!date_from.equals("")){
								if(!qw.equals("")) qw += " and ";
								qw += which_date+" >= ? ";
						}
						if(!date_to.equals("")){
								if(!qw.equals("")) qw += " and ";
								qw += which_date+" <= ? ";
						}						
						if(!qw.equals("")){
								qq += " where "+qw;
						}
						qq += " order by p.name ";
						logger.debug(qq);
						pstmt = con.prepareStatement(qq);
						int jj=1;
						if(!salary_group_id.equals("")){
								pstmt.setString(jj++, salary_group_id);								
						}
						if(!employee_id.equals("")){
								pstmt.setString(jj++, employee_id);
						}
						if(!group_id.equals("")){
								pstmt.setString(jj++, group_id);
						}						
						if(!effective_date.equals("")){
								java.util.Date date_tmp = df.parse(effective_date);
								pstmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
						}
						if(!pay_period_id.equals("")){
								pstmt.setString(jj++, pay_period_id);
						}
						if(!position_id.equals("")){
								pstmt.setString(jj++, position_id);
						}
						if(!department_id.equals("")){
								pstmt.setString(jj++, department_id);
						}
						if(!date_from.equals("")){
								java.util.Date date_tmp = df.parse(date_from);
								pstmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
						}
						if(!date_to.equals("")){
								java.util.Date date_tmp = df.parse(date_to);
								pstmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
						}						
						rs = pstmt.executeQuery();
						while(rs.next()){
								if(jobTasks == null)
										jobTasks = new ArrayList<>();
								JobTask one =
										new JobTask(
																rs.getString(1),
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
																rs.getString(15),
																rs.getString(16) != null,
																rs.getString(17),
																rs.getString(18),
																rs.getString(19),
																rs.getString(20),
																rs.getString(21) != null, 
																rs.getString(22) // position name
																);
							 
								if(!jobTasks.contains(one))
										jobTasks.add(one);
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
    public String findForUpdate(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", qw="";
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
						"j.clock_time_required,"+
						"j.hourly_rate,"+
						"date_format(j.added_date,'%m/%d/%Y'),"+
						
						"j.inactive,  "+
						" sg.name,sg.description,sg.default_regular_id,"+
						" sg.excess_culculation,sg.inactive,"+
						" p.name, "+
						" e.employee_number "+
						" from jobs j,employees e, "+
						" salary_groups sg, "+
						" positions p ";
				qq += " where g.id=j.salary_group_id and e.id=j.employee_id and e.employee_number is not null and j.position_id=p.id "+
						" and e.inactive is null ";
				// active only
				qq += " and j.inactive is null ";
				// current only
				qq += " and j.effective_date < now() and (j.expire_date > now() or j.expire_date is null) ";
				
				con = UnoConnect.getConnection();
				if(con == null){
						msg = " Could not connect to DB ";
						return msg;
				}				
				logger.debug(qq);
				try{
						pstmt = con.prepareStatement(qq);
						rs = pstmt.executeQuery();
						while(rs.next()){
								if(jobTasks == null)
										jobTasks = new ArrayList<>();
								JobTask one =
										new JobTask(
																rs.getString(1),
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
																rs.getString(15),																
																rs.getString(16) != null,
																rs.getString(17),
																rs.getString(18),
																rs.getString(19),
																rs.getString(20),
																rs.getString(21) != null,
																rs.getString(22),
																rs.getString(23)
																);
								if(!jobTasks.contains(one))
										jobTasks.add(one);
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
						
}
