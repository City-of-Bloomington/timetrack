package in.bloomington.timer.bean;

/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.sql.*;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;

public class TimeClock {

    static final long serialVersionUID = 3700L;
    static Logger logger = LogManager.getLogger(TimeClock.class);
    String id = "", id_code = "", time = "", document_id = "",
				time_in = "", time_out = "", job_id = "",
				employee_id = "";
    final static String time_clock_duration = "13"; // hrs
    Employee employee = null;
    PayPeriod currentPayPeriod = null, previousPayPeriod = null;
    Document document = null;
    List<JobTask> jobs = null;
    JobTask job = null;
    Shift shift = null;
    List<Group> groups = null;
    HourCode defaultRegularCode = null;
    boolean new_docuemnt = false;
    int time_hr = -1, time_min = -1; // hour, minute of clock
		String ip = ""; // for debug;
    TimeBlock timeBlock = new TimeBlock();
    boolean hasClockIn = false;
    //
    public TimeClock() {

    }

    public TimeClock(String val, String val2) {
				//
				setId_code(val);
				setTime(val2);
    }

    public boolean equals(Object obj) {
				if (obj instanceof TimeClock) {
						TimeClock one = (TimeClock) obj;
						return id.equals(one.getId());
				}
				return false;
    }

    public int hashCode() {
				int seed = 17;
				if (!id.equals("")) {
						try {
								seed += Integer.parseInt(id);
						} catch (Exception ex) {
						}
				}
				return seed;
    }

    //
    // getters
    //
    public String getId() {
				return id;
    }

    public String getId_code() {
				return id_code;
    }

    public String getTime_in() {
				return time_in;
    }

    public String getTime_out() {
				return time_out;
    }

    public String getJob_id() {
				return job_id;
    }

    public String getTime() {
				if (time.equals("")) {
						time = Helper.getCurrentTime();
				}
				return time;
    }

    public String getTime_in_out() {
				return time_in + " - " + time_out;
    }

    public boolean hasTime_in_out() {
				return !(time_in.equals("") || time_out.equals(""));
    }

    //
    // setters
    //
    public void setId(String val) {
				if (val != null)
						id = val;
    }

    public void setJob_id(String val) {
				if (val != null && !val.equals("-1")) {
						job_id = val;
				}
    }

    public void setId_code(String val) {
				if (val != null)
						id_code = val;
    }
		public void setIp(String val){
				if (val != null)
						ip = val;
		}
		public void setTime(String[] vals){
				if(vals != null && vals.length > 0){
						String val = vals[0];
						setTime(val);
				}
		}
    public void setTime(String val) {
				if (val != null) {
						getEmployee();
						if(!hasClockIn()){								
								return; // already taken care of
						}						
						time = Helper.getCurrentTime();
						int[] timeArr = Helper.getCurrentTimeArr();
						time_hr = timeArr[0];
						time_min = timeArr[1];
						if(shift != null){
								// needed when clock out within
								// start time window
								if(shift.hasClockInWindow()){
										if(shift.isMinuteWithin(timeArr)){
												time_min = shift.getStartMinute();
												time_hr = shift.getStartHour();
												return;
										}
								}								
								if(shift.hasClockOutWindow()){
										if(shift.isClockOutMinuteWithin(timeArr)){
												time_hr = shift.getEndHour();
												time_min = shift.getEndMinute();
												return;
										}
								}
								if(shift.hasRoundedMinute()){
										int mm = shift.getRoundedMinute(time_min);
										if(mm == 60){
												time_hr += 1;
												time_min = 0;
										}
										else{
												time_min = mm;
										}
								}
						}
				}
    }		
		private void setTimeAsIfClockIn(){
				getEmployee(); 
				int[] timeArr = Helper.getCurrentTimeArr();
				time_hr = timeArr[0];
				time_min  = timeArr[1];
				if(shift != null){
						if(shift.hasClockInWindow()){
								if(shift.isMinuteWithin(timeArr)){
										time_min = shift.getStartMinute();
										time_hr = shift.getStartHour();
										return;
								}
						}
						if(shift.hasRoundedMinute()){
								int mm = shift.getRoundedMinute(time_min);
								if(mm == 60){
										time_hr += 1;
										time_min = 0;
								}
								else{
										time_min = mm;
								}
						}
				}
		}
    public TimeBlock getTimeBlock() {
				return timeBlock;
    }

