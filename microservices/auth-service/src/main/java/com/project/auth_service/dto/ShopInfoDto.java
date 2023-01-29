package com.project.auth_service.dto;

import lombok.Data;

@Data
public class ShopInfoDto {

    private String name;

    private String mail;

    private String shopId;

    private String successUrl;

    private String cancelUrl;

    private String returnUrl;
}
