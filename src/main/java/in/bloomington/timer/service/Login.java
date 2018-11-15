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
import javax.naming.*;
import javax.naming.directory.*;
import javax.sql.*;
import java.net.URL;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jasig.cas.client.authentication.AttributePrincipal;
import in.bloomington.timer.bean.*;

public class Login extends HttpServlet{

    //
		String cookieName = ""; // "cas_session";
		String cookieValue = ""; // ".bloomington.in.gov";
    String url="";
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
				String name="", value="", username="", source="";
				AttributePrincipal principal = null;				
				if (req.getUserPrincipal() != null) {
						principal = (AttributePrincipal) req.getUserPrincipal();
						username = principal.getName();
				}
				String host_forward = req.getHeader("X-Forwarded-Host");
				String host = req.getHeader("host");	
				
				Enumeration<String> values = req.getParameterNames();
				while (values.hasMoreElements()) {
						name = values.nextElement().trim();
						value = (req.getParameter(name)).trim();
						if (name.equals("id")) {
								id = value;
						}
						if (name.equals("source")) {
								source = value;
						}						
				}
				res.setContentType("text/html");
				PrintWriter out = res.getWriter();
				if(host_forward != null){
						// System.err.println(" login host forward "+host_forward);
						url = host_forward+"/timetrack/";
				}
				else if(host != null){
						// System.err.println(" login host "+host);
						if(host.indexOf("timetrack") > -1){
								url = host;
						}
						else{
								url = host+"/timetrack/";
						}
				}
				else{
						url  = getServletContext().getInitParameter("url");
				}
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
				if(username == null || username.equals("")){
						username = req.getRemoteUser();
				}
				if(username != null){
						session = req.getSession();
						// setCookie(req, res);
						Employee user = getUser(username);
						if(session != null){
								if(user != null){
										//
										// first login, we have only one user
										// if he is an admin, we add to session
										//
										session.setAttribute("user",user);
										if(source.equals(""))
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
				message += " You can not access this system, check with IT or try again later";
				out.println("<head><title></title><body>");
				out.println("<p><font color=red>");
				out.println(message);
				out.println("</font></p>");
				out.println("</body>");
				out.println("</html>");
				out.flush();
    }

    /**
     * Procesesses the login and check for authontication.
     * Uses ldap for authentication.
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
						if(!back.equals("")){
								message += back;
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






















































