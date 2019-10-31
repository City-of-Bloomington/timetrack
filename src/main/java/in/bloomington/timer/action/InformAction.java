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
import java.util.Set;
import java.util.HashSet;
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
		String date_from="", date_to="", pageSize="50";
		String source = "";
		PayPeriod payPeriod = null;
		List<Employee> employees = null;
		List<GroupManager> managers = null;
		List<EmailLog> emailLogs = null;
		String employee_ids = null, group_ids=null;
		private static final Map<String, String> typeMap = new HashMap<>();
    static {
				typeMap.put("noSubmit","Timesheet Submit Reminder");
				typeMap.put("noApprove","Timesheet Approve Reminder");
    }		
		private static final Map<String, String> messageMap = new HashMap<>();
    static {
				messageMap.put("noSubmit","Quick reminder: Please submit your timesheet for the last pay period when you have a moment. The Timetrack system is available here: ");
				messageMap.put("noApprove","Quick reminder: When you have a moment, please review and approve time sheets for your direct reports for the last pay period. The Timetrack system is available here: ");

    }
		private static final Map<String, String> subjectMap = new HashMap<>();
    static {
				subjectMap.put("noSubmit","Timesheet Submit Reminder");
				subjectMap.put("noApprove","Timesheet Approval Reminder");
    }
		private Map<Employee, List<Group>> managerMap = null;
		public String execute(){
				String ret = SUCCESS;
				String back = doPrepare();
				if(action.equals("logs")){
						ret = "logs";
				}
				else if(action.equals("Find")){
						ret = "logs";
				}
				else if(action.equals("Send")){
						if(employee_ids != null){
								String to = "", email_from="";
								getEmployee();
								if(employee != null){
										email_from = employee.getEmail();
								}
								getEmployees();
								if(employees != null){
										for(Employee one:employees){
												to = one.getEmail();
												MailHandle mail =
														new MailHandle(mail_host,
																					 to, 
																					 email_from,
																					 email_cc,
																					 null, // bcc
																					 subject,
																					 text_message,
																					 debug
																					 );
												//
												if(activeMail){
														back = mail.send();
												}
												else{
														back = "email activity flag is turned off, if you need to send email this flag need to be turned on in your configuration file";
												}
												if(!back.equals("")){
														addError(back);
												}
												else{
														addMessage("Email send successfully");
														ret = "informSuccess";
												}
												EmailLog elog = new EmailLog(debug,
																										 user.getId(),
																										 email_from,
																										 to, // to
																										 email_cc,
																										 null,
																										 subject,
																										 text_message,
																										 back,
																										 type.equals("noSubmit")?"Approvers":"Processors");
												back = elog.doSave();
										}
								}
						}
				}
				else{
						if(employee_ids != null){
								getEmployees();
						}
						else if(group_ids != null){
								findManagers();
						}
				}
				return ret;
		}

		public void setAction2(String val){
				if(val != null && !val.equals(""))		
						action = val;
		}
		public void setEmployee_ids(String val){
				if(val != null && !val.equals("")){		
					 employee_ids = val;
				}
		}
		public void setGroup_ids(String val){
				if(val != null && !val.equals(""))		
					 group_ids = val;
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
		public void setSource(String val){
				if(val != null && !val.equals(""))		
						source = val;
		}
		public void setDate_from(String val){
				if(val != null && !val.equals(""))		
						date_from = val;
		}
		public void setDate_to(String val){
				if(val != null && !val.equals(""))		
						date_to = val;
		}
		public void setPageSize(String val){
				if(val != null && !val.equals(""))		
						pageSize = val;
		}
		public String getPageSize(){
				return pageSize;
		}
		public String getDate_from(){
				return date_from;
		}
		public String getDate_to(){
				return date_to;
		}		
		public void setApprove(String val){
				// ignore
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
		public String getType(){
				return type;
		}		
		public String getSubject(){
				if(subject.equals("") && !type.equals("")){
						if(subjectMap.containsKey(type)){
								subject = subjectMap.get(type);
						}						
				}
				return subject;
		}
		//
		// approvers only
		//
		void findManagers(){
				if(managers == null && group_ids != null){
						GroupManagerList gml = new GroupManagerList();
						gml.setApproversOnly();
						gml.setPay_period_id(pay_period_id);
						gml.execludeManager_id(employee_id);
						Set<String> grp_set = new HashSet<>();
						String[] id_arr = null;
						try{
								id_arr = group_ids.split("_");
								if(id_arr != null && id_arr.length > 0){
										for(String str:id_arr){
												gml.addGroup_id(str);
												grp_set.add(str);
										}
								}
								String back = gml.find();
								if(back.equals("")){
										List<GroupManager> ones = gml.getManagers();
										if(ones != null && ones.size() > 0){
												managers = ones;
												managerMap = new HashMap<>();
												for(GroupManager one:managers){
														Employee emp = one.getEmployee();
														Group grp = one.getGroup();
														//
														// we pick only groups in the set
														//
														if(grp_set.contains(grp.getId())){ 
																if(managerMap.containsKey(emp)){
																		List<Group> lst = managerMap.get(emp);
																		lst.add(grp);
																		managerMap.put(emp, lst);
																}
																else{
																		List<Group> lst = new ArrayList<>();
																		lst.add(grp);
																		managerMap.put(emp, lst);
																}
														}
												}
										}
								}
								else {
										addError(back);
								}
						}catch(Exception ex){
								addError(""+ex);
						}
				}
		}
		public boolean hasManagers(){
				findManagers();
				return managers != null && managers.size() > 0;
		}
		public List<GroupManager> getManagers(){
				return managers;
		}
		public Map<Employee, List<Group>> getManagerMap(){
				return managerMap;
		}
		public String getPageTitle(){
				if(type.equals("noApprove"))
						return "Remind Approvers";
				return "Remind Employees";
		}
		public String getText_message(){
				if(text_message.equals("")){
						text_message = "\n";
						if(!type.equals("")){
								if(messageMap.containsKey(type)){
										text_message += messageMap.get(type)+"\n\n "+proxy_url+"\n\n";
								}
						}
						text_message += "Thank you\n\n";
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
				if(employees == null &&
					 employee_ids != null &&
					 !employee_ids.equals("")){
						String emp_set = "";
						try{
								if(employee_ids.indexOf("_") > -1){
										emp_set = employee_ids.replace("_",",");
								}
								else if(employee_ids.indexOf(",") > -1)
										emp_set = employee_ids;
								else{
										emp_set = employee_ids;
								}
						}catch(Exception ex){
								System.err.println(ex);
						}
						EmployeeList empl = new EmployeeList();
						if(!emp_set.equals("")){
								empl.setEmployee_ids(emp_set);
								String back = empl.find();
								if(back.equals("")){
										List<Employee> ones = empl.getEmployees();
										if(employees == null)
												employees = new ArrayList<>();
										for(Employee one:ones){
												if(one.canReceiveEmail()){
														if(!employees.contains(one)){
																employees.add(one);
														}
												}
										}
								}
						}
				}
				return employees;
		}
		public boolean hasEmployees(){
				getEmployees();
				return employees != null && employees.size() > 0; 
		}
		public List<EmailLog> getEmailLogs(){
				if(emailLogs == null){
						EmailLogList ell = new EmailLogList(debug);
						ell.setDate_from(date_from);
						ell.setDate_to(date_to);
						ell.setPageSize(pageSize);
						String back = ell.find();
						if(back.equals("")){
								List<EmailLog> ones = ell.getEmailLogs();
								if(ones != null &&  ones.size() > 0){
										emailLogs = ones;
								}
						}
				}
				return emailLogs;
		}
		public boolean hasEmailLogs(){
				getEmailLogs();
				return emailLogs != null && emailLogs.size() > 0;
		}
}







































