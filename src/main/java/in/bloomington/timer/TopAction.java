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
    static String mail_host = null;
    String uri="",url="", proxy_url="";
		
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
		val = ctx.getInitParameter("proxy_url");
		if(val != null)
		    proxy_url = val;								
		val = ctx.getInitParameter("server_path");
		if(val != null)
		    server_path = val;
		val = ctx.getInitParameter("mail_host");
		if(val != null)
		    mail_host = val;
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
		//
		// timeClock we do not need login
		/**
		   if(source != null && !source.equals("timeClock.action")){
		   HttpServletResponse res = ServletActionContext.getResponse();
		   String str = "Login";								
		   if(source != null && !source.equals(""))
		   str += "?source="+source;
		   res.sendRedirect(str);
		   return super.execute();
		   }
		*/
	    }
	    else{
		user = (Employee)sessionMap.get("user");
	    }
	    if(sessionMap != null && sessionMap.containsKey("employee_id")){
		Object obj = sessionMap.get("employee_id");
		if(obj != null){
		    employee_id = (String) obj;
		}
	    }
	    setUrls();
	    clearAll();
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
		if(user != null){
		    String str = user.getId();
		    if(str != null && str.length() > 0){
			setEmployee_id(str);
		    }
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
	if(user != null){
	    getEmployee();
	    return employee_id.equals(user.getId());
	}
	return false;
    }
    // to change proxy employee back to main user
    void resetEmployee(){
	if(user != null){
	    setEmployee_id(user.getId());
	    getEmployee();
	}
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
    public String getErrorsAll(){
	String ret = "";
	if(hasErrors()){
	    for(String str:errors){
		if(!ret.equals("")) ret += ", ";
		ret += str;
	    }
	}
	return ret;
    }
    public boolean hasMessages(){
	return messages != null && messages.size() > 0;
    }		
    public List<String> getMessages(){
	return messages;
    }
    String obtainGroupIdFromSession(){
	String val = "";
	try{
	    if(sessionMap != null && sessionMap.containsKey("selected_group_id")){
		Object obj = sessionMap.get("selected_group_id");
		if(obj != null){
		    String str = (String) obj;
		    if(str != null)
			val = str;
		}
	    }
	}catch(Exception ex){
	    System.err.println(ex);
	}
	return val;
    }
    void addGroupIdToSession(String val){
	if(val != null && !val.equals("")){
	    if(sessionMap != null){						
		try{
		    sessionMap.put("selected_group_id", val);						
		}catch(Exception ex){
		    System.err.println(ex);
		}
	    }
	}
    }		
    private void setUrls(){
	HttpServletRequest request = ServletActionContext.getRequest();				
	String host_forward = request.getHeader("X-Forwarded-Host");
	String host = request.getHeader("host");				

	// System.err.println(" host forward "+host_forward);
	
	if(host_forward != null){
	    if(host_forward.indexOf("/timetrack") == -1)
		url = host_forward+"/timetrack/";
	    else
		url = host_forward;
	}
	else if(host != null){
	    if(host.indexOf("timetrack") > -1){
		url = host;
	    }
	    else{
		url = host+"/timetrack/";
	    }
	}
    }
		
}





































