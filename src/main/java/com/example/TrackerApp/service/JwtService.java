package com.example.TrackerApp.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
@Service
public class JwtService {
    @Value("${secretKey}")
    private String secretKey;
    private Key key;
    @PostConstruct
    public void initKey(){
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String generateToken(String username){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 60000 * 15))
                .signWith(key)
                .compact();
    }
    public Claims extractClaims(String token){
        return Jwts.parserBuilder().setSigningKey(key)
                .build().parseClaimsJws(token).getBody();
    }
    public String extractUsername(String token){
        return extractClaims(token).getSubject();
    }
    public boolean isTokenExpired(String token){
        return extractClaims(token).getExpiration().before(new Date());
    }
    public boolean isTokenValid(String token, String logUsername){
        try {
            String tokenUsername = extractUsername(token);
            return (logUsername.equals(tokenUsername) && !isTokenExpired(token));
        } catch (Exception e) {
            return false;
        }
    }
}
