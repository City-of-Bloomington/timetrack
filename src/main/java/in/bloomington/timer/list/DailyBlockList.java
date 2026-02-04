package in.bloomington.timer.list;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.text.*;
import java.util.Date;
import java.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;

public class DailyBlockList{

    static final long serialVersionUID = 3700L;
    static Logger logger = LogManager.getLogger(DailyBlockList.class);
    String sortBy="full_name,time_date ";
    String department_id="", pay_period_id="", group_id="", salary_group_id="";
    
    List<DailyBlock> dailyBlocks = null;
    Set<String> empNumbers = null;
    public DailyBlockList(){
    }
    public DailyBlockList(String val,
			  String val2){
	setDepartment_id(val);
	setPay_period_id(val2);
    }
    public void setDepartment_id (String val){
	if(val != null)
	    department_id = val;
    }
    public void setGroup_id(String val){
	if(val != null)
	    group_id = val;
    }    
    public void setPay_period_id(String val){
	if(val != null)
	    pay_period_id = val;
    }
    
    public void setSortby(String val){
	if(val != null)
	    sortBy = val;
    }
    public List<DailyBlock> getDailyBlocks(){
	return dailyBlocks;
    }
    public Set<String> getEmpNumbers(){
	return empNumbers;
    }
    //
    // getters
    //
    public String find(){
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="";
	String qq = " select "+
	    " t.id AS block_id, "+
	    " t.document_id AS document_id, "+
	    " j.group_id AS group_id,"+
	    " g.department_id AS department_id,"+
	    " d.pay_period_id AS pay_period_id,"+
	    " j.salary_group_id AS salary_group_id,"+
	    " p2.name AS job_title,"+
	    " c.name AS earn_code, "+
	    " r.name AS earn_code_reason,"+
	    " concat_ws(' ',e.first_name,e.last_name) AS full_name,"+
	    " e.id AS employee_id,"+
	    " e.employee_number AS empnum,"+	    	    
	    " date_format(t.date,'%m/%d/%Y') AS time_date,"+ 
	    " d2.name AS department_name,"+
	    " g.name AS group_name, "+
	    " t.notes AS notes,"+
	    " n.nw_code AS nw_code,"+
	    " n.gl_string AS gl_string, "+
	    " t.hours AS hours, "+
	    " t.amount AS amount, "+
	    " s.name "+
	    " from time_blocks t "+
	    " join hour_codes c on t.hour_code_id=c.id "+
	    " join time_documents d on d.id=t.document_id "+
	    " join pay_periods p on p.id=d.pay_period_id "+
	    " join jobs j on d.job_id=j.id "+
	    " join positions p2 on j.position_id=p2.id "+
	    " join employees e on j.employee_id=e.id "+
	    " join salary_groups s on j.salary_group_id=s.id "+
	    " join groups g on j.group_id=g.id "+
	    " join departments d2 on g.department_id=d2.id "+
	    " join code_cross_ref n on n.code_id=c.id "+
	    " left join earn_code_reasons r on t.earn_code_reason_id=r.id ";
	String qw = " where t.inactive is null and (t.hours > 0 or t.amount > 0) and "+
	    " d.pay_period_id= ? ";
	if(!salary_group_id.isEmpty()){
	    qw += " and j.salary_group_id = ? ";
	}
	if(!group_id.isEmpty()){
	    qw += " and j.group_id = ? ";
	}
	else if(!department_id.isEmpty()){
	    qw += " and g.department_id = ? ";
	}	    
	if(!qw.isEmpty()){
	    qq += qw;
	}
	if(!sortBy.isEmpty()){
	    qq += " order by "+sortBy;
	}
	if(pay_period_id.isEmpty()){
	    msg = " pay period not set ";
	    logger.error(msg);
	    return msg;
	}
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = " Could not connect to DB ";
	    logger.error(msg);
	    return msg;
	}
	logger.debug(qq);
	try{
	    pstmt = con.prepareStatement(qq);
	    String empNo = "";
	    int jj=1;
	    pstmt.setString(jj++, pay_period_id);
	    if(!salary_group_id.isEmpty()){
		pstmt.setString(jj++, salary_group_id);
	    }
	    if(!group_id.isEmpty()){
		pstmt.setString(jj++, group_id);
	    }
	    else if(!department_id.isEmpty()){
		pstmt.setString(jj++, department_id);
	    }
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		if(dailyBlocks == null)
		    dailyBlocks = new ArrayList<>();
		if(empNumbers == null)
		    empNumbers = new HashSet<>();
		String salaryGroupName = rs.getString(21);
		boolean isSeasonal = false;
		if(salaryGroupName.equals("Temp") ||
		   salaryGroupName.indexOf("Season") >-1){
		    isSeasonal = true;
		}
		DailyBlock one = new DailyBlock(
						rs.getString(1),
						rs.getString(2), // emp num
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
						rs.getDouble(19),
						rs.getDouble(20),
						isSeasonal
						);
		empNo = rs.getString(2);
		if(!empNumbers.contains(empNo))
		    empNumbers.add(empNo);
		if(!dailyBlocks.contains(one))
		    dailyBlocks.add(one);
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
