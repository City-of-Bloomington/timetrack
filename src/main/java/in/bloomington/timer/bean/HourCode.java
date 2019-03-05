package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.Serializable;
import java.util.Set;
import java.util.List;
import java.util.HashSet;
import java.util.Hashtable;
import java.sql.*;
import javax.naming.*;
import javax.naming.directory.*;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HourCode{

		static final long serialVersionUID = 700L;
		static Logger logger = LogManager.getLogger(HourCode.class);
		String id="", name = "", description="", inactive="", type="";

		Accrual accrual = null;
		AccrualWarning accrualWarning = null;
		CodeRef codeRef = null;
    private String 
				record_method="Time", // Time, Hours, Monetary
				accrual_id ="",
		// each salary group can have only one reg_default set to 0
				reg_default=""; // y for default, null for others
		private double default_monetary_amount=0.0;
		private String timeUsed="", timeEarned="", unpaid="";

    public HourCode(){
    }
    public HourCode(String val){
				setId(val);
    }
    public HourCode(String val, String val2){
				setId(val);
				setName(val2);
    }		
    public HourCode(String val,
										String val2,
										String val3,
										String val4,
										String val5,
										boolean val6,
										String val7,
										Double val8,
										boolean val9){
				setVals(val, val2, val3, val4, val5, val6, val7, val8, val9);
    }
    void setVals(String val,
								 String val2,
								 String val3,
								 String val4,
								 String val5,
								 boolean val6,
								 String val7,
								 Double val8,
								 boolean val9								 
								 ){
				setId(val);
				setName(val2);
				setDescription(val3);
				setRecord_method(val4);
				setAccrual_id(val5);
				setReg_default(val6);
				setType(val7);
				setDefaultMonetaryAmount(val8);
				setInactive(val9);
				
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
    public String getType(){
				return type;
    }		
    public String getDescription(){
				return description;
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
		public String getRecord_method(){
				if(record_method.equals("")){
						return "Time";
				}
				return record_method;
    }
		public String getAccrual_id(){
				return accrual_id;
    }
		public boolean getReg_default(){
				return !reg_default.equals("");
    }
		public boolean isRegDefault(){
				return !reg_default.equals("");
    }		
		public boolean isRecordMethodHours(){
				return record_method.equals("Hours");
		}
		public boolean isRecordMethodMonetary(){
				return record_method.equals("Monetary");
		}		
		public boolean isAccrualRelated(){
				return !accrual_id.equals("");
		}
		public double getDefaultMonetaryAmount(){
				return default_monetary_amount;
		}
		public void setDefaultMonetaryAmount(Double val){
				if(val != null)
						default_monetary_amount = val;
		}
		//
		// id-Time, id-Hours
		// needed for js
		public String getId_compound(){
				return id+"_"+getRecord_method();
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
    public void setType(String val){
				if(val != null)
						type = val;
    }		
    public void setDescription(String val){
				if(val != null){
						description = val.trim();
				}
    }		
    public void setInactive(boolean val){
				if(val)
						inactive = "y";
    }				
    public void setRecord_method(String val){
				if(val != null && !val.equals("-1"))
						record_method = val;
    }
    public void setAccrual_id (String val){
				if(val != null && !val.equals("-1"))
						accrual_id = val;
    }
    public void setReg_default(boolean val){
				if(val)
						reg_default = "y";
    }		
		public String getCodeInfo(){
				String ret = name;
				if(!description.equals("")){
						if(!ret.equals(""))
								ret += " : ";
						ret += description;
				}
				return ret;
		}
		public Accrual getAccrual(){
				if(accrual == null && !accrual_id.equals("")){
						Accrual one = new Accrual(accrual_id);
						String back = one.doSelect();
						if(back.equals("")){
								accrual = one;
						}
				}
				return accrual;
		}
		public boolean isRegular(){
				return type.equals("Regular");
		}
		public boolean isUsed(){
				return type.equals("Used");
		}
		public boolean isEarned(){
				return type.equals("Earned");
		}
		public boolean isUnpaid(){
				return type.equals("Unpaid");
		}
		public boolean isOvertime(){
				return type.equals("Overtime");
		}
		public boolean isOnCall(){
				return type.equals("Monetary");
		}
		public boolean isMonetary(){
				return type.equals("Monetary");
		}		
		public boolean isCallOut(){
				return type.equals("Call Out");
		}				
		public boolean isOther(){
				return type.equals("Other");
		}		
		public AccrualWarning getAccrualWarning(){
				if(accrualWarning == null &&
					 !id.equals("") &&
					 !accrual_id.equals("")){
						AccrualWarningList awl = new AccrualWarningList();
						awl.setAccrual_id(accrual_id);
						String back = awl.find();
						if(back.equals("")){
								List<AccrualWarning> ones = awl.getAccrualWarnings();
								if(ones != null && ones.size() > 0){
										accrualWarning = ones.get(0);
								}
						}
				}
				return accrualWarning;
		}
		boolean hasAccrualWarning(){
				getAccrualWarning();
				return accrualWarning != null;
		}
		public boolean equals(Object obj){
				if(obj instanceof HourCode){
						HourCode one =(HourCode)obj;
						return id.equals(one.getId());
				}
				return false;				
		}
		public int hashCode(){
				int seed = 29;
				if(!id.equals("")){
						try{
								seed += Integer.parseInt(id);
						}catch(Exception ex){
						}
				}
				return seed;
		}
		public boolean hasCodeRef(){
				getCodeRef();
				return codeRef != null;
		}
    public String toString(){
				return name;
    }		
		//
		// we need this to get the New World reference hour_codes
		// for export purpose
		//
		public CodeRef getCodeRef(){
				if(codeRef == null && !id.equals("")){
						CodeRefList cdr = new CodeRefList();
						cdr.setCode_id(id);
						cdr.setIgnoreHash();
						String back = cdr.find();
						if(back.equals("")){
								List<CodeRef> ones = cdr.getCodeRefs();
								if(ones != null && ones.size() == 1){
										codeRef = ones.get(0);
								}
						}
				}
				return codeRef;
		}
		
		public String doSelect(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "select id,name,description,record_method,accrual_id,"+
						" reg_default,type,default_monetary_amount,inactive "+
						" from hour_codes where id=? ";
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
								setVals(rs.getString(1),
												rs.getString(2),
												rs.getString(3),
												rs.getString(4),
												rs.getString(5),
												rs.getString(6) != null,
												rs.getString(7),
												rs.getDouble(8),
												rs.getString(9) != null
												);
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
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = " insert into hour_codes values(0,?,?,?,?, ?,?,?,?)";
				if(name.equals("")){
						msg = "Hour code name is required";
						return msg;
				}
				if(record_method.equals("")){
						msg = "Record method is required";
						return msg;
				}
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could not connect to DB ";
						return msg;
				}
				
				try{
						pstmt = con.prepareStatement(qq);
						msg = setParams(pstmt);
						if(msg.equals("")){
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
		String setParams(PreparedStatement pstmt){
				String msg = "";
				int jj=1;
				try{
						pstmt.setString(jj++, name);
						if(description.equals("")){
								pstmt.setNull(jj++, Types.VARCHAR);
						}
						else{
								pstmt.setString(jj++, description);
						}
						pstmt.setString(jj++, record_method);
						if(accrual_id.equals(""))
								pstmt.setNull(jj++, Types.INTEGER);
						else
								pstmt.setString(jj++, accrual_id);						
						if(reg_default.equals(""))
								pstmt.setNull(jj++, Types.CHAR);
						else
								pstmt.setString(jj++, "y");						
						if(type.equals(""))
								pstmt.setNull(jj++, Types.VARCHAR);
						else
								pstmt.setString(jj++, type);
						pstmt.setDouble(jj++, default_monetary_amount);
						if(inactive.equals(""))
								pstmt.setNull(jj++, Types.CHAR);
						else
								pstmt.setString(jj++, "y");

				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg);
				}
				return msg;
		}

		public	String doUpdate(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = " update hour_codes set name=?,description=?,record_method=?,accrual_id=?,reg_default=?,type=?,default_monetary_amount=?,inactive=? where id=?";
				if(name.equals("")){
						msg = "Hour code name is required";
						return msg;
				}
				if(record_method.equals("")){
						msg = "Record method is required";
						return msg;
				}
				con = UnoConnect.getConnection();
				if(con == null){
						msg = "Could not connect to DB ";
						return msg;
				}
				try{
						pstmt = con.prepareStatement(qq);
						msg = setParams(pstmt);
						pstmt.setString(9, id);
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
