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

public class ShiftTimeList{

    static final long serialVersionUID = 1600L;
    static Logger logger = LogManager.getLogger(ShiftTimeList.class);
    String group_id="", department_id="";
    List<ShiftTime> shifts = null;
    public ShiftTimeList(){
    }
    public ShiftTimeList(String val){
				setDepartment_id(val);
    }
    public void setDepartment_id (String val){
				if(val != null &&  !val.equals("-1")) 
						department_id = val;
    }
    public void setGroup_id (String val){
				if(val != null && !val.equals("-1"))
					 group_id = val;
    }		
    public List<ShiftTime> getShifts(){
				return shifts;
    }
    //
    // getters
    //
    public String find(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "select s.id,s.pay_period_id,"+
						" s.group_id,s.default_hour_code_id,"+
						" s.start_time,"+
						" s.end_time,s.dates,s.added_by_id,"+
						" date_format(s.added_time,'%m/%d/%Y %H:%i'), "+
						" s.processed "+
						" from shift_times s,groups g ";
				String qw = "s.group_id=g.id ";
				if(!department_id.equals("")){
						if(!qw.equals("")) qw += " and ";						
						qw += "g.department_id = ? ";
				}
				if(!group_id.equals("")){
						if(!qw.equals("")) qw += " and ";						
						qw += "g.id = ? ";
				}
				if(!qw.equals("")){
						qq += " where "+qw;
				}
				qq += " order by s.id desc ";
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
						if(!department_id.equals("")){
								pstmt.setString(jj++, department_id);
						}
						if(!group_id.equals("")){
								pstmt.setString(jj++, group_id);
						}						
						rs = pstmt.executeQuery();
						while(rs.next()){
								if(shifts == null)
										shifts = new ArrayList<>();
								ShiftTime one = new ShiftTime(
																			rs.getString(1),
																			rs.getString(2),
																			rs.getString(3),
																			rs.getString(4),
																			rs.getString(5),
																			rs.getString(6),
																			rs.getString(7),
																			rs.getString(8),
																			rs.getString(9),
																			rs.getString(10) != null
																							);
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
