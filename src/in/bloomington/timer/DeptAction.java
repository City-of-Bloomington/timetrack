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

public class DeptAction extends TopAction{

		static final long serialVersionUID = 3800L;	
		static Logger logger = Logger.getLogger(DeptAction.class);
		//
		// default department
		String deptsTitle = "Departments";
		Department department = null;
		List<Department> departments = null;
		List<Group> groups = null;
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
						back = department.doSave();
						if(!back.equals("")){
								addActionError(back);
						}
						else{
								addActionMessage("Saved Successfully");
						}
				}				
				else if(action.startsWith("Save")){
						back = department.doUpdate();
						if(!back.equals("")){
								addActionError(back);
						}
						else{
								addActionMessage("Saved Successfully");
						}
				}
				else{		
						getDepartment();
						if(!id.equals("")){
								back = department.doSelect();
								if(!back.equals("")){
								addActionError(back);
								}
						}
				}
				return ret;
		}
		public Department getDepartment(){
				if(department == null){
						department = new Department();
						department.setId(id);
				}
				return department;
						
		}
		public void setDepartment(Department val){
				if(val != null){
						department = val;
				}
		}
		public String getDeptsTitle(){
				
				return deptsTitle;
		}

		public void setAction2(String val){
				if(val != null && !val.equals(""))		
						action = val;
		}
		public List<Department> getDepartments(){
				if(departments == null){
						DepartmentList tl = new DepartmentList();
						tl.setActiveOnly();
						String back = tl.find();
						if(back.equals("")){
								List<Department> ones = tl.getDepartments();
								if(ones != null && ones.size() > 0){
										departments = ones;
								}
						}
				}
				return departments;
		}
		public List<Group> getGroups(){
				if(department != null && !department.getId().equals("")){
						GroupList gl = new GroupList();
						gl.setDepartment_id(department.getId());
						String back = gl.find();
						if(back.equals("")){
								List<Group> ones = gl.getGroups();
								if(ones != null && ones.size() > 0){
										groups = ones;
								}
						}
				}
				return groups;
		}		

}





































