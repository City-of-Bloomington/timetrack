/**
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
package in.bloomington.timer;
import java.util.*;
import java.io.*;
import java.text.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.opensymphony.xwork2.ModelDriven;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;  
import org.apache.struts2.dispatcher.SessionMap;  
import org.apache.struts2.interceptor.SessionAware;
import org.apache.struts2.util.ServletContextAware;  
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class TopAction extends ActionSupport implements SessionAware, ServletContextAware{

		static final long serialVersionUID = 3600L;
		static Logger logger = LogManager.getLogger(TopAction.class);		
		boolean debug = false, activeMail = false;
		static String server_path="";
		static EnvBean envBean = null;
		String uri="",url="";
		
		String action="", id="", employee_id="";
		List<String> errors = new ArrayList<>(),
				messages = new ArrayList<>();
		Employee user = null;
		Employee employee = null;
	  ServletContext ctx;
		Map<String, Object> sessionMap;

		public void setAction(String val){
				if(val != null)
						action = val;
		}
		public void setAction2(String val){
				if(val != null && !val.equals(""))
						action = val;
		}		
		public String getAction(){
				return action;
		}
		public void setId(String val){
				if(val != null)
						id = val;
		}
		public String getId(){
				return id;
		}
		public Employee getUser(){
				return user;
		}
		String doPrepare(String source){
				String back = "", val="";
				try{
						if(url.equals("")){
								val = ctx.getInitParameter("url");
								if(val != null)
										url = val;
								val = ctx.getInitParameter("server_path");
								if(val != null)
										server_path = val;
						}
						if(envBean == null){
								envBean = new EnvBean();
								val = ctx.getInitParameter("ldap_url");
								if(val != null)
										envBean.setUrl(val);
								val = ctx.getInitParameter("ldap_principle");
								if(val != null)
										envBean.setPrinciple(val);
								val = ctx.getInitParameter("ldap_password");
								if(val != null)
										envBean.setPassword(val);									
						}
						val = ctx.getInitParameter("activeMail");
						if(val != null && val.equals("true")){
								activeMail = true;																
						}
						if(sessionMap == null || sessionMap.get("user") == null){
								// timeblock we do not need login								
								if(source != null && !source.equals("timeClock")){
										HttpServletResponse res = ServletActionContext.getResponse();
										String str = url+"Login";								
										if(source != null)
												str += "?source="+source;
										res.sendRedirect(str);
										return super.execute();
								}
						}
						else{
								user = (Employee)sessionMap.get("user");
						}
						if(sessionMap.containsKey("employee_id")){
								Object obj = sessionMap.get("employee_id");
								if(obj != null){
										employee_id = (String) obj;
								}
						}
						setUrls();
				}catch(Exception ex){
						logger.error(ex);
				}
				return back;
		}
		String doPrepare(){
				return doPrepare(null);
		}

		public void setEmployee_id(String val){
				if(val != null && !val.equals("")){		
						employee_id = val;
						if(sessionMap != null){
								sessionMap.put("employee_id", employee_id);
						}
				}
		}
		public String getEmployee_id(){
				if(employee_id.equals("")){
						if(sessionMap != null && sessionMap.containsKey("employee_id")){
								Object obj = sessionMap.get("employee_id");
								if(obj != null){
										employee_id = (String) obj;
								}
						}
						else {
								String str = user.getId();
								if(str != null && str.length() > 0){
										setEmployee_id(str);
								}
						}
				}
				return employee_id;
		}
		public Employee getEmployee(){
				if(employee_id.equals("")){
						getEmployee_id();
				}
				if(!employee_id.equals("")){
						Employee one = new Employee(employee_id);
						String back = one.doSelect();
						if(back.equals("")){
								employee = one;
						}
				}				
				if(employee == null){
						if(user != null){
								employee = user;
								employee_id = user.getId();
						}
				}
				return employee;
		}
		public boolean hasEmployee(){
				getEmployee();
				return employee != null;
		}
		public boolean isUserCurrentEmployee(){
				if(user != null)
						return employee_id.equals(user.getId());
				return false;
		}
		@Override  
		public void setSession(Map<String, Object> map) {  
				sessionMap=map;  
		}
		@Override  	
		public void setServletContext(ServletContext ctx) {  
        this.ctx = ctx;  
    }
		void addError(String str){
				if(errors == null)
						errors = new ArrayList<>();
				if(str != null)
						errors.add(str);
		}
		void addMessage(String str){
				if(messages == null)
						messages = new ArrayList<>();
				if(str != null)
						messages.add(str);
		}
		void clearAll(){
				if(errors.size() > 0)
						errors = new ArrayList<>();
				if(messages.size() > 0)
						messages = new ArrayList<>();
		}
		public boolean hasErrors(){
				return errors != null && errors.size() > 0;
		}
		public List<String> getErrors(){
				return errors;
		}
		public boolean hasMessages(){
				return messages != null && messages.size() > 0;
		}		
		public List<String> getMessages(){
				return messages;
		}
		private void setUrls(){
				HttpServletRequest request = ServletActionContext.getRequest();				
				String host_forward = request.getHeader("X-Forwarded-Host");
				String host = request.getHeader("host");				
				// StringBuffer r_url = request.getRequestURL();	// https://outlaw.b.. /timeClock.action		
				String servlet_path = request.getServletPath();
				if(host_forward != null){
						// System.err.println(" host forward "+host_forward);
						url = host_forward+"/timetrack/";
				}
				else if(host != null){
						// System.err.println("host "+host);												
						if(host.indexOf("timetrack") > -1){
								url = host;
						}
						else{
								url = host+"/timetrack/";
						}
				}
		}
		
}





































