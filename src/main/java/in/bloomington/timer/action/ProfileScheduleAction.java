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
import in.bloomington.timer.util.*;
import in.bloomington.timer.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProfileScheduleAction extends TopAction{

    static final long serialVersionUID = 3850L;	
    static Logger logger = LogManager.getLogger(ProfileScheduleAction.class);
    //
    List<Department> depts = null;
    String profileSchedulesTitle = "Employees Profile Schedules";
    QuartzMisc quartzMisc = null;
    ProfileScheduler schedular = null;
    String date = "", prev_date="", next_date="", dept_ref_id="";
    public String execute(){
	String ret = SUCCESS;
	String back = canProceed("profileSchedule.action");
	if(!back.isEmpty()){
	    return back;
	}
	prepareSchedular();				
	if(action.equals("Schedule")){
	    back = doClean();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    try{
		back = schedular.run();
		if(!back.isEmpty()){
		    addError(back);
		}
		else{
		    addMessage("Scheduled Successfully");
		}
	    }catch(Exception ex){
		addActionError(""+ex);
	    }
	}
	else if(action.startsWith("Update")){ // import now given the date
	    if(dept_ref_id.isEmpty() || date.isEmpty()){
		addError("dept ref and/or date not set");
	    }
	    else{
		HandleProfile handle = new HandleProfile(dept_ref_id, date);
		back = handle.process();
		if(!back.isEmpty()){
		    System.err.println(" error "+back);
		    addError(back);
		}
		else{
		    addMessage("Update Successfully");
		}
	    }
	}
	else if(!action.isEmpty()){ // looking for one employee
	    HandleProfile handle = new HandleProfile(date);
	    // sibo: 100001341
	    // burns: 100003240 //police
	    // andrews 100001038 //fire
	    // washel 100001036 //fire mgt
	    // union dilman 100003659
	    // parks tem 859
	    // back = handle.processOne("859");
	    int[] arr = {100001341,100003240, 100001038, 100001036,100003659, 859, 756, 1633};
	    for(int jj:arr){
		back = handle.processJobs(""+jj);
	    }
	    back = handle.processJobs("2126","10/07/2019");
	    back = handle.processJobs("2348","09/27/2019");						
	    if(!back.isEmpty()){
		addActionError(back);
	    }
	    else{
		addActionMessage("found Successfully");
	    }
	}
	return ret;
    }
    private void prepareSchedular(){
	String msg = "";
	PayPeriodList pl = new PayPeriodList();
	if(date.isEmpty()){
	    pl.setLastPayPeriod();
	    msg = pl.find();
	    if(msg.isEmpty()){
		List<PayPeriod> ones = pl.getPeriods();
		if(ones != null && ones.size() > 0){
		    PayPeriod one = ones.get(0);
		    String end_date = one.getEnd_date();
		    date = Helper.getDateAfter(end_date, 5);
		}
	    }
	}
	if(!date.isEmpty()){
	    schedular = new ProfileScheduler(date);
	}
	quartzMisc = new QuartzMisc("profile"); 
	msg = quartzMisc.findScheduledDates();
	if(msg.isEmpty()){
	    prev_date = quartzMisc.getPrevScheduleDate();
	    if(prev_date.startsWith("1969")) // 0 cuases 1969 schedule date
		prev_date = "No Previous date found";
	    next_date = quartzMisc.getNextScheduleDate();
	}				
    }
    private String doClean(){
	String msg = "";
	if(quartzMisc != null){
	    msg = quartzMisc.doClean();
	}
	return msg;
    }
    public String getProfileSchedularsTitle(){
				
	return profileSchedulesTitle;
    }


    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
	    action = val;
    }
    public void setDate(String val){
	if(val != null && !val.isEmpty())		
	    date = val;
    }
    public void setDept_ref_id(String val){
	if(val != null && !val.equals("-1"))		
	    dept_ref_id = val;
    }		
    // read only 
    public String getDate(){
	return date;
    }
    public String getPrev_date(){
	return prev_date;
    }
    public String getNext_date(){
	return next_date;
    }
    public boolean hasPrevDates(){
	return !prev_date.isEmpty();
    }
    public List<Department> getDepts(){
	if(depts == null){
	    DepartmentList dl = new DepartmentList();
	    dl.setActiveOnly();
	    dl.hasRefIds();
	    String msg = dl.find();
	    if(!msg.isEmpty()){
		logger.error(msg);
	    }
	    else{
		List<Department> ones = dl.getDepartments();
		if(ones != null && ones.size() > 0){
		    depts = ones;
		}
	    }
	}
	return depts;
    }
    public boolean hasDepts(){
	getDepts();
	return depts != null && depts.size() > 0;
    }
		
}





































