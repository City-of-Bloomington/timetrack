package in.bloomington.timer.service;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */

import java.io.*;
import java.util.Enumeration;
import javax.servlet.*;
import javax.servlet.http.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;
import java.util.Set;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.time.Clock;
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
import java.text.SimpleDateFormat;
//
//
import in.bloomington.timer.*;
import in.bloomington.timer.bean.*;
import in.bloomington.timer.list.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class NewEmployeeService extends HttpServlet{

    static final long serialVersionUID = 1200L;
    static Logger logger = LogManager.getLogger(NewEmployeeService.class);
    static SimpleDateFormat dfm = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
    static SimpleDateFormat dfm2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    static SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");		
    //
    //
    // must be lowercase
    String endpoint = "tomcat2.bloomington.in.gov"; 
    boolean debug = false;
    static EnvBean envBean = null;
		
    public void doPost(HttpServletRequest req,
		       HttpServletResponse res)
				throws ServletException, IOException {
				handle(req, res);
    }
    /**
     *
     * @param req The request input stream
     * @param res The response output stream
     */
    public void doGet(HttpServletRequest req,
											HttpServletResponse res)
				throws ServletException, IOException {
				handle(req, res);
    }
    public void handle(HttpServletRequest req,
											 HttpServletResponse res)
				throws ServletException, IOException {				
				String message="", action="";
				res.setContentType("application/json");
				PrintWriter out = res.getWriter();
				String name, value;
				String Signature="",
						uri="", Service="", employee_number="", employee_name="";
				String username="", request_target="";
				String protocol ="", host="",path="", method="",
						signatureHeaders="", created="", expires="", algorithm="",
						content_type="", content_length="";
				String keyId = "", digest="";
				String headers_order = "";
				Integer port = 80;
				HttpSession session = null;
				String [] vals = null;
				ServiceKey key = null;

				StringBuilder buffer = new StringBuilder();
				BufferedReader reader = req.getReader();
				List<String> list_order = null;
				String line;
				while ((line = reader.readLine()) != null) {
						buffer.append(line);
				}
				String body = buffer.toString();
				System.err.println(" body "+body); // needed for digest in post
				protocol = req.getScheme();
				port = req.getServerPort();
				path = req.getContextPath();
				host = req.getServerName(); // tomcat2.bloomington.in.gov
				uri = req.getRequestURI(); // /timetrack/NewEmployeeService
				method = req.getMethod();				
				String path2 = req.getServletPath();
				//
				System.err.println(" uri "+uri);
				System.err.println(" path2 "+path2);	// /NewEmployeeService			
				//
				System.err.println(" protocol "+protocol);
				System.err.println(" port "+port);
				System.err.println(" path "+path);
				System.err.println(" host "+host);
				System.err.println(" method "+method);
				System.err.println(" req target "+request_target);

				Enumeration<String> values = req.getParameterNames();
				Enumeration<String> headerNames = req.getHeaderNames();

				Map<String, String> headerMap = new HashMap<>();
				while(headerNames.hasMoreElements()){
						String headerName = headerNames.nextElement();
						String headerValue = req.getHeader(headerName);
						System.err.println(headerName +" : "+headerValue);
						if(headerName.equals("username")){
								username = headerValue;
								headerMap.put(headerName, headerValue);								
						}
						else if(headerName.equals("user-agent")){
								// skip
						}
						else if(headerName.equals("Authorization")){
								// skip
						}
						else if(headerName.equals("authorization")){
								// skip
						}						
						else if(headerName.equals("Digest")){
								digest = headerValue;
								headerMap.put("digest", headerValue);
						}
						else if(headerName.equals("Signature")){
								signatureHeaders = headerValue;
								if(headerValue.indexOf("signature") > 0){
										Signature = headerValue.substring(headerValue.indexOf("signature")+10);
								}
						}
						else if(headerName.indexOf("Created") > -1){ 
								created = headerValue;
								headerMap.put("(created)", headerValue);								
						}
						else{
								headerMap.put(headerName.toLowerCase(), headerValue);
						}
				}
				while (values.hasMoreElements()){
						name = values.nextElement().trim();
						vals = req.getParameterValues(name);
						value = vals[vals.length-1].trim();
						if (name.equals("employee_number")) { // this is what jquery sends
								employee_number = value;
								System.err.println(" emp # "+employee_number);
						}
						else{
								System.err.println(name+" "+value);
						}
				}
				request_target = method.toLowerCase()+" "+uri+"?employee_number="+employee_number;
				headerMap.put("(request-target)", request_target);
				System.err.println(" map: "+headerMap);
				// get the key
				ServiceKeyList skl = new ServiceKeyList(keyId);
				String back = skl.find();
				if(back.equals("")){
						List<ServiceKey> ones = skl.getKeys();
						if(ones != null && ones.size() > 0){
								key = ones.get(0);
						}
				}
				if(!signatureHeaders.equals("")){
						try{
								String[] str_arr = signatureHeaders.split(",");
								for(String str:str_arr){
										if(str.startsWith("keyId")){
												keyId = getItemValue(str);
												System.err.println(" keyId "+keyId);
										}
										else if(str.startsWith("algorithm")){
												algorithm = getItemValue(str);
												System.err.println(" algorithm "+algorithm);
										}
										else if(str.startsWith("created")){
												created = getItemValue(str);;
												headerMap.put("(created)", created);
												System.err.println(" created "+created);
										}
										else if(str.startsWith("expires")){
												expires = getItemValue(str);
												headerMap.put("(expires)", expires);
												System.err.println(" expires "+expires);
										}
										else if(str.startsWith("username")){
												username = getItemValue(str);
												System.err.println(" username "+username);
										}
										else if(str.startsWith("headers")){
												headers_order = getItemValue(str);
												System.err.println(" order "+headers_order);
										}
										else if(str.startsWith("signature")){
												Signature = getItemValue(str);
												System.err.println(" sig "+Signature);
										}
										else{
												System.err.println(" unknown header "+str);
										}
								}
						}
						catch(Exception ex){
								System.err.println(ex);
						}
				}
				if(!headers_order.equals("")){
						String[] str_arr = headers_order.split("\\s+");
						list_order = Arrays.asList(str_arr);
						System.err.println(" list order "+list_order);
						int jj=1;
						for(String str:list_order){
								System.err.println("order "+(jj++)+" "+str);
						}
				}
				ServiceSigner signer =
						new ServiceSigner(key.getKeyValue(),
															body,
															Signature,
															algorithm,
															list_order,
															headerMap);
				
				boolean match = signer.verify();
				if(!expires.equals("")){
						// even if the match is true, if expired
						// the signing becomes false
						if(isRequestExpired(expires)){
								match = false;
								System.err.println(" is expired: yes");								
						}
				}
				System.err.println(" sig match "+match);
				
				message = "Request Received ";
				String str = "{\"message\":\""+message+"\"}";
				out.println(str);
				out.flush();
				out.close();
    }
    String getItemValue(String val){
				String str = "";
				if(val != null){
						str = val.substring(val.indexOf("=")+1);
						if(str.indexOf("\"") > -1){
								str = str.replaceAll("\"","").trim();
						}
				}
				return str;
    }
		/**
		 * check if the request is expired based on the expires parameter
		 * if present
		 */
		boolean isRequestExpired(String expires){
				boolean ret = false;
				if(expires != null){
						try{
								long timeStamp = Long.parseLong(expires);
								java.util.Date dtime =new java.util.Date((long)timeStamp*1000);
								String dtimeStr = df.format(dtime);
								System.err.println(" expire time "+dtimeStr);								
								java.util.Date date = new java.util.Date();
								System.err.println(" current time "+df.format(date));
								Long now_time = new Long(date.getTime()/1000);
								long now_long = now_time;
								System.err.println(" now "+now_long);
								if(now_long > timeStamp){
										ret = true;
								}
						}catch(Exception ex){
								System.err.println(ex);
						}
				}
				return ret;
		}
		/*
			//
			// it will change to local timestamp
			//
			long epoch = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2014-06-07 15:51:35").getTime() / 1000;
			System.err.println(" time stamp in unix "+epoch);
			}catch(Exception ex){
			   System.err.println(ex);
			}
		*/
		/*
		public CompletableFuture<String>
				getSignedUrl(String host,
										 String method,
										 String path,
										 String protocol,
										 ServiceKey key,
										 AwsCredentials credits,
										 Map<String, List<String>> param,
										 Map<String, List<String>> headers,
										 String dateTime
										 ) {
				Instant instant = null;
				try{
						java.util.Date date = dfm.parse(dateTime);
						String date2 = dfm2.format(date);
						System.err.println(" date "+date2);
						instant = ZonedDateTime.parse(date2).toInstant();
						
						// print Value 
						System.out.println("instant: "+ instant);
				}catch(Exception ex){
						System.err.println(ex);
				}
				Aws4PresignerParams params = Aws4PresignerParams.builder()
						.awsCredentials(credits)
						.signingName("s4") 
						.signingRegion(Region.EU_WEST_1) // London
						.signingClockOverride(Clock.fixed(instant, ZoneId.of("UTC")))
						.build();
				SdkHttpFullRequest request = SdkHttpFullRequest.builder()
						.host(host)
						.encodedPath(path+"/" + key.getKeyName())
						// .encodedPath(path)
						.method(SdkHttpMethod.fromValue(method))
						.protocol("https")
						.rawQueryParameters(param)
						.appendHeader("AccessKeyId","account_tracker")
						.appendHeader("AccountTrackerUsername","sibow")
						.build();
				Aws4Signer signer = Aws4Signer.create();
				SdkHttpFullRequest result = signer.sign(request, params);

				Map<String, List<String>> heads = result.headers();
				if(heads != null){
						Set<String> keys = heads.keySet();
						for(String kk:keys){
								List<String> ones = heads.get(kk);
								System.err.println(kk+" "+ones);
						}
				}
				return CompletableFuture.completedFuture(result.getUri().toString());
		}
		*/

}
