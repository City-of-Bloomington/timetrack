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
    String id="", name="", ldap_name="", description="", inactive="", ref_id="";
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
		public Department(String val, String val2, String val3, String val4, String val5, boolean val6){
				setId(val);
				setName(val2);
				setDescription(val3);
				setRef_id(val4);
				setLdap_name(val5);
				setInactive(val6);
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
				String qq = "select id,name,description,ref_id,ldap_name,inactive "+
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
				}
				return back;
		}
		public String doSave(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				inactive=""; // default
				String qq = " insert into departments values(0,?,?,?,?,?)";
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
				String qq = " update departments set name=?, description=?,ref_id=?,ldap_name=?, inactive=? where id=?";
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
						pstmt.setString(6, id);
						pstmt.executeUpdate();
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
