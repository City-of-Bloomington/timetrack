package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.Serializable;
import java.sql.*;
import java.util.*;
import java.text.SimpleDateFormat;
import in.bloomington.timer.util.*;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GroupEmployee extends CommonInc implements Serializable{

		static final long serialVersionUID = 2000L;
		static Logger logger = LogManager.getLogger(GroupEmployee.class);
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");		
    String group_id="", employee_id="", effective_date="", expire_date="",
				inactive="", id="";
		// to change group
		//
		String new_group_id="";
		//
		Group group = null;
		Employee employee = null;
		PayPeriod payPeriod = null, changePayPeriod=null;
		//
		public GroupEmployee(){
				super();
		}
		// for new record
		public GroupEmployee(String val, String val2, String val3){
				setGroup_id(val);
				setEmployee_id(val2);
				setEffective_date(val3);
		}		
		public GroupEmployee(String val,
												 String val2,
												 String val3,
												 String val4,
												 String val5,
												 boolean val6
												 ){
				setVals(val, val2, val3, val4, val5, val6);
    }
    void setVals(String val,
								 String val2,
								 String val3,
								 String val4,
								 String val5,
								 boolean val6
								){
				setId(val);
				setGroup_id(val2);				
				setEmployee_id(val3);
				setEffective_date(val4);
				setExpire_date(val5);
				setInactive(val6);
		}
		public GroupEmployee(String val){
				//
				setId(val);
    }

		public String getId(){
				return id;
		}
		public String getEmployee_id(){
				return employee_id;
		}
		public String getGroup_id(){
				return group_id;
		}
		public String getExpire_date(){
				return expire_date;
		}
		public String getEffective_date(){
				return effective_date;
		}
		public String getNew_group_id(){
				return new_group_id;
		}		
		public boolean isActive(){
				return inactive.isEmpty();
		}		
		public boolean isInactive(){
				return !inactive.isEmpty();
		}
		public void setId(String val){
				if(val != null && !val.equals("-1"))
						id = val;
		}		
		public void setGroup_id(String val){
				if(val != null && !val.equals("-1"))
						group_id = val;
		}
		public void setEmployee_id(String val){
				if(val != null && !val.equals("-1"))
						employee_id = val;
		}
		public void setNew_group_id(String val){
				if(val != null && !val.equals("-1"))
						new_group_id = val;
		}		
		public void setEffective_date(String val){
				if(val != null && !val.equals("-1"))
						effective_date = val;
		}
		public void setExpire_date(String val){
				if(val != null && !val.equals("-1"))
						expire_date = val;
		}
		public boolean hasExpireDate(){
				return !expire_date.isEmpty();
		}

		public void setInactive(boolean val){
				if(val)
						inactive = "y";
		}
		public boolean isCurrent(){
				return expire_date.isEmpty();
		}
		public Employee getEmployee(){
				if(employee == null && !employee_id.isEmpty()){
						Employee dd = new Employee(employee_id);
						String back = dd.doSelect();
						if(back.isEmpty()){
								employee = dd;
						}
				}
				return employee;				
		}
		public Group getGroup(){
				if(group == null && !group_id.isEmpty()){
						Group dd = new Group(group_id);
						String back = dd.doSelect();
						if(back.isEmpty()){
								group = dd;
						}
				}
				return group;				
		}
		// needed for changing employee group
		// if the employee has not started data entry
		// using this group, then we can change the group
		// easily without the need to go through two step change
		public boolean hasTimeData(){
				if(!employee_id.isEmpty() && !id.isEmpty()){					 
						DocumentList dl = new DocumentList(employee_id);
						String back = dl.find();
						if(back.isEmpty()){
								List<Document> ones = dl.getDocuments();
								return (ones != null && ones.size() > 0);
						}
				}
				return false;
		}
		// exlude group
		//
    public String doSave(){
				String msg = "";
				Connection con = null;
				PreparedStatement pstmt = null, pstmt2=null, pstmt3=null;
				ResultSet rs = null;
				String qc = " select count(*) from group_employees where expire_date is null and group_id=? and employee_id=?";
				String qq = " insert into group_employees values(0,?,?,?,?,null) "; 
				if(employee_id.isEmpty()){
						msg = "employee not set ";
						return msg;
				}
				if(group_id.isEmpty() && !new_group_id.isEmpty()){
						group_id = new_group_id;
				}
				if(group_id.isEmpty()){
						msg = "group not set ";
						return msg;
				}
				if(debug){
						logger.debug(qq);
				}
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could not connect to DB";
						return msg;
				}			
				try{
						int cnt = 0;						
						pstmt = con.prepareStatement(qc);
						pstmt.setString(1, group_id);
						pstmt.setString(2, employee_id);
						rs = pstmt.executeQuery();
						if(rs.next()){
								cnt = rs.getInt(1);
						}
						if(cnt > 0){
								msg = " Employee is already in this group ";
								return msg;
						}
						else{
								pstmt2 = con.prepareStatement(qq);
								pstmt2.setString(1, group_id);
								pstmt2.setString(2, employee_id);								
								if(effective_date.isEmpty())
										effective_date = Helper.getToday();
								java.util.Date date_tmp = df.parse(effective_date);
								pstmt2.setDate(3, new java.sql.Date(date_tmp.getTime()));
								if(expire_date.isEmpty())
										pstmt2.setNull(4, Types.DATE);
								else{
										date_tmp = df.parse(expire_date);
										pstmt2.setDate(4, new java.sql.Date(date_tmp.getTime()));
								}
								pstmt2.executeUpdate();
								//
								qq = "select LAST_INSERT_ID()";
								pstmt3 = con.prepareStatement(qq);
								rs = pstmt3.executeQuery();
								if(rs.next()){
										id = rs.getString(1);
								}
						}
				}catch(Exception ex){
						logger.error(ex+" : "+qq);
						msg += " "+ex;
				}
				finally{
						Helper.databaseDisconnect(rs, pstmt, pstmt2, pstmt3);
						UnoConnect.databaseDisconnect(con);
				}
				return msg;
    }
		public boolean hasOneGroupOnly(){
				GroupEmployeeList gel = new GroupEmployeeList();
				gel.setEmployee_id(employee_id);
				getPayPeriod(); // current only
				gel.setPay_period_id(payPeriod.getId()); 
				String back = gel.find();
				if(back.isEmpty()){
						List<GroupEmployee> ones = gel.getGroupEmployees();
						return ones != null && ones.size() == 1;
				}
				return false;
		}
		public boolean hasOneJobOnly(){
				getEmployee();
				getPayPeriod();
				if(employee != null){
						if(employee != null)
								employee.setPay_period_id(payPeriod.getId());
				}
				return employee != null && employee.hasOneJobOnly();
		}
		//
    public String doUpdate(){
				String msg = "";
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String qq = "update group_employees set group_id=?,employee_id=?,effective_date=?,expire_date=?,inactive=? where id=? ";				
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could not connect to DB";
						return msg;
				}			
				try{
						pstmt = con.prepareStatement(qq);
						if(new_group_id.isEmpty()){
								new_group_id = group_id;
						}
						pstmt.setString(1, new_group_id);														
						pstmt.setString(2, employee_id);
						java.util.Date date_tmp = df.parse(effective_date);
						pstmt.setDate(3, new java.sql.Date(date_tmp.getTime()));
						if(expire_date.isEmpty())
								pstmt.setNull(4, Types.DATE);
						else{
								date_tmp = df.parse(expire_date);
								pstmt.setDate(4, new java.sql.Date(date_tmp.getTime()));
						}
						if(inactive.isEmpty())
								pstmt.setNull(5, Types.CHAR);
						else{
								pstmt.setString(5,"y");
						}						
						pstmt.setString(6, id);
						pstmt.executeUpdate();
				}catch(Exception ex){
						logger.error(ex+" : "+qq);
						msg += " "+ex;
				}
				finally{
						Helper.databaseDisconnect(pstmt, rs);
						UnoConnect.databaseDisconnect(con);
				}
				if(!group_id.equals(new_group_id)){
						checkEmployeeJobGroup();
				}
				return msg;
    }
		public PayPeriod getPayPeriod(){
				//
				if(payPeriod == null){
						PayPeriodList ppl = new PayPeriodList();
						ppl.currentOnly();
						String back = ppl.find();
						if(back.isEmpty()){
								List<PayPeriod> ones = ppl.getPeriods();
								if(ones != null && ones.size() > 0){
										payPeriod = ones.get(0);
								}
						}
				}
				return payPeriod;
		}
		/**
		 * if employee has one group only then we can change jobs group,
		 * this happens when an employee was assigned to a wrong group
		 * and we want to assign him to the right one, to preserve old
		 * date we are not changing the dates on employee group or job
		 */
		void checkEmployeeJobGroup(){
				getEmployee();
				if(employee != null){
						if(payPeriod != null)
								employee.setPay_period_id(payPeriod.getId());
						List<JobTask> jobs = employee.getJobs();
						if(jobs != null && jobs.size() > 0){
								for(JobTask job:jobs){
										// change jobs that are linked to the old group only
										if(job.getGroup_id().equals(group_id)){
												job.setGroup_id(new_group_id);
												job.doUpdate();
										}
								}
						}
				}
				group_id = new_group_id;
		}
    public String doChange(){
				// getChangePayPeriod();
				String date = effective_date; // changePayPeriod.getStart_date();
				String exp_date = Helper.getDateFrom(date, -1); // one day before
				String msg = "";
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String qq = "update group_employees set expire_date=? where id=? ";				
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could not connect to DB";
						return msg;
				}			
				try{
						pstmt = con.prepareStatement(qq);
						java.util.Date date_tmp = df.parse(exp_date);
						pstmt.setDate(1, new java.sql.Date(date_tmp.getTime()));
						pstmt.setString(2, id);
						pstmt.executeUpdate();
				}catch(Exception ex){
						logger.error(ex+" : "+qq);
						msg += " "+ex;
				}
				finally{
						Helper.databaseDisconnect(pstmt, rs);
						UnoConnect.databaseDisconnect(con);
				}
				if(msg.isEmpty()){
						msg = doExpireRelatedGroupJobs();
				}
				return msg;
    }
		String doExpireRelatedGroupJobs(){
				getEmployee();
				// getChangePayPeriod();
				String date = effective_date;// changePayPeriod.getStart_date();
				String exp_date = Helper.getDateFrom(date, -1);
				if(employee != null){
						if(payPeriod != null){
								employee.setPay_period_id(payPeriod.getId());
						}
						List<JobTask> jobs = employee.getJobs();
						if(jobs != null && jobs.size() > 0){
								for(JobTask job:jobs){
										if(job.getGroup_id().equals(group_id)){
												job.setExpire_date(exp_date);
												job.doUpdate();
										}
								}
						}
				}
				id = "";
				// effective_date=date;
				group_id = new_group_id;
				return doSave();
		}
		public String doSelect(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "select id,group_id,employee_id,"+
						" date_format(effective_date,'%m/%d/%Y'),"+
						" date_format(expire_date,'%m/%d/%Y'),inactive "+
						" from group_employees where id=?";
				if(id.isEmpty()){
						msg = " no id set ";
						return msg;
				}
				logger.debug(qq);
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could not connect to DB";
						return msg;
				}							
				try{
						pstmt = con.prepareStatement(qq);
						if(!id.isEmpty()){
								pstmt.setString(1, id);
						}
						rs = pstmt.executeQuery();
						//
						if(rs.next()){
								setVals(rs.getString(1),
												rs.getString(2),
												rs.getString(3),
												rs.getString(4),
												rs.getString(5),
												rs.getString(6) != null);
						}
						else{
								msg = "Department Employee not found";
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
