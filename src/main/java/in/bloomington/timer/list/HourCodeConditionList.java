package in.bloomington.timer.list;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Hashtable;
import java.text.*;
import java.util.Date;
import java.sql.*;
import javax.naming.*;
import javax.naming.directory.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;

public class HourCodeConditionList{

		static final long serialVersionUID = 900L;
		static Logger logger = LogManager.getLogger(HourCodeConditionList.class);
		
		List<HourCodeCondition> conditions = null;
		boolean active_only = false;
		String department_id="", salary_group_id="", hour_code_id="";
    public HourCodeConditionList(){
    }
		public List<HourCodeCondition> getConditions(){
				return conditions;
		}
		public void setActiveOnly(){
				active_only = true;
		}
    public void setHour_code_id (String val){
				if(val != null)
						hour_code_id = val;
    }
    public void setDepartment_id(String val){
				if(val != null)
						department_id = val;
    }
    public void setSalary_group_id(String val){
				if(val != null)
						salary_group_id = val;
    }		
    //
    // getters
    //
		public String find(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", qw="";
				String qq = "select g.id,g.hour_code_id,g.department_id,g.salary_group_id,date_format(g.date,'%m/%d/%Y'),g.inactive from hour_code_conditions g left join hour_codes e on e.id=g.hour_code_id  ";
				logger.debug(qq);
				if(active_only){
						qw += " g.inactive is null ";
				}
				if(!department_id.equals("")){
						if(!qw.equals("")) qw += " and ";
						qw += " (g.department_id = ? or g.department_id is null)";
				}
				if(!salary_group_id.equals("")){
						if(!qw.equals("")) qw += " and ";
						qw += " (g.salary_group_id = ? or g.salary_group_id is null)";
				}				
				if(!qw.equals("")){
						qq += " where "+qw;
				}
				qq += " order by e.name ";
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
						if(!salary_group_id.equals("")){
								pstmt.setString(jj++, salary_group_id);								
						}										
						rs = pstmt.executeQuery();
						while(rs.next()){
								if(conditions == null)
									 conditions = new ArrayList<>();
							 HourCodeCondition one =
									 new HourCodeCondition(
																				 rs.getString(1),
																				 rs.getString(2),
																				 rs.getString(3),
																				 rs.getString(4),
																				 rs.getString(5),
																				 rs.getString(6) != null);
							 if(!conditions.contains(one))
									 conditions.add(one);
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
