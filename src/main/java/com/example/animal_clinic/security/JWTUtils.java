package com.example.animal_clinic.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JWTUtils {

    @Value( "${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration-time}")
    private Long lifeTime;

    private SecretKey key;

    public void init (){
        if(secret != null){
            key = Keys.hmacShaKeyFor(secret.getBytes());
        }else {
            throw new IllegalArgumentException("JWT secret is null");
        }
    }

    public String generateToken(String username){
        if(key == null)
            init();

        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + lifeTime))
                .signWith(key)
                .compact();
    }

    public String extractUsername(String token) {
        if(key == null)
            init();

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateToken(String token, UserDetails userDetails){
        if(key == null)
            init();

        try{
            String username = extractUsername(token);
            return username.equals(userDetails.getUsername());
        }
        catch (Exception e){
            return false;
        }

    }
}
