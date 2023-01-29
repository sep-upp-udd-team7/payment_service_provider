package com.project.auth_service.dto;

import lombok.Data;

@Data
public class SubscribeWebShopToPaymentMethodRequest {
    String shopId;

    String paymentApiClientId;

    String paymentApiSecret;
}
