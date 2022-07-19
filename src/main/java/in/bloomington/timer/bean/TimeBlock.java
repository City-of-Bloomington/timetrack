package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Hashtable;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import java.util.regex.*;  
import java.sql.*;
import javax.sql.*;
import java.text.SimpleDateFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;

public class TimeBlock extends Block{

    static Logger logger = LogManager.getLogger(TimeBlock.class);
    static final long serialVersionUID = 4000L;		
    private String inactive=""; // for deleted stuff;
		// String formatPattern = "##.####";
		// DecimalFormat dsf = new DecimalFormat(formatPattern);
    String hour_code = "", code_desc="", nw_code=""; // for showing on jsp
    String action_by_id="", action_type=""; // for logs
    // order_index is day order in the payperiod day list
    // Mond=0, Tue=1, Wed=2, Thu=3, Fr=4, Sat=5, Sun=6, .. Sat=12, Sun=13
    //
    String job_id = "";// needed for display onley
    final static Set<Integer> weekendSet = new HashSet<>(Arrays.asList(5,6,12,13));
    Set<String> rangeDateSet = new HashSet<>();
    int order_index=0, repeat_count=0;
    boolean include_weekends = false, overnight = false;
    boolean startNextDay=false;
    // from the interface
    Map<String, String> accrualBalance = new Hashtable<>();
    //
    Set<String> document_ids = null; // needed for employee with multiple jobs
    String start_date = "", end_date="", job_name="";
    String hour_code_id_old = ""; // for accrual purpose
    double hours_old = 0;
    String timeInfo = "";
		Shift shift = null;
    List<EarnCodeReason> earnReasons = null;
    String overnightOption = ""; // arrived before 12am, arrived after 12am
    String errors = "";
		String location_id="";
    public TimeBlock( // for save
										 String val,
										 String val2,
										 String val3,
										 String val4,
										 int val5,
										 int val6,
										 int val7,
										 int val8,
										 double val9,
										 int val10,
										 double val11
											){
				super(val, val2,
							val3,
							val4, val5, val6, val7, val8, val9, val10, val11);
    }
    public TimeBlock(
										 String val,
										 String val2,
										 String val3,
										 String val4,
										 String val5,
										 int val6,
										 int val7,
										 int val8,
										 int val9,
										 double val10,
										 int val11,
										 double val12,
										 String val13,
										 String val14
										 ){
				super(val, val2, val3,
							val4,
							val5, val6, val7, val8, val9,
							val10, val11, val12, val13, val14);
    }
												 
