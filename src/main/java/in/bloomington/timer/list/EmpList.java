package in.bloomington.timer.list;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.*;
import java.sql.*;
import java.io.*;
import javax.sql.*;
import javax.naming.*;
import javax.naming.directory.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;

public class EmpList extends CommonInc{

		static Logger logger = LogManager.getLogger(EmpList.class);
		static final long serialVersionUID = 1100L;
		static EnvBean bean = null;
		String name = "", dept_name="", group_name="";
		Hashtable<String, String> deptTable = null; // name, id
		Hashtable<String, Hashtable<String,String>> grpTable = null; // dept_id, groups
		List<Employee> emps = null;
		public EmpList(){
				super();
		}
		public EmpList(EnvBean val){
				setEnvBean(val);
				prepareTables();
		}
		public EmpList(EnvBean val, String val2){
				setEnvBean(val);
				setName(val2);
				prepareTables();
		}
		
		public List<Employee> getEmps(){
				return emps;
		}
		public void setName(String val){
				if(val != null)
						name = val;
		}
		public void setDept_name(String val){
				if(val != null)
						dept_name = val;
		}
		public void setEnvBean(EnvBean val){
				if(val != null)
						bean = val;
		}		
		public String getName(){
				return name;
		}

		public void setGroup_name(String val){
				if(val != null){
						group_name = val;
				}
		}		

