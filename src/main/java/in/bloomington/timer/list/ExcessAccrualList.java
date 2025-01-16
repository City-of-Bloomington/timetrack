package in.bloomington.timer.list;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.*;
import java.sql.*;
import java.io.*;
import javax.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;

public class ExcessAccrualList{

    static Logger logger = LogManager.getLogger(ExcessAccrualList.class);
    static final long serialVersionUID = 3800L;
    String accrual_id="3"; // comp time accrual the default
    String threshold_value="40";
    List<ExcessAccrual> excesses = null;
	
    public ExcessAccrualList(){
    }
    public ExcessAccrualList(String val, String val2){
	setAccrual_id(val);
	setThresholdValue(val2);
    }    
    public List<ExcessAccrual> getExcesses(){
	return excesses;
    }
    public void setAccrual_id(String val){
	if(val != null)
	    accrual_id = val;
    }
    public void setThresholdValue(String val){
	if(val != null)
	    threshold_value = val;
    }    

    public String find(){
		
	String back = "";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection con = UnoConnect.getConnection();
	System.err.println(" theshold "+threshold_value);
	System.err.println(" accrual id "+accrual_id);	
	String qq = "select a.accrual_id,c.description, a.employee_id,"+
	    " concat_ws(' ',e.first_name,e.last_name),e.email,"+
	    "a.hours, "+	    
	    "date_format(a.date,'%m/%d/%Y'),j.group_id "+
	    "from employee_accruals a "+
	    "join employees e on e.id=a.employee_id "+
	    "join accruals c on c.id=a.accrual_id "+
	    "join jobs j on j.employee_id=e.id "+
	    "where hours > ? and c.id = ? "+
	    "and (j.expire_date is null or j.expire_date >= curdate()) "+
	    "and j.inactive is null "+
	    "and a.date = (select date from employee_accruals order by id desc limit 1) "+
	    "order by a.id desc";
				
	if(con == null){
	    back = "Could not connect to DB";
	    return back;
	}
	String qw = "";
	try{
	    logger.debug(qq);
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, threshold_value);
	    pstmt.setString(2, accrual_id);
	    rs = pstmt.executeQuery();
	    if(excesses == null)
		excesses = new ArrayList<>();
	    while(rs.next()){
		ExcessAccrual one =
		    new ExcessAccrual(rs.getString(1),
				      rs.getString(2),
				      rs.getString(3),
				      rs.getString(4),
				      rs.getString(5),
				      rs.getFloat(6),
				      rs.getString(7),
				      rs.getString(8),
				      threshold_value);
		if(!excesses.contains(one))
		    excesses.add(one);
	    }
	}
	catch(Exception ex){
	    back += ex+" : "+qq;
	    logger.error(back);
	}
	finally{
	    Helper.databaseDisconnect(pstmt, rs);
	    UnoConnect.databaseDisconnect(con);
	}
	return back;
    }
}






















































