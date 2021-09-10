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
// import in.bloomington.timer.list.*;
import in.bloomington.timer.util.Helper;
import in.bloomington.timer.timewarp.WarpEntry;
import in.bloomington.timer.report.ReasonReport;

public class ReportCodeReasonService extends HttpServlet{

		static final long serialVersionUID = 2210L;
		static Logger logger = LogManager.getLogger(ReportCodeReasonService.class);
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
				res.setHeader("Expires", "0");
				res.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
				res.setHeader("Pragma", "public");
				String filename = "police_earn_code_reason.csv";
				res.setHeader("Content-Disposition","inline; filename="+filename);
				res.setContentType("application/csv");				
				PrintWriter out = res.getWriter();
				String name, value;
				String refUserId = "", back ="";
				String end_date = Helper.getToday();
				int cur_year = Helper.getCurrentYear();
				String start_date = "01/01/"+cur_year;
				Enumeration<String> values = req.getParameterNames();
				String [] vals = null;
				while (values.hasMoreElements()){
						name = values.nextElement().trim();
						vals = req.getParameterValues(name);
						value = vals[vals.length-1].trim();
						if (name.equals("refUserId")) {
								if(value != null && !value.isEmpty()){
										refUserId = value;
								}
						}
						else{
								System.err.println(name+" "+value);
						}
				}
				List<WarpEntry> daily = null;
				if(!refUserId.isEmpty()){
						ReasonReport report = new ReasonReport();
						if(report.checkRef(refUserId)){
								report.setDate_from(start_date);
								report.setDate_to(end_date);
								back = report.findHoursCodeDetails();
								if(back.isEmpty()){
										daily = report.getDailyEntries();
								}
								else{
										logger.error(back);
								}
						}
						else{
								logger.error("Invalid refUserId ");
						}
				}
				if(daily != null && daily.size() > 0){
						// writeJson(daily, out);
						writeCvs(daily, out);
				}
				else{
						out.println();
				}
				out.flush();
				out.close();
    }

		/**
		 * Creates a JSON array for list
		 *
		 * @param list of objects
		 * @return The json string
		 */
	 void writeJson(List<WarpEntry> ones, PrintWriter out){
				out.println("[");
				int size = ones.size();
				int jj=1;
				for(WarpEntry one:ones){
						String json = "{\"Full Name\":\""+one.getFullname()+"\",\"Employee Number\":"+one.getEmpNum()+",\"Date\":\""+one.getDate()+"\",\"Earn Code\":\""+one.getCode()+"\",\"Reason\":\""+one.getReason()+"\",\"Hours\":"+one.getHours()+"}";
						if(jj < size) json += ",";
						jj++;
						out.println(json);
				}
				out.println("]");
		}
	 void writeCvs(List<WarpEntry> ones, PrintWriter out){
			 out.println("\"Full Name\",\"Employee Number\",\"Date\",\"Earn Code\",\"Reason\",\"Hours\"");
				int size = ones.size();
				int jj=1;
				for(WarpEntry one:ones){
						String line = "\""+one.getFullname()+"\","+one.getEmpNum()+",\""+one.getDate()+"\",\""+one.getCode()+"\",\""+one.getReason()+"\","+one.getHours();
						out.println(line);
				}
		}		
}
