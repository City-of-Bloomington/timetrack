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

public class CodeRefList{

		static final long serialVersionUID = 54L;
		static Logger logger = LogManager.getLogger(CodeRefList.class);
		//
		static Hashtable<String, String> glHash = new Hashtable<>();
		static Hashtable<String, Integer> ratioHash = new Hashtable<>();
		static Hashtable<String, CodeRef> codeRefHash = new Hashtable<>();
		boolean handOnly = false, ignoreHash=false;
		List<CodeRef> codeRefs = null;
		int pto_ratio_total = 0;
		String code_id="";
    public CodeRefList(){
    }
		public int getRatio(String key){
				if(ratioHash.containsKey(key)){
						return ratioHash.get(key).intValue();
				}
				return 0;
		}
		public void setHandOnly(){
				handOnly = true;
		}
		public String getPto_ratio_total(){
				return ""+pto_ratio_total;
		}
		public Set<String> getRatioKeySet(){
				return ratioHash.keySet();
		}
		public void setCode_id(String val){
				if(val != null)
						code_id=val;
		}
		public boolean hasKey(String code){
				if(code != null){
						return codeRefHash.containsKey(code);
				}
				return false;
		}
		public CodeRef getCodeRef(String code){
				if(hasKey(code)){
						return codeRefHash.get(code);
				}
				return null;
		}
		public List<CodeRef> getCodeRefs(){
				return codeRefs;
		}
		public void setIgnoreHash(){
				ignoreHash = true;
		}
    //
		public  String find(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
		
				String qq = "select id,code_id,code,nw_code,gl_string,pto_ratio from code_cross_ref ";
				String qw = "";
				if(handOnly){
						qw = " gl_string is not null ";
				}
				if(!code_id.equals("")){
						if(!qw.equals("")) qw += " and ";
						qw += " code_id=? ";
				}
				if(!qw.equals("")){
						qq += " where "+qw;
				}
				qq += " order by nw_code ";
				String back = "";
				try{
						logger.debug(qq);
						con = Helper.getConnection();				
						if(con == null){
								back = "Could not connect to DB ";
								return back;
						}
						pstmt = con.prepareStatement(qq);
						if(!code_id.equals("")){
								pstmt.setString(1, code_id);
						}
						rs = pstmt.executeQuery();
						while(rs.next()){
								String str3 = rs.getString(3);
								String str5 = rs.getString(5);
								String str6 = rs.getString(6);
								if(!ignoreHash){
										if(str3 != null && str6 != null){
												int ratio = rs.getInt(6);
												pto_ratio_total += ratio;
												ratioHash.put(str3, ratio);
										}
								}
								CodeRef one = new CodeRef(rs.getString(1),
																					rs.getString(2),
																					rs.getString(3),
																					rs.getString(4),
																					rs.getString(5),
																					rs.getString(6));
								if(!ignoreHash){
										if(str3 != null && str5 != null) 
												glHash.put(str3, str5);
										codeRefHash.put(str3, one);
								}
								if(codeRefs == null)
										codeRefs = new ArrayList<>();
								codeRefs.add(one);
						}
				}
				catch(Exception ex){
						back += ex;
						logger.error(ex+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(con, pstmt, rs);
				}
				return back;
    }
}






















































