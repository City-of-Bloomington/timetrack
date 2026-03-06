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
/**
 * needed for end of pay period twrp report
 */
   

public class DailyBlock{

    static Logger logger = LogManager.getLogger(DailyBlock.class);
    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");		
    static final long serialVersionUID = 250L;		
    String block_id="",
	document_id="",
	group_id="",
	department_id="",
	salary_group_id="",
	salary_group_name="",
	job_title="",
	earnCode="",
	code_id="",
	empFullName="",
	pay_period_id="", //not needed
	employee_id="",
	empNumber="",
	date="",
	deptName="",
	groupName="",
	nw_code="",
	gl_string="";
    int days = 0; // could be week 1, week 2 number
    int daily_hours = 8;
    double hours = 0.0, amount = 0.0; // for dollar value
    boolean seasonal = false;
    public DailyBlock( // for save
		 String val,
		 String val2,
		 String val3,		
		 String val4,
		 String val5,
		 
		 String val6,
		 String val7,		
		 String val8,
		 String val9,
		 String val10,
		 
		 String val11,		
		 String val12,
		 String val13,		
		 String val14,
		 String val15,

		 String val16,
		 String val17,
		 Integer val18,
		 Integer val19,
		 double val20,
		 
		 double val21,
		 boolean val22
		  ){
	setVals(val,
		val2,
		val3,
		val4,
		val5,
		
		val6,
		val7,
		val8,
		val9,
		val10,
		
		val11,
		val12,
		val13,
		val14,
		val15,

		val16,
		val17,
		val18,
		val19,
		val20,
		
		val21,
		val22

		);
    }		
		
    void setVals(
		 String val,
		 String val2,
		 String val3,		
		 String val4,
		 String val5,
		 
		 String val6,
		 String val7,		
		 String val8,
		 String val9,
		 String val10,
		 
		 String val11,		
		 String val12,
		 String val13,		
		 String val14,
		 String val15,

		 String val16,
		 String val17,
		 Integer val18,
		 Integer val19,
		 Double val20,
		 
		 Double val21,
		 boolean val22
		 ){
	setBlock_id(val);
	setDocument_id(val2);
	setGroup_id(val3);
	setDepartment_id(val4);
	setSalary_group_id(val5);
	
	setJobTitle(val6);
	setEarnCode(val7);
	setCode_id(val8);
	setEmpFullName(val9);
	setEmployee_id(val10);
	
	setEmpNumber(val11);
	setDate(val12);
	setDeptName(val13);
	setGroupName(val14);  
	setNwCode(val15);
	
	setGlString(val16);
	setSalaryGroupName(val17);
	setDays(val18);
	setDailyHours(val19);
	setHours(val20);
	setAmount(val21);

	setSeasonal(val22);
    }
    public void setBlock_id(String val){
	if(val != null)
	    block_id=val;
    }
    public void setDocument_id(String val){
	if(val != null)
	    document_id=val;
	    }
    public void setGroup_id(String val){
	if(val != null)
	    group_id=val;
	    }
    public void setDepartment_id(String val){
	if(val != null)
	    department_id=val;
	    }
    public void setPay_period_id(String val){
	if(val != null)
	    pay_period_id=val;
	    }
    public void setSalary_group_id(String val){
	if(val != null)
	    salary_group_id=val;
	    }
    public void setSalaryGroupName(String val){
	if(val != null)
	    salary_group_name=val;
    }
    public void setJobTitle(String val){
	if(val != null)
	    job_title=val;
	    }
    public void setEarnCode(String val){
	if(val != null)
	    earnCode = val;
	    }
    public void setCode_id(String val){
	if(val != null)
	    code_id = val;
	    }
    public void setEmpFullName(String val){
	if(val != null)
	    empFullName=val;
	    }
	
    public void setEmployee_id(String val){
	if(val != null)
	    employee_id=val;
	    }
    public void setEmpNumber(String val){
	if(val != null)
	    empNumber= val;
	    }
    public void setDate(String val){
	if(val != null)
	    date=val;
	    }
    public void setNwCode(String val){
	if(val != null)
	    nw_code=val;
	    }
    public void setGlString(String val){
	if(val != null)
	    gl_string=val;
	    }
    public void setDeptName(String val){
	if(val != null)
	    deptName = val;
	    }
    public void setGroupName(String val){
	if(val != null)
	    groupName = val;
	    }
    public void setHours(Double val){
	if(val != null)
	    hours = val;
	    }
    public void setAmount(Double val){
	if(val != null)
	    amount=val;
	    }
    public void setDays(Integer val){
	if(val != null)
	    days = val;

    }
    public void setDailyHours(Integer val){
	if(val != null)
	    daily_hours = val;

    }    
    public void setSeasonal(boolean val){
	seasonal = val;
    }
    //
    // getters
    //
    public String getBlock_id(){
	return block_id;
    }
    public String getDocument_id(){
	return document_id;
    }
    public String getGroup_id(){
	return group_id;
    }
    public String getDepartment_id(){
	return department_id;
	    
    }
    public String getJobTitle(){
	return job_title;
    }
    public String getSalary_group_id(){
	return salary_group_id;
    }
    public String getSalaryGroupName(){
	return salary_group_name;
    }    
    public String getPay_period_id(){
	return pay_period_id;
    }
    public String getEarnCode(){
	return earnCode;
    }
    public String getCode_id(){
	return code_id;
    }		
    public String getDate(){
	return date;
    }
    public String getNwCode(){
	return nw_code;
    }
    public String getGlString(){
	return gl_string;
    }
    public String getEmployee_id(){
	return employee_id;
    }
    public String getEmpFullName(){
	return empFullName;
    }
    public String getEmpNumber(){
	return empNumber;
    }
    public String getDeptName(){
	return deptName;
    }
    public String getGroupName(){
	return groupName;
    }
    public Double getHours(){
	return hours;
    }		
    public Double getAmount(){
	return amount;
    }
    public Integer getDays(){
	return days;
    }
    public Integer getDailyHours(){
	return daily_hours;
    }    
    public boolean isSeasonal(){
	return seasonal;
    }
    public boolean isValid(){
	return hours + amount > 0;
    }
    public boolean equals(Object o) {
	if (o instanceof DailyBlock) {
	    DailyBlock c = (DailyBlock) o;
	    if ( this.block_id.equals(c.getBlock_id())) 
		return true;
	}
	return false;
    }
    public int hashCode(){
	int seed = 37;
	if(!block_id.isEmpty()){
	    try{
		seed += Integer.parseInt(block_id)*31;
	    }catch(Exception ex){
		// we ignore
	    }
	}
	return seed;
    }
}
