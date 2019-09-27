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

public class PositionList{

    static Logger logger = LogManager.getLogger(PositionList.class);
    static final long serialVersionUID = 3800L;
    String sortBy="p.name"; 
    String name = "", group_id="", department_id=""; 
    boolean active_only = false; // all
    boolean exact_match = false;
    List<Position> positions = null;
	
    public PositionList(){
    }
    public PositionList(String val){
				setName(val);
    }		
		
    public List<Position> getPositions(){
				return positions;
    }
		
    public void setName(String val){
				if(val != null)
						name = val.trim();
    }
    public void setGroup_id(String val){
				if(val != null && !val.equals("-1"))
						group_id = val;
    }
    public void setDepartment_id(String val){
				if(val != null && !val.equals("-1"))
						department_id = val;
    }		
    public void setActiveOnly(){
				active_only = true;
    }
    public void setExactMatch(){
				exact_match = true;
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
				String qq = "select distinct(p.id),p.name,p.alias,p.description,p.inactive from positions p ";
				if(con == null){
						back = "Could not connect to DB";
						return back;
				}
				String qw = "";
				try{
						if(!name.equals("")){
								if(!qw.equals("")) qw += " and ";
								qw += " (p.name like ? or p.alias like ?) ";
						}
						if(active_only){
								if(!qw.equals("")) qw += " and ";
								qw += " p.inactive is null ";
						}
						if(!group_id.equals("")){
								qq += ", jobs j ";
								if(!qw.equals("")) qw += " and ";								
								qw += " j.position_id=p.id and j.group_id=? ";
						}
						else if(!department_id.equals("")){
								qq += ", jobs j, groups g ";
								if(!qw.equals("")) qw += " and ";								
								qw += " j.position_id=p.id and j.group_id=g.id and g.department_id=? ";
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
						if(!name.equals("")){
								if(exact_match){
										pstmt.setString(jj++, name);
										pstmt.setString(jj++, name);
								}
								else{
										pstmt.setString(jj++,"%"+name+"%");
										pstmt.setString(jj++,"%"+name+"%");										
								}
						}
						if(!group_id.equals("")){
								pstmt.setString(jj++, group_id);
						}
						else if(!department_id.equals("")){
								pstmt.setString(jj++, department_id);								
						}						
						rs = pstmt.executeQuery();
						if(positions == null)
								positions = new ArrayList<Position>();
						while(rs.next()){
								Position one =
										new Position(rs.getString(1),
																 rs.getString(2),
																 rs.getString(3),
																 rs.getString(4),
																 rs.getString(5)!=null );
								if(!positions.contains(one))
										positions.add(one);
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






















































