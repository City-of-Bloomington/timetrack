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

public class FmlaReport extends MpoReport{

		static Logger logger = LogManager.getLogger(PlanReport.class);
		Department department = null;
    public FmlaReport(){
				super();
				code="%FM%"; // FML, HFML
				setIgnoreProfiles();
    }
		public void setDepartment_id(String val){
				if(val != null){
						department_id = val;
						getDepartment(); // we need ref id
				}
		}
		public void setDept_ref_id(String val){
				if(val != null)
						dept_ref_id = val;
		}
		public String getDepartment_id(){
				if(department_id.equals(""))
						return "-1";
				return department_id;
		}
		public Department getDepartment(){
				if(department == null && !department_id.equals("")){
						Department one = new Department(department_id);
						String back = one.doSelect();
						if(back.equals("")){
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
