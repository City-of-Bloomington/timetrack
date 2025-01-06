package in.bloomington.timer.action;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.*;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;  
import in.bloomington.timer.list.*;
import in.bloomington.timer.bean.*;
import in.bloomington.timer.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EmpJobsUpdateAction extends TopAction{

    static final long serialVersionUID = 3800L;	
    static Logger logger = LogManager.getLogger(EmpJobsUpdateAction.class);
    EmployeeJobsHandel handel = null;
    String date = Helper.getToday();
    public String execute(){
	String ret = SUCCESS;
	String back = "";
	if(!action.isEmpty()){
	    getHandel();
	    handel.setDate(date);
	    back = handel.find();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		addMessage("Success");
	    }
	}				
	return ret;
    }
    public EmployeeJobsHandel getHandel(){
	if(handel == null){
	    handel = new EmployeeJobsHandel();
	}
	return handel;
    }
    public void setDate(String val){
	if(val != null)
	    date = val;
    }
    public String getDate(){
	if(date.isEmpty()){
	    date = Helper.getToday();
	}
	return date;
    }
    
}
/**
uNON-U RFTx - Utilities Non-Union Reg FT Ex
TEMP  - Temporary Employees
uNON-U RFTnx - Utilities Non-Union Reg FT NonEx
FIRE SWORN - Firefighter Sworn
uAFSCME RFT - Utilities AFSCME RFT 80 Hours
NON-U RFULLnx - Non-Union Regular FT Non-Exempt
AFSCME RFT - AFSCME-80 Hours
NON-U RFULLx - Non-Union Regular FT Exempt
BOARD - Board pd members (USB, BPW, BPS)
CEDC 5/2 - Central Dispatch 5 on 2 off
POLICE SWORN  - Sworn Police Officers
CEDC4/2 - Central Dispatch 4 on 2 off
POLICE SWORN MGT - Police Sworn Management
NON-U RPARTnx - Non-Union Regular PT Non-Exempt
ELECTED  - Elected Employees
uNON-U RPARTnx - Utilities Non-Union Reg PT NonEx
POLICE SWORN DET - Police Sworn Detective
uTEMP - Utilities Temporary Employee
COUNCIL MEM - Council Members
FIRE SWORN 5X8 - Firefighter Sworn 5x8
TEMP W/BEN - Temporary Employee With Benefits

    create table benefit_salary_ref(
    id int unsigned not null auto_increment,
    benefit_name varchar(80),
    salary_group_id int unsigned,
    primary key(id),
    foreign key(salary_group_id) references salary_groups(id)
    )engine=InnoDB;
insert into benefit_salary_ref values
(0,'uNON-U RFTx - Utilities Non-Union Reg FT Ex',1),
(0,'TEMP  - Temporary Employees',3),
(0,'uNON-U RFTnx - Utilities Non-Union Reg FT NonEx',2),
(0,'FIRE SWORN - Firefighter Sworn',9),
(0,'uAFSCME RFT - Utilities AFSCME RFT 80 Hours',4),
(0,'NON-U RFULLnx - Non-Union Regular FT Non-Exempt',2),
(0,'AFSCME RFT - AFSCME-80 Hours',4),
(0,'NON-U RFULLx - Non-Union Regular FT Exempt',1),
(0,'BOARD - Board pd members (USB, BPW, BPS)',null),
(0,'CEDC 5/2 - Central Dispatch 5 on 2 off',1),
(0,'POLICE SWORN  - Sworn Police Officers',6),
(0,'CEDC4/2 - Central Dispatch 4 on 2 off',2),
(0,'POLICE SWORN MGT - Police Sworn Management',8),
(0,'NON-U RPARTnx - Non-Union Regular PT Non-Exempt',11),
(0,'ELECTED  - Elected Employees',null),
(0,'uNON-U RPARTnx - Utilities Non-Union Reg PT NonEx',11),
(0,'POLICE SWORN DET - Police Sworn Detective',7),
(0,'uTEMP - Utilities Temporary Employee',3),
(0,'COUNCIL MEM - Council Members',null),
(0,'FIRE SWORN 5X8 - Firefighter Sworn 5x8',10),
(0,'TEMP W/BEN - Temporary Employee With Benefits',12)
    

create table grade_comp_hours(
    id int unsigned not null auto_increment,
    grade_name varchar(32),
    comp_week_hours int unsigned,
    primary key(id)
    )engine=InnoDB;
    
insert into grade_comp_hours values(0,'Grade 04',40),
(0,'Grade 05',40),
(0,'Grade 06',40),
(0,'Grade 07',45),
(0,'Grade 08',45),
(0,'Grade 09',45),
(0,'Grade 10',45),
(0,'Grade 11',45),
(0,'Grade 12',50),
(0,'Grade 13',50),
(0,'Grade 14',50)


 */




































