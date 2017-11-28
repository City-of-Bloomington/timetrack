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
import javax.sql.*;
import javax.naming.*;
import javax.naming.directory.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.bean.*;
import in.bloomington.timer.list.*;

public class PickUser extends HttpServlet{

		static final long serialVersionUID = 3100L;
		static Logger logger = LogManager.getLogger(PickUser.class);
    String url ="";
    boolean debug = false;

    public void doGet(HttpServletRequest req,
											HttpServletResponse res)
				throws ServletException, IOException {
				res.setContentType("text/html");
				PrintWriter out = res.getWriter();
				String username = "", message="";
				if(url.equals("")){
						url = getServletContext().getInitParameter("url");
						String debug2 = getServletContext().getInitParameter("debug");
						if(debug2.equals("true")) debug = true;
				}
				Enumeration<String> values = req.getParameterNames();
				String name, value;
        HttpSession session = null;
				session = req.getSession(false);
				User adminUser = null;
				//
				// only admin users are allowed to use this
				// if not send them to login
				//
				if(session != null){
						User user = (User) session.getAttribute("adminUser");
						if(user != null){
								adminUser = user;
						}
				}
				if(adminUser == null){
						String str = url+"Login";
						res.sendRedirect(str);
						return;
				}
				while (values.hasMoreElements()) {
						name = values.nextElement().trim();
						value = req.getParameter(name).trim();
						if (name.equals("username")) {
								username = value.toLowerCase();
						}
						else if (name.equals("message")) {
								message = value;
						}
				}
				out.println("<html><head>");
				out.println("<link rel=\"stylesheet\" href=\""+url+"js/jquery-ui.css\" type=\"text/css\" media=\"all\" />");
				out.println("<link rel=\"stylesheet\" href=\""+url+"js/jquery.ui.theme.css\" type=\"text/css\" media=\"all\" />");
				out.println("<title>Pick User</title>");
				out.println("<script type=\"text/javascript\">");
				out.println("//<![CDATA[  ");
				out.println(" function validate(form) {");
				out.println("  if(form.user_id.value == \"\"){ ");
				out.println("    alert(\"You need to pick one user from the list\");");
        out.println("       form.fullname.focus();");
				out.println("       return false;");
				out.println("	 } ");
        out.println("    return true;");
				out.println("   }");
				out.println(" //]]>                            ");
				out.println("</script>");
				out.println("</head><body onload=\"document.forms[0].fullname.focus();\">");
				out.println("<center>");
				out.println("<h3>Pick Employee</h3>");
				if(!message.equals("")){
						out.println("<h3><font color=\"red\">"+message+"</font></h3>");
				}
				out.println("<form method='post' onsubmit='return validate(this)'> ");
				out.println("<input type=\"hidden\" name=\"user_id\" id=\"user_id\" value=\"\" />");

				out.println("<fieldset>");
				out.println("<center>");
				out.println("<p>Plesse enter at least first two letters of the user's full name and then pick from the list</p>");
				out.println("<table border=\"1\" width=\"70%\">");
				out.println("<tr><td><table width=\"100%\">");
				out.println("<tr><td align=\"right\"><b>Employee Name:</b></td>");
				out.println("<td><input id=\"fullname\" name=\"fullname\" value=\"\" size=\"50\" />");
				out.println("");

				out.println("</td></tr>");
				out.println("<tr><td>&nbsp;</td></tr>");
				out.println("</table></td></tr>");
				out.println("<tr><td><table width=\"100%\">");
				out.println("<tr><td align=\"center\">");
				out.println("<input type=\"submit\" value=\"Submit\"></td></tr>");
				out.println("</table></td></tr>");
				out.println("</table>");
				out.println("</center>");
				out.println("</fieldset>");
				out.println("</form>");
				out.println("<script type=\"text/javascript\" src=\""+url+"js/jquery-1.9.1.min.js\"></script>");
				out.println("<script type=\"text/javascript\" src=\""+url+"js/jquery-ui.min.js\"></script>");
				out.println("<script>");
				out.println("//<![CDATA[  ");
				out.println(" $(\"#fullname\").autocomplete({ ");
				out.println("		source: '"+url+"UserService?format=json&type=fullname', ");
				out.println("		minLength: 2, ");
				out.println("		select: function( event, ui ) { ");
				out.println("			if(ui.item){ ");
				out.println("				$(\"#user_id\").val(ui.item.id); ");
				out.println("			} ");
				out.println("		}  ");
				out.println("	}); ");
				out.println(" //]]>   ");
				out.println("</script>");
				out.println("</center>");
				out.println("</body></html>");
				out.close();
    }
		//
		// doPost menthod
		//
    public void doPost(HttpServletRequest req, HttpServletResponse res)
				throws ServletException, IOException {

				res.setStatus(HttpServletResponse.SC_OK);
				res.setContentType("text/html");
				PrintWriter os = null;
				os = res.getWriter();
				String empid = "", user_id="", message="";
				boolean success = true;
				//
        HttpSession session = null;
				session = req.getSession(true); // true id default
				if(url.equals("")){
						url    = getServletContext().getInitParameter("url");
						String debug2 = getServletContext().getInitParameter("debug");
						if(debug2.equals("true")) debug = true;
				}
				Enumeration<String> values = req.getParameterNames();
				String name, value;
				User user = null;
				User adminUser = null;
				if(session != null){
						User user2 = (User) session.getAttribute("adminUser");
						if(user2 != null){
								adminUser = user2;
						}
				}
				os.println("<html>");
				while (values.hasMoreElements()) {
						name = values.nextElement().trim();
						value = req.getParameter(name).trim();
						if (name.equals("empid")) {
								empid = value;
						}
						else if (name.equals("user_id")) {
								user_id = value;
						}
				}
				if(!empid.equals("")){
						user = new User(empid);
						String back = user.doSelect();
						if(!back.equals("")){
								message += back;
								user = null;
								success = false;
						}
				}
				else if(!user_id.equals("")){
						user = new User(user_id);
						String back = user.doSelect();
						if(!back.equals("")){
								message += back;
								user = null;
								success = false;
						}

				}
				if(!success || user == null || session == null){
						if(message.equals(""))
								message = "Incorrect username or user not found ";
						String str = url+"PickUser?"+
								"message="+message.replace(" ","+");
						res.sendRedirect(str);
						return;
				}
				session.setAttribute("user", user);
				os.println("<head><title></title><META HTTP-EQUIV="+
									 "\"refresh\" CONTENT=\"0; URL=" + url+
									 "Timesheet?info=yes" +
									 "\"></head>");
				os.println("<body>");
				os.println("</body>");
				os.println("</html>");
				os.flush();
				os.close();
    }

}






















































