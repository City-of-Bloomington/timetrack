package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.text.DecimalFormat;
import java.sql.*;
import javax.sql.*;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;
import in.bloomington.timer.timewarp.TimewarpManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TmwrpInitiate{

    static Logger logger = LogManager.getLogger(TmwrpInitiate.class);
    static final DecimalFormat df = new DecimalFormat("#0.00");		
    static final long serialVersionUID = 1500L;
		String pay_period_id = "";
		List<String> emps = null;
    public TmwrpInitiate(){
    }		
    public TmwrpInitiate(String val){
				setPay_period_id(val);
    }
    //
    // getters
    //
    public String getPay_period_id(){
				
				return pay_period_id;
    }
    //
    // setters
    //
    public void setPay_period_id(String val){
				if(val != null)
						pay_period_id = val;
    }
    public boolean equals(Object o) {
				if (o instanceof TmwrpInitiate) {
						TmwrpInitiate c = (TmwrpInitiate) o;
						if ( this.pay_period_id.equals(c.getPay_period_id())) 
								return true;
				}
				return false;
    }
    public int hashCode(){
				int seed = 37;
				if(!pay_period_id.equals("")){
						try{
								seed += Integer.parseInt(pay_period_id)*31;
						}catch(Exception ex){
								// we ignore
						}
				}
				return seed;
    }
    public String toString(){
				return pay_period_id;
    }
		public List<String> getEmps(){
				return emps;
		}
		/**
		 * get the list of documents ID's that have time entries but not
		 * tmwrpRun enteries
		 select distinct(d.id),e.id,concat_ws(' ',e.first_name,e.last_name) full_name from time_documents d, time_blocks t,employees e where t.document_id=d.id and e.id=d.employee_id and d.pay_period_id=555 and d.id not in (select document_id from tmwrp_runs) and t.inactive is null and t.inactive is null and ((t.clock_in is null and t.clock_out is null) or (t.clock_in is not null and t.clock_out is not null)) ";
		 
		 */
		public String doProcess(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				Set<String> docIdSet = null;
				String msg="", str="";
				String qq = "select distinct(d.id),e.id,concat_ws(' ',e.first_name,e.last_name) full_name from time_documents d, time_blocks t,employees e where t.document_id=d.id and e.id=d.employee_id and d.pay_period_id=? and d.id not in (select document_id from tmwrp_runs) and t.inactive is null and ((t.clock_in is null and t.clock_out is null) or (t.clock_in is not null and t.clock_out is not null)) ";
				logger.debug(qq);
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Unable to connect to DB ";
						return msg;
				}
				emps = new ArrayList<>();
				docIdSet = new HashSet<>();
				try{
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, pay_period_id);
						rs = pstmt.executeQuery();
						while(rs.next()){
								String doc_id = rs.getString(1);
								String emp_id = rs.getString(2);
								String emp_name = rs.getString(3);
								docIdSet.add(doc_id);
								emps.add(emp_name);
								System.err.println(doc_id+" "+emp_name);
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
				if(docIdSet != null && !docIdSet.isEmpty()){
						TimewarpManager manager = null;
						for(String id:docIdSet){
								manager = new TimewarpManager();
								manager.setDocument_id(id);
								String back = manager.doProcess();
								if(!back.equals("")){
										if(msg.indexOf(back) == -1){
												if(!msg.equals("")) msg += ", ";
												msg += back;												
										}
								}
								else{
										System.err.println(" updated doc "+id);
								}
						}
				}
				else{
						msg = "No match found";
				}
				return msg;

		}

}