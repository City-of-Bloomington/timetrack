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
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Profile extends CommonInc{

		static final long serialVersionUID = 550L;		
		static Logger logger = LogManager.getLogger(Profile.class);
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
		int comp_time_after = 40;
		int st_weekly_hrs = 40; // standard weekly hours		
		double holiday_time_multiple = 1.0;
		double hourlyRate = 0; // needed for planning dept
		boolean debug = false;
		boolean overTimeElegible = false;
		String job_name="";
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
									 double val8,
									 String val9){
				super(deb);
				setNw_employee_id(val);
				setBenefitGroup_id(val2);
				setGrade(val4);
				setEmployee_number(val5);
				setBenefitGroup(val6);
				setStWeeklyHrs(val7);
				setHourlyRate(val8);
				setJob_name(val9);
				//
				setParams();
				// System.err.println(val5+" "+val8);
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
		public void setJob_name(String val){
				if(val != null)
						job_name = val;
    }		
		public void setBenefitGroup_id(String val){ // benefit Group Id
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
						st_weekly_hrs = (int)Double.parseDouble(val);
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
								comp_time_after = Integer.parseInt(val2);
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
		public String getJob_name(){
				return job_name;
		}
		public String getJobTitle(){
				return job_name;
		}		
		public String getEmployee_number(){
				return employee_number;
		}	
		public int getCompTimeAfter(){
				return comp_time_after;
		}
		public double getCompTimeMultiple(){
				return comp_time_multiple;
		}
		public double getHolidayTimeMultiple(){
				return holiday_time_multiple;
		}
		public int getStWeeklyHrs(){
				return st_weekly_hrs;
		}
		public double getHourlyRate(){
				return hourlyRate;
		}
		public String toString(){
				return employee_number+": "+comp_time_after+": "+comp_time_multiple;
		}
		public String getSalary_group_name(){
				if(bGroup != null)
						return bGroup.getSalary_group_name();
				return "";
		}
		public boolean isOverTimeElegible(){
				return overTimeElegible;
		}
		public boolean equals(Object obj){
				if(obj instanceof Profile){
						Profile one =(Profile)obj;
						if(!employee_number.isEmpty()){
								return employee_number.equals(one.getEmployee_number());
						}
						return nw_employee_id.equals(one.getId());
				}
				return false;				
		}
		public int hashCode(){
				int seed = 29;
				String var = "";
				if(!employee_number.isEmpty()){
						var = employee_number;
				}
				else if(!nw_employee_id.isEmpty()){
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
						if(bGroup.isExemptSpecial()){ // prob not used any more
								// use the defaults, 40, 1
						}
						else if(bGroup.isTemporary()){
								st_weekly_hrs = 20;
								comp_time_after = 40;
								comp_time_multiple = 1.5;
						}
						else if(bGroup.isExempt()){
								if(payGrade > 10){ // 11, 12
										comp_time_after = 50; // hours per week
								}
								else if(payGrade > 6){ // 7,8,9,10
										comp_time_after = 45;
								}
								else{ // for 6 or less
										comp_time_after = 40; // default
								}
						}
						else if(bGroup.isCedc()){
								if(payGrade > 10){ // 11, 12
										comp_time_after = 50; // hours per week
										bGroup.setSalary_group_name("Exempt");
								}
								else if(payGrade > 6){ // 7,8,9,10
										comp_time_after = 45;
										bGroup.setSalary_group_name("Exempt");
								}
						}						
						else if(bGroup.isPolice()){
								comp_time_after = 50;
								comp_time_multiple = 0;
						}
						else if(bGroup.isFireSworn()){
								st_weekly_hrs = 48;// minimum
								comp_time_after = 106;
								comp_time_multiple = 1.5;
						}
						else if(bGroup.isFireSworn5to8()){
								st_weekly_hrs = 40;
								comp_time_after = 40;
								comp_time_multiple = 1;
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
