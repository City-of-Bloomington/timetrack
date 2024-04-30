package in.bloomington.timer.bean;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.List;
import java.util.ArrayList;
import java.text.DecimalFormat;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DayBlocks{

    static final long serialVersionUID = 3700L;	
    static Logger logger = LogManager.getLogger(DayBlocks.class);
    static DecimalFormat dfn = new DecimalFormat("##0.00");	
    List<TimeBlock> blocks = null;
    int total_minutes = 0;
    int dayInt = 0;
    //
    public DayBlocks(){
	
    }
    public void addBlocks(List<TimeBlock> list){
	if(list != null){
	    for(TimeBlock one:list){
		addBlock(one);
	    }
	}
    }
    public void addBlock(TimeBlock tblock){
	if(tblock != null){
	    if(blocks == null){
		blocks = new ArrayList<>();
	    }
	    blocks.add(tblock);
	    total_minutes += tblock.getMinutes();
	    dayInt = tblock.getDayInt();
	}
    }
    public List<TimeBlock> getBlocks(){
	return blocks;
    }
    public void setDayInt(int val){
	if(val > 0)
	    dayInt = val;
    }
    public String getDayHours(){
	String str ="0.0";
	if(total_minutes > 0){
	    str = dfn.format(total_minutes/60.);
	}
	return str;
    }
    public boolean hasDailyBlocks(){
	return blocks != null && blocks.size() > 0;
    }
    public int getDayInt(){
	return dayInt;
    }
}
