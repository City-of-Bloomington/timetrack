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

public class EmployeesLog{

		static final long serialVersionUID = 3700L;	
		static Logger logger = LogManager.getLogger(EmployeesLog.class);
    String id="", empsIdSet="", date="", status="", errors="";
		//
		public EmployeesLog(String val){
				//
				setId(val);
    }
		// for saving
		public EmployeesLog(String val,
												 String val2,
												 String val3
												 ){
				setEmpsIdSet(val);
				setStatus(val2);
				setErrors(val3);
    }				
		public EmployeesLog(String val,
												String val2,
												String val3,
												String val4,
												String val5
										){
				setId(val);
				setEmpsIdSet(val2);
				setDate(val3);
				setStatus(val4);
				setErrors(val5);
    }		
    //
    // getters
    //
    public String getId(){
				return id;
    }
    public String getEmpsIdSet(){
				return empsIdSet;
    }
    public String getErrors(){
				return errors;
    }		
    public String getDate(){
				return date;
    }
		public String getStatus(){
				return status;
    }
    //
    // setters
    //
    public void setId(String val){
				if(val != null)
						id = val;
    }
    public void setEmpsIdSet(String val){
				if(val != null)
						empsIdSet = val.trim();
    }
    public void setErrors(String val){
				if(val != null)
						errors = val.trim();
    }
    public void setStatus(String val){
				if(val != null)
						status = val;
    }
    public void setDate(String val){
				if(val != null)
						date = val;
    }
    public String toString(){
				String ret = date+" "+status;
				if(!empsIdSet.isEmpty())
						ret += ", "+empsIdSet;
				if(!errors.isEmpty())
						ret += ", "+errors;
				return ret;
    }
		public boolean hasErrors(){
				return !errors.isEmpty();

		}
		//
		public String doSelect(){
				String back = "";
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String qq = "select id,emps_id_set,date_format(date,'%m/%d/%Y %h:%i'),status,errors from employees_logs where id=?";
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
								setEmpsIdSet(rs.getString(2));
								setDate(rs.getString(3));
								setStatus(rs.getString(4));
								setErrors(rs.getString(5));
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
				return back;
		}
		public String doSave(){
				Connection con = null;
				PreparedStatement pstmt = null, pstmt2=null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = " insert into employees_logs values(0,?,now(),?,?)";
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could not connect to DB ";
						return msg;
				}
				try{
						pstmt = con.prepareStatement(qq);
						if(empsIdSet.isEmpty())
								pstmt.setNull(1, Types.VARCHAR);
						else
								pstmt.setString(1, empsIdSet);
						pstmt.setString(2, status);
						if(errors.isEmpty())
								pstmt.setNull(3, Types.VARCHAR);
						else
								pstmt.setString(3, errors);
						pstmt.executeUpdate();
						//
						qq = "select LAST_INSERT_ID()";
						pstmt2 = con.prepareStatement(qq);
						rs = pstmt2.executeQuery();
						if(rs.next()){
								id = rs.getString(1);
						}
						date = Helper.getToday();
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(rs, pstmt, pstmt2);
						UnoConnect.databaseDisconnect(con);
				}
				return msg;
		}

}
