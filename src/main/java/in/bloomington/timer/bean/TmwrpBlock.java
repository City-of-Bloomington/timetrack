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
    String id="", run_id="", document_id="", hour_code_id="", apply_date="",
				addition_type=""; // Daily, Weekly, Cycle
    double hours=0, amount=0;
    //
		HourCode hourCode = null;
		Document document = null;
		
    public TmwrpBlock(){
    }		
    public TmwrpBlock(String val){
				setId(val);
    }
    public TmwrpBlock(String val,
											String val2,
											String val3,
											String val4,
											Double val5,
											Double val6,
											String val7,
											String val8
											){
				setId(val);
				setRun_id(val2);
				setDocument_id(val3);
				setHour_code_id(val4);
				setHours(val5);
				setAmount(val6);
				setApplyDate(val7);
				setAdditionType(val8);
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
		public String getDocument_id(){
				return document_id;
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
		public String getApplyDate(){
				return apply_date;
		}
		public String getAdditionType(){
				return addition_type;
		}
    //
    // setters
    //
    public void setId(String val){
				if(val != null)
						id = val;
    }
    public void setRun_id(String val){
				if(val != null)
						run_id = val;
    }		
		
    public void setDocument_id(String val){
				if(val != null)
						document_id = val;
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
		
    public void setApplyDate(String val){
				if(val != null)
					 apply_date = val;
    }
    public void setAdditionType(String val){
				if(val != null)
					 addition_type = val;
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
				String qq = "select g.id,g.run_id,g.document_id,"+
						"g.hour_code_id,g.hours,g.amount,"+
						"date_format(g.apply_date,'%m/%d/%Y'),g.addition_type "+
						" from tmwrp_blocks g where g.id =? ";
				logger.debug(qq);
				con = UnoConnect.getConnection();				
				try{
						if(con != null){
								pstmt = con.prepareStatement(qq);
								pstmt.setString(1, id);
								rs = pstmt.executeQuery();
								if(rs.next()){
										setRun_id(rs.getString(2));
										setDocument_id(rs.getString(3));
										setHour_code_id(rs.getString(4));
										setHours(rs.getDouble(5));
										setAmount(rs.getDouble(6));
										setApplyDate(rs.getString(7));
										setAdditionType(rs.getString(8));
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
				String qq = "insert into tmwrp_blocks values(0,?,?,?,?, ?,?,?) ";
				if(run_id.equals("")){
						msg = " timewarp run not set ";
						return msg;
				}
				if(document_id.equals("")){
						msg = " document id not set ";
						return msg;
				}
				if(hour_code_id.equals("")){
						msg = " hour code id not set ";
						return msg;
				}
				if(apply_date.equals("")){
						msg = "apply date not set ";
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
						pstmt.setString(1, run_id);						
						pstmt.setString(2, document_id);
						pstmt.setString(3, hour_code_id);
						pstmt.setDouble(4, hours);
						pstmt.setDouble(5, amount);
						java.util.Date date_tmp = df.parse(apply_date);
						pstmt.setDate(6, new java.sql.Date(date_tmp.getTime()));						
						if(addition_type.equals("")){
								pstmt.setNull(7, Types.INTEGER);
						}
						else{
								pstmt.setString(7, addition_type);
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
						UnoConnect.databaseDisconnect(con);
				}
				return msg;
    }

    public String doDelete(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "delete from tmwrp_blocks where id=? ";
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
