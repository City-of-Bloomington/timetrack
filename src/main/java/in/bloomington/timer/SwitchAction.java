package in.bloomington.timer;
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
import in.bloomington.timer.list.*;
import in.bloomington.timer.bean.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SwitchAction extends TopAction{

		static final long serialVersionUID = 1150L;	
		static Logger logger = LogManager.getLogger(SwitchAction.class);
		//
		String new_employee_id = "", document_id="", source="";
		// Employee employee = null;
		String employeesTitle = "Current Employees";
		public String execute(){
				String ret = SUCCESS;
				String back = doPrepare("switch.action");
				if(!back.equals("")){
						return "login";
				}
				clearAll();
				if(!action.equals("")){ // normally 'Change'
						if(!new_employee_id.equals("")){
								setEmployee_id(new_employee_id);
								getEmployee();
								if(employee == null){
										back = "could not get employee info ";
										addActionError(back);
										addError(back);
								}
								else{
										try{
												HttpServletResponse res = ServletActionContext.getResponse();
												String str = url+"timeDetails.action?";
												if(!document_id.equals("")){
														str += "document_id="+document_id;
												}
												if(!source.equals("")){
														str += "&source="+source;
												}												
												res.sendRedirect(str);
												return super.execute();
										}catch(Exception ex){
												System.err.println(ex);
										}
								}
						}
				}
				return ret;
		}
		public String getEmployeesTitle(){
				return employeesTitle;
		}

		public void setAction2(String val){
				if(val != null && !val.equals(""))		
						action = val;
		}
		public void setNew_employee_id(String val){
				if(val != null && !val.equals(""))		
						new_employee_id = val;
		}
		
		public void setEmployee_name(String val){
				// for auto complete
		}
		public void setDocument_id(String val){
				if(val != null && !val.equals(""))		
						document_id = val;
		}
		public void setSource(String val){
				if(val != null && !val.equals(""))		
					 source = val;
		}		
		public String getEmployee_name(){
				return "";
		}

}





































