package com.project.auth_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.auth_service.dto.AuthDto;
import com.project.auth_service.dto.TokenValidationResponseDto;
import com.project.auth_service.dto.TransactionData;
import com.project.auth_service.dto.TokenDto;
import com.project.auth_service.service.AuthService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RestController
@RequiredArgsConstructor
public class TokenController {

    private final AuthService authService;
    @PostMapping("/generate-url-token")
    public TokenDto generateUrlToken(@RequestBody TransactionData transactionSensitiveData) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper(); // or use an existing one
        String json = objectMapper.writeValueAsString(transactionSensitiveData);
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        String base64 = Base64.getUrlEncoder().encodeToString(bytes);
        String jws = Jwts.builder().claim("transactionData",base64).signWith(SignatureAlgorithm.HS256, "secret").compact();
        TokenDto token=new TokenDto();
        token.setToken(jws);
        return token;
    }

    @PostMapping("/decode-url-token")
    public TransactionData decodeUrlToken(@RequestBody TokenDto token) throws JsonProcessingException {
        String userJsonBase64 = Jwts.parser().setSigningKey("secret").parseClaimsJws(token.getToken()).getBody().get("transactionData", String.class);
        byte [] bytes = Base64.getDecoder().decode(userJsonBase64);
        String json = new String(bytes, StandardCharsets.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper(); // or use an existing one
        TransactionData data = objectMapper.readValue(json, TransactionData.class);
        return data;
    }

    @PostMapping("/generate-jwt")
    public ResponseEntity<TokenDto> generateJwtToken(@RequestBody AuthDto dto){
        String token= authService.generateToken(dto.getClientId(),dto.getClientSecret());
        if (token!=null){
            return new ResponseEntity<>(new TokenDto(token), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(new TokenDto(null),HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/validate-jwt")
    public ResponseEntity<TokenValidationResponseDto> validateJwt(@RequestBody TokenDto dto){
        TokenValidationResponseDto response=authService.validateToken(dto.getToken());
        if (response.getClientId()==null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
