package in.bloomington.timer.list;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.*;
import java.sql.*;
import java.io.*;
import javax.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;

public class LocationList{

    static Logger logger = LogManager.getLogger(LocationList.class);
    static final long serialVersionUID = 3800L;
    String name = "",ip=""; // for service
    boolean hasLatLong = false, hasIpAddress=false;
    List<Location> locations = null;
	
    public LocationList(){
    }
    public void hasLatLong(){
	hasLatLong = true;
    }
    public void hasIpAddress(){
	hasIpAddress = true;
    }
    public void setIp(String val){
	if(val != null)
	    ip = val;
    }
    public List<Location> getLocations(){
	return locations;
    }
		
    public String find(){

	String back = "";
	PreparedStatement pstmt = null, pstmt2=null, pstmt3=null;
	ResultSet rs = null;
	Connection con = UnoConnect.getConnection();
	//
	// add the stats for the last two weeks, clean old data	
	String q1 = "delete from location_uses ";
	String q2 =
	    " insert into location_uses "+ 
	    " select t.location_id, count(*),now() from time_block_logs t where "+
	    " t.action_time >= DATE_SUB(CURRENT_DATE(), INTERVAL 14 DAY) "+
	    " and t.location_id is not null "+
	    " group by t.location_id  ";
	// 	
	String qq = "select l.id,l.ip_address,l.name,l.street_address,l.latitude,l.longitude,l.radius,t.time_used "+
	    "from locations l "+
	    "left join location_uses t on t.location_id = l.id ";
	String qw = "";
	if(hasLatLong){
	    if(!qw.isEmpty()) qw += " and ";	    
	    qw = " l.latitude <> 0 and l.longitude <> 0 ";
	}
	if(hasIpAddress){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " l.ip_address is not null ";
	}
	if(!ip.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " l.ip_address like ? ";
	}				
	if(!qw.isEmpty()){
	    qq += " where "+qw;
	}
	qq += " order by l.name ";
	if(con == null){
	    back = "Could not connect to DB";
	    return back;
	}
	try{
	    logger.debug(q1);
	    pstmt = con.prepareStatement(q1);
	    pstmt.executeUpdate();
	    logger.debug(q2);
	    pstmt2 = con.prepareStatement(q2);
	    pstmt2.executeUpdate();	    
	    logger.debug(qq);
	    pstmt3 = con.prepareStatement(qq);
	    if(!ip.isEmpty()){
		pstmt3.setString(1, ip);
	    }
	    rs = pstmt3.executeQuery();
	    if(locations == null)
		locations = new ArrayList<>();
	    while(rs.next()){
		Location one =
		    new Location(rs.getString(1),
				 rs.getString(2),
				 rs.getString(3),
				 rs.getString(4),
				 rs.getDouble(5),
				 rs.getDouble(6),
				 rs.getDouble(7),
				 rs.getString(8) == null?0:rs.getInt(8));
		locations.add(one);
	    }
	}
	catch(Exception ex){
	    back += ex+" : "+qq;
	    logger.error(back);
	}
	finally{
	    Helper.databaseDisconnect(rs, pstmt, pstmt2, pstmt3);
	    UnoConnect.databaseDisconnect(con);
	}
	return back;
    }
}

/**

	select l.id,l.ip_address,l.name,l.street_address,l.latitude,l.longitude,l.radius,tl.used_count
	from locations l,
	(select t.location_id location_id, count(*) used_count
	from time_block_logs t where
	t.action_time >= DATE_SUB(t.action_time, INTERVAL 14 DAY)
	and t.location_id is not null
	group by location_id 
	) tl
	where l.id=tl.location_id
	

select l.id,l.ip_address,l.name,l.street_address,l.latitude,l.longitude,l.radius,tl.used_count 
	    from locations l,
	    (select t.location_id location_id, count(*) used_count 
	    from time_block_logs t where 
	    t.action_time >= DATE_SUB(CURRENT_DATE(), INTERVAL 14 DAY) 
	    and t.location_id is not null 
	    group by location_id 
	    ) tl 
        where l.id=tl.location_id;

	;;
	 // create temporary table that will be 
	 create table location_uses (
	 location_id int unsigned,
	 time_used int ,
	 last_update date
	 )engine=InnoDB;
	 

 */




















































