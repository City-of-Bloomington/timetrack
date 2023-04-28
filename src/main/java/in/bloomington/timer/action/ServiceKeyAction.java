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
import in.bloomington.timer.util.CommonInc;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServiceKeyAction extends TopAction{

    static final long serialVersionUID = 1800L;	
    static Logger logger = LogManager.getLogger(ServiceKeyAction.class);
    //
    ServiceKey key = null;
    List<ServiceKey> keys = null;
    String keysTitle = "Current keys";
    public String execute(){
	String ret = SUCCESS;
	String back = doPrepare("serviceKey.action");
	if(action.equals("Save")){
	    back = key.doSave();
	    if(!back.equals("")){
		addError(back);
	    }
	    else{
		id = key.getId();
		addMessage("Added Successfully");
	    }
	}				
	else if(action.startsWith("Save")){
	    back = key.doUpdate();
	    if(!back.equals("")){
		addError(back);								
	    }
	    else{
		id = key.getId();
		addMessage("Updated Successfully");
	    }
	}
	else{		
	    getKey();
	    if(!id.equals("")){
		back = key.doSelect();
		if(!back.equals("")){
		    addError(back);
		}
	    }
	}
	return ret;
    }
    public ServiceKey getKey(){ 
	if(key == null){
	    key = new ServiceKey();
	    key.setId(id);
	}		
	return key;
    }

    public void setKey(ServiceKey val){
	if(val != null){
	    key = val;
	}
    }

    public String getKeysTitle(){
	return keysTitle;
    }
    public void setAction2(String val){
	if(val != null && !val.equals(""))		
	    action = val;
    }
    public boolean hasKeys(){
	getKeys();
	return keys != null && keys.size() > 0;
    }
				
    public List<ServiceKey> getKeys(){
	if(keys == null){
	    ServiceKeyList tl = new ServiceKeyList();
	    tl.setActiveOnly();
	    String back = tl.find();
	    if(back.equals("")){
		List<ServiceKey> ones = tl.getKeys();
		if(ones != null && ones.size() > 0){
		    keys = ones;
		}
	    }
	}
	return keys;
    }

}





































