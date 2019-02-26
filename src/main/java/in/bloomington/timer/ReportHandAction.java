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
import in.bloomington.timer.list.*;
import in.bloomington.timer.bean.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.report.*;
import in.bloomington.timer.timewarp.WarpEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReportHandAction extends TopAction{

		static final long serialVersionUID = 1800L;	
		static Logger logger = LogManager.getLogger(ReportHandAction.class);
		static final int startYear = 2017; // 
		//
		List<TimeBlock> timeBlocks = null;
		List<WarpEntry> entries = null;
		HandReport report = null;
		List<Integer> years = null;
		String reportTitle = "HAND MPO Report ";
		public String execute(){
				String ret = SUCCESS;
				String back = doPrepare();
				if(!action.equals("")){
						back = report.findHoursByNameCode();						
						if(!back.equals("")){
								addError(back);
						}
						else{
								if(true){
										List<WarpEntry> ones = report.getEntries();
										if(ones != null && ones.size() > 0){
												entries = ones;
												addMessage("Found "+ones.size()+" entries");
										}
								}
								if(true){
										back = report.find();
										if(!back.equals("")){
												addError(back);
										}
										else{
												List<TimeBlock> ones = report.getTimeBlocks();
												if(ones != null && ones.size() > 0){
														timeBlocks = ones;
												}
												else{
														addMessage("No match found");
												}
										}
								}
						}
				}
				else{
						getReport();
				}
				if(report.getType().equals("csv")){
						return "csv";
				}
				return ret;
		}
		public HandReport getReport(){ 
				if(report == null){
						report = new HandReport();
				}		
				return report;
		}

		public void setReport(HandReport val){
				if(val != null){
						report = val;
				}
		}

		public String getReportTitle(){
				if(report != null){
						reportTitle = " HAND MPO Report "+report.getStart_date()+" - "+report.getEnd_date();
				}
				return reportTitle;
		}
		public void setAction2(String val){
				if(val != null && !val.equals(""))		
						action = val;
		}
		// todo
		public List<TimeBlock> getTimeBlocks(){
				return timeBlocks;
		}
		public List<Integer> getYears(){
				if(years == null){
						int currentYear = Helper.getCurrentYear();
						years = new ArrayList<>();
						for(int yy=startYear;yy <= currentYear;yy++){
								years.add(yy);
						}
				}
				return years;
		}
		public String getFileName(){
				String filename="hand_mpo_report_"+report.getEnd_date().replace("/","_")+".csv";
				return filename;
		}
		public String getDateRange(){
				String str = report.getStart_date()+" - "+report.getEnd_date();
				return str;
		}
				
}





































