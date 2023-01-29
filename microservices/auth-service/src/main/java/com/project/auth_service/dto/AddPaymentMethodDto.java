package com.project.auth_service.dto;

import lombok.Data;

@Data
public class AddPaymentMethodDto {

    String shopId;

    Long paymentMethodId;

    String paymentApiClientId;

    String paymentApiSecret;
}
