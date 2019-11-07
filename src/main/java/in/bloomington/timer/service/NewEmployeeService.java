package in.bloomington.timer.service;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.sql.*;
import java.io.*;
import java.util.Enumeration;
import javax.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.ArrayList;
import java.util.List;
import java.io.UnsupportedEncodingException;

import java.net.URLDecoder;
import java.net.URLEncoder;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
// import org.apache.commons.codec.binary.Base64;
import java.util.Base64;
import in.bloomington.timer.*;
import in.bloomington.timer.bean.*;
import in.bloomington.timer.list.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class NewEmployeeService extends HttpServlet{

		static final long serialVersionUID = 1200L;
		static Logger logger = LogManager.getLogger(NewEmployeeService.class);
		static final String UTF8_CHARSET = "UTF-8";
		static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";
		//
		// /onca/xml
		static final String REQUEST_URI = "/NewEmployeeService/json"; 
		static final String REQUEST_METHOD = "GET";
		String awsAccessKeyId = "account_tracker";
		String awsSecretKey = "";
		//
		// must be lowercase
		String endpoint = "tomcat2.bloomington.in.gov"; 
    boolean debug = false;
		static EnvBean envBean = null;
		
    public void doPost(HttpServletRequest req,
											HttpServletResponse res)
				throws ServletException, IOException {
				doGet(req, res);
    }
    /**
     *
     * @param req The request input stream
     * @param res The response output stream
     */
    public void doGet(HttpServletRequest req,
											 HttpServletResponse res)
				throws ServletException, IOException {
				
				String message="", action="";
				res.setContentType("application/json");
				PrintWriter out = res.getWriter();
				String name, value;
				String AccessKeyId="", Signature="",
						uri="", Service="", employee_number="", employee_name="";
				String AccountTrackerUsername="", dateTime="";

				HttpSession session = null;
				String [] vals = null;
				ServiceKey key = null;
				Map<String, String> map = new HashMap<>();
				Enumeration<String> values = req.getParameterNames();
				Enumeration<String> headerNames = req.getHeaderNames();
				while(headerNames.hasMoreElements()){
						String headerName = headerNames.nextElement();
						System.err.println(headerName+":");
						String headerValue = req.getHeader(headerName);
						System.err.println(headerValue);
						if(headerName.equals("AccountTrackerUsername")){
								AccountTrackerUsername = headerValue;
						}
						else if(headerName.equals("authorization")){
								if(headerValue.indexOf("Signature") > 0){
										Signature = headerValue.substring(headerValue.indexOf("Signature")+10);
										
								}
						}
						else if(headerName.indexOf("Amz-Date") > -1){
								dateTime = headerValue;
						}
						else if(headerName.equals("AccessKeyId")){
								AccessKeyId = headerValue;
						}						
				}
				while (values.hasMoreElements()){
						name = values.nextElement().trim();
						vals = req.getParameterValues(name);
						value = vals[vals.length-1].trim();
						if (name.equals("employee_number")) { // this is what jquery sends
								employee_number = value;
								System.err.println(" emp # "+employee_number);
								map.put(name, value);								
						}
						else{
								// map.put(name, value);
								System.err.println(name+" "+value);
						}
				}
				ServiceKeyList skl = new ServiceKeyList("account_tracker");
				String back = skl.find();
				if(back.equals("")){
						List<ServiceKey> ones = skl.getKeys();
						if(ones != null && ones.size() > 0){
								key = ones.get(0);
						}
				}
				System.err.println(" sig "+Signature);
				System.err.println(" dateTime "+dateTime);				
				// String timestamp = parseTimestamp(dateTime);
				// System.err.println(" mod timestamp "+timestamp);
				// map.put("Timestamp", timestamp); // prob need to be in timestamp format
				map.put("AccessKeyId", awsAccessKeyId);
				map.put("AccountTrackerUsername", "sibow");				
				System.err.println(" map "+map);
				// SignedRequestsHelper();
				/*
				String dup_sig = sign(map);
				System.err.println(" dup_sig "+dup_sig);
				*/
				message = "Request Received ";
				out.println("<head><title>New Employee Service</title><body>");
				out.println("<center>");
				out.println(message);
				out.println("</center>");
				out.println("</body>");
				out.println("</html>");
				out.flush();
				out.close();
    }

		/*
		private SecretKeySpec secretKeySpec = null;
		private Mac mac = null;
				
		public void SignedRequestsHelper() {
				try{
						byte[] secretyKeyBytes = awsSecretKey.getBytes(UTF8_CHARSET);
						secretKeySpec =
								new SecretKeySpec(secretyKeyBytes, HMAC_SHA256_ALGORITHM);
						mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
						mac.init(secretKeySpec);

				}catch(Exception ex){
						logger.error(ex);
				}
		}
		*/
		/*
		public String sign(Map<String, String> params) {
				// params.put("AWSAccessKeyId", awsAccessKeyId);
				// params.put("Timestamp", timestamp());
				SecretKeySpec secretKeySpec = null;
				Mac mac = null;
				String sig = "";
				String signature = null;
				byte[] data;
				byte[] rawHmac;				
				try{
						byte[] secretyKeyBytes = awsSecretKey.getBytes(UTF8_CHARSET);
						secretKeySpec =
								new SecretKeySpec(secretyKeyBytes, HMAC_SHA256_ALGORITHM);
						mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
						mac.init(secretKeySpec);
				
						System.err.println(" params "+params);
						SortedMap<String, String> sortedParamMap =
								new TreeMap<String, String>(params);
						String canonicalQS = canonicalize(sortedParamMap);
						System.err.println(" cano "+canonicalQS);
						String toSign =
								"GET\n"+
								"tomcat2.bloomington.in.gov\n"+
								"NewEmployeeService\n"+
								"account_tracker\n"+
								canonicalQS;

								// String toSign =
								// REQUEST_METHOD + "\n"
								// + endpoint + "\n"
								// + REQUEST_URI + "\n"
								// + canonicalQS;						

						data = toSign.getBytes(UTF8_CHARSET);
						System.err.println(" data "+data);
						System.err.println(" len "+data.length);
						
						rawHmac = mac.doFinal(data);
						System.err.println(" rawHmac "+rawHmac);
						System.err.println(" len "+rawHmac.length);

						signature = new String(Base64.getEncoder().encode(rawHmac));

						// Base64 encoder = new Base64();
						// signature = new String(encoder.encode(rawHmac));
						// signature =  new String(Base64.encodeBase64(rawHmac));						


						// signature = new String(encoder.encode(rawHmac)); 
						System.err.println(" sig "+signature);
						//
						sig = percentEncodeRfc3986(signature);

						// String url = "https://" + endpoint + REQUEST_URI + "?" +
						 // canonicalQS + "&Signature=" + sig;
				}catch(Exception ex){
						System.err.println(" ex "+ex);
				}
				return sig;
		}
*/
		/*
		private String parseTimestamp(String tsmp) {
				String timestamp = null;
				Calendar cal = Calendar.getInstance();
				DateFormat dfm = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
				dfm.setTimeZone(TimeZone.getTimeZone("GMT"));
				DateFormat dfm2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
				dfm2.setTimeZone(TimeZone.getTimeZone("GMT"));				
				// dfm.parse(tsmp);
				try{
						System.err.println(" time "+tsmp);
						java.util.Date tm = dfm.parse(tsmp);
						System.err.println(" time "+tm);
						timestamp = dfm2.format(tm.getTime());
						
				}catch(Exception ex){
						System.err.println(ex);
				}
				return timestamp;
		}		
		*/
		/*
		private String canonicalize(SortedMap<String, String> sortedParamMap)
		{
				if (sortedParamMap.isEmpty()) {
						return "";
				}
				
				StringBuffer buffer = new StringBuffer();
				Iterator<Map.Entry<String, String>> iter =
						sortedParamMap.entrySet().iterator();
				
				while (iter.hasNext()) {
						Map.Entry<String, String> kvpair = iter.next();
						buffer.append(percentEncodeRfc3986(kvpair.getKey()));
						buffer.append("=");
						buffer.append(percentEncodeRfc3986(kvpair.getValue()));
						if (iter.hasNext()) {
								buffer.append("&");
						}
				}
				String canonical = buffer.toString();
				return canonical;
		}
		*/
		/*
		private String percentEncodeRfc3986(String s) {
				String out;
				try {
						out = URLEncoder.encode(s, UTF8_CHARSET)
								.replace("+", "%20")
								.replace("*", "%2A")
								.replace("%7E", "~");
				} catch (UnsupportedEncodingException e) {
						out = s;
				}
				return out;
		}
		*/
		
}
