package in.bloomington.timer.list;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.sql.*;
import java.io.*;
import javax.naming.*;
import javax.naming.directory.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.util.Helper;
import in.bloomington.timer.util.CommonInc;
import in.bloomington.timer.util.UnoConnect;
import in.bloomington.timer.bean.*;

public class EmployeeList extends CommonInc{

    static final long serialVersionUID = 1160L;
    static Logger logger = LogManager.getLogger(EmployeeList.class);
    String id = "", username="", name="",
	full_name="", group_id="", group_ids="", id_code="",
	ad_sid="",
	exclude_group_id="", groupManager_id="", department_id="",
	dept_ref_id="", // one or more values
	employee_number="",  exclude_name="",
	pay_period_id="",
	employee_ids = "", // comma separated
	no_document_for_payperiod_id="", added_status="";
    Set<String> group_id_set = new HashSet<>();
    boolean active_only = false, inactive_only = false,
	hasAdSid=false,
	hasEmployeeNumber=false, hasNoEmployeeNumber=false;
    boolean exclude_recent_records = false, recent_records_only=false;
    boolean includeAllDirectors = false, include_future = false;
    boolean used_time_track = false; // since last two weeks
    boolean not_terminated = false;
    List<Employee> employees = null;
    List<Group> groups = null;
    //
    // basic constructor
    public EmployeeList(){

    }
    public EmployeeList(String val){

	setName(val);
    }
    public String getId(){
	return id;
    }
    public String getEmployee_number(){
	return employee_number;
    }
    public String getId_code(){
	return id_code;
    }
    public String getAd_sid(){
	return ad_sid;
    }		
    public String getActiveStatus(){
	if(active_only)
	    return "Active";
	if(inactive_only)
	    return "Inactive";
	return "-1";
    }
    public String getGroup_id(){
	if(group_id.isEmpty())
	    return "-1";
	return group_id;
    }
    public String getAdded_status(){
	if(added_status.isEmpty())
	    return "-1";
	return added_status;
    }		
    public String getDepartment_id(){
	if(department_id.isEmpty())
	    return "-1";
	return department_id;
    }
    public String getName(){
	return name;
    }
    //
    // setters
    //
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setAd_sid(String val){
	if(val != null)
	    ad_sid = val;
    }		
    public void setName(String val){
	if(val != null)
	    name = val;
    }
    public void setUsername(String val){
	if(val != null)
	    username = val;
    }
    public void setId_code(String val){
	if(val != null)
	    id_code = val;
    }
    public void setEmployee_ids(String val){
	if(val != null)
	    employee_ids = val;
    }		
    public void setEmployee_number(String val){
	if(val != null)
	    employee_number = val;
    }		
    public void setDept_ref_id(String val){
	if(val != null)
	    dept_ref_id = val;
    }
    public void setDepartment_id(String val){
	if(val != null && !val.equals("-1"))
	    department_id = val;
    }		
    public void setGroup_id(String val){
	if(val != null && !val.equals("-1")){
	    group_id = val; // for the last
	    if(!group_id_set.contains(val)){
		group_id_set.add(val);
		if(!group_ids.isEmpty()) group_ids += ",";
		group_ids += val;
	    }
	}
    }

