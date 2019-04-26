package in.bloomington.timer.action;

/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.*;
import java.io.*;
import java.text.*;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.struts2.ServletActionContext;  

import in.bloomington.timer.list.*;
import in.bloomington.timer.bean.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogoutAction extends TopAction{

		static final long serialVersionUID = 1800L;	
		static Logger logger = LogManager.getLogger(LogoutAction.class);
		//
		public String execute(){
				String ret = SUCCESS;
				try{
						HttpServletRequest req = ServletActionContext.getRequest();	
						HttpSession session = req.getSession();
						Employee user = null;
						if(session != null){
								user = (Employee)session.getAttribute("user");
								session.removeAttribute("user");
								session.invalidate();
						}
				}
				catch(Exception ex){
						System.err.println(ex);
				}
				return ret;
		}
}




































