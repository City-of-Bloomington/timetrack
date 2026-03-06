package in.bloomington.timer.list;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.Map;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.*;
import java.util.Date;
import java.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;

public class DailyBlockList{

    static final long serialVersionUID = 3700L;
    static Logger logger = LogManager.getLogger(DailyBlockList.class);
    String sortBy="full_name,time_date ";
    String department_id="", pay_period_id="", group_id="", salary_group_id="";
    PayPeriod payPeriod = null;
    List<DailyBlock> dailyBlocks = null;
    List<DailyBlock> earnBlocks = null; 
    Set<String> empNumbers = null;
    Map<String, Map<String, Double>> week1EmpCodes = new TreeMap<>();
    Map<String, Map<String, Double>> week2EmpCodes = new TreeMap<>();
    Map<String, List<DailyBlock>> week1Blocks = new TreeMap<>();
    Map<String, List<DailyBlock>> week2Blocks = new TreeMap<>();	

    //
    // earn codes that timewarp adds
    // CE1.0, CE1.5, CE2.0, HCE1.0, HCE1.5, HCE2.0, OT1.0 OT1.5, OT2.0
    // 71, 34, 45, 50, 79, 46, 78, 43, 44
    final static Set<String> earnCodes;
    static {
	earnCodes = new HashSet<>();
	earnCodes.add("34");
	earnCodes.add("43");
	earnCodes.add("44");
	earnCodes.add("45");
	earnCodes.add("46");
	earnCodes.add("50");
	earnCodes.add("71");
	earnCodes.add("78");
	earnCodes.add("79");
    }
    // regular default codes
    final static Set<String> regCodes;
    static {
	regCodes = new HashSet<>();
	regCodes.add("1"); //Reg 
	regCodes.add("93");//REG Fire
	regCodes.add("117"); //Reg asset
	regCodes.add("36"); // reg_mp_admin
	regCodes.add("37"); // reg_mp_data
	regCodes.add("38");
	regCodes.add("40");
	regCodes.add("39");
	regCodes.add("41");
	regCodes.add("20"); // hand reg codes
	regCodes.add("24");
	regCodes.add("19");
	regCodes.add("21");
	regCodes.add("23");
	regCodes.add("18");
	
    }
    // bpd disp. groups
    final static Set<String> bpdDispGroups;
    static {
	bpdDispGroups = new HashSet<>();
	bpdDispGroups.add("32"); //disp
	bpdDispGroups.add("360");//disp admin
    }    
    public DailyBlockList(){
    }
    public DailyBlockList(String val,
			  String val2){
	setDepartment_id(val);
	setPay_period_id(val2);
    }
    public void setDepartment_id (String val){
	if(val != null)
	    department_id = val;
    }
    public void setGroup_id(String val){
	if(val != null)
	    group_id = val;
    }    
    public void setPay_period_id(String val){
	if(val != null)
	    pay_period_id = val;
    }
    
