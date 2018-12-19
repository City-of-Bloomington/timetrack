package in.bloomington.timer.util;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.*;
import java.text.*;
import java.sql.*;
import java.util.*;
import javax.sql.*;
import javax.naming.*;
import javax.naming.directory.*;
import java.security.MessageDigest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.list.*;
import in.bloomington.timer.bean.*;

public class Helper{

		static final long serialVersionUID = 2300L;
		static Logger logger = LogManager.getLogger(Helper.class);
		
		static int c_con = 0;
		final static String bgcolor = "silver";// #bfbfbf gray
		final static String fgcolor = "navy";// for titles
		public final static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		public final static DateFormat timeFormat = new SimpleDateFormat("HH:mm");
		final static Locale local = new Locale("Latin","US");
		final static TimeZone tzone = TimeZone.getTimeZone("America/Indiana/Indianapolis");
		// final static GregorianCalendar current_cal = new GregorianCalendar(tzone, local);
    //
    // basic constructor
    public Helper(boolean deb){
				//
				// initialize
				//
    }
		/*
			final static String getHashCodeOf(String buffer){

			String key = "Apps Secret Key "+getToday();
			byte[] out = performDigest(buffer.getBytes(),buffer.getBytes());
			String ret = bytesToHex(out);
			return ret;
			// System.err.println(ret);

			}
		*/
    final static byte[] performDigest(byte[] buffer, byte[] key) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(buffer);
            return md5.digest(key);
        } catch (Exception e) {
						System.err.println(e);
        }
        return null;
    }

    final static String bytesToHex(byte in[]) {
				byte ch = 0x00;
				int i = 0;
				if (in == null || in.length <= 0)
						return null;
				String pseudo[] = {"0", "1", "2",
													 "3", "4", "5", "6", "7", "8",
													 "9", "A", "B", "C", "D", "E",
													 "F"};
				StringBuffer out = new StringBuffer(in.length * 2);
				while (i < in.length) {
						ch = (byte) (in[i] & 0xF0); // Strip off high nibble

						ch = (byte) (ch >>> 4);
						// shift the bits down

						ch = (byte) (ch & 0x0F);
						// must do this is high order bit is on!

						out.append(pseudo[ (int) ch]); // convert the nibble to a String Character
						ch = (byte) (in[i] & 0x0F); // Strip off low nibble
						out.append(pseudo[ (int) ch]); // convert the nibble to a String Character
						i++;
				}
				String rslt = new String(out);
				return rslt;
    }
    //
    /**
     * Adds escape character before certain characters
     *
     */
    final static String escapeIt(String s) {

				StringBuffer safe = new StringBuffer(s);
				int len = s.length();
				int c = 0;
				boolean noEscapeBefore = true;
				while (c < len) {
						if ((safe.charAt(c) == '\'' ||
								 safe.charAt(c) == '"') && noEscapeBefore){
								safe.insert(c, '\\');
								c += 2;
								len = safe.length();
								noEscapeBefore = true;
						}
						else if(safe.charAt(c) == '\\'){ // to avoid double \\ before '
								noEscapeBefore = false;
								c++;
						}
						else {
								noEscapeBefore = true;
								c++;
						}
				}
				return safe.toString();
    }
    //
    // users are used to enter comma in numbers such as xx,xxx.xx
    // as we can not save this in the DB as a valid number
    // so we remove it
    public final static String cleanNumber(String s) {

				if(s == null) return null;
				String ret = "";
				int len = s.length();
				int c = 0;
				int ind = s.indexOf(",");
				if(ind > -1){
						ret = s.substring(0,ind);
						if(ind < len)
								ret += s.substring(ind+1);
				}
				else
						ret = s;
				return ret;
    }

    /**
     * Replaces the special chars that has certain meaning in html
     *
     * @param s The passing string
     * @return string The modified string
     */
    public final static String replaceSpecialChars(String s) {
				char ch[] ={'\'','\"','>','<'};
				String entity[] = {"&#39;","&#34;","&gt;","&lt;"};
				//
				// &#34; = &quot;

				String ret ="";
				int len = s.length();
				int c = 0;
				boolean in = false;
				while (c < len) {
						for(int i=0;i< entity.length;i++){
								if (s.charAt(c) == ch[i]) {
										ret+= entity[i];
										in = true;
								}
						}
						if(!in) ret += s.charAt(c);
						in = false;
						c ++;
				}
				return ret;
    }

		public final static String replaceQuote(String s) {
				char ch[] ={'\''};
				String entity[] = {"_"};
				//
				// &#34; = &quot;

				String ret ="";
				int len = s.length();
				int c = 0;
				boolean in = false;
				while (c < len) {
						for(int i=0;i< entity.length;i++){
								if (s.charAt(c) == ch[i]) {
										ret+= entity[i];
										in = true;
								}
						}
						if(!in) ret += s.charAt(c);
						in = false;
						c ++;
				}
				return ret;
    }
		/*
		public final static Connection getNwConnection(){
				Connection con = null;
				String dbSql =
						//
						// production
						// removed connection string, see web.xml for more info
				try{
						Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();					
						con = DriverManager.getConnection(dbSql);

				}
				catch(Exception ex){
						logger.error(ex);			
				}
				return con;
		}
		*/
    public final static void databaseClean(Statement stmt,
																					 ResultSet rs) {
				databaseDisconnect(null, stmt, rs);
    }			
    public final static void databaseDisconnect(Statement stmt,
																								ResultSet rs) {
				databaseDisconnect(null, stmt, rs);
    }	
		public final static Connection getConnection(){
				Connection con = null;
				int trials = 0;
				boolean pass = false;
				while(trials < 3 && !pass){
						try{
								trials++;
								logger.debug("Connection try "+trials);
								Context initCtx = new InitialContext();
								Context envCtx = (Context) initCtx.lookup("java:comp/env");
								DataSource ds = (DataSource)envCtx.lookup("jdbc/MySQL_timer");
								con = ds.getConnection();
								if(con == null){
										String str = " Could not connect to DB ";
										logger.error(str);
								}
								else{
										pass = testCon(con);
										if(pass){
												c_con++;
												logger.debug("Got connection: "+c_con);
												logger.debug("Got connection at try "+trials);
										}
								}
						}
						catch(Exception ex){
								logger.error(ex);
						}
				}
				return con;

		}
		//
		// we want to change yyyy-mm-dd format to mm/dd/yyyy format
		//
		public final static String changeDateFormat(String val){
				String ret = "";
				if(val != null){
						if(val.indexOf("-") > 0){
								String arr[] = val.split("-");
								if(arr.length == 3){
										ret = arr[1]+"/"+arr[2]+"/"+arr[0];
								}
						}
						else if(val.indexOf("/") > 0){
								String arr[] = val.split("/");
								if(arr.length == 3){
										ret = arr[2]+"-"+arr[0]+"-"+arr[1];
								}										
						}
						else{
								ret = val;
						}
				}
				else{
						ret = val;
				}
				return ret;
		}
		final static boolean testCon(Connection con){
				boolean pass = false;
				Statement stmt  = null;
				ResultSet rs = null;
				String qq = "select 1+1";
				try{
						if(con != null){
								stmt = con.createStatement();
								logger.debug(qq);
								rs = stmt.executeQuery(qq);
								if(rs.next()){
										pass = true;
								}
						}
						rs.close();
						stmt.close();
				}
				catch(Exception ex){
						logger.error(ex+":"+qq);
				}
				return pass;
		}

		/**
     * Disconnect the database and related statements and result sets
     *
     * @param con The database connection
     * @param stmt A database query
     * @param rs   A database result
     */
    public final static void databaseDisconnect(Connection con,
																								Statement stmt,
																								ResultSet rs) {
				try {
						if(rs != null) rs.close();
						rs = null;
						if(stmt != null) stmt.close();
						stmt = null;
						if(con != null) con.close();
						con = null;

						logger.debug("Closed Connection "+c_con);
						c_con--;
						if(c_con < 0) c_con = 0;
				}
				catch (Exception e) {
						e.printStackTrace();
				}
				finally{
						if (rs != null) {
								try { rs.close(); } catch (SQLException e) { ; }
								rs = null;
						}
						if (stmt != null) {
								try { stmt.close(); } catch (SQLException e) { ; }
								stmt = null;
						}
						if (con != null) {
								try { con.close(); } catch (SQLException e) { ; }
								con = null;
						}
				}
    }
    public final static void databaseDisconnect(Connection con){
				try {
						if(con != null) con.close();
						con = null;
						logger.debug("Closed Connection "+c_con);
						c_con--;
						if(c_con < 0) c_con = 0;
				}
				catch (Exception e) {
						e.printStackTrace();
				}
				finally{
						if (con != null) {
								try { con.close(); } catch (SQLException e) { ; }
								con = null;
						}
				}
    }		
		/**
     * Disconnect the database and related statements and result sets
     *
     * @param con The database connection
     * @param stmt A database query
     * @param rs   A database result
     */
    public final static void databaseDisconnect(Connection con,
																								PreparedStatement stmt,
																								ResultSet rs) {
				try {
						if(rs != null) rs.close();
						rs = null;
						if(stmt != null) stmt.close();
						stmt = null;
						if(con != null) con.close();
						con = null;

						logger.debug("Closed Connection "+c_con);
						c_con--;
						if(c_con < 0) c_con = 0;
				}
				catch (Exception e) {
						e.printStackTrace();
				}
				finally{
						if (rs != null) {
								try { rs.close(); } catch (SQLException e) { ; }
								rs = null;
						}
						if (stmt != null) {
								try { stmt.close(); } catch (SQLException e) { ; }
								stmt = null;
						}
						if (con != null) {
								try { con.close(); } catch (SQLException e) { ; }
								con = null;
						}
				}
    }
    public final static void databaseDisconnect(Connection con,
																								ResultSet rs,
																								Statement... stmt) {
				try {
						if(rs != null) rs.close();
						rs = null;
						if(stmt != null){
								for(Statement one:stmt){
										if(one != null)
												one.close();
										one = null;
								}
						}
						if(con != null) con.close();
						con = null;
						logger.debug("Closed Connection "+c_con);
						c_con--;
						if(c_con < 0) c_con = 0;
				}
				catch (Exception e) {
						e.printStackTrace();
				}
				finally{
						if (rs != null) {
								try { rs.close(); } catch (SQLException e) { }
								rs = null;
						}
						if (stmt != null) {
								try {
										for(Statement one:stmt){										
												if(one != null)
														one.close(); 
												one = null;
										}
								} catch (SQLException e) { }
						}
						if (con != null) {
								try { con.close(); } catch (SQLException e) { }
								con = null;
						}
				}
    }		
    /**
     * Write the number in bbbb.bb format needed for currency.
     *
     * = toFixed(2)
     * @param dd The input double number
     * @return The formated number as string
     */
    public final static String formatNumber(double dd){
				//
				String str = ""+dd;
				String ret="";
				int l = str.length();
				int i = str.indexOf('.');
				int r = i+3;  // required length to keep only two decimal
				// System.err.println(str+" "+l+" "+r);
				if(i > -1 && r<l){
						ret = str.substring(0,r);
				}
				else{
						ret = str;
				}
				return ret;
    }

		/**
		 * Formats a number with only 2 decimal
		 *
     * Useful for currency numbers
     *
     * @param that The number string to format
     * @return The formatted number string
     */
    final static String formatNumber(String that){

				int ind = that.indexOf(".");
				int len = that.length();
				String str = "";
				if(ind == -1){  // whole integer
						str = that + ".00";
				}
				else if(len-ind == 2){  // one decimal
						str = that + "0";
				}
				else if(len - ind > 3){ // more than two
						str = that.substring(0,ind+3);
				}
				else str = that;

				return str;
    }
		public final static String getCurrentTime(){
				Calendar cal = Calendar.getInstance();
				String time = timeFormat.format(cal.getTime());
				return time;
		}

    public final static String getToday(Calendar cal){

				String day="",month="",year="";
				int mm =  (cal.get(Calendar.MONTH)+1);
				int dd =   cal.get(Calendar.DATE);
				year = ""+ cal.get(Calendar.YEAR);
				if(mm < 10) month = "0";
				month += mm;
				if(dd < 10) day = "0";
				day += dd;
				return month+"/"+day+"/"+year;
    }
    public final static String getToday(){

				String day="",month="",year="";
				GregorianCalendar cal = new GregorianCalendar(tzone, local);
				
				int mm =  (cal.get(Calendar.MONTH)+1);
				int dd =   cal.get(Calendar.DATE);
				year = ""+ cal.get(Calendar.YEAR);
				if(mm < 10) month = "0";
				month += mm;
				if(dd < 10) day = "0";
				day += dd;
				return month+"/"+day+"/"+year;
    }
    public final static String getYesterday(){

				String day="",month="",year="";
				GregorianCalendar cal = new GregorianCalendar(tzone, local);
				cal.add(Calendar.DAY_OF_MONTH, -1);
				int mm =  (cal.get(Calendar.MONTH)+1);
				int dd =   cal.get(Calendar.DATE);
				year = ""+ cal.get(Calendar.YEAR);
				if(mm < 10) month = "0";
				month += mm;
				if(dd < 10) day = "0";
				day += dd;
				return month+"/"+day+"/"+year;
    }		
		public final static int getCurrentYear(){
				int year=2017;
				GregorianCalendar cal = new GregorianCalendar(tzone, local);
				year = cal.get(Calendar.YEAR);
				return year;
		}
    public final static int[] get_today(Calendar cal) {
				//
				// GregorianCalendar cal = new GregorianCalendar(tzone, local);
				int[] ret_val = new int[3];
				ret_val[0] = cal.get(Calendar.MONTH) + 1;
				ret_val[1] = cal.get(Calendar.DATE);
				ret_val[2] = cal.get(Calendar.YEAR);
				return ret_val;
    }

    /**
     * Gets matching list from ldap for a given substring of a user name.
     *
     * @param subid the substring of the userid, subid has to be at least
     * two characters otherwise the ldap will hang for some reason.
     * @return a list of matching caases.
     */
    public final static String[] getMatchList(String subid){
				Hashtable<String, String> env = new Hashtable<String, String>(11);
				Vector<String> vec = new Vector<String>();
				String userid = "";
				vec.add(userid);
				//
				if (!connectToServer(env)){
						System.err.println("Error Connecting to LDAp Server");
						return null;
				}
				try{
						DirContext ctx = new InitialDirContext(env);
						SearchControls ctls = new SearchControls();
						String[] attrIDs = {"uid"};
						//
						ctls.setReturningAttributes(attrIDs);
						ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
						String filter = "(uid="+subid+"*)";
						NamingEnumeration<SearchResult> answer = ctx.search("", filter, ctls);
						while(answer.hasMore()){
								//
								SearchResult sr = answer.next();
								Attributes atts = sr.getAttributes();

								Attribute user = atts.get("uid");
								if (user != null){
										userid = user.get().toString();
										vec.add(userid);
								}
						}
				}
				catch(Exception ex){
						System.err.println(ex);
				}
				int size = vec.size();
				String str[] = new String[size];
				for(int i=0;i<size;i++){
						str[i] = vec.get(i);
				}
				vec = null;
				return str;
    }

    /**
     * Connect to ldap server.
     *
     * @return boolean true if ok or false of not
     */
		final static boolean connectToServer(Hashtable<String, String> env){

				String providerUtil ="ldap://ldap.cityhall.city.bloomington.in.us:389/o=city.bloomington.in.us";
				env.put(Context.INITIAL_CONTEXT_FACTORY,
								"com.sun.jndi.ldap.LdapCtxFactory");
				env.put(Context.PROVIDER_URL, providerUtil);
				env.put(Context.SECURITY_AUTHENTICATION, "simple");
				env.put(Context.SECURITY_PRINCIPAL,
								"uid=admin, o=city.bloomington.in.us");
				env.put(Context.SECURITY_CREDENTIALS, "your pass goes here");
				try {
						DirContext ctx = new InitialDirContext(env);
				} catch (NamingException ex) {
						return false;
				}
				return true;
    }


    /**
     * Applies initial caps to a word
     *
     * @param str_in The string to format
     * @return The formatted string
     */
    final static String initCapWord(String str_in){
				String ret = "";
				if(str_in !=  null){
						if(str_in.length() == 0) return ret;
						else if(str_in.length() > 1){
								ret = str_in.substring(0,1).toUpperCase()+
										str_in.substring(1).toLowerCase();
						}
						else{
								ret = str_in.toUpperCase();
						}
				}
				// System.err.println("initcap "+str_in+" "+ret);
				return ret;
    }

    /**
     * Applies initial caps to a phrase
     *
     * @param str_in The phrase to format
     * @return The formatted string
     */
    final static String initCap(String str_in){
				String ret = "";
				if(str_in != null){
						if(str_in.indexOf(" ") > -1){
								String[] str = str_in.split("\\s"); // any space character
								for(int i=0;i<str.length;i++){
										if(i > 0) ret += " ";
										ret += initCapWord(str[i]);
								}
						}
						else
								ret = initCapWord(str_in);// it is only one word
				}
				return ret;
    }

    //
    // check the user privileges
    //
    final static void forceLogout(PrintWriter out,
																	String title,
																	String url){
				out.println("<HTML><HEAD><TITLE>"+title+" </TITLE>");
				out.println("</HEAD><BODY>");
				out.println("<h3>Session timed out. <a href="+
										url+"Login>Click here to login "+
										"again</a></h3>");
				out.println("</body>");
				out.println("</html>");
				out.close();
    }
		public final static String getDateAfter(final String dt, final int days){
				String dt2 = "";
				if(days == 0){
						return dt;
				}
				try{
						GregorianCalendar cal = new GregorianCalendar(tzone, local);
						int yy = Integer.parseInt(dt.substring(dt.lastIndexOf("/")+1));
						int mm = Integer.parseInt(dt.substring(0,dt.indexOf("/")));
						int dd = Integer.parseInt(dt.substring(dt.indexOf("/")+1,dt.lastIndexOf("/")));
						cal.set(Calendar.YEAR, yy);
						cal.set(Calendar.MONTH, mm - 1);
						cal.set(Calendar.DATE, dd);
						cal.add(Calendar.DATE, days);
						mm = cal.get(Calendar.MONTH) + 1;
						dd = cal.get(Calendar.DATE);
						yy = cal.get(Calendar.YEAR);
						if(mm < 10){
								dt2 = "0";
						}
						dt2 += ""+mm+"/";
						if(dd < 10){
								dt2 +="0";
						}
						dt2 += dd+"/";
						dt2 += yy;
				}catch(Exception ex){
						System.err.println("helper getDate after "+ex);
				}
				return dt2;

		}
		// original date in mm/dd/yyyy or mm-dd-yyyy format
		// returns date in yymmdd format
		public final static String getYymmddDate(final String dt){
				if(dt == null || dt.equals("")) return dt;
				String ret = "", yy="", mm="", dd="";
				String separator="/";
				if(dt.indexOf("-") > 0){
						separator="-";
				}
				try{
						yy = dt.substring(dt.lastIndexOf(separator)+3); // last two digits
						mm = dt.substring(0,dt.indexOf(separator));
						dd = dt.substring(dt.indexOf(separator)+1,dt.lastIndexOf(separator));
						if(mm.length() == 1){
								mm = "0"+mm;
						}
						if(dd.length() == 1){
								dd = "0"+dd;
						}
						ret = yy+mm+dd;
						
				}catch(Exception ex){
						System.err.println(ex);
				}
				return ret;
		}

		final static int[] getStartPayPeriod(int dd, int mm, int yy, Calendar cal){

				// int yy = 2014, mm = 3, dd=2;
				// GregorianCalendar cal = new GregorianCalendar(tzone,local);
				int mm2=0,yy2=0,dd2=0;
				cal.set(Calendar.HOUR,4);
				cal.set(Calendar.MINUTE,0);
				cal.set(Calendar.SECOND,0);
				// start pay period 1998 day of the year 11
				cal.set(Calendar.YEAR, 1998);
				cal.set(Calendar.MONTH, 0);
				cal.set(Calendar.DATE, 12);
				boolean found = false;
				do{
						int days=14;
						cal.add(Calendar.DATE, days);
						mm2 = cal.get(Calendar.MONTH) + 1;
						dd2 = cal.get(Calendar.DATE);
						yy2 = cal.get(Calendar.YEAR);
						if(yy2 < yy ) continue;
						else if(yy2 == yy){
								if(mm2 > mm){
										found = true;
								}
								else if(mm2 < mm) continue;
								else if(dd2 > dd){
										found = true;
								}
						}
				}while(!found);
				cal.add(Calendar.DATE, -14);
				// if the day of the week is not 2 (Monday) we add 1
				int dow = cal.get(Calendar.DAY_OF_WEEK); // we want this to be 2
				if(dow == 1){
						cal.add(Calendar.DATE, 1);
						dow = cal.get(Calendar.DAY_OF_WEEK);
				}
				mm2 = cal.get(Calendar.MONTH) + 1;
				dd2 = cal.get(Calendar.DATE);
				yy2 = cal.get(Calendar.YEAR);
				// System.err.println(" Day of week "+dow);
				// System.err.println(" pp start "+mm2+"/"+dd2+"/"+yy2);
				int ret[] = {mm2,dd2, yy2};
				return ret;
		}
		final static int[] getEndPayPeriod(int[] data, Calendar cal){

				// int yy = 2014, mm = 3, dd=2;
				// GregorianCalendar cal = new GregorianCalendar(tzone,local);
				int mm2=0,yy2=0,dd2=0;
				cal.set(Calendar.HOUR,4);
				cal.set(Calendar.MINUTE,0);
				cal.set(Calendar.SECOND,0);
				cal.set(Calendar.MONTH, data[0]-1);
				cal.set(Calendar.DATE, data[1]);
				cal.set(Calendar.YEAR, data[2]);
				cal.add(Calendar.DATE, 13);
				mm2 = cal.get(Calendar.MONTH) + 1;
				dd2 = cal.get(Calendar.DATE);
				yy2 = cal.get(Calendar.YEAR);
				// System.err.println(" pp end "+mm2+"/"+dd2+"/"+yy2);
				int ret[] = {mm2, dd2, yy2};
				return ret;
		}
		// assume date is in mm/dd/yyyy format
		public final static int getDayInt(String dt) {
				int dd = 0;
				try{
						dd = Integer.parseInt(dt.substring(dt.indexOf("/")+1,dt.lastIndexOf("/")));
				}catch(Exception ex){
						System.err.println(ex);
				}
				return dd;
		}		
		/**
		 * for this date, we need to find the day name (Mon, Tue, ..)
		 */
		public final static String getDay(String dt) {
				Calendar cal = new GregorianCalendar(tzone,local);
				return getDay(dt, cal);
		}
		public final static String getDay(String dt, Calendar cal) {

				String day = "";

				String days[] = {"","Sun","Mon","Tue","Wed","Thu","Fri","Sat"};
				if(!dt.equals("")){
						try{
								int yy = Integer.parseInt(dt.substring(dt.lastIndexOf("/")+1));
								int mm = Integer.parseInt(dt.substring(0,dt.indexOf("/")));
								int dd = Integer.parseInt(dt.substring(dt.indexOf("/")+1,dt.lastIndexOf("/")));
								cal.set(Calendar.YEAR, yy);
								cal.set(Calendar.MONTH, mm - 1);
								cal.set(Calendar.DATE, dd);
								//
								// days of week start from 1-Sun, 7-Sat
								//
								int dayId = cal.get(Calendar.DAY_OF_WEEK);
								day = days[dayId];
						}
						catch(Exception ex){
								logger.error(ex);
						}
				}
				return day;
    }

}






















































