package in.bloomington.timer.service;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.*;
import java.sql.*;
import java.io.*;
import javax.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.list.*;
import in.bloomington.timer.bean.*;

@WebServlet(urlPatterns = {"/JobTermEdit"})
public class JobTermEdit extends TopServlet{

    static final long serialVersionUID = 2210L;
    static Logger logger = LogManager.getLogger(JobTermEdit.class);
    static String[] badge_options={"Yes","No","NA"};
    public void doGet(HttpServletRequest req,
		      HttpServletResponse res)
	throws ServletException, IOException {
	doPost(req,res);
    }

    /**
     * @param req The request input stream
     * @param res The response output stream
     */
    public void doPost(HttpServletRequest req,
		       HttpServletResponse res)
	throws ServletException, IOException {
	
	//
	String message="", action="";
	res.setContentType("text/html");
	PrintWriter out = res.getWriter();
	String name, value;
	String id = "", terminate_id="", opener="";
	boolean success = true;
	JobTerminate jterm = new JobTerminate();
	Enumeration<String> values = req.getParameterNames();
	String [] vals = null;
	HttpSession session = session = req.getSession(false);
	Employee user = null;
	if(session != null){
	    user = (Employee)session.getAttribute("user");
	}
	if(user == null){
	    res.sendRedirect(url+"Login");
	    return;
	}
	while (values.hasMoreElements()){
	    name = values.nextElement().trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();
	    if (name.equals("id")) {
		if(value != null && !value.isEmpty()){
		    jterm.setId(value);
		    id = value;
		}
	    }
	    else if (name.equals("terminate_id")) {
		if(value != null && !value.isEmpty()){
		    terminate_id = value;
		    jterm.setTerminate_id(value);
		}
	    }
	    else if (name.equals("job_grade")) {
		if(value != null && !value.isEmpty()){
		    jterm.setJob_grade(value);
		}
	    }
	    else if (name.equals("job_id")) {
		if(value != null && !value.isEmpty()){
		    jterm.setJob_id(value);
		}
	    }
	    else if (name.equals("supervisor_id")) {
		if(value != null && !value.isEmpty()){
		    jterm.setSupervisor_id(value);
		}
	    }	    
	    else if (name.equals("job_step")) {
		if(value != null && !value.isEmpty()){
		    jterm.setJob_step(value);
		}
	    }
	    else if (name.equals("pay_rate")) {
		if(value != null && !value.isEmpty()){
		    jterm.setPayRate(value);
		}
	    }
	    else if (name.equals("start_date")) {
		if(value != null && !value.isEmpty()){
		    jterm.setStart_date(value);
		}
	    }
	    else if (name.equals("last_day_of_work")) {
		if(value != null && !value.isEmpty()){
		    jterm.setLast_day_of_work(value);
		}
	    }
	    else if (name.equals("badge_code")) {
		if(value != null && !value.isEmpty()){
		    jterm.setBadge_code(value);
		}
	    }
	    else if (name.equals("badge_returned")) {
		if(value != null && !value.isEmpty()){
		    jterm.setBadge_returned(value);
		}
	    }
	    else if (name.equals("nw_job_title")) {
		if(value != null && !value.isEmpty()){
		    jterm.setNwJobTitle(value);
		}
	    }	    
	    else if (name.equals("supervisor_phone")) {
		if(value != null && !value.isEmpty()){
		    jterm.setSupervisor_phone(value);
		}
	    }
	    else if (name.equals("weekly_hours")) {
		if(value != null && !value.isEmpty()){
		    jterm.setWeeklyHours(value);
		}
	    }
	    else if (name.equals("opener")){ 
		opener = value;  
	    }						    
	    else if (name.equals("action")){
		action = value;
	    }
	    else{
		System.err.println(name+" "+value);
	    }
	}
	if(action.isEmpty()){
	    if(!id.isEmpty()){
		String back = jterm.doSelect();
		if(!back.isEmpty()){
		    message = "Error "+back;
		    logger.error(back);
		    success = false;
		}
		else{
		    terminate_id = jterm.getTerminate_id();
		}
	    }
	}
	else{
	    String back = jterm.doUpdate();
	    if(!back.isEmpty()){
		message = "Error "+back;
		logger.error(back);
		success = false;
	    }
	    else{
		message = "Updated successfully";
	    }
	}
	out.println("<html><body>");
	out.println("<h3>Job Termination Edit</h3><br />");

	if(success){
	    if(!message.equals(""))
		out.println("<h3>"+message+"</h3>");
	}
	else{
	    if(!message.equals(""))
		out.println("<h3>"+message+"</h3>");
	}
	out.println("<script type=\"text/javascript\">");
	/**
	out.println(" window.onunload = refreshParent; ");
	out.println(" function refreshParent() { ");
	// out.println(" parent.location.assign(\""+url+opener+"?id="+terminate_id +"\");")
	out.println("window.opener.location.reload(true);"); 
	out.println("  } ");
	*/
	out.println("function closeNow() { ");
	out.println("open(location, '_self').close();");
	out.println("return false;");   	
	out.println("}");
	out.println("</script>                                ");
	out.println("<form id=\"myForm\" method=\"post\">");
	out.println("<input type=\"hidden\" name=\"id\" value=\""+id+"\" />");
	out.println("<input type=\"hidden\" name=\"job_id\" value=\""+jterm.getJob_id()+"\" />");
	out.println("<input type=\"hidden\" name=\"supervisor_id\" value=\""+jterm.getSupervisor_id()+"\"/>");	
	out.println("<input type=\"hidden\" name=\"terminate_id\" value=\""+jterm.getTerminate_id()+"\"/>");
	out.println("<input type=\"hidden\" name=\"nw_job_title\" value=\""+jterm.getNwJobTitle()+"\"/>");
	out.println("<fieldset>");
	out.println("<table>");
	out.println("<tr><td>");	    
	out.println("<label>Job Termination ID:</label></td><td>"+id+"&nbsp;&nbsp;");
	out.println("</td></tr>");	    
	out.println("<tr><td>");
	out.println("<label>Job Title:</label>");
	out.println("</td><td>");	
	out.println(jterm.getJob_title());
	out.println("</td></tr>");
	out.println("<tr><td>");
	out.println("<label>Job Grade:</label>");
	out.println("</td><td>");
	out.println("<input name=\"job_grade\" value=\""+jterm.getJob_grade()+"\" "+
		    "size=\"20\" maxlength=\"30\" />");
	out.println("</td></tr>");
	out.println("<tr><td>");
	out.println("<label>Job Step:</label>");
	out.println("</td><td>");
	out.println("<input name=\"job_step\" value=\""+jterm.getJob_step()+"\" "+
		    "size=\"20\" maxlength=\"30\" />");
	out.println("</td></tr>");
	out.println("<tr><td>");
	out.println("<label>Pay Rate($):</label>");
	out.println("</td><td>");
	out.println("<input name=\"pay_rate\" value=\""+jterm.getPayRate()+"\" "+
		    "size=\"20\" maxlength=\"30\" />");
	out.println("</td></tr>");	
	out.println("<tr><td>");
	out.println("<label>Weekly Hours:</label>");
	out.println("</td><td>");
	out.println("<input name=\"weekly_hours\" value=\""+jterm.getWeeklyHours()+"\" "+
		    "size=\"2\" maxlength=\"3\" />");
	out.println("</td></tr>");
	out.println("<tr><td>");
	out.println("<label>Start Date:</label>");
	out.println("</td><td>");
	out.println("<input name=\"start_date\" value=\""+jterm.getStart_date()+"\" "+
		    "size=\"10\" maxlength=\"10\" />");
	out.println("</td></tr>");
	out.println("<tr><td>");
	out.println("<label>Last Day of Work:</label>");
	out.println("</td><td>");
	out.println("<input name=\"last_day_of_work\" value=\""+jterm.getLast_day_of_work()+"\" "+
		    "size=\"10\" maxlength=\"10\" />");
	out.println("</td></tr>");	
	out.println("<tr><td>");
	out.println("<label>Supervisor:</label>");
	out.println("</td><td>");
	out.println(jterm.getSupervisor());
	out.println("</td></tr>");
	out.println("<tr><td>");	
	out.println("<label>Supervisor Phone:</label>");
	out.println("</td><td>");
	out.println("<input name=\"supervisor_phone\" value=\""+jterm.getSupervisor_phone()+"\" "+
		    "size=\"10\" maxlength=\"20\" />");
	out.println("</td></tr>");
	out.println("<tr><td>");
	out.println("<label>Badge Code:</label>");
	out.println("</td><td>");
	out.println("<input name=\"badge_code\" value=\""+jterm.getBadge_code()+"\" "+
		    "size=\"4\" maxlength=\"5\" />");
	out.println("</td></tr>");	
	out.println("<tr><td>");
	out.println("<label>Badge Returned:</label>");
	out.println("</td><td>");
	out.println("<select name=\"badge_returned\">");
	for(String str:badge_options){
	    String selected = "";
	    if(str.equals(jterm.getBadge_returned())){
		selected="selected=\"selected\"";
	    }
	    out.println("<option value=\""+str+"\" "+selected+">"+str+"</option>");
	}
	out.println("</select>");
	out.println("</td></tr>");
	out.println("<tr><td colspan=\"2\"><input type=\"submit\" name=\"action\" id=\"action\" value=\"Update\" />");
	out.println("</td></tr>");
	out.println("</table>");
	out.println("</fieldset>");
	out.println("</form>");
	if(action.isEmpty()){
	    out.println("<p><a href=\"#\" onclick=\"return closeNow();\">Close This Window</a></p>");
	}
	else{
	    out.println("<p>After you close this window, refresh/reload the parent window to see the changes.</p>");
	}
	out.println("</body></html>");
	
	out.flush();
	out.close();
    }

}
