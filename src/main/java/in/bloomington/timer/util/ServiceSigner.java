package in.bloomington.timer.util;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Base64;
import java.util.Formatter;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


final class ServiceSigner {

    String key = "";
    String keyId = "";
    String digest = "", body="";
    String signature = "", new_signature="";
    String sign_str = "", algorithm="";
    List<String> items_order = null;
    Map<String, String> headerMap = null;
    static final Map<String, String> algos = new HashMap<>();
    static {
	algos.put("hmac-sha256","HmacSHA256");
	algos.put("hmac-sha512","HmacSHA512");
    };
    //
    // Signature: KeyId="mykeyid",algorithm="hs2019",created=1402170695,
    //          headers="(request-target) (created) host digest content-length",
    //          signature="Base64(RSA-SHA512(signing string))"
    //
    ServiceSigner(String _key,
		  String _body,
		  String _signature,
		  String _algorithm,
		  List<String> itemsOrder,
		  Map<String, String> map){
	setKey(_key);
	setBody(_body);
	setSignature(_signature);
	setAlgorithm(_algorithm);				
	setItemsOrder(itemsOrder);
	setHeaderMap(map);
	//
	try{
	    if(key == null || key.equals("")){
		System.err.println(" key value not set ");
		return;
	    }
	    if(items_order.contains("digest")){
		//
		// checksum the digest
		//
		String digest2 = "SHA256="+sign(key, body, algorithm);
		System.err.println(" digest2 "+digest2);
		if(headerMap.containsKey("digest")){
		    digest = headerMap.get("digest");
		}
		if(!digest.equals(digest2)){
		    System.err.println(" digests are not equal");
		}
		else{
		    System.err.println(" digests are equal");
		}
	    }
	    composeSigningString();
	    System.err.println(" str to sign "+sign_str);
	    new_signature = sign(key, sign_str, algorithm);
	    System.err.println(" new sign "+new_signature);				
	}catch(Exception ex){
	    System.err.println(ex);				
	}
    }		

    void setHeaderMap(Map<String, String> map){
	headerMap = map;
    }
    /**
     * check if the two signatures match
     */
    boolean verify(){
	return !signature.equals("") &&
	    !new_signature.equals("") &&
	    signature.equals(new_signature);
    }		
    void composeSigningString(){
	sign_str = "";
	for(String str:items_order){
	    if(headerMap.containsKey(str)){
		addToString(str, headerMap.get(str));
	    }
	    else{
		System.err.println(" key "+str+" not in map");
	    }
	}
	sign_str = sign_str.trim();
    }
    void addToString(String name, String value){
	if(!sign_str.equals("")) sign_str+="\n";
	sign_str += name += ":";
	if(value != null && !value.equals("")){
	    sign_str += " "+value.trim();
	}
	else{
	    sign_str += " ";
	}
    }
    static byte[] HmacSHA(String data, byte[] key, String algorithm){
	byte[] ret = null;
	try{
	    // String algorithm="HmacSHA256";
	    Mac mac = Mac.getInstance(algorithm);
	    mac.init(new SecretKeySpec(key, algorithm));
	    ret =  mac.doFinal(data.getBytes("UTF-8"));
	}catch(Exception ex){
	    System.err.println(ex);
	}
	return ret;
    }
    static String sign(String secretKey, String data, String algorithm) throws Exception {
	String signature = "";
	byte[] kSecret = secretKey.getBytes("UTF-8");
	byte[] kSigning = HmacSHA(data, kSecret, algorithm);
	signature = Base64.getEncoder().encodeToString(kSigning);
	return signature;
    }
    void setKeyId(String val){
	if(val != null)
	    keyId = val;
    }
    void setAlgorithm(String val){
	if(val != null){
	    if(algos.containsKey(val)){
		algorithm = algos.get(val);
	    }
	}
    }		
    void setKey(String val){
	if(val != null)
	    key = val;
    }		
    void setDigest(String val){
	if(val != null)
	    digest = val;
    }
    void setBody(String val){
	if(val != null)
	    body = val;
    }		
    void setSignature(String val){
	if(val != null)
	    signature = val;
    }
    void setItemsOrder(List<String> val){
	if(val != null)
	    items_order = val;
    }
		
}
