package com.project.paypal.dto;

import lombok.Data;

@Data
public class PaypalRedirectUrlDto {
    String url;
    String token;
}
