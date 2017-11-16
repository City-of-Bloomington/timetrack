package in.bloomington.timer;

/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.*;
import java.io.*;
import java.text.*;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;  
import org.apache.log4j.Logger;
import in.bloomington.timer.list.*;
import in.bloomington.timer.bean.*;

public class NodeAction extends TopAction{

		static final long serialVersionUID = 1810L;	
		static Logger logger = Logger.getLogger(NodeAction.class);
		List<Node> nodes = null;
		//
		Node node = null;
		String node_id="";
		String nodesTitle = "Workflow Actions";
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
				if(action.equals("Save")){
						back = node.doSave();
						if(!back.equals("")){
								addActionError(back);
						}
						else{
								addActionMessage("Saved Successfully");
						}
				}				
				else if(action.startsWith("Save")){
						back = node.doUpdate();
						if(!back.equals("")){
								addActionError(back);
						}
						else{
								addActionMessage("Saved Successfully");
						}
				}
				else{		
						getNode();
						if(!id.equals("")){
								back = node.doSelect();
								if(!back.equals("")){
										addActionError(back);
								}
						}
				}
				return ret;
		}
		public Node getNode(){ 
				if(node == null){
						node = new Node(id);
				}		
				return node;
		}

		public void setNode(Node val){
				if(val != null){
						node = val;
				}
		}

		public String getNodesTitle(){
				return nodesTitle;
		}
		public void setAction2(String val){
				if(val != null && !val.equals(""))		
						action = val;
		}

		public void setNode_id(String val){
				if(val != null && !val.equals(""))		
						node_id = val;
		}
		public String getNode_id(){
				if(node_id.equals("")){
						if(node != null){
								node_id = node.getId();
						}
				}
				return node_id;
		}
		public boolean hasNodes(){
				getNodes();
				return nodes != null;
		}
		public List<Node> getNodes(){
				//
				NodeList gml = new NodeList();
				String back = gml.find();
				if(back.equals("")){
						List<Node> ones = gml.getNodes();
						if(ones != null && ones.size() > 0){
								nodes = ones;
						}
				}
				return nodes;
		}
}




































