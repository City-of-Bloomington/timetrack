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

public class EmployeesImportAction extends TopAction{

		static final long serialVersionUID = 290L;	
		static Logger logger = Logger.getLogger(EmployeesImportAction.class);
		//
		String file_name = "/srv/data/timetrack/facilities.csv";
		
		EmployeesImport empImport = null;
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
				if(action.equals("Import")){
						getEmpImport();
						back = empImport.doImport(file_name);
						if(!back.equals("")){
								addActionError(back);
								addError(back);
						}
						else{
								addActionMessage("Save Successfully");
								addMessage("Saved Successfully");
						}
				}				
				else{		
						getEmpImport();
				}
				return ret;
		}
		public EmployeesImport getEmpImport(){ 
				if(empImport == null){
						empImport = new EmployeesImport();
				}		
				return empImport;
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





































