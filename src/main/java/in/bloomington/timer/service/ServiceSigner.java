package in.bloomington.timer.service;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Formatter;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


final class ServiceSigner {

		String key = "abc123def223";
		String timestamp = "20191114T122500Z";
		String serviceName = "EmployeeService";
		
		ServiceSigner(){
				try{
						System.err.println(" sign key ");
						String sig = sign(key, timestamp, serviceName);
						System.err.println(" sig = "+sig);
						/*
						// expected signature is
						af6b12c1234b1bb20b52db179e993001956d098ff3d6795201617ac6ea8b21f7
						*/
				}catch(Exception ex){
						System.err.println(ex);
				}
		}
		static byte[] HmacSHA256(String data, byte[] key){
				byte[] ret = null;
				try{
						String algorithm="HmacSHA256";
						Mac mac = Mac.getInstance(algorithm);
						mac.init(new SecretKeySpec(key, algorithm));
						ret =  mac.doFinal(data.getBytes("UTF-8"));
				}catch(Exception ex){
						System.err.println(ex);
				}
				return ret;
		}
		
		static String sign(String key, String dateStamp, String serviceName) throws Exception {
				String signature = "";
				byte[] kSecret = ("City" + key).getBytes("UTF-8");
				byte[] kDate = HmacSHA256(dateStamp, kSecret);
				byte[] kSigning = HmacSHA256(serviceName, kDate);
				signature = toHexString(kSigning);				
				return signature;
		}
		private static String toHexString(byte[] bytes) {
				Formatter formatter = new Formatter();
				
				for (byte b : bytes) {
						formatter.format("%02x", b);
				}
				return formatter.toString();
		}		

		
}
