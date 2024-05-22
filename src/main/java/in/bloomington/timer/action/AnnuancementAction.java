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

public class AnnuancementAction extends TopAction{

    static final long serialVersionUID = 3800L;	
    static Logger logger = LogManager.getLogger(AnnuancementAction.class);
    //
    String annuancementsTitle = "Annuancements";
    Annuancement annuance = null;
    List<Annuancement> annuancements = null;

    public String execute(){
	String ret = SUCCESS;
	String back = "";
	if(action.equals("Save")){
	    back = annuance.doSave();
	    if(!back.isEmpty()){
		addError(back);
		addActionError(back);
	    }
	    else{
		addMessage("Saved Successfully");
	    }
	}				
	else if(action.startsWith("Save")){
	    back = annuance.doUpdate();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		addMessage("Saved Successfully");
	    }
	}
	else{		
	    getAnnuance();
	    if(!id.isEmpty()){
		back = annuance.doSelect();
		if(!back.isEmpty()){
		    addError(back);
		}
	    }
	}
	return ret;
    }
    public Annuancement getAnnuance(){
	if(annuance == null){
	    annuance = new Annuancement();
	    annuance.setId(id);
	}
	return annuance;
						
    }
    public void setAnnuance(Annuancement val){
	if(val != null){
	    annuance = val;
	}
    }
		
    public String getAnnuancementsTitle(){
				
	return annuancementsTitle;
    }

    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
	    action = val;
    }
    public List<Annuancement> getAnnuancements(){
	if(annuancements == null){
	    AnnuancementList tl = new AnnuancementList();
	    String back = tl.find();
	    if(back.isEmpty()){
		List<Annuancement> ones = tl.getAnnuancements();
		if(ones != null && ones.size() > 0){
		    annuancements = ones;
		}
	    }
	}
	return annuancements;
    }

}





































