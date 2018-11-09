package in.bloomington.timer.list;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */

import java.util.*;
import java.sql.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;

public class ProfileList{

    boolean debug = false;
		static final long serialVersionUID = 50L;
		static Logger logger = LogManager.getLogger(ProfileList.class);
		boolean currentOnly = false;
		Hashtable<String, BenefitGroup> bgroups = null;
		PayPeriod payPeriod = null;
		String end_date = null; // for planning when looking for date range
		String selected_dept_ref = "";
		String deptRefs = null;
		List<Profile> profiles = null;
    //
    // basic constructor
		//
    public ProfileList(boolean deb){
				debug = deb;
    }
		// specific dept
    public ProfileList(String val,
											 String val2,
											 List<BenefitGroup> vals){
				setEndDate(val);
				setSelectedDeptRef(val2);
				setBenefitGroups(vals);
		}
		// all depts
    public ProfileList(boolean deb,
											 Hashtable<String, BenefitGroup> vals,
											 PayPeriod period,
											 boolean currentOnly){
				debug = deb;
				setBenefitGroups(vals);
				setPayPeriod(period);
				this.currentOnly = currentOnly;
    }
		// all depts
    public ProfileList(boolean deb,
											 List<BenefitGroup> vals,
											 String val, // date
											 String val2, // dept_ref
											 boolean currentOnly){
				debug = deb;
				setBenefitGroups(vals);
				setEndDate(val);
				setSelectedDeptRef(val2);
				this.currentOnly = currentOnly;
    }		
    //
    // setters
    //
		public void setBenefitGroups(Hashtable<String,
																 BenefitGroup> vals){ // benefit Group
				if(vals != null)
						bgroups = vals;
    }
		public void setBenefitGroups(List<BenefitGroup> vals){
				if(vals != null){
						bgroups = new Hashtable<>();
						for(BenefitGroup one:vals){
								bgroups.put(one.getId(), one);
						}
				}
		}
		private void setPayPeriod(PayPeriod val){
				if(val != null)
						payPeriod = val;
		}
		public void setSelectedDeptRef(String val){
				if(val != null && !val.equals("")){
						selected_dept_ref = val;
				}
		}
		public void setEndDate(String val){
				if(val != null){
						end_date = val;
				}
		}
		public List<Profile> getProfiles(){
				return profiles;
		}
		/**
		 * not used yet
		 * finding job titles for people with multiple jobs
		 * example Parks & Rec employees
		 */
		public  String findJobs(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", back="", date = null;
				if(payPeriod != null){
						date = payPeriod.getEnd_date();
				}
				else if(!end_date.equals("")){
						date = end_date;
				}
				if(date == null){
						date = Helper.getToday();
				}
				// using current date
				String qq = "select p.JobTitle, e.EmployeeNumber, p.* "+
						" from hr.vwEmployeeJobWithPosition p, "+
						" hr.employee e "+
						" where e.EmployeeId = p.EmployeeID "+
						" and getdate() between EffectiveDate and EffectiveEndDate "+
						" and getdate() between PositionDetailESD and PositionDetailEED "+
						" and e.EmployeeNumber = '1886' "+
						" order by e.employeenumber ";
				con = SingleConnect.getNwConnection();
				if(con == null){
						msg = " Could not connect to DB ";
						logger.error(msg);
						return msg;
				}
				try{
						if(debug)
								logger.debug(qq);
						pstmt = con.prepareStatement(qq);
						rs = pstmt.executeQuery();
						ResultSetMetaData rsmd = rs.getMetaData();
						int columnCount = rsmd.getColumnCount();
						// The column count starts from 1
						for (int i = 1; i <= columnCount; i++ ) {
								String str = rsmd.getColumnName(i);
								System.err.println(i+" "+str);
						}
						/*
						while(rs.next()){


						}
						*/
				}
				catch(Exception ex){
						back += ex;
						logger.error(ex+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(pstmt, rs);
				}
				return back;
		}
    //
    // find all matching records
    // return "" or any exception thrown by DB
    //
		public  String find(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", back="", date = null;
				if(payPeriod != null){
						date = payPeriod.getEnd_date();
				}
				else if(!end_date.equals("")){
						date = end_date;
				}
				if(date == null){
						date = Helper.getToday();
				}
				if(selected_dept_ref.equals("")){
						if(deptRefs == null){
								DepartmentList dl = new DepartmentList();
								msg = dl.findDeptRefs();
								if(msg.equals("")){
										deptRefs = dl.getDeptRefs();
								}
						}
				}
				/*
				1 EmployeeJobID
				2 EffectiveDate
				3 EffectiveEndDate
				4 EmployeeID
				5 GradeId
				6 GradeType
				7 GradeStepId
				8 CycleHours
				9 DailyHours
				10 DepartmentId
				11 RateAmount
				12 PositionId
				13 JobId
				14 JobTitle
				15 PositionDetailESD  // date
				16 PositionDetailEED  // date
				17 IsPrimaryJob
				18 JobEventReasonId
				19 PositionNumber
				20 GradeCode
				21 EmployeeId
				22 EmployeeNumber
				23 DepartmentId
				24 OrgStructureDescconcatenated
				25 BenefitGroupId
				26 xGroupCodeDesc
				27 PositionTitle
				28 PositionNumberMasked
				29 vsEmploymentStatusId
				30 EmploymentStatus
				31 EmployeeSSN
				32 FirstName
				33 MiddleName
				34 LastName
				35 EmployeeName
				36 Pending
				37 ProcessStatus
				38 StandardWeeklyHours
				39 UDFAttributeID
				40 valInt
				41 valDecimal
				*/
				String qq = "select ejp.*, g.GradeCode, ei.*, "+
						" ej.StandardWeeklyHours, "+
						" ur.UDFAttributeID,ur.valInt,ur.valDecimal "+ 
						" from HR.vwEmployeeJobWithPosition ejp,"+
						" (SELECT  "+
						" E.EmployeeId, "+  
						" E.EmployeeNumber, "+ 
						" ISNULL(Jobs.DepartmentId, -1) AS DepartmentId, "+  
						" O.OrgStructureDescconcatenated, "+
						" Jobs.BenefitGroupId, "+ 
						" GH.xGroupCodeDesc, "+
						" CASE WHEN Jobs.Title IS NULL THEN P.PositionNumberMasked + ' - ' + PD.PositionTitle ELSE Jobs.Title END AS PositionTitle,  "+
						" P.PositionNumberMasked, "+ 
						" EE.vsEmploymentStatusId, "+
						" VSE1.Description AS EmploymentStatus, "+
						" SUBSTRING(ED.EmployeeSSN, 1, 3) + '-' + SUBSTRING(ED.EmployeeSSN, 4, 2) + '-' + SUBSTRING(ED.EmployeeSSN, 6, 4) AS EmployeeSSN, "+
						" EN.FirstName, "+ 
						" EN.MiddleName, "+
						" EN.LastName, "+
						" EN.LastName +  ', ' + EN.FirstName + CASE WHEN EN.MiddleName IS NOT NULL AND LEN(EN.MiddleName) > 0 THEN ' ' + EN.MiddleName ELSE '' END + CASE WHEN VSE2.Description IS NOT NULL THEN ' ' + VSE2.Description ELSE '' END AS EmployeeName, "+
						" CASE WHEN EA.EmployeeAddressId IS NULL OR Jobs.EmployeeJobId IS NULL THEN 1 ELSE 0 END AS Pending, "+
						" E.ProcessStatus "+
						" FROM HR.Employee E "+ 
						" LEFT JOIN ( "+
						" SELECT X.EmployeeId, X.EmployeeJobId, X.DepartmentId, X.BenefitGroupId, X.PositionId, X.EffectiveDate, X.EffectiveEndDate, X.Title FROM ( "+
						" SELECT E.EmployeeId, EmployeeJobId, DepartmentId, BenefitGroupId, PositionId, EffectiveDate, EffectiveEndDate, Title,  ROW_NUMBER() OVER (PARTITION BY E.EmployeeId ORDER BY CASE WHEN EJ.EffectiveDate <= '"+date+"' AND EJ.EffectiveEndDate >='"+date+"' THEN '99991231' ELSE EJ.EffectiveEndDate END DESC) AS RowNumber "+
						" FROM HR.Employee E "+ 
						" LEFT JOIN HR.EmployeeJob EJ ON E.EmployeeId = EJ.EmployeeId "+ 
						" WHERE EJ.IsPrimaryJob = 1 ) X "+
						" WHERE X.RowNumber = 1 ) Jobs ON Jobs.EmployeeId = E.EmployeeId "+
						" LEFT JOIN Position P ON P.PositionID = Jobs.PositionId "+
						" LEFT JOIN PositionDetail PD ON PD.PositionID = Jobs.PositionId "+
						" AND (PD.PositionDetailESD <= CASE WHEN Jobs.EffectiveDate <='"+date+"' AND Jobs.EffectiveEndDate >= '"+date+"' THEN '"+date+"'  "+
						" WHEN Jobs.EffectiveDate < '"+date+"' AND Jobs.EffectiveEndDate < '"+date+"' THEN Jobs.EffectiveEndDate "+
						" WHEN Jobs.EffectiveDate > '"+date+"' THEN Jobs.EffectiveDate END AND "+ 
						" PD.PositionDetailEED >= CASE WHEN Jobs.EffectiveDate <='"+date+"' AND Jobs.EffectiveEndDate >= '"+date+"' THEN '"+date+"'  "+
						" WHEN Jobs.EffectiveDate < '"+date+"' AND Jobs.EffectiveEndDate < '"+date+"' THEN Jobs.EffectiveEndDate "+
						" WHEN Jobs.EffectiveDate > '"+date+"' THEN Jobs.EffectiveDate END)  JOIN ( "+
						" SELECT  X.EmployeeId, X.EmployeeNameId, X.FirstName, X.LastName, X.MiddleName, X.vsNameSuffixId FROM ( "+
						" SELECT E.EmployeeId, EN.EmployeeNameId, EN.FirstName, EN.LastName, EN.MiddleName, EN.vsNameSuffixId, ROW_NUMBER() OVER (PARTITION BY E.EmployeeId ORDER BY CASE WHEN EN.EffectiveDate <= '"+date+"' AND EN.EffectiveEndDate >= '"+date+"' THEN '99991231' ELSE EN.EffectiveEndDate END DESC) AS RowNumber "+
						" FROM HR.Employee E "+ 
						" JOIN HR.EmployeeName EN ON E.EmployeeId = EN.EmployeeId "+
						" ) X "+
						" WHERE X.RowNumber = 1) EN ON EN.EmployeeId = E.EmployeeId "+
						" JOIN HR.EmployeeDemographics ED ON E.EmployeeId = ED.EmployeeId "+
						" JOIN (SELECT  X.EmployeeId, X.vsEmploymentStatusId FROM ( "+
						" SELECT E.EmployeeId, EE.vsEmploymentStatusId, ROW_NUMBER() OVER (PARTITION BY E.EmployeeId ORDER BY CASE WHEN EE.EffectiveDate <='"+date+"' AND EE.EffectiveEndDate >= '"+date+"' THEN '99991231' ELSE EE.EffectiveEndDate END DESC) AS RowNumber "+
						" FROM HR.Employee E "+ 
						" JOIN HR.EmployeeEmployment EE ON E.EmployeeId = EE.EmployeeId "+
						" ) X "+ 
						" WHERE X.RowNumber = 1) EE ON EE.EmployeeId = E.EmployeeId "+
						" LEFT JOIN ValidationSetEntry VSE1 ON VSE1.EntryID = EE.vsEmploymentStatusId "+
						" LEFT JOIN ValidationSetEntry VSE2 ON VSE2.EntryID = EN.vsNameSuffixId  "+
						" LEFT JOIN HR.vwOrgstructure O ON O.OrgStructureID = Jobs.DepartmentID "+
						" JOIN ( "+
						" SELECT  X.EmployeeId, X.EmployeeAddressId FROM ( "+
						" SELECT E.EmployeeId, EA.EmployeeAddressId, ROW_NUMBER() OVER (PARTITION BY E.EmployeeId ORDER BY CASE WHEN EA.EffectiveDate <='"+date+"' AND EA.EffectiveEndDate >= '"+date+"' THEN '99991231' ELSE EA.EffectiveEndDate END DESC) AS RowNumber "+
						" FROM HR.Employee E "+
						" LEFT JOIN HR.EmployeeAddress EA ON E.EmployeeId = EA.EmployeeId "+
						" ) X "+
						" WHERE X.RowNumber = 1) EA ON EA.EmployeeId = E.EmployeeId "+
						" LEFT JOIN xGroupHeader GH ON GH.xGroupHeaderID = Jobs.BenefitGroupId "+
						" ) ei, "+
						" HR.Grade g, "+
						" HR.EmployeeJob ej "+
						" left join dbo.UDFEntry ur on ur.AttachedFKey=ej.EmployeeJobID and ur.tableId=70 "+						
						" where ej.EmployeeID=ejp.EmployeeID "+
						" and ej.GradeId = g.GradeId "+
						" and ej.effectiveEndDate >= '"+date+"'"+ 
						" and ej.effectivedate <= '"+date+"'"+						
						" and ei.employeeID = ejp.employeeID "+
						" and g.GradeId = ejp.GradeId "+
						" and ejp.effectivedate <= '"+date+"'"+
						" and ejp.EffectiveEndDate >= '"+date+"' "+
						" and ejp.PositionDetailEED >= '"+date+"' "+
						" and ejp.PositionDetailESD <= '"+date+"' "+
						" and ejp.IsPrimaryJob = 1 ";
						//" and (ejp.jobEventReasonId is null or ejp.jobEventReasonId in(2,5))";
				// one dept may be with one or multiple refs				
				if(!selected_dept_ref.equals("")){
						qq += " and ejp.departmentID in ("+selected_dept_ref+") ";// list of all depts
				}
				else{ // all departments at once
						qq += " and ejp.departmentID in ("+deptRefs+") ";// list of all depts
				}
				if(currentOnly){
					qq +=	" and ei.vsEmploymentStatusId = 258 ";
				}
				con = SingleConnect.getNwConnection();
				if(con == null){
						msg = " Could not connect to DB ";
						logger.error(msg);
						return msg;
				}
				try{
						if(debug)
								logger.debug(qq);
						pstmt = con.prepareStatement(qq);
						rs = pstmt.executeQuery();
						Profile prevProfile = null, pp=null;
						String prev_id = "";
						int factorId = 0;
						if(profiles == null){
								profiles = new ArrayList<>();
						}
						int jj=1;
						while(rs.next()){
								String str = rs.getString(4); // nw employeeId 
								String str2 = rs.getString(25); // group id
								String str3 = ""; // rs.getString(3); //full time/part time, exempt/non-exempt
								String str4 = rs.getString(20); // grade info
								String str5 = rs.getString(22); // employeeNumber
								String str6 = rs.getString(38); // standard weekly hours (20,35,40)
								String str0 = rs.getString(39);
								factorId = 0;
								if(str0 != null){
										factorId = rs.getInt(39); // int val
								}
								String str7 = rs.getString(40); // int val
								String str8 = rs.getString(41); // decimal val
								double fstr9 = rs.getDouble(11); // hourly rate
								String str10 = rs.getString(34); // last name
								String str11 = rs.getString(14); // job title
								BenefitGroup bg = null;
								if(bgroups != null
									 && str2 != null
									 && bgroups.containsKey(str2)){
										bg = bgroups.get(str2);
										/*
										if(str2.equals("6")){
												System.err.println(jj+" bg code "+str2+" :"+str11);
												jj++;
										}
										*/
								}
								if(str.equals(prev_id)){
										if(factorId == 36){
												if(str8 != null){
														prevProfile.setException("multiple factor", str8);

												}
										}
										else if(factorId == 37){ // comp time after
												if(str7 != null){
														prevProfile.setException("comp time after", str7);

												}
										}										
								}
								else{
										pp = new Profile(debug,
																		 str, str2, str3,
																		 str4, str5, bg, str6,
																		 fstr9,
																		 str11); //job_name
										// multiple factor
										if(factorId == 36){
												if(str8 != null){
														pp.setException("multiple factor", str8);

												}
										}
										else if(factorId == 37){ // comp time after
												if(str7 != null){
														pp.setException("comp time after", str7);
												}
										}
										prev_id = str;
										if(prevProfile != null){ // for the first time
												// System.err.println(" prof list "+str5+" "+str10+" "+fstr9);
												profiles.add(prevProfile);
										}
										prevProfile = pp;
								}
						}
						// last one issue
						if(pp.getId().equals(prevProfile.getId())){
								profiles.add(prevProfile);
						}
				}
				catch(Exception ex){
						back += ex;
						logger.error(ex+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(pstmt, rs);
				}
				return back;
		}

}






















































