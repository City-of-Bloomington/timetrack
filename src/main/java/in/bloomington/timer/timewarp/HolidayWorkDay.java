package in.bloomington.timer.timewarp;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */

import java.util.*;
import java.sql.*;
import java.text.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HolidayWorkDay{

		boolean debug = false;
		static final long serialVersionUID = 120L;				
		static Logger logger = LogManager.getLogger(HolidayWorkDay.class);
		//
		// holiday work day info
		//
		// for regular hours only, H1.0 not included
		//
		double hours = 0;
		String date_to = ""; // report date (end of pay period)
		String employee_id = ""; 
		int seq = -1;
		//	
    public HolidayWorkDay(){
    }	
    public HolidayWorkDay(boolean deb){
				debug = deb;
    }
    public HolidayWorkDay(boolean deb,
													double val,
													String val2,
													int val3,
													String val4
													){
				debug = deb;
				setHours(val);
				setDate_to(val2);
				setSeq(val3);
				setEmployee_id(val4);
    }		

		public double getHours(){
				return hours;
    }
		public String getDate_to(){
				return date_to;
    }
		public String getEmployee_id(){
				return employee_id;
    }		
		public int getSeq(){
				return seq;
		}
    //
    // setters
    //
    public void setHours (double val){
				if(val > 0f)		
						hours = val;
    }
    public void setDate_to(String val){
				if(val != null)		
						date_to = val;
    }
    public void setEmployee_id(String val){
				if(val != null)		
						employee_id = val;
    }		
    public void setSeq(int val){
				if(val  > -1){
						seq = val;
				}
    }

}
