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
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PayPeriodProcess{

		boolean debug = false;
		static Logger logger = LogManager.getLogger(PayPeriodProcess.class);
		static final long serialVersionUID = 180L;				
		String regCode="Reg"; // TEMP for temporary work
		Employee employee = null;
		Profile profile = null;
		PayPeriod payPeriod = null;
		Document document = null;
		boolean twoDifferentYears = false;
		boolean weekOneHasSplit = false;
		boolean isUtil = false;
		Department department = null;
		static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		static DecimalFormat df = new DecimalFormat("#0.00");
		static DecimalFormat df4 = new DecimalFormat("#0.0000");

		final static String profHrsCode = "PROF HRS";
		WeekEntry week1 = null, week2 = null;
		//
		// List<Holiday> holys = null;
		HolidayList holyList = null;
		int year = 2017, splitDay = 14; // default 14 no split
		String status = "No data";
		String job_id = "", job_name="";
		CodeRefList codeRefList = null;
		// for HAND only
		Hashtable<CodeRef, String> handHash = null;
		//
		// a flag to distinguish between html or csv
		boolean csvOutput = false; // default html
		//
		// the compt time critical limit that the employee must keep
		// below that limit by using CU instead of PTO
		//
		static double compTimeAccrualLimit = 10f;
		
		boolean isHand = false; // HAND dept has special treatment
		boolean multipleJobs = false;
		//		
		// two week entries for display purpose
		//
		Map<String, Entry> entries = null;
		Entry firstEntry = null, lastEntry = null;
		//
		public PayPeriodProcess(Employee val,
														Profile val2,
														PayPeriod val3,
														HolidayList val4,
														boolean val5, // HAND flag
														boolean val6,// csv flag
														boolean val7,
														Department val8){  // isUtil 
				setEmployee(val);
				setProfile(val2);
				setPayPeriod(val3);
				setHolidayList(val4);
				setIsHand(val5);
				setCsvOutuput(val6);
				setIsUtil(val7);
				setDepartment(val8);
				//
				// prepare the objects
				//
				setWeekEntries();
		}
		// employee with multiple jobs
		public PayPeriodProcess(Employee val,
														Profile val2,
														PayPeriod val3,
														HolidayList val4,
														boolean val5, // HAND flag
														boolean val6,// csv flag
														boolean val7, // isUtil 
														String val8,
														Department val9){ //job_id
				setEmployee(val);
				setProfile(val2);
				setPayPeriod(val3);
				setHolidayList(val4);
				setIsHand(val5);
				setCsvOutuput(val6);
				setIsUtil(val7);
				setJob_id(val8);
				multipleJobs = true;
				setDepartment(val9);
				//
				// prepare the objects
				//
				setWeekEntries();
		}		
		public void setEmployee(Employee val){
				if(val != null){
						employee = val;
				}
		}
		public void setDepartment(Department val){
				if(val != null){
						department = val;
						if(department.isHand()){
								isHand = true;
						}
						else if(department.isUtilities()){
								isUtil = true;
						}
				}
		}		
		public void setProfile(Profile val){
				if(val != null){
						profile = val;
						BenefitGroup bgroup = profile.getBenefitGroup();
						if(bgroup != null){
								if(bgroup.isTemporary()){
										regCode = "TEMP";
								}
						}
				}
		}
		public void setHolidayList(HolidayList val){
				if(val != null){
					 holyList = val;
				}
		}
		public void setIsHand(boolean val){
				isHand = val;
		}
		public void setJob_id(String val){
				if(val != null)
						job_id = val;
		}
		public void setIsUtil(boolean val){
				isUtil = val;
		}		
		public void setCsvOutuput(boolean val){
				csvOutput = val;
		}
		public Employee getEmployee(){
				return employee;
		}
		public Profile getProfile(){
				return profile;
		}
		private void setWeekEntries(){
		
				if(payPeriod.hasTwoDifferentYears()){
						twoDifferentYears = true;
						splitDay = payPeriod.getDaysToYearEnd();
						//
						if(splitDay < 7){
								weekOneHasSplit = true;
								week1 = new WeekEntry(debug, profile, splitDay, department);
								week2 = new WeekEntry(debug, profile, department);
						}
						else{
								week1 = new WeekEntry(debug, profile, department);
								week2 = new WeekEntry(debug, profile, splitDay-7, department);
						}
				}
				else{
						week1 = new WeekEntry(debug, profile, department);
						week2 = new WeekEntry(debug, profile, department);
				}
		}
    //
    public void setPayPeriod (PayPeriod val){
				if(val != null){
						payPeriod = val;
						year = payPeriod.getStartYear();
						//
						// replace this with document getTimeActions 
						// findStatus();
				}
    }

		//
		String getStatus(){
				return status;
		}
		public String getRegCode(){
				if(isUtil && csvOutput){
						return "u"+regCode;
				}
				return regCode;
		}
		double getWeek1Total(){
				return week1.getTotalHours();
		}
		double getWeek2Total(){
				return week2.getTotalHours();		
		}
		double get2WeekTotal(){
				return getWeek1Total()+getWeek2Total();
		}
		double getWeek1Regular(){
				return week1.getRegularHours();
		}
		double getWeek2Regular(){
				return week2.getRegularHours();		
		}
		double getWeek1NetRegular(){
				return week1.getNetRegular();
		}
		double getWeek2NetRegular(){
				return week2.getNetRegular();
		}	
		double get2WeekRegular(){
				return getWeek1Regular()+getWeek2Regular();
		}
		public double get2WeekNetRegular(){
				return getWeek1NetRegular()+getWeek2NetRegular();
		}
		public double getTwoWeekNetRegular(){
				return getWeek1NetRegular()+getWeek2NetRegular();
		}
		public String getProfHrsCode(){
				return profHrsCode;
		}
		boolean hasProfHours(){
				return week1.getProfHours() + week2.getProfHours() > 0;
		}
		boolean hasTwoDifferentYears(){
				return twoDifferentYears;
		}
		boolean weekOneHasSplit(){
				return weekOneHasSplit;
		}
		double getProfHours(){
				return week1.getProfHours() + week2.getProfHours();
		}
		//
		public void addToHash(String nw_code, double hours, Hashtable<String, Double> hval){
				if(!nw_code.equals("") && hours > 0){
						if(hval.containsKey(nw_code)){
								double hrs = hval.get(nw_code).doubleValue();
								hrs += hours;
								hval.put(nw_code, hrs); // adjust total
						}
						else{ // add new entry
								hval.put(nw_code, hours);
						}
				}
		}		
		public void addToHash(TimeBlock te, Hashtable<String, Double> hval){
				if(hval.containsKey(te.getNw_code())){
						double hours = hval.get(te.getNw_code()).doubleValue();
						hours += te.getHours();
						hval.put(te.getNw_code(), hours); // adjust total
				}
				else{ // add new entry
						hval.put(te.getNw_code(), te.getHours());
				}
		}

		public Hashtable<String, Double> getNonRegularHours(){
				// return hash;
				Hashtable<String, Double> reg1 = week1.getNonRegularHours();
				Hashtable<String, Double> reg2 = week2.getNonRegularHours();
				if(!reg2.isEmpty())
						mergeTwoHashes(reg2, reg1);
				return reg1;		
				
		}

		public Hashtable<String, Double> getWeek1NonRegularHours(){
				return week1.getNonRegularHours();
		}
		public Hashtable<String, Double> getWeek2NonRegularHours(){
				return week2.getNonRegularHours();
		}
		public Hashtable<String, Double> getWeek1RegHash(){
				return week1.getRegularHash();
		}
		public Hashtable<String, Double> getWeek2RegHash(){
				return week2.getRegularHash();
		}
		/**
		 * consolication of two weeks regular hours (needed for HAND)
		 */
		public Hashtable<String, Double> get2WeekRegularHash(){
				Hashtable<String, Double> reg1 = week1.getRegularHash();
				Hashtable<String, Double> reg2 = week2.getRegularHash();
				if(!reg2.isEmpty())
						mergeTwoHashes(reg2, reg1);
				return reg1;		
		}
		public Hashtable<String, Double> getWeekSplitNonRegularHours(int week_no, int split_no){
				WeekEntry week = null;		
				if(week_no == 1)
						week = week1;
				else
						week = week2;
				if(split_no == 1)
						return week.splitOne.getNonRegularHours();
				else
						return week.splitTwo.getNonRegularHours();
		}
		public Hashtable<String, Double> getWeek1All(){
				return week1.getAll();
		}
		public Hashtable<String, Double> getWeek2All(){
				return week2.getAll();
		}
		// two weeks all
		public Hashtable<String, Double> getAll(){
				Hashtable<String, Double> all = new Hashtable<String, Double>();
				Hashtable<String, Double> w1all = week1.getAll();
				if(!w1all.isEmpty())
						mergeTwoHashes(w1all, all);
				Hashtable<String, Double> w2all = week2.getAll();
				if(!w2all.isEmpty())
						mergeTwoHashes(w2all, all);
				if(isUtil && csvOutput){
						Hashtable<String, Double> all2 = new Hashtable<String, Double>();
						Set<String> keySet = all.keySet();
						for(String key:keySet){
								Double val = all.get(key);
								if(val != null){
										all2.put("u"+key, val);
								}
						}
						return all2;
				}
				return all;
		}
		public Hashtable<String, ArrayList<Double>> getAll2(){
				Hashtable<String, Double> all2 = getAll();
				Hashtable<String, ArrayList<Double>> all = new Hashtable<>();
				if(!all2.isEmpty()){
						Set<String> keySet = all2.keySet();
						for(String key:keySet){
								Double val = all2.get(key);
								if(val != null){
										ArrayList<Double> lval = new ArrayList<>();
										if(key.indexOf("ONCALL") == -1){
												lval.add(val);
										}
										else{
												int cnt = (int)(val.doubleValue());
												for(int i=0;i<cnt;i++){
																lval.add(new Double(1.0));
														}
												}
										all.put(key, lval);
								}
						}
						
				}
				return all;
		}		
		boolean hasWeek1ProfHours(){
				return week1.getProfHours() > 0;
		}
		boolean hasWeek2ProfHours(){
				return week2.getProfHours() > 0;
		}
		public boolean hasMultipleJobs(){
				return multipleJobs;
		}
		public String getJob_name(){
				if(!job_id.equals("") && job_name.equals("")){
						JobTask job = new JobTask(job_id);
						String back = job.doSelect();
						if(back.equals("")){
								Position pos = job.getPosition();
								if(pos != null){
										job_name = pos.getAlias();
								}
						}
				}
				return job_name;
		}
		public double getWeek1ProfHours(){
				return week1.getProfHours();
		}
		public double getWeek2ProfHours(){
				return week2.getProfHours();
		}
		//
		public String toString(){
				return "";
		}
	  String findDocument(){
				String back = "";
				if(document == null){
						if(employee == null && payPeriod == null){
								back = " no employee or/and pay period set";
								return back;
						}
						DocumentList dl = new DocumentList();
						dl.setEmployee_id(employee.getId());
						dl.setPay_period_id(payPeriod.getId());
						if(!job_id.equals("")){
								dl.setJob_id(job_id);
						}
						back = dl.find();
						if(back.equals("")){
								List<Document> ones = dl.getDocuments();
								if(ones != null && ones.size() > 0){
										document = ones.get(0);
								}
								else{
										back = "No time entry found";
								}
						}
				}
				return back;
		}
		//
		public String find(){
				String msg = "";
				if(document == null){
						msg = findDocument();
				}
				if(!msg.equals("")){
						return msg;
				}
				document.prepareDaily(false); //false: ignore empty blocks
				List<TimeBlock> blocks = document.getTimeBlocks();
				if(blocks == null || blocks.size() == 0){
						msg = "No time entry found";
						return msg;
				}
				if(document.isTemporary()){
						regCode =  "TEMP";
				}
				try{
						for(TimeBlock one:blocks){
								String code2 = one.getHour_code();
								String code = code2.toLowerCase();
								String nw_code = one.getNw_code();
								String code_desc = one.getCode_desc(); // old cat
								int day = one.getOrder_index();
								String date = one.getDate();
								double hours = one.getHours();
								//
								// if not in refrence table we use Kuali earn code
								if(nw_code.equals("")) nw_code = code2;
								if(day > 13) continue;
								if(code.startsWith("reg") ||
									 code.endsWith("reg") ||
									 code.startsWith("temp")){
										if(holyList.isHoliday(date)){
												HolidayWorkDay hday = new HolidayWorkDay(debug, hours, payPeriod.getEnd_date(), day, employee.getId());
												if(day < 7){
														week1.setHolidayWorkDay(hday);
												}
												else{
														week2.setHolidayWorkDay(hday);
												}
										}
								}
								if(day < 7){
										week1.add(one);
								}
								else{
										week2.add(one);
								}
						}
						//
						// these are PTO, CU, HCU, FML, ..
						//
						week1.doCalculations();
						week2.doCalculations();
						if(week1.hasExessHours()){
								week1.createEarnRecord();
						}
						if(week2.hasExessHours()){
								week2.createEarnRecord();
						}
						// 
						if(week1.hasProfHours()){
								week1.createProfRecord();
						}
						if(week2.hasProfHours()){
								week2.createProfRecord();
						}
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg);
				}
				return msg;
		}
		void mergeTwoHashes(Hashtable<String, Double> tFrom,
											 Hashtable<String, Double> tTo){
				if(tFrom != null && tFrom.size() > 0){
						Enumeration<String> keys = tFrom.keys();
						while(keys.hasMoreElements()){
								String key = keys.nextElement();
								Double val = tFrom.get(key);
								if(tTo.containsKey(key)){
										double val2 = tTo.get(key).doubleValue() + val.doubleValue();
										tTo.put(key, val2);
								}
								else{
										tTo.put(key, val);
								}
						}
				}
		}
		/**
		 * since the regular hours are adjusted they considered net reg
		 */
		Hashtable<String, Double> getRegularHashForFirstPay(){
				Hashtable<String, Double> table = null, table2 = null;				
				if(weekOneHasSplit){
						//
						// we return the first week split one
						table = week1.splitOne.getRegularHash();
				}
				else{
						// all first week plus firs part of week2
						//
						table = week1.getRegularHash();
						table2 = week2.splitOne.getRegularHash();
						//
						// combine them
						if(!table2.isEmpty())
								mergeTwoHashes(table2, table);
				}
				return table;
		}
		Hashtable<String, Double> getRegularHashForSecondPay(){
				Hashtable<String, Double> table = null, table2 = null;				
				if(weekOneHasSplit){
						//
						// we return the first week split two, plus all week2
						table = week2.getRegularHash();												
						table2 = week1.splitTwo.getRegularHash();
						if(!table2.isEmpty())
								mergeTwoHashes(table2, table);
				}
				else{
						//
						// rest of week2
						//
						table = week2.splitTwo.getRegularHash();
				}
				return table;
		}		
		public double getNetRegularHoursForFirstPay(){
				double ret = 0;
				if(weekOneHasSplit){
						double w1one = week1.splitOne.getNetRegular(); // main
						double w1net = week1.getNetRegular(); // check with
						if(w1one <= w1net){
								ret = w1one;
						}
						else{
								ret = w1net;
						}
				}
				else { // add week1 plus part one of week2
						ret = week1.getNetRegular();
						double w2one = week2.splitOne.getNetRegular();
						double w2net = week2.getNetRegular();						
						if(w2one < w2net)
								ret += w2one;
						else
								ret += w2net;
				}
				return ret;
		}
		public double getNetRegularHoursForSecondPay(){
				double ret = 0;
				if(weekOneHasSplit){
						// week1 part two plus week2
						double w1net = week1.getNetRegular();						
						double w1one = week1.splitOne.getNetRegular(); 
						double w1two = week1.splitTwo.getNetRegular(); // main
						if(w1one + w1two <= w1net){
								ret = w1two;
						}
						else if(w1one < w1net){
								ret = w1net - w1one;
						}
						ret += week2.getNetRegular();
			
				}
				else { // the rest of week2
						double w2net = week2.getNetRegular();
						double w2one = week2.splitOne.getNetRegular();						
						double w2two = week2.splitTwo.getNetRegular();
						if(w2one + w2two <= w2net){
								ret = w2two;
						}
						else if(w2one < w2net){
								ret = w2net - w2one;
						}
				}
				return ret;
		}
		public Map<String, Entry> getEntries(){
				
				if(entries == null){
						String name="", val="";
						entries = new TreeMap<>();
						name = "Net "+getRegCode();
						String w1_val = "", w2_val = "";
						double ww_val = 0;
						double week1Total = 0, week2Total = 0, total=0;						
						if(getWeek1Regular() > 0){
								w1_val = df.format(getWeek1NetRegular())+" ("+df.format(getWeek1Regular())+")";
								ww_val = getWeek1NetRegular();
								if(!isHand)
										week1Total = getWeek1NetRegular();
						}
						if(getWeek2Regular() > 0){
								w2_val = df.format(getWeek2NetRegular())+" ("+df.format(getWeek2Regular())+")";
								ww_val += getWeek2NetRegular();
								if(!isHand)
										week2Total = getWeek2NetRegular();
						}
						val = df.format(ww_val);
						total += ww_val;
						//
						// first row (regular hours)
						firstEntry = new Entry(name, w1_val, w2_val, val);
						//
						Hashtable<String, Double> ww_hash = getAll(); // two weeks
						Hashtable<String, Double> w1_hash = getWeek1All();
						Hashtable<String, Double> w2_hash = getWeek2All();
						if(ww_hash != null && !ww_hash.isEmpty()){
								Enumeration<String> keys = ww_hash.keys();
								while(keys.hasMoreElements()){
										String key = keys.nextElement();
										//
										// val is the sum of two weeks
										// need to be checked 
										ww_val = ww_hash.get(key);
										w1_val = "";
										w2_val = "";
										if(w1_hash.containsKey(key) || w2_hash.containsKey(key)){
												if(w1_hash.containsKey(key)){
														if(key.indexOf("ONCALL") == -1)
																week1Total += w1_hash.get(key).doubleValue();
														w1_val = df.format(w1_hash.get(key).doubleValue());
												}
												if(w2_hash.containsKey(key)){
														if(key.indexOf("ONCALL") == -1)										 
																week2Total += w2_hash.get(key).doubleValue();
														w2_val = df.format(w2_hash.get(key).doubleValue());
												}
												Entry entry = new Entry(key, w1_val, w2_val, df.format(ww_val));
												entries.put(key, entry);
										}						
								}
						}
						total = week1Total+week2Total;								
						if(total > 0){
								w1_val="";w2_val="";
								if(week1Total > 0){
										w1_val= df.format(week1Total);
								}
								if(week2Total > 0){
										w2_val= df.format(week2Total);
								}										
								lastEntry = new Entry("Period Total",w1_val,w2_val,df.format(total));
						}
				}
				return entries;
		}
		public boolean hasEntries(){
				getEntries();
				return entries != null && entries.size() > 0;
		}
		public Entry getFirstEntry(){
				if(firstEntry == null)
						getEntries(); // to start the process
				return firstEntry;
		}
		public Entry getLastEntry(){
				if(lastEntry == null)
						getEntries();
				return lastEntry;
		}
		public Document getDocument(){
				return document;
		}
		/**
		 * all prof hrs will get to end pay period
		 */
		double getProfHoursForPayPeriod(){
				double ret = 0f;
				ret = week1.getProfHours();
				ret += week2.getProfHours();
				if(ret < 0.009)
						ret = 0;
				return ret;
		}
		/**
		 * this is needed for HAND department only
		 */
		public Hashtable<CodeRef, String> getTwoWeekHandHash(){
				if(handHash == null){
						handHash = new Hashtable<>();
						Hashtable<String, Double> handReg = get2WeekRegularHash();
						codeRefList = new CodeRefList();
						String back = codeRefList.find();
						if(back.equals("")){
								Set<String> keys = handReg.keySet();
								for(String key:keys){
										double dd = handReg.get(key).doubleValue();
										if(codeRefList.hasKey(key)){
												CodeRef one = codeRefList.getCodeRef(key);
												if(one != null){
														handHash.put(one, df.format(dd));
												}
												else{
														System.err.println("No code ref for "+key);
												}
										}
										else{
												System.err.println("HAND hour code "+key+" not found");
										}
								}
						}
						Hashtable<String, Double> nonReg = getNonRegularHours();
						Set<String> keys = nonReg.keySet();
						for(String key:keys){
								double dd = nonReg.get(key).doubleValue();
								if(codeRefList.hasKey(key)){
										CodeRef one = codeRefList.getCodeRef(key);
										if(one != null){
												handHash.put(one, df.format(dd));
										}
										else{
												System.err.println("No code ref for "+key);
										}
								}
								else{
										System.err.println("HAND hour code "+key+" not found");
								}
						}
				}
				return handHash;
		}
		public boolean hasHandHash(){
				getTwoWeekHandHash();
				return handHash != null && handHash.size() > 0;
		}
		/**
		 * this function returns the status of submission for the given pay
		 * period, either saved, enroute, finalized
		 * Submitted, approved, finalized (payroll process approve)
		 * we call this timeActions 
		 */
		void findStatus(){
				// this is in 
				// document getTimeActions()

		}

}
