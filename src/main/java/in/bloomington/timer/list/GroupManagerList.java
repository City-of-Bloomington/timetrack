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

public class GroupManagerList{

		static final long serialVersionUID = 1900L;
		static Logger logger = LogManager.getLogger(GroupManagerList.class);
		String employee_id = "", group_id="", pay_period_id="";
		boolean active_only = false,approversOnly=false,
				processorsOnly=false,
				reviewersOnly=false, dataEntryOnly = false;
		List<GroupManager> managers = null;
    public GroupManagerList(){
    }
    public GroupManagerList(String val){
				setEmployee_id(val);
    }
    public GroupManagerList(String val, String val2){
				setEmployee_id(val);
				setGroup_id(val2);
    }		
    public void setEmployee_id (String val){
				if(val != null)
						employee_id = val;
    }
    public void setGroup_id (String val){
				if(val != null)
						group_id = val;
    }
    public void setPay_period_id (String val){
				if(val != null)
						pay_period_id = val;
    }		
		public void setActiveOnly(){
				active_only = true;
		}
		public void setApproversOnly(){
				approversOnly = true;
		}
		public void setReviewersOnly(){
				reviewersOnly = true;
		}
		public void setProcessorsOnly(){
				processorsOnly = true;
		}
		public void setDataEntryOnly(){
				dataEntryOnly = true;
		}
		public List<GroupManager> getManagers(){
				return managers;
		}
		
    //		
    //
    // getters
    //
		public String find(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "select g.id,g.group_id,g.employee_id,g.wf_node_id,date_format(g.start_date,'%m/%d/%Y'),date_format(g.expire_date,'%m/%d/%Y'),g.inactive,wn.name from group_managers g join workflow_nodes wn on wn.id=g.wf_node_id ";
				String qw = "";
				if(!group_id.equals("")){
					  qw += " g.group_id=? ";
				}
				if(!employee_id.equals("")){
						if(!qw.equals("")) qw += " and ";
						qw += " g.employee_id=?";
				}
				if(!pay_period_id.equals("")){
						qq += ", pay_periods pp ";
						if(!qw.equals("")) qw += " and ";
						qw += " pp.id=? and pp.start_date > g.start_date and (g.expire_date is null or g.expire_date > pp.start_date)";
				}
				if(approversOnly){
						if(!qw.equals("")) qw += " and ";						
						qw += " wn.name like 'Approve'";
				}
				else if(processorsOnly){
						if(!qw.equals("")) qw += " and ";						
						qw += " wn.name like 'Payroll%'";
				}
				else if(reviewersOnly){
						if(!qw.equals("")) qw += " and ";						
						qw += " wn.name like 'Review'";
				}
				else if(dataEntryOnly){
						if(!qw.equals("")) qw += " and ";						
						qw += " wn.name like 'Data%'";
				}				
				if(active_only){
						if(!qw.equals("")) qw += " and ";
						qw += " g.inactive is null ";
				}
				if(!qw.equals("")){
						qq += " where "+qw;
				}
				qq += " order by g.start_date desc ";
				logger.debug(qq);
				con = Helper.getConnection();
				if(con == null){
						msg = " Could not connect to DB ";
						logger.error(msg);
						return msg;
				}
				try{
						pstmt = con.prepareStatement(qq);
						int jj=1;
						if(!group_id.equals("")){
								pstmt.setString(jj++, group_id);
						}						
						if(!employee_id.equals("")){
								pstmt.setString(jj++, employee_id);
						}
						if(!pay_period_id.equals("")){
								pstmt.setString(jj++, pay_period_id);
						}						
						rs = pstmt.executeQuery();
						while(rs.next()){
								if(managers == null)
										managers = new ArrayList<>();
							 GroupManager one = new GroupManager(
																									 rs.getString(1),
																									 rs.getString(2),
																									 rs.getString(3),
																									 rs.getString(4),
																									 rs.getString(5),
																									 rs.getString(6),
																									 rs.getString(7) != null,
																									 rs.getString(8)
																									 );
								managers.add(one);
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
