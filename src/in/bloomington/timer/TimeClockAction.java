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

public class TimeClockAction extends TopAction{

		static final long serialVersionUID = 4320L;	
		static Logger logger = Logger.getLogger(TimeClockAction.class);
		DecimalFormat dFormat = new DecimalFormat("###.00");
		//
		TimeClock timeClock = null;
		String timeClocksTitle = "Time Clock Data";
		String document_id="", date="";
		List<TimeBlock> timeBlocks = null;
		//
		public String execute(){
				String ret = SUCCESS;
				String back = doPrepare();
				if(!action.equals("")){
						back = timeClock.process();
						if(!back.equals("")){
								addActionError(back);
						}
						else{
								document_id = timeClock.getTimeBlock().getDocument_id();
								date = timeClock.getTimeBlock().getDate();
								addActionMessage("Received Successfully");
						}
				}				
				else{		
						getTimeClock();
				}
				return ret;
		}
		public TimeClock getTimeClock(){ 
				if(timeClock == null){
						timeClock = new TimeClock();
				}
				return timeClock;
		}
		public void setTimeClock(TimeClock val){
				if(val != null){
						timeClock = val;
				}
		}
		public List<TimeBlock> getTimeBlocks(){
				if(timeBlocks == null){
						if(!document_id.equals("") && !date.equals("")){
								TimeBlockList tbl = new TimeBlockList();
								tbl.setDocument_id(document_id);
								tbl.setDate(date);
								tbl.hasClockInAndOut(); //ignore clock-in only
								String back = tbl.find();
								if(back.equals("")){
										List<TimeBlock> ones = tbl.getTimeBlocks();
										// if more than one
										if(ones != null && ones.size() > 1){
												timeBlocks = ones;
										}
								}
						}
				}
				return timeBlocks;
		}
		public boolean hasTimeBlocks(){
				getTimeBlocks();
				return timeBlocks != null && timeBlocks.size() > 0;
		}
		public String getTotalHours(){
				double total = 0.;
				if(timeBlocks != null && timeBlocks.size() > 0){
						for(TimeBlock one:timeBlocks){
								total += one.getHours();
						}
				}
				return dFormat.format(total);
		}
		public String getTimeClocksTitle(){
				return timeClocksTitle;
		}
		public void setAction2(String val){
				if(val != null && !val.equals(""))		
						action = val;
		}
		//

}





































