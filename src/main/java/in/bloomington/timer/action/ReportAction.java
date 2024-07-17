package in.bloomington.timer.action;

/**
 * @copyright Copyright (C) 2014-2019 City of Bloomington, Indiana. All rights reserved.
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

public class ReportAction extends TopAction{

    static final long serialVersionUID = 1850L;	
    static Logger logger = LogManager.getLogger(ReportAction.class);
    static final int startYear = CommonInc.reportStartYear; 
    //
    String codeSet = "";    
    List<HourCode> allCodes = null;
    List<HourCode> availableCodes = null;
    List<HourCode> selectedCodes = null;

    List<TimeBlock> timeBlocks = null;
    List<WarpEntry> entries = null;
    List<Department> depts = null;
    Report report = null;
    List<Integer> years = null;
    List<WarpEntry> dailyEntries = null;
    String reportTitle = "Earn Code Generic Report ";
    public String execute(){
	String ret = SUCCESS;
	String back = doPrepare("report.action");
	if(!back.isEmpty()){
	    return back;
	}
	if(action.startsWith("Add")){
	    // we just ignore but we add the code to the list   
	}
	else if(action.startsWith("Remove")){
	    // we ignore we just remove from the list
	}
	else if(!action.isEmpty()){
	    getCodeSet();
	    report.setCodeSet(codeSet);
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
    public Report getReport(){ 
	if(report == null){
	    report = new Report();
	}		
	return report;
    }
    public void setCodeSet(String val){
	if(val != null)
	    codeSet = val;
    }
    public String getCodeSet(){
	if(codeSet == null || codeSet.isEmpty()){
	    if(sessionMap != null){	    
		if(sessionMap.get("codeSet") != null){
		    codeSet = (String) sessionMap.get("codeSet");
		}
	    }
	}
	return codeSet;
    }
    public void setAddCode(String val){
	if(val != null && !val.equals("-1")){
	    if(sessionMap != null){	    
		if(sessionMap.get("codeSet") != null){
		    codeSet = (String) sessionMap.get("codeSet");
		}
		if(!codeSet.isEmpty()){
		    codeSet +=",";
		}
		codeSet += val;
		sessionMap.put("codeSet", codeSet);		
	    }
	}
    }
    public void setRemoveCode(String val){
	String newCodeSet = "";
	if(val != null && !val.equals("-1")){
	    if(sessionMap != null){
		if(sessionMap.get("codeSet") != null){	    
		    codeSet = (String)sessionMap.get("codeSet");
		}
		if(!codeSet.isEmpty()){
		    if(codeSet.indexOf(",") > -1){
			try{
			    String[] arr = codeSet.split(",");
			    if(arr != null){
				for(String str:arr){
				    if(str.equals(val)) continue;
				    if(!newCodeSet.isEmpty()) newCodeSet += ",";
				    newCodeSet += str;
				}
			    }
			}catch(Exception ex){
			    System.err.println(ex);
			}
		    }
		    else if(codeSet.equals(val)){
			newCodeSet = "";
		    }
		}
		codeSet = newCodeSet;
		sessionMap.put("codeSet", codeSet);
	    }
	}
    }    

    public void setReport(Report val){
	if(val != null){
	    report = val;
	}
    }
    private void findAllCodes(){
	if(allCodes == null){
	    HourCodeList hcl = new HourCodeList();
	    hcl.setActiveOnly();
	    String back = hcl.findAbbreviatedList();
	    if(back.isEmpty()){
		allCodes = hcl.getHourCodes();
	    }
	}
    }
    private void findSelectedCodes(){
	if(allCodes == null)
	    findAllCodes();
	if(allCodes != null){
	    getCodeSet();
	    if(!codeSet.isEmpty()){
		if(codeSet.indexOf(",") > -1){
		    String[] arr = null;
		    try{
			arr = codeSet.split(",");
		    }catch(Exception ex){
			System.err.println(ex);
		    }
		    if(arr != null){
			for(String str:arr){
			    for(HourCode one:allCodes){
				if(str.equals(one.getId())){
				    if(selectedCodes == null)
					selectedCodes = new ArrayList<>();
				    if(!selectedCodes.contains(one))
					selectedCodes.add(one);
				}
			    }
			}
		    }
		}
		else{
		    for(HourCode one:allCodes){
			if(codeSet.equals(one.getId())){
			    if(selectedCodes == null)
				selectedCodes = new ArrayList<>();
				    if(!selectedCodes.contains(one))
					selectedCodes.add(one);
			}
		    }
		}
	    }
	}
    }
    public boolean hasAvailableCodes(){
	getAvailableCodes();
	return availableCodes != null && availableCodes.size() > 0;
    }
    public boolean hasSelectedCodes(){
	getSelectedCodes();
	return selectedCodes != null && selectedCodes.size() > 0;
	    
    }
	
    public List<HourCode> getAvailableCodes(){
	if(allCodes == null)
	    findAllCodes();
	if(allCodes != null){
	    findSelectedCodes();
	    if(availableCodes == null){
		if(selectedCodes == null){
		    availableCodes = allCodes;
		}
		else {
		    for(HourCode one:allCodes){
			if(selectedCodes.contains(one)) continue;
			if(availableCodes == null)
			    availableCodes = new ArrayList<>();
			availableCodes.add(one);
		    }
		}
	    }
	}
	return availableCodes;
    }
    public List<HourCode> getSelectedCodes(){
	if(selectedCodes == null)
	    findSelectedCodes();
	return selectedCodes;
    }
    public String getReportTitle(){
	if(report != null){
	    reportTitle = "Earn_Codes_Report_"+report.getStart_date()+" - "+report.getEnd_date()+".csv";
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
	String filename = "Ean_codes_report_";
	if(report.hasDepartment()){
	    filename += report.getDepartment().getName()+"_";
	}
	filename +=report.getEnd_date().replace("/","_")+".csv";
	return filename;
    }
    public String getDateRange(){
	String str = report.getStart_date()+" - "+report.getEnd_date();
	return str;
    }
    public List<Department> getDepts(){
	if(depts == null){
	    DepartmentList dl = new DepartmentList();
	    dl.setActiveOnly();
	    dl.hasRefIds();
	    String msg = dl.find();
	    if(!msg.isEmpty()){
		logger.error(msg);
	    }
	    else{
		List<Department> ones = dl.getDepartments();
		if(ones != null && ones.size() > 0){
		    depts = ones;
		}
	    }
	}
	return depts;
    }
    public boolean hasDepts(){
	getDepts();
	return depts != null && depts.size() > 0;
    }		
				
}





































