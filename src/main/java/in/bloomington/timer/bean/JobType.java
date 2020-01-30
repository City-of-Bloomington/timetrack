package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.sql.*;
import javax.naming.*;
import javax.naming.directory.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;
/**
 * a place holder for job info to display in daily summary
 */
public class JobType extends Type implements java.io.Serializable, Comparable<JobType> {

		static final long serialVersionUID = 3700L;	
		static Logger logger = LogManager.getLogger(JobType.class);
		//
		public JobType(){
				super();
		}
		public JobType(String val){
				//
				super(val);
    }		
		public JobType(String val, String val2){
				//
				// initialize
				//
				super(val, val2);
    }
		@Override
		public boolean equals(Object obj){
				if(obj instanceof JobType){
						JobType one =(JobType)obj;
						return id.equals(one.getId());
				}
				return false;				
		}
		@Override
		public int hashCode(){
				int seed = 37;
				if(!id.isEmpty()){
						try{
								seed += Integer.parseInt(id);
						}catch(Exception ex){
						}
				}
				return seed;
		}
		public int getId_int(){
				int seed = 23;
				try{
						seed += Integer.parseInt(id);
				}catch(Exception ex){
				}
				return seed;
		}
		@Override
    public int compareTo(JobType otherJob) {
        return (this.getId_int() - otherJob.getId_int());
    }
    //
    // getters
    //
    public String getJob_id(){
				return id;
    }
    public String getJob_name(){
				return name;
    }
    //
    // setters
    //
		public void setJob_id(String val){
				setId(val);
		}
		public void setJob_name(String val){
				setName(val);
		}		
		
}
