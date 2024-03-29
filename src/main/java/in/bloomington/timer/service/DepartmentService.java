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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.list.*;
import in.bloomington.timer.bean.*;


@WebServlet(urlPatterns = {"/DepartmentService"})
public class DepartmentService extends TopServlet{

    static final long serialVersionUID = 2210L;
    static Logger logger = LogManager.getLogger(DepartmentService.class);
		
    public void doGet(HttpServletRequest req,
		      HttpServletResponse res)
	throws ServletException, IOException {
	doPost(req,res);
    }

    /**
     * @param req The request input stream
     * @param res The response output stream
     */
    public void doPost(HttpServletRequest req,
		       HttpServletResponse res)
	throws ServletException, IOException {
	
	//
	String message="", action="";
	res.setContentType("application/json");
	PrintWriter out = res.getWriter();
	String name, value;
	String term ="", type="", department_id="";
	Enumeration<String> values = req.getParameterNames();
	String [] vals = null;
	/*
	  while (values.hasMoreElements()){
	  name = values.nextElement().trim();
	  vals = req.getParameterValues(name);
	  value = vals[vals.length-1].trim();
	  if (name.equals("term")) { // this is what jquery sends
	  term = value;
	  }
	  else if (name.equals("action")){
	  action = value;
	  }
	  else{
	  // System.err.println(name+" "+value);
	  }
	  }
	*/
	DepartmentList dlist =  null;
	List<Department> departments = null;
	dlist = new DepartmentList();
	dlist.ignoreSpecialDepts(); //ignore City Directors, Training
	String back = dlist.find();
	if(back.isEmpty()){
	    departments = dlist.getDepartments();
	}
	if(departments != null && departments.size() > 0){
	    String json = writeJson(departments);
	    out.println(json);
	}
	else{
	    out.println("[]"); // empty
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
    String writeJson(List<Department> departments){
	String json="";
	for(Department one:departments){
	    if(!json.isEmpty()) json += ",";
	    json += "{\"id\":"+one.getId()+",\"name\":\""+one.getName()+"\",\"ldap_name\":\""+one.getLdap_name()+"\"}";
	}
	json = "["+json+"]";
	// System.err.println(json);
	return json;
    }
}
