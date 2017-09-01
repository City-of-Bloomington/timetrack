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

public class UserAction extends TopAction{

		static final long serialVersionUID = 3950L;	
		static Logger logger = Logger.getLogger(UserAction.class);
		//
		String type_name="department", table_name="departments";
		List<User> employees = null;
		String employeesTitle = "Current Users";

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
				if(action.equals("Save")){
						back = employee.doSave();
						if(!back.equals("")){
								addActionError(back);
						}
						else{
								addActionMessage("Added Successfully");
						}
				}				
				else if(action.startsWith("Save")){
						back = employee.doUpdate();
						if(!back.equals("")){
								addActionError(back);
						}
						else{
								addActionMessage("Removed Successfully");
						}
				}
				else{		
						getEmployee();
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
		public List<User> getEmployees(){
				if(employees == null){
						UserList tl = new UserList();
						tl.setActiveOnly();
						String back = tl.find();
						if(back.equals("")){
								List<User> ones = tl.getUsers();
								if(ones != null && ones.size() > 0){
										employees = ones;
								}
						}
				}
				return employees;
		}

}





































