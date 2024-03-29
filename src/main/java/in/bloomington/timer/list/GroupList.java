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

public class GroupList{

    static final long serialVersionUID = 1600L;
    static Logger logger = LogManager.getLogger(GroupList.class);
    String employee_id = "",
	department_ids ="",
	department_id="",
	pay_period_id="",
	job_name="", 
	dept_ref_id = ""; // reference number in NW
    String name="", id="";
    boolean active_only = false, inactive_only=false;
    boolean allowed = false, not_allowed=false;
    boolean include_future = false, include_in_auto_batch=false;
    boolean clock_time_required = false, clock_time_not_required=false;
    List<Group> groups = null;
    String clock_status="";
    public GroupList(){
    }
    public GroupList(String val){
	setEmployee_id(val);
    }
    public void setId (String val){
	if(val != null)
	    id = val;
    }		
    public void setEmployee_id (String val){
	if(val != null)
	    employee_id = val;
    }
    public void setPay_period_id (String val){
	if(val != null && !val.isEmpty())
	    pay_period_id = val;
    }
    public void setDept_ref_id (String val){
	if(val != null && !val.isEmpty())
	    dept_ref_id = val;
    }
    public void setJobName(String val){
	if(val != null && !val.isEmpty())
	    job_name = val;
    }		
    public void setDepartment_id (String val){
	if(val != null && !val.equals("-1")){
	    if(!department_id.isEmpty()){
		department_id = val;
	    }
	    if(!department_ids.isEmpty()){
		department_ids +=",";
	    }
	    department_ids += val;
	}
    }
    public void setName(String val){
	if(val != null)
	    name = val;
    }
    public void setActive_status(String val){
	if(val != null && !val.equals("-1")){
	    if(val.equals("Active"))
		active_only = true;
	    else if(val.equals("Inactive"))
		inactive_only = true;
	}
    }
    public void setPending_accrual_status(String val){
	if(val != null && !val.equals("-1")){
	    if(val.equals("Allowed"))
		allowed = true;
	    else if(val.equals("Not_Allowed"))
		not_allowed = true;
	}
    }
    public String getPending_accrual_status(){
	if(allowed)
	    return "Allowed";
	else if(not_allowed)
	    return "Not_Allowed";
	else
	    return "-1";
    }
    public void setActiveOnly(){
	active_only = true;
    }
    public void setClock_status(String val){
	if(val != null && !val.equals("-1")){
	    if(val.equals("y")) 
		clock_time_required = true;
	    else if(val.equals("n"))
		clock_time_not_required = true;
	}
    }		    
    public String getDepartment_id(){
	if(!department_ids.isEmpty()){
	    if(department_ids.indexOf(",") > -1){
		department_id = department_ids.substring(0, department_ids.indexOf(","));
	    }
	    else{
		department_id = department_ids;
	    }
	}
	if(department_id.isEmpty())
	    return "-1";
	return department_id;
    }
    public String getId(){
	return id;
    }		
    public String getName(){
	return name;
    }
    public String getJobName(){
	return job_name;
    }		
    public List<Group> getGroups(){
	return groups;
    }
    public String getDept_ref_id(){
	return dept_ref_id;
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
    public void setIncludeInAutoBatch(String val){
	if(val != null && val.equals("true"))
	    include_in_auto_batch = true;
    }    
    public void setIncludeFuture(){
	include_future = true;
    }
    //
    // getters
    //
    public String find(){
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = "select g.id,g.name,g.description,g.department_id,g.excess_hours_earn_type,g.allow_pending_accrual,"+
	    "g.clock_time_required,"+
	    "g.include_in_auto_batch,"+
	    "g.inactive,"+
	    "d.name, "+
	    "d.description,d.ref_id,d.ldap_name,d.allow_pending_accrual,"+
	    "d.inactive "+
	    "from `groups` g join departments d on d.id=g.department_id ";							
				
	String qw = "";
	if(!department_ids.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";						
	    qw += "g.department_id in ("+department_ids+") ";
	}
	if(!dept_ref_id.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";						
	    qw += " find_in_set (?, d.ref_id) and d.ref_id is not null";
	}				
	if(!name.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";						
	    qw += "g.name like ? ";
	}
	if(!job_name.isEmpty()){
	    qq += " join jobs j on j.group_id=g.id join positions p on p.id = j.position_id ";
	    if(!qw.isEmpty()) qw += " and ";						
	    qw += "p.name like ? ";
	}
	if(clock_time_required){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " g.clock_time_required is not null ";
	}
	else if(clock_time_not_required){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " g.clock_time_required is null ";
	}
	if(include_in_auto_batch){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " g.include_in_auto_batch is not null ";
	}	
	if(!employee_id.isEmpty()
	   || include_future
	   || !pay_period_id.isEmpty()){
	    qq += " join jobs j on j.group_id = g.id ";
	    if(!employee_id.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";
		qw +=	" j.employee_id=?";
	    }
	    if(active_only){
		if(!qw.isEmpty()) qw += " and ";
		qw += " j.inactive is null ";
	    }
	    if(include_future){
		if(!qw.isEmpty()) qw += " and ";
		qw +=	" (j.expire_date is null or j.expire_date >= curdate())";
	    }
	    else if(!pay_period_id.isEmpty()){
		qq += ", pay_periods pp ";
		if(!qw.isEmpty()) qw += " and ";										
		qw +=	" j.effective_date <= pp.start_date and (j.expire_date is null or j.expire_date > pp.start_date) and pp.id=? ";
	    }
	}
	if(active_only){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " g.inactive is null ";
	}
	if(allowed){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " g.allow_pending_accrual is not null ";
	}
	if(not_allowed){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " g.allow_pending_accrual is null ";
	}				
	if(!qw.isEmpty()){
	    qq += " where "+qw;
	}
	qq += " order by g.name ";
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = " Could not connect to DB ";
	    logger.error(msg);
	    return msg;
	}
	logger.debug(qq);
	try{
	    pstmt = con.prepareStatement(qq);
	    int jj=1;
	    if(!department_ids.isEmpty()){

	    }
	    if(!dept_ref_id.isEmpty()){
		pstmt.setString(jj++, dept_ref_id);
	    }
	    if(!name.isEmpty()){
		pstmt.setString(jj++, "%"+name+"%");
	    }
	    if(!job_name.isEmpty()){
		pstmt.setString(jj++, job_name);
	    }						
	    if(!employee_id.isEmpty()){
		pstmt.setString(jj++, employee_id);
	    }
	    if(!include_future && !pay_period_id.isEmpty()){
		pstmt.setString(jj++, pay_period_id);
	    }	    
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		if(groups == null)
		    groups = new ArrayList<>();
		Group one = new Group(
				      rs.getString(1),
				      rs.getString(2),
				      rs.getString(3),
				      rs.getString(4),
				      rs.getString(5),
				      rs.getString(6) != null,
				      rs.getString(7) != null,
				      rs.getString(8) != null,
				      rs.getString(9) != null,				      
				      // dept
				      rs.getString(10),
				      rs.getString(11),
				      rs.getString(12),
				      rs.getString(13),
				      rs.getString(14) != null,
				      rs.getString(15) != null
				      );
		if(!groups.contains(one))
		    groups.add(one);
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
