package in.bloomington.timer.list;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.*;
import java.sql.*;
import java.io.*;
import javax.sql.*;
import org.apache.log4j.Logger;
import in.bloomington.timer.util.*;
import in.bloomington.timer.bean.*;

public class IpAddressList{

		static Logger logger = Logger.getLogger(IpAddressList.class);
		static final long serialVersionUID = 3800L;
		String name = ""; // for service
		List<IpAddress> ipAddresses = null;
	
		public IpAddressList(){
		}
		public List<IpAddress> getIpAddresses(){
				return ipAddresses;
		}
		
		public String find(){
		
				String back = "";
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				Connection con = Helper.getConnection();
				String qq = "select t.id,t.ip_address,t.description from ip_allowed t ";
				if(con == null){
						back = "Could not connect to DB";
						return back;
				}
				String qw = "";
				try{
						logger.debug(qq);
						pstmt = con.prepareStatement(qq);
						rs = pstmt.executeQuery();
						if(ipAddresses == null)
								ipAddresses = new ArrayList<>();
						while(rs.next()){
								IpAddress one =
										new IpAddress(rs.getString(1),
																	rs.getString(2),
																	rs.getString(3));
								ipAddresses.add(one);
						}
				}
				catch(Exception ex){
						back += ex+" : "+qq;
						logger.error(back);
				}
				finally{
						Helper.databaseDisconnect(con, pstmt, rs);
				}
				return back;
		}
}






















































