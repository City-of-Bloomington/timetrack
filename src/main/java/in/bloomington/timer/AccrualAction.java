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
// import org.apache.log4j.Logger;
import in.bloomington.timer.list.*;
import in.bloomington.timer.bean.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AccrualAction extends TopAction{

		static final long serialVersionUID = 3800L;	
		static Logger logger = LogManager.getLogger(AccrualAction.class);
		//
		String accrualsTitle = "Current Accrual Types";
		Accrual accrual = null;
		List<Accrual> accruals = null;

		public String execute(){
				String ret = SUCCESS;
				String back = doPrepare();
				if(action.equals("Save")){
						back = accrual.doSave();
						if(!back.equals("")){
								addError(back);
								addActionError(back);
						}
						else{
								addMessage("Saved Successfully");
								addActionMessage("Saved Successfully");
						}
				}				
				else if(action.startsWith("Save")){
						back = accrual.doUpdate();
						if(!back.equals("")){
								addError(back);
								addActionError(back);
						}
						else{
								addMessage("Saved Successfully");
								addActionMessage("Saved Successfully");
						}
				}
				else{		
						getAccrual();
						if(!id.equals("")){
								back = accrual.doSelect();
								if(!back.equals("")){
										addError(back);
										addActionError(back);
								}
						}
				}
				return ret;
		}
		public Accrual getAccrual(){
				if(accrual == null){
						accrual = new Accrual();
						accrual.setId(id);
				}
				return accrual;
						
		}
		public void setAccrual(Accrual val){
				if(val != null){
						accrual = val;
				}
		}
		
		public String getAccrualsTitle(){
				
				return accrualsTitle;
		}

		public void setAction2(String val){
				if(val != null && !val.equals(""))		
						action = val;
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

}





































