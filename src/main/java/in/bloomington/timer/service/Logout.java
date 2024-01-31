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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.bean.*;

@WebServlet(urlPatterns = {"/Logout"})
public class Logout extends TopServlet{

    static final long serialVersionUID = 2800L;
    static Logger logger = LogManager.getLogger(Logout.class);
    
    public void doGet(HttpServletRequest req,
		      HttpServletResponse res)
	throws ServletException, IOException{
	
	res.setContentType("text/html");
	PrintWriter out = res.getWriter();
	String name= "";
	String value = "";
	HttpSession session = req.getSession();
	Employee user = null;
	if(session != null){
	    user = (Employee)session.getAttribute("user");
	    session.removeAttribute("user");
	    session.invalidate();
	}
	String str = endpoint_logout_uri;
	res.sendRedirect(str);
	return;
	
	
    }

}






















































