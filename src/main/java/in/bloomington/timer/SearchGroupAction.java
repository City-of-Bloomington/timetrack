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

public class SearchGroupAction extends TopAction{

		static final long serialVersionUID = 1800L;	
		static Logger logger = LogManager.getLogger(SearchGroupAction.class);
		//
		GroupList grplst = null;
		String groupsTitle = "Current Groups";
		List<Group> groups = null;
		List<Type> departments = null;
		public String execute(){
				String ret = SUCCESS;
				String back = doPrepare();
				if(!action.equals("")){
						back = grplst.find();
						if(!back.equals("")){
								addError(back);
						}
						else{
								List<Group> ones = grplst.getGroups();
								if(ones != null && ones.size() > 0){
										groups = ones;
										groupsTitle = " Found "+groups.size()+" groups";
										addMessage("Found "+groups.size()+" groups");
								}
								else{
										addMessage("No match found");
										groupsTitle = "No match found";
								}
						}
				}
				getGrplst();
				return ret;
		}
		public GroupList getGrplst(){ 
				if(grplst == null){
						grplst = new GroupList();
				}		
				return grplst;
		}

		public void setGrplst(GroupList val){
				if(val != null){
						grplst = val;
				}
		}

		public String getGroupsTitle(){
				return groupsTitle;
		}
		public void setAction2(String val){
				if(val != null && !val.equals(""))		
						action = val;
		}
		public boolean hasGroups(){
				return groups != null && groups.size() > 0;
		}

		public List<Group> getGroups(){
				return groups;
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





































