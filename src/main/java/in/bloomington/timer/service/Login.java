package in.bloomington.timer.service;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.*;
import java.sql.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import javax.sql.*;
import java.net.URL;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jasig.cas.client.authentication.AttributePrincipal;
import in.bloomington.timer.bean.*;
import in.bloomington.timer.util.CommonInc;

// comment this line if you want to use ADFS
@WebServlet(urlPatterns = {"/CasLogin","/caslogin"}, loadOnStartup = 1)
public class Login extends TopServlet{

    //
    String cookieName = ""; 
    String cookieValue = "";
    static int count = 0;
    //
    // reserved usernames that issue a warning
    static final List<String> invalid_list = Arrays.asList(CommonInc.invalid_usernames);
		
    static final long serialVersionUID = 2700L;
    static Logger logger = LogManager.getLogger(Login.class);
    /**
     *
     */
    public void doGet(HttpServletRequest req,
		      HttpServletResponse res)
	throws ServletException, IOException {
	String message="", id="";
	boolean found = false;
	String name="", value="", username="", source="",location_id="";
	AttributePrincipal principal = null;				
	if (req.getUserPrincipal() != null) {
	    principal = (AttributePrincipal) req.getUserPrincipal();
	    username = principal.getName();
	}
	/*
	  Enumeration<String> headerNames = req.getHeaderNames();
	  while (headerNames.hasMoreElements()) {
	  String headerName = headerNames.nextElement();
	  System.err.println("Header Name: <em>" + headerName);
	  String headerValue = req.getHeader(headerName);
	  System.err.println(headerValue);
	  }
	*/
	String host_forward = req.getHeader("X-Forwarded-Host");
	String host = req.getHeader("host");	
	Enumeration<String> values = req.getParameterNames();
	while (values.hasMoreElements()) {
	    name = values.nextElement().trim();
	    value = (req.getParameter(name)).trim();
	    if(!value.isEmpty()){
		if (name.equals("id")) {
		    id = value;
		}
		else if (name.equals("source")) {
										
		    source = value;
		}
		else if (name.equals("location_id")) {
		    location_id = value;
		}
	    }
	}
	res.setContentType("text/html");
	PrintWriter out = res.getWriter();
	if(host_forward != null){
	    if(host_forward.indexOf("timetrack") == -1)
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
	// else url from TopServlet
	HttpSession session = null;
	if(principal != null){
	    final Map attributes = principal.getAttributes();
	    Iterator attributeNames = attributes.keySet().iterator();
	    if (attributeNames.hasNext()) {
		for (; attributeNames.hasNext(); ) {
                    String attributeName = (String) attributeNames.next();
                    // System.err.println(" name "+attributeName);
                    final Object attributeValue = attributes.get(attributeName);
		    if (attributeValue instanceof List) {
                        final List vals = (List) attributeValue;
                        // System.err.println("Multi-valued attribute: " + vals.size());
			int jj=1;
                        for (Object val : vals) {
                            System.err.println(jj+" "+val);
			    jj++;
                        }
                    } else {
                        // System.err.println(" value "+attributeValue);
                    }
		}
	    }
	}
	if(username == null || username.isEmpty()){
	    username = req.getRemoteUser();
	}
	if(username != null){
	    if(invalid_list.contains(username)){
		message = "<h3>TimeTrack: Error</h3>";
		message += "Please do not use the usernmae: "+username+" to access your timesheet <br /> ";
		message += "You need to use your own username to access timetrack<br />\n";
		message += "Suggestions <br />\n";
		message += "<ul>";
		message += "<li>Log out and log in again with your own username </li>";
		message += "<li>You may close your browser and open it again </li>";
		message += "<li>Or you may use another browser for timetrack</li>";
		message += "</ul>";
	    }
	    else{
		session = req.getSession();
		Employee user = getUser(username);
		if(session != null){
		    if(user != null){
			//System.err.println("login location ID "+location_id);
			//
			session.setAttribute("user",user);
			if(!location_id.isEmpty()){
			    source = "mobile.action?action=Next&location_id="+location_id;
			}
			else if(source.isEmpty())
			    source = "timeDetails.action";
			out.println("<head><title></title><META HTTP-EQUIV="+
				    "\"refresh\" CONTENT=\"0; URL=" + source+"\"></head>");
			out.println("<body>");
			out.println("</body>");
			out.println("</html>");
			out.flush();
			return;
		    }
		}
	    }
	}
	else{
	    count++;
	    if(count < 3){
		String str = url+"Login";
		res.sendRedirect(str);
	    }
	    message += " <p>You can not access this system, check with IT or try again later</p>";
	}
	if(message.isEmpty())
	    message += "<p> You can not access this system, check with IT or try again later</p>";
	out.println("<head><title>TimeTrack</title><body>");
	out.println("<center>");
	out.println(message);
	out.println("</center>");
	out.println("</body>");
	out.println("</html>");
	out.flush();
    }

    /**
     * Procesesses the login and check for authontication.
     * Uses CAS ldap/AD for authentication.
     * @param req
     * @param res
     */
    Employee getUser(String username){

	boolean success = true;
	Employee user = null;
	String message="";
	try{
	    Employee one = new Employee(null, username);
	    String back = one.doSelect();
	    if(!back.isEmpty()){
		message += back+": "+username;
		logger.error(message);
	    }
	    else{
		user = one;
	    }
	}
	catch (Exception ex) {
	    logger.error(ex);
	}
	return user;
    }

}






















































