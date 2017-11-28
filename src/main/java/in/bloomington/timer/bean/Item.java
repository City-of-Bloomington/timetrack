package in.bloomington.timer.bean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.*;
import java.sql.*;
import java.text.*;
import in.bloomington.timer.*;
import in.bloomington.timer.util.*;
import in.bloomington.timer.list.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Item{

		String category="";
		float value = 0f;
		boolean debug = false;
		static final long serialVersionUID = 21L;
		static Logger logger = LogManager.getLogger(Item.class);

    public Item(boolean deb){
				debug = deb;
    }
    public Item(boolean deb, String val, float val2){
				debug = deb;
				setCategory(val);
				setValue(val2);
    }
    //
    public String getCategory(){
				return category;
    }
    public float getValue(){
				return value;
    }
    //
    // setters
    //
    public void setValue (float val){
				value = val;
    }
    public void setCategory (String val){
				if(val != null)
						category = val;
    }

}
