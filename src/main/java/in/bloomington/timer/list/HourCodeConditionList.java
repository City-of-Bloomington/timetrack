package in.bloomington.timer.list;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Hashtable;
import java.text.*;
import java.util.Date;
import java.sql.*;
import javax.naming.*;
import javax.naming.directory.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;

public class HourCodeConditionList{

    static final long serialVersionUID = 900L;
    static Logger logger = LogManager.getLogger(HourCodeConditionList.class);
		
    List<HourCodeCondition> conditions = null;
    boolean active_only = false;
    String department_id="", salary_group_id="", hour_code_id="", group_id="";
    public HourCodeConditionList(){
    }
    public List<HourCodeCondition> getConditions(){
	return conditions;
    }
    public void setActiveOnly(){
	active_only = true;
    }
    public void setHour_code_id (String val){
	if(val != null && !val.equals("-1"))
	    hour_code_id = val;
    }
    public void setDepartment_id(String val){
	if(val != null && !val.equals("-1"))
	    department_id = val;
    }
    public void setSalary_group_id(String val){
	if(val != null && !val.equals("-1"))
	    salary_group_id = val;
    }
    public void setGroup_id(String val){
	if(val != null && !val.equals("-1"))
	    group_id = val;
    }		
    //
    // getters
    //
    public String find(){
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="", qw="";
	String qq = "select g.id,g.hour_code_id,g.department_id,g.salary_group_id,g.group_id,date_format(g.date,'%m/%d/%Y'),g.inactive from hour_code_conditions g left join hour_codes e on e.id=g.hour_code_id  ";
	logger.debug(qq);
	if(active_only){
	    qw += " g.inactive is null ";
	}
	if(!department_id.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " (g.department_id = ? or g.department_id is null)";
	}
	if(!salary_group_id.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " (g.salary_group_id = ? or g.salary_group_id is null)";
	}
	if(!group_id.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " (g.group_id = ? or g.group_id is null)";
	}
	if(!hour_code_id.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " g.hour_code_id = ?";
	}				
	if(!qw.isEmpty()){
	    qq += " where "+qw;
	}
	qq += " order by e.name ";
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = " Could not connect to DB ";
	    logger.error(msg);
	    return msg;
	}
	logger.debug(qq);
	try{
	    pstmt = con.prepareStatement(qq);
	    int jj=1;
	    if(!department_id.isEmpty()){
		pstmt.setString(jj++, department_id);
	    }
	    if(!salary_group_id.isEmpty()){
		pstmt.setString(jj++, salary_group_id);								
	    }
	    if(!group_id.isEmpty()){
		pstmt.setString(jj++, group_id);								
	    }
	    if(!hour_code_id.isEmpty()){
		pstmt.setString(jj++, hour_code_id);								
	    }						
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		if(conditions == null)
		    conditions = new ArrayList<>();
		HourCodeCondition one =
		    new HourCodeCondition(
					  rs.getString(1),
					  rs.getString(2),
					  rs.getString(3),
					  rs.getString(4),
					  rs.getString(5),
					  rs.getString(6),
					  rs.getString(7) != null);
		if(!conditions.contains(one))
		    conditions.add(one);
	    }
	}
	catch(Exception ex){
	    msg += " "+ex;
	    logger.error(msg+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(pstmt, rs);
	    UnoConnect.databaseDisconnect(con);
	}
	return msg;
    }

    /**
       // for fire sworn (Black, Gold, Red)(38)
       //
	select distinct h.name
	from hour_code_conditions c left join hour_codes h on h.id=c.hour_code_id  where c.salary_group_id=9 and (c.department_id is null or c.department_id=16)
	and (c.group_id is null or group_id in (268,267,269)) and
	c.inactive is null and h.inactive is null order by h.name
Acting Pay     
 ADLVPAY-BFD    
 BR             
 CA             
 COMMUTE        
 COMMUTE_EXR    
 CU             
 EL_VAX         
 Fire BRV       
 FIRE FLSA      
 FIRE HLDVR     
 FIRE LONG      
 FMLAFS         
 FMLAU          
 FMLAV          
 FMLAWC         
 FRP            
 H1.0           
 HF             
 JD             
 KELLYU         
 MAN UNS DUTY   
 MP             
 MU             
 OJI            
 PENSEC         
 PFMLU          
 REG FIRE       
 REG UNSCH FIRE 
 SBUF           
 SF             
 SF-WC          
 TWB            
 UA             
 UBF            
 UU             
 VU             
 WCA             


//
// group BC (Fire Sworn 5x8 only) (35)
//
	select distinct h.name
	from hour_code_conditions c left join hour_codes h on h.id=c.hour_code_id  where c.salary_group_id=10 and (c.department_id is null or c.department_id=16)
	and (c.group_id is null or group_id in (340)) and
	c.inactive is null and h.inactive is null order by h.name
ADLVPAY-BFD    
 BR             
 CA             
 CE1.0          
 COMMUTE        
 COMMUTE_EXR    
 CU             
 EL_VAX         
 Fire BRV       
 FMLAFS         
 FMLAU          
 FMLAV          
 FMLAVADMIN     
 FMLAWC         
 H1.0           
 HCU            
 HF             
 JD             
 KUADMIN        
 MP             
 OJI            
 ONCALL FIRE    
 PENSEC         
 PFMLU          
 PTO            
 Reg            
 REG FIRE BC    
 REG UNSCH FIRE 
 RETROPAY       
 SBU            
 UA             
 UBF            
 UU             
 VU             
 WCA            



       //
// group BC (Fire Sworn only) (38)
//
	select distinct h.name
	from hour_code_conditions c left join hour_codes h on h.id=c.hour_code_id  where c.salary_group_id=9 and (c.department_id is null or c.department_id=16)
	and (c.group_id is null or group_id in (340)) and
	c.inactive is null and h.inactive is null order by h.name

 Acting Pay     
 ADLVPAY-BFD    
 BR             
 CA             
 CE1.0          
 COMMUTE        
 COMMUTE_EXR    
 CU             
 EL_VAX         
 Fire BRV       
 FIRE FLSA      
 FIRE HLDVR     
 FIRE LONG      
 FMLAFS         
 FMLAU          
 FMLAV          
 FMLAWC         
 FRP            
 H1.0           
 HF             
 JD             
 KELLYU         
 MP             
 MU             
 OJI            
 PENSEC         
 PFMLU          
 REG FIRE       
 REG UNSCH FIRE 
 SBUF           
 SF             
 SF-WC          
 TWB            
 UA             
 UBF            
 UU             
 VU             
 WCA            


// Admin group (exempt) (47)
	select distinct h.name
	from hour_code_conditions c left join hour_codes h on h.id=c.hour_code_id  where c.salary_group_id=1 and (c.department_id is null or c.department_id=16)
	and (c.group_id is null or group_id in (270)) and
	c.inactive is null and h.inactive is null order by h.name

 Acting Pay       
 ADLVPAY-BFD      
 BR               
 BWP              
 COMMUTE          
 COMMUTE_EXR      
 CU               
 EL_VAX           
 Fire BRVADMIN    
 FMCU             
 FMLAFS           
 FMLAFSADMIN      
 FMLAH            
 FMLAU            
 FMLAV            
 FMLAWC           
 FMPTO            
 FMSCK            
 H1.0             
 HCU              
 HF               
 JD               
 KUADMIN          
 MU               
 OJI              
 ONCALL FIRE      
 OT1.5 FIRE ADMIN 
 PFMLU            
 PTO              
 PTOUN            
 Reg              
 SBU              
 SBUF             
 SBUUN            
 SF               
 SF-WC            
 SF-WC-ADMIN      
 SFADMIN          
 TWB              
 UA               
 UBADMIN          
 UBF              
 UU               
 VU               
 VUADMIN          
 WC               
 WCA               


	
// Admin group (non-exempt) (42)
	select distinct h.name
	from hour_code_conditions c left join hour_codes h on h.id=c.hour_code_id  where c.salary_group_id=2 and (c.department_id is null or c.department_id=16)
	and (c.group_id is null or group_id in (270)) and
	c.inactive is null and h.inactive is null order by h.name
	
 Acting Pay       
 ADLVPAY-BFD      
 BR               
 BWP              
 COMMUTE          
 COMMUTE_EXR      
 CU               
 EL_VAX           
 FMCU             
 FMLAFS           
 FMLAH            
 FMLAU            
 FMLAV            
 FMLAWC           
 FMPTO            
 FMSCK            
 H1.0             
 HCU              
 HF               
 JD               
 MP               
 MU               
 OJI              
 ONCALL FIRE      
 OT1.5 FIRE ADMIN 
 PFMLU            
 PTO              
 PTOUN            
 Reg              
 SBU              
 SBUUN            
 SF               
 TWB              
 UA               
 UBF              
 UU               
 VU               
 WC               
 WCA              


 // group Community Engagement (Sworn 5x8) (34)
	select distinct h.name
	from hour_code_conditions c left join hour_codes h on h.id=c.hour_code_id  where c.salary_group_id=10 and (c.department_id is null or c.department_id=16)
	and (c.group_id is null or group_id in (332)) and
	c.inactive is null and h.inactive is null order by h.name	
 ADLVPAY-BFD      
 BR               
 CA               
 COMMUTE          
 COMMUTE_EXR      
 CU               
 EL_VAX           
 Fire BRV         
 FMLAFS           
 FMLAU            
 FMLAV            
 FMLAVADMIN       
 FMLAWC           
 H1.0             
 HCU              
 JD               
 KUADMIN          
 MP               
 OJI              
 ONCALL FIRE      
 OT1.5 STORM 6.25 
 PENSEC           
 PFMLU            
 PTO              
 Reg              
 REG UNSCH FIRE   
 RETROPAY         
 SBU              
 STORM 6.25.24    
 UA               
 UBF              
 UU               
 VU               
 WCA              

 // Group Deputy Chief (Sworn 5x8)(32)
 	select distinct h.name
	from hour_code_conditions c left join hour_codes h on h.id=c.hour_code_id  where c.salary_group_id=10 and (c.department_id is null or c.department_id=16)
	and (c.group_id is null or group_id in (339)) and
	c.inactive is null and h.inactive is null order by h.name

	ADLVPAY-BFD         
 BR               
 CA               
 COMMUTE          
 COMMUTE_EXR      
 CU               
 EL_VAX           
 Fire BRV         
 FMLAFS           
 FMLAU            
 FMLAV            
 FMLAVADMIN       
 FMLAWC           
 H1.0             
 HCU              
 JD               
 KUADMIN          
 MP               
 OJI              
 ONCALL FIRE      
 PENSEC           
 PFMLU            
 PTO              
 Reg              
 REG UNSCH FIRE   
 RETROPAY         
 SBU              
 UA               
 UBF              
 UU               
 VU               
 WCA

 // Group Director(Exempt) (47)
  	select distinct h.name
	from hour_code_conditions c left join hour_codes h on h.id=c.hour_code_id  where c.salary_group_id=1 and (c.department_id is null or c.department_id=16)
	and (c.group_id is null or group_id in (302)) and
	c.inactive is null and h.inactive is null order by h.name
  Acting Pay       
 ADLVPAY-BFD      
 BR               
 BWP              
 COMMUTE          
 COMMUTE_EXR      
 CU               
 EL_VAX           
 Fire BRVADMIN    
 FMCU             
 FMLAFS           
 FMLAFSADMIN      
 FMLAH            
 FMLAU            
 FMLAV            
 FMLAWC           
 FMPTO            
 FMSCK            
 H1.0             
 HCU              
 HF               
 JD               
 KUADMIN          
 MU               
 OJI              
 ONCALL FIRE      
 OT1.5 FIRE ADMIN 
 PFMLU            
 PTO              
 PTOUN            
 Reg              
 SBU              
 SBUF             
 SBUUN            
 SF               
 SF-WC            
 SF-WC-ADMIN      
 SFADMIN          
 TWB              
 UA               
 UBADMIN          
 UBF              
 UU               
 VU               
 VUADMIN          
 WC               
 WCA

 // Group Logistics (Sworn) (38)
   	select distinct h.name
	from hour_code_conditions c left join hour_codes h on h.id=c.hour_code_id  where c.salary_group_id=9 and (c.department_id is null or c.department_id=16)
	and (c.group_id is null or group_id in (265)) and
	c.inactive is null and h.inactive is null order by h.name

	 Acting Pay     
 ADLVPAY-BFD    
 BCSI           
 BR             
 CA             
 COMMUTE        
 COMMUTE_EXR    
 CU             
 EL_VAX         
 Fire BRV       
 FIRE FLSA      
 FIRE HLDVR     
 FIRE LONG      
 FMLAFS         
 FMLAU          
 FMLAV          
 FMLAWC         
 FRP            
 H1.0           
 HF             
 JD             
 KELLYU         
 MP             
 MU             
 OJI            
 PENSEC         
 PFMLU          
 REG FIRE       
 REG UNSCH FIRE 
 SBUF           
 SF             
 SF-WC          
 TWB            
 UA             
 UBF            
 UU             
 VU             
 WCA            

 Group: MIH (non-exempt)(39)
   	select distinct h.name
	from hour_code_conditions c left join hour_codes h on h.id=c.hour_code_id  where c.salary_group_id=2 and (c.department_id is null or c.department_id=16)
	and (c.group_id is null or group_id in (266)) and
	c.inactive is null and h.inactive is null order by h.name

 Acting Pay       
 ADLVPAY-BFD      
 BR               
 BWP              
 COMMUTE          
 COMMUTE_EXR      
 CU               
 EL_VAX           
 FMCU             
 FMLAFS           
 FMLAH            
 FMLAU            
 FMLAV            
 FMLAWC           
 FMPTO            
 FMSCK            
 H1.0             
 HCU              
 HF               
 JD               
 MP               
 MU               
 OJI              
 ONCALL FIRE      
 OT1.5 FIRE ADMIN 
 PFMLU            
 PTO              
 PTOUN            
 Reg              
 SBU              
 SBUUN            
 SF               
 TWB              
 UA               
 UBF              
 UU               
 VU               
 WC               
 WCA               
	
// Group: MIH Supervisor (Exempt)(47)
   	select distinct h.name
	from hour_code_conditions c left join hour_codes h on h.id=c.hour_code_id  where c.salary_group_id=1 and (c.department_id is null or c.department_id=16)
	and (c.group_id is null or group_id in (342)) and
	c.inactive is null and h.inactive is null order by h.name

	 Acting Pay       
 ADLVPAY-BFD      
 BR               
 BWP              
 COMMUTE          
 COMMUTE_EXR      
 CU               
 EL_VAX           
 Fire BRVADMIN    
 FMCU             
 FMLAFS           
 FMLAFSADMIN      
 FMLAH            
 FMLAU            
 FMLAV            
 FMLAWC           
 FMPTO            
 FMSCK            
 H1.0             
 HCU              
 HF               
 JD               
 KUADMIN          
 MU               
 OJI              
 ONCALL FIRE      
 OT1.5 FIRE ADMIN 
 PFMLU            
 PTO              
 PTOUN            
 Reg              
 SBU              
 SBUF             
 SBUUN            
 SF               
 SF-WC            
 SF-WC-ADMIN      
 SFADMIN          
 TWB              
 UA               
 UBADMIN          
 UBF              
 UU               
 VU               
 VUADMIN          
 WC               
 WCA

 // Group: Prevention (Non-exempt)(38)
    	select distinct h.name
	from hour_code_conditions c left join hour_codes h on h.id=c.hour_code_id  where c.salary_group_id=2 and (c.department_id is null or c.department_id=16)
	and (c.group_id is null or group_id in (385)) and
	c.inactive is null and h.inactive is null order by h.name
Acting Pay  
 ADLVPAY-BFD 
 BR          
 BWP         
 COMMUTE     
 COMMUTE_EXR 
 CU          
 EL_VAX      
 FMCU        
 FMLAFS      
 FMLAH       
 FMLAU       
 FMLAV       
 FMLAWC      
 FMPTO       
 FMSCK       
 H1.0        
 HCU         
 HF          
 JD          
 MP          
 MU          
 OJI         
 ONCALL FIRE 
 PFMLU       
 PTO         
 PTOUN       
 Reg         
 SBU         
 SBUUN       
 SF          
 TWB         
 UA          
 UBF         
 UU          
 VU          
 WC          
 WCA         

// Group; Preventive Manager(non-exempt)(38)
    	select distinct h.name
	from hour_code_conditions c left join hour_codes h on h.id=c.hour_code_id  where c.salary_group_id=2 and (c.department_id is null or c.department_id=16)
	and (c.group_id is null or group_id in (344)) and
	c.inactive is null and h.inactive is null order by h.name
 
 Acting Pay  
 ADLVPAY-BFD 
 BR          
 BWP         
 COMMUTE     
 COMMUTE_EXR 
 CU          
 EL_VAX      
 FMCU        
 FMLAFS      
 FMLAH       
 FMLAU       
 FMLAV       
 FMLAWC      
 FMPTO       
 FMSCK       
 H1.0        
 HCU         
 HF          
 JD          
 MP          
 MU          
 OJI         
 ONCALL FIRE 
 PFMLU       
 PTO         
 PTOUN       
 Reg         
 SBU         
 SBUUN       
 SF          
 TWB         
 UA          
 UBF         
 UU          
 VU          
 WC          
 WCA         

 Group: Training (Sworn)  ( )
    	select distinct h.name
	from hour_code_conditions c left join hour_codes h on h.id=c.hour_code_id  where c.salary_group_id=9 and (c.department_id is null or c.department_id=16)
	and (c.group_id is null or group_id in (274)) and
	c.inactive is null and h.inactive is null order by h.name

  Acting Pay     
 ADLVPAY-BFD    
 BR             
 CA             
 COMMUTE        
 COMMUTE_EXR    
 CU             
 EL_VAX         
 Fire BRV       
 FIRE FLSA      
 FIRE HLDVR     
 FIRE LONG      
 FMLAFS         
 FMLAU          
 FMLAV          
 FMLAWC         
 FRP            
 H1.0           
 HF             
 JD             
 KELLYU         
 MP             
 MU             
 OJI            
 PENSEC         
 PFMLU          
 REG FIRE       
 REG UNSCH FIRE 
 SBUF           
 SF             
 SF-WC          
 TWB            
 UA             
 UBF            
 UU             
 VU             
 WCA             

     */
}
