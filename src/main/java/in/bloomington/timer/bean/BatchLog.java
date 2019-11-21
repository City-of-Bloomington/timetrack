package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.sql.*;
import javax.naming.*;
import javax.naming.directory.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;

public class BatchLog{

		static final long serialVersionUID = 3700L;	
		static Logger logger = LogManager.getLogger(BatchLog.class);
    String id="", names="",date="", failure_reason="", run_by="";
		Employee runner = null;
		//
		public BatchLog(String val){
				//
				setId(val);
    }
		// for saving
		public BatchLog(String val,
										String val2,
										String val3
													 ){
				setNames(val);
				setRun_by(val2);
				setFailureReason(val3);
    }				
		public BatchLog(String val,
										String val2,
										String val3,
										String val4,
										String val5
										){
				setId(val);
				setNames(val2);
				setRun_by(val3);
				setDate(val4);
				setFailureReason(val5);
    }		
    //
    // getters
    //
    public String getId(){
				return id;
    }
    public String getRun_by(){
				return run_by;
    }
    public String getNames(){
				return names;
    }		
    public String getDate(){
				return date;
    }
		public String getFailureReason(){
				return failure_reason;
    }
		public String getStatus(){
				String status = "Success";
				if(!failure_reason.equals("")) status = "Failure";
				return status;
		}
    //
    // setters
    //
    public void setId(String val){
				if(val != null)
						id = val;
    }
    public void setRun_by(String val){
				if(val != null)
						run_by = val;
    }
    public void setNames(String val){
				if(val != null)
						names = val;
    }
    public void setFailureReason(String val){
				if(val != null)
						failure_reason = val;
    }
    public void setDate(String val){
				if(val != null)
						date = val;
    }
		public Employee getRunner(){
				if(runner == null && !run_by.equals("")){
						Employee one = new Employee(run_by);
						String back = one.doSelect();
						if(back.equals("")){
								runner = one;
						}
				}
				return runner;
		}		
    public String toString(){
				return names;
    }
		//
		public String doSelect(){
				String back = "";
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String qq = "select id,names,run_by,date_format(date,'%m/%d/%Y %h:%i'),failure_reason from batch_logs where id=?";
				con = UnoConnect.getConnection();
				if(con == null){
						back = "Could not connect to DB";
						return back;
				}
				try{
						logger.debug(qq);
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1,id);
						rs = pstmt.executeQuery();
						if(rs.next()){
								setNames(rs.getString(2));
								setRun_by(rs.getString(3));
								setDate(rs.getString(4));
								setFailureReason(rs.getString(5));
						}
						else{
								back ="Record "+id+" Not found";
						}
				}
				catch(Exception ex){
						back += ex+":"+qq;
						logger.error(back);
				}
				finally{
						Helper.databaseDisconnect(pstmt, rs);
						UnoConnect.databaseDisconnect(con);
				}
				return back;
		}
		public String doSave(){
				Connection con = null;
				PreparedStatement pstmt = null, pstmt2=null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = " insert into batch_logs values(0,?,?,now(),?)";
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could not connect to DB ";
						return msg;
				}				
				try{

						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, names);
						pstmt.setString(2, run_by);
						if(failure_reason.equals(""))
								pstmt.setNull(3, Types.VARCHAR);
						else
								pstmt.setString(3, failure_reason);
						pstmt.executeUpdate();
						Helper.databaseDisconnect(pstmt, rs);
						//
						qq = "select LAST_INSERT_ID()";
						pstmt2 = con.prepareStatement(qq);
						rs = pstmt2.executeQuery();
						if(rs.next()){
								id = rs.getString(1);
						}
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(rs, pstmt, pstmt2);
						UnoConnect.databaseDisconnect(con);
				}
				return msg;
		}

}
