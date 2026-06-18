package com.openclassrooms.mddapi.security;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String generateToken(String username) {

        return JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date())
                .withExpiresAt(
                        new Date(System.currentTimeMillis() + expiration)
                )
                .sign(
                        Algorithm.HMAC256(secret)
                );
    }

    public String extractUsername(String token) {

        return JWT.require(
                        Algorithm.HMAC256(secret))
                .build()
                .verify(token)
                .getSubject();
    }

    public boolean isValid(String token) {

        try {
            JWT.require(
                            Algorithm.HMAC256(secret))
                    .build()
                    .verify(token);

            return true;

        } catch (Exception e) {
            return false;
        }
    }

}
