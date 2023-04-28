package in.bloomington.timer.action;

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

public class ReportPlanAction extends TopAction{

    static final long serialVersionUID = 1800L;	
    static Logger logger = LogManager.getLogger(ReportPlanAction.class);
    static final int startYear = CommonInc.reportStartYear; 
    //
    List<TimeBlock> timeBlocks = null;
    List<WarpEntry> entries = null;
    List<WarpEntry> dailyEntries = null;		
    PlanReport report = null;
    List<Integer> years = null;
    String reportTitle = "Planning MPO Report ";
    public String execute(){
	String ret = SUCCESS;
	String back = doPrepare();
	if(!back.isEmpty()){
	    return back;
	}
	if(!action.isEmpty()){
	    back = report.findHoursByNameCode();
	    back += report.findHoursByDateAndCode();	
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		if(true){
		    List<WarpEntry> ones = report.getEntries();
		    if(ones != null && ones.size() > 0){
			entries = ones;
			addMessage("Found "+ones.size()+" entries");
		    }
		    else{
			addMessage("No records found");
		    }
		}
		if(true){
		    List<WarpEntry> ones = report.getDailyEntries();
		    if(ones != null && ones.size() > 0){
			dailyEntries = ones;
			addMessage("Found "+ones.size()+" daily entries");
		    }
		    else{
			addMessage("No records found");
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
    public PlanReport getReport(){ 
	if(report == null){
	    report = new PlanReport();
	}		
	return report;
    }

    public void setReport(PlanReport val){
	if(val != null){
	    report = val;
	}
    }

    public String getReportTitle(){
	if(report != null){
	    reportTitle = " Planning MPO Report "+report.getStart_date()+" - "+report.getEnd_date();
	}
	return reportTitle;
    }
    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
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
	    for(int yy=currentYear;yy >= startYear;yy--){
		years.add(yy);
	    }
	}
	return years;
    }		
    // needed only for csv output
    public String getFileName(){
	String filename="planning_mpo_report_"+report.getEnd_date().replace("/","_")+".csv";
	return filename;
    }
    public String getDateRange(){
	String str = report.getStart_date()+" - "+report.getEnd_date();
	return str;
    }
				
}





































