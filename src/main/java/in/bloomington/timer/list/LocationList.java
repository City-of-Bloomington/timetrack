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
    String name = ""; // for service
    List<Location> locations = null;
	
    public LocationList(){
    }
    public List<Location> getLocations(){
	return locations;
    }
		
    public String find(){
		
	String back = "";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection con = UnoConnect.getConnection();
	String qq = "select t.id,t.ip_address,t.name from locations t ";
	if(con == null){
	    back = "Could not connect to DB";
	    return back;
	}
	String qw = "";
	try{
	    logger.debug(qq);
	    pstmt = con.prepareStatement(qq);
	    rs = pstmt.executeQuery();
	    if(locations == null)
		locations = new ArrayList<>();
	    while(rs.next()){
		Location one =
		    new Location(rs.getString(1),
				 rs.getString(2),
				 rs.getString(3));
		locations.add(one);
	    }
	}
	catch(Exception ex){
	    back += ex+" : "+qq;
	    logger.error(back);
	}
	finally{
	    Helper.databaseDisconnect(pstmt, rs);
	    UnoConnect.databaseDisconnect(con);
	}
	return back;
    }
}






















































