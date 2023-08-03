package in.bloomington.timer.report;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.ArrayList;
import java.util.*;
import java.sql.*;
import java.text.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;
import in.bloomington.timer.list.*;

public class ElReport extends MpoReport{

    //
    // EL_ emergency earn codes report
    //
    static Logger logger = LogManager.getLogger(ElReport.class);
    static final long serialVersionUID = 3820L;	
    Department department = null;
    public ElReport(){
				super();
				code="EL%"; // we need to escape _ because it is one
				//              character wild card
				setIgnoreProfiles();
    }
    public void setDepartment_id(String val){
				if(val != null && !val.equals("-1")){
						department_id = val;
						getDepartment(); 
				}
    }
    public void setDept_ref_id(String val){
				if(val != null)
						dept_ref_id = val;
    }
    public String getDepartment_id(){
				if(department_id.isEmpty())
						return "-1";
				return department_id;
    }
    public Department getDepartment(){
				if(department == null && !department_id.isEmpty()){
						Department one = new Department(department_id);
						String back = one.doSelect();
						if(back.isEmpty()){
								department = one;
								dept_ref_id = one.getRef_id();
						}
				}
				return department;
    }
    public boolean hasDepartment(){
				getDepartment();
				return department != null;
    }
		
}

