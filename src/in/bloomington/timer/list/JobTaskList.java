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
import javax.naming.*;
import javax.naming.directory.*;
import org.apache.log4j.Logger;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;

public class JobTaskList{

		static final long serialVersionUID = 2500L;
		static Logger logger = Logger.getLogger(JobTaskList.class);
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		List<JobTask> jobTasks = null;
		boolean active_only = false, current_only = false;
		String salary_group_id="", employee_id="", pay_period_id="";
		String effective_date = "";
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
		public void setActiveOnly(){
				active_only = true;
		}
    public void setEmployee_id(String val){
				if(val != null)
						employee_id = val;
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
				if(val != null)
						salary_group_id = val;
    }

		public void setCurrentOnly(){
				// current_only = true;
				active_only = true;
		}
    //
		public String find(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", qw="";
				String qq = "select j.id,j.position_id,j.salary_group_id,j.employee_id,date_format(j.effective_date,'%m/%d/%Y'),date_format(j.expire_date,'%m/%d/%Y'),j.primary_flag,"+
						"j.weekly_regular_hours,"+
						"j.comp_time_weekly_hours,"+
						"j.comp_time_factor,"+
						"j.holiday_comp_factor,"+
						"j.inactive,  "+
						"g.name,g.description,g.default_regular_id,g.inactive "+
						" from jobs j "+
						" left join salary_groups g on g.id=j.salary_group_id ";
				logger.debug(qq);
				if(active_only){
						qw += " j.inactive is null ";
				}
				if(current_only){
						qw += " j.effective_date < now() and (j.expire_date > now() or j.exire_date is null) ";
				}
				try{
						if(!salary_group_id.equals("")){
								if(!qw.equals("")) qw += " and ";
								qw += " (j.salary_group_id = ? or j.salary_group_id is null)";
						}
						if(!employee_id.equals("")){
								if(!qw.equals("")) qw += " and ";
								qw += " j.employee_id = ? ";
						}
						if(!effective_date.equals("")){
								if(!qw.equals("")) qw += " and ";
								qw += " j.effective_date <= ? and (j.expire_date > ? or j.expire_date is null)";
						}
						if(!pay_period_id.equals("")){
								qq += ", pay_periods pp ";
								if(!qw.equals("")) qw += " and ";
								qw += " j.effective_date <= pp.start_date and (j.expire_date > ? or j.expire_date is null)";
						}				
						if(!qw.equals("")){
								qq += " where "+qw;
						}
						con = Helper.getConnection();
						if(con == null){
								msg = " Could not connect to DB ";
								logger.error(msg);
								return msg;
						}
						logger.debug(qq);
						// System.err.println(qq);
						
						pstmt = con.prepareStatement(qq);
						int jj=1;
						if(!salary_group_id.equals("")){
								pstmt.setString(jj++, salary_group_id);								
						}
						if(!employee_id.equals("")){
								pstmt.setString(jj++, employee_id);
						}
						if(!effective_date.equals("")){
								java.util.Date date_tmp = df.parse(effective_date);
								pstmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
						}
						if(!pay_period_id.equals("")){
								pstmt.setString(jj++, pay_period_id);
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
													 rs.getString(7) != null,
													 rs.getInt(8),
													 rs.getInt(9),
													 rs.getDouble(10),
													 rs.getDouble(11),
													 rs.getString(12) != null,
													 rs.getString(13),
													 rs.getString(14),
													 rs.getString(15),
													 rs.getString(16) != null);
							 if(!jobTasks.contains(one))
									 jobTasks.add(one);
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
