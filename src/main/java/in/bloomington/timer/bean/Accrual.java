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

public class Accrual{

    static final long serialVersionUID = 3700L;	
    static Logger logger = LogManager.getLogger(Accrual.class);
    String id="", name="", description="", inactive="";
    int pref_max_level = 0; // no max level
    //
    public Accrual(){
	super();
    }
    public Accrual(String val){
	//
	setId(val);
    }		
    public Accrual(String val, String val2){
	//
	// initialize
	//
	setId(val);
	setName(val2);
    }
    public Accrual(String val, String val2, int val3){
	//
	// initialize
	//
	setId(val);
	setName(val2);
	setPref_max_level(val3);
    }
    public Accrual(String val, String val2, String val3, int val4){
	setId(val);
	setName(val2);
	setDescription(val3);
	setPref_max_level(val4);
    }				
    public Accrual(String val, String val2, String val3, int val4, boolean val5){
	setId(val);
	setName(val2);
	setDescription(val3);
	setPref_max_level(val4);
	setInactive(val5);
    }		
    public boolean equals(Object obj){
	if(obj instanceof Accrual){
	    Accrual one =(Accrual)obj;
	    return id.equals(one.getId());
	}
	return false;				
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
    public String getName(){
	return name;
    }
    public String getDescription(){
	return description;
    }
    public int getPref_max_level(){
	return pref_max_level;
    }
    public boolean getInactive(){
	return !inactive.equals("");
    }
    public boolean isInactive(){
	return !inactive.equals("");
    }
    public boolean isActive(){
	return inactive.equals("");
    }
    public boolean hasPref_max_leval(){
	return pref_max_level > 0;
    }
    //
    // setters
    //
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setName(String val){
	if(val != null)
	    name = val.trim();
    }
    public void setDescription(String val){
	if(val != null)
	    description = val.trim();
    }
    public void setPref_max_level(int val){
	if(val > 0)
	    pref_max_level = val;
    }		
    public void setInactive(boolean val){
	if(val)
	    inactive = "y";
    }		
    public String toString(){
	return name+": "+description;
    }
    //
    public String doSelect(){
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "select id,name,description,pref_max_level,inactive "+
	    "from accruals where id=?";
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
		setName(rs.getString(2));
		setDescription(rs.getString(3));
		setPref_max_level(rs.getInt(4));
		setInactive(rs.getString(5) != null);
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
	inactive=""; // default
	String qq = " insert into accruals values(0,?,?,?,?)";
	if(name.equals("")){
	    msg = "Earn code name is required";
	    return msg;
	}
	try{
	    con = UnoConnect.getConnection();
	    if(con == null){
		msg = "Could not connect to DB ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    msg = setParams(pstmt);
	    if(msg.equals("")){
		pstmt.executeUpdate();
		Helper.databaseDisconnect(pstmt, rs);
		//
		qq = "select LAST_INSERT_ID()";
		pstmt2 = con.prepareStatement(qq);
		rs = pstmt2.executeQuery();
		if(rs.next()){
		    id = rs.getString(1);
		}
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
    String setParams(PreparedStatement pstmt){
	String msg = "";
	int jj=1;
	try{
	    pstmt.setString(jj++, name);
	    if(description.isEmpty()){
		pstmt.setNull(jj++, Types.VARCHAR);
	    }
	    else
		pstmt.setString(jj++, description);
	    pstmt.setInt(jj++, pref_max_level);
	    if(inactive.isEmpty())
		pstmt.setNull(jj++, Types.CHAR);
	    else
		pstmt.setString(jj++, "y");						
	}
	catch(Exception ex){
	    msg += " "+ex;
	    logger.error(msg);
	}
	return msg;
    }
    public String doUpdate(){
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = " update accruals set name=?, description=?,pref_max_level=?,inactive=? where id=?";
	if(name.equals("")){
	    msg = "Earn code name is required";
	    return msg;
	}
	try{
	    con = UnoConnect.getConnection();
	    if(con == null){
		msg = "Could not connect to DB ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    msg = setParams(pstmt);
	    pstmt.setString(5, id);
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

}
