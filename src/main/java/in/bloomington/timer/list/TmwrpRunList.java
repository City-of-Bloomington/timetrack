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

public class TmwrpRunList{

    static final long serialVersionUID = 1600L;
    static Logger logger = LogManager.getLogger(TmwrpRunList.class);
    String status = "",
				document_id="", employee_id="",
				pay_period_id="";
		boolean lastRunOnly = false;
    List<TmwrpRun> tmwrpRuns = null;
    public TmwrpRunList(){
    }
    public TmwrpRunList(String val){
				setDocument_id(val);
    }
    public TmwrpRunList(String val, String val2){
				setEmployee_id(val);
				setPay_period_id(val2);
    }		

    public void setDocument_id (String val){
				if(val != null && !val.equals("-1"))
						document_id = val;
    }
    public void setEmployee_id (String val){
				if(val != null && !val.equals("-1"))
						employee_id = val;
    }		
    public void setPay_period_id (String val){
				if(val != null && !val.equals("-1"))
						pay_period_id = val;
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
    public String getEmployee_id(){
				if(employee_id.equals(""))
						return "-1";
				return employee_id;
    }		
		

    public List<TmwrpRun> getTmwrpRuns(){
				return tmwrpRuns;
    }
    //
    public String find(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "select g.id,"+
						"g.document_id,"+
						"g.reg_code_id,"+
						"date_format(g.run_time,'%m/%d/%Y %H:%i'),"+
						
						"g.week1_grs_reg_hrs, "+
						"g.week2_grs_reg_hrs, "+
						"g.week1_net_reg_hrs, "+
						"g.week2_net_reg_hrs "+
						" from tmwrp_runs g  "+
						" join time_documents d on d.id=g.document_id";

				String qw = "";
				if(!document_id.equals("")){
						if(!qw.equals("")) qw += " and ";						
						qw += "g.document_id = ? ";
				}
				if(!pay_period_id.equals("")){
						if(!qw.equals("")) qw += " and ";						
						qw += "d.pay_period_id = ? ";
				}
				if(!employee_id.equals("")){
						if(!qw.equals("")) qw += " and ";						
						qw += "d.employee_id = ? ";
				}				
				if(!qw.equals("")){
						qq += " where "+qw;
				}
				qq += " order by g.run_time desc ";
				if(lastRunOnly){
						qq += " limit 1 "; 
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
						if(!document_id.equals("")){
								pstmt.setString(jj++, document_id);
						}
						if(!pay_period_id.equals("")){
								pstmt.setString(jj++, pay_period_id);
						}
						if(!employee_id.equals("")){
								pstmt.setString(jj++, employee_id);
						}						
						rs = pstmt.executeQuery();
						while(rs.next()){
								if(tmwrpRuns == null)
										tmwrpRuns = new ArrayList<>();
							 TmwrpRun one = new TmwrpRun(
																					 rs.getString(1),
																					 rs.getString(2),
																					 rs.getString(3),
																					 rs.getString(4),
																					 rs.getDouble(5),
																					 rs.getDouble(6),
																					 rs.getDouble(7),
																					 rs.getDouble(8)
																					 );
								if(!tmwrpRuns.contains(one))
										tmwrpRuns.add(one);
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
