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
import in.bloomington.timer.list.*;
import in.bloomington.timer.bean.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EarnCodeReasonAction extends TopAction{

		static final long serialVersionUID = 750L;	
		static Logger logger = LogManager.getLogger(EarnCodeReasonAction.class);
		//
		EarnCodeReason reason = null;
		List<EarnCodeReason> reasons = null;
		List<ReasonCategory> categories = null;
		String reasonsTitle = "Current Earn Codde Reasons";
		public String execute(){
				String ret = SUCCESS;
				String back = doPrepare();
				if(action.equals("Save")){
						back = reason.doSave();
						if(!back.equals("")){
								addError(back);
						}
						else{
								addMessage("Added Successfully");
						}
				}				
				else if(action.startsWith("Save")){
						back = reason.doUpdate();
						if(!back.equals("")){
								addError(back);
						}
						else{
								addMessage("Saved Successfully");
						}
				}
				else{		
						getReason();
						if(!id.equals("")){
								back = reason.doSelect();
								if(!back.equals("")){
										addError(back);
								}
						}
				}
				return ret;
		}
		public EarnCodeReason getReason(){
				if(reason == null){
						reason = new EarnCodeReason(id);
				}
				return reason;
						
		}
		public void setReason(EarnCodeReason val){
				if(val != null){
						reason = val;
				}
		}
		public String getReasonsTitle(){
				return reasonsTitle;
		}

		public void setAction2(String val){
				if(val != null && !val.equals(""))		
						action = val;
		}
		public boolean hasReasons(){
				getReasons();
				return reasons != null;
		}
		public List<EarnCodeReason> getReasons(){
				if(reasons == null){
						EarnCodeReasonList tl = new EarnCodeReasonList();
						String back = tl.find();
						if(back.equals("")){
								List<EarnCodeReason> ones = tl.getReasons();
								if(ones != null && ones.size() > 0){
										reasons = ones;
								}
						}
				}
				return reasons;
		}
		public boolean hasCategories(){
				getCategories();
				return categories != null;
		}
		public List<ReasonCategory> getCategories(){
				if(categories == null){
						ReasonCategoryList tl = new ReasonCategoryList();
						String back = tl.find();
						if(back.equals("")){
								List<ReasonCategory> ones = tl.getReasonCategories();
								if(ones != null && ones.size() > 0){
										categories = ones;
								}
						}
				}
				return categories;
		}
}





































