package in.bloomington.timer.list;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.text.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;

public class DocumentList{

		static final long serialVersionUID = 1600L;
		static Logger logger = LogManager.getLogger(DocumentList.class);
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");		
		String employee_id = "", department_id="", pay_period_id="",
				date="", job_id="";
		Set<String> group_id_set = new HashSet<>();
		String group_ids="";// for multiple groups
		List<Document> documents = null;
    public DocumentList(){
    }
    public DocumentList(String val){
				setEmployee_id(val);
    }		
    public void setEmployee_id (String val){
				if(val != null && !val.equals("-1"))
						employee_id = val;
    }
    public void setPay_period_id (String val){
				if(val != null && !val.equals("-1"))
						pay_period_id = val;
    }		
    public void setDepartment_id (String val){
				if(val != null && !val.equals("-1"))
						department_id = val;
    }
    public void setJob_id (String val){
				if(val != null && !val.equals("-1"))
						job_id = val;
    }		
    public void setGroup_id (String val){
				if(val != null && !val.equals("-1")){
						if(!group_id_set.contains(val)){
								if(!group_ids.equals(""))
										group_ids += ",";
								group_ids += val;
								group_id_set.add(val);
						}
				}
    }
    public void setDate(String val){
				if(val != null)
					 date = val;
    }		
		public List<Document> getDocuments(){
				return documents;
		}
    //
    // getters
    //
		public String find(){
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String msg="", str="";
				String qq = "select d.id,d.employee_id,d.pay_period_id,d.job_id,date_format(d.initiated,'%m/%d/%Y %H:%i'),d.initiated_by from time_documents d, employees e ";
				String qw = "d.employee_id=e.id ";
				if(!employee_id.equals("")){
						if(!qw.equals("")) qw += " and ";						
						qw += "d.employee_id=? ";
				}
				if(!pay_period_id.equals("")){
						if(!qw.equals("")) qw += " and ";						
						qw += "d.pay_period_id=? ";
				}
				if(!job_id.equals("")){
						if(!qw.equals("")) qw += " and ";						
						qw += "d.job_id=? ";
				}				
				if(!date.equals("")){
						qq += " join pay_periods pp on pp.id=d.pay_period_id ";
						if(!qw.equals("")) qw += " and ";						
						qw += "pp.start_date <=? and pp.end_date >= ?";						
				}
				if(!department_id.equals("")){
						qq += ", groups g, group_employees ge ";
						if(!qw.equals("")) qw += " and ";						
						qw += "g.department_id=? and g.id=ge.group_id and ge.employee_id=d.employee_id ";
				}
				if(!group_ids.equals("")){
						qq += ", jobs j";
						if(!qw.equals("")) qw += " and ";									
						qw += "d.job_id=j.id and j.group_id in ("+group_ids+")";
				}
				if(!qw.equals("")){
						qq += " where "+qw;
				}
				qq += " order by e.last_name,e.first_name ";
				con = Helper.getConnection();
				if(con == null){
						msg = " Could not connect to DB ";
						logger.error(msg);
						return msg;
				}
				logger.debug(qq);
				try{
						pstmt = con.prepareStatement(qq);
						int jj=1;
						if(!employee_id.equals("")){
								pstmt.setString(jj++, employee_id);
						}
						if(!pay_period_id.equals("")){
								pstmt.setString(jj++, pay_period_id);
						}
						if(!job_id.equals("")){
								pstmt.setString(jj++, job_id);
						}						
						if(!date.equals("")){
								java.util.Date date_tmp = df.parse(date);
								pstmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
								pstmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
						}
						if(!department_id.equals("")){
								pstmt.setString(jj++, department_id);
						}
						rs = pstmt.executeQuery();
						while(rs.next()){
								if(documents == null)
										documents = new ArrayList<>();
							 Document one = new Document(
																					 rs.getString(1),
																					 rs.getString(2),
																					 rs.getString(3),
																					 rs.getString(4),
																					 rs.getString(5),
																					 rs.getString(6));
							 if(!documents.contains(one))
									 documents.add(one);
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
