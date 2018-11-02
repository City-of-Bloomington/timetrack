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
import in.bloomington.timer.leave.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LeaveBlockAction extends TopAction{

		static final long serialVersionUID = 4300L;
		DecimalFormat dFormat = new DecimalFormat("###.00");
		static Logger logger = LogManager.getLogger(LeaveBlockAction.class);
		//
		LeaveBlock leaveBlock = null;
		String leaveBlocksTitle = "Leave Block Entry";
		String document_id = "";
		String date = "";
		int order_index = 0;
		Employee employee = null;
		LeaveDocument document = null;
		PayPeriod payPeriod = null;
		List<EmployeeAccrual> employeeAccruals = null;
		// JobTask selectedJob = null;		
		// List<JobTask> jobs = null;
		List<HourCode> hourCodes = null;
		Department department = null;
		//
		public String execute(){
				String ret = SUCCESS;
				String back = doPrepare("leaveBlock.action");
				if(!back.equals("")){
						return "login";
				}
				clearAll();
				if(action.equals("Save")){
						/*
						if(leaveBlock.areAllTimesSet()){
								leaveBlock.setAction_by_id(user.getId());
								back = leaveBlock.doSave();
								if(!back.equals("")){
										addError(back);
										return "json";
								}
								else{
										addMessage("Added Successfully");
								}
						}
						else{
								back = "All fields are required";
								addError(back);
								return "json";								
						}
						*/
				}				
				else if(action.startsWith("Save")){
						/*
						if(leaveBlock.areAllTimesSet()){						
								leaveBlock.setAction_by_id(user.getId());
								back = leaveBlock.doUpdate();
								if(!back.equals("")){
										addError(back);								
										return "json";
								}
								else{
										addMessage("Updated Successfully");								
								}
						}
						else{
								back = "All fields are required";
								addError(back);
								return "json";	
						}
						*/
				}
				else if(action.equals("Delete")){
						getLeaveBlock();
						back = leaveBlock.doSelect();
						//
						// we need document_id so that when we delete the timeblock
						// we stay on the same payperiod
						//
						document_id = leaveBlock.getDocument_id();
						leaveBlock.setInactive(true);
						leaveBlock.setChange_by(user.getId());
						back = leaveBlock.doPartialUpdate();
						if(!back.equals("")){
								addActionError(back);
								addError(back);
						}
						else{
								try{
										HttpServletResponse res = ServletActionContext.getResponse();
										String str = url+"leaveDetails.action?document_id="+document_id;
										res.sendRedirect(str);
										return super.execute();
								}catch(Exception ex){
										System.err.println(ex);
								}	
						}
				}
				else{		
						getLeaveBlock();
						if(!id.equals("")){
								back = leaveBlock.doSelect();
								if(!back.equals("")){
										addActionError(back);
										addError(back);
								}
								// selected_job_id = leaveBlock.getJob_id();
								document_id = leaveBlock.getDocument_id();
						}
				}
				return ret;
		}
		public LeaveBlock getLeaveBlock(){ 
				if(leaveBlock == null){
						leaveBlock = new LeaveBlock();
						if(!id.equals(""))
								leaveBlock.setId(id);
						if(!document_id.equals(""))
								leaveBlock.setDocument_id(document_id);
						if(!date.equals(""))
								leaveBlock.setDate(date);
						leaveBlock.setOrderIndex(order_index);
				}
				return leaveBlock;
		}
		public void setLeaveBlock(LeaveBlock val){
				if(val != null){
						leaveBlock = val;
				}
		}

		public String getLeaveBlocksTitle(){
				return leaveBlocksTitle;
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
				if(order_index == 0 && leaveBlock != null){
						order_index = leaveBlock.getOrderIndex();
				}
				return order_index;
		}
		public void setErrors(String val){
				// do nothing
		}
		//
		// this is passed through the link
		public String getDocument_id(){
				if(document_id.equals("") && leaveBlock != null){
						document_id = leaveBlock.getDocument_id();
				}
				return document_id;
		}
		public LeaveDocument getDocument(){
				if(document == null){
						if(document_id.equals("")){
								getDocument_id();
						}
						if(!document_id.equals("")){
								LeaveDocument one = new LeaveDocument(document_id);
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
		/*
		public List<JobTask> getJobs(){
				if(jobs == null && employee != null){
						JobTaskList jl = new JobTaskList(employee.getId());
						if(payPeriod != null){
								jl.setPay_period_id(payPeriod.getId());
						}
						String back = jl.find();
						if(back.equals("")){
								List<JobTask> ones = jl.getJobTasks();
								if(ones != null && ones.size() > 0){
										jobs = ones;
										if(jobs.size() > 0){
												if(selected_job_id.equals("")){
														for(JobTask one:jobs){
																if(one.isPrimary()){
																		selectedJob = one; // get one
																		selected_job_id = selectedJob.getId();
																		break;
																}
														}
														if(selectedJob == null){
																selectedJob = jobs.get(0);
																selected_job_id = selectedJob.getId();
														}
												}
										}
								}
						}
				}
				return jobs;
		 }
		public boolean hasMoreThanOneJob(){
				getDocument();
				getJobs();
				return jobs != null && jobs.size() > 1;
		}
		public boolean hasOneJobOnly(){
				getDocument();
				getJobs();
				return jobs != null && jobs.size() == 1;
		}		
		*/
		
		public void setDocument_id(String val){
				if(val != null && !val.equals(""))		
						document_id = val;
		}
		public void setDate(String val){
				if(val != null && !val.equals(""))		
						date = val;
		}
		/*
		public String getSelected_job_id(){
				return selected_job_id;
		}
		*/
		public List<HourCode> getHourCodes(){
				//
				// hours code are part of finding document
				//
				getDocument();
				// getJobs();
				findHourCodes();
				return hourCodes;
		}
		void findHourCodes(){
				if(hourCodes == null){
						//	if(selectedJob != null){
						getDocument();
						if(document != null){
								HourCodeList ecl = new HourCodeList();
								String salary_group_id = document.getJob().getSalary_group_id();
								ecl.setSalary_group_id(salary_group_id);
								if(department != null){
										ecl.setDepartment_id(department.getId());
								}
								ecl.setActiveOnly();
								// hour codes that can be usef for leave only
								ecl.relatedToAccrualsOnly();
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
				// ToDo
				/*
				if(document.hasAllAccruals()){
						employeeAccruals = document.getEmpAccruals();
				}
				*/
				return employeeAccruals;
		}
		public boolean hasEmpAccruals(){
				getEmpAccruals();
				return employeeAccruals != null && employeeAccruals.size() > 0;
		}

}





































