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

public class GroupLocation{

		static final long serialVersionUID = 3700L;	
		static Logger logger = LogManager.getLogger(GroupLocation.class);
    String id="", location_id="", group_id="";
		Group group = null;
		Location location = null;
		//
		public GroupLocation(){
		}
		public GroupLocation(String val){
				//
				setId(val);
    }		
		public GroupLocation(String val, String val2, String val3){
				//
				// initialize
				//
				setId(val);
				setGroup_id(val2);
				setLocation_id(val3);
    }
		public GroupLocation(String val,
												 String val2,
												 String val3,
												 String val4,
												 String val5){
				//
				// initialize
				//
				setId(val);
				setGroup_id(val2);
				setLocation_id(val3);
				location = new Location(val3, val4, val5);
    }
		public GroupLocation(String val,
												 String val2,
												 String val3,
												 
												 String val4,
												 String val5,
												 
												 String val6, // group
												 String val7,
												 String val8,
												 boolean val9
												 ){
				//
				// initialize
				//
				setId(val);
				setGroup_id(val2);
				setLocation_id(val3);
				location = new Location(val3, val4, val5);
				group = new Group(val2, val6, val7, val8, val9);
    }		
		public int hashCode(){
				int seed = 29;
				if(!group_id.equals("")){
						try{
								seed += Integer.parseInt(group_id);
								if(!location_id.equals("")){
										seed += Integer.parseInt(location_id);
								}
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
    public String getLocation_id(){
				return location_id;
    }
    public String getGroup_id(){
				return group_id;
    }
    public String getDepartment_id(){
				return ""; // not needed
    }				
    //
    // setters
    //
    public void setId(String val){
				if(val != null)
						id = val;
    }
    public void setGroup_id(String val){
				if(val != null && !val.equals("-1"))
						group_id = val;
    }
    public void setDepartment_id(String val){
				// not needed 
    }		
    public void setLocation_id(String val){
				if(val != null && !val.equals("-1"))
						location_id = val;
    }		
    public String toString(){
				return group_id+" "+location_id;
    }
		public Location getLocation(){
				if(location == null && !location_id.equals("")){
						Location one = new Location(location_id);
						String back = one.doSelect();
						if(back.equals("")){
								location = one;
						}
				}
				return location;
		}
		public Group getGroup(){
				if(group == null && !group_id.equals("")){
						Group one = new Group(group_id);
						String back = one.doSelect();
						if(back.equals("")){
								group = one;
						}
				}
				return group;
		}
		//
		public String doSelect(){
				String back = "";
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String qq = "select id,group_id,location_id "+
						"from group_locations where id=?";
				Connection con = UnoConnect.getConnection();
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
								setGroup_id(rs.getString(2));
								setLocation_id(rs.getString(3));
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
				}
				return back;
		}
		public String doSave(){
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				Connection con = null;
				String msg="", str="";
				String qq = " insert into group_locations values(0,?,?)";
				if(group_id.equals("")){
						msg = "group id is required";
						return msg;
				}
				if(location_id.equals("")){
						msg = "location id is required";
						return msg;
				}				
				try{
						con = UnoConnect.getConnection();
						if(con == null){
								msg = "Could not connect to DB ";
								return msg;
						}
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, group_id);
						pstmt.setString(2, location_id);
						pstmt.executeUpdate();
						Helper.databaseDisconnect(pstmt, rs);
						//
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
						Helper.databaseDisconnect(pstmt, rs);
				}
				return msg;
		}
		public String doUpdate(){

				PreparedStatement pstmt = null;
				ResultSet rs = null;
				Connection con = null;
				String msg="", str="";
				String qq = " update group_locations set group_id=?,location_id=? where id=?";
				if(group_id.equals("")){
						msg = "group id not set";
						return msg;
				}
				if(location_id.equals("")){
						msg = "location id not set";
						return msg;
				}				
				try{
						con = UnoConnect.getConnection();
						if(con == null){
								msg = "Could not connect to DB ";
								return msg;
						}
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, group_id);
						pstmt.setString(2, location_id);
						pstmt.setString(3, id);
						pstmt.executeUpdate();
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(pstmt, rs);
				}
				return msg;
		}
		public String doDelete(){
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				Connection con = null;
				String msg="", str="";
				String qq = " delete from group_locations where id=?";
				if(id.equals("")){
						msg = "id is required";
						return msg;
				}
				try{
						con = UnoConnect.getConnection();
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
						Helper.databaseDisconnect(pstmt, rs);
				}
				return msg;
		}				

}
