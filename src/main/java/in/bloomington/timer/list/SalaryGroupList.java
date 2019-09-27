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

public class SalaryGroupList{

    static Logger logger = LogManager.getLogger(SalaryGroupList.class);
    static final long serialVersionUID = 3800L;
    String sortBy="t.name"; 
    String name = "";
    boolean active_only = false; // all
    List<SalaryGroup> salaryGroups = null;
	
    public SalaryGroupList(){
    }
    public SalaryGroupList(String val){
				setName(val);
    }		
    public List<SalaryGroup> getSalaryGroups(){
				return salaryGroups;
    }
		
    public void setName(String val){
				if(val != null)
						name = val;
    }
    public void setActiveOnly(){
				active_only = true;
    }
    public void setSortBy(String val){
				if(val != null)
						sortBy = val;
    }
    public String find(){
		
				String back = "";
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				Connection con = UnoConnect.getConnection();
				String qq = "select t.id,t.name,t.description,t.default_regular_id,"+
						"t.excess_culculation,t.inactive from salary_groups t ";
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
						if(active_only){
								if(!qw.equals("")) qw += " and ";
								qw += " t.inactive is null ";
						}
						if(!qw.equals("")){
								qq += " where "+qw;
						}
						if(!sortBy.equals("")){
								qq += " order by "+sortBy;
						}
						logger.debug(qq);
						pstmt = con.prepareStatement(qq);
						if(!name.equals("")){
								pstmt.setString(1,name);
						}						
						rs = pstmt.executeQuery();
						if(salaryGroups == null)
								salaryGroups = new ArrayList<SalaryGroup>();
						while(rs.next()){
								SalaryGroup one =
										new SalaryGroup(rs.getString(1),
																		rs.getString(2),
																		rs.getString(3),
																		rs.getString(4),
																		rs.getString(5),
																		rs.getString(6)!=null);
								if(!salaryGroups.contains(one))
										salaryGroups.add(one);
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






















































