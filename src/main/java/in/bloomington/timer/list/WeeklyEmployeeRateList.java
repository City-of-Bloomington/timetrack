package in.bloomington.timer.list;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.*;
import java.sql.*;
import java.io.*;
import javax.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;

public class WeeklyEmployeeRateList{

    static Logger logger = LogManager.getLogger(WeeklyEmployeeRateList.class);
    static final long serialVersionUID = 3800L;
    List<WeeklyEmployeeRate> weeklyRates = null;
    String week_no="", employee_id="", pay_period_id="";
	
    public WeeklyEmployeeRateList(){
    }
    public List<WeeklyEmployeeRate> getWeeklyRates(){
	return weeklyRates;
    }
		
    public void setEmploye_id(String val){
	if(val != null)
	    employee_id = val;
    }
    public void setPayPeriod_id(String val){
	if(val != null)
	    pay_period_id = val;
    }
    public void setWeekNo(String val){
	if(val != null)
	    week_no = val;
    }
    public String find(){
		
	String back = "";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection con = UnoConnect.getConnection();
	String qq = "select id,pay_period_id,week_no,employee_id,pay_rate "+
	    "from weekly_employee_rates ";
				
	if(con == null){
	    back = "Could not connect to DB";
	    return back;
	}
	String qw = "";
	try{
	    if(!employee_id.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";
		qw += " employee_id = ? ";
	    }
	    if(!pay_period_id.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";
		qw += " pay_period_id = ? ";
	    }
	    if(!week_no.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";
		qw += " week_no = ? ";
	    }	    
	    if(!qw.isEmpty()){
		qq += " where "+qw;
	    }
	    logger.debug(qq);
	    pstmt = con.prepareStatement(qq);
	    int jj=1;
	    if(!employee_id.isEmpty()){
		pstmt.setString(jj++, employee_id);
	    }
	    if(!pay_period_id.isEmpty()){
		pstmt.setString(jj++, pay_period_id);
	    }
	    if(!week_no.isEmpty()){
		pstmt.setString(jj++, week_no);
	    }	    
	    rs = pstmt.executeQuery();
	    if(weeklyRates == null)
		weeklyRates = new ArrayList<WeeklyEmployeeRate>();
	    while(rs.next()){
		WeeklyEmployeeRate one =
		    new WeeklyEmployeeRate(rs.getString(1),
				rs.getString(2),
				rs.getString(3),
				rs.getString(4),
				rs.getDouble(5));
		if(!weeklyRates.contains(one))
		    weeklyRates.add(one);
	    }
	}
	catch(Exception ex){
	    back += ex+" : "+qq;
	    logger.error(back);
	}
	finally{
	    Helper.databaseDisconnect(pstmt, rs);
	    UnoConnect.databaseDisconnect(con);
	}
	return back;
    }
}






















































