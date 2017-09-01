package in.bloomington.timer.bean;
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
import org.apache.log4j.Logger;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;
//
public class PayPeriod{

		static final long serialVersionUID = 2900L;
		static Logger logger = Logger.getLogger(PayPeriod.class);
		String id="", start_date="", end_date="", date="";
		int startYear =0,startMonth=0,startDay=0;
		int endYear =0,endMonth=0,endDay=0;
		public static final String[] allMonths =
		{"January","February","March","April","May","June",
		 "July","August","September","October","November","December"};
		public static final String[] allMonthsShort =
		{"Jan","Feb","Mar","Apr","May","Jun",
		 "Jul","Aug","Sep","Oct","Nov","Dec"};		
		static SimpleDateFormat dateFormat = Helper.dateFormat;

    public PayPeriod(){
    }
    public PayPeriod(String val){
				setId(val);
    }		
    public PayPeriod(int day, int month, int year){
				setDate(day, month, year);
    }
		public PayPeriod(String val, String val2, String val3){
				setId(val);
				setStart_date(val2);
				setEnd_date(val3);
		}
		public PayPeriod(String val, String val2, String val3,
										 int val4, int val5, int val6,
										 int val7, int val8, int val9){
				setVals(val, val2, val3, val4, val5, val6, val7, val8, val9);
		}
		void setVals(String val, String val2, String val3,
								 int val4, int val5, int val6,
								 int val7, int val8, int val9){
				setId(val);
				setStart_date(val2);
				setEnd_date(val3);
				setStartYear(val4);
				setStartMonth(val5);
				setStartDay(val6);
				setEndYear(val7);
				setEndMonth(val8);
				setEndDay(val9);				
		}
				//
    // getters
    //
    public String getDate(){
				return date;
    }
    public String getId(){
				return id;
    }		
    public String getStart_date(){
				return start_date;
    }
    public String getEnd_date(){
				return end_date;
    }
		public int[] getStartDateInt(){
				int[] ret = {startMonth,startDay,startYear};
				return ret;
		}
		public String getStartDateText(){
				return allMonths[startMonth-1]+" "+startDay+" "+startYear;
		}
		public String getEndDateText(){
				return allMonths[endMonth-1]+" "+endDay+" "+endYear;
		}	
		public int[] getEndDateInt(){
				int[] ret = {endMonth,endDay,endYear};
				return ret;
		}	
    //
    // setters
    //
    public void setId (String val){
				if(val != null)
						id = val;
    }		
    public void setDate (String val){
				if(val != null)
						date = val;
    }
    public void setDate (int day, int month, int year){
				date = month+"/"+day+"/"+year;
    }	
    public void setStart_date (String val){
				if(val != null)
						start_date = val;
    }
    public void setEnd_date (String val){
				if(val != null)
						end_date = val;
    }
    public void setStartYear (int val){
				startYear = val;
    }
    public void setStartMonth(int val){
				startMonth = val;
    }
    public void setStartDay(int val){
				startDay = val;
    }
    public void setEndYear (int val){
				endYear = val;
    }
    public void setEndMonth(int val){
				endMonth = val;
    }
    public void setEndDay(int val){
				endDay = val;
    }
		public int getStartYear(){
				return startYear;
		}
		public String toString(){
				return getDateRange();
		}
		public String getStartMonthName(){
				return allMonths[startMonth];
		}
		public String getMonthNames(){
				if(startMonth == endMonth){
						return allMonths[startMonth-1];
				}
				return allMonths[startMonth-1]+"/"+allMonths[endMonth-1];
		}
		public String getDateRange(){
				return start_date+" - "+end_date;
		}
		public String find(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "select p.id,"+
						"date_format(p.start_date,'%m/%d/%Y'), "+
						"date_format(p.end_date,'%m/%d/%Y'), "+
						"year(p.start_date),month(p.start_date),day(p.start_date),"+
						"year(p.end_date),month(p.end_date),day(p.end_date) "+
						"from pay_periods p where ";
				if(!id.equals("")){
						qq += " p.id=? ";
				}
				else{
						qq += " ? between p.start_date and p.end_date ";
				}
				con = Helper.getConnection();
				if(con == null){
						msg = " could not connect to Database ";
						logger.error(msg);
						return msg;
				}
				logger.debug(qq);
				try{
						pstmt = con.prepareStatement(qq);
						if(!id.equals("")){
								pstmt.setString(1, id);
						}
						else{
								pstmt.setDate(1, new java.sql.Date(dateFormat.parse(date).getTime()));
						}
						rs = pstmt.executeQuery();
						if(rs.next()){
								setId(rs.getString(1));
								setStart_date(rs.getString(2));
								setEnd_date(rs.getString(3));
								setStartYear(rs.getInt(4));
								setStartMonth(rs.getInt(5));
								setStartDay(rs.getInt(6));
								setEndYear(rs.getInt(7));
								setEndMonth(rs.getInt(8));
								setEndDay(rs.getInt(9));				
						}
						else{
								msg = "No pay period found";
						}
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(con, pstmt, rs);
				}
				return msg;
		}
		public String doSelect(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "select p.id,"+
						"date_format(p.start_date,'%m/%d/%Y'), "+
						"date_format(p.end_date,'%m/%d/%Y'), "+
						"year(p.start_date),month(p.start_date),day(p.start_date),"+
						"year(p.end_date),month(p.end_date),day(p.end_date) "+
						"from pay_periods p where id=?";
				if(id.equals("")){
						msg = " id not set ";
						logger.error(msg);
						return msg;
				}					
				con = Helper.getConnection();
				if(con == null){
						msg = " could not connect to Database ";
						logger.error(msg);
						return msg;
				}
				logger.debug(qq);
				try{
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, id);
						rs = pstmt.executeQuery();
						if(rs.next()){
								setId(rs.getString(1));
								setStart_date(rs.getString(2));
								setEnd_date(rs.getString(3));
								setStartYear(rs.getInt(4));
								setStartMonth(rs.getInt(5));
								setStartDay(rs.getInt(6));
								setEndYear(rs.getInt(7));
								setEndMonth(rs.getInt(8));
								setEndDay(rs.getInt(9));				
						}
						else{
								msg = "No pay period found";
						}
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(con, pstmt, rs);
				}
				return msg;
		}

}
