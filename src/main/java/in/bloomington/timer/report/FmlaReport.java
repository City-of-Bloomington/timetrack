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
    static final long serialVersionUID = 3820L;	
    public FmlaReport(){
	super();
	code="%FM%"; // FML, HFML
	setIgnoreProfiles();
    }
    public void setDept_ref_id(String val){
	if(val != null)
	    dept_ref_id = val;
    }
		
}
/**
	 //
	 // FMLA report from clocker since 01/01/2009
	 //
	 select u.fullname fullname,t.dt date,c.name,c.nws_name,(out_hour+out_minute/60) - (in_hour+in_minute/60) from timeinterval t join categories c on t.category_id=c.id join users u on u.id = t.user_id where c.name like 'FMLA%' and dt >='2009-01-01' order by fullname,date INTO OUTFILE '/var/lib/mysql-files/fmla_times.csv' FIELDS ENCLOSED BY '"' TERMINATED BY ',' ESCAPED BY '"' LINES TERMINATED BY '\r\n';

	 // from 07/20/2019
	 	 select u.fullname fullname,t.dt date,c.name,c.nws_name,(out_hour+out_minute/60) - (in_hour+in_minute/60) from timeinterval t join categories c on t.category_id=c.id join users u on u.id = t.user_id where c.name like 'FMLA%' and dt >='2019-07-20' order by fullname,date INTO OUTFILE '/var/lib/mysql-files/fmla_times2.csv' FIELDS ENCLOSED BY '"' TERMINATED BY ',' ESCAPED BY '"' LINES TERMINATED BY '\r\n';

	 

 */
