package com.lobanmating.budget_api.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    // Default 24 hour expiry time in milliseconds for JWT token if property not found
    @Value("${jwt.expiration:86400000}")
    private long EXPIRATION_TIME;

    private Key getSignInKey() {
        // Decode base 64-encoded secret key into bytes
        byte[] keyBytes = Base64.getDecoder().decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // TODO: add search by user id functionality for lightweight searching without making db calls
    public String generateToken(Map<String, Object> extraClaims, CustomUserDetails userDetails) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignInKey())
                .build().parseClaimsJws(token).getBody();
    }

    // Extract token claims and validate it within one method to avoid repeatedly accessing claims
    public Optional<Claims> validateAndGetClaims(String token) {
        try {
            Claims claims = extractClaims(token);
            return Optional.of(claims);
        } catch (JwtException e) {
            return Optional.empty();  // Invalid
        }
    }

    public String extractEmail(Claims claims) {
        return claims.getSubject();
    }

}