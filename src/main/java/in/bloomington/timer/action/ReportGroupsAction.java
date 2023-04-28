package in.bloomington.timer.action;

/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.*;
import java.io.*;
import java.text.*;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;  
import in.bloomington.timer.list.*;
import in.bloomington.timer.bean.*;
import in.bloomington.timer.report.*;
import in.bloomington.timer.util.CommonInc;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReportGroupsAction extends TopAction{

    static final long serialVersionUID = 1800L;	
    static Logger logger = LogManager.getLogger(ReportGroupsAction.class);
    //
    List<Department> departments = null;
    GroupsReport report = new GroupsReport();
    String dept_id="";
    List<List<String>> entries = null;
    String outputType = "html"; // html, csv
    String fileName = "groups_and_managers.csv";
    public String execute(){
	String ret = SUCCESS;
	String back = doPrepare();
	if(!back.isEmpty()){
	    return back;
	}
	if(!action.isEmpty()){
	    report.setDepartment_id(dept_id);
	    back = report.find();
	    if(back.isEmpty()){
		if(report.hasData()){
		    entries = report.getEntries();
		}
	    }
	    if(outputType.equals("csv")){
		ret = "csv";
	    }
	}
	return ret;
    }
    public void setDept_id(String val){
	if(val != null && !val.equals("-1")){
	    dept_id = val;
	}
    }
    public String getDept_id(){
	if(dept_id.isEmpty()){
	    return "-1";
	}
	return dept_id;
    }
    public List<List<String>> getEntries(){
	return entries;
    }
    public boolean hasData(){
	return entries != null;
    }
    public List<Department> getDepartments(){
	DepartmentList dl = new DepartmentList();
	String back = dl.find();
	if(back.isEmpty()){
	    List<Department> ones = dl.getDepartments();
	    if(ones != null && ones.size() > 0){
		departments = ones;
	    }
	}
	return departments;
    }
    public String getOutputType(){
	return outputType;
    }
    public String getFileName(){
	return fileName;
    }    
    public void setOutputType(String val){
	if(val != null)
	    outputType = val;
    }

}





































