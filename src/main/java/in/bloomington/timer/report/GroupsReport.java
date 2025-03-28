package in.bloomington.timer.report;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.ArrayList;
import java.util.*;
import java.sql.*;
import java.text.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;
import in.bloomington.timer.list.*;

public class GroupsReport{

    //
    // find all groups in a department and the groups current managers
    //
    static Logger logger = LogManager.getLogger(GroupsReport.class);
    static final long serialVersionUID = 3820L;
    String department_id = "";
    Department department = null;
    List<List<String>> entries = null;
    List<List<String>> entries2 = null;    
    public GroupsReport(){

    }
    public void setDepartment_id(String val){
	if(val != null && !val.equals("-1")){
	    department_id = val;
	    getDepartment(); 
	}
    }
    public String getDepartment_id(){
	if(department_id.isEmpty())
	    return "-1";
	return department_id;
    }
    public Department getDepartment(){
	if(department == null && !department_id.isEmpty()){
	    Department one = new Department(department_id);
	    String back = one.doSelect();
	    if(back.isEmpty()){
		department = one;
	    }
	}
	return department;
    }
    public boolean hasDepartment(){
	getDepartment();
	return department != null;
    }
    public List<List<String>> getJobs(){
	return entries;
    }
    public List<List<String>> getManagers(){
	return entries2;
    }    
    public boolean hasJobs(){
	return entries != null && entries.size() > 0;
    }
    public boolean hasManagers(){
	return entries2 != null && entries2.size() > 0;
    }    
    public String findJobs(){
	String msg = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "select "+
	    "g.name group_name,"+	
	    "concat_ws(' ',e.first_name, e.last_name) emp_name,"+
	    "p.name job_title,"+
	    "sg.name salary_group, "+	    
	    "j.weekly_regular_hours week_hours, "+
	    "j.comp_time_weekly_hours week_comp_hours "+
	    "from jobs j "+ 
	    "join salary_groups sg on sg.id=j.salary_group_id "+ 
	    "join employees e on e.id = j.employee_id "+
	    "join positions p on j.position_id=p.id "+
	    "join `groups` g on j.group_id=g.id "+
	    "join departments d on d.id=g.department_id "+
	    "where d.id=? "+
	    "and j.expire_date is null "+
	    "order by group_name,emp_name ";
	if(department_id.isEmpty()){
	    msg = " department ID not specified";
	    return msg;
	}
	con = Helper.getConnection();
	if(con == null){
	    msg = " Could not connect to DB ";
	    logger.error(msg);
	    return msg;
	}
	logger.debug(qq);
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, department_id);
	    
