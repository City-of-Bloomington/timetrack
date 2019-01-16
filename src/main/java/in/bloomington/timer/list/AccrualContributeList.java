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

public class AccrualContributeList{

		static Logger logger = LogManager.getLogger(AccrualContributeList.class);
		static final long serialVersionUID = 3800L;
		String name = "", accrual_id="", hour_code_id="";
		List<AccrualContribute> contributes = null;
	
		public AccrualContributeList(){
		}
		public List<AccrualContribute> getContributes(){
				return contributes;
		}
		
		public void setName(String val){
				if(val != null)
						name = val;
		}
		public void setAccrual_id(String val){
				if(val != null)
						accrual_id = val;
		}
		public void setHourCode_id(String val){
				if(val != null)
						hour_code_id = val;
		}		
		public String find(){
		
				String back = "";
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				Connection con = UnoConnect.getConnection();
				String qq = "select c.id,c.name,c.accrual_id,"+
						"c.hour_code_id,c.factor, "+
						"a.id,a.name,a.description,a.pref_max_level,a.inactive "+
						"from accrual_contributes c,accruals a ";
				String qw = " c.accrual_id=a.id ";				
				if(con == null){
						back = "Could not connect to DB";
						return back;
				}
				try{
						if(!name.equals("")){
								if(!qw.equals("")) qw += " and ";
								qw += " c.name like ? ";
						}
						if(!accrual_id.equals("")){
								if(!qw.equals("")) qw += " and ";								
								qw += " c.accrual_id = ? ";
						}
						if(!hour_code_id.equals("")){
								if(!qw.equals("")) qw += " and ";								
								qw += " c.hour_code_id = ? ";
						}
						if(!qw.equals("")){
								qq += " where "+qw;
						}
						logger.debug(qq);
						pstmt = con.prepareStatement(qq);
						int jj=1;
						if(!name.equals("")){
								pstmt.setString(jj++,"%"+name+"%");
						}
						if(!accrual_id.equals("")){
								pstmt.setString(jj++,accrual_id);
						}
						if(!hour_code_id.equals("")){
								pstmt.setString(jj++,hour_code_id);
						}						
						rs = pstmt.executeQuery();
						if(contributes == null)
								contributes = new ArrayList<AccrualContribute>();
						while(rs.next()){
								AccrualContribute one =
										new AccrualContribute(rs.getString(1),
																					rs.getString(2),
																					rs.getString(3),
																					rs.getString(4),
																					rs.getDouble(5),

																					rs.getString(6),
																					rs.getString(7),
																					rs.getString(8),
																					rs.getInt(9),
																					rs.getString(10) != null);
								if(!contributes.contains(one))
										contributes.add(one);
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






















































