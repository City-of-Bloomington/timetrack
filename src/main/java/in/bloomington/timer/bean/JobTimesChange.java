package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.sql.*;
import javax.sql.*;
import java.text.SimpleDateFormat;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;
import in.bloomington.timer.timewarp.TimewarpManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JobTimesChange implements Serializable{

    static Logger logger = LogManager.getLogger(JobTimesChange.class);
    static final long serialVersionUID = 2400L;
    private String employee_id="", to_job_id="", from_job_id="";
    String pay_period_id="";
    Employee employee = null;
    public JobTimesChange(){

		}
    //
    // getters
    //
    public String getTo_job_id(){
				return to_job_id;
    }
    public String getEmployee_id(){
				return employee_id;
    }
    public String getFrom_job_id(){
				return from_job_id;
    }
    public String getPay_period_id(){
				return pay_period_id;
    }		
    //
    // setters
    //
    public void setTo_job_id (String val){
				if(val != null && !val.equals("-1"))
						to_job_id = val;
    }
    public void setFrom_job_id (String val){
				if(val != null && !val.equals("-1"))
						from_job_id = val;
    }		
    public void setEmployee_id(String val){
				if(val != null && !val.equals("-1"))
						employee_id = val;
    }
    public void setPay_period_id(String val){
				if(val != null && !val.equals("-1"))
						pay_period_id = val;
    }		
    public String toString(){
				String str = employee_id+" "+to_job_id+" "+from_job_id;
				return str;
    }
    //
    // we can update expire date and inactive
		//
    public String doChange(){
				Connection con = null;
				PreparedStatement pstmt = null, pstmt2=null, pstmt3=null,
						pstmt4=null, pstmt5=null, pstmt6=null, pstmt7=null;
				ResultSet rs = null;
				String msg="";
				String qq = " update time_blocks set document_id=? where document_id=?";
				String qq2 = " update time_block_logs set document_id=? where document_id=?";
				String qq3 = " delete from time_actions where document_id=? ";
				String qq4 = " select id from tmwrp_runs where document_id=? ";
				String qq5 = " delete from tmwrp_blocks where run_id =? ";
				String qq6 = " delete from tmwrp_runs where id=? ";
				String qq7 = " delete from time_documents where id=? ";				
				if(employee_id.equals("")){
						msg = " Employee not set ";
						return msg;
				}
				if(to_job_id.equals("")){
						msg = " To job not set ";
						return msg;
				}
				if(from_job_id.equals("")){
						msg = " From job not set ";
						return msg;
				}
				if(pay_period_id.equals("")){
						msg = " Pay period not set ";
						return msg;
				}
				Document toDocument = null, fromDocument = null;
				DocumentList dl = new DocumentList(employee_id);
				dl.setPay_period_id(pay_period_id);
				dl.setJob_id(from_job_id);
				msg = dl.find();
				if(msg.equals("")){
						List<Document> ones = dl.getDocuments();
						if(ones != null && ones.size() > 0){
								fromDocument = ones.get(0);
						}
						else{
								msg = " No document found, no need to change times ";
								return msg;
						}
				}
				else{
						return msg;
				}
				dl = new DocumentList(employee_id);
				dl.setPay_period_id(pay_period_id);
				dl.setJob_id(to_job_id);
				msg = dl.find();
				if(msg.equals("")){
						List<Document> ones = dl.getDocuments();
						if(ones != null && ones.size() > 0){
								toDocument = ones.get(0);
						}
				}
				else{
						return msg;
				}
				// if no new document was created then we just change the job
				// on the old document to the new job and the times follows
				if(fromDocument != null && toDocument == null){
						fromDocument.setJob_id(to_job_id);
						msg = fromDocument.doUpdateJob();
						if(!msg.equals("")){
								return msg;
						}
				}
				//
				// if no new document no further action
				if(toDocument == null){
						return msg;
				}
				// if a new document was create with the new job and times added
				con = UnoConnect.getConnection();
				if(con == null){
						msg = " Could not connect to DB ";
						logger.error(msg);
						return msg;
				}
				// time blocks
				logger.debug(qq);
				try{
						// set new time blocks to the old document
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, toDocument.getId());
						pstmt.setString(2, fromDocument.getId());
						pstmt.executeUpdate();
						//
						// set time block logs to the old document
						logger.debug(qq2);
						qq = qq2;
						pstmt2 = con.prepareStatement(qq2);
						pstmt2.setString(1, toDocument.getId());
						pstmt2.setString(2, fromDocument.getId());
						pstmt2.executeUpdate();						
						//
						// delete time actions
						logger.debug(qq3);
						qq = qq3;
						pstmt3 = con.prepareStatement(qq3);
						pstmt3.setString(1, fromDocument.getId());
						pstmt3.executeUpdate();
						//
						// find run_id
						logger.debug(qq4);
						qq = qq4;
						pstmt4 = con.prepareStatement(qq4);
						pstmt4.setString(1, fromDocument.getId());
						rs = pstmt4.executeQuery();
						String run_id = "";
						if(rs.next()){
								run_id = rs.getString(1);
						}
						if(!run_id.equals("")){
								logger.debug(qq5);
								qq = qq5;
								pstmt5 = con.prepareStatement(qq5);
								pstmt5.setString(1, run_id);
								pstmt5.executeUpdate();
								logger.debug(qq6);
								qq = qq6;
								pstmt6 = con.prepareStatement(qq6);
								pstmt6.setString(1, run_id);
								pstmt6.executeUpdate();
						}
						qq = qq7;
						pstmt7 = con.prepareStatement(qq6);
						pstmt7.setString(1, fromDocument.getId());
						pstmt7.executeUpdate();
						TimewarpManager manager = new TimewarpManager(toDocument.getId());
						msg = manager.doProcess();
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(rs, pstmt, pstmt2, pstmt3, pstmt4, pstmt5);
						UnoConnect.databaseDisconnect(con);
				}
				return msg;
    }

}