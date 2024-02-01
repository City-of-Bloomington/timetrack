package in.bloomington.timer.service;

/**
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 */
import com.nimbusds.oauth2.sdk.token.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.annotation.WebServlet;
import com.nimbusds.oauth2.sdk.id.*;
import java.io.*;
import java.util.*;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.bean.*;
import in.bloomington.timer.list.*;
import in.bloomington.timer.util.*;

@WebServlet(urlPatterns = {"/callback"}, loadOnStartup = 1)
public class CallBackServlet extends TopServlet {
    static Logger logger = LogManager.getLogger(CallBackServlet.class);				
    public void doGet(HttpServletRequest request,
		      HttpServletResponse response)
	throws IOException {

	PrintWriter out = response.getWriter();
	String id = "";
	Enumeration values = request.getParameterNames();
	String name= "";
	String value = "";
	String location_id = "", source = "";
	boolean error_flag = false;
	while (values.hasMoreElements()) {
	    name = ((String)values.nextElement()).trim();
	    value = request.getParameter(name).trim();
	    if (name.equals("id"))
		id = value;
	    else if (name.equals("source")) {
		
		source = value;
	    }
	    else if (name.equals("location_id")) {
		location_id = value;
	    }	    
	    else if(name.equals("error")){
		error_flag = true;
		System.err.println(" Error : "+value);		
	    }
	    // System.err.println(" from resquset location_id "+location_id);
	}
	if(!error_flag){
	    String code = request.getParameter("code");
	    String state_str = request.getParameter("state");
	    State state = new State(state_str);
	    State original_state = null;
	    HttpSession session = request.getSession();
	    if(session != null)
		original_state = (State)session.getAttribute("state");
	    // State state = (State)request.getParameter("state");
	    // String original_state = (String)request.getSession().getAttribute("state");
	    if(session == null ||
	       original_state == null ||
	       !state.equals(original_state)){
		System.err.println(" invalid state "+state);
		System.err.println(" org state "+original_state);	
		if(session != null)
		    session.invalidate();
		String str = url+"/Login";
		response.sendRedirect(str);
		return;
	     }
	    if(location_id.isEmpty()){
		String str = (String)request.getSession().getAttribute("location_id");
		if(str != null)
		    location_id = str;
		// System.err.println(" from session location_id "+location_id);
	    }
	    // System.err.println(" state "+state);
	    // System.err.println(" code "+code);
	    /**
	    if(state == null || !original_state.equals(state)){
		System.err.println(" invalid state "+state);
		error_flag = true;
		// 
	    }
	    */
	    if(!state.equals(original_state)){
		System.err.println(" invalid state "+state);
		System.err.println(" org state "+original_state);		
		error_flag = true;
		// 
	    }
	    if(!error_flag){
		Employee user = CityClient.getInstance().endAuthentication(code, config);
		if(user != null){
		    request.getSession().setAttribute("user", user);
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
		}
		else{
		    error_flag = true;
		}
	    }
	}
	if(error_flag){
	    out.println("<head><title>Promt</title></head>");
	    out.println("<body><center>");
	    out.println("<p><font color=red>Unauthorized access, check with IT"+
			", or try again later.</font></p>");
	    out.println("</center>");
	    out.println("</body>");
	    out.println("</html>");
	}
	out.flush();
    }
    
}
