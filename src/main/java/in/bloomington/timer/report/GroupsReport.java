package in.bloomington.timer.report;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.ArrayList;
import java.util.*;
import java.sql.*;
import java.text.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;
import in.bloomington.timer.list.*;

public class GroupsReport{

    //
    // find all groups in a department and the groups current managers
    //
    static Logger logger = LogManager.getLogger(GroupsReport.class);
    static final long serialVersionUID = 3820L;
    String department_id = "";
    Department department = null;
    List<List<String>> entries = null;
    public GroupsReport(){

    }
    public void setDepartment_id(String val){
	if(val != null && !val.equals("-1")){
	    department_id = val;
	    getDepartment(); 
	}
    }
    public String getDepartment_id(){
	if(department_id.isEmpty())
	    return "-1";
	return department_id;
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
    public boolean hasDepartment(){
	getDepartment();
	return department != null;
    }
    public List<List<String>> getEntries(){
	return entries;
    }
    public boolean hasData(){
	return entries != null && entries.size() > 0;
    }
    public String find(){
	String msg = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq =
	    " select g.name group_name,concat_ws(' ',e.first_name, e.last_name) emp_name,wn.name role_name,gm.primary_flag isPrimary                                          from `groups` g left join group_managers gm on g.id=gm.group_id                   join workflow_nodes wn on wn.id=gm.wf_node_id                                   join employees e on e.id=gm.employee_id                                         where g.department_id=? and gm.expire_date is null                              and gm.inactive is null                                                         and wn.id in (3,4,5)                                                            order by group_name, role_name, emp_name ";
	if(department_id.isEmpty()){
	    msg = " department ID not specified";
	    return msg;
	}
	con = Helper.getConnection();
	if(con == null){
	    msg = " Could not connect to DB ";
	    logger.error(msg);
	    return msg;
	}
	logger.debug(qq);
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, department_id);
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		if(entries == null)
		    entries = new ArrayList<>();
		String groupName = rs.getString(1);
		String roleName = rs.getString(2);
		String employeeName = rs.getString(3);
		String str = rs.getString(4);
		String isPrimary = (str == null)?"No":"Yes";
		List<String> row = new ArrayList<>();
		row.add(groupName);
		row.add(roleName);
		row.add(employeeName);
		row.add(isPrimary);
		entries.add(row);
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
	 select g.name group_name,concat_ws(' ',e.first_name, e.last_name) emp_name,wn.name role_name,gm.primary_flag isPrimary                                          from `groups` g left join group_managers gm on g.id=gm.group_id                   join workflow_nodes wn on wn.id=gm.wf_node_id                                   join employees e on e.id=gm.employee_id                                         where g.department_id=? and gm.expire_date is null                              and gm.inactive is null                                                         and wn.id in (3,4,5)                                                            order by group_name, role_name, emp_name ";


     */
}

