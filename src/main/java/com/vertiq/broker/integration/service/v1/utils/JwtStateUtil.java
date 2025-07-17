package com.vertiq.broker.integration.service.v1.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtStateUtil {

    public static String generateStateJwt(String xUserId, String brokerId, String secret) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", xUserId);
        claims.put("brokerId", brokerId);
        long now = System.currentTimeMillis();
        // 5 min expiry
        Date expiry = new Date(now + 5 * 60 * 1000);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(now))
                .setExpiration(expiry)
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public static Map<String, Object> parseStateJwt(String jwt, String secret) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }
}
