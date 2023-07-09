/**
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
package in.bloomington.timer.action;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
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
    static String uri="",url="", proxy_url="";
    static String xls_output_location="";
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
	if(val != null && !val.isEmpty())
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
	if(user == null){
	    if(sessionMap == null || sessionMap.get("user") == null){
		//
		// timeClock we do not need login
		//
	    }
	    else{
		user = (Employee)sessionMap.get("user");
	    }
	}
	return user;
    }
    public boolean canEdit(){
	getUser();
	return user != null && user.canEdit();
    }
    public boolean isAdmin(){
	getUser();
	return user != null && user.isAdmin();
    }
    public boolean isITSAdmin(){
	getUser();
	return user != null && user.isITSAdmin();
    }    
    String doPrepare(String source){
	String back = "", val="";
	try{
	    if(url.isEmpty()){
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
		val = ctx.getInitParameter("xls_output_location");
		if(val != null)
		    xls_output_location = val;								
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
		//
		back = "No Sesion ";
		return back;
	    }
	    else{
		user = (Employee)sessionMap.get("user");
		if(sessionMap != null && sessionMap.containsKey("employee_id")){
		    Object obj = sessionMap.get("employee_id");
		    if(obj != null){
			employee_id = (String) obj;
		    }
		}
		if(url.isEmpty())
		    setUrls();
		clearAll();
	    }
	}catch(Exception ex){
	    logger.error(ex);
	}
	return back;
    }
    String doPrepare(){
	return doPrepare(null);
    }
    public void setEmployee_id(String val){
	if(val != null && !val.isEmpty()){		
	    employee_id = val;
	    if(sessionMap != null){
		sessionMap.put("employee_id", employee_id);
	    }
	}
    }
    public String getEmployee_id(){
	if(employee_id.isEmpty()){
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
			employee = user;
		    }
		}
	    }
	}
	return employee_id;
    }
    public Employee getEmployee(){
	if(employee_id.isEmpty()){
	    getEmployee_id();
	}
	if(employee == null && !employee_id.isEmpty()){
	    Employee one = new Employee(employee_id);
	    String back = one.doSelect();
	    if(back.isEmpty()){
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
	if(user == null){
	    getUser();
	}
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
	if(str != null){
	    if(!messages.contains(str.trim()))
		messages.add(str.trim());
	}
    }
    public void setMessages(String[] vals){
	if(vals != null){
	    for(String val:vals){
		addMessage(val);
	    }
	}
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
    public void setErrors(List<String> vals){
	if(vals != null)
	    errors = vals;
    }
													
    public String getErrorsAll(){
	String ret = "";
	if(hasErrors()){
	    for(String str:errors){
		if(!ret.isEmpty()) ret += ", ";
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
	if(val != null && !val.isEmpty()){
	    if(sessionMap != null){						
		try{
		    sessionMap.put("selected_group_id", val);						
		}catch(Exception ex){
		    System.err.println(ex);
		}
	    }
	}
    }
    public  String canProceed(String val){
	String back = doPrepare(val);
	if(back.isEmpty() && 
	   (canEdit() || isAdmin() || isITSAdmin())){
	    return "";
	}
	return "Not allowed";
    }
    private void setUrls(){
	if(!url.isEmpty()) return;
	HttpServletRequest request = ServletActionContext.getRequest();				
	String host_forward = request.getHeader("X-Forwarded-Host");
	String host = request.getHeader("host");				

	System.err.println(" host forward "+host_forward);
	System.err.println(" host "+host);	
	if(host_forward != null){
	    if(host_forward.indexOf("/timetrack") == -1){
		// url = host_forward+"/timetrack/";
		
	    }
	    else
		url = host_forward;
	}
	else if(host != null){
	    if(host.indexOf("timetrack") > -1){
		url = host;
	    }
	    else{
		// url = host+"/timetrack/";
	    }
	}
	if(true){
	    String pat = "timetrack";
	    Pattern pattern = Pattern.compile(pat);
	    Matcher matcher = pattern.matcher(url);
	    int cnt = 0, end_id=0;
	    while(matcher.find()){
		if(end_id == 0)
		    end_id = matcher.end();
		cnt++;
	    }
	    if(cnt > 1){
		url = url.substring(end_id+1);
	    }				
	    System.err.println(" url "+url);
	}
    }
		
}





































