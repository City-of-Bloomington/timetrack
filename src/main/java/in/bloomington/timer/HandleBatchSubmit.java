package in.bloomington.timer;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.*;
import java.sql.*;
import java.text.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HandleBatchSubmit{

    boolean debug = false;
    static final long serialVersionUID = 53L;
    static Logger logger = LogManager.getLogger(HandleBatchSubmit.class);
    String admin_id="85"; // from employees table (System user)
    String pay_period_id="", run_by="";
    public HandleBatchSubmit(String val){
	setPay_period_id(val);
    }
    public HandleBatchSubmit(PayPeriod val){
	if(val != null){
	    setPay_period_id(val.getId()); 
	}
    }
    //
    // setters
    //
    public void setRun_by(String val){
	if(val != null){		
	    run_by = val;
	}
    }
    public void setPay_period_id(String val){
	if(val != null && !val.equals("-1")){		
	    pay_period_id = val;
	}
    }		
    //
    // find all the employees who have initiated document time but
    // can not submit for approval
    //
    /**

       select d.id,concat_ws(' ',e.first_name,e.last_name)                                from time_documents d,                                                          employees e,                                                                    jobs j,                                                                         grops g                                                                         where e.id=d.employee_id                                                        and d.job_id=j.id                                                               and g.id=j.group_id                                                             and e.inactive is null                                                          and g.clock_time_required is not null                                           and g.include_in_auto_batch is not null                                         and d.pay_period_id = 571                                                       and exists (select * from time_blocks t where t.document_id=d.id and t.inactive is null and (t.hours > 0 or t.amount > 0))                                      and not exists (select * from time_actions a where a.workflow_id=2 and a.document_id=d.id) 

       
    */
    public String process(){
		
	Connection con = null;
	PreparedStatement pstmt = null, pstmt2=null, pstmt3=null;
	ResultSet rs = null;
	String msg="";
	if(run_by.isEmpty()){
	    run_by = admin_id;
	}
	Map<String, String> map = new HashMap<>();
	//
	// find all clock employees that have documents for the
	// specified pay_period_id but not submitted
	//
	String qq = " select d.id,concat_ws(' ',e.first_name,e.last_name) "+
	    " from time_documents d,"+
	    " employees e,"+
	    " jobs j, "+
	    " groups g "+
	    " where e.id=d.employee_id "+
	    " and e.inactive is null "+
	    " and d.job_id=j.id "+	    
	    " and g.id = j.group_id "+
	    " and g.clock_time_required is not null "+ // punch clock employees
	    " and g.include_in_auto_batch is not null "+
	    " and d.pay_period_id = ? "+
	    " and exists (select * from time_blocks t where t.document_id=d.id and t.inactive is null and (t.hours > 0 or t.amount > 0)) "+
	    " and not exists (select * from time_actions a where a.workflow_id=2 and a.document_id=d.id) ";
	String qq2 = "insert into time_actions values(0,?,?,?,now(),null,null)";
	String qq3 = "insert into batch_logs values(0,?,?,now(),?)";
	String success_list="", failure_list="", error_msg="";
	try{
	    con = UnoConnect.getConnection();
	    if(con == null){
		msg = "Could not connect to DB";
		return msg;
	    }
	    if(debug){
		logger.debug(qq);
	    }

	    String workflow_id="2";
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, pay_period_id);
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		String doc_id = rs.getString(1);
		String name = rs.getString(2);
		map.put(doc_id, name);
	    }
	    if(!map.isEmpty()){
		pstmt2 = con.prepareStatement(qq2);
		pstmt3 = con.prepareStatement(qq3);
		Set<String> docIdSet = map.keySet();
		for(String doc_id:docIdSet){
		    String name = map.get(doc_id);
		    pstmt2.setString(1, workflow_id);
		    pstmt2.setString(2, doc_id);
		    pstmt2.setString(3, run_by);
		    try{
			pstmt2.executeUpdate();
			if(!success_list.isEmpty()) success_list+=", ";
			success_list += doc_id+":"+name;
		    }
		    catch(Exception ex2){
			if(!failure_list.isEmpty()) failure_list+=", ";
			failure_list += doc_id+":"+name;
			msg ="" + ex2;
			if(error_msg.indexOf(msg) == -1){
			    if(!error_msg.isEmpty()) error_msg += ", ";
			    error_msg += msg;
			}
		    }
		}
		if(!success_list.isEmpty()){
		    pstmt3.setString(1, success_list);
		    pstmt3.setString(2, run_by);										
		    pstmt3.setNull(3, Types.VARCHAR);
		    pstmt3.executeUpdate();
		}
		if(!failure_list.isEmpty()){
		    pstmt3.setString(1, failure_list);
		    pstmt3.setString(2, run_by);										
		    pstmt3.setString(3, error_msg);
		    pstmt3.executeUpdate();
		}								
	    }
	}catch(Exception ex){
	    msg += ex;
	    System.err.println(ex);
	}
	finally{
	    Helper.databaseDisconnect(rs, pstmt, pstmt2, pstmt3);
	    UnoConnect.databaseDisconnect(con);
	}
	return msg;
    }

}
