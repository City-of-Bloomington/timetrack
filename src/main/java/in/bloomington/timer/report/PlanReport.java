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

public class PlanReport extends MpoReport{

    static final long serialVersionUID = 3830L;	
    static Logger logger = LogManager.getLogger(PlanReport.class);
    public PlanReport(){
	super();
	dept="Planning & Transportation";
	department_id="7";  
	dept_ref_id="31"; // planning
	code="%\\_MP\\_%";
    }	
		
}
