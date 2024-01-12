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

@WebServlet(urlPatterns = {"/DbEmployeeService"})
public class DbEmployeeService extends TopServlet{

    static final long serialVersionUID = 1200L;
    static Logger logger = LogManager.getLogger(DbEmployeeService.class);
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
	String term ="", type="", department_id="";
	HttpSession session = null;
	Enumeration<String> values = req.getParameterNames();
	String [] vals = null;
	while (values.hasMoreElements()){
	    name = values.nextElement().trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();
	    if (name.equals("term")) { // this is what jquery sends
		term = value;
	    }
	    else if (name.equals("type")) {
		if(value != null)
		    type = value;
	    }
	    else if (name.equals("department_id")) {
		if(value != null)
		    department_id = value;
	    }						
	    else if (name.equals("action")){
								action = value;
	    }
	    else{
		// System.err.println(name+" "+value);
	    }
	}
	EmployeeList empList =  null;
	List<Employee> emps = null;
	if(term.length() > 1){
	    //
	    empList = new EmployeeList();
	    empList.setDepartment_id(department_id);
	    if(!type.isEmpty() && type.equals("activeOnly")){
		empList.setActiveOnly();
	    }
	    empList.setName(term);
	    String back = empList.find();
	    if(back.isEmpty()){
		emps = empList.getEmployees();
	    }
	}
	if(emps != null && emps.size() > 0){
	    String json = writeJson(emps, type);
	    out.println(json);
	}
	out.flush();
	out.close();
    }
    
    /**
     * Creates a JSON array string for a list of users
     *
     * @param users The users
     * @param type unused
     * @return The json string
     */
    String writeJson(List<Employee> emps, String type){
	String json="";
	for(Employee one:emps){
	    if(!json.isEmpty()) json += ",";
	    json += "{\"id\":\""+one.getId()+"\",\"value\":\""+one.getFull_name()+"\",\"full_name\":\""+one.getFull_name()+"\"}";
	}
	json = "["+json+"]";
	return json;
    }
}
