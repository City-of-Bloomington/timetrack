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
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.UUID;
import java.util.*;
import org.json.*;

import com.nimbusds.oauth2.sdk.*;
import com.nimbusds.oauth2.sdk.id.*;
import com.nimbusds.oauth2.sdk.token.*;
import com.nimbusds.openid.connect.sdk.Nonce;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import in.bloomington.timer.util.Configuration.*;

public class OidcClient {
    static Logger logger = LogManager.getLogger(OidcClient.class);
    private static OidcClient oidcClient;
    // The authorisation endpoint of the server
    private URI authzEndpoint = null;
    
    // The client identifier provisioned by the server
    private ClientID clientID = null;
    
    // The requested scope values for the token
    private Scope scope = null;
    
    // The client callback URI, typically pre-registered with the server
    private URI callback = null;
    
    // Generate random state string for pairing the response to the request
    private State state = null;
    private Nonce nonce = null;
    // private Nonce nonce = null;
    private URI requestURI = null;
    private JwtHandler jwtHandler = null;
    private Configuration config = null;
    private Map<String, String> map = null;
    public static OidcClient getInstance() {
        if (oidcClient == null) {
            oidcClient = new OidcClient();
	}
	return oidcClient;	
    }
    private OidcClient() {

    }
    private void prepareParams(){
	// The authorisation endpoint of the server
	if(config == null){
	    logger.error("Config not initialized ");
	    return ;
	}
	try{
	    authzEndpoint = new URI(config.getAuthEndPoint());
						
	    // The client identifier provisioned by the server
	    clientID = new ClientID(config.getClientId());
	    
	    // The requested scope values for the token
	    scope = new Scope(config.getScope());
						
	    // The client callback URI, typically pre-registered with the server
	    callback = new URI(config.getCallbackUri());
						
	    // Generate random state string for pairing the response to the request
	    state = new State();
	    nonce = new Nonce();
	    AuthorizationRequest request =
		new AuthorizationRequest.Builder(
						 new ResponseType("code"),
						 clientID)
		.scope(scope)
		.state(state)
		.redirectionURI(callback)
		.endpointURI(authzEndpoint)
		.build();
	    // Use this URI to send the end-user's browser to the server
	    requestURI = request.toURI();
	}
	catch(Exception ex){
	    System.err.println(" Exception "+ex);
	}
    }
    public URI getRequestURI(){
	return requestURI;
    }
    public State getState(){
	return state;
    }
    public Nonce getNonce(){
	return nonce;
    }
    public void setConfig(Configuration val){
	if (val != null){
	    config = val;
	}
	prepareParams();
    }

}

