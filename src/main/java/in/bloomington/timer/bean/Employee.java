package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.Serializable;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.sql.*;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Employee implements Serializable, Comparable<Employee>{

    static Logger logger = LogManager.getLogger(Employee.class);
    static final long serialVersionUID = 1150L;		
    private String id="", inactive="", 
	id_code="", // unique
	employee_number="", //unique
	ad_sid="", // unique, last 4 bytes of AD sid object (28 bytes)
    // create a 8 character string
	email="", 
	username="", // unique
	full_name="", first_name="", last_name="";
    String added_date = "";
    String effective_date = ""; // for new record
    String[] roles = {""};
    Set<String> roleSet = new HashSet<>();
    // needed for saving
    String department_id="", group_id="";
    String department2_id = ""; // for people with two departments
    String group_ids = ""; // for new employee with multiple groups/jobs
    // normally this date is pay period start date
    String job_active_date = "", pay_period_id="", selected_job_id="";
    // User user = null;
    PayPeriod payPeriod  = null;
    List<JobTask> jobs = null;
    List<JobTask> allJobs = null;
    List<JobTask> allJobs2 = null;		
    List<Group> groups = null;
    List<GroupManager> groupManagers = null; // for any type
    List<GroupManager> approvers = null;
    List<GroupManager> processors = null;
    List<GroupManager> reviewers = null;
    List<GroupManager> enterors = null;
    List<DepartmentEmployee> departmentEmployees = null;
    // List<GroupEmployee> groupEmployees = null;
    // List<GroupEmployee> allGroupEmployees = null; // include expired ones too
    List<GroupShift> groupShifts = null;
    Shift shift = null;
    DepartmentEmployee departmentEmployee = null;
    Department department = null;
    // GroupEmployee groupEmployee = null;
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
		    String val6,
		    String val7
		    ){
	setUsername(val);
	setFirst_name(val2);
	setLast_name(val3);
	setId_code(val4);
	setEmployee_number(val5);
	setAd_sid(val6);
	setEmail(val7);
    }		
    public Employee(String val,
		    String val2,
		    String val3,
		    String val4,
		    String val5,
		    String val6,
		    String val7,
		    String val8,
		    String val9,
		    String val10,
		    boolean val11
		    ){
	setVals(val, val2, val3, val4, val5, val6,
		val7, val8, val9, val10, val11);
    }
    void setVals(String val,
		 String val2,
		 String val3,
		 String val4,
		 String val5,
		 String val6,
		 String val7,
		 String val8,
		 String val9,
		 String val10,
		 boolean val11
		 ){
	setId(val);
	setUsername(val2);
	setFirst_name(val3);
	setLast_name(val4);
	setId_code(val5);
	setEmployee_number(val6);
	setAd_sid(val7);
	setEmail(val8);
	setRolesText(val9);
	setAdded_date(val10);
	setInactive(val11);				
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
    public String getAd_sid(){
	return ad_sid;
    }		
    public boolean isInactive(){
	return !inactive.isEmpty();
    }
    public boolean isActive(){
	return inactive.isEmpty();
    }
    public boolean hasNoEmployeeNumber(){
	return employee_number.isEmpty();
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
	if(full_name.isEmpty()){
	    full_name = first_name;
	    if(!full_name.isEmpty()) full_name += " ";
	    full_name += last_name;
	}
	return full_name;
    }
    public String getNameLastFirst(){
	String ret = last_name;
	if(!ret.isEmpty()) ret += ", ";
	ret += first_name;
	return ret;
    }		
    public String getFirst_name(){
	return first_name;
    }
    public String getLast_name(){
	return last_name;
    }
    public String getAdded_date(){
	return added_date;
    }
    public String[] getRoles(){
	return roles;
    }
    public String getRolesText(){
	String ret = "";
	if(!roleSet.isEmpty()){
	    for(String str:roleSet){
		// if(ret.indexOf(str) > -1) continue;
		if(!ret.isEmpty()) ret += ", ";
		ret += str.trim();
	    }
	}
	return ret;
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
    public void setAd_sid(String val){
	if(val != null && !val.isEmpty())
	    ad_sid = val;
    }		
    public void setEmployee_number(String val){
	if(val != null && !val.trim().isEmpty())
	    employee_number = val.trim();
    }

    public void setUsername (String val){
	if(val != null){
	    username = val.trim();
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
    public void setAdded_date(String val){
	if(val != null)
	    added_date = val;
    }		
    // for auto complete
    public void setFull_name(String val){

    }
    // sometimes in AD email is set as N/A
    public void setEmail(String val){
	if(val != null && !val.equals("N/A") && !val.isEmpty()){
	    email = val.trim();
	}
	if(email.isEmpty())
	    receive_email = false;
    }
    public void setRolesText (String val){
	if(val != null && !val.isEmpty()){
	    if(val.indexOf(",") > -1){
		roles = val.split(",");
		for(String str:roles){
		    String str2 = str.trim();
		    if(roleSet.contains(str2)) continue;
		    roleSet.add(str2);
		}
	    }
	    else{
		String str = val.trim();
		roles = new String[]{str};
		roleSet.add(str);
	    }
	}
    }		
    public void setRoles (String[] vals){
	if(vals != null && vals.length > 0){
	    for(String str:vals){
		String str2 = str.trim();								
		if(roleSet.contains(str2)) continue;
		roleSet.add(str2);
	    }						
	    roles = new String[roleSet.size()];
	    int j=0;
	    for(String str:roleSet){
		roles[j++] = str;
	    }
	}
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
	if(val != null && !val.equals("-1")){
	    effective_date = val;
	    if(departmentEmployee == null){
		departmentEmployee = new DepartmentEmployee();
	    }
	    departmentEmployee.setEffective_date(val);
	    /**
	       // to delete
	    if(groupEmployee == null){
		groupEmployee = new GroupEmployee();
	    }
	    groupEmployee.setEffective_date(val);
	    */
	}
    }
    public String getEffective_date(){// needed for wizard
	return effective_date;
    }
				
    public void setDepartmentEmployee(DepartmentEmployee val){
	if(val != null)
	    departmentEmployee = val;
    }
    /**
    public void setGroupEmployee(GroupEmployee val){
	if(val != null)
	    groupEmployee = val;
    }
    */
    // needed for new employee
    public void setGroup_id(String val){
	if(val != null && !val.equals("-1")){
	    group_id = val;
	    /**
	    if(groupEmployee == null){
		groupEmployee = new GroupEmployee();
	    }
	    groupEmployee.setGroup_id(val);
	    */
	}
    }
    public void addGroup_id(String val){
	if(val != null && !val.isEmpty()){
	    if(!group_ids.isEmpty()) group_ids +=",";
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
	if(department_id.isEmpty()){
	    getDepartmentEmployees();
	    if(departmentEmployee != null){
		department_id = departmentEmployee.getDepartment_id();
		if(departmentEmployee.hasSecondaryDept()){
		    department2_id = departmentEmployee.getDepartment2_id();
		}
	    }
	}
	return department_id;
    }
    public String getGroup_id(){
	if(group_id.isEmpty()){
	    if(hasOneJobOnly()){
		JobTask job = getJob();
		group_id = job.getGroup_id();
	    }
	    /**
	    getGroupEmployee();
	    if(groupEmployee != null){
		group_id = groupEmployee.getGroup_id();
	    }
	    */
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
    @Override
    public boolean equals(Object o) {
	if (o instanceof Employee) {
	    Employee c = (Employee) o;
	    if ( this.id.equals(c.getId())) 
		return true;
	}
	return false;
    }
    @Override
    public int hashCode(){
	int seed = 31;
	if(!id.isEmpty()){
	    try{
		seed += Integer.parseInt(id)*47;
	    }catch(Exception ex){
		// we ignore
	    }
	}
	return seed;
    }
    @Override
    public int compareTo(Employee other){
        int ret = this.toString().compareTo(other.toString());
        return ret;
    }
    public String getInfo(){
	String ret="";
	if(!id.isEmpty()){
	    ret = "id = "+id+", ";
	}
	ret += "username = "+username;
	if(!first_name.isEmpty()){
	    ret += ", first name = "+first_name;
	}
	if(!last_name.isEmpty()){
	    ret += ", last name = "+last_name;
	}
	if(!employee_number.isEmpty()){
	    ret += ", emp # = "+employee_number;
	}
	if(!id_code.isEmpty()){
	    ret += ", id code = "+id_code;
	}
	if(!ad_sid.isEmpty()){
	    ret += ", AD Sid = "+ad_sid;
	}				
	if(!email.isEmpty())
	    ret += ", email = "+email;
	return ret;
    }
    public boolean hasRole(String val){
	return roleSet.contains(val);
    }		
    public boolean hasNoRole(){
	return roleSet.isEmpty();
    }
    public boolean isEmployee(){
	return hasRole("Employee");
    }
    public boolean isAdmin(){
	return hasRole("Admin");
    }
    public boolean canEdit(){
	return hasRole("Edit") && !isAdmin(); // for non-admin role
    }
    public boolean isPoliceAdmin(){
	return hasRole("PoliceAdmin");
    }
    public boolean isEngineeringAdmin(){
	return hasRole("EngineeringAdmin");
    }    
    public boolean isITSAdmin(){
	return hasRole("ITSAdmin");
    }
		
    public boolean isHrAdmin(){
	return hasRole("HrAdmin");
    }
    public boolean isPublicWorksAdmin(){
	return hasRole("PublicWorksAdmin");
    }		
    public boolean canRunTimewarp(){
	return hasRole("Timewarp");
    }
    public boolean canRunFmlaReport(){
	return hasRole("FMLAReport");
    }
    public boolean canRunMpoReport(){
	return hasRole("MPOReport");
    }
    public boolean canRunHandReport(){
	return hasRole("HANDReport");
    }		
    public boolean canRunParkReport(){
	return hasRole("ParkReport");
    }
    public boolean canRunFireReport(){
	return hasRole("FireReport");
    }		
    public boolean canRunTargetEmployee(){
	return hasRole("TargetEmployee");
    }
    public boolean hasAdSid(){
	return !ad_sid.isEmpty() && !id.isEmpty();
    }
    public boolean hasNoAdSid(){
	return ad_sid.isEmpty() && !id.isEmpty();
    }		
    void findPayPeriod(){
	if(pay_period_id.isEmpty()){
	    PayPeriodList ppl = new PayPeriodList();
	    ppl.currentOnly();
	    String back = ppl.find();
	    if(back.isEmpty()){
		List<PayPeriod> ones = ppl.getPeriods();
		if(ones != null && ones.size() > 0){
		    PayPeriod one = ones.get(0);
		    pay_period_id = one.getId();
		}
	    }
	}
    }
    public List<Group> getGroups(){
	if(groups == null && !id.isEmpty()){
	    GroupList gl = new GroupList(id);
	    gl.setActiveOnly();
	    if(pay_period_id.isEmpty()){
		findPayPeriod();
	    }
	    if(!pay_period_id.isEmpty()){
		gl.setPay_period_id(pay_period_id);
	    }
	    String back = gl.find();
	    if(back.isEmpty()){
		List<Group> ggs = gl.getGroups();
		if(ggs != null && ggs.size() > 0){
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
    public boolean hasGroupShifts(){
	if(groupShifts == null){
	    findShift();
	}
	return groupShifts != null && groupShifts.size() > 0;
    }
    public List<GroupShift> getGroupShifts(){
	return groupShifts;
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
		if(!group_ids.isEmpty()) group_ids +=","; 
		group_ids += one.getId();
	    }
	}
	if(!group_ids.isEmpty()){
	    GroupShiftList gsl = new GroupShiftList();
	    gsl.setGroup_ids(group_ids);
	    gsl.setActiveOnly();
	    String back = gsl.find();
	    if(back.isEmpty()){
		List<GroupShift> ones = gsl.getGroupShifts();
		if(ones != null && ones.size() > 0){
		    groupShifts = ones;
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
	    gml.setActiveOnly();
	    if(pay_period_id.isEmpty()){
		findPayPeriod();
	    }
	    gml.setPay_period_id(pay_period_id);
	    String back = gml.find();
	    if(back.isEmpty()){
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
	    if(pay_period_id.isEmpty()){
		findPayPeriod();
	    }
	    gml.setPay_period_id(pay_period_id);						
	    String back = gml.find();
	    if(back.isEmpty()){
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
	    if(pay_period_id.isEmpty()){
		findPayPeriod();
	    }
	    gml.setPay_period_id(pay_period_id);	
	    String back = gml.find();
	    if(back.isEmpty()){
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
	    if(pay_period_id.isEmpty()){
		findPayPeriod();
	    }
	    gml.setPay_period_id(pay_period_id);
	    String back = gml.find();
	    if(back.isEmpty()){
		List<GroupManager> ones = gml.getManagers();
		if(ones != null){
		    enterors = ones;
		}
	    }
	}
	return enterors != null && enterors.size() > 0;
    }
    public boolean isGroupManager(){
	if(groupManagers == null){
	    GroupManagerList gml = new GroupManagerList(id);
	    gml.setActiveOnly();
	    if(pay_period_id.isEmpty()){
		findPayPeriod();
	    }
	    gml.setPay_period_id(pay_period_id);
	    String back = gml.find();
	    if(back.isEmpty()){
		List<GroupManager> ones = gml.getManagers();
		if(ones != null){
		    groupManagers = ones;
		}
	    }
	}
	return groupManagers != null && groupManagers.size() > 0;
    }
    public List<GroupManager> getGroupManagers(){
	return groupManagers;
    }
    //
    public boolean hasDepartment(){
	getDepartment();
	return department != null;
    }
    public boolean hasDepartment(boolean includeFutureDate){
	if(hasDepartments()){ // include future dates
	    getDepartmentEmployees();
	    if(departmentEmployee != null){
		department = departmentEmployee.getDepartment();
	    }
	}
	return department != null;
    }
    public Department getDepartment(){
	if(department == null && !id.isEmpty()){
	    DepartmentEmployeeList del = new DepartmentEmployeeList(id);
	    if(pay_period_id.isEmpty()){
		findPayPeriod();
	    }
	    if(!pay_period_id.isEmpty()){
		del.setPay_period_id(pay_period_id);
	    }
	    String back = del.find();
	    if(back.isEmpty()){
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
	if(departmentEmployees == null && !id.isEmpty()){
	    DepartmentEmployeeList del = new DepartmentEmployeeList(id);
	    del.setIncludeFuture();
	    String back = del.find();
	    if(back.isEmpty()){
		List<DepartmentEmployee> des = del.getDepartmentEmployees();
		if(des != null && des.size() > 0){
		    departmentEmployees = des;
		    departmentEmployee = des.get(0); 
		}
	    }
	}
	return departmentEmployees;
    }
    public boolean hasMultipleDepts(){
	getDepartmentEmployees();
	return departmentEmployees != null && departmentEmployees.size() > 1;
    }
    public List<String> getAlEmpDeptIds(){
	List<String> empDeptIds = null;
	if(hasMultipleDepts()){
	    empDeptIds = new ArrayList<>();
	    for(DepartmentEmployee one:departmentEmployees){
		empDeptIds.add(one.getDepartment_id());
	    }
	}
	return empDeptIds;
    }
    public boolean hasActiveDepartment(){
	if(hasDepartment()){
	    return departmentEmployee != null;
	}
	return false;
    }
    public DepartmentEmployee getDepartmentEmployee(){
	if(hasActiveDepartment()){
	    return departmentEmployee;
	}
	return null;
    }
    public String getCurrentDepartment_id(){
	if(hasActiveDepartment()){
	    department_id = departmentEmployee.getDepartment_id();
	    if(departmentEmployee.hasSecondaryDept())
		department2_id = departmentEmployee.getDepartment2_id();
	}
	return department_id;
    }
    public String getDepartment2_id(){
	return department2_id;
    }
    public boolean hasTwoDepartments(){
	getDepartment_id();
	return !department_id.isEmpty() && !department2_id.isEmpty();
    }

    public String validate(){
	String msg = "";
	if(username.isEmpty()){
	    msg = "username";
	}
	if(last_name.isEmpty()){
	    if(!msg.isEmpty()) msg += ", ";
	    msg += "last name";
	}
	if(!msg.isEmpty()){
	    msg += " required but not set";
	}
	return msg;
    }
    public List<JobTask> getJobs(){
	if(!id.isEmpty() && jobs == null){
	    JobTaskList jtl = new JobTaskList();
	    jtl.setEmployee_id(id);
	    if(!pay_period_id.isEmpty()){
		jtl.setPay_period_id(pay_period_id);
	    }
	    String back = jtl.find();
	    if(back.isEmpty()){
		List<JobTask> ones = jtl.getJobs();
		if(ones != null && ones.size() > 0){
		    jobs = ones;
		}
	    }
	}
	return jobs;
    }
		
    public boolean hasAllJobs(){
	getAllJobs();
	return allJobs != null && allJobs.size() > 0;
    }
    public List<JobTask> getAllJobs(){
	if(!id.isEmpty() && allJobs == null){
	    JobTaskList jtl = new JobTaskList();
	    jtl.setEmployee_id(id);
	    jtl.setIncludeFuture();
	    String back = jtl.find();
	    if(back.isEmpty()){
		List<JobTask> ones = jtl.getJobs();
		if(ones != null && ones.size() > 0){
		    allJobs = ones;
		}
	    }
	}
	return allJobs;
    }
    public boolean hasAllJobs2(){
	getAllJobs2();
	return allJobs2 != null && allJobs2.size() > 0;
    }
    // include all including expired
    public List<JobTask> getAllJobs2(){
	if(!id.isEmpty() && allJobs2 == null){
	    JobTaskList jtl = new JobTaskList();
	    jtl.setEmployee_id(id);
	    String back = jtl.find();
	    if(back.isEmpty()){
		List<JobTask> ones = jtl.getJobs();
		if(ones != null && ones.size() > 0){
		    allJobs2 = ones;
		}
	    }
	}
	return allJobs2;
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
	getAllJobs();
	return allJobs == null || allJobs.size() == 0;
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
	boolean ret = one.getUsername().equals(username) && 
	    one.getLast_name().equals(last_name) &&
	    one.getFirst_name().equals(first_name) &&
	    one.getAd_sid().equals(ad_sid);
	if(!ret) return ret;
	if(employee_number.isEmpty()){
	    if(!one.getEmployee_number().isEmpty()){
		return false;
	    }
	}
	else if(one.getEmployee_number().isEmpty()){
	    //if ldap no employee number 
	    // we ignore
	}
	else {
	    ret = ret && employee_number.equals(one.getEmployee_number());
	}
	if(id_code.isEmpty()){
	    if(!one.getId_code().isEmpty()){
		return false;
	    }
	}
	else if(one.getId_code().isEmpty()){
	    // if Ldap has no id Code
	    // we ignore
	}
	else{
	    ret = ret && id_code.equals(one.getId_code());
	}
	return ret;
    }
    /**
     * find job titles from New World app
     * current employee only
     */
    /*
    // list of the fields
    1 JobTitle
    2 EmployeeNumber
    3 EmployeeJobID
    4 EffectiveDate
    5 EffectiveEndDate
    6 EmployeeID
    7 GradeId
    8 GradeType
    9 GradeStepId
    10 CycleHours
    11 DailyHours
    12 DepartmentId
    13 RateAmount
    14 PositionId
    15 JobId
    16 JobTitle
    17 PositionDetailESD
    18 PositionDetailEED
    19 IsPrimaryJob
    20 JobEventReasonId
    21 PositionNumber
    */
    public List<String> findJobTitlesFromNW(){
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", back="", date = null;
	List<String> jobTitles = new ArrayList<>();
	// using current date
	String qq = "select p.JobTitle, e.EmployeeNumber, p.* "+
	    " from hr.vwEmployeeJobWithPosition p, "+
	    " hr.employee e "+
	    " where e.EmployeeId = p.EmployeeID "+
	    " and getdate() between EffectiveDate and EffectiveEndDate "+
	    " and getdate() between PositionDetailESD and PositionDetailEED "+
	    // " and e.EmployeeNumber = ? "+
	    " and p.departmentID in (30,36) "+ // 39 parks
	    " order by e.employeenumber ";
	// p.departmentID in ();
	con = SingleConnect.getNwConnection();
	if(con == null){
	    msg = " Could not connect to DB ";
	    logger.error(msg);
	    return null;
	}
	try{
	    pstmt = con.prepareStatement(qq);
						
	    rs = pstmt.executeQuery();
	    /*
	      ResultSetMetaData rsmd = rs.getMetaData();
	      int columnCount = rsmd.getColumnCount();
	      // The column count starts from 1
	      for (int i = 1; i <= columnCount; i++ ) {
	      String str = rsmd.getColumnName(i);
	      System.err.println(i+" "+str);
	      }
	    */
	    while(rs.next()){
		String str = rs.getString(1);
		String str2 = rs.getString(2); // emp number
		// String str3 = rs.getString(7); // grade Id
		// String str4 = rs.getString(11); // daily hours
		str = str2+" "+str;
		if(!jobTitles.contains(str))
		    jobTitles.add(str);
	    }
	}
	catch(Exception ex){
	    back += ex;
	    logger.error(ex+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(pstmt, rs);
	}
	return jobTitles;
    }
    public String doSelect(){
	//
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = "select e.id,e.username,e.first_name,e.last_name,e.id_code,e.employee_number,e.ad_sid,e.email,e.roles,date_format(e.added_date,'%m/%d/%Y'),e.inactive from employees e where ";
	if(!id.isEmpty()){
	    qq += " e.id = ? ";
	}
	else if(!id_code.isEmpty()){ // for punch clock machines
	    qq += " e.id_code = ? ";		
	}
	else if(!username.isEmpty()){ // for login
	    qq += " e.username like ? ";		
	}
	else if(!ad_sid.isEmpty()){ 
	    qq += " e.ad_sid like ? ";		
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
		if(!id.isEmpty()){
		    pstmt.setString(1, id);
		}
		else if(!id_code.isEmpty()){
		    pstmt.setString(1, id_code);
		}								
		else if(!username.isEmpty()){
		    pstmt.setString(1, username);
		}
		else if(!ad_sid.isEmpty()){
		    pstmt.setString(1, ad_sid);
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
			    rs.getString(9),
			    rs.getString(10),
			    rs.getString(11) != null);
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
	PreparedStatement pstmt = null, pstmt2=null;
	ResultSet rs = null;		
	String msg="", str="";
	String qq = " insert into employees values(0,?,?,?,?, ?,?,?,?,now(),?)";				
	inactive=""; // default
	msg = validate();
	if(!msg.isEmpty()){
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
	    if(msg.isEmpty()){
		pstmt.executeUpdate();
		//
		qq = "select LAST_INSERT_ID()";
		pstmt2 = con.prepareStatement(qq);
		rs = pstmt2.executeQuery();
		if(rs.next()){
		    id = rs.getString(1);
		}
		if(departmentEmployee != null){
		    departmentEmployee.setEmployee_id(id);
		    departmentEmployee.setEffective_date(effective_date);
		    msg = departmentEmployee.doSave();
		}
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
	if(msg.isEmpty())
	    msg = doSelect();
	return msg;
    }
    String setParams(PreparedStatement pstmt){
	String msg = "";
	int jj=1;
	try{
	    pstmt.setString(jj++, username);
	    if(first_name.isEmpty())
		pstmt.setNull(jj++, Types.VARCHAR);
	    else
		pstmt.setString(jj++, first_name);
	    pstmt.setString(jj++, last_name);
	    if(id_code.isEmpty() || id_code.equals("0"))
		pstmt.setNull(jj++, Types.VARCHAR);
	    else
		pstmt.setString(jj++, id_code);
	    if(employee_number.isEmpty())
		pstmt.setNull(jj++, Types.VARCHAR);
	    else
		pstmt.setString(jj++, employee_number);
	    if(ad_sid.isEmpty())
		pstmt.setNull(jj++, Types.VARCHAR);
	    else
		pstmt.setString(jj++, ad_sid);						
	    if(email.isEmpty()){
		pstmt.setNull(jj++, Types.VARCHAR);
	    }
	    else{
		pstmt.setString(jj++, email);
	    }
	    String role = "";						
	    if(!roleSet.isEmpty()){
		for(String str:roleSet){
		    if(!role.isEmpty()) role +=",";
		    role += str.trim();
		}
	    }
	    else{
		role = "Employee";	
	    }
	    pstmt.setString(jj++, role);
	    if(id.isEmpty()) inactive = ""; // new record
	    if(inactive.isEmpty())
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
	String qq = " update employees set username=?,first_name=?,last_name=?,id_code=?, employee_number=?, ad_sid=?, email=?,roles=?,inactive=? where id=?";
	if(id.isEmpty()){
	    msg = "id is required";
	    return msg;
	}
	msg = validate();
	if(!msg.isEmpty()){
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
	    pstmt.setString(10, id);
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
	doSelect();
	return msg;
    }
    public String doUpdateDeptGroupInfo(){
	String msg="";
	if(departmentEmployee != null){
	    departmentEmployee.setEmployee_id(id);
	    msg = departmentEmployee.doSave();
	}
	return msg;
    }
    public String doUpdateFromLdap(Employee ldapEmp){
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = " update employees set username=?,first_name=?,"+
	    " last_name=?,id_code=?, email=?, employee_number=?, "+
	    " ad_sid=? "+
	    " where id=? ";
	if(id.isEmpty()){
	    msg = "id is required";
	    return msg;
	}
	if(ldapEmp == null){
	    msg = "ldap employee info not provided";
	    return msg;
	}
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = "Could not connect to DB ";
	    return msg;
	}				
	try{
	    setUsername(ldapEmp.getUsername());
	    setFirst_name(ldapEmp.getFirst_name());
	    setLast_name(ldapEmp.getLast_name());
	    //
	    // in case not in ldap yet and entered mannually
	    // we ignore ldap code if not set yet
	    if(!ldapEmp.getId_code().isEmpty()) 
		setId_code(ldapEmp.getId_code());
	    // same apply for employee number
	    if(!ldapEmp.getEmployee_number().isEmpty())						
		setEmployee_number(ldapEmp.getEmployee_number());
	    setEmail(ldapEmp.getEmail());
	    setAd_sid(ldapEmp.getAd_sid());
	    // 
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, username);
	    if(first_name.isEmpty())
		pstmt.setNull(2, Types.VARCHAR);
	    else
		pstmt.setString(2, first_name);
	    pstmt.setString(3, last_name);
	    if(id_code.isEmpty())
		pstmt.setNull(4, Types.VARCHAR);
	    else
		pstmt.setString(4, id_code);
	    if(email.isEmpty())
		pstmt.setNull(5, Types.VARCHAR);
	    else
		pstmt.setString(5, email);
	    if(employee_number.isEmpty())
		pstmt.setNull(6, Types.VARCHAR);
	    else						
		pstmt.setString(6, employee_number);
	    if(ad_sid.isEmpty())
		pstmt.setNull(7, Types.VARCHAR);
	    else
		pstmt.setString(7, ad_sid);
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
	if(idSet == null || idSet.isEmpty()){
	    return msg;
	}
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = "Could not connect to DB ";
	    return msg;
	}				
	try{
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
