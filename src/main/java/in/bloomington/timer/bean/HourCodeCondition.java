package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.Serializable;
import java.util.List;
import java.sql.*;
import javax.sql.*;
import java.text.SimpleDateFormat;
import in.bloomington.timer.list.*;
import in.bloomington.timer.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HourCodeCondition implements Serializable{

    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
    static Logger logger = LogManager.getLogger(HourCodeCondition.class);
    static final long serialVersionUID = 800L;
    private String id="", hour_code_id="", department_id="", inactive="",
	date="", salary_group_id="", group_id="";
		
    Type salaryGroup = null;
    HourCode hourCode = null;
    Type department = null;
    Group group = null;
    public HourCodeCondition(
			     String val,
			     String val2,
			     String val3,
			     String val4,
			     String val5,
			     String val6,
			     boolean val7
			     ){
	setId(val);
	setHour_code_id(val2);
	setDepartment_id(val3);
	setSalary_group_id(val4);
	setGroup_id(val5);
	setDate(val6);
	setInactive(val7);
    }
    public HourCodeCondition(String val){
	setId(val);
    }
    public HourCodeCondition(){
    }		
    //
    // getters
    //
    public String getHour_code_id(){
	return hour_code_id;
    }
    public String getGroup_id(){
	if(group_id.isEmpty())
	    return "-1";
	return group_id;
    }		
    public String getDepartment_id(){
	if(department_id.isEmpty())
	    return "-1";
	return department_id;
    }
    public String getSalary_group_id(){
	if(salary_group_id.isEmpty())
	    return "-1";
	return salary_group_id;
    }
    public String getDate(){
	if(date.isEmpty()){
	    date = Helper.getToday();
	}
	return date;
    }
    public boolean getInactive(){
	return !inactive.isEmpty();
    }
    public boolean isActive(){
	return inactive.isEmpty();
    }		
    public String getId(){
	return id;
    }
    public Type getDepartment(){
	if(!department_id.isEmpty() && department == null){
	    Type one = new Type(department_id);
	    one.setTable_name("departments");
	    String back = one.doSelect();
	    if(back.isEmpty()){
		department = one;
	    }
	    return department;
	}
	if(department == null){
	    department = new Type("All","All");
	}
	return department;
    }
		
    //
    // setters
    //
    public void setId (String val){
	if(val != null)
	    id = val;
    }
    public void setHour_code_id (String val){
	if(val != null && !val.equals("-1"))
	    hour_code_id = val;
    }
    public void setDepartment_id(String val){
	if(val != null && !val.equals("-1"))
	    department_id = val;
    }
    public void setGroup_id(String val){
	if(val != null && !val.equals("-1"))
	    group_id = val;
    }		
    public void setSalary_group_id(String val){
	if(val != null && !val.equals("-1"))
	    salary_group_id = val;
    }
    public void setDate(String val){
	if(val != null)
	    date = val;
    }
    public void setInactive(boolean val){
	if(val)
	    inactive = "y";
    }
    public String toString(){
	return id;
    }
    public boolean equals(Object o) {
	if (o instanceof HourCodeCondition) {
	    HourCodeCondition c = (HourCodeCondition) o;
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
    public Type getSalaryGroup(){
	if(!salary_group_id.isEmpty() && salaryGroup == null){
	    Type one = new Type(salary_group_id);
	    one.setTable_name("salary_groups");
	    String back = one.doSelect();
	    if(back.isEmpty()){
		salaryGroup = one;
	    }
	}
	return salaryGroup;
    }
    public HourCode getHourCode(){
	if(!hour_code_id.isEmpty() && hourCode == null){
	    HourCode one = new HourCode(hour_code_id);
	    String back = one.doSelect();
	    if(back.isEmpty()){
		hourCode = one;
	    }
	}
	return hourCode;
    }
    public Group getGroup(){
	if(!group_id.isEmpty() && group == null){
	    Group one = new Group(group_id);
	    String back = one.doSelect();
	    if(back.isEmpty()){
		group = one;
	    }
	}
	return group;
    }				
    public String doSelect(){
	//
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = "select id,hour_code_id,department_id,salary_group_id,group_id,date_format(date,'%m/%d/%Y'),inactive from hour_code_conditions where id =? ";
	logger.debug(qq);
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = "Could not connect to DB ";
	    return msg;
	}								
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, id);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		setHour_code_id(rs.getString(2));
		setDepartment_id(rs.getString(3));
		setSalary_group_id(rs.getString(4));
		setGroup_id(rs.getString(5));
		setDate(rs.getString(6));
		setInactive(rs.getString(7) != null);
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

    public String doSave(){
	//
	Connection con = null;
	PreparedStatement pstmt = null, pstmt2=null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = "insert into hour_code_conditions values(0,?,?,?,?,now(),null) ";
	if(hour_code_id.isEmpty()){
	    msg = " need to pick an hour code ";
	    return msg;
	}
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = "Could not connect to DB ";
	    return msg;
	}							
	logger.debug(qq);
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, hour_code_id);
	    if(department_id.isEmpty())
		pstmt.setNull(2,Types.INTEGER);
	    else
		pstmt.setString(2, department_id);
	    if(salary_group_id.isEmpty())
		pstmt.setNull(3,Types.INTEGER);
	    else
		pstmt.setString(3, salary_group_id);
	    if(group_id.isEmpty())
		pstmt.setNull(4,Types.INTEGER);
	    else
		pstmt.setString(4, group_id);						
	    pstmt.executeUpdate();
	    //
	    date = Helper.getToday();
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
    //
    // we can update expire date and inactive
    //
    public String doUpdate(){
	//
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", str="";
	if(id.isEmpty()){
	    return " id not set ";
	}
	if(hour_code_id.isEmpty()){
	    return " hour code is required";
	}
	String qq = "update hour_code_conditions set hour_code_id=?,department_id=?,salary_group_id=?,group_id=?,inactive=? where id=? ";
				
	logger.debug(qq);
	con = UnoConnect.getConnection();				
	if(con == null){
	    msg = "Could not connect to DB ";
	    return msg;
	}			
	try{
	    pstmt = con.prepareStatement(qq);
	    int jj=1;
	    pstmt.setString(jj++, hour_code_id);
	    if(department_id.isEmpty())
		pstmt.setNull(jj++, Types.INTEGER);
	    else
		pstmt.setString(jj++, department_id);
	    if(salary_group_id.isEmpty())
		pstmt.setNull(jj++, Types.INTEGER);
	    else
		pstmt.setString(jj++, salary_group_id);
	    if(group_id.isEmpty())
		pstmt.setNull(jj++, Types.INTEGER);
	    else
		pstmt.setString(jj++, group_id);								
						
	    if(inactive.isEmpty()){
		pstmt.setNull(jj++, Types.CHAR);
	    }
	    else{
		pstmt.setString(jj++,"y");
	    }
	    pstmt.setString(jj++, id);
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
	if(msg.isEmpty()){
	    doSelect();
	}

	return msg;
    }

}
