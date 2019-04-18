package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.Serializable;
import java.util.List;
import java.sql.*;
import javax.sql.*;
import java.text.SimpleDateFormat;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Block{

		static Logger logger = LogManager.getLogger(Block.class);
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");		
		static final long serialVersionUID = 250L;		
    String id="",
				document_id="",
				hour_code_id="",
				earn_code_reason_id="",
				clock_in="", clock_out="", holidayName="";
		String salary_group_id = "";
		String date = ""; // the user pick date, needed for PTO, Holiday etc
		double hours = 0.0, amount = 0.0; // for dollar value
		int begin_hour = 0, begin_minute=0, end_hour=0, end_minute=0;
		HourCode hourCode = null;
		CodeRef codeRef = null;
		EarnCodeReason earnCodeReason = null;
		JobTask jobTask = null;
		Document document = null;
		boolean hourCodeSet = false, isHoliday=false;
		boolean time_in_set = false, time_out_set=false, hours_set=false,
				amount_set=false;
		//
		// this flag needed for clock machines
		// when the managers change the clock in time but not clock out
		boolean time_in_changed=false, time_out_changed = false;
		String today = Helper.getToday();
    public Block( // for save
								 String val,
								 String val2,
								 // String val3,		
								 String val4,
								 int val5,
								 int val6,
								 int val7,
								 int val8,
								 double val9,
								 double val10
							 ){
				setVals(val,
								val2,
								//val3,
								val4,
								val5,
								val6, val7, val8, val9, val10);
		}		
		
    public Block(
								 String val,
								 String val2,
								 String val3,
								 // String val4,
								 String val5,
								 int val6,
								 int val7,
								 int val8,
								 int val9,
								 double val10,
								 double val11,
								 String val12,
								 String val13
							 ){
				setVals(val, val2, val3,
								// val4,
								val5,
								val6,
								val7, val8, val9, val10, val11, val12,val13);
		}
    public Block(
								 String val,
								 String val2,
								 String val3,
								 // String val4,
								 String val5,
								 int val6,
								 int val7,
								 int val8,
								 int val9,
								 double val10,
								 double val11,
								 String val12,
								 String val13,
								 boolean val14,
								 String val15
							 ){
				setVals(val, val2, val3,
								// val4,
								val5,
								val6, val7, val8, val9,
								val10, val11, val12, val13, val14, val15);
		}
		void setVals(
								 String val,
								 String val2,
								 // String val3,								 
								 String val4,
								 int val5,
								 int val6,
								 int val7,
								 int val8,
								 double val9,
								 Double val10
							 ){								
				setDocument_id(val);
				setHour_code_id(val2);
				// setEarn_code_reason_id(val3);
				setDate(val4);
				setBegin_hour(val5);
				setBegin_minute(val6);
				setEnd_hour(val7);
				setEnd_minute(val8);				
				setHoursDbl(val9);
				setAmountDbl(val10);
    }		
		void setVals(
								 String val,
								 String val2,
								 String val3,
								 // String val4,
								 String val5,
								 int val6,
								 int val7,
								 int val8,
								 int val9,
								 double val10,
								 double val11,
								 String val12,
								 String val13
							 ){								
				setId(val);
				setDocument_id(val2);
				setHour_code_id(val3);
				// setEarn_code_reason_id(val4);				
				setDate(val5);
				setBegin_hour(val6);
				setBegin_minute(val7);
				setEnd_hour(val8);
				setEnd_minute(val9);				
				setHoursDbl(val10);
				setAmountDbl(val11);
				setClock_in(val12);
				setClock_out(val13);
    }
		void setVals(
								 String val,
								 String val2,
								 String val3,
								 // String val4,
								 String val5,
								 int val6,
								 int val7,
								 int val8,
								 int val9,
								 double val10,
								 double val11,
								 String val12,
								 String val13,
								 boolean val14,
								 String val15
							 ){								
				setId(val);
				setDocument_id(val2);
				setHour_code_id(val3);
				// setEarn_code_reason_id(val4);
				setDate(val5);
				setBegin_hour(val6);
				setBegin_minute(val7);
				setEnd_hour(val8);
				setEnd_minute(val9);				
				setHoursDbl(val10);
				setAmountDbl(val11);
				setClock_in(val12);
				setClock_out(val13);
				setIsHoliday(val14);
				setHolidayName(val15);
    }		
    public Block(String val){
				setId(val);
    }
    public Block(){
    }		
    //
    // getters
    //
		public String getId(){
				return id;
    }
    public String getDocument_id(){
				return document_id;
    }
    public String getHour_code_id(){
				return hour_code_id;
    }
    public String getEarn_code_reason_id(){
				return earn_code_reason_id;
    }		
		// hourCode 
		public String getId_compound(){
				getHourCode();
				return hourCode.getId_compound();
		}
    public String getDate(){
				return date;
    }
    public int getBegin_hour(){
				return begin_hour;
    }		
    public int getBegin_minute(){
				return begin_minute;
    }
		public String getBeginHourMinute(){
				String ret = "";
				if(begin_hour == 0){
						ret = "00";
				}
				else if(begin_hour < 10){
						ret = "0"+begin_hour;
				}
				else {
						ret = ""+begin_hour;
				}
				ret += ":";
				if(begin_minute == 0){
						ret += "00";
				}
				else if(begin_minute  < 10){
						ret += "0"+begin_minute;
				}
				else{
						ret += begin_minute;
				}
				return ret;
		}
		public String getEndHourMinute(){
				String ret = "";
				if(end_hour == 0){
						ret = "00";
				}
				else if(end_hour < 10){
						ret = "0"+end_hour;
				}
				else {
						ret = ""+end_hour;
				}
				ret += ":";
				if(end_minute == 0){
						ret += "00";
				}
				else if(end_minute  < 10){
						ret += "0"+end_minute;
				}
				else{
						ret += end_minute;
				}
				return ret;
		}		
    public int getEnd_hour(){
				return end_hour;
    }		
    public int getEnd_minute(){
				return end_minute;
    }		
    public double getHours(){
				return hours;
    }
    public double getAmount(){
				return amount;
    }
    public String getAmountStr(){
				if(amount == 0.0){
						return "";
				}
				return ""+amount;
    }		
    public String getHoursStr(){
				if(hours == 0.0){
						return "";
				}
				return ""+hours;
    }
		public String getClock_in(){
				return clock_in;
    }
		public String getClock_out(){
				return clock_out;
    }
		public String getHolidayName(){
				return holidayName;
		}
		public boolean isHoliday(){
				return isHoliday;
		}
    //
    // setters
    //
    public void setId (String val){
				if(val != null)
						id = val;
    }
    public void setDocument_id (String val){
				if(val != null && !val.equals("-1"))
						document_id = val;
    }
		//
		// we may get something like 1-Hours, 3-Time
		//
    public void setHour_code_id(String val){
				if(val != null && !val.equals("-1")){
						if(val.indexOf("_") > -1){
								hour_code_id = val.substring(0,val.indexOf("_"));
						}
						else{
								hour_code_id = val;
						}
				}
    }
    public void setEarn_code_reason_id(String val){
				if(val != null && !val.equals("-1")){
						if(val.indexOf("_") > -1){
								earn_code_reason_id = val.substring(0,val.indexOf("_"));
						}
						else{
								earn_code_reason_id = val;
						}
				}
    }		
		public void setHolidayName(String val){
				if(val != null)
						holidayName = val;
    }
		public void setIsHoliday(boolean val){
				if(val)
						isHoliday = val;
		}
		//
		// set by user when click on calendar block
		// needed for hour code that uses hours instead of
		// begin time and end time
		//
    public void setDate(String val){ 
				if(val != null)
						date = val;
    }		
    public void setBegin_hour(int val){
				begin_hour = val;
    }
    public void setBegin_minute(int val){
				begin_minute = val;
    }
    public void setEnd_hour(int val){
				end_hour = val;
    }
    public void setEnd_minute(int val){
				end_minute = val;
    }
		public void setHoursDbl(Double val){
				if(val != null)
						hours = val;
		}
		public void setAmountDbl(Double val){
				if(val != null)
						amount = val;
		}		
    public void setHours(String val){
				if(val != null && !val.equals("")){
						try{
								hours = Double.parseDouble(val);
								hours_set = true;
						}catch(Exception ex){

						}
				}
				else{
						hours_set = false;
				}
    }
    public void setAmount(String val){
				if(val != null && !val.equals("")){
						try{
								amount = Double.parseDouble(val);
								amount_set = true;
						}catch(Exception ex){

						}
				}
				else{
						amount_set = false;
				}
    }		
    public void setClock_in(String val){
				if(val != null)
						clock_in = val;
    }
    public void setClock_out(String val){
				if(val != null)
						clock_out = val;
    }		
		public String toString(){
				return id;
		}
		public boolean equals(Object o) {
				if (o instanceof Block) {
						Block c = (Block) o;
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
		public void setHourCode(HourCode val){
				if(val != null){
						hourCode = val;
						hourCodeSet = true;
				}
		}
		public HourCode getHourCode(){
				if(!hourCodeSet){
						if(!hour_code_id.equals("")){
								HourCode one = new HourCode(hour_code_id);
								String back = one.doSelect();
								if(back.equals("")){
										hourCode = one;
										hourCodeSet = true;
								}
						}
						else{
								hourCode = new HourCode();
						}
				}
				return hourCode;
		}
		public EarnCodeReason getEarnCodeReason(){
				if(earnCodeReason == null){
						if(!earn_code_reason_id.equals("")){
								EarnCodeReason one = new EarnCodeReason(earn_code_reason_id);
								String back = one.doSelect();
								if(back.equals("")){
										earnCodeReason = one;
								}
						}
						else{
								earnCodeReason = new EarnCodeReason();
						}
				}
				return earnCodeReason;
		}		
		public boolean hasCodeRef(){
				getCodeRef();
				return codeRef != null;
		}
		//
		// we need this to get the New World reference hour_codes
		// for export purpose
		//
		public CodeRef getCodeRef(){
				if(codeRef == null && !hour_code_id.equals("")){
						CodeRefList cdr = new CodeRefList();
						cdr.setCode_id(hour_code_id);
						cdr.setIgnoreHash();
						String back = cdr.find();
						if(back.equals("")){
								List<CodeRef> ones = cdr.getCodeRefs();
								if(ones != null && ones.size() == 1){
										codeRef = ones.get(0);
								}
						}
				}
				return codeRef;
		}		
		public Document getDocument(){
				if(!document_id.equals("") && document == null){
						Document one = new Document(document_id);
						String back = one.doSelect();
						if(back.equals("")){
							 document = one;
						}
				}
				return document;
		}
		public String getSalary_group_id(){
				if(salary_group_id.equals("")){
						if(jobTask == null){
								getDocument();
								if(document != null){
										jobTask = document.getJob();
								}
						}
						if(jobTask != null){
								salary_group_id = jobTask.getSalary_group_id();
						}
				}
				return salary_group_id;
		}
		public void setSalary_group_id(String val){
				if(val != null)
						salary_group_id = val;
		}
		public boolean isClockIn(){
				return !clock_in.equals("");
		}
		public boolean isClockOut(){
				return !clock_out.equals("");
		}
		public boolean isClockInOnly(){
				return isClockIn() && !isClockOut();
		}
		public boolean hasClockInOut(){
				return isClockIn() && isClockOut();
		}
		public boolean hasNoClockInOut(){
				return !(isClockIn() || isClockOut());
		}
		public boolean isToday(){
				return date != null && date.equals(today);
		}

}
