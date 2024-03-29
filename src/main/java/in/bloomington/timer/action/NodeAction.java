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
import org.apache.struts2.ServletActionContext;  
import in.bloomington.timer.list.*;
import in.bloomington.timer.bean.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//
// Node is a workflow step
public class NodeAction extends TopAction{

    static final long serialVersionUID = 1810L;	
    static Logger logger = LogManager.getLogger(NodeAction.class);
    List<Node> nodes = null;
    //
    Node node = null;
    String node_id="";
    String nodesTitle = "Workflow Actions";
    public String execute(){
	String ret = SUCCESS;
	String back = canProceed("node.action");
	if(!back.isEmpty()){
	    return back;
	}
	if(action.equals("Save")){
	    back = node.doSave();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		addMessage("Saved Successfully");

	    }
	}				
	else if(action.startsWith("Save")){
	    back = node.doUpdate();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		addMessage("Saved Successfully");
	    }
	}
	else{		
	    getNode();
	    if(!id.isEmpty()){
		back = node.doSelect();
		if(!back.isEmpty()){
		    addError(back);
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
	if(val != null && !val.isEmpty())		
	    action = val;
    }

    public void setNode_id(String val){
	if(val != null && !val.isEmpty())		
	    node_id = val;
    }
    public String getNode_id(){
	if(node_id.isEmpty()){
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
	if(back.isEmpty()){
	    List<Node> ones = gml.getNodes();
	    if(ones != null && ones.size() > 0){
		nodes = ones;
	    }
	}
	return nodes;
    }
}





































