package scrum.attendance_app.security;


import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;


public class TokenStore {

    // This key has been randomly generated, of course this should not be public in real projects! :)
    private final String secretKey = "23778sah9021-12123-12s-as-12a-AS_12xoiJN-SHWQ98";

    private final static TokenStore instance = new TokenStore();

    private TokenStore() {
    }

    public static TokenStore getInstance() {
        return instance;
    }

    public String createToken(Map<String, Object> claims) throws JOSEException {
        Instant issuedAt = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        Instant expiration = issuedAt.plus(24, ChronoUnit.HOURS);

        JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder();
        for(String entry : claims.keySet()){
            builder.claim(entry, claims.get(entry));
        }
        JWTClaimsSet claimsSet = builder.issueTime(Date.from(issuedAt)).expirationTime(Date.from(expiration)).build();
        Payload payload = new Payload(claimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(new JWSHeader(JWSAlgorithm.HS256), payload);
        jwsObject.sign(new MACSigner(secretKey.getBytes()));
        return jwsObject.serialize();
    }

    public boolean verifyToken(String token) throws JOSEException, ParseException {
        try {
            getUser(token);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

//    public String getUser(String token) throws JOSEException, ParseException {
//        SignedJWT signedJWT = SignedJWT.parse(token);
//        JWSVerifier jwsVerifier = new MACVerifier(secretKey.getBytes());
//        if (signedJWT.verify(jwsVerifier)) {
//            if (new Date().before(signedJWT.getJWTClaimsSet().getExpirationTime()) && new Date().after(signedJWT.getJWTClaimsSet().getNotBeforeTime()))
//                return (String) signedJWT.getPayload().toJSONObject().get("email");
//        }
//
//
//        throw new RuntimeException("Invalid token");
//    }

    public String getUser(String token) throws JOSEException, ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWSVerifier jwsVerifier = new MACVerifier(secretKey.getBytes());
        if (signedJWT.verify(jwsVerifier)) {
            return (String) signedJWT.getPayload().toJSONObject().get("email");
        }
        throw new RuntimeException("Invalid token");
    }




    public String getUserRole(String token) throws JOSEException, ParseException {
        try {
            // Decodifica il token JWT
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey.getBytes())  // Usa la stessa chiave segreta usata per firmare il token
                    .parseClaimsJws(token)
                    .getBody();
            // Restituisce il ruolo dell'utente
            return claims.get("role", String.class);
        } catch (Exception e) {
            // Gestisci l'errore (ad esempio, se il token è invalido o scaduto)
            e.printStackTrace();
            return null;
        }
    }



    public String getToken(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(header != null && header.startsWith("Bearer "))
            return header.replace("Bearer ", "");
        return "invalid";
    }

}
