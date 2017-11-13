package in.bloomington.timer.list;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */

import java.util.*;
import java.sql.*;
import java.io.*;
import java.text.*;
import java.util.ArrayList;
import java.util.List;
import in.bloomington.timer.util.Helper;
import in.bloomington.timer.bean.Holiday;
import org.apache.log4j.Logger;

public class HolidayList{

    boolean debug;
    String year = "", date_from="", date_to="";
		static final long serialVersionUID = 54L;
		static Logger logger = Logger.getLogger(HolidayList.class);
		static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		List<Holiday> holidays = null;
		//
		// this contains only dates
		Set<String> allSet = new HashSet<String>();
    //
    // basic constructor
    public HolidayList(boolean deb){

		debug = deb;
		//
		// initialize
		//
    }
    public HolidayList(String val){
		//
		// initialize
		//
		setYear(val);
    }
    public HolidayList(String val, String val2){
		//
		// initialize
		//
		setDate_from(val);
		setDate_to(val2);		
    }	
    //
	public void setYear(String val){
		if(val != null)
			year = val;
	}
		
	public void setDate_from(String val){
		if(val != null)
			date_from = val;
	}
	public void setDate_to(String val){
		if(val != null)
			date_to = val;
	}
		public List<Holiday> getHolidays(){
				return holidays;
		}
	public boolean isHoliday(String date){
		if(holidays == null || holidays.size() == 0) return false;
		return allSet.contains(date);
	}
    //
    // find all matching records
    // return "" or any exception thrown by DB
    //
    public String find(){
		//
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String qq = "select id,date_format(date,'%m/%d/%Y'),description from "+
			" holidays ", qw = "";
		if(!year.equals("")){
			qw += " year(date) = ? ";
		}
		else{
			if(!date_from.equals("")){
				qw += " date >= ? ";
			}
			if(!date_to.equals("")){
				if(!qw.equals("")) qw += " and ";
				qw += " date <= ? ";
			}
		}
		if(!qw.equals("")){
			qq += " where "+qw;
		}
		qq += " order by date ";
		String back = "";
		try{
			if(debug){
				logger.debug(qq);
			}
			con = Helper.getConnection();				
			if(con == null){
				back = "Could not connect to DB ";
				return back;
			}
			pstmt = con.prepareStatement(qq);
			int jj = 1;
			if(!year.equals(""))
				pstmt.setString(jj,year);
			else{
				if(!date_from.equals("")){
					pstmt.setDate(jj++, new java.sql.Date(dateFormat.parse(date_from).getTime()));						
				}
				if(!date_to.equals("")){
					pstmt.setDate(jj++, new java.sql.Date(dateFormat.parse(date_to).getTime()));							
				}
			}
			rs = pstmt.executeQuery();
			while(rs.next()){
				String str  = rs.getString(1);
				String str2 = rs.getString(2);
				String str3 = rs.getString(3);
				allSet.add(str2);
				if(holidays == null)
						holidays = new ArrayList<>();
				Holiday one = new Holiday(debug, str, str2, str3);
				holidays.add(one);
			}
		}
		catch(Exception ex){
			back += ex;
			logger.error(ex+":"+qq);
		}
		finally{
				Helper.databaseDisconnect(con, pstmt, rs);
		}
		return back;
    }
}






















































