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

public class TermRecipientList{

    static Logger logger = LogManager.getLogger(TermRecipientList.class);
    static final long serialVersionUID = 3800L;
    String name = ""; // for service
    List<TermRecipient> recipients = null;
	
    public TermRecipientList(){
    }
    public List<TermRecipient> getRecipients(){
	return recipients;
    }
		
    public void setName(String val){
	if(val != null)
	    name = val;
    }
    public String find(){
		
	String back = "";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection con = UnoConnect.getConnection();
	String qq = "select t.id,t.name,t.email from term_recipients t ";
				
	if(con == null){
	    back = "Could not connect to DB";
	    return back;
	}
	String qw = "";
	try{
	    if(!name.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";
		qw += " t.name like ? ";
	    }
	    if(!qw.isEmpty()){
		qq += " where "+qw;
	    }
	    logger.debug(qq);
	    pstmt = con.prepareStatement(qq);
	    if(!name.isEmpty()){
		pstmt.setString(1,"%"+name+"%");
	    }						
	    rs = pstmt.executeQuery();
	    if(recipients == null)
		recipients = new ArrayList<TermRecipient>();
	    while(rs.next()){
		TermRecipient one =
		    new TermRecipient(rs.getString(1),
				      rs.getString(2),
				      rs.getString(3));
		if(!recipients.contains(one))
		    recipients.add(one);
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






















































