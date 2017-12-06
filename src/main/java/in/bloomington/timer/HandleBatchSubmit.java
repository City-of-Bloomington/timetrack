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
		String date="", dept_id="", pay_period_id="";
		List<User> emps = null;
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
    public void setDept_id(String val){
				if(val != null){		
						dept_id = val;
				}
    }
    public void setPay_period_id(String val){
				if(val != null && !val.equals("-1")){		
						pay_period_id = val;
				}
    }		
    public void setDate(String val){
				if(val != null){		
						date = val;
				}
    }
		//
		// find all the employees who have initiated document time but
		// can not submit for approval
		//
    String process(){
		
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="";
				//
				// find all non clock employees that have documents for the
				// specified pay_period_id but not submitted
				//
				String qq = " select d.id,concat_ws(' ',u.first_name,u.last_name) "+
						" from time_documents d,"+
						" employees e,"+
						" users u,"+
						" jobs j "+
						" where e.id=d.employee_id "+
						" and e.user_id=u.id "+
						" and e.inactive is null "+
						" and u.inactive is null "+
						" and j.employee_id=e.id "+
						" and j.clock_time_required is not null "+ // punch clock employees
						" and j.inactive is null "+
						" and d.pay_period_id = ? "+
						" and d.id not in (select a.document_id from time_actions a,time_documents d2 where a.document_id=d2.id and d2.pay_period_id=? and a.workflow_id=2) "; // initiated but not submitted for approval
				try{
						con = Helper.getConnection();
						if(con == null){
								msg = "Could not connect to DB";
								return msg;
						}
						if(debug){
								logger.debug(qq);
						}
						String admin_id="85"; // from employees table (admin user)
						String workflow_id="2";
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, pay_period_id);
						pstmt.setString(2, pay_period_id);
						rs = pstmt.executeQuery();
						while(rs.next()){
								String doc_id = rs.getString(1);
								String name = rs.getString(2);
								TimeAction one = new TimeAction(workflow_id, doc_id, admin_id);
								String back = one.doSave();
								String status = "Success";
								if(!back.equals("")){
										status = "failure";
										msg += back;
								}
								BatchLog log = new BatchLog(doc_id,name,status);
								msg += log.doSave();
								System.err.println("Submitted document "+doc_id+" for "+name);
						}
				}catch(Exception ex){
						msg += ex;
						System.err.println(ex);
				}
				finally{
						Helper.databaseDisconnect(con, pstmt, rs);
				}
				return msg;
		}

}
