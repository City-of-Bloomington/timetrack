package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.text.SimpleDateFormat;
import java.sql.*;
import javax.sql.*;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GroupShift implements Serializable{

    static Logger logger = LogManager.getLogger(GroupShift.class);
    static final long serialVersionUID = 1500L;
    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");				
    String id="", group_id="", shift_id="", inactive="";
    String start_date="", expire_date="";
    Group group = null;
    Shift shift = null;
    public GroupShift(){

    }		
    public GroupShift(String val){
	setId(val);
    }
		
    public GroupShift(String val,
		      String val2,
		      String val3,
		      String val4,
		      String val5,
		      boolean val6
		      ){
	setId(val);
	setGroup_id(val2);
	setShift_id(val3);
	setStartDate(val4);
	setExpireDate(val5);
	setInactive(val6);

    }
public GroupShift(String val,
		      String val2,
		      String val3,
		      String val4,
		      String val5,
		      boolean val6,

		      String val01, // group
		      String val02,
		      String val03,
		      String val04,
		      String val05,
		      boolean val06,
		      boolean val07,
		      boolean val08,
		      boolean val09,
		      // dept
		      String val010,
		      String val011,
		      String val012,
		      String val013,
		      boolean val014,
		      boolean val015,
		      // shift
		      String val11,
		      String val12,
		      int val13,
		      int val14,
		      int val15,
		      int val16,
		      int val17,
		      int val18,
		      boolean val19){
	setVals(val, val2, val3, val4, val5, val6,
		val01, val02, val03,val04,val05,val06,val07, val08,val09,
		val010, val011, val012, val013,val014,val015,
		val11, val12, val13, val14, val15, val16, val17, val18,
		val19
		);
    }
    private void setVals(String val,
			 String val2,
			 String val3,
			 String val4,
			 String val5,
			 boolean val6,
												 
			 String val01, // group
			 String val02,
			 String val03,
			 String val04,
			 String val05,
			 boolean val06,
			 boolean val07, // 13
			 boolean val08,
			 boolean val09,
			 // dept
			 String val010,
			 String val011,
			 String val012,
			 String val013,
			 boolean val014,
			 boolean val015, // 19
			 // shift
			 String val11,
			 String val12,
			 int val13,
			 int val14,
			 int val15,
			 int val16,
			 int val17,
			 int val18,
			 boolean val19 // 28
												 
			 ){
	setId(val);
	setGroup_id(val2);
	setShift_id(val3);
	setStartDate(val4);
	setExpireDate(val5);
	setInactive(val6);
	group = new Group(val01, val02, val03, val04, val05,val06, val07, val08,
			  val09,val010,val011,val012,val013,val014,val015);
	shift = new Shift(val11, val12, val13, val14, val15, val16,
			  val17, val18, val19);

    }		

    //
    // getters
    //
    public boolean equals(Object o) {
	if (o instanceof GroupShift) {
	    GroupShift c = (GroupShift) o;
	    if ( this.id.equals(c.getId())) 
		return true;
	}
	return false;
    }
    public int hashCode(){
	int seed = 37;
	if(!id.isEmpty()){
	    try{
		seed += Integer.parseInt(id)*31;
	    }catch(Exception ex){
		// we ignore
	    }
	}
	return seed;
    }
    public String getId(){
	return id;
    }
    public String getGroup_id(){
	return group_id;
    }
    public String getShift_id(){
	return shift_id;
    }
    public String getStartDate(){
	return start_date;
    }
    public String getExpireDate(){
	return expire_date;
    }		
    public boolean getInactive(){
	return !inactive.isEmpty();
    }
    public boolean isInactive(){
	return !inactive.isEmpty();
    }
    public boolean isActive(){
	return inactive.isEmpty();
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
    public void setShift_id(String val){
	if(val != null && !val.equals("-1"))
	    shift_id = val;
    }		
    public void setStartDate(String val){
	if(val != null)
	    start_date = val;
    }
    public void setExpireDate(String val){
	if(val != null)
	    expire_date = val;
    }		

    public void setInactive(boolean val){
	if(val)
	    inactive = "y";
    }		
    public String toString(){
	return id;
    }
    public Group getGroup(){
	if(group == null && !group_id.isEmpty()){
	    Group dd = new Group(group_id);
	    String back = dd.doSelect();
	    if(back.isEmpty()){
		group = dd;
	    }
	}
	return group;				
    }
    public Shift getShift(){
	if(shift == null && !shift_id.isEmpty()){
	    Shift dd = new Shift(shift_id);
	    String back = dd.doSelect();
	    if(back.isEmpty()){
		shift = dd;
	    }
	}
	return shift;				
    }		
    public String doSelect(){
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "select gs.id,gs.group_id,gs.shift_id,date_format(gs.start_date,'%m/%d/%Y'),date_format(gs.expire_date,'%m/%d/%Y'),"+
	    " gs.inactive, "+ //6
	    // group
	    " g.name,g.description,g.department_id,g.excess_hours_earn_type,g.allow_pending_accrual, "+ 
	    " g.clock_time_required,"+
	    " g.include_in_auto_batch,"+	    
	    " g.inactive, "+ 

	    
	    // dept
	    "d.name,d.description,d.ref_id,d.ldap_name,"+
	    "d.allow_pending_accrual,d.inactive, "+ // 19
	    // shift 
	    " s.name,s.start_hour,s.start_minute,s.duration,"+
	    " s.start_minute_window,s.end_minute_window,s.minute_rounding,"+
	    " s.inactive "+ 	// 28					
	    " from group_shifts gs "+
	    " join shifts s on s.id = gs.shift_id "+
	    " join groups g on gs.group_id=g.id "+
	    " join departments d on g.department_id=d.id ";
	qq += "where gs.id = ?";
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
		setVals(id,
			rs.getString(2),
			rs.getString(3),
			rs.getString(4),
			rs.getString(5),
			rs.getString(6) != null,
			// group
			rs.getString(2), // group_id
			rs.getString(7),
			rs.getString(8),
			rs.getString(9),
			rs.getString(10),
			rs.getString(11) != null,
			rs.getString(12) != null,
			rs.getString(13) != null,
			rs.getString(14) != null,
			// dept
			rs.getString(15),
			rs.getString(16),
			rs.getString(17),
			rs.getString(18),
			rs.getString(19) != null,
			rs.getString(20) !=null,
			// shift
			rs.getString(3), // shift id
			rs.getString(21),
			rs.getInt(22),
			rs.getInt(23),
			rs.getInt(24),
			rs.getInt(25),
			rs.getInt(26),
			rs.getInt(27),
			rs.getString(28) != null
			);
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
	String qq = " select count(*) from group_shifts where group_id=? and inactive is null and expire_date is null";
	String qq2 = " insert into group_shifts values(0,?,?,?,?,null)";
	if(group_id.isEmpty()){
	    msg = "Group is required";
	    return msg;
	}
	if(shift_id.isEmpty()){
	    msg = "Shift is required";
	    return msg;
	}				
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = "Could not connect to DB ";
	    return msg;
	}
	try{
	    int cnt = 0;
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, group_id);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		cnt = rs.getInt(1);
	    }
	    Helper.databaseDisconnect(pstmt, rs);
	    if(cnt > 0){
		msg = "There is an already shift assigned to shis group";
		return msg;
	    }
	    qq = qq2;
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, group_id);
	    pstmt.setString(2, shift_id);						
	    if(start_date.isEmpty()){
		start_date = Helper.getToday();
	    }
	    java.util.Date date_tmp = df.parse(start_date);
	    pstmt.setDate(3, new java.sql.Date(date_tmp.getTime()));
	    if(expire_date.isEmpty())
		pstmt.setNull(4, Types.DATE);
	    else{
		date_tmp = df.parse(expire_date);
		pstmt.setDate(4, new java.sql.Date(date_tmp.getTime()));
	    }
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
	String qq = " update group_shifts set group_id=?, shift_id=?,"+
	    "start_date=?,expire_date=?,"+
	    "inactive=? where id=?";
	if(group_id.isEmpty()){
	    msg = "Group is required";
	    return msg;
	}
	if(shift_id.isEmpty()){
	    msg = "Shift is required";
	    return msg;
	}				
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = "Could not connect to DB ";
	    return msg;
	}				
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, group_id);
	    pstmt.setString(2, shift_id);
	    if(start_date.isEmpty()){
		start_date = Helper.getToday();
	    }
	    java.util.Date date_tmp = df.parse(start_date);
	    pstmt.setDate(3, new java.sql.Date(date_tmp.getTime()));
	    if(expire_date.isEmpty())
		pstmt.setNull(4, Types.DATE);
	    else{
		date_tmp = df.parse(expire_date);
		pstmt.setDate(4, new java.sql.Date(date_tmp.getTime()));
	    }						

	    if(inactive.isEmpty()){
		pstmt.setNull(5, Types.CHAR);
	    }
	    else
		pstmt.setString(5, "y");								
	    pstmt.setString(6, id);
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
	String qq = " delete from group_shifts where id=?";
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

}
