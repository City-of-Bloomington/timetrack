package in.bloomington.timer.timewarp;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.*;
import java.sql.*;
import java.text.*;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;
import org.apache.log4j.Logger;

public class Entry{

		static final long serialVersionUID = 100L;		
		static Logger logger = Logger.getLogger(Entry.class);
		static final DecimalFormat df = new DecimalFormat("#0.00");			
		String name="";
		//
		// place holder for data
		//
		String val = "", val2 ="", val3="";

	
    public Entry(){
    }	
    public Entry(String name,
								 String val,
								 String val2
								 ){
				setName(name);
				setVal(val);
				setVal2(val2);
    }		
    public Entry(String name,
								 String val,
								 String val2,
								 String val3
								 ){
				setName(name);
				setVal(val);
				setVal2(val2);
				setVal3(val3);
    }		
		public String getName(){
				return name;
    }
		public String getVal(){
				return val;
		}
		public String getVal2(){
				return val2;
		}
		public String getVal3(){
				return val3;
		}
		public String toString(){
				return name+" "+val+" "+val2+" "+val3;
		}
    //
    // setters
    //
    public void setName(String val){
				if(val != null)		
						name = val;
    }
    public void setVal(String val){
				if(val != null)		
						this.val = val;
    }		
    public void setVal2(String val){
				if(val != null)		
						this.val2 = val;
    }
    public void setVal3(String val){
				if(val != null)		
						this.val3 = val;
    }				

}
