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

public class HourCodeAction extends TopAction{

		static final long serialVersionUID = 750L;	
		static Logger logger = LogManager.getLogger(HourCodeAction.class);
		//
		HourCode hourcode = null;
		List<HourCode> hourcodes = null;
		String hourcodesTitle = "Current Earn Codes";
		List<Type> accruals = null;
		String[] types = new String[]{"Regular","Used","Earned","Overtime","Unpaid","Monetary","Call Out","Other"}; 
		public String execute(){
				String ret = SUCCESS;
				String back = doPrepare();
				if(action.equals("Save")){
						back = hourcode.doSave();
						if(!back.equals("")){
								addError(back);
						}
						else{
								addMessage("Added Successfully");
						}
				}				
				else if(action.startsWith("Save")){
						back = hourcode.doUpdate();
						if(!back.equals("")){
								addError(back);
						}
						else{
								addMessage("Saved Successfully");
						}
				}
				else{		
						getHourcode();
						if(!id.equals("")){
								back = hourcode.doSelect();
								if(!back.equals("")){
										addActionError(back);
										addError(back);
								}
						}
				}
				return ret;
		}
		public HourCode getHourcode(){
				if(hourcode == null){
						hourcode = new HourCode(id);
				}
				return hourcode;
						
		}
		public void setHourcode(HourCode val){
				if(val != null){
						hourcode = val;
				}
		}
		public String getHourcodesTitle(){
				return hourcodesTitle;
		}

		public void setAction2(String val){
				if(val != null && !val.equals(""))		
						action = val;
		}
		public boolean hasHourcodes(){
				getHourcodes();
				return hourcodes != null;
		}
		public List<HourCode> getHourcodes(){
				if(hourcodes == null){
						HourCodeList tl = new HourCodeList();
						String back = tl.find();
						if(back.equals("")){
								List<HourCode> ones = tl.getHourCodes();
								if(ones != null && ones.size() > 0){
										hourcodes = ones;
								}
						}
				}
				return hourcodes;
		}
		public List<Type> getAccruals(){
				if(accruals == null){
						TypeList tl = new TypeList("accruals");
						String back = tl.find();
						if(back.equals("")){
								List<Type> ones = tl.getTypes();
								if(ones != null && ones.size() > 0){
										accruals = ones;
								}
						}
				}
				return accruals;
		}
		public String[] getTypes(){
				return types;
		}

}





































