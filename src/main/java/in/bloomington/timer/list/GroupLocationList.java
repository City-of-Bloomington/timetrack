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

public class GroupLocationList{

    static Logger logger = LogManager.getLogger(GroupLocationList.class);
    static final long serialVersionUID = 3800L;
    String group_id = "";
    Set<String> ipSet = new HashSet<>();
    List<GroupLocation> groupLocations = null;
	
    public GroupLocationList(){
    }
    public GroupLocationList(String val){
	setGroup_id(val);
    }		
    public List<GroupLocation> getGroupLocations(){
	return groupLocations;
    }
    public Set<String> getIpSet(){
	return ipSet;
    }
    public void setGroup_id(String val){
	if(val != null && !val.equals("-1"))
	    group_id = val;
    }
    //
    // check if this ipset has certain ip address
    //
    public boolean ipSetIncludes(String ipAddress){
	return !ipSet.isEmpty() &&  ipSet.contains(ipAddress);
    }
    public String find(){
		
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "select t.id,t.group_id,t.location_id,"+
	    " l.ip_address,l.name,"+
	    " g.name,g.description,g.department_id,g.excess_hours_earn_type,"+
	    " g.allow_pending_accrual,"+
	    " g.clock_time_required,"+
	    " g.include_in_auto_batch,"+	    
	    " g.inactive "+
	    " from group_locations t "+
	    " left join locations l on l.id = t.location_id "+
	    " left join `groups` g on g.id = t.group_id";
	con = UnoConnect.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    return back;
	}
	String qw = "";
	if(!group_id.isEmpty()){
	    qw  += " t.group_id = ? ";
	}
	if(!qw.isEmpty()){
	    qq += " where "+qw;
	}
	try{
	    logger.debug(qq);
	    pstmt = con.prepareStatement(qq);
	    if(!group_id.isEmpty()){
		pstmt.setString(1, group_id);
	    }						
	    rs = pstmt.executeQuery();
	    if(groupLocations == null)
		groupLocations = new ArrayList<>();
	    while(rs.next()){
		GroupLocation one =
		    new GroupLocation(rs.getString(1),
				      rs.getString(2),
				      rs.getString(3),
																			
				      rs.getString(4), // location
				      rs.getString(5),
																			
				      rs.getString(6), // group
				      rs.getString(7),
				      rs.getString(8),
				      rs.getString(9),
				      rs.getString(10) != null,
				      rs.getString(11) != null,
				      rs.getString(13) != null,
				      rs.getString(13) != null
				      
				      );
		if(!groupLocations.contains(one))
		    groupLocations.add(one);
		ipSet.add(rs.getString(4));
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






















































