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
import org.apache.log4j.Logger;
import in.bloomington.timer.list.*;
import in.bloomington.timer.bean.*;

public class TimeNoteAction extends TopAction{

		static final long serialVersionUID = 3800L;	
		static Logger logger = Logger.getLogger(TimeNoteAction.class);
		//
		String document_id = "";
		String timeNotesTitle = "Notes";
		TimeNote timeNote = null;		
		List<TimeNote> timeNotes = null;

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
						timeNote.setReported_by(user.getEmployee_id());
						back = timeNote.doSave();
						if(!back.equals("")){
								addActionError(back);
						}
						else{
								addActionMessage("Saved Successfully");
						}
				}				
				else{		
						getTimeNote();
						if(!id.equals("")){
								back = timeNote.doSelect();
								if(!back.equals("")){
										addActionError(back);
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
				if(val != null && !val.equals(""))		
						action = val;
		}
		public void setDocument_id(String val){
				if(val != null && !val.equals(""))		
						document_id = val;
		}		
		public List<TimeNote> getTimeNotes(){
				if(timeNotes == null){
						TimeNoteList tl = new TimeNoteList();
						tl.setDocument_id(document_id);
						String back = tl.find();
						if(back.equals("")){
								List<TimeNote> ones = tl.getTimeNotes();
								if(ones != null && ones.size() > 0){
										timeNotes = ones;
								}
						}
				}
				return timeNotes;
		}

}





































