package in.bloomington.timer;

/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.*;
import java.io.*;
import java.text.*;
import java.net.InetAddress; 
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;  
import org.apache.log4j.Logger;
import in.bloomington.timer.list.*;
import in.bloomington.timer.bean.*;

public class TimeClockAction extends TopAction{

		static final long serialVersionUID = 4320L;	
		static Logger logger = Logger.getLogger(TimeClockAction.class);
		DecimalFormat dFormat = new DecimalFormat("###.00");
		Set<String> ipSet = null;
		//
		TimeClock timeClock = null;
		String timeClocksTitle = "Time Clock Data";
		String document_id="", date="";
		List<TimeBlock> timeBlocks = null;
		//
		public String execute(){
				String ret = SUCCESS;
				String back = doPrepare();
				prepareIps();
				if(!action.equals("")){
						String ip="";
						try{
								HttpServletRequest req = ServletActionContext.getRequest();
								ip = req.getRemoteAddr();
								if (ip.equalsIgnoreCase("0:0:0:0:0:0:0:1")) {
										InetAddress inetAddress = InetAddress.getLocalHost();
										String ipAddress = inetAddress.getHostAddress();
										ip = ipAddress;
								}
								// System.err.println(" ip "+ip);
						}catch(Exception ex){
								System.err.println(ex);
						}
						if(ipSet != null){
								if(ipSet.contains(ip)){
										back = timeClock.process();
										if(!back.equals("")){
												addActionError(back);
										}
										else{
												document_id = timeClock.getTimeBlock().getDocument_id();
												date = timeClock.getTimeBlock().getDate();
												addActionMessage("Received Successfully");
										}
								}
								else{
										addActionError("Unrecognized location, check with ITS");
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
										// if more than one
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
				IpAddressList ial = new IpAddressList();
				String back = ial.find();
				if(back.equals("")){
						List<IpAddress> ones = ial.getIpAddresses();
						if(ones != null && ones.size() > 0){
								ipSet = new HashSet<>();
								for(IpAddress one:ones){
										String str = one.getIp_address();
										if(str != null)
												ipSet.add(str);
								}
						}
				}
		}
		//

}





































