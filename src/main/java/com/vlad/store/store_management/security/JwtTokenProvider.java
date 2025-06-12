package com.vlad.store.store_management.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration-ms}")
    private long validityInMilliseconds; // ex: 3600000 = 1h

    private Key key;

    @PostConstruct
    protected void init() {
        // Creează cheia secretă din secretKey (string) în format byte[]
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // Generează token JWT pentru un username și o listă de roluri
    public String generateToken(String username, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", roles);  // Adaugă rolurile în token

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)          // când a fost emis
                .setExpiration(validity)   // când expiră
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Extrage username-ul din token
    public String getUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Extrage lista de roluri din token (opțional, dacă ai nevoie)
    public List<String> getRoles(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("roles", List.class);
    }

    // Verifică dacă token-ul este valid (semnătură, expirare)
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Token invalid
            return false;
        }
    }
}
