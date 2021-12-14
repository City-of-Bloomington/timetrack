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
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebListener
public class UnoConnect implements ServletRequestListener{
		static Logger logger = LogManager.getLogger(UnoConnect.class);
		static int con_cnt = 0;
		private static class ThreadLocalConnection extends ThreadLocal {
				Connection connection;
				@Override
				public Object initialValue() {
						return null;
				}
				
				@Override
				public void remove() {
						super.remove();
						closeConnection();
				}
		}
		private static ThreadLocalConnection conn = new ThreadLocalConnection();

		public static Connection getConnection(){
				Connection con = null;
				con = (Connection) conn.get();
				try{
						if(con == null || con.isClosed()){
								boolean pass = false;
								Context initCtx = new InitialContext();
								Context envCtx = (Context) initCtx.lookup("java:comp/env");
								DataSource ds = (DataSource)envCtx.lookup("jdbc/MySQL_timer");
								if(con == null || con.isClosed()){
										for(int i=0;i<3;i++){				
												con = ds.getConnection();
												pass = testCon(con);
												if(pass){
														conn.set(con);
														break;
												}
										}
								}
						}
						else{
								// System.err.println(" using the same con "+con_cnt);
						}
				}
				catch(Exception ex){
						logger.error(ex);
				}
				return (Connection) conn.get();
	 }
		public static void closeConnection() {
				try{
						if(conn.get() != null){
								Connection con = (Connection)conn.get();
								try{
										if(con != null && !con.isClosed()){
												// ((Connection)conn.get()).close();
												con.close();
												// System.out.println("Removed Connection ..."+con_cnt);
												con_cnt--;
												con = null;
												conn.set(null);
										}
										else{
												con = null;
												conn.set(null);
										}
								}catch(Exception ex){
										System.err.println(ex);
								}
								finally{
										conn.set(null);
								}
						}
				}catch(Exception e){
						System.err.println(e);
				}
		}
		public void requestInitialized(ServletRequestEvent event) {
				// System.err.println(" new request ");
    }			
		public void requestDestroyed(ServletRequestEvent event) {
				closeConnection();
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

    /**
     * if want to go back for pooling we are going to need this
     */
    public static void databaseDisconnect(Connection con){
	// nothing for now
	
    }
}
