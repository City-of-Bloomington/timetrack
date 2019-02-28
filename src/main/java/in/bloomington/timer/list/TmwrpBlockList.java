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

    List<TmwrpBlock> tmwrpBlocks = null;
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
				if(run_id.equals(""))
						return "-1";
				return run_id;
    }
    public String getDocument_id(){
				if(document_id.equals(""))
						return "-1";
				return document_id;
    }		
		
    public String getPay_period_id(){
				if(pay_period_id.equals(""))
						return "-1";
				return pay_period_id;
    }		
    public List<TmwrpBlock> getTmwrpBlocks(){
				return tmwrpBlocks;
    }
    //
    // getters
    //
    public String find(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "select g.id,g.run_id,g.document_id,"+
						" g.hour_code_id,g.hours,g.amount,"+
						" date_format(g.apply_date,'%m/%d/%Y'),g.addition_type "+
						" from tmwrp_blocks g join time_documents d on d.id=g.document_id ";
				String qw = "";
				if(!run_id.equals("")){
						if(!qw.equals("")) qw += " and ";						
						qw += "g.run_id = ? ";
				}
				if(!document_id.equals("")){
						if(!qw.equals("")) qw += " and ";						
						qw += "g.document_id = ? ";
				}				
				if(!pay_period_id.equals("")){
						if(!qw.equals("")) qw += " and ";						
						qw += "d.pay_period_id = ? ";
				}
				if(!qw.equals("")){
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
						if(!run_id.equals("")){
								pstmt.setString(jj++, run_id);
						}
						if(!document_id.equals("")){
								pstmt.setString(jj++, document_id);
						}						
						if(!pay_period_id.equals("")){
								pstmt.setString(jj++, pay_period_id);
						}
						rs = pstmt.executeQuery();
						while(rs.next()){
								if(tmwrpBlocks == null)
										tmwrpBlocks = new ArrayList<>();
							 TmwrpBlock one = new TmwrpBlock(
																			rs.getString(1),
																			rs.getString(2),
																			rs.getString(3),
																			rs.getString(4),
																			rs.getDouble(5),
																			rs.getDouble(6),
																			rs.getString(7),
																			rs.getString(8));
								if(!tmwrpBlocks.contains(one))
										tmwrpBlocks.add(one);
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