    public PayPeriod getCurrentPayPeriod() {
				if (currentPayPeriod == null) {
						PayPeriodList ppl = new PayPeriodList();
						ppl.currentOnly();
						String back = ppl.find();
						if (back.equals("")) {
								List<PayPeriod> ones = ppl.getPeriods();
								if (ones != null && ones.size() > 0) {
										currentPayPeriod = ones.get(0);
								}
						}
				}				
				return currentPayPeriod;
    }
    public PayPeriod getPreviousPayPeriod() {
				if (previousPayPeriod == null) {
						PayPeriodList ppl = new PayPeriodList();
						ppl.setLastPayPeriod();
						String back = ppl.find();
						if (back.equals("")) {
								List<PayPeriod> ones = ppl.getPeriods();
								if (ones != null && ones.size() > 0) {
										previousPayPeriod = ones.get(0);
								}
						}
				}				
				return previousPayPeriod;
    }		
    public Document getDocument(){
				if(document == null)
						findDocument();
				return document;
    }
		public String getDocument_id(){
				if(document_id.equals("")){
						findDocument();
				}
				return document_id;
		}
    //
    void findDocument() {
				if (document == null) {
						DocumentList dl = new DocumentList();
						dl.setEmployee_id(employee.getId());
						dl.setPay_period_id(currentPayPeriod.getId());
						dl.setJob_id(job_id);
						String back = dl.find();
						if (back.equals("")) {
								List<Document> ones = dl.getDocuments();
								if (ones != null && ones.size() > 0) {
										document = ones.get(0);
										document_id = document.getId();
								}
						}
				}
				//
				// if we could not find, then we create a new one
				//
				if (document == null) {
						Document one = new Document(null, employee.getId(), currentPayPeriod.getId(), job_id, null,	employee.getId());
						String back = one.doSave();
						if (back.equals("")) {
								document = one;
								new_docuemnt = true;
								document_id = document.getId();
						}
				}
    }

    public JobTask getJob() {
				if(job == null){
						findJobs();
				}
				return job;
    }
    //
    public boolean hasMultipleJobs() {
				findJobs();
				return jobs != null && jobs.size() > 1;
    }

    public boolean hasEmployee() {
				getEmployee();
				return employee != null;
    }

    public List<JobTask> getJobs() {
				findJobs();
				return jobs;
    }

    public Employee getEmployee() {
				if (employee == null && !id_code.equals("")) {
						//
						// if two employee swipe one after another quickely
						// we pick the first 4 digits and ignore the second
						if(id_code.length() == 8){ 
								id_code = id_code.substring(0,4);
						}
						else if(id_code.length() > 4){ // if it is more we do not accept
								return null;
						}
						Employee one = new Employee();
						one.setId_code(id_code);
						String back = one.doSelect();
						if (back.equals("")) {
								employee = one;
								employee_id = employee.getId();
						}
						else{
								System.err.println(" get employee error "+back+" ID code "+id_code+" ip:"+ip);
						}
				}
				if (employee != null) {
						if (employee.hasGroups()) {
								groups = employee.getGroups();
						}
						if(employee.hasShift()){
								shift = employee.getShift();
						}
				}				
				return employee;
    }

    public boolean hasGroups() {
				if(groups == null)
						getEmployee();
				return groups != null && groups.size() > 0;
    }

    // assuming all groups in one location
    public boolean checkIpAddress(String ipAddress) {
				if (hasGroups()) {
						for (Group one : groups) {
								if (one.hasGroupLocations()) {
										if (one.ipSetIncludes(ipAddress))
												return true;
								}
								else{
										return true;
								}
						}
				}
				return false;
    }

    // find if hasClockIn
    public boolean hasClockIn(){
				if(!hasClockIn){
						setTimeAsIfClockIn();
						getCurrentPayPeriod();
						TimeBlockList tbl = new TimeBlockList();
						tbl.setPay_period_id(currentPayPeriod.getId());
						tbl.setEmployee_id(employee.getId());
						tbl.setDuration(time_clock_duration);
						String back = tbl.findDocumentForClockInOnly(time_hr, time_min);
						if (back.equals("")) {
								// if we have clock-in, we can get the document
								document = tbl.getDocument();
								if (document != null) {
										job_id = document.getJob_id();
										document_id = document.getId();
										hasClockIn = true;
								}
								else{
										// System.err.println(" no doc for clock in found ");
								}
						}
						if(document == null &&
							 currentPayPeriod.isTodayFirstDayOfPayPeriod()){
								//
								// needed for overnight clock out on the last day of payperiod
								//
								if(currentPayPeriod.isTodayFirstDayOfPayPeriod()){
										getPreviousPayPeriod();
										tbl.setPay_period_id(previousPayPeriod.getId());
										back = tbl.findDocumentForClockInOnly(time_hr, time_min);
										if (back.equals("")) {
												// if we have clock-in, we can get the document
												document = tbl.getDocument();
												if (document != null) {
														job_id = document.getJob_id();
														document_id = document.getId();
														hasClockIn = true;
												}
										}
								}
						}
				}
				return hasClockIn;
    }
    void findJobs(){		
				if (jobs == null) {
						if (employee != null) {
								JobTaskList jl = new JobTaskList(employee.getId());
								getCurrentPayPeriod();
								jl.setPay_period_id(currentPayPeriod.getId());
								String back = jl.find();
								if (back.equals("")) {
										List<JobTask> ones = jl.getJobTasks();
										if (ones != null && ones.size() > 0) {
												jobs = ones;
										}
								}
						}
						// find job
						if (job == null){
								if(!job_id.equals("")) {
										JobTask one = new JobTask(job_id);
										String back = one.doSelect();
										if (back.equals("")) {
												job = one;
										}
								}
								else if(jobs != null && jobs.size() > 0) {
										for (JobTask one : jobs) {
												if (one.isPrimary()) {
														job = one;
														job_id = job.getId();
												}
										}
								}
								if (job == null) {
										job = jobs.get(0);
										job_id = job.getId();
								}
						}
				}
    }

