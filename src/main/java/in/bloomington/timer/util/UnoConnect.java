package in.bloomington.timer.util;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.*;
import java.sql.*;
import javax.sql.*;
import javax.naming.*;
import java.text.*;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
// import javax.servlet.ServletRequestEvent;
// import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@WebListener
public class UnoConnect implements ServletContextListener{
		// public class UnoConnect implements ServletRequestListener{
		boolean debug = false;
		static String dbUrl = "", dbName="", dbUser="", dbPass="";
		//
		// we are using a single connect to MS sql server as were not
		// able to create connection pools
		//
		private static int con_cnt = 0;
		private static UnoConnect unoCon = null;
		static Logger logger = LogManager.getLogger(UnoConnect.class);
		static Connection con = null;
		/**
     * Create a static method to get instance.
     */
    public static UnoConnect getInstance(){
        if(unoCon == null){
            unoCon = new UnoConnect();
        }
        return unoCon;
    }

		public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext ctx = servletContextEvent.getServletContext();
    }
		public void contextDestroyed(ServletContextEvent servletContextEvent) {
				disconnect();
    }
		/**
		public void requestInitialized(ServletRequestEvent event) {
				// System.err.println(" new request ");
    }			
		public void requestDestroyed(ServletRequestEvent event) {
				disconnect();
    }
		*/
		public static Connection getConnection(){
				boolean pass = false;
				try{
						Context initCtx = new InitialContext();
						Context envCtx = (Context) initCtx.lookup("java:comp/env");
						DataSource ds = (DataSource)envCtx.lookup("jdbc/MySQL_timer");
						if(con == null || con.isClosed()){
								for(int i=0;i<3;i++){				
										con = ds.getConnection();
										pass = testCon(con);
										if(pass) break;										
								}
						}
				}
				catch(Exception ex){
						logger.error(ex);
				}
				return con;
		}		
		public void disconnect(){
				try{
						if(con != null && !con.isClosed()){
								con.close();
								con = null;
								// System.err.println(" con closed "+con_cnt);
								con_cnt--;
						}
				}catch(Exception ex){
						System.err.println(ex);
				}
				finally{
						if (con != null) {
								try { con.close(); } catch (SQLException e) { ; }
								con = null;
						}
				}
		}
		static boolean testCon(Connection cc){
				String qq = "select 1";
				Statement stmt = null;
				ResultSet rs = null;
				try{
						stmt = cc.createStatement();
						rs = stmt.executeQuery(qq);
						if(rs.next()){
								con_cnt++;
								// System.err.println(" con cnt "+con_cnt);								
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
