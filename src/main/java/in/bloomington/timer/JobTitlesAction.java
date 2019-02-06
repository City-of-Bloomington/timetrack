package in.bloomington.timer;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.*;
import java.util.Map;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;  
import in.bloomington.timer.list.*;
import in.bloomington.timer.bean.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JobTitlesAction extends TopAction{

		static final long serialVersionUID = 3800L;	
		static Logger logger = LogManager.getLogger(JobTitlesAction.class);
		//
		String jobsTitle = "New World Job Titles";
		List<String> jobTitles = null;
		Hashtable<Employee, Set<JobTask>> empJobCanDelete = null;
		Hashtable<Employee, Set<JobTask>> empJobNeedUpdate = null;		
		Hashtable<Employee, Set<JobTask>> empNotInNW = null;
		String[] del_jobs = null;
		String[] exp_jobs = null;		
		String[] exp_emps = null;		
		public String execute(){
				String ret = SUCCESS;
				String back = doPrepare();
				if(!action.equals("")){
						// do delete or update here
						if(del_jobs != null){
								for(String jb:del_jobs){
										JobTask job = new JobTask(jb);
										back = job.doDeleteJobAndDoc();
										System.err.println(back+" del "+jb);
								}
						}
				}						
				HandleJobTitleUpdate hjtl = new HandleJobTitleUpdate();
				back = hjtl.process();
				empJobCanDelete = hjtl.getEmpJobCanDelete();
				empJobNeedUpdate = hjtl.getEmpJobNeedUpdate();
				empNotInNW = hjtl.getEmpNotInNW();
				// System.err.println(" emp not nw "+empNotInNW.size());
				return ret;
		}
		// del jobs not in NW and not used
    public void setDel_jobs(String[] vals){
				if(vals != null){		
						del_jobs = vals;
				}
    }
		// certain jobs are not in NW but are used
    public void setExp_jobs(String[] vals){
				if(vals != null){		
						exp_jobs = vals;
				}
    }
		// emp not in NW 
    public void setExp_emps(String[] vals){
				if(vals != null){		
						exp_emps = vals;
				}
    }		
		public String getJobsTitle(){
				return jobsTitle;
		}

		public void setAction2(String val){
				if(val != null && !val.equals(""))		
						action = val;
		}
		public List<String> getJobTitles(){
				return jobTitles;
		}
		public boolean hasJobTitles(){
				return jobTitles != null && jobTitles.size() > 0;
		}
		public Hashtable<Employee, Set<JobTask>> getEmpJobCanDelete(){
				return empJobCanDelete;
		}
		public Hashtable<Employee, Set<JobTask>> getEmpJobNeedUpdate(){
				return empJobNeedUpdate;
		}		
		public Hashtable<Employee, Set<JobTask>> getEmpNotFoundNewWorld(){
				return empNotInNW;
		}

		public boolean hasEmpNotFoundNewWorld(){
				return empNotInNW != null && !empNotInNW.isEmpty();
		}
		public boolean hasEmpJobCanDelete(){
				return empJobCanDelete != null && !empJobCanDelete.isEmpty();
		}
		public boolean hasEmpJobNeedUpdate(){
				return empJobCanDelete != null && !empJobNeedUpdate.isEmpty();
		}				
		
}





































