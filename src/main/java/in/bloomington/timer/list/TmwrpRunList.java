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
				department_id="",
				pay_period_id="";
    List<TmwrpRun> tmwrpRuns = null;
    public TmwrpRunList(){
    }
    public TmwrpRunList(String val, String val2){
				setDepartment_id(val);
				setPay_period_id(val2);
    }

    public void setDepartment_id (String val){
				if(val != null && !val.equals("-1"))
						department_id = val;
    }
    public void setPay_period_id (String val){
				if(val != null && !val.equals("-1"))
						pay_period_id = val;
    }		

    public String getDepartment_id(){
				if(department_id.equals(""))
						return "-1";
				return department_id;
    }
    public String getPay_period_id(){
				if(pay_period_id.equals(""))
						return "-1";
				return pay_period_id;
    }		
		public void setFailedOnly(){
				status = "Error";
		}
    public List<TmwrpRun> getTmwrpRuns(){
				return tmwrpRuns;
    }
    //
    // getters
    //
    public String find(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "select g.id,g.pay_period_id,g.department_id,"+
						" date_format(g.run_time,'%m/%d/%y %H:%i'),g.status,"+
						" g.error_text,d.name from tmwrp_runs g join departments d on d.id=g.department_id ";
				String qw = "";
				if(!department_id.equals("")){
						if(!qw.equals("")) qw += " and ";						
						qw += "g.department_id = ? ";
				}
				if(!pay_period_id.equals("")){
						if(!qw.equals("")) qw += " and ";						
						qw += "g.pay_period_id = ? ";
				}
				if(!status.equals("")){
						if(!qw.equals("")) qw += " and ";						
						qw += "g.status = ? ";
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
						if(!department_id.equals("")){
								pstmt.setString(jj++, department_id);
						}
						if(!pay_period_id.equals("")){
								pstmt.setString(jj++, pay_period_id);
						}
						if(!status.equals("")){
								pstmt.setString(jj++, status);
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
																			rs.getString(5),
																			rs.getString(6),
																			rs.getString(7));
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