		/**
			 deptTable : table <Department name, depart ID>
			 grpTable : table <Department ID, table <Group Name, Group ID>
		 */
		void prepareTables(){
				DepartmentList dl = new DepartmentList();
				dl.setActiveOnly();
				String back = dl.find();
				if(back.equals("")){
						List<Department> depts = dl.getDepartments();
						if(depts != null && depts.size() > 0){
								if(deptTable == null){
										deptTable = new Hashtable<>();
								}
								for(Department dd:depts){
										deptTable.put(dd.getName(), dd.getId());
								}
						}
				}
				GroupList gl = new GroupList();
				gl.setActiveOnly();
				back = gl.find();
				if(back.equals("")){
						List<Group> groups = gl.getGroups();
						if(groups != null && groups.size() > 0){
								grpTable = new Hashtable<>();
								for(Group gg:groups){
										String deptId = gg.getDepartment_id();
										String grpId = gg.getId();
										Hashtable<String, String> gtable = null;										
										if(grpTable.containsKey(deptId)){
												gtable = grpTable.get(deptId);
										}
										else {
												gtable = new Hashtable<>();
										}
										gtable.put(gg.getName(), grpId);
										grpTable.put(deptId, gtable);
								}
						}
				}
		}
		boolean connectToServer(Hashtable<String, String> env){

				if(env != null && bean != null){
						env.put(Context.INITIAL_CONTEXT_FACTORY, 
										"com.sun.jndi.ldap.LdapCtxFactory");
						env.put(Context.PROVIDER_URL, bean.getUrl());
						env.put(Context.SECURITY_AUTHENTICATION, "simple"); 
						env.put(Context.SECURITY_PRINCIPAL, bean.getPrinciple());
						env.put(Context.SECURITY_CREDENTIALS, bean.getPassword());
				}
				else{
						return false;
				}
				try {
						DirContext ctx = new InitialDirContext(env);
				} catch (NamingException ex) {
						System.err.println(" ldap "+ex);
						return false;
				}
				return true;
    }		
		public String find(){
				Hashtable<String, String> env = new Hashtable<String, String>(11);
				String back = "", fullName="", str="";
				Employee emp = null;
				if (!connectToServer(env)){
						System.err.println("Unable to connect to ldap");
						return null;
				}
				try{
						//
						DirContext ctx = new InitialDirContext(env);
						SearchControls ctls = new SearchControls();
						String[] attrIDs = {"givenName",
																"department", // not accurate use dn instead
																"telephoneNumber",
																"mail",
																"cn",
																"sn",
																"distinguishedName",
																"dn",
																"businessCategory",
																"employeeNumber",
																"employeeId", // id_code
																"title"};
						//
						ctls.setReturningAttributes(attrIDs);
						ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
						String filter = "";
						if(!name.equals("")){
								filter = "(cn="+name+"*)";
						}
						else if (!dept_name.equals("")){
								/*
									// we can not use wildcard in AD so we have to do
									// it ourselves by bringing all users and search for 
									// dept name and group (if any)
								if(!group_name.equals("")){
										filter = "(&(ou="+group_name+")(ou="+dept_name+"))";
								}
								else
										filter = "(distinguishedName=*,ou="+dept_name+",*)";
								*/
								filter ="(&(objectCategory=person)(objectClass=user))";
						}
						else{ // all
								filter ="(&(objectCategory=person)(objectClass=user))";
						}
						System.err.println(" filter "+filter);
						NamingEnumeration<SearchResult> answer = ctx.search("", filter, ctls);
						while(answer.hasMore()){
								//
								emp = new Employee();
								SearchResult sr = answer.next();
								Attributes atts = sr.getAttributes();
								Attribute dn = atts.get("distinguishedName");
								if (dn != null){
										str = dn.get().toString();
										// System.err.println(" dn "+str);
										if(!dept_name.equals("")){
												if(str.indexOf(dept_name) == -1) continue;
												if(!group_name.equals(""))
														if(str.indexOf(group_name) == -1) continue;
										}
										String strArr[] = setDn(str);
										String deptId = "", grpId="";
										if(!strArr[0].equals("")){
												if(deptTable.containsKey(strArr[0])){
														deptId = deptTable.get(strArr[0]);
												}
												if(!deptId.equals("")){
														if(grpTable.containsKey(deptId)){
																Hashtable<String, String> ttable = grpTable.get(deptId);
																if(ttable.containsKey(strArr[1])){
																		grpId = ttable.get(strArr[1]);
																}
														}
												}
										}
										if(!deptId.equals("")){
												emp.setDepartment_id(deptId);
										}
										if(!grpId.equals("")){
												emp.setGroup_id(grpId);
										}
										// System.err.println(" found dept, grp: "+strArr[0]+" "+strArr[1]+" "+deptId+" "+grpId);
								}
								//
								Attribute cn = atts.get("cn");
								if (cn != null){
										str = cn.get().toString();
										emp.setUsername(str);
								}
								Attribute givenname = atts.get("givenName");
								if (givenname != null){
										str = givenname.get().toString();
										emp.setFirst_name(str);
								}
								Attribute sn = atts.get("sn");
								if (sn != null){
										str = sn.get().toString();
										emp.setLast_name(str);
								}
								Attribute en = atts.get("employeeNumber");
								if (en != null){
										str = en.get().toString();
										emp.setEmployee_number(str);
								}
								Attribute ei = atts.get("employeeId");
								if (ei != null){
										str = ei.get().toString();
										emp.setId_code(str);
								}
								Attribute email = (Attribute)(atts.get("mail"));
								if (email != null){
										str = email.get().toString();
										emp.setEmail(str);
								}
								/*
								Attribute department = 
										(Attribute)(atts.get("department"));
								if (department != null){
										str = department.get().toString();
										emp.setLdap_dept(str);
								}
								*/
								/*
								emp.setFullname(fullName);				
								Attribute department = 
										(Attribute)(atts.get("department"));
								if (department != null){
										String dept = department.get().toString();
										emp.setDept(dept);
								}
								Attribute tele = (Attribute)(atts.get("telephoneNumber"));
								if (tele != null){
										String phone = tele.get().toString();
										emp.setPhone(phone);
								}
								Attribute cat = (Attribute)(atts.get("businessCategory"));
								if (cat != null){
										String busCat = cat.get().toString();
										emp.setDivision(busCat);
								}
								Attribute title = (Attribute)(atts.get("title"));
								if (title != null){
										String post = title.get().toString();
										emp.setJobTitle(post);
								}
								*/								
								if(emps == null){
										emps = new ArrayList<>();
								}
								// try to avoid made up names like "*parks-user";
								if(!emp.getUsername().startsWith("*")){
										emps.add(emp);
								}
						}
				}
				catch(Exception ex){
						logger.error(ex);
				}
				return back;
		}
		String[] setDn(String val){
				String retArr[] = {"",""};
				if(val != null){
						String dept="", grp = "", sub_grp="";
						try{
								String val2 = val.substring(val.indexOf("OU"),val.indexOf("DC")-1);
								String strArr[] = val2.split(",");
								if(strArr != null){
										if(strArr.length == 2){
												dept = strArr[0]; // transit
										}
										if(strArr.length == 3){
												if(val2.indexOf("City Hall") > 0){
														dept = strArr[0];
												}
												else{
														grp = strArr[0];
														dept = strArr[1];														
												}
										}
										else if(strArr.length == 4){
												if(val2.indexOf("City Hall") > 0){
														grp = strArr[0];
														dept = strArr[1];
												}
												else{
														// example maintenance,Dillman Plant,Utilities
														sub_grp = strArr[0];
														grp = strArr[1];
														dept = strArr[2];
												}
										}
										if(!dept.equals("")){
												dept = dept.substring(dept.indexOf("=")+1);
												retArr[0] = dept;
										}
										if(!grp.equals("")){
												grp = grp.substring(grp.indexOf("=")+1);
												if(grp.equals("Showers")) grp="Staff";
										}
										else{
												grp = "Staff";
										}
										if(!sub_grp.equals("")){
												sub_grp = sub_grp.substring(sub_grp.indexOf("=")+1);
												grp = grp+" - "+sub_grp;
										}
										retArr[1] = grp;
								}
						}catch(Exception ex){
								System.err.println(ex);
						}
						// System.err.println(" dept: "+dept+" group: "+grp);
				}
				return retArr;
		}
}






















































