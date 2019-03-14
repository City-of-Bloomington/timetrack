package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.*;
import java.sql.*;
import java.text.*;
import in.bloomington.timer.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CodeRef{ 

		String code="", code_id="",
				nw_code="", gl_value = "", pto_ratio = "", id="";
		static Logger logger = LogManager.getLogger(CodeRef.class);
    public CodeRef(){}
    public CodeRef(String val,
									 String val2,
									 String val3,
									 String val4,
									 String val5,
									 String val6
									 ){
				setId(val);
				setCode_id(val2);
				setCode(val3);
				setNw_code(val4);
				setGl_value(val5);
				setPto_ratio(val6);
    }
		// to join with HourCode ID and ratio ignored
    public CodeRef(String val,
									 String val2,
									 String val3,
									 String val4
									 ){
				setCode_id(val);
				setCode(val2);
				setNw_code(val3);
				setGl_value(val4);
    }		
		public String getId(){
				return id;
    }
		public String getCode_id(){
				return code_id;
    }		
		public String getCode(){
				return code;
    }
		public String getNw_code(){
				return nw_code;
    }		
		public String getGl_value(){
				return gl_value;
		}
		public String getPto_ratio(){
				return pto_ratio;
    }
		public int getPto_ratio_int(){
				int ret = 0;
				if(!pto_ratio.equals("")){
						try{
								ret = Integer.parseInt(pto_ratio);
						}catch(Exception ex){}
				}
				return ret;
		}
		//
    // setters
    //
    public void setId(String val){
				if(val != null)		
						id = val;
    }
    public void setCode_id(String val){
				if(val != null && !val.equals("-1"))		
						code_id = val;
    }		
    public void setCode(String val){
				if(val != null)		
						code = val;
    }
    public void setNw_code(String val){
				if(val != null)		
						nw_code = val;
    }		
    public void setGl_value(String val){
				if(val != null){
						gl_value = val;
				}
    }	
    public void setPto_ratio(String val){
				if(val != null)		
						pto_ratio = val;
    }		
		public  String doSave(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
		
				String qq = 
						" insert into code_cross_ref values(0,?,?,?,?,?)";
				String back = "";
				if(nw_code.equals("")){
						back = " nw_code not set ";
						return back;
				}
				logger.debug(qq);
				con = UnoConnect.getConnection();	
				if(con == null){
						back = "Could not connect to DB ";
						return back;
				}				
				try{
						pstmt = con.prepareStatement(qq);
						int jj = 1;
						if(code_id.equals(""))
								pstmt.setNull(jj++,Types.INTEGER);
						else
								pstmt.setString(jj++, code_id);
						if(code.equals(""))
								pstmt.setNull(jj++,Types.VARCHAR);
						else
								pstmt.setString(jj++, code);						
						pstmt.setString(jj++, nw_code);
												
						if(gl_value.equals(""))
								pstmt.setNull(jj++,Types.VARCHAR);
						else
								pstmt.setString(jj++, gl_value);
						if(pto_ratio.equals(""))
								pstmt.setNull(jj++,Types.INTEGER);
						else
								pstmt.setString(jj++, pto_ratio);						
						pstmt.executeUpdate();
						Helper.databaseDisconnect(pstmt, rs);
						//
						qq = "select LAST_INSERT_ID() ";
						logger.debug(qq);
						pstmt = con.prepareStatement(qq);			
						rs = pstmt.executeQuery();
						if(rs.next()){
								id = rs.getString(1);
						}				
				}
				catch(Exception ex){
						back += ex;
						logger.error(ex+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(pstmt, rs);
						UnoConnect.databaseDisconnect(con);
				}
				return back;
    }
    public String doUpdate(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
		
				String qq = 
						" update code_cross_ref "+
						" set code_id=?,code=?, nw_code=?, gl_string=?,pto_ratio=? "+			
						" where id = ? ";
				String back = "";
				if(id.equals("") || code.equals("") || nw_code.equals("")){
						back = " code,nw_code or id not set ";
						return back;
				}
				logger.debug(qq);
				con = UnoConnect.getConnection();	
				if(con == null){
						back = "Could not connect to DB ";
						return back;
				}
				
				try{
						pstmt = con.prepareStatement(qq);
						int jj = 1;
						if(code_id.equals(""))
								pstmt.setNull(jj++,Types.INTEGER);
						else
								pstmt.setString(jj++, code_id);
						if(code.equals(""))
								pstmt.setNull(jj++,Types.VARCHAR);
						else
								pstmt.setString(jj++, code);
						pstmt.setString(jj++, nw_code);
												
						if(gl_value.equals(""))
								pstmt.setNull(jj++,Types.VARCHAR);
						else
								pstmt.setString(jj++, gl_value);
						if(pto_ratio.equals(""))
								pstmt.setNull(jj++,Types.INTEGER);
						else
								pstmt.setString(jj++, pto_ratio);						
						pstmt.setString(jj++, id);
						pstmt.executeUpdate();

				}
				catch(Exception ex){
						back += ex;
						logger.error(ex+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(pstmt, rs);
						UnoConnect.databaseDisconnect(con);
				}
				return back;
    }		
   public  String doSelect(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
		
				String qq = "select code_id,code,nw_code,gl_string,pto_ratio from "+
						" code_cross_ref where id = ? ";
				String back = "";
				logger.debug(qq);
				con = UnoConnect.getConnection();				
				if(con == null){
						back = "Could not connect to DB ";
						return back;
				}				
				try{
						pstmt = con.prepareStatement(qq);
						int jj = 1;
						pstmt.setString(jj,id);
						rs = pstmt.executeQuery();
						if(rs.next()){
								setCode_id(rs.getString(1));
								setCode(rs.getString(2));
								setNw_code(rs.getString(3));								
								setGl_value(rs.getString(4));
								setPto_ratio(rs.getString(5));								
						}
				}
				catch(Exception ex){
						back += ex;
						logger.error(ex+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(pstmt, rs);
						UnoConnect.databaseDisconnect(con);
				}
				return back;
    }	

}
