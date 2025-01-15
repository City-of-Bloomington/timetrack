package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.sql.*;
import java.util.List;
import javax.naming.*;
import javax.naming.directory.*;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExcessAccrual{

    static final long serialVersionUID = 3700L;	
    static Logger logger = LogManager.getLogger(ExcessAccrual.class);
    String id="", accrual_id="", accrual_name="",
	employee_id="", employee_name="", employee_email="",
	supervisor_id="", group_id="",
	accrual_date = "", threshold_value="40";
    float accrual_value=0f;
    Employee employee = null;
    Employee supervisor = null;
    //
    public ExcessAccrual(){
	super();
    }
    public ExcessAccrual(String val){
	//
	setId(val);
    }		
    public ExcessAccrual(String val, String val2, String val3, String val4, String val5, Float val6, String val7, String val8, String val9){
	//
	// initialize
	//
	setAccrual_id(val);
	setAccrualName(val2);	
	setEmployee_id(val3);
	setEmployeeName(val4);
	setEmployeeEmail(val5);
	setAccrualValue(val6);
	setAccrualDate(val7);
	setGroup_id(val8);
	setThresholdValue(val9);
	
    }
    //
    // getters
    //
    public String getId(){
	return id;
    }
    public String getAccrual_id(){
	return accrual_id;
    }
    public String getAccrualName(){
	return accrual_name;
    }    
    public String getEmployee_id(){
	return employee_id;
    }
    public String getEmployeeName(){
	return employee_name;
    }
    public String getEmployee_email(){
	return employee_email;
    }    
    public String getSupervisor_id(){
	return supervisor_id;
    }
    public Float getAccrualValue(){
	return accrual_value;
    }
    public String getAccrualDate(){
	return accrual_date;
    }
    public String getTresholdValue(){
	return threshold_value;
    }
    public String getGroup_id(){
	return group_id;
    }    
    //
    // setters
    //
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setAccrual_id(String val){
	if(val != null)
	    accrual_id = val;
    }
    public void setAccrualName(String val){
	if(val != null)
	    accrual_name = val.trim();
    }    
    public void setEmployee_id(String val){
	if(val != null)
	    employee_id = val;
    }
    public void setEmployeeName(String val){
	if(val != null)
	    employee_name = val;
    }
    public void setEmployeeEmail(String val){
	if(val != null)
	    employee_email = val;
    }    
    public void setSupervisor_id(String val){
	if(val != null)
	    supervisor_id = val;
    }    
    public void setAccrualValue(Float val){
	if(val != null)
	    accrual_value = val;
    }
    public void setThresholdValue(String val){
	if(val != null)
	    threshold_value = val;
    }        
    public void setAccrualDate(String val){
	if(val != null)
	    accrual_date = val;
    }
    public void setGroup_id(String val){
	if(val != null)
	    group_id = val;
    }    
    public String toString(){
	return employee_name+","+accrual_value;
    }
    public boolean equals(Object obj){
	if(obj instanceof ExcessAccrual){
	    ExcessAccrual one =(ExcessAccrual)obj;
	    return employee_id.equals(one.getEmployee_id());
	}
	return false;				
    }
    public int hashCode(){
	int seed = 29;
	if(!employee_id.equals("")){
	    try{
		seed += Integer.parseInt(employee_id);
	    }catch(Exception ex){
	    }
	}
	return seed;
    }    
    public Employee getEmployee(){
	if(employee == null && !employee_id.isEmpty()){
	    Employee one = new Employee(employee_id);
	    String back = one.doSelect();
	    if(back.isEmpty()){
		employee = one;
	    }
	}
	return employee;
    }
    public Employee getSupervisor(){
	if(supervisor == null && supervisor_id.isEmpty()){
	    findSupervisor();
	}
	if(supervisor == null && !supervisor_id.isEmpty()){
	    Employee one = new Employee(supervisor_id);
	    String back = one.doSelect();
	    if(back.isEmpty()){
		supervisor = one;
	    }
	}
	return supervisor;
    }
    void findSupervisor(){
	if(supervisor == null){
	    GroupManagerList gml = new GroupManagerList();
	    gml.setGroup_id(group_id);
	    gml.setApproversOnly();
	    gml.setActiveOnly();
	    gml.setNotExpired();
	    String back = gml.find();
	    if(back.isEmpty()){
		List<GroupManager> managers = gml.getManagers();
		if(managers != null && managers.size() > 0){
		    supervisor = managers.get(0).getEmployee();
		}
	    }
	}
    }
    //
    /**
    
    select a.date,e.first_name,e.last_name, a.hours from
    employee_accruals a
    join employees e on e.id=a.employee_id
    join accruals c on c.id=a.accrual_id
    where hours > 40 and c.id = 3
    and a.date = (select date from employee_accruals order by id desc limit 1) 
    order by a.id desc limit 30;

	select c.id,c.name,a.employee_id,
	    concat_ws(' ',e.first_name,e.last_name),e.email,
	    a.hours, 	    
	    date_format(a.date,'%m/%d/%Y'),j.group_id 
	    from employee_accruals a 
	    join employees e on e.id=a.employee_id
	    join accruals c on c.id=a.accrual_id
	    join jobs j on j.employee_id=e.id 
	    where hours > 40 and c.id = 3 
	    and (j.expire_date is null or j.expire_date >= curdate()) 
	    and j.inactive is null 
	    and a.date = (select date from employee_accruals order by id desc limit 1) 
	    order by a.id desc ;
    
    
    */
    
}
