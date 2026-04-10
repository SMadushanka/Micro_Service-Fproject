package com.agms.sensor_service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HexFormat;

@Component
public class JwtUtil {

    private final SecretKey secretKey;

    public JwtUtil(@Value("${jwt.secret}") String hexSecret) {
        byte[] keyBytes = HexFormat.of().parseHex(hexSecret);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String validateAndGetSubject(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    public String generateToken(String subject, long ttlMillis) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .subject(subject)
                .issuedAt(new Date(now))
                .expiration(new Date(now + ttlMillis))
                .signWith(secretKey)
                .compact();
    }
}
