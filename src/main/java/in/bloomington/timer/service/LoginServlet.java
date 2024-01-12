package in.bloomington.timer.service;
/**
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 */
import com.nimbusds.oauth2.sdk.*;
import com.nimbusds.oauth2.sdk.id.*;
import com.nimbusds.oauth2.sdk.token.*;
import com.nimbusds.openid.connect.sdk.Nonce;
import java.net.URI;
import javax.servlet.ServletContext;
import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.bean.*;
import in.bloomington.timer.list.*;
import in.bloomington.timer.util.*;
/**
 *
 * for ADFS login
 * 
 */
// uncomment this line if you want to use ADFS
// comment out if you want to use CAS
@WebServlet(urlPatterns = {"/Login","/login"}, loadOnStartup = 1)
public class LoginServlet extends TopServlet {

    static Logger logger = LogManager.getLogger(LoginServlet.class);
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response){
	String id="", location_id="", source="";
	try{
	    HttpSession session = request.getSession();
	    Employee user = (Employee)session.getAttribute("user");
	    Enumeration<String> values = request.getParameterNames();
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
	    if(user == null){
		OidcClient oidcClient = OidcClient.getInstance();
		//
		//
		oidcClient.setConfig(config);
		URI redirectUrl = oidcClient.getRequestURI();
		// System.err.println("login auth url "+redirectUrl.toString());
		State state = oidcClient.getState();
		Nonce nonce = oidcClient.getNonce();
		session.setAttribute("state",state.toString());
		session.setAttribute("nonce",nonce.toString());
		// save state in session for verification later
		response.sendRedirect(redirectUrl.toString());
	    }
	    else{
		PrintWriter out = response.getWriter();
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
	    }
	}
	catch(Exception ex){
	    logger.error(""+ex);
	    System.err.println(" "+ex);
	}
    }
}
