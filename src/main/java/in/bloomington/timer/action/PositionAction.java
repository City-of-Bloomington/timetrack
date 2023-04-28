package in.bloomington.timer.action;
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

public class PositionAction extends TopAction{

    static final long serialVersionUID = 3800L;	
    static Logger logger = LogManager.getLogger(PositionAction.class);
    //
    String positionsTitle = "Positions";
    List<Group> groups = null;
    Position position = null;
    List<Position> positions = null;
		
    @Override
    public String execute(){
	String ret = SUCCESS;
	String back = canProceed("position.action");
	if(!back.isEmpty()){
	    return back;
	}
	if(action.equals("Save")){
	    back = position.doSave();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		addMessage("Saved Successfully");
	    }
	}				
	else if(action.startsWith("Save")){
	    back = position.doUpdate();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		addMessage("Updated Successfully");
	    }
	}
	else{		
	    getPosition();
	    if(!id.isEmpty()){
		back = position.doSelect();
		if(!back.isEmpty()){
		    addError(back);
		}
	    }
	}
	return ret;
    }
    public Position getPosition(){
	if(position == null){
	    position = new Position();
	    position.setId(id);
	}
	return position;
						
    }
    public void setPosition(Position val){
	if(val != null){
	    position = val;
	}
    }
		
    public String getPositionsTitle(){
	return positionsTitle;
    }

    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
	    action = val;
    }
    public List<Position> getPositions(){
	if(positions == null){
	    PositionList tl = new PositionList();
	    tl.setActiveOnly();
	    String back = tl.find();
	    if(back.isEmpty()){
		List<Position> ones = tl.getPositions();
		if(ones != null && ones.size() > 0){
		    positions = ones;
		}
	    }
	}
	return positions;
    }

}





































