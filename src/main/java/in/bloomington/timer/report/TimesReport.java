package in.bloomington.timer.report;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.ArrayList;
import java.util.*;
import java.sql.*;
import java.text.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;
import in.bloomington.timer.list.*;

public class TimesReport{

		static Logger logger = LogManager.getLogger(TimesReport.class);
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");		
		static DecimalFormat df4 = new DecimalFormat("#0.0000");
		static DecimalFormat df = new DecimalFormat("#0.00");
		String date_from="", date_to="";
		int year = 0, quarter = 0;
		String start_date ="", end_date=""; // temp holders
		boolean debug = false, ignoreProfiles = false;
		String dept="", department_id="", type="html"; 
		String dept_ref_id="";
		String code="";
		String code2="";
		String employmentType = ""; // All, Full Time, Temp		
		Map<String, Set<String>> empJobs = null;
		Map<String, Set<String>> empCodes = null;
		Map<String, Map<String, Map<String, Map<String, String>>>> times = null;
		// Date-Range  Emp-Name     job        code        hours
		List<String> datesList = null;
		List<String[]> arrAll = null;
		Department department = null;
		String errors = "";
    public TimesReport(){
				
    }	
		public int getYear(){
				if(year == 0)
						return -1;
				return year;
    }
		public int getQuarter(){
				if(quarter == 0)
						return -1;
				return quarter;
    }
		public String getDate_from(){
				return date_from;
    }
		public String getDate_to(){
				return date_to;
    }
		public String getStart_date(){
				return start_date;
    }
		public String getEnd_date(){
				return end_date;
    }
		public String getType(){
				return type;
		}
		public boolean hasDepartment(){
				return !department_id.equals("");
		}
		public String getDepartment_id(){
				if(department_id.equals(""))  return "-1";
				return department_id;
		}		
		public void setEmploymentType(String val){
				if(val != null && !val.equals("-1")){
						employmentType = val; // salary_group_id = 3 for Temp
				}
		}
		public String getEmploymentType(){
				if(employmentType.equals(""))
						return "-1";
				return employmentType;
		}
		public Department getDepartment(){
				if(department == null && !department_id.equals("")){
						Department one = new Department(department_id);
						String back = one.doSelect();
						if(back.equals("")){
								department = one;
						}
				}
				return department;
		}
    //
    // setters
    //
    public void setYear (int val){
				if(val > 0)
						year = val;
    }
    public void setQuarter (int val){
				if(val > 0)
						quarter = val;
    }		
    public void setDate_from (String val){
				if(val != null){
						date_from = val;
				}
    }
    public void setDate_to (String val){
				if(val != null){
						date_to = val;
				}
    }
    public void setDepartment_id(String val){
				if(val != null && !val.equals("-1")){
						department_id = val;
				}
    }		
		
    public void setType(String val){
				if(val != null){
						type = val;
				}
    }
		public Map<String, Set<String>> getEmpJobs(){
				return empJobs;
		}
		public Map<String, Set<String>> getEmpCodes(){
				return empCodes;
		}
		public List<String> getDatesList(){
				return datesList;
		}
		public Map<String, Map<String, Map<String, Map<String, String>>>>  geTimes(){
				return times;
		}
		public List<String[]> getArrAll(){
				return arrAll;
		}
		public boolean hasData(){
				return arrAll != null && !arrAll.isEmpty();
		}
		void createOutput(){
				arrAll = new ArrayList<>();
				int size = datesList.size()+3;
				String[] arr = new String[size];
				arr[0] = "Employee";
				arr[1] = "Job";
				arr[2] = "Hour Code";
				int jj=3;
				for(String dd:datesList){
						arr[jj++] = dd;
				}
				arrAll.add(arr);
				Set<String> emps = empJobs.keySet();
				for(String emp:emps){
						Set<String> jobs = empJobs.get(emp);
						Set<String> codes = empCodes.get(emp);
						for(String job:jobs){
								for(String code:codes){
										arr = new String[size];
										arr[0] = emp;
										arr[1] = job;
										arr[2] = code;
										jj=3;
										for(String dater:datesList){
												if(times.containsKey(dater)){
														Map<String, Map<String, Map<String, String>>> map = times.get(dater);
														if(map.containsKey(emp)){
																Map<String, Map<String, String>> map2 = map.get(emp);

																if(map2.containsKey(job)){
																		Map<String, String> map3 = map2.get(job);

																		if(map3.containsKey(code)){
																				arr[jj++] = map3.get(code);
																		}
																		else{
																				arr[jj++] = "0";
																		}
																}
																else{
																		arr[jj++] = "0";
																}
														}
														else{
																arr[jj++] = "0";
														}
												}
										}
										arrAll.add(arr);
								}
						}
				}
		}
		//
		String setStartAndEndDates(){
				String msg = "";
				//
				// We use start_date and end_date so that we do not override date_from
				// and date_to if they are not set
				//
				start_date = date_from;
				end_date = date_to;
				if(year > 0 && quarter > 0){
						start_date = CommonInc.quarter_starts[quarter]+year;
						end_date = CommonInc.quarter_ends[quarter]+year;
				}
				start_date = start_date.trim();
				if(start_date.equals("")){
						msg = "Year and quarter or start date not set ";
						return msg;
				}
				if(end_date.equals("")){
						end_date = Helper.getToday();
				}
				return msg;
		}

