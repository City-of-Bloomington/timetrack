package in.bloomington.timer.timewarp;
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
import javax.naming.*;
import javax.naming.directory.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TimeEntry{

		boolean debug = false;
		static Logger logger = LogManager.getLogger(TimeEntry.class);
		static final long serialVersionUID = 140L;				
		String id="", hoursStr="", date = "", earn_code="", earn_code_desc="",
				nw_code="",
				employee_id=""; // principal_id
		//
		// added to handle Planning dept report
		String fullname="", start_time="", end_time="";
		float hours = 0.0f;
		DecimalFormat df = new DecimalFormat("#0.00");	
		int daySeq = 0; // day seq in pay period:0,1,.. 13


	
    public TimeEntry(){
    }	
    public TimeEntry(boolean deb){
				debug = deb;
    }
    public TimeEntry(boolean deb,
										 String val,
										 String val2,
										 String val3,
										 String val4,
										 String val5,
										 String val6,
										 String val7,
										 String val8
										 ){
				debug = deb;
				setId(val);
		
				setEmployee_id(val2);		
				setDate(val3);
				setHoursStr(val4);
				setEarn_code(val5);
				setNw_code(val6);
				setEarn_code_desc(val7);
				setDaySeq(val8);
    }
    public TimeEntry(boolean deb,
										 String val,
										 String val2,
										 String val3,
										 float val4,
										 String val5,
										 String val6,
										 String val7
										 ){
				debug = deb;
				setDate(val);
				setStart_time(val2);
				setEnd_time(val3);				
				setHours(val4);
				setEarn_code(val5);
				setEarn_code_desc(val6);
				setFullname(val7);

    }		
	
		public String getId(){
				return id;
    }
		public String getEmployee_id(){
				return employee_id;
    }
		public String getHoursStr(){
				return hoursStr;
    }

		public String getDate(){
				return date;
		}
		public float getHours(){
				return hours;
    }	
		public String getEarn_code(){
				return earn_code;
    }
		public String getEarn_code_desc(){
				return earn_code_desc;
    }	
		public String getNw_code(){
				return nw_code;
    }	
		public int getDaySeq(){
				return daySeq;
		}
		public String getFullname(){
				return fullname;
		}
		public String getStart_time(){
				return start_time;
		}
		public String getEnd_time(){
				return end_time;
		}		
    //
    // setters
    //
    public void setId (String val){
				if(val != null)		
						id = val;
    }
    public void setEmployee_id(String val){
				if(val != null)		
						employee_id = val;
    }
    public void setNw_code (String val){
				if(val != null)		
						nw_code = val;
    }
    public void setEarn_code (String val){
				if(val != null)		
						earn_code = val;
    }
    public void setFullname(String val){
				if(val != null)		
						fullname = val;
    }
    public void setStart_time (String val){
				if(val != null)		
						start_time = val;
    }
    public void setEnd_time (String val){
				if(val != null)		
						end_time = val;
    }		
    public void setHoursStr(String val){
				if(val != null){
						hoursStr = val;
						try{
								hours = Float.parseFloat(val);
								if(hours < 0){
										hours = -hours;
										hoursStr = ""+df.format(hours);
								}
						}catch(Exception ex){}
				}
    }
    public void setHours(float val){
				if(val > 0f){
						hours = val;
						hoursStr = ""+df.format(hours);
				}
    }		
    public void setDate (String val){
				if(val != null){
						date = val;
				}
    }	
    public void setEarn_code_desc(String val){
				if(val != null)
						earn_code_desc = val;
    }	
    public void setDaySeq(String val){
				if(val != null){
						try{
								daySeq = Integer.parseInt(val);
						}catch(Exception ex){};
				}
    }	

}
