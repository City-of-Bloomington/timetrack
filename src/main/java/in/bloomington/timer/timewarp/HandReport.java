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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;
import in.bloomington.timer.list.*;

public class HandReport extends MpoReport{

		static Logger logger = LogManager.getLogger(HandReport.class);
    public HandReport(){
				super();
				dept="HAND";
				department_id="3";  
				dept_ref_id="27"; // HAND 27
				code="%_REG";
				code2="TEMP_%";
    }	
		
}
