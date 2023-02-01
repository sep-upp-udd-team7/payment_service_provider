package com.project.auth_service.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.JWTVerifier;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

@Service
public class JwtUtil {

    @Value("${jwt.secret}")
    private String SECRET;
    private static final Date EXPIRATION_TIME = new Date(System.currentTimeMillis() + 12000 * 60 * 1000);



    public String createToken(String clientId, String roles) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET.getBytes());
        String token=JWT.create()
                .withSubject("PaymentServiceProvider")
                .withExpiresAt(EXPIRATION_TIME)
                .withClaim("clientId",clientId)
                .withClaim("roles",roles)
                .sign(algorithm);
        return token;
    }

    public DecodedJWT decodeToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET.getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }

}
