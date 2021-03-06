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

public class TimeNoteList{

    static Logger logger = LogManager.getLogger(TimeNoteList.class);
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
				Connection con = UnoConnect.getConnection();
				String qq = "select t.id,t.document_id,t.reported_by,date_format(t.date,'%m/%d/%y %H:%i'),notes from time_notes t ";
				if(con == null){
						back = "Could not connect to DB";
						return back;
				}
				String qw = "";
				try{
						if(!document_id.isEmpty()){
								if(!qw.isEmpty()) qw += " and ";
								qw += " t.document_id = ? ";
						}
						if(!qw.isEmpty()){
								qq += " where "+qw;
						}
						if(!sortBy.isEmpty()){
								qq += " order by "+sortBy;
						}
						logger.debug(qq);
						pstmt = con.prepareStatement(qq);
						if(!document_id.isEmpty()){
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
						Helper.databaseDisconnect(pstmt, rs);
						UnoConnect.databaseDisconnect(con);
				}
				return back;
    }
}






















































