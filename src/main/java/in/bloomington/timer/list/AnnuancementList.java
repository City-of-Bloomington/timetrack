package in.bloomington.timer.list;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.*;
import java.sql.*;
import java.io.*;
import java.text.SimpleDateFormat;
import javax.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;

public class AnnuancementList{

    static Logger logger = LogManager.getLogger(AnnuancementList.class);
    static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    static final long serialVersionUID = 3800L;
    String start_date = "", expire_date=""; // for service
    List<Annuancement> annuancements = null;
    Annuancement currentAnnuancement = null;
    public AnnuancementList(){
    }
    public List<Annuancement> getAnnuancements(){
	return annuancements;
    }
		
    public void setStartDate(String val){
	if(val != null)
	    start_date = val;
    }
    public void setExpireDate(String val){
	if(val != null)
	    expire_date = val;
    }
    public boolean hasCurrentAnnuance(){
	findCurrentOnly();
	return currentAnnuancement != null;
    }
    public Annuancement getCurrentAnnuance(){
	return currentAnnuancement;
    }
    public String findCurrentOnly(){
	String back = "";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection con = UnoConnect.getConnection();
	String qq = "select id, annuance_text, annuance_url, "+
	    "date_format(start_date,'%m/%d/%Y'),"+ // date
	    "date_format(expire_date,'%m/%d/%Y') "+ // date	    
	    "from annuancements where CURDATE() <= expire_date and "+
	    "(start_date <= CURDATE() or start_date is null) "+
	    " order by DATEDIFF(expire_date, CURDATE()) ";
				
	if(con == null){
	    back = "Could not connect to DB";
	    return back;
	}
	try{
	    // System.err.println(qq);
	    pstmt = con.prepareStatement(qq);
	    rs = pstmt.executeQuery();
	    if(rs.next()){ // we take the first one only
		Annuancement one =
		    new Annuancement(rs.getString(1),
				     rs.getString(2),
				     rs.getString(3),
				     rs.getString(4),
				     rs.getString(5));
		currentAnnuancement = one;
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
    public String find(){
		
	String back = "";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection con = UnoConnect.getConnection();
	String qq = "select id, annuance_text, annuance_url, "+
	    "date_format(start_date,'%m/%d/%Y'),"+ // date
	    "date_format(expire_date,'%m/%d/%Y') "+ // date	    
	    "from annuancements ";
				
	if(con == null){
	    back = "Could not connect to DB";
	    return back;
	}
	String qw = "";
	try{
	    if(!start_date.isEmpty()){
		qw += " start_date >= ?";
	    }
	    if(!expire_date.isEmpty()){
		qw += " expire_date <= ?";
	    }
	    if(!qw.isEmpty()){
		qq += " where "+qw;
	    }
	    qq +=" order by id desc ";
	    logger.debug(qq);
	    pstmt = con.prepareStatement(qq);
	    int jj=1;
	    if(!start_date.isEmpty()){
		pstmt.setDate(jj++, new java.sql.Date(dateFormat.parse(start_date).getTime()));
	    }
	    if(!expire_date.isEmpty()){
		pstmt.setDate(jj++, new java.sql.Date(dateFormat.parse(expire_date).getTime()));
	    }	    
	    rs = pstmt.executeQuery();
	    if(annuancements == null)
		annuancements = new ArrayList<>();
	    while(rs.next()){
		Annuancement one =
		    new Annuancement(rs.getString(1),
				     rs.getString(2),
				     rs.getString(3),
				     rs.getString(4),
				     rs.getString(5));
		if(!annuancements.contains(one))
		    annuancements.add(one);
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






















































