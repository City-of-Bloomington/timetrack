package in.bloomington.timer;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */

import java.util.*;
import java.sql.*;
import java.io.*;
import java.text.*;
import javax.sql.*;
import java.util.stream.Collectors;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobDataMap;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EmpNoNumJob implements Job{

    boolean debug = true;
    static final long serialVersionUID = 55L;		
    static Logger logger = LogManager.getLogger(EmpNoNumJob.class);
    List<List<String>> entries= null;
    Hashtable<String, Set<Integer>> empDepts = null;
    public EmpNoNumJob(){

    }
    public void execute(JobExecutionContext context)
        throws JobExecutionException {
	try{
	    doInit();
	    doWork();
	    doDestroy();
	}
	catch(Exception ex){
	    logger.error(ex);
	    System.err.println(ex);
	}
    }
    public void doInit(){
	empDepts = new Hashtable<>();
	String back = findEmployees();
	if(!back.isEmpty()){
	    System.err.println(back);
	    logger.error(back);
	}
    }
    public void doDestroy() {

    }	    
    public void doWork(){
	String back = findNwEmployeeSet();
	if(!back.isEmpty()){
	    System.err.println(back);
	    logger.error(back);
	}
    }
    public String findEmployees(){
	String msg = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "select distinct e.id,"+
	    " e.first_name,"+
	    " e.last_name,"+
	    " p.name job_title,"+
	    " d.ref_id reference "+
	    " from employees e "+
	    " join jobs j on j.employee_id=e.id "+
	    " join positions p on j.position_id=p.id "+
	    " join groups g on g.id=j.group_id "+
	    " join Departments d on g.department_id=d.id "+
	    " where j.expire_date is null "+
	    " and e.employee_number is null "+
	    " and e.id not in (312) "+
	    " order by e.id ";
	con = Helper.getConnection();
	if(con == null){
	    msg = " Could not connect to DB ";
	    logger.error(msg);
	    return msg;
	}
	logger.debug(qq);
	try{
	    pstmt = con.prepareStatement(qq);
	    rs = pstmt.executeQuery();
	    if(entries == null)
		entries = new ArrayList<>();
	    while(rs.next()){
		String str = rs.getString(1);
		String str2 = rs.getString(2);
		String str3 = rs.getString(3);
		String str4 = rs.getString(4);
		String str5 = rs.getString(5);
		List<String> row = new ArrayList<>();
		row.add(str);
		row.add(str2);
		row.add(str3);
		row.add(str4);
		row.add(str5);
		entries.add(row);
		Set<Integer> deptSet = null;
		if(str5.indexOf(",") > -1){
		    deptSet = Arrays.stream(str5.split(","))
			.map(Integer::parseInt)                           
			.collect(Collectors.toSet());     			
		}
		else{
		    int val = -1;
		    try{
			 val = Integer.parseInt(str5);
		    }catch(Exception ee){
			System.err.println(ee);
		    }
		    if(val > -1){
			deptSet = new HashSet<>();
			deptSet.add(val);
		    }
		}
		empDepts.put(str, deptSet);
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
    public String findNwEmployeeSet(){
	Connection con = null, con2 = null;
	PreparedStatement pstmt = null, pstmt2=null;
	ResultSet rs = null;
	String msg="", back="", date = null;
	String qq = " select etj.employeeNumber,etj.departmentID,etj.title from "+
	    " HR.vwEmployeeTimetrackJob etj where "+
	    "etj.firstname like ? and "+
	    "etj.lastname like ? ";	    
	String qq2 = " update employees set employee_number = ? where id=? ";
	con = SingleConnect.getNwConnection();
	if(con == null){
	    msg = " Could not connect to DB ";
	    logger.error(msg);
	    return msg;
	}
	con2 = Helper.getConnection();
	if(con2 == null){
	    msg = " Could not connect to DB ";
	    logger.error(msg);
	    return msg;
	}
	logger.debug(qq);
	try{
	    logger.debug(qq);
	    pstmt = con.prepareStatement(qq);
	    pstmt2 = con2.prepareStatement(qq2);
	    for (List one:entries){
		boolean found = false;
		String emp_id = (String)one.get(0);
		System.err.println(one.get(1)+" "+one.get(2)+" "+one.get(4));
		pstmt.setString(1, (String)one.get(1));
		pstmt.setString(2, (String)one.get(2));
		rs = pstmt.executeQuery();
		while(rs.next()){
		    String emp_num = rs.getString(1);
		    int dept_ref = rs.getInt(2);
		    String job_title = rs.getString(3);
		    if(empDepts.containsKey(emp_id)){
			Set<Integer> set = empDepts.get(emp_id);
			if(set.contains(dept_ref)){
			    System.err.println(" match "+one.get(0)+" "+emp_num+" "+dept_ref+" "+job_title);
			    found = true;
			    pstmt2.setString(1, emp_num);
			    pstmt2.setString(2, emp_id);
			    pstmt2.executeUpdate();
			    System.err.println(" updated "+emp_id+" "+emp_num);
			}else{
			    System.err.println(" no match dept "+dept_ref+" "+one.get(0)+" "+emp_num+" "+dept_ref+" "+job_title);
			}
		    }
		}
		if(!found){
		    System.err.println("No match "+one);
		}
	    }
	}
	catch(Exception ex){
	    back += ex;
	    logger.error(ex+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(con, rs);
	    Helper.databaseDisconnect(con2, rs);	    
	    Helper.databaseDisconnect(rs, pstmt, pstmt2);
	}
	return back;
    }    
}






















































