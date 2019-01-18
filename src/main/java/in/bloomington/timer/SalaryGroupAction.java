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

public class SalaryGroupAction extends TopAction{

		static final long serialVersionUID = 1810L;	
		static Logger logger = LogManager.getLogger(SalaryGroupAction.class);
		List<SalaryGroup> salaryGroups = null;
		//
		SalaryGroup salaryGroup = null;
		List<HourCode> hourCodes = null;
		final static String[] excess_types = {"Weekly","Daily","Pay Period","Other"};
		String salaryGroupsTitle = "Workflow Actions";
		public String execute(){
				String ret = SUCCESS;
				String back = doPrepare();
				if(action.equals("Save")){
						back = salaryGroup.doSave();
						if(!back.equals("")){
								addError(back);
						}
						else{
								addMessage("Saved Successfully");
						}
				}				
				else if(action.startsWith("Save")){
						back = salaryGroup.doUpdate();
						if(!back.equals("")){
								addError(back);
						}
						else{
								addMessage("Saved Successfully");
						}
				}
				else{		
						getSalaryGroup();
						if(!id.equals("")){
								back = salaryGroup.doSelect();
								if(!back.equals("")){
										addError(back);
								}
						}
				}
				return ret;
		}
		public SalaryGroup getSalaryGroup(){ 
				if(salaryGroup == null){
						salaryGroup = new SalaryGroup(id);
				}		
				return salaryGroup;
		}

		public void setSalaryGroup(SalaryGroup val){
				if(val != null){
						salaryGroup = val;
				}
		}

		public String getSalaryGroupsTitle(){
				return salaryGroupsTitle;
		}
		public void setAction2(String val){
				if(val != null && !val.equals(""))		
						action = val;
		}

		public boolean hasSalaryGroups(){
				getSalaryGroups();
				return salaryGroups != null && salaryGroups.size() > 0;
		}
		public List<SalaryGroup> getSalaryGroups(){
				//
				SalaryGroupList gml = new SalaryGroupList();
				String back = gml.find();
				if(back.equals("")){
						List<SalaryGroup> ones = gml.getSalaryGroups();
						if(ones != null && ones.size() > 0){
								salaryGroups = ones;
						}
				}
				return salaryGroups;
		}
		public List<HourCode> getHourCodes(){
				//
				HourCodeList gml = new HourCodeList();
				gml.setDefaultRegularOnly();
				String back = gml.find();
				if(back.equals("")){
						List<HourCode> ones = gml.getHourCodes();
						if(ones != null && ones.size() > 0){
								hourCodes = ones;
						}
				}
				return hourCodes;
		}
		public String[] getExcessTypes(){
				return excess_types;
		}
		
}





































