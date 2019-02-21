package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.sql.*;
import javax.naming.*;
import javax.naming.directory.*;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SalaryGroup{

		static final long serialVersionUID = 2750L;	
		static Logger logger = LogManager.getLogger(SalaryGroup.class);
    String id="", name="", description="", inactive="",
				default_regular_id="";
		String excess_culculation="Weekly"; // Weekly, Daily, Pay Period
		HourCode defaultRegularCode = null;
		//
		public SalaryGroup(){
				super();
		}
		public SalaryGroup(String val){
				//
				setId(val);
    }		
		public SalaryGroup(String val, String val2){
				//
				// initialize
				//
				setId(val);
				setName(val2);
    }
		public SalaryGroup(String val, String val2, String val3, String val4,
											 String val5, boolean val6){
				setId(val);
				setName(val2);
				setDescription(val3);
				setDefault_regular_id(val4);
				setExcess_culculation(val5);
				setInactive(val6);
    }		
		public boolean equals(Object obj){
				if(obj instanceof SalaryGroup){
						SalaryGroup one =(SalaryGroup)obj;
						return id.equals(one.getId());
				}
				return false;				
		}
		public int hashCode(){
				int seed = 17;
				if(!id.equals("")){
						try{
								seed += Integer.parseInt(id);
						}catch(Exception ex){
						}
				}
				return seed;
		}
    //
    // getters
    //
    public String getId(){
				return id;
    }
    public String getName(){
				return name;
    }
    public String getDescription(){
				return description;
    }
		public String getDefault_regular_id(){
				return default_regular_id;
		}
    public String getExcess_culculation(){
				return excess_culculation;
    }				
    public boolean getInactive(){
				return !inactive.equals("");
    }
		public boolean isInactive(){
				return !inactive.equals("");
		}
		public boolean isActive(){
				return inactive.equals("");
		}
		public boolean isExempt(){
				return name.equals("Exempt");
		}
		public boolean isNonExempt(){
				return name.equals("Non-Exempt");
		}		
		public boolean isTemporary(){
				return name.indexOf("Temp") > -1; // for Temp and Temp W/Ben
		}
		public boolean isPartTime(){
				return name.equals("Part Time");
		}		
		public boolean isUnionned(){
				return name.equals("Union");
		}
		public boolean isPoliceSworn(){
				return name.equals("Police Sworn");
		}
		public boolean isPoliceDetective(){
				return name.equals("Police Sworn Det");
		}
		public boolean isPoliceManagement(){
				return name.equals("Police Sworn Mgt");
		}
		public boolean isFireSworn(){
				return name.equals("Fire Sworn");
		}
		public boolean isFireSworn5x8(){
				return name.equals("Fire Sworn 5x8");
		}
		public boolean isExcessCulculationDaily(){
				return excess_culculation.equals("Daily");
		}
		public boolean isExcessCulculationWeekly(){
				return excess_culculation.equals("Weekly");
		}
		public boolean isExcessCulculationPayPeriod(){
				return excess_culculation.equals("Pay Period");
		}
		//
		// all groups are leave elegible except temp workers
		//
		public boolean isLeaveEligible(){
				return !isTemporary();
		}
    //
    // setters
    //
    public void setId(String val){
				if(val != null)
						id = val;
    }
    public void setName(String val){
				if(val != null)
						name = val.trim();
    }
    public void setExcess_culculation(String val){
				if(val != null)
						excess_culculation = val;
    }		
    public void setDescription(String val){
				if(val != null)
						description = val.trim();
    }		
    public void setInactive(boolean val){
				if(val)
						inactive = "y";
    }		
    public void setDefault_regular_id(String val){
				if(val != null)
						default_regular_id = val;
    }
		public HourCode getDefaultRegularCode(){
				if(defaultRegularCode == null && !default_regular_id.equals("")){
						HourCode one = new HourCode(default_regular_id);
						String back = one.doSelect();
						if(back.equals("")){
								defaultRegularCode = one;
						}
				}
				return defaultRegularCode;
		}
		public boolean hasDefaultRegular(){
				getDefaultRegularCode();
				return defaultRegularCode != null;
		}
    public String toString(){
				return name;
    }
		//
		public String doSelect(){
				String back = "";
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String qq = "select id,name,description,default_regular_id,"+
						"excess_culculation,inactive "+
						"from salary_groups where id=?";
				con = UnoConnect.getConnection();
				if(con == null){
						back = "Could not connect to DB";
						return back;
				}
				try{
						logger.debug(qq);
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1,id);
						rs = pstmt.executeQuery();
						if(rs.next()){
								setName(rs.getString(2));
								setDescription(rs.getString(3));
								setDefault_regular_id(rs.getString(4));
								setExcess_culculation(rs.getString(5));								
								setInactive(rs.getString(6) != null);
						}
						else{
								back ="Record "+id+" Not found";
						}
				}
				catch(Exception ex){
						back += ex+":"+qq;
						logger.error(back);
				}
				finally{
						Helper.databaseDisconnect(pstmt, rs);
						UnoConnect.databaseDisconnect(con);
				}
				return back;
		}
		public String doSave(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				inactive=""; // default
				String qq = " insert into salary_groups values(0,?,?,?,?,?)";
				if(name.equals("")){
						msg = "Earn code name is required";
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
						UnoConnect.databaseDisconnect(con);
				}
				return msg;
		}
		String setParams(PreparedStatement pstmt){
				String msg = "";
				int jj=1;
				try{
						pstmt.setString(jj++, name);
						if(description.equals("")){
								pstmt.setNull(jj++, Types.VARCHAR);
						}
						else
								pstmt.setString(jj++, description);
						if(default_regular_id.equals(""))
								default_regular_id = "1"; // Reg hour code
						pstmt.setString(jj++, default_regular_id);
						if(excess_culculation.equals("")){
								pstmt.setNull(jj++, Types.VARCHAR);
						}
						else
								pstmt.setString(jj++, excess_culculation);
						if(inactive.equals(""))
								pstmt.setNull(jj++, Types.CHAR);
						else
								pstmt.setString(jj++, "y");						
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg);
				}
				return msg;
		}
		public String doUpdate(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = " update salary_groups set name=?, description=?,default_regular_id=?,excess_culculation=?, inactive=? where id=?";
				if(name.equals("")){
						msg = "Earn code name is required";
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
						pstmt.setString(6, id);
						pstmt.executeUpdate();
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
