package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.List;
import java.sql.*;
import javax.naming.*;
import javax.naming.directory.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;

public class CleanUp{

    static final long serialVersionUID = 3700L;	
    static Logger logger = LogManager.getLogger(CleanUp.class);
    String pay_period_id="", employee_id = "";
    List<Document> documents = null;
    //
    public CleanUp(){

    }
    public boolean equals(Object obj){
	if(obj instanceof CleanUp){
	    CleanUp one = (CleanUp)obj;
	    return (one.getPay_period_id().equals(pay_period_id)
		    && one.getEmployee_id().equals(employee_id));
	}
	return false;				
    }
    public int hashCode(){
	int seed = 23;
	if(!pay_period_id.isEmpty() && !employee_id.isEmpty()){
	    try{
		seed += Integer.parseInt(employee_id);
		seed += Integer.parseInt(pay_period_id);
	    }catch(Exception ex){
	    }
	}
	return seed;
    }
    //
    // getters
    //
    public List<Document> getDocuments(){
	return documents;
    }
    //
    // setters
    //
    public void setDocuments(List<Document> vals){
	if(vals != null){
	    documents = vals;
	    Document document = documents.get(0);
	    pay_period_id = document.getPay_period_id();
	    employee_id = document.getEmployee_id();
	}
    }
    public String getEmployee_id(){
	return employee_id;
    }
    public String getPay_period_id(){
	return pay_period_id;
    }
    boolean hasDocuments(){
	return documents != null;
    }
    /**
     // test cases
     delete from time_block_logs where document_id in (37413,37414);
     delete from time_blocks where document_id in (37413,37414);
     delete from time_actions where document_id in (37413,37414);
     select id from tmwrp_runs where document_id in (37413,37414);
     we get 34777,34778
     delete from tmwrp_blocks where run_id in (34777,34778);
     delete from tmwrp_runs where id in (34777,34778);
     delete from time_documents where id in (37413,37414);
				

    */
    //
    public String doClean(){
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null, pstmt2=null,pstmt3=null,pstmt4=null,
	    pstmt5=null, pstmt6=null,pstmt7=null;
	ResultSet rs = null;
	if(!hasDocuments()){
	    back = " No documents found";
	}
	if(!back.isEmpty()){
	    logger.error(back);
	    return back;
	}
	String set = "", run_ids="";
	for(Document one:documents){
	    if(!set.isEmpty()) set += ",";
	    set += one.getId();
	}
	String qq = " delete from time_block_logs where document_id in ("+set+")";
	String qq2 = " delete from time_blocks where document_id in ("+set+")";
	String qq3 = " delete from time_actions where document_id in ("+set+")";
	String qq4 = " select id from tmwrp_runs where document_id in ("+set+")";
	String qq5 = " delete from tmwrp_blocks where run_id in ";
	String qq6 = " delete from tmwrp_runs where id in ";
	String qq7 = " delete from time_documents where id in ("+set+")";
	con = UnoConnect.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    return back;
	}
	logger.debug(qq);				
	try{

	    pstmt = con.prepareStatement(qq);
	    pstmt.executeUpdate();
	    //
	    pstmt2 = con.prepareStatement(qq2);
	    pstmt2.executeUpdate();
						
	    pstmt3 = con.prepareStatement(qq3);
	    pstmt3.executeUpdate();
	    pstmt4 = con.prepareStatement(qq4);
	    rs = pstmt4.executeQuery();
	    while(rs.next()){
		if(!run_ids.isEmpty()) run_ids += ",";
		run_ids += rs.getString(1);
	    }
	    if(!run_ids.isEmpty()){
		qq5 += "("+run_ids+")";
		pstmt5 = con.prepareStatement(qq5);
		pstmt5.executeUpdate();
		qq6 += "("+run_ids+")";
		pstmt6 = con.prepareStatement(qq6);
		pstmt6.executeUpdate();
	    }
	    pstmt7 = con.prepareStatement(qq7);
	    pstmt7.executeUpdate();						
	}
	catch(Exception ex){
	    back += ex+":"+qq;
	    logger.error(back);
	}
	finally{
	    Helper.databaseDisconnect(rs, pstmt, pstmt2, pstmt3, pstmt4, pstmt4, pstmt6, pstmt7);
	    UnoConnect.databaseDisconnect(con);
	}
	return back;
    }

}
