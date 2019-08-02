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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.util.Helper;
import in.bloomington.timer.util.CommonInc;
import in.bloomington.timer.util.UnoConnect;
import in.bloomington.timer.bean.*;

public class EmployeeList extends CommonInc{

    static final long serialVersionUID = 1160L;
    static Logger logger = LogManager.getLogger(EmployeeList.class);
    String id = "", username="", name="",
				full_name="", group_id="", group_ids="", id_code="", 
				exclude_group_id="", groupManager_id="", department_id="",
				dept_ref_id="", // one or more values
				employee_number="",  exclude_name="",
				pay_period_id="",
				employee_ids = "", // comma separated
				no_document_for_payperiod_id="", added_status="";
    Set<String> group_id_set = new HashSet<>();
    boolean active_only = false, inactive_only = false,
				hasEmployeeNumber=false, hasNoEmployeeNumber=false;
		boolean exclude_recent_records = false, recent_records_only=false;
    boolean includeAllDirectors = false, include_future = false;
    boolean used_time_track = false; // since last two weeks
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
    public String getAdded_status(){
				if(added_status.equals(""))
						return "-1";
				return added_status;
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
    public void setId_code(String val){
				if(val != null)
						id_code = val;
    }
    public void setEmployee_ids(String val){
				if(val != null)
						employee_ids = val;
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
    public void setPay_period_id(String val){
				if(val != null)
						pay_period_id = val;
    }
    public void setActiveOnly(){
				active_only = true;
    }
    public void setActiveStatus(String val){
				if(val != null && !val.equals("-1")){
						if(val.equals("Active"))
								active_only = true;
						else{
								inactive_only = true;
						}
				}
    }
		public void setAdded_status(String val){
				if(val != null && !val.equals("-1")){
						added_status = val;
						if(val.equals("Recent"))
								recent_records_only = true;
						else{
								exclude_recent_records = true;
						}
				}
		}
    public void includeAllDirectors(){
				includeAllDirectors = true;
    }
    public void setHasEmployeeNumber(){
				hasEmployeeNumber = true;
    }
    public void setUsedTimeTrack(){
				used_time_track = true;
    }
		public void setHasNoEmployeeNumber(){
				hasNoEmployeeNumber = true;
		}
		// avoid record added within last 30 days
		public void excludeRecentRecords(){
				exclude_recent_records = true;
		}
		public void recentRecordsOnly(){
				recent_records_only = true;
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
    public void setExclude_name(String val){
				if(val != null)
						exclude_name = val;
    }
		public void setIncludeFuture(){
				include_future = true; // no need already taken care of
		}				
    public String find(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String qq = "select e.id,e.username,e.first_name,e.last_name,e.id_code,e.employee_number,e.email,e.roles,date_format(e.added_date,'%m/%d/%Y'),e.inactive from employees e ";				
				String qw = "";
				if(!id.equals("")){
						if(!qw.equals("")) qw += " and ";
						qw += " e.id = ? ";
				}
				else if(!id_code.equals("")){
						if(!qw.equals("")) qw += " and ";
						qw += " e.id_code = ? ";
				}
				else if(!employee_number.equals("")){
						if(!qw.equals("")) qw += " and ";
						qw += " e.employee_number = ? ";
				}				
				else if(!employee_ids.equals("")){
						if(!qw.equals("")) qw += " and ";
						qw += " e.id  in ("+employee_ids+") ";
				}
				else{
						if(!name.equals("")){
								if(!qw.equals("")) qw += " and ";
								qw += " (e.last_name like ? or e.first_name like ?)";
						}
						else if(!exclude_name.equals("")){
								if(!qw.equals("")) qw += " and ";								
								qw += " not (e.last_name like ? or e.first_name like ?)";
						}
						if(!username.equals("")){
								if(!qw.equals("")) qw += " and ";
								qw += " e.username like ? ";
						}
						if(!no_document_for_payperiod_id.equals("") || 
							 (!pay_period_id.equals("") && (!department_id.equals("") ||
																							!group_id.equals("")))){
								qq += ", pay_periods pd ";
								if(!qw.equals("")) qw += " and ";								
								qw += " pd.id=? ";
						}
						if(!department_id.equals("")){
								qq += ", department_employees de ";
								if(!qw.equals("")) qw += " and ";
								qw += " de.employee_id=e.id and ";
								//
								// city directors = 18, human resource = 4
								//
								if(includeAllDirectors){
										qw += " (de.department_id in(?,4,18) or de.department2_id in (?,18))";// all city directors dept=18								
								}
								else{
										qw += " (de.department_id = ? or de.department2_id=?)";
								}
								if(!pay_period_id.equals("")){
										qw += " and de.effective_date <= pd.start_date ";
										qw += " and (de.expire_date is null or de.expire_date >= pd.end_date )";
								}
						}
						if(!dept_ref_id.equals("")){
								qq += " join department_employees de on de.employee_id=e.id ";
								qq += " join departments dd on de.department_id=dd.id or de.department2_id=dd.id ";
								if(!qw.equals("")) qw += " and ";
								qw += " dd.ref_id in ("+dept_ref_id+") ";
						}
						if(hasEmployeeNumber){ // related to previous one
								if(!qw.equals("")) qw += " and ";								
								qw += " e.employee_number is not null";
						}
						else if(hasNoEmployeeNumber){
								if(!qw.equals("")) qw += " and ";								
								qw += " e.employee_number is null";
						}
						if(!group_ids.equals("")){
								qq += ", group_employees ge ";
								if(!qw.equals("")) qw += " and ";
								qw += " ge.employee_id=e.id and ge.group_id in ("+group_ids+") ";
								if(!pay_period_id.equals("") ||
									 !no_document_for_payperiod_id.equals("")){
										qw += " and ge.effective_date <= pd.start_date ";
										qw += " and (ge.expire_date is null or ge.expire_date >= pd.end_date )";
								}
						}
						if(!exclude_group_id.equals("")){
								if(!qw.equals("")) qw += " and ";
								qw += " e.id not in (select ge.employee_id from group_employees ge where ge.group_id = ?)";
						}
						if(!no_document_for_payperiod_id.equals("")){
								if(!qw.equals("")) qw += " and ";
								qw += " e.id not in (select td.employee_id from time_documents td where td.pay_period_id = pd.id)";						
						}
						if(!groupManager_id.equals("")){
								qq += ", group_managers gm ";
								if(!qw.equals("")) qw += " and ";
								qw += " gm.employee_id=e.id and gm.group_id = ? ";
						}
						if(used_time_track){
								if(!qw.equals("")) qw += " and ";								
								qw += " e.id in (select employee_id from time_documents where initiated > (NOW() - INTERVAL 14 DAY)) ";
						}
						if(exclude_recent_records){
								if(!qw.equals("")) qw += " and ";								
								qw += " e.added_date < (NOW() - INTERVAL 30 DAY) ";
						}
						else if(recent_records_only){
								if(!qw.equals("")) qw += " and ";								
								qw += " e.added_date >= (NOW() - INTERVAL 30 DAY) ";
						}
						if(active_only){
								if(!qw.equals("")) qw += " and ";
								qw += " e.inactive is null";
						}
						else if(inactive_only){
								if(!qw.equals("")) qw += " and ";
								qw += " e.inactive is not null";
						}
				}
				if(!qw.equals(""))
						qq += " where "+qw;
				qq += " order by e.last_name,e.first_name ";
				String back = "";
						
				logger.debug(qq);
				con = UnoConnect.getConnection();
				if(con == null){
						back = "Could not connect to DB ";
						return back;
				}
				try{
						pstmt = con.prepareStatement(qq);
						int jj = 1;
						if(!id.equals("")){
								pstmt.setString(jj++,id);
						}
						else if(!id_code.equals("")){
								pstmt.setString(jj++, id_code);
						}
						else if(!employee_number.equals("")){
								pstmt.setString(jj++, employee_number);
						}						
						else if(!employee_ids.equals("")){
								// nothing here
						}						
						else{
								if(!name.equals("")){
										pstmt.setString(jj++,name+"%");
										pstmt.setString(jj++,name+"%");										
								}
								else if(!exclude_name.equals("")){
										pstmt.setString(jj++,exclude_name);
										pstmt.setString(jj++,exclude_name);	
								}
								if(!username.equals("")){ // for auto_complete 
										pstmt.setString(jj++,username+"%");
								}
								if(!no_document_for_payperiod_id.equals("")){
										pstmt.setString(jj++, no_document_for_payperiod_id);
								}
								else if(!pay_period_id.equals("") &&
												(!department_id.equals("") ||
												 !group_id.equals(""))){
										pstmt.setString(jj++, pay_period_id);
								}
								if(!department_id.equals("")){
										pstmt.setString(jj++, department_id);
										pstmt.setString(jj++, department_id);
								}
								if(!exclude_group_id.equals("")){
										pstmt.setString(jj++, exclude_group_id);
								}
								if(!no_document_for_payperiod_id.equals("")){
										// pstmt.setString(jj++, no_document_for_payperiod_id);
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
																 rs.getString(5),
																 rs.getString(6),
																 rs.getString(7),
																 rs.getString(8),
																 rs.getString(9),
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
						Helper.databaseDisconnect(pstmt, rs);
						UnoConnect.databaseDisconnect(con);
				}
				return back;
    }
		/*

select e.id,e.username,e.first_name,e.last_name,e.id_code,e.inactive from employees e , pay_periods pd , department_employees de , group_employees ge  where  pd.id=564  and  de.employee_id=e.id and  (de.department_id = 36 or de.department2_id=36) and de.effective_date <= pd.start_date  and (de.expire_date is null or de.expire_date >= pd.end_date ) and  e.employee_number is not null and  ge.employee_id=e.id and ge.group_id in (110)  and ge.effective_date <= pd.start_date  and (ge.expire_date is null or ge.expire_date >= pd.end_date ) order by e.last_name,e.first_name



		 */

				
}






















































