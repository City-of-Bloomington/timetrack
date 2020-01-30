package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.sql.*;
import javax.naming.*;
import javax.naming.directory.*;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AccrualContribute{

		static final long serialVersionUID = 3700L;	
		static Logger logger = LogManager.getLogger(AccrualContribute.class);
    String id="", name="";
		String accrual_id="", hour_code_id="";
		double factor = 1.0;
		HourCode hourCode = null;
		Accrual accrual = null;
		//
		public AccrualContribute(){
				super();
		}
		public AccrualContribute(String val){
				//
				setId(val);
    }		
		public AccrualContribute(String val, String val2){
				//
				// initialize
				//
				setId(val);
				setName(val2);
    }
		public AccrualContribute(String val, String val2, String val3, String val4, double val5){
				setId(val);
				setName(val2);
				setAccrual_id(val3);
				setHourCode_id(val4);
				setFactor(val5);
    }
		public AccrualContribute(String val,
														 String val2,
														 String val3,
														 String val4,
														 double val5,

														 String val01,
														 String val02,
														 String val03,
														 int val04,
														 boolean val05){
				
				setVals(val, val2, val3, val4, val5,
								val01,val02,val03,val04,val05);
		}
		void setVals(String val,
								 String val2,
								 String val3,
								 String val4,
								 double val5,
								 
								 String val01,
								 String val02,
								 String val03,
								 int val04,
								 boolean val05){
				setId(val);
				setName(val2);
				setAccrual_id(val3);
				setHourCode_id(val4);
				setFactor(val5);
				accrual = new Accrual(val01,val02,val03,val04,val05);
		}				
		public boolean equals(Object obj){
				if(obj instanceof AccrualContribute){
						AccrualContribute one =(AccrualContribute)obj;
						return id.equals(one.getId());
				}
				return false;				
		}
		public int hashCode(){
				int seed = 29;
				if(!id.isEmpty()){
						try{
								seed += Integer.parseInt(id);
						}catch(Exception ex){
						}
				}
				return seed;
		}
    //
    // getters
    //
    public String getId(){
				return id;
    }
    public String getName(){
				return name;
    }
    public String getAccrual_id(){
				return accrual_id;
    }
    public String getHourCode_id(){
				return hour_code_id;
    }
		public double getFactor(){
				return factor;
		}
		public Accrual getAccrual(){
				if(accrual == null && !accrual_id.isEmpty()){
						Accrual one = new Accrual(accrual_id);
						String back = one.doSelect();
						if(back.isEmpty()){
								accrual = one;
						}
				}
				return accrual;
		}
		public HourCode getHourCode(){
				if(hourCode == null && !hour_code_id.isEmpty()){
						HourCode one = new HourCode(hour_code_id);
						String back = one.doSelect();
						if(back.isEmpty()){
								hourCode = one;
						}
				}
				return hourCode;
		}		
    //
    // setters
    //
    public void setId(String val){
				if(val != null)
						id = val;
    }
    public void setName(String val){
				if(val != null)
						name = val.trim();
    }
    public void setAccrual_id(String val){
				if(val != null)
						accrual_id = val;
    }
    public void setHourCode_id(String val){
				if(val != null)
						hour_code_id = val;
    }
		public void setFactor(Double val){
				if(val != null)
						factor = val;
		}

    public String toString(){
				return name;
    }
		//
		public String doSelect(){
				String back = "";
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String qq = "select c.id,c.name,c.accrual_id,"+
						"c.hour_code_id,c.factor, "+
						"a.id,a.name,a.description,a.pref_max_level,a.inactive "+
						"from accrual_contributes c,accruals a "+
						"where c.accrual_id=a.id and c.id=?";
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
								setVals(id,
												rs.getString(2),
												rs.getString(3),
												rs.getString(4),
												rs.getDouble(5),

												rs.getString(6),
												rs.getString(7),
												rs.getString(8),
												rs.getInt(9),
												rs.getString(10) != null);
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
						UnoConnect.databaseDisconnect(con);
				}
				return back;
		}
		public String doSave(){
				Connection con = null;
				PreparedStatement pstmt = null, pstmt2=null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = " insert into accrual_contributes values(0,?,?,?,?)";
				if(name.isEmpty()){
						msg = "Earn code name is required";
						return msg;
				}
				try{
						con = UnoConnect.getConnection();
						if(con == null){
								msg = "Could not connect to DB ";
								return msg;
						}
						pstmt = con.prepareStatement(qq);
						msg = setParams(pstmt);
						if(msg.isEmpty()){
								pstmt.executeUpdate();
								//
								qq = "select LAST_INSERT_ID()";
								pstmt2 = con.prepareStatement(qq);
								rs = pstmt2.executeQuery();
								if(rs.next()){
										id = rs.getString(1);
								}
						}
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(rs, pstmt, pstmt2);
						UnoConnect.databaseDisconnect(con);						
				}
				return msg;
		}
		String setParams(PreparedStatement pstmt){
				String msg = "";
				int jj=1;
				try{
						pstmt.setString(jj++, name);
						pstmt.setString(jj++, accrual_id);
						pstmt.setString(jj++, hour_code_id);
						pstmt.setDouble(jj++, factor);
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg);
				}
				return msg;
		}
		public String doUpdate(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = " update accrual_contributes set name=?, description=?,accrual_id=?,hour_code_id=?,factor=? where id=?";
				if(name.isEmpty()){
						msg = "Earn code name is required";
						return msg;
				}
				try{
						con = UnoConnect.getConnection();
						if(con == null){
								msg = "Could not connect to DB ";
								return msg;
						}
						pstmt = con.prepareStatement(qq);
						msg = setParams(pstmt);
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

}
