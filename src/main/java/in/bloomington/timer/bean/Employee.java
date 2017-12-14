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
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Employee implements Serializable{

		static Logger logger = LogManager.getLogger(Employee.class);
		static final long serialVersionUID = 1150L;		
    private String id="", inactive="", id_code="", employee_number="",
				department_id="", group_id="",
				email="", role="",
				username="", full_name="", first_name="", last_name="",
				ldap_dept="";
		// normally this date is pay period start date
		String job_active_date = "", pay_period_id="", selected_job_id="";
		// User user = null;
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
				setUsername(val2);
    }
		// for new record
    public Employee(String val,
										String val2,
										String val3,
										String val4,
										String val5,
										String val6
								){
				setUsername(val);
				setFirst_name(val2);
				setLast_name(val3);
				setId_code(val4);
				setEmployee_number(val5);
				setEmail(val6);
    }		
    public Employee(String val,
										String val2,
										String val3,
										String val4,
										String val5,
										String val6,
										String val7,
										String val8,
										boolean val9
								){
				setVals(val, val2, val3, val4, val5, val6,
								val7, val8, val9);
    }
    void setVals(String val,
								 String val2,
								 String val3,
								 String val4,
								 String val5,
								 String val6,
								 String val7,
								 String val8,
								 boolean val9
								){
				setId(val);
				setUsername(val2);
				setFirst_name(val3);
				setLast_name(val4);
				setId_code(val5);
				setEmployee_number(val6);
				setEmail(val7);
				setRole(val8);
				setInactive(val9);				
    }
    //
    // getters
    //
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
    public String getUsername(){
				return username;
    }
    public String getFull_name(){
				if(full_name.equals("")){
						full_name = first_name;
						if(!full_name.equals("")) full_name += " ";
						full_name += last_name;
				}
				return full_name;
    }
		public String getFirst_name(){
				return first_name;
    }
		public String getLast_name(){
				return last_name;
    }
    public String getRole(){
				return role;
    }
		public String getEmail(){
				if(email.equals("") && !username.equals("")){
						email = username+CommonInc.emailStr;
				}
				return email;
		}
    public String getLdap_dept(){
				return ldap_dept;
    }		
    //
    // setters
    //
    public void setId (String val){
				if(val != null)
						id = val;
    }

    public void setId_code (String val){
				if(val != null)
						id_code = val.trim();
    }
    public void setEmployee_number(String val){
				if(val != null)
						employee_number = val.trim();
    }
    public void setUsername (String val){
				if(val != null)
						username = val;
    }

    public void setFirst_name (String val){
				if(val != null)
						first_name = val;
    }
    public void setLast_name (String val){
				if(val != null)
						last_name = val;
    }
    public void setEmail(String val){
				if(val != null)
						email = val;
    }
    public void setRole (String val){
				if(val != null && !val.equals("-1"))
						role = val;
    }
		// needed for new employee
    public void setDepartment_id(String val){
				if(val != null && !val.equals("-1"))
						department_id = val;
    }
		// needed for new employee
    public void setGroup_id(String val){
				if(val != null && !val.equals("-1"))
						group_id = val;
    }		
    public void setLdap_dept(String val){
				if(val != null && !val.equals("-1"))
						ldap_dept = val;
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
		public String getDepartment_id(){
				if(department_id.equals("")){
						getDepartment();
						if(department != null){
								department_id = department.getId();
						}
				}
				return department_id;
		}
		public String getGroup_id(){
				return group_id;
		}
    public void setInactive (boolean val){
				if(val)
						inactive = "y";
    }

		public String toString(){
				return getFull_name();
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
		public String getInfo(){
				String ret="";
				if(!id.equals("")){
						ret = "id = "+id+", ";
				}
				ret += "username = "+username;
				if(!first_name.equals("")){
						ret += ", first name = "+first_name;
				}
				if(!last_name.equals("")){
						ret += ", last name = "+last_name;
				}
				if(!employee_number.equals("")){
						ret += ", emp # = "+employee_number;
				}
				if(!id_code.equals("")){
						ret += ", id code = "+id_code;
				}
				return ret;
		}
		public boolean hasRole(String val){
				return role.indexOf(val) > -1;
		}
		public boolean hasNoRole(){
				return role.equals("");
		}
		public boolean isEmployee(){
				return hasRole("Employee");
		}
		public boolean isAdmin(){
				return hasRole("Admin");
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
		public String validate(){
				String msg = "";
				if(username.equals("")){
						msg = "username";
				}
				if(last_name.equals("")){
						if(!msg.equals("")) msg += ", ";
						msg += "last name";
				}
				if(!msg.equals("")){
						msg += " required but not set";
				}
				return msg;
		}
		public String doSelect(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "select e.id,e.username,e.first_name,e.last_name,e.id_code,e.employee_number,e.email,e.role,e.inactive from employees e where ";
				if(!id.equals("")){
						qq += " e.id = ? ";
				}
				else if(!username.equals("")){ // for login
						qq += " e.username like ? ";		
				}				
				else if(!id_code.equals("")){ // for punch clock machines
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
								else if(!username.equals("")){
										pstmt.setString(1, username);
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
														rs.getString(5),
														rs.getString(6),
														rs.getString(7),
														rs.getString(8),
														rs.getString(9) != null);
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
				msg = validate();
				if(!msg.equals("")){
						return msg;
				}
				String qq = " insert into employees values(0,?,?,?,?, ?,?,?,?)";
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
						pstmt.setString(jj++, username);
						if(first_name.equals(""))
								pstmt.setNull(jj++, Types.VARCHAR);
						else
								pstmt.setString(jj++, first_name);
						pstmt.setString(jj++, last_name);
						if(id_code.equals(""))
								pstmt.setNull(jj++, Types.VARCHAR);
						else
								pstmt.setString(jj++, id_code);
						if(employee_number.equals(""))
								pstmt.setNull(jj++, Types.VARCHAR);
						else
								pstmt.setString(jj++, employee_number);
						if(email.equals(""))
								getEmail();
						pstmt.setString(jj++, email);
						if(role.equals(""))
								role = "Employee";
						pstmt.setString(jj++, role);
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
				String qq = " update employees set username=?,first_name=?,last_name=?,id_code=?, employee_number=?, email=?,role=?,inactive=? where id=?";
				if(id.equals("")){
						msg = "id is required";
						return msg;
				}
				msg = validate();
				if(!msg.equals("")){
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
						pstmt.setString(9, id);
						pstmt.executeUpdate();
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
		 * change the iactive status of the following users to inactive
		 * this function is used by CurrentEmployeesHandler
		 */
		public String updateInactiveStatus(String idSet){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = " update employees set inactive='y' where id in ("+idSet+")";
				if(idSet == null || idSet.equals("")){
						return msg;
				}
				try{
						con = Helper.getConnection();
						if(con == null){
								msg = "Could not connect to DB ";
								return msg;
						}
						pstmt = con.prepareStatement(qq);
						pstmt.executeUpdate();
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
