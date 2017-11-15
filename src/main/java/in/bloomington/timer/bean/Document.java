package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.sql.*;
import javax.sql.*;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;
import org.apache.log4j.Logger;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;

public class Document{

		static Logger logger = Logger.getLogger(Document.class);
		static final long serialVersionUID = 2400L;
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		DecimalFormat dfn = new DecimalFormat("###.00");
    private String id="", employee_id="", pay_period_id="",
				initiated="",initiated_by="", selected_job_id="";
		double week1Total = 0, week2Total = 0;
		PayPeriod payPeriod = null;
		Employee employee = null;
		User initiater = null;
		TimeAction lastTimeAcion = null;
		Workflow lastWorkflow = null;
		List<TimeAction> timeActions = null;
		Map<Integer, Double> daily = null;
		List<TimeBlock> timeBlocks = null;
		List<String> warnings = new ArrayList<>();
		JobTask jobTask = null;
		Map<String, List<Double>> allAccruals = new TreeMap<>();
		Map<Integer, Double> hourCodeTotals = null;
		Map<Integer, Double> usedAccrualTotals = null;
		Map<String, Double> hourCodeWeek1 = null;
		Map<String, Double> hourCodeWeek2 = null;
		Map<Integer, List<TimeBlock>> dailyBlocks = null;
		List<EmployeeAccrual> employeeAccruals = null;
		List<TimeNote> timeNotes = null;
		List<TimeIssue> timeIssues = null;		
		Map<String, AccrualWarning> warningMap = new TreeMap<>();		
    public Document(String val,
										String val2,
										String val3,
										String val4,
										String val5
							 ){
				setId(val);
				setEmployee_id(val2);				
				setPay_period_id(val3);
				setInitiated(val4);
				setInitiated_by(val5);
				fillWarningMap();

    }
    public Document(String val){
				setId(val);
				fillWarningMap();
    }
    public Document(){
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
				if (o instanceof Document) {
						Document c = (Document) o;
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
		public User getInitiater(){
				if(!initiated_by.equals("") && initiater == null){
						User one = new User(initiated_by);
						String back = one.doSelect();
						if(back.equals("")){
								initiater = one;
						}
				}
				return initiater;
		}
		public List<TimeAction> getTimeActions(){
				if(timeActions == null && !id.equals("")){
						TimeActionList tal = new TimeActionList(id);
						tal.setSortby("a.id desc");
						String back = tal.find();
						if(back.equals("")){
								List<TimeAction> ones = tal.getTimeActions();
								if(ones != null && ones.size() > 0){
										timeActions = ones;
								}
						}
				}
				return timeActions;
		}
		public boolean hasTimeActions(){
				getTimeActions();
				return timeActions != null && timeActions.size() > 0;
		}
		public TimeAction getLastTimeAction(){
				if(lastTimeAcion == null){
						if(hasTimeActions()){
								lastTimeAcion = timeActions.get(0);
						}
				}
				return lastTimeAcion;
		}
		public boolean hasLastTimeAction(){
				getLastTimeAction();
				return lastTimeAcion != null;
		}
		public boolean hasLastWorkflow(){
				getLastWorkflow();
				return lastWorkflow != null;
		}
		public Workflow getLastWorkflow(){
				if(hasLastTimeAction()){
						lastWorkflow = lastTimeAcion.getWorkflow();
				}
				return lastWorkflow;
		}		
		private void fillWarningMap(){
				AccrualWarningList tl = new AccrualWarningList();
				String back = tl.find();
				if(back.equals("")){
						List<AccrualWarning> ones = tl.getAccrualWarnings();
						if(ones != null && ones.size() > 0){
								for(AccrualWarning one:ones){
										warningMap.put(one.getHourCode().getName(), one);
								}
						}
				}
		}		
		public boolean canBeApproved(){
				getLastTimeAction();
				if(lastTimeAcion != null){
						Workflow lastWorkFlow = lastTimeAcion.getWorkflow();
						return lastWorkFlow != null && lastWorkFlow.canApprove();
				}
				return false;
		}
		public boolean isApproved(){
				getTimeActions();
				if(timeActions != null){
						for(TimeAction one:timeActions){ 
								Workflow wf = one.getWorkflow();
								if(wf.isApproved()){
										return true;
								}
						}
				}
				return false;
		}
		public boolean isProcessed(){
				getTimeActions();
				if(timeActions != null){
						for(TimeAction one:timeActions){ 
								Workflow wf = one.getWorkflow();
								if(wf.isProcessed()){
										return true;
								}
						}
				}
				return false;
		}
		public boolean isSubmitted(){
				getTimeActions();
				if(timeActions != null){
						for(TimeAction one:timeActions){ 
								Workflow wf = one.getWorkflow();
								if(wf.isSubmitted()){
										return true;
								}
						}
				}
				return false;
		}		
		public boolean canBeProcessed(){
				return isApproved() && !isProcessed();
		}
				
		public boolean hasDaily(){
				getDaily();
				return daily != null && daily.size() > 0;
		}
		public Map<Integer, Double> getDaily(boolean includeEmptyBlocks){
				if(daily == null && !id.equals("")){
						TimeBlockList tl = new TimeBlockList();
						tl.setDocument_id(id);
						tl.setActiveOnly();
						String back = tl.find();
						if(back.equals("")){
								Map<Integer, Double> ones = tl.getDaily();
								if(ones != null && ones.size() > 0){
										daily = ones;
										dailyBlocks = tl.getDailyBlocks();
										hourCodeTotals = tl.getHourCodeTotals();
										hourCodeWeek1 = tl.getHourCodeWeek1();
										hourCodeWeek2 = tl.getHourCodeWeek2();
										List<TimeBlock> ones2 = tl.getTimeBlocks();
										if(ones2 != null && ones2.size() > 0){
												timeBlocks = ones2;
										}
								}
						}
						if(includeEmptyBlocks){
								fillTwoWeekEmptyBlocks();
						}
						getEmpAccruals();
				}
				return daily;
		}
		public Map<Integer, Double> getDaily(){
				//
				// include empty blocks as well
				//
				return getDaily(true);
		}
		
		public List<TimeBlock> getTimeBlocks(){
				if(timeBlocks == null){
						getDaily();
				}
				if(timeBlocks == null){
						timeBlocks = new ArrayList<>();
						dailyBlocks = new TreeMap<>();
				}
				return timeBlocks;
		}
		public double getWeek1TotalDbl(){
				if(week1Total == 0){
						if(daily != null){
								for(int i=0;i<7;i++){
										week1Total += daily.get(i);
								}
						}
				}
				return week1Total;
		}
		public double getWeek2TotalDbl(){
				if(week2Total == 0){
						if(daily != null){
								for(int i=7;i<14;i++){
										week2Total += daily.get(i);
								}
						}
				}
				return week2Total;
		}		
				
		public String getWeek1Total(){
				getWeek1TotalDbl();
				return ""+dfn.format(week1Total);
		}
		public String getWeek2Total(){
				getWeek2TotalDbl();
				return ""+dfn.format(week2Total);
		}
		public String getPayPeriodTotal(){
				double ret = week1Total+week2Total;
				return ""+dfn.format(ret);
		}
		// we need at least one job to find some info
		// about the employee
		public JobTask getJobTask(){
				if(jobTask == null){
						JobTaskList jl = new JobTaskList(employee_id);
						jl.setPay_period_id(pay_period_id);
						String back = jl.find();
						if(back.equals("")){
								List<JobTask> ones = jl.getJobTasks();
								if(ones != null && ones.size() > 0){
										jobTask = ones.get(0); // get one
								}
						}
				}
				return jobTask;
		}				
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
						adjustAccruals();
						checkForWarnings();
				}
				return employeeAccruals;
		}
		// short description of employee accrual balance as of now
		public String getEmployeeAccrualsShort(){
				String ret = "";
				if(hasAllAccruals()){
						for(EmployeeAccrual one:employeeAccruals){
								if(one.getHours() > 0){
										if(!ret.equals("")) ret += ", ";
										ret += one.getAccrual().getName()+":"+one.getHours();
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
		public boolean hasDailyBlocks(){
				return dailyBlocks != null;
		}
		public boolean hasTimeBlocks(){
				return timeBlocks != null;
		}
		public boolean hasHourCodeTotals(){
				return hourCodeTotals != null;
		}
		public boolean hasAllAccruals(){
				if(allAccruals.size() == 0){
						getEmpAccruals();
				}
				return allAccruals != null && allAccruals.size() > 0;
		}
		public boolean hasHourCodeWeek1(){
				return hourCodeWeek1 != null && hourCodeWeek1.size() > 0;
		}
		public boolean hasHourCodeWeek2(){
				return hourCodeWeek2 != null && hourCodeWeek2.size() > 0;
		}
		public Map<Integer, List<TimeBlock>> getDailyBlocks(){
				if(dailyBlocks == null){
						getDaily();
				}
				return dailyBlocks;
		}
		public Map<Integer, Double> getHourCodeTotals(){
				return hourCodeTotals;
		}
		public Map<String, Double> getHourCodeWeek1(){
				return hourCodeWeek1;
		}
		public Map<String, Double> getHourCodeWeek2(){
				return hourCodeWeek2;
		}
		public Map<String, List<Double>> getAllAccruals(){
				return allAccruals;
		}		
		public void adjustAccruals(){
				if(employeeAccruals != null && usedAccrualTotals != null){
						for(EmployeeAccrual one: employeeAccruals){
								Accrual accrual = one.getAccrual();
								List<Double> list = new ArrayList<>();
								String accName = accrual.getName();
								String related_id = one.getRelated_hour_code_id();
								if(related_id != null && !related_id.equals("")){
										double hrs_total = one.getHours();
										list.add(hrs_total);
										try{
												int cd_id = Integer.parseInt(related_id);
												if(usedAccrualTotals.containsKey(cd_id)){
														double hrs_used = usedAccrualTotals.get(cd_id);
														list.add(hrs_used);
														if(hrs_total > hrs_used){
																hrs_total = hrs_total - hrs_used;
														}
														else
																hrs_total = 0.0;
														one.setHours(hrs_total);
												}
												else{
														list.add(0.0); // nothing used
												}
												list.add(hrs_total); // adjusted
												if(accrual.hasPref_max_leval()){
														if(hrs_total > accrual.getPref_max_level()){
																String str = accrual.getName()+" Accrual balance of "+hrs_total+" greater than "+accrual.getPref_max_level();
																if(!warnings.contains(str))
																		warnings.add(str);
														}
												}
												allAccruals.put(accName, list);
										}catch(Exception ex){
												logger.error(ex);
										}
								}
						}
				}
		}
		// since not everyday we have timeblock, we need to fill
		// the empty ones, needed for display and related links
		void fillTwoWeekEmptyBlocks(){
				if(payPeriod == null){
						getPayPeriod();
				}
				String date = payPeriod.getStart_date();
				for(int j=13;j >=0;j--){
						if(!dailyBlocks.containsKey(j)){
								TimeBlock one =
										new TimeBlock();
								//
								// we need to set what we need in timeblock
								// document_id, date
								// probably document_id directly from action
								// and we do not need to save it in block
								one.setDocument_id(id);
								// we need to set the day date
								//
								one.setOrder_index(j);								
								String dt = Helper.getDateAfter(date, j);
								one.setDate(dt);
								// we use start date from payperiod and we add j days to it
								//
								List<TimeBlock> lone = new ArrayList<>();
								lone.add(one);
								//
								dailyBlocks.put(j, lone);
						}
				}
		}
		private void checkForWarnings(){
				if(isSubmitted())
						checkForWarningsAfter();
				else
						checkForWarningsBefore();
		}
		// after submission
		private void checkForWarningsAfter(){
				if(jobTask == null){
						getJobTask();
				}
				if(getWeek1TotalDbl() > 0){
						checkWeekWarnings(hourCodeWeek1, week1Total);
				}
				if(jobTask != null){
						if(week1Total < jobTask.getWeekly_regular_hours()){
								String str = "Week 1 total hours are less than "+jobTask.getWeekly_regular_hours()+" hrs";
								if(!warnings.contains(str))
										warnings.add(str);
						}
				}
				if(getWeek2TotalDbl() > 0){
						checkWeekWarnings(hourCodeWeek2, week2Total);
				}
				if(jobTask != null){				
						if(week2Total < jobTask.getWeekly_regular_hours()){
								String str = "Week 2 total hours are less than "+jobTask.getWeekly_regular_hours()+" hrs";
								if(!warnings.contains(str))
										warnings.add(str);
						}
				}
		}
		// before submission
		private void checkForWarningsBefore(){
				if(jobTask == null){
						getJobTask();
				}
				if(getWeek1TotalDbl() > 0){
						checkWeekWarnings(hourCodeWeek1, week1Total);
				}
				if(getWeek2TotalDbl() > 0){
						checkWeekWarnings(hourCodeWeek2, week2Total);
				}
		}		
		/**
		 * in this function we check the weekly hour code entry times
		 * to make sure they comply with the rules set in 'Accrual warnings'
		 * db rules
		 * 1-if certain hour code has a min such as PTO can be less than 1
		 * 2-if hour code hours used should be in certain increments
		 *   such 0.25 (quarter hour increments)
		 * 3-Check if excess hours were used, such as using 7 hours PTO when it is
		 *   only needed 6
		 */
		private void checkWeekWarnings(Map<String, Double> hourCodeWeek,
																			double weekTotal
																			){
				for(String key:warningMap.keySet()){
						if(hourCodeWeek.containsKey(key)){
								AccrualWarning acc_warn = warningMap.get(key);
								double dbl_used = hourCodeWeek.get(key);
								if(acc_warn.require_min()){
										if(dbl_used < acc_warn.getMin_hrs()){
												warnings.add(acc_warn.getMin_warning_text());
										}
										else {
												double d_dif = weekTotal - dbl_used; // 45.27 - 8.27
												double d_need = 0;
												if(d_dif < 40)
														d_need = 40. - d_dif;   // 40 - 37 = 3
												if(d_need > 0){
														if(acc_warn.require_step()){
																if(d_need % acc_warn.getStep_hrs() > 0.01){
																		// 
																		// adjust the need accroding to step
																		// such as 6.1 ==> 6.25
																		d_need = d_need + acc_warn.getStep_hrs() - d_need % acc_warn.getStep_hrs();
																}
																// if need is less than min (normally 1 hr)
																// than we changed to min such as 0.50, 0.75
																if(d_need < acc_warn.getMin_hrs()){
																		d_need = acc_warn.getMin_hrs();
																}
														}
												}
												double dd = dbl_used - d_need; // 8.3 - 6.25
												if(dd > 0.01){
														String str = acc_warn.getExcess_warning_text()+" ("+dfn.format(dd)+" hrs)";
														if(!warnings.contains(str))
																warnings.add(str);
												}
										}
								}
								if(acc_warn.require_step()){
										if(dbl_used % acc_warn.getStep_hrs() > 0){
												String str = acc_warn.getStep_warning_text()+" ("+dfn.format(dbl_used)+" hrs)";
												if(!warnings.contains(str))
														warnings.add(str);
										}
								}
						}
				}
		}
		public boolean hasWarnings(){
				return warnings.size() > 0;
		}
		public List<String> getWarnings(){
				return warnings;
		}
		public boolean hasTimeNotes(){
				getTimeNotes();
				return timeNotes != null && timeNotes.size() > 0;
		}
		public List<TimeNote> getTimeNotes(){
				if(timeNotes == null && !id.equals("")){
						TimeNoteList tnl = new TimeNoteList(id);
						String back = tnl.find();
						if(back.equals("")){
								List<TimeNote> ones = tnl.getTimeNotes();
								if(ones != null && ones.size() > 0){
										timeNotes = ones;
								}
						}
				}
				return timeNotes;
		}
		public boolean hasTimeIssues(){
				getTimeIssues();
				return timeIssues != null && timeIssues.size() > 0;
		}
		public List<TimeIssue> getTimeIssues(){
				if(timeIssues == null && !id.equals("")){
						TimeIssueList tnl = new TimeIssueList(id);
						tnl.setOpenOnly();
						String back = tnl.find();
						if(back.equals("")){
								List<TimeIssue> ones = tnl.getTimeIssues();
								if(ones != null && ones.size() > 0){
										timeIssues = ones;
								}
						}
				}
				return timeIssues;
		}		
		public String doSelect(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "select id,employee_id,pay_period_id,date_format(initiated,'%m/%d/%Y %H;%i'),initiated_by from time_documents where id =? ";
				logger.debug(qq);
				try{
						con = Helper.getConnection();
						if(con != null){
								pstmt = con.prepareStatement(qq);
								pstmt.setString(1, id);
								rs = pstmt.executeQuery();
								if(rs.next()){
										setEmployee_id(rs.getString(2));
										setPay_period_id(rs.getString(3));
										setInitiated(rs.getString(4));
										setInitiated_by(rs.getString(5));
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
		//
		// save only, no updates
		//
		public String doSave(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "insert into time_documents values(0,?,?,now(),?) ";
				if(employee_id.equals("")){
						msg = " employee_id not set ";
						return msg;
				}
				if(pay_period_id.equals("")){
						msg = " pay_period not set ";
						return msg;
				}
				if(initiated_by.equals("")){
						msg = " initiater not set ";
						return msg;
				}				
				logger.debug(qq);
				try{
						con = Helper.getConnection();
						if(con != null){
								pstmt = con.prepareStatement(qq);
								pstmt.setString(1, employee_id);
								pstmt.setString(2, pay_period_id);
								pstmt.setString(3, initiated_by);
								pstmt.executeUpdate();
						}
						qq = "select LAST_INSERT_ID()";
						pstmt = con.prepareStatement(qq);
						rs = pstmt.executeQuery();
						if(rs.next()){
								id = rs.getString(1);
						}
						//
						// we look for first workflow 
						//
						Workflow wf = findFirstWorkFlow();
						System.err.println(" first workflow "+wf);
						if(wf != null){
								TimeAction ta = new TimeAction(wf.getId(), id, initiated_by);
								msg = ta.doSave();
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
		Workflow findFirstWorkFlow(){
				Workflow workflow = null;
				WorkflowList wfl = new WorkflowList();
				wfl.forFirstWorkflow();
				String back = wfl.find();
				if(back.equals("")){
						List<Workflow> ones = wfl.getWorkflows();
						if(ones != null && ones.size() == 1)
								workflow = ones.get(0);
				}
				return workflow;
		}
		
}