    public void setGroupManager_id(String val){
	if(val != null)
	    groupManager_id = val;
    }		
    public void setExclude_group_id(String val){
	if(val != null)
	    exclude_group_id = val;
    }
    public void setNoDocumentForPayPeriodId(String val){
	if(val != null)
	    no_document_for_payperiod_id = val;
    }
    public void setPay_period_id(String val){
	if(val != null)
	    pay_period_id = val;
    }
    public void setActiveOnly(){
	active_only = true;
    }
    public void setNotTerminated(){
	not_terminated = true;
    } 		
    public void setActiveStatus(String val){
	if(val != null && !val.equals("-1")){
	    if(val.equals("Active"))
		active_only = true;
	    else{
		inactive_only = true;
	    }
	}
    }
    public void setAdded_status(String val){
	if(val != null && !val.equals("-1")){
	    added_status = val;
	    if(val.equals("Recent"))
		recent_records_only = true;
	    else{
		exclude_recent_records = true;
	    }
	}
    }
    public void includeAllDirectors(){
	includeAllDirectors = true;
    }
    public void setHasEmployeeNumber(){
	hasEmployeeNumber = true;
    }
    public void setUsedTimeTrack(){
	used_time_track = true;
    }
    public void setHasAdSid(){
	hasAdSid = true;
    }		
    public void setHasNoEmployeeNumber(){
	hasNoEmployeeNumber = true;
    }
    // avoid record added within last 30 days
    public void excludeRecentRecords(){
	exclude_recent_records = true;
    }
    public void recentRecordsOnly(){
	recent_records_only = true;
    }		
    public List<Employee> getEmployees(){
	return employees;
    }
    public List<Group> getGroups(){
	if(groups == null && !department_id.isEmpty()){
	    GroupList tl = new GroupList();
	    tl.setDepartment_id(department_id);
	    tl.setActiveOnly();
	    String back = tl.find();
	    if(back.isEmpty()){
		List<Group> ones = tl.getGroups();
		if(ones != null && ones.size() > 0){
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
    public void setExclude_name(String val){
	if(val != null)
	    exclude_name = val;
    }
    public void setIncludeFuture(){
	include_future = true; // no need already taken care of
    }				
    public String find(){
	//
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "select e.id,e.username,e.first_name,e.last_name,e.id_code,e.employee_number,e.ad_sid,e.email,e.roles,date_format(e.added_date,'%m/%d/%Y'),e.inactive from employees e ";				
	String qw = "";
	boolean job_table = false;
	boolean group_table = false;
	if(!id.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " e.id = ? ";
	}
	else if(!id_code.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " e.id_code = ? ";
	}
	else if(!employee_number.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " e.employee_number = ? ";
	}
	else if(!ad_sid.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " e.ad_sid = ? ";
	}				
	else if(!employee_ids.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " e.id  in ("+employee_ids+") ";
	}
	else{
	    if(!name.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";
		qw += " (concat(e.first_name,' ',e.last_name) like ? or concat(e.last_name,' ',e.first_name) like ? or e.username like ?)";
	    }
	    else if(!exclude_name.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";								
		qw += " not (e.last_name like ? or e.first_name like ?)";
	    }
	    if(!username.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";
		qw += " e.username like ? ";
	    }
	    if(!department_id.isEmpty()){
		qq += " join department_employees de on de.employee_id=e.id "; 
		qq += " join jobs j on j.employee_id=e.id ";
		qq += " join `groups` g on g.id=j.group_id ";
		job_table = true;
		group_table = true;
		//
		// city directors = 18, human resource = 4
		//
		if(includeAllDirectors){
		    if(!qw.isEmpty()) qw += " and ";
		    qw += " (de.department_id in(?,4) or de.department2_id=18) and de.employee_id=e.id ";// all city directors dept=18
		}
		else{
		    if(!qw.isEmpty()) qw += " and ";
		    qw += " de.department_id=? ";
		}
		if(!pay_period_id.isEmpty()){
		    if(!qw.isEmpty()) qw += " and ";											
		    // qw += " ((de.effective_date <= pd.start_date ";
		    // qw += " and de.expire_date is null or de.expire_date >= pd.end_date ) ";
		    qw += " j.effective_date <= pd.start_date and ";										
		    qw += " (j.expire_date is null or j.expire_date >= pd.end_date)";
		}
	    }
	    if(!dept_ref_id.isEmpty()){
		if(department_id.isEmpty())
		    qq += " join department_employees de on de.employee_id=e.id ";
		qq += " join departments dd on de.department_id=dd.id or de.department2_id=dd.id ";
		if(!qw.isEmpty()) qw += " and ";
		qw += " dd.ref_id in ("+dept_ref_id+") ";
	    }
	    if(hasEmployeeNumber){ // related to previous one
		if(!qw.isEmpty()) qw += " and ";								
		qw += " e.employee_number is not null";
	    }
	    else if(hasNoEmployeeNumber){
		if(!qw.isEmpty()) qw += " and ";								
		qw += " e.employee_number is null";
	    }
	    if(hasAdSid){ 
		if(!qw.isEmpty()) qw += " and ";								
		qw += " e.ad_sid is not null";
	    }						
	    if(!group_ids.isEmpty()){
		if(!job_table){
		    qq += " join jobs j on j.employee_id=e.id ";
		    job_table = true;
		}
		if(!qw.isEmpty()) qw += " and ";
		qw += " j.group_id in ("+group_ids+") ";
		if(!pay_period_id.isEmpty() ||
		   !no_document_for_payperiod_id.isEmpty()){
		    qw += " and j.effective_date <= pd.start_date ";
		    qw += " and (j.expire_date is null or j.expire_date >= pd.end_date )";
		}
	    }
	    if(!exclude_group_id.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";
		if(!job_table){
		    qq += " join jobs j on j.employee_id=e.id ";
		    job_table = true;
		}
		qw += " j.group_id <> ? and j.expire_date is null";
	    }
	    if(!no_document_for_payperiod_id.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";
		qw += " e.id not in (select td.employee_id from time_documents td where td.pay_period_id = pd.id)";						
	    }
	    if(!groupManager_id.isEmpty()){
		qq += " join group_managers gm on gm.employee_id=e.id ";
		if(!qw.isEmpty()) qw += " and ";
		qw += " gm.group_id = ? ";
	    }
	    if(used_time_track){
		if(!qw.isEmpty()) qw += " and ";								
		qw += " e.id in (select employee_id from time_documents where initiated > (NOW() - INTERVAL 28 DAY)) ";
	    }
	    if(exclude_recent_records){
		if(!qw.isEmpty()) qw += " and ";								
		qw += " e.added_date < (NOW() - INTERVAL 30 DAY) ";
	    }
	    else if(recent_records_only){
		if(!qw.isEmpty()) qw += " and ";								
		qw += " e.added_date >= (NOW() - INTERVAL 30 DAY) ";
	    }
	    if(active_only){
		if(!qw.isEmpty()) qw += " and ";
		qw += " e.inactive is null";
	    }
	    else if(inactive_only){
		if(!qw.isEmpty()) qw += " and ";
		qw += " e.inactive is not null";
	    }
	    if(!no_document_for_payperiod_id.isEmpty() || 
	       (!pay_period_id.isEmpty() && (!department_id.isEmpty() ||
					     !group_id.isEmpty()))){
		qq += ", pay_periods pd ";
		if(!qw.isEmpty()) qw += " and ";								
		qw += " pd.id=? ";
	    }
	    if(not_terminated){
		if(!job_table){
		    qq += " join jobs j on j.employee_id=e.id ";
		    job_table = true;
		}
		if(!qw.isEmpty()) qw += " and ";
		qw += " j.expire_date is null ";
	    }						
	}
	if(!qw.isEmpty())
	    qq += " where "+qw;
	qq += " order by e.last_name,e.first_name ";
	String back = "";
	logger.debug(qq);
	// System.err.println(qq);
	con = UnoConnect.getConnection();
	if(con == null){
	    back = "Could not connect to DB ";
	    return back;
	}
	try{
	    pstmt = con.prepareStatement(qq);
	    int jj = 1;
	    if(!id.isEmpty()){
		pstmt.setString(jj++,id);
	    }
	    else if(!id_code.isEmpty()){
		pstmt.setString(jj++, id_code);
	    }
	    else if(!employee_number.isEmpty()){
		pstmt.setString(jj++, employee_number);
	    }
	    else if(!ad_sid.isEmpty()){
		pstmt.setString(jj++, ad_sid);
	    }						
	    else if(!employee_ids.isEmpty()){
		// nothing here
	    }						
	    else{
		if(!name.isEmpty()){
		    pstmt.setString(jj++,name+"%");
		    pstmt.setString(jj++,name+"%");
		    pstmt.setString(jj++,name+"%");
		}
		else if(!exclude_name.isEmpty()){
		    pstmt.setString(jj++,exclude_name);
		    pstmt.setString(jj++,exclude_name);	
		}
		if(!username.isEmpty()){ // for auto_complete 
		    pstmt.setString(jj++,username+"%");
		}
		if(!department_id.isEmpty()){
		    pstmt.setString(jj++, department_id);
		}
		if(!exclude_group_id.isEmpty()){
		    pstmt.setString(jj++, exclude_group_id);
		}
		if(!no_document_for_payperiod_id.isEmpty()){
		    // pstmt.setString(jj++, no_document_for_payperiod_id);
		}
		if(!groupManager_id.isEmpty()){
		    pstmt.setString(jj++, groupManager_id);
		}
		if(!no_document_for_payperiod_id.isEmpty()){
		    pstmt.setString(jj++, no_document_for_payperiod_id);
		}
		else if(!pay_period_id.isEmpty() &&
			(!department_id.isEmpty() ||
			 !group_id.isEmpty())){
		    pstmt.setString(jj++, pay_period_id);
		}
								
	    }
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		if(employees == null)
		    employees = new ArrayList<>();
		Employee employee =
		    new Employee(rs.getString(1),
				 rs.getString(2),
				 rs.getString(3),
				 rs.getString(4),
				 rs.getString(5),
				 rs.getString(6),
				 rs.getString(7),
				 rs.getString(8),
				 rs.getString(9),
				 rs.getString(10),
				 rs.getString(11) != null
				 );
		if(!employees.contains(employee))
		    employees.add(employee);
	    }
	}
	catch(Exception ex){
	    back += ex;
	    logger.error(ex+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(pstmt, rs);
	    UnoConnect.databaseDisconnect(con);
	}
	return back;
    }
    /*


      select distinct e.id,e.username,e.first_name,e.last_name,e.id_code,e.employee_number,e.ad_sid,e.email,e.roles,date_format(e.added_date,'%m/%d/%Y'),e.inactive                     from employees e                                                                join department_employees de on de.employee_id=e.id                             left join jobs j on j.employee_id=e.id                                          left join `groups` g on g.id=j.group_id,                                          pay_periods pd                                                                  where  pd.id=631 and (de.department_id = 3 or de.department2_id=3 or g.department_id=3) and  ((de.effective_date <= pd.start_date  and de.expire_date is null or de.expire_date >= pd.end_date )  or (j.effective_date <= pd.start_date and  j.expire_date is null or j.expire_date >= pd.end_date)) and  e.employee_number is not null order by e.last_name,e.first_name

    */

				
}






















































