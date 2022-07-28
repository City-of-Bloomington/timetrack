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

public class GroupLocationAction extends TopAction{

		static final long serialVersionUID = 3800L;	
		static Logger logger = LogManager.getLogger(GroupLocationAction.class);
		//
		String groupLocationsTitle = "Group locations";
		String group_id = "";
		GroupLocation groupLocation = null;
		List<GroupLocation> groupLocations = null;
		List<Type> departments = null;
		List<Location> locations = null;
		public String execute(){
				String ret = SUCCESS;
				String back = doPrepare();
				if(action.equals("Save")){
						back = groupLocation.doSave();
						if(!back.isEmpty()){
								addError(back);
						}
						else{
								addMessage("Saved Successfully");
						}
				}				
				else if(action.startsWith("Save")){
						back = groupLocation.doUpdate();
						if(!back.isEmpty()){
								addError(back);
						}
						else{
								addMessage("Saved Successfully");
						}
				}
				else if(action.startsWith("Delete")){
						back = groupLocation.doDelete();
						if(!back.isEmpty()){
								addError(back);
						}
						else{
								id="";
								groupLocation = new GroupLocation();
								addMessage("Deleted Successfully");
						}
				}				
				else{		
						getGroupLocation();
						groupLocation.setGroup_id(group_id);
						if(!id.isEmpty()){
								back = groupLocation.doSelect();
								if(!back.isEmpty()){
										addError(back);
								}
						}
				}
				return ret;
		}
		public GroupLocation getGroupLocation(){
				if(groupLocation == null){
						groupLocation = new GroupLocation(id);
				}
				return groupLocation;
						
		}
		public void setGroupLocation(GroupLocation val){
				if(val != null){
						groupLocation = val;
				}
		}
		
		public String getGroupLocationsTitle(){
				
				return groupLocationsTitle;
		}

		public void setAction2(String val){
				if(val != null && !val.isEmpty())		
						action = val;
		}
		public void setGroup_id(String val){
				if(val != null && !val.equals("-1"))		
						group_id = val;
		}
		public void setDepartment_id(String val){
				// not needed
		}		
		public List<GroupLocation> getGroupLocations(){
				if(groupLocations == null){
						GroupLocationList tl = new GroupLocationList();
						tl.setGroup_id(group_id);
						String back = tl.find();
						if(back.isEmpty()){
								List<GroupLocation> ones = tl.getGroupLocations();
								if(ones != null && ones.size() > 0){
										groupLocations = ones;
								}
						}
				}
				return groupLocations;
		}
		public List<Location> getLocations(){
				if(locations == null){
						LocationList tl = new LocationList();
						String back = tl.find();
						if(back.isEmpty()){
								List<Location> ones = tl.getLocations();
								if(ones != null && ones.size() > 0){
										locations = ones;
								}
						}
				}
				return locations;
		}		
		public List<Type> getDepartments(){
				if(departments == null){
						TypeList tl = new TypeList();
						tl.setTable_name("departments");
						tl.setActiveOnly();
						String back = tl.find();
						if(back.isEmpty()){
								List<Type> ones = tl.getTypes();
								if(ones != null && ones.size() > 0){
										departments = ones;
								}
						}
				}
				return departments;
		}		

}





































