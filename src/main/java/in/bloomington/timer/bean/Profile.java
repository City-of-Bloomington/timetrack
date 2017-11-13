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
import org.apache.log4j.Logger;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;

public class Profile extends CommonInc{

		static final long serialVersionUID = 550L;		
		static Logger logger = Logger.getLogger(Profile.class);
		//
    private String employee_id="";	// from timetrack
		//
		// nw code for exempt/non exemp/ temp	
		// xGroupCode: NON-U RFULLnx, NON-U RFULLx,
		// TEMP,  temporary and interns
		// AFSCME RFT, union full time (fleet, fire)
		// POLICE SWORN, police
		// POLICE SWORN DET, police	detectives
		// CEDC 5/2, CEDC4/2 police
		//
		private String employee_number = "";
		String nw_employee_id="", // new world employee id
				benefitGroup_id = ""; // we needed this for accruals 
		double comp_time_multiple = 1.0;
		double comp_time_after = 40.0;
		double holiday_time_multiple = 1.0;
		double st_weekly_hrs = 40.0; // standard weekly hours
		double hourlyRate = 0; // needed for planning dept
		boolean debug = false;
		boolean overTimeElegible = false;
		BenefitGroup bGroup = null;
		int payGrade = 0;
		//
    public Profile(boolean deb){
				super(deb);
    }	
    public Profile(boolean deb, String val){
				super(deb);
				setEmployee_id(val); // old principal id
    }
    public Profile(boolean deb, String val, String val2){
				super(deb);
				setEmployee_id(val); // old pricipal id
				setEmployee_number(val2);
    }
		//
    public Profile(boolean deb,
									 String val,
									 String val2,
									 String val3, // not used
									 String val4,
									 String val5,
									 BenefitGroup val6,
									 String val7,
									 double val8){
				super(deb);
				setNw_employee_id(val);
				setBenefitGroup_id(val2);
				setGrade(val4);
				setEmployee_number(val5);
				setBenefitGroup(val6);
				setStWeeklyHrs(val7);
				setHourlyRate(val8);
				//
				setParams();
    }	
    //
    // getters
    //
		public void setEmployee_id(String val){
				if(val != null)
						employee_id = val;
    }
		public void setNw_employee_id(String val){
				if(val != null)
						nw_employee_id = val;
    }		
		public void setBenefitGroup_id(String val){ // benefit Group
				if(val != null)
						benefitGroup_id = val;
    }
		public void setBenefitGroup(BenefitGroup val){ // benefit Group
				if(val != null)
						bGroup = val;
    }	
		public void setGrade(String val){
				if(val != null){
						if(val.startsWith("Grade")){
								try{ // some items has Grade 08 (over)
										payGrade = Integer.parseInt(val.substring(6,8));
								}catch(Exception ex){
										System.err.println(ex);
								}
						}
				}
		}
    public void setEmployee_number(String val){
				if(val != null)
						employee_number = val;
    }
		// Check
		public void setStWeeklyHrs(String val){
				if(val != null){
						st_weekly_hrs = Double.parseDouble(val);
						if(bGroup != null && bGroup.isTemporary()){
								comp_time_after = 40;
								comp_time_multiple = 1.5;
								overTimeElegible = true;
						}
				}
		}
		public void setHourlyRate(double val){
				if(val > 0){
						hourlyRate = val;
				}
		}
		/**
		 * handle exceptions that are different than grade and exempt
		 * for certain employees
		 */
		public void setException(String val, String val2){
				try{
						if(val.contains("factor")){
								comp_time_multiple = Double.valueOf(val2).doubleValue();
						}
						else{
								comp_time_after = Double.valueOf(val2).doubleValue();
						}
				}catch(Exception ex){}
		}
		public BenefitGroup getBenefitGroup(){
				return bGroup;
		}
		public String getId(){
				return nw_employee_id;
		}
		//New World employee id
		public String getNw_employee_id(){
				return nw_employee_id;
		}
		public String getEmployee_id(){
				return employee_id;
		}		
		public String getEmployee_number(){
				return employee_number;
		}	
		public double getCompTimeAfter(){
				return comp_time_after;
		}
		public double getCompTimeMultiple(){
				return comp_time_multiple;
		}
		public double getHolidayTimeMultiple(){
				return holiday_time_multiple;
		}
		public double getStWeeklyHrs(){
				return st_weekly_hrs;
		}
		public double getHourlyRate(){
				return hourlyRate;
		}
		public String toString(){
				return employee_number+": "+comp_time_after+": "+comp_time_multiple;
		}
		public boolean isOverTimeElegible(){
				return overTimeElegible;
		}
		public boolean equals(Object obj){
				if(obj instanceof Profile){
						Profile one =(Profile)obj;
						if(!employee_number.equals("")){
								return employee_number.equals(one.getEmployee_number());
						}
						return nw_employee_id.equals(one.getId());
				}
				return false;				
		}
		public int hashCode(){
				int seed = 29;
				String var = "";
				if(!employee_number.equals("")){
						var = employee_number;
				}
				else if(!nw_employee_id.equals("")){
						var = nw_employee_id;
				}
				try{
						seed = Integer.parseInt(var);
				}catch(Exception ex){
				}
				return seed;
		}		
		//
		private void setParams(){
				if(bGroup != null){
						if(bGroup.isExempt()){
								if(payGrade > 10){ // 11, 12
										comp_time_after = 50; // hours per week
								}
								else if(payGrade > 6){ // 7,8,9,10
										comp_time_after = 45;
								}
						}
						else{ // non exempt
								comp_time_multiple = 1.5;
								holiday_time_multiple = 1.5;
								if(bGroup.isUnioned()){
										holiday_time_multiple = 2.0;
								}
						}
				}
		}


}
