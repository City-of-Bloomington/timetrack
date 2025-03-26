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

public class BenefitGroupRefList{

    static Logger logger = LogManager.getLogger(BenefitGroupRefList.class);
    static final long serialVersionUID = 3800L;
    List<BenefitGroupRef> refs = null;
	
    public BenefitGroupRefList(){
    }
    public List<BenefitGroupRef> getRefs(){
	return refs;
    }
    /**		
    public void setName(String val){
	if(val != null)
	    name = val;
    }
    */
    public String find(){
		
	String back = "";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection con = UnoConnect.getConnection();
	String qq = "select t.id,t.benefit_name,t.salary_group_id,s.name from benefit_group_refs t left join salary_groups s on s.id=t.salary_group_id ";
				
	if(con == null){
	    back = "Could not connect to DB";
	    return back;
	}
	String qw = "";
	try{
	    /**
	    if(!name.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";
		qw += " t.name like ? ";
	    }
	    */
	    if(!qw.isEmpty()){
		qq += " where "+qw;
	    }
	    logger.debug(qq);
	    pstmt = con.prepareStatement(qq);
	    rs = pstmt.executeQuery();
	    if(refs == null)
		refs = new ArrayList<>();
	    while(rs.next()){
		BenefitGroupRef one =
		    new BenefitGroupRef(rs.getString(1),
					rs.getString(2),
					rs.getString(3),
					rs.getString(4));
		if(!refs.contains(one))
		    refs.add(one);
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






















































