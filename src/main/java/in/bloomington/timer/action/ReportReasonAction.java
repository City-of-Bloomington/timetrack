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

public class ReportReasonAction extends TopAction{

    static final long serialVersionUID = 1850L;	
    static Logger logger = LogManager.getLogger(ReportReasonAction.class);
    static final int startYear = CommonInc.reportStartYear; 
    //
    List<WarpEntry> entries = null;
    ReasonReport report = null;
    List<Integer> years = null;
    List<WarpEntry> dailyEntries = null;
    String reportTitle = "Code Reasons Report ";
    String type = "Reason";
    public String execute(){
	String ret = SUCCESS;
	String back = doPrepare();
	if(!back.isEmpty()){
	    return back;
	}
	if(!action.isEmpty()){
	    if(type.equals("Reason")){
		back = report.findHoursByNameAndCode();
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
	    else{ // all codes
		back = report.findHoursCodeDetails();
		if(!back.isEmpty()){
		    addError(back);
		}
		else{
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
	if(report.getOutputType().equals("csv")){
	    return "csv";
	}
	return ret;
    }
    public ReasonReport getReport(){ 
	if(report == null){
	    report = new ReasonReport();
	}		
	return report;
    }

    public void setReport(ReasonReport val){
	if(val != null){
	    report = val;
	}
    }

    public String getReportTitle(){
	if(report != null){
	    reportTitle = "Code Reason Report "+report.getStart_date()+" - "+report.getEnd_date();
	}
	return reportTitle;
    }
    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
	    action = val;
    }
    public void setType(String val){
	if(val != null && !val.isEmpty())		
	    type = val;
    }
    public String getType(){
	return type;
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
	String filename = "code_reason_report_";
	filename +=report.getEnd_date().replace("/","_")+".csv";
	return filename;
    }
    public String getDateRange(){
	String str = report.getStart_date()+" - "+report.getEnd_date();
	return str;
    }

				
}





































