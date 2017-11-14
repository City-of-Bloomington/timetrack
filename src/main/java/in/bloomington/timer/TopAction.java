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
import org.apache.log4j.Logger;

public abstract class TopAction extends ActionSupport implements SessionAware, ServletContextAware{

		static final long serialVersionUID = 3600L;
		static Logger logger = Logger.getLogger(TopAction.class);		
		boolean debug = false, activeMail = false;
		static String url="", server_path="";		
		String action="", id="", employee_id="";
		User user = null;
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
		public User getUser(){
				return user;
		}		
		String doPrepare(){
				String back = "", val="";
				try{
						user = (User)sessionMap.get("user");
						if(user == null){
								back = LOGIN;
						}
						if(url.equals("")){
								val = ctx.getInitParameter("url");
								if(val != null)
										url = val;
								val = ctx.getInitParameter("server_path");
								if(val != null)
										server_path = val;
						}
						val = ctx.getInitParameter("activeMail");
						if(val != null && val.equals("true")){
								activeMail = true;																
						}
						if(sessionMap != null && sessionMap.containsKey("employee_id")){
								Object obj = sessionMap.get("employee_id");
								if(obj != null){
										employee_id = (String) obj;
								}
						}						
				}catch(Exception ex){
						System.out.println(ex);
				}		
				return back;
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
								String str = user.getEmployee_id();
								if(str != null && str.length() > 0){
										setEmployee_id(str);
								}
						}
				}
				return employee_id;
		}
		public Employee getEmployee(){
				if(employee == null){
						getEmployee_id();
						if(!employee_id.equals("")){
								Employee one = new Employee(employee_id);
								String back = one.doSelect();
								if(back.equals("")){
										employee = one;
								}
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
						return employee_id.equals(user.getEmployee_id());
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
}





































