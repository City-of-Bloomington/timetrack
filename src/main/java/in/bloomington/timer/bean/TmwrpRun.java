package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Map;
import java.util.TreeMap;
import java.text.DecimalFormat;
import java.sql.*;
import javax.sql.*;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TmwrpRun{

    static Logger logger = LogManager.getLogger(TmwrpRun.class);
    static final DecimalFormat df = new DecimalFormat("#0.00");		
    static final long serialVersionUID = 1500L;
    String id="", document_id="", run_time="", reg_code_id="";
		double week1_grs_reg_hrs = 0,
				week2_grs_reg_hrs=0,
				week1_net_reg_hrs=0,
				week2_net_reg_hrs=0,
				cycle1_net_reg_hrs=0,
				cycle2_net_reg_hrs=0;				
		double week1_total_hours = 0, week2_total_hours = 0;
		double week1_total_amount = 0, week2_total_amount = 0;		
		boolean old_record = false;
		boolean has_week1_rows = false, has_week2_rows = false,
				has_cycle_row=false;
		//
		// we need this flag when we deal with HAND department
		// we do not use net reg for hand
		boolean hand_flag = false; // 
		//
    //
    Document document = null;
		Department department = null;
		HourCode regCode = null;

		List<TmwrpBlock> blocks = null;
		// for display
    // Map<String, List<String>> week1Rows = null, week2Rows = null;
    List<List<String>> week1Rows = null, week2Rows = null;
		List<String> cycleTotalRow = null; // pay period row 
		Map<CodeRef, Double> csvHourRows = null;
		Map<CodeRef, Double> csvAmountRows = null;		

		// for end of the year
		Map<CodeRef, Double> csvCycle1HourRows = null;
		Map<CodeRef, Double> csvCycle1AmountRows = null;
		Map<CodeRef, Double> csvCycle2HourRows = null;
		Map<CodeRef, Double> csvCycle2AmountRows = null;		
    public TmwrpRun(){
    }		
    public TmwrpRun(String val){
				setId(val);
    }
		// new record or discovery
    public TmwrpRun(String val,
										String val2,
										Double val3,
										Double val4,
										Double val5,
										Double val6,
										Double val7,
										Double val8
										){
				setDocument_id(val);
				setReg_code_id(val2);
				setWeek1GrsRegHrs(val3);
				setWeek2GrsRegHrs(val4);
				setWeek1NetRegHrs(val5);
				setWeek2NetRegHrs(val6);
				setCycle1NetRegHrs(val7);
				setCycle2NetRegHrs(val8);				
    }		
    public TmwrpRun(String val,
										String val2,
										String val3,
										String val4,
										Double val5,
										Double val6,
										Double val7,
										Double val8,
										Double val9,
										Double val10
										){
				setId(val);
				setDocument_id(val2);
				setReg_code_id(val3);				
				setRunTime(val4);
				setWeek1GrsRegHrs(val5);
				setWeek2GrsRegHrs(val6);
				setWeek1NetRegHrs(val7);
				setWeek2NetRegHrs(val8);
				setCycle1NetRegHrs(val9);
				setCycle2NetRegHrs(val10);					
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
    public String getReg_code_id(){
				return reg_code_id;
    }

		public String getRunTime(){
				return run_time;
		}
		public double getWeek1GrsRegHrs(){
				return week1_grs_reg_hrs;
		}
		public double getWeek2GrsRegHrs(){
				return week2_grs_reg_hrs;
		}
		public double getWeek1NetRegHrs(){
				return week1_net_reg_hrs;
		}
		public double getWeek2NetRegHrs(){
				return week2_net_reg_hrs;
		}
		public double getCycle1NetRegHrs(){
				return cycle1_net_reg_hrs;
		}
		public double getCycle2NetRegHrs(){
				return cycle2_net_reg_hrs;
		}				
    //
    // setters
    //
    public void setId(String val){
				if(val != null)
						id = val;
    }
    public void setDocument_id(String val){
				if(val != null)
						document_id = val;
    }
    public void setReg_code_id(String val){
				if(val != null)
						reg_code_id = val;
    }		
    public void setRunTime(String val){
				if(val != null){
						run_time = val;
				}
    }		
    public void setWeek1GrsRegHrs(Double val){
				if(val != null)
						week1_grs_reg_hrs = val;
    }
    public void setWeek2GrsRegHrs(Double val){
				if(val != null)
						week2_grs_reg_hrs = val;
    }				
    public void setWeek1NetRegHrs(Double val){
				if(val != null){
						week1_net_reg_hrs = val;
				}
    }
    public void setWeek2NetRegHrs(Double val){
				if(val != null){
						week2_net_reg_hrs = val;
				}
    }
    public void setCycle1NetRegHrs(Double val){
				if(val != null){
						cycle1_net_reg_hrs = val;
				}
    }
    public void setCycle2NetRegHrs(Double val){
				if(val != null){
						cycle2_net_reg_hrs = val;
				}
    }
		public void setHandFlag(){
				hand_flag = true;
		}
		public Double getCycleNetRegHours(){
				return week1_net_reg_hrs+week2_net_reg_hrs;
		}
		
		public double getWeek1TotalHours(){
				return week1_total_hours;
		}
		public double getWeek2TotalHours(){
				return week2_total_hours;
		}
		public double getCycleTotalHours(){
				return week1_total_hours+week2_total_hours;
		}
		public double getWeek1TotalAmount(){
				return week1_total_amount;
		}
		public double getWeek2TotalAmount(){
				return week2_total_amount;
		}
		public double getCycleTotalAmount(){
				return week1_total_amount+week2_total_amount;
		}		
    public boolean equals(Object o) {
				if (o instanceof TmwrpRun) {
						TmwrpRun c = (TmwrpRun) o;
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
    public String toString(){
				return id;
    }
		public boolean isOldRecord(){
				return old_record;
		}
		
    public Document getDocument(){
				if(document == null && !document_id.equals("")){
						Document one = new Document(document_id);
						String back = one.doSelect();
						if(back.equals("")){
								document = one;
								Employee employee = document.getEmployee();
								if(employee != null){
										Department dp = employee.getDepartment();
										if(dp != null)
												department = dp;
								}								
						}
				}
				return document;
    }
    public HourCode getRegCode(){
				if(regCode == null && !reg_code_id.equals("")){
						HourCode one = new HourCode(reg_code_id);
						String back = one.doSelect();
						if(back.equals("")){
								regCode = one;
						}
						else{
								System.err.println(" reg code "+back);
						}
				}
				return regCode;
    }
		public void setDocument(Document val){
				if(val != null){
						document = val;
						document_id = document.getId();
						Employee employee = document.getEmployee();
						if(employee != null){
								Department dp = employee.getDepartment();
								if(dp != null)
										department = dp;
						}		
				}
		}
		//
		// include totals
		//
		public void findBlocks(){
				if(blocks == null && !id.equals("")){
						TmwrpBlockList tbl = new TmwrpBlockList(id);
						String back = tbl.find();
						if(back.equals("")){
								List<TmwrpBlock> ones = tbl.getBlocks();
								if(ones != null && ones.size() > 0){
										blocks = ones;
								}
						}
						findTotals();						
				}
		}
		public List<TmwrpBlock> getBlocks(){
				return blocks;
		}
		public boolean hasBlocks(){
				return blocks != null && blocks.size() > 0;
		}
		public void findTotals(){
				// for HAND dept we do not include net reg
				if(department == null){
						getDocument();
				}
				if(department != null){
						hand_flag = department.isHand();
				}
				if(!hand_flag){
						week1_total_hours = week1_net_reg_hrs;
						week2_total_hours = week2_net_reg_hrs;
				}
				if(blocks != null && blocks.size() > 0){
						for(TmwrpBlock one:blocks){
								double hrs = one.getHours();
								double amnt = one.getAmount();
								if(hrs + amnt > 0){
										if(one.getHourCode().isRecordMethodMonetary()){
												if(one.getTermType().equals("Week 1")){
														week1_total_amount += amnt;
												}
												else{
														week2_total_amount += amnt;
												}
										}
										else{
												if(one.getTermType().equals("Week 1")){
														week1_total_hours  += hrs;
												}
												else{
														week2_total_hours  += hrs;
												}
										}
								}
						}
				}
		}
		/**
		 * create an array list of list for display
		 * on detail and similar pages
		 */
		public boolean hasWeek1Rows(){
				if(!has_week1_rows)
						findRows();
				return has_week1_rows;
		}
		public boolean hasWeek2Rows(){
				return has_week2_rows;
		}
		public boolean hasCycleTotalRow(){
				return has_cycle_row;
		}
		public List<List<String>> getWeek1Rows(){
				return week1Rows;
		}
		public List<List<String>> getWeek2Rows(){
				return week2Rows;
		}
		// last total row
		public List<String> getCycleTotalRow(){
				return cycleTotalRow;
		}

		
		public void findRows(){
				findBlocks();
				if(hasWeek1Totals() || hasWeek2Totals()){
						week1Rows = new ArrayList<>();
						week2Rows = new ArrayList<>();										
						List<String> row = null;
						getRegCode();
						String key = "";
						if(week1_grs_reg_hrs > 0.){
								if(regCode != null){
										has_week1_rows = true;
										row = new ArrayList<>();
										key = "Gr "+regCode.getCodeInfo();
										row.add(key);
										row.add("("+df.format(week1_grs_reg_hrs)+")");
										row.add("");
										week1Rows.add(row);
										row = new ArrayList<>();
										key = "Net "+regCode.getCodeInfo();
										row.add(key);
										row.add(df.format(week1_net_reg_hrs));
										row.add("");
										week1Rows.add(row);
								}
						}
						if(week2_grs_reg_hrs > 0.){
								if(regCode != null){
										has_week2_rows = true;
										row = new ArrayList<>();
										key = "Gr "+regCode.getCodeInfo();
										row.add(key);
										row.add("("+df.format(week2_grs_reg_hrs)+")");
										row.add("");
										week2Rows.add(row);
										row = new ArrayList<>();
										key = "Net "+regCode.getCodeInfo();
										row.add(key);										
										row.add(df.format(week2_net_reg_hrs));
										row.add("");
										week2Rows.add(row);
								}
						}
						if(hasBlocks()){
								for(TmwrpBlock one:blocks){
										row = new ArrayList<>();
										key = one.getHourCode().getCodeInfo();
										if(one.getHourCode().isRecordMethodMonetary()){
												row.add(key);
												row.add("");
												row.add("$"+df.format(one.getAmount()));
												if(one.getTermType().equals("Week 1")){
														has_week1_rows = true;
														week1Rows.add(row);
												}
												else{
														has_week2_rows = true;
														week2Rows.add(row);
												}
										}
										else{
												row.add(key);
												row.add(df.format(one.getHours()));
												row.add("");
												if(one.getTermType().equals("Week 1")){
														week1Rows.add(row);
														has_week1_rows = true;
												}
												else{
														week2Rows.add(row);
														has_week2_rows = true;
												}										
										}
								}
						}
						if(hasWeek1Totals()){
								row = new ArrayList<>();
								key = "Week Total";
								row.add(key);
								if(week1_total_hours > 0.)
										row.add(df.format(week1_total_hours));
								else
										row.add("");
								if(week1_total_amount > 0.)
										row.add("$"+df.format(week1_total_amount));
								else
										row.add("");
								week1Rows.add(row);
						}
						if(week2_total_hours+week2_total_amount > 0){
								row = new ArrayList<>();
								key = "Week Total";
								row.add(key);
								if(week2_total_hours > 0.)
										row.add(df.format(week2_total_hours));
								else
										row.add("");
								if(week2_total_amount > 0.)
										row.add("$"+df.format(week2_total_amount));
								else
										row.add("");
								week2Rows.add(row);
						}
						cycleTotalRow = new ArrayList<>();
						cycleTotalRow.add("Pay Period Total");
						cycleTotalRow.add(df.format(getCycleTotalHours()));
						double dd = getCycleTotalAmount();
						if(dd > 0.)
								cycleTotalRow.add("$"+df.format(dd));
						else
								cycleTotalRow.add("");
						has_cycle_row = true;
				}
		}
		public boolean hasWeek1Totals(){
				return week1_total_hours + week1_total_amount > 0;
		}
		public boolean hasWeek2Totals(){
				return week2_total_hours + week2_total_amount > 0;
		}
		public boolean hasCsvHourRows(){
				findCsvRows();
				return csvHourRows != null && !csvHourRows.isEmpty();
		}
		
		public boolean hasCsvAmountRows(){
				return csvAmountRows != null && !csvAmountRows.isEmpty();
		}
		public Map<CodeRef, Double> getCsvHourRows(){
				return csvHourRows;
		}
		public Map<CodeRef, Double> getCsvAmountRows(){
				return csvAmountRows;
		}		
		void findCsvRows(){
				findBlocks();
				csvHourRows = new TreeMap<>();
				double dd = 0;
				if(!hand_flag){
						CodeRef codeRef = null;
						dd = getCycleNetRegHours();
						if(dd > 0){
								getRegCode();
								if(regCode != null){
										codeRef = regCode.getCodeRef();
								}
								if(codeRef != null){
										csvHourRows.put(codeRef, dd);
								}
								else{
										logger.error(" codeRef not found ");
										System.err.println(" codeRef not found ");
								}
						}
				}
				if(hasBlocks()){
						for(TmwrpBlock block:blocks){
								CodeRef codeRef = null;
								HourCode hrCode = block.getHourCode();
								if(hrCode != null)
										codeRef = hrCode.getCodeRef();
								dd = 0.;
								if(hrCode.isRecordMethodMonetary()){
										dd = block.getAmount();
										if(dd > 0 && codeRef != null){
												if(csvAmountRows == null){
														csvAmountRows = new TreeMap<>();
												}
												addToMap(csvAmountRows, codeRef, dd);
										}
										else{
												logger.error(" no code ref or dd = 0");
										}
								}
								else{
										dd = block.getHours();
										if(dd > 0 && codeRef != null){
												if(csvHourRows == null){
														csvHourRows = new TreeMap<>();
												}												
												addToMap(csvHourRows, codeRef, dd);
										}
										else{
												logger.error(" no code ref or dd = 0");
										}
								}
						}
				}
		}
		public boolean hasCycle1HourRows(){
				findCycleCsvRows();
				return csvCycle1HourRows != null && !csvCycle1HourRows.isEmpty();
		}
		
		public boolean hasCycle2HourRows(){
				return csvCycle2HourRows != null && !csvCycle2HourRows.isEmpty();
		}
		public boolean hasCycle1AmountRows(){
				return csvCycle1AmountRows != null && !csvCycle1AmountRows.isEmpty();
		}
		public boolean hasCycle2AmountRows(){
				return csvCycle2AmountRows != null && !csvCycle2AmountRows.isEmpty();
		}
		public Map<CodeRef, Double> getCycle1HourRows(){
				return csvCycle1HourRows;
		}
		public Map<CodeRef, Double> getCycle2HourRows(){
				return csvCycle2HourRows;
		}		
		public Map<CodeRef, Double> getCycle1AmountRows(){
				return csvCycle1AmountRows;
		}
		public Map<CodeRef, Double> getCycle2AmountRows(){
				return csvCycle2AmountRows;
		}		
		void findCycleCsvRows(){
				findBlocks();
				csvCycle1HourRows = new TreeMap<>();
				csvCycle2HourRows = new TreeMap<>();
				csvCycle1AmountRows = new TreeMap<>();
				csvCycle2AmountRows = new TreeMap<>();				
				double dd = 0;
				if(!hand_flag){
						CodeRef codeRef = null;
						dd = cycle1_net_reg_hrs;
						if(dd > 0){
								getRegCode();
								if(regCode != null){
										codeRef = regCode.getCodeRef();
								}
								if(codeRef != null){
										csvCycle1HourRows.put(codeRef, dd);
								}
								else{
										logger.error(" codeRef not found ");
										System.err.println(" codeRef not found ");
								}
						}
						dd = cycle2_net_reg_hrs;
						if(dd > 0){
								getRegCode();
								if(regCode != null){
										codeRef = regCode.getCodeRef();
								}
								if(codeRef != null){
										csvCycle2HourRows.put(codeRef, dd);
								}
								else{
										logger.error(" codeRef not found ");
										System.err.println(" codeRef not found ");
								}
						}						
				}
				if(hasBlocks()){
						for(TmwrpBlock block:blocks){
								CodeRef codeRef = null;
								HourCode hrCode = block.getHourCode();
								if(hrCode != null)
										codeRef = hrCode.getCodeRef();
								dd = 0.;
								if(hrCode.isRecordMethodMonetary()){
										dd = block.getAmount();
										if(dd > 0 && codeRef != null){
												if(block.getCycleOrder() == 1)
														addToMap(csvCycle1AmountRows, codeRef, dd);
												else
														addToMap(csvCycle2AmountRows, codeRef, dd);
										}
										else{
												logger.error(" no code ref or dd = 0");
										}
								}
								else{
										dd = block.getHours();
										if(dd > 0 && codeRef != null){
												if(block.getCycleOrder() == 1)
														addToMap(csvCycle1HourRows, codeRef, dd);
												else
														addToMap(csvCycle2HourRows, codeRef, dd);				
										}
										else{
												logger.error(" no code ref or dd = 0");
										}
								}
						}
				}
		}
		
		private void addToMap(Map<CodeRef, Double> map,
													CodeRef ref,
													Double dd){
				double dd2 = 0;
				if(map.containsKey(ref)){
						dd2 = map.get(ref);
				}
				dd2 = dd+dd2;
				map.put(ref, dd2);
		}

		/**
		 * since document_id is unique per record
		 * we can use this function to get it (if any)
		 */
		public String doFind(String doc_id){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "select g.id,"+
						"g.document_id,"+
						"g.reg_code_id,"+
						"date_format(g.run_time,'%m/%d/%y %H:%i'),"+
						"g.week1_grs_reg_hrs, "+
						"g.week2_grs_reg_hrs, "+
						"g.week1_net_reg_hrs, "+
						"g.week2_net_reg_hrs, "+
						"g.cycle1_net_reg_hrs, "+
						"g.cycle2_net_reg_hrs "+						
						"from tmwrp_runs g where g.document_id = ? ";
				logger.debug(qq);
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Unable to connect to DB ";
						return msg;
				}
				try{
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, doc_id);
						rs = pstmt.executeQuery();
						if(rs.next()){
								setId(rs.getString(1));
								setDocument_id(rs.getString(2));
								setReg_code_id(rs.getString(3));								
								setRunTime(rs.getString(4));
								setWeek1GrsRegHrs(rs.getDouble(5));
								setWeek2GrsRegHrs(rs.getDouble(6));
								setWeek1NetRegHrs(rs.getDouble(7));
								setWeek2NetRegHrs(rs.getDouble(8));
								setCycle1NetRegHrs(rs.getDouble(9));
								setCycle2NetRegHrs(rs.getDouble(10));								
						}
						else{
								msg = "No record found for "+doc_id;
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
		
    public String doSelect(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "select g.id,"+
						"g.document_id,"+
						"g.reg_code_id,"+
						"date_format(g.run_time,'%m/%d/%y %H:%i'),"+
						"g.week1_grs_reg_hrs, "+
						"g.week2_grs_reg_hrs, "+
						"g.week1_net_reg_hrs, "+
						"g.week2_net_reg_hrs, "+
						"g.cycle1_net_reg_hrs, "+
						"g.cycle2_net_reg_hrs "+						
						"from tmwrp_runs g where g.id =? ";
				logger.debug(qq);
				con = UnoConnect.getConnection();				
				try{
						if(con != null){
								pstmt = con.prepareStatement(qq);
								pstmt.setString(1, id);
								rs = pstmt.executeQuery();
								if(rs.next()){
										setDocument_id(rs.getString(2));
										setReg_code_id(rs.getString(3));
										setRunTime(rs.getString(4));
										setWeek1GrsRegHrs(rs.getDouble(5));
										setWeek2GrsRegHrs(rs.getDouble(6));
										setWeek1NetRegHrs(rs.getDouble(7));
										setWeek2NetRegHrs(rs.getDouble(8));
										setCycle1NetRegHrs(rs.getDouble(9));
										setCycle2NetRegHrs(rs.getDouble(10));										
								}
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
		public String doSaveOrUpdate(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "select id from tmwrp_runs where document_id=? ";
				if(document_id.equals("")){
						msg = " document not set ";
						return msg;
				}
				logger.debug(qq);
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could not connect to DB ";
						return msg;
				}				
				try{
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, document_id);						
						rs = pstmt.executeQuery();
						if(rs.next()){
								id = rs.getString(1);
								old_record = true;
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
						if(id.equals("")){
								return doSave();
						}
						else{
								return doUpdate();
						}
				}
				return msg;
		}
		/**
		 * remove old blocks so we can add the new ones
		 */
		public String doCleanUp(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "delete from tmwrp_blocks where run_id=? ";
				if(id.equals("")){
						msg = " run id not set ";
						return msg;
				}
				logger.debug(qq);
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could not connect to DB ";
						return msg;
				}				
				try{
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, id);						
						pstmt.executeUpdate();
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
    public String doSave(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "insert into tmwrp_runs values(0,?,?,now(),?,?,?,?,?,?) ";
				if(document_id.equals("")){
						msg = " document not set ";
						return msg;
				}
				if(reg_code_id.equals("")){
						msg = " regular hour code not set ";
						return msg;
				}				
				logger.debug(qq);
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could not connect to DB ";
						return msg;
				}				
				try{
						pstmt = con.prepareStatement(qq);
						
						pstmt.setString(1, document_id);						
						pstmt.setString(2, reg_code_id);
						pstmt.setDouble(3,week1_grs_reg_hrs);
						pstmt.setDouble(4,week2_grs_reg_hrs);
						pstmt.setDouble(5,week1_net_reg_hrs);
						pstmt.setDouble(6,week2_net_reg_hrs);
						pstmt.setDouble(7,cycle1_net_reg_hrs);
						pstmt.setDouble(8,cycle2_net_reg_hrs);						
						pstmt.executeUpdate();
						Helper.databaseDisconnect(pstmt, rs);
						//
						qq = "select LAST_INSERT_ID()";
						pstmt = con.prepareStatement(qq);
						rs = pstmt.executeQuery();
						if(rs.next()){
								id = rs.getString(1);
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
    public String doUpdate(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "update tmwrp_runs set "+
						"run_time=now(),"+
						"reg_code_id=?,"+
						"week1_grs_reg_hrs=?,"+
						"week2_grs_reg_hrs=?,"+
						"week1_net_reg_hrs=?,"+
						"week2_net_reg_hrs=?, "+
						"cycle1_net_reg_hrs=?,"+
						"cycle2_net_reg_hrs=? "+						
						"where id=? ";
				if(reg_code_id.equals("")){
						msg = " regular hour code not set ";
						return msg;
				}				
				logger.debug(qq);
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could not connect to DB ";
						return msg;
				}				
				try{
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, reg_code_id);
						pstmt.setDouble(2,week1_grs_reg_hrs);
						pstmt.setDouble(3,week2_grs_reg_hrs);
						pstmt.setDouble(4,week1_net_reg_hrs);
						pstmt.setDouble(5,week2_net_reg_hrs);
						pstmt.setDouble(6,cycle1_net_reg_hrs);
						pstmt.setDouble(7,cycle2_net_reg_hrs);						
						pstmt.setString(8, id);						
						pstmt.executeUpdate();
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
		/**
		 * when delete, we also delete related blocks
		 */
    public String doDelete(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "delete tmwrp_blocks where run_id=? ";
				String qq2 = "delete tmwrp_runs where id=? ";				
				logger.debug(qq);
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could not connect to DB ";
						return msg;
				}							
				try{
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, id);
						pstmt.executeUpdate();
						Helper.databaseDisconnect(pstmt, rs);
						qq = qq2;
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, id);
						pstmt.executeUpdate();						
						
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

}
