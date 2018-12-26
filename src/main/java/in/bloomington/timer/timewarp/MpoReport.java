package in.bloomington.timer.timewarp;
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

public class MpoReport{

		static Logger logger = LogManager.getLogger(MpoReport.class);
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");		
		static DecimalFormat df4 = new DecimalFormat("#0.0000");
		static DecimalFormat df = new DecimalFormat("#0.00");
		String date_from="", date_to="";
		int year = 0, quarter = 0;
		String start_date ="", end_date=""; // temp holders
		List<TimeBlock> timeBlocks = null;
		List<WarpEntry> entries = null;
		String[] quarter_starts = {"","01/01/","04/01/","07/01/","10/01/"};
		String[] quarter_ends = {"","03/31/","06/30/","09/30/","12/31/"};		
		boolean debug = false, ignoreProfiles = false;
		String dept="", department_id="", type="html"; 
		String dept_ref_id="";
		String code="";
		String code2="";
		Hashtable<String, Profile> profiles = null;
		List<BenefitGroup> benefitGroups = null;
		Map<String, List<WarpEntry>> mapEntries = null;
		Hashtable<String, Double> hoursSums = null;
		Hashtable<String, Double> amountsSums = null;
		double totalHours =0, totalAmount=0;
		String errors = "";
    public MpoReport(){
				
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
		public List<TimeBlock> getTimeBlocks(){
				return timeBlocks;
		}
		public List<WarpEntry> getEntries(){
				return entries;
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
		public Map<String, List<WarpEntry>> getMapEntries(){
				return mapEntries;
		}
		public Hashtable<String, String> getHoursSums(){
				if(hoursSums != null){
						Hashtable<String, String> hash = new Hashtable<>();
						Set<String> set =hoursSums.keySet();
						for (String str:set){
								double val = hoursSums.get(str);
								hash.put(str, df.format(val));
						}
						return hash;
				}
				return null;
		}
		public Hashtable<String, String> getAmountsSums(){
				if(amountsSums != null){
						Hashtable<String, String> hash = new Hashtable<>();
						Set<String> set =amountsSums.keySet();
						for (String str:set){
								double val = amountsSums.get(str);
								hash.put(str, df.format(val));
						}
						return hash;
				}				
				return null;
		}
		public String getTotalHours(){
				return df.format(totalHours);
		}
		public String gettotalAmount(){
				return df.format(totalAmount);
		}
		public boolean hasEntries(){
				return entries != null && entries.size() > 0;
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
    public void setType(String val){
				if(val != null){
						type = val;
				}
    }
		public void setIgnoreProfiles(){
				ignoreProfiles = true; // we do not need profiles for FMLA report
		}
			 
		String setStartAndEndDates(){
				String msg = "";
				//
				// We use start_date and end_date so that we do not override date_from
				// and date_to if they are not set
				//
				start_date = date_from;
				end_date = date_to;
				if(year > 0 && quarter > 0){
						start_date = quarter_starts[quarter]+year;
						end_date = quarter_ends[quarter]+year;
				}
				if(start_date.equals("")){
						msg = "Year, quarter and start date not set ";
						return msg;
				}
				if(end_date.equals("")){
						end_date = Helper.getToday();
				}
				return msg;
		}
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
		String setProfiles(){
				String msg="";
				getBenefitGroups();
				ProfileList pl = new ProfileList(end_date, dept_ref_id, benefitGroups); 
				msg = pl.find();
				List<Profile> ones = pl.getProfiles();
				if(msg.equals("") && ones.size() > 0){
						profiles = new Hashtable<String, Profile>(ones.size());
						for(Profile one:ones){
								String str = one.getEmployee_number();
								// System.err.println(" mpo report "+str+" "+one);
								if(str != null && !str.equals(""))
										profiles.put(str, one);
						}
				}
				return msg;
		}
		public String find(){
				//
				// We are looking for Reg earn codes and its derivatives for
				// planning department
				//
				String msg = errors;
				msg += setStartAndEndDates();
				if(!msg.equals("")){
						return msg;
				}
				TimeBlockList tbl = new TimeBlockList();
				tbl.setDate_from(start_date);
				tbl.setDate_to(end_date);
				tbl.setCode(code);
				tbl.setCode2(code2);
				tbl.setDepartment_id(department_id);
				msg = tbl.find();
				if(!msg.equals("")){
						return msg;
				}				
				List<TimeBlock> ones = tbl.getTimeBlocks();
				if(ones != null && ones.size() > 0){
						timeBlocks = ones;
				}
				else{
						msg = "No match found";
						return msg;
				}
				return msg;
		}
		public String findHoursByNameCode(){
				String msg = "";
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				//
				// We are looking for Reg earn codes and its derivatives for
				// planning department
				//
				setStartAndEndDates();
				if(!ignoreProfiles){
						msg = setProfiles();
						if(!msg.equals("")){
								return msg;
						}
				}
				//
				// using subquery
				//
				String qq = "select tt.name,tt.code,tt.empnum,sum(hours) "+
						"from ( select "+
						" concat_ws(' ',e.first_name,e.last_name) AS name,"+
						" e.employee_number AS empnum,"+
						" c.description AS code, "+												
						" t.hours AS hours "+
						" from time_blocks t "+
						" join hour_codes c on t.hour_code_id=c.id "+						
						" join time_documents d on d.id=t.document_id "+
						" join pay_periods p on p.id=d.pay_period_id "+
						" join department_employees de on de.employee_id=d.employee_id "+
						" join employees e on d.employee_id=e.id "+
						" where de.department_id = ? and t.inactive is null and "+
						" p.start_date >= ? and p.end_date <= ? ";
				if(!code2.equals("")){
						qq += " and (c.name like ? or c.name like ?) ";
				}
				else{
						qq += " and c.name like ? ";
				}
				qq += " ) tt ";
				qq += " group by tt.code,tt.name,tt.empnum ";
				con = Helper.getConnection();
				if(con == null){
						msg = " Could not connect to DB ";
						logger.error(msg);
						return msg;
				}
				logger.debug(qq);
				try{
						pstmt = con.prepareStatement(qq);
						int jj=1;
						entries = new ArrayList<>();
						pstmt = con.prepareStatement(qq);
						pstmt.setString(jj++, department_id);
						java.util.Date date_tmp = dateFormat.parse(start_date);
						pstmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
						date_tmp = dateFormat.parse(end_date);
						pstmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
						pstmt.setString(jj++, code);
						if(!code2.equals(""))
								pstmt.setString(jj++, code2);
						rs = pstmt.executeQuery();
						jj=0;
						while(rs.next()){
								jj++;
								double hourly_rate = 0;
								if(!ignoreProfiles){
										if(profiles != null){
												String str = rs.getString(2);// emp_num;
												if(profiles.containsKey(str)){
														Profile pp = profiles.get(str);
														hourly_rate = pp.getHourlyRate();
												}
										}
								}
								WarpEntry one =
										new WarpEntry(debug,
															rs.getString(1), // name
															rs.getString(2), // emp num
															rs.getString(3), // code
															rs.getDouble(4), // hours
															hourly_rate); // hourly rate
								addToHash(one);
								entries.add(one);
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
				return msg;
		}
		void addToHash(WarpEntry val){
				if(val != null){
						if(mapEntries == null){
								mapEntries = new TreeMap<>();
						}
						String code = val.getCode();
						double hr = val.getHours();
						double amount = val.getAmount();
						totalHours += hr;
						totalAmount += amount;
						if(hr > 0){
								if(mapEntries.containsKey(code)){
										List<WarpEntry> list = mapEntries.get(code);
										list.add(val);
								}
								else{
										List<WarpEntry> ll = new ArrayList<>();
										ll.add(val);
										mapEntries.put(code, ll);
								}
								if(hoursSums == null){
										hoursSums = new Hashtable<>();
								}
								if(amountsSums == null){
										amountsSums = new Hashtable<>();
								}
								if(hoursSums.containsKey(code)){
										double hr2 = hoursSums.get(code);
										hr += hr2;
								}
								hoursSums.put(code, hr);
								//
								if(amountsSums.containsKey(code)){
										double amt2 = amountsSums.get(code);
										amount += amt2;
								}
								amountsSums.put(code, amount);
						}
				}
		}
		
}
