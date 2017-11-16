package in.bloomington.timer;

/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.*;
import java.io.*;
import java.text.*;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;  
import org.apache.log4j.Logger;
import in.bloomington.timer.list.*;
import in.bloomington.timer.bean.*;

public class ClockAction extends TopAction{

		static final long serialVersionUID = 4300L;	
		static Logger logger = Logger.getLogger(ClockAction.class);
		//
		TimeClock clock = null;
		String clocksTitle = "Clock-In, Clock-Out times";
		//
		public String execute(){
				String ret = SUCCESS;
				String back = doPrepare();
				if(!back.equals("")){
						try{
								HttpServletResponse res = ServletActionContext.getResponse();
								String str = url+"Login";
								res.sendRedirect(str);
								return super.execute();
						}catch(Exception ex){
								System.err.println(ex);
						}	
				}
				if(!action.equals("")){ // action name not important
						back = ""; // clock.process();
						if(!back.equals("")){
								addActionError(back);
						}
						else{
								addActionMessage("Added Successfully");
						}
				}				
				else{		
						getClock();
				}
				return ret;
		}
		public TimeClock getClock(){ 
				if(clock == null){
						clock = new TimeClock();
				}
				return clock;
		}
		public void setTimeClock(TimeClock val){
				if(val != null){
						clock = val;
				}
		}
		public String getClocksTitle(){
				return clocksTitle;
		}
		public void setAction2(String val){
				if(val != null && !val.equals(""))		
						action = val;
		}


}




































