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

public class ReportPrimeAction extends TopAction{

    static final long serialVersionUID = 1800L;	
    static Logger logger = LogManager.getLogger(ReportPrimeAction.class);
    static final int startYear = CommonInc.reportStartYear; 
    //
    List<List<String>> allEntries = null;
    List<List<String>> aggregates = null;    
    PrimeReport report = null;
    List<Integer> years = null;
    String outputType = "html";
    String reportTitle = "Prime Report ";
    public String execute(){
	String ret = SUCCESS;
	String back = doPrepare();
	if(!back.isEmpty()){
	    return back;
	}
	if(!action.isEmpty()){
	    back = report.findAggregates();
	    back += report.find();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		List<List<String>> ones = null;		
		if(outputType.equals("html")){
		    ones = report.getAllEntries();
		    if(ones != null && ones.size() > 0){
			allEntries = ones;
			addMessage("Found "+ones.size()+" entries");
		    }
		    else{
			addMessage("No records found");
		    }
		}
		ones = report.getAggregates();
		if(ones != null && ones.size() > 0){
		    aggregates = ones;
		    addMessage("Found "+ones.size()+" aggregates");
		}
		else{
		    addMessage("No records found");
		}
	    }
	}
	else{
	    getReport();
	}
	if(outputType.equals("csv")){
	    return "csv";
	}
	return ret;
    }
    public PrimeReport getReport(){ 
	if(report == null){
	    report = new PrimeReport();
	}		
	return report;
    }
    public String getOutputType(){
	return outputType;
    }
    public void setOutputType(String val){
	if(val != null)
	    outputType = val;
    }
    public void setReport(PrimeReport val){
	if(val != null){
	    report = val;
	}
    }
    public boolean hasAllEntries(){
	return allEntries != null && allEntries.size() > 0;
    }
    public List<List<String>> getAllEntries(){
	return allEntries;
    }
    public boolean hasAggregates(){
	return aggregates != null && aggregates.size() > 0;
    }
    public List<List<String>> getAggregates(){
	return aggregates;
    }    
    public String getReportTitle(){
	if(report != null){
	    reportTitle = " Prime Report "+report.getStart_date()+" - "+report.getEnd_date();
	}
	return reportTitle;
    }
    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
	    action = val;
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
	String filename="prime_report_"+report.getEnd_date().replace("/","_")+".csv";
	return filename;
    }
    public String getDateRange(){
	String str = report.getStart_date()+" - "+report.getEnd_date();
	return str;
    }
				
}





































