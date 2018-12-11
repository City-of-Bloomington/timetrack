package in.bloomington.timer.leave;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.sql.*;
import javax.sql.*;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;
import in.bloomington.timer.bean.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LeaveDocument{

		boolean debug = false;
		static Logger logger = LogManager.getLogger(LeaveDocument.class);
		static final long serialVersionUID = 2400L;
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		DecimalFormat dfn = new DecimalFormat("##0.00");
    private String id="", employee_id="", pay_period_id="",
				initiated="",initiated_by="", selected_job_id="";
		private String job_id="";
		PayPeriod payPeriod = null;
		Employee employee = null;
		Employee initiater = null;
		JobTask job = null;
		List<JobTask> jobs = null;
		SalaryGroup salaryGroup = null;
		List<EmployeeAccrual> employeeAccruals = null;
		HolidayList holidays = null;
		List<LeaveBlock> leaveBlocks = null;
		Map<Integer, List<LeaveBlock>> dailyBlocks = new TreeMap<>();		
		/*

		Map<String, Map<Integer, String>> daily = null;				

		Map<String, List<String>> allAccruals = new TreeMap<>();
		Map<Integer, Double> hourCodeTotals = null;
		Map<Integer, Double> usedAccrualTotals = null;

		*/
    public LeaveDocument(String val,
												 String val2,
												 String val3,
												 String val4,
												 String val5,
												 String val6
												 ){
				setId(val);
				setEmployee_id(val2);				
				setPay_period_id(val3);
				setJob_id(val4);
				setInitiated(val5);
				setInitiated_by(val6);

    }
    public LeaveDocument(String val){
				setId(val);
    }
    public LeaveDocument(){
    }		
    //
    // getters
    //
    public String getEmployee_id(){
				return employee_id;
    }
    public String getPay_period_id(){
				return pay_period_id;
    }
    public String getJob_id(){
				return job_id;
    }		
    public String getInitiated(){
				return initiated;
    }
    public String getInitiated_by(){
				return initiated_by;
    }		
		public String getId(){
				return id;
    }
    //
    // setters
    //
    public void setId (String val){
				if(val != null)
						id = val;
    }
    public void setPay_period_id (String val){
				if(val != null && !val.equals("-1"))
						pay_period_id = val;
    }
    public void setJob_id (String val){
				if(val != null && !val.equals("-1"))
						job_id = val;
    }		
    public void setEmployee_id(String val){
				if(val != null && !val.equals("-1"))						
						employee_id = val;
    }
    public void setInitiated(String val){
				if(val != null)
						initiated = val;
    }
    public void setInitiated_by(String val){
				if(val != null)
						initiated_by = val;
    }
		public String toString(){
				return id;
		}

		public boolean equals(Object o) {
				if (o instanceof LeaveDocument) {
						LeaveDocument c = (LeaveDocument) o;
						if ( this.id.equals(c.getId())) 
								return true;
				}
				return false;
		}
		public int hashCode(){
				int seed = 37;
				if(!id.equals("")){
						try{
								seed += Integer.parseInt(id)*31;
						}catch(Exception ex){
								// we ignore
						}
				}
				return seed;
		}
		public PayPeriod getPayPeriod(){
				if(!pay_period_id.equals("") && payPeriod == null){
						PayPeriod one = new PayPeriod(pay_period_id);
						String back = one.doSelect();
						if(back.equals("")){
								payPeriod = one;
						}
				}
				return payPeriod;
		}				
		public Employee getEmployee(){
				if(!employee_id.equals("") && employee == null){
						Employee one = new Employee(employee_id);
						String back = one.doSelect();
						if(back.equals("")){
								employee = one;
						}
				}
				return employee;
		}
		public Employee getInitiater(){
				if(!initiated_by.equals("") && initiater == null){
						Employee one = new Employee(initiated_by);
						String back = one.doSelect();
						if(back.equals("")){
								initiater = one;
						}
				}
				return initiater;
		}
		public JobTask getJob(){
				if(!job_id.equals("") && job == null){
						JobTask one = new JobTask(job_id);
						String back = one.doSelect();
						if(back.equals("")){
							 job = one;
						}
				}
				return job;
		}		
		public List<JobTask> getJobs(){
				if(jobs == null){
						JobTaskList jl = new JobTaskList(employee_id);
						jl.setPay_period_id(pay_period_id);
						String back = jl.find();
						if(back.equals("")){
								List<JobTask> ones = jl.getJobTasks();
								if(ones != null && ones.size() > 0){
										jobs = ones; // get one
								}
						}
				}
				return jobs;
		}		
		public boolean hasJob(){
				getJob();
				return job != null;
		}
		public SalaryGroup getSalaryGroup(){
				if(salaryGroup == null){
						getJob();
						if(job != null){
								salaryGroup = job.getSalaryGroup();
						}
				}
				return salaryGroup;
		}
		public boolean isExempt(){
				if(salaryGroup == null)
						getSalaryGroup();
				if(salaryGroup != null){
						return salaryGroup.isExempt();
				}
				return false;
		}
		public boolean isTemporary(){
				if(salaryGroup == null)
						getSalaryGroup();
				if(salaryGroup != null){
						return salaryGroup.isTemporary();
				}
				return false;
		}
		public boolean isPartTime(){
				if(salaryGroup == null)
						getSalaryGroup();
				if(salaryGroup != null){
						return salaryGroup.isPartTime();
				}
				return false;
		}		
		public boolean isUnionned(){
				if(salaryGroup == null)
						getSalaryGroup();
				if(salaryGroup != null){
						return salaryGroup.isUnionned();
				}
				return false;
		}
		public List<LeaveBlock> getLeaveBlocks(){
				if(!id.equals("") && leaveBlocks == null){
						LeaveBlockList lbl = new LeaveBlockList();
						lbl.setDocument_id(id);
						String back = lbl.find();
						if(back.equals("")){
								List<LeaveBlock> ones = lbl.getLeaveBlocks();
								if(ones != null && ones.size() > 0){
										leaveBlocks = ones;
										dailyBlocks = lbl.getDailyBlocks();
								}
						}
				}
				return leaveBlocks;
		}
		/*
		 * ToDo need to be modified to work with future
		 * request
		public List<EmployeeAccrual> getEmpAccruals(){

				if(employeeAccruals == null){
						EmployeeAccrualList al = new EmployeeAccrualList();						
						al.setDocument_id(id);
						String back = al.find();
						if(back.equals("")){
								List<EmployeeAccrual> ones = al.getEmployeeAccruals();
								if(ones != null && ones.size() > 0){
										employeeAccruals = ones;
								}
						}
						// now we adjust the totals see below
						findHourCodeTotals();
						findUsedAccruals();
						if(usedAccrualTotals != null){
								Set<Integer> set = usedAccrualTotals.keySet();
								for(Integer key:set){
										Double val = usedAccrualTotals.get(key);
								}
						}
						adjustAccruals();
						checkForWarnings();
				}
				return employeeAccruals;
		}
		*/
		// short description of employee accrual balance as of now
		/*
		public String getEmployeeAccrualsShort(){
				String ret = "";
				if(hasAllAccruals()){
						for(EmployeeAccrual one:employeeAccruals){
								if(one.getHours() > 0){
										String str = one.getAccrual().getName();
										if(ret.indexOf(str) == -1){
												if(!ret.equals("")) ret += ", ";
												ret += str+": "+dfn.format(one.getHours());
										}
								}
						}
				}
				return ret;
		}
		public void findUsedAccruals(){
				if(usedAccrualTotals == null){
						TimeBlockList tl = new TimeBlockList();
						tl.setActiveOnly();
						tl.setDocument_id(id);
						String back = tl.findUsedAccruals();
						if(back.equals("")){
								usedAccrualTotals = tl.getUsedAccrualTotals();
						}
						else{
								logger.error(back);
						}
				}
		}
		public void findHourCodeTotals(){
				if(hourCodeTotals == null){
						TimeBlockList tl = new TimeBlockList();
						tl.setActiveOnly();
						tl.setDocument_id(id);
						String back = tl.find();
						if(back.equals("")){
								hourCodeTotals = tl.getHourCodeTotals();
						}
				}
		}		
		//
		public void adjustAccruals(){
				if(employeeAccruals != null && usedAccrualTotals != null){
						for(EmployeeAccrual one: employeeAccruals){
								String accrual_id = one.getAccrual_id();
								Accrual accrual = one.getAccrual();
								List<String> list = new ArrayList<>();
								String accName = accrual.getName();
								String accDesc = accrual.getDescription();
								if(accDesc == null) accDesc="";
								String codeInfo = accName+": "+accDesc;
								//
								// String related_id = one.getRelated_hour_code_id();
								if(accrual_id != null && !accrual_id.equals("")){
										double hrs_total = one.getHours();
										list.add(""+dfn.format(hrs_total));
										try{
												int cd_id = Integer.parseInt(accrual_id);
												if(usedAccrualTotals.containsKey(cd_id)){
														double hrs_used = usedAccrualTotals.get(cd_id);
														list.add(""+dfn.format(hrs_used));
														if(hrs_total > hrs_used){
																hrs_total -= hrs_used;
														}
														else{
																hrs_total = 0.0;
														}
														one.setHours(hrs_total);
												}
												else{
														list.add("0.00"); // nothing used
												}
												list.add(""+dfn.format(hrs_total)); // adjusted
												if(accrual.hasPref_max_leval()){
														if(hrs_total > accrual.getPref_max_level()){
																String str = "Your "+accrual.getName()+": "+accrual.getDescription()+" balance is "+dfn.format(hrs_total)+" and currently exceeds the city target balance. Please use Comp Time Accrued instead of PTO until this balance is reduced to no more than "+accrual.getPref_max_level()+" hours.";
																if(!warnings.contains(str))
																		warnings.add(str);
														}
												}
												allAccruals.put(codeInfo, list);
										}catch(Exception ex){
												logger.error(ex);
										}
								}
						}
				}
		}
		*/
		void prepareHolidays(){
				HolidayList hl = new HolidayList(debug);
				if(!pay_period_id.equals("")){
						hl.setPay_period_id(pay_period_id);
				}
				String back = hl.find();
				if(back.equals("")){
						holidays = hl;
				}
		}
		public boolean isHoliday(String date){
				if(holidays != null){
						return holidays.isHoliday(date);
				}
				return false;
		}
		public String getHolidayName(String date){
				if(holidays != null){
						return holidays.getHolidayName(date);
				}
				return "";
		}		
		public String doSelect(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "select id,employee_id,pay_period_id,job_id,date_format(initiated,'%m/%d/%Y %H;%i'),initiated_by from leave_documents where id =? ";
				logger.debug(qq);
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could do not get connection to DB";
						return msg;
				}
				try{
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, id);
						rs = pstmt.executeQuery();
						if(rs.next()){
								setEmployee_id(rs.getString(2));
								setPay_period_id(rs.getString(3));
								setJob_id(rs.getString(4));
								setInitiated(rs.getString(5));
								setInitiated_by(rs.getString(6));
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
		// save only, no updates
		//
		public String doSave(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "insert into leave_documents values(0,?,?,?,now(),?) ";
				if(employee_id.equals("")){
						msg = " employee ID not set ";
						return msg;
				}
				if(pay_period_id.equals("")){
						msg = " pay period not set ";
						return msg;
				}
				if(job_id.equals("")){
						msg = " job not set ";
						return msg;
				}				
				if(initiated_by.equals("")){
						msg = " initiater not set ";
						return msg;
				}				
				logger.debug(qq);
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could do not get connection to DB";
						return msg;
				}				
				try{
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, employee_id);
						pstmt.setString(2, pay_period_id);
						pstmt.setString(3, job_id);
						pstmt.setString(4, initiated_by);
						pstmt.executeUpdate();
						Helper.databaseDisconnect(pstmt, rs);						
						//
						qq = "select LAST_INSERT_ID()";
						pstmt = con.prepareStatement(qq);
						rs = pstmt.executeQuery();
						if(rs.next()){
								id = rs.getString(1);
						}
						//
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
