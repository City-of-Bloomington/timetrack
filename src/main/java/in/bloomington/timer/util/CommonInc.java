package in.bloomington.timer.util;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import javax.naming.*;
import javax.naming.directory.*;
import java.text.SimpleDateFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommonInc{

    public boolean debug = false;
    static final long serialVersionUID = 400L;			
    static Logger logger = LogManager.getLogger(CommonInc.class);
    // this year is used to create the list of years for running
    // reports, normally this the production start year
    public final static int reportStartYear = 2018; 
    // a bunch of static variables that will be used all over
    // the code
    public final static String default_effective_date = "07/01/2017";
    // approve workflow id
    public final static String default_approve_workflow_id="3";
    //
    // payroll process approve workflow id
    public final static String default_payroll_process_workflow_id="4";
    //
    // the following two items are used in bulk notification emails
    public final static String emailStr = "@bloomington.in.gov";
    // you need to set the fromEmailStr
    public final static String fromEmailStr = "helpdesk@bloomington.in.gov";

    public final static String[] hourCodeTypes = {"Regular","Used","Earned","Overtime","Unpaid", "Other"};
    public final static String[] excess_hours_earn_types={"Monetary","Earn Time","Donation"};		
    //
    // PROF HRS earn code ID (from database)
    public final static String profHoursEarnCodeID = "109";
    // related code strring
    public final static String profHoursEarnCodeStr = "PROF HRS";

    // standard city weekly hours
    public final static double cityStandardWeeklyHrs=40.;
    //
    // default regualar (Reg) earn code and related ID used by many 
    // for full time employees (match the database earn codes)
    public final static String regEarnCodeStr = "Reg";
    public final static String regEarnCodeID = "1";		
    // for temp employees
    public final static String tempEarnCodeStr = "TEMP";
    public final static String tempEarnCodeID = "14";
    //
    // Fire depart special reg code for BC group only
    public final static String regEarnCodeFireBCGroupStr = "REG FIRE BC";		
    public final static String regEarnCodeFireBCGroupID = "111";
    //
    // earn code for fire sworn 
    public final static String regEarnCodeFireStr = "REG FIRE";
    public final static String regEarnCodeFireID = "93";
    //
    // overtime earn codes
    public final static String overTime10EarnCodeID = "78"; // OT1.0		
    public final static String overTime15EarnCodeID = "43"; // OT1.5
    public final static String overTime20EarnCodeID = "44"; // OT2.0
    //
    // comp time earned earn codes
    public final static String compTime10EarnCodeID = "71"; // CE1.0
    public final static String compTime15EarnCodeID = "34"; // CE1.5
    public final static String compTime20EarnCodeID = "45"; // CE2.0
    //
    // holiday earned earn codes
    public final static String holyCompTime10EarnCodeID = "50"; // HCE1.0		
    public final static String holyCompTime15EarnCodeID = "79"; // HCE1.5
    public final static String holyCompTime20EarnCodeID = "46"; // HCE2.0
    //
    // max monetary value allowed that can be earned when used
    // with monetary earn codes that does not have default value
    public final static double maxMonetaryAmount = 1000.0; // CA for example
    public final static double critical_small = 0.01;
    //
    // the following two array are used in reports month/day in mm/dd format
    public final static String[] quarter_starts = {"","01/01/","04/01/","07/01/","10/01/"};
    public final static String[] quarter_ends = {"","03/31/","06/30/","09/30/","12/31/"};
    // pay period id where dispatch swtiched to Sunday Schedule
    public static final String pay_period_cut_id = "715"; 
    public static final String pay_period_switched_date = "2025-06-07"; 
    
    //
    // list of reserved usernames that can not be used to login in timetrack,
    // add any username if needed to the list below
    // remove the ones that are not used
    public final static String[] invalid_usernames = {"clerk","hrmail","lineloc","admin","hnd","police","public.works","council","legalarc","mayor","recruits","util-purchasing"};		
    String message = "";
    List<String> errors = null;
    //
    public CommonInc(){
    }
    public CommonInc(boolean deb){
	debug = deb;
    }
    /*
      public String getMessage(){
      return message;
      }
      public boolean hasMessage(){
      return !message.isEmpty();
      }
      public boolean hasErrors(){
      return (errors != null && errors.size() > 0);
      }
      public List<String> getErrors(){
      return errors;
      }
      public void addError(String val){
      if(errors == null)
      errors = new ArrayList<>();
      errors.add(val);
      }
    */

}
