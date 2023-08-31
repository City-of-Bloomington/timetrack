package in.bloomington.timer.list;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.*;
import java.sql.*;
import java.io.*;
import javax.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;

public class TermManagerList{

    static Logger logger = LogManager.getLogger(TermManagerList.class);
    static final long serialVersionUID = 3800L;
    String sortBy=""; 
    String employee_id="", department_id=""; 
    boolean active_only = false;
    boolean any_department = false; // for HR 
    List<TermManager> managers = null;
	
    public TermManagerList(){
    }
		
    		
    public void setEmployee_id(String val){
	if(val != null)
	    employee_id = val;
    }
    public void setDepartment_id(String val){
	if(val != null && !val.equals("-1"))
	    department_id = val;
    }
    public void setActiveOnly(){
	active_only = true;
    }
    public void setAnyDepartment(){
	any_department = true;
    }
    public void setSortBy(String val){
	if(val != null)
	    sortBy = val;
    }
    public List<TermManager> getManagers(){
	return managers;
    }
    public String find(){
	String back = "";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection con = UnoConnect.getConnection();
	String qq = "select t.id,t.employee_id,t.department_id,t.inactive, "+
	    " d.name as deptName,concat_ws(' ',e.first_name,e.last_name) as empName "+ 
	    " from term_managers t "+
	    "join employees e on e.id=t.employee_id "+	    
	    "left join department_employees de on de.department_id=t.department_id "+
	    "left join departments d on d.id = de.department_id ";
	if(con == null){
	    back = "Could not connect to DB";
	    return back;
	}
	String qw = "";
	try{
	    if(!employee_id.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";
		qw += " t.employee_id = ? ";
	    }
	    if(!department_id.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";
		qw += " t.department_id = ? ";
	    }
	    else if(any_department){
		if(!qw.isEmpty()) qw += " and ";
		qw += " t.department_id is null ";
	    }
	    if(active_only){
		if(!qw.isEmpty()) qw += " and ";
		qw += " t.inactive is null ";
	    }
	    if(!qw.isEmpty()){
		qq += " where "+qw;
	    }
	    if(!sortBy.isEmpty()){
		qq += " order by "+sortBy;
	    }
	    logger.debug(qq);
	    pstmt = con.prepareStatement(qq);
	    int jj=1;
	    if(!employee_id.isEmpty()){
		pstmt.setString(jj++, employee_id);
	    }
	    if(!department_id.isEmpty()){
		pstmt.setString(jj++, department_id);					    }						
	    rs = pstmt.executeQuery();
	    if(managers == null)
		managers = new ArrayList<>();
	    while(rs.next()){
		TermManager one =
		    new TermManager(rs.getString(1),
				    rs.getString(2),
				    rs.getString(3),
				    rs.getString(4)!=null,
				    rs.getString(5),
				    rs.getString(6));
		if(!managers.contains(one))
		    managers.add(one);
	    }
	}
	catch(Exception ex){
	    back += ex+" : "+qq;
	    logger.error(back);
	}
	finally{
	    Helper.databaseDisconnect(pstmt, rs);
	    UnoConnect.databaseDisconnect(con);
	}
	return back;
    }
}






















































