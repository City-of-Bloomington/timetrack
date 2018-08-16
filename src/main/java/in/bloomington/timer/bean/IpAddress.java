package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.sql.*;
import javax.naming.*;
import javax.naming.directory.*;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IpAddress{

		static final long serialVersionUID = 3700L;	
		static Logger logger = LogManager.getLogger(IpAddress.class);
    String id="", ip_address="", description="";
		//
		public IpAddress(){
		}
		public IpAddress(String val){
				//
				setId(val);
    }		
		public IpAddress(String val, String val2, String val3){
				//
				// initialize
				//
				setId(val);
				setIp_address(val2);
				setDescription(val3);
    }
		public int hashCode(){
				int seed = 29;
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
    public String getIp_address(){
				return ip_address;
    }
    public String getDescription(){
				return description;
    }		
    //
    // setters
    //
    public void setId(String val){
				if(val != null)
						id = val;
    }
    public void setIp_address(String val){
				if(val != null)
						ip_address = val.trim();
    }
    public void setDescription(String val){
				if(val != null)
						description = val.trim();
    }		
    public String toString(){
				return ip_address;
    }
		//
		public String doSelect(){
				String back = "";
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String qq = "select id,ip_address,description "+
						"from ip_allowed where id=?";
				con = Helper.getConnection();
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
								setIp_address(rs.getString(2));
								setDescription(rs.getString(3));
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
						Helper.databaseDisconnect(con, pstmt, rs);			
				}
				return back;
		}
		public String doSave(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = " insert into ip_allowed values(0,?,?)";
				if(ip_address.equals("")){
						msg = "ip address is required";
						return msg;
				}
				try{
						con = Helper.getConnection();
						if(con == null){
								msg = "Could not connect to DB ";
								return msg;
						}
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, ip_address);
						if(description.equals(""))
								pstmt.setNull(2, Types.VARCHAR);
						else
								pstmt.setString(2, description);
						pstmt.executeUpdate();
						qq = "select LAST_INSERT_ID()";
						pstmt = con.prepareStatement(qq);
						rs = pstmt.executeQuery();
						if(rs.next()){
								id = rs.getString(1);
						}
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(con, pstmt, rs);
				}
				return msg;
		}
		public String doUpdate(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = " update ip_allowed set ip_address=?,description=? where id=?";
				if(ip_address.equals("")){
						msg = "ip address is required";
						return msg;
				}
				try{
						con = Helper.getConnection();
						if(con == null){
								msg = "Could not connect to DB ";
								return msg;
						}
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, ip_address);
						if(description.equals(""))
								pstmt.setNull(2, Types.VARCHAR);
						else
								pstmt.setString(2, description);
						pstmt.setString(3, id);
						pstmt.executeUpdate();
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(con, pstmt, rs);
				}
				return msg;
		}
		public String doDelete(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = " delete from ip_allowed where id=?";
				if(id.equals("")){
						msg = "id is required";
						return msg;
				}
				try{
						con = Helper.getConnection();
						if(con == null){
								msg = "Could not connect to DB ";
								return msg;
						}
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, id);
						pstmt.executeUpdate();
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(con, pstmt, rs);
				}
				return msg;
		}				

}
