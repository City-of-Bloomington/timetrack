package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.Serializable;
import java.util.List;
import java.sql.*;
import javax.sql.*;
import java.text.SimpleDateFormat;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Block{

		static Logger logger = LogManager.getLogger(Block.class);
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");		
		static final long serialVersionUID = 250L;		
    String id="",
				document_id="",
				job_id="",
				hour_code_id="",
				ovt_pref="", clock_in="", clock_out="";
		String date = ""; // the user pick date, needed for PTO, Holiday etc
		double hours = 0.0;
		int begin_hour = 0, begin_minute=0, end_hour=0, end_minute=0;
		HourCode hourCode = null;
		CodeRef codeRef = null;
		JobTask jobTask = null;
		Document document = null;
		boolean hourCodeSet = false;
    public Block(
								 String val,
								 String val2,
								 String val3,
								 String val4,
								 String val5,
								 int val6,
								 int val7,
								 int val8,
								 int val9,
								 double val10,
								 String val11,
								 String val12,
								 String val13
							 ){
				setVals(val, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13);
		}
		void setVals(
								 String val,
								 String val2,
								 String val3,
								 String val4,
								 String val5,
								 int val6,
								 int val7,
								 int val8,
								 int val9,
								 double val10,
								 String val11,
								 String val12,
								 String val13
							 ){								
				setId(val);
				setDocument_id(val2);
				setJob_id(val3);
				setHour_code_id(val4);
				setDate(val5);
				setBegin_hour(val6);
				setBegin_minute(val7);
				setEnd_hour(val8);
				setEnd_minute(val9);				
				setHours(val10);
				setOvt_pref(val11);
				setClock_in(val12);
				setClock_out(val13);
    }
    public Block(String val){
				setId(val);
    }
    public Block(){
    }		
    //
    // getters
    //
		public String getId(){
				return id;
    }
    public String getJob_id(){
				return job_id;
    }
    public String getDocument_id(){
				return document_id;
    }
    public String getHour_code_id(){
				return hour_code_id;
    }
		// hourCode 
		public String getId_compound(){
				getHourCode();
				return hourCode.getId_compound();
		}
    public String getDate(){
				return date;
    }
    public int getBegin_hour(){
				return begin_hour;
    }		
    public int getBegin_minute(){
				return begin_minute;
    }
    public int getEnd_hour(){
				return end_hour;
    }		
    public int getEnd_minute(){
				return end_minute;
    }		
    public double getHours(){
				return hours;
    }
		public String getOvt_pref(){
				return ovt_pref;
    }
		public String getClock_in(){
				return clock_in;
    }
		public String getClock_out(){
				return clock_out;
    }		
    //
    // setters
    //
    public void setId (String val){
				if(val != null)
						id = val;
    }
    public void setJob_id (String val){
				if(val != null)
						job_id = val;
    }
    public void setDocument_id (String val){
				if(val != null && !val.equals("-1"))
						document_id = val;
    }
		//
		// we may get something like 1-Hours, 3-Time
		//
    public void setHour_code_id(String val){
				if(val != null && !val.equals("-1")){
						if(val.indexOf("_") > -1){
								hour_code_id = val.substring(0,val.indexOf("_"));
						}
						else{
								hour_code_id = val;
						}
				}
    }
		//
		// set by user when click on calendar block
		// needed for hour code that uses hours instead of
		// begin time and end time
		//
    public void setDate(String val){ 
				if(val != null)
						date = val;
    }		
    public void setBegin_hour(int val){
				begin_hour = val;
    }
    public void setBegin_minute(int val){
				begin_minute = val;
    }
    public void setEnd_hour(int val){
				end_hour = val;
    }
    public void setEnd_minute(int val){
				end_minute = val;
    }		
    public void setHours(double val){
				hours = val;
    }
    public void setOvt_pref(String val){
				if(val != null)
						ovt_pref = val;
    }
    public void setClock_in(String val){
				if(val != null)
						clock_in = val;
    }
    public void setClock_out(String val){
				if(val != null)
						clock_out = val;
    }		
		public String toString(){
				return id;
		}
		public boolean equals(Object o) {
				if (o instanceof Block) {
						Block c = (Block) o;
						if ( this.id.equals(c.getId())) 
								return true;
				}
				return false;
		}
		public int hashCode(){
				int seed = 37;
				if(!id.equals("")){
						try{
								seed += Integer.parseInt(id)*31;
						}catch(Exception ex){
								// we ignore
						}
				}
				return seed;
		}
		public JobTask getJobTask(){
				if(!job_id.equals("") && jobTask == null){
						JobTask one = new JobTask(job_id);
						String back = one.doSelect();
						if(back.equals("")){
							 jobTask = one;
						}
				}
				return jobTask;
		}
		public HourCode getHourCode(){
				if(!hour_code_id.equals("")){
						HourCode one = new HourCode(hour_code_id);
						String back = one.doSelect();
						if(back.equals("")){
							 hourCode = one;
							 hourCodeSet = true;
						}
				}
				else{
						hourCode = new HourCode();
				}
				return hourCode;
		}
		public boolean hasCodeRef(){
				getCodeRef();
				return codeRef != null;
		}
		//
		// we need this to get the New World reference hour_codes
		// for export purpose
		//
		public CodeRef getCodeRef(){
				if(codeRef == null && !hour_code_id.equals("")){
						CodeRefList cdr = new CodeRefList();
						cdr.setCode_id(hour_code_id);
						cdr.setIgnoreHash();
						String back = cdr.find();
						if(back.equals("")){
								List<CodeRef> ones = cdr.getCodeRefs();
								if(ones != null && ones.size() == 1){
										codeRef = ones.get(0);
								}
						}
				}
				return codeRef;
		}		
		public Document getDocument(){
				if(!document_id.equals("") && document == null){
						Document one = new Document(document_id);
						String back = one.doSelect();
						if(back.equals("")){
							 document = one;
						}
				}
				return document;
		}		
		public boolean isClockIn(){
				return !clock_in.equals("");
		}
		public boolean isClockOut(){
				return !clock_out.equals("");
		}		
}