    public TimeBlock(
										 String val,
										 String val2,
										 String val3, 
										 String val4,
										 String val5,
										 int val6,
										 int val7,
										 int val8,
										 int val9,
										 double val10,
										 int val11,
										 double val12,
										 
										 String val13,
										 String val14,
										 boolean val15,
										 String val16
										 ){
				super(val, val2, val3,
							val4,
							val5, val6, val7, val8, val9,
							val10, val11, val12, val13, val14, val15, val16);
    }
    public TimeBlock(
										 String val,
										 String val2,
										 String val3, 
										 String val4,
										 String val5,
										 int val6,
							 
										 int val7,
										 int val8,
										 int val9,
										 double val10,
										 int val11,
										 double val12,
							 
										 String val13,
										 String val14,
										 boolean val15,
										 String val16,
										 boolean val17,
							 
										 int val18,
										 String val19,
										 String val20,
										 String val21,
										 String val22,
										 String val23
										 ){
				super(val, val2,
							val3,
							val4,
							val5, val6, val7, val8, val9,
							val10, val11, val12, val13, val14, val15, val16);
				setInactive(val17);
				setOrder_index(val18);
				setHour_code(val19);
				setCode_desc(val20);
				setNw_code(val21);
				setJob_name(val22);
				setJob_id(val23);
    }
    public TimeBlock(String val){
				super(val);
    }
    public TimeBlock(){
    }		
    //
    // getters
    //
    public boolean getInactive(){
				return !inactive.isEmpty();
    }
    public boolean isActive(){
				return inactive.isEmpty();
    }
    public boolean hasData(){
				return !id.isEmpty();
    }
    public String getHour_code(){
				return hour_code;
    }
    public String getCode_desc(){
				return code_desc;
    }
    public String getNw_code(){
				return nw_code;
    }
    public String getJob_name(){
				return job_name;
    }		
    public String getStart_date(){
				if(start_date.isEmpty()){
						start_date = Helper.changeDateFormat(date);
				}
				return start_date;
    }
    public String getEnd_date(){
				if(end_date.isEmpty()){
						end_date = Helper.changeDateFormat(date);
				}				
				return end_date;
    }		
    public String getRepeat_count(){
				return "";
    }
    public boolean getInclude_weekends(){
				return false;
    }
    public String getOvernightOption(){
				String val = "-1";
				if(overnight){
						val = "arrived before 12am";
				}
				if(startNextDay){
						val = "arrived after 12am";
				}
				return val;
    }
    public boolean getOvernight(){
				return overnight;
    }
    public int getOrder_index(){
				return order_index;
    }
    public String getJob_id(){
				return job_id; // for display only
    }
		public boolean hasErrors(){
				return !errors.isEmpty();
		}
    public boolean hasShift(){
				if(shift == null){
						getDocument();
						jobTask = document.getJob();
						if(jobTask != null){
								if(jobTask.hasShift())
										shift = jobTask.getShift();
						}
				}
				return shift != null;
    }  				
		public String getErrors(){
				return errors;
		}
    //
    // setters
    //
    public void setOvernightOption(String val){
				if(val != null && !val.equals("-1")){
						if(val.equals("arrived before 12am")){
								overnight = true;
						}
						else {
								startNextDay = true;
								overnight = true;
						}
				}
    }
    public void setInactive(boolean val){
				if(val)
						inactive = "y";
    }
    public void setAction_by_id(String val){
				if(val != null)
						action_by_id = val;
    }
    public void setLocation_id(String val){
				if(val != null)
						location_id = val;
    }		
    public void setHour_code(String val){
				if(val != null)
						hour_code = val;
    }
    public void setNw_code(String val){
				if(val != null)
						nw_code = val;
    }
    public void setJob_name(String val){
				if(val != null)
						job_name = val;
    }		
    public void setStart_date(String val){
				if(val != null){
						if(val.indexOf("-") > 0){
								start_date = Helper.changeDateFormat(val);
						}
				}
    }
    public void setEnd_date(String val){
				if(val != null && val.indexOf("-") > 0){
						end_date = Helper.changeDateFormat(val);
				}
    }		
    public void setCode_desc(String val){
				if(val != null)
						code_desc = val;
    }		
    public void setOrder_index(int val){
				if(val > 0)
						order_index = val;
    }
    public void setAction_type(String val){
				if(val != null)
						action_type = val;
    }
    // for display only from TimeBlockList
    public void setJob_id(String val){
				if(val != null)
						job_id = val;
    }		
    public void setRepeat_count(String val){
				if(val != null && !val.isEmpty()){
						try{
								repeat_count = Integer.parseInt(val);
						}catch(Exception ex){}
				}
    }
    public void setInclude_weekends(boolean val){
				if(val) 
						include_weekends = val;
    }
    public void setOvernight(boolean val){
				if(val)
						overnight = true;
    }
    public void setAccrual_balance(String[] vals){
				if(vals != null){
						for(String str: vals){
								if(str.indexOf("_") > 0){
										String arr[] = str.split("_");
										accrualBalance.put(arr[0],arr[1]);
								}
						}
				}
    }
    public void setHour_code_id_old(String val){
				if(val != null)
						hour_code_id_old = val;
    }
    public void setHours_old(Double val){
				if(val != null)
						hours_old = val;
    }
    // do display in calendar view
    public int getDayInt(){
				if(date.isEmpty()){
						return 0;
				}
				return Helper.getDayInt(date);
    }
    public void setTime_in(String val2){
				if(val2 != null && !val2.trim().isEmpty()){
						String val = val2.trim().toLowerCase();
						String msg = splitTimes(val, false);
						if(msg.isEmpty()){
								time_in_changed = true;						
								time_in_set = true;
						}
						if(hasShift()){
								int[] timeArr = new int[2];
								timeArr[0] = begin_hour;
								timeArr[1] = begin_minute;
								if(shift.hasClockInWindow()){
										if(shift.isMinuteWithin(timeArr)){ // clock in
												begin_minute = shift.getStartMinute();
												begin_hour = shift.getStartHour();
										}
								}
								else if(shift.hasRoundedMinute()){
										int mm = shift.getRoundedMinute(begin_minute);
										if(mm == 60){
												begin_hour += 1;
												begin_minute = 0;
										}
										else{
												begin_minute = mm;
										}
								}
						}
				}								
				else{
						time_in_set = false;
				}
    }
    public void setTime_out(String val2){
				if(val2 != null && !val2.trim().isEmpty()){
						String val = val2.trim().toLowerCase();
						String msg = splitTimes(val, true);
						if(msg.isEmpty()){
								time_out_set = true;
								time_out_changed = true;
						}
						if(hasShift()){
								int[] timeArr = new int[2];
								timeArr[0] = end_hour;
								timeArr[1] = end_minute;
								if(shift.hasClockOutWindow()){
										if(shift.isClockOutMinuteWithin(timeArr)){ // clock-out
												end_hour = shift.getEndHour();
												end_minute = shift.getEndMinute();
										}
								}
								else if(shift.hasRoundedMinute()){
										int mm = shift.getRoundedMinute(end_minute);
										if(mm == 60){
												end_hour += 1;
												end_minute = 0;
										}
										else{
												end_minute = mm;
										}
								}
						}
				}
				else{
						time_out_set = false;
				}
    }
    private String splitTimes(String val, boolean isOut){
				String msg = "";
				// if just start hours 8, 11 am, 12 p
				String pattern = "[0123]?[\\d][\\W]?[\\s]?[ap]?[m]?";								
				// accepted entries 0930, 12:30, 11;30am, 10,30p, 11.30pm
				String pattern2 = "[0123]?[\\d][\\W]?[\\d]{2}[\\s]?[ap]?[m]?";
				// for am/pm 				
				String pattern3 = ".*[ap][m]?$";	// ends with am/pm				
				// for \\W or :;,
				String pattern4 = ".*[\\W].*";  // [;;.,]
				String delimeter = "[\\W]"; // [:;.,]
				boolean match = Pattern.matches(pattern, val);
				if(!match){
						match = Pattern.matches(pattern2, val);
				}
				if(!match){
						msg = "Error: invalid time format "+val;
						logger.error(msg);
						errors += msg;
						return msg;
				}
				// if(val != null && !val.trim().isEmpty()){
				int hrs = 0, mins=0;
				boolean is_pm = false, is_am=false;
				String dd[] = {"",""};
				String val2 = val;
				if(Pattern.matches(pattern3, val)){
						if(val.indexOf("a") > -1){
								val2 = val.substring(0,val.indexOf("a"));
								is_am = true;
						}
						else if(val.indexOf("p") > -1){
								val2 = val.substring(0,val.indexOf("p"));
								is_pm = true;
						}
				}
				val2 = val2.trim();
				if(Pattern.matches(pattern4, val2)){
						dd = val2.split(delimeter);
				}
				else if(val2.length() >= 3){
						// no colon, time format hmm, or hhmm
						if(val2.length() == 3){ // 3 digits
								dd[0] = val2.substring(0,1);
								dd[1] = val2.substring(1);
						}
						else{ // 4 digits
								dd[0] = val2.substring(0,2);
								dd[1] = val2.substring(2);
						}
				}
				else if(val2.length() > 0){ // such as 5 pm 10 pm
						dd[0] = val2;
						dd[1] = "0";
				}
				else {
						msg = "Error: Invalid time format "+val;
						logger.error(msg);
						errors += msg;
						return msg;
				}
				if(msg.isEmpty()){
						if(dd != null){
								try{
										if(dd[0].startsWith("0")){
												dd[0] = dd[0].substring(1);
										}
										hrs = Integer.parseInt(dd[0].trim());
										if(dd.length == 2){
												mins = Integer.parseInt(dd[1].trim());
										}
										if(hrs < 0){
												msg = "hours can not be negative: "+hrs;
												errors += msg;
												return msg;
										}
										if(hrs > 36){
												msg = "invalid hour: "+hrs;
												errors += msg;
												return msg;
										}
										if(is_pm && hrs < 12){
												hrs += 12;
										}
										if(is_am && hrs == 12){ 
												hrs = 0;
														}
										if(isOut){
												end_hour = hrs;
												end_minute = mins;
										}
										else{
												begin_hour = hrs;
												begin_minute = mins;
										}
								}catch(Exception ex){
										logger.error(ex);
										msg += ex;
										errors += ex;
								}
						}
				}
				return msg;
    }				
    public String getTime_in(){
				String ret = "";
				if(id.isEmpty())
						return ret;
				String am_pm = "AM";
				if(begin_hour > 24){
						begin_hour -= 24;
						startNextDay = true;
				}
				else if(begin_hour == 24){
						begin_hour = 12;
						startNextDay = true;
				}				
				else if(begin_hour > 12){
						begin_hour -= 12;						
						am_pm = "PM";
				}
				else if(begin_hour == 12){
						am_pm = "PM";
				}
				else if(begin_hour == 0){
						begin_hour = 12;
				}
				if(begin_hour < 10){
						ret = "0";
				}
				ret += begin_hour+":";
				if(begin_minute < 10){
						ret += "0";
				}
				ret += begin_minute+" "+am_pm;
				return ret;
    }
    public boolean areAllTimesSet(){
				//
				// for clock-in only
				if(id.isEmpty()){
						if(isMonetaryType()){
								return amount > 0;
						}
						if(start_date.equals(end_date) && !start_date.isEmpty()){
								if(!date.equals(start_date)){
										date = start_date;
								}
						}
						if(time_in_set && !time_out_set){
								clock_in="y";
								return true;
						}
						
				}
				else{ // update clock-in only
						if(time_in_set && !time_out_set){
								clock_in="y";
								return true;
						}
				}
				if((!time_in_set && time_out_set)) return false;
				return ((time_in_set && time_out_set) || hours_set);
    }
    public String getTime_out(){
				String ret = "";
				int e_hour=end_hour;
				int e_minute=end_minute;
				if(id.isEmpty())
						return ret;
				if(isClockInOnly()){
						return ret;
				}
				String am_pm = "AM";
				if(e_hour > 36){
						e_hour -= 36;
						overnight = true;
						am_pm = "PM";
				}
				if(e_hour == 36){
						e_hour = 12;
						overnight = true;
						am_pm = "PM";
				}				
				else if(e_hour > 24){
						e_hour -= 24;
						overnight = true;
				}
				else if(e_hour == 24){
						e_hour = 12;
						overnight = true;
				}
				else if(e_hour > 12){
						e_hour -= 12;
						am_pm = "PM";
				}
				else if(e_hour == 12){
						am_pm = "PM";
				}
				else if(e_hour == 0){
						e_hour = 12; // for 12 am
				}
				if(e_hour < 10){
						ret = "0";
				}
				ret += e_hour+":";
				if(e_minute < 10){
						ret += "0";
				}
				ret += e_minute+" "+am_pm;;
				return ret;
    }
    public String getTimeInfo(){
				if(timeInfo.isEmpty()){
						String ret = "";
						if(isHourType()){
								ret += getHours()+" "+getHour_code();
						}
						else if(isMonetaryType()){
								ret += "$"+getAmount()+" "+getHour_code();
						}
						else{
								ret = getTime_in();
								if(isClockInOnly()){
										ret += " "+getHour_code();
										ret = "Clock IN "+ret;
								}
								else{
										ret += " - "+getTime_out();
								}
						}
						timeInfo = ret;
				}
				return timeInfo;
    }
    public boolean hasEarnReasons(){
				findEarnReasons();
				return earnReasons != null && earnReasons.size() > 0;
    }
    public List<EarnCodeReason> getEarnReasons(){
				return earnReasons;
    }
    /**
     * we run this only when we have id and hour_code_id
     * this is only for updates, for new records we will use js
     * service to provide the list
     */
    public void findEarnReasons(){
				if(earnReasons == null && !id.isEmpty()){
						getDocument();
						if(document != null){
								jobTask = document.getJob();
								// Employee emp = document.getEmployee();
								if(jobTask != null){
										department = jobTask.getGroup().getDepartment();
								}
								if(department != null && department.getName().equals("Police")){
										CodeReasonConditionList crcl = new CodeReasonConditionList();
										//salary_group_id = document.getJob().getSalary_group_id();
										salary_group_id = jobTask.getSalary_group_id();
										crcl.setSalary_group_id(salary_group_id);										
										group_id = document.getJob().getGroup_id();
										crcl.setGroup_id(group_id);	
										crcl.setDepartment_id(department.getId());
										crcl.setHour_code_id(hour_code_id);
										crcl.setActiveOnly();
										String back = crcl.lookFor();
										if(back.isEmpty()){
												List<EarnCodeReason> ones = crcl.getReasons();
												if(ones != null && ones.size() > 0){
														earnReasons = ones;
												}
										}
								}
						}
				}
    }
    public String getTimeInfoNextLine(){
				String ret = "";
				if(hasNextLine()){
						ret += " "+getHours()+" "+getHour_code();
				}
				return ret;
    }
    public boolean hasNextLine(){
				if(hourCode == null){
						getHourCode();
				}
				return !isHourType() && !isClockInOnly() && !isMonetaryType();
    }
    public boolean isHourType(){
				getHourCode();
				if(hourCode != null){
						return hourCode.isRecordMethodHours();
				}
				return false;// default is Time
    }
    public boolean isMonetaryType(){
				getHourCode();
				if(hourCode != null){
						return hourCode.isRecordMethodMonetary();
				}
				return false;// default is Time
    }		
		
