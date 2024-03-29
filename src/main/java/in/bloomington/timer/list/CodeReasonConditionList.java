package in.bloomington.timer.list;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Hashtable;
import java.text.*;
import java.util.Date;
import java.sql.*;
import javax.naming.*;
import javax.naming.directory.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;

public class CodeReasonConditionList{

    static boolean debug = false;
    static final long serialVersionUID = 900L;
    static Logger logger = LogManager.getLogger(CodeReasonConditionList.class);
		
    List<CodeReasonCondition> conditions = null;
    List<EarnCodeReason> reasons = null;		
    boolean active_only = false;
    String department_id="", salary_group_id="", hour_code_id="", group_id="",
	reason_id="";
    public CodeReasonConditionList(){
    }
    public List<CodeReasonCondition> getConditions(){
	return conditions;
    }
    public List<EarnCodeReason> getReasons(){
	return reasons;
    }
    public void setActiveOnly(){
	active_only = true;
    }
    public void setHour_code_id (String val){
	if(val != null && !val.equals("-1"))
	    hour_code_id = val;
    }
    public void setDepartment_id(String val){
	if(val != null && !val.equals("-1"))
	    department_id = val;
    }
    public void setSalary_group_id(String val){
	if(val != null && !val.equals("-1"))
	    salary_group_id = val;
    }
    public void setGroup_id(String val){
	if(val != null && !val.equals("-1"))
	    group_id = val;
    }
    public void setReason_id(String val){
	if(val != null && !val.equals("-1"))
	    reason_id = val;
    }
    //
    public String find(){
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", qw="";
	String qq = "select g.id,g.reason_id,g.hour_code_id,"+
	    " g.salary_group_id,g.department_id,g.group_id,g.inactive,"+
	    " r.name,r.description,r.reason_category_id,r.inactive "+
	    " from code_reason_conditions g "+
	    " join hour_codes e on e.id=g.hour_code_id "+
	    " join earn_code_reasons r on r.id=g.reason_id ";
	logger.debug(qq);
	if(active_only){
	    qw += " g.inactive is null ";
	}
	if(!department_id.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " (g.department_id = ? or g.department_id is null)";
	}
	if(!salary_group_id.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " (g.salary_group_id = ? or g.salary_group_id is null)";
	}
	if(!group_id.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " (g.group_id = ? or g.group_id is null)";
	}
	if(!hour_code_id.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " g.hour_code_id = ?";
	}
	if(!reason_id.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " g.reason_id = ?";
	}				
	if(!qw.isEmpty()){
	    qq += " where "+qw;
	}
	qq += " order by r.name ";
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = " Could not connect to DB ";
	    logger.error(msg);
	    return msg;
	}
	logger.debug(qq);
	try{
	    pstmt = con.prepareStatement(qq);
	    int jj=1;
	    if(!department_id.isEmpty()){
		pstmt.setString(jj++, department_id);
	    }
	    if(!salary_group_id.isEmpty()){
		pstmt.setString(jj++, salary_group_id);								
	    }
	    if(!group_id.isEmpty()){
		pstmt.setString(jj++, group_id);								
	    }
	    if(!hour_code_id.isEmpty()){
		pstmt.setString(jj++, hour_code_id);								
	    }
	    if(!reason_id.isEmpty()){
		pstmt.setString(jj++, reason_id);								
	    }						
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		if(conditions == null)
		    conditions = new ArrayList<>();
		if(reasons == null)
		    reasons = new ArrayList<>();
		CodeReasonCondition one =
		    new CodeReasonCondition(
					    rs.getString(1),
					    rs.getString(2),
					    rs.getString(3),
					    rs.getString(4),
					    rs.getString(5),
					    rs.getString(6),
					    rs.getString(7) != null);
		//
		EarnCodeReason two =
		    new EarnCodeReason(debug,
				       rs.getString(2),
				       rs.getString(8),
				       rs.getString(9),
				       rs.getString(10),
				       rs.getString(11) != null
				       );
		one.setEarnCodeReason(two);
		if(!conditions.contains(one))
		    conditions.add(one);
		if(!reasons.contains(two))
		    reasons.add(two);
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
     * we want the code reasons only
     */
    public String lookFor(){
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", qw="";
	String qq = "select "+
	    " r.id,r.name,r.description,r.reason_category_id,r.inactive "+
	    " from code_reason_conditions g "+
	    " join hour_codes e on e.id=g.hour_code_id "+
	    " join earn_code_reasons r on r.id=g.reason_id ";
	logger.debug(qq);
	if(active_only){
	    qw += " g.inactive is null ";
	}
	if(!department_id.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " (g.department_id = ? or g.department_id is null)";
	}
	if(!salary_group_id.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " (g.salary_group_id = ? or g.salary_group_id is null)";
	}
	if(!group_id.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " (g.group_id = ? or g.group_id is null)";
	}
	if(!hour_code_id.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " g.hour_code_id = ?";
	}
	if(!reason_id.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " g.reason_id = ?";
	}				
	if(!qw.isEmpty()){
	    qq += " where "+qw;
	}
	qq += " order by r.name ";
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = " Could not connect to DB ";
	    logger.error(msg);
	    return msg;
	}
	logger.debug(qq);
	try{
	    pstmt = con.prepareStatement(qq);
	    int jj=1;
	    if(!department_id.isEmpty()){
		pstmt.setString(jj++, department_id);
	    }
	    if(!salary_group_id.isEmpty()){
		pstmt.setString(jj++, salary_group_id);								
	    }
	    if(!group_id.isEmpty()){
		pstmt.setString(jj++, group_id);								
	    }
	    if(!hour_code_id.isEmpty()){
		pstmt.setString(jj++, hour_code_id);								
	    }
	    if(!reason_id.isEmpty()){
		pstmt.setString(jj++, reason_id);								
	    }						
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		if(reasons == null)
		    reasons = new ArrayList<>();
		EarnCodeReason one =
		    new EarnCodeReason(debug,
				       rs.getString(1),
				       rs.getString(2),
				       rs.getString(3),
				       rs.getString(4),
				       rs.getString(5) != null
				       );
		if(!reasons.contains(one))
		    reasons.add(one);
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
