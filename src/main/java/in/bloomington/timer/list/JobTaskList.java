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
    //
    // jobs are recent if entered within 14 days
    boolean avoid_recent_jobs = false;
    //
    boolean include_future = false, order_by_employee = false,
	order_by_id = false, order_by_expire=false;
    boolean irregular_work_days = false, include_in_auto_batch=false;
    String salary_group_id="", employee_id="", pay_period_id="";
    String id="", effective_date = "", which_date="j.effective_date",
	date_from="", date_to="", position_id="", employee_name="",
	department_id="", group_id="", location_id="";
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
    public void setLocation_id(String val){
	if(val != null && !val.equals("-1"))
	    location_id = val;
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
	if(salary_group_id.isEmpty())
	    return "-1";
	return salary_group_id;
    }
    public String getGroup_id(){
	if(group_id.isEmpty())
	    return "-1";

	return group_id;
    }		
    public String getPosition_id(){
	if(position_id.isEmpty())
	    return "-1";
	return position_id;
    }
    public String getEmployee_id(){
	// for auto_complete
	return employee_id;
    }
    public String getDepartment_id(){
	if(department_id.isEmpty())
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
    public boolean getIncludeInAutoBatch(){
	return include_in_auto_batch;
    }
    public boolean getIrregularWorkDays(){
	return irregular_work_days;
    }		
    public void setNotExpired(){
	not_expired = true;
    }
    public void setNonTemp(){
	non_temp_emp = true;
    }
    public void setIncludeFuture(){
	include_future = true;
    }
    public void setIncludeInAutoBatch(boolean val){
	if(val)
	    include_in_auto_batch = true;
    }
    public void setIrregularWorkDays(boolean val){
	if(val)
	    irregular_work_days = true;
    }
    public void setOrderByEmployee(){
	order_by_employee = true;
    }
    public void setOrderById(){
	order_by_id = true;
    }
    public void setOrderByExpireDate(){
	order_by_expire = true;
    }
    public List<Group> getGroups(){
	if(groups == null){
	    if(!department_id.isEmpty()){
		GroupList gl = new GroupList();
		gl.setDepartment_id(department_id);
		gl.setActiveOnly();
		String back = gl.find();
		if(back.isEmpty()){
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
	PreparedStatement pstmt = null, pstmt2=null;
	ResultSet rs = null;
	String msg="", qw="", qo="", qq2="", qw2="";
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
	    "j.hourly_rate,"+
	    "date_format(j.added_date,'%m/%d/%Y'),"+
	    "j.irregular_work_days,"+
	    "j.inactive,  "+
						
	    "sg.name,sg.description,sg.default_regular_id,"+
	    "sg.excess_culculation,sg.inactive, "+
	    "p.name, "+ // position name
	    // group
	    "g.id,g.name,g.description,g.department_id,"+
	    "g.excess_hours_earn_type,"+ 
	    "g.allow_pending_accrual,"+
	    "g.clock_time_required,"+
	    "g.include_in_auto_batch,"+	    
	    "g.inactive,"+
	    "d.name,d.description,d.ref_id,d.ldap_name,"+
	    "d.allow_pending_accrual,d.inactive "+		
	    " from jobs j ";
	qq += " join salary_groups sg on sg.id=j.salary_group_id "+
	    " join positions p on j.position_id=p.id "+						
	    " join groups g on j.group_id=g.id ";
	if(!location_id.isEmpty()){
	    qq2 = qq;
	    qq += " left join group_locations gl on gl.group_id=j.group_id ";
	    qw += " gl.location_id = ? ";
	}
	qq += " join departments d on d.id=g.department_id ";
	if(!location_id.isEmpty()){
	    qq2 += " join departments d on d.id=g.department_id ";
	}
	logger.debug(qq);
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = " Could not connect to DB ";
	    logger.error(msg);
	    return msg;
	}
	if(active_only){
	    if(!qw.isEmpty()) qw += " and ";    
	    qw += " j.inactive is null ";
	    if(!qw2.isEmpty())	qw2 += " and ";    
	    qw2 += " j.inactive is null ";
	}
	else if(inactive_only){
	    if(!qw.isEmpty())	qw += " and ";    
	    qw += " j.inactive is not null ";
	    if(!qw2.isEmpty())	qw2 += " and ";    	    
	    qw2 += " j.inactive is not null ";
	}
	if(include_in_auto_batch){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " g.include_in_auto_batch is not null ";
	    if(!qw2.isEmpty())	qw2 += " and ";
	    qw2 += " g.include_in_auto_batch is not null ";	    
	    
	}
	if(irregular_work_days){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " j.irregular_work_days is not null ";
	    if(!qw2.isEmpty())	qw2 += " and ";
	    qw2 += " j.irregular_work_days is not null ";	    
	}
	if(not_expired){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " j.expire_date is null "; 						    if(!qw2.isEmpty())	qw2 += " and ";
	    qw2 += " j.expire_date is null "; 		    
	}
	if(current_only){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " j.effective_date <= curdate() and (j.expire_date >= curdate() or j.exire_date is null) ";
	    if(!qw2.isEmpty())	qw2 += " and ";
	    qw2 += " j.effective_date <= curdate() and (j.expire_date >= curdate() or j.exire_date is null) ";	    
	}
	if(avoid_recent_jobs){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " j.added_date < date_add(curdate(),INTERVAL 10 DAY) ";
	    if(!qw2.isEmpty())	qw2 += " and ";
	    qw2 += " j.added_date < date_add(curdate(),INTERVAL 10 DAY) ";
	}				
	if(clock_time_required){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " g.clock_time_required is not null ";
	    if(!qw2.isEmpty())	qw2 += " and ";
	    qw2 += " g.clock_time_required is not null ";
	}
	else if(clock_time_not_required){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " g.clock_time_required is null ";
	    if(!qw2.isEmpty())	qw2 += " and ";
	    qw2 += " g.clock_time_required is null ";
	}
	if(include_future){
	    if(!qw.isEmpty()) qw += " and ";				     
	    qw += " (j.expire_date > curdate() or j.expire_date is null) ";
	    if(!qw2.isEmpty())	qw2 += " and ";
	    qw2 += " (j.expire_date > curdate() or j.expire_date is null) ";	    
	}
	if(!salary_group_id.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " j.salary_group_id = ? ";
	    if(!qw2.isEmpty())	qw2 += " and ";
	    qw2 += " j.salary_group_id = ? ";	    
	}
	else if(non_temp_emp){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " j.salary_group_id <> 3 "; // Temp salary is 3
	    if(!qw2.isEmpty())	qw2 += " and ";
	    qw2 += " j.salary_group_id <> 3 "; // Temp salary is 3
	}
	if(!employee_id.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " j.employee_id = ? ";
	    if(!qw2.isEmpty())	qw2 += " and ";
	    qw2 += " j.employee_id = ? ";
	}
	if(!group_id.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " j.group_id = ? ";
	    if(!qw2.isEmpty())	qw2 += " and ";
	    qw2 += " j.group_id = ? ";	    
	}						
	if(!effective_date.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " j.effective_date <= ? and (j.expire_date > ? or j.expire_date is null)";
	    if(!qw2.isEmpty())	qw2 += " and ";
	    qw2 += " j.effective_date <= ? and (j.expire_date > ? or j.expire_date is null)";	    
	}
	if(!pay_period_id.isEmpty()){
	    qq += ", pay_periods pp ";
	    if(!qq2.isEmpty())
		qq2 += ", pay_periods pp ";		
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " j.effective_date <= pp.start_date and (j.expire_date >= pp.end_date or j.expire_date is null) and pp.id = ?";
	    if(!qw2.isEmpty())	qw2 += " and ";
	    qw2 += " j.effective_date <= pp.start_date and (j.expire_date >= pp.end_date or j.expire_date is null) and pp.id = ?";	    
	    
	}
	if(!position_id.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " j.position_id = ? ";
	    if(!qw2.isEmpty())	qw2 += " and ";
	    qw2 += " j.position_id = ? ";	    
	}
	if(!department_id.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";				    	
	    qw += " d.id = ? ";
	    if(!qw2.isEmpty())	qw2 += " and ";
	    qw2 += " d.id = ? ";	    
	}
	if(!date_from.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += which_date+" >= ? ";
	    if(!qw2.isEmpty())	qw2 += " and ";
	    qw2 += which_date+" >= ? ";	    
	}
	if(!date_to.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += which_date+" <= ? ";
	    if(!qw2.isEmpty())	qw2 += " and ";
	    qw2 += which_date+" <= ? ";	    
	}
	if(order_by_employee){
	    qq += " left join employees e on e.id=j.employee_id ";
	    if(!qq2.isEmpty())
		qq2 += " left join employees e on e.id=j.employee_id ";		
	    qo = " order by e.last_name,e.first_name";
	}
	else if(order_by_id){
	    qo = " order by j.id DESC ";
	}
	else if(order_by_expire){
	    qo = " order by j.expire_date, p.name ";
	}
	else{
	    qo = " order by p.name ";
	}
	if(!qw.isEmpty()){
	    qq += " where "+qw;
	}
	if(!qq2.isEmpty()){
	    if(!qw2.isEmpty())	    
		qq2 += " where "+qw2;
	    qq2 += qo;
	}
	qq += qo;
	logger.debug(qq);
	// System.err.println(" jobs "+qq);
	try{
						
	    pstmt = con.prepareStatement(qq);
	    int jj=1;
	    if(!location_id.isEmpty()){
		pstmt.setString(jj++, location_id);
	    }							    
	    if(!salary_group_id.isEmpty()){
		pstmt.setString(jj++, salary_group_id);								
	    }
	    if(!employee_id.isEmpty()){
		pstmt.setString(jj++, employee_id);
	    }
	    if(!group_id.isEmpty()){
		pstmt.setString(jj++, group_id);
	    }						
	    if(!effective_date.isEmpty()){
		java.util.Date date_tmp = df.parse(effective_date);
		pstmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
	    }
	    if(!pay_period_id.isEmpty()){
		pstmt.setString(jj++, pay_period_id);
	    }
	    if(!position_id.isEmpty()){
		pstmt.setString(jj++, position_id);
	    }
	    if(!department_id.isEmpty()){
		pstmt.setString(jj++, department_id);
	    }

	    if(!date_from.isEmpty()){
		java.util.Date date_tmp = df.parse(date_from);
		pstmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
	    }
	    if(!date_to.isEmpty()){
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
				// rs.getString(13) != null,
				rs.getDouble(13),
				rs.getString(14),
				// rs.getString(16) != null,
				rs.getString(15) != null,
				rs.getString(16) != null,
				rs.getString(17),
				rs.getString(18),
				rs.getString(19),
				rs.getString(20),
				rs.getString(21) != null, 
				rs.getString(22), // position name
				//
				// group
				rs.getString(23),
				rs.getString(24),
				rs.getString(25),
				rs.getString(26),
				rs.getString(27),
				rs.getString(28) != null,
				rs.getString(29) != null,
				rs.getString(30) != null,
				rs.getString(31) != null,
				//
				rs.getString(32),
				rs.getString(33),
				rs.getString(34),
				rs.getString(35),
				rs.getString(36) != null,
				rs.getString(37) != null
				);
		if(!jobTasks.contains(one))
		    jobTasks.add(one);
	    }
	    if(jobTasks == null || jobTasks.size() == 0){
		if(!qq2.isEmpty()){
		    pstmt2 = con.prepareStatement(qq2);
		    jj=1;
		    if(!salary_group_id.isEmpty()){
			pstmt2.setString(jj++, salary_group_id);								
		    }
		    if(!employee_id.isEmpty()){
			pstmt2.setString(jj++, employee_id);
		    }
		    if(!group_id.isEmpty()){
			pstmt2.setString(jj++, group_id);
		    }						
		    if(!effective_date.isEmpty()){
			java.util.Date date_tmp = df.parse(effective_date);
			pstmt2.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
		    }
		    if(!pay_period_id.isEmpty()){
			pstmt2.setString(jj++, pay_period_id);
		    }
		    if(!position_id.isEmpty()){
			pstmt2.setString(jj++, position_id);
		    }
		    if(!department_id.isEmpty()){
			pstmt2.setString(jj++, department_id);
		    }
		    
		    if(!date_from.isEmpty()){
			java.util.Date date_tmp = df.parse(date_from);
			pstmt2.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
		    }
		    if(!date_to.isEmpty()){
			java.util.Date date_tmp = df.parse(date_to);
			pstmt2.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
		    }						
		    rs = pstmt2.executeQuery();	
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
					// rs.getString(13) != null,
					rs.getDouble(13),
					rs.getString(14),
					//rs.getString(16) != null,
					rs.getString(15) != null,
					rs.getString(16) != null,
					rs.getString(17),
					rs.getString(18),
					
					rs.getString(19),
					rs.getString(20),
					rs.getString(21) != null, 
					rs.getString(22), // position name
					//
					// group
					rs.getString(23),
					rs.getString(24),
					rs.getString(25),
					rs.getString(26),
					rs.getString(27),
					rs.getString(28) != null,
					rs.getString(29) != null,							rs.getString(30) != null,
					rs.getString(31) != null,
					
					rs.getString(32),
					rs.getString(33),
					rs.getString(34),
					rs.getString(35),
					rs.getString(36) != null,
					rs.getString(37) != null
					);
			if(!jobTasks.contains(one))
			    jobTasks.add(one);
		    }		    
		}
	    }
	}
	catch(Exception ex){
	    msg += " "+ex;
	    logger.error(msg+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(pstmt, rs);
	    Helper.databaseDisconnect(pstmt2, rs);
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
						
	    " j.comp_time_factor,"+
	    " j.holiday_comp_factor,"+
	    // " j.clock_time_required,"+
	    " j.hourly_rate,"+
	    " date_format(j.added_date,'%m/%d/%Y'),"+
	    // " j.include_in_auto_batch,"+
	    " j.irregular_work_days,"+
	    " j.inactive,  "+
	    " sg.name,sg.description,sg.default_regular_id,"+
	    " sg.excess_culculation,sg.inactive,"+
	    " p.name, "+
	    " e.employee_number "+
	    " from jobs j, employees e, "+
	    " salary_groups sg, "+
	    " positions p ";
	qq += " where sg.id=j.salary_group_id and e.id=j.employee_id and e.employee_number is not null and j.position_id=p.id "+
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
				// rs.getString(13) != null,
				rs.getDouble(13),
				rs.getString(14),
				// rs.getString(16) != null,
				rs.getString(15) != null,
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
    /**
     * in additon to find above we are looking for certain jobs that were
     * used in certain pay period to enter times
     */
    public String addJobsUsedInPayPeriod(){
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
						
	    " j.comp_time_factor,"+
	    " j.holiday_comp_factor,"+
	    // " j.clock_time_required,"+
	    " j.hourly_rate,"+
	    " date_format(j.added_date,'%m/%d/%Y'),"+
	    // " j.include_in_auto_batch,"+
	    " j.irregular_work_days,"+
	    " j.inactive,  "+
	    " sg.name,sg.description,sg.default_regular_id,"+
	    " sg.excess_culculation,sg.inactive,"+
	    " p.name, "+
	    " e.employee_number "+
	    " from jobs j, employees e, "+
	    " salary_groups sg, "+
	    " positions p ";
	qw = " where sg.id=j.salary_group_id and e.id=j.employee_id and e.employee_number is not null and j.position_id=p.id "+
	    " and e.inactive is null ";
	if(!employee_id.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " j.employee_id = ? ";
	}
	if(!pay_period_id.isEmpty()){
	    qq += ", time_documents d ";
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " j.id = d.job_id and d.pay_period_id = ? ";
	}				
	qq += qw;
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = " Could not connect to DB ";
	    return msg;
	}				
	logger.debug(qq);
	try{
	    pstmt = con.prepareStatement(qq);
	    int jj=1;
	    if(!employee_id.isEmpty()){
		pstmt.setString(jj++, employee_id);
	    }
	    if(!pay_period_id.isEmpty()){
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
				rs.getString(7),
				rs.getString(8) != null,
				rs.getInt(9),
				rs.getInt(10),
				rs.getDouble(11),
				rs.getDouble(12),
				// rs.getString(13) != null,
				rs.getDouble(13),
				rs.getString(14),																
				// rs.getString(16) != null,
				rs.getString(15) != null,
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

		
    /**
       ;;
       ;; find employees jobs and group names
       ;;
       select e.employee_number,                                                       concat_ws(' ',e.last_name,e.first_name) full_name,                              p.name job_name,                                                                g.name group_name                                                               from jobs j                                                                     join positions p on j.position_id=p.id                                          join employees e on j.employee_id=e.id                                          join groups g on g.id = j.group_id                                              where j.inactive is null and e.inactive is null                                 and e.employee_number is not null                                               and j.effective_date < now()                                                    and (j.expire_date > now() or j.expire_date is null)                            order by p.name,e.employee_number                                               into outfile '/var/lib/mysql-files/employee_jobs.csv'                           FIELDS TERMINATED BY ','                                                        ENCLOSED BY '"'                                                                 LINES TERMINATED BY '\n'

    */
		
}
