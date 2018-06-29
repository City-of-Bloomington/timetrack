package in.bloomington.timer;

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

public class TimeBlockAction extends TopAction{

		static final long serialVersionUID = 4300L;
		DecimalFormat dFormat = new DecimalFormat("###.00");
		static Logger logger = LogManager.getLogger(TimeBlockAction.class);
		//
		TimeBlock timeBlock = null;
		String timeBlocksTitle = "Time Block Entry";
		String document_id = "", selected_job_id="";
		String date = "";
		int order_index = 0;
		Employee employee = null;
		Document document = null;
		PayPeriod payPeriod = null;
		List<EmployeeAccrual> employeeAccruals = null;
		JobTask selectedJob = null;		
		List<JobTask> jobTasks = null;
		List<HourCode> hourCodes = null;
		Department department = null;
		//
		public String execute(){
				String ret = SUCCESS;
				String back = doPrepare("timeBlock.action");
				if(!back.equals("")){
						return "login";
				}
				if(action.equals("Save")){
						timeBlock.setAction_by_id(user.getId());
						back = timeBlock.doSave();
						if(!back.equals("")){
								addActionError(back);
						}
						else{
								addActionMessage("Added Successfully");
						}
				}				
				else if(action.startsWith("Save")){
						timeBlock.setAction_by_id(user.getId());
						back = timeBlock.doUpdate();
						if(!back.equals("")){
								addActionError(back);
						}
						else{
								addActionMessage("Updated Successfully");
						}
				}
				else if(action.equals("Delete")){
						getTimeBlock();
						back = timeBlock.doSelect();
						//
						// we need document_id so that when we delete the timeblock
						// we stay on the same payperiod
						//
						document_id = timeBlock.getDocument_id();
						timeBlock.setAction_by_id(user.getId());
						back = timeBlock.doDelete();
						if(!back.equals("")){
								addActionError(back);
						}
						else{
								try{
										HttpServletResponse res = ServletActionContext.getResponse();
										String str = url+"timeDetails.action?document_id"+document_id;
										res.sendRedirect(str);
										return super.execute();
								}catch(Exception ex){
										System.err.println(ex);
								}	
						}
				}
				else{		
						getTimeBlock();
						if(!id.equals("")){
								back = timeBlock.doSelect();
								if(!back.equals("")){
										addActionError(back);
								}
								selected_job_id = timeBlock.getJob_id();
								document_id = timeBlock.getDocument_id();
						}
				}
				return ret;
		}
		public TimeBlock getTimeBlock(){ 
				if(timeBlock == null){
						timeBlock = new TimeBlock();
						if(!id.equals(""))
								timeBlock.setId(id);
						if(!document_id.equals(""))
								timeBlock.setDocument_id(document_id);
						if(!date.equals(""))
								timeBlock.setDate(date);
						timeBlock.setOrder_index(order_index);
				}
				return timeBlock;
		}
		public void setTimeBlock(TimeBlock val){
				if(val != null){
						timeBlock = val;
				}
		}

		public String getTimeBlocksTitle(){
				return timeBlocksTitle;
		}
		public void setAction2(String val){
				if(val != null && !val.equals(""))		
						action = val;
		}
		public void setOrder_index(int val){
				if(val > 0)
						order_index = val;
		}
		public int getOrder_index(){
				if(order_index == 0 && timeBlock != null){
						order_index = timeBlock.getOrder_index();
				}
				return order_index;
		}
		//
		// this is passed through the link
		public String getDocument_id(){
				if(document_id.equals("") && timeBlock != null){
						document_id = timeBlock.getDocument_id();
				}
				return document_id;
		}
		public Document getDocument(){
				if(document == null){
						if(document_id.equals("")){
								getDocument_id();
						}
						if(!document_id.equals("")){
								Document one = new Document(document_id);
								String back = one.doSelect();
								if(back.equals("")){
										document = one;
										employee = document.getEmployee();
										payPeriod = document.getPayPeriod();
										if(employee != null){
												department = employee.getDepartment();
										}
								}
						}
				}
				return document;
		}
		public List<JobTask> getJobTasks(){
				if(jobTasks == null && employee != null){
						JobTaskList jl = new JobTaskList(employee.getId());
						if(payPeriod != null){
								jl.setPay_period_id(payPeriod.getId());
						}
						String back = jl.find();
						if(back.equals("")){
								List<JobTask> ones = jl.getJobTasks();
								if(ones != null && ones.size() > 0){
										jobTasks = ones;
										if(jobTasks.size() > 0){
												selectedJob = jobTasks.get(0); // get one
												selected_job_id = selectedJob.getId();
										}
								}
						}
				}
				return jobTasks;
		}
		public boolean hasMoreThanOneJob(){
				getDocument();
				getJobTasks();
				return jobTasks != null && jobTasks.size() > 1;
		}
		public boolean hasOneJobOnly(){
				getDocument();
				getJobTasks();
				return jobTasks != null && jobTasks.size() == 1;
		}		
		
		public void setDocument_id(String val){
				if(val != null && !val.equals(""))		
						document_id = val;
		}
		public void setDate(String val){
				if(val != null && !val.equals(""))		
						date = val;
		}
		public String getSelected_job_id(){
				return selected_job_id;
		}		
		public List<HourCode> getHourCodes(){
				//
				// hours code are part of finding document
				//
				getDocument();
				getJobTasks();
				findHourCodes();
				return hourCodes;
		}
		void findHourCodes(){
				if(hourCodes == null){
						if(selectedJob != null){
								HourCodeList ecl = new HourCodeList();
								String salary_group_id = selectedJob.getSalary_group_id();
								ecl.setSalary_group_id(salary_group_id);
								if(department != null){
										ecl.setDepartment_id(department.getId());
								}
								ecl.setActiveOnly();
								String back = ecl.lookFor();
								if(back.equals("")){
										List<HourCode> ones = ecl.getHourCodes();
										if(ones != null && ones.size() > 0){
												hourCodes = ones;
										}
								}
						}
				}
		}				
		public boolean hasHourCodes(){
				getHourCodes();
				return hourCodes != null;
		}
		// we know we have document_id, we can use to find
		// employee accruals (if any)
		public List<EmployeeAccrual> getEmpAccruals(){
				if(document == null){
						getDocument();
				}
				if(document.hasAllAccruals()){
						employeeAccruals = document.getEmpAccruals();
				}						
				return employeeAccruals;
		}
		public boolean hasEmpAccruals(){
				getEmpAccruals();
				return employeeAccruals != null && employeeAccruals.size() > 0;
		}

}





































