package in.bloomington.timer.list;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.sql.*;
import java.io.*;
import javax.naming.*;
import javax.naming.directory.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;

public class EmployeeList extends CommonInc{

		static final long serialVersionUID = 1160L;
		static Logger logger = Logger.getLogger(EmployeeList.class);
    String id = "", user_id="", username="", name="",
				full_name="", group_id="", group_ids="", id_code="", 
				exclude_group_id="", groupManager_id="", department_id="",
				dept_ref_id="", // one or more values
				employee_number="",
				no_document_for_payperiod_id="";
		Set<String> group_id_set = new HashSet<>();
		boolean active_only = false, inactive_only = false, hasEmployeeNumber=false;
		boolean includeAllDirectors = false;
		List<Employee> employees = null;
		List<Group> groups = null;
    //
    // basic constructor
    public EmployeeList(){

    }
    public EmployeeList(String val){

				setName(val);
    }
		public String getId(){
				return id;
		}
		public String getEmployee_number(){
				return employee_number;
		}
		public String getId_code(){
				return id_code;
		}
		public String getActiveStatus(){
				if(active_only)
						return "Active";
				if(inactive_only)
						return "Inactive";
				return "-1";
		}
		public String getGroup_id(){
				if(group_id.equals(""))
						return "-1";
				return group_id;
		}
		public String getDepartment_id(){
				if(department_id.equals(""))
						return "-1";
				return department_id;
		}
		public String getName(){
				return name;
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
						name = val;
		}
		public void setUsername(String val){
				if(val != null)
						username = val;
		}
		public void setUser_id(String val){
				if(val != null)
						user_id = val;
		}
		public void setId_code(String val){
				if(val != null)
						id_code = val;
		}
		public void setEmployee_number(String val){
				if(val != null)
					 employee_number = val;
		}		
		public void setDept_ref_id(String val){
				if(val != null)
						dept_ref_id = val;
		}
		public void setDepartment_id(String val){
				if(val != null && !val.equals("-1"))
						department_id = val;
		}		
		public void setGroup_id(String val){
				if(val != null && !val.equals("-1")){
						group_id = val; // for the last
						if(!group_id_set.contains(val)){
								group_id_set.add(val);
								if(!group_ids.equals("")) group_ids += ",";
								group_ids += val;
						}
				}
		}

