package com.gui.payment_oauth_resourceServer.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class JwtTokenUtil {
    @Value("${jwt.secret}")
    private String SECRET_KEY;
    private final long EXPIRATION_TIME = 3600000;

    @PostConstruct
    public void init() {
        System.out.println("JWT Secret: " + SECRET_KEY);
    }
    public String generateJwtToken(String email, String name, List<String> roles) {
        return Jwts.builder()
                .setSubject(email)
                .claim("roles", roles)
                .claim("email", email)
                .claim("name", name)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }
    private Key getSigningKey() {
        byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    public Claims parseJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    public String extractUserEmail(String token) {
        return parseJwtToken(token).getSubject();
    }

    public String extractName(String token){
        return parseJwtToken(token).get("name", String.class);
    }


    public List<String> extractRoles(String token) {
        Claims claims = parseJwtToken(token);
        return claims.get("roles", List.class);
    }


    public boolean isTokenExpired(String token) {
        Date expiration = parseJwtToken(token).getExpiration();
        return expiration.before(new Date());
    }
    public Map<String, Object> getHeaders(String token) throws SignatureException {
        try {
            String[] splitToken = token.split("\\.");
            String headerBase64 = splitToken[0];
            byte[] decodedHeader = java.util.Base64.getUrlDecoder().decode(headerBase64);
            String headerJson = new String(decodedHeader);

            ObjectMapper objectMapper = new ObjectMapper();

            return objectMapper.readValue(headerJson, Map.class);
        } catch (Exception e) {
            throw new SignatureException("Error decoding the JWT header", e);
        }
    }


    public boolean isTokenValid(String token) {
        try {
            parseJwtToken(token);
            return !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
