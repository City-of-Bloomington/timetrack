package in.bloomington.timer;
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
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HandleJobTitleUpdate{

		boolean debug = false;
		static final long serialVersionUID = 53L;
		static Logger logger = LogManager.getLogger(HandleJobTitleUpdate.class);
		EnvBean envBean = null;
		String date="";
		// Parks
		String department_id="5", dept_ref = "39"; // department reference to New World app
		// employee_number, jobs list
		Hashtable<String, Set<String>> empJobs = null;
		// NW employee jobs
		Hashtable<String, Set<String>> empNwJobs = null;
    public HandleJobTitleUpdate(EnvBean val){
				if(val != null)
						envBean = val;
    }
    public HandleJobTitleUpdate(){

    }		
    //
    // setters
    //
    public void setDate(String val){
				if(val != null){		
						date = val;
				}
    }
    public void setDeptRef(String val){
				if(val != null){		
						dept_ref = val;
				}
    }		
		//
   public String process(){
			 findEmployeeJobs();
			 findNWJobs();
			 String msg = "", status="Success", errors="";
			 System.err.println(" TimeTrack Jobs");
			 // System.err.println(empJobs);
			 Set<String> set = empJobs.keySet();
			 int jj=1, jj2=1;
			 for(String str:set){
					 Set<String> sst = empJobs.get(str);
					 if(empNwJobs.containsKey(str)){
							 jj++;
							 Set<String> sst2 = empNwJobs.get(str);
							 // System.err.println(jj+" found "+str+" "+sst+" "+sst2);
							 for(String str2:sst){
									 if(!sst2.contains(str2)){
											 System.err.println(str+" job not found in NW "+str2);
									 }
							 }
							 for(String str2:sst2){
									 if(!sst.contains(str2)){
											 System.err.println(str+" job not found in TT "+str2);
									 }
							 }
					 }
					 else{
							 System.err.println(jj2+" not found "+str+" "+sst);
							 jj2++;
					 }
			 }
			 /*
			 System.err.println(" New World Jobs");
			 Set<String> set2 = empNwJobs.keySet();
			 jj=1;
			 for(String str:set2){
					 Set<String> sst = empNwJobs.get(str);
					 System.err.println(jj+" "+str+" "+sst);
					 jj++;
			 }			 
			 */
			 // System.err.println(empNwJobs);				
			 return msg;
		}
		String findEmployeeJobs(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg = "";
				String qq = "select e.employee_number,"+
						" upper(p.name) "+
						" from jobs j, positions p,employees e ";
				String qw = " where p.id=j.position_id and j.employee_id=e.id ";
				qw += " and j.expire_date is null ";
				if(!department_id.equals("")){
						qq += ", department_employees d ";
						qw += " and d.employee_id=e.id and d.department_id=? ";
						qw += " and d.expire_date is null ";
				}
				qq += qw;
				qq += " order by e.employee_number";
				logger.debug(qq);
				con = UnoConnect.getConnection();
				if(con == null){
						msg = " Could not connect to DB ";
						logger.error(msg);
						return msg;
				}
				try{
						pstmt = con.prepareStatement(qq);
						int jj=1;				
						if(!department_id.equals("")){
								pstmt.setString(jj++, department_id);
						}
						rs = pstmt.executeQuery();
						empJobs = new Hashtable<>();
						while(rs.next()){
								String str = rs.getString(1); //  employee_number
								String str2 = rs.getString(2); // job title
								if(str != null && !str.equals("")){
										if(empJobs.containsKey(str)){
												Set<String> jobSet = empJobs.get(str);
												jobSet.add(str2);
										}
										else{
												Set<String> jobSet = new HashSet<>();
												jobSet.add(str2);
												empJobs.put(str, jobSet);
										}
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
		public String findNWJobs(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="";
				// using current date
				String qq = "select e.EmployeeNumber,p.JobTitle "+
						" from hr.vwEmployeeJobWithPosition p, "+
						" hr.employee e "+
						" where e.EmployeeId = p.EmployeeID "+
						" and getdate() between EffectiveDate and EffectiveEndDate "+
						" and getdate() between PositionDetailESD and PositionDetailEED ";
						// " and e.EmployeeNumber = ? "+
				qq += " and p.departmentID in ("+dept_ref+") "; 
				qq += " order by e.employeenumber ";
				// p.departmentID in ();
				con = SingleConnect.getNwConnection();
				if(con == null){
						msg = " Could not connect to DB ";
						logger.error(msg);
						return msg;
				}
				try{
						pstmt = con.prepareStatement(qq);
						// pstmt.setString(1, employee_number);
						empNwJobs = new Hashtable<>();
						rs = pstmt.executeQuery();
						while(rs.next()){
								String str = rs.getString(1); // emp number
								String str2 = rs.getString(2).toUpperCase(); 
								if(str != null){
										if(empNwJobs.containsKey(str)){
												Set<String> set = empNwJobs.get(str);
												set.add(str2);
										}
										else{
												Set<String> set = new HashSet<>();
												set.add(str2);
												empNwJobs.put(str, set);
										}
								}
						}
				}
				catch(Exception ex){
						msg += ex+":"+qq;
						logger.error(msg);
				}
				finally{
						Helper.databaseDisconnect(pstmt, rs);
				}
				return msg;
		}		
}
