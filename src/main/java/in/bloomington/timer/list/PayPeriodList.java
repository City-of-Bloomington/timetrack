package in.bloomington.timer.list;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Hashtable;
import java.text.*;
import java.util.Date;
import java.sql.*;
import javax.naming.*;
import javax.naming.directory.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;

public class PayPeriodList{

    static final long serialVersionUID = 3000L;
    static Logger logger = LogManager.getLogger(PayPeriodList.class);
    String date = "", year="", id="", employee_id="", limit="";
    String next_to_id = "", previous_to_id="", order_by= " id desc ";
    boolean currentOnly = false,
				twoPeriodsAheadOnly=false,
				onePeriodAheadOnly=false,
				lastPayPeriod=false, previousOnly=false, nextOnly=false;
    boolean avoidFuturePeriods = false;
    boolean approveSuitable = false;
    List<PayPeriod> periods = null;
		List<Integer> years = null;
    public PayPeriodList(){
    }
    public PayPeriodList(String val){
				setYear(val);
    }		
    public void setDate(String val){
				if(val != null)
						date = val;
    }
    public void setYear(String val){
				if(val != null)
						year = val;
    }
    public void setEmployee_id(String val){
				if(val != null)
						employee_id = val;
    }		
    public List<PayPeriod> getPeriods(){
				return periods;
    }
    public void currentOnly(){
				currentOnly = true;
    }
    public void setNextOnly(){
				nextOnly = true;
    }
    public void setPreviousOnly(){
				previousOnly = true;
    }
    public void setNextTo(String val){
				if(val != null && !val.isEmpty())				
						next_to_id = val;
    }
    public void setPreviousTo(String val){
				if(val != null && !val.isEmpty())
						previous_to_id = val;
    }		
    public void setTwoPeriodsAheadOnly(){
				twoPeriodsAheadOnly = true;
    }
    public void setOnePeriodAheadOnly(){
				onePeriodAheadOnly = true;
    }		
    public void setLastPayPeriod(){
				lastPayPeriod = true;
    }
    public void avoidFuturePeriods(){
				avoidFuturePeriods = true;
    }
    public void setApproveSuitable(){
				approveSuitable = true;
    }
    public void setLimit(String val){
				if(val != null)
						limit = val;
    }
		public List<Integer> getYears(){
				return years;
		}
		public void setOrderBy(String val){
				if(val != null)
						order_by = val;
		}
    //
    // find
    //
    public String find(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "select p.id,"+
						"date_format(p.start_date,'%m/%d/%Y'), "+
						"date_format(p.end_date,'%m/%d/%Y'), "+
						"year(p.start_date),month(p.start_date),day(p.start_date),"+
						"year(p.end_date),month(p.end_date),day(p.end_date),"+
						"datediff(p.end_date,p.start_date),p.start_date,p.end_date "+
						"from pay_periods p ";
				String qq2 = "select p2.id,"+
						"date_format(p2.start_date,'%m/%d/%Y'), "+
						"date_format(p2.end_date,'%m/%d/%Y'), "+
						"year(p2.start_date),month(p2.start_date),day(p2.start_date),"+
						"year(p2.end_date),month(p2.end_date),day(p2.end_date),"+
						"datediff(p2.end_date,p2.start_date), "+
						"p2.start_date,p2.end_date "+
						"from pay_periods p2 ";
				String qw = "";
				String qo = " order by "+order_by;
				if(currentOnly){
						qw = " p.start_date <= curdate() and p.end_date >= curdate() ";
				}
				else if(nextOnly){
						qw = " p.start_date <= date_add(curdate(), interval 14 day) and p.end_date >= date_add(curdate(), interval 14 day) ";
				}
				else if(previousOnly){
						qw = " p.start_date <= date_sub(curdate(), interval 14 day) and p.end_date >= date_sub(curdate(), interval 14 day) ";
				}
				else if(twoPeriodsAheadOnly){
						qw = " p.start_date <= date_add(curdate(), interval 28 day) ";
						if(!employee_id.isEmpty()){
								qq2 += ", time_documents d  "+
										" where d.pay_period_id=p2.id and d.employee_id=? "+
										" and p2.start_date > date_sub(curdate(), interval 90 day) ";
						}
				}
				else if(onePeriodAheadOnly){
						qw = " p.start_date <= date_add(curdate(), interval 14 day) ";
						if(!employee_id.isEmpty()){
								qq2 += ", time_documents d  "+
										" where d.pay_period_id=p2.id and d.employee_id=? "+
										" and p2.start_date > date_sub(curdate(), interval 90 day) ";
						}
				}				
				else if(avoidFuturePeriods){
						qw = " p.start_date <= curdate() ";
				}
				else if(lastPayPeriod){
						qw = " p.end_date < curdate() ";
						qo += " limit 1 ";
				}
				else if(approveSuitable){
						// current payperiod if less than 6 days
						qw = "((datediff(curdate(), p.start_date) > 6 and datediff(p.end_date, curdate()) > -1) ";
						// or previous if more than 6 days
						qw += " or ((p.start_date <= date_sub(curdate(), interval 14 day)) and (p.end_date >= date_sub(curdate(), interval 14 day)))) ";						
						qo += " limit 1 ";
				}				
				else if(!year.isEmpty()){
						qw = " (year(p.start_date) = ? or year(p.end_date) = ?) ";
				}
				else if(!previous_to_id.isEmpty()){
						qw = " p.id < ? ";
						qo += " limit 1 ";
				}
				else if(!next_to_id.isEmpty()){
						qw = " p.id > ? ";
						qo = " order by id asc limit 1 ";
				}				
				if(!qw.isEmpty()){
						qq += " where "+qw;
				}
				if(twoPeriodsAheadOnly && !employee_id.isEmpty()){
						qq += " union "+qq2;
						
				}
				if(!limit.isEmpty() && qo.indexOf("limit") == -1){
						qo += " limit "+limit;
				}
				qq += qo;
				logger.debug(qq);
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
						if(!currentOnly && !year.isEmpty()){
								pstmt.setString(jj++, year);
								pstmt.setString(jj++, year);								
						}
						if(twoPeriodsAheadOnly && !employee_id.isEmpty()){
								pstmt.setString(jj++, employee_id);
						}
						else if(!previous_to_id.isEmpty()){
								pstmt.setString(jj++, previous_to_id);								
						}
						else if(!next_to_id.isEmpty()){
								pstmt.setString(jj++, next_to_id);			
						}								
						rs = pstmt.executeQuery();
						while(rs.next()){
								if(periods == null)
										periods = new ArrayList<>();
								PayPeriod one = new PayPeriod(
																							rs.getString(1),
																							rs.getString(2),
																							rs.getString(3),
																							rs.getInt(4),
																							rs.getInt(5),
																							rs.getInt(6),
																							rs.getInt(7),
																							rs.getInt(8),
																							rs.getInt(9),
																							rs.getInt(10),
																							rs.getString(11),
																							rs.getString(12));
								periods.add(one);
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
		public String findYearList(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "select "+
						"distinct year(start_date) year "+
						"from pay_periods order by year ";
				logger.debug(qq);
				con = UnoConnect.getConnection();
				if(con == null){
						msg = " Could not connect to DB ";
						logger.error(msg);
						return msg;
				}
				logger.debug(qq);
				try{
						pstmt = con.prepareStatement(qq);
						rs = pstmt.executeQuery();
						while(rs.next()){
								int year = rs.getInt(1);
								if(years == null) years = new ArrayList<>();
								if(year < CommonInc.reportStartYear) continue;
								years.add(year);
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
