package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.sql.*;
import java.util.*;
import java.text.SimpleDateFormat;
import in.bloomington.timer.util.*;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GroupEmployee extends CommonInc {

		static final long serialVersionUID = 2000L;
		static Logger logger = LogManager.getLogger(GroupEmployee.class);
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");		
    String group_id="", employee_id="", effective_date="", expire_date="",
				inactive="", id="";
		// to change group
		//
		String new_group_id="", change_date="";
		Group group = null;
		Employee employee = null;
		//
		public GroupEmployee(){
				super();
		}
		public GroupEmployee(String val,
												 String val2,
												 String val3,
												 String val4,
												 String val5,
												 boolean val6
												 ){
				setVals(val, val2, val3, val4, val5, val6);
    }
    void setVals(String val,
								 String val2,
								 String val3,
								 String val4,
								 String val5,
								 boolean val6
								){
				setId(val);
				setGroup_id(val2);				
				setEmployee_id(val3);
				setEffective_date(val4);
				setExpire_date(val5);
				setInactive(val6);
		}
		public GroupEmployee(String val){
				//
				setId(val);
    }

		public String getId(){
				return id;
		}
		public String getEmployee_id(){
				return employee_id;
		}
		public String getGroup_id(){
				return group_id;
		}
		public String getExpire_date(){
				return expire_date;
		}
		public String getEffective_date(){
				return effective_date;
		}
		public String getChange_date(){
				return change_date;
		}
		public String getNew_group_id(){
				return new_group_id;
		}		
		public boolean isActive(){
				return inactive.equals("");
		}		
		public boolean isInactive(){
				return !inactive.equals("");
		}
		public void setId(String val){
				if(val != null && !val.equals("-1"))
						id = val;
		}		
		public void setGroup_id(String val){
				if(val != null && !val.equals("-1"))
						group_id = val;
		}
		public void setEmployee_id(String val){
				if(val != null && !val.equals("-1"))
						employee_id = val;
		}
		public void setNew_group_id(String val){
				if(val != null && !val.equals("-1"))
						new_group_id = val;
		}		
		public void setEffective_date(String val){
				if(val != null)
						effective_date = val;
		}
		public void setExpire_date(String val){
				if(val != null)
						expire_date = val;
		}
		public void setChange_date(String val){
				if(val != null)
						change_date = val;
		}		
		public void setInactive(boolean val){
				if(val)
						inactive = "y";
		}
		public boolean isCurrent(){
				return expire_date.equals("");
		}
		public Employee getEmployee(){
				if(employee == null && !employee_id.equals("")){
						Employee dd = new Employee(employee_id);
						String back = dd.doSelect();
						if(back.equals("")){
								employee = dd;
						}
				}
				return employee;				
		}
		public Group getGroup(){
				if(group == null && !group_id.equals("")){
						Group dd = new Group(group_id);
						String back = dd.doSelect();
						if(back.equals("")){
								group = dd;
						}
				}
				return group;				
		}
		// needed for changing employee group
		// if the employee has not started data entry
		// using this group, then we can change the group
		// easily without the need to go through two step change
		public boolean hasTimeData(){
				if(!employee_id.equals("") && !id.equals("")){					 
						DocumentList dl = new DocumentList(employee_id);
						String back = dl.find();
						if(back.equals("")){
								List<Document> ones = dl.getDocuments();
								return (ones != null && ones.size() > 0);
						}
				}
				return false;
		}
		// exlude group
		//
    public String doSave(){
				String msg = "";
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String qc = " select count(*) from group_employees where group_id=? and employee_id=? ";
				String qq = " insert into group_employees values(0,?,?,?,?,null) "; 
				if(employee_id.equals("")){
						msg = "employee not set ";
						addError(msg);
						return msg;
				}
				if(group_id.equals("")){
						msg = "group not set ";
						addError(msg);
						return msg;
				}
				if(debug){
						logger.debug(qq);
				}
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could not connect to DB";
						addError(msg);
						return msg;
				}			
				try{
						pstmt = con.prepareStatement(qc);
						pstmt.setString(1, group_id);
						pstmt.setString(2, employee_id);
						rs = pstmt.executeQuery();
						int cnt = 0;
						if(rs.next()){
								cnt = rs.getInt(1);
						}
						Helper.databaseDisconnect(pstmt, rs);
						//
						if(cnt == 0){ // avoid dups
								pstmt = con.prepareStatement(qq);
								pstmt.setString(1, group_id);
								pstmt.setString(2, employee_id);								
								if(effective_date.equals(""))
										effective_date = Helper.getToday();
								java.util.Date date_tmp = df.parse(effective_date);
								pstmt.setDate(3, new java.sql.Date(date_tmp.getTime()));
								if(expire_date.equals(""))
										pstmt.setNull(4, Types.DATE);
								else{
										date_tmp = df.parse(expire_date);
										pstmt.setDate(4, new java.sql.Date(date_tmp.getTime()));
								}
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
				}catch(Exception ex){
						logger.error(ex+" : "+qq);
						msg += " "+ex;
						addError(msg);
				}
				finally{
						Helper.databaseDisconnect(pstmt, rs);
						UnoConnect.databaseDisconnect(con);
				}
				return msg;
    }
		//
    public String doUpdate(){
				String msg = "";
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String qq = "update group_employees set group_id=?,employee_id=?,effective_date=?,expire_date=?,inactive=? where id=? ";				
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could not connect to DB";
						addError(msg);
						return msg;
				}			
				try{
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, group_id);														
						pstmt.setString(2, employee_id);
						java.util.Date date_tmp = df.parse(effective_date);
						pstmt.setDate(3, new java.sql.Date(date_tmp.getTime()));
						if(expire_date.equals(""))
								pstmt.setNull(4, Types.DATE);
						else{
								date_tmp = df.parse(expire_date);
								pstmt.setDate(4, new java.sql.Date(date_tmp.getTime()));
						}
						if(inactive.equals(""))
								pstmt.setNull(5, Types.CHAR);
						else{
								pstmt.setString(5,"y");
						}						
						pstmt.setString(6, id);
						pstmt.executeUpdate();
				}catch(Exception ex){
						logger.error(ex+" : "+qq);
						msg += " "+ex;
						addError(msg);
				}
				finally{
						Helper.databaseDisconnect(pstmt, rs);
						UnoConnect.databaseDisconnect(con);
				}
				return msg;
    }
    public String doChange(){
				String msg = "";
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String qq = "update group_employees set expire_date=?,inactive='y' where id=? ";				
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could not connect to DB";
						addError(msg);
						return msg;
				}			
				try{
						pstmt = con.prepareStatement(qq);
						java.util.Date date_tmp = df.parse(change_date);
						pstmt.setDate(1, new java.sql.Date(date_tmp.getTime()));
						pstmt.setString(2, id);
						pstmt.executeUpdate();
				}catch(Exception ex){
						logger.error(ex+" : "+qq);
						msg += " "+ex;
						addError(msg);
				}
				finally{
						Helper.databaseDisconnect(pstmt, rs);
						UnoConnect.databaseDisconnect(con);
				}
				if(msg.equals("")){
						id=""; effective_date=change_date;
						group_id = new_group_id;
						msg = doSave();
				}
				return msg;
    }		
		public String doSelect(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "select id,group_id,employee_id,"+
						" date_format(effective_date,'%m/%d/%Y'),"+
						" date_format(expire_date,'%m/%d/%Y'),inactive "+
						" from group_employees where id=?";
				if(id.equals("")){
						msg = " no id set ";
						return msg;
				}
				logger.debug(qq);
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could not connect to DB";
						addError(msg);
						return msg;
				}							
				try{
						pstmt = con.prepareStatement(qq);
						if(!id.equals("")){
								pstmt.setString(1, id);
						}
						rs = pstmt.executeQuery();
						//
						if(rs.next()){
								setVals(rs.getString(1),
												rs.getString(2),
												rs.getString(3),
												rs.getString(4),
												rs.getString(5),
												rs.getString(6) != null);
						}
						else{
								msg = "Department Employee not found";
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
		
}
