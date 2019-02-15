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

public class GroupList{

    static final long serialVersionUID = 1600L;
    static Logger logger = LogManager.getLogger(GroupList.class);
    String employee_id = "",
				department_ids ="",
				department_id="",
				pay_period_id="";
    String name="", id="";
    boolean active_only = false, inactive_only=false;
    List<Group> groups = null;
    public GroupList(){
    }
    public GroupList(String val){
				setEmployee_id(val);
    }
    public void setId (String val){
				if(val != null)
						id = val;
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
				if(val != null && !val.equals("-1")){
						if(!department_id.equals("")){
								department_id = val;
						}
						if(!department_ids.equals("")){
								department_ids +=",";
						}
						department_ids += val;
				}
    }
    public void setName(String val){
				if(val != null)
						name = val;
    }
    public void setActive_status(String val){
				if(val != null && !val.equals("-1")){
						if(val.equals("Active"))
								active_only = true;
						else if(val.equals("Inactive"))
								inactive_only = true;
				}
    }		
    public void setActiveOnly(){
				active_only = true;
    }
    public String getDepartment_id(){
				if(department_id.equals(""))
						return "-1";
				return department_id;
    }
    public String getId(){
				return id;
    }		
    public String getName(){
				return name;
    }
    public List<Group> getGroups(){
				return groups;
    }
    public String getActive_status(){
				if(active_only)
						return "Active";
				else if(inactive_only)
						return "Inactive";
				return "-1";
    }
    //
    // getters
    //
    public String find(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "select g.id,g.name,g.description,g.department_id,g.excess_hours_calculation_method,g.inactive,d.name from groups g left join departments d on d.id=g.department_id ";
				String qw = "";
				if(!department_ids.equals("")){
						if(!qw.equals("")) qw += " and ";						
						qw += "g.department_id in ("+department_ids+") ";
				}
				if(!name.equals("")){
						if(!qw.equals("")) qw += " and ";						
						qw += "g.name like ? ";
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
								qw +=	" and gu.effective_date <= pp.start_date and (gu.expire_date is null or gu.expire_date > pp.start_date) and pp.id=? ";
						}
				}
				if(active_only){
						if(!qw.equals("")) qw += " and ";
						qw += " g.inactive is null ";
				}
				if(!qw.equals("")){
						qq += " where "+qw;
				}
				con = UnoConnect.getConnection();
				if(con == null){
						msg = " Could not connect to DB ";
						logger.error(msg);
						return msg;
				}
				logger.debug(qq);
				try{
						pstmt = con.prepareStatement(qq);
						int jj=1;
						if(!department_ids.equals("")){

						}
						if(!name.equals("")){
								pstmt.setString(jj++, "%"+name+"%");
						}						
						if(!employee_id.equals("")){
								pstmt.setString(jj++, employee_id);
						}
						if(!pay_period_id.equals("")){
								pstmt.setString(jj++, pay_period_id);
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
																			rs.getString(5),
																			rs.getString(6) != null,
																			rs.getString(7));
								if(!groups.contains(one))
										groups.add(one);
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

}
