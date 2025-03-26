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

public class BenefitGroupRefAction extends TopAction{

    static final long serialVersionUID = 1810L;	
    static Logger logger = LogManager.getLogger(BenefitGroupRefAction.class);
    List<BenefitGroupRef> benefitGroupRefs = null;
    //
    BenefitGroupRef benefitGroupRef = null;
    List<SalaryGroup> salaryGroups = null;
    String benefitGroupRefsTitle = "Benefit Group Refs";
    public String execute(){
	String ret = SUCCESS;
	String back = canProceed("benefitGroupRef.action");
	if(!back.isEmpty()){
	    return back;
	}
	if(action.equals("Save")){
	    back = benefitGroupRef.doSave();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		addMessage("Saved Successfully");
	    }
	}				
	else if(action.startsWith("Save")){
	    back = benefitGroupRef.doUpdate();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		addMessage("Saved Successfully");
	    }
	}
	else{		
	    getBenefitGroupRef();
	    if(!id.isEmpty()){
		back = benefitGroupRef.doSelect();
		if(!back.isEmpty()){
		    addError(back);
		}
	    }
	}
	return ret;
    }
    public BenefitGroupRef getBenefitGroupRef(){ 
	if(benefitGroupRef == null){
	    benefitGroupRef = new BenefitGroupRef(id);
	}		
	return benefitGroupRef;
    }

    public void setBenefitGroupRef(BenefitGroupRef val){
	if(val != null){
	    benefitGroupRef = val;
	}
    }

    public String getBenefitGroupRefsTitle(){
	return benefitGroupRefsTitle;
    }
    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
	    action = val;
    }

    public boolean hasBenefitGroupRefs(){
	getBenefitGroupRefs();
	return benefitGroupRefs != null && benefitGroupRefs.size() > 0;
    }
    public List<BenefitGroupRef> getBenefitGroupRefs(){
	//
	BenefitGroupRefList gml = new BenefitGroupRefList();
	String back = gml.find();
	if(back.isEmpty()){
	    List<BenefitGroupRef> ones = gml.getRefs();
	    if(ones != null && ones.size() > 0){
	       benefitGroupRefs = ones;
	    }
	}
	return benefitGroupRefs;
    }
    public List<SalaryGroup> getSalaryGroups(){
	//
       SalaryGroupList gml = new SalaryGroupList();
	String back = gml.find();
	if(back.isEmpty()){
	    List<SalaryGroup> ones = gml.getSalaryGroups();
	    if(ones != null && ones.size() > 0){
		salaryGroups = ones;
	    }
	}
	return salaryGroups;
    }
		
}





































