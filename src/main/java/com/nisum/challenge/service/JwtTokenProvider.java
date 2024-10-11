package com.nisum.challenge.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    public String createToken(String email) {
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        Claims claims = Jwts.claims().setSubject(email).build();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + (3600000L * 24 * 365 * 10)))
                .signWith(key)
                .compact();
    }
}

