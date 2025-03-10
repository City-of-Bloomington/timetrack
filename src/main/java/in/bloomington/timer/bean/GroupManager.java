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
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GroupManager implements Serializable{

    static Logger logger = LogManager.getLogger(GroupManager.class);
    static final long serialVersionUID = 1700L;
    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");		
    private String id="",
	group_id="",
    // for first assignment				
	employee_id = "", wf_node_id="",wf_node_id_2="",
    // for second assignment
	employee_id2 = "", wf_node_id2="",wf_node_id2_2="",
	inactive="", primary="",
	start_date="", expire_date="";
    List<Employee> employees = null;
    Group group = null;
    Employee employee = null;
    Node node = null; // workflow node
    public GroupManager(
			String val,
			String val2,
			String val3,
			String val4,
			String val5,
			String val6,
			boolean val7,
			boolean val8,
			String val9
			){
	setId(val);
	setGroup_id(val2);
	setEmployee_id(val3);
	setWf_node_id(val4);
	setStart_date(val5);
	setExpire_date(val6);
	setPrimary(val7);
	setInactive(val8);
	if(val9 != null){
	    node = new Node(wf_node_id, val9);
	}
    }
    public GroupManager(String val){
	setId(val);
    }
    public GroupManager(){
    }		
    //
    // getters
    //
    public String getGroup_id(){
	return group_id;
    }
    public String getEmployee_id(){
	if(employee_id.isEmpty())
	    return "-1";
	return employee_id;
    }
    public String getWf_node_id(){
	if(wf_node_id.isEmpty())
	    return "-1";
	return wf_node_id;
    }
    public String getWf_node_id_2(){
	if(wf_node_id_2.isEmpty())
	    return "-1";
	return wf_node_id_2;
    }		
    public String getEmployee_id2(){
	if(employee_id2.isEmpty())
	    return "-1";
	return employee_id2;
    }
    public String getWf_node_id2(){
	if(wf_node_id2.isEmpty())
	    return "-1";						
	return wf_node_id2;
    }
    public String getWf_node_id2_2(){
	if(wf_node_id2_2.isEmpty())
	    return "-1";						
	return wf_node_id2_2;
    }				
    public String getStart_date(){
	if(id.isEmpty())
	    return CommonInc.default_effective_date;
	return start_date;
    }
    public String getExpire_date(){
	return expire_date;
    }		
    public boolean getInactive(){
	return !inactive.isEmpty();
    }
    public boolean getPrimary(){
	return !primary.isEmpty();
    }
    public boolean isPrimary(){
	return !primary.isEmpty();
    }
    public String getId(){
	return id;
    }
    public boolean isActive(){
	return inactive.isEmpty();
    }
    public boolean canTimeMaintain(){
	getNode();
	if(node != null){
	    return node.getName().startsWith("Time");
	}
	return false;
    }
    public boolean canApprove(){
	getNode();
	if(node != null){
	    return node.getName().equals("Approve");
	}
	return false;
    }
    public boolean canPayrollProcess(){
	getNode();
	if(node != null){
	    return node.getName().startsWith("Payroll");
	}
	return false;				
    }
    public boolean canReview(){
	getNode();
	if(node != null){
	    return node.getName().equals("Review");
	}
	return false;
    }		
    public Node getNode(){
	if(!wf_node_id.isEmpty() && node == null){
	    Node one = new Node(wf_node_id);
	    String back = one.doSelect();
	    if(back.isEmpty()){
		node = one;
	    }
	}
	return node;
    }
    //
    // setters
    //
    public void setId (String val){
	if(val != null)
	    id = val;
    }
    public void setInactive (boolean val){
	if(val)
	    inactive="y";
    }
    public void setPrimary (boolean val){
	if(val)
	    primary="y";
    }		
    public void setWf_node_id(String val){
	if(val != null && !val.equals("-1"))
	    wf_node_id = val;
    }
    public void setWf_node_id_2(String val){
	if(val != null && !val.equals("-1"))
	    wf_node_id_2 = val;
    }		
    public void setWf_node_id2(String val){
	if(val != null && !val.equals("-1"))
	    wf_node_id2 = val;
    }
    public void setWf_node_id2_2(String val){
	if(val != null && !val.equals("-1"))
	    wf_node_id2_2 = val;
    }		
    public void setGroup_id(String val){
	if(val != null && !val.equals("-1"))
	    group_id = val;
    }
    public void setEmployee_id(String val){
	if(val != null)
	    employee_id = val;
    }
    public void setEmployee_id2(String val){
	if(val != null)
	    employee_id2 = val;
    }
    public void setStart_date(String val){
	if(val != null && !val.equals("-1"))
	    start_date = val;
    }
    public void setExpire_date(String val){
	if(val != null && !val.equals("-1"))
	    expire_date = val;
    }
    public boolean hasExpireDate(){
	return expire_date != null && !expire_date.isEmpty();
    }
    public String toString(){
	return id;
    }
    public boolean equals(Object o) {
	if (o instanceof GroupManager) {
	    GroupManager c = (GroupManager) o;
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
    public List<Employee> getEmployees(){
	if(!id.isEmpty()){
	    EmployeeList ul = new EmployeeList();
	    ul.setGroup_id(group_id); // group id
	    String back = ul.find();
	    if(back.isEmpty()){
		List<Employee> el = ul.getEmployees();
		if(el != null && el.size() > 0){
		    employees = el;
		}
	    }
	}
	return employees;
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
    public Employee getEmployee(){
	if(!employee_id.isEmpty() && employee == null){
	    Employee one = new Employee(employee_id);
	    String back = one.doSelect();
	    if(back.isEmpty()){
		employee = one;
	    }
	}
	return employee;
    }		
    public String doSelect(){
	//
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = "select gm.id,gm.group_id,gm.employee_id,gm.wf_node_id,date_format(gm.start_date,'%m/%d/%Y'),date_format(gm.expire_date,'%m/%d/%Y'),gm.primary_flag,gm.inactive,wn.name from group_managers gm join workflow_nodes wn on wn.id=gm.wf_node_id where gm.id =? ";
	logger.debug(qq);
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = "Could not connect to DB";
	    return msg;
	}						
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, id);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		setGroup_id(rs.getString(2));
		setEmployee_id(rs.getString(3));
		setWf_node_id(rs.getString(4));
		setStart_date(rs.getString(5));
		setExpire_date(rs.getString(6));
		setPrimary(rs.getString(7) != null);
		setInactive(rs.getString(8) != null);
		node = new Node(wf_node_id, rs.getString(9));
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
	PreparedStatement pstmt = null, pstmt2=null, pstmt3=null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = "select count(*) from group_managers gm where gm.group_id=? and gm.employee_id=? and gm.wf_node_id=? and gm.inactive is null and gm.expire_date is null";
	String qq2 = "insert into group_managers values(0,?,?,?,?,?,null,null) ";
	if(employee_id.isEmpty() || group_id.isEmpty() || wf_node_id.isEmpty()){
	    msg = " group, employee or role not set ";
	    return msg;
	}
	logger.debug(qq);
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = "unable to connect";
	    return msg;
	}				
	try{
	    //
	    // check first if this already set
	    //
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, group_id);
	    pstmt.setString(2, employee_id);
	    pstmt.setString(3, wf_node_id);
	    rs = pstmt.executeQuery();
	    int cnt = 0;
	    if(rs.next()){
		cnt = rs.getInt(1);
	    }
	    if(start_date.isEmpty()){
		start_date = Helper.getToday();
	    }
	    java.util.Date date_tmp = df.parse(start_date);						
	    //
	    qq = qq2;
	    pstmt2 = con.prepareStatement(qq);						
	    if(cnt == 0){ // if not set 
		pstmt2.setString(1, group_id);
		pstmt2.setString(2, employee_id);
		pstmt2.setString(3, wf_node_id);								
		pstmt2.setDate(4, new java.sql.Date(date_tmp.getTime()));
		if(primary.isEmpty())
		    pstmt2.setNull(5, Types.CHAR);
		else
		    pstmt2.setString(5, "y");
		pstmt2.executeUpdate();
	    }
	    if(!wf_node_id_2.isEmpty()){
		pstmt.setString(1, group_id);
		pstmt.setString(2, employee_id);
		pstmt.setString(3, wf_node_id_2);
		rs = pstmt.executeQuery();
		if(rs.next()){
		    cnt = rs.getInt(1);
		}
		if(cnt == 0){ // if not set
		    pstmt2.setString(1, group_id);
		    pstmt2.setString(2, employee_id);
		    pstmt2.setString(3, wf_node_id_2);
		    pstmt2.setDate(4, new java.sql.Date(date_tmp.getTime()));
		    if(primary.isEmpty())
			pstmt2.setNull(5, Types.CHAR);
		    else
			pstmt2.setString(5, "y");										
		    pstmt2.executeUpdate();										
		}
	    }
	    //  for second emp
	    if(!employee_id2.isEmpty()){
		if(!wf_node_id2.isEmpty()){
		    pstmt.setString(1, group_id);
		    pstmt.setString(2, employee_id2);
		    pstmt.setString(3, wf_node_id2);
		    rs = pstmt.executeQuery();
		    if(rs.next()){
			cnt = rs.getInt(1);
		    }
		    if(cnt == 0){ //
			pstmt2.setString(1, group_id);
			pstmt2.setString(2, employee_id2);
			pstmt2.setString(3, wf_node_id2);
			pstmt2.setDate(4, new java.sql.Date(date_tmp.getTime()));
			if(primary.isEmpty())
			    pstmt2.setNull(5, Types.CHAR);
			else
			    pstmt2.setString(5, "y");										
			pstmt2.executeUpdate();
		    }
		}
		// second for first emp
		if(!wf_node_id2_2.isEmpty()){
		    pstmt.setString(1, group_id);
		    pstmt.setString(2, employee_id2);
		    pstmt.setString(3, wf_node_id2_2);
		    rs = pstmt.executeQuery();
		    if(rs.next()){
			cnt = rs.getInt(1);
		    }
		    if(cnt == 0){ //
			pstmt2.setString(1, group_id);
			pstmt2.setString(2, employee_id2);
			pstmt2.setString(3, wf_node_id2_2);
			pstmt2.setDate(4, new java.sql.Date(date_tmp.getTime()));
			if(primary.isEmpty())
			    pstmt2.setNull(5, Types.CHAR);
			else
			    pstmt2.setString(5, "y");										
			pstmt2.executeUpdate();
		    }
		}
		//
		qq = "select LAST_INSERT_ID()";
		pstmt3 = con.prepareStatement(qq);
		rs = pstmt3.executeQuery();
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
	    Helper.databaseDisconnect(rs, pstmt, pstmt2, pstmt3);
	    UnoConnect.databaseDisconnect(con);
	}
	if(msg.isEmpty()){
	    msg = doSelect();
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
	if(id.isEmpty() || wf_node_id.isEmpty()){
	    return " id not set or action role not set";
	}
	String qq = "update group_managers set start_date=?,expire_date=?,primary_flag=?,inactive=?,wf_node_id=? where id=? ";
	logger.debug(qq);
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = "unable to connect";
	    return msg;
	}			
	try{
	    pstmt = con.prepareStatement(qq);
	    if(start_date.isEmpty()){
		start_date = Helper.getToday();
	    }
	    java.util.Date date_tmp = df.parse(start_date);
	    pstmt.setDate(1, new java.sql.Date(date_tmp.getTime()));
	    if(expire_date.isEmpty()){
		pstmt.setNull(2, Types.DATE);
	    }
	    else{
		date_tmp = df.parse(expire_date);
		pstmt.setDate(2, new java.sql.Date(date_tmp.getTime()));
	    }
	    if(primary.isEmpty()){
		pstmt.setNull(3, Types.CHAR);
	    }
	    else{
		pstmt.setString(3,"y");
	    }
	    if(inactive.isEmpty()){
		pstmt.setNull(4, Types.CHAR);
	    }
	    else{
		pstmt.setString(4,"y");
	    }
	    pstmt.setString(5, wf_node_id);
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
	if(msg.isEmpty()){
	    doSelect();
	}

	return msg;
    }
    public String doDelete(){
	//
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = "delete from group_managers where id=? ";
	logger.debug(qq);
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = "unable to connect";
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
/**
insert into workflow_nodes values(7,'Leave Review','Review Leave Request','y','Leave Reviewed',null);

insert into group_managers select 0,m.group_id,m.employee_id,7,m.start_date,null,m.primary_flag,null from group_managers m,groups g where m.expire_date is null and m.group_id=g.id and m.wf_node_id=3 and m.primary_flag is not null and g.department_id not in (8,16,30,36);

delete from group_managers where wf_node_id=7;

select * from groups g where g.id not in (select group_id from group_managers where wf_node_id=7) and g.department_id not in (8,16,20,36);


 */
