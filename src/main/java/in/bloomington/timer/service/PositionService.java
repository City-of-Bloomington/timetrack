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

@WebServlet(urlPatterns = {"/PositionService"})
public class PositionService extends TopServlet{

    static final long serialVersionUID = 2210L;
    static Logger logger = LogManager.getLogger(PositionService.class);
		
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
	String term ="", type="", department_id="", group_id="";
	Enumeration<String> values = req.getParameterNames();
	String [] vals = null;
	while (values.hasMoreElements()){
	    name = values.nextElement().trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();
	    if (name.equals("department_id")) {
		if(value != null && !value.isEmpty()){
		    try{
			Integer.parseInt(value);
			department_id = value;										
		    }catch(Exception ex){
		    }
		}
	    }
	    else if (name.equals("group_id")) {
		if(value != null && !value.isEmpty()){
		    try{
			Integer.parseInt(value);
			group_id = value;										
		    }catch(Exception ex){
		    }
		}
	    }						
	    else if (name.equals("action")){
		action = value;
	    }
	    else{
		// System.err.println(name+" "+value);
	    }
	}
	PositionList plist =  null;
	List<Position> positions = null;
	if(!department_id.isEmpty()){
	    //
	    plist = new PositionList();
	    plist.setDepartment_id(department_id);
	    String back = plist.find();
	    if(back.isEmpty()){
		positions = plist.getPositions();
	    }
	}
	else if(!group_id.isEmpty()){
	    plist = new PositionList();
	    plist.setGroup_id(group_id);
	    String back = plist.find();
	    if(back.isEmpty()){
		positions = plist.getPositions();
	    }
	}
	if(positions != null && positions.size() > 0){
	    String json = writeJson(positions);
	    out.println(json);
	}
	else{
	    out.println("[]"); // empty
	}
	out.flush();
	out.close();
    }

    /**
     * Creates a JSON array string for a list
     *
     * @param list of objects
     * @return The json string
     */
    String writeJson(List<Position> positions){
	String json="";
	for(Position one:positions){
	    if(!json.isEmpty()) json += ",";
	    json += "{\"id\":"+one.getId()+",\"name\":\""+one.getName()+"\"}";
	}
	json = "["+json+"]";
	return json;
    }
}
