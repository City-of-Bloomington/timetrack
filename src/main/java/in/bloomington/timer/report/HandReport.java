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
/*
	HAND report
	
				select tt.name,tt.empnum,tt.date,tt.code,sum(hours)                                  from (select                                                                    concat_ws(' ',e.first_name,e.last_name) AS name,                                e.employee_number as empnum,                                                    t.date AS date,                                                                 c.name AS code, 								                                                 t.hours AS hours                                                                from time_blocks t                                                              join hour_codes c on t.hour_code_id=c.id 						                           join time_documents d on d.id=t.document_id                                     join pay_periods p on p.id=d.pay_period_id                                      join department_employees de on de.employee_id=d.employee_id                    join employees e on d.employee_id=e.id                                          where t.inactive is null                                                        and de.department_id = 3                                                        and p.start_date >= str_to_date('01/01/2019','%m/%d/%Y')                        and p.end_date <= str_to_date('12/31/2019','%m/%d/%Y')                          and (c.name like '%_REG' or c.name like 'TEMP_%' or c.name like 'Reg' or c.name like 'PTO')) tt                                                                 group by tt.code,tt.name,tt.empnum,tt.date                                      INTO OUTFILE '/var/lib/mysql-files/hand_report.csv'                             FIELDS TERMINATED BY ','                                                        ENCLOSED BY '"'                                                                 LINES TERMINATED BY '\n';
				
			
*/
