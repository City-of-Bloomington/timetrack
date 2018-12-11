package in.bloomington.timer.list;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.ArrayList;
import java.util.List;
import java.text.*;
import java.util.Date;

import java.sql.*;
import java.text.SimpleDateFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;

public class HourCodeList{

		static final long serialVersionUID = 1000L;
		static Logger logger = LogManager.getLogger(HourCodeList.class);
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		List<HourCode> hourCodes = null;
		String department_id = "", salary_group_id="", effective_date_before="";
		String employee_id = "", accrual_id="";
		boolean active_only = false , default_regular_only = false;
		boolean current_only = false, related_to_accruals_only=false;
    public HourCodeList(){
    }
    public HourCodeList(String val, String val2){
				setDepartment_id(val);
				setSalary_group_id(val2);
    }		
    public void setDepartment_id(String val){
				if(val != null && !val.equals("-1"))
						department_id = val;
    }
    public void setSalary_group_id(String val){
				if(val != null && !val.equals("-1"))
						salary_group_id = val;
    }
    public void setEffective_date_before(String val){
				if(val != null)
						effective_date_before = val;
    }
    public void setEmployee_id(String val){
				if(val != null && !val.equals("-1"))
						employee_id = val;
    }
    public void setAccrual_id(String val){
				if(val != null && !val.equals("-1"))
						accrual_id = val;
    }		
		public void setActiveOnly(){
				active_only = true;
		}
		public void setCurrentOnly(){
				current_only = true;
		}
		public void relatedToAccrualsOnly(){
				related_to_accruals_only = true;
		}
		public void setDefaultRegularOnly(){
				default_regular_only = true; // needed for salary groups
		}
		public List<HourCode> getHourCodes(){
				return hourCodes;
		}
    //
    // for user listing
    //
		public String lookFor(){
				String back = "";
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				Connection con = null;
				if(employee_id.equals("") && (department_id.equals("") || salary_group_id.equals(""))){
						back = " employee not set or salary group not set";
				}
				if(employee_id.equals("")){
						if(department_id.equals("")){
						back = " department not set ";
						return back;
						}
						if(salary_group_id.equals("")){
								back = " salary grop not set ";
								return back;
						}
				}
				//
				// some hour codes are specific to certain departments
				// other are for non-specified department
				// 
				String qq = "select count(*) from hour_code_conditions c where c.department_id=? ";
				String qq2 = "select e.id,e.name,e.description,e.record_method,e.accrual_id,e.count_as_regular_pay,e.inactive,e.reg_default from hour_codes e left join hour_code_conditions c on c.hour_code_id=e.id ";
				String qw = "", msg="";
				logger.debug(qq);
				boolean setDept = false;
				con = UnoConnect.getConnection();
				if(con == null){
						back = " Could not connect to DB ";
						return back;
				}				
				try{
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, department_id);
						rs = pstmt.executeQuery();
						if(rs.next()){
								int cnt = rs.getInt(1);
								if(cnt > 0){
										setDept = true;
								}
						}
						if(current_only){
								if(!qw.equals("")) qw += " and "; 
								qw += " e.inactive is null ";
						}
						else if(!effective_date_before.equals("")){
								if(!qw.equals("")) qw += " and "; 
								qw += " c.date <= ? ";
						}
						if(!employee_id.equals("")){
								qq +=" join jobs j on j.salary_group_id=c.salary_group_id ";
								qq += " join department_employees de on de.employee_id=j.employee_id and c.department_id=de.department_id ";
								if(!qw.equals("")) qw += " and "; 
								qw += " j.employee_id = ? ";										
						}
						else{								
								if(!department_id.equals("")){
										if(!qw.equals("")) qw += " and "; 
										qw += " (c.department_id = ? or c.department_id is null)";
								}
								if(!salary_group_id.equals("")){
										if(!qw.equals("")) qw += " and "; 
										qw += " c.salary_group_id = ? ";
								}
						}
						if(!qw.equals("")){
								qw = " where "+qw;
						}
						qw += " order by e.reg_default,e.name";
						qq = qq2+qw;
						logger.debug(qq);								
						pstmt = con.prepareStatement(qq);
						//
						int jj=1;
						if(current_only){
						}
						else if(!effective_date_before.equals("")){
								java.util.Date date_tmp = df.parse(effective_date_before);
								pstmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
						}
						if(!employee_id.equals("")){
								pstmt.setString(jj++, employee_id);
						}
						else{
								if(!department_id.equals("")){
										pstmt.setString(jj++, department_id);
								}
								if(!salary_group_id.equals("")){
										pstmt.setString(jj++, salary_group_id);
								}
						}
						rs = pstmt.executeQuery();
						hourCodes = new ArrayList<>();
						while(rs.next()){
								HourCode one = new HourCode(rs.getString(1),
																						rs.getString(2),
																						rs.getString(3),
																						rs.getString(4),
																						rs.getString(5),
																						rs.getString(6) != null,
																						rs.getString(7) != null,
																						rs.getString(8));
								if(!hourCodes.contains(one))
											 hourCodes.add(one);
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
    //
    // for global listing (setting for example)
    //
		public String find(){
				String back = "";
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				Connection con = null;
				//
				// some hour codes are specific to certain departments
				// other are for all department
				// 
				String qq = "select e.id,e.name,e.description,e.record_method,e.accrual_id,e.count_as_regular_pay,e.inactive,e.reg_default from hour_codes e left join hour_code_conditions c on c.hour_code_id=e.id ";
				String qw = "", msg="";
				con = UnoConnect.getConnection();
				if(con == null){
						back = " Could not connect to DB ";
						return back;
				}							
				try{
						if(current_only){
								if(!qw.equals("")) qw += " and "; 
								qw += " e.inactive is null ";
						}
						else if(!effective_date_before.equals("")){
								if(!qw.equals("")) qw += " and "; 
								qw += " c.date <= ? ";
						}
						if(related_to_accruals_only){
								if(!qw.equals("")) qw += " and "; 
								qw += " e.accrual_id is not null ";
						}
						if(!accrual_id.equals("")){
								if(!qw.equals("")) qw += " and "; 
								qw += " e.accrual_id = ?  ";
						}						
						if(default_regular_only){
								if(!qw.equals("")) qw += " and "; 
								qw += " e.reg_default=0 "; // everything else is 1
						}
						if(!qw.equals("")){
								qw = " where "+qw;
						}
						qw += " order by e.name";
						qq += qw;
						logger.debug(qq);
						pstmt = con.prepareStatement(qq);
						//
						int jj=1;
						if(current_only){
						}
						if(!accrual_id.equals("")){
								pstmt.setString(jj++, accrual_id);
						}									
						rs = pstmt.executeQuery();
						hourCodes = new ArrayList<>();
						while(rs.next()){
								HourCode one = new HourCode(rs.getString(1),
																						rs.getString(2),
																						rs.getString(3),
																						rs.getString(4),
																						rs.getString(5),
																						rs.getString(6) != null,
																						rs.getString(7) != null,
																						rs.getString(8));
								if(!hourCodes.contains(one))
										hourCodes.add(one);
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
