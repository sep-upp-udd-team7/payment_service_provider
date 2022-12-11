package com.project.auth_service.dto;


import lombok.Data;

@Data
public class WebShopUrl {

    private String successUrl;

    private String cancelUrl;

    private String returnUrl;
}
