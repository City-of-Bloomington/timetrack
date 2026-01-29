package in.bloomington.timer.util;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.IOException;
import java.util.Enumeration;
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
    // public static String POLICY = "frame-src 'self'; sandbox allow-forms allow-scripts allow-popups allow-same-origin allow-top-navigation allow-popups-to-escape-sandbox; img-src 'self' data:; object-src 'none';frame-ancestors 'self'";

    public static String POLICY = "frame-src 'none'; img-src 'self' data:; object-src 'none';frame-ancestors 'self';";    
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
	res.addHeader("Content-Security-Policy", LoginFilter.POLICY);	
	res.addHeader("X-Frame-Options", "DENY");
	String uri = req.getRequestURI();		
	StringBuffer url = req.getRequestURL();
	HttpSession session = req.getSession();
	if(session == null || session.getAttribute("user") == null){
	    // these are our exludes
	    if(uri.matches(".*(timeClock|PickJob|mobileClock|callback).*") ||
	       uri.matches(".*(Service|Login|css|jpg|png|gif|js)$")){
		chain.doFilter(request, response);
		
	    }
	    else{
		/**
		Enumeration<String> headerNames = req.getHeaderNames();
		while (headerNames.hasMoreElements()) {
		    String headerName = headerNames.nextElement();
		    System.err.println("Header Name:" + headerName);
		    String headerValue = req.getHeader(headerName);
		    System.err.println(headerValue);
		}
		*/
		// String default_link = "/timetrack/timeDetails.action";
		String referer_host = req.getHeader("referer");
		// String host_forward = req.getHeader("X-Forwarded-Host");
		// String host = req.getHeader("host");
		System.err.println(" referer "+referer_host);
		//System.err.println(" host forward "+host_forward);
		// System.err.println(" host "+host);		
		//System.err.println(" url "+url);		
		String originalURL = null;
		if(referer_host != null){
		    originalURL = referer_host;
		}
		// System.err.println("origin:"+originalURL);
		if(originalURL != null){
		    req.getSession().setAttribute("originalURL", originalURL);
		}
		// we need to do cleanup the url may contain WEB-INF/jsp
		// and jsp extension
		// everything else we need login
		res.sendRedirect("login");
	    }
	}
	else{
	    String originalURL = (String) session.getAttribute("originalURL");
	    if (originalURL != null && !originalURL.isEmpty()) {
		session.removeAttribute("originalURL"); 		
		res.sendRedirect(originalURL);
	    }
	    else {
		// process the rest of the chain
		chain.doFilter(request, response);
	    }
	}
    }

    public void destroy() {
	//
    }

}
