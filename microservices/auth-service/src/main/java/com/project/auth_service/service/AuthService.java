package com.project.auth_service.service;


import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.project.auth_service.dto.TokenDto;
import com.project.auth_service.dto.TokenValidationResponseDto;
import com.project.auth_service.model.Role;
import com.project.auth_service.model.WebShop;
import com.project.auth_service.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final WebShopService webShopService;
    private final JwtUtil jwtUtil;

    public TokenDto generateToken(String clientId, String clientSecret) {
        WebShop shop = webShopService.getShopById(clientId);
        String roles = "";
        for(Role role : shop.getRoles()){
            roles += role.getName() + " ";
        }
        String token = null;
        if (new BCryptPasswordEncoder().matches(clientSecret, shop.getShopSecret())) {
            token = jwtUtil.createToken(clientId, roles);
        }
        return new TokenDto(token, roles);
    }

    public TokenValidationResponseDto validateToken(String token) {
        try {
            DecodedJWT decodedJWT = jwtUtil.decodeToken(token);
            Claim clientId = decodedJWT.getClaim("clientId");
            WebShop shop = webShopService.getShopById(clientId.asString());
            if (shop == null) {
                throw new Exception("Shop does not exist");
            }
            TokenValidationResponseDto dto = new TokenValidationResponseDto();
            dto.setClientId(shop.getShopId());
            dto.setClientSecret(shop.getShopSecret());
            return dto;
        } catch (Exception e) {
            e.printStackTrace();
            return new TokenValidationResponseDto(null, null);
        }


    }
}
