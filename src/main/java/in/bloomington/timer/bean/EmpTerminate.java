package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.sql.*;
import java.util.List;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EmpTerminate{

		static final long serialVersionUID = 3700L;	
		static Logger logger = LogManager.getLogger(EmpTerminate.class);
    String id="", expire_date="";
		List<DepartmentEmployee> departmentEmployees = null;
		List<GroupManager> groupManagers = null;
		List<GroupEmployee> groupEmployees = null;
		List<JobTask> jobs = null;
		
		Employee emp = null;
		//
		public EmpTerminate(){
	
		}
		public EmpTerminate(String val){
				//
				setId(val);
    }		
		public EmpTerminate(String val, String val2){
				//
				setId(val);
				setExpire_date(val2);
    }

		public boolean equals(Object obj){
				if(obj instanceof EmpTerminate){
						EmpTerminate one =(EmpTerminate)obj;
						return id.equals(one.getId());
				}
				return false;				
		}
		public int hashCode(){
				int seed = 29;
				if(!id.equals("")){
						try{
								seed += Integer.parseInt(id);
						}catch(Exception ex){
						}
				}
				return seed;
		}
    //
    // getters
    //
    public String getId(){
				return id;
    }
    public String getExpire_date(){
				return expire_date;
    }
		public Employee getEmp(){
				if(emp == null && !id.isEmpty()){
						Employee one = new Employee(id);
						String back = one.doSelect();
						if(back.isEmpty()){
								emp = one;
						}
				}
				return emp;				
		}
    //
    // setters
    //
    public void setId(String val){
				if(val != null)
						id = val;
    }
    public void setExpire_date(String val){
				if(val != null)
						expire_date = val;
    }
    public String toString(){
				return id;
    }
		public boolean hasDepartments(){
				return departmentEmployees != null && departmentEmployees.size() > 0;
		}
		public List<DepartmentEmployee> getDepartmentEmployees(){
				return departmentEmployees;
		}
		public boolean isGroupManager(){
				return groupManagers != null && groupManagers.size() > 0;
		}
		public List<GroupManager> getGroupManagers(){
				return groupManagers;
		}
		public boolean hasGroupEmployees(){
				return groupEmployees != null && groupEmployees.size() > 0;
		}
		public List<GroupEmployee> getGroupEmployees(){
				return groupEmployees;
		}
		public boolean hasJobs(){
				return jobs != null && jobs.size() > 0;
		}
		public List<JobTask> getJobs(){
				return jobs;
		}
		//
		public String doTerminate(){
				String back = "";
				getEmp();
				if(emp == null){
						back = "No employee found ";
						return back;
				}
				if(expire_date.isEmpty()){
						back = "Expire date not set";
						return back;
				}
				if(emp.hasDepartments()){
						departmentEmployees = emp.getDepartmentEmployees();
				}
				if(emp.isGroupManager()){
						groupManagers = emp.getGroupManagers();
						for(GroupManager one:groupManagers){
								one.setExpire_date(expire_date);
								back += one.doUpdate();
						}
				}
				if(emp.hasGroupEmployees()){
						groupEmployees = emp.getGroupEmployees();
						for(GroupEmployee one:groupEmployees){
								one.setExpire_date(expire_date);
								back += one.doUpdate();
						}
				}
				if(emp.hasJobs()){
						jobs = emp.getJobs();
						for(JobTask one:jobs){
								one.setExpire_date(expire_date);
								back += one.doUpdate();
						}
				}
				return back;
		}
}
