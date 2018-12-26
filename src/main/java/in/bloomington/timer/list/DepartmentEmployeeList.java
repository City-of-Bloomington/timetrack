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
		static final long serialVersionUID = 600L;
		static Logger logger = LogManager.getLogger(DepartmentEmployeeList.class);
		boolean active_only = false;
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
    public void setDepartment_id (String val){
				if(val != null)
						department_id = val;
    }
    public void setPay_period_id (String val){
				if(val != null)
						pay_period_id = val;
    }
		//
		// active and current
		public void setActiveOnly(){
				active_only = true;
		}
		public List<DepartmentEmployee> getDepartmentEmployees(){
				return departmentEmployees;
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
						// " expire_date < now() "+
						" from department_employees de ";
				if(!employee_id.equals("")){
						qw += " de.employee_id=? ";
				}
				if(!department_id.equals("")){
						if(!qw.equals("")) qw += " and ";
						qw += " (de.department_id=? or de.department2_id=?)";
				}
				if(!pay_period_id.equals("")){
						qq += ", pay_periods pp ";
						if(!qw.equals("")) qw += " and ";
						qw += " de.effective_date <= pp.start_date and (de.expire_date is null or de.expire_date >= pp.end_date) and pp.id=? ";		
				}
				if(active_only){
						if(!qw.equals("")) qw += " and ";
						qw += " de.effective_date < now() and (de.expire_date is null or de.expire_date > now())";		
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
						if(!department_id.equals("")){
								pstmt.setString(jj++, department_id);
								pstmt.setString(jj++, department_id);								
						}
						if(!pay_period_id.equals("")){
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
