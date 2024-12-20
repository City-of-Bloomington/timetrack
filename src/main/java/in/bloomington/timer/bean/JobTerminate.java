package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JobTerminate{

    static final long serialVersionUID = 3700L;	
    static Logger logger = LogManager.getLogger(JobTerminate.class);
    static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    static SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
    String id="", terminate_id="", job_id="";
    String job_grade="", job_step="", supervisor_id="", supervisor_phone="";
    String start_date="", last_day_of_work="", weekly_hours="";
    
    String badge_code="", badge_returned="No"; //NA, Yes, No
    String pay_rate = "", nw_job_title="";
    //
    // derived from join
    //
    String supervisor_name="", job_title="",group_id="",group_name = "",
	employee_name="";
    JobTask job = null;
    Group group = null;
    Department department = null;
    Employee emp = null, supervisor = null;
    Profile profile = null;
    List<BenefitGroup> benefitGroups = null;
    BenefitGroup benefitGroup = null;
    //
    public JobTerminate(){
	
    }
    public JobTerminate(String val){
	//
	setId(val);
    }
    public JobTerminate(String val,
			String val2,
			String val3,
			String val4,
			String val5,
			String val6){
	setNwJobTitle(val);	
	setJob_grade(val2);
	setJob_step(val3);
	setPayRate(val4);
	setStart_date(val5);
	setWeeklyHours(val6);

    }
    public JobTerminate(
			String str, String str2, String str3,
			String str4, String str5, String str6,
			
			String str7, String str8, String str9,
			String str10, String str11, String str12,

			String str13, String str14, String str15,
			String str16, String str17, String str18,
			String str19
			){
	setVals(str, str2, str3, str4, str5,
		str6, str7, str8, str9, str10,
		str11, str12,
		str13, str14, str15, str16, str17,str18,
		str19
		);
	
    }
    
    void setVals(
		 String str, String str2, String str3,
		 String str4, String str5, String str6,
		
		 String str7, String str8, String str9,
		 String str10, String str11, String str12,
		 String str13, String str14, String str15,
		 String str16, String str17, String str18,
		 String str19
		 ){
	setId(str);
	setTerminate_id(str2);
	setJob_id(str3);
	setJob_grade(str4);
	setJob_step(str5);
	
	setPayRate(str6);
	setWeeklyHours(str7);
	setSupervisor_id(str8);
	setSupervisor_phone(str9);
	setStart_date(str10);
	
	setLast_day_of_work(str11);
	setBadge_code(str12);
	setBadge_returned(str13);
	setNwJobTitle(str14);
	setEmployee_name(str15);
	
	setSupervisor_name(str16);
	setGroup_id(str17);
	setGroup_name(str18);
	setJob_title(str19);

    }
    public boolean equals(Object obj){
	if(obj instanceof EmpTerminate){
	    EmpTerminate one =(EmpTerminate)obj;
	    return id.equals(one.getId());
	}
	return false;				
    }
    public int hashCode(){
	int seed = 29;
	if(!id.equals("")){
	    try{
		seed += Integer.parseInt(id);
	    }catch(Exception ex){
	    }
	}
	return seed;
    }
    //
    // getters
    //
    public String getId(){
	return id;
    }
    public String getJob_id(){ // adding one job a time
	return job_id;
    }
    public String getTerminate_id(){
	return terminate_id;
    }
    public String getJob_grade(){ 
	return job_grade;
    }
    public String getPayRate(){
	if(pay_rate.isEmpty()){
	    // findProfile();
	    // if(profile != null){
	    // pay_rate = ""+profile.getHourlyRate();
	    // }
	}
	return pay_rate;
    }
    public void setPayRate(String val){
	if(val != null && !val.isEmpty()){
	    pay_rate = val;
	}
    }
    public String getJob_step(){ // adding one job a time
	return job_step;
    }
    public String getSupervisor_id(){
	if(supervisor_id.isEmpty() && supervisor != null){
	    supervisor_id = supervisor.getId();
	}
	return supervisor_id;
    }
    public String getSupervisor_phone(){
	return supervisor_phone;
    }
    public String getSupervisor_name(){
	return supervisor_name;
    }
    public String getGroup_id(){ 
	return group_id;
    }
    public String getGroup_name(){ 
	return group_name;
    }
    public String getEmployee_name(){ 
	return employee_name;
    }
    public String getNwJobTitle(){
	return nw_job_title;
    }
    public String getJobTitleAny(){
	String ret = "";
	if(!nw_job_title.isEmpty()){
	    ret = nw_job_title;
	}
	else{
	    ret = job_title;
	}
	return ret;
    }
    public boolean hasSupervisor(){
	getSupervisor();
	return supervisor != null;
    }
    public Employee getSupervisor(){
	if(supervisor == null && !supervisor_id.isEmpty()){
	    Employee one = new Employee(supervisor_id);
	    String back = one.doSelect();
	    if(back.isEmpty()){
		supervisor = one;
	    }
	}
	return supervisor;
    }
    
    public Employee getEmp(){
	if(emp == null){
	    if(job == null && !job_id.isEmpty()){
		JobTask job = new JobTask(job_id);
		String back = job.doSelect();
		emp = job.getEmployee();
	    }
	}
	return emp;				
    }
    public Employee getEmployee(){
	return getEmp();
    }
    public Group getGroup(){
	if(group == null){
	    getJob();
	    if(job != null){
		group = job.getGroup();
	    }
	}
	return group;
    }
    //
    // ITS
    //
    public String getBadge_returned(){
	return badge_returned;
    }
    public String getBadge_code(){
	return badge_code;
    }    
    public boolean hasJobGrade(){
	return !job_grade.isEmpty();
    }
    public boolean hasJobStep(){
	return !job_step.isEmpty();
    }
    public String getWeeklyHours(){
	return weekly_hours;
    }
    public String getStart_date(){
	return start_date;
    }
    public String getLast_day_of_work(){
	return last_day_of_work;
    }
    //
    // setters
    //
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    
    public void setTerminate_id(String val){
	if(val != null)
	    terminate_id = val;
    }    
    public void setJob_id(String val){
	if(val != null && !val.isEmpty()){
	    if(job_id.isEmpty()){
		job_id = val; 
	    }
	}
    }
    public void setStart_date(String val){
	if(val != null)
	    start_date = val;
    }
    public void setWeeklyHours(String val){    
	if(val != null)
	    weekly_hours = val;
    }    
    public void setBadge_code(String val){
	if(val != null)
	    badge_code = val;
    }
    public void setGroup_id(String val){
	if(val != null)
	    group_id = val;
    }
    public void setGroup_name(String val){
	if(val != null)
	    group_name = val;
    }
    public void setEmployee_name(String val){
	if(val != null)
	    employee_name = val;
    }
    public void setSupervisor_name(String val){
	if(val != null)
	    supervisor_name = val;
    }
    public void setJob_title(String val){
	if(val != null)
	    job_title = val;
    }
    public void setNwJobTitle(String val){
	if(val != null)
	    nw_job_title = val;
    }    
    public String toString(){
	return id;
    }
    public boolean hasJob(){
	return !job_id.isEmpty();
    }
    //
    // when dealing with one job termination
    //
    public JobTask getJob(){
	if(!job_id.isEmpty() && job == null){
	    JobTask one = new JobTask(job_id);
	    String back = one.doSelect();
	    if(back.isEmpty()){
		job = one;
		emp = job.getEmployee();
		weekly_hours = ""+job.getWeekly_regular_hours();
	    }
	    else{
		System.err.println(" find job error "+back);
	    }
	}
	return job;
    }
    public String getJob_title(){
	getJob();	
	if(job_title.isEmpty() && job != null){
	    job_title = job.getName();
	}
	return job_title;
    }
    public void setJob_grade(String val){ // adding one job a time
	if(val != null)
	    job_grade=val;
    }
    public void setJob_step(String val){ // adding one job a time
	if(val != null)
	    job_step=val;
    }
    public void setSupervisor_id(String val){
	if(val != null)
	    supervisor_id=val;
    }

    public String findSupervisorInfo(EnvBean bean){
	String back = findSupervisor();
	if(back.isEmpty()){
	    back = findSupervisorPhone(bean);
	}
	return back;
    }
    public String findSupervisor(){
	String back = "";
	if(group_id.isEmpty()){
	    getJob();
	    group_id = job.getGroup_id();
	}
	if(!group_id.isEmpty()){
	    GroupManagerList gml = new GroupManagerList();
	    gml.setGroup_id(group_id);
	    gml.setActiveOnly();
	    gml.setNotExpired();
	    gml.setApproversOnly();
	    back = gml.find();
	    if(back.isEmpty()){
		List<GroupManager> managers = gml.getManagers();
		if(managers != null && managers.size() > 0){
		    // we pick the first (primary)
		    supervisor = managers.get(0).getEmployee();
		    supervisor_id = supervisor.getId();
		    System.err.println(" super "+supervisor);
		}
	    }
	}
	else{
	    back = "No group id set ";
	    logger.error("No group id set ");
	}
	return back;
    }    

    public String findSupervisorPhone(EnvBean bean){
	//
	String back = "";
	if(supervisor != null && bean != null){
	    System.err.println(" super and bean set");
	    EmpList empl = new EmpList(bean, supervisor.getUsername());
	    back = empl.find();
	    if(back.isEmpty()){
		List<Employee> emps = empl.getEmps();
		if(emps != null && emps.size() > 0){
		    supervisor_phone = emps.get(0).getPhone();
		    System.err.println(" sup phone "+supervisor_phone);
		}
	    }
	}
	return back;
    }
    public void setSupervisor(Employee val){
	if(val != null){
	    supervisor =val;
	    supervisor_id = val.getId();
	}
    }
    public void setSupervisor_phone(String val){
	if(val != null)
	    supervisor_phone=val;
    }
    public void setLast_day_of_work(String val){
	if(val != null)
	    if(val.indexOf("-") > 0){
		last_day_of_work = Helper.changeDateFormat(val);
	    }
	    else{
		last_day_of_work=val;
	    }
    }

    public void setBadge_returned(String val){
	if(val != null && !val.equals("-1")){
	    badge_returned = val;
	}
    }

    String findProfile(){
	String back = "";

	if(benefitGroups == null){
	    findBenefitGroups();
	}
	if(emp == null) getEmployee();
	if(emp != null && benefitGroups != null){
	    ProfileList pl = new ProfileList(Helper.getToday(),
					     benefitGroups,
					     emp.getEmployee_number());
	    back = pl.findOne();
	    if(back.isEmpty()){
		List<Profile> ones = pl.getProfiles();
		if(ones != null && ones.size() > 0){
		    profile = ones.get(0);
		}
	    }
	}
	else{
	    back = "employee obj not found or benefit groups are missing ";
	}
	return back;
	
    }

    void findBenefitGroups(){
	if(benefitGroups == null){
	    BenefitGroupList tl = new BenefitGroupList();
	    String back = tl.find();
	    if(back.isEmpty()){
		List<BenefitGroup> ones = tl.getBenefitGroups();
		if(ones != null && ones.size() > 0){
		    benefitGroups = ones;
		}
	    }
	}
    }
    public String expireJob(String last_pay_period_date){
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;	
	if(job_id.isEmpty()){
	    back = "No job found ";
	    return back;
	}
	if(last_pay_period_date.isEmpty()){
	    back = "Last Pay Period Date not set";
	    return back;
	}
	String qq = "update jobs set expire_date=? where id=? ";
	con = UnoConnect.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    return back;
	}	
	logger.debug(qq);				
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setDate(1, new java.sql.Date(dateFormat.parse(last_pay_period_date).getTime()));		    
	    pstmt.setString(2, job_id);
	    pstmt.executeUpdate();
	}
	catch(Exception ex){
	    back += ex+":"+qq;
	    logger.error(back);
	}
	finally{
	    Helper.databaseDisconnect(rs, pstmt);
	    UnoConnect.databaseDisconnect(con);
	}	
	return back;	
    }
    public String doSave(){
	
	String back="";
	Connection con = null;
	PreparedStatement pstmt = null, pstmt2=null;
	ResultSet rs = null;	
	String qq = "insert into job_terminations values(0,?,?,?,?, ?,?,?,?,?,"+
	    "?,?,?,?)";
	con = UnoConnect.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    return back;
	}
	if(job_id.isEmpty()){
	    back = "job not set ";
	    return back;
	}
	if(supervisor_id.isEmpty()){
	    back = "supervisor not set ";
	    return back;
	}
	logger.debug(qq);				
	try{
	    pstmt = con.prepareStatement(qq);
	    back = setParams(pstmt);
	    if(back.isEmpty()){
		pstmt.executeUpdate();
		qq = "select LAST_INSERT_ID()";
		pstmt2 = con.prepareStatement(qq);
		rs = pstmt2.executeQuery();
		if(rs.next()){
		    id = rs.getString(1);
		}
	    }
	}
	catch(Exception ex){
	    back += ex+":"+qq;
	    logger.error(back);
	}
	finally{
	    Helper.databaseDisconnect(rs, pstmt, pstmt2);
	    UnoConnect.databaseDisconnect(con);
	}	
	return back;
    }
    String setParams(PreparedStatement pstmt){
	String back = "";
	try{
	    if(terminate_id.isEmpty()){
		pstmt.setNull(1, Types.INTEGER);
	    }
	    else{
		pstmt.setString(1, terminate_id);
	    }
	    pstmt.setString(2, job_id);
	    if(job_grade.isEmpty())
		pstmt.setNull(3, Types.VARCHAR);
	    else
		pstmt.setString(3, job_grade);
	    if(job_step.isEmpty())
		pstmt.setNull(4, Types.VARCHAR);
	    else	    
		pstmt.setString(4, job_step);
	    if(pay_rate.isEmpty()){
		pay_rate = "0";
	    }
	    pstmt.setString(5, pay_rate);
	    pstmt.setString(6, weekly_hours);	    
	    pstmt.setString(7, supervisor_id);
	    if(supervisor_phone.isEmpty())
		pstmt.setNull(8, Types.VARCHAR);
	    else
		pstmt.setString(8, supervisor_phone);
	    if(start_date.isEmpty())
		pstmt.setNull(9, Types.DATE);
	    else{
		if(start_date.indexOf("-") == -1){
		    pstmt.setDate(9, new java.sql.Date(dateFormat.parse(start_date).getTime()));
		}
		else{
		    pstmt.setDate(9, new java.sql.Date(dateFormat2.parse(start_date).getTime()));
		}
	    }
	    if(last_day_of_work.isEmpty())
		pstmt.setNull(10, Types.DATE);
	    else{
		pstmt.setDate(10, new java.sql.Date(dateFormat.parse(last_day_of_work).getTime()));	
	    }
	    if(badge_code.isEmpty())
		pstmt.setNull(11, Types.VARCHAR);
	    else
		pstmt.setString(11, badge_code);
	    if(badge_returned.isEmpty())
		pstmt.setNull(12, Types.VARCHAR);
	    else
		pstmt.setString(12, badge_returned);
	    if(nw_job_title.isEmpty())
		pstmt.setNull(13, Types.VARCHAR);
	    else
		pstmt.setString(13, nw_job_title);	    
	}catch(Exception ex){
	    back += ex;
	}
	return back;
    }
    public String doUpdate(){
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "update job_terminations set "+
	    "terminate_id=?,"+
	    "job_id=?,job_grade=?,"+
	    "job_step=?, pay_rate=?, weekly_hours=?,supervisor_id=?, "+
	    "supervisor_phone=?, start_date=?,"+
	    "last_day_of_work=?,"+ // date
	    "badge_code=?,"+
	    "badge_returned=?, "+
	    "nw_job_title=? "+
	    " where id = ? "; 
	String back = "";
	logger.debug(qq);
	con = UnoConnect.getConnection();				
	if(con == null){
	    back = "Could not connect to DB ";
	    return back;
	}
	if(job_id.isEmpty()){
	    back = "job(s) not set ";
	    return back;
	}
	if(supervisor_id.isEmpty()){
	    back = "supervisor not set ";		
	    return back;
	}
	try{
	    pstmt = con.prepareStatement(qq);
	    back = setParams(pstmt);
	    if(back.isEmpty()){
		pstmt.setString(14, id);
		pstmt.executeUpdate();
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
    public String doSelect(){
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "select  "+
	    "t.id,"+
	    "t.terminate_id,"+
	    "t.job_id,"+
	    "t.job_grade,"+
	    "t.job_step, "+
	    
	    "t.pay_rate, "+
	    "t.weekly_hours,"+
	    "t.supervisor_id, "+
	    "t.supervisor_phone,"+
	    "date_format(t.start_date,'%m/%d/%Y'),"+ // date
	    
	    "date_format(t.last_day_of_work,'%m/%d/%Y'),"+ // date
	    " t.badge_code,"+
	    " t.badge_returned, "+
	    " t.nw_job_title,"+
	    " concat_ws(' ',e.first_name,e.last_name) employee_name,"+
	    
	    " concat_ws(' ',e2.first_name,e2.last_name) supervisor_name,"+
	    " j.group_id group_id,"+
	    " g.name group_name,"+
	    " p.name job_title "+
	    
	    " from job_terminations t "+
	    " join jobs j on j.id=t.job_id "+
	    " join `groups` g on j.group_id = g.id "+
	    " join positions p on p.id = j.position_id "+
	    " left join employees e on e.id=j.employee_id "+	    
	    " left join employees e2 on e2.id=t.supervisor_id "+
	    " where t.id =? "; 
	String back = "";
	logger.debug(qq);
	con = UnoConnect.getConnection();				
	if(con == null){
	    back = "Could not connect to DB ";
	    return back;
	}
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,id);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		setVals(rs.getString(1),
			rs.getString(2),
			rs.getString(3),
			rs.getString(4),
			rs.getString(5),
			
			rs.getString(6),
			rs.getString(7),
			rs.getString(8),
			rs.getString(9),
			rs.getString(10),
			
			rs.getString(11),
			rs.getString(12),
			rs.getString(13),
			rs.getString(14),
			rs.getString(15),
			
			rs.getString(16),
			rs.getString(17),
			rs.getString(18),
			rs.getString(19)
			);
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
    /**
    //
    create table job_terminations (
    id int unsigned not null auto_increment,
    terminate_id int unsigned,    
    job_id int unsigned,
    job_grade varchar(80),
    job_step varchar(16),
    
    pay_rate double,
    weekly_hours int,
    supervisor_id int unsigned,
    supervisor_phone varchar(16),
    start_date date,
    
    last_day_of_work date,
    badge_code varchar(8),
    badge_returned varchar(32),
    nw_job_title varchar(80),
    primary key(id),
    foreign key(job_id) references jobs(id),        
    foreign key(supervisor_id) references employees(id),
    foreign key(terminate_id) references emp_terminations(id)  
    )engine=InnoDB;
    
     */

}
