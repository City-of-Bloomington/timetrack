package in.bloomington.timer;
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
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HandleShiftDifferential{

    boolean debug = false;
    static final long serialVersionUID = 53L;
    static Logger logger = LogManager.getLogger(HandleShiftDifferential.class);
    static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    static DecimalFormat df = new DecimalFormat("#0.00");
    //
    String dept_ref_id="36"; // Police Dept only
    String effective_date = "";
    Map<String, String> shiftCodeMap = null;
    Hashtable<String, String> empCodes = null;
    //
    // accrual values from New World (Carry Over)
    //
    public HandleShiftDifferential(){
	prepareCodeMap();
    }
    // new
    public HandleShiftDifferential(String val){
	prepareCodeMap();
	setEffectiveDate(val);
    }
    //
    // setters
    //
    public void setEffectiveDate(String val){
	if(val != null){		
	    effective_date = val;
	}
    }
    public Hashtable<String, String> getEmpCodes(){
	return empCodes;
    }
    private String prepareCodeMap(){
	ShiftDiffCodeList scl = new ShiftDiffCodeList();
	String back = scl.find();
	if(back.isEmpty()){
	    Map<String, String> map = scl.getShiftCodeMap();
	    if(map != null){
		shiftCodeMap = map;
	    }
	}
	return back;
    }
        /**
	   8 th column
    //Input
 HR.HRReport_EmployeeScheduleExport
@AsOfDate='2026-06-22 00:00:00',
@SelectBy=2,
@EmployeeId=default,
@DepartmentIds='36',
@BenefitGroupIds=default,
@EmployeeStatusIds='258'
    
output
1 EmployeeId
2 EmployeeNumber
3 EmployeeName
4 DepartmentId
5 Department
6 BenefitGroupId
7 BenefitGroup
8 StartDate
9 EndDate
10 ScheduleType
11 Day
12 Frequency
13 Cycle
14 FirstCycleMonth
15 CycleDay
16 HoursCode
17 Hours
18 OtherPay
19 ShiftCode
20 SeparateCheck
21 IncludeInBudget
22 IncludeIneTimesheets
23 GLOrganization
24 Project
25 IsSecondaryJob
26 SecondaryJobTitle
27 Grade
28 Rate
29 PrimaryKeyId

	*/
    //
    public String process(){
		
	Connection con = null;
	PreparedStatement pstmt = null;
	CallableStatement ps = null;
	ResultSet rs = null;
	String msg="", date="";
	String today = Helper.getToday();
	String cur_date = Helper.getYymmddDate2(today);
	empCodes = new Hashtable<>();
	//
	//
	// String qq = "{CALL HR.HRReport_EmployeePayRateReport(null,'0',null,?,null,'3,1,2',2,0,1,0,1,3,0)}";
	// String qq = "CALL HR.HRReport_EmployeeScheduleExport(@AsOfDate='2026-06-22 00:00:00',@SelectBy=2,@EmployeeId=default,@DepartmentIds='36',@BenefitGroupIds=default,@EmployeeStatusIds='258')";
	//String qq = "CALL HR.HRReport_EmployeeScheduleExport('2026-06-23 00:00:00',2,'default','36','default','258')";
	String qq = "{CALL HR.HRReport_EmployeeScheduleExport('2026-06-23',2,null,'36',null,'258')}";	
	logger.debug(qq);
	try{
	    con = SingleConnect.getNwConnection();
	    if(con == null){
		msg = " Could not connect to DB ";
		System.err.println(msg);
		logger.error(msg);
		return msg;
	    }
	    ps = con.prepareCall(qq);

	    rs = ps.executeQuery();
	    /**
	    ResultSetMetaData rsmd = rs.getMetaData();
	    int columnCount = rsmd.getColumnCount();
	    for (int i = 1; i <= columnCount; i++ ) {
		String name = rsmd.getColumnName(i);
		System.err.println(i+" "+name);
	    }
	    */
	    while(rs.next()){
		String str = rs.getString(2); // employee number
		String str2 = rs.getString(3); // name
		String str3 = rs.getString(8); // start date
		String str4 = rs.getString(9);// end date
		String str5 = rs.getString(11); // day
		String str6 = rs.getString(16); // hour code
		// we skip unwanted ones
		if(!str6.startsWith("Reg") && 
		   !str6.startsWith("BPD") &&
		   !str6.startsWith("LTD")){
		    String str8 = "";
		    String str7 = str6.substring(0,str6.indexOf(" - "));
		    if(str7 != null && !str7.isEmpty()){
			if(shiftCodeMap != null && shiftCodeMap.containsKey(str7)){
			    str8 = shiftCodeMap.get(str7);
			    empCodes.put(str, str8);
			}
		    }
		    System.err.println(str+" "+str2+" "+str7+" "+str8);
		}
	    }
	    //
	}
	catch (Exception ex) {
	    logger.error(ex+":"+qq);
	    msg += ex;
	}
	finally{
	    Helper.databaseDisconnect(ps, rs);
	    // SingleConnect.disconnect();
	}
	return msg;
    }

}




