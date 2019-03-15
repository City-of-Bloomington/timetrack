package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.Hashtable;
import java.sql.*;
import javax.sql.*;
import java.text.SimpleDateFormat;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TmwrpBlock{

    static Logger logger = LogManager.getLogger(TmwrpBlock.class);
    static final long serialVersionUID = 1500L;
		static SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");		
    String id="", run_id="", hour_code_id="",
				apply_type=""; // Week 1, Week 2, Cycle
		int start_id = 1;
    double hours=0, amount=0;
    //
		HourCode hourCode = null;
		
    public TmwrpBlock(){
    }		
    public TmwrpBlock(String val){
				setId(val);
    }
		// for new record - no amount
    public TmwrpBlock(int val,
											String val2,
											String val3,
											String val4,
											Double val5
											){
				setId(val);
				setRun_id(val2);
				setHour_code_id(val3);
				setApplyType(val4);
				setHours(val5);
    }		
		// for new record with amount
    public TmwrpBlock(int val,
											String val2,
											String val3,
											String val4,
											Double val5,
											Double val6
											){
				setId(val);
				setRun_id(val2);
				setApplyType(val3);				
				setHour_code_id(val4);
				setHours(val5);
				setAmount(val6);
    }
		
    public TmwrpBlock(String val,
											String val2,
											String val3,
											String val4,
											Double val5,
											Double val6
											){
				setId(val);
				setRun_id(val2);
				setApplyType(val3);				
				setHour_code_id(val4);
				setHours(val5);
				setAmount(val6);
    }
		// needed for the list
    public TmwrpBlock(String val,
											String val2,
											String val3,
											String val4,
											Double val5,
											Double val6,
											
											String val7, // hourCode
											String val8,
											String val9,
											String val10,
											String val11,
											boolean val12,
											String val13,
											Double val14,
											boolean val15,
										
											String val16, // nw_code
											String val17){
				setId(val);
				setRun_id(val2);
				setApplyType(val3);				
				setHour_code_id(val4);
				setHours(val5);
				setAmount(val6);
				hourCode = new HourCode(val7,
																val8,
																val9,
																val10,
																val11,
																val12,
																val13,
																val14,
																val15,
																
																val16, // nw_code
																val17);
		}
		
    //
    // getters
    //
    public String getId(){
				return id;
    }
    public String getRun_id(){
				return run_id;
    }		
    public String getHour_code_id(){
				return hour_code_id;
    }

		public double getHours(){
				return hours;
		}
		public double getAmount(){
				return amount;
		}		
		public String getApplyType(){
				return apply_type;
		}
    //
    // setters
    //
    public void setId(String val){
				if(val != null)
						id = val;
    }
    public void setId(int val){
				if(val >  0)
						id = ""+val;
    }		
		
    public void setRun_id(String val){
				if(val != null)
						run_id = val;
    }		
		
    public void setHour_code_id(String val){
				if(val != null){
						hour_code_id = val;
				}
    }
		
    public void setHours(Double val){
				if(val != null)
						hours = val;
    }
    public void setAmount(Double val){
				if(val != null)
						amount = val;
    }		
		
    public void setApplyType(String val){
				if(val != null)
					 apply_type = val;
    }		
		

    public boolean equals(Object o) {
				if (o instanceof TmwrpBlock) {
						TmwrpBlock c = (TmwrpBlock) o;
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
    public String toString(){
				return id;
    }
		
    public HourCode getHourCode(){
				if(hourCode == null && !hour_code_id.equals("")){
						HourCode one = new HourCode(hour_code_id);
						String back = one.doSelect();
						if(back.equals("")){
								hourCode = one;
						}
				}
				return hourCode;
    }
		// ToDo start here
		
    public String doSelect(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "select g.apply_type,"+
						"g.hour_code_id,g.hours,g.amount "+
						" from tmwrp_blocks g where g.id =? and run_id=? ";
				logger.debug(qq);
				con = UnoConnect.getConnection();				
				try{
						if(con != null){
								pstmt = con.prepareStatement(qq);
								pstmt.setString(1, id);
								pstmt.setString(2, run_id);								
								rs = pstmt.executeQuery();
								if(rs.next()){
										setApplyType(rs.getString(1));
										setHour_code_id(rs.getString(2));
										setHours(rs.getDouble(3));
										setAmount(rs.getDouble(4));
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

    public String doSave(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "insert into tmwrp_blocks values(?,?,?,?,?, ?) ";
				if(run_id.equals("")){
						msg = " timewarp run not set ";
						return msg;
				}
				if(id.equals("")){
						msg = " timewarp id not set ";
						return msg;
				}
								
				if(hour_code_id.equals("")){
						msg = " hour code id not set ";
						return msg;
				}
				logger.debug(qq);
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could not connect to DB ";
						return msg;
				}				
				try{
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, id);
						pstmt.setString(2, run_id);						
						pstmt.setString(3, hour_code_id);
						pstmt.setString(4, apply_type);
						pstmt.setDouble(5, hours);
						pstmt.setDouble(6, amount);
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
    public String doSaveBolk(Hashtable<String, Double> hash,
														 String apply_type,
														 String code_type){ // Hours/Amount
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "insert into tmwrp_blocks values(?,?,?,?,?, 0) ";
				if(code_type.equals("Amount")){
						qq = "insert into tmwrp_blocks values(?,?,?,?,0, ?) ";
				}
				else{

				}
				if(run_id.equals("")){
						msg = " timewarp run not set ";
						return msg;
				}
				if(hash == null || hash.isEmpty()){
						return msg;
				}
				logger.debug(qq);
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could not connect to DB ";
						return msg;
				}
				Set<String> keys = hash.keySet();
				try{
						pstmt = con.prepareStatement(qq);
						for(String key:keys){
								double dd = hash.get(key);						
								pstmt.setInt(1, start_id++);
								pstmt.setString(2, run_id);						
								pstmt.setString(3, key);
								pstmt.setString(4, apply_type);
								pstmt.setDouble(5, dd); // hours/amount
								pstmt.executeUpdate();
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
		// most likely it is not needed
		//
    public String doDelete(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "delete from tmwrp_blocks where id=? and run_id=? ";
				logger.debug(qq);
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could not connect to DB ";
						return msg;
				}							
				try{
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, id);
						pstmt.setString(2, run_id);						
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
