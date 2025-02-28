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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;

public class TimeBlockLogList{

    static final long serialVersionUID = 3700L;
    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
    static Logger logger = LogManager.getLogger(TimeBlockLogList.class);
    String document_id = "", id="", sortBy=" l.id desc ";
    String employee_id = "", start_date="", end_date="";
    String pay_period_id = "";
    List<TimeBlockLog> timeBlockLogs = null;
    public TimeBlockLogList(){
    }
    public TimeBlockLogList(String val){
	setDocument_id(val);
    }
    public void setId (String val){
	if(val != null)
	    id = val;
    }
    public void setPay_period_id (String val){
	if(val != null)
	    pay_period_id = val;
    }		    
    public void setDocument_id (String val){
	if(val != null)
	    document_id = val;
    }
    public void setEmployee_id (String val){
	if(val != null)
	    employee_id = val;
    }
    public void setStart_date(String val){
	if(val != null)
	    start_date = val;
    }
    public void setEnd_date(String val){
	if(val != null)
	    end_date = val;
    }    
    public void setSortby(String val){
	if(val != null)
	    sortBy = val;
    }
    public List<TimeBlockLog> getTimeBlockLogs(){
	return timeBlockLogs;
    }
    public String doIt(){
	return "";

    }

    //
    //
    public String find(){
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", qw="";
	String qq = "select l.id,l.document_id,l.hour_code_id,l.earn_code_reason_id,date_format(l.date,'%m/%d/%Y'),l.begin_hour,begin_minute,l.end_hour,l.end_minute,l.hours,l.minutes,l.amount,l.clock_in,l.clock_out,l.time_block_id,l.notes,l.action_type,l.action_by_id,date_format(l.action_time,'%m/%d/%y %H:%i'),l.location_id from time_block_logs l";
	qq += " join time_documents d on d.id=l.document_id ";
	if(!document_id.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += "l.document_id = ? ";
	}
	else{
	    if(!employee_id.isEmpty()){
		// qq += " join jobs j on j.id=d.job_id  ";
		if(!qw.isEmpty()) qw += " and ";
		qw += " d.employee_id = ? ";
	    }
	    if(!pay_period_id.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";	    
		qw += " d.pay_period_id=? ";
	    }
	    else {
		if(!start_date.isEmpty()){
		    if(!qw.isEmpty()) qw += " and ";
		    qw += "l.date >= ? ";
		}
		if(!end_date.isEmpty()){
		    if(!qw.isEmpty()) qw += " and ";
		    qw += "l.date <= ? ";
		}
	    }
	}
	if(!qw.isEmpty()){
	    qq += " where "+qw;
	}
	if(!sortBy.isEmpty()){
	    qq += " order by "+sortBy;
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
	    int jj=1;
	    if(!document_id.isEmpty()){
		pstmt.setString(jj++, document_id);
	    }
	    else {
		if(!employee_id.isEmpty()){
		    pstmt.setString(jj++, employee_id);
		}
		if(!pay_period_id.isEmpty()){
		    pstmt.setString(jj++, pay_period_id);
		}
		else{
		    if(!start_date.isEmpty()){
			java.util.Date date_tmp = df.parse(start_date);
			pstmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
		    }
		    if(!end_date.isEmpty()){
			java.util.Date date_tmp = df.parse(end_date);
			pstmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
		    }
		}
	    }
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		if(timeBlockLogs == null)
		    timeBlockLogs = new ArrayList<>();
		TimeBlockLog one =
		    new TimeBlockLog(rs.getString(1),
				     rs.getString(2),
				     rs.getString(3),
				     rs.getString(4),
				     rs.getString(5),
																		 
				     rs.getInt(6),
				     rs.getInt(7),
				     rs.getInt(8),
				     rs.getInt(9),
				     rs.getDouble(10),
																		 
				     rs.getInt(11),
				     rs.getDouble(12),
				     rs.getString(13),
				     rs.getString(14),
				     rs.getString(15), // tb id
				     
				     rs.getString(16), // notes
				     rs.getString(17),
				     rs.getString(18),
				     rs.getString(19),
				     rs.getString(20));
		timeBlockLogs.add(one);
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
       select id,document_id,hour_code_id,earn_code_reason_id,date_format(date,'%m/%d/%Y'),begin_hour,begin_minute,end_hour,end_minute,hours,minutes,amount,clock_in,clock_out,time_block_id,action_type,action_by_id,date_format(action_time,'%m/%d/%y %H:%i') from time_block_logs where action_time >= str_to_date('01/01/2022','%m/%d/%Y') order by id desc                                                         into outfile '/var/lib/mysql-files/timetrack_logs.csv'                          fields terminated by ','                                                        enclosed by '"'                                                                 lines terminated by '\n';

       select dayofweek(action_time) day, hour(action_time) hour,count(*) cnt from time_block_logs where action_time > str_to_date('01/01/2019','%m/%d/%Y') group by day, hour                                                                       	into outfile '/var/lib/mysql-files/timetrack_logs.csv'                          fields terminated by ','                                                        lines terminated by '\n';				
       //
       // starting pay period 12/31/2018 so we added one day
       //
       select (dayofyear(action_time)+1)%14+1 day, hour(action_time) hour,count(*) cnt from time_block_logs where action_time >= str_to_date('01/01/2022','%m/%d/%Y') group by day, hour                
				

       select concat_ws(' ', e.first_name,e.last_name) Employee,h.name HourCode,date_format(l.date,'%m/%d/%Y') Date,l.begin_hour,l.begin_minute,l.end_hour,l.end_minute,l.hours,l.minutes,l.action_type,date_format(l.action_time,'%m/%d/%y %H:%i') ActionTime from time_block_logs l
       	join time_documents d on d.id=l.document_id
	join hour_codes h on l.hour_code_id=h.id
       join jobs j on d.job_id=j.id
       join employees e on j.employee_id=e.id 
       where action_time >= str_to_date('01/01/2024','%m/%d/%Y')
       and action_time <= str_to_date('12/31/2024','%m/%d/%Y')
       and j.group_id in (112,316)
       and l.action_type = 'ClockIn' 
       into outfile '/var/lib/mysql-files/time_util_logs.csv'                          fields terminated by ','                                                        enclosed by '"'                                                                 lines terminated by '\n';
       
    */
}
