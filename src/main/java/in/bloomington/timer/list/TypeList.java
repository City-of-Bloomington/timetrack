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
import org.apache.log4j.Logger;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;

public class TypeList{

		static Logger logger = Logger.getLogger(TypeList.class);
		static final long serialVersionUID = 3800L;
		String table_name = "", condition="", join="", sortBy="t.name"; 
		String name = ""; // for service
		boolean active_only = false; // all
		List<Type> types = null;
	
		public TypeList(){
		}
		public TypeList(String val){
				setTable_name(val);
		}
		public List<Type> getTypes(){
				return types;
		}
		
		public void setTable_name(String val){
				if(val != null)
						table_name = val;
		}
		public void setName(String val){
				if(val != null)
						name = val;
		}
		public void setActiveOnly(){
				active_only = true;
		}
		public void setJoin(String val){
				if(val != null)
						join = val;
		}
		public void setCondition(String val){
				if(val != null)
						condition = val;
		}
		public void setSortBy(String val){
				if(val != null)
						sortBy = val;
		}
		public String find(){
		
				String back = "";
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				Connection con = Helper.getConnection();
				String qq = "select t.id,t.name,t.description,t.inactive from "+table_name+" t ";
				if(con == null){
						back = "Could not connect to DB";
						return back;
				}
				String qw = "";
				if(!join.equals("")){
						qq += ", "+join;
						qw += condition;
				}
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
								pstmt.setString(1,"%"+name+"%");
						}						
						rs = pstmt.executeQuery();
						if(types == null)
								types = new ArrayList<Type>();
						while(rs.next()){
								Type one =
										new Type(rs.getString(1),
														 rs.getString(2),
														 rs.getString(3),
														 rs.getString(4)!=null);
								if(!types.contains(one))
										types.add(one);
						}
				}
				catch(Exception ex){
						back += ex+" : "+qq;
						logger.error(back);
				}
				finally{
						Helper.databaseDisconnect(con, pstmt, rs);
				}
				return back;
		}
}






















































