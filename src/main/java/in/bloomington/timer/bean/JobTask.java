package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
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
	position_id_alt="",// alternative position from all list
	added_date="",
	salary_group_id="",
	inactive="",
	include_in_auto_batch="",
	irregular_work_days="",
	effective_date="", expire_date="", primary_flag="";
    private String prev_expire_date = ""; 
    private String jobTitle="", salary_group_name="";
    //
    // for job change
    String new_group_id = "", pay_period_id="";
    String department_id = "";
    //
    String clock_time_required="";
    int weekly_regular_hours=40, comp_time_weekly_hours=40;
    double comp_time_factor=1.0, holiday_comp_factor=1.0;
    double hourly_rate=0;
    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
    SalaryGroup salaryGroup = null;
    Position position = null;
    Employee employee = null;
    Group group = null;
    Shift shift = null;
    List<Group> allGroups = null; // all employee groups
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
		   String val15,
									 
		   boolean val16,
		   boolean val17,
		   boolean val18,
		   String val19,
		   String val20,
									 
		   String val21,
		   String val22,
		   boolean val23,									 
		   String val24
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
		val19,
		val20,
		val21,
		val22,
		val23,
		val24);
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
		   String val15,
									 
		   boolean val16,
		   boolean val17,
		   boolean val18,
									 
		   String val19,
		   String val20,
		   String val21,
		   String val22,

		   boolean val23,
		   String val24,									 
		   String val25
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
		val19,
		val20,
								
		val21,
		val22,
		val23,
		val24);
	setEmployee_number(val25);

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
			 String val15,
												 
			 boolean val16,
			 boolean val17,
			 boolean val18,												 
			 String val19,
			 String val20,
												 
			 String val21,
			 String val22,
			 boolean val23,
			 String val24
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
	setAdded_date(val15);
	setIncludeInAutoBatch(val16);
	setIrregularWorkDays(val17);
	setInactive(val18);
	if(!salary_group_id.isEmpty()){
	    salaryGroup = new SalaryGroup(salary_group_id, val19,val20,val21, val22, val23);
	}
	if(val24 != null && !position_id.isEmpty()){
	    position = new Position(position_id, val24);
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
	if(position_id.isEmpty()){
	    getPosition();
	}
	return position_id;
    }
    public String getEmployee_id(){
	return employee_id;
    }
    public String getGroup_id(){
	if(group_id.isEmpty()){
	    getGroup();
	}
	return group_id;
    }		
    public String getEmployee_number(){
	return employee_number;
    }		
    public String getSalary_group_id(){
	if(salary_group_id.isEmpty()){
	    getSalaryGroup();
	}
	return salary_group_id;
    }
    public String getEffective_date(){
	return effective_date;
    }
    public boolean hasEffective_date(){
	return !effective_date.isEmpty();
    }
    public String getExpire_date(){
	return expire_date;
    }
    public String getAdded_date(){
	return added_date;
    }
    public boolean hasExpireDate(){
	return !expire_date.isEmpty();
    }
    public boolean getPrimary_flag(){
	if(id.isEmpty())
	    return true; // default
	return !primary_flag.isEmpty();
    }
    public boolean isPrimary(){
	return getPrimary_flag();
    }
    public boolean hasNoGroup(){
	return group_id.isEmpty();
    }
    public boolean hasGroup(){
	return !group_id.isEmpty();
    }		
    public boolean getInactive(){
	return !inactive.isEmpty();
    }
    public boolean getClock_time_required(){
	return !clock_time_required.isEmpty();
    }
    public boolean isActive(){
	return inactive.isEmpty();
    }
    public boolean getIncludeInAutoBatch(){
	return !include_in_auto_batch.isEmpty();
    }
    public boolean getIrregularWorkDays(){
	return !irregular_work_days.isEmpty();
    }
    public boolean isIrregularWorkDayEmployee(){
	return !irregular_work_days.isEmpty();
    }		
    public boolean isPunchClockOnly(){
	return !clock_time_required.isEmpty();
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
    public void setPosition_id_alt (String val){
	if(val != null && !val.equals("-1"))
	    position_id_alt = val;
    }		
    public void setEmployee_id(String val){
	if(val != null && !val.equals("-1"))
	    employee_id = val;
    }
    public void setGroup_id(String val){
	if(val != null && !val.equals("-1"))
	    group_id = val;
    }
    public void setDepartment_id(String val){
	if(val != null && !val.equals("-1"))
	    department_id = val;
    }		
    public void setEmployee_number(String val){
	if(val != null)
	    employee_number = val;
    }		
    public void setName(String val){
	// needed for auto_complete
    }		
    public void setSalary_group_id(String val){
	if(val != null && !val.equals("-1"))
	    salary_group_id = val;
    }
    public void setEffective_date(String val){
	if(val != null && !val.equals("-1"))
	    effective_date = val;
    }
    public void setExprire_date(String val){
	if(val != null)
	    expire_date = val;
    }
    public void setPrev_exprire_date(String val){
	if(val != null)
	    prev_expire_date = val;
    }    
    public void setExpire_date(String val){
	if(val != null && !val.equals("-1"))
	    expire_date = val;
    }
    public void setAdded_date(String val){
	if(val != null)
	    added_date = val;
    }
    public void setJobTitle(String val){ // for new employee
	if(val != null)
	    jobTitle = val.trim();
    }
    public void setSalary_group_name(String val){ // for new employee
	if(val != null)
	    salary_group_name = val.trim();
    }		
    public void setInactive(boolean val){
	if(val)
	    inactive = "y";
    }
    public void setIncludeInAutoBatch(boolean val){
	if(val)
	    include_in_auto_batch = "y";
    }
    public void setIrregularWorkDays(boolean val){
	if(val)
	    irregular_work_days = "y";
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
	if(val  != null && !val.isEmpty()){
	    try{
		hourly_rate = Double.parseDouble(val);
	    }catch(Exception ex){}
	}
    }
    public void setNew_group_id(String val){
	if(val != null && !val.equals("-1"))
	    new_group_id = val;
    }
    public void setPay_period_id(String val){
	if(val != null && !val.equals("-1"))
	    pay_period_id = val;
    }
    public String getName(){
	getPosition();
	if(position != null) {
	    return position.getName();
	}
	return "";
    }
    public String toString(){
	String str = getName();
	if(str != null && !str.isEmpty()){
	    return str;
	}
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
	if(!id.isEmpty()){
	    try{
		seed += Integer.parseInt(id)*31;
	    }catch(Exception ex){
		// we ignore
	    }
	}
	return seed;
    }
    public boolean hasShift(){
	if(shift == null)
	    getGroup();
	return shift != null;
    }
    public Shift getShift(){
	return shift;
    }
    public SalaryGroup getSalaryGroup(){
	if(!salary_group_id.isEmpty() && salaryGroup == null){
	    SalaryGroup one = new SalaryGroup(salary_group_id);
	    String back = one.doSelect();
	    if(back.isEmpty()){
		salaryGroup = one;
	    }
	}
	if(salaryGroup == null && !salary_group_name.isEmpty()){
	    SalaryGroupList sgl = new SalaryGroupList(salary_group_name);
	    String back = sgl.find();
	    if(back.isEmpty()){
		List<SalaryGroup> ones = sgl.getSalaryGroups();
		if(ones != null && ones.size() > 0){
		    salaryGroup = ones.get(0);
		    salary_group_id = salaryGroup.getId();
		}
	    }
	}
	return salaryGroup;
    }
    public Group getGroup(){
	if(group == null){
	    if(!group_id.isEmpty()){
		Group one = new Group(group_id);
		String back = one.doSelect();
		if(back.isEmpty()){
		    group = one;
		}
	    }
	    else{
		if(hasOneGroupOnly()){
		    group = allGroups.get(0);
		    group_id= group.getId();
		}
	    }
	    if(group != null && group.hasShift()){
		shift = group.getShift();
	    }
	}
	return group;
    }
    void findAllGroups(){
	if(allGroups == null){
	    if(!employee_id.isEmpty()){
		GroupEmployeeList gel = new GroupEmployeeList();
		gel.setEmployee_id(employee_id);
		gel.setIncludeFuture();
		String back = gel.find();
		if(back.isEmpty()){
		    List<GroupEmployee> ones = gel.getGroupEmployees();
		    if(ones != null){
			for(GroupEmployee one:ones){
			    Group gg = one.getGroup();
			    if(allGroups == null)
				allGroups = new ArrayList<>();
			    allGroups.add(gg);
			}
		    }
		}
	    }
	}
    }
    public boolean hasOneGroupOnly(){
	findAllGroups();
	return allGroups != null && allGroups.size() == 1;
    }
    public boolean hasAllGroups(){
	findAllGroups();
	return allGroups != null && allGroups.size() > 0;
    }
    public List<Group> getAllGroups(){
	return allGroups;
    }
    public Position getPosition(){
	if(!position_id.isEmpty() && position == null){
	    Position one = new Position(position_id);
	    String back = one.doSelect();
	    if(back.isEmpty()){
		position = one;
	    }
	}
	// needed for new job through wizard
	if(position == null && !jobTitle.isEmpty()){
	    PositionList pl = new PositionList(jobTitle);
	    pl.setExactMatch();
	    String back = pl.find();
	    if(back.isEmpty()){
		List<Position> ones = pl.getPositions();
		if(ones != null && ones.size() > 0){
		    position = ones.get(0);
		    position_id = position.getId();
		}
		else{
		    Position pp = new Position(jobTitle, jobTitle, jobTitle);
		    back = pp.doSave();
		    if(back.isEmpty()){
			position = pp;
			position_id = pp.getId();
		    }
		}
	    }
	}
	return position;
    }		
    public Employee getEmployee(){
	if(!employee_id.isEmpty() && employee == null){
	    Employee one = new Employee(employee_id);
	    String back = one.doSelect();
	    if(back.isEmpty()){
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
	//
	if(bGroup.isFireSworn()){
	    new_salary_group_id = "9";
	}
	if(bGroup.isTempWithBen()){
	    new_salary_group_id = "12";
	}
	else if(bGroup.isTemporary()){
	    new_salary_group_id = "3";
	}
	else if(bGroup.isPartTime()){
	    if(bGroup.isExempt()){
		new_salary_group_id = "5";
	    }
	    else{
		new_salary_group_id = "11";
	    }
	}
	else if(bGroup.isPoliceSworn()){
	    new_salary_group_id = "6";
	}
	else if(bGroup.isPoliceDetective()){
	    new_salary_group_id = "7";
	}
	else if(bGroup.isPoliceManagement()){
	    new_salary_group_id = "8";
	}
	else if(bGroup.isFireSworn()){
	    new_salary_group_id = "9";
	}
	else if(bGroup.isFireSworn5to8()){
	    new_salary_group_id = "10";
	}
	else if(bGroup.isNonExempt()){
	    new_salary_group_id = "2";
	}
	else if(bGroup.isExempt()){
	    new_salary_group_id = "1";
	}
	else if(bGroup.isUnioned()){
	    new_salary_group_id = "4";
	}
	if(!new_salary_group_id.isEmpty()){
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
	if(!job_name.isEmpty()){
	    getPosition();
	    boolean pos_update = false;
	    if(position != null){
		String p_name = position.getName();
		if(!job_name.equals(p_name)){
		    pos_update = true;
		}
	    }
	    else{
		pos_update = true;
	    }
	    if(pos_update){
		boolean found = false;
		needUpdate = true;
		PositionList pl = new PositionList(job_name);
		pl.setActiveOnly();
		pl.setExactMatch();
		String back = pl.find();
		if(back.isEmpty()){
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
		    if(back.isEmpty()){
			position = pos;												
			position_id = pos.getId();
		    }
		}
	    }
	}
	if(needUpdate){
	    String back = doPartialUpdate();
	    if(!back.isEmpty())
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
	    "date_format(j.added_date,'%m/%d/%Y'),"+

	    "j.include_in_auto_batch,"+
	    "j.irregular_work_days,"+
	    "j.inactive, "+
	    "sg.name,sg.description,sg.default_regular_id,"+
	    "sg.excess_culculation,sg.inactive, "+
	    "p.name "+
	    " from jobs j "+
	    " join salary_groups sg on sg.id=j.salary_group_id "+
	    " join positions p on j.position_id=p.id "+
	    " where j.id =? ";
	logger.debug(qq);
	con = UnoConnect.getConnection();
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
			rs.getString(15),
			rs.getString(16) != null,
			rs.getString(17) != null,
			rs.getString(18) != null,
												
			rs.getString(19),
			rs.getString(20),
			rs.getString(21),
			rs.getString(22),
												
			rs.getString(23) != null,
			rs.getString(24)
			);
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
    public String doSaveAll(){
	String back = "";
	if(!department_id.isEmpty()){
	    if(effective_date.isEmpty()){
		back = "Need to set effective date";
		return back;
	    }
	    getEmployee();
	    if(employee != null && !employee.hasDepartment(true)){
		DepartmentEmployee de = new DepartmentEmployee(employee_id, department_id, effective_date);
		back = de.doSave();
	    }
	}
	if(!new_group_id.isEmpty() && group_id.isEmpty()){
	    group_id = new_group_id;
	    new_group_id = "";
	}
	if(back.isEmpty() && !group_id.isEmpty()){
	    GroupEmployee ge = new GroupEmployee(group_id, employee_id, effective_date);
	    back = ge.doSave();
	    back = ""; // if already in the group we ignore this message
	}
	if(back.isEmpty()){
	    back = doSave();
	}
	return back;
    }
    public String doSave(){
	//
	Connection con = null;
	PreparedStatement pstmt = null, pstmt2=null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = "insert into jobs values(0,?,?,?,?, ?,?,?,?,?, ?,?,?,?,now(),?,?,null) ";
	if(employee_id.isEmpty()){
	    msg = " employee_id not set ";
	    return msg;
	}
	if(position_id.isEmpty() && !position_id_alt.isEmpty()){
	    position_id = position_id_alt;
	}
	if(position_id.isEmpty() && position_id_alt.isEmpty()){
	    msg = " position not set ";
	    return msg;
	}				
	if(salary_group_id.isEmpty()){
	    msg = " salary group not set ";
	    return msg;
	}
	if(group_id.isEmpty()){
	    msg = " group not set ";
	    return msg;
	}
	logger.debug(qq);
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = "Could not connect to DB";
	    return msg;
	}							
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, position_id);
	    pstmt.setString(2, salary_group_id);
	    pstmt.setString(3, employee_id);
	    pstmt.setString(4, group_id);						
						
	    if(effective_date.isEmpty())
		effective_date = Helper.getToday();
	    java.util.Date date_tmp = df.parse(effective_date);
	    pstmt.setDate(5, new java.sql.Date(date_tmp.getTime()));
	    if(!expire_date.isEmpty()){
		date_tmp = df.parse(expire_date);								
		pstmt.setDate(6, new java.sql.Date(date_tmp.getTime()));
	    }
	    else
		pstmt.setNull(6, Types.DATE);	
	    if(primary_flag.isEmpty())
		pstmt.setNull(7, Types.CHAR);
	    else
		pstmt.setString(7, "y");
	    pstmt.setInt(8, weekly_regular_hours);
	    pstmt.setInt(9, comp_time_weekly_hours);
	    pstmt.setDouble(10, comp_time_factor);
	    pstmt.setDouble(11, holiday_comp_factor);
	    if(clock_time_required.isEmpty())
		pstmt.setNull(12, Types.CHAR);
	    else
		pstmt.setString(12, "y");
	    pstmt.setDouble(13, hourly_rate);
	    if(include_in_auto_batch.isEmpty())
		pstmt.setNull(14, Types.CHAR);
	    else
		pstmt.setString(14, "y");
	    if(irregular_work_days.isEmpty())
		pstmt.setNull(15, Types.CHAR);
	    else
		pstmt.setString(15, "y");
						
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
	if(id.isEmpty()){
	    return " job id not set ";
	}
	if(employee_id.isEmpty()){
	    msg = " employee_id not set ";
	    return msg;
	}
	if(position_id.isEmpty()){
	    msg = " position not set ";
	    return msg;
	}
	if(salary_group_id.isEmpty()){
	    msg = " salary group not set ";
	    return msg;
	}
	if(group_id.isEmpty()){
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
	    "include_in_auto_batch=?,"+
	    "irregular_work_days=?,"+
	    "inactive=? "+
	    "where id=? ";
	logger.debug(qq);
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = "Could not connect to DB";
	    return msg;
	}						
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, position_id);
	    pstmt.setString(2, salary_group_id);
	    pstmt.setString(3, employee_id);
	    pstmt.setString(4, group_id);
	    if(effective_date.isEmpty())
		effective_date = Helper.getToday();
	    java.util.Date date_tmp = df.parse(effective_date);
	    pstmt.setDate(5, new java.sql.Date(date_tmp.getTime()));
	    if(!expire_date.isEmpty()){
		date_tmp = df.parse(expire_date);
		pstmt.setDate(6, new java.sql.Date(date_tmp.getTime()));
	    }
	    else
		pstmt.setNull(6, Types.DATE);										
	    if(primary_flag.isEmpty())
		pstmt.setNull(7, Types.CHAR);
	    else
		pstmt.setString(7, "y");
	    pstmt.setInt(8, weekly_regular_hours);
	    pstmt.setInt(9, comp_time_weekly_hours);
	    pstmt.setDouble(10, comp_time_factor);
	    pstmt.setDouble(11,holiday_comp_factor);
	    if(clock_time_required.isEmpty())
		pstmt.setNull(12, Types.CHAR);
	    else
		pstmt.setString(12, "y");								
	    pstmt.setDouble(13, hourly_rate);
	    if(include_in_auto_batch.isEmpty())
		pstmt.setNull(14, Types.CHAR);
	    else
		pstmt.setString(14, "y");						
	    if(irregular_work_days.isEmpty())
		pstmt.setNull(15, Types.CHAR);
	    else
		pstmt.setString(15, "y");
	    if(inactive.isEmpty())
		pstmt.setNull(16, Types.CHAR);
	    else
		pstmt.setString(16, "y");
	    pstmt.setString(17, id);
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
	if(prev_expire_date.isEmpty() && !expire_date.isEmpty()){
	    msg = checkRelatedGroupForExpire();
	}
	return msg;
    }
    private String checkRelatedGroupForExpire(){
	//
	// we want to find all active and future jobs that has similar group
	//
	String back = "";
	boolean expire_group = false;
	List<JobTask> jobs = null;
	JobTaskList jtl = new JobTaskList();
	jtl.setEmployee_id(employee_id);
	jtl.setGroup_id(group_id);
	jtl.setNotExpired();
	back = jtl.find();
	if(back.isEmpty()){
	    List<JobTask> ones = jtl.getJobs();
	    if(ones == null || ones.size() == 0){
		// there is no more jobs with the same group
		// then we can expire the group_employee record
		expire_group = true;
	    }
	}
	if(expire_group){
	    List<GroupEmployee> groupEmployees = null;
	    GroupEmployeeList del = new GroupEmployeeList(employee_id);
	    del.setGroup_id(group_id);
	    del.setCurrentOnly();
	    back = del.find();
	    if(back.isEmpty()){
		List<GroupEmployee> ones = del.getGroupEmployees();
		if(ones != null && ones.size() > 0){
		    groupEmployees = ones;
		}
	    }
	    // we should get only one group
	    if(groupEmployees != null && groupEmployees.size() == 1){
		GroupEmployee groupEmployee = groupEmployees.get(0);
		groupEmployee.setExpire_date(expire_date);
		back = groupEmployee.doExpireDateOnly();
	    }
	}
	return back;
    }
    //
    public String doChange(){
	Connection con = null;
	PreparedStatement pstmt = null, pstmt2=null, pstmt3=null, pstmt4=null;
	ResultSet rs = null;
	String msg="", str="";
	boolean add_group = false;
	String qq = "select expire_date from jobs where id=? ";
	String qq2 = "update jobs set expire_date = ? where id = ? and expire_date is null";
	String qq3 = "update time_documents t set t.job_id=? where t.job_id=? and pay_period_id >= ? ";
	String qq4 = "update department_employees set expire_date = ? where employee_id=? and department_id=? and expire_date is null ";
	if(new_group_id.isEmpty()){
	    msg = "group is required";
	    return msg;
	}				
	if(!group_id.equals(new_group_id)){
	    // a new group
	    add_group = true;
	}
	if(position_id.isEmpty()){
	    msg = "position is required";
	    return msg;
	}
	if(salary_group_id.isEmpty()){
	    msg = "salary group is required";
	    return msg;
	}
	//
	// ToDo check if we need to change department
	//
	String start_date = effective_date;
	String old_expire_date = Helper.getDateFrom(start_date,-1);
	logger.debug(qq);
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = "Could not connect to DB";
	    return msg;
	}						
	try{
	    String old_job_id = id;
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, old_job_id); 
	    rs = pstmt.executeQuery();
	    boolean alreadyExpired = false;
	    if(rs.next()){
		if(rs.getString(1) != null){
		    alreadyExpired = true;
		}
	    }
	    if(!alreadyExpired){
		qq = qq2;
		pstmt2 = con.prepareStatement(qq);
		java.util.Date date_tmp = df.parse(old_expire_date);
		pstmt2.setDate(1, new java.sql.Date(date_tmp.getTime()));
		pstmt2.setString(2, id);				
		pstmt2.executeUpdate();
		//
	    }
	    //
	    String old_group_id = group_id;
	    group_id = new_group_id;
	    id = "";
	    msg = doSave();
	    if(!alreadyExpired){
		String pp_id = ""; // pay period id
		PayPeriod pp = new PayPeriod();
		pp.setDate(start_date);
		msg = pp.find(); // find the pay period the date is in
		if(msg.isEmpty()){
		    pp_id = pp.getId();
		    qq = qq3;
		    pstmt3 = con.prepareStatement(qq);										
		    pstmt3.setString(1, id);
		    pstmt3.setString(2, old_job_id);
		    pstmt3.setString(3, pp_id);
		    pstmt3.executeUpdate();
		}
	    }
	    //
	    if(add_group){
		// add employee to the new group
		GroupEmployee ge = new GroupEmployee();
		ge.setGroup_id(new_group_id);
		ge.setEmployee_id(employee_id);
		ge.setEffective_date(start_date);
		msg = ge.doSave();
		if(msg.isEmpty()){
		    // find the old employee group
		    GroupEmployeeList gel = new GroupEmployeeList();
		    gel.setGroup_id(old_group_id);
		    gel.setEmployee_id(employee_id);
		    gel.setActiveOnly();
		    msg = gel.find();
		    if(msg.isEmpty()){
			List<GroupEmployee> ones = gel.getGroupEmployees();
			if(ones != null && ones.size() > 0){
			    ge = ones.get(0);
			    ge.setExpire_date(old_expire_date);
			    ge.doUpdate();
			}
		    }
		    //
		    // work with department
		    //
		    String old_dept_id = "", new_dept_id="";										
		    Group oldGroup = new Group(old_group_id);
		    msg = oldGroup.doSelect();
		    if(msg.isEmpty()){
			old_dept_id = oldGroup.getDepartment_id();
		    }
		    Group group = new Group(group_id);
		    msg = group.doSelect();
		    if(msg.isEmpty()){
			new_dept_id = group.getDepartment_id();
		    }
		    if(!old_dept_id.isEmpty() && !new_dept_id.isEmpty() &&
		       !old_dept_id.equals(new_dept_id)){
			// expire old dept
			qq = qq4; 
			pstmt4 = con.prepareStatement(qq);
			java.util.Date date_tmp = df.parse(old_expire_date);
			pstmt4.setDate(1, new java.sql.Date(date_tmp.getTime()));												
			pstmt4.setString(2, employee_id);
			pstmt4.setString(3, old_dept_id);
			pstmt4.executeUpdate();
			DepartmentEmployee deptEmp =
			    new DepartmentEmployee(employee_id,
						   new_dept_id,
						   start_date);
			// it will check if such record exists before saving
			msg = deptEmp.doSave();
		    }
		}
	    }
	}
	catch(Exception ex){
	    msg += " "+ex;
	    logger.error(msg+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(rs, pstmt, pstmt2, pstmt3, pstmt4);
	    UnoConnect.databaseDisconnect(con);
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
	if(id.isEmpty()){
	    return " id not set ";
	}
	if(position_id.isEmpty()){
	    msg = " position not set ";
	    return msg;
	}
	if(salary_group_id.isEmpty()){
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
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = "Could not connect to DB";
	    return msg;
	}
	try{
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
	    Helper.databaseDisconnect(pstmt, rs);
	    UnoConnect.databaseDisconnect(con);
	}
	return msg;
    }		
    public String doDelete(){
	//
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = "delete from jobs where id=? ";
	logger.debug(qq);
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = "Could not connect to DB";
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
    public String doDeleteJobAndDoc(String[] job_ids){
	//
	String msg="", str="";
	Connection con = null;
	PreparedStatement pstmt = null;
	PreparedStatement pstmt2 = null;
	PreparedStatement pstmt3 = null;
	PreparedStatement pstmt4 = null;
	PreparedStatement pstmt5 = null;
	ResultSet rs = null;
	if(job_ids == null || job_ids.length < 1){
	    return msg;
	}
	String q = "select a.id from time_actions a,time_documents d where a.document_id=d.id and d.job_id=?";				
	String qq = "";
	String qd = "delete from time_actions where id=? ";
	String qq2 = "select d.id from time_documents d where d.job_id=? ";
	String qd2 = "delete from time_documents where id=? ";				
	String qd3 = "delete from jobs where id=? ";				
	List<String> action_ids = new ArrayList<>();
	List<String> doc_ids = new ArrayList<>();
	logger.debug(qq);
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = "Could not connect to DB";
	    return msg;
	}
	try{
	    pstmt = con.prepareStatement(q);
	    pstmt2 = con.prepareStatement(qd);
	    pstmt3 = con.prepareStatement(qq2);
	    pstmt4 = con.prepareStatement(qd2);
	    pstmt5 = con.prepareStatement(qd3);
	    //
	    for(String j_id:job_ids){
		qq = q;
		pstmt.setString(1, j_id);
		rs = pstmt.executeQuery();
		while(rs.next()){
		    str = rs.getString(1);
		    action_ids.add(str);
		}
		if(action_ids.size()> 0){
		    qq = qd;
		    logger.debug(qq);
		    for(String a_id:action_ids){
			pstmt2.setString(1, a_id);
			pstmt2.executeUpdate();
		    }
		}
		qq = qq2;
		logger.debug(qq);
		pstmt3.setString(1, j_id);
		rs = pstmt3.executeQuery();
		while(rs.next()){
		    str = rs.getString(1);
		    doc_ids.add(str);
		}
		if(doc_ids.size() > 0){
		    qq = qd2;
		    logger.debug(qq);
		    for(String doc_id:doc_ids){
			pstmt4.setString(1, doc_id);
			pstmt4.executeUpdate();
		    }
		}
		qq = qd3;
		logger.debug(qq);
		pstmt5.setString(1, j_id);
		pstmt5.executeUpdate();
	    }
	}
	catch(Exception ex){
	    msg += " "+ex;
	    logger.error(msg+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(rs, pstmt, pstmt2, pstmt3, pstmt4,pstmt5);
	    UnoConnect.databaseDisconnect(con);
	}
	return msg;
    }		
    public boolean checkIfJobIsUsed(){
	if(id.isEmpty()) return false;
	DocumentList dl = new DocumentList();
	dl.setJob_id(id);
	String back = dl.find();
	if(back.isEmpty()){
	    List<Document> ones = dl.getDocuments();
	    return ones != null && ones.size() > 0;
	}
	return false;
    }
    public boolean checkIfJobHasTimeBlccks(){
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", str="";
	int cnt = 0;
	String qq = "select count(*) "+
	    " from time_blocks t,time_documents d "+
	    " where t.document_id=d.id and d.job_id=? ";
	logger.debug(qq);
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = "Could not connect to DB";
	    return true;
	}
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, id);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		cnt = rs.getInt(1);
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
	return cnt > 0;
    }
    public String doExpireJobs(String[] job_ids, String date){
	String msg="", str="";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;				
	if(job_ids == null || job_ids.length < 1){
	    msg = "no job list provided";
	    return msg;
	}
	if(date == null || date.isEmpty()){
	    msg = "No date is set ";
	    return msg;
	}
	String qq = "update jobs set expire_date=? where id=? ";
	logger.debug(qq);
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = "Could not connect to DB";
	    return msg;
	}
	try{
	    pstmt = con.prepareStatement(qq);
	    java.util.Date date_tmp = df.parse(date);
	    java.sql.Date sqlDate = new java.sql.Date(date_tmp.getTime());
	    for(String j_id:job_ids){
		System.err.println(" exp job "+j_id+" on "+date);
		pstmt.setDate(1, sqlDate);
		pstmt.setString(2, j_id);
		pstmt.executeUpdate();
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

