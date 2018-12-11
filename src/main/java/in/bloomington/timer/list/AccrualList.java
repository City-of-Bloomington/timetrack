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

public class AccrualList{

		static Logger logger = LogManager.getLogger(AccrualList.class);
		static final long serialVersionUID = 3800L;
		String name = ""; // for service
		boolean active_only = false, has_pref_max_level=false;
		List<Accrual> accruals = null;
	
		public AccrualList(){
		}
		public List<Accrual> getAccruals(){
				return accruals;
		}
		
		public void setName(String val){
				if(val != null)
						name = val;
		}
		public void setActiveOnly(){
				active_only = true;
		}
		public void setHasPrefMaxLevel(){
				has_pref_max_level = true;
		}
		public String find(){
		
				String back = "";
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				Connection con = UnoConnect.getConnection();
				String qq = "select t.id,t.name,t.description,t.pref_max_level,t.inactive from accruals t ";
				
				if(con == null){
						back = "Could not connect to DB";
						return back;
				}
				String qw = "";
				try{
						if(!name.equals("")){
								if(!qw.equals("")) qw += " and ";
								qw += " t.name like ? ";
						}
						if(has_pref_max_level){
								if(!qw.equals("")) qw += " and ";								
								qw += " t.pref_max_level > 0 ";
						}
						if(active_only){
								if(!qw.equals("")) qw += " and ";
								qw += " t.inactive is null ";
						}
						if(!qw.equals("")){
								qq += " where "+qw;
						}
						logger.debug(qq);
						pstmt = con.prepareStatement(qq);
						if(!name.equals("")){
								pstmt.setString(1,"%"+name+"%");
						}						
						rs = pstmt.executeQuery();
						if(accruals == null)
								accruals = new ArrayList<Accrual>();
						while(rs.next()){
								Accrual one =
										new Accrual(rs.getString(1),
																rs.getString(2),
																rs.getString(3),
																rs.getInt(4),
																rs.getString(5)!=null);
								if(!accruals.contains(one))
										accruals.add(one);
						}
				}
				catch(Exception ex){
						back += ex+" : "+qq;
						logger.error(back);
				}
				finally{
						Helper.databaseDisconnect(pstmt, rs);
				}
				return back;
		}
}






















































