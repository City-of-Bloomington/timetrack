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

public class EmployeeNoNumberReport{

    //
    // find all groups in a department and the groups current managers
    //
    static Logger logger = LogManager.getLogger(EmployeeNoNumberReport.class);
    static final long serialVersionUID = 3820L;
    List<List<String>> entries= null;
    public EmployeeNoNumberReport(){

    }
    public boolean hasEntries(){
	return entries != null && entries.size() > 0;
	     
    }
    public List<List<String>> getEntries(){
	return entries;
    }
	
    //
    // find new employees with no employee number
    // needed for exporting to NW
    public String find(){
	String msg = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "select distinct e.id,e.username username,concat_ws(' ',e.first_name,e.last_name) full_name "+
	    " from employees e "+
	    " join jobs j on j.employee_id=e.id "+
	    " join time_documents d on d.job_id=j.id "+
	    " join pay_periods p on p.id = d.pay_period_id "+
	    " join time_blocks b on b.document_id=d.id"+
	    " where j.expire_date is null "+
	    " and e.employee_number is null"+
	    " and p.start_date <= DATE_SUB(CURDATE(), INTERVAL 5 DAY) "+
	    " and p.end_date >= DATE_SUB(CURDATE(), INTERVAL 5 DAY)"+
	    " order by full_name ";
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
		List<String> row = new ArrayList<>();
		row.add(str);
		row.add(str2);
		row.add(str3);
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
      //
      // find new employees who have no employee_number
      //
      select distinct e.id,e.username username,concat_ws(' ',e.first_name,e.last_name) full_name
      from employees e
      join jobs j on j.employee_id=e.id
      join time_documents d on d.job_id=j.id
      join pay_periods p on p.id = d.pay_period_id
      join time_blocks b on b.document_id=d.id
      where j.expire_date is null 
      and e.employee_number is null
      and p.start_date <= DATE_SUB(CURDATE(), INTERVAL 5 DAY) 
      and p.end_date >= DATE_SUB(CURDATE(), INTERVAL 5 DAY)
      order by full_name
     */
}

