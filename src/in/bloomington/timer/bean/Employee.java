package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.Serializable;
import java.util.Set;
import java.util.List;
import java.util.HashSet;
import java.util.Hashtable;
import java.sql.*;
import org.apache.log4j.Logger;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;

public class Employee implements Serializable{

		static Logger logger = Logger.getLogger(Employee.class);
		static final long serialVersionUID = 1150L;		
    private String id="", inactive="", id_code="", employee_number="",
				user_id="", department_id="";
		// normally this date is pay period start date
		String job_active_date = "", pay_period_id="", selected_job_id="";
		User user = null;
		List<Group> groups = null;
		List<GroupManager> managers = null;
		List<DepartmentEmployee> departmentEmployees = null;
		List<GroupEmployee> groupEmployees = null;		
		DepartmentEmployee departmentEmployee = null;
		Department department = null;
		GroupEmployee groupEmployee = null;
		//
		// for a given selected job and pay_period_id we need to find
		// salary group, hour codes
		//
		public Employee(){
    }
		
    public Employee(String val){
				setId(val);
    }
    public Employee(String val, String val2){
				setId(val);
				setUser_id(val2);

    }
    public Employee(String val,
										String val2,
										String val3,
										String val4,
										boolean val5
								){
				setId(val);
				setUser_id(val2);
				setId_code(val3);
				setEmployee_number(val4);
				setInactive(val5);
    }		
    public Employee(String val,
										String val2,
										String val3,
										String val4,
										boolean val5,
										String val6,
										String val7,
										String val8,
										String val9,
										String val10,
										boolean val11
								){
				setVals(val, val2, val3, val4, val5, val6,
								val7, val8, val9, val10, val11);
    }
    void setVals(String val,
								 String val2,
								 String val3,
								 String val4,
								 boolean val5,
								 String val6,
								 String val7,
								 String val8,
								 String val9,
								 String val10,
								 boolean val11
								){
				setId(val);
				setUser_id(val2);
				setId_code(val3);
				setEmployee_number(val4);
				setInactive(val5);				
				user = new User(val6, val7, val8, val9, val10, val11);

    }
    //
    // getters
    //
    public String getUser_id(){
				return user_id;
    }
		public String getId(){
				return id;
    }
    public String getId_code(){
				return id_code;
    }
    public String getEmployee_number(){
				return employee_number;
    }
		public boolean isInactive(){
				return !inactive.equals("");
		}
		public boolean isActive(){
				return inactive.equals("");
		}		
		public boolean hasGroup(){
				getGroups();
				return groups != null && groups.size() > 0;
		}
		public boolean hasGroup(Group gg){
				getGroups();
				return groups != null && groups.contains(gg);
		}
    //
    // setters
    //
    public void setId (String val){
				if(val != null)
						id = val;
    }
    public void setUser_id (String val){
				if(val != null)
						user_id = val;
    }

    public void setId_code (String val){
				if(val != null)
						id_code = val.trim();
    }
    public void setEmployee_number(String val){
				if(val != null)
						employee_number = val.trim();
    }
    public void setJob_active_date(String val){
				if(val != null)
					 job_active_date = val;
    }
		public void setPay_period_id(String val){
				if(val != null)
						pay_period_id = val;
		}
		public void setSelected_job_id(String val){
				if(val != null)
						selected_job_id = val;
    }
		public void setUser(User val){
				if(val != null)
						user = val;
		}
		public User getUser(){
				if(user == null){
						if(!user_id.equals("")){
								User one = new User(user_id);
								String back = one.doSelect();
								if(back.equals("")){
										user = one;
								}
						}
						else{ // needed for new user
								user = new User();
						}
				}
				return user;
		}
		public String getDepartment_id(){
				getDepartment();
				if(department != null){
						return department.getId();
				}
				return "";
		}
    public void setInactive (boolean val){
				if(val)
						inactive = "y";
    }

