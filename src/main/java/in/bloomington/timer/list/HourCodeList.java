package in.bloomington.timer.list;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.ArrayList;
import java.util.List;
import java.text.*;
import java.util.Date;

import java.sql.*;
import java.text.SimpleDateFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;

public class HourCodeList{

    static final long serialVersionUID = 1000L;
    static Logger logger = LogManager.getLogger(HourCodeList.class);
    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
    List<HourCode> hourCodes = null;
    String department_id = "", salary_group_id="", effective_date_before="";
    String employee_id = "", accrual_id="", group_id="";
    boolean active_only = false , default_regular_only = false;
    boolean include_holidays = false, include_monetary=false;
    boolean current_only = false, related_to_accruals_only=false;
    String type="", name="", record_method="";
    boolean allEarnTypes = false;
    public HourCodeList(){
    }
    public HourCodeList(String val, String val2){
	setDepartment_id(val);
	setSalary_group_id(val2);
    }		
    public void setDepartment_id(String val){
	if(val != null && !val.equals("-1"))
	    department_id = val;
    }
    public void setSalary_group_id(String val){
	if(val != null && !val.equals("-1"))
	    salary_group_id = val;
    }
    public void setEffective_date_before(String val){
	if(val != null)
	    effective_date_before = val;
    }
    public void setEmployee_id(String val){
	if(val != null && !val.equals("-1"))
	    employee_id = val;
    }
    public void setAccrual_id(String val){
	if(val != null && !val.equals("-1"))
	    accrual_id = val;
    }
    public void setGroup_id(String val){
	if(val != null && !val.equals("-1"))
	    group_id = val;
    }
    public void setType(String val){
	if(val != null && !val.equals("-1"))
	    type = val;
    }
    public void setName(String val){
	if(val != null)
	    name = val;
    }
    public void setRecord_method(String val){
	if(val != null)
	    record_method = val;
    }		
    public void setEarnTypesOnly(){
	allEarnTypes = true;
    }		
    public void setActiveOnly(){
	active_only = true;
    }
    public void setCurrentOnly(){
	current_only = true;
    }
    public void relatedToAccrualsOnly(){
	related_to_accruals_only = true;
    }
    public void setDefaultRegularOnly(){
	default_regular_only = true; // needed for salary groups
    }
    public void setIncludeHolidays(){
	include_holidays=true;
    }
    public void setIncludeMonetary(){
	include_monetary=true;
    }    
    public List<HourCode> getHourCodes(){
	return hourCodes;
    }
    //
    // for user listing
    //
    public String lookFor(){
	String back = "";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection con = null;
	if(employee_id.isEmpty() && (department_id.isEmpty() || salary_group_id.isEmpty())){
	    back = " employee not set or salary group not set";
	}
	if(employee_id.isEmpty()){
	    if(department_id.isEmpty()){
		back = " department not set ";
		return back;
	    }
	    if(salary_group_id.isEmpty()){
		back = " salary grop not set ";
		return back;
	    }
	}
	//
	// some hour codes are specific to certain departments
	// other are for non-specified department
	// 
	String qq = "select count(*) from hour_code_conditions c where c.department_id=? ";
	String qq2 = "select e.id,e.name,e.description,e.record_method,e.accrual_id,e.reg_default,e.type,e.default_monetary_amount,e.earn_factor,e.holiday_related,e.inactive from hour_codes e left join hour_code_conditions c on c.hour_code_id=e.id ";
	String qw = "", msg="";
	logger.debug(qq);
	boolean setDept = false;
	con = UnoConnect.getConnection();
	if(con == null){
	    back = " Could not connect to DB ";
	    return back;
	}				
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, department_id);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		int cnt = rs.getInt(1);
		if(cnt > 0){
		    setDept = true;
		}
	    }
	    if(current_only){
		if(!qw.isEmpty()) qw += " and "; 
		qw += " e.inactive is null ";
	    }
	    else if(!effective_date_before.isEmpty()){
		if(!qw.isEmpty()) qw += " and "; 
		qw += " c.date <= ? ";
	    }
	    if(!employee_id.isEmpty()){
		qq +=" join jobs j on j.salary_group_id=c.salary_group_id ";
		qq += " join department_employees de on de.employee_id=j.employee_id and c.department_id=de.department_id ";
		if(!qw.isEmpty()) qw += " and "; 
		qw += " j.employee_id = ? ";
		qw += " and (j.group_id = c.group_id or c.grou_id is null)";
	    }
	    else{								
		if(!department_id.isEmpty()){
		    if(!qw.isEmpty()) qw += " and "; 
		    qw += " (c.department_id = ? or c.department_id is null)";
		}
		if(!salary_group_id.isEmpty()){
		    if(!qw.isEmpty()) qw += " and "; 
		    qw += " c.salary_group_id = ? ";
		}
		if(!group_id.isEmpty()){
		    if(!qw.isEmpty()) qw += " and "; 
		    qw += " (c.group_id = ? or c.group_id is null)";
		}								
	    }
	    if(active_only){
		if(!qw.isEmpty()) qw += " and "; 								
		qw += " e.inactive is null "; 
	    }						
	    if(!qw.isEmpty()){
		qw = " where "+qw;
	    }
	    qw += " order by e.name";
	    qq = qq2+qw;
	    logger.debug(qq);								
	    pstmt = con.prepareStatement(qq);
	    //
	    int jj=1;
	    if(current_only){
	    }
	    else if(!effective_date_before.isEmpty()){
		java.util.Date date_tmp = df.parse(effective_date_before);
		pstmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
	    }
	    if(!employee_id.isEmpty()){
		pstmt.setString(jj++, employee_id);
	    }
	    else{
		if(!department_id.isEmpty()){
		    pstmt.setString(jj++, department_id);
		}
		if(!salary_group_id.isEmpty()){
		    pstmt.setString(jj++, salary_group_id);
		}
		if(!group_id.isEmpty()){
		    pstmt.setString(jj++, group_id);
		}								
	    }
	    rs = pstmt.executeQuery();
	    hourCodes = new ArrayList<>();
	    while(rs.next()){
		HourCode one = new HourCode(rs.getString(1),
					    rs.getString(2),
					    rs.getString(3),
					    rs.getString(4),
					    rs.getString(5),
					    rs.getString(6) != null,
					    rs.getString(7),
					    rs.getDouble(8),
					    rs.getDouble(9),
					    rs.getString(10) != null,
					    rs.getString(11) != null);
		if(one.isRegDefault()){
		    if(!hourCodes.contains(one))
			hourCodes.add(0, one);
		}
		else{
		    if(!hourCodes.contains(one))
			hourCodes.add(one);
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
    //
    // for global listing (setting for example)
    //
    public String find(){
	String back = "";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection con = null;
	//
	// some hour codes are specific to certain departments
	// other are for all department
	// 
	String qq = "select e.id,e.name,e.description,e.record_method,"+
	    " e.accrual_id,e.reg_default,e.type,"+
	    " e.default_monetary_amount,e.earn_factor,e.holiday_related,"+
	    " e.inactive, "+
	    " f.nw_code,f.gl_string,count(g.hour_code_id)"+
	    " from hour_codes e "+
	    " left join code_cross_ref f on f.code_id=e.id "+
	    " left join hour_code_conditions c on c.hour_code_id=e.id "+
	    " left join code_reason_conditions g on g.hour_code_id=e.id  ";
	String qw = "g.inactive is null ", msg="";
	con = UnoConnect.getConnection();
	if(con == null){
	    back = " Could not connect to DB ";
	    return back;
	}							
	try{
	    if(current_only){
		if(!qw.isEmpty()) qw += " and "; 
		qw += " e.inactive is null ";
	    }
	    else if(!effective_date_before.isEmpty()){
		if(!qw.isEmpty()) qw += " and "; 
		qw += " c.date <= ? ";
	    }
	    if(related_to_accruals_only){
		if(!qw.isEmpty()) qw += " and "; 
		qw += " e.accrual_id is not null ";
	    }
	    if(!accrual_id.isEmpty()){
		if(!qw.isEmpty()) qw += " and "; 
		qw += " e.accrual_id = ?  ";
	    }
	    if(!type.isEmpty()){
		if(!qw.isEmpty()) qw += " and "; 
		qw += " e.type = ?  ";
	    }
	    if(!name.isEmpty()){
		if(!qw.isEmpty()) qw += " and "; 
		qw += " e.name = ?  ";
	    }						
	    if(allEarnTypes){
		if(!qw.isEmpty()) qw += " and "; 
		qw += " (e.type = 'Earned' or e.type ='Overtime')  ";
	    }
	    if(!record_method.isEmpty()){
		if(!qw.isEmpty()) qw += " and "; 
		qw += " e.record_method = ?  ";

	    }
	    if(default_regular_only){
		if(!qw.isEmpty()) qw += " and ";
		qw += "(";
		    qw += " e.reg_default is not null";		
		if(include_holidays){
		    qw += " or (e.holiday_related is not null and e.type <> 'Earned') ";
		}
		if(include_monetary){
		    qw += " or e.type='Monetary'";		    
		}
		qw += ")";
	    }
	    if(active_only){
		if(!qw.isEmpty()) qw += " and "; 								
		qw += " e.inactive is null "; 
	    }
	    if(!qw.isEmpty()){
		qw = " where "+qw;
	    }
	    qw += " group by e.id,e.name,e.description,e.record_method, "+
	    " e.accrual_id, e.reg_default,e.type, "+
	    " e.default_monetary_amount,e.earn_factor,e.holiday_related,"+
	    " e.inactive,f.nw_code,f.gl_string ";
	    qw += " order by e.name";
	    qq += qw;
	    logger.debug(qq);
	    pstmt = con.prepareStatement(qq);
	    //
	    int jj=1;
	    if(current_only){
	    }
	    if(!accrual_id.isEmpty()){
		pstmt.setString(jj++, accrual_id);
	    }
	    if(!type.isEmpty()){
		pstmt.setString(jj++, type);
	    }
	    if(!name.isEmpty()){
		pstmt.setString(jj++, name);
	    }
	    if(!record_method.isEmpty()){
		pstmt.setString(jj++, record_method);
	    }						
	    rs = pstmt.executeQuery();
	    hourCodes = new ArrayList<>();
	    while(rs.next()){
		HourCode one = new HourCode(rs.getString(1),
					    rs.getString(2),
					    rs.getString(3),
					    rs.getString(4),
					    rs.getString(5),
					    rs.getString(6) != null,
					    rs.getString(7),
					    rs.getDouble(8),
					    rs.getDouble(9),
					    rs.getString(10) != null,

					    rs.getString(11) != null,
					    rs.getString(12),
					    rs.getString(13)
					    );
		one.setReasonRequired(rs.getInt(14) > 0);
		if(!hourCodes.contains(one))
		    hourCodes.add(one);
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


    public String lookForCommutes(){
	String back = "";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection con = null;
	//
	// some hour codes are specific to certain departments
	// other are for all department
	// 
	String qq = "select e.id,e.name,e.description,e.record_method,"+
	    " e.accrual_id,e.reg_default,e.type,"+
	    " e.default_monetary_amount,e.earn_factor,e.holiday_related,"+
	    " e.inactive, "+
	    " f.nw_code,f.gl_string,count(g.hour_code_id) "+
	    " from hour_codes e "+
	    " left join code_cross_ref f on f.code_id=e.id "+
	    " left join hour_code_conditions c on c.hour_code_id=e.id "+
	    " left join code_reason_conditions g on g.hour_code_id=e.id  ";
	String qw = "", msg="";
	con = UnoConnect.getConnection();
	if(con == null){
	    back = " Could not connect to DB ";
	    return back;
	}							
	try{
	    if(!qw.isEmpty()) qw += " and "; 
	    qw += " e.inactive is null and g.inactive is null ";
	    qw += " and e.name like ? ";
	    if(!qw.isEmpty()){
		qw = " where "+qw;
	    }
	    qw += " group by e.id,e.name,e.description,e.record_method, "+
	    " e.accrual_id, e.reg_default,e.type, "+
	    " e.default_monetary_amount,e.earn_factor,e.holiday_related,"+
	    " e.inactive,f.nw_code,f.gl_string ";	    
	    qw += " order by e.name";
	    qq += qw;
	    logger.debug(qq);
	    pstmt = con.prepareStatement(qq);
	    //
	    int jj=1;
	    pstmt.setString(jj++, "COMMUT%");
	    rs = pstmt.executeQuery();
	    hourCodes = new ArrayList<>();
	    while(rs.next()){
		HourCode one = new HourCode(rs.getString(1),
					    rs.getString(2),
					    rs.getString(3),
					    rs.getString(4),
					    rs.getString(5),
					    rs.getString(6) != null,
					    rs.getString(7),
					    rs.getDouble(8),
					    rs.getDouble(9),
					    rs.getString(10) != null,

					    rs.getString(11) != null,
					    rs.getString(12),
					    rs.getString(13)
					    );
		one.setReasonRequired(rs.getInt(14) > 0);
		if(!hourCodes.contains(one))
		    hourCodes.add(one);
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
    public String findAbbreviatedList(){
	String back = "";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection con = null;
	//
	String qq = "select e.id, concat_ws(':',e.name,e.description) "+
	    " from hour_codes e ";
	String qw = "", msg="";
	con = UnoConnect.getConnection();
	if(con == null){
	    back = " Could not connect to DB ";
	    return back;
	}							
	try{
	    qw += " e.inactive is null ";
	    if(!qw.isEmpty()){
		qw = " where "+qw;
	    }
	    qw += " order by e.name";
	    qq += qw;
	    logger.debug(qq);
	    pstmt = con.prepareStatement(qq);
	    //
	    rs = pstmt.executeQuery();
	    hourCodes = new ArrayList<>();
	    while(rs.next()){
		HourCode one = new HourCode(rs.getString(1),
					    rs.getString(2)
					    );
		if(!hourCodes.contains(one))
		    hourCodes.add(one);
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
