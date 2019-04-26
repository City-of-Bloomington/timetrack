package in.bloomington.timer.action;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.*;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;  
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;
import in.bloomington.timer.bean.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HolidayAction extends TopAction{

		static final long serialVersionUID = 3800L;	
		static Logger logger = LogManager.getLogger(HolidayAction.class);
		//
		int year = 0;
		Holiday holiday = null;
		String holidaysTitle = "Holidays";		
		List<Holiday> holidays = null;
		public String execute(){
				String ret = SUCCESS;
				String back = doPrepare();
				if(action.equals("Save")){
						back = holiday.doSave();
						if(!back.equals("")){
								addActionError(back);
								addError(back);
																
						}
						else{
								addActionMessage("Saved Successfully");
								addMessage("Saved Successfully");
						}
				}				
				else if(action.startsWith("Save")){
						back = holiday.doUpdate();
						if(!back.equals("")){
								addActionError(back);
								addError(back);
																
						}
						else{
								addActionMessage("Saved Successfully");
								addMessage("Saved Successfully");								
						}
				}
				else if(action.startsWith("Delete")){
						back = holiday.doDelete();
						if(!back.equals("")){
								addActionError(back);
								addError(back);
																
						}
						else{
								addActionMessage("Deleted Successfully");
								addMessage("Deleted Successfully");
								id="";
								holiday = new Holiday(debug);
						}
				}				
				else{		
						getHoliday();
						if(!id.equals("")){
								back = holiday.doSelect();
								if(!back.equals("")){
										addActionError(back);
										addError(back);
								}
						}
				}
				return ret;
		}
		public Holiday getHoliday(){
				if(holiday == null){
						holiday = new Holiday(debug);
						holiday.setId(id);
				}
				return holiday;
						
		}
		public void setHoliday(Holiday val){
				if(val != null){
						holiday = val;
				}
		}
		public String getHolidaysTitle(){
				
				return holidaysTitle;
		}

		public void setAction2(String val){
				if(val != null && !val.equals(""))		
						action = val;
		}
		public void setYear(int val){
				if( val > 0)		
						year = val;
		}
		public int getYear(){
				if(year == 0)
						year = Helper.getCurrentYear();
				return year;
		}
		public List<Holiday> getHolidays(){
				if(holidays == null){
						HolidayList tl = new HolidayList(debug);
						tl.setYear(""+year);
						String back = tl.find();
						if(back.equals("")){
								List<Holiday> ones = tl.getHolidays();
								if(ones != null && ones.size() > 0){
										holidays = ones;
								}
						}
				}
				return holidays;
		}
		public List<String> getYears(){
				List<String> years = new ArrayList<>();
				int startYear = Helper.getCurrentYear()+1;
				for(int i=startYear;i>startYear-5;i--){
						years.add(""+i);
				}
				return years;
		}
}





































