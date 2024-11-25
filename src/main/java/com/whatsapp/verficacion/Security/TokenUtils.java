package com.whatsapp.verficacion.Security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.lang.Collections;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;

public class TokenUtils {

    private final static String ACCESS_TOKEN_SECRET = "dd2z01OwXMGBSWO63Uw11sTD4rv5ZVzZM7A41HQH76uug7I6K1tKNBxwieNesrvV";
    private final static Integer ACCCESS_TOKEN_VALIDITY_SECONDS = 30;

    public static String createToken(String nombre, String email) {
        Integer expirationTime = ACCCESS_TOKEN_VALIDITY_SECONDS * 1000;
        Date expirationDate = new Date(System.currentTimeMillis() + expirationTime);

        Map<String, Object> extra = new HashMap<>();
        extra.put("nombre", nombre);

        return Jwts.builder()
                .claims(extra)
                .subject(email)
                .expiration(expirationDate)
                .signWith(Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET.getBytes()))
                .compact();
    }

    public static UsernamePasswordAuthenticationToken getAuthentication(String token) {
        try {
            SecretKey secretKey = Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET.getBytes());
            JwtParser parser = Jwts.parser().verifyWith(secretKey).build();
            Claims claims = parser.parseSignedClaims(token).getPayload();
            String email = claims.getSubject();
            return new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());
        } catch (JwtException e) {
            return null;
        }
    }
}