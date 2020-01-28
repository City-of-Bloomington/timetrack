package in.bloomington.timer;
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
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HandleProfile{

		boolean debug = false;
		static final long serialVersionUID = 53L;
		static Logger logger = LogManager.getLogger(HandleProfile.class);
		static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		static DecimalFormat df = new DecimalFormat("#0.00");
		List<BenefitGroup> benefitGroups = null;
		String date="", end_date="", // date of last pay period
				dept_ref_id=""; // dept referance in NW app, one or more values
		Hashtable<String, JobTask> ejobHash = null;
		List<Profile> profiles = null;
		List<String> jobTitles = null;
		Profile profile = null;
		//
    public HandleProfile(){
    }
		public HandleProfile(String val){
				setDate(val);
    }
    public HandleProfile(String val,
												 String val2){
				setDept_ref_id(val);
				setDate(val2);
    }
    public HandleProfile(boolean deb,
												 String val,
												 PayPeriod val2){
				debug = deb;
				setDept_ref_id(val);
				if(val2 != null){
						setDate(val2.getEnd_date()); // last day of pay period
				}
    }	
    //
    // setters
    //
    public void setDept_ref_id(String val){
				if(val != null){		
						dept_ref_id = val;
				}
    }
    public void setDate(String val){
				if(val != null){		
						date = val;
				}
    }
		public boolean hasProfiles(){
				return profiles != null && profiles.size() > 0;
		}
		public Profile getOneProfile(){
				return profile;
		}
		public List<String> getJobTitles(){
				return jobTitles;
		}
		private String prepareEmployeeJobs(){
				String msg = "";
				JobTaskList jbl = new JobTaskList();
				msg = jbl.findForUpdate();
				if(msg.equals("")){
						List<JobTask> ejobs = jbl.getJobs();
						if(ejobs != null && ejobs.size() > 0){
								ejobHash = new Hashtable<>();
								for(JobTask one:ejobs){
										ejobHash.put(one.getEmployee_number(), one);
								}
						}
				}
				return msg;
		}
		//
		public List<BenefitGroup> getBenefitGroups(){
				if(benefitGroups == null){
						BenefitGroupList tl = new BenefitGroupList();
						String back = tl.find();
						if(back.equals("")){
								List<BenefitGroup> ones = tl.getBenefitGroups();
								if(ones != null && ones.size() > 0){
										benefitGroups = ones;
								}
						}
				}
				return benefitGroups;
		}				
    public String process(){
		
				String msg="";
				String date = Helper.getToday();
				getBenefitGroups();
				msg = prepareEmployeeJobs();
				if(!msg.equals("") || ejobHash == null){
						msg += " could not find related employee jobs ";
						return msg;
				}
				ProfileList plist =
						new ProfileList(debug,
														benefitGroups,
														date,
														dept_ref_id, // could be empty for all
														true); // current only
				msg = plist.find();
				if(msg.equals("")){
						List<Profile> ones = plist.getProfiles();
						if(ones != null && ones.size() > 0){
								profiles = ones;
						}
				}
				for(Profile pp:profiles){
						String empNum = pp.getEmployee_number();
						if(ejobHash != null && ejobHash.containsKey(empNum)){
								JobTask job = ejobHash.get(empNum);
								double weekly_hrs = pp.getStWeeklyHrs();
								double hr_rate = pp.getHourlyRate();
								double comp_time_after = pp.getCompTimeAfter();
								double comp_time_multiple = pp.getCompTimeMultiple();
								double holiday_time_multiple = pp.getHolidayTimeMultiple();
								String job_name = pp.getJob_name();
								BenefitGroup bGroup = pp.getBenefitGroup();
								// we are skipping temp employee
								if(!bGroup.isTemporary()){
										job.compareWith(weekly_hrs,
																		hr_rate,
																		comp_time_after,
																		comp_time_multiple,
																		holiday_time_multiple,
																		job_name,
																		bGroup);
								}
						}
				}
				return msg;
		}
		/**
		 *
		 */
    public String processOne(String effective_date,
														 String emp_num,
														 String first_name,
														 String last_name
														 ){
				//
				String msg="";
				String date = "";
				String dt = Helper.getToday();
				if(effective_date == null || effective_date.equals("")){
						date = Helper.getDateAfter(dt, 21); // two weeks from now
				}
				else{
						if(effective_date.equals(dt)){
								date = Helper.getDateAfter(dt, 21); // two weeks from now
						}
						else if(Helper.isOldDate(effective_date)){ // ignore effective date
								date = Helper.getDateAfter(dt, 21); // two weeks from now
						}
						else{
								date = effective_date;
						}
				}
				getBenefitGroups();
				ProfileList plist = null;
				if(!emp_num.equals("")){
						plist = new ProfileList(date,
														benefitGroups,
														emp_num);
				}
				else{
						plist = new ProfileList(date,
																		benefitGroups,
																		first_name,
																		last_name);
				}
				msg = plist.findOne();
				if(msg.equals("")){
						List<Profile> ones = plist.getProfiles();
						if(ones != null && ones.size() > 0){
								profiles = ones;
								profile = ones.get(0);
						}
				}
				if(profiles == null){
						return msg;
				}
				return msg;
		}
    public String processJobs(String emp_num){
		
				String msg="";
				
				String date = Helper.getToday();
				getBenefitGroups();
				ProfileList plist =
						new ProfileList(date,
														benefitGroups,
														emp_num);
				msg = plist.findJobs();
				if(msg.equals("")){
						jobTitles = plist.getJobTitles();
				}				
				return msg;
		}
    public String processJobs(String emp_num, String active_date){
		
				String msg="";

				getBenefitGroups();
				ProfileList plist =
						new ProfileList(active_date,
														benefitGroups,
														emp_num);
				msg = plist.findJobs();
				if(msg.equals("")){
						jobTitles = plist.getJobTitles();
				}
				return msg;
		}			

}
