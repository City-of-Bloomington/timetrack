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

public class IpAddressAction extends TopAction{

		static final long serialVersionUID = 3800L;	
		static Logger logger = LogManager.getLogger(IpAddressAction.class);
		//
		String ipaddressesTitle = "Current IP addresses";
		IpAddress ipaddress = null;
		List<IpAddress> ipaddresses = null;

		public String execute(){
				String ret = SUCCESS;
				String back = doPrepare();
				if(!back.equals("")){
						try{
								HttpServletResponse res = ServletActionContext.getResponse();
								String str = url+"Login";
								res.sendRedirect(str);
								return super.execute();
						}catch(Exception ex){
								System.err.println(ex);
						}	
				}
				clearAll();
				if(action.equals("Save")){
						back = ipaddress.doSave();
						if(!back.equals("")){
								addActionError(back);
								addError(back);
						}
						else{
								addMessage("Saved Successfully");
								addActionMessage("Saved Successfully");
						}
				}				
				else if(action.startsWith("Save")){
						back = ipaddress.doUpdate();
						if(!back.equals("")){
								addActionError(back);
								addError(back);
						}
						else{
								addMessage("Saved Successfully");
						}
				}
				else if(action.startsWith("Delete")){
						back = ipaddress.doDelete();
						if(!back.equals("")){
								addActionError(back);
								addError(back);
						}
						else{
								id="";
								ipaddress = new IpAddress();
								addMessage("Deleted Successfully");
						}
				}				
				else{		
						getIpaddress();
						if(!id.equals("")){
								back = ipaddress.doSelect();
								if(!back.equals("")){
										addActionError(back);
										addError(back);
								}
						}
				}
				return ret;
		}
		public IpAddress getIpaddress(){
				if(ipaddress == null){
						ipaddress = new IpAddress();
						ipaddress.setId(id);
				}
				return ipaddress;
						
		}
		public void setIpaddress(IpAddress val){
				if(val != null){
						ipaddress = val;
				}
		}
		
		public String getIpaddressesTitle(){
				
				return ipaddressesTitle;
		}

		public void setAction2(String val){
				if(val != null && !val.equals(""))		
						action = val;
		}
		public List<IpAddress> getIpaddresses(){
				if(ipaddresses == null){
						IpAddressList tl = new IpAddressList();
						String back = tl.find();
						if(back.equals("")){
								List<IpAddress> ones = tl.getIpAddresses();
								if(ones != null && ones.size() > 0){
										ipaddresses = ones;
								}
						}
				}
				return ipaddresses;
		}

}





































