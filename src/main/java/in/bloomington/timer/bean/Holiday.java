/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
package in.bloomington.timer.bean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.*;
import java.sql.*;
import java.text.*;
import in.bloomington.timer.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Holiday{

		String id="", date = "", description="";
		boolean debug = false;
		static Logger logger = LogManager.getLogger(Holiday.class);
		static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");	
    public Holiday(boolean deb){
				debug = deb;
    }
    public Holiday(boolean deb,
									 String val,
									 String val2,
									 String val3
									 ){
				debug = deb;
				setId(val);
				setDate(val2);
				setDescription(val3);
    }
	
		public String getId(){
				return id;
    }
		public String getDate(){
				return date;
		}

		public String getDescription(){
				return description;
    }	
    //
    // setters
    //
    public void setId (String val){
				if(val != null)		
						id = val;
    }
    public void setDate (String val){
				if(val != null){
						date = val;
				}
    }	
    public void setDescription(String val){
				if(val != null)
						description = val;
    }
   public  String doSave(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
		
				String qq = "insert into "+ 
						" holidays values(0,?,?) ";
				String back = "";
				if(date.equals("") || description.equals("")){
						back = " date or description not set ";
						return back;
				}
				con = UnoConnect.getConnection();				
				if(con == null){
						back = "Could not connect to DB ";
						return back;
				}
				try{
						if(debug){
								logger.debug(qq);
						}
						pstmt = con.prepareStatement(qq);
						int jj = 1;
						pstmt.setDate(jj++, new java.sql.Date(dateFormat.parse(date).getTime()));			
						pstmt.setString(jj++,description);			
						pstmt.executeUpdate();
						qq = "select LAST_INSERT_ID() ";
						if(debug){
								logger.debug(qq);
						}
						pstmt = con.prepareStatement(qq);			
						rs = pstmt.executeQuery();
						if(rs.next()){
								id = rs.getString(1);
						}				
			
				}
				catch(Exception ex){
						back += ex;
						logger.error(ex+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(pstmt, rs);
				}
				return back;
    }			
    public String doUpdate(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
		
				String qq = 
						" update holidays "+
						" set date=?, description=? "+			
						" where id = ? ";
				String back = "";
				if(id.equals("") || date.equals("") || description.equals("")){
						back = " date, description or id not set ";
						return back;
				}
				con = UnoConnect.getConnection();	
				if(con == null){
						back = "Could not connect to DB ";
						return back;
				}
				try{
						if(debug){
								logger.debug(qq);
						}
						pstmt = con.prepareStatement(qq);
						int jj = 1;
						pstmt.setDate(jj++, new java.sql.Date(dateFormat.parse(date).getTime()));			
						pstmt.setString(jj++,description);			
						pstmt.setString(jj++,id);
						pstmt.executeUpdate();

				}
				catch(Exception ex){
						back += ex;
						logger.error(ex+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(pstmt, rs);
				}
				return back;
    }
    public String doDelete(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
		
				String qq = 
						" delete from holidays "+
						" where id = ? ";
				String back = "";
				if(id.equals("")){
						back = " holiday id not set ";
						return back;
				}
				if(debug){
						logger.debug(qq);
				}
				con = UnoConnect.getConnection();	
				if(con == null){
						back = "Could not connect to DB ";
						return back;
				}
				try{
						pstmt = con.prepareStatement(qq);
						int jj = 1;
						pstmt.setString(jj++,id);
						pstmt.executeUpdate();
				}
				catch(Exception ex){
						back += ex;
						logger.error(ex+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(pstmt, rs);
				}
				return back;
    }			
    public String doSelect(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
		
				String qq = "select date_format(date,'%m/%d/%Y'),description from "+
						" holidays where id = ? ";
				String back = "";
				if(debug){
						logger.debug(qq);
				}
				con = UnoConnect.getConnection();				
				if(con == null){
						back = "Could not connect to DB ";
						return back;
				}
				try{
						pstmt = con.prepareStatement(qq);
						int jj = 1;
						pstmt.setString(jj,id);
						rs = pstmt.executeQuery();
						if(rs.next()){
								String str = rs.getString(1);
								String str2 = rs.getString(2);
								setDate(str);
								setDescription(str2);
						}
				}
				catch(Exception ex){
						back += ex;
						logger.error(ex+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(pstmt, rs);
				}
				return back;
    }	

}
