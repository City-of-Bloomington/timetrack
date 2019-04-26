package in.bloomington.timer.action;

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

public class GroupManagerAction extends TopAction{

		static final long serialVersionUID = 1800L;	
		static Logger logger = LogManager.getLogger(GroupManagerAction.class);
		//
		List<Node> nodes = null;
		String group_id="";
		String department_id="";
		List<Employee> employees = null;
		List<GroupManager> groupManagers = null;
		Group group = null;
		GroupManager groupManager = null;
		String groupManagersTitle = "Managers of this group";

		public String execute(){
				String ret = SUCCESS;
				String back = doPrepare();
				if(action.equals("Save")){
						back = groupManager.doSave();
						if(!back.equals("")){
								addActionError(back);
								addError(back);
						}
						else{
								addActionMessage("Saved Successfully");
								addMessage("Saved Successfully");			
						}
				}				
				else if(action.startsWith("Save")){
						back = groupManager.doUpdate();
						if(!back.equals("")){
								addActionError(back);
								addError(back);
						}
						else{
								addActionMessage("Saved Successfully");
								addMessage("Saved Successfully");			
						}
				}
				else{		
						getGroupManager();
						if(!id.equals("")){
								back = groupManager.doSelect();
								if(!back.equals("")){
										addActionError(back);
										addError(back);
								}
						}
						else{
								ret=INPUT;
						}
				}
				return ret;
		}
		public GroupManager getGroupManager(){ 
				if(groupManager == null){
						groupManager = new GroupManager(id);
						if(!group_id.equals(""))
								groupManager.setGroup_id(group_id);
				}		
				return groupManager;
		}

		public void setGroupManager(GroupManager val){
				if(val != null){
						groupManager = val;
				}
		}

		public String getGroupManagersTitle(){
				return groupManagersTitle;
		}
		public void setAction2(String val){
				if(val != null && !val.equals(""))		
						action = val;
		}

		public void setGroup_id(String val){
				if(val != null && !val.equals(""))		
						group_id = val;
		}
		public String getGroup_id(){
				if(group_id.equals("")){
						if(group != null){
								group_id = group.getId();
						}
						else if(groupManager != null){
								group_id = groupManager.getGroup_id();
						}
				}
				return group_id;
		}
		public Group getGroup(){
				getGroup_id();
				if(!group_id.equals("")){
						Group one = new Group(group_id);
						String back = one.doSelect();
						if(back.equals("")){
								group = one;
								department_id = group.getDepartment_id();
						}
				}
				return group;
		}
		public List<Node> getNodes(){
				if(nodes == null){
						NodeList nl = new NodeList();
						nl.setManagers_only();
						String back = nl.find();
						if(back.equals("")){
								List<Node> ones = nl.getNodes();
								if(ones != null)
										nodes = ones;
						}
				}
				return nodes;
		}
		public boolean hasNodes(){
				getNodes();
				return nodes != null;
		}		
		public boolean hasEmployees(){
				getEmployees();
				return employees != null;
		}
		public List<Employee> getEmployees(){
				//
				// given a group we will find employees in the related department
				//
				if(employees == null){
						if(department_id.equals("")){
								getGroup();
						}
						EmployeeList empl = new EmployeeList();
						if(!department_id.equals("")){
								empl.setDepartment_id(department_id);
						}
						empl.includeAllDirectors();
						//
						// managers can not be from the same group
						//
						empl.setExclude_group_id(group_id);
						String back = empl.find();
						if(back.equals("")){
								List<Employee> ones = empl.getEmployees();
								if(ones != null && ones.size() > 0){
										employees = ones;
								}
						}
				}
				return employees;
		}
		public boolean hasGroupManagers(){
				getGroupManagers();
				return groupManagers != null;
		}
		public List<GroupManager> getGroupManagers(){
				//
				getGroup_id();
				GroupManagerList gml = new GroupManagerList();
				gml.setGroup_id(group_id);
				String back = gml.find();
				if(back.equals("")){
						List<GroupManager> ones = gml.getManagers();
						if(ones != null && ones.size() > 0){
								groupManagers = ones;
						}
				}
				return groupManagers;
		}
}





































