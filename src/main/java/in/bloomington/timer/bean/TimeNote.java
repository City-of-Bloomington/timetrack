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

public class TimeNote{

		static final long serialVersionUID = 3700L;	
		static Logger logger = LogManager.getLogger(TimeNote.class);
    String id="", document_id="", reported_by="", date="", notes="";
		Employee reporter = null;
		//
		public TimeNote(){

		}
		public TimeNote(String val){
				//
				setId(val);
    }		
		public TimeNote(String val, String val2, String val3, String val4, String val5){
				setId(val);
				setDocument_id(val2);
				setReported_by(val3);
				setDate(val4);
				setNotes(val5);
    }		
		public boolean equals(Object obj){
				if(obj instanceof TimeNote){
						TimeNote one =(TimeNote)obj;
						return id.equals(one.getId());
				}
				return false;				
		}
		public int hashCode(){
				int seed = 17;
				if(!id.isEmpty()){
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
    public String getDocument_id(){
				return document_id;
    }
    public String getReported_by(){
				return reported_by;
    }		
		public String getDate(){
				return date;
		}
		public String getNotes(){
				return notes;
		}
		public Employee getReporter(){
				if(reporter == null && !reported_by.isEmpty()){
						Employee one = new Employee(reported_by);
						String back = one.doSelect();
						if(back.isEmpty()){
								reporter = one;
						}
				}
				return reporter;
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
						document_id = val;
    }
    public void setReported_by(String val){
				if(val != null)
						reported_by = val;
    }		
    public void setNotes(String val){
				if(val != null)
						notes = val.trim();
    }
    public void setDate(String val){
				if(val != null)
						date = val;
    }		
		//
		public String doSelect(){
				String back = "";
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String qq = "select id,document_id,reported_by,date_format(date,'%m/%d/%Y %H:%i'),notes "+
						"from time_notes where id=?";
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
								setReported_by(rs.getString(3));
								setDate(rs.getString(4));
								setNotes(rs.getString(5));
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
	public	String doSave(){
				Connection con = null;
				PreparedStatement pstmt = null, pstmt2=null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = " insert into time_notes values(0,?,?,now(),?)";
				if(notes.isEmpty()){
						msg = "notes is required";
						return msg;
				}
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could not connect to DB ";
						return msg;
				}
				try{
						pstmt = con.prepareStatement(qq);
						msg = setParams(pstmt);
						if(msg.isEmpty()){
								pstmt.executeUpdate();
								//
								qq = "select LAST_INSERT_ID()";
								pstmt2 = con.prepareStatement(qq);
								rs = pstmt2.executeQuery();
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
						Helper.databaseDisconnect(rs, pstmt, pstmt2);
						UnoConnect.databaseDisconnect(con);
				}
				return msg;
		}
		public String doDelete(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = " delete from time_notes where id=?";
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
		
		String setParams(PreparedStatement pstmt){
				String msg = "";
				int jj=1;
				try{
						pstmt.setString(jj++, document_id);
						pstmt.setString(jj++, reported_by);
						pstmt.setString(jj++, notes);										
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg);
				}
				return msg;
		}

}
