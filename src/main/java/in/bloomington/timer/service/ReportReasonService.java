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
import java.util.List;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
//
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

//
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.util.Helper;
import in.bloomington.timer.timewarp.WarpEntry;
import in.bloomington.timer.report.ReasonReport;

public class ReportReasonService extends HttpServlet{

		static final long serialVersionUID = 2210L;
		static Logger logger = LogManager.getLogger(ReportReasonService.class);
    static String url="";
		static String outputFileLocation = ""; 
    static boolean debug = false;
		
    public void doGet(HttpServletRequest req,
											HttpServletResponse res)
				throws ServletException, IOException {
				String message="", action="";
				// doPost(req,res);
				
				res.setHeader("Expires", "0");
				res.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
				res.setHeader("Pragma", "public");
				String format = "xml";// csv, xls, json, xls file
				String filename = "police_earn_code_reason.xls";
				String name, value;
				String refUserId = "", back ="";
				String outputType = "";
				String end_date = Helper.getToday();
				// String end_date = "02/01/2021";
				int cur_year = Helper.getCurrentYear();
				String start_date = "01/01/"+cur_year;
				String host_forward = req.getHeader("X-Forwarded-Host");
				String host = req.getHeader("host");
				if(host_forward != null){
						if(host_forward.indexOf("timetrack") == -1)
								url = host_forward+"/timetrack/";
						else
								url = host_forward;
				}
				else if(host != null){
						if(host.indexOf("timetrack") > -1){
								url = host;
						}
						else{
								url = host+"/timetrack/";
						}
				}
				else{
						url  = getServletContext().getInitParameter("url");
				}
				// to be determined
				outputFileLocation = getServletContext().getInitParameter("outputFileLocation");				
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
						else if(name.equals("schema")){
								outputType = "schema";
						}						
						else if(name.equals("Employees.xsd")){
								outputType = "schema";
						}
						else{
								System.err.println(name+" "+value);
						}
				}
				if(!outputType.isEmpty()){
						filename = "Employees.xsd";
						res.setHeader("Content-Disposition","inline; filename="+filename);
						res.setContentType("application/xml");
				}
				else if(format.equals("xml")){
						filename = "police_earn_code_reason.xml";
						res.setHeader("Content-Disposition","inline; filename="+filename);
						res.setContentType("application/xml");		
				}				
				else if(format.equals("csv")){
						filename = "police_earn_code_reason.csv";
						res.setHeader("Content-Disposition","inline; filename="+filename);
						res.setContentType("application/csv");										
				}
				else if(format.equals("xls")){
						filename = "police_earn_code_reason.xls";
						res.setHeader("Content-Disposition","inline; filename="+filename);
						res.setContentType("application/vnd.ms-excel");										
				}
				else if(format.equals("json")){
						filename = "police_earn_code_reason.json";
						res.setHeader("Content-Disposition","inline; filename="+filename);
						res.setContentType("application/json");		
				}

