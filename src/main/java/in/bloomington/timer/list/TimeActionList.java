package in.bloomington.timer.list;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.ArrayList;
import java.util.List;
import java.text.*;
import java.util.Date;
import java.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;

public class TimeActionList{

    static final long serialVersionUID = 3700L;
    static Logger logger = LogManager.getLogger(TimeActionList.class);
    String document_id = "", id="", sortBy=" a.id desc ";
    boolean last_action = false;
    List<TimeAction> timeActions = null;
    public TimeActionList(){
    }
    public TimeActionList(String val){
	setDocument_id(val);
    }
    public void setId (String val){
	if(val != null)
	    id = val;
    }		
    public void setDocument_id (String val){
	if(val != null)
	    document_id = val;
    }
    public void onlyLastAction(){
	last_action = true;
    }
    public void setSortby(String val){
	if(val != null)
	    sortBy = val;
    }
    public List<TimeAction> getTimeActions(){
	return timeActions;
    }
    public String doIt(){
	return "";

    }

    //
    // getters
    //
    public String find(){
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", qw="";
	String qq = "select a.id,a.workflow_id,a.document_id,a.action_by,"+
	    " date_format(a.action_time,'%m/%d/%y %H:%i'),w.node_id,w.next_node_id from time_actions a join workflows w on w.id=a.workflow_id ";
	if(!id.equals("")){
	    qw += " w.id = ? ";
	}
	if(!document_id.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += " a.document_id = ? ";
	}
	if(!qw.equals("")){
	    qq += " where "+qw;
	}
	if(!sortBy.equals("")){
	    qq += " order by "+sortBy;
	}
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
	    if(!id.equals("")){
		pstmt.setString(jj++, id);
	    }
	    if(!document_id.equals("")){
		pstmt.setString(jj++, document_id);
	    }						
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		if(timeActions == null)
		    timeActions = new ArrayList<>();
		TimeAction one = new TimeAction(
						rs.getString(1),
						rs.getString(2),
						rs.getString(3),
						rs.getString(4),
						rs.getString(5),
						rs.getString(6),
						rs.getString(7));
		timeActions.add(one);
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
