package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.sql.*;
import javax.naming.*;
import javax.naming.directory.*;
import java.text.SimpleDateFormat;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Annuancement{

    static final long serialVersionUID = 3700L;	
    static Logger logger = LogManager.getLogger(Annuancement.class);
    static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");   
    String id="", annuance_text="",
	annuance_url="",
	start_date="", expire_date="";
    //
    public Annuancement(){

    }	    
    public Annuancement(String val){
	//
	setId(val);
    }		
    public Annuancement(String val, String val2, String val3, String val4, String val5){
	setId(val);
	setAnnuanceText(val2);
	setAnnuanceUrl(val3);
	setStartDate(val4);
	setExpireDate(val5);
	
    }
 
    public boolean equals(Object obj){
	if(obj instanceof Annuancement){
	    Annuancement one =(Annuancement)obj;
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
    //
    // getters
    //
    public String getId(){
	return id;
    }
    public String getAnnuanceText(){
	return annuance_text;
    }
    public String getAnnuanceUrl(){
	return annuance_url;
    }
    public String getStartDate(){
	return start_date;
    }
    public String getExpireDate(){
	return expire_date;
    }    
    //
    // setters
    //
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setAnnuanceText(String val){
	if(val != null)
	    annuance_text = val.trim();
    }
    public void setAnnuanceUrl(String val){
	if(val != null)
	    annuance_url = val.trim();
    }
    public void setStartDate(String val){
	if(val != null)
	    start_date = val.trim();
    }
    public void setExpireDate(String val){
	if(val != null)
	    expire_date = val.trim();
    }
    public String getInfo(){
	return annuance_text;
    }
    public String toString(){
	return getInfo();
    }
    public boolean hasUrl(){
	return !annuance_url.isEmpty();
    }
    //
    public String doSelect(){
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "select id,annuance_text,annuance_url,"+
	    "date_format(start_date,'%m/%d/%Y'),"+ // date
	    "date_format(expire_date,'%m/%d/%Y') "+ // date	    
	    "from annuancements where id=? ";
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
		setAnnuanceText(rs.getString(2));
		setAnnuanceUrl(rs.getString(3));
		setStartDate(rs.getString(4));
		setExpireDate(rs.getString(5));
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
	String qq = " insert into annuancements values(0,?,?,?,?)";
	if(annuance_text.equals("")){
	    msg = "Annuancement text is required";
	    return msg;
	}
	if(expire_date.equals("")){
	    msg = "Expire date is required";
	    return msg;
	}	
	try{
	    con = UnoConnect.getConnection();
	    if(con == null){
		msg = "Could not connect to DB ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, annuance_text);
	    if(annuance_url.isEmpty())
		pstmt.setNull(2, Types.VARCHAR);
	    else
		pstmt.setString(2, annuance_url);
	    if(start_date.isEmpty())
		pstmt.setNull(3, Types.DATE);
	    else
		pstmt.setDate(3, new java.sql.Date(dateFormat.parse(start_date).getTime()));
	    pstmt.setDate(4, new java.sql.Date(dateFormat.parse(expire_date).getTime()));
	    pstmt.executeUpdate();
	    qq = "select LAST_INSERT_ID()";
	    pstmt2 = con.prepareStatement(qq);
	    rs = pstmt2.executeQuery();
	    if(rs.next()){
		id = rs.getString(1);
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
    public String doUpdate(){
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", str="";
	String qq = " update annuancements set annuance_text=?, annuance_url=?,start_date=?,expire_date=? where id=?";
	if(annuance_text.equals("")){
	    msg = "Annuancement text is required";
	    return msg;
	}
	if(expire_date.equals("")){
	    msg = "Expire date is required";
	    return msg;
	}	
	try{
	    con = UnoConnect.getConnection();
	    if(con == null){
		msg = "Could not connect to DB ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, annuance_text);
	    if(annuance_url.isEmpty())
		pstmt.setNull(2, Types.VARCHAR);
	    else
		pstmt.setString(2, annuance_url);
	    if(start_date.isEmpty())
		pstmt.setNull(3, Types.DATE);
	    else
		pstmt.setDate(3, new java.sql.Date(dateFormat.parse(start_date).getTime()));
	    pstmt.setDate(4, new java.sql.Date(dateFormat.parse(expire_date).getTime()));
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
/**
    create table annuancements (
    id int unsigned not null auto_increment,
    annuance_text varchar(160) not null,
    annuance_url varchar(160),
    start_date date,
    expire_date date,
    primary key(id)
    )engine=InnoDB;



 */
