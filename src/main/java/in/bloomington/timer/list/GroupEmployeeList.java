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

public class GroupEmployeeList{

		static final long serialVersionUID = 1900L;
		static Logger logger = LogManager.getLogger(GroupEmployeeList.class);
		String employee_id = "", group_id="", pay_period_id="";
		boolean active_only = false, current_only=false;
		List<GroupEmployee> groupEmployees = null;
    public GroupEmployeeList(){
    }
    public GroupEmployeeList(String val){
				setEmployee_id(val);
    }
    public GroupEmployeeList(String val, String val2){
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
		public void setCurrentOnly(){
				current_only = true;
		}		
		public List<GroupEmployee> getGroupEmployees(){
				return groupEmployees;
		}
    //
		/*						
select g.id,g.name,g.description,g.department_id,g.inactive,d.name from groups g
 left join departments d on d.id=g.department_id, group_employees gu where g.id=gu.group_id  and gu.employee_id=9 and gu.inactive is null  and  g.inactive is null
		*/
    //
    // getters
    //
		public String find(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "select g.id,g.group_id,g.employee_id,date_format(g.effective_date,'%m/%d/%Y'),date_format(g.expire_date,'%m/%d/%Y'),g.inactive from group_employees g ";
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
						qw += " pp.id=? and pp.start_date > g.effective_date and (g.expire_date is null or g.expire_date > pp.start_date )";
				}
				else if(current_only){
						if(!qw.equals("")) qw += " and ";						
						qw += " g.effective_date <= now() and (g.expire_date is null or g.expire_date > now())";
				}
				if(active_only){
						if(!qw.equals("")) qw += " and ";
						qw += " g.inactive is null ";
				}
				if(!qw.equals("")){
						qq += " where "+qw;
				}
				qq += " order by g.effective_date desc ";
				logger.debug(qq);
				con = UnoConnect.getConnection();
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
								if(groupEmployees == null)
										groupEmployees = new ArrayList<>();
							 GroupEmployee one = new GroupEmployee(
																									 rs.getString(1),
																									 rs.getString(2),
																									 rs.getString(3),
																									 rs.getString(4),
																									 rs.getString(5),
																									 rs.getString(6) != null);
							 if(!groupEmployees.contains(one))
									 groupEmployees.add(one);
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

}
