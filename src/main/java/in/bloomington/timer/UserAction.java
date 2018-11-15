package in.bloomington.timer;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserAction extends TopAction{

		static final long serialVersionUID = 3950L;	
		static Logger logger = LogManager.getLogger(UserAction.class);
		//
		List<User> users = null;
		String usersTitle = "Current Users";
		User usser = null; // to distinguish from logged in user
		public String execute(){
				String ret = SUCCESS;
				String back = doPrepare();
				if(action.equals("Save")){
						back = usser.doSave();
						if(!back.equals("")){
								addActionError(back);
						}
						else{
								addActionMessage("Added Successfully");
						}
				}				
				else if(action.startsWith("Save")){
						back = usser.doUpdate();
						if(!back.equals("")){
								addActionError(back);
						}
						else{
								addActionMessage("Updated Successfully");
						}
				}
				else{		
						getUsser();
						if(!id.equals("")){
								back = usser.doSelect();
								if(!back.equals("")){
										addActionError(back);
								}								
						}
				}
				return ret;
		}
		public String getUsersTitle(){
				return usersTitle;
		}
		public User getUsser(){
				if(usser == null){
						usser = new User(id);
				}
				return usser;
		}
		public void setUsser(User val){
				if(val != null)
						usser = val;
		}
		public void setAction2(String val){
				if(val != null && !val.equals(""))		
						action = val;
		}
		public List<User> getUsers(){
				if(users == null){
						UserList tl = new UserList();
						tl.setActiveOnly();
						String back = tl.find();
						if(back.equals("")){
								List<User> ones = tl.getUsers();
								if(ones != null && ones.size() > 0){
										users = ones;
								}
						}
				}
				return users;
		}
		public boolean hasUsers(){
				getUsers();
				return users != null && users.size() > 0;
		}

}





































