package com.project.auth_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.auth_service.dto.TransactionData;
import com.project.auth_service.dto.Token;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RestController
public class TokenController {

    @PostMapping("/generate-token")
    public Token generateToken(@RequestBody TransactionData transactionSensitiveData) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper(); // or use an existing one
        String json = objectMapper.writeValueAsString(transactionSensitiveData);
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        String base64 = Base64.getUrlEncoder().encodeToString(bytes);
        String jws = Jwts.builder().claim("transactionData",base64).signWith(SignatureAlgorithm.HS256, "secret").compact();
        Token token=new Token();
        token.setContent(jws);
        return token;
    }

    @PostMapping("/decode-token")
    public TransactionData decodeToken(@RequestBody Token token) throws JsonProcessingException {
        String userJsonBase64 = Jwts.parser().setSigningKey("secret").parseClaimsJws(token.getContent()).getBody().get("transactionData", String.class);
        byte [] bytes = Base64.getDecoder().decode(userJsonBase64);
        String json = new String(bytes, StandardCharsets.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper(); // or use an existing one
        TransactionData data = objectMapper.readValue(json, TransactionData.class);
        return data;
    }
}
