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
import java.util.TreeMap;
import java.util.Map;
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
    PayPeriod payPeriod = null;
    List<DailyBlock> dailyBlocks = null;
    Set<String> empNumbers = null;
    Map<String, Map<String, Double>> week1EmpCodes = new TreeMap<>();
    Map<String, Map<String, Double>> week2EmpCodes = new TreeMap<>();    
    //
    // earn codes that timewarp adds
    // CE1.0, CE1.5, CE2.0, HCE1.0, HCE1.5, HCE2.0, OT1.0 OT1.5, OT2.0
    // 71, 34, 45, 50, 79, 46, 78, 43, 44
    final static Set<String> earnCodes;
    static {
	earnCodes = new HashSet<>();
	earnCodes.add("34");
	earnCodes.add("43");
	earnCodes.add("44");
	earnCodes.add("45");
	earnCodes.add("46");
	earnCodes.add("50");
	earnCodes.add("71");
	earnCodes.add("78");
	earnCodes.add("79");
    }	
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
    public Map<String,Map<String, Double>> getWeek1EmpCodes(){
	return week1EmpCodes;
    }
    public Map<String,Map<String, Double>> getWeek2EmpCodes(){
	return week2EmpCodes;
    }    
    void getPayPeriod(){
	if(payPeriod == null){
	    if(!pay_period_id.isEmpty()){
		PayPeriod one = new PayPeriod(pay_period_id);
		String back = one.doSelect();
		if(back.isEmpty()){
		    payPeriod = one;
		}
		else{
		    logger.error(back);
		}
	    }
	}
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
	    " c.id AS code_id,"+
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
	    " datediff(t.date, p.start_date) AS days "+ 
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
	    " join code_cross_ref n on n.code_id=c.id ";
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
		String code_id = rs.getString(9);
		int days = rs.getInt(21);
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
						rs.getInt(21),
						isSeasonal
						);
		empNo = rs.getString(12);
		if(!empNumbers.contains(empNo))
		    empNumbers.add(empNo);
		if(!dailyBlocks.contains(one)){
		    dailyBlocks.add(one);
		    if(earnCodes.contains(code_id)){
			if(days < 7){
			    addToHash(week1EmpCodes, one);
			}
			else{
			    addToHash(week2EmpCodes, one);
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
	return msg;
    }
    private void addToHash(Map<String, Map<String, Double>> hash, DailyBlock block){
	String empNum = block.getEmpNumber();
	String code_id = block.getCode_id();
	double hours = block.getHours();
	if(hash.containsKey(empNum)){
	    Map<String, Double> map = hash.get(empNum);
	    if(map.containsKey(code_id)){
		double dd = map.get(code_id);
		dd = dd + hours;
		map.put(code_id, dd);
		hash.put(empNum, map);
	    }
	    else{
		map.put(code_id, hours);
		hash.put(empNum, map);
	    }
	}
	else{
	    Map<String, Double> map = new TreeMap<>();
	    map.put(code_id, hours);
	    hash.put(empNum, map);
	}
    }
    /**
     * find comp and prof calculated hours
     * from twarp tables
     */
    /**
  select t.run_id,t.hour_code_id,c.name,t.hours,t.amount from  tmwrp_blocks t join hour_codes c on t.hour_code_id=c.id limit 50;
     */
    public String find2(){
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="";
	String week1_end_date = "";
	String week2_end_date = "";
	if(payPeriod == null){
	    getPayPeriod();
	}
	if(payPeriod != null){
	    week1_end_date = payPeriod.getWeek1EndDate();
	    week2_end_date = payPeriod.getEndDate();
	}
	String qq = " select "+
	    " t.id AS block_id, "+
	    " r.document_id AS document_id, "+
	    " j.group_id AS group_id,"+
	    " g.department_id AS department_id,"+
	    " d.pay_period_id AS pay_period_id,"+
	    " j.salary_group_id AS salary_group_id,"+
	    " p2.name AS job_title,"+
	    " c.name AS earn_code, "+
	    " c.id AS code_id,"+
	    " concat_ws(' ',e.first_name,e.last_name) AS full_name,"+
	    " e.id AS employee_id,"+
	    " e.employee_number AS empnum,"+	    	    
	    " if(t.term_type = 'Week 1','"+week1_end_date+"','"+week1_end_date+"') AS time_date, "+ 
	    " d2.name AS department_name,"+
	    " g.name AS group_name, "+
	    " ' ' AS notes,"+
	    " n.nw_code AS nw_code,"+
	    " n.gl_string AS gl_string, "+
	    " t.hours AS hours, "+
	    " t.amount AS amount, "+
	    " if(t.term_type = 'Week 1',1,2) AS days  "+
	    " from tmwrp_blocks t join tmwrp_runs r on r.id=t.run_id "+
	    " join hour_codes c on t.hour_code_id=c.id "+
	    " join time_documents d on d.id=r.document_id "+
	    " join pay_periods p on p.id=d.pay_period_id "+
	    " join jobs j on d.job_id=j.id "+
	    " join positions p2 on j.position_id=p2.id "+
	    " join employees e on j.employee_id=e.id "+
	    " join salary_groups s on j.salary_group_id=s.id "+
	    " join groups g on j.group_id=g.id "+
	    " join departments d2 on g.department_id=d2.id "+
	    " join code_cross_ref n on n.code_id=c.id ";
	String qw = " where (t.hours > 0 or t.amount > 0) and "+
	    " c.id in (34,43,45,46,50,71,78,79,109) and "+
	    " d.pay_period_id=? ";
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
						rs.getDouble(19),
						rs.getDouble(20),
						rs.getInt(21),
						isSeasonal
						);
		empNo = rs.getString(12);
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
/**
	 select 
	     t.id AS block_id, 
	     r.document_id AS document_id, 
	     j.group_id AS group_id,
	     g.department_id AS department_id,
	     d.pay_period_id AS pay_period_id,
	     j.salary_group_id AS salary_group_id,
	     p2.name AS job_title,
	     c.name AS earn_code, 
	     ' ' AS earn_code_reason,
	     concat_ws(' ',e.first_name,e.last_name) AS full_name,
	     e.id AS employee_id,
	     e.employee_number AS empnum,	    	    
	     if(t.term_type = 'Week 1','01/11/2026','01/18/2026') AS time_date, 
	     d2.name AS department_name,
	     g.name AS group_name, 
	     ' ' AS notes,
	     n.nw_code AS nw_code,
	     n.gl_string AS gl_string, 
	     t.hours AS hours, 
	     t.amount AS amount, 
	     s.name 
	     from tmwrp_blocks t join tmwrp_runs r on r.id=t.run_id 
	     join hour_codes c on t.hour_code_id=c.id 
	     join time_documents d on d.id=r.document_id 
	     join pay_periods p on p.id=d.pay_period_id 
	     join jobs j on d.job_id=j.id 
	     join positions p2 on j.position_id=p2.id 
	     join employees e on j.employee_id=e.id 
	     join salary_groups s on j.salary_group_id=s.id 
	     join groups g on j.group_id=g.id 
	     join departments d2 on g.department_id=d2.id 
	     join code_cross_ref n on n.code_id=c.id 
	where (t.hours > 0 or t.amount > 0) and 
	     c.id in (71,34,45,109,50,79,46) and
	     d.pay_period_id=731 and d2.id=1

*/
	    
