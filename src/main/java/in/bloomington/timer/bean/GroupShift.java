package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.text.SimpleDateFormat;
import java.sql.*;
import javax.sql.*;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GroupShift{

		static Logger logger = LogManager.getLogger(GroupShift.class);
		static final long serialVersionUID = 1500L;
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");				
		String id="", group_id="", shift_id="", inactive="";
		String start_date="", expire_date="";
		Group group = null;
		Shift shift = null;
    public GroupShift(){

    }		
    public GroupShift(String val){
				setId(val);
    }
		
    public GroupShift(String val,
											String val2,
											String val3,
											String val4,
											String val5,
											boolean val6
								 ){
				setId(val);
				setGroup_id(val2);
				setShift_id(val3);
				setStartDate(val4);
				setExpireDate(val5);
				setInactive(val6);

    }
    public GroupShift(String val,
											String val2,
											String val3,
											String val4,
											String val5,
											boolean val6,

											String val01, // group
											String val02,
											String val03,
											String val04,
											String val05,
											boolean val06,
											String val07,

											String val11,
											String val12,
											int val13,
											int val14,
											int val15,
											int val16,
											int val17,
											boolean val18
											
								 ){
				setId(val);
				setGroup_id(val2);
				setShift_id(val3);
				setStartDate(val4);
				setExpireDate(val5);
				setInactive(val6);
				group = new Group(val01, val02, val03, val04, val05,val06, val07);
				shift = new Shift(val11, val12, val13, val14, val15, val16,
													val17, val18);

    }		
		
		//
    // getters
    //
		public boolean equals(Object o) {
				if (o instanceof GroupShift) {
						GroupShift c = (GroupShift) o;
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
    public String getId(){
				return id;
    }
    public String getGroup_id(){
				return group_id;
    }
    public String getShift_id(){
				return shift_id;
    }
		public String getStartDate(){
				return start_date;
		}
		public String getExpireDate(){
				return expire_date;
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
    public void setGroup_id(String val){
				if(val != null && !val.equals("-1"))
						group_id = val;
    }
    public void setShift_id(String val){
				if(val != null && !val.equals("-1"))
						shift_id = val;
    }		
    public void setStartDate(String val){
				if(val != null)
						start_date = val;
    }
    public void setExpireDate(String val){
				if(val != null)
						expire_date = val;
    }		

    public void setInactive(boolean val){
				if(val)
						inactive = "y";
    }		
    public String toString(){
				return id;
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
		public Shift getShift(){
				if(shift == null && !shift_id.equals("")){
						Shift dd = new Shift(shift_id);
						String back = dd.doSelect();
						if(back.equals("")){
								shift = dd;
						}
				}
				return shift;				
		}		
		public String doSelect(){
				String back = "";
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String qq = "select id,group_id,shift_id,date_format(start_date,'%m/%d/%Y'),date_format(expire_date,'%m/%d/%Y'),"+
						" inactive "+
						" from group_shifts where id=?";
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
								setGroup_id(rs.getString(2));
								setShift_id(rs.getString(3));
								setStartDate(rs.getString(4));
								setExpireDate(rs.getString(5));
								setInactive(rs.getString(6) != null);
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
				}
				return back;
		}
		public String doSave(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				inactive=""; // default
				String qq = " select count(*) from group_shifts where group_id=? and inactive is null and expire_date is null";
				String qq2 = " insert into group_shifts values(0,?,?,?,?,null)";
				if(group_id.equals("")){
						msg = "Group is required";
						return msg;
				}
				if(shift_id.equals("")){
						msg = "Shift is required";
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
						pstmt.setString(1, group_id);
						rs = pstmt.executeQuery();
						if(rs.next()){
								cnt = rs.getInt(1);
						}
						Helper.databaseDisconnect(pstmt, rs);
						if(cnt > 0){
								msg = "There is an already shift assigned to shis group";
								return msg;
						}
						qq = qq2;
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, group_id);
						pstmt.setString(2, shift_id);						
						if(start_date.equals("")){
								start_date = Helper.getToday();
						}
						java.util.Date date_tmp = df.parse(start_date);
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
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(pstmt, rs);
				}
				return msg;
		}
		public String doUpdate(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = " update group_shifts set group_id=?, shift_id=?,"+
						"start_date=?,expire_date=?,"+
						"inactive=? where id=?";
				if(group_id.equals("")){
						msg = "Group is required";
						return msg;
				}
				if(shift_id.equals("")){
						msg = "Shift is required";
						return msg;
				}				
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could not connect to DB ";
						return msg;
				}				
				try{
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, group_id);
						pstmt.setString(2, shift_id);
						if(start_date.equals("")){
								start_date = Helper.getToday();
						}
						java.util.Date date_tmp = df.parse(start_date);
						pstmt.setDate(3, new java.sql.Date(date_tmp.getTime()));
						if(expire_date.equals(""))
								pstmt.setNull(4, Types.DATE);
						else{
								date_tmp = df.parse(expire_date);
								pstmt.setDate(4, new java.sql.Date(date_tmp.getTime()));
						}						

						if(inactive.equals("")){
								pstmt.setNull(5, Types.CHAR);
						}
						else
								pstmt.setString(5, "y");								
						pstmt.setString(6, id);
						pstmt.executeUpdate();
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(pstmt, rs);
				}
				return msg;
		}

}
