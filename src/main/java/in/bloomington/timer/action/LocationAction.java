package in.bloomington.timer.action;
/**
 * @copyright Copyright (C) 2014-2019 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.*;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;  
import in.bloomington.timer.list.*;
import in.bloomington.timer.bean.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LocationAction extends TopAction{

		static final long serialVersionUID = 3800L;	
		static Logger logger = LogManager.getLogger(LocationAction.class);
		//
		String locationsTitle = "Current IP addresses locations";
		Location location = null;
		List<Location> locations = null;

		public String execute(){
				String ret = SUCCESS;
				String back = doPrepare();
				if(action.equals("Save")){
						back = location.doSave();
						if(!back.isEmpty()){
								addError(back);
						}
						else{
								addMessage("Saved Successfully");
						}
				}				
				else if(action.startsWith("Save")){
						back = location.doUpdate();
						if(!back.isEmpty()){
								addError(back);
						}
						else{
								addMessage("Saved Successfully");
						}
				}
				else if(action.startsWith("Delete")){
						back = location.doDelete();
						if(!back.isEmpty()){
								addError(back);
						}
						else{
								id="";
								location = new Location();
								addMessage("Deleted Successfully");
						}
				}				
				else{		
						getLocation();
						if(!id.isEmpty()){
								back = location.doSelect();
								if(!back.isEmpty()){
										addError(back);
								}
						}
				}
				return ret;
		}
		public Location getLocation(){
				if(location == null){
						location = new Location(id);
				}
				return location;
						
		}
		public void setLocation(Location val){
				if(val != null){
						location = val;
				}
		}
		
		public String getLocationsTitle(){
				
				return locationsTitle;
		}

		public void setAction2(String val){
				if(val != null && !val.isEmpty())		
						action = val;
		}
		public List<Location> getLocations(){
				if(locations == null){
						LocationList tl = new LocationList();
						String back = tl.find();
						if(back.isEmpty()){
								List<Location> ones = tl.getLocations();
								if(ones != null && ones.size() > 0){
										locations = ones;
								}
						}
				}
				return locations;
		}

}





































