package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.Serializable;
import java.util.List;
import java.sql.*;
import javax.sql.*;
import java.text.SimpleDateFormat;
import in.bloomington.timer.list.*;
import in.bloomington.timer.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CodeReasonCondition implements Serializable{

		boolean debug = false;
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		static Logger logger = LogManager.getLogger(CodeReasonCondition.class);
		static final long serialVersionUID = 800L;
    private String id="", reason_id="", hour_code_id="",
				department_id="", inactive="",
				date="", salary_group_id="",
				group_id="";
		
		Type salaryGroup = null;
		HourCode hourCode = null;
		EarnCodeReason earnCodeReason = null;
		Type department = null;
		Group group = null;
    public CodeReasonCondition(
												String val,
												String val2,
												String val3,
												String val4,
												String val5,
												String val6,
												boolean val7
									 ){
				setId(val);
				setReason_id(val2);
				setHour_code_id(val3);
				setSalary_group_id(val4);				
				setDepartment_id(val5);
				setGroup_id(val6);
				setInactive(val7);
    }
    public CodeReasonCondition(
												String val,
												String val2,
												String val3,
												String val4,
												String val5,
												String val6,
												boolean val7,

												String val8,
												String val9,
												String val10,
												boolean val11
									 ){
				setId(val);
				setReason_id(val2);
				setHour_code_id(val3);
				setSalary_group_id(val4);				
				setDepartment_id(val5);
				setGroup_id(val6);
				setInactive(val7);
				//
				earnCodeReason = new EarnCodeReason(debug, reason_id, val8, val9, val10, val11);
    }		
    public CodeReasonCondition(String val){
				setId(val);
    }
    public CodeReasonCondition(){
    }		
    //
    // getters
    //
    public String getHour_code_id(){
				return hour_code_id;
    }
    public String getGroup_id(){
				if(group_id.equals(""))
						return "-1";
				return group_id;
    }		
    public String getDepartment_id(){
				if(department_id.equals(""))
						return "-1";
				return department_id;
    }
    public String getSalary_group_id(){
				if(salary_group_id.equals(""))
						return "-1";
				return salary_group_id;
    }
    public String getReason_id(){
				if(reason_id.equals(""))
						return "-1";
				return reason_id;
    }		
		public boolean getInactive(){
				return !inactive.equals("");
    }
		public boolean isActive(){
				return inactive.equals("");
    }		
		public String getId(){
				return id;
    }
		public Type getDepartment(){
				if(!department_id.equals("") && department == null){
						Type one = new Type(department_id);
						one.setTable_name("departments");
						String back = one.doSelect();
						if(back.equals("")){
								department = one;
						}
						return department;
				}
				if(department == null){
						department = new Type("All","All");
				}
				return department;
		}
		
    //
    // setters
    //
    public void setId (String val){
				if(val != null)
						id = val;
    }
    public void setHour_code_id (String val){
				if(val != null && !val.equals("-1"))
						hour_code_id = val;
    }
    public void setDepartment_id(String val){
				if(val != null && !val.equals("-1"))
						department_id = val;
    }
    public void setReason_id(String val){
				if(val != null && !val.equals("-1"))
						reason_id = val;
    }		
    public void setGroup_id(String val){
				if(val != null && !val.equals("-1"))
						group_id = val;
    }		
    public void setSalary_group_id(String val){
				if(val != null && !val.equals("-1"))
						salary_group_id = val;
    }
		public void setInactive(boolean val){
				if(val)
						inactive = "y";
		}
		public void setEarnCodeReason(EarnCodeReason val){
				if(val != null)
						earnCodeReason = val;
		}
		public String toString(){
				return id;
		}
		public boolean equals(Object o) {
				if (o instanceof CodeReasonCondition) {
						CodeReasonCondition c = (CodeReasonCondition) o;
						if ( this.id.equals(c.getId())) 
								return true;
				}
				return false;
		}
		public int hashCode(){
				int seed = 37;
				if(!id.equals("")){
						try{
								seed += Integer.parseInt(id)*31;
						}catch(Exception ex){
								// we ignore
						}
				}
				return seed;
		}
		public Type getSalaryGroup(){
				if(!salary_group_id.equals("") && salaryGroup == null){
						Type one = new Type(salary_group_id);
						one.setTable_name("salary_groups");
						String back = one.doSelect();
						if(back.equals("")){
								salaryGroup = one;
						}
				}
				return salaryGroup;
		}
		public HourCode getHourCode(){
				if(!hour_code_id.equals("") && hourCode == null){
						HourCode one = new HourCode(hour_code_id);
						String back = one.doSelect();
						if(back.equals("")){
								hourCode = one;
						}
				}
				return hourCode;
		}
		public EarnCodeReason getEarnCodeReason(){
				if(!reason_id.equals("") && earnCodeReason == null){
						EarnCodeReason one = new EarnCodeReason(reason_id);
						String back = one.doSelect();
						if(back.equals("")){
								earnCodeReason = one;
						}
				}
				return earnCodeReason;
		}		
		public Group getGroup(){
				if(!group_id.equals("") && group == null){
						Group one = new Group(group_id);
						String back = one.doSelect();
						if(back.equals("")){
								group = one;
						}
				}
				return group;
		}				
		public String doSelect(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "select g.id,g.reason_id,g.hour_code_id,g.salary_group_id,"+
						" g.department_id,g.group_id,g.inactive, "+
						" r.name,r.description,r.reason_category_id,r.inactive "+
						" from code_reason_conditions g "+
						" join earn_code_reasons r on r.id=g.reason_id "+
						" where g.id =? ";
				logger.debug(qq);
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could not connect to DB ";
						return msg;
				}								
				try{
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, id);
						rs = pstmt.executeQuery();
						if(rs.next()){
								setReason_id(rs.getString(2));								
								setHour_code_id(rs.getString(3));
								setSalary_group_id(rs.getString(4));
								setDepartment_id(rs.getString(5));
								setGroup_id(rs.getString(6));
								setInactive(rs.getString(7) != null);
								earnCodeReason = new EarnCodeReason(debug,
																										reason_id,
																										rs.getString(8),
																										rs.getString(9),
																										rs.getString(10),
																										rs.getString(11) != null);
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

		public String doSave(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "insert into code_reason_conditions values(0,?,?,?,?,?,null) ";
				if(hour_code_id.equals("")){
						msg = " need to pick an hour code ";
						return msg;
				}
				if(reason_id.equals("")){
						msg = " reason code not selected ";
						return msg;
				}
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could not connect to DB ";
						return msg;
				}							
				logger.debug(qq);
				try{
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, reason_id);						
						pstmt.setString(2, hour_code_id);
						if(salary_group_id.equals(""))
								pstmt.setNull(3,Types.INTEGER);
						else
								pstmt.setString(3, salary_group_id);
						if(department_id.equals(""))
								pstmt.setNull(4,Types.INTEGER);
						else
								pstmt.setString(4, department_id);
						if(group_id.equals(""))
								pstmt.setNull(5,Types.INTEGER);
						else
								pstmt.setString(5, group_id);						
						pstmt.executeUpdate();
						Helper.databaseDisconnect(pstmt, rs);
						//
						date = Helper.getToday();
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
						UnoConnect.databaseDisconnect(con);
				}
				return msg;
		}
		//
		// we can update expire date and inactive
		//
		public String doUpdate(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				if(id.equals("")){
						return " id not set ";
				}
				if(hour_code_id.equals("")){
						return " hour code is required";
				}
				if(reason_id.equals("")){
						return " reason code is required";
				}
				String qq = "update code_reason_conditions set reason_id=?,hour_code_id=?,salary_group_id=?,department_id=?,group_id=?,inactive=? where id=? ";
				
				logger.debug(qq);
				con = UnoConnect.getConnection();				
				if(con == null){
						msg = "Could not connect to DB ";
						return msg;
				}			
				try{
						pstmt = con.prepareStatement(qq);
						int jj=1;
						pstmt.setString(jj++, reason_id);						
						pstmt.setString(jj++, hour_code_id);
						if(salary_group_id.equals(""))
								pstmt.setNull(jj++, Types.INTEGER);
						else
								pstmt.setString(jj++, salary_group_id);
						if(department_id.equals(""))
								pstmt.setNull(jj++, Types.INTEGER);
						else
								pstmt.setString(jj++, department_id);
						if(group_id.equals(""))
								pstmt.setNull(jj++, Types.INTEGER);
						else
								pstmt.setString(jj++, group_id);								
						
						if(inactive.equals("")){
								pstmt.setNull(jj++, Types.CHAR);
						}
						else{
								pstmt.setString(jj++,"y");
						}
						pstmt.setString(jj++, id);
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
				if(msg.equals("")){
						doSelect();
				}

				return msg;
		}
		public String doDelete(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				if(id.equals("")){
						return " id not set ";
				}
				String qq = "delete from  code_reason_conditions where id=? ";
				
				logger.debug(qq);
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

}
