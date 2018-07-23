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

public class TypeAction extends TopAction{

		static final long serialVersionUID = 3800L;	
		static Logger logger = LogManager.getLogger(TypeAction.class);
		//
		String type_name="position", table_name="positions";
		String typesTitle = "Positions", title="Position";		
		List<Group> groups = null;
		Type type = null;
		List<Type> types = null;
		private static final Map<String, String> tables = new HashMap<String, String>();
		private static final Map<String, String> titles = new HashMap<String, String>();
		
		static {
				tables.put("position", "positions");
				tables.put("node", "workflow_nodes");
				
				titles.put("position","Position");
				titles.put("node","Workflow Node");				
    }
		@Override
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
						type.setTable_name(table_name);
						back = type.doSave();
						if(!back.equals("")){
								addActionError(back);
								addError(back);
						}
						else{
								addActionMessage("Saved Successfully");
								addMessage("Saved Successfully");
						}
				}				
				else if(action.startsWith("Save")){
						type.setTable_name(table_name);
						back = type.doUpdate();
						if(!back.equals("")){
								addActionError(back);
								addError(back);
						}
						else{
								addActionMessage("Saved Successfully");
								addMessage("Saved Successfully");
						}
				}
				else{		
						getType();
						if(!id.equals("")){
								back = type.doSelect();
								if(!back.equals("")){
										addActionError(back);
										addError(back);
								}
						}
				}
				return ret;
		}
		public Type getType(){
				if(type == null){
						type = new Type();
						type.setId(id);
						type.setTable_name(table_name);
				}
				return type;
						
		}
		public void setType(Type val){
				if(val != null){
						type = val;
				}
		}
		public void setType_name(String val){
				if(val != null && !val.equals("-1")){
						type_name = val;
						if(tables.containsKey(type_name)){
								table_name = tables.get(type_name);
						}
						if(titles.containsKey(type_name)){
								title = titles.get(type_name);
						}						
				}
		}
		public String getType_name(){
				return type_name;
		}
		
		public String getTypesTitle(){
				
				return title+"s";
		}
		public String getTitle(){
				return title;
		}		

		public void setAction2(String val){
				if(val != null && !val.equals(""))		
						action = val;
		}
		public List<Type> getTypes(){
				if(types == null){
						TypeList tl = new TypeList();
						tl.setTable_name(table_name);
						tl.setActiveOnly();
						String back = tl.find();
						if(back.equals("")){
								List<Type> ones = tl.getTypes();
								if(ones != null && ones.size() > 0){
										types = ones;
								}
						}
				}
				return types;
		}

}





































