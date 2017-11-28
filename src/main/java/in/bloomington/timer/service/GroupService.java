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
    String url="";
    boolean debug = false;
		static EnvBean envBean = null;
		
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
				boolean success = true;
				HttpSession session = null;
				if(url.equals("")){
						url    = getServletContext().getInitParameter("url");
						//
						String str = getServletContext().getInitParameter("debug");
						if(str.equals("true")) debug = true;

				}
				Enumeration<String> values = req.getParameterNames();
				String [] vals = null;
				while (values.hasMoreElements()){
						name = values.nextElement().trim();
						vals = req.getParameterValues(name);
						value = vals[vals.length-1].trim();
						if (name.equals("term")) { // this is what jquery sends
								term = value;
						}
						else if (name.equals("department_id")) {
								department_id = value;
						}
						else if (name.equals("action")){
								action = value;
						}
						else{
								// System.err.println(name+" "+value);
						}
				}
				GroupList glist =  null;
				List<Group> groups = null;
				if(!department_id.equals("")){
						//
						glist = new GroupList();
						glist.setDepartment_id(department_id);
						String back = glist.find();
						if(back.equals("")){
								groups = glist.getGroups();
						}
				}
				if(groups != null && groups.size() > 0){
						String json = writeJson(groups);
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
		String writeJson(List<Group> groups){
				String json="";
				for(Group one:groups){
						if(!json.equals("")) json += ",";
						json += "{\"id\":\""+one.getId()+"\",\"name\":\""+one.getName()+"\"}";
				}
				json = "["+json+"]";
				return json;
		}
}
