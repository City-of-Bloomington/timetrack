package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.sql.*;
import javax.sql.*;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Group implements Serializable{

    static Logger logger = LogManager.getLogger(Group.class);
    static final long serialVersionUID = 1500L;
    String id="", name="", description="", inactive="",
	allow_pending_accrual="";
    String department_id="";
    //
    // moved from job class
    String include_in_auto_batch="";
    String clock_time_required="";
    //
    // default_earn_code_id is needed when 
    // employees work more than weekly or daily hours
    //
    // the following changed from excess_hours_calculation_method
    String excess_hours_earn_type = "Earn Time";
    Department department = null;
    HourCode defaultEarnCode = null;
    // List<GroupEmployee> groupEmployees = null;
    List<Employee> employees = null;
    List<JobTask> jobs = null;
    List<JobTask> activeJobs = null;
    List<GroupLocation> groupLocations = null;
    List<GroupShift> groupShifts = null;
    Shift shift = null;
    Set<String> ipSet = null;
    Location location = null;
		
    public Group(){
    }		
    public Group(String val){
	setId(val);
    }
    public Group(String val,
		 String val2
		 ){
	setId(val);
	setName(val2);
    }		

    public Group(String val,
		 String val2,
		 String val3,
		 String val4,
		 String val5,
		 boolean val6,
		 boolean val7,
		 boolean val8,
		 boolean val9
		 ){
	setId(val);
	setName(val2);
	setDescription(val3);
	setDepartment_id(val4);
	setExcessHoursEarnType(val5);
	setAllowPendingAccrual(val6);
	setClock_time_required(val7);
	setIncludeInAutoBatch(val8);
	setInactive(val9);				
    }


    public Group(String val, // id
		 String val2, // name
		 String val3, // descrp
		 String val4, // dept_id
		 String val5, // excess_hours_earn_type
		 boolean val6, // allow pending accrual
		 boolean val7,
		 boolean val8,
		 boolean val9, // inactive
		 
		 // dept
		 // String val4, // dept_id use val4
		 String val10, // dept_name
		 String val11, // dept_desc
		 String val12, // ref_id
		 String val13, // ldap_name
		 boolean val14,  // allow pending accrual
		 boolean val15 // inactive

		 ){
	setId(val);
	setName(val2);
	setDescription(val3);
	setDepartment_id(val4);
	setExcessHoursEarnType(val5);
	setAllowPendingAccrual(val6);
	setClock_time_required(val7);
	setIncludeInAutoBatch(val8);
	setInactive(val9);
	// dept obj
	department = new Department(val4, val10, val11, val12, val13, val14,val15);
    }		
    //
    // getters
    //
    public String getId(){
	return id;
    }
    public String getName(){
	return name;
    }
    public String getDescription(){
	return description;
    }		
    public boolean getInactive(){
	return !inactive.isEmpty();
    }
    public boolean getAllowPendingAccrual(){
	return !allow_pending_accrual.isEmpty();
    }		
    public boolean isInactive(){
	return !inactive.isEmpty();
    }
    public boolean isActive(){
	return inactive.isEmpty();
    }
    public boolean isPendingAccrualAllowed(){
	return !allow_pending_accrual.isEmpty();
    }		
    public String getDepartment_id(){
	return department_id;
    }
    public String getExcessHoursEarnType(){
	return excess_hours_earn_type;
    }

    public boolean getClock_time_required(){
	return !clock_time_required.isEmpty();
    }
    public boolean getIncludeInAutoBatch(){
	return !include_in_auto_batch.isEmpty();
    }    
    public boolean isPunchClockOnly(){
	return !clock_time_required.isEmpty();
    }    
    //
    // setters
    //
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setName(String val){
	if(val != null)
	    name = val.trim();
    }
    public void setDescription(String val){
	if(val != null){
	    description = val.trim();
	}
    }		
    public void setInactive(boolean val){
	if(val)
	    inactive = "y";
    }
    public void setAllowPendingAccrual(boolean val){
	if(val)
	    allow_pending_accrual = "y";
    }

    public void setClock_time_required(boolean val){
	if(val)
	    clock_time_required = "y";
    }
    public void setIncludeInAutoBatch(boolean val){
	if(val)
	    include_in_auto_batch = "y";
    }    
    
    public void setDepartment_id (String val){
	if(val != null && !val.equals("-1")){
	    department_id = val;
	    Department one = new Department(department_id);
	    String back = one.doSelect();
	    if(back.isEmpty()){
		department = one;
	    }
	}
    }

    public void setExcessHoursEarnType(String val){
	if(val != null)
	    excess_hours_earn_type = val;
    }
    public boolean equals(Object o) {
	if (o instanceof Group) {
	    Group c = (Group) o;
	    if ( this.id.equals(c.getId())) 
		return true;
	}
	return false;
    }
    public int hashCode(){
	int seed = 37;
	if(!id.isEmpty()){
	    try{
		seed += Integer.parseInt(id)*31;
	    }catch(Exception ex){
		// we ignore
	    }
	}
	return seed;
    }
    public String toString(){
	return name;
    }
		
    public List<Employee> getEmployees(){
	if(!id.isEmpty() && employees == null){
	    EmployeeList ul = new EmployeeList();
	    ul.setGroup_id(id);
	    String back = ul.find();
	    if(back.isEmpty()){
		List<Employee> ones = ul.getEmployees();
		if(ones.size() > 0){
		    employees = ones;
		}
	    }
	}
	return employees;
    }
    public boolean hasEmployees(){
	getEmployees();
	return employees != null && employees.size() > 0;
    }
    public boolean hasJobs(){
	getJobs();
	return jobs != null && jobs.size() > 0;
    }
    public boolean hasActiveJobs(){
	getActiveJobs();
	return activeJobs != null && activeJobs.size() > 0;
    }
    public List<JobTask> getActiveJobs(){
	if(!id.isEmpty() && activeJobs == null){
	    JobTaskList jl = new JobTaskList();
	    jl.setGroup_id(id);
	    jl.setNotExpired();
	    String back = jl.find();
	    if(back.isEmpty()){
		List<JobTask> ones = jl.getJobs();
		if(ones != null && ones.size() > 0){
		    activeJobs = ones;
		}
	    }
	}
	return activeJobs;
    }    
    public List<JobTask> getJobs(){
	if(!id.isEmpty() && jobs == null){
	    JobTaskList jl = new JobTaskList();
	    jl.setGroup_id(id);
	    jl.setOrderByExpireDate();
	    String back = jl.find();
	    if(back.isEmpty()){
		List<JobTask> ones = jl.getJobs();
		if(ones != null && ones.size() > 0){
		    jobs = ones;
		}
	    }
	}
	return jobs;
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
    public Location getLocation(){
	return location;
    }
    public boolean hasLocation(){
	return location != null;
    }
    public boolean hasGroupShifts(){
	if(groupShifts == null){
	    findShifts();
	}
	return groupShifts != null && groupShifts.size() > 0;
    }
    public List<GroupShift> getGroupShifts(){
	return groupShifts;
    }
    public boolean hasNoShift(){
	return !hasShift();
    }
    public boolean hasShift(){
	if(shift == null)
	    findShifts();
	return shift != null;
    }
    public Shift getShift(){
	return shift;
    }
    void findShifts(){
	GroupShiftList del = new GroupShiftList();
	del.setGroup_id(id);
	del.setCurrentOnly();
	del.setActiveOnly();
	String back = del.find();
	if(back.isEmpty()){
	    List<GroupShift> ones = del.getGroupShifts();
	    if(ones != null && ones.size() > 0){
		groupShifts = ones;
	    }
	    if(groupShifts != null && groupShifts.size() > 0){
		shift = groupShifts.get(0).getShift();
	    }
	}
    }

    public List<GroupLocation> getGroupLocations() {
	if (!id.isEmpty() && groupLocations == null) {
	    GroupLocationList ul = new GroupLocationList(id);
	    String back = ul.find();
	    if (back.isEmpty()) {
		List<GroupLocation> ones = ul.getGroupLocations();
		if (ones.size() > 0) {
		    groupLocations = ones;
		    ipSet = ul.getIpSet();
		}
	    }
	}
	return groupLocations;
    }

    public boolean hasGroupLocations() {
	getGroupLocations();
	return groupLocations != null && groupLocations.size() > 0;
    }
    /**
     * if employee have group locations we check that
     * if no group locations assigned yet, then return true
     */
    public boolean ipSetIncludes(String ipAddress) {
	if(hasGroupLocations()){
	    return !ipSet.isEmpty() && ipSet.contains(ipAddress);
	}
	return true; // when no group locations set yet;
    }
		
    public String doSelect(){
	//
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = "select g.id,g.name,g.description,g.department_id,"+
	    "g.excess_hours_earn_type,"+ // renamed
	    "g.allow_pending_accrual,"+
	    "g.clock_time_required,"+
	    "g.include_in_auto_batch,"+
	    "g.inactive,"+
	    "d.name,d.description,d.ref_id,d.ldap_name,"+
	    "d.allow_pending_accrual,d.inactive, "+
	    "l.id,l.ip_address,l.name,l.street_address,l.latitude,l.longitude,l.radius "+
	    "from `groups` g left join departments d on d.id=g.department_id "+
	    "left join group_locations gl on gl.group_id=g.id "+
	    "left join locations l on l.id = gl.location_id "+
	    "where g.id =? ";
	logger.debug(qq);
	con = UnoConnect.getConnection();				
	try{
	    if(con != null){
		pstmt = con.prepareStatement(qq);
		pstmt.setString(1, id);
		rs = pstmt.executeQuery();
		if(rs.next()){
		    setName(rs.getString(2));
		    setDescription(rs.getString(3));
		    setDepartment_id(rs.getString(4));
		    setExcessHoursEarnType(rs.getString(5));
		    setAllowPendingAccrual(rs.getString(6) != null);
		    setClock_time_required(rs.getString(7) != null);
		    setIncludeInAutoBatch(rs.getString(8) != null);
		    setInactive(rs.getString(9) != null);
department = new Department(department_id,
						rs.getString(10),
						rs.getString(11),
						rs.getString(12),
						rs.getString(13),
						rs.getString(14) != null,
						rs.getString(15) != null
						);
		    str = rs.getString(16); // location id
		    if(str != null){
			location = new Location(rs.getString(16),
						rs.getString(17),
						rs.getString(18),
						rs.getString(19),
						rs.getDouble(20),
						rs.getDouble(21),
						rs.getDouble(22));
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
	    UnoConnect.databaseDisconnect(con);
	}
	return msg;
    }

    public String doSave(){
	//
	Connection con = null;
	PreparedStatement pstmt = null, pstmt2=null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = "insert into `groups` values(0,?,?,?,?,?,?,?,null) ";
	if(name.isEmpty()){
	    msg = " name not set ";
	    return msg;
	}
	if(department_id.isEmpty()){
	    msg = " department not set ";
	    return msg;
	}				
	logger.debug(qq);
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = "Could not connect to DB ";
	    return msg;
	}				
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, name);
	    if(description.isEmpty())
		pstmt.setNull(2, Types.VARCHAR);
	    else
		pstmt.setString(2, description);
	    pstmt.setString(3, department_id);
	    if(excess_hours_earn_type.isEmpty())
		pstmt.setNull(4, Types.INTEGER);
	    else
		pstmt.setString(4, excess_hours_earn_type);
	    
	    if(allow_pending_accrual.isEmpty()){
		pstmt.setNull(5, Types.CHAR);
	    }
	    else{
		pstmt.setString(5,"y");
	    }
	    if(clock_time_required.isEmpty()){
		pstmt.setNull(6, Types.CHAR);
	    }
	    else{
		pstmt.setString(6,"y");
	    }
	    if(include_in_auto_batch.isEmpty()){
		pstmt.setNull(7, Types.CHAR);
	    }
	    else{
		pstmt.setString(7,"y");
	    }
	    pstmt.executeUpdate();
	    //
	    qq = "select LAST_INSERT_ID()";
	    pstmt2 = con.prepareStatement(qq);
	    rs = pstmt2.executeQuery();
	    if(rs.next()){
		id = rs.getString(1);
	    }
	}
	catch(Exception ex){
	    msg += " "+ex;
	    logger.error(msg+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(rs, pstmt, pstmt2);
	    UnoConnect.databaseDisconnect(con);
	}
	if(msg.isEmpty()){
	    doSelect();
	}
	return msg;
    }

    public String doUpdate(){
	//
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", str="";
	if(name.isEmpty()){
	    return " name not set ";
	}
	String qq = "update `groups` set name=?,description=?,department_id=?,"+
	    "excess_hours_earn_type=?,"+ // renamed
	    "allow_pending_accrual=?,"+
	    "clock_time_required=?,"+
	    "include_in_auto_batch=?,"+
	    "inactive=? where id=? ";
	logger.debug(qq);
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = "Could not connect to DB ";
	    return msg;
	}			
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, name);
	    if(description.isEmpty())
		pstmt.setNull(2, Types.VARCHAR);
	    else
		pstmt.setString(2, description);								
	    pstmt.setString(3, department_id);
	    if(excess_hours_earn_type.isEmpty())
		pstmt.setNull(4, Types.INTEGER);
	    else
		pstmt.setString(4, excess_hours_earn_type);
	    if(allow_pending_accrual.isEmpty()){
		pstmt.setNull(5, Types.CHAR);
	    }
	    else{
		pstmt.setString(5,"y");
	    }
	    if(clock_time_required.isEmpty()){
		pstmt.setNull(6, Types.CHAR);
	    }
	    else{
		pstmt.setString(6,"y");
	    }
	    if(include_in_auto_batch.isEmpty()){
		pstmt.setNull(7, Types.CHAR);
	    }
	    else{
		pstmt.setString(7,"y");
	    }	    	    
	    if(inactive.isEmpty()){
		pstmt.setNull(8, Types.CHAR);
	    }
	    else{
		pstmt.setString(8,"y");
	    }
	    pstmt.setString(9, id);
	    pstmt.executeUpdate();
	}
	catch(Exception ex){
	    msg += " "+ex;
	    logger.error(msg+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(pstmt, rs);
	    UnoConnect.databaseDisconnect(con);
	}
	if(msg.isEmpty()){
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
	String qq = "delete `groups` where id=? ";
	logger.debug(qq);
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = "Could not connect to DB ";
	    return msg;
	}							
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, id);
	    pstmt.executeUpdate();
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
     *
     // 1 - stop timetrack on timetrack server
     // 2 - backup database
     // 3 - copy war file to war.old
     // 4 - update database
     // add to groups first
     alter table groups add clock_time_required char(1) after allow_pending_accrual;
     alter table groups add include_in_auto_batch char(1) after clock_time_required;     
     //
     // find all jobs that have these flags on
     update groups g set g.clock_time_required='y' where g.id in (select distinct j.group_id from jobs j where j.clock_time_required is not null and j.expire_date is not null);
     //
     update groups g set g.include_in_auto_batch='y' where g.id in (select distinct j.group_id from jobs j where j.include_in_auto_batch is not null and j.expire_date is not null);
     //
     //
     alter table jobs drop column clock_time_required;
     alter table jobs drop column include_in_auto_batch;
     //
     //
     drop table group_employees;
     //
     check groups for clock_time_required flag for parks groups
     //
     //
     

     
     */

    
}
