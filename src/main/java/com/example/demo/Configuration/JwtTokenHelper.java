package com.example.demo.Configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenHelper {

    //  Minimum 32 characters required
    private final String SECRET = "mygate_secret_key_must_be_32_chars";

    private final Key SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes());

    //  Generate Token
    public String generateToken(UserDetails userDetails) {

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)
                )
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    //  Extract Username
    public String getUsernameFromToken(String token) {

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    //  Validate Token
    public boolean validateToken(String token, UserDetails userDetails) {

        String username = getUsernameFromToken(token);
        return username.equals(userDetails.getUsername());
    }
}
