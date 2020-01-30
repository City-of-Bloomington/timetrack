package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.sql.*;
import java.util.*;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TimeActiveAudit{

		static final long serialVersionUID = 3700L;	
		static Logger logger = LogManager.getLogger(TimeActiveAudit.class);
    String start_date="", end_date="";
		boolean include_name = false;
		List<List<String>> entries = null;
		//
		public TimeActiveAudit(){

		}
		public int hashCode(){
				int seed = 29;
				return seed;
		}
    //
    // getters
    //
    public String getStart_date(){
				return start_date;
    }
    public String getEnd_date(){
				return end_date;
    }
		public boolean getIncludeName(){
				return include_name;
		}
    //
    // setters
    //
    public void setIncludeName(boolean val){
				if(val)
					 include_name = true;
    }
    public void setStart_date(String val){
				if(val != null)
						start_date = val;
    }
    public void setEnd_date(String val){
				if(val != null)
						end_date = val;
    }
		public boolean hasEntries(){
				return entries != null && entries.size() > 0;
		}
    public String toString(){
				return "time active audit";
    }
		public List<List<String>> getEntries(){
				return entries;
		}
		//
		public String find(){
				String back = "";
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String qq = "select distinct(concat_ws(' ',e.first_name,e.last_name)) name,e.email email from time_block_logs l, time_documents d, employees e "+
						" where "+
						" d.employee_id=e.id "+
						" and l.document_id=d.id "+
						" and not (e.email is null or e.email = '')";
				if(!end_date.isEmpty()){
						qq += " and (date(l.action_time) = str_to_date(?,'%m/%d/%Y') or date(l.action_time) = str_to_date(?,'%m/%d/%Y')) ";
				}
				else{
						qq += " and date(l.action_time) = str_to_date(?,'%m/%d/%Y')";						
				}
				if(include_name)
						qq += " order by name ";
				else
						qq += " order by email ";
				con = UnoConnect.getConnection();
				if(con == null){
						back = "Could not connect to DB";
						return back;
				}
				logger.debug(qq);				
				try{

						pstmt = con.prepareStatement(qq);
						if(!end_date.isEmpty()){
								pstmt.setString(1, start_date);
								pstmt.setString(2, end_date);								

						}
						else{
								pstmt.setString(1, start_date);
						}
						rs = pstmt.executeQuery();
						boolean first_row = true;
						while(rs.next()){
								if(first_row){
										List<String> ll = new ArrayList<>();
										if(include_name)
												ll.add("Name");
										ll.add("Email");
										if(entries == null)
												entries = new ArrayList<>();
										entries.add(ll);										
										first_row = false;
								}
								String str = rs.getString(1);
								String str2 = rs.getString(2);
								if(str != null && str2 != null){
										List<String> ll = new ArrayList<>();
										if(include_name)
												ll.add(str);
										ll.add(str2);
										entries.add(ll);
								}
						}
				}
				catch(Exception ex){
						back += ex+":"+qq;
						logger.error(back);
				}
				finally{
						Helper.databaseDisconnect(pstmt, rs);
						UnoConnect.databaseDisconnect(con);
				}
				return back;
		}

}
