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

public class ShiftTimeAction extends TopAction{

		static final long serialVersionUID = 1800L;	
		static Logger logger = LogManager.getLogger(ShiftTimeAction.class);
		//
		ShiftTime shift = null;
		List<ShiftTime> shifts = null;
		List<Group> groups = null;
		List<HourCode> hourCodes = null;
		List<PayPeriod> payPeriods = null;
		List<Department> departments = null;
		String department_id = "", group_id="";
		String shiftsTitle = "Current shift times";
		Department department = null;
		public String execute(){
				String ret = SUCCESS;
				String back = doPrepare();
				if(action.equals("Save")){
						getUser();
						shift.setAdded_by_id(user.getId());
						back = shift.doSave();
						if(!back.equals("")){
								addError(back);
						}
						else{
								addMessage("Added Successfully");
						}
				}				
				else if(action.startsWith("Save")){
						getUser();
						shift.setAdded_by_id(user.getId());						
						back = shift.doUpdate();
						Group group = shift.getGroup();
						department_id = group.getDepartment_id();
						if(!back.equals("")){
								addError(back);								
						}
						else{
								addMessage("Added Successfully");
						}
				}
				else if(action.startsWith("Process")){
						getUser();
						shift.setAdded_by_id(user.getId());
						back = shift.doProcess();
						Group group = shift.getGroup();
						department_id = group.getDepartment_id();						
						if(!back.equals("")){
								addError(back);								
						}
						else{
								addMessage("Times Added Successfully");
						}
				}				
				else{		
						getShift();
						if(!id.equals("")){
								back = shift.doSelect();
								Group group = shift.getGroup();
								department_id = group.getDepartment_id();
								if(!back.equals("")){
										addError(back);
								}								
						}
				}
				return ret;
		}
		public ShiftTime getShift(){ 
				if(shift == null){
						shift = new ShiftTime();
						shift.setId(id);
				}		
				return shift;
		}

		public void setShift(ShiftTime val){
				if(val != null){
						shift = val;
				}
		}
		public void setGroup_id(String val){
				if(val != null && !val.equals("-1")){
						group_id = val;
				}
		}
		public void setDepartment_id(String val){
				if(val != null && !val.equals("-1")){
						department_id = val;
				}
		}
		public boolean hasDepartment(){
				return !department_id.equals("");
		}
		public String getDepartment_id(){
				if(department_id.equals("")){
						return "-1";
				}
				return department_id;
		}		
		public void setDate(String val){

		}		
		public String getShiftsTitle(){
				return shiftsTitle;
		}
		public String getDate(){
				return "";
		}		
		public void setAction2(String val){
				if(val != null && !val.equals(""))		
						action = val;
		}
		public List<ShiftTime> getShifts(){
				if(shifts == null){
						ShiftTimeList tl = new ShiftTimeList();
						String back = tl.find();
						if(back.equals("")){
								List<ShiftTime> ones = tl.getShifts();
								if(ones != null && ones.size() > 0){
										shifts = ones;
								}
						}
				}
				return shifts;
		}
		//
		// if department Id is set
		public boolean hasGroups(){
				getGroups();
				return groups != null  && groups.size() > 0;
		}
		public List<Group> getGroups(){
				if(groups == null){
						if(!department_id.equals("")){
								GroupList gsl = new GroupList();
								gsl.setDepartment_id(department_id);
								String back = gsl.find();
								if(back.equals("")){
										List<Group> ones = gsl.getGroups();
										if(ones != null && ones.size() > 0){
												groups = ones;
										}
								}
						}
				}
				return groups;
		}
		public Department getDepartment(){
				if(department == null && !department_id.equals("")){
						Department one = new Department(department_id);
						String back = one.doSelect();
						if(back.equals("")){
								department = one;
						}
				}
				return department;
		}
		public List<Department> getDepartments(){
				if(departments == null){
						DepartmentList gsl = new DepartmentList();
						gsl.ignoreSpecialDepts();
						String back = gsl.find();
						if(back.equals("")){
								List<Department> ones = gsl.getDepartments();
								if(ones != null && ones.size() > 0){
										departments = ones;
								}
						}
				}
				return departments;
		}		
		public List<HourCode> getHourCodes(){
				if(hourCodes == null){
						HourCodeList gsl = new HourCodeList();
						gsl.setDefaultRegularOnly();
						String back = gsl.find();
						if(back.equals("")){
								List<HourCode> ones = gsl.getHourCodes();
								if(ones != null && ones.size() > 0){
										hourCodes = ones;
								}
						}
				}
				return hourCodes;
		}
		public List<PayPeriod> getPayPeriods(){
				if(payPeriods == null){
						PayPeriodList gsl = new PayPeriodList();
						gsl.setTwoPeriodsAheadOnly();
						gsl.setLimit("4");
						String back = gsl.find();
						if(back.equals("")){
								List<PayPeriod> ones = gsl.getPeriods();
								if(ones != null && ones.size() > 0){
										payPeriods = ones;
								}
						}
				}
				return payPeriods;
		}		
		public boolean hasShifts(){
				getShifts();
				return shifts != null && shifts.size() > 0;
		}

		
}





































