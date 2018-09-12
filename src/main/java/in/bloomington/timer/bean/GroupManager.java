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
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GroupManager implements Serializable{

		static Logger logger = LogManager.getLogger(GroupManager.class);
		static final long serialVersionUID = 1700L;
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");		
    private String id="", group_id="", employee_id="", wf_node_id="",
				employee_id2 = "", wf_node_id2="", // for second assignment
				inactive="",
				start_date="", expire_date="";
		List<Employee> employees = null;
		Group group = null;
		Employee employee = null;
		Node node = null; // workflow node
    public GroupManager(
												String val,
												String val2,
												String val3,
												String val4,
												String val5,
												String val6,
												boolean val7,
												String val8
									 ){
				setId(val);
				setGroup_id(val2);
				setEmployee_id(val3);
				setWf_node_id(val4);
				setStart_date(val5);
				setExpire_date(val6);
				setInactive(val7);
				if(val8 != null){
						node = new Node(wf_node_id, val8);
				}
    }
    public GroupManager(String val){
				setId(val);
    }
    public GroupManager(){
    }		
    //
    // getters
    //
    public String getGroup_id(){
				return group_id;
    }
    public String getEmployee_id(){
				if(employee_id.equals(""))
						return "-1";
				return employee_id;
    }
    public String getWf_node_id(){
				if(wf_node_id.equals(""))
						return "-1";
				return wf_node_id;
    }
    public String getEmployee_id2(){
				if(employee_id2.equals(""))
						return "-1";
				return employee_id2;
    }
    public String getWf_node_id2(){
				if(wf_node_id2.equals(""))
						return "-1";						
				return wf_node_id2;
    }		
    public String getStart_date(){
				if(id.equals(""))
						return CommonInc.default_effective_date;
				return start_date;
    }
    public String getExpire_date(){
				return expire_date;
    }		
		public boolean getInactive(){
				return !inactive.equals("");
    }
		public String getId(){
				return id;
    }
    public boolean isActive(){
				return inactive.equals("");
    }
		public boolean canEnterData(){
				getNode();
				if(node != null){
						return node.getName().startsWith("Data");
				}
				return false;
		}
		public boolean canApprove(){
				getNode();
				if(node != null){
						return node.getName().equals("Approve");
				}
				return false;
		}
		public boolean canPayrollProcess(){
				getNode();
				if(node != null){
						return node.getName().startsWith("Payroll");
				}
				return false;				
		}
		public boolean canReview(){
				getNode();
				if(node != null){
						return node.getName().equals("Review");
				}
				return false;
		}		
		public Node getNode(){
				if(!wf_node_id.equals("") && node == null){
						Node one = new Node(wf_node_id);
						String back = one.doSelect();
						if(back.equals("")){
								node = one;
						}
				}
				return node;
		}
    //
    // setters
    //
    public void setId (String val){
				if(val != null)
						id = val;
    }
    public void setInactive (boolean val){
				if(val)
						inactive="y";
    }
    public void setWf_node_id(String val){
				if(val != null && !val.equals("-1"))
						wf_node_id = val;
    }
    public void setWf_node_id2(String val){
				if(val != null && !val.equals("-1"))
						wf_node_id2 = val;
    }		
    public void setGroup_id(String val){
				if(val != null)
						group_id = val;
    }
    public void setEmployee_id(String val){
				if(val != null)
						employee_id = val;
    }
    public void setEmployee_id2(String val){
				if(val != null)
						employee_id2 = val;
    }
    public void setStart_date(String val){
				if(val != null)
						start_date = val;
    }
    public void setExpire_date(String val){
				if(val != null)
						expire_date = val;
    }		
		public String toString(){
				return id;
		}
		public boolean equals(Object o) {
				if (o instanceof GroupManager) {
						GroupManager c = (GroupManager) o;
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
		public List<Employee> getEmployees(){
				if(!id.equals("")){
						EmployeeList ul = new EmployeeList();
						ul.setGroup_id(group_id); // group id
						String back = ul.find();
						if(back.equals("")){
								List<Employee> el = ul.getEmployees();
								if(el != null && el.size() > 0){
										employees = el;
								}
						}
				}
				return employees;
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
		public Employee getEmployee(){
				if(!employee_id.equals("") && employee == null){
						Employee one = new Employee(employee_id);
						String back = one.doSelect();
						if(back.equals("")){
								employee = one;
						}
				}
				return employee;
		}		
		public String doSelect(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "select gm.id,gm.group_id,gm.employee_id,gm.wf_node_id,date_format(gm.start_date,'%m/%d/%Y'),date_format(gm.expire_date,'%m/%d/%Y'),gm.inactive,wn.name from group_managers gm join workflow_nodes wn on wn.id=gm.wf_node_id where gm.id =? ";
				logger.debug(qq);
				try{
						con = Helper.getConnection();
						if(con != null){
								pstmt = con.prepareStatement(qq);
								pstmt.setString(1, id);
								rs = pstmt.executeQuery();
								if(rs.next()){
										setGroup_id(rs.getString(2));
										setEmployee_id(rs.getString(3));
										setWf_node_id(rs.getString(4));
										setStart_date(rs.getString(5));
										setExpire_date(rs.getString(6));
										setInactive(rs.getString(7) != null);
										node = new Node(wf_node_id, rs.getString(8));
								}
						}
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(con, pstmt, rs);
				}
				return msg;
		}

		public String doSave(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "insert into group_managers values(0,?,?,?,?,null,null) ";
				if(employee_id.equals("") || group_id.equals("") || wf_node_id.equals("")){
						msg = " group, employee or node_id not set ";
						return msg;
				}
				logger.debug(qq);
				try{
						con = Helper.getConnection();
						if(con != null){
								pstmt = con.prepareStatement(qq);
								pstmt.setString(1, group_id);
								pstmt.setString(2, employee_id);
								pstmt.setString(3, wf_node_id);
								if(start_date.equals("")){
										start_date = Helper.getToday();
								}
								java.util.Date date_tmp = df.parse(start_date);
								pstmt.setDate(4, new java.sql.Date(date_tmp.getTime()));
								pstmt.executeUpdate();
								if(!employee_id2.equals("") && !wf_node_id2.equals("")){
										pstmt.setString(1, group_id);
										pstmt.setString(2, employee_id2);
										pstmt.setString(3, wf_node_id2);
										pstmt.setDate(4, new java.sql.Date(date_tmp.getTime()));
										pstmt.executeUpdate();
								}
						}
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
						Helper.databaseDisconnect(con, pstmt, rs);
				}
				if(msg.equals("")){
						msg = doSelect();
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
				String qq = "update group_managers set start_date=?,expire_date=?,inactive=? where id=? ";
				logger.debug(qq);
				try{
						con = Helper.getConnection();
						if(con != null){
								pstmt = con.prepareStatement(qq);
								if(start_date.equals("")){
										start_date = Helper.getToday();
								}
								java.util.Date date_tmp = df.parse(start_date);
								pstmt.setDate(1, new java.sql.Date(date_tmp.getTime()));
								if(expire_date.equals("")){
										pstmt.setNull(2, Types.DATE);
								}
								else{
										date_tmp = df.parse(expire_date);
										pstmt.setDate(2, new java.sql.Date(date_tmp.getTime()));
								}
								if(inactive.equals("")){
										pstmt.setNull(3, Types.CHAR);
								}
								else{
										pstmt.setString(3,"y");
								}
								pstmt.setString(4, id);
								pstmt.executeUpdate();
						}
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(con, pstmt, rs);
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
				String qq = "delete group_managers where id=? ";
				logger.debug(qq);
				try{
						con = Helper.getConnection();
						if(con != null){
								pstmt = con.prepareStatement(qq);
								pstmt.setString(1, id);
								pstmt.executeUpdate();
						}
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(con, pstmt, rs);
				}
				return msg;
		}		

}
