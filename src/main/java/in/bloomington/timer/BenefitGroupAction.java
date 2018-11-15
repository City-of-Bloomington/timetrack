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

public class BenefitGroupAction extends TopAction{

		static final long serialVersionUID = 3800L;	
		static Logger logger = LogManager.getLogger(BenefitGroupAction.class);
		//
		String benefitGroupsTitle = "Benefit Groups";
		BenefitGroup benefitGroup = null;
		List<BenefitGroup> benefitGroups = null;

		public String execute(){
				String ret = SUCCESS;
				String back = doPrepare();
				if(action.equals("Save")){
						back = benefitGroup.doSave();
						if(!back.equals("")){
								addActionError(back);
						}
						else{
								addActionMessage("Saved Successfully");
						}
				}				
				else if(action.startsWith("Save")){
						back = benefitGroup.doUpdate();
						if(!back.equals("")){
								addActionError(back);
								addError(back);
						}
						else{
								addActionMessage("Saved Successfully");
								addMessage("Saved Successfully");
						}
				}
				else if(action.startsWith("Delete")){
						back = benefitGroup.doDelete();
						if(!back.equals("")){
								addActionError(back);
						}
						else{
								addActionMessage("Deleted Successfully");
								addMessage("Deleted Successfully");
								id="";
								benefitGroup = new BenefitGroup(debug);
						}
				}				
				else{		
						getBenefitGroup();
						if(!id.equals("")){
								back = benefitGroup.doSelect();
								if(!back.equals("")){
										addActionError(back);
										addError(back);
								}
						}
				}
				return ret;
		}
		public BenefitGroup getBenefitGroup(){
				if(benefitGroup == null){
						benefitGroup = new BenefitGroup();
						benefitGroup.setId(id);
				}
				return benefitGroup;
						
		}
		public void setBenefitGroup(BenefitGroup val){
				if(val != null){
						benefitGroup = val;
				}
		}
		
		public String getBenefitGroupsTitle(){
				
				return benefitGroupsTitle;
		}
		public void setAction2(String val){
				if(val != null && !val.equals(""))		
						action = val;
		}
		public List<BenefitGroup> getBenefitGroups(){
				if(benefitGroups == null){
						BenefitGroupList tl = new BenefitGroupList();
						String back = tl.find();
						if(back.equals("")){
								List<BenefitGroup> ones = tl.getBenefitGroups();
								if(ones != null && ones.size() > 0){
										benefitGroups = ones;
								}
						}
				}
				return benefitGroups;
		}

}





































