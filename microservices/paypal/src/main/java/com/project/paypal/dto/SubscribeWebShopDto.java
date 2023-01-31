package com.project.paypal.dto;

import lombok.Data;

@Data
public class SubscribeWebShopDto {
    String shopId;

    String paymentApiClientId;

    String paymentApiSecret;
}
