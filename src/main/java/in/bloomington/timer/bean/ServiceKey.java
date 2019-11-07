package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.sql.*;
import javax.naming.*;
import javax.naming.directory.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;

public class ServiceKey{

		static final long serialVersionUID = 3700L;	
		static Logger logger = LogManager.getLogger(ServiceKey.class);
    String id="", key_name="", key_value="", inactive="";
		//
		public ServiceKey(){

		}
		public ServiceKey(String val){
				//
				setId(val);
    }
		public ServiceKey(String val, String val2){
				setKeyName(val);
				setKeyValue(val2);
    }				
		public ServiceKey(String val, String val2, String val3, boolean val4){
				setId(val);
				setKeyName(val2);
				setKeyValue(val3);
				setInactive(val4);
    }		
		public boolean equals(Object obj){
				if(obj instanceof ServiceKey){
						ServiceKey one =(ServiceKey)obj;
						return id.equals(one.getId());
				}
				return false;				
		}
		public int hashCode(){
				int seed = 17;
				if(!id.equals("")){
						try{
								seed += Integer.parseInt(id);
						}catch(Exception ex){
						}
				}
				return seed;
		}
    //
    // getters
    //
    public String getId(){
				return id;
    }
    public String getKeyName(){
				return key_name;
    }
    public String getKeyValue(){
				return key_value;
    }		
		public boolean getInactive(){
				return !inactive.equals("");
		}
    //
    // setters
    //
    public void setId(String val){
				if(val != null)
						id = val;
    }
    public void setKeyName(String val){
				if(val != null)
						key_name = val;
    }
    public void setKeyValue(String val){
				if(val != null)
						key_value = val;
    }		
    public void setInactive(boolean val){
				if(val)
					 inactive="y";
    }
		//
		public String doSelect(){
				String back = "";
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String qq = "select id,key_name,key_value,inactive "+
						"from service_keys where id=?";
				con = UnoConnect.getConnection();
				if(con == null){
						back = "Could not connect to DB";
						return back;
				}
				try{
						logger.debug(qq);
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1,id);
						rs = pstmt.executeQuery();
						if(rs.next()){
								setKeyName(rs.getString(2));
								setKeyValue(rs.getString(3));
								setInactive(rs.getString(4) != null);
						}
						else{
								back ="Record "+id+" Not found";
						}
				}
				catch(Exception ex){
						back += ex+":"+qq;
						logger.error(back);
				}
				finally{
						Helper.databaseDisconnect(pstmt, rs);
						UnoConnect.databaseDisconnect(con);
				}
				return back;
		}
		public String doSave(){
				Connection con = null;
				PreparedStatement pstmt = null, pstmt2=null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = " insert into service_keys values(0,?,?,null)";
				if(key_name.equals("")){
						msg = "key name is required";
						return msg;
				}
				if(key_value.equals("")){
						msg = "key value is required";
						return msg;
				}				
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could not connect to DB ";
						return msg;
				}
				try{
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, key_name);
						pstmt.setString(2, key_value);
						pstmt.executeUpdate();
						//
						qq = "select LAST_INSERT_ID()";
						pstmt2 = con.prepareStatement(qq);
						rs = pstmt2.executeQuery();
						if(rs.next()){
								id = rs.getString(1);
						}
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(rs, pstmt, pstmt2);
						UnoConnect.databaseDisconnect(con);
				}
				return msg;
		}
		public String doUpdate(){
				Connection con = null;
				PreparedStatement pstmt = null, pstmt2=null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = " update service_keys set key_name=?,key_value=?,inactive=? where id=?";
				if(key_name.equals("")){
						msg = "key name is required";
						return msg;
				}
				if(key_value.equals("")){
						msg = "key value is required";
						return msg;
				}				
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could not connect to DB ";
						return msg;
				}
				try{
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, key_name);
						pstmt.setString(2, key_value);
						if(inactive.equals(""))
								pstmt.setNull(3, Types.CHAR);
						else
								pstmt.setString(3, "y");								
						pstmt.setString(4, id);
						pstmt.executeUpdate();
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(pstmt, rs);
						UnoConnect.databaseDisconnect(con);
				}
				return msg;
		}		
		public String doDelete(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = " delete from service_keys where id=?";
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could not connect to DB ";
						return msg;
				}
				try{
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, id);
						pstmt.executeUpdate();
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(pstmt, rs);
						UnoConnect.databaseDisconnect(con);
				}
				return msg;
		}
		

}
