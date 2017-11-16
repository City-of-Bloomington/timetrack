package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.sql.*;
import java.util.List;
import org.apache.log4j.Logger;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;

public class TimeClock{

		static final long serialVersionUID = 3700L;	
		static Logger logger = Logger.getLogger(TimeClock.class);
    String id="", id_code="", time="", document_id="", time_in="", time_out="";
		Employee employee = null;
		PayPeriod currentPayPeriod  = null;
		Document document = null;
		List<JobTask> jobTasks = null;
		JobTask selectedJob = null;
		HourCode defaultRegularCode = null;
		boolean new_docuemnt = false;
		int time_hr = -1, time_min = -1; // hour, minute of clock
		TimeBlock timeBlock = new TimeBlock();
		//
		public TimeClock(){

		}
		public TimeClock(String val, String val2){
				//
				// initialize
				//
				setId_code(val);
				setTime(val2);
    }
		public boolean equals(Object obj){
				if(obj instanceof TimeClock){
						TimeClock one =(TimeClock)obj;
						return id.equals(one.getId());
				}
				return false;				
		}
		public int hashCode(){
				int seed = 17;
				if(!id.equals("")){
						try{
								seed += Integer.parseInt(id);
						}catch(Exception ex){
						}
				}
				return seed;
		}
    //
    // getters
    //
    public String getId(){
				return id;
    }
    public String getId_code(){
				return id_code;
    }
    public String getTime_in(){
				return time_in;
    }
    public String getTime_out(){
				return time_out;
    }
    public String getTime(){
				if(time.equals("")){
						time = Helper.getCurrentTime();
				}
				return time;
    }		
		public String getTime_in_out(){
				return time_in+" - "+time_out;
		}
		public boolean hasTime_in_out(){
				return !(time_in.equals("") || time_out.equals(""));
		}
		//
    // setters
    //
    public void setId(String val){
				if(val != null)
						id = val;
    }
    public void setId_code(String val){
				if(val != null)
						id_code = val;
    }
    public void setTime(String val){
				if(val != null){
						time = val;
						splitTime(time);
				}
    }
		public TimeBlock getTimeBlock(){
				return timeBlock;
		}
		private String splitTime(String val){
				String msg = "";
				if(val != null && val.indexOf(":") > 0){
						try{
								String str_arr[] = val.split(":");
								if(str_arr != null && str_arr.length == 2){
										time_hr = Integer.parseInt(str_arr[0]);
										time_min = Integer.parseInt(str_arr[1]);
								}
								else{
										msg = "Invalid time "+val;
								}
						}
						catch(Exception ex){
								msg += " "+ex;
						}
				}
				return msg;
		}
		public PayPeriod getCurrentPayPeriod(){
				//
				if(currentPayPeriod == null){
						PayPeriodList ppl = new PayPeriodList();
						ppl.currentOnly();
						String back = ppl.find();
						if(back.equals("")){
								List<PayPeriod> ones = ppl.getPeriods();
								if(ones != null && ones.size() > 0){
										currentPayPeriod = ones.get(0);
								}
						}
				}
				return currentPayPeriod;
		}
		public Document getDocument(){
				if(document == null){
						DocumentList dl = new DocumentList();
						dl.setEmployee_id(employee.getId());
						dl.setPay_period_id(currentPayPeriod.getId());
						String back = dl.find();
						if(back.equals("")){
								List<Document> ones = dl.getDocuments();
								if(ones != null && ones.size() > 0){
										document = ones.get(0);
								}
						}
				}
				// 
				// if we could not find, then we create a new one
				//
				if(document == null){
						Document one = new Document(null, employee.getId(), currentPayPeriod.getId(), null, employee.getUser().getId());
						String back = one.doSave();
						if(back.equals("")){
								document = one;
								new_docuemnt = true;
						}
				}
				return document;
		}
		public List<JobTask> getJobTasks(){
				if(jobTasks == null){
						JobTaskList jl = new JobTaskList(employee.getId());
						jl.setPay_period_id(currentPayPeriod.getId());
						String back = jl.find();
						if(back.equals("")){
								List<JobTask> ones = jl.getJobTasks();
								if(ones != null && ones.size() > 0){
										jobTasks = ones;
										if(jobTasks.size() > 0){
												selectedJob = jobTasks.get(0);
												SalaryGroup salaryGroup = selectedJob.getSalaryGroup();
												if(salaryGroup != null){
														defaultRegularCode = salaryGroup.getDefaultRegularCode();
												}
										}
								}
						}
				}
				return jobTasks;
		}		
		//
		public String process(){
				String msg="", hour_code_id="1"; // 1:Reg, 14:TEMP
				String date = Helper.getToday();
				if(id_code.equals("")){
						msg = "Employee ID code is required ";
						return msg;
				}
				// find employee using id_code				
				EmployeeList empl = new EmployeeList();
				empl.setId_code(id_code);
				String back = empl.find();
				if(!back.equals("")){
						msg += back;
						System.err.println(msg);
						return msg;
				}
				List<Employee> ones = empl.getEmployees();
				if(ones != null && ones.size() > 0){
						employee = ones.get(0);
				}
				if(employee == null){
						msg += " No matching employee found for ID "+id_code;
						System.err.println(msg);
						return msg;
				}
				//
				// find current pay_period
				getCurrentPayPeriod();
				if(currentPayPeriod == null){
						msg = "Could not find current pay period ";
						System.err.println(msg);
						return msg;						
				}
				//			
				// find document, if non create one
				//
				getDocument();
				// System.err.println(" found doc "+document.getId());
				//
				// we need the employee job
				getJobTasks();
				System.err.println(" job "+selectedJob.getId());
				//				
				if(defaultRegularCode != null){
						hour_code_id = defaultRegularCode.getId();
						// System.err.println(" hr code id "+hour_code_id);
				}
				//
				// find if there is a clock-in, if not this a clock-in
				//         else it is a clock-out
				if(new_docuemnt){
						System.err.println(" new docment, now save ");
						// if this is a new document ,this means this first entry
						// in this pay period, so will consider as clock_in
						timeBlock = new TimeBlock(null,
																				 document.getId(),
																				 selectedJob.getId(),
																				 hour_code_id,
																				 date,
																				 
																				 time_hr,
																				 time_min,
																				 0,
																				 0,
																				 0, // hours
																				 
																				 null,
																				 "y",
																				 null);
						timeBlock.setAction_type("ClockIn");
						timeBlock.setAction_by_id(document.getEmployee().getUser_id());						
						msg = timeBlock.doSave();
				}
				else{
						//
						// check if we have clockIn, if so then this is clockOut
						//
						TimeBlockList tbl = new TimeBlockList();
						tbl.setDocument_id(document.getId());
						tbl.setJob_id(selectedJob.getId());
						tbl.hasClockInOnly();
						tbl.setActiveOnly();
						msg = tbl.find();
						if(msg.equals("")){
								List<TimeBlock> tbs = tbl.getTimeBlocks();
								if(tbs != null && tbs.size() > 0){
										timeBlock = tbs.get(0);
										// it is a clock-out
										System.err.println("it is clock-out ");										
										timeBlock.setEnd_hour(time_hr);
										timeBlock.setEnd_minute(time_min);
										timeBlock.setClock_out("y");
										timeBlock.setAction_type("ClockOut");
										timeBlock.setAction_by_id(document.getEmployee().getUser_id());
										msg = timeBlock.doUpdate();
								}
								else{ // it is a clock-in
										System.err.println("it is clock-in ");
										timeBlock = new TimeBlock(null,
																							document.getId(),
																							selectedJob.getId(),
																							hour_code_id,
																							date,
																							
																							time_hr,
																							time_min,
																							0,
																							0,
																							0, // hours
																							
																							null,
																							"y",
																							null);
										timeBlock.setAction_type("ClockIn");
										timeBlock.setAction_by_id(document.getEmployee().getUser_id());
										msg = timeBlock.doSave();
								}
						}
				}
				return msg;
		}


}