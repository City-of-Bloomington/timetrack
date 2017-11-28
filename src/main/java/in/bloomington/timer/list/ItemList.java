package in.bloomington.timer.list;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.text.*;
import java.util.Date;
import java.sql.*;
import javax.naming.*;
import javax.naming.directory.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;


public class ItemList extends ArrayList<Item>{

    private String user_id="", start_date = "", end_date="";
		float comp_time_multiple = 1.0f;
		float comp_time_after = 40.0f;
		boolean debug = false;
		static final long serialVersionUID = 22L;
		SimpleDateFormat dateFormat = Helper.dateFormat;
		static Logger logger = LogManager.getLogger(ItemList.class);

    public ItemList(boolean deb){
				debug = deb;
    }
    public ItemList(boolean deb, String val){
				debug = deb;
				setUser_id(val);
    }
    //
    // getters
    //
    public void setUser_id (String val){
				if(val != null)
						user_id = val;
    }
    public void setStartDate (String val){
				if(val != null)
						start_date = val;
    }
    public void setEndDate (String val){
				if(val != null)
						end_date = val;
    }
		public float getCompTimeAfter(){
				return comp_time_after;
		}
		public float getCompTimeMultiple(){
				return comp_time_multiple;
		}
		public String toString(){
				return user_id+":"+start_date+"-"+end_date;
		}
		public String find(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String	qq = "select tt.c2 work_category, "+
						"sum(tt.c3) total_hours " +
						"from " +
						"(select tc.name c2, " +
						"((ti.out_hour + "+
						"(ti.out_minute / 60)) - "+
						"(ti.in_hour + (ti.in_minute / 60))) c3 " +
						" from timeinterval ti,categories tc " +
						" where ti.user_id = ? and " +
						" ti.category_id=tc.id and "+
						" ti.dt >= ? " +
						" and ti.dt <= ? " +
						") tt " +
						"group by work_category order by work_category";
				con = Helper.getConnection();
				if(con == null){
						msg = " Could not connect to DB ";
						logger.error(msg);
						return msg;
				}
				if(debug)
						logger.debug(qq);
				try{
						pstmt = con.prepareStatement(qq);
						// per user
						pstmt.setString(1, user_id);
						int jj = 2;
						pstmt.setDate(jj++, new java.sql.Date(dateFormat.parse(start_date).getTime()));
						pstmt.setDate(jj++, new java.sql.Date(dateFormat.parse(end_date).getTime()));
						rs = pstmt.executeQuery();
						while(rs.next()){
								Item item = new Item(debug,
																		 rs.getString(1),
																		 rs.getFloat(2));
								add(item);
						}
						qq = "select name, value from profiles "+
								" where user_id = ? ";
						logger.debug(qq);
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, user_id);
						rs = pstmt.executeQuery();
						while (rs.next()) {
								str = rs.getString(1);
								if(str != null){
										if(str.indexOf("multiple") > -1){
												comp_time_multiple = rs.getFloat(2);
										}
										else if(str.indexOf("after") > -1){
												comp_time_after = rs.getFloat(2);
										}
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

}
