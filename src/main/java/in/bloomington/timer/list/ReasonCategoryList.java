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

public class ReasonCategoryList{

    static Logger logger = LogManager.getLogger(ReasonCategoryList.class);
    static final long serialVersionUID = 3800L;
    String sortBy="t.name"; 
    String name = ""; // for service
    boolean active_only = false; // all
    List<ReasonCategory> reasonCategories = null;
	
    public ReasonCategoryList(){
    }
    public ReasonCategoryList(String val){
				setName(val);
    }
    public List<ReasonCategory> getReasonCategories(){
				return reasonCategories;
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
				String qq = "select t.id,t.name,t.inactive from reason_categories t ";
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
						if(active_only){
								if(!qw.isEmpty()) qw += " and ";
								qw += " t.inactive is null ";
						}
						if(!qw.isEmpty()){
								qq += " where "+qw;
						}
						if(!sortBy.isEmpty()){
								qq += " order by "+sortBy;
						}
						logger.debug(qq);
						pstmt = con.prepareStatement(qq);
						if(!name.isEmpty()){
								pstmt.setString(1,"%"+name+"%");
						}						
						rs = pstmt.executeQuery();
						if(reasonCategories == null)
								reasonCategories = new ArrayList<ReasonCategory>();
						while(rs.next()){
								ReasonCategory one =
										new ReasonCategory(rs.getString(1),
																			 rs.getString(2),
																			 rs.getString(3)!=null);
								if(!reasonCategories.contains(one))
										reasonCategories.add(one);
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






















































