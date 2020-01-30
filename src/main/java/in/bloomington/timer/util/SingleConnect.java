package in.bloomington.timer.util;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.*;
import java.sql.*;
import java.text.*;
import org.apache.log4j.PropertyConfigurator;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebListener
public class SingleConnect implements ServletContextListener{

		boolean debug = false;
		static String dbUrl = "", dbName="", dbUser="", dbPass="";
		//
		// we are using a single connect to MS sql server as were not
		// able to create connection pools
		//
		private static SingleConnect myCon = null;
		static Logger logger = LogManager.getLogger(SingleConnect.class);
		static Connection con = null;
		/**
     * Create a static method to get instance.
     */
    public static SingleConnect getInstance(){
        if(myCon == null){
            myCon = new SingleConnect();
        }
        return myCon;
    }	
		public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext ctx = servletContextEvent.getServletContext();
        String str = ctx.getInitParameter("dbUrl");
				if(str != null)
						dbUrl = str;
        str = ctx.getInitParameter("dbUser");
				if(str != null)
						dbUser = str;
        str = ctx.getInitParameter("dbPass");
				if(str != null)
						dbPass = str;
        str = ctx.getInitParameter("dbName");
				if(str != null)
						dbName = str;				
    }	
		public void contextDestroyed(ServletContextEvent servletContextEvent) {
				disconnect();
    }
		public static Connection getNwConnection(){
				boolean pass = false;
				if(dbName.isEmpty() || dbUser.isEmpty()){
						System.err.println("MS sql database: no database specified");
						return null;
				}
				String dbSql = dbUrl+";database="+dbName+";user="+dbUser+";password="+dbPass;
				for(int i=0;i<3;i++){
						try{
								if(con == null || con.isClosed()){
										 Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); 
										// Class.forName(dbSql);
										con = DriverManager.getConnection(dbSql);
										pass = testCon(con);
								}
						}
						catch(Exception ex){
								logger.error(ex);
						}
						if(pass) break;
				}
				return con;
		}		
		public void disconnect(){
				System.err.println("closing connection");
				Helper.databaseDisconnect(con);
		}
		
		static boolean testCon(Connection cc){
				String qq = "select 1";
				Statement stmt = null;
				ResultSet rs = null;
				try{
						stmt = cc.createStatement();
						rs = stmt.executeQuery(qq);
						if(rs.next()){
								return true;
						}
				}
				catch(Exception ex){
						logger.error(ex);
				}
				finally{
						Helper.databaseClean(stmt, rs);
				}
				return false;
		}

}
