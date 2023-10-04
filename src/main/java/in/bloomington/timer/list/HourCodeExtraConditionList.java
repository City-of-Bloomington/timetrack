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

public class HourCodeExtraConditionList{

    static final long serialVersionUID = 900L;
    static Logger logger = LogManager.getLogger(HourCodeExtraConditionList.class);
    String hour_code_id = "";
    List<HourCodeExtraCondition> conditions = null;
    boolean active_only = false;
    public HourCodeExtraConditionList(){
    }
    public List<HourCodeExtraCondition> getConditions(){
	return conditions;
    }
    public void setActiveOnly(){
	active_only = true;
    }
    public void setHour_code_id (String val){
	if(val != null && !val.equals("-1"))
	    hour_code_id = val;
    }
    //
    // getters
    //
    public String find(){
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", qw="";
	String qq = "select id,hour_code_id,times_per_day,default_value_fixed,max_total_per_year,hour_code_associate_type,inactive "+
	    " from hour_code_extra_conditions ";
	logger.debug(qq);
	if(active_only){
	    qw += " inactive is null ";
	}
	if(!hour_code_id.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " hour_code_id = ?";
	}				
	if(!qw.isEmpty()){
	    qq += " where "+qw;
	}
	qq += " order by id ";
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
		if(conditions == null)
		    conditions = new ArrayList<>();
		HourCodeExtraCondition one =
		    new HourCodeExtraCondition(
					  rs.getString(1),
					  rs.getString(2),
					  rs.getInt(3),
					  rs.getString(4) != null,
					  rs.getDouble(5),
					  rs.getString(6),
					  rs.getString(7) != null);
		if(!conditions.contains(one))
		    conditions.add(one);
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
