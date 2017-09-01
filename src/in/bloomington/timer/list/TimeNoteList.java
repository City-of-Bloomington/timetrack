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

public class TimeNoteList{

		static Logger logger = Logger.getLogger(TimeNoteList.class);
		static final long serialVersionUID = 3800L;
		String document_id="", sortBy="t.date"; 
		List<TimeNote> timeNotes = null;
	
		public TimeNoteList(){
		}
		public TimeNoteList(String val){
				setDocument_id(val);
		}
		public List<TimeNote> getTimeNotes(){
				return timeNotes;
		}
		
		public void setDocument_id(String val){
				if(val != null)
						document_id = val;
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
				String qq = "select t.id,t.document_id,t.reported_by,date_format(t.date,'%m/%d/%Y %H:%i'),notes from time_notes t ";
				if(con == null){
						back = "Could not connect to DB";
						return back;
				}
				String qw = "";
				try{
						if(!document_id.equals("")){
								if(!qw.equals("")) qw += " and ";
								qw += " t.document_id = ? ";
						}
						if(!qw.equals("")){
								qq += " where "+qw;
						}
						if(!sortBy.equals("")){
								qq += " order by "+sortBy;
						}
						logger.debug(qq);
						pstmt = con.prepareStatement(qq);
						if(!document_id.equals("")){
								pstmt.setString(1, document_id);
						}						
						rs = pstmt.executeQuery();
						if(timeNotes == null)
								timeNotes = new ArrayList<TimeNote>();
						while(rs.next()){
								TimeNote one =
										new TimeNote(rs.getString(1),
																 rs.getString(2),
																 rs.getString(3),
																 rs.getString(4),
																 rs.getString(5));
								if(!timeNotes.contains(one))
										timeNotes.add(one);
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






















