	    rs = pstmt.executeQuery();
	    if(entries == null)
		entries = new ArrayList<>();
	    List<String> row = new ArrayList<>();
	    row.add("Dept - Group Name");
	    row.add("Employee");
	    row.add("Job Title");
	    row.add("Salary Group");
	    row.add("Weekly Hrs");
	    row.add("Weekly Comp Hrs");
	    entries.add(row);
	    while(rs.next()){
		String group_name = rs.getString(1);
		String emp_name = rs.getString(2);
		String job_title = rs.getString(3);
		String salary_group = rs.getString(4);
		String weekly_hrs = rs.getString(5);
		String comp_hrs = rs.getString(6);		
		row = new ArrayList<>();
		row.add(group_name);
		row.add(emp_name);
		row.add(job_title);
		row.add(salary_group);
		row.add(weekly_hrs);
		row.add(comp_hrs);
		entries.add(row);
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
    public String findManagers(){
	String msg = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "select a.group_name, a.approvers,b.processors from( "+
	    "select g.name group_name, g.id group_id, group_concat(distinct concat_ws(' ',e.first_name,e.last_name, if(gm.primary_flag is not null,\"(Primary)\",\"\")) "+
	    "order by gm.primary_flag desc, e.first_name "+
	    "asc separator ';') approvers "+
	    "from `groups` g "+
	    "left join group_managers gm on g.id=gm.group_id "+
	    "join workflow_nodes wn on wn.id=gm.wf_node_id "+
	    "join employees e on e.id=gm.employee_id "+
	    "where g.department_id=? and gm.expire_date is null "+
	    "and wn.id=3 "+
	    "group by g.id order by g.name) a, "+
	    "(select g.name group_name, g.id group_id, "+
	    "group_concat(distinct concat_ws(' ',e.first_name,e.last_name, if(gm.primary_flag is not null,\"(Primary)\",\"\")) "+
	    "order by gm.primary_flag desc, e.first_name "+
	    "asc separator ';') processors "+
	    "from `groups` g "+
	    "left join group_managers gm on g.id=gm.group_id "+
	    "join workflow_nodes wn on wn.id=gm.wf_node_id "+
	    "join employees e on e.id=gm.employee_id "+
	    "where g.department_id=? and gm.expire_date is null "+
	    "and wn.id=4 "+
	    "group by g.id order by g.name) b "+
	    "where a.group_id=b.group_id";
	if(department_id.isEmpty()){
	    msg = " department ID not specified";
	    return msg;
	}
	con = Helper.getConnection();
	if(con == null){
	    msg = " Could not connect to DB ";
	    logger.error(msg);
	    return msg;
	}
	logger.debug(qq);
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, department_id);
	    pstmt.setString(2, department_id);	    
	    rs = pstmt.executeQuery();
	    if(entries2 == null)
		entries2 = new ArrayList<>();
	    List<String> row = new ArrayList<>();
	    row.add("Dept - Group Name");
	    row.add("Approvers");
	    row.add("Payroll Approvers");
	    entries2.add(row);
	    while(rs.next()){
		String group_name = rs.getString(1);
		String approvers = rs.getString(2);
		String processors = rs.getString(3);
		row = new ArrayList<>();
		row.add(group_name);
		row.add(approvers);
		row.add(processors);
		entries2.add(row);
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
	 select g.name group_name,concat_ws(' ',e.first_name, e.last_name) emp_name,wn.name role_name,gm.primary_flag isPrimary                                          from `groups` g left join group_managers gm on g.id=gm.group_id                   join workflow_nodes wn on wn.id=gm.wf_node_id                                   join employees e on e.id=gm.employee_id                                         where g.department_id=? and gm.expire_date is null                              and gm.inactive is null                                                         and wn.id in (3,4,5)                                                            order by group_name, role_name, emp_name ";

	 
	 // employee job and groups
	select
	    g.name group_name,	
	    concat_ws(' ',e.first_name, e.last_name) emp_name,
	    p.name job_title ,
	    sg.name salary_group,	    
	    j.weekly_regular_hours week_hours,
	    j.comp_time_weekly_hours week_comp_hours
	    from jobs j 
	    join salary_groups sg on sg.id=j.salary_group_id 
	    join employees e on e.id = j.employee_id 
	    join positions p on j.position_id=p.id 
	    join `groups` g on j.group_id=g.id 
	    join departments d on d.id=g.department_id 
	    where d.id=1
	    and j.expire_date is null
	    order by group_name,emp_name


//
// group managers
//
	    select a.group_name, a.approvers,b.processors from(
	    select g.name group_name, g.id group_id, group_concat(distinct concat_ws(' ',e.first_name,e.last_name, if(gm.primary_flag is not null,"(Primary)","")) 
	    order by e.first_name 
	    asc separator ';') approvers
	    from `groups` g
	    left join group_managers gm on g.id=gm.group_id
	    join workflow_nodes wn on wn.id=gm.wf_node_id
	    join employees e on e.id=gm.employee_id
	    where g.department_id=1 and gm.expire_date is null
	    and wn.id=3
	    group by g.id order by g.name) a,
	    (select g.name group_name, g.id group_id, 
	    group_concat(distinct concat_ws(' ',e.first_name,e.last_name, if(gm.primary_flag is not null,"(Primary)","")) 
	    order by e.first_name
	    asc separator ';') processors
	    from `groups` g
	    left join group_managers gm on g.id=gm.group_id
	    join workflow_nodes wn on wn.id=gm.wf_node_id
	    join employees e on e.id=gm.employee_id
	    where g.department_id=1 and gm.expire_date is null
	    and wn.id=4
	    group by g.id order by g.name) b
	    where a.group_id=b.group_id;
	    
	    
     */
}

