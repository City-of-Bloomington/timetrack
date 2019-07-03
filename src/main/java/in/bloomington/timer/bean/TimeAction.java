package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.Serializable;
import java.util.Set;
import java.util.List;
import java.util.HashSet;
import java.util.Hashtable;
import java.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;

public class TimeAction implements Serializable{

		static Logger logger = LogManager.getLogger(TimeAction.class);
		static final long serialVersionUID = 3600L;
    private String id="", workflow_id="", document_id="",
				action_by="", // employee id
				action_time="";
		String cancelled_by = "", cancelled_time="";
		
		Type action = null;
		Type nextAction = null;
		Workflow workflow = null;
		Employee actioner = null, canceller=null;
		public TimeAction(){
    }
		
    public TimeAction(String val){
				setId(val);
    }
		// for saving
    public TimeAction(String val,
											String val2,
											String val3){
				setWorkflow_id(val);
				setDocument_id(val2);
				setAction_by(val3);
		}
    public TimeAction(String val,
											String val2,
											String val3,
											String val4,
											String val5,
											String val6,
											String val7,
											String val8,
											String val9
								){
				setVals(val, val2, val3, val4, val5, val6, val7, val8, val9);
    }
    void setVals(String val,
								 String val2,
								 String val3,
								 String val4,
								 String val5,
								 String val6,
								 String val7,
								 String val8,
								 String val9
								){
				setId(val);
				setWorkflow_id(val2);
				setDocument_id(val3);
				setAction_by(val4);
				setAction_time(val5);
				setCancelled_by(val6);
				setCancelled_time(val7);
				if(val8 != null){
						workflow = new Workflow(workflow_id, val8, val9);
				}
    }		
    //
    // getters
    //

		public String getId(){
				return id;
    }
		public String getAction_by(){
				return action_by;
    }
		public String getAction_time(){
				return action_time;
    }
		public String getCancelled_time(){
				return cancelled_time;
    }		
		public String getDocument_id(){
				return document_id;
    }
		public String getWorkflow_id(){
				return workflow_id;
    }
		public String getCancelled_by(){
				return cancelled_by;
    }
		public boolean isCancelled(){
				return !cancelled_time.equals("");
		}
		public String getCancelInfo(){
				if(isCancelled()){
						return " Cancelled on "+cancelled_time+" by "+getCanceller();
				}
				return "";
		}
		// only Payroll approve can be cancelled
		public boolean canBeCancelled(){
				if(id.equals("") || isCancelled()) return false;
				getWorkflow();
				return workflow != null && workflow.isProcessed();
		}
    //
    // setters
    //
    public void setId (String val){
				if(val != null)
						id = val;
    }
    public void setWorkflow_id (String val){
				if(val != null)
						workflow_id = val;
    }
		public void setDocument_id (String val){
				if(val != null)
						document_id = val;
    }
    public void setAction_by (String val){
				if(val != null)
						action_by = val;
    }
    public void setCancelled_by(String val){
				if(val != null)
						cancelled_by = val;
    }		
    public void setAction_time(String val){
				if(val != null)
						action_time = val;
    }
    public void setCancelled_time(String val){
				if(val != null)
						cancelled_time = val;
    }		
		public Workflow getWorkflow(){
				if(workflow == null && !workflow_id.equals("")){
						Workflow one = new Workflow(workflow_id);
						String back = one.doSelect();
						if(back.equals("")){
								workflow = one;
						}
				}
				return workflow;
		}
		public Employee getActioner(){
				if(actioner == null && !action_by.equals("")){
						Employee one = new Employee(action_by);
						String back = one.doSelect();
						if(back.equals("")){
								actioner = one;
						}
				}
				return actioner;
		}
		public Employee getCanceller(){
				if(canceller == null && !cancelled_by.equals("")){
						Employee one = new Employee(cancelled_by);
						String back = one.doSelect();
						if(back.equals("")){
								canceller = one;
						}
				}
				return canceller;
		}
		public String toString(){
				return id;
		}
		public boolean equals(Object o) {
				if (o instanceof TimeAction) {
						TimeAction c = (TimeAction) o;
						if ( this.id.equals(c.getId())) 
								return true;
				}
				return false;
		}
		public int hashCode(){
				int seed = 31;
				if(!id.equals("")){
						try{
								seed += Integer.parseInt(id)*47;
						}catch(Exception ex){
								// we ignore
						}
				}
				return seed;
		}		

		public String doSelect(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "select a.id,a.workflow_id,a.document_id,a.action_by,"+
						" date_format(a.action_time,'%m/%d/%Y %H:%i'),"+
						" a.cancelled_by, date_format(a.cancelled_time,'%m/%d/%Y %H:%i'),"+
						" w.node_id,w.next_node_id from time_actions a "+
						" join workflows w on a.workflow_id=w.id where a.id=? ";
				logger.debug(qq);
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could not connect to DB ";
						return msg;
				}				
				try{
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, id);
						rs = pstmt.executeQuery();
						//
						if(rs.next()){
								setVals(rs.getString(1),
												rs.getString(2),
												rs.getString(3),
												rs.getString(4),
												rs.getString(5),
												rs.getString(6),
												rs.getString(7),
												rs.getString(8),
												rs.getString(9));
						}
						else{
								msg = "record not found";
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
		//
		// save only, no update
		//
		public String doSave(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				//
				// check if it is not performed yet (approve or else)
				String qq = " select count(*) from time_actions where document_id=? and workflow_id=? and cancelled_time is null";
				String qq2 = " insert into time_actions values(0,?,?,?,now(),null,null)";
				if(workflow_id.equals("")){
						msg = "workflow id not set ";
						return msg;
				}
				if(document_id.equals("")){
						msg = "document id not set ";
						return msg;
				}
				if(action_by.equals("")){
						msg = "action user not set ";
						return msg;
				}
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could not connect to DB ";
						return msg;
				}				
				try{
						// to avoid multiple approve
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, document_id);
						pstmt.setString(2, workflow_id);
						rs = pstmt.executeQuery();
						int cnt = 0;
						if(rs.next()){
								cnt = rs.getInt(1);
						}
						if(cnt == 0){
								qq = qq2;
								pstmt = con.prepareStatement(qq);								
								pstmt.setString(1, workflow_id);
								pstmt.setString(2, document_id);
								pstmt.setString(3, action_by);
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
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(pstmt, rs);
						UnoConnect.databaseDisconnect(con);
				}
				msg += doSelect();
				return msg;
		}
		//
		// if the action need to be cancelled
		//
		public String doCancel(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				//
				// check if it is not performed yet (approve or else)
				String qq = " update time_actions set cancelled_by=?, cancelled_time=now() where id=? ";
				if(id.equals("")){
						msg = "action id not set ";
						return msg;
				}
				if(cancelled_by.equals("")){
						msg = "cancelled by  set ";
						return msg;
				}
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could not connect to DB ";
						return msg;
				}				
				try{
						// to avoid multiple approve
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, cancelled_by);
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
				msg += doSelect();
				return msg;
		}
		

}
