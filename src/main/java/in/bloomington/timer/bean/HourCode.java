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
    // each salary group can have only one reg_default
	reg_default=""; // y for default, null for others
    //
    // codes that con be used on holidays only
    private String holiday_related="";
    //
    private double default_monetary_amount=0.0, earn_factor=0.0;
    private String timeUsed="", timeEarned="", unpaid="";
    // recently added 
    private boolean reason_required = false;
    public HourCode(){
    }
    public HourCode(String val){
	setId(val);
    }
    public HourCode(String val, String val2){
	setId(val);
	setName(val2);
    }
    // used in time block list
    public HourCode(String code_id,
		    String code_name,
		    String code_desc,
		    String record_method,
		    String related_accrual_id,
		    String code_type,
		    double default_amount,
		    double earn_factor,
		    boolean holiday_related
		    ){
	setId(code_id);
	setName(code_name);
	setDescription(code_desc);
	setRecord_method(record_method);
	setAccrual_id(related_accrual_id);
	setType(code_type);
	setDefaultMonetaryAmount(default_amount);
	setEarnFactor(earn_factor);
	setHolidayRelated(holiday_related);
    }
		
    public HourCode(String val,
		    String val2,
		    String val3,
		    String val4,
		    String val5,
		    boolean val6,
		    String val7,
		    Double val8,
		    Double val9,
		    boolean val10,
		    boolean val11){
	setVals(val, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11);
    }
    public HourCode(String val,
		    String val2,
		    String val3,
		    String val4,
		    String val5,
		    boolean val6,
		    String val7,
		    Double val8,
		    Double val9,
		    boolean val10,
		    boolean val11,
										
		    String val12, // nw_code
		    String val13){ // gl_string 
	setVals(val, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13);
		
    }		
    void setVals(String val,
		 String val2,
		 String val3,
		 String val4,
		 String val5,
		 boolean val6,
		 String val7,
		 Double val8,
		 Double val9,
		 boolean val10,
		 boolean val11
		 ){
	setId(val);
	setName(val2);
	setDescription(val3);
	setRecord_method(val4);
	setAccrual_id(val5);
	setReg_default(val6);
	setType(val7);
	setDefaultMonetaryAmount(val8);
	setEarnFactor(val9);
	setHolidayRelated(val10);
	setInactive(val11);
				
    }
    void setVals(String val,
		 String val2,
		 String val3,
		 String val4,
		 String val5,
		 boolean val6,
		 String val7,
		 Double val8,
		 Double val9,
		 boolean val10,
		 boolean val11,
								 
		 String val12,
		 String val13
		 ){
	setVals(val, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11);
	if(val12 != null){
	    codeRef = new CodeRef(id, name, val12, val13);
	}
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
	return !inactive.isEmpty();
    }
    public boolean isInactive(){
	return !inactive.isEmpty();
    }
    public boolean isActive(){
	return inactive.isEmpty();
    }
    public boolean isHolidayRelated(){
	return !holiday_related.isEmpty();
    }
    public String getRecord_method(){
	if(record_method.isEmpty()){
	    return "Time";
	}
	return record_method;
    }
    public String getAccrual_id(){
	return accrual_id;
    }
    public boolean getReg_default(){
	return !reg_default.isEmpty();
    }
    public boolean isRegDefault(){
	return !reg_default.isEmpty();
    }		
    public boolean isRecordMethodHours(){
	return record_method.equals("Hours");
    }
    public boolean isRecordMethodMonetary(){
	return record_method.equals("Monetary");
    }		
    public boolean isAccrualRelated(){
	return !accrual_id.isEmpty();
    }
    public double getDefaultMonetaryAmount(){
	return default_monetary_amount;
    }
    public double getEarnFactor(){
	return earn_factor;
    }		
    public void setDefaultMonetaryAmount(Double val){
	if(val != null)
	    default_monetary_amount = val;
    }
    public void setEarnFactor(Double val){
	if(val != null)
	    earn_factor = val;
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
    public void setHolidayRelated(boolean val){
	if(val)
	    holiday_related = "y";
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
    public void setReasonRequired(boolean val){
	if(val)
	    reason_required = val;
    }
    public String getCodeInfo(){
	String ret = name;
	if(!description.isEmpty()){
	    if(!ret.isEmpty())
		ret += " : ";
	    ret += description;
	}
	return ret;
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
	return isRecordMethodMonetary();
    }
    public boolean isMonetary(){
	return isRecordMethodMonetary();
    }		
    public boolean isCallOut(){
	return type.equals("Call Out");
    }				
    public boolean isOther(){
	return type.equals("Other");
    }
    public boolean isReasonRequired(){
	return reason_required;
    }
    public boolean requireReason(){
	return reason_required;
    }
    public boolean hasEarnFactor(){
	return earn_factor > 0;
    }
    public AccrualWarning getAccrualWarning(){
	if(accrualWarning == null &&
	   !id.isEmpty() &&
	   !accrual_id.isEmpty()){
	    AccrualWarningList awl = new AccrualWarningList();
	    awl.setAccrual_id(accrual_id);
	    String back = awl.find();
	    if(back.isEmpty()){
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
	if(!id.isEmpty()){
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
	if(codeRef == null && !id.isEmpty()){
	    CodeRefList cdr = new CodeRefList();
	    cdr.setCode_id(id);
	    cdr.setIgnoreHash();
	    String back = cdr.find();
	    if(back.isEmpty()){
		List<CodeRef> ones = cdr.getCodeRefs();
		if(ones != null && ones.size() > 0){
		    codeRef = ones.get(0);
		}
	    }
	}
	return codeRef;
    }

    /**
     *
     select h.id,h.name,h.description,h.record_method,                                      h.accrual_id, h.reg_default,h.type,                                             h.default_monetary_amount,h.earn_factor,h.holiday_related,                      h.inactive, f.nw_code,f.gl_string,count(g.hour_code_id)                         from hour_codes h left join code_cross_ref f on f.code_id=h.id                  left join code_reason_conditions g on g.hour_code_id=h.id                       where h.id=81 and g.inactive is null group by h.id,h.name,                      h.description,h.record_method, h.accrual_id, h.reg_default,h.type,              h.default_monetary_amount,h.earn_factor,h.holiday_related,                      h.inactive, f.nw_code,f.gl_string

	    
     */
    public String doSelect(){
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = "select h.id,h.name,h.description,h.record_method,"+
	    " h.accrual_id, h.reg_default,h.type,"+
	    " h.default_monetary_amount,h.earn_factor,h.holiday_related,"+
	    " h.inactive, "+
	    " f.nw_code,f.gl_string,count(g.hour_code_id) "+
	    " from hour_codes h left join code_cross_ref f on f.code_id=h.id "+
	    " left join code_reason_conditions g on g.hour_code_id=h.id  "+
	    " where h.id=? and g.inactive is null "+
	    " group by h.id,h.name,h.description,h.record_method, "+
	    " h.accrual_id, h.reg_default,h.type, "+
	    " h.default_monetary_amount,h.earn_factor,h.holiday_related,"+
	    " h.inactive,f.nw_code,f.gl_string ";
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
			rs.getDouble(9),
			rs.getString(10) != null,
			rs.getString(11) != null,
			rs.getString(12),
			rs.getString(13)
			);
		setReasonRequired(rs.getInt(14) > 0);
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
	PreparedStatement pstmt = null, pstmt2=null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = " insert into hour_codes values(0,?,?,?,?, ?,?,?,?, ?,?)";
	if(name.isEmpty()){
	    msg = "Hour code name is required";
	    return msg;
	}
	if(record_method.isEmpty()){
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
	    if(description.isEmpty()){
		pstmt.setNull(jj++, Types.VARCHAR);
	    }
	    else{
		pstmt.setString(jj++, description);
	    }
	    pstmt.setString(jj++, record_method);
	    if(accrual_id.isEmpty())
		pstmt.setNull(jj++, Types.INTEGER);
	    else
		pstmt.setString(jj++, accrual_id);						
	    if(reg_default.isEmpty())
		pstmt.setNull(jj++, Types.CHAR);
	    else
		pstmt.setString(jj++, "y");						
	    if(type.isEmpty())
		pstmt.setNull(jj++, Types.VARCHAR);
	    else
		pstmt.setString(jj++, type);
	    pstmt.setDouble(jj++, default_monetary_amount);
	    pstmt.setDouble(jj++, earn_factor);
	    if(holiday_related.isEmpty())
		pstmt.setNull(jj++, Types.CHAR);
	    else
		pstmt.setString(jj++, "y");						
	    if(inactive.isEmpty())
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

    public String doUpdate(){
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = " update hour_codes set name=?,description=?,record_method=?,accrual_id=?,reg_default=?,type=?,default_monetary_amount=?,earn_factor=?,holiday_related=?,inactive=? where id=?";
	if(name.isEmpty()){
	    msg = "Hour code name is required";
	    return msg;
	}
	if(record_method.isEmpty()){
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
	    pstmt.setString(11, id);
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