				List<WarpEntry> daily = null;
				if(!outputType.isEmpty()){
						PrintWriter out = res.getWriter();
						writeSchema(out);
						out.flush();
						out.close();										
				}
				else {
						if(!refUserId.isEmpty()){
								ReasonReport report = new ReasonReport();
								if(report.checkRef(refUserId)){						
										report.setDate_from(start_date);
										report.setDate_to(end_date);
										back = report.findHoursCodeDetails();
										if(back.isEmpty()){
												daily = report.getDailyEntries();
												if(format.equals("csv")){
														PrintWriter out = res.getWriter();
														if(daily != null && daily.size() > 0){
																writeCsv(daily, out);
																out.flush();
																out.close();										
														}
														else{
																out.println();
														}
												}
												else if(format.equals("xls")){
														ServletOutputStream out = res.getOutputStream();						
														if(daily != null && daily.size() > 0){
																writeXls(daily, out);
																out.flush();
																out.close();										
														}
														else{
																out.println();
														}
												}
												else if(format.equals("json")){
														PrintWriter out = res.getWriter();
														if(daily != null && daily.size() > 0){
																writeJson(daily, out);
																out.flush();
																out.close();										
														}
														else{
																out.println();
														}
												}
												else if(format.equals("xml")){
														PrintWriter out = res.getWriter();
														if(daily != null && daily.size() > 0){
																writeXml(daily, out);
																out.flush();
																out.close();										
														}
														else{
																out.println();
														}
												}
										}
										else{
												logger.error(back);
										}
								}
								else{
										logger.error("Invalid refUserId ");
								}
						}
				}
    }								
		/**
		 * Creates a JSON array for list
		 *
		 * @param list of objects
		 * Output in MS xls format
		 */
		void writeXls(List<WarpEntry> ones, ServletOutputStream out){
				String file_output = outputFileLocation+"code_reason.xls";
				int cell_count = 6;
				int k=0;
				String headers[] = {"Full Name","Employee Numbe","Date","Earn Code","Reason","Hours"};
				try{
						HSSFWorkbook hwb = new HSSFWorkbook();
						HSSFSheet sheet = hwb.createSheet("new sheet");
						HSSFRow row = sheet.createRow((short) 0+k);
						for(int p=0;p<cell_count;p++){
								HSSFCell cell = row.createCell((short) p); // cell count
								cell.setCellType(CellType.STRING);
								cell.setCellValue(headers[p]);
						}
						int size = ones.size();
						for(WarpEntry one:ones){
								k++; // row number
								row = sheet.createRow((short) 0+k);
								int p = 0; // cell number
								HSSFCell cell = row.createCell((short) p);
								cell.setCellType(CellType.STRING);
								cell.setCellValue(one.getFullname());
								p++;
								cell = row.createCell((short) p);
								cell.setCellType(CellType.NUMERIC);
								cell.setCellValue(one.getEmpNum());
								p++;
								cell = row.createCell((short) p);
								cell.setCellType(CellType.STRING);
								cell.setCellValue(one.getDate());
								p++;
								cell = row.createCell((short) p);
								cell.setCellType(CellType.STRING);
								cell.setCellValue(one.getCode());
								p++;
								cell = row.createCell((short) p);
								cell.setCellType(CellType.STRING);
								cell.setCellValue(one.getReason());
								p++;
								cell = row.createCell((short) p);
								cell.setCellType(CellType.NUMERIC);
								cell.setCellValue(one.getHours());
								p++;
						}
						// FileOutputStream fileOut = new FileOutputStream(file_output);
						hwb.write(out);
						out.close();
				}catch(Exception ex){
						System.err.println(ex);
				}
		}
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
	 void writeCsv(List<WarpEntry> ones, PrintWriter out){
			 out.println("\"Full Name\",\"Employee Number\",\"Date\",\"Earn Code\",\"Reason\",\"Hours\"");
				int size = ones.size();
				int jj=1;
				for(WarpEntry one:ones){
						String line = "\""+one.getFullname()+"\","+one.getEmpNum()+",\""+one.getDate()+"\",\""+one.getCode()+"\",\""+one.getReason()+"\","+one.getHours();
						out.println(line);
				}
		}
		void writeXml(List<WarpEntry> ones, PrintWriter out){
				out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
				out.println();
				out.println("<Employees ");
				out.println("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
				out.println("xsi:noNamespaceSchemaLocation=\"Employees.xsd\">");
				/**
					 // did not work
				out.println("xmlns=\"https://www.w3schools.com\"");
				out.println("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
				out.println("xsi:schemaLocation=\""+url+"ReportReasonService Employees.xsd\">"); // space required
				*/
				int jj=1;
				for(WarpEntry one:ones){
						out.println("  <Employee>");
						/**
						if(!one.getReason().isEmpty()){
								System.err.println(jj+" "+one.getFullname()+" "+one.getReason());
						}
						*/
						out.println("    <Name>"+one.getFullname()+"</Name>");
						out.println("    <EmployeeNumber>"+one.getEmpNum()+"</EmployeeNumber>");
						out.println("    <Date>"+one.getDate()+"</Date>");
						out.println("    <Code>"+one.getCode()+"</Code>");
						out.println("    <Reason>"+one.getReason().replaceAll("&"," and ").replaceAll("<","&lt;").replaceAll(">","&gt;").replaceAll("\"","&quot;").replaceAll("`","&apos;")+"</Reason>");
						out.println("    <Hours>"+one.getHours()+"</Hours>");
						out.println("    <Amount>"+one.getHourlyRate()+"</Amount>");// amount						
						out.println("  </Employee>");
						jj++;
				}
				out.println("</Employees>");				
		}
		void writeSchema(PrintWriter out){
				out.println("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
				out.println("<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">");
				out.println("<xs:element name=\"Employees\">");
				out.println("<xs:complexType>");
				out.println("<xs:sequence>");
				out.println("<xs:element name=\"Employee\">");
				out.println("<xs:complexType>");
				out.println("<xs:sequence>");
 				out.println("<xs:element name=\"Name\" type=\"xs:string\"/>");
				out.println("<xs:element name=\"EmployeeNumber\" type=\"xs:integer\"/>");
				out.println("<xs:element name=\"Date\" type=\"xs:date\"/>");
				out.println("<xs:element name=\"Code\" type=\"xs:string\"/>");
				out.println("<xs:element name=\"Reason\" type=\"xs:string\"/>");
				out.println("<xs:element name=\"Hours\" type=\"xs:decimal\"/>");
				out.println("<xs:element name=\"Amount\" type=\"xs:decimal\"/>");				
				out.println("</xs:sequence>");
				out.println("</xs:complexType>");
				out.println("</xs:element>");
				out.println("</xs:sequence>");
				out.println("</xs:complexType>");
				out.println("</xs:element>");				
				out.println("</xs:schema>");
		}
}
