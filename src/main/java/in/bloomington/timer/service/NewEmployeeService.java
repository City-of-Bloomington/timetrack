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
import java.util.List;
import java.util.Set;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.time.Clock;
import java.io.UnsupportedEncodingException;
import software.amazon.awssdk.auth.signer.params.Aws4PresignerParams;

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
// amazon lib
//
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.utils.http.SdkHttpUtils;
import software.amazon.awssdk.http.*;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.signer.Aws4Signer;
import software.amazon.awssdk.auth.signer.params.Aws4SignerParams;
import software.amazon.awssdk.regions.Region;

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
		// DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimePattern);
		// DateTimeFormatter formatter2 = DateTimeFormatter.ISO_INSTANT;		
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
				String protocol ="", host="",path="", httpMethod="";
				Integer port = 80;
				HttpSession session = null;
				String [] vals = null;
				ServiceKey key = null;
				Map<String, List<String>> params = new HashMap<>();
				Map<String, List<String>> headers = new HashMap<>();
				protocol = req.getScheme();
				port = req.getServerPort();
				path = req.getContextPath();
				host = req.getServerName();
				//
				System.err.println(" call prop set ");				
				// setSystemProperties();
				//
				httpMethod = req.getMethod();
				System.err.println(" protocol "+protocol);
				System.err.println(" port "+port);
				System.err.println(" path "+path);
				System.err.println(" host "+host);
				Enumeration<String> values = req.getParameterNames();
				Enumeration<String> headerNames = req.getHeaderNames();
				// for testing
				ServiceSigner helper =
						new ServiceSigner();

				while(headerNames.hasMoreElements()){
						String headerName = headerNames.nextElement();
						System.err.println(headerName+":");
						String headerValue = req.getHeader(headerName);
						System.err.println(headerValue);
						if(headers.containsKey(headerName)){
								List<String> list = headers.get(headerName);
								list.add(headerValue);
								headers.put(headerName, list);
						}
						else{
								List<String> list = new ArrayList<>();
								list.add(headerValue);
								headers.put(headerName, list);
						}
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
								List<String> list = new ArrayList<>();
								list.add(value);
								params.put(name, list);
						}
						else{
								List<String> list = new ArrayList<>();
								list.add(value);								
								params.put(name, list);								
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
				
				TimeTrackCredentialsProvider awsCreds =
						new TimeTrackCredentialsProvider(key.getKeyName(),
																						 key.getKeyValue());				
				System.err.println(" sig "+Signature);
				System.err.println(" dateTime "+dateTime);
				CompletableFuture signedUrl = getSignedUrl(host,
																									 httpMethod,
																									 path,
																									 protocol,
																									 key,
																									 awsCreds,
																									 params,
																									 headers,
																									 dateTime
																									 );
				System.err.println(" signedUrl "+signedUrl);


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

		public CompletableFuture<String> getSignedUrl(String host,
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
						System.out.println("instant: "
															 + instant);
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

		/*
			AWS4-HMAC-SHA256 Credential=account_tracker/20191113/eu-west-1/s4/aws4_request, SignedHeaders=accesskeyid;accounttrackerusername;host;x-amz-date, Signature=4f58ce0be3350ae5f0595eacf1a4d24e0146d3110525836503e411e901b1d996
			AWS4-HMAC-SHA256 Credential=account_tracker/20191113/eu-west-1/s4/aws4_request, SignedHeaders=accesskeyid;accounttrackerusername;host;x-amz-date, Signature=71f3a9d9a7119ab0fde9dc590157e60591d8153f7ba16ac229e5b3bcc7f761df

		*/

		
		
}
