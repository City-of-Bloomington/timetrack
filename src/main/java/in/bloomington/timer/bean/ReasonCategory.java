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

public class ReasonCategory implements java.io.Serializable{

		static final long serialVersionUID = 3700L;	
		static Logger logger = LogManager.getLogger(ReasonCategory.class);
    String id="", name="", inactive="";
		//
		public ReasonCategory(){

		}
		public ReasonCategory(String val){
				//
				setId(val);
    }		
		public ReasonCategory(String val, String val2){
				//
				setId(val);
				setName(val2);
    }
		public ReasonCategory(String val, String val2, boolean val3){
				setId(val);
				setName(val2);
				setInactive(val3);
    }		
		public boolean equals(Object obj){
				if(obj instanceof ReasonCategory){
						ReasonCategory one =(ReasonCategory)obj;
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
    public String getName(){
				return name;
    }
    public boolean getInactive(){
				return !inactive.isEmpty();
    }
		public boolean isInactive(){
				return !inactive.isEmpty();
		}
		public boolean isActive(){
				return inactive.isEmpty();
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
    
    public void setInactive(boolean val){
				if(val)
						inactive = "y";
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
				String qq = "select id,name,inactive "+
						"from reason_categories where id=?";
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
								setInactive(rs.getString(3) != null);
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
				inactive=""; // default
				String qq = "select count(*) "+
						"from reason_categories where name like ?";
				String qq2 = " insert into reason_categories values(0,?,?)";
				if(name.isEmpty()){
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
						rs = pstmt.executeQuery();
						if(rs.next()){
								cnt = rs.getInt(1);
						}
						if(cnt > 0){
								msg = "This name alrady exist";
						}
						else {
								pstmt = con.prepareStatement(qq2);						
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
		String setParams(PreparedStatement pstmt){
				String msg = "";
				int jj=1;
				try{
						pstmt.setString(jj++, name);
						if(inactive.isEmpty())
								pstmt.setNull(jj++, Types.CHAR);
						else
								pstmt.setString(jj++, "y");						
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg);
				}
				return msg;
		}
		public String doUpdate(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = " update reason_categories set name=?, inactive=? where id=?";
				if(name.isEmpty()){
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
						msg = setParams(pstmt);
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
