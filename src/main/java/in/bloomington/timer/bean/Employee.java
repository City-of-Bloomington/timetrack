package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.Serializable;
import java.util.Set;
import java.util.List;
import java.util.HashSet;
import java.util.Hashtable;
import java.sql.*;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Employee implements Serializable{

    static Logger logger = LogManager.getLogger(Employee.class);
    static final long serialVersionUID = 1150L;		
    private String id="", inactive="",
	id_code="", employee_number="", // both are unique
	email="", role="",
	username="", // unique
	full_name="", first_name="", last_name="";
    // needed for saving
    String department_id="", group_id="";
    String group_ids = ""; // for new employee with multiple groups/jobs
    // normally this date is pay period start date
    String job_active_date = "", pay_period_id="", selected_job_id="";
    // User user = null;
    PayPeriod payPeriod  = null;
    List<JobTask> jobs = null;
    List<Group> groups = null;
    List<GroupManager> approvers = null;
    List<GroupManager> processors = null;
    List<GroupManager> reviewers = null;
    List<GroupManager> enterors = null;
    List<DepartmentEmployee> departmentEmployees = null;
    List<GroupEmployee> groupEmployees = null;
    Shift shift = null;
    DepartmentEmployee departmentEmployee = null;
    Department department = null;
    GroupEmployee groupEmployee = null;
    boolean receive_email = true;
    //
    // for a given selected job and pay_period_id we need to find
    // salary group, hour codes
    //
    public Employee(){
    }
		
    public Employee(String val){
	setId(val);
    }
    public Employee(String val, String val2){
	setId(val);
	setUsername(val2);
    }
    public Employee(String val,
		    String val2,
		    String val3,
		    String val4,
		    String val5
		    ){
	setId(val);
	setUsername(val2);
	setFirst_name(val3);
	setLast_name(val4);
	setEmail(val5);
    }				
    // for new record (ldap)
    public Employee(String val,
		    String val2,
		    String val3,
		    String val4,
		    String val5,
		    String val6
		    ){
	setUsername(val);
	setFirst_name(val2);
	setLast_name(val3);
	setId_code(val4);
	setEmployee_number(val5);
	setEmail(val6);
    }		
    public Employee(String val,
		    String val2,
		    String val3,
		    String val4,
		    String val5,
		    String val6,
		    String val7,
		    String val8,
		    boolean val9
		    ){
	setVals(val, val2, val3, val4, val5, val6,
		val7, val8, val9);
    }
    void setVals(String val,
		 String val2,
		 String val3,
		 String val4,
		 String val5,
		 String val6,
		 String val7,
		 String val8,
		 boolean val9
		 ){
	setId(val);
	setUsername(val2);
	setFirst_name(val3);
	setLast_name(val4);
	setId_code(val5);
	setEmployee_number(val6);
	setEmail(val7);
	setRole(val8);
	setInactive(val9);				
    }
    //
    // getters
    //
    public String getId(){
	return id;
    }
    public String getId_code(){
	return id_code;
    }
    public String getEmployee_number(){
	return employee_number;
    }
    public boolean isInactive(){
	return !inactive.equals("");
    }
    public boolean isActive(){
	return inactive.equals("");
    }
    public boolean hasNoEmployeeNumber(){
	return employee_number.equals("");
    }
    public boolean hasOneGroup(){
	getGroups();
	return groups != null && groups.size() == 1;
    }
    public Group getGroup(){
	getGroups();
	return groups.get(0);
    }
    public boolean hasGroup(){
	getGroups();
	return groups != null && groups.size() > 0;
    }
    public boolean hasGroup(Group gg){
	getGroups();
	return groups != null && groups.contains(gg);
    }
    public String getUsername(){
	return username;
    }
    public String getFull_name(){
	if(full_name.equals("")){
	    full_name = first_name;
	    if(!full_name.equals("")) full_name += " ";
	    full_name += last_name;
	}
	return full_name;
    }
    public String getFirst_name(){
	return first_name;
    }
    public String getLast_name(){
	return last_name;
    }
    public String getRole(){
	return role;
    }
    public String getEmail(){
				
	return email;
    }
    public boolean canReceiveEmail(){
	return receive_email;
    }
    //
    // setters
    //
    public void setId (String val){
	if(val != null)
	    id = val;
    }

    public void setId_code (String val){
	if(val != null && !val.equals("0"))
	    id_code = val.trim();
    }
    public void setEmployee_number(String val){
	if(val != null)
	    employee_number = val.trim();
    }
    public void setUsername (String val){
	if(val != null){
	    username = val.trim();
	    if(username.indexOf(".") > 0)
		receive_email = false;
	}
    }
    public void setFirst_name (String val){
	if(val != null)
	    first_name = val.trim();
    }
    public void setLast_name (String val){
	if(val != null)
	    last_name = val.trim();
    }
    // for auto complete
    public void setFull_name(String val){

    }
    // sometimes in AD email is set as N/A
    public void setEmail(String val){
	if(val != null && !val.equals("N/A")){
	    email = val.trim();
	}
	if(email.equals(""))
	    receive_email = false;
    }
    public void setRole (String val){
	if(val != null && !val.equals("-1"))
	    role = val;
    }
    // needed for new employee
    public void setDepartment_id(String val){
	if(val != null && !val.equals("-1")){
	    department_id = val;
	    if(departmentEmployee == null){
		departmentEmployee = new DepartmentEmployee();
	    }
	    departmentEmployee.setDepartment_id(val);
	}
    }
    public void setEffective_date(String val){
	if(val != null){
	    if(departmentEmployee == null){
		departmentEmployee = new DepartmentEmployee();
	    }
	    departmentEmployee.setEffective_date(val);
	    if(groupEmployee == null){
		groupEmployee = new GroupEmployee();
	    }
	    groupEmployee.setEffective_date(val);
	}
    }
    public void setDepartmentEmployee(DepartmentEmployee val){
	if(val != null)
	    departmentEmployee = val;
    }
    public void setGroupEmployee(GroupEmployee val){
	if(val != null)
	    groupEmployee = val;
    }		
    // needed for new employee
    public void setGroup_id(String val){
	if(val != null && !val.equals("-1")){
	    group_id = val;
	    if(groupEmployee == null){
		groupEmployee = new GroupEmployee();
	    }
	    groupEmployee.setGroup_id(val);
	}
    }
    public void addGroup_id(String val){
	if(val != null && !val.equals("")){
	    if(!group_ids.equals("")) group_ids +=",";
	    group_ids += val;
	}
    }		
    public void setJob_active_date(String val){
	if(val != null)
	    job_active_date = val;
    }
    public void setPay_period_id(String val){
	if(val != null)
	    pay_period_id = val;
    }
    public void setSelected_job_id(String val){
	if(val != null)
	    selected_job_id = val;
    }
    public String getDepartment_id(){
	if(department_id.equals("")){
	    getDepartmentEmployees();
	    if(departmentEmployee != null){
		department_id = departmentEmployee.getDepartment_id();
	    }
	}
	return department_id;
    }
    public String getGroup_id(){
	if(group_id.equals("")){
	    getGroupEmployee();
	    if(groupEmployee != null){
		group_id = groupEmployee.getGroup_id();
	    }
	}
	return group_id;
    }
    public void setInactive (boolean val){
	if(val)
	    inactive = "y";
    }

    public String toString(){
	return getFull_name();
    }
    public boolean equals(Object o) {
	if (o instanceof Employee) {
	    Employee c = (Employee) o;
	    if ( this.id.equals(c.getId())) 
		return true;
	}
	return false;
    }
    public int hashCode(){
	int seed = 31;
	if(!id.equals("")){
	    try{
		seed += Integer.parseInt(id)*47;
	    }catch(Exception ex){
		// we ignore
	    }
	}
	return seed;
    }
    public String getInfo(){
	String ret="";
	if(!id.equals("")){
	    ret = "id = "+id+", ";
	}
	ret += "username = "+username;
	if(!first_name.equals("")){
	    ret += ", first name = "+first_name;
	}
	if(!last_name.equals("")){
	    ret += ", last name = "+last_name;
	}
	if(!employee_number.equals("")){
	    ret += ", emp # = "+employee_number;
	}
	if(!id_code.equals("")){
	    ret += ", id code = "+id_code;
	}
	if(!email.equals(""))
	    ret += ", email ="+email;
	return ret;
    }
    public boolean hasRole(String val){
	return role.indexOf(val) > -1;
    }
    public boolean hasNoRole(){
	return role.equals("");
    }
    public boolean isEmployee(){
	return hasRole("Employee");
    }
    public boolean isAdmin(){
	return hasRole("Admin");
    }
    void findPayPeriod(){
	if(pay_period_id.equals("")){
	    PayPeriodList ppl = new PayPeriodList();
	    ppl.currentOnly();
	    String back = ppl.find();
	    if(back.equals("")){
		List<PayPeriod> ones = ppl.getPeriods();
		if(ones != null && ones.size() > 0){
		    PayPeriod one = ones.get(0);
		    pay_period_id = one.getId();
		}
	    }
	}
    }
    public List<Group> getGroups(){
	if(groups == null && !id.equals("")){
	    GroupList gl = new GroupList(id);
	    gl.setActiveOnly();
	    if(pay_period_id.equals("")){
		findPayPeriod();
	    }
	    if(!pay_period_id.equals("")){
		gl.setPay_period_id(pay_period_id);
	    }
	    String back = gl.find();
	    if(back.equals("")){
		List<Group> ggs = gl.getGroups();
		if(ggs.size() > 0){
		    groups = ggs;
		}
	    }
	}
	return groups;
    }
    public boolean hasGroups(){
	getGroups();
	return groups != null && groups.size() > 0;
    }
    public Shift getShift(){
	if(shift == null) findShift();
	return shift;
    }
    public boolean hasShift(){
	getShift();
	return shift != null;
    }
    /**
     * even if an employee have more than one group,
     * we need one active shift
     */
    void findShift(){
	getGroups();
	String group_ids = "";
	if(groups != null && groups.size() > 0){
	    for(Group one:groups){
		if(!group_ids.equals("")) group_ids +=","; 
		group_ids += one.getId();
	    }
	}
	if(!group_ids.equals("")){
	    GroupShiftList gsl = new GroupShiftList();
	    gsl.setGroup_ids(group_ids);
	    gsl.setActiveOnly();
	    String back = gsl.find();
	    if(back.equals("")){
		List<GroupShift> ones = gsl.getGroupShifts();
		if(ones != null && ones.size() > 0){
		    Shift one = ones.get(0).getShift();
		    if(one != null){
			shift = one;
		    }
		}
	    }
	    
	}
    }

    public boolean canApprove(){
	if(approvers == null){
	    GroupManagerList gml = new GroupManagerList(id);
	    gml.setActiveOnly();
	    gml.setApproversOnly();
	    String back = gml.find();
	    if(back.equals("")){
		List<GroupManager> ones = gml.getManagers();
		if(ones != null){
		    approvers = ones;
		}
	    }
	}
	return approvers != null && approvers.size() > 0;
    }
    public List<GroupManager> getApprovers(){
	return approvers;
    }
    public List<GroupManager> getProcessors(){
	return processors;
    }
    public List<GroupManager> getReviewers(){
	return reviewers;
    }
    public List<GroupManager> getEnterors(){
	return enterors;
    }		
    public boolean canPayrollProcess(){
	if(processors == null){
	    GroupManagerList gml = new GroupManagerList(id);
	    gml.setActiveOnly();
	    gml.setProcessorsOnly();
	    String back = gml.find();
	    if(back.equals("")){
		List<GroupManager> ones = gml.getManagers();
		if(ones != null){
		    processors = ones;
		}
	    }
	}
	return processors != null && processors.size() > 0;
    }
    public boolean canReview(){
	if(reviewers == null){
	    GroupManagerList gml = new GroupManagerList(id);
	    gml.setActiveOnly();
	    gml.setReviewersOnly();
	    String back = gml.find();
	    if(back.equals("")){
		List<GroupManager> ones = gml.getManagers();
		if(ones != null){
		    reviewers = ones;
		}
	    }
	}
	return reviewers != null && reviewers.size() > 0;
    }
    /**
     * time maintainer group role
     */
    public boolean canMaintain(){
	if(enterors == null){
	    GroupManagerList gml = new GroupManagerList(id);
	    gml.setActiveOnly();
	    gml.setTimeMaintainerOnly();
	    String back = gml.find();
	    if(back.equals("")){
		List<GroupManager> ones = gml.getManagers();
		if(ones != null){
		    enterors = ones;
		}
	    }
	}
	return enterors != null && enterors.size() > 0;
    }		
    //
    public boolean hasDepartment(){
	getDepartment();
	return department != null;
    }
    public Department getDepartment(){
	if(department == null && !id.equals("")){
	    DepartmentEmployeeList del = new DepartmentEmployeeList(id);
	    if(pay_period_id.equals("")){
		findPayPeriod();
	    }
	    if(!pay_period_id.equals("")){
		del.setPay_period_id(pay_period_id);
	    }
	    String back = del.find();
	    if(back.equals("")){
		List<DepartmentEmployee> des = del.getDepartmentEmployees();
		if(des != null && des.size() > 0){
		    DepartmentEmployee one = des.get(0);// first
		    department = one.getDepartment();
		}
	    }
	}
	return department;
    }
    public boolean hasDepartments(){
	getDepartmentEmployees();
	return departmentEmployees != null;
    }		
    public List<DepartmentEmployee> getDepartmentEmployees(){
	if(departmentEmployees == null && !id.equals("")){
	    DepartmentEmployeeList del = new DepartmentEmployeeList(id);
	    if(!pay_period_id.equals("")){
		findPayPeriod();
	    }
	    del.setPay_period_id(pay_period_id);
	    String back = del.find();
	    if(back.equals("")){
		List<DepartmentEmployee> des = del.getDepartmentEmployees();
		if(des != null && des.size() > 0){
		    departmentEmployees = des;
		    departmentEmployee = des.get(0); 
		}
	    }
	}
	return departmentEmployees;
    }
    public boolean hasActiveDepartment(){
	if(hasDepartments()){
	    return departmentEmployee != null;
	}
	return false;
    }
    public String getCurrentDepartment_id(){
	if(hasActiveDepartment()){
	    department_id = departmentEmployee.getDepartment_id();
	}
	return department_id;
    }
    public List<GroupEmployee> getGroupEmployees(){
	if(groupEmployees == null && !id.equals("")){
	    GroupEmployeeList del = new GroupEmployeeList(id);
	    if(!pay_period_id.equals("")){
		findPayPeriod();
	    }
	    del.setPay_period_id(pay_period_id);
	    String back = del.find();
	    if(back.equals("")){
		List<GroupEmployee> ones = del.getGroupEmployees();
		if(ones != null && ones.size() > 0){
		    groupEmployees = ones;
		    for(GroupEmployee one:ones){
			if(one.isActive() && one.isCurrent()){
			    groupEmployee = one;
			    break;
			}
		    }
		    if(groupEmployee == null){
			// first one the defualt
			groupEmployee = groupEmployees.get(0);
		    }
		}
	    }
	}
	return groupEmployees;
    }
    public boolean hasActiveGroup(){
	getGroupEmployees();
	return groupEmployee != null;
    }
    public boolean hasMultipleGroups(){
	getGroupEmployees();
	return groupEmployees != null && groupEmployees.size() > 1;
    }
    public boolean hasOneGroupOnly(){
	getGroupEmployees();
	return groupEmployees != null && groupEmployees.size() == 1;
    }		
    public GroupEmployee getGroupEmployee(){
	getGroupEmployees();
	return groupEmployee;
    }
    public String validate(){
	String msg = "";
	if(username.equals("")){
	    msg = "username";
	}
	if(last_name.equals("")){
	    if(!msg.equals("")) msg += ", ";
	    msg += "last name";
	}
	if(!msg.equals("")){
	    msg += " required but not set";
	}
	return msg;
    }
    public List<JobTask> getJobs(){
	if(!id.equals("") && jobs == null){
	    JobTaskList jtl = new JobTaskList();
	    jtl.setEmployee_id(id);
	    if(!pay_period_id.equals("")){
		jtl.setPay_period_id(pay_period_id);
	    }
	    String back = jtl.find();
	    if(back.equals("")){
		List<JobTask> ones = jtl.getJobs();
		if(ones != null && ones.size() > 0){
		    jobs = ones;
		}
	    }
	}
	return jobs;
    }
    public boolean hasJobs(){
	getJobs();
	return jobs != null && jobs.size() > 0;
    }
    public boolean hasMultipleJobs(){
	getJobs();
	return jobs != null && jobs.size() > 1;
    }
    public boolean hasOneJobOnly(){
	getJobs();
	return jobs != null && jobs.size() == 1;
    }
    public JobTask getJob(){
	if(hasOneJobOnly())
	    return jobs.get(0);
	return null;
    }
    public boolean hasNoJob(){
	getJobs();
	return jobs == null || jobs.size() == 0;
    }
    public boolean isLeaveEligible(){
	JobTask job = null;				
	getJobs();
	if(jobs != null){
	    job = jobs.get(0); // primary job if more than one
	}
	return job != null && job.isLeaveEligible();
    }
    public boolean isSameEntity(Employee one){
	return one.getUsername().equals(username) && 
	    one.getLast_name().equals(last_name) &&
	    one.getFirst_name().equals(first_name) &&
	    one.getId_code().equals(id_code) &&
	    one.getEmployee_number().equals(employee_number);
    }
    public String doSelect(){
	//
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = "select e.id,e.username,e.first_name,e.last_name,e.id_code,e.employee_number,e.email,e.role,e.inactive from employees e where ";
	if(!id.equals("")){
	    qq += " e.id = ? ";
	}
	else if(!username.equals("")){ // for login
	    qq += " e.username like ? ";		
	}				
	else if(!id_code.equals("")){ // for punch clock machines
	    qq += " e.id_code = ? ";		
	}
	else{
	    msg = "Employee info can not be found as no employee id is set";
	    return msg;
	}
	logger.debug(qq);
	try{
	    con = UnoConnect.getConnection();
	    if(con != null){
		pstmt = con.prepareStatement(qq);
		if(!id.equals("")){
		    pstmt.setString(1, id);
		}
		else if(!username.equals("")){
		    pstmt.setString(1, username);
		}
		else{
		    pstmt.setString(1, id_code);
		}
		rs = pstmt.executeQuery();
		//
		if(rs.next()){
		    setVals(rs.getString(1),
			    rs.getString(2),
			    rs.getString(3),
			    rs.getString(4),
			    rs.getString(5),
			    rs.getString(6),
			    rs.getString(7),
			    rs.getString(8),
			    rs.getString(9) != null);
		}
		else{
		    msg = "Employee not found";
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
    /**
     * data for user and employee are entered in the same form
     * so we need to save user class first
     */
    public String doSave(){
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;		
	String msg="", str="";
	inactive=""; // default
	msg = validate();
	if(!msg.equals("")){
	    return msg;
	}
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = "Could not connect to DB ";
	    return msg;
	}
	String qq = " insert into employees values(0,?,?,?,?, ?,?,?,?)";
	try{
	    pstmt = con.prepareStatement(qq);
	    msg = setParams(pstmt);
	    if(msg.equals("")){
		pstmt.executeUpdate();
		Helper.databaseDisconnect(pstmt, rs);
		//
		qq = "select LAST_INSERT_ID()";
		pstmt = con.prepareStatement(qq);
		rs = pstmt.executeQuery();
		if(rs.next()){
		    id = rs.getString(1);
		}
		if(departmentEmployee != null){
		    departmentEmployee.setEmployee_id(id);
		    msg = departmentEmployee.doSave();
		}
		if(!group_id.equals("")){
		    if(groupEmployee != null){
			groupEmployee.setEmployee_id(id);
			msg = groupEmployee.doSave();
		    }
		}
		else if(!group_ids.equals("")){
		    String[] g_arr = null;
		    try{
			g_arr = group_ids.split(",");
		    }catch(Exception ex){
			System.err.println(ex);
		    }
		    if(g_arr != null && g_arr.length > 0){
			for(String str2:g_arr){
			    groupEmployee = new GroupEmployee();
			    groupEmployee.setGroup_id(str2);
			    groupEmployee.setEmployee_id(id);
			    msg = groupEmployee.doSave();
			}
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
	if(msg.equals(""))
	    msg = doSelect();
	return msg;
    }
    String setParams(PreparedStatement pstmt){
	String msg = "";
	int jj=1;
	try{
	    pstmt.setString(jj++, username);
	    if(first_name.equals(""))
		pstmt.setNull(jj++, Types.VARCHAR);
	    else
		pstmt.setString(jj++, first_name);
	    pstmt.setString(jj++, last_name);
	    if(id_code.equals("") || id_code.equals("0"))
		pstmt.setNull(jj++, Types.VARCHAR);
	    else
		pstmt.setString(jj++, id_code);
	    if(employee_number.equals(""))
		pstmt.setNull(jj++, Types.VARCHAR);
	    else
		pstmt.setString(jj++, employee_number);
	    if(email.equals(""))
		getEmail();
	    pstmt.setString(jj++, email);
	    if(role.equals(""))
		role = "Employee";
	    pstmt.setString(jj++, role);
	    if(id.equals("")) inactive = ""; // new record
	    if(inactive.equals(""))
		pstmt.setNull(jj++, Types.CHAR);
	    else
		pstmt.setString(jj++, "y");						
	}
	catch(Exception ex){
	    msg += " "+ex;
	    logger.error(msg);
	}
	return msg;
    }
    public String doUpdate(){
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = " update employees set username=?,first_name=?,last_name=?,id_code=?, employee_number=?, email=?,role=?,inactive=? where id=?";
	if(id.equals("")){
	    msg = "id is required";
	    return msg;
	}
	msg = validate();
	if(!msg.equals("")){
	    return msg;
	}
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = "Could not connect to DB ";
	    return msg;
	}
	try{
	    pstmt = con.prepareStatement(qq);
	    msg = setParams(pstmt);
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
	return msg;
    }
    public String doUpdateDeptGroupInfo(){
	String msg="";
	if(departmentEmployee != null){
	    departmentEmployee.setEmployee_id(id);
	    msg = departmentEmployee.doSave();
	}
	if(!group_id.equals("")){
	    if(groupEmployee != null){
		groupEmployee.setEmployee_id(id);
		msg = groupEmployee.doSave();
	    }
	}
	return msg;
    }
    public String doUpdateFromLdap(Employee ldapEmp){
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = " update employees set username=?,first_name=?,last_name=?,id_code=?, email=?, employee_number=? where id=? ";
	if(id.equals("")){
	    msg = "id is required";
	    return msg;
	}
	if(ldapEmp == null){
	    msg = "ldap employee info not provided";
	    return msg;
	}
	try{
	    // System.err.println("Old emp info "+getInfo()); // old data
	    // System.err.println("New emp inof "+ldapEmp.getInfo());
	    setUsername(ldapEmp.getUsername());
	    setFirst_name(ldapEmp.getFirst_name());
	    setLast_name(ldapEmp.getLast_name());
	    // in case not in ldap yet and entered mannually
	    // we ignore ldap code if not set yet
	    if(!ldapEmp.getId_code().equals("")) 
		setId_code(ldapEmp.getId_code());
	    // same apply for employee number
	    if(!ldapEmp.getEmployee_number().equals(""))						
		setEmployee_number(ldapEmp.getEmployee_number());
	    setEmail(ldapEmp.getEmail());
						
	    con = UnoConnect.getConnection();
	    if(con == null){
		msg = "Could not connect to DB ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, username);
	    if(first_name.equals(""))
		pstmt.setNull(2, Types.VARCHAR);
	    else
		pstmt.setString(2, first_name);
	    pstmt.setString(3, last_name);
	    if(id_code.equals(""))
		pstmt.setNull(4, Types.VARCHAR);
	    else
		pstmt.setString(4, id_code);
	    if(email.equals(""))
		getEmail();
	    pstmt.setString(5, email);						

	    pstmt.setString(6, employee_number);
	    pstmt.setString(7, id);
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
     * change the iactive status of the following users to inactive
     * this function is used by CurrentEmployeesHandler
     */
    public String updateInactiveStatus(String idSet){
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = " update employees set inactive='y' where id in ("+idSet+")";
	if(idSet == null || idSet.equals("")){
	    return msg;
	}
	try{
	    con = UnoConnect.getConnection();
	    if(con == null){
		msg = "Could not connect to DB ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
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

}
