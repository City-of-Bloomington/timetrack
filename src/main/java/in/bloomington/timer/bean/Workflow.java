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

public class Workflow implements Serializable{

		static Logger logger = LogManager.getLogger(Workflow.class);
		static final long serialVersionUID = 3600L;
    private String id="", node_id="", next_node_id="";
		String next_workflow_id = "";
		Node node = null;
		Node nextNode = null;
		
		public Workflow(){
    }
		
    public Workflow(String val){
				setId(val);
    }
    public Workflow(String val,
										String val2,
										String val3
								){
				setId(val);
				setNode_id(val2);
				setNext_node_id(val3);
    }		
    public Workflow(String val,
										String val2,
										String val3,
										
										String val4,
										String val5,
										boolean val6,
										String val7,
										boolean val8,
										
										String val9,
										String val10,
										boolean val11,
										String val12,
										boolean val13
								){
				setVals(val, val2, val3,
								val4, val5, val6, val7, val8,
								val9, val10, val11, val12, val13
								);
    }
    void setVals(String val,
								 String val2,
								 String val3,
								 
								 String val4,
								 String val5,
								 boolean val6,
								 String val7,
								 boolean val8,
								 
								 String val9,
								 String val10,
								 boolean val11,
								 String val12,
								 boolean val13
								 
								){
				setId(val);
				setNode_id(val2);
				setNext_node_id(val3);
				if(!node_id.equals("") && val4 != null){
						node = new Node(node_id, val4, val5, val6, val7, val8);
				}
				if(!next_node_id.equals("") && val9 != null){
						nextNode = new Node(next_node_id, val9, val10, val11, val12, val13);
				}				
    }		
    //
    // getters
    //
		public String getId(){
				return id;
    }
		public String getNode_id(){
				return node_id;
    }
		public String getNext_node_id(){
				return next_node_id;
    }		
		public boolean isLastNode(){
				return next_node_id.equals("");
		}
		public boolean hasNextNode(){
				return !next_node_id.equals("");
		}
		public Node getNode(){
				if(node == null && !id.equals("")){
						doSelect();
				}
				return node;
		}
		public Node getNextNode(){
				if(node == null && !id.equals("")){
						doSelect();
				}
				return nextNode;
		}		
    //
    // setters
    //
    public void setId (String val){
				if(val != null)
						id = val;
    }
    public void setNode_id (String val){
				if(val != null)
						node_id = val;
    }
    public void setNext_node_id (String val){
				if(val != null)
						next_node_id = val;
    }

    public void setLast_node (boolean val){
				if(val)
						next_node_id = "";
    }

		public String toString(){
				return id;
		}
		public boolean equals(Object o) {
				if (o instanceof Workflow) {
						Workflow c = (Workflow) o;
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
		// next_work_flow_id is normally the next_node_id (if exist)
		public String getNext_workflow_id(){
				if(hasNextNode()){
						return  next_node_id;
				}
				return "";
		}
		public boolean canSubmit(){
				return checkNext("Submit"); 
		}
		public boolean canApprove(){
				return checkNext("Approve"); 
		}
		public boolean canPayroll(){
				return checkNext("Payroll"); 
		}		
		// we always check with next node if any
		// we check if the next action is val
		// where val is one of the workflow_nodes name values
		// 2:Submit for Approval, 3:Approve, 4:Payroll Process
		public boolean checkNext(String val){
				if(hasNextNode()){
						getNextNode();
						return nextNode != null && nextNode.getName().startsWith(val);
				}
				return false;
		}
		public boolean isSubmitted(){
				return checkNode("Submit"); 
		}
		public boolean isApproved(){
				return checkNode("Approve"); 
		}
		public boolean isProcessed(){
				return checkNode("Payroll"); 
		}
		// we check with this node if any
		// where val is one of the workflow_nodes name values
		// 2:Submit for Approval, 3:Approve, 4:Payroll Process
		public boolean checkNode(String val){
				getNode();
				return node != null && node.getName().startsWith(val);
		}		
		public String doSelect(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "select w.id,w.node_id,w.next_node_id,"+
						" s.name,s.description,s.managers_only,s.annotation,s.inactive, "+
						" s2.name,s2.description,s2.managers_only,s2.annotation,s2.inactive "+
						" from workflows w "+
						" join workflow_nodes s on w.node_id=s.id "+
						" left join workflow_nodes s2 on w.next_node_id=s2.id "+
						" where ";
				if(!id.equals("")){
						qq += " w.id = ? ";
				}
				else if(!node_id.equals("")){
						qq += " w.node_id = ? ";
				}
				else{
						msg = "Workflow info can not be found as no workflow id is set";
						return msg;
				}
				logger.debug(qq);
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could not connect to DB ";
						return msg;
				}
				try{
						pstmt = con.prepareStatement(qq);
						if(!id.equals("")){
								pstmt.setString(1, id);
						}
						else{
								pstmt.setString(1, node_id);
						}
						rs = pstmt.executeQuery();
						//
						if(rs.next()){
								setVals(rs.getString(1),
												rs.getString(2),
												rs.getString(3),
												
												rs.getString(4),
												rs.getString(5),
												rs.getString(6) != null,
												rs.getString(7),
												rs.getString(8) != null,
												
												rs.getString(9),
												rs.getString(10),
												rs.getString(11) != null,
												rs.getString(12),
												rs.getString(13) != null
												);
						}
						else{
								msg = "Workflow not found";
						}
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(pstmt, rs);
				}
				return msg;
		}
		String doSave(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = " insert into workflows values(0,?,?)";
				if(node_id.equals("")){
						msg = "node id is required";
						return msg;
				}
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could not connect to DB ";
						return msg;
				}
				try{
						pstmt = con.prepareStatement(qq);
						msg = setParams(pstmt);
						if(msg.equals("")){
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
				}
				msg += doSelect();
				return msg;
		}
		String setParams(PreparedStatement pstmt){
				String msg = "";
				int jj=1;
				try{
						pstmt.setString(jj++, node_id);
						if(next_node_id.equals(""))
								pstmt.setNull(jj++, Types.INTEGER);
						else
								pstmt.setString(jj++, next_node_id);								
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg);
				}
				return msg;
		}
		String doUpdate(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = " update workflows set node_id=?, next_node_id=? where id=?";
				if(node_id.equals("")){
						msg = "node is required";
						return msg;
				}
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could not connect to DB ";
						return msg;
				}
				try{
						pstmt = con.prepareStatement(qq);
						msg = setParams(pstmt);
						pstmt.setString(3, id);
						pstmt.executeUpdate();
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(pstmt, rs);
				}
				return msg;
		}				

}
