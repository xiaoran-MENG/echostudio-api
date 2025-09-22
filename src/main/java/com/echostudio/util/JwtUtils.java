package com.echostudio.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtils {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expiration;

    public String token(UserDetails userDetails, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername()) // User email
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + this.expiration))
                .signWith(signingKey())
                .compact();
    }

    public <T> T claim(String jwtToken, Function<Claims, T> extractor) {
        return extractor.apply(this.claims(jwtToken));
    }

    public Claims claims(String token) {
        return Jwts.parser()
                .verifyWith(this.signingKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean verifyToken(String token, UserDetails userDetails) {
        return this.email(token).equals(userDetails.getUsername()) && !this.tokenExpired(token);
    }

    private boolean tokenExpired(String token) {
        return this.expiration(token).before(new Date());
    }

    private Date expiration(String token) {
        return this.claim(token, Claims::getExpiration);
    }

    public String email(String token) {
        return this.claim(token, Claims::getSubject);
    }

    private SecretKey signingKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
}
