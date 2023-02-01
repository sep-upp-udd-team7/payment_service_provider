package com.project.auth_service.dto;

import lombok.Data;

@Data
public class RegisterShopResponse {
    private String shopId;

    private String shopSecret;

    private Boolean using2FA;
    private String secret;
    private String qrCode;
}
