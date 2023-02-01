package com.project.auth_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class TokenDto {
    private String token;
    private String roles;

    public TokenDto(String token) {
        this.token = token;
    }
}
