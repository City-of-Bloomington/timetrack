package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.sql.*;
import javax.sql.*;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;
import in.bloomington.timer.timewarp.TimewarpManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ShiftTime{

    static Logger logger = LogManager.getLogger(ShiftTime.class);
    static final long serialVersionUID = 1500L;
    String id="", group_id="", pay_period_id="",
				default_hour_code_id="", start_time="", end_time="",
				dates="", added_by_id="", added_time="", processed="";
		Employee addedBy = null;
		Group group = null;
		PayPeriod payPeriod = null;
		HourCode defaultHourCode = null;
		int begin_hour=0,begin_minute=0,end_hour=0, end_minute = 0;
		double hours = 0, amount=0;
		int minutes = 0;
		String[] datesArr = null;
    public ShiftTime(){

    }		
    public ShiftTime(String val){
				setId(val);
    }
		
    public ShiftTime(String val,
										 String val2,
										 String val3,
										 String val4,
										 String val5,
										 String val6,
										 String val7,
										 String val8,
										 String val9,
										 String val10,
										 String val11,
										 boolean val12
								 ){
				setVals(val, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12);

		}
		void setVals(String val,
								 String val2,
								 String val3,
								 String val4,
								 String val5,
								 String val6,
								 String val7,
								 String val8,
								 String val9,
								 String val10,
								 String val11,
								 boolean val12){
				setId(val);
				setPay_period_id(val2);
				setGroup_id(val3);
				setDefault_hour_code_id(val4);
				setStartTime(val5);
				setEndTime(val6);
				setHours(val7);
				setAmount(val8);
				setDates(val9);
				setAdded_by_id(val10);
				setAddedTime(val11);
				setProcessed(val12);
    }
		
    //
    // getters
    //
    public boolean equals(Object o) {
				if (o instanceof ShiftTime) {
						ShiftTime c = (ShiftTime) o;
						if ( this.id.equals(c.getId())) 
								return true;
				}
				return false;
    }
    public int hashCode(){
				int seed = 37;
				if(!id.isEmpty()){
						try{
								seed += Integer.parseInt(id)*31;
						}catch(Exception ex){
								// we ignore
						}
				}
				return seed;
    }
    public String getId(){
				return id;
    }
    public String getPay_period_id(){
				return pay_period_id;
    }
    public String getGroup_id(){
				return group_id;
    }
    public String getDefault_hour_code_id(){
				return default_hour_code_id;
    }
		public String getId_compound(){
				if(!default_hour_code_id.isEmpty()){
						getDefaultHourCode();
						if(defaultHourCode != null){
								return defaultHourCode.getId_compound();
						}
				}
				return default_hour_code_id;
		}
    public String getStartTime(){
				return start_time;
    }
    public String getEndTime(){
				return end_time;
    }		
    public String getDates(){
				return dates;
    }
    public String getAdded_by_id(){
				return added_by_id;
    }
		public String getAddedTime(){
				return added_time;
    }
		public String getHours(){
				return ""+hours;
    }
		public String getAmount(){
				return ""+amount;
    }		
		public boolean getProcessed(){
				return !processed.isEmpty();
		}
    //
    // setters
    //
    public void setId(String val){
				if(val != null)
						id = val;
    }
    public void setPay_period_id(String val){
				if(val != null && !val.equals("-1"))
						pay_period_id = val;
    }
    public void setGroup_id(String val){
				if(val != null && !val.equals("-1"))
						group_id = val;
    }
    public void setDefault_hour_code_id(String val){
				if(val != null && !val.equals("-1")){
						if(val.indexOf("_") > -1){
								default_hour_code_id = val.substring(0, val.indexOf("_"));
						}
						else{
								default_hour_code_id = val;								
						}
				}
    }		
    public void setStartTime(String val){
				if(val != null)
						start_time = val;
    }
    public void setEndTime(String val){
				if(val != null)
						end_time = val;
    }
    public void setHours(String val){
				if(val != null && !val.isEmpty()){
						try{
								hours = Double.parseDouble(val);
						}catch(Exception ex){}
				}
    }
    public void setAmount(String val){
				if(val != null && !val.isEmpty()){
						try{
								amount = Double.parseDouble(val);
						}catch(Exception ex){}								
				}
    }		
		public String getStartEndTimes(){
				String ret = "";
				if(!start_time.isEmpty()){
						ret = start_time;
						if(!end_time.isEmpty()){
								ret += " - "+end_time;
						}
				}
				return ret;
		}
    public void setDates(String val){
				if(val != null)
						dates = val;
    }
    public void setAdded_by_id(String val){
				if(val != null)
						added_by_id = val;
    }
    public void setAddedTime(String val){
				if(val != null)
					 added_time = val;
    }
		public void setProcessed(boolean val){
				if(val)
						processed="y";

		}
		public Employee getAddedBy(){
				if(addedBy == null && !added_by_id.isEmpty()){
						Employee one = new Employee(added_by_id);
						String back = one.doSelect();
						if(back.isEmpty()){
								addedBy = one;
						}
				}
				return addedBy;
		}
		public Group getGroup(){
				if(group == null && !group_id.isEmpty()){
						Group one = new Group(group_id);
						String back = one.doSelect();
						if(back.isEmpty()){
								group = one;
						}
				}
				return group;

		}
		public PayPeriod getPayPeriod(){
				if(payPeriod == null && !pay_period_id.isEmpty()){
						PayPeriod one = new PayPeriod(pay_period_id);
						String back = one.doSelect();
						if(back.isEmpty()){
								payPeriod = one;
						}
				}
				return payPeriod;

		}
		public HourCode getDefaultHourCode(){
				if(defaultHourCode == null && !default_hour_code_id.isEmpty()){
						HourCode one = new HourCode(default_hour_code_id);
						String back = one.doSelect();
						if(back.isEmpty()){
								defaultHourCode = one;
						}
				}
				return defaultHourCode;

		}		
    public String toString(){
				return id;
    }
		void findDatesArray(){
				if(datesArr == null){
						if(dates != null && !dates.trim().isEmpty()){
								try{
										datesArr = dates.split(",");
								}
								catch(Exception ex){
										System.err.println(ex);
								}
						}
				}
		}
		String checkDates(){
				String back = "";
				findDatesArray();
				if(datesArr == null || datesArr.length == 0){
						back = "No dates are set ";
						return back;
				}				
				getPayPeriod();
				if(payPeriod == null){
						back = "No pay period selected ";
						return back;
				}
				for(String date:datesArr){
						if(!payPeriod.isDateWithin(date)){ 
								back = " date "+date+" is not within this pay period "+
										payPeriod.getDateRange();
								return back;
						}
				}
				return back;
		}
		void findHours(){
				if(!start_time.isEmpty() &&
					 start_time.indexOf(":") > -1 &&
					 !end_time.isEmpty() &&
					 end_time.indexOf(":") > -1 ){
						try{
								String[] arr = start_time.split(":");
								if(arr != null && arr.length == 2){
										begin_hour = Integer.parseInt(arr[0]);
										begin_minute = Integer.parseInt(arr[1]);
								}
								arr = end_time.split(":");
								if(arr != null && arr.length == 2){
										end_hour = Integer.parseInt(arr[0]);
										end_minute = Integer.parseInt(arr[1]);
								}								
								minutes = (end_hour*60+end_minute) - (begin_hour*60+begin_minute);
								hours = minutes/60;
						}
						catch(Exception ex){
								System.err.println(ex);								
						}
				}
		}
		public String doProcess(){
				String msg = "";
				TimewarpManager tmwrpManager = null;
				List<JobTask> jobs = null;
				if(id.isEmpty()){
						msg = " id not set ";
						return msg;
				}
				
				if(group_id.isEmpty()){
						msg = " group not found ";
						return msg;
				}
				findDatesArray();
				if(datesArr == null || datesArr.length == 0){
						msg = "no dates provided";
						return msg;
				}
				//
				// check dates are withing pay period
				//
				msg = checkDates();
				if(!msg.isEmpty()){
						return msg;
				}
				getDefaultHourCode();
				if(defaultHourCode != null && defaultHourCode.isRecordMethodMonetary()){
						double dd = defaultHourCode.getDefaultMonetaryAmount();
						if(dd > 0){
								amount = dd;
						}
						if(amount <= 0.){
								msg = "invalid amount "+amount;
								return msg;
						}						
				}
				else if(defaultHourCode.isRecordMethodHours()){
						if(hours <= 0.){
								msg = "invalid hours "+hours;
								return msg;
						}
				}
				else{ // time based
						findHours();
						if(hours <= 0.){
								msg = "invalid times ";
								return msg;
						}						
				}
				JobTaskList jl = new JobTaskList();
				jl.setGroup_id(group_id);
				jl.setPay_period_id(pay_period_id);
				msg = jl.find();
				if(!msg.isEmpty()){
						return msg;
				}
				jobs = jl.getJobs();
				if(jobs == null || jobs.size() == 0){
						msg = "No jobs found ";
						return msg;
				}
				int jj=1;
				for(JobTask job:jobs){
						System.err.println(jj+" emp "+job.getEmployee()+" "+job.getId());
						Document document = null;
						String document_id = "";
						document = new Document(job.getEmployee_id(),
																		pay_period_id,
																		job.getId(),
																		added_by_id);
						msg = document.findOrSave();
						if(!msg.isEmpty()){
								System.err.println(" doc error "+msg);
								return msg;
						}
						document_id = document.getId();
						for(String date:datesArr){
								System.err.println(" date "+date);
								if(date.indexOf("-") > -1){
										date = Helper.changeDateFormat(date);
								}
								TimeBlock tb = new TimeBlock(document.getId(),
																						 default_hour_code_id,
																						 null,// earn_code_reason_id
																						 date,
																						 begin_hour,
																						 begin_minute,
																						 end_hour,
																						 end_minute,
																						 hours,
																						 minutes,
																						 amount // amount
																						 );
								tb.setAction_by_id(added_by_id); // for logs
								if(defaultHourCode != null){
										tb.setHourCode(defaultHourCode);
								}
								msg += tb.doSaveForInOut();
						}
						tmwrpManager = new TimewarpManager(document_id);
						msg += tmwrpManager.doProcess();
				}
				if(msg.isEmpty()){
						msg = doUpdateProcessed();
				}
				return msg;
		}
    public String doSelect(){
				String back = "";
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String qq = "select id,pay_period_id,group_id,default_hour_code_id,"+
						" start_time,"+
						" end_time,hours, amount, dates,added_by_id,"+
						" date_format(added_time,'%m/%d/%Y %H:%i'),processed "+
						" from shift_times where id=?";
				con = UnoConnect.getConnection();
				if(con == null){
						back = "Could not connect to DB";
						return back;
				}
				logger.debug(qq);				
				try{

						pstmt = con.prepareStatement(qq);
						pstmt.setString(1,id);
						rs = pstmt.executeQuery();
						if(rs.next()){
								setVals(id,
												rs.getString(2),
												rs.getString(3),
												rs.getString(4),
												rs.getString(5),
												rs.getString(6),
												rs.getString(7),
												rs.getString(8),
												rs.getString(9),
												rs.getString(10),
												rs.getString(11),
												rs.getString(12) != null);												
						}
						else{
								back ="Record "+id+" Not found";
						}
				}
				catch(Exception ex){
						back += ex+":"+qq;
						logger.error(back);
				}
				finally{
						Helper.databaseDisconnect(pstmt, rs);
						UnoConnect.databaseDisconnect(con);
				}
				return back;
    }
    public String doSave(){
				Connection con = null;
				PreparedStatement pstmt = null, pstmt2=null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = " insert into shift_times values(0,?,?,?,?, ?,?,?,?,?, now(),null)";
				if(pay_period_id.isEmpty()){
						msg = "Pay period not set";
						return msg;
				}
				if(group_id.isEmpty()){
						msg = "Group not set";
						return msg;
				}
				if(default_hour_code_id.isEmpty()){
						msg = "Default hour code not set";
						return msg;
				}
				getDefaultHourCode();
				if(defaultHourCode != null && defaultHourCode.isRecordMethodMonetary()){
						double dd = defaultHourCode.getDefaultMonetaryAmount();
						if(dd > 0){
								amount = dd;
						}
						if(amount <= 0.){
								msg = "invalid amount "+amount;
								return msg;
						}						
				}
				else if(defaultHourCode.isRecordMethodHours()){
						if(hours <= 0.){
								msg = "invalid hours "+hours;
								return msg;
						}
				}
				else{ // time based
						if(start_time.isEmpty()){
								msg = "Start time not set";
								return msg;
						}
						if(end_time.isEmpty()){
								msg = "End time not set";
								return msg;
						}
						findHours();
						if(hours <= 0.){
								msg = "invalid times ";
								return msg;
						}						
				}				
				findDatesArray();
				if(datesArr == null || datesArr.length == 0){
						msg = "no dates provided";
						return msg;
				}
				msg = checkDates();
				if(!msg.isEmpty()){
						return msg;
				}
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could not connect to DB ";
						return msg;
				}
				logger.debug(qq);
				try{
						int cnt = 0;
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, pay_period_id);
						pstmt.setString(2, group_id);
						pstmt.setString(3, default_hour_code_id);
						if(start_time.isEmpty())
								pstmt.setNull(4, Types.VARCHAR);
						else
								pstmt.setString(4, start_time);
						if(end_time.isEmpty())
								pstmt.setNull(5, Types.VARCHAR);
						else
								pstmt.setString(5, end_time);
						pstmt.setString(6, ""+hours);						
						pstmt.setString(7, ""+amount);
						pstmt.setString(8, dates);						
						if(added_by_id.isEmpty()){
								pstmt.setNull(9, Types.INTEGER);
						}
						else
								pstmt.setString(9, added_by_id);
						pstmt.executeUpdate();

						//
						qq = "select LAST_INSERT_ID()";
						pstmt2 = con.prepareStatement(qq);
						rs = pstmt2.executeQuery();
						if(rs.next()){
								id = rs.getString(1);
						}
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(rs, pstmt, pstmt2);
						UnoConnect.databaseDisconnect(con);
				}
				if(msg.isEmpty()){
						msg = doSelect();
				}
				return msg;
    }
		public String doUpdateProcessed(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				processed="y";
				String qq = " update shift_times set "+
						" processed='y' "+
						" where id=?";
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could not connect to DB ";
						return msg;
				}
				logger.debug(qq);
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
						UnoConnect.databaseDisconnect(con);
				}
				return msg;
		}
    public String doUpdate(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = " update shift_times set pay_period_id=?, "+
						" group_id=?,default_hour_code_id=?,"+
						" start_time=?,end_time=?,"+
						" dates=? "+
						" where id=?";
				if(pay_period_id.isEmpty()){
						msg = "Pay period not set";
						return msg;
				}
				if(group_id.isEmpty()){
						msg = "Group not set";
						return msg;
				}
				if(default_hour_code_id.isEmpty()){
						msg = "Default hour code not set";
						return msg;
				}
				if(start_time.isEmpty()){
						msg = "Start time not set";
						return msg;
				}
				if(end_time.isEmpty()){
						msg = "End time not set";
						return msg;
				}
				if(id.isEmpty()){
						msg = "Id not set";
						return msg;
				}
				findHours();
				if(hours <= 0.){
						msg = "invalid times entered ";
						return msg;
				}
				findDatesArray();
				if(datesArr == null || datesArr.length == 0){
						msg = "no dates provided";
						return msg;
				}								
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could not connect to DB ";
						return msg;
				}
				logger.debug(qq);
				try{
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, pay_period_id);
						pstmt.setString(2, group_id);
						pstmt.setString(3, default_hour_code_id);
						pstmt.setString(4, start_time);
						pstmt.setString(5, end_time);
						pstmt.setString(6, dates);
						pstmt.setString(7, id);
						pstmt.executeUpdate();
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
