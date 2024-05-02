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
import java.text.SimpleDateFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;

public class JobTerminateList{

    static Logger logger = LogManager.getLogger(JobTerminateList.class);
    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");	
    static final long serialVersionUID = 3800L;
    String job_id="", group_id="";
    String terminate_id = "";
	
    boolean recent_only = false, set_limit=true;
    List<JobTerminate> jobTerms = null;
	
    public JobTerminateList(){
    }
    public void setJob_id(String val){
	if(val != null)
	    job_id =  val;
    }
    public void setTerminate_id(String val){
	if(val != null)
	    terminate_id =  val;
    }    
    public String getJob_id(){
	return job_id ;
    }
    public String getTerminate_id(){
	return terminate_id ;
    }    
    public List<JobTerminate> getJobTerms(){
	return jobTerms;
    }

    public String find(){
		
	String back = "";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection con = UnoConnect.getConnection();
	String qq = "select  t.id,"+
	    "t.terminate_id,"+
	    "t.job_id,"+
	    "t.job_grade,"+
	    "t.job_step, "+
	    
	    "t.pay_rate, "+
	    "t.weekly_hours,"+
	    "t.supervisor_id, "+
	    "t.supervisor_phone,"+
	    "date_format(t.start_date,'%m/%d/%Y'),"+ // date
	    
	    "date_format(t.last_day_of_work,'%m/%d/%Y'),"+ // date
	    " t.badge_code,"+
	    " t.badge_returned, "+
	    " t.nw_job_title,"+
	    " concat_ws(' ',e.first_name,e.last_name) employee_name,"+
	    
	    " concat_ws(' ',e2.first_name,e2.last_name) supervisor_name,"+
	    " j.group_id group_id,"+
	    " g.name group_name,"+
	    " p.name job_title "+
	    
	    " from job_terminations t "+
	    " join jobs j on j.id=t.job_id "+
	    " join `groups` g on j.group_id = g.id "+
	    " join positions p on p.id = j.position_id "+
	    " left join employees e on e.id=j.employee_id "+	    
	    " left join employees e2 on e2.id=t.supervisor_id ";
	if(con == null){
	    back = "Could not connect to DB";
	    return back;
	}
	String qw = "";
	try{
	    if(!terminate_id.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";
		qw += " t.terminate_id = ? ";		
	    }	    	    
	    if(!job_id.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";
		qw += " t.job_id = ? ";		
	    }
	    if(!group_id.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";
		qw += " j.group_id = ? ";		
	    }	    

	    if(!qw.isEmpty()){
		qq += " where "+qw;
	    }
	    qq += " order by t.id desc ";
	    if(set_limit)
		qq += " limit 10 ";
	    logger.debug(qq);
	    pstmt = con.prepareStatement(qq);
	    int jj=1;
	    if(!terminate_id.isEmpty()){
		pstmt.setString(jj++, terminate_id);
	    }
	    if(!job_id.isEmpty()){
		pstmt.setString(jj++, job_id);
	    }
	    if(!group_id.isEmpty()){
		pstmt.setString(jj++, group_id);
	    }	    
	    rs = pstmt.executeQuery();
	    if(jobTerms == null)
		jobTerms = new ArrayList<>();
	    while(rs.next()){
		JobTerminate one =
		    new JobTerminate(
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
				     rs.getString(19)
				     );
		if(!jobTerms.contains(one))
		    jobTerms.add(one);
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

/**
            select  
	    t.id,t.terminate_id,t.job_id,t.job_grade,
	    t.job_step, t.pay_rate, t.supervisor_id, t.supervisor_phone,
	    date_format(t.start_date,'%m/%d/%Y'), 
	    
	    date_format(t.last_day_of_work,'%m/%d/%Y'), 
	    t.badge_code,t.badge_returned,
	    t.nw_job_title, 
	     concat_ws(' ',e.first_name,e.last_name) employee_name,
	     concat_ws(' ',e2.first_name,e2.last_name) supervisor_name,
	     j.group_id group_id,
	     g.name group_name,
	     p.name job_title 
	    
	     from job_terminations t 
	     join jobs j on j.id=t.job_id 
	     join `groups` g on j.group_id = g.id 
	     join positions p on p.id = j.position_id 
	     left join employees e on e.id=j.employee_id  	    
	     left join employees e2 on e2.id=t.supervisor_id ;





 */




















































