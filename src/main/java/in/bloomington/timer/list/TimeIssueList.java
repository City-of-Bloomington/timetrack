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

public class TimeIssueList{

    static Logger logger = LogManager.getLogger(TimeIssueList.class);
    static final long serialVersionUID = 3800L;
    String time_block_id="", document_id="", sortBy="t.date";
    String status="";
    List<TimeIssue> timeIssues = null;
	
    public TimeIssueList(){
    }
    public TimeIssueList(String val){
				setDocument_id(val);
    }
    public List<TimeIssue> getTimeIssues(){
				return timeIssues;
    }
    public void setTime_block_id(String val){
				if(val != null)
						time_block_id = val;
    }		
    public void setDocument_id(String val){
				if(val != null)
						document_id = val;
    }
    public void setStatus(String val){
				if(val != null && !val.equals("-1"))
						status = val;
    }
    public void setSortBy(String val){
				if(val != null)
						sortBy = val;
    }
    public void setOpenOnly(){
				status = "Open";
    }
    public String find(){
		
				String back = "";
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				Connection con = UnoConnect.getConnection();
				String qq = "select i.id,i.time_block_id,i.reported_by,date_format(i.date,'%m/%d/%Y %H:%i'),i.issue_notes,i.status,date_format(i.closed_date,'%m/%d/%y %H:%i'),i.closed_by from time_issues i ";
				if(con == null){
						back = "Could not connect to DB";
						return back;
				}
				String qw = "";
				try{
						if(!time_block_id.isEmpty()){
								if(!qw.isEmpty()) qw += " and ";
								qw += " i.time_block_id = ? ";
						}						
						if(!document_id.isEmpty()){
								qq += " join time_blocks t on t.id=i.time_block_id ";
								if(!qw.isEmpty()) qw += " and ";
								qw += " t.document_id = ? ";
						}
						if(!status.isEmpty()){
								if(!qw.isEmpty()) qw += " and ";
								qw += " i.status = ? ";
						}
						if(!qw.isEmpty()){
								qq += " where "+qw;
						}
						if(!sortBy.isEmpty()){
								qq += " order by "+sortBy;
						}
						logger.debug(qq);
						pstmt = con.prepareStatement(qq);
						int jj = 1;
						if(!time_block_id.isEmpty()){
								pstmt.setString(jj++, time_block_id);
						}
						if(!document_id.isEmpty()){
								pstmt.setString(jj++, document_id);
						}
						if(!status.isEmpty()){
								pstmt.setString(jj++, status);
						}
						rs = pstmt.executeQuery();
						if(timeIssues == null)
								timeIssues = new ArrayList<>();
						while(rs.next()){
								TimeIssue one =
										new TimeIssue(rs.getString(1),
																	rs.getString(2),
																	rs.getString(3),
																	rs.getString(4),
																	rs.getString(5),
																	rs.getString(6),
																	rs.getString(7),
																	rs.getString(8)
																	);
								if(!timeIssues.contains(one))
										timeIssues.add(one);
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






















































