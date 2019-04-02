package in.bloomington.timer.list;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */

import java.util.*;
import java.sql.*;
import java.io.*;
import java.text.*;
import java.util.ArrayList;
import java.util.List;
import in.bloomington.timer.util.Helper;
import in.bloomington.timer.util.UnoConnect;
import in.bloomington.timer.bean.EarnCodeReason;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EarnCodeReasonList{

    boolean debug = false;
    String name = "";
    static final long serialVersionUID = 54L;
    static Logger logger = LogManager.getLogger(EarnCodeReasonList.class);
		boolean activeOnly = false;
    List<EarnCodeReason> reasons = null;
    //
    public EarnCodeReasonList(){
    }		
    public EarnCodeReasonList(boolean deb){

				debug = deb;
    }
		// usefull for auto complete
    public EarnCodeReasonList(String val){
				setName(val);
    }
    //
    public void setName(String val){
				if(val != null)
						name = val;
    }
		
    public List<EarnCodeReason> getReasons(){
				return reasons;
    }
    //
    // find all matching records
    // return "" for success or any exception thrown by DB
    //
    public String find(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
		
				String qq = "select h.id,h.name,h.description,h.inactive from "+
						" earn_code_reasons h", qw = "";
				if(!name.equals("")){
						qw += " name like ? ";
				}
				if(activeOnly){
						if(!qw.equals("")) qw += " and ";
						qw += " inactive is null ";
				}
				if(!qw.equals("")){
						qq += " where "+qw;
				}
				qq += " order by name ";
				String back = "";
				con = UnoConnect.getConnection();				
				if(con == null){
						back = "Could not connect to DB ";
						return back;
				}
				try{
						if(debug){
								logger.debug(qq);
						}
						pstmt = con.prepareStatement(qq);
						int jj = 1;
						if(!name.equals("")){
								pstmt.setString(jj,"%"+name+"%");

						}
						rs = pstmt.executeQuery();
						while(rs.next()){
								String str  = rs.getString(1);
								String str2 = rs.getString(2);
								String str3 = rs.getString(3);
								boolean str4 = rs.getString(4) != null;
								// allSet.add(str2);
								if(reasons == null)
										reasons = new ArrayList<>();
								EarnCodeReason one = new EarnCodeReason(debug, str, str2, str3, str4);
								reasons.add(one);
						}
				}
				catch(Exception ex){
						back += ex;
						logger.error(ex+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(pstmt, rs);
						UnoConnect.databaseDisconnect(con);
				}
				return back;
    }
}






















































