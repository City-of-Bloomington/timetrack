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

public class TmwrpRun{

    static Logger logger = LogManager.getLogger(TmwrpRun.class);
    static final long serialVersionUID = 1500L;
    String id="", pay_period_id="", run_time="", status="", error_text="";
    String department_id="";
    //
    Department department = null;
		PayPeriod payPeriod = null;
		/*
		List<GroupShift> groupShifts = null;
		*/
		
    public TmwrpRun(){
    }		
    public TmwrpRun(String val){
				setId(val);
    }
    public TmwrpRun(String val,
										String val2,
										String val3,
										String val4,
										String val5,
										String val6
								 ){
				setId(val);
				setPay_period_id(val2);
				setDepartment_id(val3);
				setRunTime(val4);
				setStatus(val5);
				setErrorText(val6);				
    }
    public TmwrpRun(String val,
										String val2,
										String val3,
										String val4,
										String val5,
										String val6,
										String val7
								 ){
				setId(val);
				setPay_period_id(val2);
				setDepartment_id(val3);
				setRunTime(val4);
				setStatus(val5);
				setErrorText(val6);
				if(val7 != null){
						department = new Department(department_id, val7);
				}
    }		
    //
    // getters
    //
    public String getId(){
				return id;
    }
		public String getPay_period_id(){
				return pay_period_id;
		}
    public String getDepartment_id(){
				return department_id;
    }

		public String getRunTime(){
				return run_time;
		}
		public String getStatus(){
				return status;
		}
		public String getErrorText(){
				return error_text;
		}
    //
    // setters
    //
    public void setId(String val){
				if(val != null)
						id = val;
    }
    public void setPay_period_id(String val){
				if(val != null)
						pay_period_id = val;
    }
    public void setErrorText(String val){
				if(val != null){
						error_text = val.trim();
				}
    }
    public void setStatus(String val){
				if(val != null){
						status = val;
				}
    }
    public void setRunTime(String val){
				if(val != null){
						run_time = val;
				}
    }		
		
    public void setDepartment_id (String val){
				if(val != null && !val.equals("-1"))
						department_id = val;
    }

    public boolean equals(Object o) {
				if (o instanceof TmwrpRun) {
						TmwrpRun c = (TmwrpRun) o;
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
    public String toString(){
				return id;
    }
		
    public Department getDepartment(){
				if(department == null && !department_id.equals("")){
						Department one = new Department(department_id);
						String back = one.doSelect();
						if(back.equals("")){
								department = one;
						}
				}
				return department;
    }
    public PayPeriod getPayPeriod(){
				if(payPeriod == null && !pay_period_id.equals("")){
						PayPeriod one = new PayPeriod(pay_period_id);
						String back = one.doSelect();
						if(back.equals("")){
								payPeriod = one;
						}
				}
				return payPeriod;
    }
		// ToDo start here
		
    public String doSelect(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "select g.id,g.pay_period_id,g.department_id,"+
						"date_format(g.run_time,'%m/%d/%y %H:%i'),g.status,d.error_text from tmwrp_runs g where g.id =? ";
				logger.debug(qq);
				con = UnoConnect.getConnection();				
				try{
						if(con != null){
								pstmt = con.prepareStatement(qq);
								pstmt.setString(1, id);
								rs = pstmt.executeQuery();
								if(rs.next()){
										setPay_period_id(rs.getString(2));
										setDepartment_id(rs.getString(3));
										setRunTime(rs.getString(4));
										setStatus(rs.getString(5));
										setErrorText(rs.getString(6));
								}
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
				return msg;
    }

    public String doSave(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "insert into tmwrp_runs values(0,?,?,now(),null,null) ";
				if(department_id.equals("")){
						msg = " department not set ";
						return msg;
				}
				if(pay_period_id.equals("")){
						msg = " pay period not set ";
						return msg;
				}				
				logger.debug(qq);
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could not connect to DB ";
						return msg;
				}				
				try{
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, pay_period_id);						
						pstmt.setString(2, department_id);
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
				return msg;
    }

    public String doDelete(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "delete tmwrp_runs where id=? ";
				logger.debug(qq);
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could not connect to DB ";
						return msg;
				}							
				try{
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, id);
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
				return msg;
    }
		/**
		 * update status Error or Success
		 * if error add error text
		 */
    public String doUpdateStatus(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "update tmwrp_runs set status=?,error_text=? where id=? ";
				logger.debug(qq);
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could not connect to DB ";
						return msg;
				}							
				try{
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, status);
						if(error_text.equals(""))
								pstmt.setNull(2, Types.VARCHAR);
						else
								pstmt.setString(2, error_text);
						pstmt.setString(3, id);
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
				return msg;
    }		
		

}
