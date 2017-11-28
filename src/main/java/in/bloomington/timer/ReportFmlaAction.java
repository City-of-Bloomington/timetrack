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
import in.bloomington.timer.timewarp.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReportFmlaAction extends TopAction{

		static final long serialVersionUID = 1850L;	
		static Logger logger = LogManager.getLogger(ReportFmlaAction.class);
		static final int startYear = 2017; // 
		//
		List<TimeBlock> timeBlocks = null;
		List<WarpEntry> entries = null;
		List<Department> depts = null;
		FmlaReport report = null;
		List<Integer> years = null;
		String reportTitle = "Fmla Report ";
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
				if(!action.equals("")){
						back = report.findHoursByNameCode();						
						if(!back.equals("")){
								addActionError(back);
						}
						else{
								if(true){
										List<WarpEntry> ones = report.getEntries();
										if(ones != null && ones.size() > 0){
												entries = ones;
												addActionMessage("Found "+ones.size()+" entries");
										}
								}
								if(true){
										back = report.find();
										if(!back.equals("")){
												addActionError(back);
										}
										else{
												List<TimeBlock> ones = report.getTimeBlocks();
												if(ones != null && ones.size() > 0){
														timeBlocks = ones;
												}
												else{
														addActionMessage("No match found");
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
		public FmlaReport getReport(){ 
				if(report == null){
						report = new FmlaReport();
				}		
				return report;
		}

		public void setReport(FmlaReport val){
				if(val != null){
						report = val;
				}
		}

		public String getReportTitle(){
				if(report != null){
						reportTitle = " FMLA Report "+report.getStart_date()+" - "+report.getEnd_date();
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
		
		// needed only for csv output
		public String getFileName(){
				String filename = "fmla_report_";
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
						if(!msg.equals("")){
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





































