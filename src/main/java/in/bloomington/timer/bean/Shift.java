package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.sql.*;
import javax.sql.*;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Shift{

    static Logger logger = LogManager.getLogger(Shift.class);
    static final long serialVersionUID = 1500L;
    String id="", name="", inactive="";
    int start_hour=8, start_minute = 0, duration = 480; // minutes = 8 hrs
    int start_minute_window = 0, minute_rounding = 0;
		int end_minute_window = 0;
		int end_hour = -1, end_minute = -1;
    public Shift(){

    }		
    public Shift(String val){
				setId(val);
    }
		
    public Shift(String val,
								 String val2,
								 int val3,
								 int val4,
								 int val5,
								 int val6,
								 int val7,
								 int val8,
								 boolean val9
								 ){
				setId(val);
				setName(val2);
				setStartHour(val3);
				setStartMinute(val4);
				setDuration(val5);
				setStartMinuteWindow(val6);
				setEndMinuteWindow(val7);
				setMinuteRounding(val8);
				setInactive(val9);
				setEndHourMinute();
    }
		
    //
    // getters
    //
    public boolean equals(Object o) {
				if (o instanceof Shift) {
						Shift c = (Shift) o;
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
    public String getId(){
				return id;
    }
    public String getName(){
				return name;
    }
    public int getStartHour(){
				return start_hour;
    }
    public int getStartMinute(){
				return start_minute;
    }
    public int getEndHour(){
				//
				// to avoid double of 24
				//
				if(end_hour >= 24)
						return end_hour - 24;
				return end_hour;
    }
    public int getEndMinute(){
				return end_minute;
    }		
    public String getStartHourMinute(){
				String ret = "";
				if(start_hour > 0){
						ret = start_hour+":";
						if(start_minute < 10){
								ret += "0"+start_minute;
						}
						else{
								ret += start_minute;
						}
				}
				return ret;
    }
    public int getDuration(){
				return duration;
    }
    public int getStartMinuteWindow(){
				return start_minute_window;
    }
    public int getEndMinuteWindow(){
				return end_minute_window;
    }		
    public int getMinuteRounding(){
				return minute_rounding;
    }
		
    public boolean getInactive(){
				return !inactive.equals("");
    }
    public boolean isInactive(){
				return !inactive.equals("");
    }
    public boolean isActive(){
				return inactive.equals("");
    }
    public boolean hasRoundedMinute(){
				return minute_rounding > 0;
    }
    public boolean hasWindows(){
				return start_minute_window > 0 || end_minute_window > 0;
    }		
		void setEndHourMinute(){
				if(duration  > 0){
						end_hour = start_hour+(duration/60);
						end_minute = start_minute+duration%60;
						if(end_minute >= 60){
								end_hour += 1;
								end_minute -= 60;
						}
				}
		}
		public String getInfo(){
				String ret = getStartHourMinute();
				if(!ret.equals("")){
						ret = "Start time "+ret;
				}
				if(duration > 0){
						if(!ret.equals("")){
								ret += ", ";
						}						
						ret += "Duration "+(duration/60.);
				}
				if(start_minute_window > 0){
						if(!ret.equals("")){
								ret += ", ";
						}
						ret += "Start window: "+start_minute_window;
				}
				if(end_minute_window > 0){
						if(!ret.equals("")){
								ret += ", ";
						}						
						ret += "End window: "+end_minute_window;
				}
				if(minute_rounding > 0){
						if(!ret.equals("")){
								ret += ", ";
						}						
						ret += "Rounding: "+minute_rounding;;
				}
				return ret;
		}
    /**
     * example of with  minute_rounding = 15
     * 07 => 0
     * 08 => 15
     * 11 => 15
     * 24 => 30
     */
		
    public int getRoundedMinute(int val){
				int ret = 0;
				if(minute_rounding > 0){
						/**
						 * more logical steps
						 int remain = val%minute_rounding;
						 int factor = val/minute_rounding;
						 ret = factor * minute_rounding;
						 if(remain + (minute_rounding/2) >= minute_rounding){
						 ret += minute_rounding;
						 }
						*/
						ret = (val/minute_rounding) * minute_rounding+
								((val%minute_rounding+minute_rounding/2)/minute_rounding)*minute_rounding;
				}
				return ret;
    }
    //
    // clockInTime is in hh:mm format
    // for example for shift that starts at 8:00
    // if start_minute_window = 15 minutes
    // if badge at 7:50 val = 10, 10 < 15 ==> true
    // if badge at 7:35 val = 25, 25 is not < 15 ==> false
    //
    // for shif that starts at 8:30 
    public boolean isMinuteWithin(String clockInTime){
				if(clockInTime.indexOf(":") > -1){
						try{
								String val[] = clockInTime.split(":");
								if(val != null && val.length == 2){
										int hh = -1, mm = -1;
										hh = Integer.parseInt(val[0]);
										mm = Integer.parseInt(val[1]);
										if(start_minute == 0){ //such as 8:00
												if(hh+1 == start_hour){
														return (60 - mm) < start_minute_window;
												}
										}
										else{ // such as 8:30
												if(hh == start_hour){
														return (start_minute - mm) > 0 && (start_minute - mm) < start_minute_window;
												}
										}
								}
						}catch(Exception ex){
								System.err.println(ex);
						}

				}
				return false;
    }
    public boolean isClockOutMinuteWithin(String clockTime){
				if(clockTime.indexOf(":") > -1){
						try{
								boolean added = false;
								String val[] = clockTime.split(":");
								if(val != null && val.length == 2){
										int hh = -1, mm = -1;
										hh = Integer.parseInt(val[0]);
										mm = Integer.parseInt(val[1]);
										if(end_hour > 24){ // over night
												hh += 24;
												added = true;
										}
										if(hh == end_hour){										
												if(mm > end_minute){
														// such 17:40  compare to 17:30
														return (mm - end_minute) <= end_minute_window;
												}
										}
										else if( hh == end_hour+1){
												// such as 18:05 compate to 17:45 (end hour/end minute)
												return (60 - end_minute)+ mm <= end_minute_window;
										}
								}
						}catch(Exception ex){
								System.err.println(ex);
						}
				}
				return false;
    }		
    //
    // setters
    //
    public void setId(String val){
				if(val != null)
						id = val;
    }
    public void setName(String val){
				if(val != null)
						name = val.trim();
    }
    public void setStartHour(Integer val){
				if(val != null)
						start_hour = val.intValue();
    }
    public void setStartMinute(Integer val){
				if(val != null)
						start_minute = val.intValue();
    }
    public void setDuration(Integer val){
				if(val != null)
						duration = val.intValue();
    }		
    public void setStartMinuteWindow(Integer val){
				if(val != null)
						start_minute_window = val.intValue();
    }
    public void setEndMinuteWindow(Integer val){
				if(val != null)
						end_minute_window = val.intValue();
    }		
    public void setMinuteRounding(Integer val){
				if(val != null)
						minute_rounding = val.intValue();
    }
    public void setInactive(boolean val){
				if(val)
						inactive = "y";
    }		
    public String toString(){
				return name;
    }
    public String doSelect(){
				String back = "";
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String qq = "select id,name,start_hour,start_minute,duration,"+
						" start_minute_window,end_minute_window,minute_rounding,"+
						" inactive "+
						" from shifts where id=?";
				con = UnoConnect.getConnection();
				if(con == null){
						back = "Could not connect to DB";
						return back;
				}
				try{
						logger.debug(qq);
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1,id);
						rs = pstmt.executeQuery();
						if(rs.next()){
								setName(rs.getString(2));
								start_hour = rs.getInt(3);
								start_minute = rs.getInt(4);
								duration = rs.getInt(5);
								start_minute_window = rs.getInt(6);
								end_minute_window = rs.getInt(7);
								minute_rounding = rs.getInt(8);
								setInactive(rs.getString(9) != null);
						}
						else{
								back ="Record "+id+" Not found";
						}
				}
				catch(Exception ex){
						back += ex+":"+qq;
						logger.error(back);
				}
				finally{
						Helper.databaseDisconnect(pstmt, rs);
						UnoConnect.databaseDisconnect(con);
				}
				setEndHourMinute();
				return back;
    }
    public String doSave(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				inactive=""; // default
				String qq = " insert into shifts values(0,?,?,?,?, ?,?,?,null)";
				if(name.equals("")){
						msg = "Name is required";
						return msg;
				}
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could not connect to DB ";
						return msg;
				}
				try{
						int cnt = 0;
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, name);
						pstmt.setInt(2, start_hour);
						pstmt.setInt(3, start_minute);
						pstmt.setInt(4, duration);
						pstmt.setInt(5, start_minute_window);
						pstmt.setInt(6, end_minute_window);						
						pstmt.setInt(7, minute_rounding);
						pstmt.executeUpdate();
						Helper.databaseDisconnect(pstmt, rs);
						//
						qq = "select LAST_INSERT_ID()";
						pstmt = con.prepareStatement(qq);
						rs = pstmt.executeQuery();
						if(rs.next()){
								id = rs.getString(1);
						}
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(pstmt, rs);
						UnoConnect.databaseDisconnect(con);
				}
				setEndHourMinute();
				return msg;
    }
    public String doUpdate(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = " update shifts set name=?, start_hour=?,start_minute=?,"+
						"duration=?,start_minute_window=?,end_minute_window=?,"+
						"minute_rounding=?,"+
						"inactive=? where id=?";
				if(name.equals("")){
						msg = "Earn code name is required";
						return msg;
				}
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could not connect to DB ";
						return msg;
				}				
				try{
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, name);
						pstmt.setInt(2, start_hour);
						pstmt.setInt(3, start_minute);
						pstmt.setInt(4, duration);
						pstmt.setInt(5, start_minute_window);
						pstmt.setInt(6, end_minute_window);						
						pstmt.setInt(7, minute_rounding);
						if(inactive.equals("")){
								pstmt.setNull(8, Types.CHAR);
						}
						else
								pstmt.setString(8, "y");								
						pstmt.setString(9, id);
						pstmt.executeUpdate();
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(pstmt, rs);
						UnoConnect.databaseDisconnect(con);
				}
				setEndHourMinute();
				return msg;
    }

}
