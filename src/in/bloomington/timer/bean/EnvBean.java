package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import org.apache.log4j.Logger;

public class EnvBean {

		static Logger logger = Logger.getLogger(EnvBean.class);
		static final long serialVersionUID = 1400L;		
		String url = "", principle = "", method="", password = "", ctxFactory = "";

		public EnvBean(){};
		/**
		 * setters
		 */
		public void setUrl(String val){
				if(val != null)
						url = val;
		}
		public void setPrinciple(String val){
				if(val != null)
						principle = val;
		}	
		public void setMethod(String val){
				if(val != null)
						method = val;
		}
		public void setPassword(String val){
				if(val != null)
						password = val;
		}
		public void setCtxFactory(String val){
				if(val != null)
						ctxFactory = val;
		}
		/**
		 * getters
		 */
		public String getUrl(){
				return url;
		}
		public String getPrinciple(){
				return principle;
		}	
		public String getMethod(){
				return method;
		}
		public String getPassword(){
				return password;
		}
		public String getCtxFactory(){
				return ctxFactory;
		}
		public String toString(){
				String str = url;
				str += " "+principle+" "+password;
				return str;
		}

}
