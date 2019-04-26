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

public class GroupShiftAction extends TopAction{

		static final long serialVersionUID = 2100L;	
		static Logger logger = LogManager.getLogger(GroupShiftAction.class);
		//
		String group_id="", shift_id="", department_id="";
		Shift shift = null;
		Group group = null;
		List<Group> groups = null;
		List<Shift> shifts = null;		
		GroupShift groupShift = null;
		List<GroupShift> groupShifts = null;
		List<Department> departments = null;
		String groupShiftsTitle = "Group Shifts";
		public String execute(){
				String ret = SUCCESS;
				String back = doPrepare();
				if(action.equals("Save")){
						back = groupShift.doSave();
						if(!back.equals("")){
								addActionError(back);
								addError(back);
						}
						else{
								group_id = groupShift.getGroup_id();
								shift_id = groupShift.getShift_id();
								addMessage("Saved Successfully");								
						}
				}				
				else if(action.startsWith("Save")){ 
						back = groupShift.doUpdate();
						if(!back.equals("")){
								addError(back);
								addActionError(back);
						}
						else{
								group_id = groupShift.getGroup_id();
								shift_id = groupShift.getShift_id();
								addMessage("Saved Successfully");			
						}
				}
				else if(action.startsWith("Remove")){
						back = groupShift.doSelect();
						group_id = groupShift.getGroup_id();						
						back = groupShift.doDelete();
						id = "";
						groupShift = new GroupShift();
						groupShift.setGroup_id(group_id);
						if(!back.equals("")){
								addError(back);
						}
						else{
								addMessage("Removed Successfully");			
						}
				}				
				else{		
						getGroupShift();
						if(!id.equals("")){
								back = groupShift.doSelect();
								if(!back.equals("")){
										addError(back);
								}
								shift_id = groupShift.getShift_id();
								group_id = groupShift.getGroup_id();
						}
				}
				return ret;
		}
		public GroupShift getGroupShift(){ 
				if(groupShift == null){
						groupShift = new GroupShift(id);
						groupShift.setShift_id(shift_id);
						groupShift.setGroup_id(group_id);
				}		
				return groupShift;
		}

		public void setGroupShift(GroupShift val){
				if(val != null){
						groupShift = val;
				}
		}
		public String getDepartment_id(){
				if(department_id.equals("") && !group_id.equals("")){
						getGroup();
						if(group != null){
								department_id = group.getDepartment_id();
						}
				}
				return department_id;
		}
		public Shift getShift(){
				if(shift == null && !shift_id.equals("")){
						Shift one = new Shift(shift_id);
						String back = one.doSelect();
						if(back.equals("")){
								shift = one;
						}
				}
				return shift;
		}
		public Group getGroup(){
				if(group == null && !group_id.equals("")){
						Group one = new Group(group_id);
						String back = one.doSelect();
						if(back.equals("")){
								group = one;
						}
				}
				return group;
		}		
		public String getGroupShiftsTitle(){
				return groupShiftsTitle;
		}

		public void setAction2(String val){
				if(val != null && !val.equals(""))		
						action = val;
		}
		public void setGroup_id(String val){
				if(val != null && !val.equals("-1"))		
						group_id = val;
		}
		public void setShift_id(String val){
				if(val != null && !val.equals("-1"))		
						shift_id = val;
		}
		public void setDepartment_id(String val){
				if(val != null && !val.equals("-1"))		
						department_id = val;
		}					
		public List<Group> getGroups(){
				if(groups == null && !department_id.equals("")){
						// we are interested in the group of the shift department
						// we need this list just in case we need to change
						// shift group
						GroupList tl = new GroupList();
						tl.setDepartment_id(department_id);
						tl.setActiveOnly();
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
		public boolean hasGroupShifts(){
				getGroupShifts();
				return groupShifts != null;
		}
		public List<GroupShift> getGroupShifts(){
				if(groupShifts == null){
						GroupShiftList gel = new GroupShiftList();
						gel.setGroup_id(group_id);
						String back = gel.find();
						if(back.equals("")){
								List<GroupShift> ones = gel.getGroupShifts();
								if(ones != null && ones.size() > 0){
										groupShifts = ones;
								}
						}
				}
				return groupShifts;
		}
		public List<Shift> getShifts(){
				if(shifts == null){
						ShiftList gel = new ShiftList();
						gel.setActiveOnly();
						String back = gel.find();
						if(back.equals("")){
								List<Shift> ones = gel.getShifts();
								if(ones != null && ones.size() > 0){
										shifts = ones;
								}
						}
				}
				return shifts;
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

}





































