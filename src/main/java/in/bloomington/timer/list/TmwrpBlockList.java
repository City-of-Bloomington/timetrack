package in.bloomington.timer.list;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.ArrayList;
import java.util.List;
import java.text.*;
import java.util.Date;
import java.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;

public class TmwrpBlockList{

    static final long serialVersionUID = 1600L;
    static Logger logger = LogManager.getLogger(TmwrpBlockList.class);
    String run_id="", document_id="", pay_period_id="";

    List<TmwrpBlock> blocks = null;
    public TmwrpBlockList(){
    }
    public TmwrpBlockList(String val){
	setRun_id(val);
    }
    public void setRun_id (String val){
	if(val != null && !val.equals("-1"))
	    run_id = val;
    }
    public void setDocument_id (String val){
	if(val != null && !val.equals("-1"))
	    document_id = val;
    }
    public void setPay_period_id (String val){
	if(val != null && !val.equals("-1"))
	    pay_period_id = val;
    }		

    public String getRun_id(){
	if(run_id.isEmpty())
	    return "-1";
	return run_id;
    }
    public String getDocument_id(){
	if(document_id.isEmpty())
	    return "-1";
	return document_id;
    }		
		
    public String getPay_period_id(){
	if(pay_period_id.isEmpty())
	    return "-1";
	return pay_period_id;
    }		
    public List<TmwrpBlock> getBlocks(){
	return blocks;
    }
    //
    // getters
    //
    public String find(){
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = "select g.id,g.run_id,"+
	    "g.hour_code_id,g.term_type,g.cycle_order,g.hours,g.amount, "+
	    " h.id,h.name,h.description,h.record_method,"+
	    " h.accrual_id, h.reg_default,h.type,"+
	    " h.default_monetary_amount,h.earn_factor,h.holiday_related,"+
	    " h.inactive, "+
	    " f.nw_code,f.gl_string "+
	    " from tmwrp_blocks g "+						
	    " join hour_codes h on h.id=g.hour_code_id "+
	    " left join code_cross_ref f on f.code_id=h.id ";					
	String qw = "";
	if(!run_id.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";						
	    qw += "g.run_id = ? ";
	}
	if(!qw.isEmpty()){
	    qq += " where "+qw;
	}
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = " Could not connect to DB ";
	    logger.error(msg);
	    return msg;
	}
	logger.debug(qq);
	try{
	    pstmt = con.prepareStatement(qq);
	    int jj=1;
	    if(!run_id.isEmpty()){
		pstmt.setString(jj++, run_id);
	    }
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		if(blocks == null)
		    blocks = new ArrayList<>();
		TmwrpBlock one = new TmwrpBlock(
						rs.getString(1),
						rs.getString(2),
						rs.getString(3),
						rs.getString(4),
						rs.getInt(5),
						rs.getDouble(6),
						rs.getDouble(7),
																			
						rs.getString(8), // HourCode
						rs.getString(9),
						rs.getString(10),
						rs.getString(11),
						rs.getString(12),
						rs.getString(13) != null,
						rs.getString(14),
						rs.getDouble(15),
						rs.getDouble(16),
						rs.getString(17) != null,
						rs.getString(18) != null,
																			
						rs.getString(19),
						rs.getString(20));


																			
		if(!blocks.contains(one))
		    blocks.add(one);
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

}
