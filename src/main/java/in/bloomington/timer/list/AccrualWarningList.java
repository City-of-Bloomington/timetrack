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

public class AccrualWarningList{

		static Logger logger = LogManager.getLogger(AccrualWarningList.class);
		static final long serialVersionUID = 3850L;
		String sortBy="w.id", id="", hour_code_id="", accrual_id=""; 
		List<AccrualWarning> accrualWarnings = null;
	
		public AccrualWarningList(){
		}
		
		public void setId(String val){
				if(val != null)
						id = val;
		}
		public void setHour_code_id(String val){
				if(val != null)
						hour_code_id = val;
		}
		public void setAccrual_id(String val){
				if(val != null)
						accrual_id = val;
		}
		public void setRelated_accrual_id(String val){
				if(val != null)
						accrual_id = val;
		}		
		public List<AccrualWarning> getAccrualWarnings(){
				return accrualWarnings;
		}
		
		public String find(){
		
				String back = "";
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				Connection con = UnoConnect.getConnection();
				String qq = "select w.id,w.accrual_id,w.min_hrs,w.step_hrs,w.related_accrual_max_leval,w.step_warning_text,w.min_warning_text,w.excess_warning_text "+
						"from accrual_warnings w";						
				if(con == null){
						back = "Could not connect to DB";
						return back;
				}
				String qw = "";
				try{
						if(!hour_code_id.equals("")){
								qq +=", hour_codes c ";
								if(!qw.equals("")) qw += " and ";
								qw += " c.accrual_id=w.accrual_id and ";
								qw += " c.id = ? ";
						}
						if(!accrual_id.equals("")){
								if(!qw.equals("")) qw += " and ";
								qw += " w.accrual_id = ? ";
						}
						if(!qw.equals("")){
								qq += " where "+qw;
						}
						if(!sortBy.equals("")){
								qq += " order by "+sortBy;
						}
						logger.debug(qq);
						pstmt = con.prepareStatement(qq);
						int jj=1;
						if(!hour_code_id.equals("")){
								pstmt.setString(jj++, hour_code_id);
						}
						if(!accrual_id.equals("")){
								pstmt.setString(jj++, accrual_id);
						}						
						rs = pstmt.executeQuery();
						if(accrualWarnings == null)
								accrualWarnings = new ArrayList<AccrualWarning>();
						while(rs.next()){
								AccrualWarning one =
										new AccrualWarning(rs.getString(1),
																			 rs.getString(2),
																			 rs.getDouble(3),
																			 rs.getDouble(4),
																			 rs.getDouble(5),
																			 rs.getString(6),
																			 rs.getString(7),
																			 rs.getString(8));
								if(!accrualWarnings.contains(one))
										accrualWarnings.add(one);
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






















































