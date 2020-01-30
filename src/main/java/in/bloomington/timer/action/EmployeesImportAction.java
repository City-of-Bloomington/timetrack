package in.bloomington.timer.action;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.*;
import java.io.*;
import java.io.File;
import org.apache.commons.io.FileUtils;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;
import in.bloomington.timer.bean.RollBack;
import in.bloomington.timer.*;
import in.bloomington.timer.action.TopAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EmployeesImportAction extends TopAction{

		static final long serialVersionUID = 290L;	
		static Logger logger = LogManager.getLogger(EmployeesImportAction.class);
		//
		String file_name = "/srv/data/timetrack/facilities.csv";
		String multiJob = "";
		File file = null;
		String fileName="", contentType="";
		EmployeesImport empImport = null;
		RollBack roll = null;
		public String execute(){
				String ret = SUCCESS;
				String back = doPrepare();
				if(action.equals("Import")){
						if(file != null){
								roll = new RollBack();
								roll.doPrepare();
								if(roll.isFailure()){
										addError("Table max error:  this may cause the unsuccessful import roll back");
								}
								getEmpImport();
								if(multiJob.isEmpty())
										back += empImport.doImport(file);
								else
										back += empImport.doImportMultiJobs(file);
								if(!back.isEmpty()){
										addError(back);
								}
								else{
										addMessage("Data Imported Successfully");
								}
						}
						else{
								addError("You need to specify the CSV file to import");
						}
				}
				if(action.equals("Rollback")){
						roll = new RollBack();
						back = roll.doRollback();
						if(!back.isEmpty()){
								addError(back);
						}
						else{
								addMessage("Rolled back Successfully");
						}
				}				
				else{		
						getEmpImport();
						getRoll();
						roll.findLastRollDate();						
				}
				return ret;
		}
		public EmployeesImport getEmpImport(){ 
				if(empImport == null){
						empImport = new EmployeesImport(envBean);
				}		
				return empImport;
		}
		public void setCsv_fileFileName(String val) {
				if(val != null){
						this.fileName = val;
				}
		}
		public void setCsv_fileContentType(String val) {
				if(val != null){
						contentType = val;
				}
		}		
		public void setCsv_file(File file) {
				this.file = file;
		}		
		public void setEmpImport(EmployeesImport val){
				if(val != null){
						empImport = val;
				}
		}
		public RollBack getRoll(){
				if(roll == null)
						roll = new RollBack();
				return roll;
		}		
		public String getFile_name(){
				return file_name;
		}
		public void setFile_name(String val){
				if(val != null)
						file_name = val;
		}
		public boolean getMultiJob(){
				return !multiJob.isEmpty();
		}
		public void setMultiJob(String val){
				if(val != null && !val.isEmpty())
						multiJob = "y";
		}


}





































