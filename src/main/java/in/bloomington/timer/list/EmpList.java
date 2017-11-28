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
		String name = "";
		
		List<User> emps = null;
		public EmpList(){
				super();
		}
		public EmpList(EnvBean val){
				setEnvBean(val);
		}
		public EmpList(EnvBean val, String val2){
				setEnvBean(val);
				setName(val2);
		}		
		public List<User> getEmps(){
				return emps;
		}
		public void setName(String val){
				if(val != null)
						name = val;
		}
		public void setEnvBean(EnvBean val){
				if(val != null)
						bean = val;
		}		
		public String getName(){
				return name;
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
				User emp = null;
				if (!connectToServer(env)){
						System.err.println("Unable to connect to ldap");
						return null;
				}
				try{
						//
						DirContext ctx = new InitialDirContext(env);
						SearchControls ctls = new SearchControls();
						String[] attrIDs = {"givenName",
																"department",
																"telephoneNumber",
																"mail",
																"cn",
																"sn",
																"businessCategory",
																"title"};
						//
						ctls.setReturningAttributes(attrIDs);
						ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
						String filter = "(cn="+name+"*)";
						NamingEnumeration<SearchResult> answer = ctx.search("", filter, ctls);
						while(answer.hasMore()){
								//
								emp = new User();
								SearchResult sr = answer.next();
								Attributes atts = sr.getAttributes();
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
								Attribute email = (Attribute)(atts.get("mail"));
								if (email != null){
										String post = email.get().toString();
										emp.setEmail(post);
								}
								*/
								if(emps == null){
										emps = new ArrayList<>();
								}
								emps.add(emp);
						}
				}
				catch(Exception ex){
						logger.error(ex);
				}
				return back;
		}
}






















































