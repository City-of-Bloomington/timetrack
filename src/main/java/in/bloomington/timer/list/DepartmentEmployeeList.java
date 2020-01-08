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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;

public class DepartmentEmployeeList{

		String employee_id = "", department_id="", pay_period_id="";
		String id_code="", employee_number="";
		static final long serialVersionUID = 600L;
		static Logger logger = LogManager.getLogger(DepartmentEmployeeList.class);
		boolean active_only = false, no_expire_date=false;
		boolean emp_active_only = false, inactive_only = false;
		boolean include_future = false;
		List<DepartmentEmployee> departmentEmployees = null;
    public DepartmentEmployeeList(){
    }
    public DepartmentEmployeeList(String val){
				setEmployee_id(val);
    }		
    public void setEmployee_id (String val){
				if(val != null)
						employee_id = val;
    }
    public void setId_code(String val){
				if(val != null)
						id_code = val;
    }
    public void setEmployee_number(String val){
				if(val != null)
						employee_number = val;
    }				
    public void setDepartment_id (String val){
				if(val != null && !val.equals("-1"))
						department_id = val;
    }
    public void setPay_period_id (String val){
				if(val != null && !val.equals("-1"))
						pay_period_id = val;
    }
    public void setName(String val){
				// needed for auto complete but not used
    }
		public String getEmployee_id(){
				return employee_id;
		}
		public String getDepartment_id(){
				return department_id;
		}
		public String getId_code(){
				return id_code;
		}
		public String getEmployee_number(){
				return employee_number;
		}		
    public String getPay_period_id (){
				return pay_period_id;
    }		
		//
		// active and current
		public void setActiveOnly(){
				active_only = true;
		}
		public void setEmployeeActiveOnly(){
				emp_active_only = true;
		}		
		public void setNoExpireDate(){
				no_expire_date = true;
		}		
		public List<DepartmentEmployee> getDepartmentEmployees(){
				return departmentEmployees;
		}
		public void setIncludeFuture(){
				include_future = true;
		}
    public String getActiveStatus(){
				if(active_only)
						return "Active";
				if(inactive_only)
						return "Inactive";
				return "-1";
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
    //
    // getters
    //
		public String find(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", qw="";
				String qq = "select de.id,de.employee_id,de.department_id,"+
						" de.department2_id,"+
						" date_format(de.effective_date,'%m/%d/%Y'),"+
						" date_format(de.expire_date,'%m/%d/%Y') "+
						" from department_employees de "+
						" join employees e on de.employee_id=e.id ";
				if(!employee_id.equals("")){
						qw += " de.employee_id=? ";
				}
				else if(!id_code.equals("")){
						qw += " e.id_code=? ";
				}
				else if(!employee_number.equals("")){
						qw += " e.employee_number = ? ";
				}
				if(!department_id.equals("")){
						if(!qw.equals("")) qw += " and ";
						qw += " (de.department_id=? or de.department2_id=?)";
				}
				if(include_future){
						if(!qw.equals("")) qw += " and ";
						qw += " (de.expire_date is null or de.expire_date >= curdate())";
				}
				else if(!pay_period_id.equals("")){
						qq += ", pay_periods pp ";
						if(!qw.equals("")) qw += " and ";
						qw += " de.effective_date <= pp.start_date and (de.expire_date is null or de.expire_date >= pp.end_date) and pp.id=? ";		
				}
				if(no_expire_date){
						if(!qw.equals("")) qw += " and ";
						qw += " de.effective_date < curdate() and de.expire_date is null";		
				}				
				else if(active_only){
						if(!qw.equals("")) qw += " and ";
						qw += " de.effective_date < curdate() and (de.expire_date is null or de.expire_date > curdate())";		
				}
				else if(inactive_only){
						if(!qw.equals("")) qw += " and ";
						qw += " de.expire_date < curdate() ";	
				}
				if(emp_active_only){
						if(!qw.equals("")) qw += " and ";						
						qw += " e.inactive is null ";
				}
				con = UnoConnect.getConnection();
				if(con == null){
						msg = " Could not connect to DB ";
						logger.error(msg);
						return msg;
				}
				if(!qw.equals("")){
						qq += " where "+qw;
				}
				logger.debug(qq);
				try{
						pstmt = con.prepareStatement(qq);
						int jj=1;
						if(!employee_id.equals("")){
								pstmt.setString(jj++, employee_id);
						}
						else if(!id_code.equals("")){
								pstmt.setString(jj++, id_code);
						}
						else if(!employee_number.equals("")){
								pstmt.setString(jj++, employee_number);
						}						
						if(!department_id.equals("")){
								pstmt.setString(jj++, department_id);
								pstmt.setString(jj++, department_id);								
						}
						if(!include_future && !pay_period_id.equals("")){
								pstmt.setString(jj++, pay_period_id);
						}						
						rs = pstmt.executeQuery();
						while(rs.next()){
								if(departmentEmployees == null)
										departmentEmployees = new ArrayList<>();
								DepartmentEmployee one =
										new DepartmentEmployee(
																			 rs.getString(1),
																			 rs.getString(2),
																			 rs.getString(3),
																			 rs.getString(4),
																			 rs.getString(5),
																			 rs.getString(6));
								if(!departmentEmployees.contains(one))
										departmentEmployees.add(one);
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
