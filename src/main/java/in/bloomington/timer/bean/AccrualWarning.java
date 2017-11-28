package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.sql.*;
import javax.naming.*;
import javax.naming.directory.*;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AccrualWarning{

		static final long serialVersionUID = 3700L;	
		static Logger logger = LogManager.getLogger(AccrualWarning.class);
    String id="", hour_code_id="",
				step_warning_text="",
				min_warning_text="",
				excess_warning_text="";
		double min_hrs = 0., step_hrs=0.,
				related_accrual_max_leval=0.;
		HourCode hourCode = null;
		//
		public AccrualWarning(){
				super();
		}
		public AccrualWarning(String val){
				//
				setId(val);
    }		
		public AccrualWarning(String val,
													String val2,
													double val3,
													double val4,
													double val5,
													String val6,
													String val7,
													String val8
													){
				setVals(val, val2, val3, val4, val5, val6, val7, val8);
		}
		private void setVals(String val,
												 String val2,
												 double val3,
												 double val4,
												 double val5,
												 String val6,
												 String val7,
												 String val8
												 ){
				setId(val);
				setHour_code_id(val2);
				setMin_hrs(val3);
				setStep_hrs(val4);
				setRelated_accrual_max_level(val5);
				setStep_warning_text(val6);
				setMin_warning_text(val7);
				setExcess_warning_text(val8);
    }		
		public boolean equals(Object obj){
				if(obj instanceof AccrualWarning){
						AccrualWarning one =(AccrualWarning)obj;
						return id.equals(one.getId());
				}
				return false;				
		}
		public int hashCode(){
				int seed = 29;
				if(!id.equals("")){
						try{
								seed += Integer.parseInt(id);
						}catch(Exception ex){
						}
				}
				return seed;
		}
    //
    // getters
    //
    public String getId(){
				return id;
    }
    public String getHour_code_id(){
				return hour_code_id;
    }
    public double getMin_hrs(){
				return min_hrs;
    }
    public double getStep_hrs(){
				return step_hrs;
    }
    public double getRelated_accrual_max_level(){
				return related_accrual_max_leval;
    }		
    public boolean require_min(){
				return min_hrs > 0.0;
    }
    public boolean require_step(){
				return step_hrs > 0.0;
    }
    public boolean require_accrual_max(){
				return related_accrual_max_leval > 0.0;
    }
    public String getStep_warning_text(){
				return step_warning_text;
    }
		public String getMin_warning_text(){
				return min_warning_text;
    }
		public String getExcess_warning_text(){
				return excess_warning_text;
    }
    //
    // setters
    //
    public void setId(String val){
				if(val != null)
						id = val;
    }
    public void setHour_code_id(String val){
				if(val != null)
						hour_code_id = val;
    }
    public void setMin_hrs(double val){
						min_hrs = val;
    }
    public void setStep_hrs(double val){
						step_hrs = val;
    }
    public void setRelated_accrual_max_level(double val){
						related_accrual_max_leval = val;
    }
    public void setStep_warning_text(String val){
				if(val != null)
						step_warning_text = val;
    }
    public void setMin_warning_text(String val){
				if(val != null)
						min_warning_text = val;
    }
    public void setExcess_warning_text(String val){
				if(val != null)
						excess_warning_text = val;
    }
		public HourCode getHourCode(){
				if(hourCode == null && !hour_code_id.equals("")){
						HourCode one = new HourCode(hour_code_id);
						String back = one.doSelect();
						if(back.equals("")){
								hourCode = one;
						}
				}
				return hourCode;
		}
    public String toString(){
				return id;
    }
		//
		public String doSelect(){
				String back = "";
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String qq = "select id,hour_code_id,min_hrs,step_hrs,related_accrual_max_leval,step_warning_text,min_warning_text,excess_warning_text "+
						"from accrual_warnings where id=?";
				con = Helper.getConnection();
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
								setVals(rs.getString(1),
												rs.getString(2),
												rs.getDouble(3),
												rs.getDouble(4),
												rs.getDouble(5),
												rs.getString(6),
												rs.getString(7),
												rs.getString(8));
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
						Helper.databaseDisconnect(con, pstmt, rs);			
				}
				return back;
		}
		public String doSave(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				if(hour_code_id.equals("")){
						msg = " hour code not set ";
						return msg;
				}
				if(excess_warning_text.equals("")){
						msg = " Excess warning test not set ";
						return msg;
				}				
				String qq = " insert into accrual_warnings values(0,?,?,?,?, ?,?,?)";
				try{
						con = Helper.getConnection();
						if(con == null){
								msg = "Could not connect to DB ";
								return msg;
						}
						pstmt = con.prepareStatement(qq);
						msg = setParams(pstmt);
						if(msg.equals("")){
								pstmt.executeUpdate();
								qq = "select LAST_INSERT_ID()";
								pstmt = con.prepareStatement(qq);
								rs = pstmt.executeQuery();
								if(rs.next()){
										id = rs.getString(1);
								}
						}
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(con, pstmt, rs);
				}
				return msg;
		}
		String setParams(PreparedStatement pstmt){
				String msg = "";
				int jj=1;
				try{
						pstmt.setString(jj++, hour_code_id);
						pstmt.setDouble(jj++, min_hrs);
						pstmt.setDouble(jj++, step_hrs);
						pstmt.setDouble(jj++, related_accrual_max_leval);
						if(step_warning_text.equals("")){
								pstmt.setNull(jj++, Types.VARCHAR);
						}
						else
								pstmt.setString(jj++, step_warning_text);										
						if(min_warning_text.equals("")){
								pstmt.setNull(jj++, Types.VARCHAR);
						}
						else
								pstmt.setString(jj++, min_warning_text);
						if(excess_warning_text.equals("")){
								pstmt.setNull(jj++, Types.VARCHAR);
						}
						else
								pstmt.setString(jj++, excess_warning_text);	
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg);
				}
				return msg;
		}
	public	String doUpdate(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = " update accrual_warnings set "+
						" hour_code_id=?, min_hrs=?,step_hrs=?,"+
						" related_accrual_max_leval=?, step_warning_text=?,"+
						" min_warning_text=?,excess_warning_text=? "+
						" where id=? ";
				if(hour_code_id.equals("")){
						msg = "hour code is required";
						return msg;
				}
				if(excess_warning_text.equals("")){
						msg = " Excess warning test not set ";
						return msg;
				}								
				try{
						con = Helper.getConnection();
						if(con == null){
								msg = "Could not connect to DB ";
								return msg;
						}
						pstmt = con.prepareStatement(qq);
						msg = setParams(pstmt);
						pstmt.setString(8, id);
						pstmt.executeUpdate();
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(con, pstmt, rs);
				}
				return msg;
		}		

}
