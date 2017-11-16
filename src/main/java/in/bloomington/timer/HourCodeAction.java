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
import org.apache.log4j.Logger;
import in.bloomington.timer.list.*;
import in.bloomington.timer.bean.*;

public class HourCodeAction extends TopAction{

		static final long serialVersionUID = 750L;	
		static Logger logger = Logger.getLogger(HourCodeAction.class);
		//
		HourCode hourcode = null;
		List<HourCode> hourcodes = null;
		String hourcodesTitle = "Current hour codes";
		List<Type> accruals = null;
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
						back = hourcode.doSave();
						if(!back.equals("")){
								addActionError(back);
						}
						else{
								addActionMessage("Added Successfully");
						}
				}				
				else if(action.startsWith("Save")){
						back = hourcode.doUpdate();
						if(!back.equals("")){
								addActionError(back);
						}
						else{
								addActionMessage("Removed Successfully");
						}
				}
				else{		
						getHourcode();
						if(!id.equals("")){
								back = hourcode.doSelect();
								if(!back.equals("")){
										addActionError(back);
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

}




































