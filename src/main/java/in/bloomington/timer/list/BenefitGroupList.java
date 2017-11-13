package in.bloomington.timer.list;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */

import java.util.*;
import java.sql.*;
import java.io.*;
import java.text.*;
import java.util.ArrayList;
import java.util.List;
import in.bloomington.timer.util.Helper;
import in.bloomington.timer.bean.BenefitGroup;
import org.apache.log4j.Logger;

public class BenefitGroupList{

    boolean debug;
		static final long serialVersionUID = 57L;
		static Logger logger = Logger.getLogger(BenefitGroupList.class);
		List<BenefitGroup> benefitGroups = null;
		//
    public BenefitGroupList(){
    }
		public List<BenefitGroup> getBenefitGroups(){
				return benefitGroups;
		}
    //
    public String find(){
				//
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
		
				String qq = "select id,name,fullTime,exempt,unioned from benefit_groups ";
				String back = "";
				try{
						if(debug){
								logger.debug(qq);
						}
						con = Helper.getConnection();
						if(con == null){
								back = "Could not connect to DB ";
								return back;
						}
						pstmt = con.prepareStatement(qq);
						int jj = 1;
						rs = pstmt.executeQuery();
						while(rs.next()){
								if(benefitGroups == null)
										benefitGroups = new ArrayList<>();
								BenefitGroup one =
										new BenefitGroup(debug,
																		 rs.getString(1),
																		 rs.getString(2),
																		 rs.getString(3) != null,
																		 rs.getString(4) != null,
																		 rs.getString(5) != null);
								benefitGroups.add(one);
						}
				}
				catch(Exception ex){
						back += ex;
						logger.error(ex+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(con, pstmt, rs);
				}
				return back;
    }
}






















































