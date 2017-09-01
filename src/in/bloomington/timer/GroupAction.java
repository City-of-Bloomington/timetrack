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
import in.bloomington.timer.list.*;
import in.bloomington.timer.bean.*;

public class GroupAction extends TopAction{

		static final long serialVersionUID = 1800L;	
		static Logger logger = Logger.getLogger(GroupAction.class);
		//
		Group group = null;
		List<Group> groups = null;
		List<GroupManager> groupManagers;
		String groupsTitle = "Current groups";
		List<Type> departments = null;
		String department_id="";
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
						back = group.doSave();
						if(!back.equals("")){
								addActionError(back);
						}
						else{
								addActionMessage("Added Successfully");
						}
				}				
				else if(action.startsWith("Save")){
						back = group.doUpdate();
						if(!back.equals("")){
								addActionError(back);
						}
						else{
								addActionMessage("Updated Successfully");
						}
				}
				else{		
						getGroup();
						if(!id.equals("")){
								back = group.doSelect();
								if(!back.equals("")){
										addActionError(back);
								}								
						}
				}
				return ret;
		}
		public Group getGroup(){ 
				if(group == null){
						group = new Group();
						group.setId(id);
						group.setDepartment_id(department_id);
				}		
				return group;
		}

		public void setGroup(Group val){
				if(val != null){
						group = val;
				}
		}

		public String getGroupsTitle(){
				return groupsTitle;
		}
		public void setAction2(String val){
				if(val != null && !val.equals(""))		
						action = val;
		}
		public void setDepartment_id(String val){
				if(val != null && !val.equals(""))		
						department_id = val;
		}		
		public List<Group> getGroups(){
				if(groups == null){
						GroupList tl = new GroupList();
						tl.setActiveOnly();
						if(!department_id.equals("")){
								tl.setDepartment_id(department_id);
						}
						String back = tl.find();
						if(back.equals("")){
								List<Group> ones = tl.getGroups();
								if(ones != null && ones.size() > 0){
										groups = ones;
								}
						}
				}
				return groups;
		}
		public List<Type> getDepartments(){
				TypeList tl = new TypeList("departments");
				String back = tl.find();
				if(back.equals("")){
						List<Type> ones = tl.getTypes();
						if(ones != null && ones.size() > 0){
								departments = ones;
						}
				}
				return departments;
		}
		public boolean hasGroupManagers(){
				getGroupManagers();
				return groupManagers != null;
		}
		public List<GroupManager> getGroupManagers(){
				//
				getId();
				GroupManagerList gml = new GroupManagerList();
				gml.setGroup_id(id);
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





































