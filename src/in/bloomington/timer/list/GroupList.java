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
import org.apache.log4j.Logger;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;

public class GroupList{

		static final long serialVersionUID = 1600L;
		static Logger logger = Logger.getLogger(GroupList.class);
		String employee_id = "", department_id="", pay_period_id="";
		boolean active_only = false;
		List<Group> groups = null;
    public GroupList(){
    }
    public GroupList(String val){
				setEmployee_id(val);
    }		
    public void setEmployee_id (String val){
				if(val != null)
						employee_id = val;
    }
    public void setPay_period_id (String val){
				if(val != null)
						pay_period_id = val;
    }		
    public void setDepartment_id (String val){
				if(val != null)
						department_id = val;
    }
		
		public void setActiveOnly(){
				active_only = true;
		}
		public List<Group> getGroups(){
				return groups;
		}
    //
    // getters
    //
		public String find(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "select g.id,g.name,g.description,g.department_id,g.inactive,d.name from groups g left join departments d on d.id=g.department_id ";
				String qw = "";
				if(!department_id.equals("")){
						if(!qw.equals("")) qw += " and ";						
						qw += "g.department_id=? ";
				}				
				if(!employee_id.equals("") || !pay_period_id.equals("")){
						qq += ", group_employees gu ";						
						if(!qw.equals("")) qw += " and ";
						qw += " g.id=gu.group_id ";
						if(!employee_id.equals("")){
							qw +=	" and gu.employee_id=?";
						}
						if(active_only)
								qw += " and gu.inactive is null ";
						if(!pay_period_id.equals("")){
								qq += ", pay_periods pp ";
								qw +=	" and gu.start_date <= pp.start_date and (gu.expire_date is null or gu.expire_date > pp.start_date)";
						}
				}
				if(active_only){
						if(!qw.equals("")) qw += " and ";
						qw += " g.inactive is null ";
				}
				if(!qw.equals("")){
						qq += " where "+qw;
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
						if(!department_id.equals("")){
								pstmt.setString(1, department_id);
						}						
						if(!employee_id.equals("")){
								pstmt.setString(1, employee_id);
						}
						rs = pstmt.executeQuery();
						while(rs.next()){
								if(groups == null)
										groups = new ArrayList<>();
							 Group one = new Group(
																		 rs.getString(1),
																		 rs.getString(2),
																		 rs.getString(3),
																		 rs.getString(4),
																		 rs.getString(5) != null,
																		 rs.getString(6));
								groups.add(one);
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
