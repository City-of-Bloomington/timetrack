package in.bloomington.timer.util;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 */
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.List;

class JwtHandler {

    private final JWKSet publicKeys;

    public JwtHandler(String jwsKeysUri) {
        try {
            publicKeys = JWKSet.load(new URL(jwsKeysUri));
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public JSONObject getPayload(String jws) {
        try {
            List<JWK> keys = publicKeys.getKeys();

            JWSObject jwsObject = JWSObject.parse(jws);
            boolean signatureIsOK = jwsObject.verify(new RSASSAVerifier((RSAKey) keys.get(0)));

            if (!signatureIsOK) {
                System.err.println("Signature in jwk could not be verified.");
            }
						else{
								return new JSONObject(jwsObject.getPayload().toString());
						}
        } catch (ParseException | JOSEException e) {
						System.err.println(" JwtHandler "+e);
            throw new RuntimeException(e);
        }
				return null;
    }
}
