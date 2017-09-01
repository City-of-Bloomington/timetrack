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
import org.apache.log4j.Logger;
import in.bloomington.timer.list.*;
import in.bloomington.timer.bean.*;

public class TimeBlockAction extends TopAction{

		static final long serialVersionUID = 4300L;
		DecimalFormat dFormat = new DecimalFormat("###.00");
		static Logger logger = Logger.getLogger(TimeBlockAction.class);
		//
		TimeBlock timeBlock = null;
		String timeBlocksTitle = "Time Block Entry";
		String document_id = "", selected_job_id="";
		String date = "";
		Employee employee = null;
		Document document = null;
		PayPeriod payPeriod = null;
		List<EmployeeAccrual> employeeAccruals = null;
		Map<Integer, Double> hourCodeTotals = null;
		JobTask selectedJob = null;		
		List<JobTask> jobTasks = null;
		List<HourCode> hourCodes = null;
		Type department = null;
		//
		public String execute(){
				String ret = SUCCESS;
				String back = doPrepare();
				if(!back.equals("")){
						try{
								HttpServletResponse res = ServletActionContext.getResponse();
								String str = url+"Login";
								res.sendRedirect(str);
								return super.execute();
						}catch(Exception ex){
								System.err.println(ex);
						}	
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
						timeBlock.setAction_by_id(user.getId());
						back = timeBlock.doDelete();
						if(!back.equals("")){
								addActionError(back);
						}
						else{
								try{
										HttpServletResponse res = ServletActionContext.getResponse();
										String str = url+"timeDetails.action";
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
						}

				}
				return ret;
		}
		public TimeBlock getTimeBlock(){ 
				if(timeBlock == null){
						timeBlock = new TimeBlock();
						timeBlock.setId(id);
						timeBlock.setDocument_id(document_id);
						timeBlock.setDate(date);
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
				if(employeeAccruals == null){
						EmployeeAccrualList al = new EmployeeAccrualList();						
						getDocument_id();
						al.setDocument_id(document_id);
						String back = al.find();
						if(back.equals("")){
								List<EmployeeAccrual> ones = al.getEmployeeAccruals();
								if(ones != null && ones.size() > 0){
										employeeAccruals = ones;
								}
						}
						// now we adjust the totals see below
						findHourCodeTotals();
						adjustAccruals();
				}
				return employeeAccruals;
		}
		public boolean hasEmpAccruals(){
				getEmpAccruals();
				return employeeAccruals != null && employeeAccruals.size() > 0;
		}
		public void findHourCodeTotals(){
				if(hourCodeTotals == null){
						TimeBlockList tl = new TimeBlockList();
						tl.setActiveOnly();
						if(document_id.equals("")){
								getDocument_id();
						}
						tl.setDocument_id(document_id);
						String back = tl.find();
						if(back.equals("")){
								hourCodeTotals = tl.getHourCodeTotals();
						}
				}
		}
		//
		// we need to adjust accruals according to hour code use totals
		// so that the balance is what will be available to use
		//
		public void adjustAccruals(){
				if(employeeAccruals != null && hourCodeTotals != null){
						for(EmployeeAccrual one: employeeAccruals){
								String related_id = one.getRelated_hour_code_id();
								if(related_id != null && !related_id.equals("")){
										try{
												int cd_id = Integer.parseInt(related_id);
												if(hourCodeTotals.containsKey(cd_id)){
														double hrs_used = hourCodeTotals.get(cd_id);
														double hrs_total = one.getHours();
														// System.err.println(" total used "+hrs_total+" "+hrs_used);
														if(hrs_total > hrs_used){
																hrs_total = hrs_total - hrs_used;
														}
														else
																hrs_total = 0.0;
														one.setHours(hrs_total);
												}
										}catch(Exception ex){

										}
								}
						}
				}
		}		

}





































