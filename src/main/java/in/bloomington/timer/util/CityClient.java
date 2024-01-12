package in.bloomington.timer.util;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 */

import java.net.URI;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.security.Signature;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64.Decoder;
import java.util.Base64.*;
import java.util.Base64;
import org.json.*;
import java.util.ArrayList;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.SecretKey;
import javax.crypto.Mac;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import io.jsonwebtoken.*;
import io.jsonwebtoken.SignatureAlgorithm.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.client.entity.*;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.util.EntityUtils;
import java.util.UUID;
import org.apache.http.impl.client.HttpClients;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.bloomington.timer.bean.*;
import in.bloomington.timer.list.*;

public class CityClient {

    private static CityClient cityClient;
    static Logger logger = LogManager.getLogger(CityClient.class);
    public static CityClient getInstance() {
        if (cityClient == null) {
	    cityClient = new CityClient();
	}
	return cityClient;	
    }
    public Employee endAuthentication(String code, Configuration config) {
	String uri = config.getTokenEndPoint();
	Employee user = null;
	try{
	    CloseableHttpClient client = HttpClients.createDefault();
	    HttpPost httpPost = new HttpPost(uri);
	    httpPost.setHeader("Content-Type","application/x-www-form-urlencoded");
	    String nonce = UUID.randomUUID().toString();
	    // System.err.println(" nonce "+nonce);
	    List<NameValuePair> params = new ArrayList<NameValuePair>();
	    params.add(new BasicNameValuePair("code", code));
						
	    params.add(new BasicNameValuePair("client_id", config.getClientId()));
	    params.add(new BasicNameValuePair("client_secret", config.getClientSecret()));
	    params.add(new BasicNameValuePair("redirect_uri", config.getCallbackUri()));
	    params.add(new BasicNameValuePair("grant_type", "authorization_code"));
	    // params.add(new BasicNameValuePair("nonce", nonce));
	    httpPost.setEntity(new UrlEncodedFormEntity(params));
						
	    CloseableHttpResponse response = client.execute(httpPost);
	    // check if not 200				
	    int resStatus = response.getStatusLine().getStatusCode();
	    // System.err.println(" res status "+resStatus);
	    String jsonString = EntityUtils.toString(response.getEntity());
	    // System.err.println("response json str "+jsonString);
	    JSONObject json = new JSONObject(jsonString);
	    String access_token = json.getString("access_token");
	    String id_token = json.getString("id_token");
	    // System.err.println(" id_token "+id_token);

	    client.close();
	    String[] chunks = id_token.split("\\.");
	    String signature = "";
	    if(chunks.length == 3){
		signature = chunks[2];
	    }
	    String header_chunk = chunks[0];
	    String payLoad_chunk = chunks[1];
	    Base64.Decoder decoder = Base64.getDecoder();  
	    String header = new String(decoder.decode(header_chunk));
	    String payload = new String(decoder.decode(payLoad_chunk));
						
	    // System.err.println(" header "+header);
	    // System.err.println(" payload "+payload);
	    JSONObject jjson = new JSONObject(payload);
	    String username = jjson.getString(config.getUsername());
	    // System.err.println(" username "+username);
	    if(username != null && !username.isEmpty()){
		user = getUser(username);
	    }
	    verify(header_chunk, payLoad_chunk, signature, config);
						

	}catch(Exception ex){
	    System.err.println(" Error "+ex);
	    logger.error(ex);
	}
	return user;
    }
    Employee getUser(String username){

	Employee user = null;
	String message="";
	String username2 = ""; 
	if(username.indexOf("\\") > 0){ // to remove COB\\sibow 
	    username2 = username.substring(username.indexOf("\\")+1);
	}
	else{
	    username2 = username;
	}
	System.err.println(username2);
	try{
	    Employee user2 = new Employee(null, username2);
	    String back = user2.doSelect();
	    if(!back.equals("")){
		message += back; // an error or no user found
		logger.error(back);
	    }
	    else{
		user = user2;
	    }
	}
	catch (Exception ex) {
	    logger.error(ex);
	    message += ex;
	}
	return user;
    }
    //
    // convert client secret to SecretKey
    //
    private SecretKey convertStringToSecretKey(String encodedKey) {
	// System.err.println(" convert string to secret key");
	byte[] decodedKey = Base64.getUrlDecoder().decode(encodedKey);
	SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256"); // AES, DES, HmacSHA256
	return originalKey;
    }
    boolean verify(String header_chunk, String payLoad_chunk,
		   String signature, Configuration config){
	try{
	    Mac mac = Mac.getInstance("HmacSHA256");
	    String key = config.getClientSecret();
	    SecretKey secretKey = convertStringToSecretKey(key);
	    String content = (header_chunk + "." + payLoad_chunk);
	    // System.err.println(" content "+content);
	    byte[] content_bytes = content.getBytes("UTF-8");
	    mac.init(secretKey);
	    byte[] digest = mac.doFinal(content_bytes);
	    String digest_str = Base64.getUrlEncoder().encodeToString(digest);
	    // System.err.println(" sig "+signature);
	    // System.err.println(" sig2 "+digest_str);
	    if(signature.equals(digest_str)){
		return true;
	    }
	}catch(Exception ex){
	    System.err.println(" verify "+ex);
	}
	return false;
    }
						
    boolean verify3(String header_chunk, String payLoad_chunk,
		    String signature, Configuration config){
	try{
	    String key = config.getClientSecret();
	    // for keys with - or _
	    byte [] key_bytes = Base64.getUrlDecoder().decode(key);
	    // for others use the following
	    // byte [] key_bytes = Base64.getDecoder().decode(key);
	    //
	    // X509EncodedKeySpec keySpec = new X509EncodedKeySpec(key_bytes);
	    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(key_bytes);
	    KeyFactory kf = KeyFactory.getInstance("RSA");
	    PrivateKey privKey = kf.generatePrivate(keySpec);
	    // System.out.println(privKey);
				
	    String content = (header_chunk + "." + payLoad_chunk);

	    Signature instance = Signature.getInstance("SHA256withRSA"); 
	    instance.initSign(privKey);
	    instance.update(content.getBytes("UTF-8"));
	    byte[] s = instance.sign();
	    String signature2 = Base64.getEncoder().encodeToString(s);
	    System.err.println("sig 1 "+signature);
	    System.err.println("sig 2 "+signature2);
	    if(signature.equals(signature2)){
		System.err.println(" not valid signature");
		return true;
	    }

	}catch(Exception ex){
	    System.err.println(" verify "+ex);
	}
	return false;
    }

}

