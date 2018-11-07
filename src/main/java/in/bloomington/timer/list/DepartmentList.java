package in.bloomington.timer.list;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.*;
import java.sql.*;
import java.io.*;
import javax.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;

public class DepartmentList{

		static Logger logger = LogManager.getLogger(DepartmentList.class);
		static final long serialVersionUID = 3800L;
		String condition="", join="", sortBy="t.name"; 
		String name = ""; // for service
		String deptRefs = "";
		boolean active_only = false, hasRefIds = false; // all
		/*
			// Department ID's in New World app, needed
			// to find current employee in each department
			// and to get employee accruals
			//
			// OrgStructureID is Dept #, ITS = 16, animal shelter=17, 
				// clerk = 19, CFRD=20, controller=21,council=22,econ & sus=23,HR=24
				// engineering=25, facilities=26,HAND = 27,Legal = 28, Mayor = 29,
				// Parking = 30, plan = 31, PW Showers = 32, Risk = 33, Fire=35,
				// Police=36, fire=37,Fleet Maint=38, Parks & Rec=39		
				depts.put("ITS","16");
				depts.put("Animal Shelter","17");
				depts.put("Clerk","19");
				depts.put("CFRD","20");
				depts.put("Controller","21");		
				depts.put("Council","22"); xx
				depts.put("ESD","23"); 
				depts.put("HR","24");
				depts.put("Engineering","25"); // planning 
				depts.put("Facilities","26");
				depts.put("HAND","27");
				depts.put("Legal","28");
				depts.put("OOTM","29"); // office of the mayor
				depts.put("Parking","30");
				depts.put("Planning","25,31"); //include engineering
				depts.put("PUBLICWORKS","26,32"); // showers
				depts.put("Risk","33");
				depts.put("Fire","35");   
				depts.put("Police","36");
				depts.put("Fire","37");    // need FIX
				depts.put("Fleet Maintenance","38");
				depts.put("Fleet","38");		
				depts.put("Parks","39");
				//
				Sanitation    37
					Street    40
					Utilities/Accounting    42
					Utilities/Billings & Collections    43
					Utilities/Blucher Poole WWTP    44
					Utilities/Communications    46
					Utilities/Customer Relations    47
					Utilities/Department of the Director    48
					Utilities/Dillman Road WWTP    49
					Utilities/Engineering    50
					Utilities/Laboratory    53
					Utilities/Meters    55
					Utilities/Monroe Water Treatment Plant    56
					Utilities/Purchasing    57
					Utilities/Stormwater    58
					Utilities/Transmission & Distribution    59
					Utilities/Utility Service Board    60
				  42,43,44,46,47,48,49,50,53,55,56,57,58,59,60
		*/
		List<Department> departments = null;
		
		public DepartmentList(){
		}
		public List<Department> getDepartments(){
				return departments;
		}
		
		public void setName(String val){
				if(val != null)
						name = val;
		}
		public void setActiveOnly(){
				active_only = true;
		}
		public void setJoin(String val){
				if(val != null)
						join = val;
		}
		public void setCondition(String val){
				if(val != null)
						condition = val;
		}
		public void setSortBy(String val){
				if(val != null)
						sortBy = val;
		}
		public String getDeptRefs(){
				return deptRefs;
		}
		public void hasRefIds(){
				hasRefIds = true;
		}
		public String find(){
		
				String back = "";
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				Connection con = Helper.getConnection();
				String qq = "select t.id,t.name,t.description,t.ref_id,t.ldap_name,t.inactive from departments t ";
				if(con == null){
						back = "Could not connect to DB";
						return back;
				}
				String qw = "";
				if(!join.equals("")){
						qq += ", "+join;
						qw += condition;
				}
				try{
						if(!name.equals("")){
								if(!qw.equals("")) qw += " and ";
								qw += " (t.name like ? or t.ldap_name like ?)";
						}
						if(active_only){
								if(!qw.equals("")) qw += " and ";
								qw += " t.inactive is null ";
						}
						if(hasRefIds){
								if(!qw.equals("")) qw += " and ";
								qw += " t.ref_id is not null ";
						}
						if(!qw.equals("")){
								qq += " where "+qw;
						}
						if(!sortBy.equals("")){
								qq += " order by "+sortBy;
						}
						logger.debug(qq);
						pstmt = con.prepareStatement(qq);
						if(!name.equals("")){
								pstmt.setString(1,"%"+name+"%");
								pstmt.setString(2,"%"+name+"%");								
						}						
						rs = pstmt.executeQuery();
						if(departments == null)
								departments = new ArrayList<>();
						while(rs.next()){
								Department one =
										new Department(rs.getString(1),
																	 rs.getString(2),
																	 rs.getString(3),
																	 rs.getString(4),
																	 rs.getString(5),
																	 rs.getString(6)!=null);
								if(!departments.contains(one))
										departments.add(one);
						}
				}
				catch(Exception ex){
						back += ex+" : "+qq;
						logger.error(back);
				}
				finally{
						Helper.databaseDisconnect(con, pstmt, rs);
				}
				return back;
		}
		public String findDeptRefs(){
		
				String back = "";
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				Connection con = Helper.getConnection();
				String qq = "select t.ref_id from departments t where ref_id is not null and inactive is null";
				if(con == null){
						back = "Could not connect to DB";
						return back;
				}
				try{
						logger.debug(qq);
						pstmt = con.prepareStatement(qq);
						deptRefs = "";
						while(rs.next()){
								String str = rs.getString(1);
								if(!deptRefs.equals("")) deptRefs += ",";
								deptRefs += str;
						}
				}
				catch(Exception ex){
						back += ex+" : "+qq;
						logger.error(back);
				}
				finally{
						Helper.databaseDisconnect(con, pstmt, rs);
				}
				return back;
		}
		
}






















































