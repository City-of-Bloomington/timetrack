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

public class TimeNoteAction extends TopAction{

    static final long serialVersionUID = 3800L;	
    static Logger logger = LogManager.getLogger(TimeNoteAction.class);
    //
    String document_id = "";
    String timeNotesTitle = "Notes";
    TimeNote timeNote = null;		
    List<TimeNote> timeNotes = null;

    public String execute(){
	String ret = SUCCESS;
	String back = doPrepare();
	if(!back.isEmpty()){
	    return back;
	}
	if(action.equals("Save")){
	    timeNote.setReported_by(user.getId());
	    back = timeNote.doSave();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		addMessage("Saved Successfully");								
	    }
	}
	else if(action.equals("Delete")){
	    getTimeNote();
	    back = timeNote.doDelete();
	    if(!back.isEmpty()){
		addError(back);
	    }
	    else{
		addMessage("Deleted Successfully");
		ret="redirectTimeDetails";
		/**
		   protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
		   RequestDispatcher dispatcher = getServletContext()
		   .getRequestDispatcher("/forwarded");
		   dispatcher.forward(req, resp);
		   }

		*/
	    }
						
	}
	else{		
	    getTimeNote();
	    if(!id.isEmpty()){
		back = timeNote.doSelect();
		if(!back.isEmpty()){
		    addError(back);
		}
	    }
	}
	return ret;
    }
    public TimeNote getTimeNote(){
	if(timeNote == null){
	    timeNote = new TimeNote();
	    timeNote.setId(id);
	    timeNote.setDocument_id(document_id);
	}
	return timeNote;
						
    }
    public void setTimeNote(TimeNote val){
	if(val != null){
	    timeNote = val;
	}
    }
    public String getTimeNotesTitle(){
	return timeNotesTitle;
    }

    public void setAction2(String val){
	if(val != null && !val.isEmpty())		
	    action = val;
    }
    public void setDocument_id(String val){
	if(val != null && !val.isEmpty())		
	    document_id = val;
    }
    public String getDocument_id(){
	return document_id;
    }		
    public List<TimeNote> getTimeNotes(){
	if(timeNotes == null){
	    TimeNoteList tl = new TimeNoteList();
	    tl.setDocument_id(document_id);
	    String back = tl.find();
	    if(back.isEmpty()){
		List<TimeNote> ones = tl.getTimeNotes();
		if(ones != null && ones.size() > 0){
		    timeNotes = ones;
		}
	    }
	}
	return timeNotes;
    }

}





































