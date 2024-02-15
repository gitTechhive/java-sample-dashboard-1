package com.sampledashboard1.config.security.jwt;

import com.sampledashboard1.config.security.CustomUser;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@Log4j2
public class JwtProvider {

    @Value("${dng.app.jwtSecret}")
    private String jwtSecret;

    @Value("${dng.app.jwtExpiration}")
    private int jwtExpiration;

    //generate token for user
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    //while creating the token -
    //1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
    //2. Sign the JWT using the HS512 algorithm and secret key.
    //3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
    //   compaction of the JWT to a URL-safe string
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateJwtToken(Authentication authentication) {
        CustomUser userPrinciple = (CustomUser) authentication.getPrincipal();

        Claims claims = Jwts.claims().setSubject(userPrinciple.getId().toString());
        claims.put("iat", LocalDateTime.now());

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //creates a spec-compliant secure-random key:
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        log.info("key base: {}", Encoders.BASE64.encode(key.getEncoded()));

        JwtBuilder builder = Jwts.builder().setClaims(claims).setIssuedAt(now)
                .setExpiration(new Date((new Date()).getTime() + jwtExpiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256);

        // if it has been specified, let's add the expiration
        if (jwtExpiration >= 0) {
            long expMillis = nowMillis + jwtExpiration;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        return builder.compact();

    }


    //for retrieving any information from token we will need the secret key
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    //retrieve expiration date from jwt token
    public Date extractExpirationDate(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    //check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = extractExpirationDate(token);
        return expiration.before(new Date());
    }

    public String getUserRoleFromJwtToken(String token) {
        Claims body = extractAllClaims(token);
        Object roleType = body.get("roleType");
        return roleType.toString();
    }

    //validate token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public boolean validateJwtToken(String authToken) {
        try {
            extractUsername(authToken);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token -> Message: {}", e);
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token -> Message: {}", e);
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token -> Message: {}", e);
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty -> Message: {}", e);
        }

        return false;
    }

}
