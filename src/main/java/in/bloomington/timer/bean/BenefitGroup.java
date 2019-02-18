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
				nonExempt = false,
				temporary = false,
				tempWithBen = false,
				partTime = false, // non temp, exempt, ono-exempt
				unioned = false,
				afscme = false,
				isCedc = false,
				
				policeSworn = false,
				policeDetective = false,
				policeManagement = false,
				police = false,
				
				fire = false,
				fireSworn = false,
				fireSworn5to8 = false;
		String salary_group_name = "";
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
		public boolean isCedc(){
				return isCedc;
		}
		//
		// utility non union FT exempt Special group
		// uNon-U RFTx-Spec
		//
		public boolean isExemptSpecial(){
				return name.indexOf("RFTx-Spec") > 0;
		}
		public boolean isPartTime(){
				return partTime;
    }
		public boolean getExempt(){
				return exempt;
		}
		public boolean isNonExempt(){
				return // isFullTime() &&
						!isTemporary() &&
						!isUnioned() && !isExempt();
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
		public boolean isTempWithBen(){
				return tempWithBen;
		}		
		public boolean hasBenefits(){ // full time and part time w/benefits
				return !temporary; // everybody except temp
		}
		public boolean overTimeElegible(){
				return tempWithBen || temporary || afscme || isNonExempt();  // only temp workers have overtime 
		}
		public boolean isAfscme(){
				return afscme;
		}
		public String toString(){
				return name+" : "+salary_group_name;
		}
		public String getSalary_group_name(){
				return salary_group_name;
				/*
				if(isExempt()){
						return "Exempt";
				}
				else if(isNonExempt()){
						return "Non-Exempt";
				}
				else if(isUnioned()){
						return "Union";
				}
				else if(isTemporary()){
						return "Temp";
				}
				else if(isPartTime()){
						return "Part Time";
				}
				return "Unknown";
				*/
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
						setSalary_group_name();
				}
		}
		// this is needed for CEDC 5/2
		public void setSalary_group_name(String val){
				if(val != null){
						salary_group_name = val;
						if(val.equals("Exempt")){
								exempt = true;
								nonExempt = false;
						}
				}
		}
    public void setSalary_group_name(){
				// System.err.println(" name "+name);
				if(name.indexOf("TEMP") > -1){
						if(name.indexOf("BEN") > -1){
								salary_group_name = "Temp W/Ben";
								tempWithBen = true;
						}
						else{
								salary_group_name = "Temp";
								temporary = true;
						}
				}
				else if(name.indexOf("PART") > -1){
						// NON-U RPARTnx, NON-U RPARTx
						salary_group_name = "Part Time Non-Exempt";
						partTime = true;
						if(name.indexOf("Tx") > -1){
								salary_group_name = "Part Time Exempt";
								exempt = true;
						}
				}				
				else if(name.indexOf("RPT") > -1 || name.indexOf("LPT") > -1){
						// NON-U RPTx
						partTime = true;
						salary_group_name = "Part Time Non-Exempt";
						if(name.indexOf("Tx") > -1){
						salary_group_name = "Part Time Exempt";								
								exempt = true;
						}
				}
				else if(name.indexOf("nx") > -1){
						fullTime = true;
						salary_group_name = "Non-Exempt";
						nonExempt = true;
				}
				else if(name.indexOf("CEDC") > -1){ // CEDC 5/2 5 day work 2 off
						fullTime = true;
						salary_group_name = "Non-Exempt";
						nonExempt = true;
						isCedc = true;
						// if grade more than 6 will be exempt
						// we modify in profile
				}				
				else if(name.indexOf("LLx") > -1){
						fullTime = true;
						salary_group_name = "Exempt";
						exempt = true;
				}
				else if(name.indexOf("RFTx") > -1){
						fullTime = true;
						salary_group_name = "Exempt";
						exempt = true;
				}
				else if(name.indexOf("AFSCME") > -1){
						salary_group_name = "Union";
						afscme = true;
						unioned = true;
				}
				else if(name.indexOf("POLICE SWORN DET") > -1){
						policeDetective = true;
						fullTime = true;
						police = true;
						salary_group_name = "Police Sworn Det";
				}
				else if(name.indexOf("POLICE SWORN MGT") > -1){
						policeManagement = true;
						fullTime = true;
						police = true;
						salary_group_name = "Police Sworn Mgt";
				}
				else if(name.indexOf("POLICE SWORN") > -1){
						policeSworn = true;
						fullTime = true;
						police = true;
						salary_group_name = "Police Sworn";
				}
				else if(name.indexOf("FIRE SWORN 5X8") > -1){
						fireSworn5to8 = true;
						fullTime = true;
						fire = true;
						salary_group_name = "Fire Sworn 5X8";
				}				
				else if(name.indexOf("FIRE SWORN") > -1){
						fireSworn = true;
						fullTime = true;
						fire = true;
						salary_group_name = "Fire Sworn";
				}
				else{
						// System.err.println(" Unknown Salary group for "+name);
				}
    }
		public boolean isPolice(){
				return police;
		}
		public boolean isFire(){
				return fire;
		}
		public boolean isPoliceSworn(){
				return policeSworn;
		}
		public boolean isPoliceDetective(){
				return policeDetective;
		}
		public boolean isPoliceManagement(){
				return policeManagement;
		}
		public boolean isFireSworn(){
				return fireSworn;
		}
		public boolean isFireSworn5to8(){
				return fireSworn5to8;
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
						con = UnoConnect.getConnection();				
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
						back += ex;
						logger.error(ex+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(pstmt, rs);
						UnoConnect.databaseDisconnect(con);
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
						con = UnoConnect.getConnection();				
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
						Helper.databaseDisconnect(pstmt, rs);
						UnoConnect.databaseDisconnect(con);
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
						con = UnoConnect.getConnection();				
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
						Helper.databaseDisconnect(pstmt, rs);
						UnoConnect.databaseDisconnect(con);
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
						con = UnoConnect.getConnection();				
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
						Helper.databaseDisconnect(pstmt, rs);
						UnoConnect.databaseDisconnect(con);
				}
				return back;
    }	

}
