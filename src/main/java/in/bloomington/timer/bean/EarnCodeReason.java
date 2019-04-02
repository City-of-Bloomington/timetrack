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

public class EarnCodeReason{

		String id="", name = "", description="", inactive="";
		boolean debug = false;
		static Logger logger = LogManager.getLogger(EarnCodeReason.class);

    public EarnCodeReason(){
    }
    public EarnCodeReason(boolean deb){
				debug = deb;
    }		
    public EarnCodeReason(String val){
				setId(val);
    }				
    public EarnCodeReason(boolean deb,
													String val,
													String val2,
													String val3,
													boolean val4
									 ){
				debug = deb;
				setId(val);
				setName(val2);
				setDescription(val3);
				setInactive(val4);
    }
	
		public String getId(){
				return id;
    }
		public String getName(){
				return name;
		}

		public String getDescription(){
				return description;
    }
		public boolean getInactive(){
				return !inactive.equals("");
		}
		public boolean isActive(){
				return inactive.equals("");
		}
    //
    // setters
    //
    public void setId (String val){
				if(val != null)		
						id = val;
    }
    public void setName(String val){
				if(val != null){
						name = val.trim();
				}
    }
		public void setInactive(boolean val){
				if(val)
						inactive = "y";
		}
    public void setDescription(String val){
				if(val != null)
						description = val.trim();
    }
		public String toString(){
				return name;
		}
    public int hashCode(){
				int seed = 47;
				if(!id.equals("")){
						try{
								seed += Integer.parseInt(id)*29;
						}catch(Exception ex){
								// we ignore
						}
				}
				return seed;
    }		
   public  String doSave(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
		
				String qq = "insert into "+ 
						" earn_code_reasons values(0,?,?,null) ";
				String back = "";
				if(name.equals("")){
						back = "Name not set ";
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
						pstmt.setString(jj++, name);
						if(description.equals("")){
								pstmt.setNull(jj++, Types.VARCHAR);
						}
						else{
								pstmt.setString(jj++,description);
						}
						pstmt.executeUpdate();
						Helper.databaseDisconnect(pstmt, rs);
						//
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
						UnoConnect.databaseDisconnect(con);
				}
				return back;
    }			
    public String doUpdate(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
		
				String qq = 
						" update earn_code_reasons "+
						" set name=?, description=?,inactive=? "+			
						" where id = ? ";
				String back = "";
				if(id.equals("") || name.equals("")){
						back = " Name or id not set ";
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
						pstmt.setString(jj++, name);
						if(description.equals("")){
								pstmt.setNull(jj++, Types.VARCHAR);
						}
						else{						
								pstmt.setString(jj++,description);
						}
						if(inactive.equals("")){
								pstmt.setNull(jj++, Types.CHAR);
						}
						else{
								pstmt.setString(jj++,"y");
						}
						pstmt.setString(jj++,id);
						pstmt.executeUpdate();

				}
				catch(Exception ex){
						back += ex;
						logger.error(ex+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(pstmt, rs);
						UnoConnect.databaseDisconnect(con);
				}
				return back;
    }
    public String doDelete(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
		
				String qq = 
						" delete from earn_code_reasons "+
						" where id = ? ";
				String back = "";
				if(id.equals("")){
						back = " earnCodeReason id not set ";
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
						name = "";
						description = "";
				}
				catch(Exception ex){
						back += ex;
						logger.error(ex+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(pstmt, rs);
						UnoConnect.databaseDisconnect(con);
				}
				return back;
    }			
    public String doSelect(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
		
				String qq = "select name,description,inactive from "+
						" earn_code_reasons where id = ? ";
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
								setName(rs.getString(1));
								setDescription(rs.getString(2));
								setInactive(rs.getString(3) !=null);
						}
				}
				catch(Exception ex){
						back += ex;
						logger.error(ex+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(pstmt, rs);
						UnoConnect.databaseDisconnect(con);
				}
				return back;
    }	

}
