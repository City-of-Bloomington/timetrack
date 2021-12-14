package in.bloomington.timer.util;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.*;
import java.sql.*;
import java.text.*;
// import org.apache.log4j.PropertyConfigurator;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebListener
public class SingleContextHolder implements ServletContextListener{

		boolean debug = false;
		//
		private static SingleContextHolder myHolder = null;
		static Logger logger = LogManager.getLogger(SingleContextHolder.class);
		static ServletContext ctx = null;
		/**
     * Create a static method to get instance.
     */
    public static SingleContextHolder getInstance(){
        if(myHolder == null){
            myHolder = new SingleContextHolder();
        }
        return myHolder;
    }	
		public void contextInitialized(ServletContextEvent servletContextEvent) {
        ctx = servletContextEvent.getServletContext();
    }	
		public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
		public static ServletContext getContext(){
				return ctx;
		}		

}
