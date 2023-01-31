package com.project.auth_service.dto;

import lombok.Data;

@Data
public class RegisterShopDto {

    private String name;

    private String mail;

    private String password;

    private String successUrl;

    private String cancelUrl;

    private String returnUrl;

    private Boolean using2FA;

    private String secret;

    private String qrCode;
}
