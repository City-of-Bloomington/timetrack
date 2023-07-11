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

public class TerminateGroupAction extends TopAction{

    static final long serialVersionUID = 1150L;	
    static Logger logger = LogManager.getLogger(TerminateGroupAction.class);
    //
    String department_id="", group_id="", pay_period_id="",
	expire_date="", source="";
    Group group = null;
    GroupJobTerminate term = null;
    List<Document> documents = null;
    List<PayPeriod> payPeriods = null;    
    CleanUp cleanUp = null;
    String terminateTitle = "Group Termination Wizard";
    public String execute(){
	String ret = SUCCESS;
	String back = doPrepare("terminateGroup.action");
	if(!action.isEmpty()){ // normally 'Submit'
	    if(!group_id.isEmpty()){
		getGroup();
	    }
	    if(group == null){
		back = "could not get group info ";
		addError(back);
	    }
	    getTerm();
	    back = term.doTerminate();
	    if(!back.isEmpty()){
		back = "could not get employee info ";
		addError(back);
	    }
	    else{
		addMessage("Terminated successfully");
		if(hasDocuments()){
		    getCleanUp();
		    back = cleanUp.doClean();
		    if(back.isEmpty()){
			addMessage("Cleanup Success");
		    }
		    else{
			addError(back);
		    }
		}
		else{
		    addMessage("No document found for cleanup");
		}
	    }
	}
	else {
	    getUser();
	    if(user != null && !(user.isAdmin() || user.isHrAdmin())){
		if(user.hasDepartment()){
		    department_id = user.getDepartment_id();
		}
	    }
	}
	if(!group_id.isEmpty()){
	    getGroup();
	}
	return ret;
    }
    public GroupJobTerminate getTerm(){
	if(term == null){
	    term = new GroupJobTerminate(group_id, expire_date);
	}
	return term;
    }
    public void setTerm(GroupJobTerminate val){
	if(val != null){
	    term = val;
	    term.setGroup_id(group_id);
	    term.setExpire_date(expire_date);
	}
    }
    public void setCleanUp(CleanUp val){
	if(val != null)
	    cleanUp = val;
    }
    public CleanUp getCleanUp(){
	cleanUp = new CleanUp();
	if(hasDocuments()){
	    cleanUp.setDocuments(documents);
	}
	return cleanUp;
    }
    void findDocuments(){
	//
	if(!expire_date.isEmpty()){
	    PayPeriod pp = new PayPeriod();
	    if(pp.findByEndDate(expire_date)){
		pay_period_id = pp.getId();
	    }
	    else{
		System.err.println(" could not find pay_period_id for "+expire_date);
	    }
	}
	if(documents == null
	   && !group_id.isEmpty()
	   && !pay_period_id.isEmpty()){
	    DocumentList dl = new DocumentList();
	    dl.setGroup_id(group_id);
	    dl.setPay_period_id(pay_period_id);
	    String back = dl.findForCleanUp();
	    if(back.isEmpty()){
		List<Document> ones = dl.getDocuments();
		if(ones != null && ones.size() > 0){
		    documents = ones;
		}
	    }
	}
    }
    boolean hasDocuments(){
	findDocuments();
	return documents != null && documents.size() > 0;
    }		
    public String getTermiateTitle(){
	return terminateTitle;
    }
    public String getDepartment_id(){
	return department_id;
    }
    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
	    action = val;
    }
    public boolean hasGroupId(){
	return !group_id.isEmpty();
    }
    public void setGroup_id(String val){
	if(val != null && !val.isEmpty())		
	    group_id = val;
    }
    public Group getGroup(){
	if(group == null && !group_id.isEmpty()){
	    Group one = new Group(group_id);
	    String back = one.doSelect();
	    if(back.isEmpty()){
		group = one;
	    }
	    else{
		addError("No group found for "+group_id);
	    }
	}
	return group;
    }
    public String getGroup_id(){
	return group_id;
    }
    public boolean hasGroup(){
	getGroup();
	return group != null;
    }
				
    public void setDepartment_id(String val){
	if(val != null && !val.isEmpty())		
	    department_id = val;
    }		
    public void setSource(String val){
	if(val != null && !val.isEmpty())		
	    source = val;
    }		
    public String getExpire_date(){
	return expire_date;
    }
    public void setExpire_date(String val){
	if(val != null && !val.equals("-1"))
	    expire_date = val;
    }
    public List<PayPeriod> getPayPeriods(){
	if(payPeriods == null){
	    PayPeriodList tl = new PayPeriodList();
	    tl.setAheadPeriods(4);
	    tl.setLimit("10");
	    String back = tl.find();
	    if(back.isEmpty()){
		List<PayPeriod> ones = tl.getPeriods();
		if(ones != null && ones.size() > 0){
		    payPeriods = ones;
		}
	    }
	}
	return payPeriods;
    }		
}





































