package in.bloomington.timer;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InformAction extends TopAction{

		static final long serialVersionUID = 1160L;	
		static Logger logger = LogManager.getLogger(InformAction.class);
		//
		String pay_period_id="", text_message="", subject="";
		String type="", inform_type="", email_cc="";
		PayPeriod payPeriod = null;
		List<Employee> employees = null;
		String employee_ids = null;
		private static final Map<String, String> typeMap = new HashMap<>();
    static {
				typeMap.put("noEntry","Timesheet Submit Reminder");
				typeMap.put("noSubmit","Timesheet Submit Reminder");
				typeMap.put("noApprove","Timesheet Approve Reminder");
    }		
		private static final Map<String, String> messageMap = new HashMap<>();
    static {
				messageMap.put("noEntry","Quick reminder: Please submit your timesheet for the last pay period when you have a moment. The Timetrack system is available here: ");
				messageMap.put("noSubmit","Quick reminder: Please submit your timesheet for the last pay period when you have a moment. The Timetrack system is available here: ");
				messageMap.put("noApprove","Quick reminder: When you have a moment, please review and approve time sheets for your direct reports for the last pay period . The Timetrack system is available here: ");

    }
		private static final Map<String, String> subjectMap = new HashMap<>();
    static {
				subjectMap.put("noEntry","Timesheet Submit Reminder");
				subjectMap.put("noSubmit","Timesheet Submit Reminder");
				subjectMap.put("noApprove","Timesheet Approval Reminder");
    }		
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
				clearAll();
				if(action.equals("Send")){
						if(employee_ids != null){
								/*
								for(String doc_id:document_ids){
										TimeAction one =
												new TimeAction(CommonInc.default_approve_workflow_id,
																			 doc_id,
																			 user.getId());
										back = one.doSave();
										if(!back.equals("")){
												if(!back.equals("")){
														addActionError(back);
														addError(back);
												}
										}
								}
								if(!hasErrors()){
										addMessage("Approved successfully");
								}
								*/
						}
				}
				return ret;
		}

		public void setAction2(String val){
				if(val != null && !val.equals(""))		
						action = val;
		}
		public void setEmployee_ids(String val){
				if(val != null && !val.equals(""))		
					 employee_ids = val;
		}
		public void setType(String val){
				if(val != null && !val.equals("")){		
						type = val;
						if(typeMap.containsKey(type)){
								inform_type = typeMap.get(type);
						}
				}
		}
		public void setInform_type(String val){
				if(val != null && !val.equals("")){
						inform_type = val;
				}
		}		
		public void setEmail_cc(String val){
				if(val != null && !val.equals("-1"))		
						email_cc = val;
		}		
		public void setSubject(String val){
				if(val != null && !val.equals(""))		
						subject = val;
		}
		public void setText_message(String val){
				if(val != null && !val.equals(""))		
						text_message = val;
		}		
		public void setPay_period_id(String val){
				if(val != null && !val.equals("-1"))		
						pay_period_id = val;
		}
		public String getInform_type(){
				if(inform_type.equals("") && !type.equals("")){
						if(typeMap.containsKey(type)){
								inform_type = typeMap.get(type);
						}
				}
				return inform_type;
		}		
		public String getSubject(){
				return subject;
		}
		
		public String getText_message(){
				if(text_message.equals("")){
						text_message = "Dear employees(s) \n\n";
						if(!type.equals("")){
								if(messageMap.containsKey(type)){
										text_message += messageMap.get(type)+"\n\n "+url+"\n\n";
								}
						}
						text_message += "Thanks your \n";
				}
				return text_message;
		}		
		public PayPeriod getPayPeriod(){
				//
				if(payPeriod == null){
						if(!pay_period_id.equals("")){
								PayPeriod one = new PayPeriod(pay_period_id);
								String back = one.doSelect();
								if(back.equals(""))
										payPeriod = one;
						}
						else {
								getCurrentPayPeriod();
						}
				}
				return payPeriod;
		}		
		public PayPeriod getCurrentPayPeriod(){
				//
				if(payPeriod == null){
						PayPeriodList ppl = new PayPeriodList();
						ppl.currentOnly();
						String back = ppl.find();
						if(back.equals("")){
								List<PayPeriod> ones = ppl.getPeriods();
								if(ones != null && ones.size() > 0){
										payPeriod = ones.get(0);
										if(pay_period_id.equals("")){
												pay_period_id = payPeriod.getId();
										}
								}
						}
				}
				return payPeriod;
		}
		public List<Employee> getEmployees(){
				if(employees == null && employee_ids != null){
						String[] emp_arr = null;
						try{
								emp_arr = employee_ids.split("_");

						}catch(Exception ex){
								System.err.println(ex);
						}
						String emp_set = "";
						if(emp_arr != null){
								for(String str:emp_arr){
										if(!emp_set.equals("")) emp_set += ","; 
										emp_set += str;
								}
						}
						EmployeeList empl = new EmployeeList();
						if(!emp_set.equals("")){
								empl.setEmployee_ids(emp_set);
						}
						String back = empl.find();
						if(back.equals("")){
								List<Employee> ones = empl.getEmployees();
								if(ones != null && ones.size() > 0){
										employees = ones;
								}
						}
				}
				return employees;
		}
		public boolean hasEmployees(){
				getEmployees();
				return employees != null && employees.size() > 0; 
		}

}





































