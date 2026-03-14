package com.agms.api_gateway.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HexFormat;

/**
 * Temporary utility to generate a test JWT token.
 * Run main() directly — remove this class before production deployment.
 */
public class TokenGenerator {

    public static void main(String[] args) {
        String secret = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
        SecretKey key = Keys.hmacShaKeyFor(HexFormat.of().parseHex(secret));

        String token = Jwts.builder()
                .subject("test-user")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000)) // 24h
                .signWith(key)
                .compact();

        System.out.println("=== TEST JWT TOKEN ===");
        System.out.println(token);
        System.out.println("======================");
        System.out.println("Use as: Authorization: Bearer " + token);
    }
}
