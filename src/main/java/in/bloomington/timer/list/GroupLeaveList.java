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

public class GroupLeaveList{

    static Logger logger = LogManager.getLogger(GroupLeaveList.class);
    static final long serialVersionUID = 3800L;
    String group_id = "", salary_group_id="";
    List<GroupLeave> groupLeaves = null;
	
    public GroupLeaveList(){
    }
    public GroupLeaveList(String val){
	setGroup_id(val);
    }		
    public List<GroupLeave> getGroupLeaves(){
	return groupLeaves;
    }
    public void setGroup_id(String val){
	if(val != null && !val.equals("-1"))
	    group_id = val;
    }
    public void setSalary_group_id(String val){
	if(val != null && !val.equals("-1"))
	    salary_group_id = val;
    }    
    //
    //
    public String find(){
		
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "select t.id,t.group_id,t.salary_group_id,t.inactive "+
	    " from group_leaves t ";
	String qw = "";
	con = UnoConnect.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    return back;
	}
	if(!group_id.isEmpty()){
	    qw  += " t.group_id = ? ";
	}
	if(!salary_group_id.isEmpty()){
	    if(!qw.isEmpty())
		qw += " and ";
	    qw  += " t.salary_group_id = ? ";
	}
	if(!qw.isEmpty()){
	    qq += " where "+qw;
	}
	try{
	    int jj=1;
	    logger.debug(qq);
	    pstmt = con.prepareStatement(qq);
	    if(!group_id.isEmpty()){
		pstmt.setString(jj++, group_id);
	    }
	    if(!salary_group_id.isEmpty()){
		pstmt.setString(jj++, salary_group_id);
	    }	    
	    rs = pstmt.executeQuery();
	    if(groupLeaves == null)
		groupLeaves = new ArrayList<>();
	    while(rs.next()){
		GroupLeave one =
		    new GroupLeave(rs.getString(1),
				   rs.getString(2),
				   rs.getString(3),
				   rs.getString(4) !=null 
				   );
		if(!groupLeaves.contains(one))
		    groupLeaves.add(one);
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






















































