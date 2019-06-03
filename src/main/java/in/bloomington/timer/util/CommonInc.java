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
		//
		// Reg code ID used by many (from database)
		public final static String regEarnCodeID = "1";
		public final static String regEarnCodeStr = "Reg";
		public final static String tempEarnCodeID = "14";
		public final static String tempEarnCodeStr = "TEMP";		
		//
		// Fire depart special reg code for BC group only
		public final static String regEarnCodeFireBCGroupID = "111";
		public final static String regEarnCodeFireBCGroupStr = "REG FIRE BC";
		public final static String regEarnCodeFireID = "93";
		public final static String regEarnCodeFireStr = "REG FIRE";
		public final static String overTime10EarnCodeID = "78"; // OT1.0		
		public final static String overTime15EarnCodeID = "43"; // OT1.5
		public final static String overTime20EarnCodeID = "44"; // OT2.0
		public final static String compTime10EarnCodeID = "71"; // CE1.0
		public final static String compTime15EarnCodeID = "34"; // CE1.5
		public final static String compTime20EarnCodeID = "45"; // CE2.0
		public final static String holyCompTime10EarnCodeID = "50"; // HCE1.0		
		public final static String holyCompTime15EarnCodeID = "79"; // HCE1.5
		public final static String holyCompTime20EarnCodeID = "48"; // HCE2.0			
		public final static double maxMonetaryAmount = 1000.0; // CA for example

   	String message = "";
		List<String> errors = null;
		//
		public CommonInc(){
		}
		public CommonInc(boolean deb){
				debug = deb;
		}

		public String getMessage(){
				return message;
		}
		public boolean hasMessage(){
				return !message.equals("");
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

}