		public void setGroupManager_id(String val){
				if(val != null)
						groupManager_id = val;
		}		
		public void setExclude_group_id(String val){
				if(val != null)
						exclude_group_id = val;
		}
		public void setNoDocumentForPayPeriodId(String val){
				if(val != null)
						no_document_for_payperiod_id = val;
		}
		public void setActiveOnly(){
				active_only = true;
		}
		public void setActiveStatus(String val){
				if(val != null && !val.equals("-1")){
						if(val.equals("Active"))
								active_only = true;
						else if(val.equals("Inactive")){
								inactive_only = true;
						}
				}
		}
		public void includeAllDirectors(){
				includeAllDirectors = true;
		}
		public void setHasEmployeeNumber(){
				hasEmployeeNumber = true;
		}
		public List<Employee> getEmployees(){
				return employees;
		}
		public List<Group> getGroups(){
				if(groups == null && !department_id.equals("")){
						GroupList tl = new GroupList();
						tl.setDepartment_id(department_id);
						tl.setActiveOnly();
						String back = tl.find();
						if(back.equals("")){
								List<Group> ones = tl.getGroups();
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
    public String find(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String qq = "select e.id,e.user_id,e.id_code,e.employee_number,e.inactive,u.id,u.username,u.first_name,u.last_name,u.role,u.inactive from employees e join users u on u.id = e.user_id ";				
				String qw = "";
				if(!id.equals("")){
						if(!qw.equals("")) qw += " and ";
						qw += " e.id = ? ";
				}
				else if(!id_code.equals("")){
						if(!qw.equals("")) qw += " and ";
						qw += " e.id_code = ? ";
				}
				else{
						if(!name.equals("")){
								if(!qw.equals("")) qw += " and ";
								qw += " (u.last_name like ? or u.first_name like ?)";
						}
						if(!username.equals("")){
								if(!qw.equals("")) qw += " and ";
								qw += " u.username like ? ";
						}
						if(!user_id.equals("")){
								if(!qw.equals("")) qw += " and ";
						qw += " e.user_id = ? ";
						}
						if(!department_id.equals("")){
								qq += " join department_employees de on de.employee_id=e.id  ";
								if(!qw.equals("")) qw += " and ";
								if(includeAllDirectors){
										qw += " de.department_id in(?,18) ";// all city directors dept=18								
								}
								else{
										qw += " de.department_id = ? ";
								}
						}
						if(!dept_ref_id.equals("")){
								qq += " join department_employees de on de.employee_id=e.id  ";
								qq += " join departments dd on de.department_id=dd.id ";
								if(!qw.equals("")) qw += " and ";
								qw += " dd.ref_id in (?) ";
						}
						if(hasEmployeeNumber){ // related to previous one
								if(!qw.equals("")) qw += " and ";								
								qw += " e.employee_number is not null";
						}
						if(!group_ids.equals("")){
								qq += " join group_employees ge on ge.employee_id=e.id  ";
								if(!qw.equals("")) qw += " and ";
								qw += " ge.group_id in ("+group_ids+") ";
						}
						if(!exclude_group_id.equals("")){
								if(!qw.equals("")) qw += " and ";
								qw += " e.id not in (select ge.employee_id from group_employees ge where ge.group_id = ?)";
						}
						if(!no_document_for_payperiod_id.equals("")){
								if(!qw.equals("")) qw += " and ";
								qw += " e.id not in (select td.employee_id from time_documents td where td.pay_period_id = ?)";						
						}
						if(!groupManager_id.equals("")){
								qq += " join group_managers gm on gm.employee_id=e.id ";
								if(!qw.equals("")) qw += " and ";
								qw += " gm.group_id = ? ";
						}				
						if(active_only){
								if(!qw.equals("")) qw += " and ";
								qw += " u.inactive is null ";
						}
						else if(inactive_only){
								if(!qw.equals("")) qw += " and ";
								qw += " u.inactive is not null ";
						}
				}
				if(!qw.equals(""))
						qq += " where "+qw;
				qq += " order by u.first_name,u.last_name ";
				// System.err.println(qq);
				String back = "";
				try{
						logger.debug(qq);
						con = Helper.getConnection();
						if(con == null){
								back = "Could not connect to DB ";
								return back;
						}
						pstmt = con.prepareStatement(qq);
						int jj = 1;
						if(!id.equals("")){
								pstmt.setString(jj++,id);
						}
						else if(!id_code.equals("")){
								pstmt.setString(jj++,id_code);
						}
						else{
								if(!name.equals("")){
										pstmt.setString(jj++,name+"%");
										pstmt.setString(jj++,name+"%");										
								}
								if(!username.equals("")){ // for auto_complete 
										pstmt.setString(jj++,username+"%");
								}
								if(!user_id.equals("")){
										pstmt.setString(jj++,user_id);
								}
								if(!department_id.equals("")){
										pstmt.setString(jj++, department_id);
								}
								if(!dept_ref_id.equals("")){
										pstmt.setString(jj++, dept_ref_id);
								}
								if(!exclude_group_id.equals("")){
										pstmt.setString(jj++, exclude_group_id);
								}
								if(!no_document_for_payperiod_id.equals("")){
										pstmt.setString(jj++, no_document_for_payperiod_id);
								}
								if(!groupManager_id.equals("")){
										pstmt.setString(jj++, groupManager_id);
								}
						}
						rs = pstmt.executeQuery();
						while(rs.next()){
								if(employees == null)
										employees = new ArrayList<>();
								Employee employee =
										new Employee(rs.getString(1),
																 rs.getString(2),
																 rs.getString(3),
																 rs.getString(4),
																 rs.getString(5) != null,
																 rs.getString(6),
																 rs.getString(7),
																 rs.getString(8),
																 rs.getString(9),
																 rs.getString(10),
																 rs.getString(10) != null
																 );
								if(!employees.contains(employee))
										employees.add(employee);
						}
				}
				catch(Exception ex){
						back += ex;
						logger.error(ex+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(con, pstmt, rs);
				}
				return back;
    }
}





















































