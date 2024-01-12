package in.bloomington.timer.service;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.*;
import java.sql.*;
import java.io.*;
import javax.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.util.ArrayList;
import java.util.List;
import in.bloomington.timer.*;
import in.bloomington.timer.bean.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet(urlPatterns = {"/GroupEmployeeService"})
public class GroupEmployeeService extends TopServlet{

    static final long serialVersionUID = 2200L;
    static Logger logger = LogManager.getLogger(GroupEmployeeService.class);
    
    public void doGet(HttpServletRequest req,
		      HttpServletResponse res)
	throws ServletException, IOException {
	doPost(req,res);
    }
    
    /**
     * Generates the Group form and processes view, add, update and delete operations.
     *
     * @param req The request input stream
     * @param res The response output stream
     */
    public void doPost(HttpServletRequest req,
		       HttpServletResponse res)
	throws ServletException, IOException {
	
	String id = "";
	
	//
	String message="", action="";
	res.setContentType("application/json");
	PrintWriter out = res.getWriter();
	String name, value;
	String group_id="";
	Enumeration<String> values = req.getParameterNames();
	String [] vals = null;
	while (values.hasMoreElements()){
	    name = values.nextElement().trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();
	    if (name.equals("group_id")) {
		if(value != null && !value.isEmpty()){
		    try{
			Integer.parseInt(value);
			group_id = value;										
		    }catch(Exception ex){
		    }
		}
	    }
	    else{
		System.err.println(name+" "+value);
	    }
	}
	EmployeeList employeeList =  null;
	List<Employee> employees = null;
	if(!group_id.isEmpty()){
	    //
	    employeeList = new EmployeeList();
	    employeeList.setActiveOnly();
	    employeeList.setGroup_id(group_id);
	    String back = employeeList.find();
	    if(back.isEmpty()){
		employees = employeeList.getEmployees();
	    }
	}
	if(employees != null && employees.size() > 0){
	    String json = writeJson(employees);
	    out.println(json);
	}
	else{
	    out.println("[]"); // empty array
	}
	out.flush();
	out.close();
    }
    
    /**
     * Creates a JSON array string for a list of employees
     *
     * @param employees The employees
     * @return The json string
     */
    String writeJson(List<Employee> employees){
	String json="";
	for(Employee one:employees){
	    if(!json.isEmpty()) json += ",";
	    json += "{\"id\":\""+one.getId()+"\",\"fullname\":\""+one.getFull_name()+"\"}";
	}
	json = "["+json+"]";
	return json;
    }
}
