package in.bloomington.timer.bean;
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
import in.bloomington.timer.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BenefitGroup{

		static Logger logger = LogManager.getLogger(BenefitGroup.class);
		String id="", name="";
		boolean debug = false;
		boolean fullTime = false,
				exempt = false,
				temporary = false,
				unioned = false,
				afscme = false;

		public BenefitGroup(){
		}		
    public BenefitGroup(
												String val
												){
				setId(val);
		}		
    public BenefitGroup(boolean deb,
												String val
												){
				debug = deb;
				setId(val);
		}
    public BenefitGroup(boolean deb,
												String val,
												String val2,
												boolean val3,
												boolean val4,
												boolean val5
												){
				debug = deb;
				setId(val);
				setName(val2);
				setFullTime(val3);
				setExempt(val4);
				setUnioned(val5);
    }
    public BenefitGroup(boolean deb){
				debug = deb;
    }	
		public String getId(){
				return id;
    }
		public String getName(){
				return name;
		}

		public boolean isFullTime(){
				return fullTime;
    }
		public boolean getFullTime(){
				return fullTime;
		}
		public boolean isExempt(){
				return exempt;
    }
		public boolean getExempt(){
				return exempt;
		}
		public boolean getUnioned(){
				return unioned;
		}
		public boolean isUnioned(){
				return unioned;
		}
		public boolean isTemporary(){
				return temporary;
		}
		public boolean hasBenefits(){ // full time and part time w/benefits
				return !temporary; // evertybody except temp
		}
		public boolean overTimeElegible(){
				return temporary;  // only temp workers have overtime 
		}
		public boolean isAfscme(){
				return afscme;
		}
		public String toString(){
				return name;
		}
		public boolean equals(Object  gg){
				boolean match = false;
				if (gg != null && gg instanceof BenefitGroup){
						match = id.equals(((BenefitGroup)gg).id);
				}
				return match;
		}
		public int hashCode(){
				int code = 0;
				try{
						code = Integer.parseInt(id);
				}catch(Exception ex){};
				return code;
		}
    //
    // setters
    //
    public void setId (String val){
				if(val != null)		
						id = val;
    }
    public void setName(String val){
				if(val != null){
						name = val;
						if(name.indexOf("TEMP") > -1){
								temporary = true;
						}
						else if(name.indexOf("AFSCME") > -1){
								afscme = true;
								unioned = true;
						}
				}
    }
    public void setFullTime(boolean val){
				if(val){
						fullTime = true;
				}
    }	
    public void setExempt(boolean val){
				if(val){
						exempt = true;
				}
    }
    public void setUnioned(boolean val){
				if(val){
						unioned = true;
				}
    }	
   public  String doSave(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
		
				String qq = "insert into benefit_groups "+ 
						"  values(0,?,?,?,?) ";
				String back = "";

				try{
						if(debug){
								logger.debug(qq);
						}
						con = Helper.getConnection();				
						if(con == null){
								back = "Could not connect to DB ";
								return back;
						}
						pstmt = con.prepareStatement(qq);
						int jj = 1;
						pstmt.setString(jj++, name);
						if(fullTime)
								pstmt.setString(jj++,"y");
						else
								pstmt.setNull(jj++,Types.CHAR);
						if(exempt)
								pstmt.setString(jj++,"y");
						else
								pstmt.setNull(jj++,Types.CHAR);
						if(unioned)
								pstmt.setString(jj++,"y");
						else
								pstmt.setNull(jj++,Types.CHAR);
						pstmt.executeUpdate();
						qq = "select LAST_INSERT_ID()";
						pstmt = con.prepareStatement(qq);
						rs = pstmt.executeQuery();
						if(rs.next()){
								id = rs.getString(1);
						}						
				}
				catch(Exception ex){
						back += ex;
						logger.error(ex+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(con, pstmt, rs);
				}
				return back;
    }			

   public  String doUpdate(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
		
				String qq = 
						" update benefit_groups "+
						" set name=?, fullTime=?, exempt=?, unioned=? "+			
						" where id = ? ";
				String back = "";
				try{
						if(debug){
								logger.debug(qq);
						}
						con = Helper.getConnection();				
						// con = Helper.getConnection();
						if(con == null){
								back = "Could not connect to DB ";
								return back;
						}
						pstmt = con.prepareStatement(qq);
						int jj = 1;
						pstmt.setString(jj++, name);
						if(fullTime)
								pstmt.setString(jj++,"y");
						else
								pstmt.setNull(jj++,Types.CHAR);
						if(exempt)
								pstmt.setString(jj++,"y");
						else
								pstmt.setNull(jj++,Types.CHAR);
						if(unioned)
								pstmt.setString(jj++,"y");
						else
								pstmt.setNull(jj++,Types.CHAR);
						pstmt.setString(jj++,id);
						pstmt.executeUpdate();

				}
				catch(Exception ex){
						back += ex;
						logger.error(ex+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(con, pstmt, rs);
				}
				return back;
    }
   public  String doDelete(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
		
				String qq = 
						" delete from benefit_groups where id=?";
				String back = "";
				try{
						if(debug){
								logger.debug(qq);
						}
						con = Helper.getConnection();				
						if(con == null){
								back = "Could not connect to DB ";
								return back;
						}
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, id);
						pstmt.executeUpdate();

				}
				catch(Exception ex){
						back += ex;
						logger.error(ex+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(con, pstmt, rs);
				}
				return back;
    }				
  public  String doSelect(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
		
				String qq = "select name,fullTime,exempt,unioned from "+
						" benefit_groups where id = ? ";
				String back = "";
				try{
						if(debug){
								logger.debug(qq);
						}
						con = Helper.getConnection();				
						if(con == null){
								back = "Could not connect to DB ";
								return back;
						}
						pstmt = con.prepareStatement(qq);
						int jj = 1;
						pstmt.setString(jj,id);
						rs = pstmt.executeQuery();
						if(rs.next()){
								setName(rs.getString(1));
								setFullTime(rs.getString(2) != null);
								setExempt(rs.getString(3) != null);
								setUnioned(rs.getString(4) != null);
						}
				}
				catch(Exception ex){
						back += ex;
						logger.error(ex+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(con, pstmt, rs);
				}
				return back;
    }	

}
