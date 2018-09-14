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

public class HourCode extends Type{

		static final long serialVersionUID = 700L;
		static Logger logger = LogManager.getLogger(HourCode.class);

		Type accrual = null;
		CodeRef codeRef = null;
    private String 
				record_method="Time", // time or hours
				accrual_id ="",
     		// each salary group can have only one reg_default set to 0
				reg_default="", // 0 for default, 1 for others
				count_as_regular_pay=""; // char Yes/No flag

		private String timeUsed="", timeEarned="", unpaid="";

    public HourCode(){
    }
    public HourCode(String val){
				super(val);
    }
    public HourCode(String val, String val2){
				super(val, val2);
    }		
    public HourCode(String val, String val2, String val3, String val4, String val5, boolean val6, boolean val7, String val8){
				super(val, val2, val3, val7);
				setVals(val4, val5, val6, val8);
    }
    void setVals(String val,String val2, boolean val3, String val4){
				setRecord_method(val);
				setAccrual_id(val2);
				setCount_as_regular_pay(val3);
				setReg_default(val4);
    }
    void setVals(String val, String val2, String val3, String val4,String val5, boolean val6, boolean val7, String val8){
				setId(val);
				setName(val2);
				setDescription(val3);
				setVals(val4, val5, val6, val8);				
				setInactive(val7);

    }						
    //
    // getters
    //
		public String getRecord_method(){
				if(record_method.equals("")){
						return "Time";
				}
				return record_method;
    }
		public String getAccrual_id(){
				return accrual_id;
    }
		public String getReg_default(){
				return reg_default;
    }
		public boolean isRecordMethodHours(){
				return record_method.equals("Hours");
		}
		public boolean isAccrualRelated(){
				return !accrual_id.equals("");
		}
		public boolean getCount_as_regular_pay(){
				if(count_as_regular_pay.equals("") && id.equals(""))
						return true;
				return !count_as_regular_pay.equals("");
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
    public void setRecord_method(String val){
				if(val != null && !val.equals("-1"))
						record_method = val;
    }
    public void setAccrual_id (String val){
				if(val != null && !val.equals("-1"))
						accrual_id = val;
    }
    public void setReg_default(String val){
				if(val != null && !val.equals("-1"))
						reg_default = val;
    }		
    public void setCount_as_regular_pay (boolean val){
				if(val)
						count_as_regular_pay = "y";
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
		public Type getAccrual(){
				if(accrual == null && !accrual_id.equals("")){
						Type one = new Type(accrual_id);
						one.setTable_name("accruals");
						String back = one.doSelect();
						if(back.equals("")){
								accrual = one;
						}
				}
				return accrual;
		}
		@Override
		public boolean equals(Object obj){
				if(obj instanceof HourCode){
						HourCode one =(HourCode)obj;
						return id.equals(one.getId());
				}
				return false;				
		}
		@Override
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
		
		@Override
		public String doSelect(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "select id,name,description,record_method,accrual_id,"+
						" count_as_regular_pay,inactive,reg_default from hour_codes where id=? ";
				logger.debug(qq);
				try{
						con = Helper.getConnection();
						if(con != null){
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
														rs.getString(7) != null,
														rs.getString(8));
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
		@Override
		public String doSave(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = " insert into hour_codes values(0,?,?,?,?, ?,?,?)";
				if(name.equals("")){
						msg = "Hour code name is required";
						return msg;
				}
				if(record_method.equals("")){
						msg = "Record method is required";
						return msg;
				}				
				try{
						con = Helper.getConnection();
						if(con == null){
								msg = "Could not connect to DB ";
								return msg;
						}
						pstmt = con.prepareStatement(qq);
						msg = setParams(pstmt);
						if(msg.equals("")){
								pstmt.executeUpdate();
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
						Helper.databaseDisconnect(con, pstmt, rs);
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
								if(description.length() > 1){
										description = description.substring(0,1).toUpperCase()+description.substring(1).toLowerCase();
								}
								pstmt.setString(jj++, description);

						}
						pstmt.setString(jj++, record_method);
						if(accrual_id.equals(""))
								pstmt.setNull(jj++, Types.INTEGER);
						else
								pstmt.setString(jj++, accrual_id);						
						if(count_as_regular_pay.equals(""))
								pstmt.setNull(jj++, Types.CHAR);
						else
								pstmt.setString(jj++, "y");
						if(inactive.equals(""))
								pstmt.setNull(jj++, Types.CHAR);
						else
								pstmt.setString(jj++, "y");
						if(reg_default.equals(""))
								reg_default = "1";
								pstmt.setString(jj++, reg_default);						
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg);
				}
				return msg;
		}
		@Override
		public	String doUpdate(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = " update hour_codes set name=?,description=?,record_method=?,accrual_id=?,count_as_regular_pay=?, inactive=?,reg_default=? where id=?";
				if(name.equals("")){
						msg = "Hour code name is required";
						return msg;
				}
				if(record_method.equals("")){
						msg = "Record method is required";
						return msg;
				}				
				try{
						con = Helper.getConnection();
						if(con == null){
								msg = "Could not connect to DB ";
								return msg;
						}
						pstmt = con.prepareStatement(qq);
						msg = setParams(pstmt);
						pstmt.setString(8, id);
						pstmt.executeUpdate();
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
