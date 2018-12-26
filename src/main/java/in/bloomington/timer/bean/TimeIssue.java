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

public class TimeIssue{

		static final long serialVersionUID = 3700L;	
		static Logger logger = LogManager.getLogger(TimeNote.class);
    String id="", time_block_id="", reported_by="", date="", issue_notes="";
		String status="", closed_date="", closed_by = "";
		Employee reporter=null, closed_by_emp = null;
		//
		public TimeIssue(){

		}
		public TimeIssue(String val){
				//
				setId(val);
    }		
		public TimeIssue(String val, String val2, String val3, String val4, String val5, String val6, String val7, String val8){
				setVals(val,val2, val3, val4, val5, val6, val7, val8);
		}
		private void setVals(String val, String val2, String val3, String val4, String val5, String val6, String val7, String val8){
				setId(val);
				setTime_block_id(val2);
				setReported_by(val3);
				setDate(val4);
				setIssue_notes(val5);
				setStatus(val6);
				setClosed_date(val7);
				setClosed_by(val8);
    }		
		public boolean equals(Object obj){
				if(obj instanceof TimeIssue){
						TimeIssue one =(TimeIssue)obj;
						return id.equals(one.getId());
				}
				return false;				
		}
		public int hashCode(){
				int seed = 23;
				if(!id.equals("")){
						try{
								seed += Integer.parseInt(id);
						}catch(Exception ex){
						}
				}
				return seed;
		}
    //
    // getters
    //
    public String getId(){
				return id;
    }
    public String getTime_block_id(){
				return time_block_id;
    }
    public String getReported_by(){
				return reported_by;
    }		
		public String getDate(){
				return date;
		}
		public String getIssue_notes(){
				return issue_notes;
		}
		public String getStatus(){
				return status;
		}
		public String getClosed_date(){
				return closed_date;
		}
		public String getClosed_by(){
				return closed_by;
		}
		public boolean isOpen(){
				return status.equals("Open");
		}		
		public boolean isClosed(){
				return status.equals("Closed");
		}
		public Employee getReporter(){
				if(reporter == null && !reported_by.equals("")){
						Employee one = new Employee(reported_by);
						String back = one.doSelect();
						if(back.equals("")){
								reporter = one;
						}
				}
				return reporter;
		}
		public Employee getClosed_by_emp(){
				if(closed_by_emp == null && !closed_by.equals("")){
						Employee one = new Employee(closed_by);
						String back = one.doSelect();
						if(back.equals("")){
								closed_by_emp = one;
						}
				}
				return closed_by_emp;
		}		
    //
    // setters
    //
    public void setId(String val){
				if(val != null)
						id = val;
    }
    public void setTime_block_id(String val){
				if(val != null)
						time_block_id = val;
    }
    public void setReported_by(String val){
				if(val != null)
						reported_by = val;
    }		
    public void setIssue_notes(String val){
				if(val != null)
						issue_notes = val.trim();
    }
    public void setDate(String val){
				if(val != null)
						date = val;
    }
    public void setStatus(String val){
				if(val != null)
						status = val;
    }
    public void setClosed_date(String val){
				if(val != null)
						closed_date = val;
    }
    public void setClosed_by(String val){
				if(val != null)
						closed_by = val;
    }		
		//
		public String doSelect(){
				String back = "";
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String qq = "select id,time_block_id,reported_by,date_format(date,'%m/%d/%Y %H:%i'),issue_notes,status,date_format(closed_date,'%m/%d/%Y %H:%i'),closed_by from time_issues where id=?";
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
								setVals(rs.getString(1),
												rs.getString(2),
												rs.getString(3),
												rs.getString(4),
												rs.getString(5),
												rs.getString(6),
												rs.getString(7),
												rs.getString(8));
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
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = " insert into time_issues values(0,?,?,now(),?,'Open',null,null)";
				if(issue_notes.equals("")){
						msg = "notes are required";
						return msg;
				}
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could not connect to DB ";
						return msg;
				}
				try{
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, time_block_id);
						pstmt.setString(2, reported_by);
						pstmt.setString(3, issue_notes);		
						pstmt.executeUpdate();
						Helper.databaseDisconnect(pstmt, rs);
						//
						qq = "select LAST_INSERT_ID()";
						pstmt = con.prepareStatement(qq);
						rs = pstmt.executeQuery();
						if(rs.next()){
								id = rs.getString(1);
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
				if(msg.equals("")){
						msg = doSelect();
				}
				return msg;
		}
		public String doClose(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = " update time_issues set status='Closed',closed_date=now(),closed_by=? where id=? ";
				if(issue_notes.equals("")){
						msg = "notes are required";
						return msg;
				}
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could not connect to DB ";
						return msg;
				}				
				try{

						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, closed_by);
						pstmt.setString(2, id);		
						pstmt.executeUpdate();
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(pstmt, rs);
						UnoConnect.databaseDisconnect(con);
				}
				if(msg.equals("")){
						msg = doSelect();
				}
				return msg;
		}		

}
