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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.bean.*;


public class Logout extends HttpServlet{

		static final long serialVersionUID = 2800L;
		static Logger logger = LogManager.getLogger(Logout.class);
    String url = "", cas_url="";
		
    public void doGet(HttpServletRequest req,
					  HttpServletResponse res)
		throws ServletException, IOException{

		res.setContentType("text/html");
		PrintWriter out = res.getWriter();
		String name= "";
		String value = "";
		HttpSession session = req.getSession();
		User user = null;
		if(session != null){
			user = (User)session.getAttribute("user");
			session.removeAttribute("user");
			session.invalidate();
		}
		if(url.equals("")){
			url    = getServletContext().getInitParameter("url");
			cas_url = getServletContext().getInitParameter("cas_url");
		}
		String str = cas_url+"?url="+url;
		res.sendRedirect(str);
		return;


    }

}






















































