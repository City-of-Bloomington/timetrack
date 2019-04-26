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
// import org.apache.log4j.Logger;
import in.bloomington.timer.list.*;
import in.bloomington.timer.bean.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AccrualContributeAction extends TopAction{

		static final long serialVersionUID = 3800L;	
		static Logger logger = LogManager.getLogger(AccrualContributeAction.class);
		//
		String contributesTitle = "Current Accrual Contribute Types";
		AccrualContribute contribute = null;
		List<AccrualContribute> contributes = null;
		List<Accrual> accruals = null;
		List<HourCode> hourCodes = null;
		public String execute(){
				String ret = SUCCESS;
				String back = doPrepare();
				if(action.equals("Save")){
						back = contribute.doSave();
						if(!back.equals("")){
								addError(back);
						}
						else{
								addMessage("Saved Successfully");
						}
				}				
				else if(action.startsWith("Save")){
						back = contribute.doUpdate();
						if(!back.equals("")){
								addError(back);
						}
						else{
								addMessage("Saved Successfully");
						}
				}
				else{		
						getContribute();
						if(!id.equals("")){
								back = contribute.doSelect();
								if(!back.equals("")){
										addError(back);
								}
						}
				}
				return ret;
		}
		public AccrualContribute getContribute(){
				if(contribute == null){
						contribute = new AccrualContribute();
						contribute.setId(id);
				}
				return contribute;
						
		}
		public void setContribute(AccrualContribute val){
				if(val != null){
						contribute = val;
				}
		}
		
		public String getContributesTitle(){
				
				return contributesTitle;
		}

		public void setAction2(String val){
				if(val != null && !val.equals(""))		
						action = val;
		}
		public List<AccrualContribute> getContributes(){
				if(contributes == null){
						AccrualContributeList tl = new AccrualContributeList();
						String back = tl.find();
						if(back.equals("")){
								List<AccrualContribute> ones = tl.getContributes();
								if(ones != null && ones.size() > 0){
										contributes = ones;
								}
						}
				}
				return contributes;
		}
		public List<Accrual> getAccruals(){
				if(accruals == null){
						AccrualList tl = new AccrualList();
						tl.setActiveOnly();
						String back = tl.find();
						if(back.equals("")){
								List<Accrual> ones = tl.getAccruals();
								if(ones != null && ones.size() > 0){
										accruals = ones;
								}
						}
				}
				return accruals;
		}		
		public List<HourCode> getHourCodes(){
				if(hourCodes == null){
						HourCodeList tl = new HourCodeList();
						tl.setActiveOnly();
						String back = tl.find();
						if(back.equals("")){
								List<HourCode> ones = tl.getHourCodes();
								if(ones != null && ones.size() > 0){
										hourCodes = ones;
								}
						}
				}
				return hourCodes;
		}
		public boolean hasAccruals(){
				getAccruals();
				return accruals  != null && accruals.size() > 0;
		}
		public boolean hasHourCodes(){
				getHourCodes();
				return hourCodes != null && hourCodes.size() > 0;
		}
		public boolean hasContributes(){
				getContributes();
				return contributes  != null && contributes.size() > 0;
		}		
}





































