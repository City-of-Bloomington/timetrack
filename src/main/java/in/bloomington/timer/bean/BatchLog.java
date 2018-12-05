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

public class BatchLog{

		static final long serialVersionUID = 3700L;	
		static Logger logger = LogManager.getLogger(BatchLog.class);
    String id="", document_id="", name="",date="", status="";
		//
		public BatchLog(String val){
				//
				setId(val);
    }
		// for saving
		public BatchLog(String val, String val2,
													 String val3
													 ){
				setDocument_id(val);
				setName(val2);
				setStatus(val3);
    }				
		public BatchLog(String val, String val2,
													 String val3, String val4,
													 String val5
													 ){
				setId(val);
				setDocument_id(val2);
				setName(val3);
				setDate(val4);
				setStatus(val5);
    }		
    //
    // getters
    //
    public String getId(){
				return id;
    }
    public String getDocument_id(){
				return document_id;
    }
    public String getName(){
				return name;
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
    public void setDocument_id(String val){
				if(val != null)
						document_id = val.trim();
    }
    public void setName(String val){
				if(val != null)
						name = val.trim();
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
				return name;
    }
		//
		public String doSelect(){
				String back = "";
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String qq = "select id,document_id,name,date_format(date,'%m/%d/%Y %h:%i'),status from batch_logs where id=?";
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
								setDocument_id(rs.getString(2));
								setName(rs.getString(3));
								setDate(rs.getString(4));
								setStatus(rs.getString(5));
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
				}
				return back;
		}
		public String doSave(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = " insert into batch_logs values(0,?,?,now(),?)";
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could not connect to DB ";
						return msg;
				}				
				try{

						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, document_id);
						pstmt.setString(2, name);
						pstmt.setString(3, status);
						pstmt.executeUpdate();
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
				}
				return msg;
		}

}