		public String find(){
				String msg = "";
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String date_range="",full_name="", job_name="", hour_code="",
						hours="", amount="";
				//
				// We are looking for Reg earn codes and its derivatives for
				// planning department
				//
				msg = setStartAndEndDates();
				if(!msg.equals("")){
						return msg;
				}
				//
				// We are looking for Reg earn codes and its derivatives for
				// planning department
				//
				String qq = "select concat_ws(' - ',date_format(p.start_date, '%m/%d'), date_format(p.end_date,'%m/%d')) date_range,                                                     concat_ws(', ',e.last_name,e.first_name) full_name,                             ps.name job_name,                                                               c.name hour_code,			                                                         sum(t.hours) hours,                                                             sum(t.amount) amount                                                            from time_blocks t                                                              join hour_codes c on t.hour_code_id=c.id                                        join time_documents d on d.id=t.document_id                                     join pay_periods p on p.id=d.pay_period_id                                      join jobs j on d.job_id=j.id                                                    join positions ps on j.position_id=ps.id                                        join employees e on e.id=d.employee_id                                          join department_employees de on de.employee_id=e.id                             where                                                                           t.inactive is null                                                              and ((t.clock_in is not null and t.clock_out is not null) or (t.clock_in is null and t.clock_out is null))                                                       and (de.department_id=? or de.department2_id=?)                                 and p.start_date >= ?                                                           and p.end_date <= ?   ";
				if(employmentType.startsWith("Temp")){
						qq += " and j.salary_group_id=3 ";
				}
				else if(employmentType.indexOf("Other") > -1){ 
						qq += " and j.salary_group_id <> 3 ";
				}
				qq += "group by date_range,full_name,job_name,hour_code                                order by date_range,full_name,job_name,hour_code ";
				con = UnoConnect.getConnection();				
				if(con == null){
						msg = " Could not connect to DB ";
						logger.error(msg);
						return msg;
				}
				if(department_id.equals("")){
						msg = "Need to select a Department ";
						return msg;
				}
				if(start_date.equals("")){
						msg = "start date not set ";
						return msg;
				}
				if(end_date.equals("")){
						msg = "end date not set ";
						return msg;
				}				
				logger.debug(qq);
				empJobs = new TreeMap<>();
				empCodes = new TreeMap<>();
				datesList = new ArrayList<>();
				times = new TreeMap<>();
				try{
						pstmt = con.prepareStatement(qq);
						int jj=1;
						pstmt.setString(jj++, department_id);
						pstmt.setString(jj++, department_id);						
						java.util.Date date_tmp = dateFormat.parse(start_date);
						pstmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
						date_tmp = dateFormat.parse(end_date);
						pstmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
						rs = pstmt.executeQuery();
						while(rs.next()){
								date_range = rs.getString(1);
								full_name = rs.getString(2);
								job_name = rs.getString(3);
								hour_code = rs.getString(4);
								hours = rs.getString(5);
								amount = rs.getString(6);
								double amt = rs.getDouble(6);
								addToEmp(full_name, job_name, hour_code);
								if(!datesList.contains(date_range)){
										datesList.add(date_range);
								}
								if(amt > 0.0)
										addToTimes(date_range, full_name, job_name, hour_code, "$"+amount);									
								else
										addToTimes(date_range, full_name, job_name, hour_code, hours);										
		
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
				if(msg.equals("")){
						createOutput();
				}
				return msg;
		}
		void showOutput(){
				/**
				System.err.println(" dates "+datesList);				
				System.err.println(" emp jobs "+empJobs);
				System.err.println(" emps codes "+empCodes);
				System.err.println(" emp times "+times);
				for(String[] arr:arrAll){
						String line = "";
						for(int jj=0;jj< arr.length;jj++){
								if(!line.equals("")) line += ", ";
								line += arr[jj];
						}
						System.err.println(line);
				}
				*/
		}
		void addToEmp(String name, String job, String code){
				if(empJobs.containsKey(name)){
						Set<String> set = empJobs.get(name);
						set.add(job);
				}
				else{
						Set<String> set = new HashSet<>();
						set.add(job);
						empJobs.put(name, set);
				}
				if(empCodes.containsKey(name)){
						Set<String> set = empCodes.get(name);
						set.add(code);
				}
				else{
						Set<String> set = new HashSet<>();
						set.add(code);
						empCodes.put(name, set);
				}				
		}
		void addToTimes(String date,
										String name,
										String job,
										String code,
										String hours){
				if(times.containsKey(date)){
						Map<String, Map<String, Map<String, String>>> map =times.get(date);
						if(map.containsKey(name)){
								Map<String, Map<String, String>> map2 = map.get(name);
								if(map2.containsKey(job)){
										Map<String, String> map3 = map2.get(job);
										map3.put(code, hours);
								}
								else{
										Map<String, String> map3 = new TreeMap<>();
										map3.put(code, hours);
										map2.put(job, map3);
								}
						}
						else{
								Map<String, String> map3 = new TreeMap<>();
								map3.put(code, hours);
								Map<String, Map<String, String>> map2 = new TreeMap<>();
								map2.put(job, map3);
								map.put(name, map2);
						}
				}
				else{
						Map<String, String> map3 = new TreeMap<>();
						map3.put(code, hours);
						Map<String, Map<String, String>> map2 = new TreeMap<>();
						map2.put(job, map3);
						Map<String, Map<String, Map<String, String>>> map = new TreeMap<>();						
						map.put(name, map2);
						times.put(date, map);
				}
		}
				
}
