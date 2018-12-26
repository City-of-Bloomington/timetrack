package in.bloomington.timer.leave;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Hashtable;
import java.util.TreeMap;
import java.util.Map;
import java.util.Set;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;
import java.util.Date;
import java.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;

public class LeaveBlockList{

		boolean debug = false;
		static final long serialVersionUID = 4200L;
		static Logger logger = LogManager.getLogger(LeaveBlockList.class);
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		DecimalFormat dfn = new DecimalFormat("##0.00");		
		String employee_id = "", pay_period_id="", job_id="";
		String date_from="", date_to="", document_id="", department_id="";
		String date = "";// specific day
		String code = "", code2 = ""; 
		boolean active_only = false, for_today = false, dailyOnly=false,
				clockInOnly = false, hasClockInAndOut = false;
		List<LeaveBlock> leaveBlocks = null;
		//
		// for 14 days pay period
		//
		Map<Integer, List<LeaveBlock>> dailyBlocks = new TreeMap<>();
		Map<Integer, Double> hourCodeTotals = new TreeMap<>();
		Map<Integer, Double> usedAccrualTotals = new TreeMap<>();
		Map<String, Map<Integer, Double>> daily = new TreeMap<>();		
		LeaveDocument document = null;
		List<String> jobNames = null;
    public LeaveBlockList(){
    }
    public LeaveBlockList(String val){
				setEmployee_id(val);
    }
    public void setDocument_id (String val){
				if(val != null)
						document_id = val;
    }		
    public void setEmployee_id (String val){
				if(val != null)
						employee_id = val;
    }
    public void setJob_id (String val){
				if(val != null)
						job_id = val;
    }
    public void setDepartment_id(String val){
				if(val != null)
						department_id = val;
    }		
    public void setPay_period_id (String val){
				if(val != null)
						pay_period_id = val;
    }
    public void setDate_from (String val){
				if(val != null)
						date_from = val;
    }
    public void setDate_to (String val){
				if(val != null)
						date_to = val;
    }
    public void setDate(String val){
				if(val != null)
						date = val;
    }
		public String getEmployee_id(){
				return employee_id;
		}
		public String getPay_period_id(){
				return pay_period_id;
		}
		public String getDate_from(){
				return date_from;
		}
		public String getDate_to(){
				return date_to;
		}
		public String getDate(){
				return date;
		}		
		public void setActiveOnly(){
				active_only = true;
		}
		public void setForToday(){
				for_today = true;
		}
		public void setDailyOnly(){
				dailyOnly = true;
		}
		public List<LeaveBlock> getLeaveBlocks(){
				return leaveBlocks;
		}
		public Map<Integer, List<LeaveBlock>> getDailyBlocks(){
				return dailyBlocks;
		}
		public Map<Integer, Double> getHourCodeTotals(){
				return hourCodeTotals;
		}
		public Map<Integer, Double> getUsedAccrualTotals(){
				return usedAccrualTotals;
		}		
		public Map<String, Map<Integer, String>> getDaily(){
				Set<String> set = daily.keySet();
				Map<String, Map<Integer, String>> mapd = new TreeMap<>();
				for(String str:set){
						Map<Integer, String> map2 = new TreeMap<>();
						
						Map<Integer, Double> map = daily.get(str);
						for(int j=0;j<16;j++){ // 8 total week1, 15 total week2
								double val = map.get(j);
								map2.put(j, dfn.format(val));
						}
						mapd.put(str, map2);
				}
				return mapd;
		}				
		public LeaveDocument getDocument(){
				if(document == null && !document_id.equals("")){
						LeaveDocument one = new LeaveDocument(document_id);
						String back = one.doSelect();
						if(back.equals("")){
								document = one;
						}
				}
				return document;
		}
		// find employee jobs in this pay period
		//
		// normally one job only per document
		//
		private List<String> findJobNames(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				List<String> jobNames = new ArrayList<>();
				String qq = "select "+
						" distinct ps.name "+ // job name (position)
						" from positions ps join jobs j on ps.id=j.position_id, "+
						" time_documents d,"+
						" pay_periods p ";
				String qw = "d.id=? and d.job_id=j.id and p.id=d.pay_period_id and ps.inactive is null and j.inactive is null and (j.expire_date is null or "+
						" j.expire_date <= p.end_date) and j.effective_date <= p.start_date ";
				qq = qq +" where "+qw;
				con = UnoConnect.getConnection();
				if(con == null){
						logger.error(msg);
						return null;
				}
				logger.debug(qq);
				try{
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, document_id);
						rs = pstmt.executeQuery();
						while(rs.next()){
								str = rs.getString(1);
								if(!jobNames.contains(str)){
										jobNames.add(str);
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
				return jobNames;
		}
    //
    // getters
    //
		public String find(){

				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "select t.id,"+
						" t.document_id,"+
						" t.hour_code_id,"+
						" date_format(t.date,'%m/%d/%Y'),"+
						" t.hours,"+
						
						" date_format(t.request_date,'%m/%d/%Y'),"+
						" t.request_approval,"+
						" t.action_status,"+
						" t.action_by,"+
						" date_format(t.action_date,'%m/%d/%Y'),"+						
						
						" t.inactive,"+
						" datediff(t.date,p.start_date), "+ // order id start at 0
						" c.name,"+
						" c.description,"+
						" ps.name "+ // job name
						
						" from leave_blocks t "+
						" join leave_documents d on d.id=t.document_id "+
						" join pay_periods p on p.id=d.pay_period_id "+
						" join jobs j on d.job_id=j.id "+
						" join positions ps on j.position_id=ps.id "+
						" join hour_codes c on t.hour_code_id=c.id ";

				String qw = "";
				if(!department_id.equals("")){
						qq += ", department_employees de ";
						if(!qw.equals("")) qw += " and ";								
						qw += "  de.employee_id=d.employee_id and de.department_id=? ";
				}
				if(!pay_period_id.equals("")){
						if(!qw.equals("")) qw += " and ";						
						qw += "d.pay_period_id=? ";
				}
				if(!document_id.equals("")){
						if(!qw.equals("")) qw += " and ";						
						qw += "t.document_id=? ";
				}				
				if(!employee_id.equals("")){
						if(!qw.equals("")) qw += " and ";
						qw += "d.employee_id=? ";
				}
				if(!job_id.equals("")){
						if(!qw.equals("")) qw += " and ";
						qw += "d.job_id=? ";
				}				
				if(!date_from.equals("")){
						if(!qw.equals("")) qw += " and ";
						qw += "t.date >= ? ";
				}
				if(!date_to.equals("")){
						if(!qw.equals("")) qw += " and ";
						qw += "t.date <= ? ";
				}
				if(!date.equals("")){
						if(!qw.equals("")) qw += " and ";
						qw += "t.date = ? ";
				}
				if(active_only){
						if(!qw.equals("")) qw += " and ";
						qw += " t.inactive is null ";
				}
				if(!qw.equals("")){
						qq += " where "+qw;
				}
				qq += " order by t.date ";
				con = UnoConnect.getConnection();
				if(con == null){
						msg = " Could not connect to DB ";
						logger.error(msg);
						return msg;
				}
				logger.debug(qq);
				try{
						pstmt = con.prepareStatement(qq);
						int jj=1;
						if(!department_id.equals("")){
								pstmt.setString(jj++, department_id);
						}										
						if(!pay_period_id.equals("")){
								pstmt.setString(jj++, pay_period_id);
						}
						if(!document_id.equals("")){
								pstmt.setString(jj++, document_id);
						}						
						if(!employee_id.equals("")){
								pstmt.setString(jj++, employee_id);
						}
						if(!job_id.equals("")){
								pstmt.setString(jj++, job_id);
						}						
						if(!date_from.equals("")){
								java.util.Date date_tmp = df.parse(date_from);
								pstmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
						}
						if(!date_to.equals("")){
								java.util.Date date_tmp = df.parse(date_to);
								pstmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
						}
						if(!date.equals("")){
								java.util.Date date_tmp = df.parse(date);
								pstmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
						}
						rs = pstmt.executeQuery();
						while(rs.next()){
								if(leaveBlocks == null)
										leaveBlocks = new ArrayList<>();
								LeaveBlock one =
										new LeaveBlock(rs.getString(1),
																	 rs.getString(2),
																	 rs.getString(3),
																	 rs.getString(4),
																	 rs.getDouble(5),
																	 
																	 rs.getString(6),
																	 rs.getString(7) != null,
																	 rs.getString(8),
																	 rs.getString(9),
																	 rs.getString(10),
																	 
																	 rs.getString(11) != null,
																	 rs.getInt(12),
																	 rs.getString(13),
																	 rs.getString(14),
																	 rs.getString(15)
																	 );
								if(!leaveBlocks.contains(one)){
										leaveBlocks.add(one);
										addToDailyBlocks(one);
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
		void addToDailyBlocks(LeaveBlock one){
				if(one != null){
						if(dailyBlocks.containsKey(one.getOrderIndex())){
								List<LeaveBlock> ll = dailyBlocks.get(one.getOrderIndex());
								ll.add(one);
								dailyBlocks.put(one.getOrderIndex(), ll);
						}
						else{
								List<LeaveBlock> ll = new ArrayList<>();
								ll.add(one);
								dailyBlocks.put(one.getOrderIndex(), ll);
						}
				}
		}
		/**
			 select t.hour_code_id, sum(t.hours)                                             from time_blocks t,time_documents d                                             where t.document_id=d.id and t.inactive is null and                             t.date >= '2018-08-27' and d.employee_id=10                                      and t.date <= (select end_date from pay_periods p,time_documents d              where p.id=d.pay_period_id and d.id=310) and t.hour_code_id in                   (select id from hour_codes where accrual_id is not null)                        group by t.hour_code_id;
			 
 
		 */
		public String findUsedAccruals(){

				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", last_date="", // last accrual date
						emp_id = "", end_date=""; // payPeriod end_date
				//
				// we assume all employee accruals are updated the day before
				// any payperiod therefore we are concerned by the current
				// pay period end date
				//
				// we find the last accrual carry over date and employee id
				// given document id and the end date of this pay period
				//
				String qq = " ";
				qq = " select a.date,a.employee_id,p.end_date from employee_accruals a,                pay_periods p, time_documents d                                                 where d.employee_id=a.employee_id                                               and p.id = d.pay_period_id and a.date < p.start_date                            and d.id = ? order by a.date desc limit 1 ";
				//
				// find total accrual hours used  (PTO and other related
				// hour codes) since last accrual carry over date
				//
				String qq2 = " select c.accrual_id, sum(t.hours)                                     from time_blocks t,time_documents d,hour_codes c                                 where t.document_id=d.id and t.inactive is null                                 and c.id=t.hour_code_id and c.inactive is null                                  and c.accrual_id is not null                                                    and t.date >= ? and d.employee_id=?                                             and t.date <= ? group by c.accrual_id";
				
				//
				// this addition will cause to add all the accrual used since
				// the last update, this could mean two pay period hours
				//

				con = UnoConnect.getConnection();
				if(con == null){
						msg = " Could not connect to DB ";
						logger.error(msg);
						return msg;
				}
				logger.debug(qq);
				try{
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, document_id);
						rs = pstmt.executeQuery();
						if(rs.next()){
								last_date = rs.getString(1); // accrual date
								emp_id = rs.getString(2);
								end_date = rs.getString(3); // end of pay period
						}
						qq = qq2;
						logger.debug(qq);
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, last_date);
						pstmt.setString(2, emp_id);
						pstmt.setString(3, end_date);						
						rs = pstmt.executeQuery();
						while(rs.next()){
								int code_id = rs.getInt(1); // accrual_id now
								double hrs = rs.getDouble(2);
								usedAccrualTotals.put(code_id, hrs);
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
		
}
