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
import in.bloomington.timer.*;
import in.bloomington.timer.bean.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EmployeeService extends HttpServlet{

		static final long serialVersionUID = 1200L;
		static Logger logger = LogManager.getLogger(EmployeeService.class);
    String url="";
    boolean debug = false;
		static EnvBean envBean = null;
		
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
				String term ="", type="";
				boolean success = true;
				HttpSession session = null;
				if(url.equals("")){
						url    = getServletContext().getInitParameter("url");
						//
						String str = getServletContext().getInitParameter("debug");
						if(str.equals("true")) debug = true;
						if(envBean == null){
								envBean = new EnvBean();
								str = getServletContext().getInitParameter("ldap_url");
								envBean.setUrl(str);
								str = getServletContext().getInitParameter("ldap_principle");
								envBean.setPrinciple(str);
								str = getServletContext().getInitParameter("ldap_password");
								envBean.setPassword(str);
						}
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
						else if (name.equals("type")) {
								type = value;
						}
						else if (name.equals("action")){
								action = value;
						}
						else{
								// System.err.println(name+" "+value);
						}
				}
				EmpList empList =  null;
				List<User> emps = null;
				if(term.length() > 1){
						//
						empList = new EmpList(envBean);
						empList.setName(term);
						String back = empList.find();
						if(back.equals("")){
								emps = empList.getEmps();
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
		String writeJson(List<User> emps, String type){
				String json="";
				for(User one:emps){
						if(!json.equals("")) json += ",";
						json += "{\"id\":\""+one.getFull_name()+"\",\"value\":\""+one.getFull_name()+"\",\"email\":\"\",\"fullname\":\""+one.getFull_name()+"\",\"phone\":\"\",\"dept\":\"\",\"division\":\"\",\"title\":\"\"}";
				}
				json = "["+json+"]";
				// System.err.println("json "+json);
				return json;
		}
}
