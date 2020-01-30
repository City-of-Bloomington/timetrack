package in.bloomington.timer;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.*;
import java.sql.*;
import java.text.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import javax.servlet.ServletContext;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HandleEmployeesUpdate{

		boolean debug = false;
		//
		// added this flag so that we do not run this function
		// on test servers very frequently
		//
		boolean run_employee_update = true;
		static final long serialVersionUID = 53L;
		static Logger logger = LogManager.getLogger(HandleEmployeesUpdate.class);
		EnvBean envBean = null;
		String date="", pay_period_id="";
		Hashtable<String, Employee> empTable = null, empUsrTable=null;
    public HandleEmployeesUpdate(){
				setAllFlags();
    }		
    public HandleEmployeesUpdate(EnvBean val){
				if(val != null)
						envBean = val;
				setAllFlags();
    }
		
    //
    // setters
    //
    public void setDate(String val){
				if(val != null){		
						date = val;
				}
    }
    public String getPay_period_id(){
				//
				if(pay_period_id.isEmpty()){
						PayPeriodList ppl = new PayPeriodList();
						ppl.currentOnly();
						String back = ppl.find();
						if(back.isEmpty()){
								List<PayPeriod> ones = ppl.getPeriods();
								if(ones != null && ones.size() > 0){
									 PayPeriod payPeriod = ones.get(0);
									 pay_period_id = payPeriod.getId();
								}
						}
				}
				return pay_period_id;
    }
		void setAllFlags(){
				ServletContext ctx = SingleContextHolder.getContext();
				if(ctx != null){
						String val = ctx.getInitParameter("run_employee_update");
						if(val != null){
								if(val.equals("false")){
										run_employee_update = false;
								}
						}
						if(envBean == null){
								envBean = new EnvBean();
								val = ctx.getInitParameter("ldap_url");
								if(val != null)
										envBean.setUrl(val);
								val = ctx.getInitParameter("ldap_principle");
								if(val != null)
										envBean.setPrinciple(val);
								val = ctx.getInitParameter("ldap_password");
								if(val != null)
								envBean.setPassword(val);
						}
						System.err.println(" emps run update flag "+run_employee_update);
				}
				else{
						System.err.println(" ctx is null, could not retreive flags ");
				}
		}		
		//
		// find employees in AD and check if their info changed during 
		// the previous time span (normally 2 hours on regular busniss day
		// check the related schedule for crown day and time schedules
		//
   public String process(){
			 String msg = "", status="Success", errors="";
			 
			 if(!run_employee_update){
					 System.err.println(" update skipped");
					 return msg;
			 }
			 List<Employee> dbEmps = null;
				List<Employee> ldapEmps = null;
				Hashtable<String, Employee> empTable = null;
				String inactiveSet = "", inactiveLog = "";
				EmployeeList ul = new EmployeeList();
				ul.setExclude_name("System");// exlude admin emp
				// ul.setHasAdSid();
				//
				String back = ul.find();
				if(back.isEmpty()){
						List<Employee> ones = ul.getEmployees();
						if(ones != null && ones.size() > 0){
								dbEmps = ones;
						}
				}
				else{
						msg = back;
						status = "Failure";
						errors = "Error get employee list from DB "+msg;
				}
				if(msg.isEmpty()){
						EmpList el = new EmpList(envBean);
						back = el.find();
						if(back.isEmpty()){
								ldapEmps = el.getEmps();
								empTable = new Hashtable<>();
								for(Employee one:ldapEmps){
										empTable.put(one.getAd_sid(), one);
										empTable.put(one.getUsername(), one);
								}
						}
						else{
								msg = back;
								status = "Failure";
								errors = "Error get emps from ldap "+msg;								
						}
				}
				if(msg.isEmpty()){
						// changes
						int jj=1;
						for(Employee one:dbEmps){
								if(one.hasAdSid()){
										if(empTable.containsKey(one.getAd_sid())){
												Employee ldapEmp = empTable.get(one.getAd_sid());
												if(!one.isSameEntity(ldapEmp)){
														System.err.println(" emp using AdSid "+one.getInfo());
														System.err.println((jj++)+" found "+ldapEmp.getInfo());
														// System.err.println(" update "+one.getInfo());
														// System.err.println(" to => "+ldapEmp.getInfo());
														msg += one.doUpdateFromLdap(ldapEmp);
												}
										}
								}
								else{
										if(empTable.containsKey(one.getUsername())){
												System.err.println("found emp "+one.getUsername());
												Employee ldapEmp = empTable.get(one.getUsername());

												if(!one.isSameEntity(ldapEmp)){
														System.err.println(" emp using username "+one.getInfo());
														System.err.println((jj++)+" found "+ldapEmp.getInfo());
														msg += one.doUpdateFromLdap(ldapEmp);
												}										
										}
								}
						}
						//
						//
						/*
							// we do not need this feature any more
						if(inactiveSet.isEmpty()){
								System.err.println(" No employees found that need change ");
						}
						else{
								System.err.println(" The following id list of inactive employees "+inactiveSet);
								Employee emp = new Employee();
								msg = emp.updateInactiveStatus(inactiveSet);
								if(!msg.isEmpty()){
										status = "Failure";
										errors =" error updating emps status "+msg;
								}
								EmployeesLog ll = new EmployeesLog(inactiveLog, status, errors);
								msg = ll.doSave();								
						}
						*/
				}
				return msg;
		}
		//
		// we run this one time only and not needed after that
		// for adding AD sid value
		//
   public String process2(){
			 Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg = "", status="Success", errors="";
				List<Employee> dbEmps = null;
				List<Employee> ldapEmps = null;
				Hashtable<String, Employee> empTable = null;
				String inactiveSet = "", inactiveLog = "";
				EmployeeList ul = new EmployeeList();
				// ul.setActiveOnly();
				ul.setExclude_name("System");// exlude admin emp
				//
				String back = ul.find();
				if(back.isEmpty()){
						List<Employee> ones = ul.getEmployees();
						if(ones != null && ones.size() > 0){
								dbEmps = ones;
						}
				}
				else{
						msg = back;
						status = "Failure";
						errors = "Error get employee list from DB "+msg;
				}
				if(msg.isEmpty()){
						EmpList el = new EmpList(envBean);
						back = el.find();
						if(back.isEmpty()){
								ldapEmps = el.getEmps();
								empUsrTable = new Hashtable<>();
								for(Employee one:ldapEmps){
										empUsrTable.put(one.getUsername(), one);
								}
						}
						else{
								msg = back;
								System.err.println(back);
						}
				}
				if(msg.isEmpty()){
						//
						String qq = " update employees set ad_sid=? where id=?";						
						con = UnoConnect.getConnection();
						try{
								pstmt = con.prepareStatement(qq);						
								for(Employee one:dbEmps){
										if(empUsrTable.containsKey(one.getUsername())){
												Employee emp = empUsrTable.get(one.getUsername());
												if(emp != null){
														String emp_id = one.getId();
														String ad_sid = emp.getAd_sid();
														System.err.println(emp_id+" "+ad_sid);
														if(ad_sid != null && emp_id != null){
																pstmt.setString(1, ad_sid);
																pstmt.setString(2, emp_id);
																pstmt.executeUpdate();
														}
												}
										}
								}
						}
						catch(Exception ex){
								System.err.println(" "+ex);
						}
						finally{
								Helper.databaseDisconnect(con, pstmt, rs);
						}
				}
				return msg;
		}


}
