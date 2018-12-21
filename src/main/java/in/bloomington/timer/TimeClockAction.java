package in.bloomington.timer;

/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.*;
import java.io.*;
import java.text.*;
import java.time.Instant;
import java.time.Duration;
import java.net.InetAddress; 
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;  
import in.bloomington.timer.list.*;
import in.bloomington.timer.bean.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TimeClockAction extends TopAction{

		static final long serialVersionUID = 4320L;	
		static Logger logger = LogManager.getLogger(TimeClockAction.class);
		DecimalFormat dFormat = new DecimalFormat("###.00");
		static final long period_length = 5L; // minutes
		static Instant startTime = null;		
		static Set<String> ipSet = null;
		//

		TimeClock timeClock = null;
		String timeClocksTitle = "Time Clock Data";
		String document_id="", date="";
		String ip="";
		List<TimeBlock> timeBlocks = null;
		private static final String[] IP_HEADER_CANDIDATES = { 
				"X-Forwarded-For",
				"Proxy-Client-IP",
				"WL-Proxy-Client-IP",
				"HTTP_X_FORWARDED_FOR",
				"HTTP_X_FORWARDED",
				"HTTP_X_CLUSTER_CLIENT_IP",
				"HTTP_CLIENT_IP",
				"HTTP_FORWARDED_FOR",
				"HTTP_FORWARDED",
				"HTTP_VIA",
				"REMOTE_ADDR" };
		
		//
		public String execute(){
				String ret = SUCCESS;
				String back = doPrepare("timeClock.action");
				prepareIps();
				try{
						HttpServletRequest req = ServletActionContext.getRequest();
						for (String header : IP_HEADER_CANDIDATES) {
								String ip2 = req.getHeader(header);
								if (ip2 != null && ip2.length() != 0 && !"unknown".equalsIgnoreCase(ip2)) {
										 ip = ip2;
								}
						}
						if(ip.equals("")){
								ip = req.getRemoteAddr();
						}
						if (ip.equalsIgnoreCase("0:0:0:0:0:0:0:1")) {
								InetAddress inetAddress = InetAddress.getLocalHost();
								String ipAddress = inetAddress.getHostAddress();
								ip = ipAddress;
						}
						// System.err.println(" ip "+ip);
				}catch(Exception ex){
						logger.error(ex);
				}
				if(!action.equals("")){
						if(ipSet != null){
								if(ipSet.contains(ip)){
										try{
												if(!timeClock.hasEmployee()){
														back = "Unrecognized Employee ID: "+timeClock.getId_code();
														addError(back);
														return ret;
												}
												// uncomment the following "else if" when
												// group locations are assigned 
												//
												/**
												else if(!timeClock.checkIpAddress(ip)){
														back = "This ip address is not allowed at this location ";
														addError(back);
														return ret;														
												}
												*/
												else if(!timeClock.hasClockIn() &&
																timeClock.hasMultipleJobs()){
														if(action.equals("Submit"))
																return "pickJob";
												}
												back = timeClock.process();
												if(!back.equals("")){
														addError(back);
												}
												else{
														document_id = timeClock.getTimeBlock().getDocument_id();
														date = timeClock.getTimeBlock().getDate();
														addMessage("Received Successfully");
												}
										}
										catch(Exception ex){
												logger.error(ex);
												addError("Error");
												return ret;
										}
								}
								else{
										back = "Unrecognized location, check with ITS";
										addError(back);
										addActionError(back);
								}
						}
				}				
				else{		
						getTimeClock();
				}
				return ret;
		}
		public TimeClock getTimeClock(){ 
				if(timeClock == null){
						timeClock = new TimeClock();
				}
				return timeClock;
		}
		public void setTimeClock(TimeClock val){
				if(val != null){
						timeClock = val;
				}
		}
		public List<TimeBlock> getTimeBlocks(){
				if(timeBlocks == null){
						if(!document_id.equals("") && !date.equals("")){
								TimeBlockList tbl = new TimeBlockList();
								tbl.setDocument_id(document_id);
								tbl.setDate(date);
								tbl.hasClockInAndOut(); //ignore clock-in only
								String back = tbl.find();
								if(back.equals("")){
										List<TimeBlock> ones = tbl.getTimeBlocks();
										if(ones != null && ones.size() > 1){
												timeBlocks = ones;
										}
								}
						}
				}
				return timeBlocks;
		}
		public boolean hasTimeBlocks(){
				getTimeBlocks();
				return timeBlocks != null && timeBlocks.size() > 0;
		}
		public String getTotalHours(){
				double total = 0.;
				if(timeBlocks != null && timeBlocks.size() > 0){
						for(TimeBlock one:timeBlocks){
								total += one.getHours();
						}
				}
				return dFormat.format(total);
		}
		public String getTimeClocksTitle(){
				return timeClocksTitle;
		}
		public void setAction2(String val){
				if(val != null && !val.equals(""))		
						action = val;
		}
		private void prepareIps(){
				boolean needUpdate = false;
				if(startTime == null){
						startTime =  Instant.now();
				}
				else {
						Instant now = Instant.now();
						long minutes = Duration.between(startTime, now).toMinutes();
						if(minutes > period_length){
								needUpdate = true;
								startTime = now;
						}
				}
				if(ipSet == null || needUpdate){
						needUpdate = false;
						LocationList ial = new LocationList();
						String back = ial.find();
						if(back.equals("")){
								List<Location> ones = ial.getLocations();
								if(ones != null && ones.size() > 0){
										ipSet = new HashSet<>();
										for(Location one:ones){
												String str = one.getIp_address();
												if(str != null)
														ipSet.add(str);
										}
								}
						}
				}
		}
		public String getIpaddr(){
				return ip;
		}
		public boolean isAccepted(){
				if(ipSet != null){
						return ipSet.contains(ip);
				}
				return false;
		}

}





































