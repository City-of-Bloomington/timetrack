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

public class WorkflowList{

		static final long serialVersionUID = 3700L;
		static Logger logger = LogManager.getLogger(WorkflowList.class);
		String node_id = "", id="", sortBy="";
		
		boolean active_only = false;
		List<Workflow> workflows = null;
    public WorkflowList(){
    }
    public void setId (String val){
				if(val != null)
						id = val;
    }		
    public void setNode_id (String val){
				if(val != null)
						node_id = val;
    }
		public void setActiveOnly(){
				active_only = true;
		}
		public void forFirstWorkflow(){
				sortBy = " w.id asc limit 1 ";
		}
		public List<Workflow> getWorkflows(){
				return workflows;
		}
    //
    // find the list
    //
		public String find(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", qw="";
				String qq = "select w.id,w.node_id,w.next_node_id,"+
							" s.name,s.description,s.managers_only,s.annotation,s.inactive, "+
						" s2.name,s2.description,s2.managers_only,s2.annotation,s2.inactive "+						
						" from workflows w "+
						" join workflow_nodes s on w.node_id=s.id "+
						" left join workflow_nodes s2 on w.next_node_id=s2.id ";
				if(!id.equals("")){
						qw += " w.id = ? ";
				}
				if(!node_id.equals("")){
						if(!qw.equals("")) qw += " and ";
						qw += " (w.node_id = ? or w.next_node_id=?) ";
				}
				if(!qw.equals("")){
						qq += " where "+qw;
				}
				if(!sortBy.equals("")){
						qq += " order by "+sortBy;
				}
				con = Helper.getConnection();
				if(con == null){
						msg = " Could not connect to DB ";
						logger.error(msg);
						return msg;
				}
				logger.debug(qq);
				try{
						pstmt = con.prepareStatement(qq);
						int jj=1;
						if(!id.equals("")){
								pstmt.setString(jj++, id);
						}
						if(!node_id.equals("")){
								pstmt.setString(jj++, node_id);
								pstmt.setString(jj++, node_id);
						}						
						rs = pstmt.executeQuery();
						while(rs.next()){
								if(workflows == null)
										workflows = new ArrayList<>();
							 Workflow one = new Workflow(
																		 rs.getString(1),
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
								workflows.add(one);
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

}
