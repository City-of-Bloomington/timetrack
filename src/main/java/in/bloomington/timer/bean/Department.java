package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.List;
import java.sql.*;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Department implements java.io.Serializable{

		static final long serialVersionUID = 3700L;	
		static Logger logger = LogManager.getLogger(Department.class);
    String id="", name="", ldap_name="", description="",
				inactive="", ref_id="", allow_pending_accrual="",
				old_allow_pending_accrual=""; // to check for change
		List<Group> groups = null;
		//
		public Department(){

		}
		public Department(String val){
				//
				setId(val);
    }		
		public Department(String val, String val2){
				//
				// initialize
				//
				setId(val);
				setName(val2);
    }
		public Department(String val, String val2, String val3, String val4, String val5, boolean val6, boolean val7){
				setId(val);
				setName(val2);
				setDescription(val3);
				setRef_id(val4);
				setLdap_name(val5);
				setAllowPendingAccrual(val6);
				setInactive(val7);
    }		
		public boolean equals(Object obj){
				if(obj instanceof Department){
						Department one =(Department)obj;
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
    public String getLdap_name(){
				return ldap_name;
    }		
    public String getDescription(){
				return description;
    }
		// ref id from New World app
    public String getRef_id(){
				return ref_id;
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
		public boolean isHand(){
				return !name.equals("") && name.equals("HAND");
		}
		public boolean isSanitation(){
				return !name.equals("") && name.equals("Sanitation");
		}
		public boolean isUtilities(){
				return !name.equals("") && name.equals("Utilities");
		}
    public boolean getAllowPendingAccrual(){
				return !allow_pending_accrual.equals("");
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
    public void setLdap_name(String val){
				if(val != null)
						ldap_name = val.trim();
    }		
    public void setRef_id(String val){
				if(val != null)
						ref_id = val;
    }		
    public void setDescription(String val){
				if(val != null)
						description = val.trim();
    }		
    public void setInactive(boolean val){
				if(val)
						inactive = "y";
    }
    public void setAllowPendingAccrual(boolean val){
				if(val)
						allow_pending_accrual = "y";
    }
		public void setOldAllowPendingAccrual(boolean val){
				if(val)
						old_allow_pending_accrual = "y";
		}
    public String toString(){
				return name;
    }
		public List<Group> getGroups(){
				if(!id.equals("") && groups == null){
						GroupList gl = new GroupList();
						gl.setDepartment_id(id);
						gl.setActiveOnly();
						String back = gl.find();
						if(back.equals("")){
								List<Group> ones = gl.getGroups();
								if(ones != null && ones.size() > 0){
										groups = ones;
								}
						}
				}
				return groups;
		}
		public boolean hasGroups(){
				getGroups();
				return groups != null && groups.size() > 0;
		}
		//
		public String doSelect(){
				String back = "";
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String qq = "select id,name,description,ref_id,ldap_name,allow_pending_accrual,inactive "+
						"from departments where id=?";
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
								setRef_id(rs.getString(4));
								setLdap_name(rs.getString(5));
								setAllowPendingAccrual(rs.getString(6) != null);
								setInactive(rs.getString(7) != null);
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
				String qq = " insert into departments values(0,?,?,?,?,?,?)";
				if(name.equals("")){
						msg = "name is required";
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
						if(ref_id.equals("")){
								pstmt.setNull(jj++, Types.VARCHAR);
						}
						else
								pstmt.setString(jj++, ref_id);
						if(ldap_name.equals("")){
								pstmt.setNull(jj++, Types.VARCHAR);
						}
						else
								pstmt.setString(jj++, ldap_name);
						if(allow_pending_accrual.equals(""))
								pstmt.setNull(jj++, Types.CHAR);
						else
								pstmt.setString(jj++, "y");							
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
				String msg="", str="", qq2="";
				boolean changeGroupAllowPending = false;
				String qq = " update departments set name=?, description=?,ref_id=?,ldap_name=?,allow_pending_accrual=?,inactive=? where id=?";
				if(name.equals("")){
						msg = "name is required";
						return msg;
				}
				if(!allow_pending_accrual.equals(old_allow_pending_accrual)){
						changeGroupAllowPending = true;						
						if(!allow_pending_accrual.equals("")){
								// turn it on for all groups in this department
								qq2 = "update groups g set g.allow_pending_accrual='y' where g.department_id=? and g.inactive is null "; 
						}
						else{
								qq2 = "update groups g set g.allow_pending_accrual=null where g.department_id=? and g.inactive is null "; 
						}
				}
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could not connect to DB ";
						return msg;
				}
				try{
						pstmt = con.prepareStatement(qq);
						msg = setParams(pstmt);
						pstmt.setString(7, id);
						pstmt.executeUpdate();
						if(changeGroupAllowPending){
								Helper.databaseDisconnect(pstmt, rs);
								pstmt = con.prepareStatement(qq2);
								pstmt.setString(1, id);
								pstmt.executeUpdate();								
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
/*

	select id,caseId,street_num,street_dir,street_name,street_type,sud_type,sud_num from legal_addresses where invalid_addr is not null into outfile '/var/lib/mysql-files/invalid_addresses.csv' fields enclosed by '"' terminated by ',' escaped by '"' lines terminated by '\r\n'





 */
