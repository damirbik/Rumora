package org.lamdateam.rumora_demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret:I4fl9s0xhq3D9Bkmg37cQjzSV4WSxv87CFDSxI/CAbdGtTd6GcSIFDDqYfUQl7r0mdINXg/VPqfVoruZlTpt8g==}")
    private String jwtSecret;

    @Value("${jwt.expiration:86400000}") // 24 часа
    private int jwtExpiration;

    public String generateToken(String username, String roleName) {
        System.out.println("Generating token for: " + username + " with role: " + roleName);

        Date expiryDate = new Date(System.currentTimeMillis() + jwtExpiration);

        return Jwts.builder()
                .setSubject(username)
                .claim("role", roleName)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            System.out.println("Token validation failed: " + e.getMessage());
            return false;
        }
    }
}