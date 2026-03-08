package com.example.booking.util;

//package com.example.booking.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final String SECRET = "mysecurejwtsecretkeymysecurejwtsecretkey";

    private Key getSignKey(){
        byte[] keyBytes = SECRET.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token){
        return Jwts.parser()
                .setSigningKey(getSignKey())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String generateToken(String username){

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+86400000))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token, String username){

        String extractedUsername = extractUsername(token);

        return extractedUsername.equals(username)
                && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token){

        Date expiration = Jwts.parser()
                .setSigningKey(getSignKey())
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();

        return expiration.before(new Date());
    }
}
