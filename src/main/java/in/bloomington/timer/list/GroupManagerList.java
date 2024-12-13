package in.bloomington.timer.list;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.ArrayList;
import java.util.List;
import java.text.*;
import java.util.Date;
import java.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;

public class GroupManagerList{

    static final long serialVersionUID = 1900L;
    static Logger logger = LogManager.getLogger(GroupManagerList.class);
    String employee_id = "", group_id="", pay_period_id="";
    String group_ids = "", wf_node_id="";
    String execludeManager_id = ""; // employee_id
    boolean active_only = false,approversOnly=false,
	processorsOnly=false, exclude_reviewers = false,
	reviewersOnly=false, timeMaintainOnly = false,
	approversAndProcessorsOnly = false,
	leaveReviewOnly = false,
	notExpired = false;
		
    List<GroupManager> managers = null;
    public GroupManagerList(){
    }
    public GroupManagerList(String val){
	setEmployee_id(val);
    }
    public GroupManagerList(String val, String val2){
	setEmployee_id(val);
	setGroup_id(val2);
    }		
    public void setEmployee_id (String val){
	if(val != null)
	    employee_id = val;
    }
    public void setGroup_id (String val){
	if(val != null)
	    group_id = val;
    }
    public void setPay_period_id (String val){
	if(val != null)
	    pay_period_id = val;
    }
    public void addGroup_id(String val){
	if(val != null){
	    if(!group_ids.isEmpty()) group_ids +=",";
	    group_ids += val;
	}
    }
    public void setWorkflow_id(String val){
	if(val != null)
	    wf_node_id = val;
    }		
    public void setActiveOnly(){
	active_only = true;
    }
    public void setApproversOnly(){
	approversOnly = true;
    }
    public void setReviewersOnly(){
	reviewersOnly = true;
    }
    public void setProcessorsOnly(){
	processorsOnly = true;
    }
    public void setLeaveReviewOnly(){
	leaveReviewOnly = true;
    }    
    public void setApproversAndProcessorsOnly(){
	approversAndProcessorsOnly = true;
    }		
    public void setTimeMaintainerOnly(){
	timeMaintainOnly = true;
    }
    public void excludeReviewers(){
	exclude_reviewers = true;
    }
    public void setNotExpired(){
	notExpired = true;
    }
    public void execludeManager_id(String val){
	if(val != null)
	    execludeManager_id = val;
    }		
    public List<GroupManager> getManagers(){
	return managers;
    }
		
    //		
    //
    // getters
    //
    public String find(){
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = "select gm.id,gm.group_id,gm.employee_id,gm.wf_node_id,date_format(gm.start_date,'%m/%d/%Y'),date_format(gm.expire_date,'%m/%d/%Y'),gm.primary_flag,gm.inactive,wn.name from group_managers gm join `groups` g on g.id=gm.group_id join workflow_nodes wn on wn.id=gm.wf_node_id ";
	String qw = "";
	if(!group_id.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " gm.group_id=? ";
	}
	else if(!group_ids.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " gm.group_id in ("+group_ids+")";
	}
	if(!employee_id.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " gm.employee_id=?";
	}
	if(!wf_node_id.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " gm.wf_node_id=?";
	}				
	if(!execludeManager_id.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " gm.employee_id <> ?";
	}
	if(exclude_reviewers){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " gm.wf_node_id <> 6";
	}
	if(!pay_period_id.isEmpty()){
	    qq += ", pay_periods pp ";
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " pp.id=? and pp.start_date >= gm.start_date and (gm.expire_date is null or gm.expire_date > pp.start_date)";
	}
	if(approversOnly){
	    if(!qw.isEmpty()) qw += " and ";						
	    qw += " wn.name like 'Approve'";
	}
	else if(leaveReviewOnly){
	    if(!qw.isEmpty()) qw += " and ";						
	    qw += " wn.name like 'Leave%'";
	}	
	else if(processorsOnly){
	    if(!qw.isEmpty()) qw += " and ";						
	    qw += " wn.name like 'Payroll%'";
	}
	else if(reviewersOnly){
	    if(!qw.isEmpty()) qw += " and ";						
	    qw += " wn.name like 'Review'";
	}
	else if(timeMaintainOnly){
	    if(!qw.isEmpty()) qw += " and ";						
	    qw += " wn.name like 'Time%'";
	}
	else if(approversAndProcessorsOnly){
	    if(!qw.isEmpty()) qw += " and ";						
	    qw += "(wn.name like 'Approve' or wn.name like 'Payroll%')";
	}
	if(active_only){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " gm.inactive is null ";
	}
	if(notExpired){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " gm.expire_date is null ";
	}
	if(!qw.isEmpty()){
	    qq += " where "+qw;
	}
	qq += " order by gm.expire_date,gm.primary_flag desc, g.name ";
	logger.debug(qq);
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = " Could not connect to DB ";
	    logger.error(msg);
	    return msg;
	}
	try{
	    pstmt = con.prepareStatement(qq);
	    int jj=1;
	    if(!group_id.isEmpty()){
		pstmt.setString(jj++, group_id);
	    }						
	    if(!employee_id.isEmpty()){
		pstmt.setString(jj++, employee_id);
	    }
	    if(!wf_node_id.isEmpty()){
		pstmt.setString(jj++, wf_node_id);
	    }						
	    if(!execludeManager_id.isEmpty()){
		pstmt.setString(jj++, execludeManager_id);
	    }
	    if(!pay_period_id.isEmpty()){
		pstmt.setString(jj++, pay_period_id);
	    }						
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		if(managers == null)
		    managers = new ArrayList<>();
		GroupManager one = new GroupManager(
						    rs.getString(1),
						    rs.getString(2),
						    rs.getString(3),
						    rs.getString(4),
						    rs.getString(5),
						    rs.getString(6),
						    rs.getString(7) != null,
						    rs.getString(8) != null,
						    rs.getString(9)
						    );
		managers.add(one);
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
     //
     // find the list of managers for all groups in certain department
     //
     select g.name group_name,concat_ws(' ',e.first_name, e.last_name) emp_name,wn.name role_name,gm.primary_flag isPrimary                                          from group_managers gm join groups g on g.id=gm.group_id                        join workflow_nodes wn on wn.id=gm.wf_node_id                                   join employees e on e.id=gm.employee_id                                         where g.department_id=5 and gm.expire_date is null                              and gm.inactive is null                                                         and wn.id in (3,4,5)                                                            order by group_name, role_name, emp_name                                        into outfile '/var/lib/mysql-files/managers.csv'                                fields terminated by ','                                                        enclosed by '"'                                                                 lines terminated by '\n';


     // email list of the managers
     //
     select distinct concat_ws(' ',e.first_name, e.last_name) emp_name,                 e.email email                                                                from group_managers gm join groups g on g.id=gm.group_id                        join workflow_nodes wn on wn.id=gm.wf_node_id                                   join employees e on e.id=gm.employee_id                                         where g.department_id not in (16,20) and gm.expire_date is null                 and gm.inactive is null and (e.email is not null and e.email <> '')             and wn.id in (3,4,5)                                                            order by emp_name                                                               into outfile '/var/lib/mysql-files/manager_emails.csv'                          fields terminated by ','                                                        enclosed by '"'                                                                 lines terminated by '\n';				
				
    */
}
