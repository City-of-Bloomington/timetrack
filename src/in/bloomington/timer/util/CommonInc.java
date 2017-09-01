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
import org.apache.log4j.Logger;

public class CommonInc{

		public boolean debug = false;
		static final long serialVersionUID = 400L;			
		static Logger logger = Logger.getLogger(CommonInc.class);
		// a bunch of static variables that will be used all over
		// the code
		public final static String default_effective_date = "07/01/2017";
		// approve workflow id
		public final static String default_approve_workflow_id="3";
		// payroll process approve workflow id
		public final static String default_payroll_process_workflow_id="4";		
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
