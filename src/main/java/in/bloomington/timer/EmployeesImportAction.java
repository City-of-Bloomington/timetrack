package in.bloomington.timer;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.*;
import java.io.*;
import java.io.File;
// import java.nio.file.*;
import org.apache.commons.io.FileUtils;
// import org.apache.tika.Tika;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;
import in.bloomington.timer.bean.RollBack;
import org.apache.log4j.Logger;

public class EmployeesImportAction extends TopAction{

		static final long serialVersionUID = 290L;	
		static Logger logger = Logger.getLogger(EmployeesImportAction.class);
		//
		String file_name = "/srv/data/timetrack/facilities.csv";
		File file = null;
		String fileName="", contentType="";
		EmployeesImport empImport = null;
		public String execute(){
				String ret = SUCCESS;
				String back = doPrepare();
				if(action.equals("Import")){
						if(file != null){
								RollBack roll = new RollBack();
								roll.doPrepare();
								if(roll.isFailure()){
										addError("Table max error:  this may cause the unsuccessful import roll back");
								}
								getEmpImport();
								back += empImport.doImport(file);
								if(!back.equals("")){
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
						RollBack roll = new RollBack();
						back = roll.doRollback();
						if(!back.equals("")){
								addError(back);
						}
						else{
								addMessage("Rolled back Successfully");
						}
				}				
				else{		
						getEmpImport();
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
		public String getFile_name(){
				return file_name;
		}
		public void setFile_name(String val){
				if(val != null)
						file_name = val;
		}


}





































