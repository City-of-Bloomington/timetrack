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
import java.text.SimpleDateFormat;
import java.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;

public class DocumentList{

    static final long serialVersionUID = 1600L;
    static Logger logger = LogManager.getLogger(DocumentList.class);
    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");		
    String employee_id = "", department_id="", pay_period_id="",
	date="", job_id="",  id="";
    Set<String> group_id_set = new HashSet<>();
    String group_ids="";// for multiple groups
    int page_size = 0;
    int page_number = 1;
    int total_records = 0;
    List<Document> documents = null;
    public DocumentList(){
    }
    public DocumentList(String val){
	setEmployee_id(val);
    }		
    public void setEmployee_id (String val){
	if(val != null && !val.equals("-1"))
	    employee_id = val;
    }
    public void setPay_period_id (String val){
	if(val != null && !val.equals("-1"))
	    pay_period_id = val;
    }		
    public void setDepartment_id (String val){
	if(val != null && !val.equals("-1"))
	    department_id = val;
    }
    public void setJob_id (String val){
	if(val != null && !val.equals("-1"))
	    job_id = val;
    }
    // needed for clean up
    public void setId (String val){
	if(val != null && !val.equals("-1"))
	    id = val;
    }				
    public void setGroup_id (String val){
	if(val != null && !val.equals("-1")){
	    // group_id = val;
	    if(!group_id_set.contains(val)){
		if(!group_ids.isEmpty())
		    group_ids += ",";
		group_ids += val;
		group_id_set.add(val);
	    }
	}
    }
    public void setDate(String val){
	if(val != null)
	    date = val;
    }		
    public List<Document> getDocuments(){
	return documents;
    }
    public void setPageSize(Integer val){
	if(val != null)
	    page_size = val;
    }
    public void setPageNumber(Integer val){
	if(val != null)
	    page_number = val;
    }
    public int getTotalRecords(){
	return total_records;
    }
    //
    //
    public String find(){
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", str="";
	String qc = "select count(*) from time_documents d, employees e,pay_periods pp,jobs j ";				
	String qq = "select d.id,d.employee_id,d.pay_period_id,d.job_id,date_format(d.initiated,'%m/%d/%Y %H:%i'),d.initiated_by from time_documents d, employees e,pay_periods pp,jobs j ";
	String qw = "d.employee_id=e.id and pp.id=d.pay_period_id and j.id=d.job_id ";				
	boolean periodTbl = false;
	if(!employee_id.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += "d.employee_id=? ";
	}
	if(!pay_period_id.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += "d.pay_period_id=? ";
	}
	if(!job_id.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";						
	    qw += "d.job_id=? ";
	}				
	if(!date.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += "pp.start_date <= ? and pp.end_date >= ?";
	}
	if(!department_id.isEmpty()){
	    qq += ", `groups` g  ";
	    qc += ", `groups` g  ";
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " g.department_id=? and g.id=j.group_id ";
						
	}
	if(!group_ids.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += "j.group_id in ("+group_ids+")";
	}
	if(!qw.isEmpty()){
	    qc += " where "+qw;
	    qq += " where "+qw;
	}
	qq += " order by e.last_name,e.first_name ";
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = " Could not connect to DB ";
	    logger.error(msg);
	    return msg;
	}
	logger.debug(qq);
	try{
	    pstmt = con.prepareStatement(qc);
	    int jj=1;
	    if(!employee_id.isEmpty()){
		pstmt.setString(jj++, employee_id);
	    }
	    if(!pay_period_id.isEmpty()){
		pstmt.setString(jj++, pay_period_id);
	    }
	    if(!job_id.isEmpty()){
		pstmt.setString(jj++, job_id);
	    }						
	    if(!date.isEmpty()){
		java.util.Date date_tmp = df.parse(date);
		pstmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
		pstmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
	    }
	    if(!department_id.isEmpty()){
		pstmt.setString(jj++, department_id);
	    }
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		total_records = rs.getInt(1);
	    }
	    Helper.databaseDisconnect(pstmt, rs);
	    if(total_records > 0){
		if(page_size > 0){
		    int offset = (page_number - 1) * page_size;
		    if(total_records > page_size){
			qq += " limit "+offset+", "+page_size;
		    }
		}
		pstmt = con.prepareStatement(qq);
		jj=1;
		if(!employee_id.isEmpty()){
		    pstmt.setString(jj++, employee_id);
		}
		if(!pay_period_id.isEmpty()){
		    pstmt.setString(jj++, pay_period_id);
		}
		if(!job_id.isEmpty()){
		    pstmt.setString(jj++, job_id);
		}						
		if(!date.isEmpty()){
		    java.util.Date date_tmp = df.parse(date);
		    pstmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
		    pstmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
		}
		if(!department_id.isEmpty()){
		    pstmt.setString(jj++, department_id);
		}
		rs = pstmt.executeQuery();						
		while(rs.next()){
		    if(documents == null)
			documents = new ArrayList<>();
		    Document one = new Document(
						rs.getString(1),
						rs.getString(2),
						rs.getString(3),
						rs.getString(4),
						rs.getString(5),
						rs.getString(6));
		    if(!documents.contains(one))
			documents.add(one);
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
     * needed for cleanup class
     * pay_period_id is not really needed
     */
    public String findForCleanUp(){
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = "select d.id,d.employee_id,d.pay_period_id,d.job_id,"+
	    "date_format(d.initiated,'%m/%d/%Y %H:%i'),d.initiated_by "+
	    "from time_documents d, jobs j,pay_periods p "+
	    "where d.job_id=j.id and j.expire_date is not null "+
	    "and p.id = d.pay_period_id "+
	    "and j.expire_date <= p.start_date "+
	    "and d.pay_period_id >= ? ";
	qq += " order by d.id ";	
	if(!job_id.isEmpty()){
	    qq += " and d.job_id=? ";
	}
	else if(!employee_id.isEmpty()){
	    qq += " and d.employee_id=? ";
	 
	}
	else if(!group_ids.isEmpty()){
	    qq += " and j.group_id in ("+group_ids+")";
	}
	else{
	    msg = "employee, job, or group not set";
	}
	if(pay_period_id.isEmpty()){
	    if(!msg.isEmpty()) msg += ", ";
	    msg += "pay period not set";
	}
	if(!msg.isEmpty()){
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
	// System.err.println(qq);
	try{
	    pstmt = con.prepareStatement(qq);
	    int jj=1;
	    pstmt.setString(jj++, pay_period_id);
	    if(!job_id.isEmpty()){
		pstmt.setString(jj++, job_id);
	    }
	    else if(!employee_id.isEmpty()){
		pstmt.setString(jj++, employee_id);
	    }
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		if(documents == null)
		    documents = new ArrayList<>();
		Document one = new Document(
					    rs.getString(1),
					    rs.getString(2),
					    rs.getString(3),
					    rs.getString(4),
					    rs.getString(5),
					    rs.getString(6));
		if(!documents.contains(one))
		    documents.add(one);
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
    public String findForGroupCleanUp(){
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = "select d.id,d.employee_id,d.pay_period_id,d.job_id,"+
	    "date_format(d.initiated,'%m/%d/%Y %H:%i'),d.initiated_by "+
	    "from time_documents d, jobs j,pay_periods p "+
	    "where d.job_id=j.id and j.expire_date is not null "+
	    "and p.id = d.pay_period_id "+
	    "and j.expire_date <= p.start_date "+
	    "and d.pay_period_id >= ? ";
	qq += " order by d.id ";	
	if(!group_ids.isEmpty()){
	    qq += " and j.group_id in ("+group_ids+")";
	 
	}
	else{
	    msg = "group not set";
	}
	if(pay_period_id.isEmpty()){
	    if(!msg.isEmpty()) msg += ", ";
	    msg += "pay period not set";
	}
	if(!msg.isEmpty()){
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
	// System.err.println(qq);
	try{
	    pstmt = con.prepareStatement(qq);
	    int jj=1;
	    pstmt.setString(jj++, pay_period_id);
	    pstmt.setString(jj++, group_id);
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		if(documents == null)
		    documents = new ArrayList<>();
		Document one = new Document(
					    rs.getString(1),
					    rs.getString(2),
					    rs.getString(3),
					    rs.getString(4),
					    rs.getString(5),
					    rs.getString(6));
		if(!documents.contains(one))
		    documents.add(one);
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
    */
}
