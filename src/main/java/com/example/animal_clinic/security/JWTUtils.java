package com.example.animal_clinic.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JWTUtils {
    private static final Logger logger = LoggerFactory.getLogger(JWTUtils.class);

    @Value( "${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration-time}")
    private Long lifeTime;

    private SecretKey key;

    public void init (){
        if(secret != null){
            logger.info("JWT secret is not null. Initializing key.");
            key = Keys.hmacShaKeyFor(secret.getBytes());
        }else {
            logger.warn("JWT secret is null. Cannot initialize key.");
            throw new IllegalArgumentException("JWT secret is null");
        }
    }

    public String generateToken(String username){
        if(key == null)
            init();

        String token = Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + lifeTime))
                .signWith(key)
                .compact();
        logger.info("Generated token for user {}.", username);
        return token;
    }

    public String extractUsername(String token) {
        if(key == null)
            init();

        try{
            String username = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();

            logger.info("Extracted username {} from token.", username);
            return username;
        }catch (Exception e){
            logger.error("Error extracting username from token.", e);
            throw new IllegalArgumentException("Invalid token");
        }
    }

    public boolean validateToken(String token, UserDetails userDetails){
        if(key == null)
            init();

        try{
            String username = extractUsername(token);
            boolean valid = username.equals(userDetails.getUsername());

            if (valid){
                logger.info("Token for user {} is valid.", username);
            }else {
                logger.warn("Token for user {} is invalid.", username);
            }
            return valid;
        }
        catch (Exception e){
            logger.error("Error validating token.", e);
            return false;
        }

    }
}
