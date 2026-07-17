package in.bloomington.timer;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.*;
import java.sql.*;
import java.text.*;
import javax.naming.*;
import javax.naming.directory.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HandleTempNotification{

    boolean debug = false;
    static final long serialVersionUID = 53L;
    static Logger logger = LogManager.getLogger(HandleTempNotification.class);
    static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    static DecimalFormat df = new DecimalFormat("#0.00");
    static int temp_max_days = 275; // 9 months
    static int seasonal_max_days = 183; // 6 months
    //
    String dept_ref_id=""; // dept referance in NW app, one or more values
    String date = "";
    Hashtable<String, String> empHash = null;
    List<List<String>> tempJobs = null;
    List<List<String>> seasonalJobs = null;
    //
    // accrual values from New World (Carry Over)
    //
    public HandleTempNotification(){
    }
    public HandleTempNotification(String val){
	setDate(val);
    }

    //
    // setters
    //
    public void setDept_ref_id(String val){
	if(val != null){		
	    dept_ref_id = val;
	}
    }
    public void setDate(String val){
	if(val != null){		
	    date = val;
	}
    }
    /**
    private String prepareEmployee(){
	String msg = "";
	EmployeeList empl = new EmployeeList();
	if(!dept_ref_id.isEmpty()){
	    empl.setDept_ref_id(dept_ref_id);
	}
	empl.setHasEmployeeNumber();
	msg = empl.find();
	if(msg.isEmpty()){
	    List<Employee> emps = empl.getEmployees();
	    if(emps != null && emps.size() > 0){
		empHash = new Hashtable<>();
		empNewRates = new Hashtable<>();		
		for(Employee one:emps){
		    // System.err.println(" emp "+one.getId()+","+one.getEmployee_number());
		    empHash.put(one.getEmployee_number(), one.getId());
		}
	    }
	}
	EmpPayRateList epl = new EmpPayRateList();
	msg = epl.findLatest();
	if(msg.isEmpty()){
	    empOldRates = epl.getRateHash();
	}
	else{
	    logger.error(msg);
	    System.err.println(msg);
	}
	return msg;
    }
    */
    /**
	select distinct (j.id),
	concat_ws(' ',e.first_name,e.last_name) as full_name,
	concat_ws(' ',e2.first_name,e2.last_name) as approver,
	e2.email approver_email,
	    p.name as job_title, 
	    g.name as group_name,
	    DATEDIFF(now(), j.effective_date) as days_since_start 
	    from jobs j 
	    join salary_groups sg on sg.id=j.salary_group_id 
	    join positions p on j.position_id=p.id 
	     join employees e on j.employee_id=e.id 
	     join `groups` g on g.id=j.group_id 
	     join departments d on g.department_id=d.id
	     left join group_managers mg on mg.group_id=g.id
	     and mg.employee_id = (select gm.employee_id from group_managers gm join workflow_nodes wn on wn.id=gm.wf_node_id
	     join employees e3 on gm.employee_id=e3.id and e3.inactive is null 
	     where gm.group_id = g.id and gm.expire_date is null and gm.wf_node_id=3 and gm.inactive is null order by gm.primary_flag desc limit 1)
	     left join employees e2 on mg.employee_id=e2.id 
	     where j.salary_group_id = 3 and
	     j.inactive is null and 
	     j.expire_date is null and
	     DATEDIFF(now(), j.effective_date) > 275 and
	     e.inactive is null 

     */
    //
    public String process(){
		
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="";
	String qq = "select distinct (j.id),"+
	    "concat_ws(' ',e.first_name,e.last_name) as full_name,"+
	    "concat_ws(' ',e2.first_name,e2.last_name) as approver,"+
	    "e2.email approver_email,"+
	    "p.name as job_title,"+
	    "g.name as group_name,"+
	    "DATEDIFF(now(), j.effective_date) as days_since_start"+
	    "from jobs j "+
	    "join salary_groups sg on sg.id=j.salary_group_id "+
	    "join positions p on j.position_id=p.id "+
	    "join employees e on j.employee_id=e.id "+
	    "join `groups` g on g.id=j.group_id "+
	    "join departments d on g.department_id=d.id "+
	    "left join group_managers mg on mg.group_id=g.id "+
	    "and mg.employee_id = (select gm.employee_id from group_managers gm join workflow_nodes wn on wn.id=gm.wf_node_id "+
	    "join employees e3 on gm.employee_id=e3.id and e3.inactive is null "+
	    "where gm.group_id = g.id and gm.expire_date is null and gm.wf_node_id=3 and gm.inactive is null order by gm.primary_flag desc limit 1) "+
	    "left join employees e2 on mg.employee_id=e2.id "+
	    "where j.salary_group_id = ? and "+ // temp 3, seasonal  13
	    "j.inactive is null and  "+
	    "j.expire_date is null and "+
	    "DATEDIFF(now(), j.effective_date) > ? and "+
	    "e.inactive is null"; 

	con = UnoConnect.getConnection();
	if(con == null){
	    msg = "Could not connect to DB";
	    return msg;
	}
	try{
	    logger.debug(qq);
	    pstmt = con.prepareStatement(qq);
	    pstmt.setInt(1,3);
	    pstmt.setInt(2, temp_max_days);
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		if(tempJobs == null) tempJobs = new ArrayList<>();
		List<String> one = new ArrayList<>();
		for(int j=1;j<8;j++){
		    one.add(rs.getString(j));
		}
		tempJobs.add(one);
	    }
	    pstmt.setInt(1,13);
	    pstmt.setInt(2, seasonal_max_days);
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		if(seasonalJobs == null) seasonalJobs = new ArrayList<>();
		List<String> one = new ArrayList<>();
		for(int j=1;j<8;j++){
		    one.add(rs.getString(j));
		}
		seasonalJobs.add(one);
	    }	    
	}
	catch (Exception ex) {
	    logger.error(ex+":"+qq);
	    msg += ex;
	}
	finally{
	    Helper.databaseDisconnect(pstmt, rs);
	    UnoConnect.databaseDisconnect(con);
	}
	return msg;
    }

}
