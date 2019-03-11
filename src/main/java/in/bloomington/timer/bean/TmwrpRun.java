package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.sql.*;
import javax.sql.*;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TmwrpRun{

    static Logger logger = LogManager.getLogger(TmwrpRun.class);
    static final long serialVersionUID = 1500L;
    String id="", document_id="", run_time="", reg_code_id="";
		double week1_grs_reg_hrs = 0,
				week2_grs_reg_hrs=0,
				week1_net_reg_hrs=0,
				week2_net_reg_hrs=0;
		
    //
    Document document = null;
		HourCode regCode = null;

		List<TmwrpBlock> blocks = null;

		
    public TmwrpRun(){
    }		
    public TmwrpRun(String val){
				setId(val);
    }
		// new record or discovery
    public TmwrpRun(String val,
										String val2,
										Double val3,
										Double val4,
										Double val5,
										Double val6){
				setDocument_id(val);
				setReg_code_id(val2);
				setWeek1GrsRegHrs(val3);
				setWeek2GrsRegHrs(val4);
				setWeek1GrsRegHrs(val5);
				setWeek2GrsRegHrs(val6);				
    }		
    public TmwrpRun(String val,
										String val2,
										String val3,
										String val4,
										Double val5,
										Double val6,
										Double val7,
										Double val8){
				setId(val);
				setDocument_id(val2);
				setRunTime(val3);
				setReg_code_id(val4);
				setWeek1GrsRegHrs(val5);
				setWeek2GrsRegHrs(val6);
				setWeek1GrsRegHrs(val7);
				setWeek2GrsRegHrs(val8);				
    }
    //
    // getters
    //
    public String getId(){
				return id;
    }
		public String getDocument_id(){
				return document_id;
		}
    public String getReg_code_id(){
				return reg_code_id;
    }

		public String getRunTime(){
				return run_time;
		}
		public double getWeek1GrsRegHrs(){
				return week1_grs_reg_hrs;
		}
		public double getWeek2GrsRegHrs(){
				return week2_grs_reg_hrs;
		}
		public double getWeek1NetRegHrs(){
				return week1_net_reg_hrs;
		}
		public double getWeek2NetRegHrs(){
				return week2_net_reg_hrs;
		}		
    //
    // setters
    //
    public void setId(String val){
				if(val != null)
						id = val;
    }
    public void setDocument_id(String val){
				if(val != null)
						document_id = val;
    }
    public void setReg_code_id(String val){
				if(val != null)
						reg_code_id = val;
    }		
    public void setRunTime(String val){
				if(val != null){
						run_time = val;
				}
    }		
    public void setWeek1GrsRegHrs(Double val){
				if(val != null)
						week1_grs_reg_hrs = val;
    }
    public void setWeek2GrsRegHrs(Double val){
				if(val != null)
						week2_grs_reg_hrs = val;
    }				
    public void setWeek1NetRegHrs(Double val){
				if(val != null)
						week1_net_reg_hrs = val;
    }
    public void setWeek2NetRegHrs(Double val){
				if(val != null)
						week2_net_reg_hrs = val;
    }		

    public boolean equals(Object o) {
				if (o instanceof TmwrpRun) {
						TmwrpRun c = (TmwrpRun) o;
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
		
    public Document getDocument(){
				if(document == null && !document_id.equals("")){
						Document one = new Document(document_id);
						String back = one.doSelect();
						if(back.equals("")){
								document = one;
						}
				}
				return document;
    }
    public HourCode getRegCode(){
				if(regCode == null && !reg_code_id.equals("")){
						HourCode one = new HourCode(reg_code_id);
						String back = one.doSelect();
						if(back.equals("")){
								regCode = one;
						}
				}
				return regCode;
    }
		public List<TmwrpBlock> getBlocks(){
				if(blocks == null && !id.equals("")){
						TmwrpBlockList tbl = new TmwrpBlockList(id);
						String back = tbl.find();
						if(back.equals("")){
								List<TmwrpBlock> ones = tbl.getBlocks();
								if(ones != null && ones.size() > 0){
										blocks = ones;
								}
						}

				}
				return blocks;
		}
		public boolean hasBlocks(){
				getBlocks();
				return blocks != null && blocks.size() > 0;
		}
		
    public String doSelect(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "select g.id,g.reg_code_id,g.document_id,g.reg_code_id,"+
						"date_format(g.run_time,'%m/%d/%y %H:%i'),"+
						"g.week1_grs_reg_hrs, "+
						"g.week2_grs_reg_hrs, "+
						"g.week1_net_reg_hrs, "+
						"g.week2_net_reg_hrs "+						
						"from tmwrp_runs g where g.id =? ";
				logger.debug(qq);
				con = UnoConnect.getConnection();				
				try{
						if(con != null){
								pstmt = con.prepareStatement(qq);
								pstmt.setString(1, id);
								rs = pstmt.executeQuery();
								if(rs.next()){
										setDocument_id(rs.getString(2));
										setReg_code_id(rs.getString(3));
										setRunTime(rs.getString(4));
										setWeek1GrsRegHrs(rs.getDouble(5));
										setWeek2GrsRegHrs(rs.getDouble(6));
										setWeek1NetRegHrs(rs.getDouble(7));
										setWeek2NetRegHrs(rs.getDouble(8));
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
		public String doSaveOrUpdate(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "select id from tmwrp_runs where document_id=? ";
				if(document_id.equals("")){
						msg = " document not set ";
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
						pstmt.setString(1, document_id);						
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
				if(msg.equals("")){
						if(id.equals("")){
								return doSave();
						}
						else{
								return doUpdate();
						}
				}
				return msg;
		}
    public String doSave(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "select id from tmwrp_runs where document_id=? ";
				String qq2 = "insert into tmwrp_runs values(0,?,?,now(),?,?,?,?) ";
				if(document_id.equals("")){
						msg = " document not set ";
						return msg;
				}
				if(reg_code_id.equals("")){
						msg = " regular hour code not set ";
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
						pstmt.setString(1, document_id);						
						pstmt.setString(2, reg_code_id);
						pstmt.setDouble(3,week1_grs_reg_hrs);
						pstmt.setDouble(4,week2_grs_reg_hrs);
						pstmt.setDouble(5,week1_net_reg_hrs);
						pstmt.setDouble(6,week2_net_reg_hrs);						
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
						UnoConnect.databaseDisconnect(con);
				}
				return msg;
    }
    public String doUpdate(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "update tmwrp_runs set "+
						"reg_code_id=?,"+
						"week1_grs_reg_hrs=?,"+
						"week2_grs_reg_hrs=?,"+
						"week1_net_reg_hrs=?,"+
						"week1_net_reg_hrs=? "+
						"where id=? ";
				if(reg_code_id.equals("")){
						msg = " regular hour code not set ";
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
						pstmt.setString(1, reg_code_id);
						pstmt.setDouble(2,week1_grs_reg_hrs);
						pstmt.setDouble(2,week2_grs_reg_hrs);
						pstmt.setDouble(3,week1_net_reg_hrs);
						pstmt.setDouble(4,week2_net_reg_hrs);
						pstmt.setString(5, id);						
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
		/**
		 * when delete, we also delete related blocks
		 */
    public String doDelete(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "delete tmwrp_blocks where run_id=? ";
				String qq2 = "delete tmwrp_runs where id=? ";				
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
						Helper.databaseDisconnect(pstmt, rs);
						qq = qq2;
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