    //
    public String process() {
				//
				String msg = "", hour_code_id = "1"; // 1:Reg, 14:TEMP
				String date = Helper.getToday();
				String yesterday = Helper.getYesterday();
				if (id_code.equals("")) {
						msg = "Employee ID code is required ";
						return msg;
				}
				// find employee using id_code
				//
				// find current pay_period
				getCurrentPayPeriod();
				if (currentPayPeriod == null) {
						msg = "Could not find current pay period ";
						System.err.println(msg);
						return msg;
				}
				//
				// find document, if non create
				// we need the employee job
				if (job_id.equals("")) {
						if (jobs.size() > 1 && job_id.equals("")) {
								msg = "you need to select a job ";
								return msg;
						}
						if (job == null) {
								msg = "Could not find related job ";
								logger.error(msg);
								return msg;
						}
				}
				if (job != null) {
						SalaryGroup salaryGroup = job.getSalaryGroup();
						if (salaryGroup != null) {
								defaultRegularCode = salaryGroup.getDefaultRegularCode();
						}
						if (defaultRegularCode != null) {
								hour_code_id = defaultRegularCode.getId();
						}
				}
				getDocument();
				//
				// find if there is a clock-in, if not this a clock-in
				// else it is a clock-out
				try {
						if (new_docuemnt) {
								// System.err.println(" new docment, now save ");
								// if this is a new document ,this means this first entry
								// in this pay period, so will consider as clock_in
								timeBlock = new TimeBlock(null,
																					document.getId(),
																					hour_code_id,
																					null, // earn_code_reason_id,
																					date,
																					
																					time_hr,
																					time_min,
																					0, 0, 0, 0, 

																					"y", null, false, null);
								timeBlock.setAction_type("ClockIn");
								timeBlock.setAction_by_id(employee_id);
								msg = timeBlock.doSave();
						} else {
								//
								// check if we have clockIn, if so then this is clockOut
								//
								TimeBlockList tbl = new TimeBlockList();
								tbl.setDocument_id(document.getId());
								tbl.hasClockInOnly();
								tbl.setActiveOnly();
								tbl.setDate_from(yesterday); // yesterday
								tbl.setDate_to(date); // today
								tbl.setDuration(time_clock_duration);
								msg = tbl.findTimeBlocksForClockIn(time_hr, time_min);
								if (msg.equals("")) {
										List<TimeBlock> tbs = tbl.getTimeBlocks();
										if (tbs != null && tbs.size() > 0) {
												timeBlock = tbs.get(0);
												String date_old = timeBlock.getDate();
												if(date_old.equals(yesterday)){
														timeBlock.setOvernight(true);
												}
												timeBlock.setEnd_hour(time_hr);
												timeBlock.setEnd_minute(time_min);
												timeBlock.setClock_out("y");
												timeBlock.setAction_type("ClockOut");
												timeBlock.setAction_by_id(employee_id);
												msg = timeBlock.doUpdate();
										} else { // it is a clock-in
												timeBlock = new TimeBlock(null,
																									document.getId(),
																									hour_code_id,
																									null,// earn_code_reason_id
																									date,

																									time_hr, time_min,
																									0, 0, 0, 0, 

																									"y", null, false, null);
												timeBlock.setAction_type("ClockIn");
												timeBlock.setAction_by_id(employee_id);
												msg = timeBlock.doSave();
										}
								}
						}
				} catch (Exception ex) {
						msg += ex;
						logger.error(msg);
				}
				return msg;
    }

}
