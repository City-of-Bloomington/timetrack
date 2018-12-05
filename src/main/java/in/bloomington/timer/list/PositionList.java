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
		String sortBy="t.name"; 
		String name = ""; 
		boolean active_only = false; // all
		boolean exact_match = false;
		List<Position> positions = null;
	
		public PositionList(){
		}
		public List<Position> getPositions(){
				return positions;
		}
		
		public void setName(String val){
				if(val != null)
						name = val;
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
				String qq = "select t.id,t.name,t.alias,t.description,t.inactive from positions t ";
				if(con == null){
						back = "Could not connect to DB";
						return back;
				}
				String qw = "";
				try{
						if(!name.equals("")){
								if(!qw.equals("")) qw += " and ";
								qw += " (t.name like ? or t.alias like ?) ";
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
								if(exact_match){
										pstmt.setString(1, name);
										pstmt.setString(2, name);
								}
								else{
										pstmt.setString(1,"%"+name+"%");
										pstmt.setString(2,"%"+name+"%");										
								}
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
				}
				return back;
		}
}






















































