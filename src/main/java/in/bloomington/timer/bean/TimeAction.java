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

		Type action = null;
		Type nextAction = null;
		Workflow workflow = null;
		Employee actioner = null;
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
											String val7
								){
				setVals(val, val2, val3, val4, val5, val6, val7);
    }
    void setVals(String val,
								 String val2,
								 String val3,
								 String val4,
								 String val5,
								 String val6,
								 String val7
								){
				setId(val);
				setWorkflow_id(val2);
				setDocument_id(val3);
				setAction_by(val4);
				setAction_time(val5);
				if(val6 != null){
						workflow = new Workflow(workflow_id, val6, val7);
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
		public String getDocument_id(){
				return document_id;
    }
		public String getWorkflow_id(){
				return workflow_id;
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
    public void setAction_time(String val){
				if(val != null)
						action_time = val;
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
				String qq = "select a.id,a.workflow_id,a.document_id,a.action_by,date_format(a.action_time,'%m/%d/%Y %H:%i'),w.node_id,w.next_node_id from time_actions a join workflows w on a.workflow_id=w.id where a.id=? ";
				logger.debug(qq);
				try{
						con = Helper.getConnection();
						if(con != null){
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
														rs.getString(7));
								}
								else{
										msg = "record not found";
								}
						}
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(con, pstmt, rs);
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
				String qq = " insert into time_actions values(0,?,?,?,now())";
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
				try{
						con = Helper.getConnection();
						if(con == null){
								msg = "Could not connect to DB ";
								return msg;
						}
						pstmt = con.prepareStatement(qq);
						msg = setParams(pstmt);
						if(msg.equals("")){
								pstmt.executeUpdate();
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
						Helper.databaseDisconnect(con, pstmt, rs);
				}
				msg += doSelect();
				return msg;
		}
		String setParams(PreparedStatement pstmt){
				String msg = "";
				int jj=1;
				try{
						pstmt.setString(jj++, workflow_id);
						pstmt.setString(jj++, document_id);
						pstmt.setString(jj++, action_by);
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg);
				}
				return msg;
		}

}
