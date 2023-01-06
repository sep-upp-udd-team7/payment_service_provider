package com.project.auth_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class TokenValidationResponseDto {

    private String clientId;
    private String clientSecret;
}
