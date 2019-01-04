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

public class GroupShiftList{

    static final long serialVersionUID = 1600L;
    static Logger logger = LogManager.getLogger(GroupShiftList.class);
    String employee_id = "", group_id="", group_ids="";
    String shift_id="", id="";
    boolean active_only = false, inactive_only=false;
    List<GroupShift> groupShifts = null;
    public GroupShiftList(){
    }
    public GroupShiftList(String val){
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
    public void setGroup_id (String val){
				if(val != null)
						group_id = val;
    }
    public void setGroup_ids (String val){
				if(val != null)
						group_ids = val;
    }		
    public void setShift_id (String val){
				if(val != null)
						shift_id = val;
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

    public String getId(){
				return id;
    }		
    public String getGroup_id(){
				return group_id;
    }
    public String getShift_id(){
				return shift_id;
    }		
    public List<GroupShift> getGroupShifts(){
				return groupShifts;
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
				String qq = "select gs.id,"+
						"gs.group_id,"+
						"gs.shift_id,"+
						" date_format(gs.start_date,'%m/%d/%Y'),"+
						" date_format(gs.expire_date,'%m/%d/%Y'),"+						
						" gs.inactive, "+
						// group
						" g.name,g.description,g.department_id,g.default_earn_code_id,g.inactive,d.name, "+
						// shift 
						" s.name,s.start_hour,s.start_minute,s.duration,"+
						" s.start_minute_window,s.end_minute_window,s.minute_rounding,"+
						" s.inactive "+ 						
						" from group_shifts gs, groups g,departments d, shifts s ";
				String qw = "d.id=g.department_id and gs.group_id=g.id ";
				qw += " and gs.shift_id=s.id ";
				if(!group_id.equals("")){
						if(!qw.equals("")) qw += " and ";						
						qw += "gs.group_id = ? ";
				}
				if(!group_ids.equals("")){
						if(!qw.equals("")) qw += " and ";						
						qw += "gs.group_id in ("+group_ids+")";
				}				
				if(!shift_id.equals("")){
						if(!qw.equals("")) qw += " and ";						
						qw += "gs.shift_id = ? ";
				}								
				if(active_only){
						if(!qw.equals("")) qw += " and ";
						qw += " gs.inactive is null and g.inactive is null ";
				}
				if(!employee_id.equals("")){
						qq += ", group_employees ge ";						
						if(!qw.equals("")) qw += " and ";
						qw += " g.id=ge.group_id ";
						qw +=	" and ge.employee_id=?";
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
						if(!group_id.equals("")){
								pstmt.setString(jj++, group_id);
						}
						if(!shift_id.equals("")){
								pstmt.setString(jj++, shift_id);
						}
						if(!employee_id.equals("")){
								pstmt.setString(jj++, employee_id);
						}						
						rs = pstmt.executeQuery();
						while(rs.next()){
								if(groupShifts == null)
										groupShifts = new ArrayList<>();
								GroupShift one = new GroupShift(
																								rs.getString(1),
																								rs.getString(2),
																								rs.getString(3),
																								rs.getString(4),
																								rs.getString(5),
																								rs.getString(6) != null,

																								rs.getString(2), // group_id
																								rs.getString(7),
																								rs.getString(8),
																								rs.getString(9),
																								rs.getString(10),
																								rs.getString(11) != null,
																								rs.getString(12),

																								rs.getString(3), // shift id
																								rs.getString(13),
																								rs.getInt(14),
																								rs.getInt(15),
																								rs.getInt(16),
																								rs.getInt(17),
																								rs.getInt(18),
																								rs.getInt(19),
																								rs.getString(20) != null
																								);
								if(!groupShifts.contains(one))
										groupShifts.add(one);
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
