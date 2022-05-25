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

public class Location{

		static final long serialVersionUID = 3700L;	
		static Logger logger = LogManager.getLogger(Location.class);
		static final double default_radius=0.;
    String id="", ip_address="", name="",
				street_address="";
		Double latitude=0.,longitude=0., radius=0.;
		//
		public Location(){
		}
		public Location(String val){
				//
				setId(val);
    }		
		public Location(String val, String val2, String val3){
				setId(val);
				setIp_address(val2);
				setName(val3);
    }
		public Location(String val, String val2, String val3, String val4, Double val5, Double val6, Double val7){
				setId(val);
				setIp_address(val2);
				setName(val3);
				setStreet_address(val4);
				setLatitude(val5);
				setLongitude(val6);
				setRadius(val7);
    }		
		public int hashCode(){
				int seed = 29;
				if(!id.isEmpty()){
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
    public String getName(){
				return name;
    }
    public String getStreet_address(){
				return street_address;
    }
		public Double getLatitude(){
				return latitude;
    }
		public Double getLongitude(){
				return longitude;
    }
		public Double getRadius(){
				return radius;
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
    public void setName(String val){
				if(val != null)
						name = val.trim();
    }
    public void setStreet_address(String val){
				if(val != null)
						street_address = val;
    }
    public void setLatitude(Double val){
				if(val != null)
						latitude = val;
    }
		public void setLongitude(Double val){
				if(val != null)
						longitude = val;
    }
    public void setRadius(Double val){
				if(val != null)
						radius = val;
    }
		public boolean hasLatLng(){
				return latitude > 0 && longitude < 0;
		}
		public boolean verify(){
				if(ip_address.isEmpty() && (latitude == 0. || longitude == 0.))
						return false;
				return true;
		}
    public String toString(){
				if(!name.isEmpty()){
						return name;
				}
				return getInfo();
    }
		public String getInfo(){
				String ret = "";
				if(!name.isEmpty()) {
						ret += name;
				}
				if(!ip_address.isEmpty()){
						if(!ret.isEmpty()) ret +=" ";						
						ret += "("+ip_address+")";
				};
				if(!street_address.isEmpty()){
						if(!ret.isEmpty()) ret +=" ";
						ret += street_address;
				}
				if(hasLatLng()){
						if(!ret.isEmpty()) ret +=" ";
						ret += "("+latitude+", "+longitude+")";
				}
				return ret;
		}
		//
		public String doSelect(){
				String back = "";
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String qq = "select id,ip_address,name,street_address,latitude,longitude,radius "+
						"from locations where id=?";
				/**
				String qq = "select id,ip_address,name "+
						"from locations where id=?";
				*/
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
								setIp_address(rs.getString(2));
								setName(rs.getString(3));
								setStreet_address(rs.getString(4));
								setLatitude(rs.getDouble(5));
								setLongitude(rs.getDouble(6));
								setRadius(rs.getDouble(7));
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
				String qq = " insert into locations values(0,?,?,?,?, ?,?)";
				// String qq = " insert into locations values(0,?,?)";				
				if(!verify()){
						msg = "ip address or 'latitude and longitude' are required"; 
						return msg;
				}
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could not connect to DB ";
						return msg;
				}
				try{
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, ip_address);
						if(name.isEmpty())
								pstmt.setNull(2, Types.VARCHAR);
						else
								pstmt.setString(2, name);
						if(street_address.isEmpty())
								pstmt.setNull(3, Types.VARCHAR);
						else
								pstmt.setString(3, street_address);
						pstmt.setDouble(4, latitude);
						pstmt.setDouble(5, longitude);
						pstmt.setDouble(6, radius);
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
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "update locations set ip_address=?,name=?,street_address=?,latitude=?,longitude=?,radius=? where id=?";
				//
				if(!verify()){
						msg = "ip address or 'latitude and longitude' are required"; 
						return msg;
				}
				con = Helper.getConnection();
				if(con == null){
						msg = "Could not connect to DB ";
						return msg;
				}
				try{
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, ip_address);
						if(name.isEmpty())
								pstmt.setNull(2, Types.VARCHAR);
						else
								pstmt.setString(2, name);
						if(street_address.isEmpty())
								pstmt.setNull(3, Types.VARCHAR);
						else
								pstmt.setString(3, street_address);
						pstmt.setDouble(4, latitude);
						pstmt.setDouble(5, longitude);
						pstmt.setDouble(6, radius);
						pstmt.setString(7, id); 
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
				String qq = " delete from locations where id=?";
				if(id.isEmpty()){
						msg = "id is required";
						return msg;
				}
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

		/**
			 // new changes to database
			 alter table locations add (street_address varchar(128),latitude double default 0, longitude double default 0, radius double default 0);
			  alter table locations modify ip_address varchar(15) default null;
				alter table locations drop index ip_address;
				alter table time_block_logs add location_id varchar(10) default null;
				//
				insert into locations values(0,null,'Alison Jukebox','351 S Washington St 47401', 39.16308255,-86.53213975,0);
				insert into locations values(0,null,'Banneker','930 W 7th St 47404',39.16892229,-86.54504903,0),                                                                (0,null,'Switchyard Park','1601 S Rogers St',39.14678876,-86.53672338,0),                                                                                       (0,null,'Bryan Park Pool','1020 S Woodlawn Ave 47401',39.15484498, -86.52331411,0),                                                                             (0,null,'Mills Park Pool','1100 W 14th ST 47404',39.17605119, -86.5447654,0),                                                                                   (0,null,'Cascades Golf Course','3550 N Kinser Pike 47404',39.2012919, -86.5390416,0),                                                                           (0,null,'Frank Southern Arena','2100 S Henderson 47401',39.14291292, -86.52736103,0),                                                                           (0,null,'Twin Lakes Sports Park','2350 W Bloomfield Rd',39.15383286, -86.56754897,0),                                                                           (0,null,'Twin Lakes Rec Center','1700 W Bloomfield Rd 47403',39.15621113,  -86.56335985,0),                                                                     (0,null,'Olcott Park','2300 E Canada Drive 47401',39.12563036,  -86.51051401,0),                                                                                (0,null,'Winslow Sports Complex','2120 S Highland Ave',39.14170598,  -86.5167582,0),
				(0,null,'Walid Home',null,39.1380836, -86.5527257,0);
				

				
				

		 */
}
