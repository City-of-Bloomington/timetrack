package in.bloomington.timer.action;

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
import in.bloomington.timer.timewarp.TimewarpManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TimeClockAction extends TopAction{

    static final long serialVersionUID = 4320L;	
    static Logger logger = LogManager.getLogger(TimeClockAction.class);
    DecimalFormat dFormat = new DecimalFormat("###.00");
    static final long period_length = 5L; // minutes
    static Instant startTime = null;		
    static Set<String> ipSet = null;
    static Hashtable<String, String> locationHash = null;
    //
    TimeClock timeClock = null;
    String timeClocksTitle = "Time Clock Data";
    String document_id="", date="", location_id="";
    String ip="";
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
	    if(ip.isEmpty()){
		ip = req.getRemoteAddr();
	    }
	    if (ip.equalsIgnoreCase("0:0:0:0:0:0:0:1")) {
		InetAddress inetAddress = InetAddress.getLocalHost();
		String ipAddress = inetAddress.getHostAddress();
		ip = ipAddress;
	    }
	    if(ipSet != null){
		if(ipSet.contains(ip)){
		    if(locationHash.containsKey(ip)){
			location_id = locationHash.get(ip);
			getTimeClock();
			timeClock.setLocation_id(location_id);
		    }
		}
	    }
	}catch(Exception ex){
	    logger.error(ex);
	}
	if(!action.isEmpty()){
	    if(ipSet != null){
		if(ipSet.contains(ip)){
		    if(locationHash.containsKey(ip)){
			location_id = locationHash.get(ip);
			timeClock.setLocation_id(location_id);
		    }
		    try{
			timeClock.setIp(ip);
			if(!timeClock.hasEmployee()){
			    back = "Unrecognized Employee ID: "+timeClock.getId_code();
			    addError(back);
			    return ret;
			}
			// uncomment the following "else if" when
			// group locations are assigned 
			//
			/**
			   if(!timeClock.checkIpAddress(ip)){
			   back = "This ip address is not allowed at this location ";
			   addError(back);
			   return ret;														
			   }
			*/
			if(!timeClock.hasClockIn()){
			    if(timeClock.hasMultipleJobs()){
				if(action.equals("Submit"))
				    return "pickJob";
			    }
			    if(timeClock.hasNoJob()){
				addError("No active job found");
				return ret;
			    }
			}
			boolean hasClockIn = timeClock.hasClockIn();
			back = timeClock.process();
			if(!back.isEmpty()){
			    addError(back);
			}
			else{
			    if(hasClockIn){
				TimewarpManager tmwrpManager =
				    new TimewarpManager(timeClock.getDocument_id());
				back = tmwrpManager.doProcess();
			    }
			    addMessage("Received Successfully");
			}
		    }
		    catch(Exception ex){
			logger.error(ex);
			addError("Error "+ex);
			return ret;
		    }
		}
		else{
		    back = "Unrecognized location, check with ITS";
		    addError(back);
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

    public String getTimeClocksTitle(){
	return timeClocksTitle;
    }
    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
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
	    ial.hasIpAddress();
	    String back = ial.find();
	    if(back.isEmpty()){
		List<Location> ones = ial.getLocations();
		if(ones != null && ones.size() > 0){
		    ipSet = new HashSet<>();
		    locationHash = new Hashtable<>();
		    for(Location one:ones){
			String str = one.getIp_address();
			if(str != null){
			    ipSet.add(str);
			    locationHash.put(str, one.getId());
			}
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





































