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
	pay_period_id="",
	department_id="",
	salary_group_id="",
	job_title="",
	earnCode="",
	code_id="",
	empFullName="",
	
	employee_id="",
	empNumber="",
	date="",
	deptName="",
	groupName="",
	notes="",	
	nw_code="",
	gl_string="";
    int days = 0; // could be week 1, week 2 number
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
		 String val18,
		 double val19,
		 double val20,
		 
		 Integer val21,
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
		 String val18,
		 Double val19,
		 Double val20,
		 Integer val21,
		 boolean val22
		 ){
	setBlock_id(val);
	setDocument_id(val2);
	setGroup_id(val3);
	setDepartment_id(val4);
	setPay_period_id(val5);
	
	setSalary_group_id(val6);
	setJobTitle(val7);
	setEarnCode(val8);
	setCode_id(val9);
	setEmpFullName(val10);
	
	setEmployee_id(val11);
	setEmpNumber(val12);
	setDate(val13);
	setDeptName(val14);
	setGroupName(val15);  
	
	setNotes(val16);
	setNwCode(val17);
	setGlString(val18);
	setHours(val19);
	setAmount(val20);
	setDays(val21);
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
    public void setNotes(String val){
	if(val != null)
	    notes=val;
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
    public String getNotes(){
	return notes;
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
