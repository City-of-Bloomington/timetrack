package in.bloomington.timer.action;
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

public class EmpTimeChangeAction extends TopAction{

		static final long serialVersionUID = 1150L;	
		static Logger logger = LogManager.getLogger(EmpTimeChangeAction.class);
		//
		String source="",
				department_id="", related_employee_id = "";
		Employee emp = null;
		String employeesTitle = "Related Employee";
		public String execute(){
				String ret = SUCCESS;
				String back = doPrepare("empTimeChange.action");
				if(!action.equals("")){ // 'Next'
						if(!related_employee_id.equals("")){
								getEmp();
								if(emp == null){
										back = "could not get employee info ";
										addError(back);
								}
								else{
										try{
												HttpServletResponse res = ServletActionContext.getResponse();
												String str = "jobTimeChange.action?related_employee_id="+related_employee_id;
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
				else {
						getUser();
						if(user != null && !(user.isAdmin() || user.isHrAdmin())){
								String val = user.getDepartment_id();
								if(val != null)
										department_id = val;
						}
				}
				return ret;
		}
		public String getEmployeesTitle(){
				return employeesTitle;
		}
		public String getDepartment_id(){
				return department_id;
		}
		public void setAction2(String val){
				if(val != null && !val.equals(""))		
						action = val;
		}
		public void setRelated_employee_id(String val){
				if(val != null && !val.equals(""))		
						related_employee_id = val;
		}
		
		public void setEmployee_name(String val){
				// for auto complete
		}
		public void setDepartment_id(String val){
				if(val != null && !val.equals(""))		
						department_id = val;
		}		
		public void setSource(String val){
				if(val != null && !val.equals(""))		
					 source = val;
		}		
		public String getEmployee_name(){
				return "";
		}
    public Employee getEmp(){
				if(emp == null && !related_employee_id.equals("")){
						Employee one = new Employee(related_employee_id);
						String back = one.doSelect();
						if(back.equals("")){
								emp = one;
						}
				}				
				return emp;
    }
		
}





































