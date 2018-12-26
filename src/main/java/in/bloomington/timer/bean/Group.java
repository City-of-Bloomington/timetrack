package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.sql.*;
import javax.sql.*;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Group{

		static Logger logger = LogManager.getLogger(Group.class);
		static final long serialVersionUID = 1500L;
		String id="", name="", description="", inactive="";
		String department_id="";
		//
		// default_earn_code_id is needed when 
		// employees work more than weekly or daily hours
		String default_earn_code_id="";
		Type department = null;
		HourCode defaultEarnCode = null;
		List<GroupEmployee> groupEmployees = null;
		List<Employee> employees = null;
    List<GroupLocation> groupLocations = null;
    Set<String> ipSet = null;
		
    public Group(){
    }		
    public Group(String val){
				setId(val);
    }
    public Group(String val,
								 String val2
								 ){
				setId(val);
				setName(val2);
    }		
    public Group(String val,
								 String val2,
								 String val3,
								 String val4,
								 String val5,
								 boolean val6
								 ){
				setId(val);
				setName(val2);
				setDescription(val3);
				setDepartment_id(val4);
				setDefaultEacnCode_id(val5);
				setInactive(val6);				
    }
    public Group(String val,
								 String val2,
								 String val3,
								 String val4,
								 String val5,
								 boolean val6,
								 String val7
								 ){
				setId(val);
				setName(val2);
				setDescription(val3);
				setDepartment_id(val4);
				setDefaultEacnCode_id(val5);
				setInactive(val6);
				if(val7 != null && !val7.equals("")){
						department = new Type(department_id, val7);
				}
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
    public boolean getInactive(){
				return !inactive.equals("");
    }
		public boolean isInactive(){
				return !inactive.equals("");
		}
		public boolean isActive(){
				return inactive.equals("");
		}		
		public String getDepartment_id(){
				return department_id;
    }
		public String getDefaultEarnCode_id(){
				return default_earn_code_id;
		}
		//ToDo
		public String getDefaultEarnCodeName(){
				String ret = "";
				
				return ret;
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
    public void setDescription(String val){
				if(val != null){
						description = val.trim();
				}
    }		
    public void setInactive(boolean val){
				if(val)
						inactive = "y";
    }				
    public void setDepartment_id (String val){
				if(val != null && !val.equals("-1"))
						department_id = val;
    }
		public void setDefaultEacnCode_id(String val){
				if(val != null && !val.equals("-1"))
						default_earn_code_id = val;
		}
		public HourCode getDefaultEarnCode(){
				if(defaultEarnCode == null && !default_earn_code_id.equals("")){
						HourCode one = new HourCode(default_earn_code_id);
						String back = one.doSelect();
						if(back.equals("")){
								defaultEarnCode = one;
						}
				}
				return defaultEarnCode;
		}
		public boolean equals(Object o) {
				if (o instanceof Group) {
						Group c = (Group) o;
						if ( this.id.equals(c.getId())) 
								return true;
				}
				return false;
		}
		public int hashCode(){
				int seed = 37;
				if(!id.equals("")){
						try{
								seed += Integer.parseInt(id)*31;
						}catch(Exception ex){
								// we ignore
						}
				}
				return seed;
		}
    public String toString(){
				return name;
    }
		
		public List<Employee> getEmployees(){
				if(!id.equals("")){
						EmployeeList ul = new EmployeeList();
						ul.setGroup_id(id);
						String back = ul.find();
						if(back.equals("")){
								List<Employee> ones = ul.getEmployees();
								if(ones.size() > 0){
										employees = ones;
								}
						}
				}
				return employees;
		}
		public boolean hasGroupEmployees(){
				getGroupEmployees();
				return groupEmployees != null;
		}
		public List<GroupEmployee> getGroupEmployees(){
				GroupEmployeeList del = new GroupEmployeeList();
				del.setGroup_id(id);
				// we want all
				String back = del.find();
				if(back.equals("")){
						List<GroupEmployee> des = del.getGroupEmployees();
						if(des != null && des.size() > 0){
								groupEmployees = des;
						}
				}
				return groupEmployees;
		}		
		public Type getDepartment(){
				if(department == null && !department_id.equals("")){
						Type one = new Type(department_id);
						one.setTable_name("departments");
						String back = one.doSelect();
						if(back.equals("")){
								department = one;
						}
				}
				return department;
		}


    public List<GroupLocation> getGroupLocations() {
				if (!id.equals("") && groupLocations == null) {
						GroupLocationList ul = new GroupLocationList(id);
						String back = ul.find();
						if (back.equals("")) {
								List<GroupLocation> ones = ul.getGroupLocations();
								if (ones.size() > 0) {
										groupLocations = ones;
										ipSet = ul.getIpSet();
								}
						}
				}
				return groupLocations;
    }

    public boolean hasGroupLocations() {
				getGroupLocations();
				return groupLocations != null && groupLocations.size() > 0;
    }
		/**
		 * if employee have group locations we check that
		 * if no group locations assigned yet, then return true
		 */
    public boolean ipSetIncludes(String ipAddress) {
				if(hasGroupLocations()){
						return !ipSet.isEmpty() && ipSet.contains(ipAddress);
				}
				return true; // when no group locations set yet;
    }
		
		public String doSelect(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "select g.id,g.name,g.description,g.department_id,g.default_earn_code_id,g.inactive,d.name from groups g left join departments d on d.id=g.department_id where g.id =? ";
				logger.debug(qq);
				con = UnoConnect.getConnection();				
				try{
						if(con != null){
								pstmt = con.prepareStatement(qq);
								pstmt.setString(1, id);
								rs = pstmt.executeQuery();
								if(rs.next()){
										setName(rs.getString(2));
										setDescription(rs.getString(3));
										setDepartment_id(rs.getString(4));
										setDefaultEacnCode_id(rs.getString(5));
										setInactive(rs.getString(6) != null);
										str = rs.getString(7);
										if(str != null){
												department = new Type(department_id, str);
										}
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

		public String doSave(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "insert into groups values(0,?,?,?,?,null) ";
				if(name.equals("")){
						msg = " name not set ";
						return msg;
				}
				if(department_id.equals("")){
						msg = " department not set ";
						return msg;
				}				
				logger.debug(qq);
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could not connect to DB ";
						return msg;
				}				
				try{
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, name);
						if(description.equals(""))
								pstmt.setNull(2, Types.VARCHAR);
						else
								pstmt.setString(2, description);
						pstmt.setString(3, department_id);
						if(default_earn_code_id.equals(""))
								pstmt.setNull(4, Types.INTEGER);
						else
								pstmt.setString(4, default_earn_code_id);
						
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

		public String doUpdate(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				if(name.equals("")){
						return " name not set ";
				}
				String qq = "update groups set name=?,description=?,department_id=?,default_earn_code_id=?,inactive=? where id=? ";
				logger.debug(qq);
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could not connect to DB ";
						return msg;
				}			
				try{
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, name);
						if(description.equals(""))
								pstmt.setNull(2, Types.VARCHAR);
						else
								pstmt.setString(2, description);								
						pstmt.setString(3, department_id);
						if(default_earn_code_id.equals(""))
								pstmt.setNull(4, Types.INTEGER);
						else
								pstmt.setString(4, default_earn_code_id);						
						if(inactive.equals("")){
								pstmt.setNull(5, Types.CHAR);
						}
						else{
								pstmt.setString(5,"y");
						}
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
		public String doDelete(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "delete groups where id=? ";
				logger.debug(qq);
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could not connect to DB ";
						return msg;
				}							
				try{
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, id);
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