    @Override
    public boolean equals(Object o) {
				if (o instanceof TimeBlock) {
						TimeBlock c = (TimeBlock) o;
						if ( this.id.equals(c.getId())) 
								return true;
				}
				return false;
    }
    public String checkWithAvailableBalance(){
				String msg = "";
				if(accrualBalance != null){
						if(accrualBalance.containsKey(hour_code_id)){
								String val = accrualBalance.get(hour_code_id);
								try{
										double aval = Double.parseDouble(val);
										if(!id.isEmpty() && hour_code_id_old.equals(hour_code_id)){
												if(hours_old > 0){
														aval += hours_old; // add old ones to balance
												}
										}
										if(hours > aval){
												msg = "Entered value "+hours+" greater than available balance "+aval;
										}
								}catch(Exception ex){}
						}
				}
				return msg;
    }
    public String checkForMinOrStep(){
				String str="";
				getHourCode();
				if(hourCode != null){
						if(hourCode.hasAccrualWarning()){
								AccrualWarning aw = hourCode.getAccrualWarning();
								if(aw.require_min()){
										if(hours < aw.getMin_hrs()){
												str = hourCode.getName()+" hours used of "+hours+" are less than min hours required of "+aw.getMin_hrs();
												return str;
										}
								}
								if(aw.require_step()){
										if(hours % aw.getStep_hrs() > 0.0){
												str = aw.getStep_warning_text();
										}
								}
						}
				}
				return str;
    }
    public String checkHourCodeForHoliday(){
				String ret = "";
				getHourCode();
				getDocument();
				if(hourCode != null){
						if(hourCode.isHolidayRelated()){
								document.prepareHolidays();
								if(!document.isHoliday(date)){
										ret = " You can not use hour code "+hourCode.getName()+" in non-holiday day";
								}
						}
				}
				return ret;
    }
    private void checkForOvernight(){
				if(startNextDay){
						if(begin_hour < 24){
								begin_hour += 24;
						}
				}
				if(overnight){
						if(end_hour < 24)
								end_hour += 24;
				}
    }
    private void adjustAccraulBalance(String code_id, double hrs){
				if(accrualBalance != null){
						if(accrualBalance.containsKey(code_id)){
								String val = accrualBalance.get(code_id);
								try{
										double aval = Double.parseDouble(val);
										aval = aval - hrs;
										if(aval >= 0){
												accrualBalance.put(code_id, ""+aval);
										}
								}catch(Exception ex){}
						}
				}
    }
    // given any documen id, we need to find all document ids
    // for employee with multiple jobs
    void findAllDocumentsForPayPeriod(){
				
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg = "";
				String qq = " select d.id from time_documents d,time_documents d2 "+
						" where d.pay_period_id = d2.pay_period_id "+
						" and d.employee_id=d2.employee_id and d2.id = ? ";				
				if(!document_id.isEmpty() && document_ids == null){
						logger.debug(qq);
						con = UnoConnect.getConnection();				
						if(con == null){
								msg = " Could not connect to DB ";
								logger.error(msg);
								return ;
						}
						try{
								document_ids = new HashSet<>();
								document_ids.add(document_id);
								pstmt = con.prepareStatement(qq);
								pstmt.setString(1, document_id);
								rs = pstmt.executeQuery();
								while(rs.next()){
										String str = rs.getString(1);
										if(str != null){
												document_ids.add(str);
										}
								}
						}
						catch(Exception ex){
								logger.error(ex+":"+qq);
						}
						finally{
								Helper.databaseDisconnect(pstmt, rs);
								UnoConnect.databaseDisconnect(con);
						}												
				}
    }
    /**
     * check if the data entry conflict with existing data on the same
     * day for Time entry, we do not check for hours entry,
     * we ignore inactive records
     select t.id from time_blocks t where t.document_id=2 and t.date = '2017-08-03' and ((13.0 > t.begin_hour+t.begin_minute/60. and 13.0 < t.end_hour+t.end_minute/60.) or (16.0 > t.begin_hour+t.begin_minute/60. and 16.0 < t.end_hour+t.end_minute/60.)) and t.inactive is null;
		 
    */
    public String checkForConflicts(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg = "";
				//
				findAllDocumentsForPayPeriod();
				double timeIn = begin_hour+begin_minute/60.;
				double timeOut = end_hour+end_minute/60.;
				String qq = " select t.id,t.hour_code_id from time_blocks t "+
						" where t.document_id=? and t.date = ? and "+
						"t.inactive is null and "+
						" ((t.clock_in is null and t.clock_out is null) or "+
						" (t.clock_in is not null and t.clock_out is not null)) ";
				if(!id.isEmpty()){
						qq += " and t.id <> ? "; // 5
				}
				String qq2 = " select t.id,t.hour_code_id from time_blocks t "+
						" where t.document_id=? and t.date = ? and "+
						"t.inactive is null and "+
						" t.clock_in is not null and t.clock_out is null ";
				if(!id.isEmpty()){
						qq2 += " and t.id <> ? ";
				}								
				qq2 += " and ? <= t.begin_hour+t.begin_minute/60. "+ 
						" and ? >= t.begin_hour+t.begin_minute/60. ";
				//
				//either non or both
				//
				if(hasNoClockInOut() || hasClockInOut()){
						qq +=" and (((? > t.begin_hour+t.begin_minute/60. "+ // start in between
								" and ? < t.end_hour+t.end_minute/60. "+
								" ) or "+
								" (? > t.begin_hour+t.begin_minute/60. "+ // end in between
								" and ? < t.end_hour+t.end_minute/60. "+
								" ) or ";
						// remove =
						qq +=" (? >= t.begin_hour+t.begin_minute/60. "+ // start in between
								" and ? <= t.end_hour+t.end_minute/60. "+
								" )) or ";
						// start and end in between
						qq +=" (? <= t.begin_hour+t.begin_minute/60. "+ 
								" and ? >= t.end_hour+t.end_minute/60.)) ";
						//
						// for updates we would have an id to exclude
						//
						
						if(timeOut < timeIn){
								msg = "Time IN is greater than time OUT";
								return msg;
						}
						// final
						qq = " select count(*) from ("+qq+" union all "+qq2+") t2 where t2.hour_code_id in (select id from hour_codes c where c.record_method='Time' )";
						
				}
				else if(isClockIn()){	// remove =
						qq +=" and (? > t.begin_hour+t.begin_minute/60. "+ // start in between
								" and ? < t.end_hour+t.end_minute/60.) ";
						// final
						qq = " select count(*) from ("+qq+") t2 where t2.hour_code_id in (select id from hour_codes c where c.record_method='Time' )";						
						
				}
				logger.debug(qq);
				con = UnoConnect.getConnection();				
				if(con == null){
						msg = " Could not connect to DB ";
						return msg;
				}
				try{
						pstmt = con.prepareStatement(qq);
						if(date.isEmpty())
								date = today;
						java.util.Date date_tmp = df.parse(date);						
						for(String doc_id: document_ids){
								int jj=1;						
								pstmt.setString(jj++, doc_id); // 1
								pstmt.setDate(jj++, new java.sql.Date(date_tmp.getTime())); // 2
								//
								if(!id.isEmpty()){
										pstmt.setString(jj++, id); // 3
								}														
								pstmt.setDouble(jj++, timeIn); // time in between
								pstmt.setDouble(jj++, timeIn); // 5
								if((clock_in.isEmpty() && clock_out.isEmpty())
									 || (!clock_in.isEmpty() && !clock_out.isEmpty())){
										
										pstmt.setDouble(jj++, timeOut); //6 time out between
										pstmt.setDouble(jj++, timeOut);
										
										pstmt.setDouble(jj++, timeIn);
										pstmt.setDouble(jj++, timeOut); // 9
										//
										// between start and end between two
										pstmt.setDouble(jj++, timeIn);
										pstmt.setDouble(jj++, timeOut); // 11
										
										//
										// for qq2
										pstmt.setString(jj++, document_id); // 12
										pstmt.setDate(jj++, new java.sql.Date(date_tmp.getTime())); //13
										if(!id.isEmpty()){
												pstmt.setString(jj++, id); //14
										}
										pstmt.setDouble(jj++, timeIn); //15
										pstmt.setDouble(jj++, timeOut);//16
								}
								rs = pstmt.executeQuery();
								if(rs.next()){
										int cnt = rs.getInt(1);
										if(cnt > 0){
												msg = "Data entry conflict on "+date+" times";
												errors += msg;
										}
								}
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
    public String doSelect(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "select t.id,"+
						"t.document_id,"+
						"t.hour_code_id,"+
						"t.earn_code_reason_id,"+
						"date_format(t.date,'%m/%d/%Y'),"+
						
						"t.begin_hour,t.begin_minute,"+
						"t.end_hour,t.end_minute,"+
						"t.hours,t.minutes,t.amount,t.clock_in,t.clock_out,t.inactive, datediff(t.date,p.start_date),c.name,c.description,cf.nw_code "+
						" from time_blocks t "+
						" join time_documents d on d.id=t.document_id "+
						" join pay_periods p on p.id=d.pay_period_id "+
						" join hour_codes c on t.hour_code_id=c.id "+
						" left join code_cross_ref cf on c.id=cf.code_id "+
						" where t.id=? ";
				logger.debug(qq);
				con = UnoConnect.getConnection();
				if(con == null){
						msg = " Could not connect to DB ";
						return msg;
				}				
				try{
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, id);
						rs = pstmt.executeQuery();
						if(rs.next()){
								String hrCode = rs.getString(16);
								double hrs = rs.getDouble(10);
								int mints = rs.getInt(11);
								if(hrCode != null){
										if(hrCode.indexOf("ONCALL") > -1){
												hrs = 1.0;
												mints = 60;
										}
										else if(hrCode.indexOf("CO") > -1){ // Call Out
												if(hrs < 3.){
														hrs = 3;
														mints = 180;
												}
										}
								}
								setVals(
												rs.getString(1),
												rs.getString(2),
												rs.getString(3),
												rs.getString(4),
												rs.getString(5),
												rs.getInt(6),
												rs.getInt(7),
												rs.getInt(8),
												rs.getInt(9),
												hrs,
												mints,
												rs.getDouble(12),
												rs.getString(13),
												rs.getString(14),
												false, // isHoliday
												null // holiday
												);
								
								setInactive(rs.getString(15) != null);
								setOrder_index(rs.getInt(16));
								setHour_code(rs.getString(17));
								setCode_desc(rs.getString(18));
								setNw_code(rs.getString(19));
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
    //
    // calculate hours for time entry type codes
    // such Reg, Temp, etc
    String prepareTimes(){

				String msg = "";
				if(isHourType()){
						// to put these in the end when ordered by begin_hour, begin_minute
						// this for PTO, Holiday, etc
						begin_hour = 23;
						begin_minute = 59;
						end_hour = 23;
						end_minute = 59;
						amount=0;
						if(minutes == 0){
								minutes = (int)(hours*60);
						}
				}
				else if(isMonetaryType()){
						begin_hour = 23;
						begin_minute = 59;
						end_hour = 23;
						end_minute = 59;
						hours=0;
						minutes=0;
				}
				else {
						minutes = (end_hour*60+end_minute) - (begin_hour*60+begin_minute);
						hours = minutes/60.;
						if(hours < 0){
								hours = 0.0;
								minutes = 0;
								msg = "Time in is greater than time out";
								errors += msg;
						}
						else if(minutes < 3){
								msg = "Time duration is less than 3 minutes: ( "+minutes+" )";
								hours = 0.0;
								minutes = 0;
								errors += msg;
						}
				}
				return msg;
    }
    /**
     * since repeat count should not exceed days in the payperiod
     * if so we adjust it
     */ 
    private void checkRepeatCount(){
				PayPeriod payPeriod = null;
				int days = 13; // days in pay period - 2 days (this day excluded);
				if(document == null){
						getDocument();
				}
				if(document != null){
						payPeriod = document.getPayPeriod();
						if(payPeriod != null){
								days = payPeriod.getDays() - 1;
						}
				}
				if(repeat_count > 0 && !include_weekends){
						for(int j=0;j<repeat_count+1;j++){
								int jj=order_index+j;
								if(weekendSet.contains(jj)){
										repeat_count++;
								}
						}
				}
				if(repeat_count+order_index > days){
						// we need to adjust it
						repeat_count = days - order_index;
						if(repeat_count < 0){
								repeat_count = 0;
						}
				}
    }
    private String prepareDateSet(){

				String msg = "";
				String payPeriodDates[] = new String[14];
				Set<String> weekendDates = new HashSet<>();
				PayPeriod payPeriod = null;
				if(document == null){
						getDocument();
				}
				if(document != null){
						int start_index=-1, end_index=-1;
						payPeriod = document.getPayPeriod();
						String dt = payPeriod.getStart_date();
						payPeriodDates [0] = dt;
						if(dt.equals(start_date))
								start_index = 0;
						else if(dt.equals(end_date))
								end_index = 0;
						for(int i=1;i<14;i++){
								String dt2 = Helper.getDateAfter(dt, i);
								if(dt2.equals(start_date))
										start_index = i;
								else if(dt2.equals(end_date))
										end_index = i;
								payPeriodDates[i] = dt2;
						}
						if(start_index < 0 || end_index < 0){
								msg = "invalid date range "+start_date+"-"+end_date;
								return msg;
						}
						if(start_index > end_index){ // swap
								int i=start_index;
								start_index = end_index;
								end_index = i;
						}
						for(int i=start_index;i<=end_index;i++){
								if(include_weekends)
										rangeDateSet.add(payPeriodDates[i]);										
								else if(!include_weekends && !weekendSet.contains(i))
										rangeDateSet.add(payPeriodDates[i]);										
						}
				}
				return msg;
    }		
    public String doSave(){
				if(time_in_changed && !time_out_changed){
						clock_in = "y";
						return doSaveForInOnly();
				}
				return doSaveForInOut();
    }
    private String doSaveForInOnly(){
				Connection con = null;
				PreparedStatement pstmt = null, pstmt2=null;
				ResultSet rs = null;
				String msg="", str="";
				if(hasErrors()){
						return errors;
				}
				if(action_type.isEmpty()) action_type="Add";
				String qq = "insert into time_blocks values(0,?,?,?,?, ?,?,?,?,?, ?,?,?,?, null) ";
				String qq2 = "select LAST_INSERT_ID()";
				msg = checkForConflicts();
				if(!msg.isEmpty()){
						System.err.println(" conflict "+msg);
						return msg;
				}
				if(document_id.isEmpty()){
						msg = " document not set ";
						return msg;
				}
				if(hour_code_id.isEmpty()){
						msg = " hour code not set ";
						return msg;
				}
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could do not get connection to DB";
						return msg;
				}
				logger.debug(qq);
				if(date.isEmpty())
						date = Helper.getToday();
				try{
						pstmt = con.prepareStatement(qq);
						int jj=1;
						pstmt.setString(jj++, document_id);
						pstmt.setString(jj++, hour_code_id);
						if(earn_code_reason_id.isEmpty())
								pstmt.setNull(jj++, Types.INTEGER);
						else
								pstmt.setString(jj++, earn_code_reason_id);								
						//
						// pstmt.setString(jj+=, earn_code_reason_id);
						java.util.Date date_tmp = df.parse(date);
						pstmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
						pstmt.setInt(jj++, begin_hour);
						pstmt.setInt(jj++, begin_minute);
						pstmt.setInt(jj++, end_hour);
						pstmt.setInt(jj++, end_minute);
						pstmt.setDouble(jj++, 0.0);
						pstmt.setInt(jj++, 0);
						pstmt.setDouble(jj++, 0.0);
						pstmt.setString(jj++, "y"); // clock_in
						pstmt.setNull(jj++,Types.CHAR); // clock_out
						pstmt.executeUpdate();
						//
						pstmt2 = con.prepareStatement(qq2);
						rs = pstmt2.executeQuery();
						if(rs.next()){
								id = rs.getString(1);
						}
						TimeBlockLog tbl = new TimeBlockLog(null,
																								document_id,
																								hour_code_id,
																								earn_code_reason_id,
																								date,
																								begin_hour,
																								begin_minute,
																								end_hour,
																								end_minute,
																								hours,
																								minutes,
																								amount,
																								clock_in,
																								clock_out,
																								id,
																								action_type,
																								action_by_id,
																								null,
																								location_id);
						msg += tbl.doSave();								
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(rs, pstmt, pstmt2);
						UnoConnect.databaseDisconnect(con);
				}
				return msg;
    }
    String doSaveForInOut(){
				//
				Connection con = null;
				PreparedStatement pstmt = null, pstmt2=null;
				ResultSet rs = null;
				String msg="", str="", mgtext="";
				if(hasErrors()){
						return errors;
				}
				if(action_type.isEmpty()) action_type="Add";
				String qq = "insert into time_blocks values(0,?,?,?,?, ?,?,?,?,?, ?,?,?,?,null) ";
				String qq2 = "select LAST_INSERT_ID()";
				checkForOvernight();
				if((clock_in.isEmpty() && clock_out.isEmpty())
					 || (!clock_in.isEmpty() && !clock_out.isEmpty())){
						msg = prepareTimes();
				}
				if(!msg.isEmpty()){
						return msg;
				}				
				if(document_id.isEmpty()){
						msg = " document not set ";
						return msg;
				}
				if(hour_code_id.isEmpty()){
						msg = " hour code not set ";
						return msg;
				}
				getHourCode();
				if(isMonetaryType()){
						if(hourCode != null){
								double dd = hourCode.getDefaultMonetaryAmount();
								if(dd > 0){ // use the default amount 
										amount = dd; 
								}
								else if(amount > CommonInc.maxMonetaryAmount){
										msg = ""+amount+" Amount entered exceeds max limit ";
										return msg;
								}
						}
				}
				if(hourCode != null){
						if(hourCode.requireReason() && earn_code_reason_id.isEmpty()){
								msg = "You need to pick a reason for ean code ";		
								return msg;
						}
				}
				//
				if(!start_date.equals(end_date)){
						msg = prepareDateSet();
						if(!msg.isEmpty())
								return msg;
				}
				else{
						if(date.isEmpty()){
								date = start_date;
						}
						if(date.isEmpty())
								date = today;
						rangeDateSet.add(date); // one date
				}
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could do not get connection to DB";
						return msg;
				}
				logger.debug(qq);
				try{
						// date2 is now the old date 
						for(String dd:rangeDateSet){
								date = dd;
								if(isHourType()){
										mgtext = checkWithAvailableBalance();
										if(mgtext.isEmpty()){
												mgtext = checkForMinOrStep();
										}
										if(mgtext.isEmpty()){
												mgtext = checkHourCodeForHoliday();
										}
								}
								else if(isMonetaryType()){
										// some earn codes are monetary but holiday related
										// such as HF (holiday firke)
										mgtext = checkHourCodeForHoliday();
										if(!mgtext.isEmpty()){
												return mgtext;
										}
								}
								else{
										id=""; // for multiple input
										mgtext = checkForConflicts();
								}
								if(!mgtext.isEmpty()){
										// we do not show the conflict for multiple entries
										if(rangeDateSet.size() == 1){ 
												msg += mgtext;
										}
								}
								else{
										pstmt = con.prepareStatement(qq);
										int jj=1;
										pstmt.setString(jj++, document_id);
										pstmt.setString(jj++, hour_code_id);
										if(earn_code_reason_id.isEmpty())
												pstmt.setNull(jj++, Types.INTEGER);
										else
												pstmt.setString(jj++, earn_code_reason_id);
										java.util.Date date_tmp = df.parse(date);
										pstmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
										pstmt.setInt(jj++, begin_hour);
										pstmt.setInt(jj++, begin_minute);
										pstmt.setInt(jj++, end_hour);
										pstmt.setInt(jj++, end_minute);
										pstmt.setDouble(jj++, hours);
										pstmt.setInt(jj++, minutes);
										pstmt.setDouble(jj++, amount);
										if(clock_in.isEmpty())
												pstmt.setNull(jj++,Types.CHAR);
										else
												pstmt.setString(jj++, "y");
										if(clock_out.isEmpty())
												pstmt.setNull(jj++,Types.CHAR);
										else
												pstmt.setString(jj++, "y");								
										pstmt.executeUpdate();
										//
										pstmt2 = con.prepareStatement(qq2);
										rs = pstmt2.executeQuery();
										if(rs.next()){
												id = rs.getString(1);
										}
										// if we are using accruals, we need to deduce the
										// amount we used in this day
										if(isHourType()){
												adjustAccraulBalance(hour_code_id, hours);
										}
										TimeBlockLog tbl = new TimeBlockLog(null,
																												document_id,
																												hour_code_id,
																												earn_code_reason_id,
																												date,
																												begin_hour, begin_minute,
																												end_hour, end_minute,
																												hours, minutes, amount,
																												clock_in,clock_out,
																												id,
																												action_type,
																												action_by_id,
																												null,
																												location_id);
										msg += tbl.doSave();
								}
						}
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(rs, pstmt, pstmt2);
				}
				return msg;
    }
		
    public String doUpdate(){
				/// for admins who changes the clock in but no clock out
				if(time_in_changed && !time_out_changed){
						return doUpdateForInOnly();
				}
				return doUpdateForInOut();
    }
    public String doUpdateForInOnly(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				if(action_type.isEmpty()) action_type="Update";
				if(id.isEmpty()){
						return " id not set ";
				}
				msg = checkForConflicts();
				if(!msg.isEmpty()){
						return msg;
				}
				String qq = "update time_blocks set begin_hour=?,begin_minute=? where id=? ";
				logger.debug(qq);
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could do not get connection to DB";
						return msg;
				}				
				try{
						pstmt = con.prepareStatement(qq);
						pstmt.setInt(1, begin_hour);
						pstmt.setInt(2, begin_minute);
						pstmt.setString(3, id);
						pstmt.executeUpdate();
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(pstmt, rs);
				}
				if(msg.isEmpty()){
						msg = doSelect(); // to get the other info for logging
				}
				if(msg.isEmpty()){
						TimeBlockLog tbl = new TimeBlockLog(null,
																								document_id,
																								hour_code_id,
																								earn_code_reason_id,
																								date,
																								begin_hour, begin_minute,
																								end_hour, end_minute,
																								hours, minutes, amount,
																								clock_in,clock_out,
																								id, action_type,
																								action_by_id,null,
																								location_id);
						msg = tbl.doSave();
				}				
				return msg;				

    }
    //
    // we can update expire date and inactive
    //
    public String doUpdateForInOut(){		
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				checkForOvernight();
				if(isClockIn() && !isClockOut()){
						if(time_out_changed){ /// for admins who
								setClock_out("y");
								setAction_type("ClockOut");
						}
				}
				if(action_type.isEmpty()) action_type="Update";
				if((clock_in.isEmpty() && clock_out.isEmpty())
					 || (!clock_in.isEmpty() && !clock_out.isEmpty())){				
						msg = prepareTimes();
				}
				if(!msg.isEmpty()){
						return msg;
				}
				if(hasErrors()){
						return errors;
				}
				if(id.isEmpty()){
						return " id not set ";
				}
				if(hour_code_id.isEmpty()){
						msg = " hour code not set ";
						return msg;
				}
				if(isHourType()){
						msg = checkWithAvailableBalance();
						if(msg.isEmpty()){
								msg = checkForMinOrStep();
						}
						if(msg.isEmpty()){
								msg = checkHourCodeForHoliday();
						}
						amount = 0;
				}
				else if(isMonetaryType()){
						if(hourCode != null){
								double dd = hourCode.getDefaultMonetaryAmount();
								if(dd > 0)
										amount = dd;
								hours = 0;
						}
				}
				else{
						if(hourCode != null){
								if(hourCode.requireReason() && earn_code_reason_id.isEmpty()){
										msg = "You need to pick a reason for ean code ";
										return msg;
								}
						}						
						msg = checkForConflicts();
				}				
				if(!msg.isEmpty()){
						return msg;
				}
				String qq = "update time_blocks set hour_code_id=?,earn_code_reason_id=?,begin_hour=?,begin_minute=?,end_hour=?,end_minute=?,hours=?,minutes=?,amount=?,clock_in=?,clock_out=? where id=? ";
				logger.debug(qq);
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could do not get connection to DB";
						return msg;
				}				
				try{
						pstmt = con.prepareStatement(qq);
						int jj=1;
						pstmt.setString(jj++, hour_code_id);
						if(earn_code_reason_id.isEmpty())
								pstmt.setNull(jj++, Types.INTEGER);
						else						
								pstmt.setString(jj++, earn_code_reason_id);
						pstmt.setInt(jj++, begin_hour);
						pstmt.setInt(jj++, begin_minute);
						pstmt.setInt(jj++, end_hour);
						pstmt.setInt(jj++, end_minute);
						pstmt.setDouble(jj++, hours);
						pstmt.setInt(jj++, minutes);
						pstmt.setDouble(jj++, amount);						
						if(clock_in.isEmpty())
								pstmt.setNull(jj++, Types.CHAR);
						else
								pstmt.setString(jj++, "y");
						if(clock_out.isEmpty())
								pstmt.setNull(jj++, Types.CHAR);
						else
								pstmt.setString(jj++, "y");	
						//
						// we do not change inactive here
						// doDelete will take care of it
						pstmt.setString(jj++, id);
						pstmt.executeUpdate();
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(pstmt, rs);
				}
				if(msg.isEmpty()){
						TimeBlockLog tbl = new TimeBlockLog(null,
																								document_id,
																								hour_code_id,
																								earn_code_reason_id,
																								date,
																								begin_hour, begin_minute,
																								end_hour, end_minute,
																								hours,
																								minutes,
																								amount,
																								clock_in,clock_out,
																								id, action_type,
																								action_by_id,null,
																								location_id);
						msg = tbl.doSave();
						msg = doSelect();
				}				
				// add log
				return msg;
    }
    /*
     * we do not delete record but we turn it inactive
     */
    public String doDelete(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				action_type = "Delete";
				String qq = "update time_blocks set inactive='y' where id=? ";
				logger.debug(qq);
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could do not get connection to DB";
						return msg;
				}							
				try{
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, id);
						pstmt.executeUpdate();
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(pstmt, rs);
				}
				msg = doSelect();
				if(msg.isEmpty()){
						TimeBlockLog tbl = new TimeBlockLog(null,
																								document_id,
																								hour_code_id,
																								earn_code_reason_id,
																								date,
																								begin_hour, begin_minute,
																								end_hour, end_minute,
																								hours,
																								minutes,
																								amount,
																								clock_in,clock_out,
																								id, action_type,
																								action_by_id,
																								null,
																								location_id);
						msg = tbl.doSave();
																								
				}
				return msg;
    }		

}
