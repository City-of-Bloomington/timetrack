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
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.list.*;
import in.bloomington.timer.bean.*;


public class GroupService extends HttpServlet{

		static final long serialVersionUID = 2210L;
		static Logger logger = LogManager.getLogger(GroupService.class);
    static String url="";
    static boolean debug = false;
		
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
				String term ="", type="",
						job_name="",
						department_id="",
						dept_ref_id=""; // Dept Ref from NW
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
						else if (name.equals("dept_ref_id")) {
								if(value != null && !value.isEmpty()){
										try{
												Integer.parseInt(value);
												dept_ref_id = value;										
										}catch(Exception ex){
										}
								}
						}						
						else if (name.equals("action")){
								action = value;
						}
						else if (name.equals("jobName")){
								job_name = value;
						}						
						else{
								// System.err.println(name+" "+value);
						}
				}
				GroupList glist =  null;
				List<Group> groups = null;
				if(!department_id.isEmpty() ||
					 !job_name.isEmpty() ||
					 !dept_ref_id.isEmpty()){
						//
						glist = new GroupList();
						glist.setDepartment_id(department_id);
						glist.setDept_ref_id(dept_ref_id);
						glist.setJobName(job_name);
						String back = glist.find();
						if(back.isEmpty()){
								groups = glist.getGroups();
						}
				}
				if(groups != null && groups.size() > 0){
						String json = writeJson(groups);
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
		String writeJson(List<Group> groups){
				String json="";
				for(Group one:groups){
						if(!json.isEmpty()) json += ",";
						json += "{\"id\":"+one.getId()+",\"name\":\""+one.getName()+"\"}";
				}
				json = "["+json+"]";
				return json;
		}
}
