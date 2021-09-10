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

public class TerminateAction extends TopAction{

		static final long serialVersionUID = 1150L;	
		static Logger logger = LogManager.getLogger(TerminateAction.class);
		//
		String emp_id = "", pay_period_id="",
				full_name="",
				department_id="",
				expire_date="", source="";
		List<Document> documents = null;
		Employee emp = null;
		EmpTerminate term = null;
		CleanUp cleanUp = null;
		List<PayPeriod> payPeriods = null;
		
		String terminateTitle = "Employee Termination Wizard";
		public String execute(){
				String ret = SUCCESS;
				String back = doPrepare("terminate.action");
				if(!action.isEmpty()){ // normally 'Submit'
						if(!emp_id.isEmpty()){
								getEmp();
								if(emp == null){
										back = "could not get employee info ";
										addError(back);
								}
								else{
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
				if(!emp_id.isEmpty()){
						getEmp();
				}
				return ret;
		}
		public EmpTerminate getTerm(){
				if(term == null){
						term = new EmpTerminate(emp_id, expire_date);
				}
				return term;
		}
		public void setTerm(EmpTerminate val){
				if(val != null){
						term = val;
						term.setId(emp_id);
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
					 && !emp_id.isEmpty()
					 && !pay_period_id.isEmpty()){
						DocumentList dl = new DocumentList();
						dl.setEmployee_id(emp_id);
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
		public boolean hasEmpId(){
				return !emp_id.isEmpty();
		}
		public void setEmp_id(String val){
				if(val != null && !val.isEmpty())		
						emp_id = val;
		}
		public void setFull_name(String val){
				// for auto complete
				if(val != null)
						full_name = val;
		}
		public Employee getEmp(){
				if(emp == null && !emp_id.isEmpty()){
						Employee one = new Employee(emp_id);
						String back = one.doSelect();
						if(back.isEmpty()){
								emp = one;
						}
						else{
								addError("No employee found for "+emp_id);
						}
				}
				return emp;
		}
		public String getEmp_id(){
				return emp_id;
		}
		public boolean hasEmp(){
				getEmp();
				return emp != null;
		}
				
		public void setDepartment_id(String val){
				if(val != null && !val.isEmpty())		
						department_id = val;
		}		
		public void setSource(String val){
				if(val != null && !val.isEmpty())		
					 source = val;
		}		
		public String getFull_name(){
				if(full_name.isEmpty() && !emp_id.isEmpty()){
						getEmp();
						if(emp != null){
								full_name = emp.getFull_name();
						}
				}
				return full_name;
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





































