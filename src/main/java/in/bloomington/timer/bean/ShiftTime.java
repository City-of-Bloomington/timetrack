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
		double hours = 0;
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
										 boolean val10
								 ){
				setVals(val, val2, val3, val4, val5, val6, val7, val8, val9, val10);

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
								 boolean val10){
				setId(val);
				setPay_period_id(val2);
				setGroup_id(val3);
				setDefault_hour_code_id(val4);
				setStartTime(val5);
				setEndTime(val6);
				setDates(val7);
				setAdded_by_id(val8);
				setAddedTime(val9);
				setProcessed(val10);
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
				if(!id.equals("")){
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
		public boolean getProcessed(){
				return !processed.equals("");
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
				if(val != null && !val.equals("-1"))
						default_hour_code_id = val;
    }		
    public void setStartTime(String val){
				if(val != null)
						start_time = val;
    }
    public void setEndTime(String val){
				if(val != null)
						end_time = val;
    }
		public String getStartEndTimes(){
				String ret = start_time;
				if(!end_time.equals("")){
						ret += " - "+end_time;
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
				if(addedBy == null && !added_by_id.equals("")){
						Employee one = new Employee(added_by_id);
						String back = one.doSelect();
						if(back.equals("")){
								addedBy = one;
						}
				}
				return addedBy;
		}
		public Group getGroup(){
				if(group == null && !group_id.equals("")){
						Group one = new Group(group_id);
						String back = one.doSelect();
						if(back.equals("")){
								group = one;
						}
				}
				return group;

		}
		public PayPeriod getPayPeriod(){
				if(payPeriod == null && !pay_period_id.equals("")){
						PayPeriod one = new PayPeriod(pay_period_id);
						String back = one.doSelect();
						if(back.equals("")){
								payPeriod = one;
						}
				}
				return payPeriod;

		}
		public HourCode getDefaultHourCode(){
				if(defaultHourCode == null && !default_hour_code_id.equals("")){
						HourCode one = new HourCode(default_hour_code_id);
						String back = one.doSelect();
						if(back.equals("")){
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
						if(dates != null && !dates.trim().equals("")){
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
				if(!start_time.equals("") &&
					 start_time.indexOf(":") > -1 &&
					 !end_time.equals("") &&
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
				if(id.equals("")){
						msg = " id not set ";
						return msg;
				}
				
				if(group_id.equals("")){
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
				if(!msg.equals("")){
						return msg;
				}
				findHours();
				if(hours <= 0.){
						msg = "invalid times ";
						return msg;
				}
				JobTaskList jl = new JobTaskList();
				jl.setGroup_id(group_id);
				jl.setPay_period_id(pay_period_id);
				msg = jl.find();
				if(!msg.equals("")){
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
						if(!msg.equals("")){
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
																						 0.0 // amount
																						 );
								tb.setAction_by_id(added_by_id); // for logs
								msg = tb.doSave();
						}
						tmwrpManager = new TimewarpManager(document_id);
						msg = tmwrpManager.doProcess();
				}
				if(msg.equals("")){
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
						" end_time,dates,added_by_id,"+
						" date_format(added_time,'%m/%d/%Y %H:%i'),processed "+
						" from shift_times where id=?";
				con = UnoConnect.getConnection();
				if(con == null){
						back = "Could not connect to DB";
						return back;
				}
				try{
						logger.debug(qq);
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
												rs.getString(10) != null);												
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
				String qq = " insert into shift_times values(0,?,?,?,?, ?,?,?,now(),null)";
				if(pay_period_id.equals("")){
						msg = "Pay period not set";
						return msg;
				}
				if(group_id.equals("")){
						msg = "Group not set";
						return msg;
				}
				if(default_hour_code_id.equals("")){
						msg = "Default hour code not set";
						return msg;
				}
				if(start_time.equals("")){
						msg = "Start time not set";
						return msg;
				}
				if(end_time.equals("")){
						msg = "End time not set";
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
				msg = checkDates();
				if(!msg.equals("")){
						return msg;
				}
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could not connect to DB ";
						return msg;
				}
				try{
						int cnt = 0;
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, pay_period_id);
						pstmt.setString(2, group_id);
						pstmt.setString(3, default_hour_code_id);
						pstmt.setString(4, start_time);
						pstmt.setString(5, end_time);
						pstmt.setString(6, dates);
						if(added_by_id.equals("")){
								pstmt.setNull(7, Types.INTEGER);
						}
						else
								pstmt.setString(7, added_by_id);
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
				if(msg.equals("")){
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
				if(pay_period_id.equals("")){
						msg = "Pay period not set";
						return msg;
				}
				if(group_id.equals("")){
						msg = "Group not set";
						return msg;
				}
				if(default_hour_code_id.equals("")){
						msg = "Default hour code not set";
						return msg;
				}
				if(start_time.equals("")){
						msg = "Start time not set";
						return msg;
				}
				if(end_time.equals("")){
						msg = "End time not set";
						return msg;
				}
				if(id.equals("")){
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
