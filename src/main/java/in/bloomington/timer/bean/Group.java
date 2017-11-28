package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.Serializable;
import java.util.List;
import java.sql.*;
import javax.sql.*;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Group extends Type{

    private String department_id="";
		static Logger logger = LogManager.getLogger(Group.class);
		static final long serialVersionUID = 1500L;
		Type department = null;
		List<GroupEmployee> groupEmployees = null;
		List<Employee> employees = null;
    public Group(){
				setTable_name("groups");
    }		
    public Group(String val){
				super(val);
				setTable_name("groups");
    }
		
    public Group(String val,
								 String val2,
								 String val3,
								 String val4,
								 boolean val5
								 ){
				super(val, val2, val3,val5);
    }
		
    public Group(String val,
								 String val2,
								 String val3,
								 String val4,
								 boolean val5,
								 String val6
								 ){
				super(val, val2, val3, val5);
				if(val6 != null){
						department = new Type(department_id, val6);
				}
    }		
    //
    // getters
    //
		public String getDepartment_id(){
				return department_id;
    }
    //
    // setters
    //
    public void setDepartment_id (String val){
				if(val != null && !val.equals("-1"))
						department_id = val;
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
		@Override
		public String doSelect(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "select g.id,g.name,g.description,g.department_id,g.inactive,d.name from groups g left join departments d on d.id=g.department_id where g.id =? ";
				logger.debug(qq);
				try{
						con = Helper.getConnection();
						if(con != null){
								pstmt = con.prepareStatement(qq);
								pstmt.setString(1, id);
								rs = pstmt.executeQuery();
								if(rs.next()){
										setName(rs.getString(2));
										setDescription(rs.getString(3));
										setDepartment_id(rs.getString(4));
										setInactive(rs.getString(5) != null);
										str = rs.getString(6);
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
						Helper.databaseDisconnect(con, pstmt, rs);
				}
				return msg;
		}
		@Override
		public String doSave(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "insert into groups values(0,?,?,?,null) ";
				if(name.equals("")){
						msg = " name not set ";
						return msg;
				}
				if(department_id.equals("")){
						msg = " department not set ";
						return msg;
				}				
				logger.debug(qq);
				try{
						con = Helper.getConnection();
						if(con != null){
								pstmt = con.prepareStatement(qq);
								pstmt.setString(1, name);
								if(description.equals(""))
										pstmt.setNull(2, Types.VARCHAR);
								else
										pstmt.setString(2, description);
								pstmt.setString(3, department_id);
								pstmt.executeUpdate();
						}
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
						Helper.databaseDisconnect(con, pstmt, rs);
				}
				return msg;
		}
		@Override
		public String doUpdate(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				if(name.equals("")){
						return " name not set ";
				}
				String qq = "update groups set name=?,description=?,department_id=?,inactive=? where id=? ";
				logger.debug(qq);
				try{
						con = Helper.getConnection();
						if(con != null){
								pstmt = con.prepareStatement(qq);
								pstmt.setString(1, name);
								if(description.equals(""))
										pstmt.setNull(2, Types.VARCHAR);
								else
										pstmt.setString(2, description);								
								pstmt.setString(3, department_id);								
								if(inactive.equals("")){
										pstmt.setNull(4, Types.CHAR);
								}
								else{
										pstmt.setString(4,"y");
								}
								pstmt.setString(5, id);
								pstmt.executeUpdate();
						}
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(con, pstmt, rs);
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
				try{
						con = Helper.getConnection();
						if(con != null){
								pstmt = con.prepareStatement(qq);
								pstmt.setString(1, id);
								pstmt.executeUpdate();
						}
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(con, pstmt, rs);
				}
				return msg;
		}		

}
