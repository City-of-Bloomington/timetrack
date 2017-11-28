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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SearchEmployeeAction extends TopAction{

		static final long serialVersionUID = 1800L;	
		static Logger logger = LogManager.getLogger(SearchEmployeeAction.class);
		//
		EmployeeList emplst = null;
		String employeesTitle = "Current Employees";
		List<Employee> employees = null;
		List<Type> departments = null;
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
						back = emplst.find();
						if(!back.equals("")){
								addActionError(back);
						}
						else{
								List<Employee> ones = emplst.getEmployees();
								if(ones != null && ones.size() > 0){
										employees = ones;
										addActionMessage("Found "+employees.size()+" employees");
								}
								else{
										addActionMessage("No match found");
								}
						}
				}
				getEmplst();
				return ret;
		}
		public EmployeeList getEmplst(){ 
				if(emplst == null){
						emplst = new EmployeeList();
				}		
				return emplst;
		}

		public void setEmplst(EmployeeList val){
				if(val != null){
						emplst = val;
				}
		}

		public String getEmployeeTitle(){
				return employeesTitle;
		}
		public void setAction2(String val){
				if(val != null && !val.equals(""))		
						action = val;
		}
		public boolean hasEmployees(){
				return employees != null && employees.size() > 0;
		}

		public List<Employee> getEmployees(){
				return employees;
		}
		public List<Type> getDepartments(){
				if(departments == null){
						TypeList tl = new TypeList();
						tl.setTable_name("departments");
						tl.setActiveOnly();
						String back = tl.find();
						if(back.equals("")){
								List<Type> ones = tl.getTypes();
								if(ones != null && ones.size() > 0){
										departments = ones;
								}
						}
				}
				return departments;
		}		
		
}





































