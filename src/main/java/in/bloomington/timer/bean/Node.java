package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.Serializable;
import java.util.List;
import java.sql.*;
import javax.sql.*;
import org.apache.log4j.Logger;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;
public class Node extends Type{

    private String managers_only="",annotation="";
		static Logger logger = Logger.getLogger(Node.class);
		static final long serialVersionUID = 1510L;
    public Node(){
				setTable_name("workflow_nodes");
    }		
    public Node(String val){
				super(val);
				setTable_name("workflow_nodes");
    }
    public Node(String val, String val2){
				super(val, val2);
				setTable_name("workflow_nodes");
    }		
		
    public Node(String val,
								String val2,
								String val3,
								boolean val4,
								String val5,								
								boolean val6
								){
				super(val, val2, val3,val6);
				setManagers_only(val4);
				setAnnotation(val5);
    }
    //
    // getters
    //
		public boolean getManagers_only(){
				return !managers_only.equals("");
    }
		public String getAnnotation(){
				return annotation;
		}
    //
    // setters
    //
    public void setManagers_only(boolean val){
				if(val == true)
						managers_only = "y";
				else
						managers_only="";
    }
		public void setAnnotation(String val){
				if(val != null)
						annotation = val;
		}
		public boolean equals(Object o) {
				if (o instanceof Node) {
						Node c = (Node) o;
						if ( this.id.equals(c.getId())) 
								return true;
				}
				return false;
		}
		public int hashCode(){
				int seed = 37;
				if(!id.equals("")){
						try{
								seed += Integer.parseInt(id)*31;
						}catch(Exception ex){
								// we ignore
						}
				}
				return seed;
		}
		@Override
		public String doSelect(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "select g.id,g.name,g.description,g.managers_only,g.annotation,g.inactive from workflow_nodes g where g.id =? ";
				logger.debug(qq);
				try{
						con = Helper.getConnection();
						if(con != null){
								pstmt = con.prepareStatement(qq);
								pstmt.setString(1, id);
								rs = pstmt.executeQuery();
								if(rs.next()){
										setName(rs.getString(2));
										setDescription(rs.getString(3));
										setManagers_only(rs.getString(4) != null);
										setAnnotation(rs.getString(5));
										setInactive(rs.getString(6) != null);
								}
						}
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(con, pstmt, rs);
				}
				return msg;
		}
		@Override
		public String doSave(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "insert into workflow_nodes values(0,?,?,?,?,null) ";
				if(name.equals("")){
						msg = " name not set ";
						return msg;
				}
				logger.debug(qq);
				try{
						con = Helper.getConnection();
						if(con != null){
								pstmt = con.prepareStatement(qq);
								pstmt.setString(1, name);
								if(description.equals(""))
										pstmt.setNull(2, Types.VARCHAR);
								else
										pstmt.setString(2, description);
								if(managers_only.equals(""))
										pstmt.setNull(3, Types.CHAR);
								else
										pstmt.setString(3, "y");
								if(annotation.equals(""))
										pstmt.setNull(4, Types.VARCHAR);
								else
										pstmt.setString(4, annotation);								
								pstmt.executeUpdate();
						}
						qq = "select LAST_INSERT_ID()";
						pstmt = con.prepareStatement(qq);
						rs = pstmt.executeQuery();
						if(rs.next()){
								id = rs.getString(1);
						}
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(con, pstmt, rs);
				}
				return msg;
		}
		@Override
		public String doUpdate(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				if(name.equals("")){
						return " name not set ";
				}
				String qq = "update workflow_nodes set name=?,description=?,managers_only=?,annotation=?,inactive=? where id=? ";
				logger.debug(qq);
				try{
						con = Helper.getConnection();
						if(con != null){
								pstmt = con.prepareStatement(qq);
								pstmt.setString(1, name);
								if(description.equals(""))
										pstmt.setNull(2, Types.VARCHAR);
								else
										pstmt.setString(2, description);
								if(managers_only.equals("")){
										pstmt.setNull(3, Types.CHAR);
								}
								else{
										pstmt.setString(3,"y");
								}
								if(annotation.equals(""))
										pstmt.setNull(4, Types.VARCHAR);
								else
										pstmt.setString(4, annotation);								
								if(inactive.equals("")){
										pstmt.setNull(5, Types.CHAR);
								}
								else{
										pstmt.setString(5,"y");
								}
								pstmt.setString(6, id);
								pstmt.executeUpdate();
						}
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(con, pstmt, rs);
				}
				return msg;
		}
		public String doDelete(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "delete workflow_nodes where id=? ";
				logger.debug(qq);
				try{
						con = Helper.getConnection();
						if(con != null){
								pstmt = con.prepareStatement(qq);
								pstmt.setString(1, id);
								pstmt.executeUpdate();
						}
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(msg+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(con, pstmt, rs);
				}
				return msg;
		}		

}
