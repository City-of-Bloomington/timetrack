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

public class ServiceKeyList{

    static Logger logger = LogManager.getLogger(ServiceKeyList.class);
    static final long serialVersionUID = 2850L;
    String sortBy="t.id desc"; 
    String name = "";
    boolean active_only = false; // all
    List<ServiceKey> keys = null;
	
    public ServiceKeyList(){
    }
    public ServiceKeyList(String val){
				setName(val);
    }		
    public List<ServiceKey> getKeys(){
				return keys;
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
				String qq = "select t.id,t.key_name,t.key_value,t.inactive from service_keys t ";
				if(con == null){
						back = "Could not connect to DB";
						return back;
				}
				String qw = "";
				try{
						if(!name.equals("")){
								if(!qw.equals("")) qw += " and ";
								qw += " t.key_name like ? ";
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
								pstmt.setString(1,name+"%");
						}						
						rs = pstmt.executeQuery();
						if(keys == null)
								keys = new ArrayList<>();
						while(rs.next()){
								ServiceKey one =
										new ServiceKey(rs.getString(1),
																	 rs.getString(2),
																	 rs.getString(3),
																	 rs.getString(4) != null);
								if(!keys.contains(one))
										keys.add(one);
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






















































