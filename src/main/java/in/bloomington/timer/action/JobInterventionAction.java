package in.bloomington.timer.action;
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
import in.bloomington.timer.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JobInterventionAction extends TopAction{

		static final long serialVersionUID = 3800L;	
		static Logger logger = LogManager.getLogger(JobInterventionAction.class);
		//
		String jobsTitle = "New World Job Titles";
		List<String> jobTitles = null;
		Hashtable<Employee, Set<JobTask>> empJobCanDelete = null;
		Hashtable<Employee, Set<JobTask>> empJobNeedUpdate = null;		
		Hashtable<Employee, Set<JobTask>> empNotInNW = null;
		String pay_period_id = "";
		PayPeriod payPeriod = null;
		String[] del_jobs = null;
		String[] exp_jobs = null;		
		String[] exp_emps = null;		
		public String execute(){
				String ret = SUCCESS;
				String back = doPrepare();
				if(!action.isEmpty()){
						JobTask job = new JobTask();
						// do delete or update here
						if(del_jobs != null){
								back = job.doDeleteJobAndDoc(del_jobs);
								if(!back.isEmpty()){
										addError(back);
								}
						}
						if(exp_jobs != null){
								getPayPeriod();
								String date = payPeriod.getEnd_date();
								System.err.println(" date "+date);
								back = job.doExpireJobs(exp_jobs, date);
								if(!back.isEmpty()){
										addError(back);
								}
						}
						if(exp_emps != null){
								getPayPeriod();
								String date = payPeriod.getEnd_date();
								List<String> job_ids = new ArrayList<>();
								for(String emp_id:exp_emps){
										Employee emp = new Employee(emp_id);
										emp.setPay_period_id(pay_period_id);
										List<GroupEmployee> grpEmps = emp.getGroupEmployees();
										List<JobTask> jobs = emp.getJobs();
										DepartmentEmployee de = emp.getDepartmentEmployee();
										if(de != null){
												de.setExpire_date(date);
												back = de.doUpdate();
										}
										for(GroupEmployee ge:grpEmps){
												ge.setExpire_date(date);
												back += ge.doUpdate();
										}
										for(JobTask jj:jobs){
												jj.setExpire_date(date);
												back += jj.doUpdate();
										}
								}
								System.err.println(" date "+date);

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
				if(val != null && !val.isEmpty())		
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
    public PayPeriod getPayPeriod(){
				//
				if(payPeriod == null){
						if(!pay_period_id.isEmpty()){
								PayPeriod pp = new PayPeriod(pay_period_id);
								String back = pp.doSelect();
								if(back.isEmpty()){
										payPeriod = pp;
										pay_period_id = payPeriod.getId();
								}
						}
						else{
								PayPeriodList ppl = new PayPeriodList();
								ppl.setLastPayPeriod(); 
								String back = ppl.find();
								if(back.isEmpty()){
										List<PayPeriod> ones = ppl.getPeriods();
										if(ones != null && ones.size() > 0){
												payPeriod = ones.get(0);
										}
								}
						}
				}
				return payPeriod;
    }		
		
}





































