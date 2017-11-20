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
import org.apache.log4j.Logger;
import in.bloomington.timer.list.*;
import in.bloomington.timer.bean.*;

public class SwitchAction extends TopAction{

		static final long serialVersionUID = 1150L;	
		static Logger logger = Logger.getLogger(SwitchAction.class);
		//
		Employee employee = null;
		String employeesTitle = "Current Employees";
		public String execute(){
				String ret = SUCCESS;
				String back = doPrepare();
				if(!back.equals("")){
						return "error";
				}
				if(action.startsWith("Change")){
						if(!employee_id.equals("")){
								getEmployee();
								back = employee.doSelect();
								if(!back.equals("")){
										addActionError(back);
								}
								else{
										try{
												HttpServletResponse res = ServletActionContext.getResponse();
												String str = url+"timeDetails.action";
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
		public Employee getEmployee(){
				if(employee == null){
						employee = new Employee();
						employee.setId(employee_id);
				}
				return employee;
						
		}
		public String getEmployeesTitle(){
				return employeesTitle;
		}

		public void setAction2(String val){
				if(val != null && !val.equals(""))		
						action = val;
		}
		public void setEmployee_name(String val){
				// for auto complete
		}
		public String getEmployee_name(){
				return "";
		}

}





































