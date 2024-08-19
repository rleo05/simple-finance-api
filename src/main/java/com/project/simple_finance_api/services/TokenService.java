package com.project.simple_finance_api.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.project.simple_finance_api.entities.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
    @Value("${jwt.token.secret.key}")
    private String secretKey;


    public String generateToken(User user){
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        return JWT.create()
                .withIssuer("finance-api")
                .withSubject(user.getEmail())
                .withExpiresAt(getExpirationInstant())
                .sign(algorithm);
    }

    public String validateToken(String token){
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        return JWT.require(algorithm)
                .withIssuer("finance-api")
                .build()
                .verify(token)
                .getSubject();
    }

    private Instant getExpirationInstant(){
        return LocalDateTime.now().plusMinutes(5).toInstant(ZoneOffset.of("-03:00"));
    }
}
