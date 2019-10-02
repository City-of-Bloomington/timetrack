package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.sql.*;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;

public class Position implements java.io.Serializable{

		static final long serialVersionUID = 3750L;	
		static Logger logger = LogManager.getLogger(Position.class);
    String id="", name="", description="", alias="", inactive="";
		//
		public Position(){
				super();
		}
		public Position(String val){
				//
				setId(val);
    }		
		public Position(String val, String val2){
				//
				// initialize
				//
				setId(val);
				setName(val2);
    }
		public Position(String val, String val2, String val3){
				// // for new record
				setName(val);
				setAlias(val2);
				setDescription(val3);
    }		
		public Position(String val, String val2, String val3, String val4, boolean val5){
				setId(val);
				setName(val2);
				setAlias(val3);
				setDescription(val4);
				setInactive(val5);
    }		
		public boolean equals(Object obj){
				if(obj instanceof Position){
						Position one =(Position)obj;
						return id.equals(one.getId());
				}
				return false;				
		}
		public int hashCode(){
				int seed = 17;
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
    public String getName(){
				return name;
    }
    public String getAlias(){
				return alias;
    }		
    public String getDescription(){
				return description;
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
    public void setAlias(String val){
				if(val != null)
						alias = val.trim();
    }		
    public void setDescription(String val){
				if(val != null){
						description = val.trim();
				}
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
				String qq = "select name,alias,description,inactive "+
						"from positions where id=?";
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
								setName(rs.getString(1));
								setAlias(rs.getString(2));								
								setDescription(rs.getString(3));
								setInactive(rs.getString(4) != null);
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
				PreparedStatement pstmt = null, pstmt2=null, pstmt3=null;
				ResultSet rs = null;
				String msg="", str="";
				inactive=""; // default
				String qq = "select count(*) "+
						"from positions where name like ?";
				String qq2 = " insert into positions values(0,?,?,?,?)";
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
						rs = pstmt.executeQuery();
						if(rs.next()){
								cnt = rs.getInt(1);
						}
						if(cnt > 0){
								msg = "This name alrady exist";
						}
						else {
								pstmt2 = con.prepareStatement(qq2);						
								msg = setParams(pstmt2);
								if(msg.equals("")){
										pstmt2.executeUpdate();
										//
										qq = "select LAST_INSERT_ID()";
										pstmt3 = con.prepareStatement(qq);
										rs = pstmt3.executeQuery();
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
						Helper.databaseDisconnect(rs, pstmt, pstmt2, pstmt3);
						UnoConnect.databaseDisconnect(con);
				}
				return msg;
		}
		public String doSaveBatch(List<String> batch){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="";
				String qq = " insert into positions values(0,?,?,?,null)";
				if(batch == null || batch.size() == 0){
						return ""; // nothing to be done
				}
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could not connect to DB ";
						return msg;
				}
				try{
						pstmt = con.prepareStatement(qq);
						for(String str:batch){
								pstmt.setString(1, str);
								pstmt.setString(2, str);
								pstmt.setString(3, str);
								pstmt.executeUpdate();
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
		String setParams(PreparedStatement pstmt){
				String msg = "";
				int jj=1;
				try{
						pstmt.setString(jj++, name);
						if(alias.equals("")){
								alias = name;
						}
						pstmt.setString(jj++, alias);
						if(description.equals("")){
								description = name;
						}
						pstmt.setString(jj++, description);										
						if(inactive.equals(""))
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
				String qq = " update positions set name=?, alias=?, description=?,inactive=? where id=?";
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
						msg = setParams(pstmt);
						pstmt.setString(5, id);
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