    public void setSortby(String val){
	if(val != null)
	    sortBy = val;
    }
    public List<DailyBlock> getDailyBlocks(){
	return dailyBlocks;
    }
    public Set<String> getEmpNumbers(){
	return empNumbers;
    }
    public Map<String,Map<String, Double>> getWeek1EmpCodes(){
	return week1EmpCodes;
    }
    public Map<String,Map<String, Double>> getWeek2EmpCodes(){
	return week2EmpCodes;
    }    
    void getPayPeriod(){
	if(payPeriod == null){
	    if(!pay_period_id.isEmpty()){
		PayPeriod one = new PayPeriod(pay_period_id);
		String back = one.doSelect();
		if(back.isEmpty()){
		    payPeriod = one;
		}
		else{
		    logger.error(back);
		}
	    }
	}
    }
    public String doProcess(){
	List<DailyBlock> week1ValidBlocks = new ArrayList<>();
	List<DailyBlock> week2ValidBlocks = new ArrayList<>();
	Map<String, Double> week1EmpSum = new TreeMap<>();
	Map<String, Double> week2EmpSum = new TreeMap<>();
	String back = "";
	find();
	if(!back.isEmpty()){
	    logger.error(back);
	    return back;
	}
	back = find2();
	if(!back.isEmpty()){
	    logger.error(back);
	    return back;
	}
	
	if(earnBlocks != null){
	    adjustEarnBlocks(earnBlocks);
	    for(DailyBlock block:earnBlocks){
		if(block.isValid()){ // avoid 0 values
		    if(block.getDays() == 1){
			week1ValidBlocks.add(block);
			// add to earned sum
			String empNo = block.getEmpNumber();
			double dd = block.getHours();
			addToSum(empNo, dd, week1EmpSum);
		    }
		    else{
			// add to earned sum
			week2ValidBlocks.add(block);
			String empNo = block.getEmpNumber();
			double dd = block.getHours();
			addToSum(empNo, dd, week2EmpSum);			
		    }
		}
	    }
	}
	//
	// now we need to remove the excess hours from regular hours
	//
	if(week1EmpSum.size() > 0){
	    for(String empNo:week1EmpSum.keySet()){
		double hours = week1EmpSum.get(empNo);
		adjustDialyBocks(1, empNo, hours);
	    }
	}
	if(week2EmpSum.size() > 0){
	    for(String empNo:week2EmpSum.keySet()){
		double hours = week2EmpSum.get(empNo);
		adjustDialyBocks(2, empNo, hours);
	    }
	}
	
	if(week1ValidBlocks != null && week1ValidBlocks.size() > 0)
	    dailyBlocks.addAll(week1ValidBlocks);
	if(week2ValidBlocks != null && week2ValidBlocks.size() > 0)
	    dailyBlocks.addAll(week2ValidBlocks);		
	return back;
    }
    void adjustDialyBocks(int week_no, String empNo, double hrs2){
	double earn_hrs = hrs2;
	DailyBlock stblock = null; // last block
	List<DailyBlock> list = null;
	if(week_no == 1){
	    if(week1Blocks.containsKey(empNo)){
		list = week1Blocks.get(empNo);
	    }
	}
	else {
	    if(week2Blocks.containsKey(empNo)){
		list = week2Blocks.get(empNo);
	    }
	}
	int cnt = 0;
	if(list != null){
	    for(DailyBlock block:list){
		int daily_hours = block.getDailyHours();
		if(block.getHours() > 2){
		    cnt++;
		}
		if(block.getHours() > daily_hours){
		    double hrs = block.getHours();
		    double dif = hrs - daily_hours;
		    if(earn_hrs >= dif ){
			earn_hrs = earn_hrs - dif;
			hrs = 8;
		    }
		    else{
			hrs = hrs - earn_hrs;
			earn_hrs = 0;
		    }
		    BigDecimal bd = new BigDecimal(Double.toString(hrs)); 
		    bd = bd.setScale(2, RoundingMode.HALF_UP); 
		    hrs = bd.doubleValue();	
		    block.setHours(hrs);
		}
		if(earn_hrs <= 0.001){
		    return;
		}	    	    
	    }
	    if(earn_hrs > 0.001 && cnt > 0){
		double dd = earn_hrs/(cnt+0.);
		BigDecimal bd = new BigDecimal(Double.toString(dd)); 
		// Set the scale to 2 decimal places using the desired RoundingMode
		bd = bd.setScale(2, RoundingMode.HALF_UP); 
		dd = bd.doubleValue();		
		// split the diff between multiple days
		for(DailyBlock block:list){
		    if(block.getHours() > dd ){
			double hrs = block.getHours() - dd;
			earn_hrs = earn_hrs - dd;
			block.setHours(hrs);		    
		    }
		}
	    }
	}
	if(earn_hrs > 0.001){
	    System.err.println(week_no+" "+empNo+" "+earn_hrs);
	}
    }
    void addToSum(String empNo, Double dd, Map<String, Double> map){
	if(map.containsKey(empNo)){
	    double dd2 = map.get(empNo)+dd;
	    map.put(empNo, dd2);
	}
	else{
	    map.put(empNo, dd);
	}
    }
    void adjustEarnBlocks(List<DailyBlock> blocks){

	for(DailyBlock one:blocks){
	    String empNum = one.getEmpNumber();
	    String code_id = one.getCode_id();
	    double hours = one.getHours();
	    int week_no = one.getDays();
	    if(week_no == 1){
		if(week1EmpCodes.containsKey(empNum)){
		    Map<String, Double> map = week1EmpCodes.get(empNum);
		    if(map.containsKey(code_id)){
			double dd = map.get(code_id);
			hours = hours - dd;
			one.setHours(hours);
		    }
		}
	    }
	    else{
		if(week2EmpCodes.containsKey(empNum)){
		    Map<String, Double> map = week2EmpCodes.get(empNum);
		    if(map.containsKey(code_id)){
			double dd = map.get(code_id);
			hours = hours - dd;
			one.setHours(hours);
		    }
		}		
	    }
	}
    }    
    //
    // getters
    //
    public String find(){
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="";
	String qq2 = "", qw2=""; // used for union
	String qq = " select "+
	    " concat_ws('-',t.date,j.id,c.id) AS block_id,"+
	    " t.document_id AS document_id, "+
	    " j.group_id AS group_id,"+
	    " g.department_id AS department_id,"+
	    " j.salary_group_id AS salary_group_id,"+
	    
	    " p2.name AS job_title,"+
	    " c.name AS earn_code, "+
	    " c.id AS code_id,"+
	    " concat_ws(' ',e.first_name,e.last_name) AS full_name,"+
	    " e.id AS employee_id,"+
	    
	    " e.employee_number AS empnum,"+	    	    
	    " date_format(t.date,'%m/%d/%Y') AS time_date,"+ 
	    " d2.name AS department_name,"+
	    " g.name AS group_name, "+
	    " n.nw_code AS nw_code,"+
	    
	    " n.gl_string AS gl_string, "+
	    " s.name AS salaryGroupName, "+
	    " datediff(t.date, p.start_date) AS days, "+
	    " if(ss.duration is null,8,ss.duration/60) daily_hours, "+ 	    
	    " sum(t.hours) AS hours, "+
	    " sum(t.amount) AS amount "+
	    " from time_blocks t "+
	    " join hour_codes c on t.hour_code_id=c.id "+
	    " join time_documents d on d.id=t.document_id "+
	    " join jobs j on d.job_id=j.id "+
	    " join positions p2 on j.position_id=p2.id "+
	    " join employees e on j.employee_id=e.id "+
	    " join salary_groups s on j.salary_group_id=s.id "+
	    " join groups g on j.group_id=g.id "+
	    " join departments d2 on g.department_id=d2.id "+
	    " join code_cross_ref n on n.code_id=c.id "+
	    " left join group_shifts gs on g.id=gs.group_id "+
	    " left join shifts ss on ss.id=gs.shift_id ";
	qq2 = qq;
	if(group_id.isEmpty() || !(bpdDispGroups.contains(group_id))){
	    qq += " join pay_periods p on p.id=d.pay_period_id ";
	}
	qq2 += " join pay_periods_alt p on p.id=d.pay_period_id ";
	String qw = " where t.inactive is null and (t.hours > 0 or t.amount > 0) and "+
	    " d.pay_period_id= ? ";
	if(!salary_group_id.isEmpty()){
	    qw += " and j.salary_group_id = ? ";
	}
	qw2 = qw;
	if(!group_id.isEmpty()){
	    if(bpdDispGroups.contains(group_id)){
		qq += " join pay_periods_alt p on p.id=d.pay_period_id ";
	    }
	    qw += " and j.group_id = ? ";
	}
	else if(!department_id.isEmpty()){
	    qw += " and g.department_id = ? ";
	    qw2 += " and g.id in (32,360) "; // dispatch only
	    if(department_id.equals("20")){ // police
		qw += " and g.id not in (32,360) "; // exclude dispatch
	    }
	}
	if(!qw.isEmpty()){
	    qq += qw;
	    qq2 += qw2;
	}
	qq += " group by 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19 ";
	qq2 += " group by 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19 ";
	if(pay_period_id.isEmpty()){
	    msg = " pay period not set ";
	    logger.error(msg);
	    return msg;
	}
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = " Could not connect to DB ";
	    logger.error(msg);
	    return msg;
	}
	if(department_id.equals("20")){
	    qq = qq+" UNION ALL "+qq2;
	}
	if(!sortBy.isEmpty()){
	    qq += " order by "+sortBy;
	}
	logger.debug(qq);	
	try{
	    pstmt = con.prepareStatement(qq);
	    String empNo = "";
	    int jj=1;
	    pstmt.setString(jj++, pay_period_id);
	    if(!salary_group_id.isEmpty()){
		pstmt.setString(jj++, salary_group_id);
	    }
	    if(!group_id.isEmpty()){
		pstmt.setString(jj++, group_id);
	    }
	    else if(!department_id.isEmpty()){
		pstmt.setString(jj++, department_id);
	    }
	    // for qq2
	    if(department_id.equals("20")){
		if(!salary_group_id.isEmpty()){
		    pstmt.setString(jj++, salary_group_id);
		}
		pstmt.setString(jj++, department_id);
	    }
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		if(dailyBlocks == null)
		    dailyBlocks = new ArrayList<>();
		if(empNumbers == null)
		    empNumbers = new HashSet<>();
		String salaryGroupName = rs.getString(17);
		boolean isSeasonal = false;
		if(salaryGroupName.equals("Temp") ||
		   salaryGroupName.indexOf("Season") >-1){
		    isSeasonal = true;
		}
		String code_id = rs.getString(8);
		int days = rs.getInt(18);
		DailyBlock one = new DailyBlock(
						rs.getString(1),
						rs.getString(2), // emp num
						rs.getString(3),
						rs.getString(4),
						rs.getString(5),
						
						rs.getString(6),
						rs.getString(7),
						rs.getString(8),
						rs.getString(9),
						rs.getString(10),
						
						rs.getString(11),
						rs.getString(12),
						rs.getString(13),
						rs.getString(14),
						rs.getString(15),
						
						rs.getString(16),
						rs.getString(17),
						rs.getInt(18),
						rs.getInt(19), // daily
						rs.getDouble(20),
						rs.getDouble(21),
						isSeasonal
						);
		empNo = rs.getString(11);
		if(!empNumbers.contains(empNo))
		    empNumbers.add(empNo);
		if(!dailyBlocks.contains(one)){
		    dailyBlocks.add(one);
		    // check if the earn code is in the earn code set above
		    if(earnCodes.contains(code_id)){
			if(days < 7){
			    addToHash(week1EmpCodes, one);
			}
			else{
			    addToHash(week2EmpCodes, one);
			}
		    }
		    if(regCodes.contains(code_id)){
			if(days < 7){
			    addToWeekBlocks(1, one);
			}
			else{
			    addToWeekBlocks(2, one);
			}
		    }		    
		}
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
    private void addToWeekBlocks(int week_no, DailyBlock block){
	if(regCodes.contains(block.getCode_id())){	
	    if(week_no == 1){
		if(week1Blocks.containsKey(block.getEmpNumber())){
		    List<DailyBlock> list = week1Blocks.get(block.getEmpNumber());
		    list.add(block);
		    week1Blocks.put(block.getEmpNumber(),list);
		}
		else{
		    List<DailyBlock> list = new ArrayList<>();
		    list.add(block);
		    week1Blocks.put(block.getEmpNumber(), list);
		}
	    }
	    else{
		if(week2Blocks.containsKey(block.getEmpNumber())){
		    List<DailyBlock> list = week2Blocks.get(block.getEmpNumber());
		    list.add(block);
		    week2Blocks.put(block.getEmpNumber(),list);
		}
		else{
		    List<DailyBlock> list = new ArrayList<>();
		    list.add(block);
		    week2Blocks.put(block.getEmpNumber(), list);
		}		
	    }
	}
    }
    private void addToHash(Map<String, Map<String, Double>> hash, DailyBlock block){
	String empNum = block.getEmpNumber();
	String code_id = block.getCode_id();
	double hours = block.getHours();
	if(hash.containsKey(empNum)){
	    Map<String, Double> map = hash.get(empNum);
	    if(map.containsKey(code_id)){
		double dd = map.get(code_id);
		dd = dd + hours;
		map.put(code_id, dd);
		hash.put(empNum, map);
	    }
	    else{
		map.put(code_id, hours);
		hash.put(empNum, map);
	    }
	}
	else{
	    Map<String, Double> map = new TreeMap<>();
	    map.put(code_id, hours);
	    hash.put(empNum, map);
	}
    }
    /**
     * find comp and prof calculated hours
     * from twarp tables
     */
    /**
  select t.run_id,t.hour_code_id,c.name,t.hours,t.amount from  tmwrp_blocks t join hour_codes c on t.hour_code_id=c.id limit 50;
     */
    public String find2(){
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg="";
	String week1_end_date = "";
	String week2_end_date = "";
	String week1_end_date_alt = "";
	String week2_end_date_alt = "";	
	String qq2="", qw2="", qs="";
	if(payPeriod == null){
	    getPayPeriod();
	}
	if(payPeriod != null){
	    week1_end_date = payPeriod.getWeek1EndDate();
	    week2_end_date = payPeriod.getEndDate();
	}
	week1_end_date_alt = Helper.getDateAfter(payPeriod.getWeek1EndDate(),-1);
	week2_end_date_alt = Helper.getDateAfter(payPeriod.getEndDate(),-1);	
	if(!group_id.isEmpty() && bpdDispGroups.contains(group_id)){
	    week1_end_date = Helper.getDateAfter(payPeriod.getWeek1EndDate(),-1);
	    week2_end_date = Helper.getDateAfter(payPeriod.getEndDate(),-1);
	}
	String qq = " select "+
	    " concat_ws('-',r.id,t.id) AS block_id, "+
	    " r.document_id AS document_id, "+
	    " j.group_id AS group_id,"+
	    " g.department_id AS department_id,"+
	    " j.salary_group_id AS salary_group_id,"+
	    
	    " p2.name AS job_title,"+
	    " c.name AS earn_code, "+
	    " c.id AS code_id,"+
	    " concat_ws(' ',e.first_name,e.last_name) AS full_name,"+
	    " e.id AS employee_id,"+
	    
	    " e.employee_number AS empnum,";
	qq2 = qq;
	qq +=" if(t.term_type = 'Week 1','"+week1_end_date+"','"+week2_end_date+"') AS time_date, ";
	qq2 +=" if(t.term_type = 'Week 1','"+week1_end_date_alt+"','"+week2_end_date_alt+"') AS time_date, ";	
	qs = " d2.name AS department_name,"+
	    " g.name AS group_name, "+
	    " n.nw_code AS nw_code,"+
	    
	    " n.gl_string AS gl_string, "+
	    " s.name AS salaryGroupName, "+
	    " if(t.term_type = 'Week 1',1,2) AS days,  "+
	    " if(ss.duration is null,8,ss.duration/60) daily_hours, "+ 	
	    " sum(t.hours) AS hours, "+
	    " sum(t.amount) AS amount "+
	    " from tmwrp_blocks t join tmwrp_runs r on r.id=t.run_id "+
	    " join hour_codes c on t.hour_code_id=c.id "+
	    " join time_documents d on d.id=r.document_id "+

	    " join jobs j on d.job_id=j.id "+
	    " join positions p2 on j.position_id=p2.id "+
	    " join employees e on j.employee_id=e.id "+
	    " join salary_groups s on j.salary_group_id=s.id "+
	    " join groups g on j.group_id=g.id "+
	    " join departments d2 on g.department_id=d2.id "+
	    " join code_cross_ref n on n.code_id=c.id "+
	    " left join group_shifts gs on gs.group_id = g.id "+
	    " left join shifts ss on ss.id=gs.shift_id ";
	qq += qs;
	qq2 += qs;
	
	if(group_id.isEmpty() || !(bpdDispGroups.contains(group_id))){
	    qq += " join pay_periods p on p.id=d.pay_period_id ";
	}
	qq2 += " join pay_periods_alt p on p.id=d.pay_period_id ";
	String qw = " where (t.hours > 0 or t.amount > 0) and "+
	    " c.id in (34,43,44,45,46,50,71,78,79,109) and "+
	    " d.pay_period_id=? ";
	if(!salary_group_id.isEmpty()){
	    qw += " and j.salary_group_id = ? ";
	}
	qw2 = qw;
	if(!group_id.isEmpty()){
	    qw += " and j.group_id = ? ";
	    if(bpdDispGroups.contains(group_id)){
		qq += "join pay_periods_alt p on p.id=d.pay_period_id ";
	    }
	}
	else if(!department_id.isEmpty()){
	    qw += " and g.department_id = ? ";
	    if(department_id.equals("20")){
		qw += " and g.id not in (32,360) "; // exclude dispatch
		qw2 += " and g.id in (32,360) "; // dispatch only		
	    }
	    
	}
	if(!qw.isEmpty()){
	    qq += qw;
	    qq2 += qw2;
	}
	qq += " group by 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19 ";
	qq2 += " group by 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19 ";
	if(department_id.equals("20")){
	    qq = qq+" UNION ALL "+qq2;
	}
	if(!sortBy.isEmpty()){
	    qq += " order by "+sortBy;
	}
	if(pay_period_id.isEmpty()){
	    msg = " pay period not set ";
	    logger.error(msg);
	    return msg;
	}
	con = UnoConnect.getConnection();
	if(con == null){
	    msg = " Could not connect to DB ";
	    logger.error(msg);
	    return msg;
	}

	logger.debug(qq);
	try{
	    pstmt = con.prepareStatement(qq);
	    String empNo = "";
	    int jj=1;
	    pstmt.setString(jj++, pay_period_id);
	    if(!salary_group_id.isEmpty()){
		pstmt.setString(jj++, salary_group_id);
	    }
	    if(!group_id.isEmpty()){
		pstmt.setString(jj++, group_id);
	    }
	    else if(!department_id.isEmpty()){
		pstmt.setString(jj++, department_id);
	    }
	    if(department_id.equals("20")){
		pstmt.setString(jj++, salary_group_id);
	    }
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		if(earnBlocks == null)
		    earnBlocks = new ArrayList<>();
		String salaryGroupName = rs.getString(17);
		boolean isSeasonal = false;
		if(salaryGroupName.equals("Temp") ||
		   salaryGroupName.indexOf("Season") >-1){
		    isSeasonal = true;
		}
		DailyBlock one = new DailyBlock(
						rs.getString(1),
						rs.getString(2), 
						rs.getString(3),
						rs.getString(4),
						rs.getString(5),
						rs.getString(6),
						rs.getString(7),
						rs.getString(8),
						rs.getString(9),
						rs.getString(10),
						rs.getString(11),
						rs.getString(12),
						rs.getString(13),
						rs.getString(14),
						rs.getString(15),
						rs.getString(16),
						rs.getString(17),
						rs.getInt(18),
						rs.getInt(19),
						rs.getDouble(20),
						rs.getDouble(21),
						isSeasonal
						);
		if(!earnBlocks.contains(one))
		    earnBlocks.add(one);
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
    

}
/**
	 select
	     concat_ws('-',r.id,t.id) AS block_id, 
	     r.document_id AS document_id, 
	     j.group_id AS group_id,
	     g.department_id AS department_id,
	     d.pay_period_id AS pay_period_id,
	     j.salary_group_id AS salary_group_id,
	     p2.name AS job_title,
	     c.name AS earn_code, 
	     ' ' AS earn_code_reason,
	     concat_ws(' ',e.first_name,e.last_name) AS full_name,
	     e.id AS employee_id,
	     e.employee_number AS empnum,	    	    
	     if(t.term_type = 'Week 1','02/08/2026','02/15/2026') AS time_date, 
	     d2.name AS department_name,
	     g.name AS group_name, 
	     n.nw_code AS nw_code,
	     n.gl_string AS gl_string,
	     s.name AS salaryGroupName,
	     if(t.term_type = 'Week 1',1,2) AS days ,	     
	     t.hours AS hours, 
	     t.amount AS amount
	     from tmwrp_blocks t join tmwrp_runs r on r.id=t.run_id 
	     join hour_codes c on t.hour_code_id=c.id 
	     join time_documents d on d.id=r.document_id 
	     join pay_periods p on p.id=d.pay_period_id 
	     join jobs j on d.job_id=j.id 
	     join positions p2 on j.position_id=p2.id 
	     join employees e on j.employee_id=e.id 
	     join salary_groups s on j.salary_group_id=s.id 
	     join groups g on j.group_id=g.id 
	     join departments d2 on g.department_id=d2.id 
	     join code_cross_ref n on n.code_id=c.id 
	where (t.hours > 0 or t.amount > 0) and
	c.id in (34,43,44,45,46,50,71,78,79,109) and  
	     d.pay_period_id=733 and d2.id=1

	     // daily records
	     //
	        select
		concat_ws('-',t.date,j.id,c.id) AS block_id,
	    t.document_id AS document_id, 
	    j.group_id AS group_id,
	    g.department_id AS department_id,
	     j.salary_group_id AS salary_group_id,
	     
	     p2.name AS job_title,
	     c.name AS earn_code, 
	     c.id AS code_id,
	     concat_ws(' ',e.first_name,e.last_name) AS full_name,
	     e.id AS employee_id,
	     
	     e.employee_number AS empnum,	    	    
	     date_format(t.date,'%m/%d/%Y') AS time_date, 
	     d2.name AS department_name,
	     g.name AS group_name, 
	     n.nw_code AS nw_code,
	     
	     n.gl_string AS gl_string,
	     s.name AS salaryGroupName,
	     datediff(t.date, p.start_date) AS days, 	    
	     if(ss.duration is null,8,ss.duration/60) daily_hours,	     
	     sum(t.hours) AS hours, 
	     sum(t.amount) AS amount

	     from time_blocks t 
	     join hour_codes c on t.hour_code_id=c.id 
	     join time_documents d on d.id=t.document_id 
	     join pay_periods p on p.id=d.pay_period_id 
	     join jobs j on d.job_id=j.id 
	     join positions p2 on j.position_id=p2.id 
	     join employees e on j.employee_id=e.id 
	     join salary_groups s on j.salary_group_id=s.id 
	     join groups g on j.group_id=g.id 
	     join departments d2 on g.department_id=d2.id 
	     join code_cross_ref n on n.code_id=c.id
	     left join group_shifts gs on gs.group_id=g.id
	     left join shifts ss on ss.id=gs.shift_id 
	    where t.inactive is null and (t.hours > 0 or t.amount > 0) and 
	     d.pay_period_id=733 and d2.id=1
	     group by 1,2,3,4,5,6,7,8,9.10,11,12,13,14,15,16,17,18,19

	     
	    

	     

	     

*/
	    
