package in.bloomington.timer.service;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Base64;
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
    String keyId = "";
    String request_target = "";
    String created = "", expires="";
    String host = "", username="";
    String date = "";
    String content_type="";
    String digest = "", body="";
    String content_length="";
    String signature = "", new_signature="";
    String sign_str = "";
    List<String> items_order = null;
    //
    // Signature
    // Signature: KeyId="mykeyid",algorithm="hs2019",created=1402170695,
    //          headers="(request-target) (created) host digest content-length",
    //          signature="Base64(RSA-SHA512(signing string))"
    //
    // the singing string will be
    // (request-target): post /foo
    // (created): 1402170695
    // host: example.org
    // digest: SHA-256=X48E9qOokqqrvdts8nOJRJN3OWDUoyWxBf7kbu9DBPE=
    // content-length: 18
    //
    ServiceSigner(String _key,
		  String _request_taget,
		  String _created,
		  String _expires,
		  String _username,
		  String _host,
		  String _body,
		  String _content_type,
		  String _content_length,
		  String _signature,
		  List<String> items){
	setKey(_key);
	setRequestTarget(_request_taget);
	setCreated(_created);
	setExpires(_expires);
	setUsername(_username);
	setHost(_host);
	setBody(_body);
	setContentLength(_content_length);
	setContentType(_content_type);
	setSignature(_signature);
	setItemsOrder(items);
	//
	try{
	    if(items_order.contains("digest")){
		//
		// checksum the digest
		//
		if(body != null && !body.equals("")){
		    digest = "SHA-256="+sign(key, body);
		    System.err.println(" digest "+digest);
		}
	    }
	    composeSigningString();
	    System.err.println(" str to sign "+sign_str);
	    new_signature = sign(key, sign_str);
	    System.err.println(" new sign "+new_signature);				
	}catch(Exception ex){
	    System.err.println(ex);				
	}
    }
    ServiceSigner(){
	try{
	    System.err.println(" sign key ");
	    String sig = ""; // sign(key, timestamp, serviceName);
	    System.err.println(" sig = "+sig);
	}catch(Exception ex){
	    System.err.println(ex);
	}
    }
    /**
     * if the two signatures match, return true;
     */
    boolean verify(){
	return !signature.equals("") &&
	    !new_signature.equals("") &&
	    signature.equals(new_signature);
    }		
    void composeSigningString(){
	sign_str = "";
	for(String str:items_order){
	    if(str.equals("(request-target)")){
		addToString("(request-target)", request_target);
	    }
	    else if(str.equals("(created)")){
		addToString("(created)", created);
	    }
	    else if(str.equals("(expires)")){
		addToString("(expires)", expires);
	    }						
	    else if(str.equals("host")){
		addToString("host", host);
	    }
	    else if(str.equals("username")){
		addToString("username", username);
	    }	    
	    else if(str.equals("digest")){
		addToString("digest", digest);
	    }
	    else if(str.equals("content-type")){
		addToString("content-type,", content_type);
	    }
	    else if(str.equals("content-length")){
		addToString("content-length", content_length);
	    }						
	}
    }
    void addToString(String name, String value){
	if(!sign_str.equals("")) sign_str+=" ";
	sign_str += name += ":";
	if(value != null && !value.equals("")){
	    sign_str += " "+value;
	}
    }
    static byte[] HmacSHA256(String data, byte[] key){
	byte[] ret = null;
	try{
	    String algorithm="HmacSHA512";
	    Mac mac = Mac.getInstance(algorithm);
	    mac.init(new SecretKeySpec(key, algorithm));
	    ret =  mac.doFinal(data.getBytes("UTF-8"));
	}catch(Exception ex){
	    System.err.println(ex);
	}
	return ret;
    }
    static String sign(String key, String data) throws Exception {
	String signature = "";
	byte[] kSecret = key.getBytes("UTF-8");
	byte[] kSigning = HmacSHA256(data, kSecret);
	signature = toHexString(kSigning);				
	return signature;
    }		
    private static String toHexString(byte[] bytes) {
	String str = Base64.getEncoder().encodeToString(bytes);
	return str;
    }
    void setKeyId(String val){
	if(val != null)
	    keyId = val;
    }
    void setUsername(String val){
	if(val != null)
	    username = val;
    }    
    void setKey(String val){
	if(val != null)
	    key = val;
    }		
    void setRequestTarget(String val){
	if(val != null)
	    request_target = val;
    }
    void setCreated(String val){
	if(val != null)
	    created = val;
    }
    void setExpires(String val){
	if(val != null)
	    expires = val;
    }
    void setHost(String val){
	if(val != null)
	    host = val;
    }
    void setDigest(String val){
	if(val != null)
	    digest = val;
    }
    void setBody(String val){
	if(val != null)
	    body = val;
    }		
    void setContentLength(String val){
	if(val != null)
	    content_length = val;
    }
    void setContentType(String val){
	if(val != null)
	    content_type = val;
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
