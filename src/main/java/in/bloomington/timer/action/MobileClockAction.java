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
import in.bloomington.timer.util.Helper;
import in.bloomington.timer.timewarp.TimewarpManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MobileClockAction extends TopAction{

		static final long serialVersionUID = 4320L;	
		static Logger logger = LogManager.getLogger(MobileClockAction.class);
		DecimalFormat dFormat = new DecimalFormat("###.00");
			// max distance allowed in lat_long units
		//
		final static double EARTH_AVERAGE_RADIUS = 6371000; // metres
		// max radius in lat long distance equivalent to feet
		static final double default_radius  = 0.0003492; // 100 ft
		static final double default_radius2 = 0.0005498; // 156 ft
		static final double default_radius3 = 0.0006994; // 200 ft
		static final double default_ft_radius  = 100; // ft
		static final double default_ft_radius2 = 150; // ft
		static final double default_ft_radius3 = 200; // ft		
		static final long period_length = 15L; // minutes
		static Instant startTime = null;		
		double user_lat = 0, user_long=0;
		//
		List<Location> locations = null;
		TimeClock mobileClock = null;
		String timeClocksTitle = "Mobile Clock Data";
		String document_id="", date="";
		String location_id="";
		//
		public String execute(){
				String ret = SUCCESS;
				String back = doPrepare("mobile.action");
				if(!action.isEmpty()){
						if(action.equals("checkLocation")){
								if(isWithinLocation()){
										// next go to login
										try{
												HttpServletResponse res = ServletActionContext.getResponse();
												String str = "Login?location_id="+location_id;
												res.sendRedirect(str);
												return super.execute();
										}catch(Exception ex){
												System.err.println(ex);
										}												
								}
								else{
										back = "Your location is not close enough";
										addError(back);
										ret = "error"; // back to the location web
										return ret;
								}
						}
						else if(action.equals("Next")){
								getEmployee();
								if(employee != null){
										getMobileClock();
										mobileClock.setEmployee(employee);
										mobileClock.setTime(Helper.getCurrentTime());
										mobileClock.setLocation_id(location_id);
										boolean hasClockIn = mobileClock.hasClockIn();
										if(!hasClockIn){										
												if(mobileClock.hasMultipleJobs()){
														return "mobilePickJob";
												}
												if(mobileClock.hasNoJob()){
														addError("No active job found");
														ret = "error";
														return ret;
												}
										}
										try{
												back = mobileClock.process();
												if(!back.isEmpty()){
														addError(back);
														ret = "error";
														return ret;
												}
												else{
														if(hasClockIn){
																TimewarpManager tmwrpManager =
																		new TimewarpManager(mobileClock.getDocument_id());
																back = tmwrpManager.doProcess();
														}
														addMessage("Received Successfully");
														ret = "final";														
												}
										}
										catch(Exception ex){
												logger.error(ex);
												addError("Error "+ex);
												ret = "error";
										}
								}
						}
						else if(action.startsWith("Clock")){ // Clock In
								//
								// here we are coming from multiple job selection
								//
								// mobileClock.setEmployee(employee);
								boolean hasClockIn = mobileClock.hasClockIn();
								back = mobileClock.process();
								if(!back.isEmpty()){
										addError(back);
										ret="error";
								}
								else{
										if(hasClockIn){
												TimewarpManager tmwrpManager =
														new TimewarpManager(mobileClock.getDocument_id());
												back = tmwrpManager.doProcess();
										}
										location_id = mobileClock.getLocation_id();
										addMessage("Received Successfully");
										ret = "final";
								}
						}
						else{
								back = "Unrecognized location, check with ITS";
								addError(back);
								ret = "error";
						}
				}
				else{		
						getMobileClock();
				}
				return ret;				
		}
		//
		// the location is accepted if we have location_id set
		// this means the user is within acceptable radius
		//
		public boolean isAccepted(){
				return !location_id.isEmpty();
		}
		public TimeClock getMobileClock(){ 
				if(mobileClock == null){
						mobileClock = new TimeClock();
				}
				// if(!location_id.isEmpty()){
				// mobileClock.setLocation_id();
				// }
				return mobileClock;
		}
		public void setMobileClock(TimeClock val){
				if(val != null){
						mobileClock = val;
				}
		}

		public String getTimeClocksTitle(){
				return timeClocksTitle;
		}
		public void setAction2(String val){
				if(val != null && !val.isEmpty())		
						action = val;
		}
		public void setLocation_id(String val){
				if(val != null && !val.isEmpty())		
						location_id = val;
		}		
		public void setUser_lat(Double val){
				if(val != null)		
						user_lat = val;
		}
		public void setUser_long(Double val){
				if(val != null)		
						user_long = val;
		}		
		private void prepareLocations(){
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
				if(locations == null || needUpdate){
						needUpdate = false;
						LocationList ial = new LocationList();
						ial.hasLatLong();
						String back = ial.find();
						if(back.isEmpty()){
								List<Location> ones = ial.getLocations();
								if(ones != null && ones.size() > 0){
										locations = ones;
								}
						}
				}
		}
		private boolean isWithinLocation(){
				
				if(locations == null)
						prepareLocations();
				if(user_lat == 0 && user_long == 0){
						return false;
				}
				System.err.println(" lat lng "+user_lat+" "+user_long);
				for(Location one:locations){
						double dist = findExactDistance(user_lat, user_long,
																			 one.getLatitude(),
																			 one.getLongitude());
						System.err.println("dist "+dist);
						/**
						 * if we want each location has it is own radius replace
						 * the following line with the next
						*/
						// if(dist < one.getRadius()){
						if(dist < default_ft_radius3){
								location_id = one.getId();
								System.err.println(" found location ID "+location_id);
								return true;
						}						
				}
				return false;
		}
		private double findDistance(double lat1, double lon1,
																double lat2, double lon2){
				// 
				// R earth average radius in meters

				double x = (lat1 - lat2);
				double y = (lon2 - lon1);
				double dist = Math.sqrt(x*x+y*y); 
				return dist;
		}
		/**
		 * test values
		 * double lat1=39.16892229, lon1=-86.54504903,
		 * lat2=   39.16890000, lon2=-86.54435000; 
		 */
		private double findExactDistance(double lat1, double lon1,
																		double lat2, double lon2){
				double latDistance = Math.toRadians(lat1 - lat2);
				double lngDistance = Math.toRadians(lon1 - lon2);
				
				double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
						+ (Math.cos(Math.toRadians(lat1))) *
						(Math.cos(Math.toRadians(lat2))) *
					 (Math.sin(lngDistance / 2)) *
						(Math.sin(lngDistance / 2));

			double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
			double dist = EARTH_AVERAGE_RADIUS * c * 3.3; // convert to feet
			System.err.println(" distance feet "+dist);
			return dist;
		}		

}





































