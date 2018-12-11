package in.bloomington.timer.list;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.sql.*;
import java.text.SimpleDateFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;

public class EmployeeAccrualList{

		static final long serialVersionUID = 200L;
		static Logger logger = LogManager.getLogger(EmployeeAccrualList.class);
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		String employee_id ="", pay_period_id="", date_from="", date_to="";
		String document_id = "";
		String date=""; // target date
		boolean most_current = false;
		List<EmployeeAccrual> employeeAccruals = null;
    public EmployeeAccrualList(){

    }
    public EmployeeAccrualList(String val, String val2){
				setEmployee_id(val);
				setPay_period_id(val2);
    }
    public EmployeeAccrualList(String val, String val2, String val3){
				setEmployee_id(val);
				setDate_from(val2);
				setDate_to(val3);
    }		
    public void setEmployee_id (String val){
				if(val != null)
						employee_id = val;
    }
    public void setDocument_id (String val){
				if(val != null)
						document_id = val;
    }		
			// this is needed for old history
    public void setPay_period_id (String val){
				if(val != null)
						pay_period_id = val;
    }
    public void setDate_from(String val){
				if(val != null)
						date_from = val;
    }		
    public void setDate_to(String val){
				if(val != null)
						date_to = val;
    }
    public void setDate(String val){
				if(val != null)
						date = val;
    }		
		public void setMostCurrent(){
				most_current = true;
		}
		public List<EmployeeAccrual> getEmployeeAccruals(){
				return employeeAccruals;
		}
		/**
		 *
			select a.id,a.accrual_id,ec.id,a.employee_id,a.hours,date_format(a.date,'%m/%d/%Y'),t.name,t.pref_max_level,ec.name from employee_accruals a join accruals t on t.id=a.accrual_id join hour_codes ec on ec.accrual_id=a.accrual_id                             join time_documents d on d.employee_id=a.employee_id join pay_periods p on p.id = d.pay_period_id                                                              where a.date = (select a2.date from employee_accruals a2 where a2.date < p.start_date and a2.employee_id=d.employee_id order by a2.date desc limit 1) and d.id = 2;
		 
		 */
		 
			 
		public String find(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", qw="";
				String qq = "select a.id,a.accrual_id,ec.id,a.employee_id,a.hours,date_format(a.date,'%m/%d/%Y'),t.name,t.description,t.pref_max_level,ec.name from employee_accruals a join accruals t on t.id=a.accrual_id join hour_codes ec on ec.accrual_id=a.accrual_id ";
				if(!document_id.equals("")){
						qq += " join time_documents d on d.employee_id=a.employee_id ";
						qq += " join pay_periods p on p.id = d.pay_period_id ";						
						if(!qw.equals("")) qw += " and "; // the last date
						qw += " a.date = (select a2.date from employee_accruals a2 where a2.date < p.start_date and a2.employee_id=d.employee_id order by a2.date desc limit 1) ";
						qw += " and d.id = ? ";
				}				
				else if(!employee_id.equals("")){
						if(!qw.equals("")) qw += " and ";				
						qw += " a.employee_id = ? ";
						if(most_current){
								// we are looking for the last one
								if(!qw.equals("")) qw += " and ";								
								qw += " a.date = (select a2.date from employee_accruals a2 where a2.accrual_id = a.accrual_id order by a2.date desc limit 1) ";
						}
						else if(!pay_period_id.equals("")){
								qq += ", pay_periods p ";
								if(!qw.equals("")) qw += " and ";
								qw += " p.id=? and a.date <= p.start_date ";
						}
				}
				else{
						// this will be the usual use
						if(!date.equals("")){
								if(!qw.equals("")) qw += " and ";													
								qw += " a.date=? ";
						}
						else {
								if(!date_from.equals("")){
										if(!qw.equals("")) qw += " and ";								
										qw += " a.date >= ? ";
								}
								if(!date_to.equals("")){
										if(!qw.equals("")) qw += " and ";
										qw += " a.date <= ? ";
								}
						}
				}
				if(!qw.equals("")){
						qq += " where "+qw;
				}
				logger.debug(qq);
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could not connect to DB ";
						return msg;
				}				
				try{
						pstmt = con.prepareStatement(qq);
						int jj=1;
						if(!document_id.equals("")){
								pstmt.setString(jj++, document_id);
						}								
						else if(!employee_id.equals("")){
								pstmt.setString(jj++, employee_id);
								if(most_current){
										// 
								}
								else if(!pay_period_id.equals("")){
										pstmt.setString(jj++, pay_period_id);										
								}
								else{
										if(!date.equals("")){
												java.util.Date date_tmp = df.parse(date);
												pstmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
										}
										else {
												if(!date_from.equals("")){
														java.util.Date date_tmp = df.parse(date_from);
														pstmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
												}
												if(!date_to.equals("")){
														java.util.Date date_tmp = df.parse(date_to);
														pstmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
												}
										}
								}																		
						}
						rs = pstmt.executeQuery();
						//
						while(rs.next()){
								EmployeeAccrual one =
										new EmployeeAccrual(rs.getString(1),
																				rs.getString(2),
																				rs.getString(3),
																				rs.getString(4),
																				rs.getDouble(5),
																				rs.getString(6),
																				rs.getString(7),
																				rs.getString(8),
																				rs.getInt(9),
																				rs.getString(10));
								if(employeeAccruals == null)
										employeeAccruals = new ArrayList<>();
								employeeAccruals.add(one);
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
