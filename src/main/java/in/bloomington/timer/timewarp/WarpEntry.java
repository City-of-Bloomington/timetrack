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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WarpEntry{

		String code="", fullname="", hoursStr="", emp_num="", date="";
		//
		// added to handle dept report
		//
		double hours = 0, hourlyRate = 0;
		String hourlyRateStr = "0.00";
		final static double AnnualHours = 2080; // number of work hours per year
		DecimalFormat df = new DecimalFormat("#0.00");	
		boolean debug = false;
		static Logger logger = LogManager.getLogger(WarpEntry.class);
	
    public WarpEntry(){
    }	
    public WarpEntry(boolean deb){
				debug = deb;
    }
    public WarpEntry(boolean deb,
								 String val,
								 double val2
								 ){
				debug = deb;
				setCode(val);
				setHours(val2);
    }		
    public WarpEntry(boolean deb,
										 String val,
										 String val2,
										 String val3,
										 double val4,
										 double val5
										 ){
				debug = deb;
				setFullname(val);
				setEmpNum(val2);				
				setCode(val3);
				setHours(val4);
				setHourlyRate(val5);
    }
    public WarpEntry(boolean deb,
										 String val,
										 String val2,
										 String val3,
										 String val4,
										 double val5,
										 double val6
										 ){
				debug = deb;
				setFullname(val);
				setEmpNum(val2);				
				setCode(val3);
				setDate(val4);
				setHours(val5);
				setHourlyRate(val6);
    }		
		public String getHoursStr(){
				return hoursStr;
    }
		public double getHours(){
				return hours;
    }
		public double getHourlyRate(){
				return hourlyRate;
    }
		public String getHourlyRateStr(){
				return hourlyRateStr;
		}
		public double getAmount(){
				return hours*hourlyRate;
		}
		public String getAmountStr(){
				double ret =  hours*hourlyRate;
				String str = df.format(ret);
				return str;
		}		
		public String getCode(){
				return code;
    }
		public String getDate(){
				return date;
    }		
		public String getFullname(){
				return fullname;
		}
		public String getEmpNum(){
				return emp_num;
    }		
    //
    // setters
    //
    public void setCode (String val){
				if(val != null)		
						code = val;
    }
    public void setDate(String val){
				if(val != null)		
						date = val;
    }		
    public void setEmpNum(String val){
				if(val != null)		
						emp_num = val;
    }		
    public void setFullname(String val){
				if(val != null)		
						fullname = val;
    }
    public void setHours(double val){
				if(val > 0){
						hours = val;
						hoursStr = ""+df.format(hours);
				}
    }
    public void setHourlyRate(double val){
				if(val > 0){
						if(val > AnnualHours){
								hourlyRate = val/AnnualHours;
								hourlyRateStr = df.format(hourlyRate);
						}
						else
								hourlyRate = val;
				}
    }		

}