		public String toString(){
				getUser();
				if(user != null){
						return user.getFull_name();
				}
				return id;
		}
		public boolean equals(Object o) {
				if (o instanceof Employee) {
						Employee c = (Employee) o;
						if ( this.id.equals(c.getId())) 
								return true;
				}
				return false;
		}
		public int hashCode(){
				int seed = 31;
				if(!id.equals("")){
						try{
								seed += Integer.parseInt(id)*47;
						}catch(Exception ex){
								// we ignore
						}
				}
				return seed;
		}		
		public List<Group> getGroups(){
				if(groups == null && !id.equals("")){
						GroupList gl = new GroupList(id);
						gl.setActiveOnly();
						if(!pay_period_id.equals("")){
								gl.setPay_period_id(pay_period_id);
						}
						String back = gl.find();
						if(back.equals("")){
								List<Group> ggs = gl.getGroups();
								if(ggs.size() > 0){
										groups = ggs;
								}
						}
				}
				return groups;
		}

		public boolean canApprove(){
				GroupManagerList gml = new GroupManagerList(id);
				gml.setActiveOnly();
				gml.setApproversOnly();
				String back = gml.find();
				if(back.equals("")){
						List<GroupManager> ones = gml.getManagers();
						if(ones != null){
								managers = ones;
						}
				}
				return managers != null && managers.size() > 0;
		}
		public boolean canPayrollProcess(){
				GroupManagerList gml = new GroupManagerList(id);
				gml.setActiveOnly();
				gml.setProcessorsOnly();
				String back = gml.find();
				if(back.equals("")){
						List<GroupManager> ones = gml.getManagers();
						if(ones != null){
								managers = ones;
						}
				}
				return managers != null && managers.size() > 0;
		}
		public boolean canReview(){
				GroupManagerList gml = new GroupManagerList(id);
				gml.setActiveOnly();
				gml.setReviewersOnly();
				String back = gml.find();
				if(back.equals("")){
						List<GroupManager> ones = gml.getManagers();
						if(ones != null){
								managers = ones;
						}
				}
				return managers != null && managers.size() > 0;
		}
		public boolean canDataEntry(){
				GroupManagerList gml = new GroupManagerList(id);
				gml.setActiveOnly();
				gml.setDataEntryOnly();
				String back = gml.find();
				if(back.equals("")){
						List<GroupManager> ones = gml.getManagers();
						if(ones != null){
								managers = ones;
						}
				}
				return managers != null && managers.size() > 0;
		}		
		//
		public boolean hasDepartment(){
				getDepartment();
				return department != null;
		}
		public Department getDepartment(){
				if(department == null && !id.equals("")){
						DepartmentEmployeeList del = new DepartmentEmployeeList(id);
						if(!pay_period_id.equals("")){
								del.setPay_period_id(pay_period_id);
						}
						else
								del.setActiveOnly();
						String back = del.find();
						if(back.equals("")){
								List<DepartmentEmployee> des = del.getDepartmentEmployees();
								if(des != null && des.size() > 0){
										DepartmentEmployee one = des.get(0);// first
										department = one.getDepartment();
								}
						}
				}
				return department;
		}
		public boolean hasDepartments(){
				getDepartmentEmployees();
				return departmentEmployees != null;
		}		
		public List<DepartmentEmployee> getDepartmentEmployees(){
				if(departmentEmployees == null && !id.equals("")){
						DepartmentEmployeeList del = new DepartmentEmployeeList(id);
						// we want all
						String back = del.find();
						if(back.equals("")){
								List<DepartmentEmployee> des = del.getDepartmentEmployees();
								if(des != null && des.size() > 0){
										departmentEmployees = des;
										for(DepartmentEmployee one:des){
												if(one.isActive()){
														departmentEmployee = one; // active one
														break;
												}
										}
								}
						}
				}
				return departmentEmployees;
		}
		public boolean hasActiveDepartment(){
				if(hasDepartments()){
						return departmentEmployee != null;
				}
				return false;
		}
		public String getCurrentDepartment_id(){
				if(hasActiveDepartment()){
						department_id = departmentEmployee.getDepartment_id();
				}
				return department_id;
		}
		public List<GroupEmployee> getGroupEmployees(){
				if(groupEmployees == null && !id.equals("")){
						GroupEmployeeList del = new GroupEmployeeList(id);
						// we want all
						String back = del.find();
						if(back.equals("")){
								List<GroupEmployee> des = del.getGroupEmployees();
								if(des != null && des.size() > 0){
										groupEmployees = des;
										for(GroupEmployee one:des){
												if(one.isActive() && one.isCurrent()){
														groupEmployee = one;
														break;
												}
										}
								}
						}
				}
				return groupEmployees;
		}
		public boolean hasActiveGroup(){
				getGroupEmployees();
				return groupEmployee != null;
		}
		public GroupEmployee getGroupEmployee(){
				return groupEmployee;
		}
		public String doSelect(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "select e.id,e.user_id,e.id_code,e.employee_number,e.inactive,u.id,u.username,u.first_name,u.last_name,u.role,u.inactive from employees e join users u on u.id = e.user_id where ";
				if(!id.equals("")){
						qq += " e.id = ? ";
				}
				else if(!user_id.equals("")){ // for time clock machines
						qq += " e.user_id = ? ";		
				}				
				else if(!id_code.equals("")){ // for time clock machines
						qq += " e.id_code = ? ";		
				}
				else{
						msg = "Employee info can not be found as no employee id is not set";
						return msg;
				}
				logger.debug(qq);
				try{
						con = Helper.getConnection();
						if(con != null){
								pstmt = con.prepareStatement(qq);
								if(!id.equals("")){
										pstmt.setString(1, id);
								}
								else if(!user_id.equals("")){
										pstmt.setString(1, user_id);
								}
								else{
										pstmt.setString(1, id_code);
								}
								rs = pstmt.executeQuery();
								//
								if(rs.next()){
										setVals(rs.getString(1),
														rs.getString(2),
														rs.getString(3),
														rs.getString(4),
														rs.getString(5) != null,
														rs.getString(6),
														rs.getString(7),
														rs.getString(8),
														rs.getString(9),
														rs.getString(10),
														rs.getString(11) != null);
								}
								else{
										msg = "Employee not found";
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
		/**
		 * data for user and employee are entered in the same form
		 * so we need to save user class first
		 */
		public String doSave(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;		
				String msg="", str="";
				inactive=""; // default
				if(user_id.equals("")){
						if(user != null){
								msg = user.doSave();
								if(msg.equals("")){
										user_id = user.getId();
								}
						}
				}
				String qq = " insert into employees values(0,?,?,?,?)";
				if(user_id.equals("")){
						msg = "user id is required";
						return msg;
				}
				try{
						con = Helper.getConnection();
						if(con == null){
								msg = "Could not connect to DB ";
								return msg;
						}
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
						Helper.databaseDisconnect(con, pstmt, rs);
				}
				msg = doSelect();
				return msg;
		}
		String setParams(PreparedStatement pstmt){
				String msg = "";
				int jj=1;
				try{
						pstmt.setString(jj++, user_id);
						if(id_code.equals(""))
								pstmt.setNull(jj++, Types.VARCHAR);
						else
								pstmt.setString(jj++, id_code);
						if(employee_number.equals(""))
								pstmt.setNull(jj++, Types.VARCHAR);
						else
								pstmt.setString(jj++, employee_number);						
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
				String qq = " update employees set user_id=?,id_code=?, employee_number=?, inactive=? where id=?";
				if(id.equals("")){
						msg = "id is required";
						return msg;
				}
				if(user_id.equals("")){
						msg = "user id is required";
						return msg;
				}				
				try{
						con = Helper.getConnection();
						if(con == null){
								msg = "Could not connect to DB ";
								return msg;
						}
						pstmt = con.prepareStatement(qq);
						msg = setParams(pstmt);
						pstmt.setString(5, id);
						pstmt.executeUpdate();
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(con, pstmt, rs);
				}
				if(user != null){
						msg = user.doUpdate();
				}
				msg = doSelect();
				return msg;
		}		
}
