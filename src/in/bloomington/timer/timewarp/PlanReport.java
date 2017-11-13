package in.bloomington.timer.timewarp;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.ArrayList;
import java.util.*;
import java.sql.*;
import java.text.*;
import org.apache.log4j.Logger;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;
import in.bloomington.timer.list.*;

public class PlanReport extends MpoReport{

		static Logger logger = Logger.getLogger(PlanReport.class);
    public PlanReport(){
				super();
				dept="Planning & Transportation";
				department_id="7";  
				dept_ref_id="25,31"; // planning & engineering
				code="%_MP_%";
    }	
		
}
