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
import org.javatuples.Quartet;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;
import in.bloomington.timer.list.*;


public class UnscheduledReport{

		static Logger logger = LogManager.getLogger(UnscheduledReport.class);
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");		
		static DecimalFormat df = new DecimalFormat("#0.00");
		String date="";
		java.util.Date start_date = null, end_date=null;
		boolean debug = false;
		List<Quartet<String, String, String, String>> unscheduleds = null; 
		double totalHours = 0;
		final static String[] headerTitles = {"Employee","Date","Earn Cdoe","Hours"};
		String errors = "";
    public UnscheduledReport(){
				
    }	
		public List<Quartet<String, String, String, String>> getUnscheduleds(){
				return unscheduleds;
		}
		public void setDate(String val){
				if(val != null)
						date = val;
		}
		public String getDate(){
				if(date.equals(""))
						date = Helper.getToday();
				return date;
		}
		public String getTotalHours(){
				return df.format(totalHours);
		}
		public String[] getHeaderTitles(){
				return headerTitles;
		}
		public boolean hasUnscheduleds(){
				return unscheduleds != null && unscheduleds.size() > 0;
		}
    //
    // setters
    //
		void findStartDates(){
				if(!date.equals("") && date.indexOf("/") > -1){
						try{
								int month=1, day=1,year=2018;
								String strArr[] = date.split("/");
								month = Integer.parseInt(strArr[0]);
								day = Integer.parseInt(strArr[1]);
								year = Integer.parseInt(strArr[2]);
								Calendar cal = new GregorianCalendar();
								cal.set(year, (month-1), day);
								cal.set(Calendar.HOUR_OF_DAY, 0);//to run at 7am of the specified day
								cal.set(Calendar.MINUTE, 0);
								end_date = cal.getTime();
								//
								// one year before
								cal.add(Calendar.YEAR, -1);
								start_date = cal.getTime();
						}
						catch(Exception ex){
								System.err.println(ex);
						}
				}
				else{
						Calendar cal = Calendar.getInstance();
						end_date = cal.getTime();
						//
						// one year before
						cal.add(Calendar.YEAR, -1);
						start_date = cal.getTime();
				}
		}
		public String find(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				findStartDates();
				String msg = "";
				String qq = "select e.last_name,e.first_name,                                    date_format(t.date,'%m/%d/%Y') date, c.name code, sum(t.hours)                  from time_blocks t,time_documents d,hour_codes c,                               employees e                                                                     where t.document_id=d.id and t.inactive is null                                 and c.id=t.hour_code_id and c.inactive is null                                  and (c.name like 'PTOUN' or c.name like 'SBUUN')                                and d.employee_id=e.id  and  t.date >= ?  and t.date <= ?                       group by last_name, first_name, date, code ";
				con = UnoConnect.getConnection();
				if(con == null){
						msg = " Could not connect to DB ";
						logger.error(msg);
						return msg;
				}
				logger.debug(qq);
				try{
						pstmt = con.prepareStatement(qq);
						pstmt.setDate(1, new java.sql.Date(start_date.getTime()));
						pstmt.setDate(2, new java.sql.Date(end_date.getTime()));						
						rs = pstmt.executeQuery();
						while(rs.next()){
								String str = rs.getString(1)+", "+rs.getString(2);
								String str2 = rs.getString(3);
								String str3 = rs.getString(4);
								String str4 = rs.getString(5);
								if(unscheduleds == null){
										unscheduleds = new ArrayList<Quartet<String, String, String, String>>();
								}
								Quartet<String, String, String, String> qrt = Quartet.with(str, str2, str3, str4);
								unscheduleds.add(qrt);
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
