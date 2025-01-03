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

public class GradeCompHoursList{

    static Logger logger = LogManager.getLogger(GradeCompHoursList.class);
    static final long serialVersionUID = 3800L;
    String grade_name = "";
    List<GradeCompHours> gradeHours = null;
	
    public GradeCompHoursList(){
    }
    public List<GradeCompHours> getGradeHours(){
	return gradeHours;
    }

    public void setGradeName(String val){
	String grade_name = "";
	if(val != null)
	    grade_name = val.trim();
	
    }
    public GradeCompHours findHoursFor(String grade_name){
	GradeCompHours gradeHour = null;
	if(gradeHours == null){
	    find();
	}
	if(gradeHours != null && gradeHours.size() > 0){
	    for(GradeCompHours one:gradeHours){
		if(one.getGrade().equals(grade_name)){
		    gradeHour = one;
		    break;
		}
	    }
	}
	return gradeHour;
    }

    public String find(){
		
	String back = "";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection con = UnoConnect.getConnection();
	String qq = "select t.id,t.grade_name,t.comp_week_hours from grade_comp_hours t ";
	if(con == null){
	    back = "Could not connect to DB";
	    return back;
	}
	String qw = "";
	try{
	    if(!grade_name.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";
		qw += " t.grade_name like ? ";
	    }
	    if(!qw.isEmpty()){
		qq += " where "+qw;
	    }
	    logger.debug(qq);
	    pstmt = con.prepareStatement(qq);
	    if(!grade_name.isEmpty()){
		pstmt.setString(1,"%"+grade_name+"%");
	    }

	    rs = pstmt.executeQuery();
	    if(gradeHours == null)
		gradeHours = new ArrayList<>();
	    while(rs.next()){
		GradeCompHours one =
		    new GradeCompHours(rs.getString(1),
				       rs.getString(2),
				       rs.getInt(3));
		if(!gradeHours.contains(one))
		    gradeHours.add(one);
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






















































