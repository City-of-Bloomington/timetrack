package in.bloomington.timer.util;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginFilter implements Filter {
		static Logger logger = LogManager.getLogger(LoginFilter.class);
		private ServletContext ctx = null;
		public void init(FilterConfig config) throws ServletException {
				ctx = config.getServletContext();
		}
		
		public void doFilter(ServletRequest request,
												 ServletResponse response,
												 FilterChain chain) throws IOException,
																									 ServletException {

				HttpServletRequest req = (HttpServletRequest) request;
				HttpServletResponse res = (HttpServletResponse) response;
		
				String uri = req.getRequestURI();
				HttpSession session = req.getSession(false);
				if(session == null || session.getAttribute("user") == null){
						// these are our exludes
						if(uri.indexOf("timeClock") > -1 ||
							 uri.matches(".*(css|jpg|png|gif|js)$") ||							 
							 uri.indexOf("Login") > -1){
								chain.doFilter(request, response);
						}
						else if(!(uri.matches(".*(css|jpg|png|gif|js)$"))){
								// everything else we need login
								res.sendRedirect("Login");
						}
				}
				else{
						// process the rest of chain
						chain.doFilter(request, response);
				}
		}

		public void destroy() {
				//
		}

}
