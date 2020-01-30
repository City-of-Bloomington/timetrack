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

public class ShiftList{

    static final long serialVersionUID = 1600L;
    static Logger logger = LogManager.getLogger(ShiftList.class);
    String employee_id = "", group_id="";
    String name="", id="";
    boolean active_only = false, inactive_only=false;
    List<Shift> shifts = null;
    public ShiftList(){
    }
    public ShiftList(String val){
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

    public String getId(){
				return id;
    }		
    public String getName(){
				return name;
    }
    public List<Shift> getShifts(){
				return shifts;
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
				String qq = "select g.id,g.name,g.start_hour,g.start_minute,"+
						"g.duration,"+
						"g.start_minute_window,"+
						"g.end_minute_window,"+
						"g.minute_rounding,"+
						"g.inactive from shifts g ";
				String qw = "";
				if(!name.isEmpty()){
						if(!qw.isEmpty()) qw += " and ";						
						qw += "g.name like ? ";
				}				
				if(active_only){
						if(!qw.isEmpty()) qw += " and ";
						qw += " g.inactive is null ";
				}
				if(!qw.isEmpty()){
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
						if(!name.isEmpty()){
								pstmt.setString(jj++, "%"+name+"%");
						}						
						rs = pstmt.executeQuery();
						while(rs.next()){
								if(shifts == null)
										shifts = new ArrayList<>();
								Shift one = new Shift(
																			rs.getString(1),
																			rs.getString(2),
																			rs.getInt(3),
																			rs.getInt(4),
																			rs.getInt(5),
																			rs.getInt(6),
																			rs.getInt(7),
																			rs.getInt(8),
																			rs.getString(9) != null);
								shifts.add(one);
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
