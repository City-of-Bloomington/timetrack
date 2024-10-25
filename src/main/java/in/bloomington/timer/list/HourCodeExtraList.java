package in.bloomington.timer.list;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Hashtable;
import java.text.*;
import java.util.Date;
import java.sql.*;
import javax.naming.*;
import javax.naming.directory.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;

public class HourCodeExtraList{

    static final long serialVersionUID = 900L;
    static Logger logger = LogManager.getLogger(HourCodeExtraList.class);
    String hour_code_id = "";
    List<HourCodeExtra> extras = null;
    boolean active_only = false;
    public HourCodeExtraList(){
    }
    public List<HourCodeExtra> getExtras(){
	return extras;
    }
    public void setActiveOnly(){
	active_only = true;
    }
    public void setHour_code_id(String val){
	if(val != null)
	    hour_code_id = val;
    }
    //
    //
    public String find(){
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", qw="";
	String qq = "select e.id,e.times_per_day,e.default_value_fixed,e.max_total_per_year,e.hour_code_associate_type,e.inactive "+
	    " from hour_code_extras e left join hour_code_extra_related r on r.hour_code_extra_id = e.id "; 
	logger.debug(qq);
	if(!hour_code_id.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " r.hour_code_id = ?";
	}				
	if(active_only){
	    qw += " e.inactive is null ";
	}
	if(!qw.isEmpty()){
	    qq += " where "+qw;
	}
	qq += " order by e.id ";
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = " Could not connect to DB ";
	    logger.error(msg);
	    return msg;
	}
	logger.debug(qq);
	try{
	    pstmt = con.prepareStatement(qq);
	    int jj=1;
	    if(!hour_code_id.isEmpty()){
		pstmt.setString(jj++, hour_code_id);								
	    }						
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		if(extras == null)
		    extras = new ArrayList<>();
		HourCodeExtra one =
		    new HourCodeExtra(
				      rs.getString(1),
				      rs.getInt(2),
				      rs.getString(3) != null,
				      rs.getDouble(4),
				      rs.getString(5),
				      rs.getString(6) != null);
		if(!extras.contains(one))
		    extras.add(one);
	    }
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
